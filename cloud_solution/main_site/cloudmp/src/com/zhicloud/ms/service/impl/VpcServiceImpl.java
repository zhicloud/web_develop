
package com.zhicloud.ms.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhicloud.ms.app.pool.CloudHostPoolManager;
import com.zhicloud.ms.app.pool.addressPool.AddressExt;
import com.zhicloud.ms.app.pool.addressPool.AddressPool;
import com.zhicloud.ms.app.pool.addressPool.AddressPoolManager;
import com.zhicloud.ms.app.pool.network.NetworkInfoExt;
import com.zhicloud.ms.app.pool.network.NetworkInfoExt.Host;
import com.zhicloud.ms.common.util.RandomPassword;
import com.zhicloud.ms.exception.AppException;
import com.zhicloud.ms.httpGateway.HttpGatewayChannelExt;
import com.zhicloud.ms.httpGateway.HttpGatewayManager;
import com.zhicloud.ms.httpGateway.HttpGatewayResponseHelper;
import com.zhicloud.ms.mapper.CloudHostMapper;
import com.zhicloud.ms.mapper.SysDiskImageMapper;
import com.zhicloud.ms.mapper.VpcBaseInfoMapper;
import com.zhicloud.ms.mapper.VpcBindHostMapper;
import com.zhicloud.ms.mapper.VpcBindPortMapper;
import com.zhicloud.ms.mapper.VpcOuterIpMapper;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.INetworkService;
import com.zhicloud.ms.service.IVpcService;
import com.zhicloud.ms.transform.util.TransFormLoginHelper;
import com.zhicloud.ms.transform.util.TransFormLoginInfo;
import com.zhicloud.ms.util.CapacityUtil;
import com.zhicloud.ms.util.DateUtil;
import com.zhicloud.ms.util.FlowUtil;
import com.zhicloud.ms.util.StringUtil;
import com.zhicloud.ms.vo.CloudHostVO;
import com.zhicloud.ms.vo.SysDiskImageVO;
import com.zhicloud.ms.vo.VpcBaseInfoVO;
import com.zhicloud.ms.vo.VpcBindPortVO;
import com.zhicloud.ms.vo.VpcOuterIpVO;
 
@Transactional(readOnly=true)
@Service("vpcService")
public class VpcServiceImpl implements IVpcService {
    
    private static final Logger logger = Logger.getLogger(VpcServiceImpl.class); 
    @Resource
	private SqlSession sqlSession;
    
	@Override
	public List<VpcBaseInfoVO> getAllVpc() {
		logger.debug("VpcServiceImpl.getAllVpc()");
		VpcBaseInfoMapper vpcBaseInfoMapper = this.sqlSession.getMapper(VpcBaseInfoMapper.class);
		List<VpcBaseInfoVO> vpcList = vpcBaseInfoMapper.getAll();
		return vpcList;
	}

	@Override
	@Transactional(readOnly=false)
	public MethodResult addVpc(String displayName, String ipAmount,String description, String[] hosts,HttpServletRequest request) {
		logger.debug("VpcServiceImpl.addVpc()");
		if(StringUtil.isBlank(displayName)){
			return new MethodResult(MethodResult.FAIL, "请输入专属云显示名");
		}
		if(StringUtil.isBlank(ipAmount)){
			return new MethodResult(MethodResult.FAIL, "请输入专IP数量");
		}
		try{
			String addressPoolId = "";
			AddressPool ap = AddressPoolManager.singleton().getPool("1");
			List<AddressExt> addressList = ap.getAll();
			for(AddressExt address : addressList){
				if(address.getName().equals("default")){
					addressPoolId = address.getUuid();
					break;
				}
			}
			INetworkService service = new NetworkServiceImpl();
			//定义vpcID
			String id = StringUtil.generateUUID();
			NetworkInfoExt result = service.createSync(new Integer(1), id, 27, "test", addressPoolId);
			if (result == null) {
				logger.info("fail to create network.");
				return new MethodResult(MethodResult.FAIL, "创建失败");
			} else {
				logger.info(String.format("success to create network. uuid[%s], network_address[%s]", result.getUuid(), result.getHostNetworkAddress()));
			}
			String realVpcId = result.getUuid();
			//创建成功回填数据库
			VpcBaseInfoMapper vpcBaseInfoMapper = this.sqlSession.getMapper(VpcBaseInfoMapper.class); 
			CloudHostMapper chMapper = this.sqlSession.getMapper(CloudHostMapper.class); 
			VpcBindHostMapper vpcBindHostMapper = this.sqlSession.getMapper(VpcBindHostMapper.class); 
			SysDiskImageMapper sysDiskImageMapper = this.sqlSession.getMapper(SysDiskImageMapper.class); 
			VpcOuterIpMapper vpcOuterIpMapper = this.sqlSession.getMapper(VpcOuterIpMapper.class); 


			TransFormLoginInfo loginInfo = TransFormLoginHelper.getLoginInfo(request);
			
			Map<String, Object> data = new LinkedHashMap<String, Object>();
			data.put("id",       id);
			data.put("realVpcId",  realVpcId); //
			data.put("name",  id);
			data.put("displayName",  displayName);
 			data.put("description",  description);
 			data.put("status",  1);// 创建成功
 			data.put("hostAmount",  0);
 			data.put("ipAmount",  0);
 			data.put("userId",  loginInfo.getBillid());
 			data.put("createTime",  StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
			int n = vpcBaseInfoMapper.addVpc(data);
			//申请ip 
			result = service.attachAddressSync(new Integer(1), realVpcId, Integer.parseInt(ipAmount));
			
			for(int i = 0;i<Integer.parseInt(ipAmount);i++){					
				data.put("id", StringUtil.generateUUID());
				data.put("vpcId", id);
				data.put("ip", result.getIp()[i]);
 				data.put("createTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
				vpcOuterIpMapper.add(data);				
			}
			//更新IP个数
			data.put("id", id);
			data.put("amount", Integer.parseInt(ipAmount));
			vpcBaseInfoMapper.updateVpcIpAmount(data); 
			logger.info("begin to create host for vpc");
			if(n > 0){
				String computePoolId = "";
				HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
				if(channel!=null){
					JSONObject poolResult = channel.computePoolQuery();
					JSONArray computerList = poolResult.getJSONArray("compute_pools");
					if(computerList.size()>0){
						JSONObject computerObject = computerList.getJSONObject(0);
						computePoolId = computerObject.getString("uuid");
					}else{
						return new MethodResult(MethodResult.FAIL,"无法获取计算资源池");
					}
				}	
				//新创建云主机
				if(hosts!=null && hosts.length>0){
					for(int i=0;i<hosts.length;i++){
						String[] info = hosts[i].split("/");
						String cpuCore = info[0];
						String memory = info[1];
						String dataDisk = info[2];
						String bandwidth = info[3];
						String sysImageId = info[4];
						String sysImageName = sysDiskImageMapper.getById(sysImageId).getName();
						//定义主机ID
						String cloudHostId = StringUtil.generateUUID();
						Map<String, Object> cloudHostData = new LinkedHashMap<String, Object>();
						cloudHostData.put("id",              cloudHostId);                                                              
						cloudHostData.put("realHostId",      null);                                                                     
						cloudHostData.put("type",            3); //1：云桌面主机 2：云服务器 3：专属云                           
						cloudHostData.put("userId",          loginInfo.getBillid());                                                                   
						cloudHostData.put("hostName",        cloudHostId);                                                                     
						cloudHostData.put("displayName",     "vpc_"+loginInfo.getUsercount()+RandomPassword.getRandomPwd(16));                                                                     
						cloudHostData.put("account","vpc");
						cloudHostData.put("password",RandomPassword.getRandomPwd(16));
						cloudHostData.put("cpuCore",Integer.parseInt(cpuCore));
						cloudHostData.put("memory",CapacityUtil.fromCapacityLabel(memory+"GB"));
						cloudHostData.put("status",0);
						cloudHostData.put("sysImageId",sysImageId);
						cloudHostData.put("sysDisk",CapacityUtil.fromCapacityLabel("10GB"));
						cloudHostData.put("dataDisk",CapacityUtil.fromCapacityLabel(dataDisk+"GB"));
						cloudHostData.put("bandwidth",FlowUtil.fromFlowLabel(bandwidth+"Mbps"));
						cloudHostData.put("sysImageName",sysImageName);
						cloudHostData.put("poolId",computePoolId);
						chMapper.insertCloudHost(cloudHostData);
						
						//新增主机和VPC的关系
						data.put("id", StringUtil.generateUUID());
						data.put("vpcId", id);
						data.put("hostId", cloudHostId);
						data.put("createTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
						vpcBindHostMapper.add(data);
					}
					//更新主机个数
					data.put("id", id);
					data.put("amount", hosts.length);
					vpcBaseInfoMapper.updateVpcHostAmount(data);
				}
				
				return new MethodResult(MethodResult.SUCCESS, "添加成功");
			}else{
				return new MethodResult(MethodResult.FAIL, "添加失败");				
			}
		}catch(Exception e){
			e.printStackTrace();
			return new MethodResult(MethodResult.FAIL, "添加失败");
		}
		
	}
	
	/**
	 * 删除VPC
	 */
	@Transactional(readOnly = false,noRollbackFor={Exception.class})
	@Override
	public MethodResult deleteVpc(String id) {

		logger.debug("VpcServiceImpl.deleteVpc()");
		try
		{
		    MethodResult result =  new MethodResult(MethodResult.SUCCESS, "删除成功");
            result.setProperty("name", ""); 
            
			if( StringUtil.isBlank(id) )
			{
			     result.status = MethodResult.FAIL;
			     result.message = "删除失败";
			     return result;
			} 
			 
			VpcBaseInfoMapper vpcBaseInfoMapper = this.sqlSession.getMapper(VpcBaseInfoMapper.class); 
			VpcBindHostMapper vpcBindHostMapper = this.sqlSession.getMapper(VpcBindHostMapper.class); 
			CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class); 
			VpcBaseInfoVO vpcBaseInfo = vpcBaseInfoMapper.queryVpcById(id);
			if( vpcBaseInfo == null){
			    result.status = MethodResult.FAIL;
                result.message = "找不到专属云信息";
                return result;		
			}
			result.setProperty("name", vpcBaseInfo.getDisplayName());
			Map<String,Object> cloudHostData = new HashMap<String,Object>();
			cloudHostData.put("vpcId", id);
			List<CloudHostVO> cloudHostList = cloudHostMapper.getAllHostByVpcId(cloudHostData);
			List<String> hostLists = new ArrayList<>();
			if(cloudHostList!=null && cloudHostList.size()>0){
				for(CloudHostVO cloudHost : cloudHostList){
					if(!StringUtil.isBlank(cloudHost.getRealHostId())){
						HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(new Integer(1));
						JSONObject hostDeleteResult = channel.hostDelete(cloudHost.getRealHostId());
						if (HttpGatewayResponseHelper.isSuccess(hostDeleteResult)) {
							logger.info("CloudHostServiceImpl.deleteCloudHostById() > [" + Thread.currentThread().getId() + "] delete host '" + cloudHost.getHostName() + "' succeeded, uuid:[" + cloudHost.getRealHostId() + "], message:[" + HttpGatewayResponseHelper.getMessage(hostDeleteResult) + "]");
							hostLists.add(cloudHost.getId());
						} else {
							logger.warn("CloudHostServiceImpl.deleteCloudHostById() > [" + Thread.currentThread().getId() + "] delete host '" + cloudHost.getHostName() + "' failed, uuid:[" + cloudHost.getRealHostId() + "], message:[" + HttpGatewayResponseHelper.getMessage(hostDeleteResult) + "]");
						}
						
						// 从缓冲池删除
						CloudHostPoolManager.getCloudHostPool().removeByRealHostId(cloudHost.getRealHostId());
						CloudHostPoolManager.getCloudHostPool().removeByHostName(new Integer(1), cloudHost.getHostName());
					}else {
						hostLists.add(cloudHost.getId());
					}
				}
			}
			/*
			 删除主机
			 */
			cloudHostMapper.deleteById(id);
			/**
			 * 删除主机与vpc的关联
			 */
			Map<String,Object> bindHostData = new HashMap<String,Object>();
			bindHostData.put("hostIds",hostLists);
			bindHostData.put("removeTime",DateUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
			int m = vpcBindHostMapper.deleteLogical(bindHostData);
			//最后删除那个啥
			List<String> idList = new ArrayList<String>();
			idList.add(id);
			int n = vpcBaseInfoMapper.logicDeleteVpcByIds(idList);
			if(n > 0 && m>0){
  			    return result;
			}else{
			    result.status = MethodResult.FAIL;
                result.message = "删除失败";
                return result;
				
			}
		}
		catch( Exception e )
		{
			logger.error(e);
			return new MethodResult(MethodResult.FAIL, "删除失败");
		}
	}
	
	/**
	 * 停用VPC
	 */
 	@Transactional(readOnly = false)
 	@Override
	public MethodResult disableVpc(String vpcId) {
     	MethodResult mr =  new MethodResult(MethodResult.FAIL, "停用失败");
        mr.setProperty("name", ""); 
		VpcBaseInfoMapper vpcBaseInfoMapper = this.sqlSession.getMapper(VpcBaseInfoMapper.class); 
		VpcBaseInfoVO vo = vpcBaseInfoMapper.queryVpcById(vpcId);
		if(vo == null ){
		    mr.message = "未找到专属云信息";
		    return mr;
 		}
		if(StringUtil.isBlank(vo.getRealVpcId())){
 			mr.message = "专属云未创建成功";
            return mr;
		}
		mr.setProperty("name", vo.getName());
		INetworkService service = new NetworkServiceImpl();
		try {
			boolean result = service.stopSync(new Integer(1), vo.getRealVpcId());
			if(result){
				Map<String, Object> data = new LinkedHashMap<String, Object>();
				data.put("id",              vo.getId());                                                              
				data.put("modifyTime",      StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));         
				data.put("status",      2);         
				vpcBaseInfoMapper.updateVpc(data);
				mr.status = MethodResult.SUCCESS;
				mr.message = "停用成功";
				return mr;	
			}else{
				return mr;						
			}
		} catch (Exception e) {
			e.printStackTrace();
			return mr;
			
		}
 	}
 	
 	/**
	 * 恢复VPC
	 */
	@Transactional(readOnly = false)
	@Override
	public MethodResult ableVpc(String vpcId) {
	    MethodResult mr =  new MethodResult(MethodResult.FAIL, "恢复失败");
        mr.setProperty("name", ""); 
		VpcBaseInfoMapper vpcBaseInfoMapper = this.sqlSession.getMapper(VpcBaseInfoMapper.class); 
		VpcBaseInfoVO vo = vpcBaseInfoMapper.queryVpcById(vpcId);
 		if(vo == null ){
 		   mr.message = "未找到专属云信息";
           return mr;
		}
		if(StringUtil.isBlank(vo.getRealVpcId())){
		    mr.message = "专属云未创建成功";
            return mr;	
		}
		
		mr.setProperty("name", vo.getName());
		INetworkService service = new NetworkServiceImpl();
		try {
			boolean result = service.startAsync(new Integer(1), vo.getRealVpcId());
			if(result){
				Map<String, Object> data = new LinkedHashMap<String, Object>();
				data.put("id",              vo.getId());                                                              
				data.put("modifyTime",      StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));         
				data.put("status",      1);         
				vpcBaseInfoMapper.updateVpc(data);				
				mr.status = MethodResult.SUCCESS;
                mr.message = "停用成功";
                return mr;
			}else{
				return mr;							
			}
		} catch (Exception e) {
			e.printStackTrace();
			return mr;
		}
	}
	
	@Override
	public List<VpcOuterIpVO> getAllIpByVpcId(String vpcId) {
		Map<String,Object> condition = new HashMap<String, Object>();
		condition.put("vpcId", vpcId);
		VpcOuterIpMapper vpcOuterIpMapper = this.sqlSession.getMapper(VpcOuterIpMapper.class); 
		List<VpcOuterIpVO> ipList = vpcOuterIpMapper.getAllIpByVpcId(condition);
		return ipList;
	}
	
	@Transactional(readOnly = false)
	@Override
	public MethodResult addIpCount(String ipCount,String vpcId) {
	    MethodResult mr =  new MethodResult(MethodResult.FAIL, "恢复失败");
        mr.setProperty("name", ""); 
		if(ipCount==null){
		    mr.message = "数量不能为空";
		    return mr;
 		}
		
		Integer count = Integer.parseInt(ipCount);
		VpcOuterIpMapper vpcOuterIpMapper = this.sqlSession.getMapper(VpcOuterIpMapper.class);
		VpcBaseInfoMapper vpcBaseInfoMapper = this.sqlSession.getMapper(VpcBaseInfoMapper.class); 
 		VpcBaseInfoVO vpc =  vpcBaseInfoMapper.queryVpcById(vpcId);
		mr.setProperty("name", vpc.getName());
 		INetworkService service = new NetworkServiceImpl();
		NetworkInfoExt result;
		try {
			result = service.attachAddressSync(new Integer(1), vpc.getRealVpcId(), count);
			if(result == null ){		
			    mr.message = "添加失败";
	            return mr;
 			}
			// ip表添加记录 
			for(int i=0;i<count;i++){
				Map<String,Object> condition = new HashMap<String, Object>();
				condition.put("id",StringUtil.generateUUID());
				condition.put("vpcId", vpcId);
				condition.put("createTime", DateUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
				condition.put("ip", result.getIp()[i]);
				vpcOuterIpMapper.add(condition);
			}
			//vpc表更新ip数量
			Map<String,Object> data = new HashMap<String, Object>();
			data.put("id", vpcId);
			data.put("amount", ipCount);
			data.put("modifyTime", DateUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
			vpcBaseInfoMapper.updateVpcIpAmount(data);
			mr.message = "添加成功";
			mr.status = MethodResult.SUCCESS;
            return mr;
 		} catch (Exception e) {
			e.printStackTrace();
			return mr;
		}
	}
    
	@Transactional(readOnly = false)
	@Override
	public MethodResult deleteIps(String vpcId,List<String> ids,List<String> ipValues) {
	    MethodResult mr =  new MethodResult(MethodResult.FAIL, "删除失败");
        mr.setProperty("name", "");
		try{
			if(ids == null || ids.size()<1){
			    mr.message = "请选择需要删除的IP";
			    return mr;
 			}
			VpcOuterIpMapper vpcOuterIpMapper = this.sqlSession.getMapper(VpcOuterIpMapper.class);
			VpcBindPortMapper vpcBindPortMapper = this.sqlSession.getMapper(VpcBindPortMapper.class);
			VpcBaseInfoMapper vpcBaseInfoMapper = this.sqlSession.getMapper(VpcBaseInfoMapper.class);
			CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
			VpcBaseInfoVO vpc = vpcBaseInfoMapper.queryVpcById(vpcId);
		    mr.setProperty("name", vpc.getName());
			INetworkService service = new NetworkServiceImpl();
			String [] ipToDelete = new String[ipValues.size()] ;
			for(int i = 0;i<ipValues.size();i++){
				ipToDelete[i] = ipValues.get(i);
			}
			NetworkInfoExt result = service.detachAddressSync(new Integer(1), vpc.getRealVpcId(), ipToDelete);
			if(result == null ){	
                 return mr;
 			}
			
			vpcOuterIpMapper.deleteIps(ids);
			List<CloudHostVO> cloudHostList = cloudHostMapper.getCloudHostInVpc(vpcId);
			for(String ip : ipValues){
				vpcBindPortMapper.deleteByOuterIp(ip);
				// 如果有主机绑定了该ip，重新绑定
				for(CloudHostVO host : cloudHostList){
					if(ip.equals(host.getOuterIp())){
						this.defaultBindPort(vpcId, host);
					}
				}
			}
			Map<String,Object> data = new HashMap<String, Object>();
			data.put("id", vpcId);
			data.put("amount", -ids.size());
			data.put("modifyTime", DateUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
			vpcBaseInfoMapper.updateVpcIpAmount(data);
		}catch(Exception e){ 
			e.printStackTrace();
			throw new AppException("删除失败"); 
		}
		mr.status = MethodResult.SUCCESS;
		mr.message = "删除成功";
		return mr;
 	}
	/**
	* @Description:创建vpc以后随机绑定默认端口
	* @param vpcid
	* @param wwip 外网IP
	*/
	public void defaultBindPort(String vpcid, CloudHostVO cloudHost) {
	     Map<String, Object> condition = new HashMap<String, Object>();
	     condition.put("vpcId", vpcid); 
	     try {
	         // 根据vpcid取得已经分配的外网端口和主机端口
	         VpcBindPortMapper vpcBindPortMapper = this.sqlSession.getMapper(VpcBindPortMapper.class);
	         CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
	         VpcOuterIpMapper vpcOuterIpMapper = this.sqlSession.getMapper(VpcOuterIpMapper.class);
	         
	         VpcBaseInfoMapper vpcBaseInfoMapper = this.sqlSession.getMapper(VpcBaseInfoMapper.class);
	         VpcBaseInfoVO vpc = vpcBaseInfoMapper.queryVpcById(vpcid);
	         List<VpcBindPortVO> portList = vpcBindPortMapper.queryByVpcId(condition);
	         // 外网端口集合
	         Set<Integer> outerPorts = new HashSet<Integer>();
	         // 主机端口集合
	         Set<Integer> hostports = new HashSet<Integer>();
	         for (VpcBindPortVO vpcBindPort : portList) {
	             outerPorts.add(vpcBindPort.getOuterPort());
	             hostports.add(vpcBindPort.getHostPort());
	         } 
	         Integer outerPort_spice = getVpcPort(outerPorts);
	         outerPorts.add(outerPort_spice);
	         // 外网IP从主机信息里面取第一个值
	         String  wwip = vpcOuterIpMapper.getAllIpByVpcId(condition).get(0).getIp();
	         // 绑定
	          
	         INetworkService service = new NetworkServiceImpl();
	         String[] protocolList = new String[] {"0"};
	         String[] ipList = new String[] { wwip };
	         String[] portListNew = new String[] { outerPort_spice+""};
	         String[] hostList = new String[] { cloudHost.getRealHostId()};
	         String[] hostPortList = new String[] { cloudHost.getInnerPort()+""};

	         NetworkInfoExt network = service.bindPortSync(new Integer(1), vpc.getRealVpcId(), protocolList, ipList, portListNew, hostList, hostPortList);
	         if(network == null){
	             return ;              
	         } 
	         
	         Map<String,Object> data = new HashMap<String, Object>();
	         data.put("id",StringUtil.generateUUID());
	         data.put("vpc_id",vpcid);
	         data.put("host_id",cloudHost.getId());
	         data.put("outer_ip",wwip);
	         data.put("outer_port",outerPort_spice);
	         data.put("host_port",cloudHost.getInnerPort());
	         data.put("protocol",0);
	         data.put("flag", 1);
	         data.put("createTime",DateUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
	         int result = vpcBindPortMapper.add(data);
	         if(result <= 0){
	             return;
	         }
	         
	         // 绑定成功以后 将ip和端口更新到云主机
	         Map<String, Object> newcondition = new HashMap<String, Object>();
	         newcondition.put("id", cloudHost.getId());
	         newcondition.put("realHostId", cloudHost.getRealHostId());
	         newcondition.put("outerIp", wwip);
	         newcondition.put("outerPort", outerPort_spice);
	         cloudHostMapper.updateRealHostIdById(newcondition); 
	         
	         //添加3389端口
	         if(StringUtil.isBlank(cloudHost.getSysImageName())){
	        	 SysDiskImageMapper sysDiskImageMapper = this.sqlSession.getMapper(SysDiskImageMapper.class);
	        	 SysDiskImageVO image = sysDiskImageMapper.getById(cloudHost.getSysImageId());
	        	 if(image != null){
	        		 cloudHost.setSysImageName(image.getName());
	        	 }
	         }
	         if(cloudHost.getSysImageName()!=null && cloudHost.getSysImageName().contains("indows")){
	        	 
	        	 Integer outerPort_3389 = getVpcPort(outerPorts);
	        	 
	        	 protocolList = new String[] {"0"};
	             ipList = new String[] { wwip };
	             portListNew = new String[] { outerPort_3389+""};
	             hostList = new String[] { cloudHost.getRealHostId()};
	             hostPortList = new String[] { "3389"};

	             network = service.bindPortSync(new Integer(1), vpc.getRealVpcId(), protocolList, ipList, portListNew, hostList, hostPortList);
	             if(network == null){
	                 return ;              
	             } 
	             
	              data.put("id",StringUtil.generateUUID());
	             data.put("vpc_id",vpcid);
	             data.put("host_id",cloudHost.getId());
	             data.put("outer_ip",wwip);
	             data.put("outer_port",outerPort_3389);
	             data.put("host_port",3389);
	             data.put("protocol",0);
	             data.put("flag", 0);
	             data.put("createTime",DateUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
	             vpcBindPortMapper.add(data);
	        	 
	         }
	         //开通22
	         else if(cloudHost.getSysImageName()!=null && (cloudHost.getSysImageName().contains("entos")||cloudHost.getSysImageName().contains("ubuntu"))){
	        	 
	             Integer outerPort_22 = getVpcPort(outerPorts);
	        	 
	        	 protocolList = new String[] {"0"};
	             ipList = new String[] { wwip };
	             portListNew = new String[] { outerPort_22+""};
	             hostList = new String[] { cloudHost.getRealHostId()};
	             hostPortList = new String[] { "22"};

	             network = service.bindPortSync(new Integer(1), vpc.getRealVpcId(), protocolList, ipList, portListNew, hostList, hostPortList);
	             if(network == null){
	                 return ;              
	             } 
	             
	              data.put("id",StringUtil.generateUUID());
	             data.put("vpc_id",vpcid);
	             data.put("host_id",cloudHost.getId());
	             data.put("outer_ip",wwip);
	             data.put("outer_port",outerPort_22);
	             data.put("host_port",22);
	             data.put("protocol",0);
	             data.put("flag", 0);
	             data.put("createTime",DateUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
	             vpcBindPortMapper.add(data);
	        	 
	         }
	           
	     } catch (Exception e) {
	         logger.error(e);
	         throw new AppException(e);
	     } 
	 }
	
	/**
	 * @Description:获取外网IP可用端口，随机取一个(1-65535)
	 * @param ports 已使用的端口号
	 * @return 可用端口号
	*/
	public Integer getVpcPort(Set<Integer> ports) {
	     Random random = new Random();
	     Integer re = random.nextInt(65535);
	     if (re != 0 && (ports.size() == 0 || !ports.contains(re))) {
	         return re;
	     } else {
	         getVpcPort(ports);
	     }
	     return 0;
	 }
	
	@Override
	public List<VpcBindPortVO> getAllBindPort(String vpcId) {
		Map<String,Object> condition = new HashMap<String, Object>();
		condition.put("vpcId", vpcId);
		VpcBindPortMapper vpcBindPortMapper = this.sqlSession.getMapper(VpcBindPortMapper.class); 
		List<VpcBindPortVO> vpcBindPortList = vpcBindPortMapper.queryByVpcId(condition);		
		return vpcBindPortList;
	}
	
	@Transactional(readOnly = false)
	@Override
	public MethodResult addBindPortItem(String vpcId, String outerIp,String outerPort, String hostId, String port, String protocol) {
	    MethodResult mr =  new MethodResult(MethodResult.FAIL, "添加失败");
        mr.setProperty("name", "");
		try{
			if(StringUtil.isBlank(vpcId)){
			    mr.message = "vpcId不能为空";
			    return mr;
 			}
			if(StringUtil.isBlank(outerIp)){
			    mr.message = "外网IP不能为空";
                return mr;
 			}
			if(StringUtil.isBlank(outerPort)){
			    mr.message = "外网端口不能为空";
                return mr;
 			}
			if(StringUtil.isBlank(hostId)){
			    mr.message = "主机ID不能为空";
                return mr;
 			}
			if(StringUtil.isBlank(port)){
			    mr.message = "主机端口不能为空";
                return mr;
 			}
			if(StringUtil.isBlank(protocol)){
			    mr.message = "端口类型不能为空";
                return mr;
 			}
			VpcBaseInfoMapper vpcBaseInfoMapper = this.sqlSession.getMapper(VpcBaseInfoMapper.class);
			VpcBindPortMapper vpcBindPortMapper = this.sqlSession.getMapper(VpcBindPortMapper.class);
			CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
			CloudHostVO host = cloudHostMapper.getById(hostId);
			VpcBaseInfoVO vpc = vpcBaseInfoMapper.queryVpcById(vpcId);
			mr.setProperty("name", vpc.getName());
			INetworkService service = new NetworkServiceImpl();
			String[] protocolList = new String[] {protocol};
			String[] ipList = new String[] { outerIp };
			String[] portList = new String[] { outerPort };
			String[] hostList = new String[] { host.getRealHostId() };
			String[] hostPortList = new String[] { port };
			//验证是否已绑定
			Map<String,Object> checkMapOne = new HashMap<String, Object>();
			checkMapOne.put("outerIp", outerIp);
			checkMapOne.put("outerPort", outerPort);
			checkMapOne.put("protocol", protocol);
			Map<String,Object> checkMapTwo = new HashMap<String, Object>();
			checkMapTwo.put("hostId", hostId);
			checkMapTwo.put("hostPort", port);
			checkMapTwo.put("protocol", protocol);
			VpcBindPortVO vpcBindPortOne = vpcBindPortMapper.checkIpAndPortAndProtocol(checkMapOne);
			VpcBindPortVO vpcBindPortTwo = vpcBindPortMapper.checkHostAndPortAndProtocol(checkMapTwo);
			if(vpcBindPortOne!=null){
			    mr.message = "IP"+outerIp+"的"+outerPort+"端口已绑定";
                return mr;
 			}
			if(vpcBindPortTwo!=null){
			    mr.message = "主机"+vpcBindPortTwo.getDisplayName()+"的"+port+"端口已绑定";
                return mr;
 			}
			
			NetworkInfoExt network = service.bindPortSync(new Integer(1), vpc.getRealVpcId(), protocolList, ipList, portList, hostList, hostPortList);
 			if(network == null){
 			    return mr;
  			}
			Map<String,Object> condition = new HashMap<String, Object>();
			condition.put("id",StringUtil.generateUUID());
			condition.put("vpc_id",vpcId);
			condition.put("host_id",hostId);
			condition.put("outer_ip",outerIp);
			condition.put("outer_port",Integer.parseInt(outerPort));
			condition.put("host_port",Integer.parseInt(port));
			condition.put("protocol",Integer.parseInt(protocol));
			condition.put("flag",0);
			condition.put("createTime",DateUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
			int result = vpcBindPortMapper.add(condition);
			if(result <= 0){
				return mr;
			}
		}catch(Exception e){
			e.printStackTrace();
			return mr;
 		}
		mr.status = MethodResult.SUCCESS;
		mr.message = "添加成功";
		return mr;
	}
	
	@Transactional(readOnly = false)
	@Override
	public MethodResult deleteBindPorts(String vpcId,List<String> ids) {
	    MethodResult mr =  new MethodResult(MethodResult.FAIL, "添加失败");
        mr.setProperty("name", "");
		try{
			if(ids == null || ids.size()<1){
			    mr.message = "请选择需要删除的项";
			    return mr;
 			}
			if(StringUtil.isBlank(vpcId)){
			    mr.message = "vpcId不能为空";
                return mr;
 			}
			VpcBindPortMapper vpcBindPortMapper = this.sqlSession.getMapper(VpcBindPortMapper.class);
			VpcBaseInfoMapper vpcBaseInfoMapper = this.sqlSession.getMapper(VpcBaseInfoMapper.class);
			VpcBaseInfoVO baseInfo = vpcBaseInfoMapper.queryVpcById(vpcId);
			mr.setProperty("name", baseInfo.getName());
			String[] protocolList = new String[ids.size()];
			String[] ipList = new String[ids.size()];
			String[] portList = new String[ids.size()];
			for(int i=0;i<ids.size();i++){
				VpcBindPortVO bindPort = vpcBindPortMapper.queryById(ids.get(i));
				protocolList[i] = bindPort.getProtocol().toString();
				ipList[i] = bindPort.getOuterIp();
				portList[i] = bindPort.getOuterPort().toString();
			}
			INetworkService service = new NetworkServiceImpl();
			NetworkInfoExt result = service.unbindPortSync(new Integer(1), baseInfo.getRealVpcId(), protocolList, ipList, portList);
			if(result!=null){
				vpcBindPortMapper.deleteByIds(ids);
			}else{
			    return mr; 
			}
		}catch(Exception e){
			e.printStackTrace();
			return mr;
		}
		mr.status = MethodResult.SUCCESS;
		mr.message = "删除成功";
		return mr;
	}
	
	@Transactional(readOnly = false)
	@Override
	public List<CloudHostVO> toVpcHostList(String vpcId) {
			try {
	 		CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
			VpcBaseInfoMapper vpcBaseInfoMapper = this.sqlSession.getMapper(VpcBaseInfoMapper.class);
	 		VpcBaseInfoVO vpcVo = vpcBaseInfoMapper.queryVpcById(vpcId);
	 		
	 		List<CloudHostVO> myCloudHostList = cloudHostMapper.getCloudHostInVpc(vpcId);
			List<CloudHostVO> myNewCloudHostList = new ArrayList<CloudHostVO>();
			INetworkService service = new NetworkServiceImpl();
	
	 		for(CloudHostVO cloudHost : myCloudHostList){
 				//如果没有vpcIP，重新获取
 				if(StringUtil.isBlank(cloudHost.getVpcIp())){
 					NetworkInfoExt network = service.queryHostSync(new Integer(1), vpcVo.getRealVpcId());
 					Host[] hosts = null;
 					if(network != null){				
 						hosts = network.getHostList();
 						logger.info(network); 
 						for (Host host : hosts) {
 							if(host.getUuid().equals(cloudHost.getRealHostId())){
 								cloudHost.setVpcIp(host.getNetworkAddress());
 								Map<String, Object> newIP = new LinkedHashMap<String, Object>();
 								newIP.put("vpcIp", host.getNetworkAddress());
 								newIP.put("id", cloudHost.getId());
 								cloudHostMapper.updateVpcIpById(newIP);
 							}
 						}
 					}
	 				myNewCloudHostList.add(cloudHost);
	 			}
	 		}
	 		return myNewCloudHostList;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	@Transactional(readOnly = false)
	@Override
	public MethodResult addNewHostToVpcBaseInfo(String vpcId,String cpuCore,String memory,String dataDisk,String bandwidth,String sysImageId,String hostAmount) {
	    MethodResult mr =  new MethodResult(MethodResult.FAIL, "添加失败");
	    mr.setProperty("name", "");
	    try
		{
			if(StringUtil.isBlank(vpcId)){			
			    mr.message = "错误VPC";
				return mr;
			}
			VpcBaseInfoMapper vpcBaseInfoMapper = this.sqlSession.getMapper(VpcBaseInfoMapper.class);
			VpcBaseInfoVO vpc = vpcBaseInfoMapper.queryVpcById(vpcId);
			mr.setProperty("name", vpc.getName());
			if(vpc == null ){
			    mr.message = "未找到VPC信息";
                return mr; 
			}
			if(StringUtil.isBlank(hostAmount)){	
			    mr.message = "请输入主机个数";
                return mr; 
 			} 
 			
 			CloudHostMapper cloudHostMapper                                     = this.sqlSession.getMapper(CloudHostMapper.class);
 			SysDiskImageMapper sysDiskImageMapper               = this.sqlSession.getMapper(SysDiskImageMapper.class); 
			VpcBindHostMapper vpcBindHostMapper = this.sqlSession.getMapper(VpcBindHostMapper.class); 

			if(cpuCore==null||cpuCore.equals("")){
			    mr.message = "请选择CPU";
                return mr;
 			} 
			if(memory==null||memory.equals("")){
	            mr.message = "请选择内存";
	            return mr;				
			} 
			if(dataDisk==null||dataDisk.equals("")){
			    mr.message = "请选择硬盘";
                return mr;				
			} 
			if(bandwidth==null||bandwidth.equals("")){
			    mr.message = "请选择带宽";
                return mr;
 			} 
			BigInteger sysDisk  = CapacityUtil.fromCapacityLabel("10GB"); 
			//获取资源池ID
			String computePoolId = "";
			HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
			if(channel!=null){
				JSONObject poolResult = channel.computePoolQuery();
				JSONArray computerList = poolResult.getJSONArray("compute_pools");
				if(computerList.size()>0){
					JSONObject computerObject = computerList.getJSONObject(0);
					computePoolId = computerObject.getString("uuid");
				}else{
				    mr.message = "无法获取计算资源池";
	                return mr;
 				}
			}
			for(int i =0 ;i<Integer.parseInt(hostAmount);i++){
				String hostId = StringUtil.generateUUID();
				String hostName = hostId;
				
				SysDiskImageVO sysDiskIamge= sysDiskImageMapper.getById(sysImageId);
				String sysImageName = (sysDiskIamge == null ? null : sysDiskIamge.getName());
				// 新增云主机信息，创建不需要添加计费记录，到云主机创建成功之后再去创建
				Map<String, Object> cloudHostData = new LinkedHashMap<String, Object>();
				cloudHostData.put("id",              hostId);                                                              
				cloudHostData.put("realHostId",      null);                                                                     
				cloudHostData.put("type",            3);                            
				cloudHostData.put("userId",          vpc.getUserId());                                                                   
				cloudHostData.put("hostName",        hostName);                                                                     
				cloudHostData.put("displayName",     "vpc_"+vpc.getUserId()+RandomPassword.getRandomPwd(16));                                                                     
				cloudHostData.put("account",         "vpc");                                                   
				cloudHostData.put("password",        RandomPassword.getRandomPwd(16));                                                   
				cloudHostData.put("cpuCore",         cpuCore);                                                                  
				cloudHostData.put("memory",          CapacityUtil.fromCapacityLabel(memory + "GB"));                                                                   
				cloudHostData.put("sysImageId",      sysImageId);                                                               
				cloudHostData.put("sysImageName",    sysImageName);                                                               
				cloudHostData.put("sysDisk",         sysDisk);                                                                  
				cloudHostData.put("dataDisk",        CapacityUtil.fromCapacityLabel(dataDisk + "GB"));                                                                 
				cloudHostData.put("bandwidth",       FlowUtil.fromFlowLabel(bandwidth+"Mbps"));                                                                
				cloudHostData.put("status",          0);  
				cloudHostData.put("poolId",computePoolId);
				cloudHostMapper.insertCloudHost(cloudHostData);
				
				//新增主机和VPC的关系
				Map<String, Object> data = new LinkedHashMap<String, Object>();
				data.put("id", StringUtil.generateUUID());
				data.put("vpcId", vpcId);
				data.put("hostId", hostId);
				data.put("createTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
				vpcBindHostMapper.add(data);
				
			}
			
			//更新主机个数
			Map<String, Object> data = new LinkedHashMap<String, Object>();
			data.put("id", vpcId);
			data.put("amount", hostAmount);
			vpcBaseInfoMapper.updateVpcHostAmount(data);
			mr.message = "新增成功";
			mr.status = MethodResult.SUCCESS;
            return mr;
 			
		}
		catch( Exception e )
		{
			logger.error(e);
			return mr; 
		}
	}
	
	@Transactional(readOnly = false)
	@Override
	public MethodResult unboundByHostId(String hostId) {
	    MethodResult mr =  new MethodResult(MethodResult.FAIL, "添加失败");
        mr.setProperty("name", "");
		VpcBaseInfoMapper vpcBaseInfoMapper = this.sqlSession.getMapper(VpcBaseInfoMapper.class);
		VpcBindHostMapper vpcBindHostMapper = this.sqlSession.getMapper(VpcBindHostMapper.class);
		VpcBindPortMapper vpcBindPortMapper = this.sqlSession.getMapper(VpcBindPortMapper.class);
		CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
		
		if(StringUtil.isBlank(hostId)){
		    mr.message = "请选择需要解除绑定的云主机";
			return mr;
		}
		VpcBaseInfoVO vpcBaseInfoVO = vpcBaseInfoMapper.queryVpcByHostId(hostId);
		if(vpcBaseInfoVO == null){
		    mr.message = "未查询到VPC信息";
            return mr;
 		}
		List<CloudHostVO> hostList = cloudHostMapper.getCloudHostInVpc(vpcBaseInfoVO.getId());
		if(hostList.size()  == 2){
		    mr.message = "最少应该保留两个主机";
            return mr;
 		}
		CloudHostVO cloudHost = cloudHostMapper.getById(hostId);
		// 结算产生新的订单
		try {
			//暂时先用主机删除的接口，后面换成下面的detach-host接口
			HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(cloudHost.getRegion());
			JSONObject hostDeleteResult = channel.hostDelete(cloudHost.getRealHostId());
			if (HttpGatewayResponseHelper.isSuccess(hostDeleteResult)) {			
				Map<String, Object> data = new LinkedHashMap<String, Object>();
				data.put("id", vpcBaseInfoVO.getId());
				data.put("amount", -1);
				vpcBaseInfoMapper.updateVpcHostAmount(data);
				
				Map<String,Object> condition = new HashMap<String, Object>();
				condition.put("hostIds",new String[]{hostId});
				condition.put("removeTime",DateUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
				vpcBindHostMapper.deleteLogical(condition);
				
				// 逻辑删除
				String[] strArray={hostId};
				cloudHostMapper.deleteById(hostId);
				
				//删除端口绑定
				vpcBindPortMapper.deleteByHostIds(strArray);
				mr.message = "解除绑定成功";
				mr.status = MethodResult.SUCCESS;
	            return mr; 
			}else{
			    mr.message = "解除绑定失败";
				return mr;
				
			}
		} catch (Exception e) {			
 			e.printStackTrace();
 			return new MethodResult(MethodResult.FAIL,"解除绑定失败");
		}
 	}
	
	@Transactional(readOnly=false,noRollbackFor={Exception.class})
	@Override
	public MethodResult deleteVpcByIds(List<String> ids) {
	    MethodResult mr =  new MethodResult(MethodResult.FAIL, "添加失败");
        mr.setProperty("name", "");
		try{
			VpcBaseInfoMapper vpcBaseInfoMapper = this.sqlSession.getMapper(VpcBaseInfoMapper.class);
			VpcBindHostMapper vpcBindHostMapper = this.sqlSession.getMapper(VpcBindHostMapper.class);
			CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
			if(ids == null || ids.size()<1){
			    mr.message = "请选择需要删除的VPC";
				return mr;
			}
			//获取所有hostId
			List<String> hostIds = new ArrayList<String>();
			//后台删除云主机
			for(String id : ids){
				Map<String,Object> cloudHostData = new HashMap<String,Object>();
				cloudHostData.put("vpcId", id);
				//根据vpcid查询所有的云主机
				List<CloudHostVO> cloudHostList = cloudHostMapper.getAllHostByVpcId(cloudHostData);
				for(CloudHostVO cloudHost : cloudHostList){ 
					// 从http gateway删除
					if(!StringUtil.isBlank(cloudHost.getRealHostId())){
						HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(new Integer(1));
						JSONObject hostDeleteResult = channel.hostDelete(cloudHost.getRealHostId());
						if (HttpGatewayResponseHelper.isSuccess(hostDeleteResult)) {
							logger.info("vpcServiceImpl.deleteCloudHostById() > [" + Thread.currentThread().getId() + "] delete host '" + cloudHost.getHostName() + "' succeeded, uuid:[" + cloudHost.getRealHostId() + "], message:[" + HttpGatewayResponseHelper.getMessage(hostDeleteResult) + "]");
							hostIds.add(cloudHost.getId());
						} else {
							logger.warn("vpcServiceImpl.deleteCloudHostById() > [" + Thread.currentThread().getId() + "] delete host '" + cloudHost.getHostName() + "' failed, uuid:[" + cloudHost.getRealHostId() + "], message:[" + HttpGatewayResponseHelper.getMessage(hostDeleteResult) + "]");
						}
						
						// 从缓冲池删除
						CloudHostPoolManager.getCloudHostPool().removeByRealHostId(cloudHost.getRealHostId());
						CloudHostPoolManager.getCloudHostPool().removeByHostName(cloudHost.getRegion(), cloudHost.getHostName());
					}else{
						hostIds.add(cloudHost.getId());
					}
				}
			}
			//逻辑删除vpc主机
			for(String hostId : hostIds){
				cloudHostMapper.deleteById(hostId);
			}
			//后台同步删除VPC
			INetworkService service = new NetworkServiceImpl();
			
			List<String> curVpcIds = new ArrayList<>();
			for(String id : ids){
				VpcBaseInfoVO vpc = vpcBaseInfoMapper.queryVpcById(id);
				if(vpc!=null && vpc.getRealVpcId()!=null){
					String realId = vpc.getRealVpcId();
					boolean result = service.deleteSync(new Integer(1), realId);
					if(result){
						curVpcIds.add(id);
					}
				}
			}
			int m = 0;
			// 逻辑删除vpc与主机的关联
			if(hostIds.size()>0){
				Map<String,Object> condition = new HashMap<String,Object>();
				condition.put("hostIds",hostIds);
				condition.put("removeTime",DateUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
				m = vpcBindHostMapper.deleteLogical(condition);
			}
			if(curVpcIds.size()>0){
				vpcBaseInfoMapper.logicDeleteVpcByIds(curVpcIds);
			}
			if(m>0){
			    mr.message = "删除成功";
			    mr.status = MethodResult.SUCCESS;
				return mr;
			}
			return mr;
		}catch(Exception e){ 
			e.printStackTrace();
			return mr; 
		}
	}
}

 

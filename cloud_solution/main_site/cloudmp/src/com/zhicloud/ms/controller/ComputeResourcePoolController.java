package com.zhicloud.ms.controller;

import com.zhicloud.ms.app.pool.storage.StorageManager;
import com.zhicloud.ms.app.pool.storage.StorageResult;
import com.zhicloud.ms.httpGateway.HttpGatewayAsyncChannel;
import com.zhicloud.ms.httpGateway.HttpGatewayChannelExt;
import com.zhicloud.ms.httpGateway.HttpGatewayManager;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.ICloudHostService;
import com.zhicloud.ms.service.ICloudHostWarehouseService;
import com.zhicloud.ms.service.IOperLogService;
import com.zhicloud.ms.transform.constant.TransFormPrivilegeConstant;
import com.zhicloud.ms.transform.util.TransFormPrivilegeUtil;
import com.zhicloud.ms.util.StringUtil;
import com.zhicloud.ms.vo.CloudHostVO;
import com.zhicloud.ms.app.pool.computePool.ComputeInfoExt;
import com.zhicloud.ms.app.pool.computePool.ComputeInfo;
import com.zhicloud.ms.vo.MountDiskVo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
/**
 * 
* @ClassName: ComputeResourcePoolController 
* @Description: 资源池-计算资源池管理功能
* @author A18ccms a18ccms_gmail_com 
* @date 2015年6月25日 上午10:17:26 
*
 */
@Controller
@RequestMapping("/computeresourcepool")
public class ComputeResourcePoolController {

    public static final Logger logger = Logger.getLogger(ComputeResourcePoolController.class);


    @Resource
    ICloudHostService cloudHostService;
    @Resource
    private ICloudHostWarehouseService  CloudHostWarehouseService;
    @Resource
    private IOperLogService operLogService;   
	/**
	 * 
	* @Title: getAll 
	* @Description: 查询所有计算资源池 
	* @param @param model
	* @param @param request
	* @param @return      
	* @return String     
	* @throws
	 */
	@RequestMapping(value="/all",method=RequestMethod.GET)
	public String getAll(Model model,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.compute_resource_pool_query)){
			return "not_have_access";
		}
		try {
			List<ComputeInfo> cList = new ArrayList<>();
				HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
				if(channel!=null){
					JSONObject result = channel.computePoolQuery();
            if ("fail".equals(result.getString("status"))) {
                logger.error("ComputeResourcePoolController.getAll>>>获取资源池失败");
            	return "resourcepool/all_compute_pool_manage";
//                return "not_responsed";
            }
					JSONArray computerList = result.getJSONArray("compute_pools");
					for (int i = 0; i < computerList.size(); i ++) {
						JSONObject computerObject = computerList.getJSONObject(i);
						String uuid = computerObject.getString("uuid");
						String name = computerObject.getString("name");
						int status = computerObject.getInt("status");
						Integer cpuCount = computerObject.getInt("cpu_count");
						BigDecimal cpuUsage = new BigDecimal(computerObject.getString("cpu_usage"));
						BigDecimal memoryUsage = new BigDecimal(computerObject.getString("memory_usage"));
						BigDecimal diskUsage = new BigDecimal(computerObject.getString("disk_usage"));
						
						JSONArray memoryList = computerObject.getJSONArray("memory");
						BigInteger[] mcount = new BigInteger[memoryList.size()];
						for(int j=0;j<memoryList.size();j++){
							mcount[j] = new BigInteger(memoryList.getString(j));
						}
						
						JSONArray diskList = computerObject.getJSONArray("disk_volume");
						BigInteger[] dcount = new BigInteger[diskList.size()];
						for(int j=0;j<diskList.size();j++){
							dcount[j] = new BigInteger(diskList.getString(j));
						}
						
						JSONArray nList = computerObject.getJSONArray("node");
						Integer[] ncount = new Integer[nList.size()];
						for(int j=0;j<nList.size();j++){
							ncount[j] = nList.getInt(j);
						}
						
						JSONArray hList = computerObject.getJSONArray("host");
						Integer[] hcount = new Integer[hList.size()];
						for(int j=0;j<hList.size();j++){
							hcount[j] = hList.getInt(j);
						}
						ComputeInfo computer = new ComputeInfo();
						computer.setCpuCount(cpuCount);
						computer.setCpuUsage(cpuUsage);
						computer.setDiskUsage(diskUsage);
						computer.setDiskVolume(dcount);
						computer.setHost(hcount);
						computer.setMemory(mcount);
						computer.setMemoryUsage(memoryUsage);
						computer.setName(name);
						computer.setNode(ncount);
						computer.setStatus(status);
						computer.setUuid(uuid);
						computer.setRegion(1);
						cList.add(computer);
					}
				}
			
			model.addAttribute("computerPool", cList);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "resourcepool/all_compute_pool_manage";
	}
	/**
	 * 
	* @Title: resourceDetail 
	* @Description:  查询计算资源池的nc节点
	* @param @param uuid
	* @param @param model
	* @param @param request
	* @param @return      
	* @return String     
	* @throws
	 */
	@RequestMapping(value="/{uuid}/nc",method=RequestMethod.GET)
	public String resourceDetail(@PathVariable("uuid") String uuid,Model model,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.compute_resource_pool_node_query)){
			return "not_have_access";
		}
		String searchName = request.getParameter("sn");
		try {
			String flag = "no";
			List<ComputeInfoExt> cList = new ArrayList<>();
			List<ComputeInfoExt> curList = new ArrayList<>();
				HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
				if(channel!=null){
					JSONObject result = channel.computePoolQueryResource(uuid);
            if ("fail".equals(result.getString("status"))) {
                logger.error("ComputeResourcePoolController.resourceDetail>>>获取资源池失败");
                return "not_responsed";
            }
					JSONArray computerList = result.getJSONArray("compute_resources");
					for (int i = 0; i < computerList.size(); i ++) {
						JSONObject computerObject = computerList.getJSONObject(i);
						String name = computerObject.getString("name");
						String ip = computerObject.getString("ip");
						int status = computerObject.getInt("status");
						Integer cpuCount = computerObject.getInt("cpu_count");
						BigDecimal cpuUsage = new BigDecimal(computerObject.getString("cpu_usage"));
						BigDecimal memoryUsage = new BigDecimal(computerObject.getString("memory_usage"));
						BigDecimal diskUsage = new BigDecimal(computerObject.getString("disk_usage"));
						
						JSONArray memoryList = computerObject.getJSONArray("memory");
						BigInteger[] mcount = new BigInteger[memoryList.size()];
						for(int j=0;j<memoryList.size();j++){
							mcount[j] = new BigInteger(memoryList.getString(j));
						}
						
						JSONArray diskList = computerObject.getJSONArray("disk_volume");
						BigInteger[] dcount = new BigInteger[diskList.size()];
						for(int j=0;j<diskList.size();j++){
							dcount[j] = new BigInteger(diskList.getString(j));
						}
						
						ComputeInfoExt computer = new ComputeInfoExt();
						computer.setCpuCount(cpuCount);
						computer.setCpuUsage(cpuUsage);
						computer.setDiskUsage(diskUsage);
						computer.setDiskVolume(dcount);
						computer.setMemory(mcount);
						computer.setMemoryUsage(memoryUsage);
						computer.setName(name);
						computer.setStatus(status);
						computer.setIp(ip);
						cList.add(computer);
					}
					if(searchName!=null && searchName!="" && cList.size()>0){
						for(ComputeInfoExt cp : cList){
							if(cp.getName()!=null && cp.getName().toLowerCase().contains(searchName.toLowerCase())){
								curList.add(cp);
							}
						}
					}else{
						curList = cList;
					}
				}
				JSONObject remainNode = channel.computePoolQueryResource("");
				JSONArray remainList = remainNode.getJSONArray("compute_resources");
				if(remainList.size()>0) {
					flag = "yes";
				}
				model.addAttribute("remain",flag);				
			model.addAttribute("computerPoolDetail", curList);
			model.addAttribute("poolId", uuid);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "resourcepool/resource_pool_manage_nc";
	}
	
	/**
	 * 创建资源(节点)页面
	 * @param uuid
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/{uuid}/an",method=RequestMethod.GET)
	public String addNodePage(@PathVariable("uuid") String uuid,Model model){
		List<ComputeInfoExt> curList = new ArrayList<>();
		HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
		try {
			if(channel!=null){
				JSONObject result = channel.computePoolQueryResource("");
				JSONArray computerList = result.getJSONArray("compute_resources");
				for (int i = 0; i < computerList.size(); i ++) {
					JSONObject computerObject = computerList.getJSONObject(i);
					String name = computerObject.getString("name");
					ComputeInfoExt computer = new ComputeInfoExt();
					computer.setName(name);
					curList.add(computer);
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		model.addAttribute("nodeList", curList);
		model.addAttribute("poolId", uuid);
		return "resourcepool/resource_node_add";
	}
	
	/**
	 * 删除资源(节点)
	 * @param name
	 * @param ip
	 * @param poolId
	 * @return
	 */
	@RequestMapping(value="/dn",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult deleteNode(@RequestParam("name") String name,
			@RequestParam("ip") String ip,
			@RequestParam("poolId") String poolId,
			HttpServletRequest request){
	    if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.server_resource_node_remove)){
	        return new MethodResult(MethodResult.FAIL,"您没有删除资源的权限，请联系管理员");
	    }
		boolean flag = true;
		try {
				HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
				if(channel!=null){
					JSONObject result = channel.hostQuery(poolId);
					if("success".equals(result.get("status"))){
						
						JSONArray computerList = result.getJSONArray("hosts");
						for (int i = 0; i < computerList.size(); i ++) {
							JSONObject computerObject = computerList.getJSONObject(i);
							String uid = computerObject.getString("uuid");
							JSONArray ips = computerObject.getJSONArray("ip");
							if(ip.equals(ips.getString(0))){
								JSONObject dhr = channel.hostDelete(uid);
								if("fail".equals(dhr.getString("status"))){
									flag = false;
								}
							}
						}
					}
				}
				if(flag){
					JSONObject dnr = channel.computePoolRemoveResource(poolId, name);
					if("success".equals(dnr.getString("status"))){
				        operLogService.addLog("计算资源池", "移除计算节点失败", "1", "1", request);
						return new MethodResult(MethodResult.SUCCESS,"资源移除成功");
					}
				}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    operLogService.addLog("计算资源池", "移除计算节点失败", "1", "2", request);
		return new MethodResult(MethodResult.FAIL,"资源删除失败");
	}
		
	/**
	 * 创建资源(节点)
	 * @param uuid
	 * @param name
	 * @return
	 */
	@RequestMapping(value="/an",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult addNode(String uuid,String name, HttpServletRequest request){
	    if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.compute_resource_node_add)){
            return new MethodResult(MethodResult.FAIL,"您没有添加资源节点的权限，请联系管理员");
        }
		if(StringUtil.isBlank(uuid)){
			return new MethodResult(MethodResult.FAIL,"资源池ID不能为空");
		}
		if(StringUtil.isBlank(name)){
			return new MethodResult(MethodResult.FAIL,"节点名不能为空");
		}
		try {
				HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
				if(channel!=null){
					JSONObject result = channel.computePoolAddResource(uuid, name);
					if("success".equals(result.get("status"))){
				        operLogService.addLog("计算资源池", "创建计算节点成功", "1", "1", request);
						return new MethodResult(MethodResult.SUCCESS,"创建成功");
					}
                    operLogService.addLog("计算资源池", "创建计算节点失败", "1", "2", request);
					return new MethodResult(MethodResult.FAIL,"创建失败");
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
        operLogService.addLog("计算资源池", "创建计算节点失败", "1", "2", request);
		return new MethodResult(MethodResult.FAIL,"创建失败");
	}
	/**
     * 跳转到主机列表页
     * @param uuid
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value="/{uuid}/{name}/hpdetail",method=RequestMethod.GET)
    public String hostPoolDetail(@PathVariable("uuid") String uuid,@PathVariable("name") String name,Model model,HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.compute_resource_pool_host_query)){
            return "not_have_access";
        }
        try {
            List<CloudHostVO> cList = new ArrayList<>();
                HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
                if(channel!=null){
                    JSONObject result = channel.hostQuery(uuid);
                    if("success".equals(result.get("status"))){
                        
                        JSONArray computerList = result.getJSONArray("hosts");
                        for (int i = 0; i < computerList.size(); i ++) {
                            JSONObject computerObject = computerList.getJSONObject(i);
                            String hostName = computerObject.getString("name");
                            String uid = computerObject.getString("uuid");
                            int status = computerObject.getInt("status");
                            Integer cpuCount = computerObject.getInt("cpu_count");
                            BigDecimal cpuUsage = new BigDecimal(computerObject.getString("cpu_usage"));
                            BigDecimal memoryUsage = new BigDecimal(computerObject.getString("memory_usage"));
                            BigDecimal diskUsage = new BigDecimal(computerObject.getString("disk_usage"));
                            
                            JSONArray memoryList = computerObject.getJSONArray("memory");
                            BigInteger[] mcount = new BigInteger[memoryList.size()];
                            for(int j=0;j<memoryList.size();j++){
                                mcount[j] = new BigInteger(memoryList.getString(j));
                            }
                            
                            JSONArray diskList = computerObject.getJSONArray("disk_volume");
                            BigInteger[] dcount = new BigInteger[diskList.size()];
                            for(int j=0;j<diskList.size();j++){
                                dcount[j] = new BigInteger(diskList.getString(j));
                            }
                            
                            JSONArray ips = computerObject.getJSONArray("ip");
                            String[] hostIps = new String[ips.size()];
                            for(int j=0;j<ips.size();j++){
                                hostIps[j] = ips.getString(j);
                            }
                            CloudHostVO computer = new CloudHostVO();
                            computer.setCpuCore(cpuCount);
                            computer.setCpuUsage(cpuUsage.doubleValue());
                            computer.setDiskUsage(diskUsage.doubleValue());
                            computer.setSysDisk(dcount[1]);
                            computer.setMemory(mcount[1]);
                            computer.setMemoryUsage(memoryUsage.doubleValue());
                            computer.setHostName(hostName);
                            computer.setDisplayName(hostName);
                            computer.setStatus(status);
                            computer.setId(uid);
                            computer.setRealHostId(uid);
                            if(hostIps!=null && hostIps.length == 1){
                                computer.setInnerIp(hostIps[0]);
                            }else if(hostIps!=null && hostIps.length == 2){
                                computer.setInnerIp(hostIps[0]);
                                computer.setOuterIp(hostIps[1]);                                
                            }
                             cList.add(computer);
                        }
                    }
                    if ("fail".equals(result.getString("status"))) {
                        logger.error("ComputeResourcePoolController.hostPoolDetail>>>获取资源池失败");
                        return "not_responsed";
                    }
                    if ("fail".equals(result.getString("status"))) {
                        logger.error("ComputeResourcePoolController.hostPoolDetail>>>获取资源池失败");
                        return "not_responsed";
                    }
                }
                if(cList.size()>0){
                    for(CloudHostVO cp : cList){
                        CloudHostVO cloudHost = cloudHostService.getByRealHostId(cp.getRealHostId());
                        if(cloudHost!=null){
                            // 设置主机显示名称和类型
                            cp.setDisplayName(cloudHost.getDisplayName());
                            cp.setType(cloudHost.getType());
                        } 
                        
                    }
                }
            model.addAttribute("hostPoolDetail", cList);
            int poolType = 0;// 0表示默认
            if(name.contains("server_pool")){
                poolType = 1; //1表示云服务器的资源池
            }else if(name.contains("desktop_pool")){
                poolType = 2; // 2表示云桌面的资源池
                //筛选仓库
                model.addAttribute("wareHoseList",CloudHostWarehouseService.getAll());
            }
            model.addAttribute("poolType", poolType);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "resourcepool/host_pool_detail";
    }
    /**
     * 
    * @Title: startCloudHost 
    * @Description: 主机开机
    * @param @param id
    * @param @param request
    * @param @return      
    * @return MethodResult     
    * @throws
     */
    @RequestMapping(value="/{id}/start",method=RequestMethod.GET)
    @ResponseBody
    public MethodResult startCloudHost(@PathVariable("id") String id,HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.compute_resource_pool_host_start)){
            return new MethodResult(MethodResult.FAIL,"您没有开机的权限，请联系管理员");
        }
        MethodResult mr = cloudHostService.operatorCloudHostByRealHostId(id, "1");
        return mr;
    }
    
	/**
	    * 
	    * @Title: nodeDevice 
	    * @Description: NC磁盘挂载
	    * @param @param nodename
	    * @param @param request
	    * @param @return      
	     */
	@RequestMapping(value="/{nodename}/device",method=RequestMethod.GET)
	public String nodeDevice(@PathVariable("nodename") String target,Model model,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.compute_resource_pool_node_device)){
			return "not_have_access";
		}
		
		String poolId = request.getParameter("poolId");
		List<MountDiskVo> cList = new ArrayList<MountDiskVo>();
		try {
			HttpGatewayAsyncChannel channel = HttpGatewayManager.getAsyncChannel(Integer.valueOf(1)); 
			if(channel!=null){ 
				long reqTime = System.currentTimeMillis();
				JSONObject result = channel.serverQueryStorageDevice(MountDiskVo.LEVEL, target, MountDiskVo.DISK_TYPE_LOCALSTORAGE);  
				logger.info("HttpGatewayAsyncChannel.serverQueryStorageDevice>result:"+result.toString());
                if("success".equals(result.get("status"))){
                	List<MountDiskVo> list = StorageManager.singleton().getDiskVoList(reqTime, MountDiskVo.DISK_TYPE_LOCALSTORAGE);
                	cList.addAll(list);
                }

				reqTime = System.currentTimeMillis();
				result = channel.serverQueryStorageDevice(MountDiskVo.LEVEL, target, MountDiskVo.DISK_TYPE_CLOUDSTORAGE);  
                if("success".equals(result.get("status"))){
                	List<MountDiskVo> list = StorageManager.singleton().getDiskVoList(reqTime, MountDiskVo.DISK_TYPE_CLOUDSTORAGE);
                	cList.addAll(list);
                }
		
				reqTime = System.currentTimeMillis();
				result = channel.serverQueryStorageDevice(MountDiskVo.LEVEL, target, MountDiskVo.DISK_TYPE_NASDISK);  
                if("success".equals(result.get("status"))){
                	List<MountDiskVo> list = StorageManager.singleton().getDiskVoList(reqTime, MountDiskVo.DISK_TYPE_NASDISK);
                	cList.addAll(list);
                }

				reqTime = System.currentTimeMillis();
				result = channel.serverQueryStorageDevice(MountDiskVo.LEVEL, target, MountDiskVo.DISK_TYPE_IPSAN);  
                if("success".equals(result.get("status"))){
                	List<MountDiskVo> list = StorageManager.singleton().getDiskVoList(reqTime, MountDiskVo.DISK_TYPE_IPSAN);
                	cList.addAll(list);
                }                
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();		
		}	
		
		model.addAttribute("formPageFlag", "1");
		model.addAttribute("poolId", poolId);
		model.addAttribute("deviceList", cList);
		model.addAttribute("nodename", target);		
		return "resourcepool/server_resource_pool_node_device";
	}
	
	/**
	    * 
	    * @Title: mount 
	    * @Description: 本地磁盘挂载
	    * @param @param request
	    * @param @return      
	     */
	@RequestMapping(value="/mount",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult mountDevice(HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.compute_resource_pool_node_mount)){
			return new MethodResult(MethodResult.FAIL,"您没有挂载本地硬盘的权限，请联系管理员");
		}

		String target = request.getParameter("nodename");
		Integer[] index = new Integer[]{Integer.valueOf(request.getParameter("index"))};
		Integer disk_type = Integer.valueOf(request.getParameter("disk_type"));
		try {
			HttpGatewayAsyncChannel channel = HttpGatewayManager.getAsyncChannel(Integer.valueOf(1));
			if(channel==null){
				operLogService.addLog("云计算资源池", "计算节点"+target+"挂载本地硬盘失败", "2", "2", request);
				return new MethodResult(MethodResult.FAIL,"挂载本地硬盘失败，请联系管理员");
			}	
			long reqTime = System.currentTimeMillis();	
			JSONObject result = channel.serverAddStorageDevice(MountDiskVo.LEVEL, target, disk_type, index, "", "", "");
			if ("fail".equals(result.get("status"))){
				operLogService.addLog("云计算资源池", "计算节点"+target+"挂载本地硬盘失败", "2", "2", request);
				return new MethodResult(MethodResult.FAIL,result.getString("message"));
			}
			//等待异步处理结果。
			StorageResult ret = StorageManager.singleton().getAddStorageReslt(reqTime);
			if (!ret.isFlag()){
				operLogService.addLog("云计算资源池", "计算节点"+target+"挂载本地硬盘失败，返回："+ret.getMsg(), "2", "2", request);
				return new MethodResult(MethodResult.FAIL,ret.getMsg());
			}			
		} catch (MalformedURLException e) {		
			// TODO Auto-generated catch block
			e.printStackTrace();
			operLogService.addLog("云计算资源池", "计算节点"+target+"挂载本地硬盘失败", "2", "2", request);
			return new MethodResult(MethodResult.FAIL,"挂载本地硬盘失败，请联系管理员");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			operLogService.addLog("云计算资源池", "计算节点"+target+"挂载本地硬盘失败", "2", "2", request);
			return new MethodResult(MethodResult.FAIL,"挂载本地硬盘失败，请联系管理员");			
		} 
		operLogService.addLog("云计算资源池", "计算节点"+target+"挂载本地硬盘成功", "2", "1", request);
		return new MethodResult(MethodResult.SUCCESS,"挂载硬盘成功！");	
	}
	/**
    * 
    * @Title: unmount 
    * @Description: 取消NC磁盘挂载
    * @param @param request
    * @param @return      
     */
	@RequestMapping(value="/unmount",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult unmountDevice(HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.compute_resource_pool_node_unmount)){
			return new MethodResult(MethodResult.FAIL,"您没有取消挂载硬盘的权限，请联系管理员");
		}	

		String target = request.getParameter("nodename");
		Integer index = Integer.valueOf(request.getParameter("index"));
		Integer diskType = Integer.valueOf(request.getParameter("disk_type"));
		try {
			HttpGatewayAsyncChannel channel = HttpGatewayManager.getAsyncChannel(Integer.valueOf(1));
			if(channel==null){
				operLogService.addLog("云计算资源池", "计算节点"+target+"取消挂载本地硬盘失败", "2", "2", request);
				return new MethodResult(MethodResult.FAIL,"取消挂载本地硬盘失败，请联系管理员");
			}	
			long reqTime = System.currentTimeMillis();			
			JSONObject result = channel.serverRemoveStorageDevice(MountDiskVo.LEVEL,target, diskType, index);
			if ("fail".equals(result.get("status"))){
				operLogService.addLog("云计算资源池", "计算节点"+target+"取消挂载本地硬盘失败", "2", "2", request);
				return new MethodResult(MethodResult.FAIL,result.getString("message"));
			}
			//等待异步处理结果。
			StorageResult ret = StorageManager.singleton().getRemoveStorageReslt(reqTime);
			if (!ret.isFlag()){
				operLogService.addLog("云计算资源池", "计算节点"+target+"取消挂载本地硬盘失败，返回："+ret.getMsg(), "2", "2", request);
				return new MethodResult(MethodResult.FAIL,ret.getMsg());
			}			
		} catch (MalformedURLException e) {		
			// TODO Auto-generated catch block
			e.printStackTrace();
			operLogService.addLog("云计算资源池", "计算节点"+target+"取消挂载本地硬盘失败", "2", "2", request);
			return new MethodResult(MethodResult.FAIL,"取消挂载本地硬盘失败，请联系管理员");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			operLogService.addLog("云计算资源池", "计算节点"+target+"取消挂载本地硬盘失败", "2", "2", request);
			return new MethodResult(MethodResult.FAIL,"取消挂载本地硬盘失败，请联系管理员");			
		} 
        operLogService.addLog("云计算资源池", "计算节点"+target+"取消挂载本地硬盘成功", "2", "1", request);
        
		return new MethodResult(MethodResult.SUCCESS,"取消挂载硬盘成功！");
	}
	
	
	/**
	    * 
	    * @Title: enableDisk 
	    * @Description: 启用本地存储设备
	    * @param @param request
	    * @param @return      
	     */
	@RequestMapping(value="/enableDisk",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult enableDisk(HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.compute_resource_pool_node_enabledisk)){
			return new MethodResult(MethodResult.FAIL,"您没有启用本地存储设备权限！");
		}	
		String target = request.getParameter("nodename");
		Integer index = Integer.valueOf(request.getParameter("index"));

		try {
			HttpGatewayAsyncChannel channel = HttpGatewayManager.getAsyncChannel(Integer.valueOf(1));
			if(channel==null){
				operLogService.addLog("云计算资源池", "计算节点"+target+"启用本地硬盘失败", "2", "2", request);
				return new MethodResult(MethodResult.FAIL,"启用本地硬盘失败，请联系管理员");
			}	
			long reqTime = System.currentTimeMillis();			
			JSONObject result = channel.serverEnableStorageDevice(MountDiskVo.LEVEL, target, MountDiskVo.DISK_TYPE_LOCALSTORAGE, index);
			if ("fail".equals(result.get("status"))){
				operLogService.addLog("云计算资源池", "计算节点"+target+"启用本地硬盘失败", "2", "2", request);
				return new MethodResult(MethodResult.FAIL,result.getString("message"));
			}
			//等待异步处理结果。
			StorageResult ret = StorageManager.singleton().getEnableStorageReslt(reqTime);
			if (!ret.isFlag()){
				operLogService.addLog("云计算资源池", "计算节点"+target+"启用本地硬盘失败，返回："+ret.getMsg(), "2", "2", request);
				return new MethodResult(MethodResult.FAIL,ret.getMsg());
			}					
		} catch (MalformedURLException e) {		
			// TODO Auto-generated catch block
			e.printStackTrace();
			operLogService.addLog("云计算资源池", "计算节点"+target+"启用本地硬盘失败", "2", "2", request);
			return new MethodResult(MethodResult.FAIL,"启用本地硬盘失败，请联系管理员");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			operLogService.addLog("云计算资源池", "计算节点"+target+"启用本地硬盘失败", "2", "2", request);
			return new MethodResult(MethodResult.FAIL,"启用本地硬盘失败，请联系管理员");			
		} 
        operLogService.addLog("云计算资源池", "计算节点"+target+"启用本地存储设备成功", "2", "1", request);
        
		return new MethodResult(MethodResult.SUCCESS,"启用本地存储设备成功！");
	}	
		
	/**
	    * 
	    * @Title: disableDisk 
	    * @Description: 禁用本地磁盘
	    * @param @param request
	    * @param @return      
	     */
	@RequestMapping(value="/disableDisk",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult disableDisk(HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.compute_resource_pool_node_disabledisk)){
			return new MethodResult(MethodResult.FAIL,"您没有禁用本地存储设备权限！");
		}	
		String target = request.getParameter("nodename");
		Integer index = Integer.valueOf(request.getParameter("index"));
		try {
			HttpGatewayAsyncChannel channel = HttpGatewayManager.getAsyncChannel(Integer.valueOf(1));
			if(channel==null){
				operLogService.addLog("云计算资源池", "计算节点"+target+"禁用本地硬盘失败", "2", "2", request);
				return new MethodResult(MethodResult.FAIL,"禁用本地硬盘失败，请联系管理员");
			}	
			
			long reqTime = System.currentTimeMillis();			
			JSONObject result = channel.serverDisableStorageDevice(MountDiskVo.LEVEL, target, MountDiskVo.DISK_TYPE_LOCALSTORAGE, index);
			if ("fail".equals(result.get("status"))){
				operLogService.addLog("云计算资源池", "计算节点"+target+"禁用本地硬盘失败", "2", "2", request);
				return new MethodResult(MethodResult.FAIL,result.getString("message"));
			}
			//等待异步处理结果。
			StorageResult ret = StorageManager.singleton().getDisableStorageReslt(reqTime);
			if (!ret.isFlag()){
				operLogService.addLog("云计算资源池", "计算节点"+target+"禁用本地硬盘失败返回："+ret.getMsg(), "2", "2", request);
				return new MethodResult(MethodResult.FAIL,ret.getMsg());
			}				
		} catch (MalformedURLException e) {		
			// TODO Auto-generated catch block
			e.printStackTrace();
			operLogService.addLog("云计算资源池", "计算节点"+target+"禁用本地硬盘失败", "2", "2", request);
			return new MethodResult(MethodResult.FAIL,"禁用本地硬盘失败，请联系管理员");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			operLogService.addLog("云计算资源池", "计算节点"+target+"禁用本地硬盘失败", "2", "2", request);
			return new MethodResult(MethodResult.FAIL,"禁用本地硬盘失败，请联系管理员");			
		} 
		operLogService.addLog("云计算资源池", "计算节点"+target+"禁用本地存储设备成功", "2", "1", request);
		return new MethodResult(MethodResult.SUCCESS,"禁用本地存储设备成功！");
	}		
	/**
	    * 
	    * @Title: addShareDisk 
	    * @Description: 共享存储挂载
	    * @param @param request
	    * @param @return      
	     */
	@RequestMapping(value="/addShareDisk",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult addShareDisk(HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.compute_resource_pool_node_mount)){
			return new MethodResult(MethodResult.FAIL,"您没有挂载共享存储的权限，请联系管理员");
		}	
		String target = request.getParameter("nodename");
		Integer[] index = new Integer[]{Integer.valueOf(9)};//挂载网络存储是否需要？
		Integer disk_type = Integer.valueOf(request.getParameter("disk_type"));
		String name = request.getParameter("name");
		String path = request.getParameter("path");
		String crypt = request.getParameter("crypt");
		try {
			HttpGatewayAsyncChannel channel = HttpGatewayManager.getAsyncChannel(Integer.valueOf(1));
			if(channel==null){
				operLogService.addLog("云计算资源池", "计算节点"+target+"挂载共享存储失败", "2", "2", request);
				return new MethodResult(MethodResult.FAIL,"挂载共享存储失败，请联系管理员");
			}	
			long reqTime = System.currentTimeMillis();			
			JSONObject result = channel.serverAddStorageDevice(MountDiskVo.LEVEL, target, disk_type, index, name, path, crypt);
			if ("fail".equals(result.get("status"))){
				operLogService.addLog("云计算资源池", "计算节点"+target+"挂载共享存储失败", "2", "2", request);
				return new MethodResult(MethodResult.FAIL,result.getString("message"));
			}
			//等待异步处理结果。
			StorageResult ret = StorageManager.singleton().getAddStorageReslt(reqTime);
			if (!ret.isFlag()){
				operLogService.addLog("云计算资源池", "计算节点"+target+"挂载共享存储返回："+ret.getMsg(), "2", "2", request);
				return new MethodResult(MethodResult.FAIL,ret.getMsg());
			}				
		} catch (MalformedURLException e) {		
			// TODO Auto-generated catch block
			e.printStackTrace();
			operLogService.addLog("云计算资源池", "计算节点"+target+"挂载共享存储失败", "2", "2", request);
			return new MethodResult(MethodResult.FAIL,"挂载共享存储失败，请联系管理员");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			operLogService.addLog("云计算资源池", "计算节点"+target+"挂载共享存储失败", "2", "2", request);
			return new MethodResult(MethodResult.FAIL,"挂载共享存储失败，请联系管理员");			
		} 
		operLogService.addLog("云计算资源池", "计算节点"+target+"挂载共享存储成功", "2", "1", request);
		return new MethodResult(MethodResult.SUCCESS,"挂载共享存储成功！");	
	}
	
	
	
    /**
     * 
    * @Title: stopCloudHost 
    * @Description: 主机关机 
    * @param @param id
    * @param @param request
    * @param @return      
    * @return MethodResult     
    * @throws
     */
    @RequestMapping(value="/{id}/stop",method=RequestMethod.GET)
    @ResponseBody
    public MethodResult stopCloudHost(@PathVariable("id") String id,HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.compute_resource_pool_host_stop)){
            return new MethodResult(MethodResult.FAIL,"您没有关机的权限，请联系管理员");
        }
        MethodResult mr = cloudHostService.operatorCloudHostByRealHostId(id, "2");
        return mr;
    }
    /**
     * 
    * @Title: restartCloudHost 
    * @Description: 主机重启
    * @param @param id
    * @param @param request
    * @param @return      
    * @return MethodResult     
    * @throws
     */
    @RequestMapping(value="/{id}/restart",method=RequestMethod.GET)
    @ResponseBody
    public MethodResult restartCloudHost(@PathVariable("id") String id,HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.compute_resource_pool_host_restart)){
            return new MethodResult(MethodResult.FAIL,"您没有重启的权限，请联系管理员");
        }
        MethodResult mr = cloudHostService.operatorCloudHostByRealHostId(id, "3");
        return mr;
    }
    /**
     * 
    * @Title: haltCloudHost 
    * @Description: 主机强制关机
    * @param @param id
    * @param @param request
    * @param @return      
    * @return MethodResult     
    * @throws
     */
    @RequestMapping(value="/{id}/halt",method=RequestMethod.GET)
    @ResponseBody
    public MethodResult haltCloudHost(@PathVariable("id") String id,HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.compute_resource_pool_host_halt)){
            return new MethodResult(MethodResult.FAIL,"您没有强制的权限，请联系管理员");
        }
        MethodResult mr = cloudHostService.operatorCloudHostByRealHostId(id, "4");
        return mr;
    }
    @RequestMapping(value="/{id}/addserver",method=RequestMethod.GET)
    @ResponseBody
    public MethodResult addServer(@PathVariable("id") String id,HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.compute_resource_pool_host_changetoserver)){
            return new MethodResult(MethodResult.FAIL,"您没有添加云服务器的权限，请联系管理员");
        }
        MethodResult mr = cloudHostService.addHostToServerByRealHostId(id);
        return mr;
    }
    @RequestMapping(value="/{hostId}/{wareHouseId}/adddesktop",method=RequestMethod.GET)
    @ResponseBody
    public MethodResult addDesktop(@PathVariable("hostId") String hostId,@PathVariable("wareHouseId") String wareHouseId,HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.compute_resource_pool_host_changetodesktop)){
            return new MethodResult(MethodResult.FAIL,"您没有添加云服务器的权限，请联系管理员");
        }
        MethodResult mr = cloudHostService.addHostToDeskTopByRealHostId(hostId, wareHouseId);
        return mr;
    }

}

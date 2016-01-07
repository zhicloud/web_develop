package com.zhicloud.ms.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.zhicloud.ms.app.helper.DefaultTreeNode;
import com.zhicloud.ms.common.util.RandomPassword;
import com.zhicloud.ms.mapper.CloudHostConfigModelMapper;
import com.zhicloud.ms.mapper.CloudHostMapper;
import com.zhicloud.ms.mapper.CloudHostWarehouseMapper;
import com.zhicloud.ms.mapper.SysGroupMapper;
import com.zhicloud.ms.mapper.SysUserMapper;
import com.zhicloud.ms.mapper.TerminalUserMapper;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.ICloudHostWarehouseService;
import com.zhicloud.ms.service.IOperLogService;
import com.zhicloud.ms.util.DateUtil;
import com.zhicloud.ms.util.StringUtil;
import com.zhicloud.ms.vo.CloudHostConfigModel;
import com.zhicloud.ms.vo.CloudHostVO;
import com.zhicloud.ms.vo.CloudHostWarehouse;
import com.zhicloud.ms.vo.SysGroupVO;
import com.zhicloud.ms.vo.SysUser;
import com.zhicloud.ms.vo.TerminalUserVO;

@Transactional(readOnly=true)
 class CloudHostWarehouseServiceImpl implements ICloudHostWarehouseService {
	
	private static final Logger logger = Logger.getLogger(UserServiceImpl.class);
	
	@Resource
    private IOperLogService operLogService;
	
	private SqlSession sqlSession;
	
	public SqlSession getSqlSession() {
		return sqlSession;
	}

	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	@Override
	public List<CloudHostWarehouse> getAll() {
		logger.debug("CloudHostWarehouseServiceImpl.getAll()");
		CloudHostWarehouseMapper chwMapper = this.sqlSession.getMapper(CloudHostWarehouseMapper.class);
		List<CloudHostWarehouse> cloudHostWarehouseList = chwMapper.getAll();
		return cloudHostWarehouseList;
	}
	@Override
	@Transactional(readOnly=false)
	public MethodResult addWarehouse(CloudHostWarehouse chw,HttpServletRequest request) {
 		logger.debug("CloudHostWarehouseServiceImpl.addWarehouse()");
		CloudHostWarehouseMapper chwMapper = this.sqlSession.getMapper(CloudHostWarehouseMapper.class);
		CloudHostMapper chMapper = this.sqlSession.getMapper(CloudHostMapper.class);
		CloudHostConfigModelMapper chcmMapper = this.sqlSession.getMapper(CloudHostConfigModelMapper.class);
//		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		Map<String,Object> condition = new HashMap<String,Object>();
		int result = 0;
		if(chw!=null){
			String warehouseId = StringUtil.generateUUID();
			condition.put("id", warehouseId);
			condition.put("cloudHostConfigModelId", chw.getCloudHostConfigModelId());
			condition.put("name", chw.getName());
			condition.put("totalAmount", chw.getTotalAmount());
			condition.put("createTime", DateUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
			result = chwMapper.addWarehouse(condition);
			if(result>0){
				CloudHostConfigModel chcm = chcmMapper.getById(chw.getCloudHostConfigModelId());
				String random = RandomPassword.getRandomPwd(3).toUpperCase();
				for(int i=0;i<chw.getTotalAmount();i++){
					Map<String,Object> cloudHostData = new HashMap<String,Object>();
					String id = StringUtil.generateUUID();
					cloudHostData.put("id",id);
					cloudHostData.put("warehouseId",warehouseId);
					cloudHostData.put("hostName",id+i);
					cloudHostData.put("displayName",chw.getName()+"-"+random+"-"+i);
					cloudHostData.put("account","warehouse");
					cloudHostData.put("password",RandomPassword.getRandomPwd(16));
					cloudHostData.put("cpuCore",chcm.getCpuCore());
					cloudHostData.put("memory",chcm.getMemory());
					cloudHostData.put("status",0);
					cloudHostData.put("sysImageId",chcm.getSysImageId());
					cloudHostData.put("sysDisk",chcm.getSysDisk());
					cloudHostData.put("dataDisk",chcm.getDataDisk());
					cloudHostData.put("bandwidth",chcm.getBandwidth());
					cloudHostData.put("sysImageName",chcm.getSysImageName());
					cloudHostData.put("type",1);
					cloudHostData.put("poolId",chw.getPoolId());
					chMapper.insertCloudHost(cloudHostData);
				}
			}
		}

        operLogService.addLog("主机仓库", "创建主机仓库"+chw.getName()+"成功", "1", "1", request);
		return new MethodResult(MethodResult.SUCCESS,"添加成功");
	}

	@Override
	public CloudHostWarehouse getById(String id) {
		logger.debug("CloudHostWarehouseServiceImpl.getById()");
		CloudHostWarehouseMapper chwMapper = this.sqlSession.getMapper(CloudHostWarehouseMapper.class);
		CloudHostWarehouse chw = chwMapper.getById(id);
		return chw;
	}

	@Override
	public CloudHostWarehouse getByName(String name) {
		logger.debug("CloudHostWarehouseServiceImpl.getByName()");
		CloudHostWarehouseMapper chwMapper = this.sqlSession.getMapper(CloudHostWarehouseMapper.class);
		CloudHostWarehouse chw = chwMapper.getByName(name);
		return chw;
	}

	@Override
	@Transactional(readOnly=false)
	public MethodResult updateWarehouse(String id,CloudHostWarehouse chw) {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		logger.debug("CloudHostWarehouseServiceImpl.updateWarehouse()");
		CloudHostWarehouseMapper chwMapper = this.sqlSession.getMapper(CloudHostWarehouseMapper.class);
		MethodResult mr = new MethodResult(MethodResult.FAIL,"更新失败");
		Map<String,Object> condition = new HashMap<String,Object>();
		int result = 0;
		if(chw!=null){
			condition.put("id", id);
			condition.put("cloudHostConfigModelId", chw.getCloudHostConfigModelId());
			condition.put("name", chw.getName());
			condition.put("modifyTime", DateUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
			result = chwMapper.updateWarehouse(condition);
			if(result>0){
				mr.status = MethodResult.SUCCESS;
				mr.message = "更新成功";
			}
		}
		if(mr.isSuccess()){
	          operLogService.addLog("主机仓库", "修改主机仓库"+chw.getName()+"成功", "1", "1", request);		    
		}else{	        
		    operLogService.addLog("主机仓库", "修改主机仓库"+chw.getName()+"失败", "1", "2", request);
		}
		return mr;
	}

	@Override
	@Transactional(readOnly=false)
	public MethodResult deleteWarehouse(String id) {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		logger.debug("CloudHostWarehouseServiceImpl.deleteWarehouse()");
		CloudHostWarehouseMapper chwMapper = this.sqlSession.getMapper(CloudHostWarehouseMapper.class);
		CloudHostMapper chMapper = this.sqlSession.getMapper(CloudHostMapper.class);
		List<CloudHostVO> cloudHostList = chMapper.getByWarehouseId(id);
		if(cloudHostList != null && cloudHostList.size() >0){	
	          operLogService.addLog("主机仓库", "删除主机仓库失败，仓库中还存在主机", "1", "2", request);
			return new MethodResult(MethodResult.FAIL, "仓库无法删除，请先删除其云主机");
		}
		int i = chwMapper.deleteWarehouse(id); 
		if(i > 0){			
            operLogService.addLog("主机仓库", "删除主机仓库成功", "1", "1", request);
			return new MethodResult(MethodResult.SUCCESS, "仓库删除成功");
		}
        operLogService.addLog("主机仓库", "删除主机仓库失败", "1", "2", request);
		return new MethodResult(MethodResult.FAIL, "删除失败");
	}
	/**
	 * 根据仓库ID分配仓库中的主机给用户IDs
	 * @see com.zhicloud.ms.service.ICloudHostWarehouseService#allocateHostsByWarehouseIdAndUserIds(java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(readOnly=false)
	public MethodResult allocateHostsByWarehouseIdAndUserIds(
			String warehouseId, String userIds) {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		CloudHostWarehouseMapper chwMapper = this.sqlSession.getMapper(CloudHostWarehouseMapper.class);
		CloudHostMapper chMapper = this.sqlSession.getMapper(CloudHostMapper.class);
		TerminalUserMapper tuMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
		CloudHostWarehouse warehouse = chwMapper.getById(warehouseId);
		if(warehouse == null){
	        operLogService.addLog("主机仓库", "主机仓库分配主机给用户失败，仓库不存在", "1", "2", request);
			return new MethodResult(MethodResult.FAIL, "仓库不存在");
		}else{
			String[] idArray = userIds.split(","); 
			if(idArray.length > warehouse.getRemainAmount()){
		         operLogService.addLog("主机仓库", "主机仓库分配主机给用户失败，仓库库存不足", "1", "2", request);
				return new MethodResult(MethodResult.FAIL, "该仓库剩余库存不足");				
			}
			Map<String,Object> warehouseData = new HashMap<String,Object>();
			warehouseData.put("warehouseId", warehouseId);
			List<CloudHostVO> hostList = chMapper.getHostNotDispatchByWarehouseId(warehouseData);
			if(idArray.length > hostList.size()){
	            operLogService.addLog("主机仓库", "主机仓库分配主机给用户失败，仓库库存不足", "1", "2", request);
				return new MethodResult(MethodResult.FAIL, "该仓库剩余库存不足");				
			}
			for(int i=0;i<idArray.length;i++){
				Map<String,Object> data = new HashMap<String,Object>();
				data.put("userId", idArray[i]);
				data.put("id", hostList.get(i).getId());
				data.put("assignTime", DateUtil.dateToString(new Date(),"yyyyMMddHHmmssSSS"));
				int updateClousHost = chMapper.updateCloudHostUserIdById(data);
				if(updateClousHost > 0){ 
					warehouseData.put("id", warehouseId);
					chwMapper.updateWarehouseForDispatchHost(warehouseData);
					
					Map<String,Object> userData = new HashMap<String,Object>();
					userData.put("id", idArray[i]);
					userData.put("modified_time", DateUtil.dateToString(new Date(),"yyyyMMddHHmmssSSS"));
					tuMapper.updateCloudHostAmount(userData);
				}
			}
            operLogService.addLog("主机仓库", "主机仓库分配主机给用户成功", "1", "1", request);
			return new MethodResult(MethodResult.SUCCESS, "分配成功");				
		}
		
 	}
	@Override
	@Transactional(readOnly=false)
	public MethodResult addAmount(String id, String addAmount,String poolId) {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		logger.debug("CloudHostWarehouseServiceImpl.addAmount()");
		if(StringUtil.isBlank(addAmount) || "0".equals(addAmount)){
            operLogService.addLog("主机仓库", "为仓库添加主机数量失败", "1", "2", request);
			return new MethodResult(MethodResult.FAIL,"添加的数量不能为0");
		}
		Integer myAddAmount = Integer.valueOf(addAmount);
		CloudHostWarehouseMapper chwMapper = this.sqlSession.getMapper(CloudHostWarehouseMapper.class);
		CloudHostConfigModelMapper chcmMapper = this.sqlSession.getMapper(CloudHostConfigModelMapper.class);
		CloudHostMapper chMapper = this.sqlSession.getMapper(CloudHostMapper.class);
		CloudHostWarehouse chw = this.getById(id);
		Map<String,Object> addCondition = new HashMap<String,Object>();
		addCondition.put("id", id);
		addCondition.put("addAmount",myAddAmount);
		addCondition.put("modifyTime",DateUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
		int n = chwMapper.addAmount(addCondition);
		if(n>0){
			Integer oldTotalAmount = chw.getTotalAmount();
			CloudHostConfigModel chcm = chcmMapper.getById(chw.getCloudHostConfigModelId());
			String random = RandomPassword.getRandomPwd(3).toUpperCase();
			for(int i=0;i<myAddAmount;i++){
				Integer curIndex = oldTotalAmount+i;
				Map<String,Object> cloudHostData = new HashMap<String,Object>();
				String curId = StringUtil.generateUUID();
				cloudHostData.put("id",curId);
				cloudHostData.put("warehouseId",chw.getId());
				cloudHostData.put("hostName",curId+curIndex);
				cloudHostData.put("displayName",chw.getName()+"-"+random+"-"+curIndex);
				cloudHostData.put("account","warehouse");
				cloudHostData.put("password",RandomPassword.getRandomPwd(16));
				cloudHostData.put("cpuCore",chcm.getCpuCore());
				cloudHostData.put("memory",chcm.getMemory());
				cloudHostData.put("sysImageId",chcm.getSysImageId());
				cloudHostData.put("sysDisk",chcm.getSysDisk());
				cloudHostData.put("dataDisk",chcm.getDataDisk());
				cloudHostData.put("bandwidth",chcm.getBandwidth());
				cloudHostData.put("sysImageName",chcm.getSysImageName());
				cloudHostData.put("status",0);
				cloudHostData.put("type",1);
				cloudHostData.put("poolId",poolId);
				chMapper.insertCloudHost(cloudHostData);
			}
		}
        operLogService.addLog("主机仓库", "主机仓库分配主机给用户成功", "1", "1", request);
		return new MethodResult(MethodResult.SUCCESS,"添加成功") ;
	}
	
	@Override
	@Transactional(readOnly=false)
	public MethodResult addAmount(String id, String addAmount,String poolId,String forJob) {
		logger.debug("CloudHostWarehouseServiceImpl.addAmount()");
		if(StringUtil.isBlank(addAmount) || "0".equals(addAmount)){
			return new MethodResult(MethodResult.FAIL,"添加的数量不能为0");
		}
		Integer myAddAmount = Integer.valueOf(addAmount);
		CloudHostWarehouseMapper chwMapper = this.sqlSession.getMapper(CloudHostWarehouseMapper.class);
		CloudHostConfigModelMapper chcmMapper = this.sqlSession.getMapper(CloudHostConfigModelMapper.class);
		CloudHostMapper chMapper = this.sqlSession.getMapper(CloudHostMapper.class);
		CloudHostWarehouse chw = this.getById(id);
		Map<String,Object> addCondition = new HashMap<String,Object>();
		addCondition.put("id", id);
		addCondition.put("addAmount",myAddAmount);
		addCondition.put("modifyTime",DateUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
		int n = chwMapper.addAmount(addCondition);
		if(n>0){
			Integer oldTotalAmount = chw.getTotalAmount();
			CloudHostConfigModel chcm = chcmMapper.getById(chw.getCloudHostConfigModelId());
			String random = RandomPassword.getRandomPwd(3).toUpperCase();
			for(int i=0;i<myAddAmount;i++){
				Integer curIndex = oldTotalAmount+i;
				Map<String,Object> cloudHostData = new HashMap<String,Object>();
				String curId = StringUtil.generateUUID();
				cloudHostData.put("id",curId);
				cloudHostData.put("warehouseId",chw.getId());
				cloudHostData.put("hostName",curId+curIndex);
				cloudHostData.put("displayName",chw.getName()+"-"+random+"-"+curIndex);
				cloudHostData.put("account","warehouse");
				cloudHostData.put("password",RandomPassword.getRandomPwd(16));
				cloudHostData.put("cpuCore",chcm.getCpuCore());
				cloudHostData.put("memory",chcm.getMemory());
				cloudHostData.put("sysImageId",chcm.getSysImageId());
				cloudHostData.put("sysDisk",chcm.getSysDisk());
				cloudHostData.put("dataDisk",chcm.getDataDisk());
				cloudHostData.put("bandwidth",chcm.getBandwidth());
				cloudHostData.put("sysImageName",chcm.getSysImageName());
				cloudHostData.put("status",0);
				cloudHostData.put("type",1);
				cloudHostData.put("poolId",poolId);
				chMapper.insertCloudHost(cloudHostData);
			}
		}
		return new MethodResult(MethodResult.SUCCESS,"添加成功") ;
	}
	@Override
    public DefaultTreeNode getAllTree() {
        SysGroupMapper sgMapper = this.sqlSession.getMapper(SysGroupMapper.class);
        TerminalUserMapper terminalMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
        // 获取所有第一级节点(parentid为0)
        List<SysGroupVO> sgList = sgMapper.queryByParentId("0");
        // 定义根节点
        
        DefaultTreeNode root = new DefaultTreeNode();
        root.setId("allUsers");
        root.setChecked(false);
        root.setText("全体员工");
        root.setIconCls("icon-all");
        // 循环所有一级节点加入group
        for (SysGroupVO group : sgList) {
            DefaultTreeNode groupNode = new DefaultTreeNode();
            groupNode.setId(group.getId());
            groupNode.setText(group.getGroupName());
            groupNode.setIconCls("icon-group");
            
            DefaultTreeNode childNode = new DefaultTreeNode();
            // 递归构造树型子节点
            buildTreeNodes(groupNode, group.getId());
            if (childNode.getChildren().size() > 0) {
                groupNode.getChildren().add(childNode);
            }
            root.getChildren().add(groupNode);
            // 处理该节点是否存在用户
            List<TerminalUserVO> terminalUserList = terminalMapper.getUserByGroupId(group.getId());
            if (terminalUserList.size() > 0) {
                for (TerminalUserVO user : terminalUserList) {
                    // 定义临时的User节点
                    DefaultTreeNode userNode = new DefaultTreeNode();
                    userNode.setId(user.getId());
                    userNode.setText(user.getName());
                    userNode.setIconCls("icon-user");
                    groupNode.getChildren().add(userNode);
                }
            }
        }
        return root;
    }
	
	@Override
	@Transactional(readOnly=false)
	public MethodResult assignToUsers(String warehouseId, String[] nodes) {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		TerminalUserMapper terminalMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
		CloudHostWarehouseMapper chwMapper = this.sqlSession.getMapper(CloudHostWarehouseMapper.class);
		CloudHostMapper chMapper = this.sqlSession.getMapper(CloudHostMapper.class);
		Map<String,Object> data = new HashMap<String, Object>();
		data.put("warehouseId", warehouseId);
		//查询仓库中所有未分配的云主机
		List<CloudHostVO> chList = chMapper.getHostNotDispatchByWarehouseId(data);
		if(chList==null || chList.size()<nodes.length || chList.size()<1 || nodes.length<1){
            operLogService.addLog("主机仓库", "分配主机失败", "1", "2", request);
			return new MethodResult(MethodResult.FAIL, "分配失败");
		}
		for(int i=0;i<nodes.length;i++){
			//给云主机设置userId
			Map<String,Object> cloudHostData = new HashMap<String, Object>();
			cloudHostData.put("id", chList.get(i).getId());
			cloudHostData.put("userId",nodes[i]);
			cloudHostData.put("assignTime",DateUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
			chMapper.updateCloudHostUserIdById(cloudHostData);
			//用户云主机持有数量加 1
			Map<String,Object> terminalUserData = new HashMap<String, Object>();
			terminalUserData.put("id", nodes[i]);
			terminalUserData.put("modified_time", DateUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
			terminalMapper.updateCloudHostAmount(terminalUserData);
			//仓库已分配数量加1，未分配数量减1
			Map<String,Object> warehouseData = new HashMap<String, Object>();
			warehouseData.put("id", warehouseId);
			chwMapper.updateWarehouseForDispatchHost(warehouseData);
		}
        operLogService.addLog("主机仓库", "分配主机成功", "1", "1", request);
		return new MethodResult(MethodResult.SUCCESS,"分配成功");
	}
	
	@Override
	@Transactional(readOnly=false)
	public MethodResult assignToUsersTwo(String warehouseId, String[] nodes,String[] hostIds) {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		TerminalUserMapper terminalMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
		CloudHostWarehouseMapper chwMapper = this.sqlSession.getMapper(CloudHostWarehouseMapper.class);
		CloudHostMapper chMapper = this.sqlSession.getMapper(CloudHostMapper.class);
		if(nodes.length<1 || hostIds.length<1 || hostIds.length<nodes.length){
		    operLogService.addLog("主机仓库", "分配主机失败", "1", "2", request);
			return new MethodResult(MethodResult.FAIL,"分配失败");
		}
		for(int i=0;i<nodes.length;i++){
			//给云主机设置userId
			Map<String,Object> cloudHostData = new HashMap<String, Object>();
			cloudHostData.put("id", hostIds[i]);
			cloudHostData.put("userId",nodes[i]);
			cloudHostData.put("assignTime",DateUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
			chMapper.updateCloudHostUserIdById(cloudHostData);
			//用户云主机持有数量加 1
			Map<String,Object> terminalUserData = new HashMap<String, Object>();
			terminalUserData.put("id", nodes[i]);
			terminalUserData.put("modified_time", DateUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
			terminalMapper.updateCloudHostAmount(terminalUserData);
			//仓库已分配数量加1，未分配数量减1
			Map<String,Object> warehouseData = new HashMap<String, Object>();
			warehouseData.put("id", warehouseId);
			chwMapper.updateWarehouseForDispatchHost(warehouseData);
		}
		operLogService.addLog("主机仓库", "分配主机成功", "1", "1", request);
		return new MethodResult(MethodResult.SUCCESS,"分配成功");
	}
	
	/**
	 * @Description:递归构造树型节点
	 * @param groupNode 节点
	 * @param parentid  父节点ID   
	 * @return DefaultTreeNode 
	 */
    @Transactional(readOnly = false)
    public void buildTreeNodes(DefaultTreeNode groupNode, String parentid) {
        if (!"".equals(parentid)) {
            SysGroupMapper sgMapper = this.sqlSession.getMapper(SysGroupMapper.class);
            TerminalUserMapper terminalMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
            // 获取该节点的所有子节点
            List<SysGroupVO> childList = sgMapper.queryByParentId(parentid);
            if(childList==null||childList.size()==0) return;
            for (SysGroupVO group : childList) {
                // 拼装该节点数据
                DefaultTreeNode childNode = new DefaultTreeNode();
                childNode.setId(group.getId());
                childNode.setText(group.getGroupName());
                childNode.setIconCls("icon-group");
                // 处理该节点是否存在用户
                List<TerminalUserVO> terminalUserList = terminalMapper.getUserByGroupId(group.getId());
                if (terminalUserList.size() > 0) {
                    for (TerminalUserVO user : terminalUserList) {
                        // 定义临时的User节点
                        DefaultTreeNode userNode = new DefaultTreeNode();
                        userNode.setId(user.getId());
                        userNode.setText(user.getName());
                        userNode.setIconCls("icon-user");
                        childNode.getChildren().add(userNode);
                    }
                }
                // 继续处理该节点是否存在子节点
                buildTreeNodes(childNode, group.getId());
                groupNode.getChildren().add(childNode);
            }
        }

    }
    
    public JSONArray buildTreeJSON(String menuid, JSONArray array, String roleid) {
    	SysGroupMapper sgMapper = this.sqlSession.getMapper(SysGroupMapper.class);
    	SysUserMapper suMapper = this.sqlSession.getMapper(SysUserMapper.class);
        TerminalUserMapper terminalMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
        // 获取所有第一级节点(parentid为0)
        JSONArray returnArray = new JSONArray();
        if(array==null){
        	return null;
        }
        // 定义根节点
        if("all".equals(menuid)){
        	JSONObject json = new JSONObject();
        	json.put("id", "allUsers");
        	json.put("text", "全体员工");
        	json.put("level","all");
        	JSONArray obj = buildTreeJSON(null, array, roleid);
        	if(obj==null || obj.size()<1){
        		obj = new JSONArray();
        	}
        	Map<String,Object> uCondition = new LinkedHashMap<String, Object>();
        	uCondition.put("type", 2);
        	List<SysUser> uList = suMapper.queryAllTerminalOutGroup(uCondition);
        	for(SysUser u : uList){
        		JSONObject jb = new JSONObject();
        		jb.put("id", u.getId());
        		jb.put("text", u.getUsername());
        		jb.put("level", "user");
        		obj.add(jb);
        	}
        	if (obj!=null && obj.size() > 0) {
        		json.put("children", obj);
        	}
        	returnArray.add(json);
        }
        // 循环所有一级节点加入group
        List<SysGroupVO> sgList = null;
        if(menuid==null || StringUtil.isBlank(menuid)){
        	sgList = sgMapper.queryByParentId("0");
        }else if(!"all".equals(menuid) && menuid!=null){
        	sgList = sgMapper.queryByParentId(menuid);
        }else{
        	 return returnArray;
        }
        if(sgList!=null && sgList.size()>0){
        	for (SysGroupVO group : sgList) {
        		JSONObject obj = new JSONObject();
        		obj.put("id", group.getId());
        		obj.put("text", group.getGroupName());
        		obj.put("level","group");
        		JSONArray childArray = buildTreeJSON(group.getId(),array,roleid);
        		if(childArray==null || childArray.size()<1){
        			childArray = new JSONArray();
        		}
        		List<TerminalUserVO> userList = terminalMapper.getUserByGroupId(group.getId());
        		if(userList!=null && userList.size()>0){
        			for(TerminalUserVO user : userList){
        				JSONObject subObj = new JSONObject();
        				subObj.put("id", user.getId());
        				subObj.put("text", user.getUsername());
        				subObj.put("level","user");
        				childArray.add(subObj);
        			}
        		}
        		if(childArray!=null && childArray.size()>0){
        			obj.put("children", childArray);
        		}
        		returnArray.add(obj);
        	}
        }else {
        	return null;
        }
        return returnArray;
    }
	@Override
	@Transactional(readOnly = false)
	public MethodResult deleteByIds(List<String> ids) {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		logger.debug("CloudHostWarehouseServiceImpl.deleteByIds()");
		CloudHostWarehouseMapper chwMapper = this.sqlSession.getMapper(CloudHostWarehouseMapper.class);
		CloudHostMapper chMapper = this.sqlSession.getMapper(CloudHostMapper.class);
		List<String> curIds = new ArrayList<>();
		if(ids!=null && ids.size()>0){
			for(String id : ids){
				List<CloudHostVO> cloudHostList = chMapper.getByWarehouseId(id);
				if(cloudHostList != null && cloudHostList.size() >0){
					continue;
				}else{
					curIds.add(id);
				}
			}
		}
		if(curIds.size()<1){
		    operLogService.addLog("主机仓库", "批量删除失败", "1", "2", request);
			return new MethodResult(MethodResult.FAIL, "删除失败,请先删除云主机");
		}
		int i = chwMapper.deleteByIds(curIds);
		if(i > 0){			
	          operLogService.addLog("主机仓库", "批量删除"+i+"个主机成功", "1", "1", request);
			return new MethodResult(MethodResult.SUCCESS, i+"个仓库删除成功");
		}
        operLogService.addLog("主机仓库", "批量删除失败", "1", "2", request);
		return new MethodResult(MethodResult.FAIL, "删除失败,请先删除云主机");
	}
	@Override
	@Transactional(readOnly = false)
	public MethodResult updateCheckTimeOrMinimum(Map<String, Object> condition) {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		logger.debug("CloudHostWarehouseServiceImpl.updateCheckTimeOrMinimum()");
		try {
			CloudHostWarehouseMapper chwMapper = this.sqlSession.getMapper(CloudHostWarehouseMapper.class);
			int n = chwMapper.updateCheckTimeOrMinimum(condition);
			if(n > 0){
		        operLogService.addLog("主机仓库", "更新仓库最小库存成功", "1", "1", request);
				return new MethodResult(MethodResult.SUCCESS, "更新成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        operLogService.addLog("主机仓库", "更新仓库最小库存失败", "1", "2", request);
		return new MethodResult(MethodResult.FAIL, "更新失败");
	}

	/**
	 * 根据warehouseId更正仓库的库存等数量 
	* <p>Title: updateCount</p> 
	* <p>Description: </p> 
	* @param warehouseId
	* @return 
	* @see com.zhicloud.ms.service.ICloudHostWarehouseService#updateCount(java.lang.String)
	 */
    @Override
    @Transactional(readOnly = false)
    public MethodResult updateCount(String warehouseId) {
        CloudHostWarehouseMapper chwMapper = this.sqlSession.getMapper(CloudHostWarehouseMapper.class);
        chwMapper.correctAllCount(warehouseId);
        return new MethodResult(MethodResult.SUCCESS,"成功");

    }
    
    /**
     * @Description:增加资源池最大并发创建数
     * @param condition 参数
     * @return int
     */
    public int addConcurrent(Map<String, Object> condition) {
        CloudHostWarehouseMapper chwMapper = this.sqlSession.getMapper(CloudHostWarehouseMapper.class);
        return chwMapper.addConcurrent(condition);
    }

    /**
     * @Description:修改资源池最大并发创建数
     * @param condition 参数
     * @return int
     */
    public int updateConcurrent(Map<String, Object> condition) {
        CloudHostWarehouseMapper chwMapper = this.sqlSession.getMapper(CloudHostWarehouseMapper.class);
        return chwMapper.updateConcurrent(condition);
    }
    
    /**
     * @Description:获取资源池最大并发创建数集合
     * @param condition 参数
     * @return List<CloudHostWarehouse>
     */
    public List<CloudHostWarehouse> getAllConcurrent() {
        CloudHostWarehouseMapper chwMapper = this.sqlSession.getMapper(CloudHostWarehouseMapper.class);
        return chwMapper.getAllConcurrent();
    }

    /**
     * @Description:保存资源池最大并发创建数设置
     * @param condition 参数
     * @return MethodResult
     */
    @Transactional(readOnly=false)
    public int saveConcurrent(Map<String, Object> condition) {
        CloudHostWarehouseMapper chwMapper = this.sqlSession.getMapper(CloudHostWarehouseMapper.class);
        // 先查询需要保存的资源池是否已经存在数据库表中
        CloudHostWarehouse host = chwMapper.getConcurrent(condition.get("pool_id") + "");
        if (host == null) {
            synchronized (this) {
                List<CloudHostWarehouse> lists = CloudHostServiceImpl.maxconcurrent_lists;
                // 如果保存的时候还没有执行过创建主机,则不更新缓存
                if (lists != null) {
                    CloudHostWarehouse newhost = new CloudHostWarehouse();
                    newhost.setPoolId(condition.get("pool_id") + "");
                    newhost.setPool_name(condition.get("pool_name") + "");
                    newhost.setMax_creating(Integer.parseInt(condition.get("max_creating") + ""));
                    lists.add(newhost);
                }
            }
            return addConcurrent(condition);
        } else {
            synchronized (this) {
                List<CloudHostWarehouse> lists = CloudHostServiceImpl.maxconcurrent_lists;
                // 如果保存的时候还没有执行过创建主机,则不更新缓存
                if (lists != null) {
                    for (CloudHostWarehouse cloud : lists) {
                        if (cloud.getPoolId().equals(condition.get("pool_id"))) {
                            cloud.setMax_creating(Integer.parseInt(condition.get("max_creating") + ""));
                        }
                    }
                }
            }
            return updateConcurrent(condition);
        }
    }
}


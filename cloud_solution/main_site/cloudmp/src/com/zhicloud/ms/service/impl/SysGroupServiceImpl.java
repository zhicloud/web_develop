/**
 * Project Name:ms
 * File Name:SysGroupServiceImpl.java
 * Package Name:com.zhicloud.ms.service.impl
 * Date:Mar 16, 20155:21:36 PM
 * 
 *
 */

package com.zhicloud.ms.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhicloud.ms.common.util.StringUtil;
import com.zhicloud.ms.mapper.SysGroupMapper;
import com.zhicloud.ms.mapper.SysUserMapper;
import com.zhicloud.ms.mapper.TerminalUserMapper;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.ISysGroupService;
import com.zhicloud.ms.util.TreeNode;
import com.zhicloud.ms.vo.SysGroupVO;
import com.zhicloud.ms.vo.SysGroupVOTree;
import com.zhicloud.ms.vo.TerminalUserVO;


/**
 * ClassName: SysGroupServiceImpl 
 * Function: 分组Service实现类. 
 * date: Mar 16, 2015 5:21:36 PM 
 *
 * @author sean
 * @version 
 * @since JDK 1.7
 */
@Service("sysGroupService")
@Transactional(readOnly=true)
public class SysGroupServiceImpl implements ISysGroupService {
	
	private static final Logger logger = Logger.getLogger(SysGroupServiceImpl.class);
	@Resource
	private SqlSession sqlSession;
	
	/**
	 * @see com.zhicloud.ms.service.ISysGroupService#queryAll()
	 */
	@Override
	public List<SysGroupVO> queryAll() {
		logger.debug("SysGroupServiceImpl.queryAll()");
		SysGroupMapper sysGroupMapper = this.sqlSession.getMapper(SysGroupMapper.class);
		return sysGroupMapper.getAll();
	}
	
	/**
	 * 
	 * @see com.zhicloud.ms.service.ISysGroupService#getAllWithoutMyself(java.lang.String)
	 */
	@Override
	public List<SysGroupVO> getAllWithoutMyself(String groupId) {
		SysGroupMapper sysGroupMapper = this.sqlSession.getMapper(SysGroupMapper.class);
		List<SysGroupVO> groupList = sysGroupMapper.getAll();
		Iterator<SysGroupVO> iterator = groupList.iterator();
		while(iterator.hasNext()){
			SysGroupVO sysGroupVO = iterator.next();
			if(groupId.equals(sysGroupVO.getId())){
				iterator.remove();
			}
		}
		return groupList;
	}


	/**
	 * @see com.zhicloud.ms.service.ISysGroupService#getAllAvailable(java.lang.String)
	 */
	@Override
	public List<SysGroupVO> getAllAvailable(String groupId) {
		SysGroupMapper sysGroupMapper = this.sqlSession.getMapper(SysGroupMapper.class);
		List<SysGroupVO> groupList = sysGroupMapper.getAll();
		
		SysGroupVO currentVO = sysGroupMapper.getById(groupId);
		
		List<SysGroupVO> result = sysGroupMapper.queryByParentId(currentVO.getParentId());
		while(!result.isEmpty()){
			for(SysGroupVO sysGroupVO : result){
				List<SysGroupVO> tmp = sysGroupMapper.queryByParentId(sysGroupVO.getId());
			}
		}
		
		
		
		return groupList;
	}

	@Override
	public List<SysGroupVO> getSysGroupByParentId(String parentId) {
		SysGroupMapper sysGroupMapper = this.sqlSession.getMapper(SysGroupMapper.class);
		return sysGroupMapper.queryByParentId(parentId);
		
	}
	
	@Override
	public List<SysGroupVO> getAllChildren(String groupId) {
		SysGroupMapper sysGroupMapper = this.sqlSession.getMapper(SysGroupMapper.class);
		List<SysGroupVO> allList = sysGroupMapper.getAll();
		TreeNode nodes = SysGroupVOTree.getInstance(allList).getTreeNode(groupId);
		List<TreeNode> list = nodes.getAllChildren();
		List<SysGroupVO> result = new ArrayList<SysGroupVO>();
		for(TreeNode tmp : list) {
			result.add((SysGroupVO) tmp.getBindData());
		}
		
		
		return result;
	}

	@Override
	public List<SysGroupVO> removeAllChildren(String groupId) {
		
		List<SysGroupVO> result = queryAll();
		List<SysGroupVO> children = getAllChildren(groupId);
		children.add(queryById(groupId));
		
		Iterator<SysGroupVO> iterator = result.iterator();
		
		while(iterator.hasNext()) {
			SysGroupVO parentVO = iterator.next();
			for(SysGroupVO tmp : children) {
				if(parentVO.getId().equals(tmp.getId())) {
					iterator.remove();
					continue;
				}
			}
		}
		return result;
	}

	/**
	 * 
	 * @see com.zhicloud.ms.service.ISysGroupService#queryById()
	 */
	@Override
	public SysGroupVO queryById(String groupId) {
		logger.debug("SysGroupServiceImpl.queryById()");
	
		SysGroupMapper sysGroupMapper = this.sqlSession.getMapper(SysGroupMapper.class);
		return sysGroupMapper.getById(groupId);
	}

	/**
	 * 
	 * @see com.zhicloud.ms.service.ISysGroupService#addSysGroup(java.util.Map)
	 */
	@Override
	@Transactional(readOnly=false)
	public MethodResult addSysGroup(Map<String, Object> parameter) {
		logger.debug("SysGroupServiceImpl.addSysGroup()");
		
		String groupName = (String) parameter.get("group_name");
		String parentId = (String) parameter.get("parent_id");
		String description = (String) parameter.get("description");
		
		SysGroupMapper sysGroupMapper = this.sqlSession.getMapper(SysGroupMapper.class);
		Map<String, Object> data = new LinkedHashMap<String, Object>();
		

		data.put("id", StringUtil.generateUUID());
		data.put("group_name", groupName);
		if(StringUtil.isBlank(parentId)){
			parentId = "0";
		}
		data.put("parent_id", parentId);
		data.put("amount", 0);
		data.put("cloud_host_amount", 0);
		data.put("description", description);
		data.put("create_time", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
		data.put("modified_time", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
		
		Integer result = sysGroupMapper.addSysGroup(data);
		
		if(result > 0) {
			return new MethodResult(MethodResult.SUCCESS, "添加成功");
		}
		return new MethodResult(MethodResult.FAIL, "添加失败");
	}

	/**
	 * @see com.zhicloud.ms.service.ISysGroupService#addGroupItem(java.util.Map)
	 */
	@Override
	public MethodResult addGroupItem(Map<String, Object> parameter) {

		return null;
	}

	/**
	 * @see com.zhicloud.ms.service.ISysGroupService#updateSysGroupById(java.util.Map)
	 */
	@Override
	@Transactional(readOnly=false)
	public MethodResult updateSysGroupById(Map<String, Object> parameter) {
		String id = (String) parameter.get("id");
		String parentId = (String) parameter.get("parent_id");
		String groupName = (String) parameter.get("group_name");
		String description = (String) parameter.get("description");
		SysGroupMapper sysGroupMapper = this.sqlSession.getMapper(SysGroupMapper.class);
		Map<String, Object> data = new LinkedHashMap<String, Object>();
		
		data.put("id", id);
		data.put("parent_id", parentId);
		data.put("group_name", groupName);
		data.put("description", description);
		data.put("modified_time", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
		
		Integer result = sysGroupMapper.updateSysGroupById(data);
		
		if(result > 0) {
			return new MethodResult(MethodResult.SUCCESS, "修改成功");
		}
		return new MethodResult(MethodResult.FAIL, "修改失败");

	}

	/**
	 * @see com.zhicloud.ms.service.ISysGroupService#deleteSysGroupByIds(java.util.List)
	 */
	@Override
	public MethodResult deleteSysGroupByIds(List<String> groupIds) {
		SysGroupMapper sysGroupMapper = this.sqlSession.getMapper(SysGroupMapper.class);
		Integer result = 0;
		if(groupIds!=null && groupIds.size()>0){
			result = sysGroupMapper.deleteSysGroupByIds(groupIds);
		}
		if(result > 0) {
			return new MethodResult(MethodResult.SUCCESS, "修改成功");
		}
		return new MethodResult(MethodResult.FAIL, "修改失败");
	}

	/**
	 * 
	 * @see com.zhicloud.ms.service.ISysGroupService#deleteSysGroupById(java.lang.String)
	 */
	@Override
	@Transactional(readOnly=false)
	public MethodResult deleteSysGroupById(String groupId) {
	    MethodResult mr =  new MethodResult(MethodResult.SUCCESS, "修改成功");
		SysGroupMapper sysGroupMapper = this.sqlSession.getMapper(SysGroupMapper.class);
		SysGroupVO group = sysGroupMapper.getById(groupId);
		mr.setProperty("name", group.getGroupName());
		Integer result = sysGroupMapper.deleteSysGroupById(groupId);
		
		if(result > 0) {
			return mr;
		}
		mr.status = MethodResult.FAIL;
		mr.message = "修改失败";
		return mr;
	}
	
	/**
	 * 
	 * @see com.zhicloud.ms.service.ISysGroupService#manageItems(java.util.Map)
	 */
	@Override
	@Transactional(readOnly=false)
	public MethodResult manageItems(Map<String, Object> parameter) {
	    MethodResult mr = new MethodResult(MethodResult.SUCCESS, "添加成功");
		try {
			SysUserMapper userMapper = this.sqlSession.getMapper(SysUserMapper.class);
		     SysGroupMapper sysGroupMapper = this.sqlSession.getMapper(SysGroupMapper.class);
		    

			String groupId = StringUtil.trim(parameter.get("group_id"));
			String userIdIn = StringUtil.trim(parameter.get("user_id_in"));
			String userIdOut = StringUtil.trim(parameter.get("user_id_out"));
			SysGroupVO group = sysGroupMapper.getById(groupId);
			mr.setProperty("name", group.getGroupName());
			String[] userIdsIn = userIdIn.split(","); 
			String[] userIdsOut = userIdOut.split(",");
			
			int addResult = 0;
			int removeResult = 0;
			
			if(!StringUtil.isBlank(userIdIn)) {
				
				for(int i = 0; i < userIdsIn.length; i++) {
					Map<String,Object> condition = new LinkedHashMap<String,Object>();
					condition.put("group_id", groupId);
					condition.put("id", userIdsIn[i]);
					addResult += userMapper.addItems(condition);
				}
			}else {
				addResult = 1;
			}
			
			Map<String,Object> condition = new LinkedHashMap<String,Object>();
			condition.put("id", groupId);
			condition.put("modified_time", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
			

			if(!StringUtil.isBlank(userIdOut)){
				for(int i = 0; i < userIdsOut.length; i++) {
					removeResult += userMapper.deleteItems(userIdsOut[i]);
				}
				
			}else {
				removeResult = 1;
			}
			
			boolean addFlag = (addResult == userIdsIn.length);
			boolean removeFlage = (removeResult == userIdsOut.length);
			
			if(addFlag && removeFlage) {
			    return mr; 
			}else {
			    mr.status = MethodResult.FAIL;
			    mr.message = "添加失败";
				return mr;
			}
		} catch (Exception e) {
		    mr.status = MethodResult.FAIL;
            mr.message = "添加失败";
            return mr;
		}
		
	}

	/**
	 * 
	 * @see com.zhicloud.ms.service.ISysGroupService#getCloudHostAmountInGroup(java.lang.String)
	 */
	@Override
	public List<SysGroupVO> getCloudHostAmountInGroup() {
				
		SysGroupMapper sysGroupMapper = this.sqlSession.getMapper(SysGroupMapper.class);
		TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
		
		List<SysGroupVO> result = new ArrayList<SysGroupVO>();
		List<SysGroupVO> list = sysGroupMapper.getAll();
		
		for(SysGroupVO sysGroupVO:list) {
			List<TerminalUserVO> terminalUserList= terminalUserMapper.getUserByGroupId(sysGroupVO.getId());
			int amount = terminalUserList.size();
			int cloudHostAmount = 0;
			for(TerminalUserVO terminalUserVO: terminalUserList){
				cloudHostAmount += terminalUserVO.getCloudHostAmount();
			}
			sysGroupVO.setAmount(amount);
			sysGroupVO.setCloudHostAmount(cloudHostAmount);
			result.add(sysGroupVO);
		}
		
		return result;
	}

	/**
	 * @see com.zhicloud.ms.service.ISysGroupService#checkAvailable(java.lang.String)
	 */
	@Override
	public boolean checkAvailable(String groupName) {
		SysGroupMapper sysGroupMapper = this.sqlSession.getMapper(SysGroupMapper.class);
 		SysGroupVO sysGroupVO = sysGroupMapper.queryGroupByGroupName(groupName);
 		boolean flag = false;
 		if(sysGroupVO == null) {
 			flag = true;
 		}
		return flag;
	}
	

}


package com.zhicloud.op.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.zhicloud.op.app.helper.ServiceHelper;
import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.exception.AppException;
import com.zhicloud.op.modal.DefaultTreeNode;
import com.zhicloud.op.mybatis.mapper.SysPrivilegeMapper;
import com.zhicloud.op.mybatis.mapper.SysPrivilegeRoleRelationMapper;
import com.zhicloud.op.remote.BeanDirectCallableDefaultImpl;
import com.zhicloud.op.remote.Callable;
import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.service.SysPrivilegeService;
import com.zhicloud.op.vo.SysPrivilegeRoleRelationVO;
import com.zhicloud.op.vo.SysPrivilegeVO;

@Transactional(readOnly=true)
public class SysPrivilegeServiceImpl extends BeanDirectCallableDefaultImpl implements SysPrivilegeService
{
	public static final Logger logger = Logger.getLogger(SysPrivilegeServiceImpl.class);
	
	//-----------------
	
	private SqlSession sqlSession;
	
	public void setSqlSession(SqlSession sqlSession)
	{
		this.sqlSession = sqlSession;
	}

	//-----------------
	
	/**
	 * 
	 */
	@Callable
	public void getPrivilegeTree(HttpServletRequest request, HttpServletResponse response)
	{
		
		logger.info("++++++++++++++++++++++++++++++++++++开始设置+++++++++++++++++++++++++");
		logger.debug("SysPrivilegeServiceImpl.getPrivilegeTree()");
		try
		{
			response.setContentType("text/plain; charset=utf-8");
			
			// 参数处理
			String roleId = request.getParameter("roleId");
			if( StringUtil.isBlank(roleId) )
			{
				throw new AppException("roleId不能为空");
			}

			SysPrivilegeRoleRelationMapper sysPrivilegeRoleRelationMapper = this.sqlSession.getMapper(SysPrivilegeRoleRelationMapper.class);
			SysPrivilegeMapper sysPrivilegeMapper = this.sqlSession.getMapper(SysPrivilegeMapper.class);
			
			// 构造用户的权限集
			Set<String> privilegeSet = new HashSet<String>();
			List<SysPrivilegeRoleRelationVO> sysPrivilegeRoleRelationList = sysPrivilegeRoleRelationMapper.getByRoleId(roleId);
			for( SysPrivilegeRoleRelationVO sysPrivilegeRoleRelationVO : sysPrivilegeRoleRelationList )
			{
				privilegeSet.add(sysPrivilegeRoleRelationVO.getPrivilegeId());
			}
			
			// 构造树的节点
			List<SysPrivilegeVO> sysPrivilegeList = sysPrivilegeMapper.getAll();	// 需要r.level, sort_num排序
			
			DefaultTreeNode rootTreeNode = new DefaultTreeNode();
			rootTreeNode.setId("--root--node--@ZhiCloud--");	// 一个惟一不重复的code就可以了
			rootTreeNode.setText("所有权限");
			
			Map<String, DefaultTreeNode> nodeMap = new HashMap<String, DefaultTreeNode>();
			for( SysPrivilegeVO sysPrivilegeVO : sysPrivilegeList )
			{
				// 构造树节点
				DefaultTreeNode thisTreeNode = new DefaultTreeNode();
				thisTreeNode.setId(sysPrivilegeVO.getId());
				thisTreeNode.setText(sysPrivilegeVO.getName());
				thisTreeNode.setChecked(privilegeSet.contains(sysPrivilegeVO.getId()));
				
				// 存起来
				nodeMap.put(thisTreeNode.getId(), thisTreeNode);
				
				// 寻找父节点
				String parentId = StringUtil.trim(sysPrivilegeVO.getParentId());
				DefaultTreeNode parentTreeNode = nodeMap.get(parentId);
				if( parentTreeNode==null )
				{
					parentTreeNode = rootTreeNode;
				}
				
				// 放在父节点里
				parentTreeNode.setChecked(false);				// 凡是有子节点的一律置为false
				parentTreeNode.getChildren().add(thisTreeNode);
			}
			
			// 写到返回流
			ServiceHelper.writeJsonTo(response.getOutputStream(), new DefaultTreeNode[]{rootTreeNode});
		}
		catch( IOException e )
		{
			logger.error(e);
			throw new AppException("查询失败");
		}
	}
	
	
	/**
	 * 
	 */
	@Callable
	@Transactional(readOnly=false)
	public MethodResult saveSysRolePrivilege(String roleId, List<String> privilegeIdList)
	{
		MethodResult result = new MethodResult();
		
		// 先删除这个角色关联的权限
		SysPrivilegeRoleRelationMapper sysPrivilegeRoleRelationMapper = this.sqlSession.getMapper(SysPrivilegeRoleRelationMapper.class);
		sysPrivilegeRoleRelationMapper.deleteByRoleId(roleId);
		
		// 再为这个角色加上权限
		int n = 0;	// 总共添加了n条记录
		for( String privilegeId : privilegeIdList )
		{
			Map<String, Object> privRoleRelation = new LinkedHashMap<String, Object>();
			privRoleRelation.put("id", StringUtil.generateUUID());
			privRoleRelation.put("roleId", roleId);
			privRoleRelation.put("privilegeId", privilegeId);
			n += sysPrivilegeRoleRelationMapper.add(privRoleRelation);
		}
		
		result.put("add_count", n);
		result.status = MethodResult.SUCCESS;
		result.message = "保存成功";
		return result;
	}
	
	
	//--------------------
	
	
	
	
	
	
}


















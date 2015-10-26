package com.zhicloud.op.service.impl;

import java.net.URL;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.transaction.annotation.Transactional;

import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.exception.IllegalDataException;
import com.zhicloud.op.mybatis.mapper.SysPrivilegeMapper;
import com.zhicloud.op.mybatis.mapper.SysPrivilegeRoleRelationMapper;
import com.zhicloud.op.service.PrivilegeService;
import com.zhicloud.op.service.constant.AppConstant;
import com.zhicloud.op.vo.SysPrivilegeVO;

@Transactional(readOnly=true)
public class PrivilegeServiceImpl implements PrivilegeService
{
	
	private static final Logger logger = Logger.getLogger(PrivilegeServiceImpl.class);
	
	//-------------
	
	private SqlSession sqlSession;
	
	public void setSqlSession(SqlSession sqlSession)
	{
		this.sqlSession = sqlSession;
	}
	
	//---------------

	@Transactional(readOnly=false)
	public void load(URL url)
	{
		new LoadPrivilegeConfigTask().load(url);
	}
	
	//---------------

	/*
	 * 用于加载 /META-INF/privilege-config.xml, 读取权限信息
	 */
	public class LoadPrivilegeConfigTask
	{

		private Set<String> privilegeCodeSet = new LinkedHashSet<String>();
		
		private SysPrivilegeMapper sysPrivilegeMapper = PrivilegeServiceImpl.this.sqlSession.getMapper(SysPrivilegeMapper.class);
		private SysPrivilegeRoleRelationMapper sysPrivilegeRoleRelationMapper = PrivilegeServiceImpl.this.sqlSession.getMapper(SysPrivilegeRoleRelationMapper.class);

		@SuppressWarnings("unchecked")
		public void load(URL authConfigURL)
		{
			try
			{
				Document doc = new SAXReader().read(authConfigURL);
				Element root = doc.getRootElement();
				
				// 判断是否需要更新，如果不需要更新，直接返回
				String updateDatabase = root.attributeValue("updateDatabase");
				if( "true".equals(updateDatabase)==false )
				{
					return ;
				}
				
				// 下面开始正式干活
				int orderNum = 1;
				for( Iterator<Element> it = root.elementIterator("privilege"); it.hasNext(); )
				{
					Element element = it.next();
					handlePrivilegeElement(element, orderNum);
					orderNum++;
				}
				
				// 删除一些冗余的菜单
				deleteRedundancyPrivileges();
			}
			catch( Exception e )
			{
				logger.error("", e);
			}
		}

		@SuppressWarnings("unchecked")
		private void handlePrivilegeElement(Element element, int thisOrderNum) throws IllegalDataException
		{
			
			// 获取这个节点的信息
			String privilegeCode  = element.attributeValue("code");
			String privilegeType  = element.attributeValue("type");
			String privilegeName  = element.attributeValue("name");
			Integer privilegeLevel = 1;
			privilegeCodeSet.add(privilegeCode);
			
			// 获取父节点的信息
			Element paElement      = element.getParent();
			String paPrivilegeCode = paElement.attributeValue("code");
			String paPrivilegeId   = null;
			Integer paLevel        = null;
			if( paPrivilegeCode != null )
			{
				SysPrivilegeVO paSysPrivilege = sysPrivilegeMapper.getByPrivilegeCode(paPrivilegeCode);
				if( paSysPrivilege!=null )
				{
					paPrivilegeId  = paSysPrivilege.getId();
					paLevel        = paSysPrivilege.getLevel();
					privilegeLevel = paLevel + 1;
				}
				else
				{
					throw new IllegalDataException("找不到父节点对应的信息");
				}
			}
			
			// 
			SysPrivilegeVO sysPrivilege = sysPrivilegeMapper.getByPrivilegeCode(privilegeCode);
			if( sysPrivilege==null )
			{
				// 不存在,添加这个节点的信息
				Map<String, Object> privilegeData = new LinkedHashMap<String, Object>();
				privilegeData.put("privilegeCode", privilegeCode);
				privilegeData.put("type", ("menu".equals(privilegeType) ? AppConstant.PRIVILEGE_TYPE_MENU : AppConstant.PRIVILEGE_TYPE_FUNCTION));
				privilegeData.put("level", privilegeLevel);
				privilegeData.put("name", privilegeName);
				privilegeData.put("parentId", paPrivilegeId);
				privilegeData.put("sortNum", thisOrderNum);
				privilegeData.put("id", StringUtil.generateUUID());
				sysPrivilegeMapper.addSysPrivilege(privilegeData);
			}
			else
			{
				// 存在,更新这个节点信息
				Map<String, Object> privilegeData = new LinkedHashMap<String, Object>();
				privilegeData.put("privilegeCode", privilegeCode);
				privilegeData.put("type", ("menu".equals(privilegeType) ? AppConstant.PRIVILEGE_TYPE_MENU : AppConstant.PRIVILEGE_TYPE_FUNCTION));
				privilegeData.put("level", privilegeLevel);
				privilegeData.put("name", privilegeName);
				privilegeData.put("parentId", paPrivilegeId);
				privilegeData.put("sortNum", thisOrderNum);
				privilegeData.put("id", sysPrivilege.getId());
				sysPrivilegeMapper.updateSysPrivilegeById(privilegeData);
			}
			
			int orderNum = 1;
			for( Iterator<Element> it = element.elementIterator("privilege"); it.hasNext(); )
			{
				Element subElement = it.next();
				handlePrivilegeElement(subElement, orderNum);
				orderNum++;
			}
		}

		/*
		 * 删除掉冗余的privilege
		 */
		private void deleteRedundancyPrivileges()
		{
			if( privilegeCodeSet.size() == 0 )
			{
				return;
			}
			logger.info("delete delete delete " + privilegeCodeSet);
			sysPrivilegeMapper.deleteByCodesNotIn(privilegeCodeSet.toArray(new String[0]));
			sysPrivilegeRoleRelationMapper.deleteUselessRecords();
		}

	}
}

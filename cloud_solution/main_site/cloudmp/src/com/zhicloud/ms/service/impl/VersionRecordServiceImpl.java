/**
 * Project Name:CloudDeskTopMS
 * File Name:VersionRecordServiceImpl.java
 * Package Name:com.zhicloud.ms.service.impl
 * Date:2015年4月21日下午6:11:32
 * 
 *
*/ 

package com.zhicloud.ms.service.impl;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhicloud.ms.common.util.StringUtil;
import com.zhicloud.ms.constant.AppConstant;
import com.zhicloud.ms.mapper.SysUserMapper;
import com.zhicloud.ms.mapper.TerminalUserMapper;
import com.zhicloud.ms.mapper.VersionRecordMapper;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.IVersionRecordService;
import com.zhicloud.ms.util.MD5;
import com.zhicloud.ms.vo.VersionRecordVO;
 
/**
 * ClassName: VersionRecordServiceImpl 
 * Function:  版本管理
 * date: 2015年4月21日 下午6:11:32 
 *
 * @author sasa
 * @version 
 * @since JDK 1.7
 */
@Transactional(readOnly=false)
@Service("versionRecordService")
public class VersionRecordServiceImpl implements IVersionRecordService {
	
	private static final Logger logger = Logger.getLogger(UserServiceImpl.class);
	@Resource
	private SqlSession sqlSession;

	/**
	 * 查询所有版本
	 * @see com.zhicloud.ms.service.IVersionRecordService#getAllVersion()
	 */
	@Override
	public List<VersionRecordVO> getAllVersion() {
		VersionRecordMapper versionRecordMapper = this.sqlSession.getMapper(VersionRecordMapper.class);
		return versionRecordMapper.getAll(); 
	}

	/**
	 * 新增版本
	 * @see com.zhicloud.ms.service.IVersionRecordService#addVersion(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public MethodResult addVersion(VersionRecordVO version) { 
		
		 

		VersionRecordMapper versionRecordMapper = this.sqlSession.getMapper(VersionRecordMapper.class);
		Map<String, Object> data = new LinkedHashMap<String, Object>();
 		data.put("id", version.getId());
		data.put("versionNumber", version.getVersionNumber());
		data.put("updateInfo", version.getUpdateInfo()); 
		data.put("name", version.getName()); 
		data.put("fsize", version.getFsize()); 
		data.put("createTime",StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS")); 
		Integer result = versionRecordMapper.addVersion(data);
 
		if (result > 0) {
			return new MethodResult(MethodResult.SUCCESS, "添加成功");
		}

		return new MethodResult(MethodResult.FAIL, "添加失败");
	}

	/**
	 * 删除版本
	 * @see com.zhicloud.ms.service.IVersionRecordService#deleteVersion(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public MethodResult deleteVersion(String id) {
		VersionRecordMapper versionRecordMapper = this.sqlSession.getMapper(VersionRecordMapper.class);
		String [] versionIds = id.split(",");
 		Integer result = versionRecordMapper.deleteVersion(versionIds);
		if (result > 0) {
			return new MethodResult(MethodResult.SUCCESS, "删除成功");
		}

		return new MethodResult(MethodResult.FAIL, "删除失败");
	}

	/**
	 * 获取最新的版本号
	 * @see com.zhicloud.ms.service.IVersionRecordService#getLatestVersion()
	 */
	@Override
	public VersionRecordVO getLatestVersion() {
		VersionRecordMapper versionRecordMapper = this.sqlSession.getMapper(VersionRecordMapper.class);
		return versionRecordMapper.getLatestVersion();
	}

	/**
	 * 根据id查询版本
	 * @see com.zhicloud.ms.service.IVersionRecordService#getVersionById(java.lang.String)
	 */
	@Override
	public VersionRecordVO getVersionById(String id) {
		
		VersionRecordMapper versionRecordMapper = this.sqlSession.getMapper(VersionRecordMapper.class);
		return versionRecordMapper.getVersionById(id);
	} 

}


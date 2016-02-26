/**
 * Project Name:CloudDeskTopMS
 * File Name:IVersionRecordService.java
 * Package Name:com.zhicloud.ms.service
 * Date:2015年4月21日下午6:02:03
 * 
 *
*/ 

package com.zhicloud.ms.service; 

import java.util.List; 

import com.zhicloud.ms.remote.MethodResult;
 import com.zhicloud.ms.vo.VersionRecordVO;

/**
 * ClassName: IVersionRecordService 
 * Function:  版本管理
 * date: 2015年4月21日 下午6:02:03 
 *
 * @author sasa
 * @version 
 * @since JDK 1.7
 */
public interface IVersionRecordService {
	
	
	/**
	 * 
	 * getAllVersion:查询所有版本 
	 *
	 * @author sasa
	 * @return List<VersionRecordVO>
	 * @since JDK 1.7
	 */
	public List<VersionRecordVO> getAllVersion();
	/**
	 * 
	 * getLatestVersion:获取最新版本号 
	 *
	 * @author sasa
	 * @return VersionRecordVO
	 * @since JDK 1.7
	 */
	public VersionRecordVO getLatestVersion(String platformType);
	/**
	 * 
	 * getVersionById: 根据id查询版本
	 *
	 * @author sasa
	 * @param id
	 * @return VersionRecordVO
	 * @since JDK 1.7
	 */
	public VersionRecordVO getVersionById(String id);
	/**
	 * 
	 * addVersion:新增版本
	 *
	 * @author sasa
	 * @param request
	 * @param response
	 * @return MethodResult
	 * @since JDK 1.7
	 */
	public MethodResult addVersion(VersionRecordVO version);
	/**
	 * 
	 * deleteVersion:删除版本
	 *
	 * @author sasa
	 * @param request
	 * @param response
	 * @return MethodResult
	 * @since JDK 1.7
	 */
	public MethodResult deleteVersion(String id); 
	
	

}


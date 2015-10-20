/**
 * Project Name:CloudDeskTopMS
 * File Name:VersionRecordMapper.java
 * Package Name:com.zhicloud.ms.mapper
 * Date:2015年4月21日下午6:14:03
 * 
 *
*/ 

package com.zhicloud.ms.mapper; 

import java.util.List;
import java.util.Map;
 
import com.zhicloud.ms.vo.VersionRecordVO;

/**
 * ClassName: VersionRecordMapper 
 * Function: 版本控制数据库操作
 * date: 2015年4月21日 下午6:14:03 
 *
 * @author sasa
 * @version 
 * @since JDK 1.7
 */
public interface VersionRecordMapper {
	
	/**
	 * 
	 * getAll:获取所有的版本
	 *
	 * @author sasa
	 * @return List<VersionRecordVO>
	 * @since JDK 1.7
	 */
	public List<VersionRecordVO> getAll(); 
	/**
	 * 
	 * getLatestVersion:获取最新的版本号 
	 *
	 * @author sasa
	 * @return VersionRecordVO
	 * @since JDK 1.7
	 */
	public VersionRecordVO getLatestVersion();
	/**
	 * 
	 * getVersionById:根据id查询版本 
	 *
	 * @author sasa
	 * @return VersionRecordVO
	 * @since JDK 1.7
	 */
	public VersionRecordVO getVersionById(String id); 
	/**
	 * 
	 * addVersion: 新增版本 
	 *
	 * @author sasa
	 * @param data
	 * @return int
	 * @since JDK 1.7
	 */
	public int addVersion(Map<String, Object> data);
	/**
	 * 
	 * deleteVersion:根据id删除版本
	 *
	 * @author sasa
	 * @param id
	 * @return int
	 * @since JDK 1.7
	 */
	public int deleteVersion(String [] id); 
	/**
	 * 
	 * updateVersion: 更新版本 
	 *
	 * @author sasa
	 * @param data
	 * @return int
	 * @since JDK 1.7
	 */
	public int updateVersion(Map<String, Object> data);

}


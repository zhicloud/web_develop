package com.zhicloud.ms.mapper;

import java.util.List;
import java.util.Map;

import com.zhicloud.ms.vo.HostMigrationVO;

/**
 * ClassName: CloudHostMapper 
 * Function: 用于查询cloud_host_migrateion表数据. 
 * 
 * date: 2015年5月19日 下午16:44:18 
 *
 * @author sasa
 * @version 
 * @since JDK 1.7
 */
public interface HostMigrateionMapper {
	
	/**
	 * 查询所以数据
	 * @return
	 */
	public List<HostMigrationVO> queryHostMigration();
	
	/**
	 * 根据id查询
	 * @return
	 */
	public HostMigrationVO queryById(String id,String time);
	
	/**
	 * 根基Id修改数据
	 * @param id 标示位
	 * @param status 状态
	 * @return
	 */
	public int updateById(String id,String time, int status);
	
	/**
	 * 添加
	 * @param condition
	 * @return
	 */
	public int addHostMigration(Map<String,Object> condition);
	
	public int delById(String id,String time);

}

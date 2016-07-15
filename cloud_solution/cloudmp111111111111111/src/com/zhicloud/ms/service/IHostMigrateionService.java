package com.zhicloud.ms.service;

import java.util.List;
import java.util.Map;

import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.vo.HostMigrationVO;

public interface IHostMigrateionService {

	/**
	 * 查询所以数据
	 * @return
	 */
	public List<HostMigrationVO> getHostMigration();
	
	/**
	 * 根基Id修改数据
	 * @param id 标示位
	 * @param status 状态
	 * @return
	 */
	public int updateHostMigrationById(String id, int status);
	
	/**
	 * 添加
	 * @param vo
	 * @return
	 */
	public int addHostMigration(Map<String,Object> condition);
	
	/**
	 * 删除
	 * @param id 唯一标示id
	 * @return
	 */
	public int delById(String id);
}

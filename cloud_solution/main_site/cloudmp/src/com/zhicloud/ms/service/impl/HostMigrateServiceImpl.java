package com.zhicloud.ms.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.zhicloud.ms.mapper.HostMigrateionMapper;
import com.zhicloud.ms.service.IHostMigrateionService;
import com.zhicloud.ms.service.IOperLogService;
import com.zhicloud.ms.vo.HostMigrationVO;

@Transactional(readOnly=true)
public class HostMigrateServiceImpl implements IHostMigrateionService {
	
	private static final Logger logger = Logger.getLogger(UserServiceImpl.class); 
	
    private SqlSession sqlSession;
    @Resource
    private IOperLogService operLogService;
    
    public SqlSession getSqlSession() {
        return sqlSession;
    }

    public void setSqlSession(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

	@Override
	public List<HostMigrationVO> getHostMigration() {
		HostMigrateionMapper hostMigrateionMapper = this.sqlSession.getMapper(HostMigrateionMapper.class);
		return hostMigrateionMapper.queryHostMigration();
	}

	@Override
	@Transactional(readOnly=false)
	public int updateHostMigrationById(String id,String time, int status) {
		HostMigrateionMapper hostMigrateionMapper = this.sqlSession.getMapper(HostMigrateionMapper.class);
		return hostMigrateionMapper.updateById(id,time, status);
	}

	@Override
	@Transactional(readOnly=false)
	public int addHostMigration(Map<String,Object> condition) {
		HostMigrateionMapper hostMigrateionMapper = this.sqlSession.getMapper(HostMigrateionMapper.class);
		return hostMigrateionMapper.addHostMigration(condition);
	}

	@Override
	@Transactional(readOnly=false)
	public int delById(String id,String time) {
		HostMigrateionMapper hostMigrateionMapper = this.sqlSession.getMapper(HostMigrateionMapper.class);
		return hostMigrateionMapper.delById(id,time);
	}

}

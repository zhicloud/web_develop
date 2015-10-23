package com.zhicloud.ms.service.impl;

import java.util.List;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhicloud.ms.mapper.ResourceStatisticsMapper;
import com.zhicloud.ms.service.ResourceStatisticsService;
import com.zhicloud.ms.vo.ResourceStatisticsVO;
/**
 * @ClassName: ResourceStatisticsServiceImpl
 * @Description: 资源使用情况处理
 * @author 梁绍辉 于 2015年9月15日 下午3:36:01
 */
@Service("resourceStatisticsService")
@Transactional
public class ResourceStatisticsServiceImpl implements ResourceStatisticsService{
    /* 日志 */
    private static final Logger logger = Logger.getLogger(LogServiceImpl.class);
    
    @Resource
    private SqlSession sqlSession;

    /**
     * @Description:添加状态信息
     * @param map 参数
     * @return List<LogVO>
     */
    public int addResourceStatistics(Map<String, Object> map) {
        logger.debug("ResourceStatisticsServiceImpl.addResourceStatistics()");
        ResourceStatisticsMapper mapper = this.sqlSession.getMapper(ResourceStatisticsMapper.class);
        return mapper.addResourceStatistics(map);
    }

    /**
     * @Description:根据条件取得所有状态信息
     * @param map 参数
     * @return List<LogVO>
     */
    public List<ResourceStatisticsVO> queryPage(Map<String, Object> map) {
        logger.debug("ResourceStatisticsServiceImpl.queryPage()");
        ResourceStatisticsMapper mapper = this.sqlSession.getMapper(ResourceStatisticsMapper.class);
        return mapper.queryPage(map);
    }

}

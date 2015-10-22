package com.zhicloud.ms.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhicloud.ms.mapper.ServerComponentMapper;
import com.zhicloud.ms.service.ServerComponentService;
import com.zhicloud.ms.vo.ServerComponentVO;

/**
 * @ClassName: ServerComponentServiceImpl
 * @Description: 资源使用情况处理
 * @author 梁绍辉 于 2015年9月16日 下午3:36:01
 */
@Service("serverComponentService")
@Transactional
public class ServerComponentServiceImpl  implements ServerComponentService{
    /* 日志 */
    private static final Logger logger = Logger.getLogger(LogServiceImpl.class);
    
    @Resource
    private SqlSession sqlSession;   
    
    /**
     * @Description:取得所有组件配置信息
     * @param map 参数
     * @return List<ServerComponentVO>
     */
    public List<ServerComponentVO> queryPage() {
        logger.debug("ResourceStatisticsServiceImpl.queryPage()");
        ServerComponentMapper mapper = this.sqlSession.getMapper(ServerComponentMapper.class);
        return mapper.queryPage();
    }

}

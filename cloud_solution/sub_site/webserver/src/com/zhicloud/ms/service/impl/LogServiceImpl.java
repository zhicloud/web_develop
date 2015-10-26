package com.zhicloud.ms.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhicloud.ms.common.util.StringUtil;
import com.zhicloud.ms.mapper.LogMapper;
import com.zhicloud.ms.service.LogService;
import com.zhicloud.ms.vo.LogVO;

/**
 * @ClassName: LogServiceImpl
 * @Description: 日志业务处理
 * @author 张本缘 于 2015年9月7日 下午3:36:01
 */
@Service("logService")
@Transactional
public class LogServiceImpl implements LogService{
    
    /* 日志 */
    private static final Logger logger = Logger.getLogger(LogServiceImpl.class);
    
    @Resource
    private SqlSession sqlSession;
    
    /**
     * Description: 查询日志记录
     * @param map
     * @return List<LogVO>
     */
    public List<LogVO> queryPage(Map<String, Object> map) {
        logger.debug("LogServiceImpl.queryPage()");
        LogMapper mapper = this.sqlSession.getMapper(LogMapper.class);
        return mapper.queryPage(map);
    }

    /**
     * Description:返回日志记录数
     * @param map 参数
     * @return int
     */
    public int queryPageCount(Map<String, Object> map) {
        logger.debug("LogServiceImpl.queryPageCount()");
        LogMapper mapper = this.sqlSession.getMapper(LogMapper.class);
        return mapper.queryPageCount(map);
    }

    /**
     * Description:保存日志信息
     * @param map 参数
     * @return int
     */
    public int saveLog(Map<String, Object> map) {
        logger.debug("LogServiceImpl.addLog()");
        LogMapper mapper = this.sqlSession.getMapper(LogMapper.class);
        return mapper.addLog(map);
    }
    
    /**
     * @Description:传入动作描述和userid，增加日志
     * @param actiondesc
     * @param userid
     * @return int
     */
    public int addLog(String actiondesc, String userid) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("id", StringUtil.generateUUID());
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        map.put("operatetime", sf.format(new Date()));
        map.put("actiondesc", actiondesc);
        map.put("userid", userid);
        return this.saveLog(map);
    }
}

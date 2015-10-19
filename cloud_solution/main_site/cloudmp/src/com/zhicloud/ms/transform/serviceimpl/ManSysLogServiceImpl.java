package com.zhicloud.ms.transform.serviceimpl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.zhicloud.ms.exception.AppException;
import com.zhicloud.ms.transform.constant.TransformConstant;
import com.zhicloud.ms.transform.mapper.ManSystemLogMapper;
import com.zhicloud.ms.transform.service.ManSysLogService;
import com.zhicloud.ms.transform.vo.ManSystemLogVO;

/**
 * @ClassName: ManSysLogServiceImpl
 * @Description: 日志管理
 * @author 张本缘 于 2015年5月22日 上午10:00:36
 */
public class ManSysLogServiceImpl implements ManSysLogService {
    /**
     * 日志
     */
    public static final Logger logger = Logger.getLogger(ManSysLogServiceImpl.class);

    private SqlSession sqlSession;

    /**
     * @Description:sqlSession注入
     * @param sqlSession
     */
    public void setSqlSession(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }
    
    /**
     * Description:取得所有日志
     * @return List<ManSystemLogVO
     */
    public List<ManSystemLogVO> getAll() {
        ManSystemLogMapper systemLogMapper = this.sqlSession.getMapper(ManSystemLogMapper.class);
        return systemLogMapper.getAll();
    }

    /**
     * Description:取得不同类型的日志信息
     * @param typeid
     * @return List<ManSystemLogVO>
     */
    public List<ManSystemLogVO> getAllByType(String typeid) {
        ManSystemLogMapper systemLogMapper = this.sqlSession.getMapper(ManSystemLogMapper.class);
        return systemLogMapper.getAllByType(Integer.parseInt(typeid));
    }
    
    /**
     * Description:添加日志信息
     * @param parameter
     * @return String
     */
    @Transactional(readOnly = false)
    public String addSysLog(Map<String, Object> parameter) {
        logger.debug("ManSysLogServiceImpl.addSysLog()");
        try {
            ManSystemLogMapper systemLogMapper = this.sqlSession.getMapper(ManSystemLogMapper.class);
            systemLogMapper.addSystemLog(parameter);
            systemLogMapper.deleteLogsWithoutDays(TransformConstant.logkeep);
            return TransformConstant.success;
        } catch (Exception e) {
            logger.error(e);
            throw new AppException("添加失败");
        }
    }

}

package com.zhicloud.ms.service.impl;

import com.zhicloud.ms.mapper.TimerInfoMapper;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.ITimerInfoService;
import com.zhicloud.ms.vo.TimerInfoVO;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger; 
import org.springframework.transaction.annotation.Transactional;
 
import java.util.LinkedHashMap;
/**
 * 
* @ClassName: TimerInfoServiceImpl 
* @Description: 定时器信息操作
* @author sasa
* @date 2015年7月29日 上午9:26:52 
*
 */ 
@Transactional(readOnly=false)
public class TimerInfoServiceImpl implements ITimerInfoService {

     private SqlSession sqlSession;
     
     
 
    public void setSqlSession(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    } 
    /**
     * 根据key查询定时器，只返回一个结果
    * <p>Title: queryTimerInfoByKey</p> 
    * <p>Description: </p> 
    * @param key
    * @return 
    * @see com.zhicloud.ms.service.ITimerInfoService#queryTimerInfoByKey(java.lang.String)
     */
    @Override
    public TimerInfoVO queryTimerInfoByKey(String key) {
        TimerInfoMapper timerInfoMapper = this.sqlSession.getMapper(TimerInfoMapper.class);
        TimerInfoVO timerInfoVO = timerInfoMapper.queryByKey(key);
         return timerInfoVO;
    }
    /**
     * 新增或者是更新定时器的信息
    * <p>Title: insertOrUpdateBackUpTimerInfo</p> 
    * <p>Description: </p> 
    * @param timer
    * @return 
    * @see com.zhicloud.ms.service.ITimerInfoService#insertOrUpdateBackUpTimerInfo(com.zhicloud.ms.vo.TimerInfoVO)
     */
    @Transactional(readOnly=false)
    @Override
    public MethodResult insertOrUpdateBackUpTimerInfo(TimerInfoVO timer) {
        TimerInfoMapper timerInfoMapper = this.sqlSession.getMapper(TimerInfoMapper.class);
        
        LinkedHashMap<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("id", timer.getId());
        data.put("mode", timer.getMode()); 
        data.put("disk", timer.getDisk()); 
        data.put("week", timer.getWeek());
        data.put("day", timer.getDay()); 
        data.put("key", timer.getKey());
        data.put("hour", timer.getHour());
        data.put("minute", timer.getMinute()); 
        data.put("second", timer.getSecond());
        data.put("status", timer.getStatus()); 
        data.put("type", timer.getType()); 
        data.put("createTime", timer.getCreateTime()); 
        timerInfoMapper.insertOrUpdate(data);
        return new MethodResult(MethodResult.SUCCESS,"保存成功");
    }

    /**
     * @function 新增定时器
     * @author 张翔
     * @param timer
     * @return
     */
    @Override
    public MethodResult insertOrUpdateTimerInfo(TimerInfoVO timer) {

        TimerInfoMapper timerInfoMapper = this.sqlSession.getMapper(TimerInfoMapper.class);

        LinkedHashMap<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("id", timer.getId());
        data.put("mode", timer.getMode());
        data.put("disk", timer.getDisk());
        data.put("week", timer.getWeek());
        data.put("day", timer.getDay());
        data.put("key", timer.getKey());
        data.put("hour", timer.getHour());
        data.put("minute", timer.getMinute());
        data.put("second", timer.getSecond());
        data.put("second", timer.getSecond());
        data.put("status", timer.getStatus());
        data.put("type", timer.getType());
        data.put("createTime", timer.getCreateTime());
        timerInfoMapper.insertOrUpdate(data);
        return new MethodResult(MethodResult.SUCCESS,"保存成功");
    }

}

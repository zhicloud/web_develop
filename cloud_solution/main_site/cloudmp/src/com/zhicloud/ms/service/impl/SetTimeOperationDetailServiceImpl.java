package com.zhicloud.ms.service.impl;

import com.zhicloud.ms.mapper.CloudHostMapper;
import com.zhicloud.ms.mapper.SetTimeOperationDetailMapper;
import com.zhicloud.ms.service.ISetTimeOperationDetailService;
import com.zhicloud.ms.util.StringUtil;
import com.zhicloud.ms.vo.CloudHostVO;
import com.zhicloud.ms.vo.SetTimeOperationDetailVO;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 
* @ClassName: SetTimeBackDetailServiceImpl 
* @Description: 定是备份记录 
* @author sasa
* @date 2015年7月30日 下午3:01:34 
*
 */
//@Service("setTimeOperationDetailService")
@Transactional(readOnly=true)
public class SetTimeOperationDetailServiceImpl implements
    ISetTimeOperationDetailService {

    private static final Logger logger = Logger.getLogger(SetTimeOperationDetailServiceImpl.class);
    private SqlSession sqlSession;


    public SqlSession getSqlSession() {
        return sqlSession;
    }

    public void setSqlSession(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    /**
     * 新增定时备份记录
    * <p>Title: insertDetail</p>
    * <p>Description: </p>
    * @return
     */
    @Transactional(readOnly=false)
    public int insertDetail(String realHostId,Integer status, Integer type){
        try{
            SetTimeOperationDetailMapper setTimeOperationDetailMapper = this.sqlSession.getMapper(SetTimeOperationDetailMapper.class);
            CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
            CloudHostVO host = cloudHostMapper.getByRealHostId(realHostId);
            //只限定为云桌面主机
            if(host.getType() == 1){
                Map<String,Object> data = new LinkedHashMap<String,Object>();
                data.put("id", StringUtil.generateUUID());
                data.put("displayName", host.getDisplayName());
                data.put("type", type);
                data.put("hostId", host.getId());
                data.put("status", status);
                data.put("backUpTime",  StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
                data.put("createTime",  StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
                return setTimeOperationDetailMapper.insertDetail(data);
            }else{
                logger.info("SetTimeBackDetailServiceImpl.insertDetail:["+realHostId+"]主机不是桌面云主机");
                return 1;
            }
        } catch (Exception e) {
           e.printStackTrace();
        }
        return 1;

    }

    /**
     * 查询所有的历史开关机记录，根据时间降序排序
    * <p>Title: getAllDetail</p>
    * <p>Description: </p>
    * @return
     */
    @Override
    public List<SetTimeOperationDetailVO> getAllDetail() {
        try{
            SetTimeOperationDetailMapper setTimeOperationDetailMapper = this.sqlSession.getMapper(SetTimeOperationDetailMapper.class);
            return setTimeOperationDetailMapper.getAllDetail();
        } catch (Exception e) {
            e.printStackTrace();
        }
       return null;
    }

    @Override public Long getTimeInterval(String startTime, String endTime) {
        String format="HH:mm";
        SimpleDateFormat sd = new SimpleDateFormat(format);
        long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
        long nh = 1000 * 60 * 60;// 一小时的毫秒数
        long nm = 1000 * 60;// 一分钟的毫秒数
        long diff, start, end;

        try {
            start = sd.parse(startTime).getTime();
            end = sd.parse(endTime).getTime();

            if (start > end) {
                diff = start - end;
            } else {
                diff = end - start;
            }
            long min = diff / (60 * 1000);

            return min;// 计算差多少分钟

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0l;
    }

}

package com.zhicloud.ms.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhicloud.ms.common.util.StringUtil;
import com.zhicloud.ms.mapper.TerminalInformationPushMapper;
import com.zhicloud.ms.service.ITerminalInformationPushService;
import com.zhicloud.ms.vo.TerminalInformationPushVO;
@Service("terminalInformationPushService")
@Transactional(readOnly=true)
public class TerminalInformationPushServiceImpl implements ITerminalInformationPushService {
    @Resource
    private SqlSession sqlSession;

    /**
     * 查询所有消息
    * <p>Title: queryAll</p> 
    * <p>Description: </p> 
    * @return 
    * @see com.zhicloud.ms.service.ITerminalInformationPushService#queryAll()
     */
    @Override
    public List<TerminalInformationPushVO> queryAll() {
        return this.sqlSession.getMapper(TerminalInformationPushMapper.class).getAll();
    }
    

    
    /**
     * 根据时间查询不同的数据
    * <p>Title: queryAllByTime</p> 
    * <p>Description: </p> 
    * @param time
    * @return 
    * @see com.zhicloud.ms.service.ITerminalInformationPushService#queryAllByTime(java.lang.String)
     */
    @Override
    public List<TerminalInformationPushVO> queryAllByTime(String time) {
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        
        if("1".equals(time)){
            data.put("startTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS").substring(0, 8)+"000000000");
            data.put("endTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
        }else if("2".equals(time)){
            Calendar nowC = Calendar.getInstance();  
            nowC.add(Calendar.DAY_OF_MONTH, -1); 
            data.put("startTime", StringUtil.dateToString(nowC.getTime(), "yyyyMMddHHmmssSSS").substring(0, 8)+"000000000");
            data.put("endTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS").substring(0, 8)+"000000000");
            
        }else if("3".equals(time)){
            Calendar nowC = Calendar.getInstance();  
            nowC.add(Calendar.DAY_OF_MONTH, -5); 
            data.put("startTime", StringUtil.dateToString(nowC.getTime(), "yyyyMMddHHmmssSSS"));
            data.put("endTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));            
        }else if("4".equals(time)){
            Calendar nowC = Calendar.getInstance();  
            nowC.add(Calendar.DAY_OF_MONTH, -5); 
            data.put("endTime", StringUtil.dateToString(nowC.getTime(), "yyyyMMddHHmmssSSS"));
         }
        return this.sqlSession.getMapper(TerminalInformationPushMapper.class).getAllByTime(data);
     }

    /**
     * 根据群组id和time查询应该推送给盒子的信息
    * <p>Title: queryInfomationByGroupIdAndTime</p> 
    * <p>Description: </p> 
    * @param groupId
    * @param time
    * @return 
    * @see com.zhicloud.ms.service.ITerminalInformationPushService#queryInfomationByGroupIdAndTime(java.lang.String, java.lang.String)
     */
    @Override
    public List<TerminalInformationPushVO> queryInfomationByCondition(String groupId, String time, String region,
            String industry) {
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("groupId", groupId);
        data.put("time", time);
        data.put("region", region);
        data.put("industry", industry);
        return this.sqlSession.getMapper(TerminalInformationPushMapper.class).queryInfomationBygroupIdAndTime(data);
    }

    /**
     * 批量删除
    * <p>Title: deleteInformation</p> 
    * <p>Description: </p> 
    * @param ids
    * @return 
    * @see com.zhicloud.ms.service.ITerminalInformationPushService#deleteInformation(java.util.List)
     */
    @Transactional(readOnly=false)
    @Override
    public Integer deleteInformation(List<String> ids) {
 
        return  this.sqlSession.getMapper(TerminalInformationPushMapper.class).deleteInfomationByIds(ids.toArray(new String[ids.size()]));
    }

    /**
     * 新增消息
    * <p>Title: addInfomation</p> 
    * <p>Description: </p> 
    * @param vo
    * @return 
    * @see com.zhicloud.ms.service.ITerminalInformationPushService#addInfomation(com.zhicloud.ms.vo.TerminalInformationPushVO)
     */
    @Transactional(readOnly=false)
    @Override
    public Integer addInfomation(TerminalInformationPushVO vo) {
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("id", StringUtil.generateUUID());
        data.put("groupId", vo.getGroupId());
        data.put("title", vo.getTitle());
        data.put("content", vo.getContent());
        data.put("createTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
        data.put("region", vo.getRegion());
        data.put("industry", vo.getIndustry());
        return  this.sqlSession.getMapper(TerminalInformationPushMapper.class).insertInfomation(data);
    }

}

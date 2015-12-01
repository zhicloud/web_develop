package com.zhicloud.ms.service.impl;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.zhicloud.ms.constant.AppConstant;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger; 
import org.springframework.transaction.annotation.Transactional;

import com.zhicloud.ms.mapper.CloudHostMapper;
import com.zhicloud.ms.mapper.BackUpDetailMapper; 
import com.zhicloud.ms.service.IBackUpDetailService;
import com.zhicloud.ms.util.StringUtil;
import com.zhicloud.ms.vo.CloudHostVO;
import com.zhicloud.ms.vo.BackUpDetailVO;
/**
 * 
* @ClassName: SetTimeBackDetailServiceImpl 
* @Description: 定是备份记录 
* @author sasa
* @date 2015年7月30日 下午3:01:34 
*
 */
//@Service("backUpDetailService")
@Transactional(readOnly=false)
public class BackUpDetailServiceImpl implements IBackUpDetailService {
    
    private static final Logger logger = Logger.getLogger(BackUpDetailServiceImpl.class); 
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
    * @param condition
    * @return 
    * @see com.zhicloud.ms.service.IBackUpDetailService#insertDetail(java.util.Map)
     */
    @Transactional(readOnly=false)
    public int insertDetail(String realHostId,Integer status, Integer type,Integer mode,Integer disk){
        BackUpDetailMapper setTimeBackUpDetailMapper = this.sqlSession.getMapper(BackUpDetailMapper.class);
        CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
        CloudHostVO host = cloudHostMapper.getByRealHostId(realHostId); 
        Map<String,Object> data = new LinkedHashMap<String,Object>();
        data.put("id", StringUtil.generateUUID());
        data.put("displayName", host.getDisplayName());
        data.put("type", type);
        data.put("hostId", host.getId());
        data.put("status", status);
        data.put("mode", mode);
        data.put("disk", disk);
//        data.put("backUpTime",  StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
        data.put("createTime",  StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
        return setTimeBackUpDetailMapper.insertDetail(data); 
        
    }

    /**
     * 查询所有的历史备份记录，根据时间降序排序
    * <p>Title: getAllDetail</p> 
    * <p>Description: </p> 
    * @return 
    * @see com.zhicloud.ms.service.IBackUpDetailService#getAllDetail()
     */
    @Override
    public List<BackUpDetailVO> getAllDetail(Integer type) {
        BackUpDetailMapper setTimeBackUpDetailMapper = this.sqlSession.getMapper(BackUpDetailMapper.class);
        return setTimeBackUpDetailMapper.getAllDetail(type);
    }
    /**
     * 根据realhostid和oldstatus 更新新的status
    * <p>Title: updateDetail</p> 
    * <p>Description: </p> 
    * @param realHostId
    * @param type
    * @param oldstatus
    * @param newstatus
    * @return 
    * @see com.zhicloud.ms.service.IBackUpDetailService#updateDetail(java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Integer)
     */
    @Override
    public int updateDetail(String realHostId, Integer oldstatus, Integer newstatus) {
        BackUpDetailMapper setTimeBackUpDetailMapper = this.sqlSession.getMapper(BackUpDetailMapper.class);
        CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
        CloudHostVO host = cloudHostMapper.getByRealHostId(realHostId);
         Map<String,Object> data = new LinkedHashMap<String,Object>(); 
        data.put("hostId", host.getId());
        data.put("oldstatus", oldstatus);
        data.put("newstatus", newstatus);
        if(oldstatus == AppConstant.BACK_UP_DETAIL_STATUS_BACKINGUP){
            data.put("backUpTime",  StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
        }
         return setTimeBackUpDetailMapper.updateDetailStatus(data);
        
    }

    /**
     * 根据主机id查询最新的可用备份信息
    * <p>Title: getLastAvailableBackUp</p> 
    * <p>Description: </p> 
    * @param hostId
    * @return 
    * @see com.zhicloud.ms.service.IBackUpDetailService#getLastAvailableBackUp(java.lang.String)
     */
    @Override
    public BackUpDetailVO getLastAvailableBackUp(String hostId) {
        BackUpDetailMapper setTimeBackUpDetailMapper = this.sqlSession.getMapper(BackUpDetailMapper.class);
        return setTimeBackUpDetailMapper.getLastAvailableBackUp(hostId);
    }

}

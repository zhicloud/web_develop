package com.zhicloud.ms.service.impl;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhicloud.ms.mapper.BoxRealInfoMapper;
import com.zhicloud.ms.service.IBoxRealInfoService;
import com.zhicloud.ms.util.StringUtil;
import com.zhicloud.ms.vo.BoxRealInfoVO;
/**
 * 
* @ClassName: BoxRealInfoServiceImpl 
* @Description: 盒子真实使用信息业务层 
* @author sasa
* @date 2015年8月6日 下午5:20:49 
*
 */
@Service("boxRealInfoService")
@Transactional(readOnly=false)
public class BoxRealInfoServiceImpl implements IBoxRealInfoService {
    private static final Logger logger = Logger.getLogger(UserServiceImpl.class);
    @Resource
    private SqlSession sqlSession;
    /**
     * 新增或者是更新盒子的使用信息
    * <p>Title: addOrUpdateBoxInfo</p> 
    * <p>Description: </p> 
    * @param info
    * @return 
    * @see com.zhicloud.ms.service.IBoxRealInfoService#addOrUpdateBoxInfo(com.zhicloud.ms.transform.vo.BoxRealInfoVO)
     */
    @Override
    public int addOrUpdateBoxInfo(BoxRealInfoVO info) {
        BoxRealInfoMapper boxRealInfoMapper = this.sqlSession.getMapper(BoxRealInfoMapper.class);
        Map<String,Object> data = new LinkedHashMap<String,Object>();
        data.put("mac", info.getMac());
        data.put("userId", info.getUserId());
        data.put("userName", info.getUserName());
        data.put("gateway", info.getGateway());
        data.put("hardwareVersion", info.getHardwareVersion());
        data.put("softwareVersion", info.getSoftwareVersion());
        data.put("ip", info.getIp());
        data.put("lastAliveTime", info.getLastAliveTime());
        data.put("lastLoginTime", info.getLastLoginTime());
        data.put("lastLogoutTime", info.getLastLogoutTime());
        return boxRealInfoMapper.addOrUpdateInfo(data); 
        
        //
    }
    /**
     * 获取所有的信息
    * <p>Title: getAllInfo</p> 
    * <p>Description: </p> 
    * @return 
    * @see com.zhicloud.ms.service.IBoxRealInfoService#getAllInfo()
     */
    @Override
    public List<BoxRealInfoVO> getAllInfo() {
        BoxRealInfoMapper boxRealInfoMapper = this.sqlSession.getMapper(BoxRealInfoMapper.class);
        return boxRealInfoMapper.queryAllInfo();

    }

    @Override public List<BoxRealInfoVO> getAllInfo(Map<String, Object> condition) {
        return this.sqlSession.getMapper(BoxRealInfoMapper.class).queryAllInfoWithConditions(condition);
    }

    /**
     * 根据mac地址查询信息 
    * <p>Title: getInfoByMac</p> 
    * <p>Description: </p> 
    * @param mac
    * @return 
    * @see com.zhicloud.ms.service.IBoxRealInfoService#getInfoByUserId(java.lang.String)
     */
    @Override
    public BoxRealInfoVO getInfoByUserId(String userId) {
        BoxRealInfoMapper boxRealInfoMapper = this.sqlSession.getMapper(BoxRealInfoMapper.class);
        return boxRealInfoMapper.queryByUserId(userId);
    }
    
    /**
    * 计算每个盒子的在线时间。 
    * <p>Title: getInfoByMac</p> 
    * <p>Description: </p> 
    * @param mac
    * @return 
    * @see com.zhicloud.ms.service.IBoxRealInfoService#getInfoByUserId(java.lang.String)
     */   
	@Override
	public void CumulativeOnLineTime() {
		 BoxRealInfoMapper boxRealInfoMapper = this.sqlSession.getMapper(BoxRealInfoMapper.class);
	     Date now = new Date();
		 List<BoxRealInfoVO> list =  boxRealInfoMapper.queryOnlineInfo();
		 for (BoxRealInfoVO o :list){
			 long diff = now.getTime() - o.getLastAliveDate().getTime();   
			 long minute = diff / (1000 * 60); //计算上次心跳时间和当前时间差
			 if( minute <= 15){ //判断为在线+1 分钟
			      Map<String,Object> data = new LinkedHashMap<String,Object>();
			      data.put("userId", o.getUserId());
			      data.put("cumulativeOnlineTime", 1000*60);
			      boxRealInfoMapper.updateCumulativeOnlineTimeByUserId(data);
			 } 
			 if (diff>(16*60*1000)){ //掉线则写入离线时间16钟后算。
				 Map<String,Object> data = new LinkedHashMap<String,Object>();
				 data.put("userId", o.getUserId());
				 data.put("last_logout_time",StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
				 boxRealInfoMapper.updateLogoutTimeByUserId(data); 
			 }
		 }
         logger.info("Cumulative online time");
	}

}

package com.zhicloud.ms.service.impl;

import com.zhicloud.ms.app.cache.box.BoxRealInfoCache;
import com.zhicloud.ms.app.cache.box.BoxRealInfoCacheManager;
import com.zhicloud.ms.common.util.StringUtil;
import com.zhicloud.ms.mapper.BoxRealInfoMapper;
import com.zhicloud.ms.service.IBoxRealInfoService;
import com.zhicloud.ms.vo.BoxRealInfoVO;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

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
    * @see com.zhicloud.ms.service.IBoxRealInfoService#addOrUpdateBoxInfo(com.zhicloud.ms.vo.BoxRealInfoVO)
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
        BoxRealInfoCache cache = BoxRealInfoCacheManager.singleton().getCache();
        BoxRealInfoVO[] boxRealInfoVOs = cache.getAll();
        //缓存中有值,先写入数据库
        if (boxRealInfoVOs.length > 0) {
            boxRealInfoMapper.updateBoxInfoBatch(Arrays.asList(boxRealInfoVOs));
        }

        return boxRealInfoMapper.queryAllInfo();

    }

    @Override public List<BoxRealInfoVO> getAllInfo(Map<String, Object> condition) {

        BoxRealInfoMapper boxRealInfoMapper = this.sqlSession.getMapper(BoxRealInfoMapper.class);
        BoxRealInfoCache cache = BoxRealInfoCacheManager.singleton().getCache();
        BoxRealInfoVO[] boxRealInfoVOs = cache.getAll();
        List<BoxRealInfoVO> list = Arrays.asList(boxRealInfoVOs);

        //缓存中有值,先写入数据库
        if (boxRealInfoVOs.length > 0) {
            boxRealInfoMapper.updateBoxInfoBatch(list);
            cache.removeAll();
        }
        return boxRealInfoMapper.queryAllInfoWithConditions(condition);
    }

    /**
     * 根据mac地址查询信息 
    * <p>Title: getInfoByMac</p> 
    * <p>Description: </p> 
    * @param userId
    * @return 
    * @see com.zhicloud.ms.service.IBoxRealInfoService#getInfoByUserId(java.lang.String)
     */
    @Override
    public BoxRealInfoVO getInfoByUserId(String userId) {
        BoxRealInfoMapper boxRealInfoMapper = this.sqlSession.getMapper(BoxRealInfoMapper.class);
        BoxRealInfoCache cache = BoxRealInfoCacheManager.singleton().getCache();
        BoxRealInfoVO[] boxRealInfoVOs = cache.getAll();
        //缓存中有值,先写入数据库
        if (boxRealInfoVOs.length > 0) {
            boxRealInfoMapper.updateBoxInfoBatch(Arrays.asList(boxRealInfoVOs));
        }
        return boxRealInfoMapper.queryByUserId(userId);
    }
    
    /**
    * 计算每个盒子的在线时间。 
    * <p>Title: getInfoByMac</p> 
    * <p>Description: </p> 
    * @return
    * @see com.zhicloud.ms.service.IBoxRealInfoService#CumulativeOnLineTime()
     */   
	@Override
	public void CumulativeOnLineTime() {
      BoxRealInfoMapper boxRealInfoMapper = this.sqlSession.getMapper(BoxRealInfoMapper.class);
      Date now = new Date();
      List<BoxRealInfoVO> list =  boxRealInfoMapper.queryOnlineInfo();
      BoxRealInfoCache cache = BoxRealInfoCacheManager.singleton().getCache();

		 for (BoxRealInfoVO o :list){
			 long diff = now.getTime() - o.getLastAliveDate().getTime();   
			 long minute = diff / (1000 * 60); //计算上次心跳时间和当前时间差
			 if( minute <= 15){ //判断为在线+1 分钟

           long cumulativeOnlineTime =  o.getCumulativeOnlineTime() + Long.valueOf(1000 * 60);
           o.setCumulativeOnlineTime(cumulativeOnlineTime);
           cache.put(o);
			 } 
			 if (diff>(16*60*1000)){ //掉线则写入离线时间16钟后算。
           o.setLastLoginTime(StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
           cache.put(o);
			 }
		 }
         logger.info("Cumulative online time");
	}

    @Override public void CumulativeOnLineTimeBatch() {

        BoxRealInfoCache cache = BoxRealInfoCacheManager.singleton().getCache();
        BoxRealInfoVO[] list = cache.getAll();

        List<BoxRealInfoVO> boxRealInfoVOs = Arrays.asList(list);

        //批量更新盒子信息
        int n = this.sqlSession.getMapper(BoxRealInfoMapper.class).updateBoxInfoBatch(boxRealInfoVOs);

        if (n > 0) {
            cache.removeAll();
        }
        logger.info("Cumulative online time in batch");

    }

}

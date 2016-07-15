package com.zhicloud.ms.service;

import com.zhicloud.ms.vo.BoxRealInfoVO;

import java.util.List;
import java.util.Map;

 
public interface IBoxRealInfoService {
    /**
     * 
    * @Title: addOrUpdateBoxInfo 
    * @Description: 新增或者是修改盒子的使用信息 
    * @param @param info
    * @param @return      
    * @return int     
    * @throws
     */
    public int addOrUpdateBoxInfo(BoxRealInfoVO info);
    /**
     * 
    * @Title: getAllInfo 
    * @Description: 获取所有的真实使用信息 
    * @param @return      
    * @return List<BoxRealInfoVO>     
    * @throws
     */
    public List<BoxRealInfoVO> getAllInfo();

    /**
     * @Title: getAllInfo
     * @Description: 根据多个参数获取所有的真实使用信息
     * @author 张翔
     * @param condition
     * @return
     */
    public List<BoxRealInfoVO> getAllInfo(Map<String, Object> condition);
    /**
     * 
    * @Title: getInfoByMac 
    * @Description: 根据mac地址查询盒子信息
    * @param @param userId
    * @param @return      
    * @return BoxRealInfoVO     
    * @throws
     */
    public BoxRealInfoVO getInfoByUserId(String userId);
    
    /**
     * 
    * @Title: CumulativeOnLineTime 
    * @Description: 累算在线时长
    * @throws
     */
    public void CumulativeOnLineTime();

    /**
     * @author 张翔
     * @Title: CumulativeOnLineTimeBatch
     * @Description: 累算在线时长批处理
     * @throws
     */
    public void CumulativeOnLineTimeBatch();

}

package com.zhicloud.ms.mapper;

import com.zhicloud.ms.vo.BoxRealInfoVO;

import java.util.List;
import java.util.Map;

 /**
 * 
* @ClassName: BoxRealInfoMapper 
* @Description: 盒子真实使用信息操作 
* @author sasa
* @date 2015年8月6日 下午4:19:32 
*
 */
public interface BoxRealInfoMapper {
    /**
     * 
    * @Title: addOrUpdateInfo 
    * @Description: 新增或者更新盒子的使用信息 
    * @param @param condition
    * @param @return      
    * @return int     
    * @throws
     */
    public int addOrUpdateInfo(Map<String,Object> condition);
    /**
     * 
    * @Title: queryAllInfo 
    * @Description: 查询所有的盒子使用信息 
    * @param @return      
    * @return List<BoxRealInfoVO>     
    * @throws
     */
    public List<BoxRealInfoVO> queryAllInfo();


     /**
      *
      * @Title: queryAllInfo
      * @author 张翔
      * @Description: 根据多个条件查询所有的盒子使用信息
      * @return List<BoxRealInfoVO>
      * @throws
      */
     public List<BoxRealInfoVO> queryAllInfoWithConditions(Map<String, Object> condition);
    /**
     * 
    * @Title: queryByMac 
    * @Description:根据mac地址查询信息
    * @param @param mac
    * @param @return      
    * @return BoxRealInfoVO     
    * @throws
     */
    public BoxRealInfoVO queryByUserId(String mac);
    
    /**
     * 
    * @Title: updateCumulativeOnlineTimeByUserId 
    * @Description: 刷新累计在线时长
    * @param @param Map<String,Object> condition
    * @param @return      
    * @return int     
    * @throws
     */
    public int updateCumulativeOnlineTimeByUserId(Map<String,Object> condition);

    /**
     * 
    * @Title: updateCumulativeOnlineTimeByUserId 
    * @Description: 写入掉线时间
    * @param @param Map<String,Object> condition
    * @param @return      
    * @return int     
    * @throws
     */
    public int updateLogoutTimeByUserId(Map<String,Object> condition);
    
    /**
     * 
    * @Title: queryOnlineInfo 
    * @Description: 查询所有的在线盒子使用信息 
    * @param @return      
    * @return List<BoxRealInfoVO>     
    * @throws
     */
    public List<BoxRealInfoVO> queryOnlineInfo();
}

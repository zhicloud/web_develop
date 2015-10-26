package com.zhicloud.ms.service;

import java.util.List;

import com.zhicloud.ms.vo.ResUsageVO;
import com.zhicloud.ms.vo.BcastVO;
import com.zhicloud.ms.vo.NetVO;
import com.zhicloud.ms.vo.ServiceVO;
import com.zhicloud.ms.vo.ServerVO;

/**
 * @Description: 资源信息处理
 * @author 梁绍辉 于 2015年9月9日 下午1:25:19
 */
public interface StateService {
    /**
     * @Description:获取使用率
     * @return ResUsageVO
     */
    public ResUsageVO queryUsage();
    
    /**
     * @Description:获取服务信息
     * @return List<ServiceVO>
     */
    public List<ServiceVO> queryService();
   
    /**
     * @Description:获取广播信息
     * @return List<BcastVO>
     */
    public List<BcastVO> queryBcast();
  
    /**
     * @Description:获取网络信息
     * @return List<NetVO>
     */
    public List<NetVO> queryNet();    

    /**
     * @Description:获取服务器情况
     * @return ServerVO
     */
    public ServerVO queryServer();
}

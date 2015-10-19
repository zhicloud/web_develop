package com.zhicloud.ms.service;

import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.vo.QosVO;

import java.util.List;
import java.util.Map;

/**
 * @description Qos service接口
 * @author 张翔
 */
public interface IQosService {

    /**
     * @function 获取全部Qos规则
     * @param type 1：云桌面 2：云服务器 3：专属云
     * @return 全部Qos规则
     */
    public List<QosVO> getAll(int type);

    /**
     * @function 检测Qos规则是否重名
     * @param condition
     * @return 全部是否存在该Qos规则
     */
    public MethodResult checkName(Map<String, Object> condition);

    /**
     * @function 新增Qos规则
     * @param parameter Qos规则
     * @return 该方法执行结果（成功或者失败）
     */
    public MethodResult addQos(Map<String, Object> parameter);

    /**
     * @function 移除Qos规则
     * @param ids Qos规则ids
     * @return 该方法执行结果（成功或者失败）
     */
    public MethodResult removeQos(List<String> ids);

}

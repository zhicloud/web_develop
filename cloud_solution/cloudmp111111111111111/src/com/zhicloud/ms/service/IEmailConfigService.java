package com.zhicloud.ms.service;


import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.vo.EmailConfigVO;

import java.util.List;
import java.util.Map;

/**
 * @author 张翔
 * @function 邮件配置Service
 */
public interface IEmailConfigService {

    /**
     * @function 获取全部配置
     * @return
     */
    public List<EmailConfigVO> getAllConfig();

    /**
     * @function 获取制定id的配置
     * @param id 配置id
     * @return
     */
    public EmailConfigVO getConfigById(String id);

    /**
     * @function 检测配置是否重名
     * @param name 配置名
     * @return
     */
    public MethodResult checkConfigName(String name);

    /**
     * @function 新增邮件配置
     * @param parameter
     * @return
     */
    public MethodResult addConfig(Map<String, Object> parameter);

    /**
     * @function 修改邮件配置
     * @param parameter
     * @return
     */
    public MethodResult modifyConfigById(Map<String, Object> parameter);

    /**
     * @function 删除邮件配置
     * @param ids
     * @return
     */
    public MethodResult removeConfigByIds(List<?> ids);
}

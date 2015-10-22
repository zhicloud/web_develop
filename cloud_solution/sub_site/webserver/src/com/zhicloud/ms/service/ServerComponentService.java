package com.zhicloud.ms.service;

import java.util.List;
import java.util.Map;

import com.zhicloud.ms.vo.ServerComponentVO;

public interface ServerComponentService {
    /**
     * @Description:取得所有组件配置信息
     * @return List<ServerComponentVO>
     */
    public List<ServerComponentVO> queryPage();
}

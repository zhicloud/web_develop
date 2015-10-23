package com.zhicloud.ms.mapper;

import java.util.List;
import java.util.Map;

import com.zhicloud.ms.vo.ServerComponentVO;

public interface ServerComponentMapper {
    /**
     * @Description:取得所有组件配置信息
     * @param map 参数
     * @return List<ServerComponentVO>
     */
    public List<ServerComponentVO> queryPage();
}

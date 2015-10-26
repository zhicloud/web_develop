package com.zhicloud.ms.service;

import java.util.List;
import java.util.Map;

import com.zhicloud.ms.vo.UserVO;

public interface LoginService {
    
    /**
     * @Description:根据条件取得所有用户信息
     * @param map 参数
     * @return List<LogVO>
     */
    public List<UserVO> queryPage(Map<String, Object> map);
    
    /**
     * @Description:根据条件获取用户的数量
     * @param map 参数
     * @return int
     */
    public int queryPageCount(Map<String, Object> map);
    /**
     * 获取机器MAC地址
     * @return
     */
    public String getMAC();
}

package com.zhicloud.ms.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.hyperic.sigar.NetFlags;
import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.Sigar;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhicloud.ms.mapper.UserMapper;
import com.zhicloud.ms.service.LoginService;
import com.zhicloud.ms.vo.UserVO;

/**
 * @ClassName: LogServiceImpl
 * @Description: 登录业务处理
 * @author 张本缘 于 2015年9月7日 下午3:36:01
 */
@Service("loginService")
@Transactional
public class LoginServiceImpl implements LoginService{
    
    /* 日志 */
    private static final Logger logger = Logger.getLogger(LoginServiceImpl.class);
    
    @Resource
    private SqlSession sqlSession;
    
    /**
     * Description: 查询用户记录
     * @param map
     * @return List<UserVO>
     */
    public List<UserVO> queryPage(Map<String, Object> map) {
        logger.debug("LoginServiceImpl.queryPage()");
        UserMapper mapper = this.sqlSession.getMapper(UserMapper.class);
        return mapper.queryPage(map);
    }

    /**
     * Description:返回用户记录数
     * @param map 参数
     * @return int
     */
    public int queryPageCount(Map<String, Object> map) {
        logger.debug("LoginServiceImpl.queryPageCount()");
        UserMapper mapper = this.sqlSession.getMapper(UserMapper.class);
        return mapper.queryPageCount(map);
    }
    /**
     * 获取机器MAC地址
     * @return
     */
    public String getMAC() {
        Sigar sigar = null;
        try {
            sigar = new Sigar();
            String[] ifaces = sigar.getNetInterfaceList();
            String hwaddr = null;
            for (int i = 0; i < ifaces.length; i++) {
                NetInterfaceConfig cfg = sigar.getNetInterfaceConfig(ifaces[i]);
                if (NetFlags.LOOPBACK_ADDRESS.equals(cfg.getAddress())
                    || (cfg.getFlags() & NetFlags.IFF_LOOPBACK) != 0
                    || NetFlags.NULL_HWADDR.equals(cfg.getHwaddr())) {
                    continue;
                }

                hwaddr = cfg.getHwaddr();
                break;
            }
            return hwaddr != null ? hwaddr : null;
        } catch (Exception e) {
            return null;
        } finally {
            if (sigar != null)
                sigar.close();
        }
    }
}

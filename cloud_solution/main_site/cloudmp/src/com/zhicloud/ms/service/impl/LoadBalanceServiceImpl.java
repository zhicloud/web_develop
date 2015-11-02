
package com.zhicloud.ms.service.impl;


import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhicloud.ms.service.ILoadBalanceService;

 
@Transactional(readOnly=true)
@Service("loadBalance")
public class LoadBalanceServiceImpl implements ILoadBalanceService {
    
    private static final Logger logger = Logger.getLogger(LoadBalanceServiceImpl.class); 
    @Resource
	private SqlSession sqlSession;
	
}

 

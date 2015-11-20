package com.zhicloud.ms.service; 

import com.zhicloud.ms.vo.BalancerForwardVO;


/**
 * @author ZYFTMX
 *
 */
public interface ILoadBalanceService {
	
	public BalancerForwardVO getBalanceForwardByHostId(String hostId);
}


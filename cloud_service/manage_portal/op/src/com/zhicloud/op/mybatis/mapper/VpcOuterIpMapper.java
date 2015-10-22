/**
 * Project Name:op
 * File Name:VpcOuterIpMapper.java
 * Package Name:com.zhicloud.op.mybatis.mapper
 * Date:2015年4月1日下午7:28:02
 * 
 *
*/ 

package com.zhicloud.op.mybatis.mapper; 

import java.util.List;
import java.util.Map;

import com.zhicloud.op.vo.VpcOuterIpVO;

/**
 * ClassName: VpcOuterIpMapper 
 * Function:  vpc ip操作
 * date: 2015年4月1日 下午7:28:02 
 *
 * @author sasa
 * @version 
 * @since JDK 1.7
 */
public interface VpcOuterIpMapper {
	/**
	 * 
	 * add: 新增ip
	 *
	 * @author sasa
	 * @param condition
	 * @return int
	 * @since JDK 1.7
	 */
    public int add(Map<String, Object> condition);
    /**
     * 
     * delete:删除ip
     *
     * @author sasa
     * @param condition
     * @return int
     * @since JDK 1.7
     */
    public int deleteIps(List<String> ips);
    /**
     * 查询一个vpc的所有ip
     * @param condition
     * @return
     */
    public List<VpcOuterIpVO> getAllIpByVpcId(Map<String,Object> condition);
    
    /**
     * 通过vpcid查询ip的数量
     * 
     * @param condition
     * @return
     */
    public int getCountByVpcId(Map<String,Object> condition);
}


/**
 * Project Name:op
 * File Name:VpcBindHostMapper.java
 * Package Name:com.zhicloud.op.mybatis.mapper
 * Date:2015年4月1日下午6:18:50
 * 
 *
*/ 

package com.zhicloud.op.mybatis.mapper; 

import java.util.Map;

/**
 * ClassName: VpcBindHostMapper 
 * Function:  主机和vpc的关联关系表操作
 * date: 2015年4月1日 下午6:18:50 
 *
 * @author sasa
 * @version 
 * @since JDK 1.7
 */
public interface VpcBindHostMapper {
	/**
	 * 
	 * add: 新增一个关系
	 *
	 * @author sasa
	 * @param condition
	 * @return int
	 * @since JDK 1.7
	 */
	public int add(Map<String, Object> condition);
	/**
	 * 
	 * delete:逻辑删除关联关系
	 *
	 * @author sasa
	 * @param condition
	 * @return int
	 * @since JDK 1.7
	 */
	public int deleteLogical(Map<String, Object> condition);

}


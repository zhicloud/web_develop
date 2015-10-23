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

import com.zhicloud.op.vo.VpcPriceVO;


/**
 * @author ZYFTMX
 * vpc数量与对应的价格
 */
public interface VpcPriceMapper {
	/**
	 * 增加一条对应记录
	 * @param condition
	 * @return
	 */
	public int addVpcPrice(Map<String,Object> condition);
	/**
	 * 删除一条对应记录
	 * @param id
	 * @return
	 */
	public int deleteVpcPrice(String id);
	/**
	 * 按地域查询所有的记录
	 * @return
	 */
	public List<VpcPriceVO> queryAllVpcPriceByRegion(Map<String,Object> condition);
	
	/**
	 * 按ID查询对应记录
	 * @param id
	 * @return
	 */
	public VpcPriceVO getById(String id);
	
	/**
	 * 更新一条VpcPrice记录
	 * @param condition
	 * @return
	 */
	public int updateVpcPrice(Map<String,Object> condition);
	
	/**
	 * 获取记录总数
	 * @param condition
	 * @return
	 */
	public int getCount(Map<String,Object> condition);
	
	/**
	 * 根据数量和地域查询记录(用于查重和获取价格)
	 * @param condition
	 * @return
	 */
	public VpcPriceVO getAmountAndRegion(Map<String,Object> condition);
	
}


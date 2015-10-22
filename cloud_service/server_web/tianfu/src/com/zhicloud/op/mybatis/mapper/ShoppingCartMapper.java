package com.zhicloud.op.mybatis.mapper;

import java.util.List;
import java.util.Map;

import com.zhicloud.op.vo.ShoppingCartVO; 

public interface ShoppingCartMapper {
	public ShoppingCartVO getCartByUserId(String userId);
	public List<ShoppingCartVO> getCartDetailByCartId(String cartId);
	public List<ShoppingCartVO> getConfigByIds(String[] item);

	public int inserIntoCart(Map<String, Object> condition);
	public int updateCart(Map<String, Object> condition);
	public int inserIntoCartDetail(Map<String, Object> condition);
	public int inserIntoConfig(Map<String, Object> condition);
	public int deleteDetailByItemId(String[] item);
	public int deleteConfigById(String[] item);
	public int deleteCartDetail(String userId);
	public int deleteCartDetailByIds(String[] item);
	public int deletePortByIds(String[] item);
	public int deleteAllCart(String userId);
	public int inserIntoConfigPort(Map<String, Object> condition);

}

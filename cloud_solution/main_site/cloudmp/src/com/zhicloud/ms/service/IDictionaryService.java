package com.zhicloud.ms.service;

import java.util.List;

import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.vo.DictionaryVO;

 
/**
 * 
* @ClassName: IDictionaryService 
* @Description: 字典表service 
* @author A18ccms a18ccms_gmail_com 
* @date 2015年7月20日 上午11:39:44 
*
 */
public interface IDictionaryService {
	/**
	 * 
	* @Title: getValuesByCode 
	* @Description: 根据code查询value
	* @param @param code
	* @param @return      
	* @return List<DictionaryVO>     
	* @throws
	 */
	public List<DictionaryVO> getValuesByCode(String code);
	/**
	 * 
	* @Title: updateValueById 
	* @Description: 根据Id更新字典表数据
	* @param @param dictionary
	* @param @return      
	* @return MethodResult     
	* @throws
	 */
	public MethodResult updateValueById(DictionaryVO dictionary);
	/**
	 * 
	* @Title: refreshProductNameValueInCash 
	* @Description: 刷新缓存里面的产品名
	* @param       
	* @return void     
	* @throws
	 */
	public void refreshProductNameValueInCash();

}

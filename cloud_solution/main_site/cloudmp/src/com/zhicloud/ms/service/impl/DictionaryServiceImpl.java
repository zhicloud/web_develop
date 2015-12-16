package com.zhicloud.ms.service.impl;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

 
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger; 
import org.springframework.transaction.annotation.Transactional;

import com.zhicloud.ms.constant.AppInconstant;
import com.zhicloud.ms.mapper.DictionaryMapper;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.IDictionaryService;
import com.zhicloud.ms.util.StringUtil;
import com.zhicloud.ms.vo.DictionaryVO;
/**
 * 
* @ClassName: DictionaryServiceImpl 
* @Description: 字典表dictionaryserviceimpl 
* @author sasa 
* @date 2015年7月20日 上午11:48:02 
*
 */
 @Transactional(readOnly=true)
public class DictionaryServiceImpl implements IDictionaryService {
  	private SqlSession sqlSession;
 	

	public SqlSession getSqlSession() {
		return sqlSession;
	}

	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	/**
	 * 根据code查询value List
	* <p>Title: getValuesByCode</p> 
	* <p>Description: </p> 
	* @param code
	* @return 
	* @see com.zhicloud.ms.service.IDictionaryService#getValuesByCode(java.lang.String)
	*/
	public List<DictionaryVO> getValuesByCode(String code) {
		DictionaryMapper dictionaryMapper = this.sqlSession.getMapper(DictionaryMapper.class);
		 List<DictionaryVO> dictionaryList = dictionaryMapper.queryValueByCode(code);		 
		return dictionaryList;
	}

	/**
	 * 根据Id更新字典表数据值 
	* <p>Title: updateValueById</p> 
	* <p>Description: </p> 
	* @param dictionary
	* @return 
	* @see com.zhicloud.ms.service.IDictionaryService#updateValueById(com.zhicloud.ms.vo.DictionaryVO)
	 */
	 @Transactional(readOnly=false)
	public MethodResult updateValueById(DictionaryVO dictionary) {
		DictionaryMapper dictionaryMapper = this.sqlSession.getMapper(DictionaryMapper.class);
		Map<String,Object> data = new LinkedHashMap<String,Object>();
		data.put("id", dictionary.getId());
		data.put("value", dictionary.getValue());
		data.put("modifyTime",  StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
		int i = dictionaryMapper.updateValueById(data);
		if(i<0){
			return new MethodResult(MethodResult.FAIL,"更新失败");
		} 
		return new MethodResult(MethodResult.SUCCESS,"更新成功");
	}

	@Override
	public void refreshProductNameValueInCash() {
		DictionaryMapper dictionaryMapper = this.sqlSession.getMapper(DictionaryMapper.class);
		List<DictionaryVO> dictionaryList = dictionaryMapper.queryValueByCode("product_name");
		if(dictionaryList!=null && dictionaryList.size() >0){
			//同步代码
			 synchronized (AppInconstant.productName) {
				 DictionaryVO productName = dictionaryList.get(0);
					//数据库的值和缓存不一致，更新缓存
					if(! (productName.getValue().equals(AppInconstant.productName))){
						AppInconstant.productName = productName.getValue();
					}  
			 }
			
		}
		
        dictionaryList = dictionaryMapper.queryValueByCode("init_user");
        if(dictionaryList!=null && dictionaryList.size() >0){
            //同步代码
             synchronized (AppInconstant.initUser) {
                 DictionaryVO initUser = dictionaryList.get(0); 
                 AppInconstant.initUser = initUser.getValue();      
             }
            
        }
		
		
	}

}

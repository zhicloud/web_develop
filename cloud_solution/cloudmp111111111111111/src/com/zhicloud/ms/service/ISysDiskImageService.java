/**
 * Project Name:CloudDeskTopMS
 * File Name:ISysDiskImageService.java
 * Package Name:com.zhicloud.ms.service
 * Date:2015年3月17日下午7:20:35
 * 
 *
*/ 

package com.zhicloud.ms.service;

import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.vo.SysDiskImageVO;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

/**
 * ClassName: ISysDiskImageService 
 * Function: 镜像的操作
 * date: 2015年3月17日 下午7:20:35 
 *
 * @author sasa
 * @version 
 * @since JDK 1.7
 */
public interface ISysDiskImageService {
	/**
	 * 
	 * queryAllSysDiskImage:查询镜像信息
	 *
	 * @author sasa
	 * @return List<SysDiskImageVO>
	 * @since JDK 1.7
	 */
	public List<SysDiskImageVO> queryAllSysDiskImage(); 
	
	/**
	 * 
	 * addSysDiskImage:新增镜像 
	 *
	 * @author sasa
	 * @param image
	 * @return MethodResult
	 * @throws IOException 
	 * @throws MalformedURLException 
	 * @since JDK 1.7
	 */
	public MethodResult addSysDiskImage(SysDiskImageVO image) throws MalformedURLException, IOException;
	
	/**
	 * 
	 * getSysDiskImageByName:通过名字获取镜像
	 *
	 * @author sasa
	 * @param name
	 * @return SysDiskImageVO
	 * @since JDK 1.7
	 */
	public SysDiskImageVO getSysDiskImageByName(String name);
	/**
	 * 
	 * getSysDiskImageById:根据镜像ID获取镜像 
	 *
	 * @author sasa
	 * @param id
	 * @return SysDiskImageVO
	 * @since JDK 1.7
	 */
	public SysDiskImageVO getSysDiskImageById(String id);
	/**
	 * 
	 * deleteSysDiskImage:根据镜像id删除镜像 
	 *
	 * @author sasa
	 * @param id
	 * @return MethodResult
	 * @since JDK 1.7
	 */
	public MethodResult deleteSysDiskImage(String id);
	/**
	 * 
	 * updateSysDiskImage:更新镜像信息
	 *
	 * @author sasa
	 * @param id
	 * @param image
	 * @return MethodResult
	 * @since JDK 1.7
	 */
	public MethodResult updateSysDiskImage(String id,SysDiskImageVO image);
	
	/**
	 * 
	 * initSysDiskImageFromHttpGateway:从http-gateway获取镜像同步到数据库中
	 *
	 * @author sasa
	 * @return boolean
	 * @since JDK 1.7
	 */
	public boolean initSysDiskImageFromHttpGateway();
	/**
	 * 根据用户类型查询不同的镜像
	 * @param type
	 * @return
	 */
	public List<SysDiskImageVO> querySysDiskImageByImageType(Integer type);

    /**
     * 根据根据多个参数获取镜像
     * @author  张翔
     * @param condition
     * @return
     */
    public List<SysDiskImageVO> getSysDiskImageByMultiParams(Map<String, Object> condition);

	/**
	 * 
	* @Title: updateImageType 
	* @Description: 根据镜像id更新镜像用途类型
	* @param @param ids
	* @param @param imageType
	* @param @return      
	* @return MethodResult     
	* @throws
	 */
	public MethodResult updateImageType(String[] ids,Integer imageType);
	
	/**
	 * 
	* @Title: updateTypeByUuid 
	* @Description: 根据真实镜像id更新镜像类型 
	* @param @param uuid
	* @param @param type
	* @param @return      
	* @return MethodResult     
	* @throws
	 */
	public MethodResult updateTypeByUuid(String uuid,Integer type);
	
	

}


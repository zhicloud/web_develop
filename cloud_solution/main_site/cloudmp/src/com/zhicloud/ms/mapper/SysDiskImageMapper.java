package com.zhicloud.ms.mapper;

import java.util.List;
import java.util.Map; 

import com.zhicloud.ms.vo.SysDiskImageVO;

public interface SysDiskImageMapper
{
	/**
	 * 
	 * getAll:获取所有的镜像 
	 *
	 * @author sasa
	 * @return List<SysDiskImageVO>
	 * @since JDK 1.7
	 */
	public List<SysDiskImageVO> getAll(); 	
    /**
     * 
     * getById:通过ID查询镜像 
     *
     * @author sasa
     * @param sysDiskImageId
     * @return SysDiskImageVO
     * @since JDK 1.7
     */
	public SysDiskImageVO getById(String sysDiskImageId);	
	/**
	 * 
	 * addSysDiskImage:新增镜像
	 *
	 * @author sasa
	 * @param data
	 * @return int
	 * @since JDK 1.7
	 */
	public int addSysDiskImage(Map<String, Object> data);
    /**
     * 
     * updateSysDiskImageById:根据镜像ID跟新镜像
     *
     * @author sasa
     * @param data
     * @return int
     * @since JDK 1.7
     */
	public int updateSysDiskImageById(Map<String, Object> data);  
	/**
	 * 
	 * deleteSysDiskImageByIds:根据镜像ID删除镜像.  
	 * 可批量 
	 *
	 * @author sasa
	 * @param sysDiskImageIds
	 * @return int
	 * @since JDK 1.7
	 */
	 
	public int deleteSysDiskImageByIds(String[] sysDiskImageIds);
	/**
	 * 
	 * getSysDiskImageByName:通过名字查询镜像 
	 *
	 * @author sasa
	 * @param data
	 * @return SysDiskImageVO
	 * @since JDK 1.7
	 */
	public SysDiskImageVO getSysDiskImageByName(Map<String, Object> data);
	/**
	 * getByRealImageId:根据真实镜像ID获取镜像信息 
	 *
	 * @author sasa
	 * @param uuid
	 * @return SysDiskImageVO
	 * @since JDK 1.7
	 */
	public SysDiskImageVO getByRealImageId(String id);
	/**
	 * updateUnrelatedSysDiskImageByName: 根据name更新镜像信息 
	 *
	 * @author sasa
	 * @param sysDiskImageData int
	 * @since JDK 1.7
	 */
	public int updateUnrelatedSysDiskImageByName( Map<String, Object> sysDiskImageData); 
	/**
	 * 
	* @Title: querySysDiskImageByImageType 
	* @Description: 根据镜像的类型查询镜像信息
	* @param  type  
	* @return List<SysDiskImageVO>     
	* @throws
	 */
	public List<SysDiskImageVO> querySysDiskImageByImageType(Integer imageType);

    /**
     *
     * @Title: querySysDiskImageByMultiParams
     * @author 张翔
     * @Description: 根据根据多个参数获取镜像
     * @param  conditione
     * @return List<SysDiskImageVO>
     * @throws
     */
    public List<SysDiskImageVO> querySysDiskImageByMultiParams(Map<String, Object> conditione);
	/**
	 * 
	* @Title: updateImageTypeByIds 
	* @Description: 更新镜像的用途类型
	* @param   data     
	* @return int     
	* @throws
	 */
	public int updateImageTypeById(Map<String, Object> data);
	
 }

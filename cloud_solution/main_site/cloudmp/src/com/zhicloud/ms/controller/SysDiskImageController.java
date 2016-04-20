/**
 * Project Name:CloudDeskTopMS
 * File Name:ImageController.java
 * Package Name:com.zhicloud.ms.controller
 * Date:2015年3月17日下午1:24:40
 * 
 *
*/ 

package com.zhicloud.ms.controller; 

import com.zhicloud.ms.constant.AppConstant;
import com.zhicloud.ms.constant.AppInconstant;
import com.zhicloud.ms.quartz.ComputeInfoCacheJob;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.CloudHostConfigModelService;
import com.zhicloud.ms.service.ICloudHostService;
import com.zhicloud.ms.service.ICloudHostWarehouseService;
import com.zhicloud.ms.service.IOperLogService;
import com.zhicloud.ms.service.ISysDiskImageService;
import com.zhicloud.ms.service.ImageUploadAddressService;
import com.zhicloud.ms.transform.constant.TransFormPrivilegeConstant;
import com.zhicloud.ms.transform.util.TransFormLoginHelper;
import com.zhicloud.ms.transform.util.TransFormPrivilegeUtil;
import com.zhicloud.ms.util.StringUtil;
import com.zhicloud.ms.vo.CloudHostConfigModel;
import com.zhicloud.ms.vo.CloudHostVO;
import com.zhicloud.ms.vo.CloudHostWarehouse;
import com.zhicloud.ms.vo.SysDiskImageVO;

import org.quartz.JobExecutionException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName: ImageController 
 * Function: 镜像相关操作
 * date: 2015年3月17日 下午1:24:40 
 *
 * @author sasa
 * @version 
 * @since JDK 1.7
 */
@Controller
@RequestMapping("/image")
public class SysDiskImageController {
	
	@Resource
	private ISysDiskImageService sysDiskImageService;
	
	@Resource
	private ICloudHostService cloudHostService;
	
	@Resource
	private ICloudHostWarehouseService cloudHostWarehouseService;
	
	@Resource
	private CloudHostConfigModelService cloudHostConfigModelService;
	
	@Resource
	private IOperLogService operLogService;
	
	@Resource
	private ImageUploadAddressService imageUploadAddressService;
	
	/**
	 * 
	 * toIamgeList:查询镜像信息并跳转镜像列表信息
	 *
	 * @author sasa
	 * @param model
	 * @return String
	 * @since JDK 1.7
	 */
	@RequestMapping(value="/imagelist",method=RequestMethod.GET)
	public String toIamgeList(Model model,HttpServletRequest request)
      throws UnsupportedEncodingException {
      if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_image_query)){
          return "not_have_access";
      }
      String name = request.getParameter("name");
      String typeStr = request.getParameter("type");
      String imageTypeStr = request.getParameter("image_type");
      String statusStr = request.getParameter("status");

      model.addAttribute("image_type", imageTypeStr);
      model.addAttribute("status", statusStr);
      model.addAttribute("type", typeStr);

      Integer type, imageType, status;

      if(StringUtil.isBlank(name)){
          name = null;
      }else{
          name = new String(request.getParameter("name").getBytes("ISO-8859-1"),"UTF-8");
          model.addAttribute("name", name);
          name = "%"+name+"%";
      }

      if(StringUtil.isBlank(typeStr)){
          type = null;
      } else{
          type = Integer.valueOf(typeStr);
      }
      if(StringUtil.isBlank(imageTypeStr)){
          imageType = null;
      } else {
          imageType = Integer.valueOf(imageTypeStr);
      }
      if(StringUtil.isBlank(statusStr)){
          status = null;
      } else {
          status = Integer.valueOf(statusStr);
      }

      Map<String, Object> condition = new LinkedHashMap<String, Object>();
      condition.put("name", name);
      condition.put("image_type", imageType);
      condition.put("status", status);
      condition.put("type", type);

      List<SysDiskImageVO> sysDiskImageList = sysDiskImageService.getSysDiskImageByMultiParams(condition);
      List<CloudHostWarehouse> warehouseList = cloudHostWarehouseService.getAll();
      model.addAttribute("sysDiskImageList", sysDiskImageList);
      model.addAttribute("warehouseList", warehouseList);
      //获取客户端IP，提供了镜像上传使用
      model.addAttribute("clientIP", TransFormLoginHelper.getClientIP(request));
      try {
        model.addAttribute("serverIP", imageUploadAddressService.getAvailableAddress(request));
      } catch (IOException e) {
        e.printStackTrace();
      }
      model.addAttribute("chunkSize", AppInconstant.chunkSize);

      return "sys_disk_image_manage";
	}
	@RequestMapping(value="/login",method=RequestMethod.GET)
	public String toLogin(Model model){ 
		return "index";
	} 
	/**
	 * 
	 * getHost:通过仓库ID筛选主机 
	 *
	 * @author sasa
	 * @param warehouseId
	 * @return MethodResult
	 * @since JDK 1.7
	 */
	@RequestMapping(value="/gethost",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult getHost(String warehouseId,HttpServletRequest request){ 
		List<CloudHostVO> hostList = cloudHostService.getHostByWareId(warehouseId);
		List<CloudHostVO> hostListLast = new ArrayList<CloudHostVO>();
		for(int i=0;i<hostList.size();i++){
			CloudHostVO vo = hostList.get(i);
			if(!StringUtil.isBlank(vo.getUserId())){
				hostListLast.add(hostList.get(i));
			}
		}
		MethodResult result = new MethodResult(MethodResult.SUCCESS,"");
		result.put("hostList", hostListLast);
		return result;
	}
	/**
	 * 
	 * addImage:新增镜像
	 *
	 * @author sasa
	 * @param image
	 * @return MethodResult
	 * @since JDK 1.7
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult addImage(SysDiskImageVO image,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_image_add)){
			return new MethodResult(MethodResult.FAIL,"您没有新增镜像的权限，请联系管理员");
		}
 		try {
 			image.setImageType(AppConstant.DISK_IMAGE_TYPE_DESKTOP);
			MethodResult result = sysDiskImageService.addSysDiskImage(image);
			if(result.isSuccess()){
	            operLogService.addLog("主机镜像", "新增主机镜像"+image.getDisplayName()+"成功", "1", "1", request);
	        }else{
	            operLogService.addLog("主机镜像", "新增主机镜像"+image.getDisplayName()+"失败", "1", "2", request);
	        }
			return result;
		} catch (MalformedURLException e) { 
			e.printStackTrace();
			operLogService.addLog("主机镜像", "新增主机镜像"+image.getDisplayName()+"失败", "1", "2", request);
			return new MethodResult(MethodResult.FAIL,"添加失败");			
			
		} catch (IOException e) { 
			e.printStackTrace();
	         operLogService.addLog("主机镜像", "新增主机镜像"+image.getDisplayName()+"失败", "1", "2", request);
			return new MethodResult(MethodResult.FAIL,"添加失败");						
		}
	} 
	/**
	 * 
	 * checkImageName:检查是否是可用的镜像名 
	 *
	 * @author sasa
	 * @param name
	 * @return MethodResult
	 * @since JDK 1.7
	 */
	@RequestMapping(value="/checkimagename",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult checkImageName(String name){
		SysDiskImageVO vo = sysDiskImageService.getSysDiskImageByName(name);
 		if(vo == null){
			return new MethodResult(MethodResult.SUCCESS,"可用名字");			
		}else{
			return new MethodResult(MethodResult.FAIL,"该镜像名已经存在");			
		}
	}
	/**
	 * 
	 * deleteSysDiskImage:根据镜像ID删除镜像
	 *
	 * @author sasa
	 * @param id
	 * @return MethodResult
	 * @since JDK 1.7
	 */
	@RequestMapping(value="/{id}/delete",method=RequestMethod.GET)
	@ResponseBody
	public MethodResult deleteSysDiskImage(@PathVariable("id") String id,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_image_delete)){
			return new MethodResult(MethodResult.FAIL,"您没有删除镜像的权限，请联系管理员");
		}
		int su1 = 0; //  完全删除成功  
		int su2 = 0; //  镜像类型不能删除
		int su3 = 0; //  删除失败
		String [] imageIds = id.split(",");
		MethodResult result = new MethodResult(MethodResult.SUCCESS,"删除成功");
		for(String imageId : imageIds){			
			List<CloudHostConfigModel> modelList = cloudHostConfigModelService.getCloudHostConfigModelServiceByImageId(imageId);
			if(modelList != null && modelList.size() > 0){
				su2 ++ ;
 			}else{				
				MethodResult re = sysDiskImageService.deleteSysDiskImage(imageId);
				if(re.isSuccess()){
					su1 ++ ;
				}else{
					su3 ++ ; 
				}
			}
		}
		if(su1 >0 && su2==0 && su3==0){
	        operLogService.addLog("主机镜像", "批量删除镜像成功", "1", "1", request);
			return new MethodResult(MethodResult.SUCCESS,"删除成功");
		}
		if(su1 >0 && su2>0 && su3>0){
            operLogService.addLog("主机镜像", "批量删除镜像成功,部分镜像平台删除失败,部分镜像已经创建类型需先删除类型", "1", "1", request);
			return new MethodResult(MethodResult.SUCCESS,"删除成功,部分镜像平台删除失败,部分镜像已经创建类型需先删除类型");
		}
		if(su1 >0 && su2>0 && su3==0){
            operLogService.addLog("主机镜像", "批量删除镜像成功,部分镜像已经创建类型需先删除类型", "1", "1", request);
			return new MethodResult(MethodResult.SUCCESS,"删除成功,部分镜像已经创建类型需先删除类型");			
		}
		if(su1 >0 && su2==0 && su3>0){
            operLogService.addLog("主机镜像", "批量删除镜像成功,部分镜像平台删除失败", "1", "1", request);
			return new MethodResult(MethodResult.SUCCESS,"删除成功,部分镜像平台删除失败");			
		}
		if(su1 ==0 ){
            operLogService.addLog("主机镜像", "批量删除镜像失败", "1", "2", request);
			return new MethodResult(MethodResult.FAIL,"删除失败");			
		}
		return result;
	}
	/**
	 * 
	 * getImageInfo:根据镜像ID获取镜像
	 *
	 * @author sasa
	 * @param id
	 * @return SysDiskImageVO
	 * @since JDK 1.7
	 */
	@RequestMapping(value="/{id}/update",method=RequestMethod.GET) 
	public String getImageInfo(@PathVariable("id") String id,Model model,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_image_modify)){
			return "not_have_access";
		}
		SysDiskImageVO image = sysDiskImageService.getSysDiskImageById(id);
		model.addAttribute("image", image); 
 		return "sys_disk_image_mod";
 	}
	/**
	 * 
	 * updateImage:更新镜像信息
	 *
	 * @author sasa
	 * @param id
	 * @param image
	 * @return MethodResult
	 * @since JDK 1.7
	 */
	@RequestMapping(value="/{id}/update",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult updateImage(@PathVariable("id") String id,SysDiskImageVO image,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_image_modify)){
			return new MethodResult(MethodResult.FAIL,"您没有修改镜像的权限，请联系管理员");
		}
		MethodResult result = sysDiskImageService.updateSysDiskImage(id, image);
		if(result.isSuccess()){
            operLogService.addLog("主机镜像", "修改主机镜像"+image.getDisplayName()+"成功", "1", "1", request);
		}else{
            operLogService.addLog("主机镜像", "修改主机镜像"+image.getDisplayName()+"失败", "1", "2", request); 
		}
		return result;
	}
	
	/**
	 * 
	 * toIamgeList:查询镜像信息并跳转镜像列表信息
	 *
	 * @author sasa
	 * @param model
	 * @return String
	 * @since JDK 1.7
	 */
	@RequestMapping(value="/add",method=RequestMethod.GET)
	public String toAddIamgeList(Model model,HttpServletRequest request){	
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_image_add)){
			return "not_have_access";
		}
		List<CloudHostVO> hostList = cloudHostService.getAllHost();
		model.addAttribute("hostList", hostList); 
 		return "sys_disk_image_add";
	}
	
	@RequestMapping(value="/change/imagetype",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult updateStatus(String ids, String imageType,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_image_type_modify)){
			return new MethodResult(MethodResult.FAIL,"您没有修改镜像用途的权限，请联系管理员");
		}
		String[] idsArr = ids.split(",");
		int statusInt = Integer.parseInt(imageType);
		int result = 0;
		
		MethodResult mr =  sysDiskImageService.updateImageType(idsArr, statusInt); 
		if(mr.isSuccess()){
            operLogService.addLog("主机镜像", "修改主机镜像用途成功", "1", "1", request);
        }else{
            operLogService.addLog("主机镜像", "修改主机镜像用途失败", "1", "2", request); 
        }
		return mr;
 	}
	
	
	/**
	 * 
	* @Title: updateImage 
	* @Description: 用户用户变更之后 
	* @param @param type disk 表示磁盘镜像 iso表示光盘镜像
	* @param @param request
	* @param @return      
	* @return MethodResult     
	* @throws
	 */
	@RequestMapping(value="/update",method=RequestMethod.GET)
    @ResponseBody
    public MethodResult updateImage(String type,String uuid, HttpServletRequest request) {
        if ("disk".equals(type)) {
            sysDiskImageService.initSysDiskImageFromHttpGateway();
            sysDiskImageService.updateTypeByUuid(uuid, 2);
        } else if ("iso".equals(type)) {
            try {
                ComputeInfoCacheJob.singleton().execute(null);
            } catch (JobExecutionException e) {
                e.printStackTrace();
            }
        }

        return new MethodResult(MethodResult.SUCCESS, "success");
    }
	

    /**
     * @Description:上传前验证是否具有上传权限
     * @param type(disk 磁盘,iso 光盘)
     * @param request
     * @return MethodResult
     */
    @RequestMapping(value = "/hasprivilege", method = RequestMethod.GET)
    @ResponseBody
    public MethodResult isHasPrivilege(String type, HttpServletRequest request) {
        if ("disk".equals(type)) {
            if (!new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.disk_image_upload)) {
                return new MethodResult(MethodResult.FAIL, "您没有磁盘镜像上传权限");
            } else {
                return new MethodResult(MethodResult.SUCCESS, "您有磁盘镜像上传权限");
            }
        } else if ("iso".equals(type)) {
            if (!new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.iso_image_upload)) {
                return new MethodResult(MethodResult.FAIL, "您没有光盘镜像上传权限");
            } else {
                return new MethodResult(MethodResult.SUCCESS, "您有光盘镜像上传权限");
            }
        }

        return new MethodResult(MethodResult.FAIL, "您没有权限");
    }

}


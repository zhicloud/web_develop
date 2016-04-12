package com.zhicloud.ms.controller;

import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.CloudHostConfigModelService;
import com.zhicloud.ms.service.ICloudHostWarehouseService;
import com.zhicloud.ms.service.IOperLogService;
import com.zhicloud.ms.service.ISysDiskImageService;
import com.zhicloud.ms.transform.constant.TransFormPrivilegeConstant;
import com.zhicloud.ms.transform.util.TransFormPrivilegeUtil;
import com.zhicloud.ms.vo.CloudHostConfigModel;
import com.zhicloud.ms.vo.CloudHostWarehouse;
import com.zhicloud.ms.vo.SysDiskImageVO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Controller
public class CloudHostConfigModelController {
	@Resource
	CloudHostConfigModelService cloudHostConfigModelService;
	@Resource
	ICloudHostWarehouseService cloudHostWarehouseService;
	@Resource
	ISysDiskImageService sysDiskImageService;
	@Resource
    private IOperLogService operLogService;
	/**
	 * 
	 * 获取所有主机类型
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/chcm/all",method=RequestMethod.GET)
	public String getAll(Model model,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_type_query)){
			return "not_have_access";
		}
		List<CloudHostConfigModel> chcmList =  cloudHostConfigModelService.getAll();
		model.addAttribute("chcmList", chcmList);
		List<CloudHostWarehouse> warehouseList = cloudHostWarehouseService.getAll();
 		model.addAttribute("warehouseList", warehouseList);
 		List<SysDiskImageVO> sdiList = sysDiskImageService.queryAllSysDiskImage();
 		model.addAttribute("sdiList",sdiList);
		return "host_type_manage";
	}
	
	/**
	 * 添加一个主机类型
	 * @param chcm
	 * @return
	 */
	@RequestMapping(value="/chcm/add",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult addType(CloudHostConfigModel chcm,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_type_add)){
			return new MethodResult(MethodResult.FAIL,"您没有新增配置的权限，请联系管理员");
		}
		String id = cloudHostConfigModelService.addType(chcm);
		MethodResult mr = new MethodResult();
		mr.status = "success";
		mr.message = "添加成功";
		mr.setProperty("id", id);
		mr.setProperty("name", chcm.getName());
        operLogService.addLog("主机配置", "新增主机配置", "1", "1", request);

		return mr;
	}
	/**
	 * 删除一个主机类型
	 * @param id
	 * @return
	 */
	@RequestMapping(value={"/chcm/{id}/delete","/cscm/{id}/delete"},method=RequestMethod.GET)
	@ResponseBody
	public MethodResult deleteType(@PathVariable("id") String id,HttpServletRequest request){
      if(!((new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_type_delete)) || (new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.server_type_delete)))){
          return new MethodResult(MethodResult.FAIL,"您没有删除配置的权限，请联系管理员");
      }

      MethodResult result = cloudHostConfigModelService.deleteType(id);
      String flag = "2";
      if (result.isSuccess()) {
          flag = "1";
      }
      operLogService.addLog("主机配置", "删除主机配置", "1", flag, request);

		return new MethodResult(result.status, result.message);
	}
	/**
	 * 查询一个主机类型，返回至更新页
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/chcm/{id}/update",method=RequestMethod.GET) 
	public String updataTypePage(@PathVariable("id") String id,Model model,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_type_modify)){
			return "not_have_access";
		}
		CloudHostConfigModel chcm = cloudHostConfigModelService.getById(id);
		model.addAttribute("chcm",chcm);
		List<SysDiskImageVO> list = sysDiskImageService.queryAllSysDiskImage();
		model.addAttribute("imageList",list);
		return "desktop_type_mod";
	}
	/**
	 * 更新一个主机类型
	 * @param id
	 * @param chcm
	 * @return
	 */
	@RequestMapping(value="/chcm/{id}/update",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult updataType(@PathVariable("id") String id,CloudHostConfigModel chcm,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_type_modify)){
			return new MethodResult(MethodResult.FAIL,"您没有修改配置的权限，请联系管理员");
		}
		cloudHostConfigModelService.updateType(id, chcm);
        operLogService.addLog("主机配置", "修改主机配置", "1", "1", request);

		return new MethodResult(MethodResult.SUCCESS,"更新成功");
	}
	
	@RequestMapping(value="/chcm/checktypename",method=RequestMethod.GET)
	@ResponseBody
	public MethodResult checkTypeName(String name,HttpServletRequest request){
		CloudHostConfigModel type = cloudHostConfigModelService.getCloudHostConfigModelByName(name);
		if(type == null){
			return new MethodResult(MethodResult.SUCCESS,"可用名字");			
		}else{
			return new MethodResult(MethodResult.FAIL,"该类型名已经存在");			
		}
	}
	
	/**
	 * 
	 * addPage: 跳转到主机类型新增页面
	 *
	 * @author sasa
	 * @param model
	 * @return String
	 * @since JDK 1.7
	 */
	@RequestMapping(value="/chcm/addpage",method=RequestMethod.GET)
	public String addPage(Model model,HttpServletRequest request){ 
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_type_add)){
			return "not_have_access";
		}
		List<SysDiskImageVO> list = sysDiskImageService.queryAllSysDiskImage();
		model.addAttribute("imageList",list);
		return "desktop_type_add";
	}
	
	/**
	 * 
	 * 获取所有服务器配置类型并跳转到管理页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/cscm/allserver",method=RequestMethod.GET)
	public String getAllServer(Model model,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.server_type_query)){
			return "not_have_access";
		}
		List<CloudHostConfigModel> chcmList =  cloudHostConfigModelService.getAllServer();
		model.addAttribute("serverTypeList", chcmList);
 		List<SysDiskImageVO> sdiList = sysDiskImageService.queryAllSysDiskImage();
 		model.addAttribute("sdiList",sdiList);
		return "server_type_manage";
	}
	
	/**
	 * 跳转到添加服务器配置页面
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/cscm/addserverpage",method=RequestMethod.GET)
	public String addServerPage(Model model,HttpServletRequest request){ 
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.server_type_add)){
			return "not_have_access";
		}
		List<SysDiskImageVO> list = sysDiskImageService.queryAllSysDiskImage();
		model.addAttribute("imageList",list);
		return "server_type_add";
	}
	
	@RequestMapping(value="/cscm/addserver",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult addServerType(CloudHostConfigModel chcm,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.server_type_add)){
			return new MethodResult(MethodResult.FAIL,"您没有新增配置的权限，请联系管理员");
		}
		MethodResult mr = cloudHostConfigModelService.addServerType(chcm);
		return mr;
	}
	
	/**
	 * 查询一个服务器类型，返回至更新页
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/cscm/{id}/updateserverpage",method=RequestMethod.GET) 
	public String updataServerTypePage(@PathVariable("id") String id,Model model,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.server_type_modify)){
			return "not_have_access";
		}
		CloudHostConfigModel chcm = cloudHostConfigModelService.getById(id);
		model.addAttribute("chcm",chcm);
		List<SysDiskImageVO> list = sysDiskImageService.queryAllSysDiskImage();
		model.addAttribute("imageList",list);
		return "server_type_mod";
	}
	
	/**
	 * 更新一个服务器类型
	 * @param id
	 * @param chcm
	 * @return
	 */
	@RequestMapping(value="/cscm/{id}/updateserver",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult updataServerType(@PathVariable("id") String id,CloudHostConfigModel chcm,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.server_type_modify)){
			return new MethodResult(MethodResult.FAIL,"您没有修改配置的权限，请联系管理员");
		}
		MethodResult mr = cloudHostConfigModelService.updateServerType(id, chcm);
		if(mr.isSuccess()){
            operLogService.addLog("主机配置","修改主机配置" , "1", "1", request);
        }else{
            operLogService.addLog("主机配置","修改主机配置" , "1", "2", request);
        }
        return mr; 
	}
}

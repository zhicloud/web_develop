package com.zhicloud.ms.controller;

import com.zhicloud.ms.httpGateway.HttpGatewayChannelExt;
import com.zhicloud.ms.httpGateway.HttpGatewayManager;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.ICloudDiskService;
import com.zhicloud.ms.service.IOperLogService;
import com.zhicloud.ms.transform.constant.TransFormPrivilegeConstant;
import com.zhicloud.ms.transform.util.TransFormPrivilegeUtil;
import com.zhicloud.ms.vo.CloudDisk;
import com.zhicloud.ms.vo.StoragePoolVO;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.util.*;


@Controller
@RequestMapping("/cloudDisk")
public class CloudDiskController {
	
	
	public static final Logger logger = Logger.getLogger(CloudServerController.class);

	@Resource
	ICloudDiskService cloudDiskService;
	
	@Resource
    private IOperLogService operLogService;
	/**
	 * 查询所有云主机
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/all",method=RequestMethod.GET)
	public String getAll(Model model,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.cloud_disk_manager)){
			return "not_have_access";
		}
		List<CloudDisk> list = cloudDiskService.getAllCloudDisk(new CloudDisk()); 
		model.addAttribute("cloudDiskList", list);
		return "cloud_disk_manage";
	}
	
	
	
	/**
	 * 
	* @Title: add 
	* @Description: 打开新增资源池页面 
	* @param @param model
	* @param @param request     
	* @return String     
	* @throws
	 */
	@RequestMapping(value="/add",method=RequestMethod.GET)
	public String initCloudDiskAdd(Model model,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.cloud_disk_manager_add)){
			return "not_have_access";//等待修改
		}
		HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
		try {
			List<StoragePoolVO> dList = new ArrayList<StoragePoolVO>();
				
			if(channel!=null){
				JSONObject  result = channel.storagePoolQuery();
				if (!"fail".equals(result.getString("status"))) {
				JSONArray computerList = result.getJSONArray("storagePools");
				for (int i = 0; i < computerList.size(); i ++) {
					JSONObject computerObject = computerList.getJSONObject(i);
					String uuid = computerObject.getString("uuid");
					String name = computerObject.getString("name");
					BigDecimal diskUsage = new BigDecimal(computerObject.getString("disk_usage"));
					JSONArray diskList = computerObject.getJSONArray("disk_volume");
					BigInteger[] dcount = new BigInteger[diskList.size()];
					for(int j=0;j<diskList.size();j++){
						dcount[j] = new BigInteger(diskList.getString(j));
					}
					StoragePoolVO storage = new StoragePoolVO();
					storage.setUuid(uuid);
					storage.setDiskUsage(diskUsage);
					storage.setDiskVolume(dcount);
					storage.setName(name);
	
					dList.add(storage);
					}
				}
		    }	
			model.addAttribute("diskPool", dList);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "cloud_disk_manager_add";
	}
	
	
	/**
	 * 
	* @Title: add 
	* @Description: 保存云硬盘
	* @param @param model
	* @param @param request     
	* @return String     
	* @throws
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult cloudDiskAdd(CloudDisk disk,HttpServletRequest request){

		
		if((disk.getDiskdiy()==null) && (disk.getDisk()==null)){
		    operLogService.addLog("云硬盘", "创建云硬盘", "1", "2", request);
			return new MethodResult(MethodResult.FAIL,"必须设置云硬盘容量！");
		}
	
		MethodResult result =  cloudDiskService.addCloudDisk(disk, request);
		 if(result.isSuccess()){
             operLogService.addLog("云硬盘","创建云硬盘" , "1", "1", request);
         }else{
             operLogService.addLog("云硬盘","创建云硬盘" , "1", "2", request);
         }
         return result;
	}
	
	
	/**
	 * 
	* @Title: del 
	* @Description: 删除云硬盘
	* @param @param model
	* @param @param request     
	* @return String     
	* @throws
	 */
	@RequestMapping(value="/del",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult couldDiskDel(String ids,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.cloud_disk_manager_delete)){
			return new MethodResult(MethodResult.FAIL, "对不起，您没有删除硬盘权限！");
		}	
		if (ids.endsWith(",")){
			ids = ids.substring(0,ids.length()-1);
		}
		String[] uids = ids.split(",");
		for (String id : uids){
			MethodResult ret = cloudDiskService.delCloudDisk(id);
			if (ret.status.equals(MethodResult.FAIL)){
			    operLogService.addLog("云硬盘","删除云硬盘" , "1", "2", request);
				return ret;
			}
		}
        operLogService.addLog("云硬盘","删除云硬盘" , "1", "1", request);
		return new MethodResult(MethodResult.SUCCESS, "删除硬盘成功");
	}
	
	/**
	 * 
	* @Title: onoff 
	* @Description: 修改状态
	* @param @param model
	* @param @param request     
	* @return String     
	* @throws
	 */
	@RequestMapping(value="/onoff",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult cloudDiskAdd(String id, Integer status,HttpServletRequest request){
		if( !new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.cloud_disk_manager_onoff)){
			return new MethodResult(MethodResult.FAIL, "对不起，您没有启动或停用云硬盘权限！");
		}			
		MethodResult result = cloudDiskService.changeDiskStatus(id,status, request);
		
		if(result.isSuccess()){
            operLogService.addLog("云硬盘","停用云硬盘" , "1", "1", request);
        }else{
            operLogService.addLog("云硬盘","停用云硬盘" , "1", "2", request);
        }
		return result;
	}
	
	
	
	/**
	 * 
	* @Title: update 
	* @Description: 修改资源池页面 
	* @param @param model
	* @param @param request     
	* @return String     
	* @throws
	 */
	@RequestMapping(value="/update",method=RequestMethod.GET)
	public String initCloudDiskUpdate(String id, Model model,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.cloud_disk_manager_update)){
			return "not_have_access";//等待修改
		}
		CloudDisk disk  =  cloudDiskService.selectCloudDiskById(id);
		model.addAttribute("disk", disk);
		return "cloud_disk_manager_update";
	}
	
	
	
	
	
	/**
	 * 
	* @Title: update 
	* @Description: 修改云硬盘
	* @param @param model
	* @param @param request     
	* @return String     
	* @throws
	 */
	@RequestMapping(value="/update",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult cloudDiskUpdate(CloudDisk disk,HttpServletRequest request){
		if( !new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.cloud_disk_manager_update)){
			return new MethodResult(MethodResult.FAIL, "对不起，您没有修改云硬盘权限！");
		}			
		MethodResult result =  cloudDiskService.updateCloudDisk(disk, request);
		
		if(result.isSuccess()){
            operLogService.addLog("云硬盘","修改云硬盘" , "1", "1", request);
        }else{
            operLogService.addLog("云硬盘","修改云硬盘" , "1", "2", request);
        }
        return result;
	}
}

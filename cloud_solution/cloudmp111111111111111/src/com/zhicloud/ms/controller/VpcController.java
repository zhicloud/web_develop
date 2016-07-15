package com.zhicloud.ms.controller;

import com.zhicloud.ms.constant.AppConstant;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.*;
import com.zhicloud.ms.transform.constant.TransFormPrivilegeConstant;
import com.zhicloud.ms.transform.util.TransFormPrivilegeUtil;
import com.zhicloud.ms.vo.*;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Controller
@RequestMapping("/vpc")
public class VpcController {
	
	
	public static final Logger logger = Logger.getLogger(VpcController.class);

	@Resource
	IVpcService vpcService;
	@Resource
	ISysDiskImageService sysDiskImageService;
	@Resource
	ICloudHostService cloudHostService;
    @Resource
    ISysGroupService sysGroupService;
	@Resource
    private IOperLogService operLogService;
	/**
	 * 跳转到VPC管理页
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/all",method=RequestMethod.GET)
	public String allVpc(Model model,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.vpc_manage_page)){
			return "not_have_access";
		}
		List<VpcBaseInfoVO> vpcList = vpcService.getAllVpc();
		model.addAttribute("vpcList", vpcList);
		return "vpc_manage";
	}
	
	@RequestMapping(value="/add",method=RequestMethod.GET)
	public String addVpcPage(Model model,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.vpc_manage_add_page)){
			return "not_have_access";
		}
		List<SysDiskImageVO> list = sysDiskImageService.queryAllSysDiskImage();
      List<SysGroupVO> sysGroupVOList = sysGroupService.queryAll();
		model.addAttribute("imageList",list);
      model.addAttribute("sys_group_list", sysGroupVOList);
		return "vpc_manage_add";
	}
	
	/**
	 * 创建vpc
	 * @param displayName
	 * @param ipAmount
	 * @param description
	 * @param hosts
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult addVpc(@RequestParam("displayName") String displayName,
			@RequestParam("ipAmount") String ipAmount,
      @RequestParam("groupId") String groupId,
			@RequestParam("description") String description,
			@RequestParam("hosts[]") String[] hosts,
			HttpServletRequest request){
		MethodResult mr = vpcService.addVpc(displayName, ipAmount, groupId, description, hosts, request);
		if(mr.isSuccess()){
		    operLogService.addLog("专属云", "新增专属云"+displayName+"成功", "1", "1", request);
		}else{
	          operLogService.addLog("专属云", "新增专属云"+displayName+"失败", "1", "2", request);
		}
		return mr;
	}
	
	/**
	 * 删除vpc
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/{id}/delete",method=RequestMethod.GET)
	@ResponseBody
	public MethodResult deleteVpc(@PathVariable("id") String id,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.vpc_manage_delete)){
			return new MethodResult(MethodResult.FAIL,"您没有删除专属云的权限，请联系管理员");
		} 
		MethodResult mr = vpcService.deleteVpc(id);
		if(mr.isSuccess()){
            operLogService.addLog("专属云", "删除专属云"+mr.getProperty("name")+"成功", "1", "1", request);
        }else{
              operLogService.addLog("专属云", "删除专属云"+mr.getProperty("name")+"失败", "1", "2", request);
        }
		return mr;
	}
	
	/**
	 * 停用vpc
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/{id}/stop",method=RequestMethod.GET)
	@ResponseBody
	public MethodResult stopVpc(@PathVariable("id") String id,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.vpc_manage_stop)){
			return new MethodResult(MethodResult.FAIL,"您没有停用专属云的权限，请联系管理员");
		}
		MethodResult mr = vpcService.disableVpc(id);
		if(mr.isSuccess()){
            operLogService.addLog("专属云", "删除专属云"+mr.getProperty("name")+"成功", "1", "1", request);
        }else{
              operLogService.addLog("专属云", "删除专属云"+mr.getProperty("name")+"失败", "1", "2", request);
        }
		return mr;
	}
	
	/**
	 * 恢复vpc
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/{id}/start",method=RequestMethod.GET)
	@ResponseBody
	public MethodResult startVpc(@PathVariable("id") String id,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.vpc_manage_start)){
			return new MethodResult(MethodResult.FAIL,"您没有恢复专属云的权限，请联系管理员");
		}
		MethodResult mr = vpcService.ableVpc(id);
		if(mr.isSuccess()){
            operLogService.addLog("专属云", "恢复专属云"+mr.getProperty("name")+"成功", "1", "1", request);
        }else{
              operLogService.addLog("专属云", "恢复专属云"+mr.getProperty("name")+"失败", "1", "2", request);
        }
		return mr;
	}
	
	/**
	 * ip管理页
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/{id}/ipManage",method=RequestMethod.GET)
	public String ipManagePage(@PathVariable("id") String id, Model model,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.vpc_manage_ip_page)){
			return "not_have_access";
		}
		List<VpcOuterIpVO> ipList = vpcService.getAllIpByVpcId(id);
		model.addAttribute("ipList", ipList);
		model.addAttribute("ipAmount", ipList.size());
		model.addAttribute("vpcId", id);
		return "vpc_manage_ip";
	}
	
	/**
	 * 增加IP数量
	 * @param id
	 * @param ipAmount
	 * @return
	 */
	@RequestMapping(value="/addIp",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult addIpCount(@RequestParam("id") String id,@RequestParam("ipAmount") String ipAmount,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.vpc_manage_ip_add)){
			return new MethodResult(MethodResult.FAIL,"您没有添加专属云IP数量的权限，请联系管理员");
		}
		MethodResult mr = vpcService.addIpCount(ipAmount, id);
		if(mr.isSuccess()){
            operLogService.addLog("专属云", "添加专属云"+mr.getProperty("name")+"IP成功", "1", "1", request);
        }else{
              operLogService.addLog("专属云", "添加专属云"+mr.getProperty("name")+"IP失败", "1", "2", request);
        }
		return mr;
	}
	
	/**
	 * 批量删除IP
	 * @param ips
	 * @param ids
	 * @param vpcId
	 * @return
	 */
	@RequestMapping(value="/deleteIps",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult deleteIps(@RequestParam("ips[]") String[] ips,
			@RequestParam("ids[]") String[] ids,
			@RequestParam("vpcId") String vpcId,
			HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.vpc_manage_ip_delete)){
			return new MethodResult(MethodResult.FAIL,"您没有删除专属云IP的权限，请联系管理员");
		}
		List<String> idList = Arrays.asList(ids);
		List<String> ipList = Arrays.asList(ips);
		MethodResult mr = vpcService.deleteIps(vpcId, idList, ipList);
		if(mr.isSuccess()){
            operLogService.addLog("专属云", "删除专属云"+mr.getProperty("name")+"IP"+ips+"成功", "1", "1", request);
        }else{
              operLogService.addLog("专属云", "删除专属云"+mr.getProperty("name")+"IP"+ips+"失败", "1", "2", request);
        }
		return mr;
	}
	
	/**
	 * 跳转到端口配置页
	 * @param vpcId
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/{vpcId}/networkManage",method=RequestMethod.GET)
	public String networkManagePage(@PathVariable("vpcId") String vpcId, Model model,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.vpc_manage_network_page)){
			return "not_have_access";
		}
		List<VpcBindPortVO> itemList = vpcService.getAllBindPort(vpcId);
		model.addAttribute("itemList", itemList);
		model.addAttribute("vpcId", vpcId);
		return "vpc_manage_network";
	}
	
	/**
	 * 添加端口配置页
	 * @param vpcId
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/{id}/addNetworkPage",method=RequestMethod.GET)
	public String addNetworkPage(@PathVariable("id") String vpcId, Model model,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.vpc_manage_network_add)){
			return "not_have_access";
		}
		List<VpcOuterIpVO> outerIpList = vpcService.getAllIpByVpcId(vpcId);
		List<CloudHostVO> hostList = cloudHostService.getAllHostByVpcId(vpcId);
		model.addAttribute("vpcId",vpcId);
		model.addAttribute("outerIpList",outerIpList);
		model.addAttribute("hostList",hostList);
		return "vpc_manage_network_add";
	}
	
	/**
	 * 添加端口配置项
	 * @param vpcId
	 * @param outerIp
	 * @param outerPort
	 * @param hostId
	 * @param port
	 * @param protocol
	 * @return
	 */
	@RequestMapping(value="/addNetwork",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult addNetwork(@RequestParam("vpcId") String vpcId,
			@RequestParam("outerIp") String outerIp,
			@RequestParam("outerPort") String outerPort,
			@RequestParam("hostId") String hostId,
			@RequestParam("port") String port,
			@RequestParam("protocol") String protocol,
			HttpServletRequest request){
		MethodResult mr = vpcService.addBindPortItem(vpcId, outerIp, outerPort, hostId, port, protocol);
		if(mr.isSuccess()){
            operLogService.addLog("专属云", "添加专属云"+mr.getProperty("name")+"端口配置成功", "1", "1", request);
        }else{
              operLogService.addLog("专属云", "添加专属云"+mr.getProperty("name")+"端口配置失败", "1", "2", request);
        }
		return mr;
	}
	
	/**
	 * 批量删除端口配置
	 * @param vpcId
	 * @param ids
	 * @return
	 */
	@RequestMapping(value="/deleteItems",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult deleteItems(@RequestParam("vpcId") String vpcId,
			@RequestParam("ids[]") String[] ids,
			HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.vpc_manage_network_delete)){
			return new MethodResult(MethodResult.FAIL,"您没有删除专属云端口配置的权限，请联系管理员");
		}
		List<String> idList = Arrays.asList(ids);
		MethodResult mr = vpcService.deleteBindPorts(vpcId, idList);
		if(mr.isSuccess()){
            operLogService.addLog("专属云", "删除专属云"+mr.getProperty("name")+"端口配置成功", "1", "1", request);
        }else{
              operLogService.addLog("专属云", "删除专属云"+mr.getProperty("name")+"端口配置失败", "1", "2", request);
        }
		return mr;
	}
	
	/**
	 * 跳转到主机列表页
	 * @param vpcId
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/{id}/hostList",method=RequestMethod.GET)
	public String hostList(@PathVariable("id") String vpcId, Model model,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.vpc_manage_host_page)){
			return "not_have_access";
		}
		List<CloudHostVO> hostList = vpcService.toVpcHostList(vpcId);
		model.addAttribute("hostList", hostList);
		model.addAttribute("vpcId", vpcId);
		return "vpc_manage_host";
	}
	
	/**
	 * 跳转到vpc增加主机页
	 * @param vpcId
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/{id}/addVpcHost",method=RequestMethod.GET)
	public String addVpcHostPage(@PathVariable("id") String vpcId,Model model,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.vpc_manage_host_add)){
			return "not_have_access";
		}
		List<SysDiskImageVO> list = sysDiskImageService.queryAllSysDiskImage();
		model.addAttribute("imageList",list);
		model.addAttribute("vpcId", vpcId);
		return "vpc_manage_host_add";
	}
	
	/**
	 * 添加vpc云主机
	 * @param vpcId
	 * @param cpuCore
	 * @param memory
	 * @param dataDisk
	 * @param bandwidth
	 * @param sysImageId
	 * @param hostAmount
	 * @return
	 */
	@RequestMapping(value="addHostToVpc",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult addHostToVpc(@RequestParam("vpcId") String vpcId,
			@RequestParam("cpuCore") String cpuCore,
			@RequestParam("memory") String memory,
			@RequestParam("dataDisk") String dataDisk,
			@RequestParam("bandwidth") String bandwidth,
			@RequestParam("sysImageId") String sysImageId,
			@RequestParam("hostAmount") String hostAmount,
			HttpServletRequest request){
		MethodResult mr = vpcService.addNewHostToVpcBaseInfo(vpcId, cpuCore, memory, dataDisk, bandwidth, sysImageId, hostAmount);
		if(mr.isSuccess()){
            operLogService.addLog("专属云", "专属云"+mr.getProperty("name")+"添加主机成功", "1", "1", request);
        }else{
              operLogService.addLog("专属云", "专属云"+mr.getProperty("name")+"添加主机失败", "1", "2", request);
        }
		return mr;
	}
	
	/**
	 * 启动云主机
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/{id}/startHost",method=RequestMethod.GET)
	@ResponseBody
	public MethodResult startCloudHost(@PathVariable("id") String id,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_warehouse_host_start)){
			return new MethodResult(MethodResult.FAIL,"您没有启动主机的权限，请联系管理员");
		}
		MethodResult mr = cloudHostService.operatorCloudHost(id, "1",false,0); 
		return mr;
	}
	/**
	 * 关闭云主机
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/{id}/shutdownHost",method=RequestMethod.GET)
	@ResponseBody
	public MethodResult shutdownCloudHost(@PathVariable("id") String id,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_warehouse_host_shut_down)){
			return new MethodResult(MethodResult.FAIL,"您没有强制关闭主机的权限，请联系管理员");
		}
		MethodResult mr = cloudHostService.operatorCloudHost(id, "2",false,0);
		return mr;
	}
	
	/**
	 * 重启云主机
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/{id}/restartHost",method=RequestMethod.GET)
	@ResponseBody
	public MethodResult restartCloudHost(@PathVariable("id") String id,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_warehouse_host_restart)){
			return new MethodResult(MethodResult.FAIL,"您没有重启主机的权限，请联系管理员");
		}
		MethodResult mr = cloudHostService.operatorCloudHost(id, "3",false,0);
		return mr;
	}
	/**
	 * 强制关闭云主机
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/{id}/haltHost",method=RequestMethod.GET)
	@ResponseBody
	public MethodResult haltCloudHost(@PathVariable("id") String id,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_warehouse_host_shut_down)){
			return new MethodResult(MethodResult.FAIL,"您没有强制关机的权限，请联系管理员");
		}
		MethodResult mr = cloudHostService.operatorCloudHost(id, "4",false,0);
		return mr;
	}
	
	/**
	 * 查询一个主机类型，返回至更新页
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/{id}/{status}/updateHost",method=RequestMethod.GET) 
	public String updataServerPage(@PathVariable("id") String id,@PathVariable("status") String status ,Model model,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.server_manage_modify)){
			return "not_have_access";
		}
		CloudHostVO cloudServer = cloudHostService.getById(id);
		List<SysDiskImageVO> list = sysDiskImageService.querySysDiskImageByImageType(AppConstant.DISK_IMAGE_TYPE_SERVER);
		model.addAttribute("cloudServer", cloudServer);
		model.addAttribute("imageList",list);
		model.addAttribute("runStatus",status);
		return "vpc_manage_host_update";
	}
	
	/**
	 * 更新主机配置
	 * @param server
	 * @return
	 */
	@RequestMapping(value="/updateHost",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult updateServer(CloudHostVO server){
		MethodResult mr = cloudHostService.modifyAllocation(server);
		return mr;
	}
	
	/**
	 * 解绑云主机
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/{id}/unbound",method=RequestMethod.GET)
	@ResponseBody
	public MethodResult unboundHost(@PathVariable("id") String id,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.vpc_manage_host_unbound)){
			return new MethodResult(MethodResult.FAIL,"您没有解绑专属云主机的权限，请联系管理员");
		}
		MethodResult mr = vpcService.unboundByHostId(id);
		if(mr.isSuccess()){
            operLogService.addLog("专属云", "专属云"+mr.getProperty("name")+"解除主机成功", "1", "1", request);
        }else{
              operLogService.addLog("专属云", "专属云"+mr.getProperty("name")+"解除主机失败", "1", "2", request);
        }
		return mr;
	}
	
	/**
	 * 批量删除vpc
	 * @param ids
	 * @return
	 */
	@RequestMapping(value="/deleteVpcByIds",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult deleteVpcByIds(@RequestParam("ids[]") String[] ids,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.vpc_manage_delete)){
			return new MethodResult(MethodResult.FAIL,"您没有删除专属云的权限，请联系管理员");
		}
		List<String> idList = new ArrayList<>();
		for(String s : ids){
			idList.add(s);
		}
		MethodResult mr = vpcService.deleteVpcByIds(idList);
		if(mr.isSuccess()){
            operLogService.addLog("专属云", "批量删除专属云"+mr.getProperty("name")+"主机成功", "1", "1", request);
        }else{
            operLogService.addLog("专属云", "批量删除专属云"+mr.getProperty("name")+"主机失败", "1", "2", request);
        }
		return mr;
	}
}

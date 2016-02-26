package com.zhicloud.ms.controller;

import java.io.IOException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody; 

import com.zhicloud.ms.common.util.json.JSONLibUtil;
import com.zhicloud.ms.httpGateway.HttpGatewayChannelExt;
import com.zhicloud.ms.httpGateway.HttpGatewayManager;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.IOperLogService;
import com.zhicloud.ms.transform.constant.TransFormPrivilegeConstant;
import com.zhicloud.ms.transform.util.TransFormPrivilegeUtil;
import com.zhicloud.ms.util.StringUtil;
import com.zhicloud.ms.vo.IpResourcePoolDetailVO;
import com.zhicloud.ms.vo.IpResourcePoolVO;

@Controller
@RequestMapping("/networkpool/ipresourcepool")
public class IpResourcePoolController {
    
    @Resource
    private IOperLogService operLogService;
    
	/**
	 * 
	* @Title: getAll 
	* @Description: 查询地址资源池 
	* @param @param model
	* @param @param request
	* @param @return      
	* @return String     
	* @throws
	 */
	@RequestMapping(value="/all",method=RequestMethod.GET)
	public String getAll(Model model,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.ip_resource_pool_query)){
			return "not_have_access";
		}
		try {
			List<IpResourcePoolVO> cList = new ArrayList<IpResourcePoolVO>();
				HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
				if(channel!=null){
					JSONObject  result = channel.addressPoolQuery();
					if("success".equals(result.get("status"))){					
						JSONArray ipList = result.getJSONArray("addressPools");
						for (int i = 0; i < ipList.size(); i ++) {
							JSONObject ipObject = ipList.getJSONObject(i);
							String uuid = ipObject.getString("uuid");
							String name = ipObject.getString("name");
							int status = ipObject.getInt("status");
	  						BigInteger[] count = JSONLibUtil.getBigIntegerArray(ipObject, "count");
	 						 
							IpResourcePoolVO address = new IpResourcePoolVO(); 
							address.setName(name);
							address.setStatus(status);
	 						address.setUuid(uuid);
	 						address.setCount(count);
							cList.add(address);
						}
					}
				}
			
			model.addAttribute("addressPool", cList);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "resourcepool/all_ip_resource_pool_manage";
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
	public String ipResourcePoolAdd(Model model,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.ip_resource_pool_add)){
			return "not_have_access";
		}
		return "resourcepool/ip_resource_pool_add";
	}

	
	/**
	 * 
	* @Title: add 
	* @Description: 保存创建地址资源池
	* @param @param name      
	* @return MethodResult     
	* @throws
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody 
	public MethodResult ipResourcePoolAdd(String name,HttpServletRequest request){	
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.ip_resource_pool_add)){
 			return new MethodResult(MethodResult.FAIL,"对不起，您无添加地址资源池的权限！");
		}		
		
		if(StringUtil.isBlank(name)){
            operLogService.addLog("地址资源池", "创建地址资源池失败", "1", "2", request);
			return new MethodResult(MethodResult.FAIL,"地址资源池名不能为空");
		}
		try {
			HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
			if(channel!=null){
				
				JSONObject  resultList = channel.addressPoolQuery();
				if("success".equals(resultList.get("status"))){					
					JSONArray ipList = resultList.getJSONArray("addressPools");
					for (int i = 0; i < ipList.size(); i ++) {
						JSONObject ipObject = ipList.getJSONObject(i);
						if  (ipObject.getString("name").equals(name)){
				            operLogService.addLog("地址资源池", "创建地址资源池失败", "1", "2", request);
							return new MethodResult(MethodResult.FAIL,"该名字的地址资源池已经存在");
						}
					}
				} 
//				else{
//                    operLogService.addLog("地址资源池", "创建地址资源池失败", "1", "2", request);
//
//					return new MethodResult(MethodResult.FAIL,"校验地址资源池名失败！");
//				}
				
				JSONObject result = channel.addressPoolCreate(name);
				if("success".equals(result.get("status"))){
                    operLogService.addLog("地址资源池", "创建地址资源池成功", "1", "1", request);
					return new MethodResult(MethodResult.SUCCESS,"创建成功");
				}
                operLogService.addLog("地址资源池", "创建地址资源池失败", "1", "2", request);
				return new MethodResult(MethodResult.FAIL,"创建失败: gateway返回失败！");
				
			}
	} catch (Exception e) {
		e.printStackTrace();
        operLogService.addLog("地址资源池", "创建地址资源池失败", "1", "2", request);

		return new MethodResult(MethodResult.FAIL,"创建失败: 调用 gateway失败！");
	}
        operLogService.addLog("地址资源池", "创建地址资源池失败", "1", "2", request);

	return new MethodResult(MethodResult.FAIL,"创建失败");
	}
	
	/**
	 * 
	* @Title: delete 
	* @Description: 删除地址资源池
	* @param @param uuid      
	* @return MethodResult     
	* @throws
	 */
	@RequestMapping(value="/rm",method=RequestMethod.POST) 
	@ResponseBody
	public MethodResult ipResourcePoolDelete(String uuid,HttpServletRequest request){	
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.ip_resource_pool_remove)){
			return new MethodResult(MethodResult.FAIL,"对不起，您无删除地址资源池的权限！");
		}		
		try {
			HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
			if(channel!=null){
				JSONObject result = channel.addressPoolDelete(uuid);
				if("success".equals(result.get("status"))){
	                operLogService.addLog("地址资源池", "删除地址资源池失败", "1", "2", request);
					return new MethodResult(MethodResult.SUCCESS,"删除地址资源池成功");
				}
                operLogService.addLog("地址资源池", "删除地址资源池失败", "1", "2", request);
				return new MethodResult(MethodResult.FAIL,"删除地址资源池: gateway返回失败");
			}
	} catch (Exception e) {
		e.printStackTrace();
        operLogService.addLog("地址资源池", "删除地址资源池失败", "1", "2", request);
		return new MethodResult(MethodResult.FAIL,"删除地址资源池：调用gateway失败!");
	}
        operLogService.addLog("地址资源池", "删除地址资源池失败", "1", "2", request);
        return new MethodResult(MethodResult.FAIL,"删除地址资源池失败");
	}
	
	/**
	 * 
	* @Title: ipResourceDetail 
	* @Description: 查询地址资源
	* @param @param name      
	* @return MethodResult     
	* @throws
	 */
	@RequestMapping(value="/{uuid}/qn",method=RequestMethod.GET)
	public String ipResourceDetail(@PathVariable("uuid") String uuid, Model model,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.ip_resource_pool_detail_query)){
			return "not_have_access";
		}
		
		try {
			
			List<IpResourcePoolDetailVO> cList = new ArrayList<IpResourcePoolDetailVO>();
			HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
			if(channel!=null){
				JSONObject result = channel.addressPoolQueryResource(uuid);
				if("success".equals(result.get("status"))){
					JSONArray addressesList = result.getJSONArray("addresses");
					for (int i = 0; i < addressesList.size(); i ++) {
						IpResourcePoolDetailVO ipVO = new IpResourcePoolDetailVO();
						JSONObject ipObject = addressesList.getJSONObject(i);
						ipVO.setIp(ipObject.getString("ip"));
						ipVO.setStatus(ipObject.getInt("status"));
						BigInteger[] count = JSONLibUtil.getBigIntegerArray(ipObject, "count");
						ipVO.setCount(count);
						cList.add(ipVO);
					}
				}
			}
			model.addAttribute("ipPoolDetailList", cList);
			model.addAttribute("poolId", uuid);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "resourcepool/ip_resource_pool_detail";
	}

	/**
	 * 删除地址资源(节点)
	 * @param ip
	 * @param poolId
	 * @return
	 */
	@RequestMapping(value="/dn",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult deleteNode(@RequestParam("ip") String ip,
								   @RequestParam("poolId") String poolId,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.ip_resource_pool_detail_remove)){
			return new MethodResult(MethodResult.FAIL,"对不起，您无移除地址资源的权限!");
		}		
		try {
			HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
			if(channel!=null){
				JSONObject dnr = channel.addressPoolRemoveResource(poolId, ip);
				if("success".equals(dnr.getString("status"))){
                    operLogService.addLog("地址资源池", "删除地址资源成功", "1", "1", request);
					return new MethodResult(MethodResult.SUCCESS,"地址资源移除成功");
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
            operLogService.addLog("地址资源池", "删除地址资源失败", "1", "2", request);
			return new MethodResult(MethodResult.FAIL,"地址资源移除：调用gateway失败!");
		} catch (IOException e) {
			e.printStackTrace();
            operLogService.addLog("地址资源池", "删除地址资源失败", "1", "2", request);
			return new MethodResult(MethodResult.FAIL,"地址资源移除：调用gateway失败!");
		}
        operLogService.addLog("地址资源池", "删除地址资源失败", "1", "2", request);
		return new MethodResult(MethodResult.FAIL,"地址资源移除：gateway返回失败！");
	}	
	
	
	/**
	 * 
	* @Title: add 
	* @Description: 打开新增地址资源页面 
	* @param @param model
	* @param @param request     
	* @return String     
	* @throws
	 */
	@RequestMapping(value="/{uuid}/an",method=RequestMethod.GET)
	public String ipResourcePoolAdd(@PathVariable("uuid") String uuid,Model model,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.ip_resource_pool_detail_add)){
			return "not_have_access";
		}
		model.addAttribute("poolId", uuid);
		return "resourcepool/ip_resource_pool_detail_add";
	}
	
	
	
	/**
	 * 
	* @Title: add 
	* @Description: 新增地址资源页面 
	* @param @param model
	* @param @param request     
	* @return String     
	* @throws
	 */
	@RequestMapping(value="/{uuid}/an",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult ipResourcePoolAdd(@PathVariable("uuid") String uuid,String ip,String range,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.ip_resource_pool_detail_add)){
			return new MethodResult(MethodResult.FAIL,"对不起，您无创建地址资源的权限!");
		}	
 		if(StringUtil.isBlank(ip)){
            operLogService.addLog("地址资源池", "新增地址资源失败", "1", "2", request);
			return new MethodResult(MethodResult.FAIL,"起始IP地址不能为空");
		}
		if(StringUtil.isBlank(range)){
            operLogService.addLog("地址资源池", "新增地址资源失败", "1", "2", request);
			return new MethodResult(MethodResult.FAIL,"IP数量不能为空");
		}
		try {
			HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
			if(channel!=null){
				String[] ips = new String[]{ip};
				Integer[] ranges = new Integer[]{Integer.valueOf(range)};
//				JSONObject result = channel.addressPoolAddResource(uuid, ips, ranges);
				JSONObject result = channel.addressPoolAddResource(uuid, ip, range);
				if("success".equals(result.getString("status"))){
		            operLogService.addLog("地址资源池", "新增地址资源成功", "1", "1", request);
					return new MethodResult(MethodResult.SUCCESS,"创建成功！");
				}
	            operLogService.addLog("地址资源池", "新增地址资源失败", "1", "2", request);
				return new MethodResult(MethodResult.FAIL,"创建地址资源：gateway返回"+result.getString("message"));
			}
	} catch (Exception e) {
		e.printStackTrace();
        operLogService.addLog("地址资源池", "新增地址资源失败", "1", "2", request);
		return new MethodResult(MethodResult.FAIL,"创建地址资源：调用gateway失败!");
		}
        operLogService.addLog("地址资源池", "新增地址资源失败", "1", "2", request);
	return new MethodResult(MethodResult.FAIL,"创建地址资源失败!");
	}
}

package com.zhicloud.ms.controller;

import com.zhicloud.ms.httpGateway.HttpGatewayChannelExt;
import com.zhicloud.ms.httpGateway.HttpGatewayManager;
import com.zhicloud.ms.httpGateway.HttpGatewayResponseHelper;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.IOperLogService;
import com.zhicloud.ms.transform.constant.TransFormPrivilegeConstant;
import com.zhicloud.ms.transform.util.TransFormPrivilegeUtil;
import com.zhicloud.ms.vo.DataNodeVO;
import com.zhicloud.ms.vo.StoragePoolVO;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/storageresourcepool")
public class StorageResourcePoolController {
    
    @Resource
    private IOperLogService operLogService;

    public static final Logger logger = Logger.getLogger(ResourcePoolController.class);


    @RequestMapping(value="/all",method=RequestMethod.GET)
	public String getAll(Model model,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.storage_resource_pool_query)){
			return "not_have_access";
		}
		try {
			List<StoragePoolVO> cList = new ArrayList<>();
				HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
				if(channel!=null){
					JSONObject result = channel.storagePoolQuery();
            if ("fail".equals(result.getString("status"))) {
                logger.error("StorageResourcePoolController.getAll>>>获取资源池失败");
                return "not_responsed";
            }
					JSONArray computerList = result.getJSONArray("storagePools");
					for (int i = 0; i < computerList.size(); i ++) {
						JSONObject computerObject = computerList.getJSONObject(i);
						String uuid = computerObject.getString("uuid");
						String name = computerObject.getString("name");
						int status = computerObject.getInt("status");
						Integer cpuCount = computerObject.getInt("cpu_count");
						BigDecimal cpuUsage = new BigDecimal(computerObject.getString("cpu_usage"));
						BigDecimal memoryUsage = new BigDecimal(computerObject.getString("memory_usage"));
						BigDecimal diskUsage = new BigDecimal(computerObject.getString("disk_usage"));
						
						JSONArray memoryList = computerObject.getJSONArray("memory");
						BigInteger[] mcount = new BigInteger[memoryList.size()];
						for(int j=0;j<memoryList.size();j++){
							mcount[j] = new BigInteger(memoryList.getString(j));
						}
						
						JSONArray diskList = computerObject.getJSONArray("disk_volume");
						BigInteger[] dcount = new BigInteger[diskList.size()];
						for(int j=0;j<diskList.size();j++){
							dcount[j] = new BigInteger(diskList.getString(j));
						}
						
						JSONArray nList = computerObject.getJSONArray("node");
						Integer[] ncount = new Integer[nList.size()];
						for(int j=0;j<nList.size();j++){
							ncount[j] = nList.getInt(j);
						}
						
						JSONArray hList = computerObject.getJSONArray("disk");
						Integer[] hcount = new Integer[hList.size()];
						for(int j=0;j<hList.size();j++){
							hcount[j] = hList.getInt(j);
						}
						StoragePoolVO storage = new StoragePoolVO();
						storage.setCpuCount(cpuCount);
						storage.setCpuUsage(cpuUsage);
						storage.setDiskUsage(diskUsage);
						storage.setDiskVolume(dcount);
 						storage.setMemory(mcount);
						storage.setMemoryUsage(memoryUsage);
						storage.setName(name);
						storage.setNode(ncount);
						storage.setStatus(status);
						storage.setUuid(uuid);
						storage.setRegion(1);
						storage.setDisk(hcount);
						cList.add(storage);
					}
				}
			
			model.addAttribute("computerPool", cList);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "resourcepool/all_storage_pool_manage";
	}
	
	@RequestMapping(value="/{id}/datanode",method=RequestMethod.GET)
	public String resourceDetail(@PathVariable("id") String uuid,Model model,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.storage_resource_pool_node_query)){
			return "not_have_access";
		}
		String searchName = request.getParameter("sn");
		try {
			List<DataNodeVO> cList = new ArrayList<>();
			List<DataNodeVO> curList = new ArrayList<>();
				HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
				if(channel!=null){
					JSONObject result = channel.storagePoolQueryResource(uuid);
            if ("fail".equals(result.getString("status"))) {
                logger.error("StorageResourcePoolController.resourceDetail>>>获取资源池失败");
                return "not_responsed";
            }
					JSONArray storageList = result.getJSONArray("storages");
					for (int i = 0; i < storageList.size(); i ++) {
						JSONObject computerObject = storageList.getJSONObject(i);
						String name = computerObject.getString("name");
						String ip = computerObject.getString("ip");
						int status = computerObject.getInt("status");
						Integer cpuCount = computerObject.getInt("cpu_count");
						BigDecimal cpuUsage = new BigDecimal(computerObject.getString("cpu_usage"));
						BigDecimal memoryUsage = new BigDecimal(computerObject.getString("memory_usage"));
						BigDecimal diskUsage = new BigDecimal(computerObject.getString("disk_usage"));
						
						JSONArray memoryList = computerObject.getJSONArray("memory");
						BigInteger[] mcount = new BigInteger[memoryList.size()];
						for(int j=0;j<memoryList.size();j++){
							mcount[j] = new BigInteger(memoryList.getString(j));
						}
						
						JSONArray diskList = computerObject.getJSONArray("disk_volume");
						BigInteger[] dcount = new BigInteger[diskList.size()];
						for(int j=0;j<diskList.size();j++){
							dcount[j] = new BigInteger(diskList.getString(j));
						}
						
						DataNodeVO dataNode = new DataNodeVO();
						dataNode.setCpuCount(cpuCount);
						dataNode.setCpuUsage(cpuUsage);
						dataNode.setDiskUsage(diskUsage);
						dataNode.setDiskVolume(dcount);
						dataNode.setMemory(mcount);
						dataNode.setMemoryUsage(memoryUsage);
						dataNode.setName(name);
						dataNode.setStatus(status);
						dataNode.setIp(ip);
						cList.add(dataNode);
					}
					if(searchName!=null && searchName!="" && cList.size()>0){
						for(DataNodeVO cp : cList){
							if(cp.getName()!=null && cp.getName().toLowerCase().contains(searchName.toLowerCase())){
								curList.add(cp);
							}
						}
					}else{
						curList = cList;
					}
				}
			
			model.addAttribute("dataNodeList", curList);
			model.addAttribute("poolId", uuid);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "resourcepool/storage_resource_pool_data_node";
	}
	
	@RequestMapping(value="/{name}/{id}/deletedn",method=RequestMethod.GET)
	@ResponseBody
	public MethodResult deleteDataNode(@PathVariable("name") String name,@PathVariable("id") String id,Model model,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.storage_resource_pool_node_delete)){
			return new MethodResult(MethodResult.FAIL,"您没有删除存储节点的权限，请联系管理员");
		}
 		try { 
			HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1); 
			JSONObject result = channel.storagePoolRemoveResource(id, name);
			if( HttpGatewayResponseHelper.isSuccess(result) ){ 
			    operLogService.addLog("存储资源池", "删除存储节点成功", "1", "1", request);
				return new MethodResult(MethodResult.SUCCESS, "删除成功");
			} 
  		} catch (Exception e) {
			e.printStackTrace();
		}  
        operLogService.addLog("存储资源池", "删除存储节点失败", "1", "2", request);
 		return new MethodResult(MethodResult.FAIL, "删除失败");
 	}

}

package com.zhicloud.ms.controller;


import com.zhicloud.ms.app.pool.computePool.ComputeInfo;
import com.zhicloud.ms.app.pool.serviceInfoPool.ServiceInfoExt;
import com.zhicloud.ms.app.pool.serviceInfoPool.ServiceInfoPool;
import com.zhicloud.ms.app.pool.serviceInfoPool.ServiceInfoPoolManager;
import com.zhicloud.ms.constant.AppInconstant;
import com.zhicloud.ms.constant.MonitorConstant;
import com.zhicloud.ms.httpGateway.HttpGatewayChannelExt;
import com.zhicloud.ms.httpGateway.HttpGatewayManager;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.ICloudHostService;
import com.zhicloud.ms.service.IPlatformResourceService;
import com.zhicloud.ms.service.SharedMemoryService;
import com.zhicloud.ms.transform.constant.TransFormPrivilegeConstant;
import com.zhicloud.ms.transform.util.TransFormPrivilegeUtil;
import com.zhicloud.ms.vo.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.util.*;


@Controller
public class PlatformResourceController {

    public static final Logger logger = Logger.getLogger(ResourcePoolController.class);

    @Resource
	  ICloudHostService cloudHostService;

    @Resource
    SharedMemoryService sharedMemoryService;

    @Resource
    IPlatformResourceService platformResourceService;

	/**
	 * 查询所有资源池
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/paltform/all",method=RequestMethod.GET)
	public String getAll(Model model,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.plat_resource_manage)){
			return "not_have_access";
		}
		try {
			List<StoragePoolVO> sList = new ArrayList<>();
			List<ComputeInfo> cList = new ArrayList<>();
			HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
			if(channel!=null){
				JSONObject result = channel.storagePoolQuery();
			if (!"fail".equals(result.getString("status"))){
//				logger.error("PlatformResourceController.getAll>>>获取资源池失败");
//				return "not_responsed";
				JSONArray storageList = result.getJSONArray("storagePools");
				for (int i = 0; i < storageList.size(); i ++) {
					JSONObject computerObject = storageList.getJSONObject(i);
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
					sList.add(storage);
				}
			}
				//查询主机资源池
				JSONObject cresult = channel.computePoolQuery();
				if ("success".equals(cresult.getString("status"))) {
					JSONArray computerList = cresult.getJSONArray("compute_pools");
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
						
						JSONArray hList = computerObject.getJSONArray("host");
						Integer[] hcount = new Integer[hList.size()];
						for(int j=0;j<hList.size();j++){
							hcount[j] = hList.getInt(j);
						}
              ComputeInfo computer = new ComputeInfo();
						computer.setCpuCount(cpuCount);
						computer.setCpuUsage(cpuUsage);
						computer.setDiskUsage(diskUsage);
						computer.setDiskVolume(dcount);
						computer.setHost(hcount);
						computer.setMemory(mcount);
						computer.setMemoryUsage(memoryUsage);
						computer.setName(name);
						computer.setNode(ncount);
						computer.setStatus(status);
						computer.setUuid(uuid);
						computer.setRegion(1);
						cList.add(computer);
					}
				}
			}
			BigDecimal cpuCount = new BigDecimal(0);
			BigDecimal cpuUsage = new BigDecimal(0);
			BigDecimal memoryCount = new BigDecimal(0);
			BigDecimal memoryUsage = new BigDecimal(0);
			BigDecimal diskCount = new BigDecimal(0);
			BigDecimal diskUsage = new BigDecimal(0);
			Integer totalDisk = 0;
			if(sList.size()>0){
				for(StoragePoolVO s : sList){
					cpuCount = cpuCount.add(new BigDecimal(s.getCpuCount()));
					cpuUsage = cpuUsage.add(new BigDecimal(s.getCpuCount()).multiply(s.getCpuUsage().divide(new BigDecimal(100))));
					memoryCount = memoryCount.add(new BigDecimal(s.getMemory()[1]));
					memoryUsage = memoryUsage.add(new BigDecimal(s.getMemory()[1].subtract(s.getMemory()[0])));
					diskCount = diskCount.add(new BigDecimal(s.getDiskVolume()[1]));
					diskUsage = diskUsage.add(new BigDecimal(s.getDiskVolume()[1].subtract(s.getDiskVolume()[0])));
					for(int i=0;i<s.getDisk().length;i++){
						totalDisk += s.getDisk()[i];
					}
				}
			}
			PlatformResourceMonitorVO pr = AppInconstant.platformResourceMonitorData.get(1);
			if(pr==null){
				model.addAttribute("pDisk", 0);
				model.addAttribute("pDiskRemain", 0);
				model.addAttribute("pDiskUsage", 0);
			}else{
				JSONArray pDiskList = pr.getDiskVolume();
				BigInteger[] pDCount = new BigInteger[pDiskList.size()];
				for(int j=0;j<pDiskList.size();j++){
					pDCount[j] = new BigInteger(pDiskList.getString(j));
				}
				BigDecimal pDiskUsage = new BigDecimal(pr.getDiskUsage().substring(0, pr.getDiskUsage().length()-2));
				model.addAttribute("pDisk", pDCount[1]);
				model.addAttribute("pDiskRemain", pDCount[1].subtract(pDCount[0]));
				model.addAttribute("pDiskUsage", pDiskUsage);
			}
			model.addAttribute("dCpuUsage", cpuUsage.compareTo(new BigDecimal(0))==0?0.00:cpuUsage.divide(cpuCount,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
			model.addAttribute("dMemoryUsage", memoryUsage.compareTo(new BigDecimal(0))==0?0.00:memoryUsage.divide(memoryCount,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
			model.addAttribute("dDiskUsage", diskUsage.compareTo(new BigDecimal(0))==0?0.00:diskUsage.divide(diskCount,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
			model.addAttribute("totalDisk", totalDisk);
			//云服务器临时参数
			BigDecimal sCpuCount = new BigDecimal(0);
			BigDecimal sCpuUsage = new BigDecimal(0);
			BigDecimal sMemoryCount = new BigDecimal(0);
			BigDecimal sMemoryUsage = new BigDecimal(0);
			BigDecimal sDiskCount = new BigDecimal(0);
			BigDecimal sDiskUsage = new BigDecimal(0);
			Integer totalServer = 0;
			//云桌面临时参数
			BigDecimal tCpuCount = new BigDecimal(0);
			BigDecimal tCpuUsage = new BigDecimal(0);
			BigDecimal tMemoryCount = new BigDecimal(0);
			BigDecimal tMemoryUsage = new BigDecimal(0);
			BigDecimal tDiskCount = new BigDecimal(0);
			BigDecimal tDiskUsage = new BigDecimal(0);
			Integer totalTop = 0;
			if(cList.size()>0){
				for(ComputeInfo c : cList){
					if(c.getName().contains("desktop_pool")){
						tCpuCount = tCpuCount.add(new BigDecimal(c.getCpuCount()));
						tCpuUsage = tCpuUsage.add(new BigDecimal(c.getCpuCount()).multiply(c.getCpuUsage()));
						tMemoryCount = tMemoryCount.add(new BigDecimal(c.getMemory()[1]));
						tMemoryUsage = tMemoryUsage.add(new BigDecimal(c.getMemory()[1].subtract(c.getMemory()[0])));
						tDiskCount = tDiskCount.add(new BigDecimal(c.getDiskVolume()[1]));
						tDiskUsage = tDiskUsage.add(new BigDecimal(c.getDiskVolume()[1].subtract(c.getDiskVolume()[0])));
						for(int i=0;i<c.getHost().length;i++){
							totalTop += c.getHost()[i];
						}
					}else{
						sCpuCount = sCpuCount.add(new BigDecimal(c.getCpuCount()));
						sCpuUsage = sCpuUsage.add(new BigDecimal(c.getCpuCount()).multiply(c.getCpuUsage()));
						sMemoryCount = sMemoryCount.add(new BigDecimal(c.getMemory()[1]));
						sMemoryUsage = sMemoryUsage.add(new BigDecimal(c.getMemory()[1].subtract(c.getMemory()[0])));
						sDiskCount = sDiskCount.add(new BigDecimal(c.getDiskVolume()[1]));
						sDiskUsage = sDiskUsage.add(new BigDecimal(c.getDiskVolume()[1].subtract(c.getDiskVolume()[0])));
						for(int i=0;i<c.getHost().length;i++){
							totalServer += c.getHost()[i];
						}
					}
				}
			}
			model.addAttribute("sCpuUsage",sCpuUsage.compareTo(new BigDecimal(0))==0?0.00:sCpuUsage.divide(sCpuCount,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
			model.addAttribute("sMemoryUsage", sMemoryUsage.compareTo(new BigDecimal(0))==0?0.00:sMemoryUsage.divide(sMemoryCount,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
			model.addAttribute("sDiskUsage", sDiskUsage.compareTo(new BigDecimal(0))==0?0.00:sDiskUsage.divide(sDiskCount,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
			model.addAttribute("totalServer", totalServer);
			model.addAttribute("tCpuUsage", tCpuUsage.compareTo(new BigDecimal(0))==0?0.00:tCpuUsage.divide(tCpuCount,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
			model.addAttribute("tMemoryUsage", tMemoryUsage.compareTo(new BigDecimal(0))==0?0.00:tMemoryUsage.divide(tMemoryCount,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
			model.addAttribute("tDiskUsage", tDiskUsage.compareTo(new BigDecimal(0))==0?0.00:tDiskUsage.divide(tDiskCount,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
			model.addAttribute("totalTop", totalTop);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "platform_resource_manage";
	}
	
	/**
	 * 查询平台资源监控信息
	 * @return
	 */
	@RequestMapping(value="/paltform/getpr",method=RequestMethod.POST)
	@ResponseBody
	public PlatformResourceMonitorVO getPlatformResource(){
		PlatformResourceMonitorVO pr = AppInconstant.platformResourceMonitorData.get(1);
		if(pr==null){
			pr = new PlatformResourceMonitorVO();
		}
		return pr;
	}
	
	/**
	 * 查询计算资源池
	 * @return
	 */
	@RequestMapping(value="/paltform/getcr",method=RequestMethod.POST)
	@ResponseBody
	public ComputeResourceViewVO getComputeResource(){
		ComputeResourceViewVO cVO = new ComputeResourceViewVO();
		try {
			List<ComputeInfo> cList = new ArrayList<>();
			HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
			if(channel!=null) {
          JSONObject result = channel.computePoolQuery();
          if ("success".equals(result.getString("status"))) {
              JSONArray computerList = result.getJSONArray("compute_pools");
              for (int i = 0; i < computerList.size(); i++) {
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
                  for (int j = 0; j < memoryList.size(); j++) {
                      mcount[j] = new BigInteger(memoryList.getString(j));
                  }

                  JSONArray diskList = computerObject.getJSONArray("disk_volume");
                  BigInteger[] dcount = new BigInteger[diskList.size()];
                  for (int j = 0; j < diskList.size(); j++) {
                      dcount[j] = new BigInteger(diskList.getString(j));
                  }

                  JSONArray nList = computerObject.getJSONArray("node");
                  Integer[] ncount = new Integer[nList.size()];
                  for (int j = 0; j < nList.size(); j++) {
                      ncount[j] = nList.getInt(j);
                  }

                  JSONArray hList = computerObject.getJSONArray("host");
                  Integer[] hcount = new Integer[hList.size()];
                  for (int j = 0; j < hList.size(); j++) {
                      hcount[j] = hList.getInt(j);
                  }
                  ComputeInfo computer = new ComputeInfo();
                  computer.setCpuCount(cpuCount);
                  computer.setCpuUsage(cpuUsage);
                  computer.setDiskUsage(diskUsage);
                  computer.setDiskVolume(dcount);
                  computer.setHost(hcount);
                  computer.setMemory(mcount);
                  computer.setMemoryUsage(memoryUsage);
                  computer.setName(name);
                  computer.setNode(ncount);
                  computer.setStatus(status);
                  computer.setUuid(uuid);
                  computer.setRegion(1);
                  cList.add(computer);
              }
          }
          BigDecimal cpuCount = new BigDecimal(0);
          BigDecimal cpuUsage = new BigDecimal(0);
          BigDecimal memoryCount = new BigDecimal(0);
          BigDecimal memoryUsage = new BigDecimal(0);
          if (cList.size() > 0) {
              for (ComputeInfo c : cList) {
                  cpuCount = cpuCount.add(new BigDecimal(c.getCpuCount()));
                  cpuUsage = cpuUsage.add(new BigDecimal(c.getCpuCount()).multiply(c.getCpuUsage()));
                  memoryCount = memoryCount.add(new BigDecimal(c.getMemory()[1]));
                  memoryUsage = memoryUsage.add(new BigDecimal(c.getMemory()[1].subtract(c.getMemory()[0])));
              }
          }
          if (cpuUsage.compareTo(BigDecimal.ZERO) == 0) {
              cVO.setCpuUsage(cpuUsage);
          } else {
              cVO.setCpuUsage(cpuUsage.divide(cpuCount, 2, BigDecimal.ROUND_HALF_UP)
                  .multiply(new BigDecimal(100)));
          }
          if (memoryUsage.compareTo(BigDecimal.ZERO) == 0) {
              cVO.setMemoryUsage(memoryUsage);
          } else {
              cVO.setMemoryUsage(memoryUsage.divide(memoryCount, 2, BigDecimal.ROUND_HALF_UP)
                  .multiply(new BigDecimal(100)));
          }
      }
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cVO;
	}
	
	/**
	 * 查询存储资源池
	 * @return
	 */
	@RequestMapping(value="/paltform/getsr",method=RequestMethod.POST)
	@ResponseBody
	public StorageResourceViewVO getStorageResource(){
		StorageResourceViewVO sVO = new StorageResourceViewVO();
		try {
			List<StoragePoolVO> sList = new ArrayList<>();
			HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
			if(channel!=null){
				JSONObject  result = channel.storagePoolQuery();
				if ("success".equals(result.getString("status"))) {
					JSONArray storageList = result.getJSONArray("storagePools");
					for (int i = 0; i < storageList.size(); i ++) {
						JSONObject computerObject = storageList.getJSONObject(i);
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
						sList.add(storage);
					}
				}
			}
			BigDecimal cpuCount = new BigDecimal(0);
			BigDecimal cpuUsage = new BigDecimal(0);
			BigDecimal memoryCount = new BigDecimal(0);
			BigDecimal memoryUsage = new BigDecimal(0);
			if(sList.size()>0){
				for(StoragePoolVO s : sList){
					cpuCount = cpuCount.add(new BigDecimal(s.getCpuCount()));
					cpuUsage = cpuUsage.add(new BigDecimal(s.getCpuCount()).multiply(s.getCpuUsage().divide(new BigDecimal(100))));
					memoryCount = memoryCount.add(new BigDecimal(s.getMemory()[1]));
					memoryUsage = memoryUsage.add(new BigDecimal(s.getMemory()[1].subtract(s.getMemory()[0])));
				}
			}
			if(cpuUsage.compareTo(BigDecimal.ZERO)==0){
				sVO.setCpuUsage(cpuUsage);
			}else{
				sVO.setCpuUsage(cpuUsage.divide(cpuCount,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
			}
			if(memoryUsage.compareTo(BigDecimal.ZERO)==0){
				sVO.setMemoryUsage(memoryUsage);
			}else{
				sVO.setMemoryUsage(memoryUsage.divide(memoryCount,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sVO;
	}
	
	/**
	 * 跳转到服务管理页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/mypaltform/service",method=RequestMethod.GET)
	public String servicePage(Model model,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.plat_service_manage)){
			return "not_have_access";
		}
		ServiceInfoPool pool = ServiceInfoPoolManager.singleton().getPool();
		ServiceInfoExt[] services = pool.getAll();
		List<ServiceInfoExt> serviceList = Arrays.asList(services);
		model.addAttribute("serviceList", serviceList);
        // 状态数量值
        int normal = 0, warning = 0, error = 0, stop = 0;
        String newstatus = "";
        if (serviceList != null && serviceList.size() > 0) {
            for (ServiceInfoExt service : serviceList) {
                newstatus = service.getNewStatus();
                //System.out.println("服务状态:"+newstatus);
                if (MonitorConstant.status_stop.equals(newstatus)) {
                    stop++;
                } else if (MonitorConstant.status_error.equals(newstatus)) {
                    error++;
                } else if (MonitorConstant.status_warn.equals(newstatus)) {
                    warning++;
                } else if (MonitorConstant.status_normal.equals(newstatus)) {
                    normal++;
                } else {
                    normal++;
                }
            }
        }
        JSONObject json = new JSONObject();
        json.put("normal", normal);
        json.put("warning", warning);
        json.put("error", error);
        json.put("stop", stop);
        model.addAttribute("statusdata", json);
		return "service_and_version_manage";
	}

    @RequestMapping(value="/mypaltform/service/mod",method=RequestMethod.GET)
    public String serviceModifyPage(Model model,HttpServletRequest request){

        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.plat_service_manage_mod)){
            return "not_have_access";
        }

        int type = Integer.valueOf(request.getParameter("type"));
        String target = request.getParameter("target");
        int diskType = Integer.parseInt(request.getParameter("disk_type"));

        ServiceInfoExt serviceInfo = new ServiceInfoExt();
        serviceInfo.setType(type);
        serviceInfo.setName(target);
        serviceInfo.setDiskType(diskType);

        model.addAttribute("service_info", serviceInfo);


        // 获取共享存储源列表
        List<SharedMemoryVO> sharedMemoryVOList = sharedMemoryService.queryInfo(null);

        model.addAttribute("shared_memory_vo_list", sharedMemoryVOList);

        return "service/service_and_version_mod";
    }

    @RequestMapping(value="/mypaltform/service/mod",method=RequestMethod.POST)
    public MethodResult serviceModify(ServiceInfoExt serviceInfo,HttpServletRequest request)
        throws IOException {

        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.plat_service_manage_mod)){
            return new MethodResult(MethodResult.FAIL,"您没有修改服务的权限，请联系管理员");
        }

        int type = serviceInfo.getType();
        String target = serviceInfo.getName();
        int diskType = serviceInfo.getDiskType();
        String diskSource = serviceInfo.getDiskSource();

        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("type", type);
        data.put("target", target);
        data.put("disk_type", diskType);
        data.put("disk_source", diskSource);

        MethodResult result = platformResourceService.modifyServiceSync(data);

        return new MethodResult(result.status, result.message);
    }


	@RequestMapping(value="/mypaltform/enableService",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult enableService(@RequestParam("name") String name,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.plat_service_enable)){
    		return new MethodResult(MethodResult.FAIL,"您没有启用服务的权限，请联系管理员");
		}
		MethodResult mr = new MethodResult(MethodResult.FAIL);
		HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
		try {
			channel.serviceEnable(name);
			String status = (String)AppInconstant.serviceEnableResult.get("status");
			String message = (String)AppInconstant.serviceEnableResult.get("message");
			mr.status = status;
			mr.message = message;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mr;
	}

	@RequestMapping(value="/mypaltform/disableService",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult disableService(@RequestParam("name") String name,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.plat_service_disable)){
    		return new MethodResult(MethodResult.FAIL,"您没有禁用服务的权限，请联系管理员");
		}
		MethodResult mr = new MethodResult(MethodResult.FAIL);
		HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
		try {
			channel.serviceDisable(name);
			String status = (String)AppInconstant.serviceDisableResult.get("status");
			String message = (String)AppInconstant.serviceDisableResult.get("message");
			mr.status = status;
			mr.message = message;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mr;
	}
}

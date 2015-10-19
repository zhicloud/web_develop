package com.zhicloud.ms.controller;

import com.zhicloud.ms.app.pool.computePool.ComputeInfoExt;
import com.zhicloud.ms.app.pool.computePool.ComputeInfoPool;
import com.zhicloud.ms.app.pool.computePool.ComputeInfoPoolManager;
import com.zhicloud.ms.httpGateway.HttpGatewayAsyncChannel;
import com.zhicloud.ms.httpGateway.HttpGatewayChannelExt;
import com.zhicloud.ms.httpGateway.HttpGatewayManager;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.ICloudHostService;
import com.zhicloud.ms.service.ICloudHostWarehouseService;
import com.zhicloud.ms.service.IComputePoolService;
import com.zhicloud.ms.service.IOperLogService;
import com.zhicloud.ms.transform.constant.TransFormPrivilegeConstant;
import com.zhicloud.ms.transform.util.TransFormPrivilegeUtil;
import com.zhicloud.ms.util.StringUtil;
import com.zhicloud.ms.vo.*;

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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/cdrpm")
public class ResourcePoolController {
 
    public static final Logger logger = Logger.getLogger(ResourcePoolController.class);

    @Resource
      ICloudHostService cloudHostService;

    @Resource
    IComputePoolService computePoolService;
    
    @Resource
    private ICloudHostWarehouseService  CloudHostWarehouseService;

    @Resource 
    
    private IOperLogService operLogService;
 

 
    /**
     * 查询所有资源池
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value="/all",method=RequestMethod.GET)
    public String getAll(Model model,HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_resource_pool_query)){
            return "not_have_access";
        }
        try {
            List<ComputerPoolVO> cList = new ArrayList<>();
                HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
                if(channel!=null){
                    JSONObject result = channel.computePoolQuery();
            if ("fail".equals(result.getString("status"))){
                logger.error("ResourcePoolController.getAll>>>获取资源池失败");
                return "not_responsed";
            }
                    JSONArray computerList = result.getJSONArray("compute_pools");
                    for (int i = 0; i < computerList.size(); i ++) {
                        JSONObject computerObject = computerList.getJSONObject(i);
                        String uuid = computerObject.getString("uuid");
                        String name = computerObject.getString("name");
                        if(!name.contains("desktop_pool")){
                            continue;
                        }
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
                        ComputerPoolVO computer = new ComputerPoolVO();
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
            
            model.addAttribute("computerPool", cList);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "resource_pool_manage";
    }
    
    /**
     * 跳转到资源(节点)列表页
     * @param uuid
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value="/{uuid}/rd",method=RequestMethod.GET)
    public String resourceDetail(@PathVariable("uuid") String uuid,Model model,HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_resource_node_query)){
            return "not_have_access";
        }
        String searchName = request.getParameter("sn");
        try {
            List<ComputerPoolDetailVO> cList = new ArrayList<>();
            List<ComputerPoolDetailVO> curList = new ArrayList<>();
            String flag = "no";
                HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
                if(channel!=null){
                    JSONObject result = channel.computePoolQueryResource(uuid);
            if ("fail".equals(result.getString("status"))){
                logger.error("ResourcePoolController.resourceDetail>>>获取资源池失败");
                return "not_responsed";
            }
                    JSONArray computerList = result.getJSONArray("compute_resources");
                    for (int i = 0; i < computerList.size(); i ++) {
                        JSONObject computerObject = computerList.getJSONObject(i);
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
                        
                        ComputerPoolDetailVO computer = new ComputerPoolDetailVO();
                        computer.setCpuCount(cpuCount);
                        computer.setCpuUsage(cpuUsage);
                        computer.setDiskUsage(diskUsage);
                        computer.setDiskVolume(dcount);
                        computer.setMemory(mcount);
                        computer.setMemoryUsage(memoryUsage);
                        computer.setName(name);
                        computer.setStatus(status);
                        computer.setIp(ip);
                        cList.add(computer);
                    }
                    if(searchName!=null && searchName!="" && cList.size()>0){
                        for(ComputerPoolDetailVO cp : cList){
                            if(cp.getName()!=null && cp.getName().toLowerCase().contains(searchName.toLowerCase())){
                                curList.add(cp);
                            }
                        }
                    }else{
                        curList = cList;
                    }
                    JSONObject remainNode = channel.computePoolQueryResource("");
                    JSONArray remainList = remainNode.getJSONArray("compute_resources");
                    if(remainList.size()>0) {
                        flag = "yes";
                    }
                }
            
            model.addAttribute("remain",flag);
            model.addAttribute("computerPoolDetail", curList);
            model.addAttribute("poolId", uuid);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "resourcepool/resource_pool_manage_detail";
    }
    
    /**
     * 跳转到主机列表页
     * @param uuid
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value="/{uuid}/{name}/hpdetail",method=RequestMethod.GET)
    public String hostPoolDetail(@PathVariable("uuid") String uuid,@PathVariable("name") String name,Model model,HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_resource_host_query)){
            return "not_have_access";
        }
        try {
                 List<CloudHostVO> cList = new ArrayList<>();
                HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
                if(channel!=null){
                    JSONObject result = channel.hostQuery(uuid);
                    if("success".equals(result.get("status"))){
                        
                        JSONArray computerList = result.getJSONArray("hosts");
                        for (int i = 0; i < computerList.size(); i ++) {
                            JSONObject computerObject = computerList.getJSONObject(i);
                            String hostName = computerObject.getString("name");
                            String uid = computerObject.getString("uuid");
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
                            
                            JSONArray ips = computerObject.getJSONArray("ip");
                            String[] hostIps = new String[ips.size()];
                            for(int j=0;j<ips.size();j++){
                                hostIps[j] = ips.getString(j);
                            }
                            CloudHostVO computer = new CloudHostVO();
                            computer.setCpuCore(cpuCount);
                            computer.setCpuUsage(cpuUsage.doubleValue());
                            computer.setDiskUsage(diskUsage.doubleValue());
                            computer.setSysDisk(dcount[1]);
                            computer.setMemory(mcount[1]);
                            computer.setMemoryUsage(memoryUsage.doubleValue());
                            computer.setHostName(hostName);
                            computer.setDisplayName(hostName);
                            computer.setStatus(status);
                            computer.setId(uid);
                            computer.setRealHostId(uid);
                            if(hostIps!=null && hostIps.length == 1){
                                computer.setInnerIp(hostIps[0]);
                            }else if(hostIps!=null && hostIps.length == 2){
                                computer.setInnerIp(hostIps[0]);
                                computer.setOuterIp(hostIps[1]);                                
                            }
                             cList.add(computer);
                        }
                    }
                    if ("fail".equals(result.getString("status"))){
                        logger.error("ResourcePoolController.hostPoolDetail>>>获取资源池失败");
                        return "not_responsed";
                    }
                }
                
                if(cList.size()>0){
                    for(CloudHostVO cp : cList){
                        CloudHostVO cloudHost = cloudHostService.getByRealHostId(cp.getRealHostId());
                        if(cloudHost!=null){
                            // 设置主机显示名称和类型
                            cp.setDisplayName(cloudHost.getDisplayName());
                            cp.setType(cloudHost.getType());
                        } 
                        
                    }
                }
            model.addAttribute("hostPoolDetail", cList);
            int poolType = 0;// 0表示默认
            if(name.contains("server_pool")){
                poolType = 1; //1表示云服务器的资源池
            }else if(name.contains("desktop_pool")){
                poolType = 2; // 2表示云桌面的资源池
                //筛选仓库
                model.addAttribute("wareHoseList",CloudHostWarehouseService.getAll());
            }
            model.addAttribute("poolType", poolType);
 
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "desktop/host_pool_detail";
    }
    
    /**
     * 跳转到创建资源池页面
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value="/add",method=RequestMethod.GET)
    public String addResourcePool(Model model,HttpServletRequest request){
//      if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_resource_pool_add)){
//          return "not_have_access";
//      }
        try {
            List<IpPoolVO> ipList = new ArrayList<>();
            List<PortPoolVO> portList = new ArrayList<>();
                HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
                if(channel!=null){
                    JSONObject result = channel.addressPoolQuery();
            if ("fail".equals(result.getString("status"))){
                logger.error("ResourcePoolController.addResourcePool>>>获取资源池失败");
                return "not_responsed";
            }
                    JSONObject resultPort = channel.portPoolQuery();
            if ("fail".equals(resultPort.getString("status"))){
                logger.error("ResourcePoolController.addResourcePool>>>获取端口失败");
                return "not_responsed";
            }

                    JSONArray IpPoolList = result.getJSONArray("addressPools");
                    JSONArray portPoolList = resultPort.getJSONArray("portPools");
                    for (int i = 0; i < IpPoolList.size(); i ++) {
                        JSONObject ipObject = IpPoolList.getJSONObject(i);
                        String name = ipObject.getString("name");
                        String uid = ipObject.getString("uuid");
                        int status = ipObject.getInt("status");
                        
                        JSONArray countList = ipObject.getJSONArray("count");
                        Integer[] ccount = new Integer[countList.size()];
                        for(int j=0;j<countList.size();j++){
                            ccount[j] = countList.getInt(j);
                        }
                        
                        IpPoolVO vo = new IpPoolVO();
                        vo.setName(name);
                        vo.setStatus(status);
                        vo.setUuid(uid);
                        vo.setCount(ccount);
                        ipList.add(vo);
                    }
                    for (int i = 0; i < portPoolList.size(); i ++) {
                        JSONObject portObject = portPoolList.getJSONObject(i);
                        String name = portObject.getString("name");
                        String uid = portObject.getString("uuid");
                        int status = portObject.getInt("status");
                        
                        JSONArray countList = portObject.getJSONArray("count");
                        Integer[] pcount = new Integer[countList.size()];
                        for(int j=0;j<countList.size();j++){
                            pcount[j] = countList.getInt(j);
                        }
                        
                        PortPoolVO vo = new PortPoolVO();
                        vo.setName(name);
                        vo.setStatus(status);
                        vo.setUuid(uid);
                        vo.setCount(pcount);
                        portList.add(vo);
                    }
                }
            model.addAttribute("ipList", ipList);
            model.addAttribute("portList", portList);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return "resourcepool/resource_pool_add";
    }
    
    /**
     * 创建资源池
     * @param name
     * @param networkType
     * @param networkId
     * @param diskType
     * @param diskId
     * @return
     */
    @RequestMapping(value="/add",method=RequestMethod.POST)
    @ResponseBody
    public MethodResult addResourcePool(String name,String networkType,String networkId,String diskType,String diskId,String prefixion,
    		 int mode0, int mode1, int mode2, int mode3,String path,String crypt){
        if(StringUtil.isBlank(name)){
            return new MethodResult(MethodResult.FAIL,"资源池名不能为空");
        }
        if(StringUtil.isBlank(networkType)){
            return new MethodResult(MethodResult.FAIL,"网络类型不能为空");
        }
        if(("1".equals(networkType) || "2".equals(networkType)) && StringUtil.isBlank(networkType)){
            return new MethodResult(MethodResult.FAIL,"IP或端口资源池不能为空");
        }
        if(StringUtil.isBlank(diskType)){
            return new MethodResult(MethodResult.FAIL,"存储类型不能为空");
        }
        if(!"0".equals(diskType) && StringUtil.isBlank(diskId)){
            return new MethodResult(MethodResult.FAIL,"资源池名不能为空");
        }
        Integer[] mode = new Integer[4];
        mode[0] = mode0;
        mode[1] = mode1;
        mode[2] = mode2;
        mode[3] = mode3;
        try {
        	HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
                if(channel!=null){
                    JSONObject result = channel.computePoolCreate(prefixion+name, Integer.parseInt(networkType), networkId, 
                    		                                          Integer.parseInt(diskType), diskId, mode, path, crypt);
                    if("success".equals(result.get("status"))){                    	
                        return new MethodResult(MethodResult.SUCCESS,"创建成功");
                    }
                    return new MethodResult(MethodResult.FAIL,"创建失败");
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new MethodResult(MethodResult.FAIL,"创建失败");
    }
    
    /**
     * 检查名字是否可用
     * @param name
     * @return
     */
    @RequestMapping(value="/checkAvaliable",method=RequestMethod.POST)
    @ResponseBody
    public MethodResult checkAvaliable(String name){
        boolean flag = true;
        try {
            HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
            if(channel!=null){
                JSONObject result = channel.computePoolQuery();
        if ("fail".equals(result.getString("status"))) {
            return new MethodResult(MethodResult.FAIL,"获取资源池失败");
        }
                JSONArray computerList = result.getJSONArray("compute_pools");
                for (int i = 0; i < computerList.size(); i ++) {
                    JSONObject computerObject = computerList.getJSONObject(i);
                    String oldName = computerObject.getString("name");
                    if(oldName.equals(name)){
                        flag = false;
                        break;
                    }
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }   
        if(flag){
            return new MethodResult(MethodResult.SUCCESS,"资源池名可用");
        }
        return new MethodResult(MethodResult.FAIL,"资源池名已存在");
    }

    /**
     * 修改资源池页面
     * @author 张翔
     * @param uuid
     * @param request
     * @return
     */
    @RequestMapping(value="/{uuid}/mod",method=RequestMethod.GET)
    public String modifyResourcePool(@PathVariable("uuid") String uuid, Model model, HttpServletRequest request)
        throws IOException {
        try {
            HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
            ComputeInfoExt computeInfoExt = computePoolService.getComputePoolDetailSync(uuid);
            if(channel!=null) {

                if (computeInfoExt == null) {
                    logger.error("ResourcePoolController.modifyResourcePool>>>获取资源池失败");
                    return "not_responsed";
                }
//
                computeInfoExt.setMode0(computeInfoExt.getMode()[0]);
                computeInfoExt.setMode1(computeInfoExt.getMode()[1]);
                computeInfoExt.setMode2(computeInfoExt.getMode()[2]);
                computeInfoExt.setMode3(computeInfoExt.getMode()[3]);
                
                List<IpPoolVO> ipList = new ArrayList<>();
                List<PortPoolVO> portList = new ArrayList<>();
                JSONObject resultIp = channel.addressPoolQuery();
                if ("fail".equals(resultIp.getString("status"))) {
                    logger.error("ResourcePoolController.modifyResourcePool>>>获取IP失败");
                    return "not_responsed";
                }
                JSONObject resultPort = channel.portPoolQuery();
                if ("fail".equals(resultPort.getString("status"))) {
                    logger.error("ResourcePoolController.modifyResourcePool>>>获取端口失败");
                    return "not_responsed";
                }

                JSONArray IpPoolList = resultIp.getJSONArray("addressPools");
                JSONArray portPoolList = resultPort.getJSONArray("portPools");
                for (int i = 0; i < IpPoolList.size(); i ++) {
                    JSONObject ipObject = IpPoolList.getJSONObject(i);
                    String name = ipObject.getString("name");
                    String uid = ipObject.getString("uuid");
                    int status = ipObject.getInt("status");

                    JSONArray countList = ipObject.getJSONArray("count");
                    Integer[] ccount = new Integer[countList.size()];
                    for(int j=0;j<countList.size();j++){
                        ccount[j] = countList.getInt(j);
                    }

                    IpPoolVO vo = new IpPoolVO();
                    vo.setName(name);
                    vo.setStatus(status);
                    vo.setUuid(uid);
                    vo.setCount(ccount);
                    ipList.add(vo);
                }
                for (int i = 0; i < portPoolList.size(); i ++) {
                    JSONObject portObject = portPoolList.getJSONObject(i);
                    String name = portObject.getString("name");
                    String uid = portObject.getString("uuid");
                    int status = portObject.getInt("status");

                    JSONArray countList = portObject.getJSONArray("count");
                    Integer[] pcount = new Integer[countList.size()];
                    for (int j = 0; j < countList.size(); j++) {
                        pcount[j] = countList.getInt(j);
                    }

                    PortPoolVO vo = new PortPoolVO();
                    vo.setName(name);
                    vo.setStatus(status);
                    vo.setUuid(uid);
                    vo.setCount(pcount);
                    portList.add(vo);
                }
                model.addAttribute("ipList", ipList);
                model.addAttribute("portList", portList);
                model.addAttribute("computeInfoExt", computeInfoExt);

                return "resourcepool/resource_pool_mod";
            }
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        return "not_responsed";
    }

    /**
     * 修改资源池
     * @author 张翔
     * @param computeInfoExt
     * @param request
     * @return
     */
    @RequestMapping(value="/mod",method=RequestMethod.POST)
    @ResponseBody
    public MethodResult modifyComputePool(ComputeInfoExt computeInfoExt, HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_resource_pool_mod)){
            return new MethodResult(MethodResult.FAIL,"您没有修改资源池的权限，请联系管理员");
        }
        try {
            //  获取参数
            String uuid = computeInfoExt.getUuid();
            String name = computeInfoExt.getName();
            Integer networkType = computeInfoExt.getNetworkType();
            String network = computeInfoExt.getNetwork();
            Integer diskType = computeInfoExt.getDiskType();
            String diskSource = computeInfoExt.getDiskSource();
            int mode0 = computeInfoExt.getMode0();
            int mode1 = computeInfoExt.getMode1();
            int mode2 = computeInfoExt.getMode2();
            int mode3 = computeInfoExt.getMode3();
            String path = computeInfoExt.getPath();
            String crypt = computeInfoExt.getCrypt();            
            if(StringUtil.isBlank(uuid)){
                return new MethodResult(MethodResult.FAIL,"资源池uuid不能为空");
            }
            if(StringUtil.isBlank(name)){
                return new MethodResult(MethodResult.FAIL,"资源池名不能为空");
            }
            if(StringUtil.isBlank(String.valueOf(networkType))){
                return new MethodResult(MethodResult.FAIL,"网络类型不能为空");
            }
            if(("1".equals(networkType) || "2".equals(networkType)) && StringUtil.isBlank(network)){
                return new MethodResult(MethodResult.FAIL,"IP或端口资源池不能为空");
            }
            if(StringUtil.isBlank(String.valueOf(diskType))){
                return new MethodResult(MethodResult.FAIL,"存储类型不能为空");
            }
            if("1".equals(diskType) && StringUtil.isBlank(diskSource)){
                return new MethodResult(MethodResult.FAIL,"云存储不能为空");
            }
            if(StringUtil.isBlank(String.valueOf(mode0))){
                return new MethodResult(MethodResult.FAIL,"高可用选项不能为空");
            }
            if(StringUtil.isBlank(String.valueOf(mode1))){
                return new MethodResult(MethodResult.FAIL,"自动Qos选项不能为空");
            }

            Map<String, Object> data = new LinkedHashMap<String, Object>();
            data.put("uuid", uuid);
            data.put("name", name);
            data.put("network_type", networkType);
            data.put("network", network);
            data.put("disk_type", diskType);
            data.put("disk_source", diskSource);
            data.put("mode0", mode0);
            data.put("mode1", mode1);
            data.put("mode2", mode2);
            data.put("mode3", mode3);            
            data.put("path", path);
            data.put("crypt", crypt);
            MethodResult result = computePoolService.modifyComputePoolSync(data);
            if("success".equals(result.status)){
                operLogService.addLog("桌面云资源池管理", "修改计算资源池", "1", "1", request);
                return new MethodResult(MethodResult.SUCCESS,"资源池修改成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            operLogService.addLog("桌面云资源池管理", "修改计算资源池", "1", "2", request);
            return new MethodResult(MethodResult.FAIL,"资源池修改失败");
        }

        operLogService.addLog("桌面云资源池管理", "修改计算资源池", "1", "2", request);
        return new MethodResult(MethodResult.FAIL,"资源池修改失败");
    }

    /**
     * 删除资源池
   * @author 张翔
 
	 * @param uuid
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/{uuid}/delete",method=RequestMethod.GET)
	@ResponseBody
	public MethodResult deleteComputePool(@PathVariable("uuid") String uuid,HttpServletRequest request){
       if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_resource_pool_remove)){
          return new MethodResult(MethodResult.FAIL,"您没有删除资源池的权限，请联系管理员");
      }
      try {
          MethodResult result = computePoolService.removeComputePool(uuid);
          if("success".equals(result.status)){
              operLogService.addLog("桌面云资源池管理", "删除计算资源池", "1", "1", request);
              return new MethodResult(MethodResult.SUCCESS,"资源池删除成功");
          }
      } catch (Exception e) {
          e.printStackTrace();
      }
      operLogService.addLog("桌面云资源池管理", "删除计算资源池", "1", "2", request);
      return new MethodResult(MethodResult.FAIL,"资源池删除失败");
    }
    
    /**
     * 创建资源(节点)页面
     * @param uuid
     * @param model
     * @return
     */
    @RequestMapping(value="/{uuid}/an",method=RequestMethod.GET)
    public String addNodePage(@PathVariable("uuid") String uuid,Model model){
        List<ComputerPoolDetailVO> curList = new ArrayList<>();
        HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
        try {
            if(channel!=null){
                JSONObject result = channel.computePoolQueryResource("");
          if ("fail".equals(result.getString("status"))){
              logger.error("ResourcePoolController.addNodePage>>>获取资源池失败");
              return "not_responsed";
          }
                JSONArray computerList = result.getJSONArray("compute_resources");
                for (int i = 0; i < computerList.size(); i ++) {
                    JSONObject computerObject = computerList.getJSONObject(i);
                    String name = computerObject.getString("name");
                    ComputerPoolDetailVO computer = new ComputerPoolDetailVO();
                    computer.setName(name);
                    curList.add(computer);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        model.addAttribute("nodeList", curList);
        model.addAttribute("poolId", uuid);
        return "resourcepool/resource_node_add";
    }
    
    /**
     * 创建资源(节点)
     * @param uuid
     * @param name
     * @return
     */
    @RequestMapping(value="/an",method=RequestMethod.POST)
    @ResponseBody
    public MethodResult addNode(String uuid,String name){
        if(StringUtil.isBlank(uuid)){
            return new MethodResult(MethodResult.FAIL,"资源池ID不能为空");
        }
        if(StringUtil.isBlank(name)){
            return new MethodResult(MethodResult.FAIL,"节点名不能为空");
        }
        try {
                HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
                if(channel!=null){
                    JSONObject result = channel.computePoolAddResource(uuid, name);
                    if("success".equals(result.get("status"))){
                        return new MethodResult(MethodResult.SUCCESS,"创建成功");
                    }
                    return new MethodResult(MethodResult.FAIL,"创建失败");
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new MethodResult(MethodResult.FAIL,"创建失败");
    }
    
    /**
     * 删除资源(节点)
     * @param name
     * @param ip
     * @param poolId
     * @return
     */
    @RequestMapping(value="/dn",method=RequestMethod.POST)
    @ResponseBody
    public MethodResult deleteNode(@RequestParam("name") String name,
            @RequestParam("ip") String ip,
            @RequestParam("poolId") String poolId){
        boolean flag = true;
        try {
                HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
                if(channel!=null){
                    JSONObject result = channel.hostQuery(poolId);
                    if("success".equals(result.get("status"))){
                        
                        JSONArray computerList = result.getJSONArray("hosts");
                        for (int i = 0; i < computerList.size(); i ++) {
                            JSONObject computerObject = computerList.getJSONObject(i);
                            String uid = computerObject.getString("uuid");
                            JSONArray ips = computerObject.getJSONArray("ip");
                            if(ip.equals(ips.getString(0))){
                                JSONObject dhr = channel.hostDelete(uid);
                                if("fail".equals(dhr.getString("status"))){
                                    flag = false;
                                }else{
                                	cloudHostService.deleteByRealId(uid);
                                }
                            }
                        }
                    }
                }
                if(flag){
                    JSONObject dnr = channel.computePoolRemoveResource(poolId, name);
                    if("success".equals(dnr.getString("status"))){
                        return new MethodResult(MethodResult.SUCCESS,"资源移除成功");
                    }
                }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new MethodResult(MethodResult.FAIL,"资源删除失败");
    }
     /**
     * 
    * @Title: startCloudHost 
    * @Description: 主机开机
    * @param @param id
    * @param @param request
    * @param @return      
    * @return MethodResult     
    * @throws
     */
    @RequestMapping(value="/{id}/start",method=RequestMethod.GET)
    @ResponseBody
    public MethodResult startCloudHost(@PathVariable("id") String id,HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_resource_pool_host_start)){
            return new MethodResult(MethodResult.FAIL,"您没有开机的权限，请联系管理员");
        }
        MethodResult mr = cloudHostService.operatorCloudHostByRealHostId(id, "1");
        return mr;
    }
    /**
     * 
    * @Title: stopCloudHost 
    * @Description: 主机关机 
    * @param @param id
    * @param @param request
    * @param @return      
    * @return MethodResult     
    * @throws
     */
    @RequestMapping(value="/{id}/stop",method=RequestMethod.GET)
    @ResponseBody
    public MethodResult stopCloudHost(@PathVariable("id") String id,HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_resource_pool_host_stop)){
            return new MethodResult(MethodResult.FAIL,"您没有关机的权限，请联系管理员");
        }
        MethodResult mr = cloudHostService.operatorCloudHostByRealHostId(id, "2");
        return mr;
    }
    /**
     * 
    * @Title: restartCloudHost 
    * @Description: 主机重启
    * @param @param id
    * @param @param request
    * @param @return      
    * @return MethodResult     
    * @throws
     */
    @RequestMapping(value="/{id}/restart",method=RequestMethod.GET)
    @ResponseBody
    public MethodResult restartCloudHost(@PathVariable("id") String id,HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_resource_pool_host_restart)){
            return new MethodResult(MethodResult.FAIL,"您没有重启的权限，请联系管理员");
        }
        MethodResult mr = cloudHostService.operatorCloudHostByRealHostId(id, "3");
        return mr;
    }
    /**
     * 
    * @Title: haltCloudHost 
    * @Description: 主机强制关机
    * @param @param id
    * @param @param request
    * @param @return      
    * @return MethodResult     
    * @throws
     */
    @RequestMapping(value="/{id}/halt",method=RequestMethod.GET)
    @ResponseBody
    public MethodResult haltCloudHost(@PathVariable("id") String id,HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_resource_pool_host_halt)){
            return new MethodResult(MethodResult.FAIL,"您没有强制的权限，请联系管理员");
        }
        MethodResult mr = cloudHostService.operatorCloudHostByRealHostId(id, "4");
        return mr;
    }
    /**
     * 
    * @Title: addServer 
    * @Description: 添加主机进云服务器 
    * @param @param id
    * @param @param request
    * @param @return      
    * @return MethodResult     
    * @throws
     */
    @RequestMapping(value="/{id}/addserver",method=RequestMethod.GET)
    @ResponseBody
    public MethodResult addServer(@PathVariable("id") String id,HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.compute_resource_pool_host_changetoserver)){
            return new MethodResult(MethodResult.FAIL,"您没有添加云服务器的权限，请联系管理员");
        }
        MethodResult mr = cloudHostService.addHostToServerByRealHostId(id);
        return mr;
    }
    /**
     * 
    * @Title: addDesktop 
    * @Description: 添加资源池主机进云桌面 
    * @param @param hostId
    * @param @param wareHouseId
    * @param @param request
    * @param @return      
    * @return MethodResult     
    * @throws
     */
    @RequestMapping(value="/{hostId}/{wareHouseId}/adddesktop",method=RequestMethod.GET)
    @ResponseBody
    public MethodResult addDesktop(@PathVariable("hostId") String hostId,@PathVariable("wareHouseId") String wareHouseId,HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_resource_pool_host_changetodesktop)){
            return new MethodResult(MethodResult.FAIL,"您没有添加云服务器的权限，请联系管理员");
        }
        MethodResult mr = cloudHostService.addHostToDeskTopByRealHostId(hostId, wareHouseId);
        return mr;
    }
}

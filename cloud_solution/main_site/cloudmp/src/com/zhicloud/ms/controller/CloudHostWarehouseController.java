package com.zhicloud.ms.controller;

import com.zhicloud.ms.app.helper.DefaultTreeNode;
import com.zhicloud.ms.httpGateway.HttpGatewayChannelExt;
import com.zhicloud.ms.httpGateway.HttpGatewayManager;
import com.zhicloud.ms.quartz.QuartzManage;
import com.zhicloud.ms.quartz.WarehouseJob;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.CloudHostConfigModelService;
import com.zhicloud.ms.service.ICloudHostWarehouseService;
import com.zhicloud.ms.transform.constant.TransFormPrivilegeConstant;
import com.zhicloud.ms.transform.util.TransFormPrivilegeUtil;
import com.zhicloud.ms.util.StringUtil;
import com.zhicloud.ms.vo.CloudHostConfigModel;
import com.zhicloud.ms.vo.CloudHostWarehouse;
import com.zhicloud.ms.app.pool.computePool.ComputeInfo;
import com.zhicloud.ms.app.pool.computePool.ComputeInfoExt;
import com.zhicloud.ms.app.pool.computePool.ComputeInfoPool;
import com.zhicloud.ms.app.pool.computePool.ComputeInfoPoolManager;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.quartz.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;

@Controller
@RequestMapping("/warehouse")
public class CloudHostWarehouseController {
    //日志
    public static final Logger logger = Logger.getLogger(CloudHostWarehouseController.class);
	
    @Resource
	ICloudHostWarehouseService cloudHostWarehouseService;
	@Resource
	CloudHostConfigModelService cloudHostConfigModelService;
	/**
	 * 查询所有云主机仓库
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/all",method=RequestMethod.GET)
	public String getAll(Model model,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_warehouse_query)){
			return "not_have_access";
		}
		List<CloudHostWarehouse> cloudHostWarehouseList = cloudHostWarehouseService.getAll();
		List<CloudHostConfigModel> chcmList = cloudHostConfigModelService.getAll();
		model.addAttribute("cloudHostWarehouseList", cloudHostWarehouseList);
		model.addAttribute("cloudHostConfigModeList",chcmList);
		try {

		    List<ComputeInfoExt> cList = new ArrayList<ComputeInfoExt>(); 
            ComputeInfoPool  pool = ComputeInfoPoolManager.singleton().getPool();
            Map<String, ComputeInfoExt>  poolMap = pool.getAllComputePool();
            for(Map.Entry<String, ComputeInfoExt> entry:poolMap.entrySet()){ 
                ComputeInfoExt poolDetail = entry.getValue();
                if(poolDetail.getName().indexOf("desktop_pool") != -1){
                    cList.add(entry.getValue());    
                }
            }   
            model.addAttribute("computerPool", cList); 
		} catch (Exception e) {
 			e.printStackTrace();
		}  
		return "warehouse_manage";
	}
	
	@RequestMapping(value="/add",method=RequestMethod.GET)
	public String addWarehouse(Model model,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_warehouse_add)){
			return "not_have_access";
		}
		List<CloudHostConfigModel> chcmList = cloudHostConfigModelService.getAll();
		model.addAttribute("cloudHostConfigModeList",chcmList);
		try {

			List<ComputeInfoExt> cList = new ArrayList<ComputeInfoExt>(); 
            ComputeInfoPool  pool = ComputeInfoPoolManager.singleton().getPool();
            Map<String, ComputeInfoExt>  poolMap = pool.getAllComputePool();
            for(Map.Entry<String, ComputeInfoExt> entry:poolMap.entrySet()){ 
                ComputeInfoExt poolDetail = entry.getValue();
                if(poolDetail.getName().indexOf("desktop_pool") != -1){
                    cList.add(entry.getValue());    
                }
            }   			
			model.addAttribute("computerPool", cList); 
		} catch (Exception e) {
 			e.printStackTrace();
		}
		return "warehouse_manage_add";
	}
	/**
	 * 添加一个云主机仓库
	 * @param chw
	 * @return
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult addWarehouse(CloudHostWarehouse chw,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_warehouse_add)){
			return new MethodResult(MethodResult.FAIL,"您没有新增镜像的权限，请联系管理员");
		}
		MethodResult mr = cloudHostWarehouseService.addWarehouse(chw,request);
		return mr;
	}
	
	@RequestMapping(value="/{id}/addAmount",method=RequestMethod.GET)
	public String addAmount(@PathVariable("id") String id,Model model,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_warehouse_add_host_amount)){
			return "not_have_access";
		}
		CloudHostWarehouse cloudHostWarehouse = cloudHostWarehouseService.getById(id);
		model.addAttribute("hostWarehouse",cloudHostWarehouse);
		try {

			List<ComputeInfo> cList = new ArrayList<ComputeInfo>();
				HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
				if(channel!=null){
					JSONObject result = channel.computePoolQuery();
            if ("fail".equals(result.getString("status"))){
                return "not_responsed";
            }
					JSONArray computerList = result.getJSONArray("compute_pools");
					for (int i = 0; i < computerList.size(); i ++) {
						JSONObject computerObject = computerList.getJSONObject(i);
						String name = computerObject.getString("name");
						if(!name.contains("desktop_pool")){
							continue;
						}
						String uuid = computerObject.getString("uuid");
						int status = computerObject.getInt("status");
						 
						ComputeInfo computer = new ComputeInfo(); 
						computer.setName(name);
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
		return "warehouse_manage_add_amount";
	}
	
	@RequestMapping(value="/addAmount",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult addAmount(String id,String addAmount,String poolId,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_warehouse_add_host_amount)){
			return new MethodResult(MethodResult.FAIL,"您没有增加主机数的权限，请联系管理员");
		}
		MethodResult mr = cloudHostWarehouseService.addAmount(id, addAmount,poolId);
		return mr;
	}
	/**
	 * 查询需要更新的对象，返回到页面显示给用户
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/{id}/update",method=RequestMethod.GET)
	public String getById(@PathVariable("id") String id,Model model,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_warehouse_modify)){
			return "not_have_access";
		}
		CloudHostWarehouse chw = cloudHostWarehouseService.getById(id);
		List<CloudHostConfigModel> chcmList = cloudHostConfigModelService.getAll();
		CloudHostConfigModel chcf = cloudHostConfigModelService.getById(chw.getCloudHostConfigModelId());
		if(chcf==null){
			chw.setCloudHostConfigModelId("0");
		}
		model.addAttribute("cloudHostConfigModeList",chcmList);
		model.addAttribute("warehouse", chw);
		return "warehouse_manage_update";
	}
	
	/**
	 * 通过名字查询对象，检查名字是否可用(对象存在即不可用，不存在即可用)
	 * @param name
	 * @return
	 */
	@RequestMapping(value="/checkname",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult getByName(@RequestParam("name") String name,HttpServletRequest request){
		MethodResult mr = new MethodResult();
		CloudHostWarehouse chw = cloudHostWarehouseService.getByName(name);
		if(chw==null){
			mr.status = "success";
		}else{
			mr.status = "fail";
		}
		return mr;
	}
	
	/**
	 * 修改主机仓库
	 * @param id
	 * @param chw
	 * @return
	 */
	@RequestMapping(value="/{id}/update",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult updateWarehouse(@PathVariable("id") String id,CloudHostWarehouse chw,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_warehouse_modify)){
			return new MethodResult(MethodResult.FAIL,"您没有修改仓库的权限，请联系管理员");
		}
		MethodResult mr = cloudHostWarehouseService.updateWarehouse(id, chw);
		return mr;
	}
	
	/**
	 * 删除仓库
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/{id}/delete",method=RequestMethod.GET)
	@ResponseBody
	public MethodResult deleteWarehouse(@PathVariable("id") String id,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_warehouse_delete)){
			return new MethodResult(MethodResult.FAIL,"您没有删除仓库的权限，请联系管理员");
		}
		return cloudHostWarehouseService.deleteWarehouse(id);
 	}
	
	/**
	 * 批量删除仓库
	 * @param
	 * @return
	 */
	@RequestMapping(value="/deleteIds",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult deleteWarehouses(@RequestParam("ids[]") String[] ids,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_warehouse_delete)){
			return new MethodResult(MethodResult.FAIL,"您没有删除仓库的权限，请联系管理员");
		}
		List<String> idList = Arrays.asList(ids);
		return cloudHostWarehouseService.deleteByIds(idList);
 	}
	
	/**
	 * 跳转到分配页面
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/{id}/assign",method=RequestMethod.GET)
	public String AssignWare(@PathVariable("id") String id,Model model,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_warehouse_allocate)){
			return "not_have_access";
		}
		CloudHostWarehouse cloudHostWarehouse = cloudHostWarehouseService.getById(id);
		model.addAttribute("warehouse", cloudHostWarehouse);
		model.addAttribute("byIds","no");
		return "warehouse_manage_assign";
	}
	
	@RequestMapping(value="/assign",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult AssignWareTwo(@RequestParam("warehouseId") String warehouseId, @RequestParam("ids[]") String[] ids,HttpSession session,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_warehouse_allocate)){
			return new MethodResult(MethodResult.FAIL,"您没有分配仓库的权限，请联系管理员");
		}
		CloudHostWarehouse cloudHostWarehouse = cloudHostWarehouseService.getById(warehouseId);
		session.setAttribute("ids", ids);
		session.setAttribute("idsSize", ids.length);
		session.setAttribute("byIds", "yes");
		session.setAttribute("warehouse",cloudHostWarehouse);
		return new MethodResult(MethodResult.SUCCESS,"成功");
	}
	/**
	 * 
	 * getWareHouseForDispatch:获取仓库列表，提供给分配使用
	 *
	 * @author sasa
	 * @return MethodResult
	 * @since JDK 1.7
	 */
	@RequestMapping(value="/getwarehousefordispatch",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult getWareHouseForDispatch(){
		List<CloudHostWarehouse> cloudHostWarehouseList = cloudHostWarehouseService.getAll();
		MethodResult result = new MethodResult(MethodResult.SUCCESS,""); 
		result.put("warehouseList", cloudHostWarehouseList);
		return result;
	}
	
	/**
	 * 获取用户树
	 * @return
	 */
	@RequestMapping(value="/tree",method=RequestMethod.GET)
	@ResponseBody
	public DefaultTreeNode[] getAllTrees(){
		DefaultTreeNode treeNode = cloudHostWarehouseService.getAllTree();
		return new DefaultTreeNode[]{treeNode};
	}
	@RequestMapping(value="/assigntousers",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult assignToUsers(@RequestParam("warehouseId") String warehouseId,
			@RequestParam("allNodes[]") String[] allNodes,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_warehouse_allocate)){
			return new MethodResult(MethodResult.FAIL,"您没有分配仓库的权限，请联系管理员");
		}
		MethodResult mr = cloudHostWarehouseService.assignToUsers(warehouseId, allNodes);
		return mr;
	}
	@RequestMapping(value="/assigntouserstwo",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult assignToUsersTwo(@RequestParam("warehouseId") String warehouseId,
			@RequestParam("allNodes[]") String[] allNodes,HttpSession session,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_warehouse_allocate)){
			return new MethodResult(MethodResult.FAIL,"您没有分配仓库的权限，请联系管理员");
		}
		String[] hostIds = (String[])session.getAttribute("ids");
		MethodResult mr = cloudHostWarehouseService.assignToUsersTwo(warehouseId, allNodes,hostIds);
		return mr;
	}
	
	/**
     * @Description:获取用户树
     * @param request
     * @param response
     * @throws
     */
    @RequestMapping("/assignTree")
    public void getAssignTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	String roleid = request.getParameter("roleid");
    	response.setHeader("content-type", "application/x-www-form-urlencoded;charset=utf-8");
        response.getWriter().print(JSONArray.fromObject(cloudHostWarehouseService.buildTreeJSON("all", new JSONArray(), roleid))
                .toString());

    }
    /**
     * 跳转到检查管理页面
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value="/{id}/checkpage",method=RequestMethod.GET)
    public String checkManagePage(@PathVariable("id") String id,Model model,HttpServletRequest request){
    	if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_warehouse_check_time)){
			return "not_have_access";
		}
    	CloudHostWarehouse chw = cloudHostWarehouseService.getById(id);
    	String sec = "无";
    	String min = "无";
    	String hour = "无";
    	if(chw.getCheckTime()==null || StringUtil.isBlank(chw.getCheckTime())){
    		model.addAttribute("sec",sec);
    		model.addAttribute("min",min);
    		model.addAttribute("hour",hour);
    	}else{
    		//checkTime存的是时分秒，分别获得相应时间用于前台显示
    		hour = chw.getCheckTime().substring(0, 2);
    		min = chw.getCheckTime().substring(2, 4);
    		sec = chw.getCheckTime().substring(4);
    		model.addAttribute("sec",sec);
    		model.addAttribute("min",min);
    		model.addAttribute("hour",hour);
    	}
    	if(chw.getMinimum()==null || chw.getMinimum()==0){
    		model.addAttribute("minimum",0);
    	}else{
    		model.addAttribute("minimum",chw.getMinimum());
    	}
    	model.addAttribute("total", chw.getTotalAmount());
    	model.addAttribute("id", id);
    	model.addAttribute("poolId", chw.getPoolId());
    	try {
			List<ComputeInfo> cList = new ArrayList<>();
				HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
				if(channel!=null){
					JSONObject result = channel.computePoolQuery();
            if ("fail".equals(result.getString("status"))){
                return "not_responsed";
            }
					JSONArray computerList = result.getJSONArray("compute_pools");
					for (int i = 0; i < computerList.size(); i ++) {
						JSONObject computerObject = computerList.getJSONObject(i);
						String name = computerObject.getString("name");
						if(!name.contains("desktop_pool")){
							continue;
						}
						String uuid = computerObject.getString("uuid");
						int status = computerObject.getInt("status");
						 
						ComputeInfo computer = new ComputeInfo(); 
						computer.setName(name);
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
    	return "warehouse_manage_check";
    }
    
    @RequestMapping(value="/updatecheck",method=RequestMethod.POST)
    @ResponseBody
    public MethodResult updateCheck(@RequestParam("minimum") String minimum,
    		@RequestParam("hour") String hour,
    		@RequestParam("min") String min,
    		@RequestParam("sec") String sec,
    		@RequestParam("id") String id,
    		@RequestParam("poolId") String poolId){
    	CloudHostWarehouse chw = cloudHostWarehouseService.getById(id);
    	if(hour==null){
    		return new MethodResult(MethodResult.FAIL, "小时值不能为空");
    	}
    	if(min==null){
    		return new MethodResult(MethodResult.FAIL, "分钟值不能为空");
    	}
    	if(sec==null){
    		return new MethodResult(MethodResult.FAIL, "秒值不能为空");
    	}
    	if(hour.length()==1){
    		hour = "0"+hour;
    	}
    	if(min.length()==1){
    		min = "0"+min;
    	}
    	if(sec.length()==1){
    		sec = "0"+sec;
    	}
    	if(chw==null){
    		return new MethodResult(MethodResult.FAIL, "找不到相应仓库");
    	}
    	//用于检查输入的时间与数据库时间是否相同
    	String checkTime = hour+min+sec;
    	//用于设置新的出发时间
    	String time = sec+" "+min+" "+hour+" * * ? *";
    	try {
    		JobDetail jdCheck = QuartzManage.getQuartzManage().getScheduler().getJobDetail(new JobKey(id,"groupJob"));
    		//判断最小库存是否为0,为0则不会定时检测
    		if(Integer.parseInt(minimum)==0){
    			//判断任务是否存在,存在则删除
    			if(jdCheck!=null){
    				//删除任务
    				QuartzManage.getQuartzManage().deleteTrigger(new JobKey(id,"groupJob"),new TriggerKey(id,"groupTrigger"));
    			}
    			//修改数据库数据
    			Map<String,Object> condition = new HashMap<>();
    			condition.put("checkTime", checkTime);
    			condition.put("minimum", minimum);
    			condition.put("poolId", poolId);
    			condition.put("id", id);
    			MethodResult mr = cloudHostWarehouseService.updateCheckTimeOrMinimum(condition);
    			return mr;
    		}else{//库存不为0时，判断是否有任务，没任务则添加，有任务则修改
    			//没有任务则添加任务
    			if(jdCheck==null){
    				//定义任务
    				JobDetail jd = JobBuilder.newJob(WarehouseJob.class)
    						.withIdentity(new JobKey(id,"groupJob"))
    						.usingJobData("id", id)
    						.requestRecovery(true)
    						.build();
    				//定义触发器
    				CronTrigger ct = (CronTrigger)TriggerBuilder.newTrigger()
    						.withIdentity(new TriggerKey(id,"groupTrigger"))
    						.withSchedule(CronScheduleBuilder.cronSchedule(time).withMisfireHandlingInstructionDoNothing())
    						.startNow()
    						.build();
    				//添加任务
    				QuartzManage.getQuartzManage().addTrigger(jd, ct);
    			}else{//存在任务则修改任务
    				//如果时间不相同则修改任务
    				if(!checkTime.equals(chw.getCheckTime())){
        				//定义触发器
        				CronTrigger ct = (CronTrigger)TriggerBuilder.newTrigger()
        						.withIdentity(new TriggerKey(id,"groupTrigger"))
        						.withSchedule(CronScheduleBuilder.cronSchedule(time).withMisfireHandlingInstructionDoNothing())
        						.startNow()
        						.build();
        				//修改任务出发的时间规则
        				QuartzManage.getQuartzManage().updateTrigger(new TriggerKey(id,"groupTrigger"), ct);
    				} //else do nothing
    			}
    			//修改数据库数据
    			Map<String,Object> condition = new HashMap<>();
    			condition.put("checkTime", checkTime);
    			condition.put("minimum", minimum);
    			condition.put("poolId", poolId);
    			condition.put("id", id);
    			MethodResult mr = cloudHostWarehouseService.updateCheckTimeOrMinimum(condition);
    			return mr;
    		}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
    	return new MethodResult(MethodResult.FAIL, "更新失败");
    }
    
    /**
     * 通过poolId查询是否是thin模式的资源池
     * @param name
     * @return
     */
    @RequestMapping(value="/checkpoolisthin",method=RequestMethod.POST)
    @ResponseBody
    public MethodResult checkPoolIsThin(@RequestParam("id") String id,HttpServletRequest request){
         ComputeInfoPool  pool = ComputeInfoPoolManager.singleton().getPool();
        ComputeInfoExt poolDetail = pool.getDuplicationFromComputePool(id);  
        if(poolDetail!=null && poolDetail.getMode2() == 1){
            return new MethodResult(MethodResult.SUCCESS,"是thin");
        }else{
            return new MethodResult(MethodResult.FAIL,"不是thin");
        }
     }
    
    /**
     * 跳转到最大并发创建数设置页面
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/beforeconcurrent", method = RequestMethod.GET)
    public String beforeConcurrent(HttpServletRequest request) {
        if (!new TransFormPrivilegeUtil().isHasPrivilege(request,
                TransFormPrivilegeConstant.desktop_warehouse_set_maxconcurrent)) {
            return "not_have_access";
        }
        try {
            // 获取数据库保存信息
            List<CloudHostWarehouse> lists = cloudHostWarehouseService.getAllConcurrent();

            ComputeInfoPool pool = ComputeInfoPoolManager.singleton().getPool();
            Map<String, ComputeInfoExt> poolMap = pool.getAllComputePool();
            Set<String> pool_keys = poolMap.keySet();
            Iterator<String> its = pool_keys.iterator();
            JSONArray pool_arrays = new JSONArray();

            while (its.hasNext()) {
                ComputeInfoExt ext = poolMap.get(its.next());
                JSONObject obj = new JSONObject();
                obj.put("uuid", ext.getUuid());
                obj.put("name", ext.getName());
                // 循环比对
                for (CloudHostWarehouse cloud : lists) {
                    if (ext.getUuid().equals(cloud.getPoolId())) {
                        obj.put("max_creating", cloud.getMax_creating());
                        break;
                    } else {
                        obj.put("max_creating", "");
                    }
                }
                pool_arrays.add(obj);
            }
            request.setAttribute("lists", pool_arrays);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "warehouse_concurrent";
    }
    
    /**
     * @Description:保存资源池最大并发创建数
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/savemaxconcurrent", method = RequestMethod.POST)
    @ResponseBody
    public MethodResult saveMaxConcurrent(@RequestParam(value = "data", defaultValue = "") String data, HttpServletRequest request) {
        logger.debug("CloudHostWarehouseController.saveMaxConcurrent()");
        JSONObject json = JSONObject.fromObject(data);
        if (json != null && !json.isEmpty()) {
            try {
                Map<String, Object> map = new LinkedHashMap<String, Object>();
                map.put("pool_id", json.getString("pool_id"));
                map.put("pool_name", json.getString("pool_name"));
                map.put("max_creating", json.getString("max_creating"));
                Integer re = cloudHostWarehouseService.saveConcurrent(map);
                if (re > 0) {
                    return new MethodResult(MethodResult.SUCCESS, "保存成功");
                } else {
                    return new MethodResult(MethodResult.FAIL, "保存失败,请联系管理员");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return new MethodResult(MethodResult.FAIL, "保存失败,请联系管理员");
            }
        }
        return new MethodResult(MethodResult.FAIL, "参数不能为空");
    }
}


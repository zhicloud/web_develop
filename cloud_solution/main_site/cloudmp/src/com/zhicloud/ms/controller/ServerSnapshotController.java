/**
 * Project Name:CloudDeskTopMS
 * File Name:ImageController.java
 * Package Name:com.zhicloud.ms.controller
 * Date:2015年3月17日下午1:24:40
 * 
 *
*/ 

package com.zhicloud.ms.controller; 

import java.io.IOException; 
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.util.LinkedList;
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

import com.zhicloud.ms.app.pool.snapshot.SnapshotManager;
import com.zhicloud.ms.app.pool.snapshot.SnapshotRequest;
import com.zhicloud.ms.common.util.StringUtil;
import com.zhicloud.ms.httpGateway.HttpGatewayAsyncChannel;
import com.zhicloud.ms.httpGateway.HttpGatewayChannelExt;
import com.zhicloud.ms.httpGateway.HttpGatewayManager;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.IOperLogService;
import com.zhicloud.ms.transform.constant.TransFormPrivilegeConstant;
import com.zhicloud.ms.transform.util.TransFormPrivilegeUtil; 
import com.zhicloud.ms.vo.Snapshot;

/**
 * ClassName: ImageController 
 * Function: 云服务器快照相关操作
 * date: 2015年3月17日 下午1:24:40 
 *
 * @author chenjinhua
 * @version 
 * @since JDK 1.7
 */
@Controller
@RequestMapping("/snapshot")
public class ServerSnapshotController {
	
   @Resource
    private IOperLogService operLogService;
	/**
	 * allDiskSnapshot:查询全磁盘快照列表信息
	 * @author chenjinhua
	 * @param id
	 * @param model
	 * @return String
	 * @since JDK 1.7
	 */
	@RequestMapping(value="/{id}/query",method=RequestMethod.GET) 
	public String getAllDiskSnapshot(@PathVariable("id") String id,String idx, Model model, HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.server_snapshot_query)){
			return "not_have_access";
		}
		model.addAttribute("realId", id);
		model.addAttribute("idx", idx);
		//测试数据-------------------------------------------------------
		List<Snapshot> list2 = new LinkedList<Snapshot>();
		Snapshot vo= new Snapshot();
		vo.setId("9340934309843095eA");
		vo.setTimestamp("2015-10-12 23:00:22");   						
		list2.add(vo);
		Snapshot vo2= new Snapshot();
		vo2.setId("9340xxxxxxxxxxxxxxxx");
		vo2.setTimestamp("2015-10-13 11:00:24");   						
		list2.add(vo2);
		model.addAttribute("snapshotList", list2);
		
		BigInteger[] dList2 =  new BigInteger[2];;
		dList2[0] = new BigInteger("0");
		dList2[1] = new BigInteger("1");
		
		if (StringUtil.isBlank(idx)){ 
			model.addAttribute("mode", 0);
		}
		else{
			model.addAttribute("mode", 1);
		}
			
		model.addAttribute("diskList", dList2);
		SnapshotRequest snapshotReq = new SnapshotRequest();
		snapshotReq.setCommand("create");
		snapshotReq.setLevel(30);		
		model.addAttribute("runing", snapshotReq); 
		
		//测试数据结束-----------------------------------------------
		try {
			HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
			BigInteger[] dList = null;
			if(channel!=null){
				JSONObject result = channel.hostQueryInfo(id);
				if("fail".equals(result.getString("status"))){
					model.addAttribute("msg", "查询磁盘失败！");
					return "snapshot/snapshot_manage";
				}
				JSONObject hostObject = result.getJSONObject("host");
				JSONArray diskList = hostObject.getJSONArray("disk_volume");
				BigInteger[] dcount = new BigInteger[diskList.size()];
				for(int j=0;j<diskList.size();j++){
					dcount[j] = new BigInteger(diskList.getString(j));
				}
				dList = dcount;
			}

			model.addAttribute("diskList", dList);
			model.addAttribute("idx", idx);
			
			long stamp = System.currentTimeMillis();
			HttpGatewayAsyncChannel asyncChannel =  HttpGatewayManager.getAsyncChannel(1);
			if(asyncChannel!=null){
				JSONObject ret = null;
				SnapshotManager.singleton().newQeruyList(asyncChannel.getSessionId(), stamp);
				if (StringUtil.isBlank(idx)){ 
					ret = asyncChannel.snapshotQuery(0, id, 0, 0); //mode=0  全主机磁盘 
					model.addAttribute("mode", 0);
				}else{
					ret = asyncChannel.snapshotQuery(0, id, 1,Integer.valueOf(idx)); //mode=1指定磁盘
					model.addAttribute("mode", 1);
				}
				if("fail".equals(ret.getString("status"))){
					model.addAttribute("msg", "查询磁盘失败！");
					return "snapshot/snapshot_manage";
				}
				
				List<Snapshot> list = SnapshotManager.singleton().getSnapShotVoList(asyncChannel.getSessionId(), stamp);
				model.addAttribute("snapshotList", list);
				model.addAttribute("runing", SnapshotManager.singleton().checkTasking(id)); //检查是否有操作正在进行中	
			} 
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "snapshot/snapshot_manage";
	}
	
	
	/**
	 * 删除快照
	 * @param target 主机ID
	 * @param curId  快照ID
	 * @param mode   模式0全局 1 单个磁盘
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/del",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult delSnapshot(@RequestParam("target") String target,
								   @RequestParam("snapshotId") String snapshotId,
								   @RequestParam("mode") String mode,
								   HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.server_snapshot_delete)){
			return new MethodResult(MethodResult.FAIL,"对不起，您无删除快照的权限!");
		}		
		try {
			HttpGatewayAsyncChannel channel = HttpGatewayManager.getAsyncChannel(1);	
			if(channel!=null){
				long stamp = System.currentTimeMillis();
				//添加异步任务
				SnapshotManager.singleton().newSnapshotTask(channel.getSessionId(), stamp,target, "delete");
				
				JSONObject dnr = channel.snapshotDel(0, target, Integer.valueOf(mode), snapshotId);
				if("success".equals(dnr.getString("status"))){
                    operLogService.addLog("快照管理", "提交删除快照成功", "1", "1", request);
				} else{
					//移除异步任务
					SnapshotManager.singleton().removeSnapshotTask(target, channel.getSessionId());
                    operLogService.addLog("快照管理", "删除快照失败", "1", "2", request);
					return new MethodResult(MethodResult.FAIL,"删除快照失败!");		
			    }
                
				String ret = SnapshotManager.singleton().getDelResltSnapShot(channel.getSessionId(), stamp);
                SnapshotManager.singleton().removeSnapshotTask(target, channel.getSessionId());
                
                //ret 为空，说明删除正常。
                if (!StringUtil.isBlank(ret)){
                	operLogService.addLog("快照管理", "删除快照失败", "1", "2", request);
 					return new MethodResult(MethodResult.FAIL,"删除快照失败!返回原因："+ret);	
                }
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
            operLogService.addLog("快照管理", "删除快照失败", "1", "2", request);
			return new MethodResult(MethodResult.FAIL,"删除快照失败：调用gateway失败!");
		} catch (IOException e) {
			e.printStackTrace();
            operLogService.addLog("快照管理", "删除快照失败", "1", "2", request);
			return new MethodResult(MethodResult.FAIL,"删除快照失败：调用gateway失败!");
		}
        operLogService.addLog("快照管理", "删除快照成功！", "1", "2", request);
		return new MethodResult(MethodResult.SUCCESS,"删除快照成功！");
	}	

	/**
	 * 删除快照
	 * @param target 主机ID
	 * @param curId  快照ID
	 * @param mode   模式0全局 1 单个磁盘
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/delMutil",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult delMutil(@RequestParam("target") String target,
								   @RequestParam("snapshotId") String snapshotId,
								   @RequestParam("mode") String mode,
								   HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.server_snapshot_delete)){
			return new MethodResult(MethodResult.FAIL,"对不起，您无删除快照的权限!");
		}		
		try {
			HttpGatewayAsyncChannel channel = HttpGatewayManager.getAsyncChannel(1);	
			if(channel!=null){
				//添加异步任务
				String[] snapshotIds = snapshotId.split(",");
				for (int i=0;i<snapshotIds.length;i++){
					long stamp = System.currentTimeMillis();
					SnapshotManager.singleton().newSnapshotTask(channel.getSessionId(), stamp, target, "delete");
					JSONObject dnr = channel.snapshotDel(0, target, Integer.valueOf(mode), snapshotIds[i]);
					if("success".equals(dnr.getString("status"))){
	                    operLogService.addLog("快照管理", "提交批量删除快照成功", "1", "1", request);
					} else{
						//移除异步任务
						SnapshotManager.singleton().removeSnapshotTask(target, channel.getSessionId());
	                    operLogService.addLog("快照管理", "批量删除快照失败", "1", "2", request);
						return new MethodResult(MethodResult.FAIL,"批量删除快照失败!");		
				    }                
					String ret = SnapshotManager.singleton().getDelResltSnapShot(channel.getSessionId(), stamp);
		            SnapshotManager.singleton().removeSnapshotTask(target, channel.getSessionId());   
					//ret 为空，说明删除正常。
	                if (!StringUtil.isBlank(ret)){
	                	operLogService.addLog("快照管理", "批量删除快照失败", "1", "2", request);
	 					return new MethodResult(MethodResult.FAIL,"批量删除快照失败!返回原因："+ret);	
	                }
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
            operLogService.addLog("快照管理", "批量删除快照失败", "1", "2", request);
			return new MethodResult(MethodResult.FAIL,"批量删除快照失败：调用gateway失败!");
		} catch (IOException e) {
			e.printStackTrace();
            operLogService.addLog("快照管理", "批量删除快照失败", "1", "2", request);
			return new MethodResult(MethodResult.FAIL,"批量删除快照失败：调用gateway失败!");
		}
        operLogService.addLog("快照管理", "批量删除快照成功！", "1", "2", request);
		return new MethodResult(MethodResult.SUCCESS,"批量删除快照成功！");
	}	
	
	
	/**
	 * 恢复快照
	 * @param target 主机ID
	 * @param curId  快照ID
	 * @param mode   模式0全局 1 单个磁盘
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/resume",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult resumeSnapshot(@RequestParam("target") String target,
								       @RequestParam("snapshotId") String snapshotId,
								       @RequestParam("mode") String mode,
								       HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.server_snapshot_resume)){
			return new MethodResult(MethodResult.FAIL,"对不起，您无恢复快照的权限!");
		}		
		try {
			HttpGatewayAsyncChannel channel = HttpGatewayManager.getAsyncChannel(1);	
			if(channel!=null){
				long stamp = System.currentTimeMillis();
				//添加异步任务
				SnapshotManager.singleton().newSnapshotTask(channel.getSessionId(), stamp, target, "resume");
				
				//
				JSONObject object = channel.snapshotResume(0, target, Integer.valueOf(mode), snapshotId);
				if("success".equals(object.getString("status"))){
                    operLogService.addLog("快照管理", "提交恢复快照成功", "1", "1", request);
				} else{
					//移除异步任务
					SnapshotManager.singleton().removeSnapshotTask(target, channel.getSessionId());
                    operLogService.addLog("快照管理", "提交恢复快照失败", "1", "2", request);
					return new MethodResult(MethodResult.FAIL,"提交恢复快照失败!");		
			    }
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			operLogService.addLog("快照管理", "提交恢复快照失败", "1", "2", request);
			return new MethodResult(MethodResult.FAIL,"提交恢复快照失败!");
		} catch (IOException e) {
			e.printStackTrace();
			operLogService.addLog("快照管理", "提交恢复快照失败", "1", "2", request);
			return new MethodResult(MethodResult.FAIL,"提交恢复快照失败!");
		}
		return new MethodResult(MethodResult.SUCCESS,"提交恢复快照成功！");
	}	
	
	
	/**
	 * 创建快照
	 * @param target 主机ID
	 * @param curId  快照ID
	 * @param mode   模式0全局 1 单个磁盘
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/create",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult createSnapshot(@RequestParam("target") String target,
								       @RequestParam("index") String index,
								       @RequestParam("mode") String mode,
								       HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.server_snapshot_resume)){
			return new MethodResult(MethodResult.FAIL,"对不起，您无恢复快照的权限!");
		}		
		try {
			HttpGatewayAsyncChannel channel = HttpGatewayManager.getAsyncChannel(1);	
			if(channel!=null){
				long stamp = System.currentTimeMillis();
				//添加异步任务
				SnapshotManager.singleton().newSnapshotTask(channel.getSessionId(), stamp, target, "create");
				
				//
				JSONObject object = channel.snapshotCreate(0, target, Integer.valueOf(mode), index);
				if("success".equals(object.getString("status"))){
                    operLogService.addLog("快照管理", "提交创建快照成功", "1", "1", request);
				} else{
					//移除异步任务
					SnapshotManager.singleton().removeSnapshotTask(target, channel.getSessionId());
                    operLogService.addLog("快照管理", "提交恢创建照失败", "1", "2", request);
					return new MethodResult(MethodResult.FAIL,"提交创建快照失败!");		
			    }
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			operLogService.addLog("快照管理", "提交创建快照失败", "1", "2", request);
			return new MethodResult(MethodResult.FAIL,"提交创建快照失败!");
		} catch (IOException e) {
			e.printStackTrace();
			operLogService.addLog("快照管理", "提交创建快照失败", "1", "2", request);
			return new MethodResult(MethodResult.FAIL,"提交创建快照失败!");
		}
		return new MethodResult(MethodResult.SUCCESS,"提交创建快照成功！");
	}
	
	
	/**
	 * 判断当前主机是否有快照任务
	 * @param target 主机ID
	 * @return
	 */
	@RequestMapping(value="/runing",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult checkRuningTask(@RequestParam("target") String target,
								       HttpServletRequest request){       
	   if (null != SnapshotManager.singleton().checkTasking(target)){
    	   return new MethodResult(MethodResult.SUCCESS,"目前有快照任务进行中！");
       } else{
    	   return new MethodResult(MethodResult.FAIL,""); 
       }
	}	
}


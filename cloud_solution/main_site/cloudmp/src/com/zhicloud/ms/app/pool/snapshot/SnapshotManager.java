package com.zhicloud.ms.app.pool.snapshot;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.zhicloud.ms.vo.Snapshot;

public class SnapshotManager {

	private static SnapshotManager instance = null;
	private Map<String,SnapshotRequest> queryMap= new HashMap<String,SnapshotRequest>();     //查询
	private Map<String,SnapshotRequest> operateMap= new HashMap<String,SnapshotRequest>();   //操作进度
	private Map<String,String> hostMap= new HashMap<String,String>();		               //会话管理

	/**
	 * 单例模式的snapshotManager
	 * @return
	 */
	public static SnapshotManager singleton() {
		if (SnapshotManager.instance == null) {
			SnapshotManager.instance = new SnapshotManager();
		}
		return SnapshotManager.instance;
	}
	
	/**
	 * 新增一个查询 
	 * @param sessionId
	 */
	public void newQeruyList(String sessionId,long stamp){
		SnapshotRequest snapshotReq = new SnapshotRequest();
		snapshotReq.setStamp(stamp);
		snapshotReq.setCommand("query");
		snapshotReq.setHasData(false);
		queryMap.put(sessionId, snapshotReq);
	}
	
	
	/**
	 * 快照查询返回结果。
	 * @param result
	 */
	public void putSnapShot(String sessionId,JSONObject result) {
		SnapshotRequest snapshotReq = queryMap.get(sessionId);
		if (snapshotReq ==null) return;
		
        if("success".equals(result.get("status"))){	
        	snapshotReq.setStamp(System.currentTimeMillis());	
        	JSONArray obj= result.getJSONArray("index");
		    if (obj==null){
		    	return;
		    }
		    snapshotReq.setIdAarry(result.getJSONArray("index"));
		    snapshotReq.setTimestampAarry(result.getJSONArray("timestamp"));
		    snapshotReq.setHasData(true);
        }
    }
	
	
	/**
	 * 异步变同步 读取快照结果。
	 * @param reqTime
	 * @param diskType
	 */
	public List<Snapshot> getSnapShotVoList(String sessionId,long reqTime){
		List<Snapshot> list = new LinkedList<Snapshot>();
		while (true){
			long l= (System.currentTimeMillis()-reqTime);
			//System.err.println(l);
			if (l>5*1000){//超过5秒
				//超时退出
				return list;
			}
			SnapshotRequest snapshotReq = queryMap.get(sessionId);
			if (snapshotReq ==null) return list;	
			
			if (snapshotReq.getStamp()==reqTime){
				try {
					Thread.sleep(100);//等待0.1
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				if (snapshotReq.isHasData()){//如果有数据则读
					for (int i=0;i<snapshotReq.getIdAarry().size();i++){
						Snapshot vo= new Snapshot();
            			vo.setId(snapshotReq.getIdAarry().getString(i));
            			vo.setTimestamp(snapshotReq.getTimestampAarry().getString(i));   						
						list.add(vo);
					}
				}
			    break;
			}
		}	
		return list;
	}
		
	/**
	 * 更新创建或恢复进度。
	 * @param sessionId
	 * @param messageData
	 * @param command
	 */
	public void updatePressLevelSnapShot(String sessionId,JSONObject messageData){
		if(!"success".equals(messageData.getString("status"))){	
			return;
		}
		//解析messageData		
		SnapshotRequest snapshotReq = this.operateMap.get(sessionId);
		if (snapshotReq==null){
			return;
		}
		snapshotReq.setLevel(messageData.getInt("level"));
	}

	
	/**
	 * 注册新任务。
	 * @param sessionId
	 * @param hostId
	 * @param command
	 */
	public void newSnapshotTask(String sessionId, long stamp, String hostid, String command){
		this.hostMap.put(hostid, sessionId);
		
		SnapshotRequest ssr = new SnapshotRequest();
		ssr.setCommand(command);
		ssr.setLevel(0);
		ssr.setStamp(stamp);
		this.operateMap.put(sessionId, ssr);
	}
	
	
	/**
	 * 移除任务
	 * @param sessionId
	 * @param hostId
	 * @param command
	 */
	public void removeSnapshotTask( String hostid,String sessionId){
		this.hostMap.remove(hostid);
		this.operateMap.remove(sessionId);
	}	
	/**
	 * 完成任务通知。
	 * @param sessionId
	 * @param messageData
	 */
	public void finishCommandSnapShot(String sessionId,JSONObject messageData){
		SnapshotRequest ssr = this.operateMap.get(sessionId);
		if("success".equals(messageData.getString("status"))){	//成功
			ssr.setStamp(System.currentTimeMillis());
			ssr.setIsComplete(1);
		}else{
			ssr.setStamp(System.currentTimeMillis());           //失败
			ssr.setIsComplete(2);
			ssr.setMsg(messageData.getString("message"));          
		}

	}
	
	
	/**
	 * 异步变同步 删除快照结果。
	 * @param reqTime
	 * @param diskType
	 */
	public String getDelResltSnapShot(String sessionId,long reqTime){
		SnapshotRequest snapshotReq = queryMap.get(sessionId);
		
		while (true){
			long l= (System.currentTimeMillis()-snapshotReq.getStamp());
			//System.err.println(l);
			if (l>5*1000){//超过5秒
				//超时退出
				return "等待删除快照操作结果，超时...";
			}
			if (snapshotReq.getStamp()<=reqTime){
				try {
					Thread.sleep(100);//等待0.1
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				if (snapshotReq.isComplete>0){
					return snapshotReq.getMsg();
				}
			}
		}	
	}
	
	
	/**
	 * 判断任务是否在进行中...
	 * @param sessionId
	 * @param hostId
	 * @param command
	 */
	public SnapshotRequest checkTasking(String hostid){	
		String sessionId = this.hostMap.get(hostid);
		SnapshotRequest ssr = this.operateMap.get(sessionId);
		if (ssr!=null){
			if (ssr.getIsComplete()==0){
				long l= (System.currentTimeMillis()-ssr.stamp);
				//System.err.println(l);
				if (l>2*60*60*1000){//超过2小时
					this.operateMap.remove(sessionId);
					this.hostMap.remove(hostid);
					return null;
				}
				return ssr;
			}
		}
		return null;
	}
}

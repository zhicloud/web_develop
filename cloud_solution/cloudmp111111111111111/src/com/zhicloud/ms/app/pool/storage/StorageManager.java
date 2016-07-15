package com.zhicloud.ms.app.pool.storage;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.zhicloud.ms.util.StringUtil;
import com.zhicloud.ms.vo.MountDiskVo;

public class StorageManager {

	private static StorageManager instance = null;
	long stamp;
	boolean hasData = false;
	private JSONArray indexAarry ;
	private JSONArray targetAarry; 
	private JSONArray statusAarry; 
	private JSONArray availableAarry;           
	private JSONArray volumeAarry;     
	private JSONArray usedAarry; 	
	
	private StorageResult addRet;
	private StorageResult removeRet;	
	private StorageResult enableRet;	
	private StorageResult disableRet;
	
	/**
	 * 单例模式的StorageManager
	 * @return
	 */
	public static StorageManager singleton() {
		if (StorageManager.instance == null) {
			StorageManager.instance = new StorageManager();
		}
		return StorageManager.instance;
	}
	
	/**
	 * 启用硬盘的异步回调 写入结果
	 * @param result
	 */
	public void setEnableResult(JSONObject result) {
		if (this.enableRet== null){
			this.enableRet = new StorageResult();
		}	
        if("success".equals(result.getString("status"))){	
        	this.enableRet.setFlag(true);
        	this.enableRet.setStamp(System.currentTimeMillis());
        } else{
        	this.enableRet.setFlag(false);
        	this.enableRet.setStamp(System.currentTimeMillis());   
        	this.enableRet.setMsg("启用硬盘失败, 后台返回："+result.getString("message"));
        }
	}	
	
	/**
	 * 禁用硬盘的异步回调 写入结果
	 * @param result
	 */
	public void setDisableResult(JSONObject result) {
		if (this.disableRet== null){
			this.disableRet = new StorageResult();
		}	
        if("success".equals(result.getString("status"))){	
        	this.disableRet.setFlag(true);
        	this.disableRet.setStamp(System.currentTimeMillis());
        } else{
        	this.disableRet.setFlag(false);
        	this.disableRet.setStamp(System.currentTimeMillis());   
        	this.disableRet.setMsg("禁用硬盘失败，后台返回："+result.getString("message"));
        }
	}	
	
	/**
	 * 取消挂载硬盘的异步回调 写入结果
	 * @param result
	 */
	public void setRemoveResult(JSONObject result) {
		if (this.removeRet== null){
			this.removeRet = new StorageResult();
		}	
        if("success".equals(result.getString("status"))){	
        	this.removeRet.setFlag(true);
        	this.removeRet.setStamp(System.currentTimeMillis());
        } else{
        	this.removeRet.setFlag(false);
        	this.removeRet.setStamp(System.currentTimeMillis());   
        	this.removeRet.setMsg("取消挂载硬盘失败，后台返回："+result.getString("message"));
        }
	}
	
	/**
	 * 挂载硬盘的异步回调 写入结果
	 * @param result
	 */
	public void setAddResult(JSONObject result) {
		if (this.addRet== null){
			this.addRet = new StorageResult();
		}	
        if("success".equals(result.getString("status"))){	
        	this.addRet.setFlag(true);
        	this.addRet.setStamp(System.currentTimeMillis());
        } else{
        	this.addRet.setFlag(false);
        	this.addRet.setStamp(System.currentTimeMillis());   
        	this.addRet.setMsg("挂载硬盘失败，后台返回："+result.getString("message"));
        }
	}
	
	/**
	 * 异步变同步 启用硬盘异步结果读取。
	 * @param reqTime
	 */
	public StorageResult getEnableStorageReslt(long reqTime){
		if (this.enableRet== null){
			this.enableRet = new StorageResult();
		}
		while (true){
			long l= (System.currentTimeMillis()-reqTime);
			//System.err.println(l);
			if (l>5*1000){//超过5秒
				//超时退出
	        	this.enableRet.setFlag(false);
	        	this.enableRet.setMsg("获取返回结果超时！");				
				return this.enableRet;
			}
			if (this.enableRet.getStamp()<reqTime){
				try {
					Thread.sleep(100);//等待0.1
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				break;
			}
		}	
		return this.enableRet;
	}	
	
	/**
	 * 异步变同步 禁用硬盘异步结果读取。
	 * @param reqTime
	 */
	public StorageResult getDisableStorageReslt(long reqTime){
		if (this.disableRet== null){
			this.disableRet = new StorageResult();
		}
		while (true){
			long l= (System.currentTimeMillis()-reqTime);
			//System.err.println(l);
			if (l>5*1000){//超过5秒
				//超时退出
	        	this.disableRet.setFlag(false);
	        	this.disableRet.setMsg("获取返回结果超时！");				
				return this.disableRet;
			}
			if (this.disableRet.getStamp()<reqTime){
				try {
					Thread.sleep(100);//等待0.1
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				break;
			}
		}	
		return this.disableRet;
	}	
	
	/**
	 * 异步变同步 移除挂载硬盘异步结果读取。
	 * @param reqTime
	 */
	public StorageResult getRemoveStorageReslt(long reqTime){
		if (this.removeRet== null){
			this.removeRet = new StorageResult();
		}
		while (true){
			long l= (System.currentTimeMillis()-reqTime);
			//System.err.println(l);
			if (l>5*1000){//超过5秒
				//超时退出
	        	this.removeRet.setFlag(false);
	        	this.removeRet.setMsg("获取返回结果超时！");				
				return this.removeRet;
			}
			if (this.removeRet.getStamp()<reqTime){
				try {
					Thread.sleep(100);//等待0.1
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				break;
			}
		}	
		return this.removeRet;
	}
	
	/**
	 * 异步变同步 挂载硬盘异步结果读取。
	 * @param reqTime
	 */
	public StorageResult getAddStorageReslt(long reqTime){
		if (this.addRet== null){
			this.addRet = new StorageResult();
		}
		while (true){
			long l= (System.currentTimeMillis()-reqTime);
			//System.err.println(l);
			if (l>5*1000){//超过5秒
				//超时退出
	        	this.addRet.setFlag(false);
	        	this.addRet.setMsg("获取返回结果超时！");				
				return this.addRet;
			}
			if (this.addRet.getStamp()<reqTime){
				try {
					Thread.sleep(100);//等待0.1
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				break;
			}
		}	
		return this.addRet;
	}
	
	/**
	 * 查询NC节点挂载硬盘异步结果写入。
	 * @param result
	 */
	public void put(JSONObject result) {
        if("success".equals(result.get("status"))){		
        	JSONArray obj= result.getJSONArray("index");
		    if (obj==null){
		    	hasData = false;
		    	stamp = System.currentTimeMillis();			    	
		    	return;
		    }
			this.indexAarry = result.getJSONArray("index");
			this.targetAarry  = result.getJSONArray("target");
	        this.statusAarry = result.getJSONArray("device_status"); 
	        this.availableAarry = result.getJSONArray("available");             
	        this.volumeAarry = result.getJSONArray("disk_volume");      
	        this.usedAarry = result.getJSONArray("disk_used");
	    	stamp = System.currentTimeMillis();		        
	    	hasData = true;
        } else{
	    	hasData = false;
	    	stamp = System.currentTimeMillis();	        	
        }
	}
	
	/**
	 * 异步变同步 查询NC节点挂载硬盘挂载。
	 * @param reqTime
	 * @param diskType
	 */
	public List<MountDiskVo> getDiskVoList(long reqTime,int diskType){
		List<MountDiskVo> list = new LinkedList<MountDiskVo>();
		while (true){
			long l= (System.currentTimeMillis()-reqTime); 
			if (l>5*1000){//超过5秒
				//超时退出
				return list;
			}
			if (this.stamp<reqTime){
				try {
					Thread.sleep(100);//等待0.1
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				if (this.hasData){//如果有数据则读
					for (int i=0;i<this.indexAarry.size();i++){
						MountDiskVo vo= new MountDiskVo();
            			vo.setId(StringUtil.generateUUID());
            			vo.setDisk_type(diskType);
            			vo.setIndex(indexAarry.getInt(i));
            			vo.setAvailable(availableAarry.getInt(i));
            			vo.setStatus(statusAarry.getInt(i));
            			vo.setTarget(targetAarry.getString(i));
            			vo.setDisk_used(BigInteger.valueOf(usedAarry.getLong(i)));	 //单位GB	
            			vo.setDisk_volume(BigInteger.valueOf(volumeAarry.getLong(i)));//单位GB	    						
						list.add(vo);
					}
					return list;
				}else{
				    return list;
				} 
			}
		}	
		
	 
	}
}

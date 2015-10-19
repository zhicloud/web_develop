package com.zhicloud.ms.app.pool.storage;

/**
 * @author Administrator
 * 挂载硬盘的异步操作结果。
 */
public class StorageResult {
	long stamp;
	boolean flag;
	String msg;
	
	public long getStamp() {
		return stamp;
	}
	public void setStamp(long stamp) {
		this.stamp = stamp;
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

}

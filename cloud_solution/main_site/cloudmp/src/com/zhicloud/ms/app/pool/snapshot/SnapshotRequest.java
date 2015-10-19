package com.zhicloud.ms.app.pool.snapshot;

import net.sf.json.JSONArray;

/**
 * @author Administrator
 * 快照的异步操作结果。
 */
public class SnapshotRequest {
	long stamp;        //时间戳
	String snapshotId; //快照Id
    String command;    //操作add,delete,resume
    int level;         //快照创建进度
    int isComplete;    //是否完成0 未，1 成功，  2失败
	boolean hasData = false;
	JSONArray idAarry ;       //快照ID
	JSONArray timestampAarry; //查询快照时间
	String msg;               //原因
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public long getStamp() {
		return stamp;
	}
	public void setStamp(long stamp) {
		this.stamp = stamp;
	}
	public String getSnapshotId() {
		return snapshotId;
	}
	public void setSnapshotId(String snapshotId) {
		this.snapshotId = snapshotId;
	}
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getIsComplete() {
		return isComplete;
	}
	public void setIsComplete(int isComplete) {
		this.isComplete = isComplete;
	}
    public boolean isHasData() {
		return hasData;
	}
	public void setHasData(boolean hasData) {
		this.hasData = hasData;
	}
	public JSONArray getIdAarry() {
		return idAarry;
	}
	public void setIdAarry(JSONArray idAarry) {
		this.idAarry = idAarry;
	}
	public JSONArray getTimestampAarry() {
		return timestampAarry;
	}
	public void setTimestampAarry(JSONArray timestampAarry) {
		this.timestampAarry = timestampAarry;
	}
}

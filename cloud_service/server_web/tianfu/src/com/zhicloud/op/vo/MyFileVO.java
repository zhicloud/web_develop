package com.zhicloud.op.vo;

import java.math.BigInteger;

import com.zhicloud.op.common.util.json.JSONBean;

public class MyFileVO implements JSONBean{

	private String id;
	private String userId;
	private String fileName;
	private String filePath;
	private BigInteger size  = BigInteger.ZERO;
	private String uploadTime;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public BigInteger getSize() {
		return size;
	}
	public void setSize(BigInteger size) {
		this.size = size;
	}
	public String getUploadTime() {
		return uploadTime;
	}
	public void setUploadTime(String uploadTime) {
		this.uploadTime = uploadTime;
	}
	
	
}

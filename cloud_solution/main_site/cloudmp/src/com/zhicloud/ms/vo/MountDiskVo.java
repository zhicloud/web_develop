package com.zhicloud.ms.vo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

public class MountDiskVo {
	public static final Integer LEVEL= 0;
	public static final Integer DISK_TYPE_LOCALSTORAGE= 0;
	public static final Integer DISK_TYPE_CLOUDSTORAGE= 1;	
	public static final Integer DISK_TYPE_NASDISK= 2;	
	public static final Integer DISK_TYPE_IPSAN= 3;	
	private String id;
	private int disk_type;           //挂载类型：0=本地磁盘，1=云存储，2=nas磁盘，3=ip san
	private String target;           //本地磁盘设备名或者NAS url
	private int status;              //0=未挂载，1=已挂载
	private int available;           //0不可用、1 可用
	private BigInteger disk_used;    //已用空间，单位字节	 
	private BigInteger disk_volume;  //总空间，单位字节	
	private int index;               //本地存储专用，磁盘索引
	private String name;             //共享存储专用，路径标识名
	private String path;             //共享存储专用，需要连接的共享存储路径
	private String crypt;            //共享存储专用，共享存储连接信息	
	
	
	public int getAvailable() {
		return available;
	}
	public void setAvailable(int available) {
		this.available = available;
	}
	public BigInteger getDisk_used() {
		return disk_used;
	}
	public void setDisk_used(BigInteger disk_used) {
		this.disk_used = disk_used;
	}
	
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getCrypt() {
		return crypt;
	}
	public void setCrypt(String crypt) {
		this.crypt = crypt;
	}

	public BigDecimal getDiskUsageFormat(){
		MathContext mc = new MathContext(4, RoundingMode.HALF_DOWN);
		try{
			return (new BigDecimal(this.disk_used)).divide(new BigDecimal(this.disk_volume),mc).multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP);
		}catch (Throwable e){
			return new BigDecimal(0);
		}
 	}	
	public String getDiskTypeText(){	
		switch(this.disk_type){
			case 1: return "云存储";
			case 2: return "nas磁盘";		
			case 3: return "ip san";
			default: return "本地磁盘";
		}
	}		
	public String getStatusText(){
		if(this.status == 0){
			return "未挂载";
		}else{
			return "已挂载";
		}
	}	
	
	public String getAvailableText(){
		if(this.available == 0){
			return "不可用";
		}else{
			return "可用";
		}
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getDisk_type() {
		return disk_type;
	}
	public void setDisk_type(int disk_type) {
		this.disk_type = disk_type;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public BigInteger getDisk_volume() {
		return disk_volume;
	}
	public void setDisk_volume(BigInteger disk_volume) {
		this.disk_volume = disk_volume;
	}
	
	
}

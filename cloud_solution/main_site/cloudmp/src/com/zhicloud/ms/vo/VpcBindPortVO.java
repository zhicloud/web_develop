package com.zhicloud.ms.vo; 

import com.zhicloud.ms.util.json.JSONBean;

/**
 * @author ZYFTMX
 *
 */
public class VpcBindPortVO implements JSONBean{
	
	private String id;
	private String VpcId;
	private String hostId;
	private String outerIp;
	private Integer outerPort;
	private Integer protocol;
	private Integer hostPort;
	private String createTime;
	private String displayName;
	private Integer flag;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getVpcId() {
		return VpcId;
	}
	public void setVpcId(String vpcId) {
		VpcId = vpcId;
	}
	public String getHostId() {
		return hostId;
	}
	public void setHostId(String hostId) {
		this.hostId = hostId;
	}
	public String getOuterIp() {
		return outerIp;
	}
	public void setOuterIp(String outerIp) {
		this.outerIp = outerIp;
	}
	public Integer getOuterPort() {
		return outerPort;
	}
	public void setOuterPort(Integer outerPort) {
		this.outerPort = outerPort;
	}
	public Integer getProtocol() {
		return protocol;
	}
	public void setProtocol(Integer protocol) {
		this.protocol = protocol;
	}
	public Integer getHostPort() {
		return hostPort;
	}
	public void setHostPort(Integer hostPort) {
		this.hostPort = hostPort;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
    public Integer getFlag() {
        return flag;
    }
    public void setFlag(Integer flag) {
        this.flag = flag;
    }
	
    public String getProtocolText(){
    	if(this.protocol==0){
    		return "全部";
    	}else if(this.protocol==1){
    		return "TCP";
    	}else if(this.protocol==2){
    		return "UDP";
    	}else{
    		return "";
    	}
    }
	

}


package com.zhicloud.op.vo;

import java.util.Date;

public class HttpStatusNoticeVO {
	//首次发现时间
	private Date fristHappen;
	
	//上次发送告警时间
	
	private Date lastNotice;
	
	//发生次数，大于三次就应该发送告警
	private Integer times;

	public Date getFristHappen() {
		return fristHappen;
	}

	public void setFristHappen(Date fristHappen) {
		this.fristHappen = fristHappen;
	}

	public Date getLastNotice() {
		return lastNotice;
	}

	public void setLastNotice(Date lastNotice) {
		this.lastNotice = lastNotice;
	}

	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	} 
	
	
	
	

}

package com.zhicloud.op.vo;

public class JsBankFormVO {
	StringBuffer propertystr = new StringBuffer();
	String MERCHANTID ;//商户代号
	String POSID ;//柜台号
	String BRANCHID ;//建行分行代号
	String ORDERID ;//订单号
	String PAYMENT ;//支付金额
	String CURCODE ;//支付币种类型 ，01代表人民币
	String TXCODE ;//交易码，由建行统一分配，个人客户520100，企业客户为690401
	String REMARK1 ;//备注一
	String REMARK2 ;//备注二，备注网银不处理，直接传送城综网
	String MAC ;//经过MD5加密过的32码 
	String TYPE ;//接口类型 0- 非钓鱼接口 1- 防钓鱼接口
	String PUB ;//公钥后30位
	String GATEWAY ;//网关类型
	String CLIENTIP ;//客户端IP
	String REGINFO ;//客户注册信息
	String PROINFO ;//商品信息
	String REFERER ;//商品信息
	public StringBuffer getPropertystr() {
		return propertystr;
	}
	public void setPropertystr(StringBuffer propertystr) {
		this.propertystr = propertystr;
	}
	public String getMERCHANTID() {
		return MERCHANTID;
	}
	public void setMERCHANTID(String mERCHANTID) {
		MERCHANTID = mERCHANTID;
	}
	public String getPOSID() {
		return POSID;
	}
	public void setPOSID(String pOSID) {
		POSID = pOSID;
	}
	public String getBRANCHID() {
		return BRANCHID;
	}
	public void setBRANCHID(String bRANCHID) {
		BRANCHID = bRANCHID;
	}
	public String getORDERID() {
		return ORDERID;
	}
	public void setORDERID(String oRDERID) {
		ORDERID = oRDERID;
	}
	public String getPAYMENT() {
		return PAYMENT;
	}
	public void setPAYMENT(String pAYMENT) {
		PAYMENT = pAYMENT;
	}
	public String getCURCODE() {
		return CURCODE;
	}
	public void setCURCODE(String cURCODE) {
		CURCODE = cURCODE;
	}
	public String getTXCODE() {
		return TXCODE;
	}
	public void setTXCODE(String tXCODE) {
		TXCODE = tXCODE;
	}
	public String getREMARK1() {
		return REMARK1;
	}
	public void setREMARK1(String rEMARK1) {
		REMARK1 = rEMARK1;
	}
	public String getREMARK2() {
		return REMARK2;
	}
	public void setREMARK2(String rEMARK2) {
		REMARK2 = rEMARK2;
	}
	public String getMAC() {
		return MAC;
	}
	public void setMAC(String mAC) {
		MAC = mAC;
	}
	public String getTYPE() {
		return TYPE;
	}
	public void setTYPE(String tYPE) {
		TYPE = tYPE;
	}
	public String getPUB() {
		return PUB;
	}
	public void setPUB(String pUB) {
		PUB = pUB;
	}
	public String getGATEWAY() {
		return GATEWAY;
	}
	public void setGATEWAY(String gATEWAY) {
		GATEWAY = gATEWAY;
	}
	public String getCLIENTIP() {
		return CLIENTIP;
	}
	public void setCLIENTIP(String cLIENTIP) {
		CLIENTIP = cLIENTIP;
	}
	public String getREGINFO() {
		return REGINFO;
	}
	public void setREGINFO(String rEGINFO) {
		REGINFO = rEGINFO;
	}
	public String getPROINFO() {
		return PROINFO;
	}
	public void setPROINFO(String pROINFO) {
		PROINFO = pROINFO;
	}
	public String getREFERER() {
		return REFERER;
	}
	public void setREFERER(String rEFERER) {
		REFERER = rEFERER;
	}


}

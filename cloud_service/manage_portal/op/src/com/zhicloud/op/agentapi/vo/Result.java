package com.zhicloud.op.agentapi.vo;

 
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.zhicloud.op.vo.AccountBalanceDetailVO;
import com.zhicloud.op.vo.CloudHostVO;
import com.zhicloud.op.vo.SysDiskImageVO;

@XmlRootElement(name="result") 
public class Result {
	protected  String status;
	protected  String errorCode;
	protected  String sessionId;
	protected  String message;
	protected  String name;
	protected  String phone;
	protected  String userId;
	protected  String agentId;
	protected  String accountBalance;
	protected  String count;
	protected  String totalCost;
	protected List<CloudHostVO> cloudHostList;
	protected List<OperLogForApiVO> operLogList;
	protected List<SysDiskImageVO> sysDiskImageList;
	protected List<PayExpenses> payExpensesList;
	protected List<AgentConsumption> consumptionList;
	
	public String getStatus() {
		return status;
	}
	@XmlElement(name="status")
	public void setStatus(String status) {
		this.status = status;
	}
	public String getErrorCode() {
		return errorCode;
	}
	@XmlElement(name="error_code")
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getSessionId() {
		return sessionId;
	}
	@XmlElement(name="session_id")
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	public String getMessage() {
		return message;
	}
	@XmlElement(name="message")
	public void setMessage(String message) {
		this.message = message;
	}
	public String getName() {
		return name;
	}
	@XmlElement(name="name")
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPhone() {
		return phone;
	}
	@XmlElement(name="phone")
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getUserId() {
		return userId;
	}  
	@XmlElement(name="user_id")
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@XmlElementWrapper(name="host_list")
	@XmlElement(name="cloud_host")
	public List<CloudHostVO> getCloudHostList() {
		return cloudHostList;
	}
	public void setCloudHostList(List<CloudHostVO> cloudHostList) {
		this.cloudHostList = cloudHostList;
	}
	@XmlElementWrapper(name="list")
	@XmlElement(name="oper_log")
	public List<OperLogForApiVO> getOperLogList() {
		return operLogList;
	}
	public void setOperLogList(List<OperLogForApiVO> operLogList) {
		this.operLogList = operLogList;
	}
	
	@XmlElement(name="count")
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}  
	
	@XmlElementWrapper(name="image_list")
	@XmlElement(name="image")
	public List<SysDiskImageVO> getSysDiskImageList() {
		return sysDiskImageList;
	}
	public void setSysDiskImageList(List<SysDiskImageVO> sysDiskImageList) {
		this.sysDiskImageList = sysDiskImageList;
	}
	@XmlElementWrapper(name="consumption_list")
	@XmlElement(name="consumption")
	public List<AgentConsumption> getConsumptionList() {
		return consumptionList;
	}
	public void setConsumptionList(List<AgentConsumption> consumptionList) {
		this.consumptionList = consumptionList;
	}
	@XmlElement(name="total_cost")
	public String getTotalCost() {
		return totalCost;
	}
	public void setTotalCost(String totalCost) {
		this.totalCost = totalCost;
	}
	@XmlElementWrapper(name="pay_expensesList")
	@XmlElement(name="pay_expenses")
	public List<PayExpenses> getPayExpensesList() {
		return payExpensesList;
	}
	public void setPayExpensesList(List<PayExpenses> payExpensesList) {
		this.payExpensesList = payExpensesList;
	}
	@XmlElement(name="agent_id")
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	@XmlElement(name="account_balance")
	public String getAccountBalance() {
		return accountBalance;
	}
	public void setAccountBalance(String accountBalance) {
		this.accountBalance = accountBalance;
	}
	
	
	
	
	
	
	
	

}

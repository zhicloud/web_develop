package com.zhicloud.op.vo;

import java.math.BigDecimal;

import com.zhicloud.op.common.util.json.JSONBean;

public class AgentVO implements JSONBean
{

	private String id;
	private String account;
	private String groupId;
	private String name;
	private String password;
	private String email;
	private String phone;
	private String status;
	private String create_time;
	private BigDecimal accountBalance; 
	private String balanceUpdateTime; 
	private BigDecimal percentOff; 
	private String markName;
	private BigDecimal recharge;
	private BigDecimal consum;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	// ----

	public String getAccount()
	{
		return account;
	}

	public void setAccount(String account)
	{
		this.account = account;
	}

	public String getGroupId()
	{
		return groupId;
	}

	public void setGroupId(String groupId)
	{
		this.groupId = groupId;
	}

	// ---
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	// ----

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	// ---

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	// ---

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	// ---

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	// ---

	public String getCreate_time()
	{
		return create_time;
	}

	public void setCreate_time(String create_time)
	{
		this.create_time = create_time;
	}

	public BigDecimal getAccountBalance() {
		if(accountBalance==null){
			return BigDecimal.ZERO;
		}
		return accountBalance;
	}

	public void setAccountBalance(BigDecimal accountBalance) {
		this.accountBalance = accountBalance;
	}

	public String getBalanceUpdateTime() {
		return balanceUpdateTime;
	}

	public void setBalanceUpdateTime(String balanceUpdateTime) {
		this.balanceUpdateTime = balanceUpdateTime;
	}

	public BigDecimal getPercentOff() {
		return percentOff;
	}

	public void setPercentOff(BigDecimal percentOff) {
		this.percentOff = percentOff;
	}

	public String getMarkName() {
		return markName;
	}

	public void setMarkName(String markName) {
		this.markName = markName;
	}

    public BigDecimal getRecharge() {
        return recharge;
    }

    public void setRecharge(BigDecimal recharge) {
        this.recharge = recharge;
    }

    public BigDecimal getConsum() {
        return consum;
    }

    public void setConsum(BigDecimal consum) {
        this.consum = consum;
    }
	
	

}

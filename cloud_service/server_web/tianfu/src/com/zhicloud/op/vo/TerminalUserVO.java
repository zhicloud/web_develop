package com.zhicloud.op.vo;

import java.math.BigDecimal;

import com.zhicloud.op.common.util.json.JSONBean;

public class TerminalUserVO implements JSONBean {
	
	private String id;
	private String account;
	private String groupId;
	private String name;
	private String password;
	private String email;
	private String idCard;
	private String belongingAccount;
	/**
	 * 业务额
	 */
	private BigDecimal turnover; 
	
	/**
	 * 邮箱是否验证
	 * 1：否
	 * 2：是
	 */
	private int emailVerified;
	
	private String phone;
	
	/**
	 * 手机是否验证
	 * 1：否
	 * 2：是
	 */
	private int phoneVerified;
	
	/**
	 * 用户状态
	 * 1：未验证
	 * 2：正常
	 * 3：禁用
	 * 4：欠费
	 * 5：结束
	 */
	private int status;
	
	/**
	 * 帐号归属
	 * 1：运营商
	 * 2：代理商
	 */
	private int belongingType;
	
	/**
	 * 填运营商或代理商ID，用户自己注册的填null
	 */
	private String belongingId;
	private BigDecimal accountBalance;
	private String createTime;
	private String balanceUpdateTime; 
	private BigDecimal percentOff; 
	
	
	public BigDecimal getPercentOff() 
	{
		return percentOff;
	}

	public void setPercentOff(BigDecimal percentOff) 
	{
		this.percentOff = percentOff;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

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

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getIdCard()
	{
		return idCard;
	}

	public void setIdCard(String idCard)
	{
		this.idCard = idCard;
	}

	public int getEmailVerified()
	{
		return emailVerified;
	}

	public void setEmailVerified(int emailVerified)
	{
		this.emailVerified = emailVerified;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public int getPhoneVerified()
	{
		return phoneVerified;
	}

	public void setPhoneVerified(int phoneVerified)
	{
		this.phoneVerified = phoneVerified;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public int getBelongingType()
	{
		return belongingType;
	}

	public void setBelongingType(int belongingType)
	{
		this.belongingType = belongingType;
	}

	public String getBelongingId()
	{
		return belongingId;
	}

	public void setBelongingId(String belongingId)
	{
		this.belongingId = belongingId;
	}

	public BigDecimal getAccountBalance()
	{
		return accountBalance;
	}

	public void setAccountBalance(BigDecimal accountBalance)
	{
		this.accountBalance = accountBalance;
	}

	public String getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(String createTime)
	{
		this.createTime = createTime;
	}

	public String getBelongingAccount()
	{
		return belongingAccount;
	}

	public void setBelongingAccount(String belongingAccount)
	{
		this.belongingAccount = belongingAccount;
	}

	public String getBalanceUpdateTime() {
		return balanceUpdateTime;
	}

	public void setBalanceUpdateTime(String balanceUpdateTime) {
		this.balanceUpdateTime = balanceUpdateTime;
	}

	public BigDecimal getTurnover() {
		return turnover;
	}

	public void setTurnover(BigDecimal turnover) {
		this.turnover = turnover;
	}
	
}

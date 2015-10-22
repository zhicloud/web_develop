package com.zhicloud.op.vo;

import java.math.BigDecimal;
import java.text.ParseException;

import com.zhicloud.op.common.util.DateUtil;
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
	private String recharge;
	private String consum;
	private String account_balance;
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
	private Integer hostAmount; 
	private Integer vpcAmount; 
	private Integer diskAmount; 
	private String markName;
	private String displayStatus;
 	
	
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
	public String getStatusFormat(){
		if(this.status==1){
			return "未验证";
		}else if(this.status==2){
			return "正常";
		}else if(this.status==3){
			return "禁用";
		}else{
			return "欠费";
		}
	}
	public String getCraeteTimeFormat(){
		try {
			return DateUtil.formatDateString(this.createTime, "yyyyMMddHHmmssSSS", "yyyy-MM-dd HH:mm:ss");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
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

	public Integer getHostAmount() {
		return hostAmount;
	}

	public void setHostAmount(Integer hostAmount) {
		this.hostAmount = hostAmount;
	}

	public Integer getVpcAmount() {
		return vpcAmount;
	}

	public void setVpcAmount(Integer vpcAmount) {
		this.vpcAmount = vpcAmount;
	}

	public String getMarkName() {
		return markName;
	}

	public void setMarkName(String markName) {
		this.markName = markName;
	}

	public Integer getDiskAmount() {
		return diskAmount;
	}

	public void setDiskAmount(Integer diskAmount) {
		this.diskAmount = diskAmount;
	}

    public String getRecharge() {
        return recharge;
    }

    public void setRecharge(String recharge) {
        this.recharge = recharge;
    }

    public String getConsum() {
        return consum;
    }

    public void setConsum(String consum) {
        this.consum = consum;
    }

    public String getAccount_balance() {
        return account_balance;
    }

    public void setAccount_balance(String account_balance) {
        this.account_balance = account_balance;
    }

    public String getDisplayStatus() {
        return displayStatus;
    }

    public void setDisplayStatus(String displayStatus) {
        this.displayStatus = displayStatus;
    }
	
	
	
}

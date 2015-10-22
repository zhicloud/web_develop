package com.zhicloud.ms.service;


public interface DomainService {
    
    /**
     * Description:设置组播地址
     * @param ip ip
     * @param module 模块
     */
    public void setBroadcast(String ip, String module);
    
    /**
     * Description:获取组播地址
     * @param module 模块名
     * @return
     */
    public String getBroadcast(String module);
    
    /**
     * Description:设置域名信息
     * @param domain 新域名
     * @param module 模块
     */
    public void setDomain(String domain, String module);
    
    /**
     * Description:获取域名信息
     * @param module 模块名
     * @return String
     */
    public String getDomain(String module);
    
}

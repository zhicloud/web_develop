package com.zhicloud.ms.service.impl;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhicloud.ms.service.DomainService;

/**
 * @ClassName: DomainServiceImpl
 * @Description: 域名业务处理
 * @author 张本缘 于 2015年9月11日 下午2:41:51
 */
@Service("domainService")
@Transactional
public class DomainServiceImpl implements DomainService{
    
    /* 日志 */
    private static final Logger logger = Logger.getLogger(DomainServiceImpl.class);
    
    @Resource
    private SqlSession sqlSession;

    /**
     * Description:设置组播地址
     * @param ip ip
     * @param module 模块
     */
    public void setBroadcast(String ip,String module) {
        logger.debug("DomainServiceImpl.setBroadcast(" + ip + ")");
        try {
            String splitter = "=";
            String fileName = "/var/zhicloud/config/" + module + "/node.conf";
            String broadcast = "group_ip";
            // 更新设定文件
            List<?> lines = FileUtils.readLines(new File(fileName));
            List<String> newLines = new ArrayList<String>();
            boolean findFlag = false;
            boolean updateFlag = false;
            for (Object line : lines) {
                String strLine = (String) line;
                if (StringUtils.isNotEmpty(strLine) && !strLine.startsWith("#")) {
                    int index = strLine.toLowerCase().indexOf(broadcast.toLowerCase());
                    if (index != -1) {
                        String[] array = strLine.split(splitter);
                        findFlag = true;
                        if (array[1].equals(ip)) {
                            // 如果IP相同，则不更新
                            newLines.add(strLine);
                        } else {
                            // 更新成设定好的IP地址
                            StringBuilder sb = new StringBuilder();
                            sb.append(broadcast);
                            sb.append(splitter).append(ip);
                            newLines.add(sb.toString());
                            updateFlag = true;
                        }
                    }
                }
                newLines.add(strLine);
            }
            // 如果没有broadcast名，则追加
            if (!findFlag) {
                newLines.add(new StringBuilder(broadcast).append(splitter).append(ip).toString());
            }

            if (updateFlag || !findFlag) {
                // 写设定文件
                FileUtils.writeLines(new File(fileName), newLines);
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("设置组播地址出错:" + e.getMessage());
        }
    }

    /**
     * Description:获取组播地址
     * @param module 模块名
     * @return String
     */
    public String getBroadcast(String module) {
        String splitter = "=";
        String fileName = "/var/zhicloud/config/" + module + "/node.conf";
        String broadcast = "group_ip";
        String restr = "";
        try {
            // 更新设定文件
            List<?> lines = FileUtils.readLines(new File(fileName));
            for (Object line : lines) {
                String strLine = (String) line;
                if (StringUtils.isNotEmpty(strLine) && !strLine.startsWith("#")) {
                    int index = strLine.toLowerCase().indexOf(broadcast.toLowerCase());
                    if (index != -1) {
                        String[] array = strLine.split(splitter);
                        restr = array[1];
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("获取组播地址出错:" + e.getMessage());
        }
        return restr;
    }
    
    /**
     * Description:获取域名信息
     * @param module 模块名
     * @return String
     */
    public String getDomain(String module) {
        String splitter = "=";
        String fileName = "/var/zhicloud/config/" + module + "/node.conf";
        String broadcast = "domain";
        String restr = "";
        try {
            // 更新设定文件
            List<?> lines = FileUtils.readLines(new File(fileName));
            for (Object line : lines) {
                String strLine = (String) line;
                if (StringUtils.isNotEmpty(strLine) && !strLine.startsWith("#")) {
                    int index = strLine.toLowerCase().indexOf(broadcast.toLowerCase());
                    if (index != -1) {
                        String[] array = strLine.split(splitter);
                        restr = array[1];
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("获取域名信息出错:" + e.getMessage());
        }
        return restr;
    }
    
    /**
     * Description:设置域名信息
     * @param domain 新域名
     * @param module 模块
     */
    public void setDomain(String domain, String module) {
        logger.debug("DomainServiceImpl.setDomain(" + domain + ")");
        try {
            String splitter = "=";
            String fileName = "/var/zhicloud/config/" + module + "/node.conf";
            String broadcast = "domain";
            // 更新设定文件
            List<?> lines = FileUtils.readLines(new File(fileName));
            List<String> newLines = new ArrayList<String>();
            boolean findFlag = false;
            boolean updateFlag = false;
            for (Object line : lines) {
                String strLine = (String) line;
                if (StringUtils.isNotEmpty(strLine) && !strLine.startsWith("#")) {
                    int index = strLine.toLowerCase().indexOf(broadcast.toLowerCase());
                    if (index != -1) {
                        String[] array = strLine.split(splitter);
                        findFlag = true;
                        if (array[1].equals(domain)) {
                            // 如果域名相同，则不更新
                            newLines.add(strLine);
                        } else {
                            // 更新成设定好的域名地址
                            StringBuilder sb = new StringBuilder();
                            sb.append(broadcast);
                            sb.append(splitter).append(domain);
                            newLines.add(sb.toString());
                            updateFlag = true;
                        }
                    }
                }
                newLines.add(strLine);
            }
            // 如果没有broadcast名，则追加
            if (!findFlag) {
                newLines.add(new StringBuilder(broadcast).append(splitter).append(domain).toString());
            }

            if (updateFlag || !findFlag) {
                // 写设定文件
                FileUtils.writeLines(new File(fileName), newLines);
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("设置域名信息出错:" + e.getMessage());
        }
    } 
    
}

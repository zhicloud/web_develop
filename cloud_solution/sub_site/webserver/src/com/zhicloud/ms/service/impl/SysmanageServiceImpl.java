package com.zhicloud.ms.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhicloud.ms.app.propeties.AppProperties;
import com.zhicloud.ms.common.util.FileUtil;
import com.zhicloud.ms.mapper.ServerComponentMapper;
import com.zhicloud.ms.service.SysmanageService;
import com.zhicloud.ms.util.LinuxShellUtil;
import com.zhicloud.ms.vo.ServerComponentVO;

@Service("sysmanageService")
@Transactional
public class SysmanageServiceImpl implements SysmanageService{
    /* 日志 */
    private static final Logger logger = Logger.getLogger(SysmanageServiceImpl.class);
    
    @Resource
    private SqlSession sqlSession;
    
    @Override
    public boolean updateDatetime(String netDatetime) {
        logger.info("SysmanageServiceImpl:updateDatetime()");
        
        String cmdString = AppProperties.getValue("update_server_datetime", "date -s {%%}");
        String[]dtSplit = netDatetime.split(" ");
        boolean bn = false;
        if(dtSplit.length == 2){
            for(String st:dtSplit){
                if(st.length() > 0){
                    bn = LinuxShellUtil.callShell(cmdString.replace("{%%}", st));
                    logger.debug("update Datetime:" + cmdString.replace("{%%}", st));
                }
            }
        }
        return bn;
    }

    @Override
    public boolean updateHostName(String newHostname) {
        logger.info("SysmanageServiceImpl:updateHostName()");

        String hosts = "/etc/hosts";
        String network = "/etc/sysconfig/network";
        String hostname = "";
        
        // 修改文件/etc/sysconfig/network
        File confFile = new File(network);
        String line = "";
        StringBuilder sb = new StringBuilder();
        try {
            FileReader fr = new FileReader(confFile);
            BufferedReader br = new BufferedReader(fr);
            while((line = br.readLine()) != null){
                String[] info = line.split("=");
                if(info[0].equals("HOSTNAME")){
                    hostname = info[1];
                    sb.append("HOSTNAME=" + newHostname + "\n");
                }
                else{
                    sb.append(line + "\n");
                }
            }
            br.close();
            
            FileWriter fw = new FileWriter(confFile);
            fw.write(sb.toString());
            fw.close();
        } catch (FileNotFoundException e) {
            logger.error("SysmanageServiceImpl:updateHostName. update file:network Exception:" + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            logger.error("SysmanageServiceImpl:updateHostName. update file:network Exception:" + e.getMessage());
            e.printStackTrace();
        }        
        
        //logger.debug("Old hostname:" + hostname);
        // 修改文件/etc/hosts
        if(hostname.length() > 0){
            confFile = new File(hosts);
            line = "";
            sb = new StringBuilder();
            try {
                FileReader fr = new FileReader(confFile);
                BufferedReader br = new BufferedReader(fr);
                while((line = br.readLine()) != null){
                    String[] info = line.split(" ");
                    for(int i = 0; i < info.length; i++){
                        if(info[i].equals(hostname)){
                            info[i] = newHostname;
                        }
                    }
                    //logger.debug(StringUtils.join(info, " "));
                    sb.append(StringUtils.join(info, " ") + "\n");
                }
                br.close();
                
                FileWriter fw = new FileWriter(confFile);
                fw.write(sb.toString());
                fw.close();
            } catch (FileNotFoundException e) {
                logger.error("SysmanageServiceImpl:updateHostName. update file:hosts Exception:" + e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                logger.error("SysmanageServiceImpl:updateHostName. update file:hosts Exception:" + e.getMessage());
                e.printStackTrace();
            }  
        }

        String cmdString = AppProperties.getValue("update_server_hostname", "hostname {%%}");
        LinuxShellUtil.callShell(cmdString.replace("{%%}", newHostname));
        return true;
    }

    /**
     * 关闭服务器
     */
    public boolean shutdownServer() {
        logger.info("SysmanageServiceImpl:shutdownServer()");

        String cmdString = AppProperties.getValue("shutdown_server", "shutdown -h now");
        return LinuxShellUtil.callShell(cmdString);        
    }

    /**
     * 重启服务器
     */
    public boolean rebootServer() {
        logger.info("SysmanageServiceImpl:rebootServer()");

        String cmdString = AppProperties.getValue("reboot_server", "reboot");
        return LinuxShellUtil.callShell(cmdString);
    }

    /**
     * 重启Tomcat
     */
    public boolean rebootTomcat() {
        String cmdString = AppProperties.getValue("reboot_tomcat", "/opt/start.sh");
        return LinuxShellUtil.callShell(cmdString);
    }

    /**
     * 重启组件服务
     * @throws InterruptedException 
     */
    public boolean restartComponent(String componentName) {
        List<ServerComponentVO> serverComponentVOList = getComponentList();
        //判断是否存在服务组件.
        ServerComponentVO vo = null;
        for(ServerComponentVO votemp:serverComponentVOList){
            if(votemp.getKeyword().equals(componentName)){
                vo = votemp;
                break;
            }
        }
        if(vo == null){
            return false;
        }        
        // 停止组件服务
        String cmdString = AppProperties.getValue("stop_component_service", "{%%} stop").replace("{%%}", componentName);
        if(cmdString.indexOf("/") > -1){
            cmdString =cmdString.substring(cmdString.indexOf("/") + 1);
        }
        cmdString = vo.getPath() + "/" + cmdString;
        logger.debug("stop_component_service:" + cmdString);
        LinuxShellUtil.callShell(cmdString);
        //等待服务停止
        String process_id = AppProperties.getValue("find_process_id", "ps -ef|grep {%%}|grep -v grep").replace("{%%}", componentName);
        List<String> getidList = LinuxShellUtil.getCallShell(process_id);
        while(getidList.size() > 0 && getidList.get(0).length() > 0){
            // 等待0.5秒时间
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            getidList = LinuxShellUtil.getCallShell(process_id);
        }

        // 备份原文件
        String backPath = AppProperties.getValue("component_back_path", "/opt/temp");
        //若无该文件夹自动创建
        File fp = new File(backPath);            
        if(!fp.exists()){
            fp.mkdirs();
        }        
        LinuxShellUtil.callShell("mv -f " + vo.getPath() + "/" + componentName + "  " + backPath + "/" + componentName);
        //FileUtil.copyFile(vo.getPath() + "/" + componentName, backPath + "/" + componentName);
        logger.debug("copy file:" + vo.getPath() + "/" + componentName + "  To:" + backPath + "/" + componentName);
        // 等待2秒时间
        /*
        try {
            Thread.sleep(2*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 强制杀进程
        String killProcess = AppProperties.getValue("kill_process", "ps -ef|grep {%%} | grep -v grep | awk '{print $2}'| xargs kill -9 ");
        logger.debug("killProcess:" + killProcess.replace("{%%}", componentName));
        LinuxShellUtil.callShell(killProcess.replace("{%%}", componentName));
        */
        
        // 拷贝新文件到运行目录        
        String tempPath = AppProperties.getValue("component_temp_path", "/home/temp");
        LinuxShellUtil.callShell("cp -f " + tempPath + "/" + componentName + "  " + vo.getPath() + "/" + componentName);
        LinuxShellUtil.callShell("chmod +x " + vo.getPath() + "/" + componentName);
        //FileUtil.copyFile(tempPath + "/" + componentName, vo.getPath() + "/" + componentName);
        
        // 重新启动组件
        cmdString = AppProperties.getValue("start_component_service", "{%%} start").replace("{%%}", componentName);
        if(cmdString.indexOf("/") > -1){
            cmdString =cmdString.substring(cmdString.indexOf("/") + 1);
        }
        cmdString = vo.getPath() + "/" + cmdString;
        logger.debug("start_component_service:" + cmdString);        
        LinuxShellUtil.callShell(cmdString);  // 此处不能及时返回。
        return true;
    }

    /**
     * 获取组件列表
     */
    public List<ServerComponentVO> getComponentList() {
        ServerComponentMapper serverComponentMapper = this.sqlSession.getMapper(ServerComponentMapper.class);
        return serverComponentMapper.queryPage();
    }
}

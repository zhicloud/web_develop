package com.zhicloud.ms.controller;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhicloud.ms.common.util.StringUtil;
import com.zhicloud.ms.service.LogService;
import com.zhicloud.ms.service.StateService;
import com.zhicloud.ms.service.ResourceStatisticsService;
import com.zhicloud.ms.vo.BcastVO;
import com.zhicloud.ms.vo.NetVO;
import com.zhicloud.ms.vo.ResUsageVO;
import com.zhicloud.ms.vo.ResourceStatisticsVO;
import com.zhicloud.ms.vo.ServerVO;
import com.zhicloud.ms.vo.ServiceVO;
import com.zhicloud.ms.vo.UserVO;

/**
 * @ClassName: StateController
 * @Description: 状态处理类
 * @author 梁绍辉 于 2015年9月9日 下午1:25:19
 */
@Controller
@RequestMapping("/state")
public class StateController {
    /* 日志 */
    public static final Logger logger = Logger.getLogger(StateController.class);
    

    @Resource(name="stateService")
    private StateService stateService;
    
    @Resource(name = "logService")
    private LogService logService;
    
    @Resource(name="resourceStatisticsService")
    public ResourceStatisticsService resourceStatisticsService;
    
    /**
     * @Description: 运行状态页面管理
     * @param request
     * @return String
     */
    @RequestMapping(value = "/manage", method = RequestMethod.GET)
    public String managePage(HttpServletRequest request) {
        // 获取基本信息
        List<ServiceVO> service = stateService.queryService();
        List<NetVO> net = stateService.queryNet();
        List<BcastVO> bcast = stateService.queryBcast();
        
        ResUsageVO usage = stateService.queryUsage();
        logger.info("1");
        ServerVO server = stateService.queryServer();
        logger.info("2");
        request.setAttribute("serviceList", service);
        request.setAttribute("netList", net);
        request.setAttribute("bcastList", bcast);
        
        request.setAttribute("usage", usage);
        request.setAttribute("server", server); 
        
        //UserVO user = (UserVO) request.getSession().getAttribute("user");
        //logService.addLog("查看服务器状态.", user.getId());
        logger.info("ok");
        
        return "/state/statelist";
    }

    
    /**
     * @Description:取服务器状态
     * @param request
     * @throws
     */
    @RequestMapping(value = "/queryusage")
    @ResponseBody
    public JSONObject queryUsage(HttpServletRequest request, HttpServletResponse response) {
        ResUsageVO usage = stateService.queryUsage();
        JSONObject re = new JSONObject();
        re.put("cpu", usage.getCpuper());
        re.put("mem", usage.getMemper());
        re.put("data", usage.getData());
        re.put("disk", usage.getDiskper());
        UserVO user = (UserVO) request.getSession().getAttribute("user");
        logService.addLog("刷新当前资源使用情况.", user.getId());
        return re;        
    }
    
    /**
     * @Description:取服务器状态
     * @param request
     * @throws
     */
    @RequestMapping(value = "/queryshowdata")
    @ResponseBody
    public JSONObject queryShowData(HttpServletRequest request, HttpServletResponse response) {        
        JSONObject re = new JSONObject();
        String data_date = StringUtil.trim(request.getParameter("c_stime"));
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("data_date", data_date);
        List<ResourceStatisticsVO> stateList = resourceStatisticsService.queryPage(map);
        String cpu1 = "",  cpu2 = "";
        String mem1 = "",  mem2 = "";
        String disk1 = "", disk2 = ""; 
        String data1 = "", data2 = "";

        for(ResourceStatisticsVO rsvo:stateList){
            cpu1 += rsvo.getDataHours() + ";";
            cpu2 += rsvo.getCpuUsage() + ";";
            
            mem1 += rsvo.getDataHours() + ";";
            mem2 += rsvo.getMemoryUsage() + ";";
            
            disk1 += rsvo.getDataHours() + ";";
            disk2 += rsvo.getDiskUsage() + ";";
            
            data1 += rsvo.getDataHours() + ";";
            data2 += rsvo.getThroughput() + ";";
        }
        if(cpu1.length() > 0){
            cpu1 = cpu1.substring(0, cpu1.length() - 1);
        }        
        if(cpu2.length() > 0){
            cpu2 = cpu2.substring(0, cpu2.length() - 1);
        }
        
        if(mem1.length() > 0){
            mem1 = mem1.substring(0, mem1.length() - 1);
        }
        if(mem2.length() > 0){
            mem2 = mem2.substring(0, mem2.length() - 1);
        }
        if(disk1.length() > 0){
            disk1 = disk1.substring(0, disk1.length() - 1);
        }
        if(disk2.length() > 0){
            disk2 = disk2.substring(0, disk2.length() - 1);
        }
        
        if(data1.length() > 0){
            data1 = data1.substring(0, data1.length() - 1);
        }        
        if(data2.length() > 0){
            data2 = data2.substring(0, data2.length() - 1);
        }
        re.put("cpu1", cpu1.split(";"));
        re.put("cpu2", cpu2.split(";"));
        re.put("mem1", mem1.split(";"));
        re.put("mem2", mem2.split(";"));
        re.put("disk1", disk1.split(";"));
        re.put("disk2", disk2.split(";"));
        re.put("data1", data1.split(";"));
        re.put("data2", data2.split(";"));        

        UserVO user = (UserVO) request.getSession().getAttribute("user");
        logService.addLog("查询[" + data_date +  "]的资源使用情况.", user.getId());
        return re;  
    }
}

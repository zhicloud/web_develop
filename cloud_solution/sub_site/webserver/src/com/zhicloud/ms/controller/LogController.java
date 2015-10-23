package com.zhicloud.ms.controller;

import java.util.LinkedHashMap;
import java.util.List;
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
import com.zhicloud.ms.vo.LogVO;

/**
 * @ClassName: LogController
 * @Description: 日志处理类
 * @author 张本缘 于 2015年9月8日 上午10:25:19
 */
@Controller
@RequestMapping("/log")
public class LogController {
    /* 日志 */
    public static final Logger logger = Logger.getLogger(LogController.class);
    
    @Resource(name="logService")
    private LogService logService;
    
    /**
     * @Description: 日志清单跳转
     * @param request
     * @return String
     */
    @RequestMapping(value = "/manage", method = RequestMethod.GET)
    public String managePage(HttpServletRequest request) {
        return "/log/loglist";
    }

    /**
     * @Description:日志清单数据查询
     * @param request
     * @throws
     */
    @RequestMapping(value = "/querydata")
    @ResponseBody
    public JSONObject queryLog(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> condition = new LinkedHashMap<String, Object>();
        // 获取参数
        Integer page = StringUtil.parseInteger(request.getParameter("page"), 1) - 1; // 前台过来的是从1开始，需要的是从0开始
        Integer rows = StringUtil.parseInteger(request.getParameter("rows"), 10);
        String start_time = StringUtil.trim(request.getParameter("start_time"));
        String end_time = StringUtil.trim(request.getParameter("end_time"));
        condition.put("start_row", page * rows);
        condition.put("row_count", rows);
        condition.put("start_time", start_time);
        condition.put("end_time", end_time);
        List<LogVO> logs = logService.queryPage(condition);
        int total = logService.queryPageCount(condition);
        JSONObject re = new JSONObject();
        re.put("total", total);
        re.put("rows", logs);
        return re;
    }
    
}

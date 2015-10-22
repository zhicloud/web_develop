package com.zhicloud.op.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zhicloud.op.common.util.CapacityUtil;
import com.zhicloud.op.common.util.DateUtil;
import com.zhicloud.op.common.util.ExportExcelUtils;
import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.core.CoreSpringContextManager;
import com.zhicloud.op.exception.AppException;
import com.zhicloud.op.service.AccountBalanceService;
import com.zhicloud.op.service.AgentService;
import com.zhicloud.op.service.CashCouponService;
import com.zhicloud.op.service.CloudDiskService;
import com.zhicloud.op.service.CloudHostService;
import com.zhicloud.op.service.MarkService;
import com.zhicloud.op.service.OperLogService;
import com.zhicloud.op.service.TerminalUserService;
import com.zhicloud.op.service.VpcService;
import com.zhicloud.op.vo.AccountBalanceDetailVO;
import com.zhicloud.op.vo.AgentVO;
import com.zhicloud.op.vo.CashCouponVO;
import com.zhicloud.op.vo.CloudDiskVO;
import com.zhicloud.op.vo.CloudHostVO;
import com.zhicloud.op.vo.MarkVO;
import com.zhicloud.op.vo.OperLogVO;
import com.zhicloud.op.vo.TerminalUserVO;
import com.zhicloud.op.vo.VpcBaseInfoVO;
/**
 * @ClassName: ExportController
 * @Description: 导出类
 * @author 张本缘 于 2015年6月18日 下午3:19:29
 */
@Controller
public class ExportController {

    public static final Logger logger = Logger.getLogger(ExportController.class);
    
    /**
     * @Description:代理商管理导出数据
     * @param request
     * @param response
     */
    @RequestMapping(value = "/agentExportData.do")
    public void exportAgentData(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("ExportController.exportAgentData()");
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json; charset=utf-8");
            AgentService agentService = CoreSpringContextManager.getAgentService();
            List<AgentVO> agentList = agentService.getAgentDataByCondition(request, response);
            // 翻译字段信息
            for (AgentVO agent : agentList) {
                // 状态
                if ("1".equals(agent.getStatus())) {
                    agent.setStatus("正常");
                } else if ("2".equals(agent.getStatus())) {
                    agent.setStatus("禁用");
                } else {
                    agent.setStatus("结束");
                }
                // 时间
                if (!"".equals(agent.getCreate_time())) {
                    agent.setCreate_time(DateUtil.dateToString(
                            DateUtil.stringToDate(agent.getCreate_time(), "yyyyMMddHHmmssSSS"), "yyyy-MM-dd HH:mm:ss"));
                }
            }
            String[][] columns = new String[][] { {"昵称", "Name" }, {"邮箱", "Account" }, {"手机", "Phone" },
                    {"标签", "MarkName" }, {"折扣率", "PercentOff" }, {"账户余额", "AccountBalance" }, {"累计充值", "Recharge" },
                    {"累计消费", "Consum" }, {"状态", "Status" }, {"创建时间", "Create_time" } };
            ExportExcelUtils.export(request, response, agentList, "代理商数据", columns, AgentVO.class);
        } catch (Exception e) {
            logger.error("AgentServiceImpl.exportAgentData()", e);
            throw new AppException("导出失败");
        }
    }
    
    /**
     * @Description:导出终端用户数据
     * @param request
     * @param response
     */
    @RequestMapping(value = "/terminaluserExportData.do")
    public void exporTerminalUserData(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("ExportController.exporTerminalUserData()");
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json; charset=utf-8");
            TerminalUserService userService = CoreSpringContextManager.getTerminalUserService();
            List<TerminalUserVO> userList = userService.queryTerminalUserForExport(request, response);
            // 翻译字段信息
            for (TerminalUserVO user : userList) {
                // 状态
                if (user.getStatus() == 1) {
                    user.setDisplayStatus("未验证");
                } else if (user.getStatus() == 2) {
                    user.setDisplayStatus("正常");
                } else if (user.getStatus() == 3) {
                    user.setDisplayStatus("禁用");
                } else if (user.getStatus() == 4) {
                    user.setDisplayStatus("欠费");
                } else {
                    user.setDisplayStatus("结束");
                }
                // 时间
                if (!"".equals(user.getCreateTime())) {
                    user.setCreateTime(DateUtil.dateToString(
                            DateUtil.stringToDate(user.getCreateTime(), "yyyyMMddHHmmssSSS"), "yyyy-MM-dd HH:mm:ss"));
                }
            }
            String[][] columns = new String[][] { {"昵称", "Name" }, {"邮箱", "Account" }, {"手机", "Phone" },
                    {"归属", "BelongingAccount" }, {"标签", "MarkName" }, {"折扣率", "PercentOff" },
                    {"账户余额", "Account_balance" }, {"累计充值", "Recharge" }, {"累计消费", "Consum" }, {"状态", "DisplayStatus" },
                    {"创建时间", "CreateTime" } };
            ExportExcelUtils.export(request, response, userList, "终端用户数据", columns, TerminalUserVO.class);
        } catch (Exception e) {
            logger.error("AgentServiceImpl.exporTerminalUserData()", e);
            throw new AppException("导出失败");
        }
    }
    
    /**
     * @Description:导出用户日志数据
     * @param request
     * @param response
     */
    @RequestMapping(value = "/userLogExportData.do")
    public void exporUserLogData(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("ExportController.exporUserLogData()");
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json; charset=utf-8");
            OperLogService logService = CoreSpringContextManager.getOperLogService();
            List<OperLogVO> logList = logService.queryOperLogForExport(request, response);
            // 翻译字段信息
            for (OperLogVO log : logList) {
                // 状态
                if (log.getStatus() == 1) {
                    log.setDisplayStatus("成功");
                } else {
                    log.setDisplayStatus("失败");
                }
                // 用户类型
                if (log.getUserType() == 1) {
                    log.setTypeName("超级管理员");
                } else if (log.getUserType() == 2) {
                    log.setTypeName("运营商");
                } else if (log.getUserType() == 3) {
                    log.setTypeName("代理商");
                } else if (log.getUserType() == 4) {
                    log.setTypeName("终端用户");
                }
                // 时间
                if (!"".equals(log.getOperTime())) {
                    log.setOperTime(DateUtil.dateToString(
                            DateUtil.stringToDate(log.getOperTime(), "yyyyMMddHHmmssSSS"), "yyyy-MM-dd HH:mm:ss"));
                }
            }
            String[][] columns = new String[][] { {"时间", "OperTime" }, {"昵称", "Name" }, {"用户名", "Account" },
                    {"用户类型", "TypeName" }, {"操作内容", "Content" }, {"状态", "DisplayStatus" } };
            ExportExcelUtils.export(request, response, logList, "用户日志数据", columns, OperLogVO.class);
        } catch (Exception e) {
            logger.error("AgentServiceImpl.exporUserLogData()", e);
            throw new AppException("导出失败");
        }
    }
    
    /**
     * @Description:导出用户云主机数据
     * @param request
     * @param response
     */
    @RequestMapping(value = "/cloudHostExportData.do")
    public void exporCloudHostData(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("ExportController.exporCloudHostData()");
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json; charset=utf-8");
            CloudHostService cloudHostService = CoreSpringContextManager.getCloudHostService();
            List<CloudHostVO> cloudHostList = cloudHostService.queryCloudHostForExport(request, response);
            for (CloudHostVO host : cloudHostList) {
                if (!"".equals(host.getCpuCore())) {
                    host.setCpuname(host.getCpuCore() + "核");
                }
                if (!"".equals(host.getMemory())) {
                    host.setMemoryname(CapacityUtil.toCapacityLabel(host.getMemory(), 0));
                }
                if (!"".equals(host.getSysDisk())) {
                    host.setSysdiskname(CapacityUtil.toCapacityLabel(host.getSysDisk(), 0));
                }
                if (!"".equals(host.getDataDisk())) {
                    host.setDatadiskname(CapacityUtil.toCapacityLabel(host.getDataDisk(), 0));
                }
                if (!"".equals(host.getBandwidth())) {
                    host.setBandwidthname(CapacityUtil.toCapacityLabel(host.getBandwidth(), 0));
                }
                // 地域
                if (host.getRegion() == 1) {
                    host.setRegionname("广州");
                } else if (host.getRegion() == 2) {
                    host.setRegionname("成都");
                } else if (host.getRegion() == 3) {
                    host.setRegionname("北京");
                } else {
                    host.setRegionname("香港");
                }
                // 时间
                if (!"".equals(host.getCreateTime())) {
                    host.setCreateTime(DateUtil.dateToString(
                            DateUtil.stringToDate(host.getCreateTime(), "yyyyMMddHHmmssSSS"), "yyyy-MM-dd HH:mm:ss"));
                }
            }
            String[][] columns = new String[][] { {"所属用户", "UserAccount" }, {"昵称", "Username" },{"手机","Phone"}
                    ,{"邮箱","Email"},{"账户余额","Account_balance"},{"用户标签", "Markname" },{"地域","Regionname"},{"账户创建时间","CreateTime"},
                    {"归属", "Belong_account" }, {"真实主机名", "HostName" }, {"显示名称", "DisplayName" },
                    {"CPU核心数量", "Cpuname" }, {"内存", "Memoryname" }, {"系统磁盘", "Sysdiskname" },
                    {"数据磁盘", "Datadiskname" }, {"网络带宽", "Bandwidthname" }, {"内部监控地址", "InnerMonitorAddr" },
                    {"外部监控地址", "OuterMonitorAddr" }, {"备注", "Description" } };
            ExportExcelUtils.export(request, response, cloudHostList, "用户云主机数据", columns, CloudHostVO.class);
        } catch (Exception e) {
            logger.error("AgentServiceImpl.exporCloudHostData()", e);
            throw new AppException("导出失败");
        }
    }

    /**
     * @Description:导出用户云硬盘数据
     * @param request
     * @param response
     */
    @RequestMapping(value = "/cloudDiskExportData.do")
    public void exporCloudDiskData(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("ExportController.exporCloudDiskData()");
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json; charset=utf-8");
            CloudDiskService cloudDiskService = CoreSpringContextManager.getCloudDiskService();
            List<CloudDiskVO> cloudDiskList = cloudDiskService.getAllCloudDiskForExport(request, response);
            for (CloudDiskVO disk : cloudDiskList) {
                if (!"".equals(disk.getDisk())) {
                    disk.setDiskname(CapacityUtil.toCapacityLabel(disk.getDisk(), 0));
                }
                // 地域
                if (disk.getRegion() == 1) {
                    disk.setRegionname("广州");
                } else if (disk.getRegion() == 2) {
                    disk.setRegionname("成都");
                } else if (disk.getRegion() == 3) {
                    disk.setRegionname("北京");
                } else {
                    disk.setRegionname("香港");
                }
                // 状态
                if (disk.getStatus() == 1) {
                    disk.setStatusname("正常");
                } else if (disk.getStatus() == 2) {
                    disk.setStatusname("停机");
                } else {
                    disk.setStatusname("欠费");
                }

            }
            String[][] columns = new String[][] { {"所属用户", "UserName" }, {"用户昵称", "Accountname" },
                    {"用户标签", "Markname" }, {"归属", "Belongaccount" }, {"硬盘名", "Name" }, {"IP", "Ip" },
                    {"硬盘大小", "Diskname" }, {"地域", "Regionname" }, {"状态", "Statusname" }, {"创建时间", "CreateTime" } };
            ExportExcelUtils.export(request, response, cloudDiskList, "用户云硬盘数据", columns, CloudDiskVO.class);
        } catch (Exception e) {
            logger.error("AgentServiceImpl.exporCloudDiskData()", e);
            throw new AppException("导出失败");
        }
    }
    
    /**
     * @Description:导出用户专属云数据
     * @param request
     * @param response
     */
    @RequestMapping(value = "/cloudVPCExportData.do")
    public void exporCloudVPCData(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("ExportController.exporCloudVPCData()");
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json; charset=utf-8");
            VpcService vpcService = CoreSpringContextManager.getVpcService();
            List<VpcBaseInfoVO> vpcList = vpcService.queryVpcHostForExport(request, response);
            String[][] columns = new String[][] { {"所属用户", "UserName" }, {"VPC名", "DisplayName" },
                    {"地域", "RegionFormat" }, {"状态", "StatusFormat" }, {"绑定主机数", "HostAmount" }, {"绑定IP数", "IpAmount" } };
            ExportExcelUtils.export(request, response, vpcList, "用户专属云数据", columns, VpcBaseInfoVO.class);
        } catch (Exception e) {
            logger.error("AgentServiceImpl.exporCloudVPCData()", e);
            throw new AppException("导出失败");
        }
    }
    
    /**
     * @Description:导出现金券数据
     * @param request
     * @param response
     */
    @RequestMapping(value = "/cashCouponExportData.do")
    public void exporCashCouponData(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("ExportController.exporCashCouponData()");
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json; charset=utf-8");
            CashCouponService cashService = CoreSpringContextManager.getCashCouponService();
            List<CashCouponVO> cashList = cashService.queryCashCouponForExport(request, response);
            for (CashCouponVO cash : cashList) {
                if (cash.getStatus() == 1) {
                    cash.setStatusname("未发送");
                } else if (cash.getStatus() == 2) {
                    cash.setStatusname("已发送");
                } else if (cash.getStatus() == 3) {
                    cash.setStatusname("已过期");
                } else if (cash.getStatus() == 4) {
                    cash.setStatusname("已使用");
                }
                // 时间
                if (!"".equals(cash.getCreateTime())) {
                    cash.setCreateTime(DateUtil.dateToString(
                            DateUtil.stringToDate(cash.getCreateTime(), "yyyyMMddHHmmssSSS"), "yyyy-MM-dd HH:mm:ss"));
                }

            }
            String[][] columns = new String[][] { {"现金券券码", "CashCode" }, {"所属运营商", "UserName" }, {"昵称", "Name" },
                    {"价值", "Money" }, {"创建时间", "CreateTime" }, {"电子邮箱", "Email" }, {"手机号码", "Phone" },
                    {"状态", "Statusname" }, {"发送地址", "SendAddress" } };
            ExportExcelUtils.export(request, response, cashList, "现金券数据", columns, CashCouponVO.class);
        } catch (Exception e) {
            logger.error("AgentServiceImpl.exporCashCouponData()", e);
            throw new AppException("导出失败");
        }
    }
    
    /**
     * @Description:导出终端用户的消费情况
     * @param request
     * @param response
     */
    @RequestMapping(value = "/userConsumExportData.do")
    public void exporUserConsumData(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("ExportController.exporUserConsumData()");
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json; charset=utf-8");
            // 消费时间
            String consum_time_from = StringUtil.trim(request.getParameter("consum_time_from"));
            String consum_time_to = StringUtil.trim(request.getParameter("consum_time_to"));
            Map<String, Object> condition = new LinkedHashMap<String, Object>();
            //取得已定义好的特殊标签
            MarkService markService = CoreSpringContextManager.getMarkService();
            MarkVO markvo = markService.getSpecialMark("消费");
            if (markvo != null) {
                condition.put("markId", markvo.getId());
            }
            // 查询用户(根据消费时间)
            TerminalUserService userService = CoreSpringContextManager.getTerminalUserService();
            List<TerminalUserVO> userList = userService.queryTerminalUserForConsum(request, response, condition);
            condition.put("consum_time_from", consum_time_from);
            condition.put("consum_time_to", consum_time_to);
            // 根据记录设置表头
            List<Map<String, String>> titles = new ArrayList<Map<String, String>>();
            Map<String, String> title = new LinkedHashMap<String, String>();
            Map<String, String> amounts = new LinkedHashMap<String, String>();
            title.put("name", "日期");
            title.put("code", "change_time");
            titles.add(title);
            if (userList.size() > 0) {
                for (TerminalUserVO user : userList) {
                    Map<String, String> title1 = new LinkedHashMap<String, String>();
                    title1.put("name", user.getAccount());
                    title1.put("code", user.getAccount());
                    titles.add(title1);
                    // 余额
                    amounts.put(user.getAccount(), user.getAccount_balance());
                }
            }
            // 消费日期
            Date consum_from = new SimpleDateFormat("yyyy-MM-dd").parse(consum_time_from);
            Date consum_to = new SimpleDateFormat("yyyy-MM-dd").parse(consum_time_to);
            // 获取相减后天数
            long day = (consum_to.getTime() - consum_from.getTime()) / (24 * 60 * 60 * 1000);
            AccountBalanceService accountService = CoreSpringContextManager.getAccountBalanceService();
            AccountBalanceDetailVO detail = null;
            List<Map<String, String>> details = new LinkedList<Map<String, String>>();
            
            SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = new GregorianCalendar();
            // 循环日期
            for (int i = 0; i < day; i++) {
                condition.put("i", i);
                calendar.setTime(consum_from);
                calendar.add(Calendar.DATE, i);//
                Map<String, String> map = new LinkedHashMap<String, String>();
                map.put("change_time", formatter.format(calendar.getTime()));
                for (TerminalUserVO user : userList) {
                    condition.put("user_id", user.getId());
                    detail = accountService.getConsumDetail(request, response, condition);
                    map.put(user.getAccount(), detail == null ? "" : detail.getAmount().toString());
                }
                details.add(map);
            }
            ExportExcelUtils.exportConsumData(request, response, details, "消费记录", titles, amounts);
        } catch (Exception e) {
            logger.error("AgentServiceImpl.exporUserConsumData()", e);
            throw new AppException("导出失败");
        }
    }
}

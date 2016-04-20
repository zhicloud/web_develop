package com.zhicloud.op.service.impl;

import com.zhicloud.op.app.authorization.PrivilegeConstant;
import com.zhicloud.op.app.helper.*;
import com.zhicloud.op.app.helper.RegionHelper.RegionData;
import com.zhicloud.op.app.pool.CloudHostData;
import com.zhicloud.op.app.pool.CloudHostPoolManager;
import com.zhicloud.op.app.pool.host.back.HostBackupProgressData;
import com.zhicloud.op.app.pool.host.back.HostBackupProgressPool;
import com.zhicloud.op.app.pool.host.back.HostBackupProgressPoolManager;
import com.zhicloud.op.app.pool.host.reset.HostResetProgressData;
import com.zhicloud.op.app.pool.host.reset.HostResetProgressPool;
import com.zhicloud.op.app.pool.host.reset.HostResetProgressPoolManager;
import com.zhicloud.op.app.pool.hostMonitorInfoPool.HostMonitorInfo;
import com.zhicloud.op.app.pool.hostMonitorInfoPool.HostMonitorInfoManager;
import com.zhicloud.op.app.pool.network.NetworkInfoExt;
import com.zhicloud.op.app.pool.network.NetworkInfoExt.Host;
import com.zhicloud.op.app.propeties.AppProperties;
import com.zhicloud.op.common.util.*;
import com.zhicloud.op.common.util.json.JSONLibUtil;
import com.zhicloud.op.core.CoreSpringContextManager;
import com.zhicloud.op.exception.AppException;
import com.zhicloud.op.httpGateway.HttpGatewayAsyncChannel;
import com.zhicloud.op.httpGateway.HttpGatewayChannelExt;
import com.zhicloud.op.httpGateway.HttpGatewayManager;
import com.zhicloud.op.httpGateway.HttpGatewayResponseHelper;
import com.zhicloud.op.login.LoginInfo;
import com.zhicloud.op.mybatis.mapper.*;
import com.zhicloud.op.remote.BeanDirectCallableDefaultImpl;
import com.zhicloud.op.remote.CallWithoutLogin;
import com.zhicloud.op.remote.Callable;
import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.request.RequestContext;
import com.zhicloud.op.service.CloudHostService;
import com.zhicloud.op.service.NetworkService;
import com.zhicloud.op.service.VpcService;
import com.zhicloud.op.service.constant.AppConstant;
import com.zhicloud.op.service.constant.AppInconstant;
import com.zhicloud.op.vo.*;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Transactional(readOnly = true)
public class CloudHostServiceImpl extends BeanDirectCallableDefaultImpl implements CloudHostService {

    public static final Logger logger = Logger.getLogger(CloudHostServiceImpl.class);

    private SqlSession sqlSession;

    private Date flag;

    private String port;

    public void setSqlSession(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public void setFlag(Date flag) {
        this.flag = flag;
    }

    public Date getFlag() {
        return flag;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getPort() {
        return port;
    }

    // ---------------

    /**
     * 自用云主机
     */
    @Callable
    public String selfUseCloudUsePage(HttpServletRequest request,
        HttpServletResponse response) {
        logger.debug("CloudHostServiceImpl.selfUseCloudUsePage()");

        // 权限判断
        LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
        if (loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_OPERATOR
            && loginInfo.hasPrivilege(PrivilegeConstant.self_use_cloud_host_page) == false) {
            return "/public/have_not_access.jsp";
        } else if (loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_AGENT
            && loginInfo.hasPrivilege(PrivilegeConstant.self_use_cloud_host_page_agent) == false) {
            return "/public/have_not_access.jsp";
        }

        return "/security/operator/self_use_cloud_host.jsp";
    }

    /**
     * 自用云主机的查询结果
     */
    @Callable
    public String cloudHostQueryResultPartPage(HttpServletRequest request,
        HttpServletResponse response) {
        logger.debug("CloudHostServiceImpl.cloudHostQueryResultPartPage()");

        // 权限判断
        LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
        if (loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_OPERATOR
            && loginInfo.hasPrivilege(PrivilegeConstant.self_use_cloud_host_page) == false) {
            return "/public/have_not_access.jsp";
        } else if (loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_AGENT
            && loginInfo.hasPrivilege(PrivilegeConstant.self_use_cloud_host_page_agent) == false) {
            return "/public/have_not_access.jsp";
        }

        CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);

        // 参数处理
        String cloudHostName = StringUtil.trim(request.getParameter("cloudHostName"));

        //
        Map<String, Object> condition = new LinkedHashMap<String, Object>();
        condition.put("cloudHostName", "%" + cloudHostName + "%");
        List<CloudHostVO> selfUseCloudHostList =
            cloudHostMapper.queryOperatorSelfUseCloudHost(condition);
        request.setAttribute("selfUseCloudHostList", selfUseCloudHostList);

        return "/security/operator/self_use_cloud_host_query_part.jsp";
    }

    /**
     * 创建云主机
     */
    @Callable
    public String createCloudHostPage(HttpServletRequest request,
        HttpServletResponse response) {
        logger.debug("CloudHostServiceImpl.createCloudHostPage()");

        // 权限判断
        LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
        if (loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_OPERATOR
            && loginInfo.hasPrivilege(PrivilegeConstant.cloud_host_create) == false) {
            return "/public/have_not_access_dialog.jsp";
        } else if (loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_AGENT
            && loginInfo.hasPrivilege(PrivilegeConstant.cloud_host_create_agent) == false) {
            return "/public/have_not_access_dialog.jsp";
        }

        CpuPackageOptionMapper cpuPackageOptionMapper =
            this.sqlSession.getMapper(CpuPackageOptionMapper.class);
        MemoryPackageOptionMapper memoryPackageOptionMapper =
            this.sqlSession.getMapper(MemoryPackageOptionMapper.class);
        DiskPackageOptionMapper diskPackageOptionMapper =
            this.sqlSession.getMapper(DiskPackageOptionMapper.class);
        SysDiskImageMapper sysDiskImageMapper = this.sqlSession.getMapper(SysDiskImageMapper.class);

        // 获取CPU套餐项
        List<CpuPackageOptionVO> cpuOptions = cpuPackageOptionMapper.getAll();
        request.setAttribute("cpuOptions", cpuOptions);

        // 获取内存套餐项
        List<MemoryPackageOptionVO> memoryOptions = memoryPackageOptionMapper.getAll();
        request.setAttribute("memoryOptions", memoryOptions);


        // 磁盘套餐项
        DiskPackageOptionVO diskOption = diskPackageOptionMapper.getOne();
        if (diskOption == null) { // 如果还没有配置磁盘套餐项，则设置一下默认的
            diskOption = new DiskPackageOptionVO(CapacityUtil.fromCapacityLabel("1GB"),
                CapacityUtil.fromCapacityLabel("100GB"));
        }
        request.setAttribute("diskOption", diskOption);
        // 系统磁盘镜像
        List<SysDiskImageVO> sysDiskImageOptions =
            sysDiskImageMapper.getDiyImage(loginInfo.getUserId());
        List<SysDiskImageVO> CommonSysDiskImage = sysDiskImageMapper.getAllCommonImage();
        sysDiskImageOptions.addAll(CommonSysDiskImage);
        request.setAttribute("sysDiskImageOptions", sysDiskImageOptions);
        if (loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_AGENT) {

            request.setAttribute("userId", StringUtil.trim(request.getParameter("terminalUserId")));
            request.setAttribute("hostName", this.getNewCloudHostNameByUserId(
                StringUtil.trim(request.getParameter("terminalUserId")), "1"));
            AgentMapper agentMapper = this.sqlSession.getMapper(AgentMapper.class);
            AgentVO agentVO = agentMapper.getAgentById(loginInfo.getUserId());
            request.setAttribute("agent", agentVO);


            //获取套餐

            PackagePriceMapper packagePriceMapper =
                this.sqlSession.getMapper(PackagePriceMapper.class);
            CloudHostSysDefaultPortsMapper cloudHostSysDefaultPortsMapper =
                this.sqlSession.getMapper(CloudHostSysDefaultPortsMapper.class);
            Map<String, Object> data = new LinkedHashMap<String, Object>();
            data.put("status1", 2);
            data.put("status2", 3);
            List<PackagePriceVO> allPriceList = packagePriceMapper.getAllPackagePrice(data);
            List<PackagePriceVO> package1 = new ArrayList<PackagePriceVO>();
            List<PackagePriceVO> package2 = new ArrayList<PackagePriceVO>();
            List<PackagePriceVO> package4 = new ArrayList<PackagePriceVO>();
            List<PackagePriceVO> cpuRegion1 = new ArrayList<PackagePriceVO>();
            List<PackagePriceVO> cpuRegion2 = new ArrayList<PackagePriceVO>();
            List<PackagePriceVO> cpuRegion4 = new ArrayList<PackagePriceVO>();
            List<PackagePriceVO> memoryRegion1 = new ArrayList<PackagePriceVO>();
            List<PackagePriceVO> memoryRegion2 = new ArrayList<PackagePriceVO>();
            List<PackagePriceVO> memoryRegion4 = new ArrayList<PackagePriceVO>();
            List<PackagePriceVO> diskRegion1 = new ArrayList<PackagePriceVO>();
            List<PackagePriceVO> diskRegion2 = new ArrayList<PackagePriceVO>();
            List<PackagePriceVO> diskRegion4 = new ArrayList<PackagePriceVO>();
            List<PackagePriceVO> bandwidthRegion1 = new ArrayList<PackagePriceVO>();
            List<PackagePriceVO> bandwidthRegion2 = new ArrayList<PackagePriceVO>();
            List<PackagePriceVO> bandwidthRegion4 = new ArrayList<PackagePriceVO>();
            for (PackagePriceVO vo : allPriceList) {
                if (vo.getType() != null && vo.getType() == 1 && vo.getRegion() == 1) {
                    cpuRegion1.add(vo);
                } else if (vo.getType() != null && vo.getType() == 1 && vo.getRegion() == 2) {
                    cpuRegion2.add(vo);
                } else if (vo.getType() != null && vo.getType() == 1 && vo.getRegion() == 4) {
                    cpuRegion4.add(vo);
                } else if (vo.getType() != null && vo.getType() == 2 && vo.getRegion() == 1) {
                    memoryRegion1.add(vo);
                } else if (vo.getType() != null && vo.getType() == 2 && vo.getRegion() == 2) {
                    memoryRegion2.add(vo);
                } else if (vo.getType() != null && vo.getType() == 2 && vo.getRegion() == 4) {
                    memoryRegion4.add(vo);
                } else if (vo.getType() != null && vo.getType() == 3 && vo.getRegion() == 1) {
                    diskRegion1.add(vo);
                } else if (vo.getType() != null && vo.getType() == 3 && vo.getRegion() == 2) {
                    diskRegion2.add(vo);
                } else if (vo.getType() != null && vo.getType() == 3 && vo.getRegion() == 4) {
                    diskRegion4.add(vo);
                } else if (vo.getType() != null && vo.getType() == 4 && vo.getRegion() == 1) {
                    bandwidthRegion1.add(vo);
                } else if (vo.getType() != null && vo.getType() == 4 && vo.getRegion() == 2) {
                    bandwidthRegion2.add(vo);
                } else if (vo.getType() != null && vo.getType() == 4 && vo.getRegion() == 4) {
                    bandwidthRegion4.add(vo);
                } else if (vo.getType() != null && vo.getType() == 5 && vo.getRegion() == 1) {
                    package1.add(vo);
                } else if (vo.getType() != null && vo.getType() == 5 && vo.getRegion() == 2) {
                    package2.add(vo);
                } else if (vo.getType() != null && vo.getType() == 5 && vo.getRegion() == 4) {
                    package4.add(vo);
                }
            }
            request.setAttribute("cpuRegion1", cpuRegion1);
            request.setAttribute("cpuRegion2", cpuRegion2);
            request.setAttribute("cpuRegion4", cpuRegion4);
            request.setAttribute("memoryRegion1", memoryRegion1);
            request.setAttribute("memoryRegion2", memoryRegion2);
            request.setAttribute("memoryRegion4", memoryRegion4);
            request.setAttribute("diskRegion1", diskRegion1);
            request.setAttribute("diskRegion2", diskRegion2);
            request.setAttribute("diskRegion4", diskRegion4);
            request.setAttribute("bandwidthRegion1", bandwidthRegion1);
            request.setAttribute("bandwidthRegion2", bandwidthRegion2);
            request.setAttribute("bandwidthRegion4", bandwidthRegion4);
            request.setAttribute("package1", package1);
            request.setAttribute("package2", package2);
            request.setAttribute("package4", package4);
            request.setAttribute("dataDiskMin", AppProperties.getValue("dataDiskMin", ""));
            request.setAttribute("dataDiskMax", AppProperties.getValue("dataDiskMax", ""));
            request.setAttribute("bandwidthMin_1", AppProperties.getValue("bandwidthMin_1", ""));
            request.setAttribute("bandwidthMin_2", AppProperties.getValue("bandwidthMin_2", ""));
            request.setAttribute("bandwidthMin_4", AppProperties.getValue("bandwidthMin_4", ""));
            request.setAttribute("bandwidthMax_1", AppProperties.getValue("bandwidthMax_1", ""));
            request.setAttribute("bandwidthMax_2", AppProperties.getValue("bandwidthMax_2", ""));
            request.setAttribute("bandwidthMax_4", AppProperties.getValue("bandwidthMax_4", ""));

            List<CloudHostSysDefaultPortsVO> defaultPorts =
                cloudHostSysDefaultPortsMapper.getAllPorts();
            request.setAttribute("defaultPorts", defaultPorts);

            return "/security/agent/cloud_host_create.jsp";
        }

        return "/security/operator/cloud_host_create.jsp";
    }

    /**
     * 查看详情页面
     */
    @Callable
    public String cloudHostViewDetailPage(HttpServletRequest request,
        HttpServletResponse response) {
        logger.debug("CloudHostServiceImpl.cloudHostViewDetailPage()");

        // 权限判断
        LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
        if (loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_OPERATOR
            && loginInfo.hasPrivilege(PrivilegeConstant.cloud_host_view_detail) == false) {
            return "/public/have_not_access_dialog.jsp";
        } else if (loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_AGENT
            && loginInfo.hasPrivilege(PrivilegeConstant.cloud_host_view_detail_agent) == false) {
            return "/public/have_not_access_dialog.jsp";
        }

        CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
        CloudHostOpenPortMapper cloudHostOpenPortMapper =
            this.sqlSession.getMapper(CloudHostOpenPortMapper.class);

        // 参数处理
        String cloudHostId = StringUtil.trim(request.getParameter("cloudHostId"));
        if (StringUtil.isBlank(cloudHostId)) {
            throw new AppException("cloudHostId不能为空");
        }
        CloudHostVO cloudHost = cloudHostMapper.getById(cloudHostId);
        request.setAttribute("cloudHost", cloudHost);
        //      if (cloudHost == null || "创建中".equals(cloudHost.getSummarizedStatusText()) || "创建失败".equals(cloudHost.getSummarizedStatusText())) {
        //          request.setAttribute("message", "找不到云主机信息");
        //          return "/public/warning_dialog.jsp";
        //      }
        if (cloudHost == null) {
            request.setAttribute("message", "找不到云主机信息");
            return "/public/warning_dialog.jsp";
        }

        // 创建成功，转到查看云主机详情页面
        if (cloudHost.getRealHostId() != null
            || AppConstant.CLOUD_HOST_SHOPPING_CONFIG_PROCESS_STATUS_SUCCESS
            .equals(cloudHost.getProcessStatus())) {
            List<CloudHostOpenPortVO> ports = cloudHostOpenPortMapper.getByHostId(cloudHostId);
            request.setAttribute("ports", ports);
            if (loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_AGENT) {
                return "/security/agent/diagram.jsp";
            } else if (loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_OPERATOR) {
                return "/security/operator/cloud_host_view_detail.jsp";
            } else {
                return "/security/user/diagram.jsp";
            }
        }
        // 非创建成功，转到查看创建进度的页面
        else {
            try {
                MethodResult creationResult = getCloudHostCreationResult(cloudHostId);
                request.setAttribute("progress", creationResult.get("progress"));
                request.setAttribute("creation_status", creationResult.get("creation_status"));
                request.setAttribute("creation_result", creationResult.get("creation_result"));
                return "/security/operator/cloud_host_creation_progress.jsp";
            } catch (Exception e) {
                request.setAttribute("message", e.getMessage());
                return "/public/warning_dialog.jsp";
            }
        }

    }

    /**
     *
     */
    @Callable
    public String startCloudHostPage(HttpServletRequest request,
        HttpServletResponse response) {
        logger.debug("CloudHostServiceImpl.startCloudHostPage()");

        // 权限判断
        LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
        if (loginInfo.getUserType() != AppConstant.SYS_USER_TYPE_TERMINAL_USER
            && loginInfo.hasPrivilege(PrivilegeConstant.operator_self_use_cloud_host_start_page)
            == false) {
            return "/public/have_not_access_dialog.jsp";
        }

        // 参数处理
        String cloudHostId = StringUtil.trim(request.getParameter("cloudHostId"));
        if (StringUtil.isBlank(cloudHostId)) {
            throw new AppException("cloudHostId不能为空");
        }

        CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);

        // 获取云主机的信息
        CloudHostVO cloudHostVO = cloudHostMapper.getById(cloudHostId);
        if (cloudHostVO == null) {
            throw new AppException("找不到对应的云主机信息");
        }
        request.setAttribute("cloudHostVO", cloudHostVO);

        return "/security/operator/start_cloud_host_dlg.jsp";
    }

    /**
     *
     */
    @Callable
    public String restartCloudHostPage(HttpServletRequest request,
        HttpServletResponse response) {
        logger.debug("CloudHostServiceImpl.restartCloudHostPage()");

        // 权限判断
        LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
        if (loginInfo.getUserType() != 4
            && loginInfo.hasPrivilege(PrivilegeConstant.operator_self_use_cloud_host_start_page)
            == false) {
            return "/public/have_not_access_dialog.jsp";
        }

        // 参数处理
        String cloudHostId = StringUtil.trim(request.getParameter("cloudHostId"));
        if (StringUtil.isBlank(cloudHostId)) {
            throw new AppException("cloudHostId不能为空");
        }

        CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);

        // 获取云主机的信息
        CloudHostVO cloudHostVO = cloudHostMapper.getById(cloudHostId);
        if (cloudHostVO == null) {
            throw new AppException("找不到对应的云主机信息");
        }
        request.setAttribute("cloudHostVO", cloudHostVO);

        return "/security/operator/start_cloud_host_dlg.jsp";
    }

    /**
     *
     */
    @Callable
    public String insertIsoImagePage(HttpServletRequest request,
        HttpServletResponse response) {
        logger.debug("CloudHostServiceImpl.insertIsoImagePage()");

        CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);

        // 权限判断
        LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
        if (loginInfo.getUserType() != AppConstant.SYS_USER_TYPE_TERMINAL_USER && loginInfo
            .hasPrivilege(PrivilegeConstant.operator_self_use_cloud_host_insert_iso_image_page)
            == false) {
            return "/public/have_not_access_dialog.jsp";
        }

        // 参数处理
        String cloudHostId = StringUtil.trim(request.getParameter("cloudHostId"));
        if (StringUtil.isBlank(cloudHostId)) {
            throw new AppException("cloudHostId不能为空");
        }

        //
        CloudHostVO cloudHostVO = cloudHostMapper.getById(cloudHostId);
        if (cloudHostVO == null) {
            throw new AppException("找不到云主机信息");
        }
        request.setAttribute("cloudHostVO", cloudHostVO);

        return "/security/operator/insert_iso_image.jsp";
    }

    /**
     *
     */
    @Callable
    public String managePage(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("CloudHostServiceImpl.managePage()");

        LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
        if (loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_OPERATOR
            && loginInfo.hasPrivilege(PrivilegeConstant.cloud_host_manage_page) == false) {
            return "/public/have_not_access.jsp";
        } else if (loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_AGENT
            && loginInfo.hasPrivilege(PrivilegeConstant.cloud_host_manage_page_agent) == false) {
            return "/public/have_not_access.jsp";
        }
        if (loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_AGENT) {
            return "/security/agent/cloud_host_manage.jsp";
        }
        String userId = request.getParameter("user_id");
        MarkMapper markMapper = this.sqlSession.getMapper(MarkMapper.class);
        List<MarkVO> markList = markMapper.getAll();
        request.setAttribute("markList", markList);
        request.setAttribute("userId", userId);
        return "/security/operator/cloud_host_manage.jsp";
    }

    /**
     * 编辑端口
     */
    @Callable
    public String addPortPage(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("CloudHostServiceImpl.addPortPage()");
        LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
        // 检测运营商权限
        if (loginInfo.getUserType() != 4
            && loginInfo.hasPrivilege(PrivilegeConstant.operator_self_use_cloud_host_add_port_page)
            == false) {
            return "/public/have_not_access_dialog.jsp";
        }

        String hostId = request.getParameter("hostId");

        CloudHostOpenPortMapper cloudHostOpenPortMapper =
            this.sqlSession.getMapper(CloudHostOpenPortMapper.class);
        List<CloudHostOpenPortVO> data = cloudHostOpenPortMapper.getByHostId(hostId);
        
        CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
        CloudHostVO host = cloudHostMapper.getById(hostId);
        request.setAttribute("host", host);
        request.setAttribute("host_id", hostId);
        request.setAttribute("data", data);
        return "/security/user/add_port.jsp";
    }

    /**
     * 绑定云终端
     */
    @Callable
    public String cloudTerminalBindingPage(HttpServletRequest request,
        HttpServletResponse response) {
        logger.debug("CloudHostServiceImpl.cloudTerminalBindingPage()");
        LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
        // 检测运营商权限
        if (loginInfo.getUserType() != 4 && loginInfo
            .hasPrivilege(PrivilegeConstant.operator_self_use_cloud_host_terminal_binding_page)
            == false) {
            return "/public/have_not_access_dialog.jsp";
        }
        String hostId = request.getParameter("hostId");
        String userId = request.getParameter("userId");

        CloudTerminalBindingMapper cloudTerminalBindingMapper =
            this.sqlSession.getMapper(CloudTerminalBindingMapper.class);

        Map<String, Object> condition = new LinkedHashMap<String, Object>();
        condition.put("userId", userId);
        condition.put("hostId", hostId);
        List<CloudTerminalBindingVO> data =
            cloudTerminalBindingMapper.getCloudTerminalId(condition);

        request.setAttribute("user_id", userId);
        request.setAttribute("host_id", hostId);
        request.setAttribute("data", data);

        return "/security/user/cloud_terminal_binding.jsp";
    }

    /**
     * 修改监控密码
     */
    @Callable
    public String updatePasswordPage(HttpServletRequest request,
        HttpServletResponse response) {
        logger.debug("CloudHostServiceImpl.updatePasswordPage()");
        LoginInfo loginInfo = LoginHelper.getLoginInfo(request);

        // 检测运营商权限
        if (loginInfo.getUserType() != 4 &&
            loginInfo.hasPrivilege(PrivilegeConstant.operator_self_use_cloud_host_update_password)
                == false) {
            return "/public/have_not_access_dialog.jsp";
        }

        String hostId = request.getParameter("hostId");
        String userId = request.getParameter("userId");
        request.setAttribute("user_id", userId);
        request.setAttribute("host_id", hostId);

        if (loginInfo.getUserType() == 4) {
            return "/security/user/cloud_update_password.jsp";
        }

        return "/security/operator/cloud_update_password.jsp";
    }

    /**
     * 进入云主机
     */
    @Callable
    public String cloudHostPage(HttpServletRequest request,
        HttpServletResponse response) {
        logger.debug("CloudHostServiceImpl.cloudHostPage()");
        // 权限判断
        LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
        if (loginInfo.getUserType() != AppConstant.SYS_USER_TYPE_TERMINAL_USER) {
            return "/public/have_not_access_dialog.jsp";
        }

        String ip = StringUtil.trim(request.getParameter("ip"));
        String password = StringUtil.trim(request.getParameter("password"));
        String port = getPort(ip);
        request.setAttribute("ip", ip);
        request.setAttribute("port", port);
        request.setAttribute("password", password);

        // 心跳检测，查看浏览器客户端是否健在
        final ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
        Runnable pinger = new Runnable() {
            public void run() {
                Date flag = getFlag();
                String obj_port = getPort();
                Date now = new Date();
                long diff = now.getTime() - flag.getTime();
                if ((diff / 1000) > 10) {
                    sendPort(obj_port);
                    ses.shutdown();
                }
            }
        };
        ses.scheduleAtFixedRate(pinger, 10, 10, TimeUnit.SECONDS);
        try {
            Thread thread = Thread.currentThread();
            thread.sleep(2000);// 暂停2秒,等待 Python 服务开启
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "/security/user/enter_my_cloud_host.jsp";
    }

    /**
     * 配置端口
     */
    @Callable
    public String modifyPortPage(HttpServletRequest request,
        HttpServletResponse response) {
        logger.debug("CloudHostServiceImpl.modifyPortPage()");
        String hostId = request.getParameter("hostId");
        CloudHostOpenPortMapper cloudHostOpenPortMapper =
            this.sqlSession.getMapper(CloudHostOpenPortMapper.class);
        CloudHostSysDefaultPortsMapper cloudHostSysDefaultPortsMapper =
            this.sqlSession.getMapper(CloudHostSysDefaultPortsMapper.class);

        List<CloudHostOpenPortVO> ports = cloudHostOpenPortMapper.getByHostId(hostId);
        List<CloudHostOpenPortVO> defaultPortsVO = new LinkedList<CloudHostOpenPortVO>();
        List<CloudHostSysDefaultPortsVO> cloudHostDefaultPortsVO =
            new LinkedList<CloudHostSysDefaultPortsVO>();

        List<String> defaultNames = new LinkedList<String>();
        List<String> defaultPorts = new LinkedList<String>();

        cloudHostDefaultPortsVO = cloudHostSysDefaultPortsMapper.getAllPorts();

        Iterator<CloudHostOpenPortVO> iterator = ports.iterator();
        while (iterator.hasNext()) {
            CloudHostOpenPortVO port = iterator.next();
            //合并相同服务的端口
            if ("FTP".equals(port.getName())) {
                //移除相同服务的其他端口
                if (port.getPort() != 21) {
                    iterator.remove();
                } else {
                    //添加到默认列表
                    defaultPortsVO.add(port);
                    iterator.remove();
                }
            }
            if ("RDP".equals(port.getName()) || "SSH".equals(port.getName()) || "主机面板"
                .equals(port.getName())) {
                defaultPortsVO.add(port);
                iterator.remove();
            }
        }

        for (CloudHostSysDefaultPortsVO defaultPort : cloudHostDefaultPortsVO) {
            defaultPorts.add(defaultPort.getPort());
            defaultNames.add(defaultPort.getName());

        }

        String defaultPortsStr = defaultPorts.toString().split("\\[|\\]")[1];
        String defaultNamesStr = defaultNames.toString().split("\\[|\\]")[1];

        request.setAttribute("default_ports", defaultPortsStr);
        request.setAttribute("default_names", defaultNamesStr);
        request.setAttribute("host_id", hostId);
        request.setAttribute("default_portsVO", defaultPortsVO);
        request.setAttribute("ports", ports);
        return "/security/user/edit_ports.jsp";
    }


    public String getPort(String ip) {
        try {
            Socket socket = new Socket("127.0.0.1", 5959);
            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os);
            pw.write(ip);
            pw.flush();
            socket.shutdownOutput();
            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String port = br.readLine();
            br.close();
            is.close();
            pw.close();
            os.close();
            socket.close();
            return port;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 修改端口配置
     */
    @Callable
    public void keepAlive(String port) {
        setFlag(new Date());
        setPort(port);
    }

    // --------------------------

    @Callable
    public void queryCloudHost(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("CloudHostServiceImpl.queryCloudHost()");
        try {
            // 权限判断
            LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
            if (loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_OPERATOR
                && loginInfo.hasPrivilege(PrivilegeConstant.cloud_host_manage_page) == false) {
                throw new AppException("您没有权限进行些操作");
            } else if (loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_AGENT
                && loginInfo.hasPrivilege(PrivilegeConstant.cloud_host_manage_page_agent)
                == false) {
                throw new AppException("您没有权限进行些操作");
            }

            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json; charset=utf-8");

            // 获取参数
            String queryMark = StringUtil.trim(request.getParameter("query_by_mark"));
            String queryStatus = StringUtil.trim(request.getParameter("query_by_status"));
            String region = StringUtil.trim(request.getParameter("region"));
            String hostName = StringUtil.trim(request.getParameter("host_name"));
            String username = StringUtil.trim(request.getParameter("username"));
            String belongaccount = StringUtil.trim(request.getParameter("belongaccount"));
            String outerIp = StringUtil.trim(request.getParameter("outerIp"));
            String userId = StringUtil.trim(request.getParameter("user_id"));
            Integer page =
                StringUtil.parseInteger(request.getParameter("page"), 1) - 1; // 前台过来的是从1开始，需要的是从0开始
            Integer rows = StringUtil.parseInteger(request.getParameter("rows"), 10);
            if ("all".equals(queryStatus)) {
                queryStatus = null;
            }
            if ("all".equals(queryMark)) {
                queryMark = null;
            }
            if (StringUtil.isBlank(region)) {
                region = null;
            }
            if(userId!=null && "null".equals(userId)){
            	userId = null;
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_MONTH, -7);
            String dayTime = DateUtil.dateToString(calendar.getTime(), "yyyyMMddHHmmssSSS");
            // 查询数据库
            CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
            Map<String, Object> condition = new LinkedHashMap<String, Object>();
            condition.put("agentId", loginInfo.getUserId());
            condition.put("hostName", "%" + hostName + "%");
            condition.put("start_row", page * rows);
            condition.put("row_count", rows);
            int total = 0;
            List<CloudHostVO> cloudHostList = null;
            if (loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_AGENT) {
                total = cloudHostMapper.queryPageCountForAgent(condition); // 总行数
                cloudHostList = cloudHostMapper.queryPageForAgent(condition);// 分页结果
            } else {
                condition.put("markId", queryMark);
                condition.put("queryStatus", queryStatus);
                condition.put("region", region);
                condition.put("time", dayTime);
                condition.put("userId", userId);
                condition.put("username", "%" + username + "%");
                condition.put("belongaccount", "%" + belongaccount + "%");
                condition.put("outerIp", "%" + outerIp + "%");
                total = cloudHostMapper.queryPageCount(condition); // 总行数
                cloudHostList = cloudHostMapper.queryPage(condition);// 分页结果
            }
            String outerport = "";
            String innerport = "";
            String temp_name = "";
            String outerip = "";
            String innerip = "";
            // 处理特殊合并项字段
            if (cloudHostList != null && cloudHostList.size() > 0) {
                for (CloudHostVO host : cloudHostList) {
                    host.setHostanddisplay(host.getHostName() + "<br>" + host.getDisplayName());
                    host.setSystemanddatadisk(CapacityUtil.toCapacityLabel(host.getSysDisk(), 0) + "<br>"
                            + CapacityUtil.toCapacityLabel(host.getDataDisk(), 0));
                    if (host.getOuterPort() == null || "null".equals(host.getOuterPort())) {
                        outerport = "无";
                    } else {
                        outerport = host.getOuterPort() + "";
                    }
                    if (host.getInnerPort() == null || "null".equals(host.getInnerPort())) {
                        innerport = "无";
                    } else {
                        innerport = host.getInnerPort() + "";
                    }
                    if (host.getPhone() == null || "".equals(host.getPhone())) {
                        temp_name = "无";
                    } else {
                        temp_name = host.getPhone();
                    }
                    if (host.getInnerIp() == null || "".equals(host.getInnerIp())) {
                        innerip = "无";
                    } else {
                        innerip = host.getInnerIp();
                    }
                    if (host.getOuterIp() == null || "".equals(host.getOuterIp())) {
                        outerip = "无";
                    } else {
                        outerip = host.getOuterIp();
                    }
                    if ("停机".equals(host.getSummarizedStatusText())) {
                        host.setInactivateTimeText(host.getSummarizedStatusText()
                                + "<br>"
                                + DateUtil.dateToString(
                                        DateUtil.stringToDate(host.getInactivateTime(), "yyyyMMddHHmmssSSS"),
                                        "yyyy-MM-dd HH:mm:ss"));
                    } else {
                        host.setInactivateTimeText(host.getSummarizedStatusText());
                    }
                    host.setOuterandinnerip(innerip + ":" + innerport + "<br>" + outerip + ":" + outerport);
                    host.setBelong_accountandusername(host.getBelong_account() + "<br>" + temp_name);
                    host.setEverymonth(host.getMonthlyPrice() + "<br>" + host.getAccount_balance());
                }
            }
           
            // 输出json数据
            ServiceHelper
                .writeDatagridResultAsJsonTo(response.getOutputStream(), total, cloudHostList);
        } catch (Exception e) {
            logger.error(e);
            throw new AppException("失败");
        }
    }

    @SuppressWarnings("unchecked")
    @Callable
    @Transactional(readOnly = false)
    public MethodResult addCloudHost(Map<String, Object> parameter) {
        Long begin = System.currentTimeMillis();
        Integer logStatus = AppConstant.OPER_LOG_FAIL;
        HttpServletRequest request = RequestContext.getHttpRequest();
        LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
        String _hostName = "";
        logger.debug("CloudHostServiceImpl.addCloudHost()");
        try {
            // 参数处理
            String hostName = StringUtil.trim(parameter.get("host_name"));
            Integer region = Integer.valueOf((String) parameter.get("region"));
            String cpuCore = StringUtil.trim(parameter.get("cpu_core"));
            String memory = StringUtil.trim(parameter.get("memory"));
            String sysDiskType = StringUtil.trim(parameter.get("sys_disk_type"));
            String sysImageId = StringUtil.trim(parameter.get("sys_image_id"));
            String dataDisk = StringUtil.trim(parameter.get("data_disk"));
            List<String> isAutoStartup = (List<String>) parameter.get("is_auto_startup");
            List<String> ports = (List<String>) parameter.get("ports");
            _hostName = hostName;
            if (region == null) {
                return new MethodResult(MethodResult.FAIL, "'地域'不能为空");
            }
            if (StringUtil.isBlank(hostName)) {
                return new MethodResult(MethodResult.FAIL, "主机名不能为空");
            }
            if (StringUtil.isBlank(cpuCore)) {
                return new MethodResult(MethodResult.FAIL, "请选择CPU核心数");
            }
            if (StringUtil.isBlank(memory)) {
                return new MethodResult(MethodResult.FAIL, "请选择内存");
            }

            CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
            SysDiskImageMapper sysDiskImageMapper =
                this.sqlSession.getMapper(SysDiskImageMapper.class);

            SysDiskImageVO sysDiskImageVO = null;
            if (sysDiskType.equals("from_sys_image")) {
                if (StringUtil.isBlank(sysImageId)) {
                    return new MethodResult(MethodResult.FAIL, "请选择系统磁盘镜像");
                }
                sysDiskImageVO = sysDiskImageMapper.getById(sysImageId);
                if (sysDiskImageVO == null || StringUtil.isBlank(sysDiskImageVO.getRealImageId())) {
                    return new MethodResult(MethodResult.FAIL, "选择的磁盘镜像还没有创建成功");
                }
            }

            // “T2_”开头表示这个运营商的云主机
            hostName = "T2_" + hostName;

            // 判断主机名是否已经存在
            CloudHostVO cloudHost = cloudHostMapper.getByHostName(hostName);
            if (cloudHost != null) {
                return new MethodResult(MethodResult.FAIL, "主机名[" + hostName + "]已经存在");
            }

            // 如果选择了“从磁盘镜像”创建
            if (sysDiskType.equals("from_sys_image")) {
                // 从云主机仓库获取云主机
                Map<String, Object> newCloudHostData = new LinkedHashMap<String, Object>();
                newCloudHostData.put("type", AppConstant.CLOUD_HOST_TYPE_1_OPERATOR);
                newCloudHostData.put("userId", loginInfo.getUserId());
                newCloudHostData.put("hostName", hostName);
                newCloudHostData.put("account", loginInfo.getAccount());
                newCloudHostData.put("password", loginInfo.getAccount());
                newCloudHostData.put("cpuCore", Integer.valueOf(cpuCore));
                newCloudHostData.put("memory", new BigInteger(memory));
                newCloudHostData.put("dataDisk", CapacityUtil.fromCapacityLabel(dataDisk));
                newCloudHostData.put("bandwidth", new BigInteger("9999999999"));
                newCloudHostData.put("isAutoStartup", isAutoStartup.contains("yes") ?
                    AppConstant.CLOUD_HOST_IS_AUTO_STARTUP_YES :
                    AppConstant.CLOUD_HOST_IS_AUTO_STARTUP_NO);
                newCloudHostData.put("ports", _formatToHttpGatewayFormatPorts(ports));
                newCloudHostData.put("monthlyPrice", "0");
                MethodResult result = new CloudHostWarehouseHelper(this.sqlSession)
                    .getAndAllocateWarehouseCloudHost(sysImageId, region, newCloudHostData);
                if (MethodResult.SUCCESS.equals(result.status)) {
                    logStatus = AppConstant.OPER_LOG_SUCCESS;
                    return result;
                }
            }

            // 从云主机仓库获取云主机失败，创建新的云主机
            return _createSelfUseCloudHost(region, hostName, parameter, sysDiskImageVO);

        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException(e);
        } finally {
            try {
                OperLogMapper operLogMapper = this.sqlSession.getMapper(OperLogMapper.class);
                Map<String, Object> operLogData = new LinkedHashMap<String, Object>();
                operLogData.put("id", StringUtil.generateUUID());
                operLogData.put("userId", loginInfo.getUserId());
                operLogData.put("content", "创建云主机:" + _hostName);
                operLogData
                    .put("operTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
                operLogData.put("status", logStatus);
                operLogData.put("resourceName", "主机名:" + _hostName);
                operLogData.put("operDuration", System.currentTimeMillis() - begin);
                operLogMapper.addOperLog(operLogData);
            } catch (Exception e) {
                logger.error(e);
            }

        }
    }

    /**
     *
     */
    @SuppressWarnings("unchecked")
    private MethodResult _createSelfUseCloudHost(Integer region,
        String hostName, Map<String, Object> parameter, SysDiskImageVO sysDiskImageVO)
        throws MalformedURLException, IOException {
        HttpServletRequest request = RequestContext.getHttpRequest();
        LoginInfo loginInfo = LoginHelper.getLoginInfo(request);

        // 参数处理
        String cpuCore = StringUtil.trim(parameter.get("cpu_core"));
        String memory = StringUtil.trim(parameter.get("memory"));
        String sysDiskType = StringUtil.trim(parameter.get("sys_disk_type"));
        String sysDiskImage = StringUtil.trim(parameter.get("sys_image_id"));
        String sysDisk = StringUtil.trim(parameter.get("sys_disk"));
        String dataDisk = StringUtil.trim(parameter.get("data_disk"));
        List<String> isAutoStartup = (List<String>) parameter.get("is_auto_startup");
        List<String> ports = (List<String>) parameter.get("ports");
        String sysImageName = sysDiskImageVO.getName();
        if (region == null) {
            throw new AppException("region is null");
        }

        CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);

        // 添加云主机进数据库
        String hostId = StringUtil.generateUUID();
        Map<String, Object> cloudHostData = new LinkedHashMap<String, Object>();
        cloudHostData.put("id", hostId);
        cloudHostData.put("realHostId", null);
        cloudHostData.put("type", AppConstant.CLOUD_HOST_TYPE_1_OPERATOR);
        cloudHostData.put("userId", loginInfo.getUserId());
        cloudHostData.put("hostName", hostName);
        cloudHostData.put("displayName", hostName);
        cloudHostData.put("account", loginInfo.getAccount());
        cloudHostData.put("password", loginInfo.getAccount());
        cloudHostData.put("cpuCore", Integer.parseInt(cpuCore));
        cloudHostData.put("memory", new BigInteger(memory));
        cloudHostData.put("sysImageId", sysDiskImage);
        cloudHostData.put("sysImageName", sysImageName);
        cloudHostData.put("sysDisk", CapacityUtil.fromCapacityLabel(sysDisk));
        cloudHostData.put("dataDisk", CapacityUtil.fromCapacityLabel(dataDisk));
        cloudHostData.put("bandwidth", new BigInteger("9999999999"));
        cloudHostData.put("isAutoStartup", isAutoStartup.contains("yes") ?
            AppConstant.CLOUD_HOST_IS_AUTO_STARTUP_YES :
            AppConstant.CLOUD_HOST_IS_AUTO_STARTUP_NO);
        cloudHostData.put("runningStatus", AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTDOWN);
        cloudHostData.put("status", AppConstant.CLOUD_HOST_STATUS_1_NORNAL);
        cloudHostData.put("region", region);

        int n = cloudHostMapper.addCloudHost(cloudHostData);
        if (n <= 0) {
            throw new AppException("添加失败");
        }

        // 从云主机缓冲池里面取，如果这个名字的云主机在平台已经存在，则可以找到
        CloudHostData cacheCloudHostData =
            CloudHostPoolManager.getCloudHostPool().getByHostName(region, hostName);
        if (cacheCloudHostData != null) {
            _handleCloudHostFromPool(hostId, region, cacheCloudHostData);
        }
        // 如果这个名字的云主机不存在，则发消息到云平台去创建
        else {
            try {
                // 添加开放端口
                CloudHostOpenPortMapper cloudHostOpenPortMapper =
                    this.sqlSession.getMapper(CloudHostOpenPortMapper.class);
                for (String port : ports) {
                    String[] arr = port.split(":");
                    if (arr.length != 2) {
                        throw new AppException("端口格式错误, [" + port + "]");
                    }
                    Map<String, Object> cloudHostOpenPort = new LinkedHashMap<String, Object>();
                    cloudHostOpenPort.put("id", StringUtil.generateUUID());
                    cloudHostOpenPort.put("hostId", hostId);
                    cloudHostOpenPort.put("protocol", Integer.parseInt(arr[0]));
                    cloudHostOpenPort.put("port", Integer.parseInt(arr[1]));
                    cloudHostOpenPortMapper.addCloudHostOpenPort(cloudHostOpenPort);
                }

                // 发消息到http gateway
                HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(region);

                // 获取默认资源池
                JSONObject computePool = channel.getDefaultComputePool();
                if (computePool == null) {
                    logger.info(
                        "CloudHostServiceImpl.addCloudHost() > [" + Thread.currentThread().getId()
                            + "] 无法获取计算资源池");
                    throw new AppException("无法获取计算资源池");
                }

                Integer[] options = null;
                String realDiskImageId = null;
                if (sysDiskType.equals("from_sys_image") == false) {
                    options = new Integer[] {0, 1, 0, 1, 1};
                    realDiskImageId = "";
                } else {
                    options = new Integer[] {1, 1, 0, 1, 1};
                    realDiskImageId = sysDiskImageVO.getRealImageId();
                }

                JSONObject createHostResult = channel
                    .hostCreate(hostName, (String) computePool.get("uuid"),
                        Integer.valueOf(cpuCore), new BigInteger(memory), options, realDiskImageId,
                        new BigInteger[] {CapacityUtil.fromCapacityLabel(sysDisk),
                            CapacityUtil.fromCapacityLabel(dataDisk)},
                        _formatToHttpGatewayFormatPorts(ports), loginInfo.getAccount(), "T2",
                        loginInfo.getAccount(), loginInfo.getAccount(), "",
                        new BigInteger("9999999999"), new BigInteger("9999999999"));

                if (HttpGatewayResponseHelper.isSuccess(createHostResult) == false) {
                    logger.warn(
                        "CloudHostServiceImpl.addCloudHost() > [" + Thread.currentThread().getId()
                            + "] create host '" + hostName + "' failed, message:["
                            + HttpGatewayResponseHelper.getMessage(createHostResult) + "]");
                    // return new MethodResult(MethodResult.FAIL,
                    // "http gateway创建云主机失败");
                    throw new AppException("http gateway创建云主机失败");
                }
                logger.warn(
                    "CloudHostServiceImpl.addCloudHost() > [" + Thread.currentThread().getId()
                        + "] create host '" + hostName + "' succeeded, message:["
                        + HttpGatewayResponseHelper.getMessage(createHostResult) + "]");
            } catch (ConnectException e) {
                throw new AppException("连接http gateway失败", e);
            }
        }

        return new MethodResult(MethodResult.SUCCESS, "添加成功");
    }

    private Integer[] _formatToHttpGatewayFormatPorts(List<String> ports) {
        List<Integer> result = new ArrayList<Integer>();
        for (String port : ports) {
            String[] arr = port.split(":");
            result.add(Integer.valueOf(arr[0])); // protocol
            result.add(Integer.valueOf(arr[1])); // port
        }
        return result.toArray(new Integer[0]);
    }

    /**
     * @param hostId             : mysql的hostId
     * @param cacheCloudHostData : 缓存的真实云主机的信息
     */
    private void _handleCloudHostFromPool(String hostId, Integer region,
        CloudHostData cacheCloudHostData) throws MalformedURLException, IOException {
        String uuid = cacheCloudHostData.getRealHostId();
        String hostName = cacheCloudHostData.getHostName();

        logger.info(
            "CloudHostServiceImpl.handleCloudHostFromPool() > [" + Thread.currentThread().getId()
                + "] 云主机已经存在, uuid:[" + uuid + "], name:[" + hostName + "]");

        CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
        CloudHostOpenPortMapper cloudHostOpenPortMapper =
            this.sqlSession.getMapper(CloudHostOpenPortMapper.class);

        HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(region);

        JSONObject hostResult = channel.hostQueryInfo(uuid);

        if (HttpGatewayResponseHelper.isSuccess(hostResult) == false) {
            // 失败，不处理
            logger.warn(
                "CloudHostServiceImpl.handleCloudHostFromPool() > [" + Thread.currentThread()
                    .getId() + "] query host info fail, uuid[" + uuid + "]");
            return;
        }

        JSONObject hostInfo = (JSONObject) hostResult.get("host");

        BigInteger[] diskVolume = JSONLibUtil.getBigIntegerArray(hostInfo, "disk_volume");
        String[] ip = JSONLibUtil.getStringArray(hostInfo, "ip");
        Integer[] displayPort = JSONLibUtil.getIntegerArray(hostInfo, "display_port");
        Integer[] port = JSONLibUtil.getIntegerArray(hostInfo, "port");

        // 更新云主机的real_host_id字段
        Map<String, Object> cloudHostData = new LinkedHashMap<String, Object>();
        cloudHostData.put("id", hostId);
        cloudHostData.put("realHostId", uuid);
        cloudHostData.put("sysDisk", diskVolume[0]);
        cloudHostData.put("dataDisk", diskVolume[1]);
        cloudHostData.put("innerIp", ip[0]);
        cloudHostData.put("innerPort", displayPort[0]);
        cloudHostData.put("outerIp", ip[1]);
        cloudHostData.put("outerPort", displayPort[1]);
        cloudHostMapper.updateRealHostIdById(cloudHostData);

        // 更新(先删除，后添加)云主机的端口
        cloudHostOpenPortMapper.deleteByHostId(hostId);

        for (int j = 0; j < port.length; j += 4) {
            long protocol = port[j + 0];
            long serverPort = port[j + 1];
            long hostPort = port[j + 2];
            long outerPort = port[j + 3];
            Map<String, Object> portData = new LinkedHashMap<String, Object>();
            portData.put("id", StringUtil.generateUUID());
            portData.put("hostId", hostId);
            portData.put("protocol", protocol);
            portData.put("port", hostPort);
            portData.put("serverPort", serverPort);
            portData.put("outerPort", outerPort);
            cloudHostOpenPortMapper.addCloudHostOpenPort(portData);
        }
    }

    /**
     *
     */
    @Callable
    @Transactional(readOnly = false)
    public MethodResult operatorDeleteCloudHostByIds(
        List<?> hostIds) {
        logger.debug(
            "CloudHostServiceImpl.operatorDeleteCloudHostByIds() > [" + Thread.currentThread()
                .getId() + "] ");
        try {
            if (hostIds == null || hostIds.size() == 0) {
                throw new AppException("hostIds不能为空");
            }
            VpcBaseInfoMapper vpcBaseInfoMapper =
                this.sqlSession.getMapper(VpcBaseInfoMapper.class);
            CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
            VpcBindPortMapper vpcBindPortMapper =
                this.sqlSession.getMapper(VpcBindPortMapper.class);
            List<String> ids = new ArrayList<String>();
            int resultCount = 0;
            for (int i = 0; i < hostIds.size(); i++) {
                String hostId = String.valueOf(hostIds.get(i));
                CloudHostVO cloudHost = cloudHostMapper.getById(hostId);
                if (cloudHost.getRealHostId() == null) {
                    // 如果没有成功，则删除数据库数据即可
                    int n = cloudHostMapper.deleteByIds(new String[] {hostId});
                    if (n <= 0) {
                        throw new AppException("删除失败");
                    } else {
                        VpcBaseInfoVO vo = vpcBaseInfoMapper.queryVpcByHostId(cloudHost.getId());
                        if (vo != null) {
                            Map<String, Object> data = new LinkedHashMap<String, Object>();
                            data.put("id", vo.getId());
                            data.put("amount", -1);
                            vpcBaseInfoMapper.updateVpcHostAmount(data);
                        }
                        resultCount++;
                    }
                    continue;
                }
                // 从http gateway删除
                HttpGatewayChannelExt channel =
                    HttpGatewayManager.getChannel(cloudHost.getRegion());
                JSONObject hostDeleteResult = channel.hostDelete(cloudHost.getRealHostId());
                if (HttpGatewayResponseHelper.isSuccess(hostDeleteResult)) {
                    logger.info("CloudHostServiceImpl.operatorDeleteCloudHostByIds() > [" + Thread
                        .currentThread().getId() + "] delete host succeeded, uuid:[" + cloudHost
                        .getRealHostId() + "], message:[" + HttpGatewayResponseHelper
                        .getMessage(hostDeleteResult) + "]");
                    ids.add(hostId);
                } else {
                    // 删除失败，回滚事务
                    logger.warn("CloudHostServiceImpl.operatorDeleteCloudHostByIds() > [" + Thread
                        .currentThread().getId() + "] delete host failed, uuid:[" + cloudHost
                        .getRealHostId() + "], message:[" + HttpGatewayResponseHelper
                        .getMessage(hostDeleteResult) + "]");
                    //查询主机是否存在，不存在也加入删除数据库
                    JSONObject hostResult = channel.hostQueryInfo(cloudHost.getRealHostId());
                    if (HttpGatewayResponseHelper.isSuccess(hostResult) == false) {
                        ids.add(hostId);
                    }
                }

                // 从缓冲池删除
                CloudHostPoolManager.getCloudHostPool()
                    .removeByRealHostId(cloudHost.getRealHostId());
                CloudHostPoolManager.getCloudHostPool()
                    .removeByHostName(cloudHost.getRegion(), cloudHost.getHostName());
            }
            for (int i = 0; i < ids.size(); i++) {
                CloudHostVO host = cloudHostMapper.getById((String) ids.get(i));
                if (host.getType() == AppConstant.CLOUD_HOST_TYPE_5_TERMINAL_USER_VPC
                    || host.getType() == AppConstant.CLOUD_HOST_TYPE_6_Agent_VPC) {
                    VpcBaseInfoVO vo = vpcBaseInfoMapper.queryVpcByHostId(host.getId());
                    Map<String, Object> data = new LinkedHashMap<String, Object>();
                    data.put("id", vo.getId());
                    data.put("amount", -1);
                    vpcBaseInfoMapper.updateVpcHostAmount(data);
                }
            }
            // 逻辑删除
            if (ids.size() > 0) {
                resultCount =
                    resultCount + cloudHostMapper.updateForDeleteByIds(ids.toArray(new String[0]));
                vpcBindPortMapper.deleteByHostIds(ids.toArray(new String[0]));
            }
            if (resultCount <= 0) {
                return new MethodResult(MethodResult.FAIL, "删除失败");
            }
            return new MethodResult(MethodResult.SUCCESS, "删除成功");
        } catch (AppException e) {
            throw AppException.wrapException(e);
        } catch (Exception e) {
            throw new AppException("操作失败，请联系管理员");
        }
    }

    /**
     *
     */
    @Callable
    @Transactional(readOnly = false)
    public MethodResult deleteCloudHostById(
        String hostId) {
        Long begin = System.currentTimeMillis();
        Integer logStatus = AppConstant.OPER_LOG_FAIL;
        HttpServletRequest request = RequestContext.getHttpRequest();
        LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
        String hostName = "";

        logger.debug(
            "CloudHostServiceImpl.deleteCloudHostById() > [" + Thread.currentThread().getId()
                + "] ");
        try {
            if (StringUtil.isBlank(hostId)) {
                throw new AppException("hostId不能为空");
            }

            CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
            CloudHostShoppingConfigMapper cloudhostShoppingConfig =
                this.sqlSession.getMapper(CloudHostShoppingConfigMapper.class);

            CloudHostVO cloudHost = cloudHostMapper.getById(hostId);
            if (cloudHost == null) {
                throw new AppException("找不到云主机的数据");
            }

            hostName = cloudHost.getDisplayName();

            // 从http gateway删除
            HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(cloudHost.getRegion());
            JSONObject hostDeleteResult = channel.hostDelete(cloudHost.getRealHostId());
            if (HttpGatewayResponseHelper.isSuccess(hostDeleteResult)) {
                logger.info(
                    "CloudHostServiceImpl.deleteCloudHostById() > [" + Thread.currentThread()
                        .getId() + "] delete host '" + hostName + "' succeeded, uuid:[" + cloudHost
                        .getRealHostId() + "], message:[" + HttpGatewayResponseHelper
                        .getMessage(hostDeleteResult) + "]");
                // 从缓冲池删除
                CloudHostPoolManager.getCloudHostPool().removeByRealHostId(cloudHost.getRealHostId());
                CloudHostPoolManager.getCloudHostPool()
                    .removeByHostName(cloudHost.getRegion(), cloudHost.getHostName());

                // 从数据库删除
                int n = cloudHostMapper.updateForDeleteByIds(new String[] {hostId});
                int m = cloudhostShoppingConfig.deleteByHostId(hostId);

                if (n <= 0 || m <= 0) {
                    return new MethodResult(MethodResult.FAIL, "删除失败");
                }
                logStatus = AppConstant.OPER_LOG_SUCCESS;
                return new MethodResult(MethodResult.SUCCESS, "删除成功");
            } else {
                logger.warn(
                    "CloudHostServiceImpl.deleteCloudHostById() > [" + Thread.currentThread()
                        .getId() + "] delete host '" + hostName + "' failed, uuid:[" + cloudHost
                        .getRealHostId() + "], message:[" + HttpGatewayResponseHelper
                        .getMessage(hostDeleteResult) + "]"); 
                return new MethodResult(MethodResult.FAIL, "删除失败");
            }

            
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException(e);
        } finally {
            try {
                OperLogMapper operLogMapper = this.sqlSession.getMapper(OperLogMapper.class);
                Map<String, Object> operLogData = new LinkedHashMap<String, Object>();
                operLogData.put("id", StringUtil.generateUUID());
                operLogData.put("userId", loginInfo.getUserId());
                operLogData.put("content", "删除云主机:" + hostName);
                operLogData
                    .put("operTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
                operLogData.put("status", logStatus);
                operLogData.put("resourceName", "用户名:" + loginInfo.getAccount());
                operLogData.put("operDuration", System.currentTimeMillis() - begin);
                operLogMapper.addOperLog(operLogData);
            } catch (AppException e) {
                throw e;
            } catch (Exception e) {
                throw new AppException(e);
            }

        }
    }

    /**
     *
     */
    @Callable
    @Transactional(readOnly = false)
    public MethodResult deleteCloudHostByIds(
        List<?> hostIds) {
        Long begin = System.currentTimeMillis();
        Integer logStatus = AppConstant.OPER_LOG_FAIL;
        HttpServletRequest request = RequestContext.getHttpRequest();
        LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
        String _hostName = "";
        logger.debug(
            "CloudHostServiceImpl.deleteCloudHostByIds() > [" + Thread.currentThread().getId()
                + "] ");
        try {
            if (hostIds == null || hostIds.size() == 0) {
                throw new AppException("hostIds不能为空");
            }

            CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
            CloudHostShoppingConfigMapper cloudhostShoppingConfig =
                this.sqlSession.getMapper(CloudHostShoppingConfigMapper.class);
            CloudHostVO cloudHost = cloudHostMapper.getById((String) hostIds.get(0));
            VpcBaseInfoMapper vpcBaseInfoMapper =
                this.sqlSession.getMapper(VpcBaseInfoMapper.class);
            _hostName = cloudHost.getDisplayName();

            // for( int i=0; i<hostIds.size(); i++ )
            // {
            // String hostId = String.valueOf(hostIds.get(i));
            // CloudHostVO cloudHost = cloudHostMapper.getById(hostId);
            // if(loginInfo.getUserType()==4){
            // _hostName = cloudHost.getHostName();
            // }
            // // 从http gateway删除
            // HttpGatewayChannelExt channel =
            // HttpGatewayManager.getChannel(cloudHost.getRegion());
            // JSONObject hostDeleteResult =
            // channel.hostDelete(cloudHost.getRealHostId());
            // if( HttpGatewayResponseHelper.isSuccess(hostDeleteResult) )
            // {
            // logger.info("CloudHostServiceImpl.deleteCloudHostByIds() > ["+Thread.currentThread().getId()+"] delete host succeeded, uuid:["+cloudHost.getRealHostId()+"], message:["+HttpGatewayResponseHelper.getMessage(hostDeleteResult)+"]");
            // }
            // else
            // {
            // logger.warn("CloudHostServiceImpl.deleteCloudHostByIds() > ["+Thread.currentThread().getId()+"] delete host failed, uuid:["+cloudHost.getRealHostId()+"], message:["+HttpGatewayResponseHelper.getMessage(hostDeleteResult)+"]");
            // }
            //
            // // 从缓冲池删除
            // CloudHostPoolManager.getCloudHostPool().removeByRealHostId(cloudHost.getRealHostId());
            // CloudHostPoolManager.getCloudHostPool().removeByHostName(cloudHost.getRegion(),
            // cloudHost.getHostName());
            // }

            // 如果是VPC云主机，需要立即结算，产生新的账单
            if (cloudHost.getType() == AppConstant.CLOUD_HOST_TYPE_5_TERMINAL_USER_VPC
                || cloudHost.getType() == AppConstant.CLOUD_HOST_TYPE_6_Agent_VPC) {
                VpcBaseInfoVO vo = vpcBaseInfoMapper.queryVpcByHostId(cloudHost.getId());
                new VpcBillHelper(sqlSession).settleAllUndoneVpcBills(vo.getId(), new Date(), true);
                Map<String, Object> data = new LinkedHashMap<String, Object>();
                data.put("id", vo.getId());
                data.put("amount", -1);
                vpcBaseInfoMapper.updateVpcHostAmount(data);
            }
            for (int i = 1; i < hostIds.size(); i++) {
                CloudHostVO host = cloudHostMapper.getById((String) hostIds.get(i));
                if (host.getType() == AppConstant.CLOUD_HOST_TYPE_5_TERMINAL_USER_VPC
                    || host.getType() == AppConstant.CLOUD_HOST_TYPE_6_Agent_VPC) {
                    VpcBaseInfoVO vo = vpcBaseInfoMapper.queryVpcByHostId(host.getId());
                    Map<String, Object> data = new LinkedHashMap<String, Object>();
                    data.put("id", vo.getId());
                    data.put("amount", -1);
                    vpcBaseInfoMapper.updateVpcHostAmount(data);
                }
            }
            // 从数据库删除
            int n = cloudHostMapper.deleteByIds(hostIds.toArray(new String[0]));
            int m = cloudhostShoppingConfig.deleteByHostId((String) hostIds.get(0));
            if (n <= 0 || m <= 0) {
                return new MethodResult(MethodResult.FAIL, "删除失败");
            }
            logStatus = AppConstant.OPER_LOG_SUCCESS;
            return new MethodResult(MethodResult.SUCCESS, "删除成功");
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException(e);
        } finally {
            try {
                OperLogMapper operLogMapper = this.sqlSession.getMapper(OperLogMapper.class);
                Map<String, Object> operLogData = new LinkedHashMap<String, Object>();
                operLogData.put("id", StringUtil.generateUUID());
                operLogData.put("userId", loginInfo.getUserId());
                operLogData.put("content", "删除云主机:" + _hostName);
                operLogData
                    .put("operTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
                operLogData.put("status", logStatus);
                operLogData.put("resourceName", "用户名:" + loginInfo.getAccount());
                operLogData.put("operDuration", System.currentTimeMillis() - begin);
                operLogMapper.addOperLog(operLogData);
            } catch (AppException e) {
                throw e;
            } catch (Exception e) {
                throw new AppException(e);
            }

        }
    }

    @SuppressWarnings("unchecked")
    @Callable
    @Transactional(readOnly = false)
    public MethodResult addPort(Map<String, Object> parameter) {
        Long begin = System.currentTimeMillis();
        Integer logStatus = AppConstant.OPER_LOG_FAIL;
        HttpServletRequest request = RequestContext.getHttpRequest();
        LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
        List<Integer> portList = new ArrayList<Integer>();
        logger.debug("CloudHostServiceImpl.addPort()");
        try {
            String hostId = (String) parameter.get("host_id");
            JSONArray portsArr = (JSONArray) parameter.get("ports");
            //          System.out.println(portsArr.toString());
            List<String> ports = new ArrayList<String>();
            List<String> protocols = new ArrayList<String>();

            for (Object param : portsArr) {
                String[] params = param.toString().split("&", 2);
                protocols.add(params[0]);
                ports.add(params[1]);
            }

            // String deletePort = (String) parameter.get("delete_port");
            // String[] deletePorts = deletePort.split(",");

            CloudHostOpenPortMapper cloudHostOpenPortMapper =
                this.sqlSession.getMapper(CloudHostOpenPortMapper.class);
            CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
            CloudHostSysDefaultPortsMapper cloudHostSysDefaultPortsMapper =
                this.sqlSession.getMapper(CloudHostSysDefaultPortsMapper.class);

            //
            CloudHostVO cloudHostVO = cloudHostMapper.getById(hostId);
            if (cloudHostVO == null) {
                throw new AppException("找不到云主机的信息");
            }
            if (StringUtil.isBlank(cloudHostVO.getRealHostId())) {
                throw new AppException("云主机还没有创建 ");
            }

            // 整理端口参数
            List<Integer> arguPorts = new ArrayList<Integer>();
            for (int i = 0; i < protocols.size(); i++) {
                arguPorts.add(Integer.valueOf((String) protocols.get(i)));
                arguPorts.add(Integer.valueOf((String) ports.get(i)));
            }
            if (loginInfo.getUserType() != AppConstant.SYS_USER_TYPE_OPERATOR) {
                for (int j = 30000; j < 30004; j++) {
                    arguPorts.add(1);
                    arguPorts.add(j);
                }
                arguPorts.add(1);
                arguPorts.add(21);
            }

            // 发送消息到http gateway修改端口
            HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(cloudHostVO.getRegion());
            JSONObject hostModifyResult = channel
                .hostModify(cloudHostVO.getRealHostId(), "", 0, BigInteger.ZERO, new Integer[0],
                    arguPorts.toArray(new Integer[0]), "", "", "", BigInteger.ZERO,
                    BigInteger.ZERO);
            if (HttpGatewayResponseHelper.isSuccess(hostModifyResult) == false) {
                logger.info("CloudHostServiceImpl.addPort() > [" + Thread.currentThread().getId()
                    + "] modify host ports failed, exception:[" + HttpGatewayResponseHelper
                    .getMessage(hostModifyResult) + "], region:[" + cloudHostVO.getRegion() + "]");
                throw new AppException("修改云主机开放端口失败");
            }

            // 获取旧的端口信息
            List<CloudHostOpenPortVO> oldPortDatas = cloudHostOpenPortMapper.getByHostId(hostId);
            Map<String, CloudHostOpenPortVO> oldPortsMap =
                new LinkedHashMap<String, CloudHostOpenPortVO>();
            for (CloudHostOpenPortVO oldCloudHostOpenPortVO : oldPortDatas) {
                String key =
                    oldCloudHostOpenPortVO.getProtocol() + "|" + oldCloudHostOpenPortVO.getPort();
                oldPortsMap.put(key, oldCloudHostOpenPortVO);

            }

            // 将新的端口信息保存进数据库
            Set<String> newPortsSet = new LinkedHashSet<String>();
            JSONArray nat = hostModifyResult.getJSONArray("nat");

            for (int i = 0; i < nat.size(); i += 4) {
                Integer protocol = nat.getInt(i);
                Integer serverPort = nat.getInt(i + 1);
                Integer hostPort = nat.getInt(i + 2);
                Integer publicPort = nat.getInt(i + 3);

                String key = protocol + "|" + hostPort;
                newPortsSet.add(key);
                CloudHostOpenPortVO cloudHostOpenPortVO = oldPortsMap.get(key);

                Map<String, Object> newPortData = new LinkedHashMap<String, Object>();
                newPortData.put("id", StringUtil.generateUUID());
                newPortData.put("hostId", hostId);
                newPortData.put("protocol", protocol);
                newPortData.put("port", hostPort);

                Map<String, Object> condition = new LinkedHashMap<String, Object>();
                condition.put("protocol", protocol);
                condition.put("port", hostPort);
                CloudHostSysDefaultPortsVO cloudHostSysDefaultPortsVO =
                    cloudHostSysDefaultPortsMapper.getByProtocolAndPort(condition);
                String name = "自定义";
                if (cloudHostSysDefaultPortsVO != null) {
                    name = cloudHostSysDefaultPortsVO.getName();
                }

                newPortData.put("name", name);
                newPortData.put("serverPort", serverPort);
                newPortData.put("outerPort", publicPort);
                portList.add(hostPort);
                if (cloudHostOpenPortVO != null) {
                    // 原来已经有了这个端口
                    cloudHostOpenPortMapper.updateMappingPortsById(newPortData);
                } else {
                    // 原来没有这个端口
                    cloudHostOpenPortMapper.addCloudHostOpenPort(newPortData);
                }
            }


            // 删除一些旧的没有用的端口
            Iterator<Entry<String, CloudHostOpenPortVO>> iter = oldPortsMap.entrySet().iterator();
            while (iter.hasNext()) {
                Entry<String, CloudHostOpenPortVO> entry = iter.next();
                String key = entry.getKey();
                CloudHostOpenPortVO oldPortData = entry.getValue();

                if (newPortsSet.contains(key) == false) {
                    cloudHostOpenPortMapper.deleteById(oldPortData.getId());
                }
            }

            logStatus = AppConstant.OPER_LOG_SUCCESS;
            return new MethodResult(MethodResult.SUCCESS, "保存成功");
        } catch (Exception e) {
            logger.error(e);
            throw new AppException("保存失败");
        } finally {
            try {
                OperLogMapper operLogMapper = this.sqlSession.getMapper(OperLogMapper.class);
                Map<String, Object> operLogData = new LinkedHashMap<String, Object>();
                operLogData.put("id", StringUtil.generateUUID());
                operLogData.put("userId", loginInfo.getUserId());
                operLogData.put("content", "添加端口:" + portList);
                operLogData
                    .put("operTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
                operLogData.put("status", logStatus);
                operLogData.put("resourceName", "用户名:" + loginInfo.getAccount());
                operLogData.put("operDuration", System.currentTimeMillis() - begin);
                operLogMapper.addOperLog(operLogData);
            } catch (Exception e) {
                logger.error(e);
            }

        }
    }

    @SuppressWarnings("unchecked")
    @Callable
    @Transactional(readOnly = false)
    public MethodResult cloudTerminalBinding(Map<String, Object> parameter) {
        Long begin = System.currentTimeMillis();
        Integer logStatus = AppConstant.OPER_LOG_FAIL;
        HttpServletRequest request = RequestContext.getHttpRequest();
        LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
        logger.debug(
            "CloudHostServiceImpl.cloudTerminalBinding() > [" + Thread.currentThread().getId()
                + "] ");
        try {

            String userId = (String) parameter.get("user_id");
            String hostId = (String) parameter.get("host_id");
            List<String> cloudTerminalIds = (List<String>) parameter.get("cloud_terminal_ids");

            CloudTerminalBindingMapper cloudTerminalBindingMapper =
                this.sqlSession.getMapper(CloudTerminalBindingMapper.class);

            // 删除旧数据
            Map<String, Object> condition = new LinkedHashMap<String, Object>();
            condition.put("userId", userId);
            condition.put("hostId", hostId);
            cloudTerminalBindingMapper.deleteByUserIdAndHostId(condition);

            // 插入数据库
            for (int i = 0; i < cloudTerminalIds.size(); i++) {
                Map<String, Object> data_for_check = new LinkedHashMap<String, Object>();
                data_for_check.put("userId", userId);
                data_for_check.put("cloudTerminalId", cloudTerminalIds.get(i));
                List<CloudTerminalBindingVO> vo =
                    cloudTerminalBindingMapper.getCloudTerminalBycloudTerminalId(data_for_check);
                // 判断是否已经被绑定
                if (vo == null || vo.size() == 0) {

                    Map<String, Object> data = new LinkedHashMap<String, Object>();
                    data.put("id", StringUtil.generateUUID());
                    data.put("hostId", hostId);
                    data.put("userId", userId);
                    data.put("cloudTerminalId", cloudTerminalIds.get(i));
                    cloudTerminalBindingMapper.addCloudTerminalBinding(data);
                } else {
                    throw new AppException(cloudTerminalIds.get(i) + "已经被绑定");
                }
            }
            logStatus = AppConstant.OPER_LOG_SUCCESS;
            return new MethodResult(MethodResult.SUCCESS, "保存成功");
        } catch (Exception e) {
            logger.error(e);
            throw new AppException("保存失败");
        } finally {
            try {
                OperLogMapper operLogMapper = this.sqlSession.getMapper(OperLogMapper.class);
                Map<String, Object> operLogData = new LinkedHashMap<String, Object>();
                operLogData.put("id", StringUtil.generateUUID());
                operLogData.put("userId", loginInfo.getUserId());
                operLogData.put("content", "绑定云终端");
                operLogData
                    .put("operTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
                operLogData.put("status", logStatus);
                operLogData.put("resourceName", "用户名:" + loginInfo.getAccount());
                operLogData.put("operDuration", System.currentTimeMillis() - begin);
                operLogMapper.addOperLog(operLogData);
            } catch (Exception e) {
                logger.error(e);
            }

        }
    }

    @Transactional(readOnly = false)
    public MethodResult fetchNewestCloudHostFromHttpGateway() {
        MethodResult result = new MethodResult(MethodResult.SUCCESS);
        CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);

        RegionData[] regionDatas = RegionHelper.singleton.getAllResions();
        for (RegionData regionData : regionDatas) {
            try {
                HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(regionData.getId());
                // 获取默认计算资源池
                JSONObject defaultComputePool = channel.getDefaultComputePool();
                if (defaultComputePool == null) {// 不能获取，则跳过该region
                    logger.error(String.format("fail to get default compute pool. region[%s].",
                        regionData.getId()));
                    continue;
                }

                // 从http gateway获取所有的云主机
                JSONObject hostQueryResult =
                    channel.hostQuery((String) defaultComputePool.get("uuid"));
                if (HttpGatewayResponseHelper.isSuccess(hostQueryResult) == false) {// 失败则跳过该region
                    logger.info(String
                        .format("fail to query host from http gateway. region[%s], message[%s]",
                            regionData.getId(),
                            HttpGatewayResponseHelper.getMessage(hostQueryResult)));
                    continue;
                }

                JSONArray hosts = (JSONArray) hostQueryResult.get("hosts");
                // 连接region成功后，维护该region的云主机资源监控信息
                HostMonitorInfo hostMonitorInfo =
                    HostMonitorInfoManager.singleton().getHostMonitorInfo(regionData.getId());
                if (hostMonitorInfo == null) {
                    hostMonitorInfo = new HostMonitorInfo();
                    hostMonitorInfo.setRegion(regionData.getId());
                    hostMonitorInfo.setNeedToRestart(false);
                    HostMonitorInfoManager.singleton().saveHostMonitorInfo(hostMonitorInfo);
                }

                int total = 0;
                List<String> allHostNames = new ArrayList<String>();
                Set<String> hostList = new HashSet<String>();
                Set<String> originalHostList = hostMonitorInfo.getHostList();
                boolean isEqual = true;// hostList 是否与originalHostList相同, 默认相同

                // 循环这些云主机
                for (int i = 0; i < hosts.size(); i++) {
                    JSONObject host = (JSONObject) hosts.get(i);
                    String uuid = JSONLibUtil.getString(host, "uuid");
                    String name = JSONLibUtil.getString(host, "name");

                    if (uuid == null) {// uuid不可以为空
                        logger.warn(String.format(
                            "found no uuid host when fetch host list from http gateway. Host name[%s], region[%s].",
                            name, regionData.getId()));
                        continue;
                    }

                    // 更新缓冲池的数据
                    CloudHostPoolManager.getSingleton()
                        .updateRealCloudHost(regionData.getId(), host);
                    hostList.add(uuid);

                    if (originalHostList != null && originalHostList.contains(uuid)) {
                        // 上次已经处理过
                        continue;
                    } else {
                        isEqual = false;
                    }

                    // 判断数据库中是否已经有了该云主机，没有则开始处理
                    CloudHostVO cloudHostVO = cloudHostMapper.getByRealHostId(uuid);
                    if (cloudHostVO == null) {
                        total++;
                        allHostNames.add(name);
                        _handleOrdinaryHostName(regionData.getId(), host);
                    }
                }
                logger.info(String
                    .format("found new host. total[%s]: %s, region:[%s:%s]", total, allHostNames,
                        regionData.getId(), regionData.getName()));

                if (isEqual && (originalHostList != null && originalHostList.size() != hostList
                    .size())) {
                    isEqual = false;
                }
                // 当主机列表不相同，则设置host_list_update为true
                if (isEqual == false) {
                    synchronized (hostMonitorInfo) {// 同步处理, 保证这是一个操作单元
                        hostMonitorInfo.setHostList(hostList);
                        hostMonitorInfo.setNeedToRestart(true);
                    }
                    result.put("host_list_update", true);
                }
                HttpStatusNoticeVO vo =
                    AppInconstant.httpStatusInfo.get(regionData.getHttpGatewayAddr());
                if (vo != null) {
                    if (vo.getLastNotice() != null) {
                        String notification_on_off =
                            AppProperties.getValue("notification_on_off", "yes");
                        if ("yes".equals(notification_on_off)) {
                            new SendMail().sendHttpGateWayStatus(regionData.getName(), "2");
                            //                          new SendSms().sendHttpGateWayStatus(regionData.getName(),"2",AppProperties.getValue("monitor_phone1", ""));
                            //                          new SendSms().sendHttpGateWayStatus(regionData.getName(),"2",AppProperties.getValue("monitor_phone2", ""));
                            //                          new SendSms().sendHttpGateWayStatus(regionData.getName(),"2",AppProperties.getValue("monitor_phone3", ""));
                        }
                    }
                    AppInconstant.httpStatusInfo.remove(regionData.getHttpGatewayAddr());
                }
            } catch (SocketException e) {
                HttpStatusNoticeVO vo =
                    AppInconstant.httpStatusInfo.get(regionData.getHttpGatewayAddr());
                if (vo == null) {
                    vo = new HttpStatusNoticeVO();
                    vo.setFristHappen(new Date());
                    vo.setTimes(1);
                    AppInconstant.httpStatusInfo.put(regionData.getHttpGatewayAddr(), vo);
                } else {
                    if (vo.getTimes() < 3) {
                        vo.setTimes(vo.getTimes() + 1);
                        AppInconstant.httpStatusInfo.put(regionData.getHttpGatewayAddr(), vo);
                    } else {
                        String notification_on_off =
                            AppProperties.getValue("notification_on_off", "yes");
                        if (vo.getLastNotice() != null) {
                            long diff = new Date().getTime() - vo.getLastNotice().getTime();
                            long min = diff / (1000 * 60);
                            if (min > 60) {
                                if ("yes".equals(notification_on_off)) {
                                                                      new SendMail().sendHttpGateWayStatus(regionData.getName(),"1");
                                    vo.setLastNotice(new Date());
                                    AppInconstant.httpStatusInfo
                                        .put(regionData.getHttpGatewayAddr(), vo);
                                                                      new SendSms().sendHttpGateWayStatus(regionData.getName(),"1",AppProperties.getValue("monitor_phone1", ""));
                                                                      new SendSms().sendHttpGateWayStatus(regionData.getName(),"1",AppProperties.getValue("monitor_phone2", ""));
                                                                      new SendSms().sendHttpGateWayStatus(regionData.getName(),"1",AppProperties.getValue("monitor_phone3", ""));
                                }
                            }
                        } else {
                            if ("yes".equals(notification_on_off)) {
                                vo.setLastNotice(new Date());
                                AppInconstant.httpStatusInfo
                                    .put(regionData.getHttpGatewayAddr(), vo);
                                                              new SendMail().sendHttpGateWayStatus(regionData.getName(),"1");
                                                              new SendSms().sendHttpGateWayStatus(regionData.getName(),"1",AppProperties.getValue("monitor_phone1", ""));
                                                              new SendSms().sendHttpGateWayStatus(regionData.getName(),"1",AppProperties.getValue("monitor_phone2", ""));
                                                              new SendSms().sendHttpGateWayStatus(regionData.getName(),"1",AppProperties.getValue("monitor_phone3", ""));
                            }
                        }
                    }
                }
                logger.error(
                    "connect to http gateway failed, exception:[" + e.getMessage() + "], region:["
                        + String.format("%s:%s", regionData.getId(), regionData.getName()) + "]");
            } catch (Exception e) {
                e.printStackTrace();
                throw AppException.wrapException(e);
            }
        }

        result.status = MethodResult.SUCCESS;
        return result;
    }

    /**
     * @param host : 通过channel.hostQuery(computePoolUUID)获取的云主机数据
     */
    private void _handleOrdinaryHostName(Integer region, JSONObject host)
        throws MalformedURLException, IOException {
        String realHostId = JSONLibUtil.getString(host, "uuid");
        String hostName = JSONLibUtil.getString(host, "name");

        logger.info("CloudHostServiceImpl.fetchNewestCloudHostFromHttpGateway() > [" + Thread
            .currentThread().getId() + "] 发现新创建的云主机, name[" + hostName + "], uuid[" + realHostId
            + "]");

        CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);

        // 查找云主机
        Map<String, Object> condition = new LinkedHashMap<String, Object>();
        condition.put("region", region);
        condition.put("hostName", hostName);
        CloudHostVO cloudHostVO = cloudHostMapper.getByRegionAndHostName(condition);
        if (cloudHostVO == null) {
            logger.warn("CloudHostServiceImpl.fetchNewestCloudHostFromHttpGateway() > [" + Thread
                .currentThread().getId() + "] 数据库中没有发现名为[" + hostName + "]的云主机");
            return;
        }
        if (cloudHostVO.getRealHostId() != null && cloudHostVO.getRealHostId().length() > 0) {
            logger.warn("CloudHostServiceImpl.fetchNewestCloudHostFromHttpGateway() > [" + Thread
                .currentThread().getId() + "] db have host [" + hostName + "] uuid [ " + cloudHostVO
                .getRealHostId() + "]");
            return;
        }

        // 从http gateway获取云主机的详细信息
        HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(cloudHostVO.getRegion());
        JSONObject hostResult = channel.hostQueryInfo(realHostId);

        _handleFoundHost(realHostId, hostResult, cloudHostVO);
    }

    /**
     * @param realHostId          :
     * @param hostQueryInfoResult : 调用channel.hostQueryInfo(realHostId)获取的结果
     * @param cloudHostVO         ：mysql数据库里面对应的记录
     */
    @Transactional(readOnly = false,noRollbackFor={Exception.class})
    private void _handleFoundHost(String realHostId, JSONObject hostQueryInfoResult,
        CloudHostVO cloudHostVO) throws MalformedURLException, IOException {
        logger.info("CloudHostServiceImpl -> _handleFoundHost begin to update cloud_host");
        CloudHostShoppingConfigMapper cloudHostShoppingConfigMapper =
            this.sqlSession.getMapper(CloudHostShoppingConfigMapper.class);
        CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
        CloudHostOpenPortMapper cloudHostOpenPortMapper =
            this.sqlSession.getMapper(CloudHostOpenPortMapper.class);
        CloudHostBillDetailMapper cloudHostBillDetailMapper =
            this.sqlSession.getMapper(CloudHostBillDetailMapper.class);
        CloudHostWarehouseDetailMapper cloudHostWarehouseDetailMapper =
            this.sqlSession.getMapper(CloudHostWarehouseDetailMapper.class);
        CloudHostSysDefaultPortsMapper cloudHostSysDefaultPortsMapper =
            this.sqlSession.getMapper(CloudHostSysDefaultPortsMapper.class);

        String hostId = cloudHostVO.getId();
        String strNow = DateUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS");

        if (HttpGatewayResponseHelper.isSuccess(hostQueryInfoResult) == false) {
            // 失败，不处理
            logger.warn("CloudHostServiceImpl.fetchNewestCloudHostFromHttpGateway() > [" + Thread
                .currentThread().getId() + "] query host info fail, uuid[" + realHostId + "]");
            return;
        }

        JSONObject hostInfo = (JSONObject) hostQueryInfoResult.get("host");

        BigInteger[] diskVolume = JSONLibUtil.getBigIntegerArray(hostInfo, "disk_volume");
        String[] ip = JSONLibUtil.getStringArray(hostInfo, "ip");
        Integer[] displayPort = JSONLibUtil.getIntegerArray(hostInfo, "display_port");
        Integer[] port = JSONLibUtil.getIntegerArray(hostInfo, "port");

        // 更新云主机配置的处理状态
        Map<String, Object> cloudHostConfigData = new LinkedHashMap<String, Object>();
        cloudHostConfigData.put("hostId", hostId);
        cloudHostConfigData
            .put("processStatus", AppConstant.CLOUD_HOST_SHOPPING_CONFIG_PROCESS_STATUS_SUCCESS);
        cloudHostConfigData.put("processMessage", "creation completed");
        cloudHostShoppingConfigMapper.updateProcessStatusByHostId(cloudHostConfigData);

        // 更新云主机的real_host_id字段
        Map<String, Object> cloudHostData = new LinkedHashMap<String, Object>();
        cloudHostData.put("id", hostId);
        cloudHostData.put("realHostId", realHostId);
        cloudHostData.put("sysDisk", diskVolume[0]);
        cloudHostData.put("dataDisk", diskVolume.length < 2 ? BigInteger.ZERO : diskVolume[1]);
        cloudHostData.put("innerIp", ip[0]);
        cloudHostData.put("innerPort", displayPort[0]);
        cloudHostData.put("outerIp", ip[1]);
        cloudHostData.put("outerPort", displayPort[1]);
        cloudHostData.put("createTime", strNow);
        cloudHostMapper.updateRealHostIdById(cloudHostData);

        // 更新数据
        cloudHostVO.setRealHostId(realHostId);
        cloudHostVO.setSysDisk(diskVolume[0]);
        cloudHostVO.setDataDisk(diskVolume.length < 2 ? BigInteger.ZERO : diskVolume[1]);
        cloudHostVO.setInnerIp(ip[0]);
        cloudHostVO.setInnerPort(displayPort[0]);
        cloudHostVO.setOuterIp(ip[1]);
        cloudHostVO.setCreateTime(strNow);

        // 更新(先删除，后添加)云主机的端口
        cloudHostOpenPortMapper.deleteByHostId(hostId);

        for (int j = 0; j < port.length; j += 4) {
            long protocol = port[j + 0];
            long serverPort = port[j + 1];
            long hostPort = port[j + 2];
            long outerPort = port[j + 3];
            Map<String, Object> portData = new LinkedHashMap<String, Object>();
            Map<String, Object> portCondition = new LinkedHashMap<String, Object>();

            portData.put("id", StringUtil.generateUUID());
            portData.put("hostId", hostId);
            portData.put("protocol", protocol);
            portData.put("port", hostPort);

            Integer intProtocol = Integer.parseInt(String.valueOf(protocol));
            Integer intPort = Integer.parseInt(String.valueOf(hostPort));

            //获取服务名称
            portCondition.put("protocol", intProtocol);
            portCondition.put("port", intPort);
            CloudHostSysDefaultPortsVO cloudHostSysDefaultPortsVO =
                cloudHostSysDefaultPortsMapper.getByProtocolAndPort(portCondition);
            if (cloudHostSysDefaultPortsVO != null) {
                String name = cloudHostSysDefaultPortsVO.getName();
                portData.put("name", name);
            } else {
                portData.put("name", "自定义");
            }
            portData.put("serverPort", serverPort);
            portData.put("outerPort", outerPort);
            cloudHostOpenPortMapper.addCloudHostOpenPort(portData);
        }

        if (AppConstant.CLOUD_HOST_TYPE_3_TERMINAL_USER.equals(cloudHostVO.getType())
            || AppConstant.CLOUD_HOST_TYPE_2_AGENT.equals(cloudHostVO.getType())) {
            // 添加一条云主机计费的记录
            Map<String, Object> cloudHostBillDetailData = new LinkedHashMap<String, Object>();
            cloudHostBillDetailData.put("id", StringUtil.generateUUID());
            cloudHostBillDetailData.put("host_id", hostId);
            cloudHostBillDetailData
                .put("type", AppConstant.CLOUD_HOST_BILL_DETAIL_TYPE_4_PAY_FOR_TIME);
            cloudHostBillDetailData.put("cpuCore", cloudHostVO.getCpuCore());
            cloudHostBillDetailData.put("cpuUsed", cloudHostVO.getCpuCore());
            cloudHostBillDetailData
                .put("memory", CapacityUtil.toMBValue(cloudHostVO.getMemory(), 2));
            cloudHostBillDetailData
                .put("memoryUsed", CapacityUtil.toMBValue(cloudHostVO.getMemory(), 2));
            cloudHostBillDetailData.put("sysImageId", cloudHostVO.getSysImageId());
            cloudHostBillDetailData
                .put("sysDisk", CapacityUtil.toGBValue(cloudHostVO.getSysDisk(), 2));
            cloudHostBillDetailData
                .put("sysDiskUsed", CapacityUtil.toGBValue(cloudHostVO.getSysDisk(), 2));
            cloudHostBillDetailData
                .put("dataDisk", CapacityUtil.toGBValue(cloudHostVO.getDataDisk(), 2));
            cloudHostBillDetailData
                .put("dataDiskUsed", CapacityUtil.toGBValue(cloudHostVO.getDataDisk(), 2));
            cloudHostBillDetailData.put("diskRead", 0);
            cloudHostBillDetailData.put("diskWrite", 0);
            cloudHostBillDetailData
                .put("bandwidth", FlowUtil.toMbpsValue(cloudHostVO.getBandwidth(), 2));
            cloudHostBillDetailData.put("networkTraffic", 0);
            cloudHostBillDetailData.put("startTime", strNow);
            cloudHostBillDetailData.put("endTime", null);
            cloudHostBillDetailData.put("fee", null);
            cloudHostBillDetailData.put("isPaid", AppConstant.CLOUD_HOST_BILL_DETAIL_IS_PAID_NOT);
            cloudHostBillDetailData.put("createTime", strNow);
            cloudHostBillDetailMapper.addCloudHostBillDetail(cloudHostBillDetailData);
        } else if (AppConstant.CLOUD_HOST_TYPE_4_WAREHOUSE.equals(cloudHostVO.getType())) {
            // 更新云主机仓库对应记录的状态
            Map<String, Object> warehouseDetailData = new LinkedHashMap<String, Object>();
            warehouseDetailData
                .put("status", AppConstant.CLOUD_HOST_WAREHOUSE_DETAIL_STATUS_3_NOT_DISTRIBUTED);
            warehouseDetailData.put("processMessage", "cloud host created");
            warehouseDetailData.put("hostId", hostId);
            cloudHostWarehouseDetailMapper.updateStatusByHostId(warehouseDetailData);
        }
        // 如果不是仓库云主机，启动云主机
        if (cloudHostVO.getType() != null && cloudHostVO.getType() != 4) {
            if(AppInconstant.cloudHostProgress.get(cloudHostVO.getHostName())!=null){
                CloudHostService cloudHostService = CoreSpringContextManager.getCloudHostService();
                cloudHostService.startCloudHost(hostId);
            }
            
        }
        //云主机关联VPC
        if (cloudHostVO.getType() == AppConstant.CLOUD_HOST_TYPE_5_TERMINAL_USER_VPC
            || cloudHostVO.getType() == AppConstant.CLOUD_HOST_TYPE_6_Agent_VPC) {
            VpcBaseInfoMapper vpcBaseInfoMapper =
                this.sqlSession.getMapper(VpcBaseInfoMapper.class);
            VpcBaseInfoVO vo = vpcBaseInfoMapper.queryVpcByHostId(hostId);
            NetworkService service = CoreSpringContextManager.getNetworkService();
            boolean flag = true;
            NetworkInfoExt network = service.queryHostSync(vo.getRegion(), vo.getRealVpcId());
            if (network == null) {
                logger.info("fail to query network host.");
                return;
            } else {
                logger.info(
                    String.format("success to query network host. uuid[%s]", network.getUuid()));
                Host[] hosts = network.getHostList();
                for (Host host : hosts) {
                    if (host.getUuid().equals("realHostId")) {
                        logger.info(String
                            .format("host_uuid[%s], host_name[%s], host_network_address[%s]",
                                host.getUuid(), host.getName(), host.getNetworkAddress()));
                        flag = false;
                        return;
                    }
                }
            }
            if (flag) {
                NetworkInfoExt attachHostResult =
                    service.attachHostSync(cloudHostVO.getRegion(), vo.getRealVpcId(), realHostId);
                if (attachHostResult != null) {

                    // 重新绑定端口和IP
                    VpcService vpcServie = CoreSpringContextManager.getVpcService();
                    cloudHostVO.setSysImageName(cloudHostVO.getSysImageNameOld());
                    vpcServie.defaultBindPort(vo.getId(), cloudHostVO);
                    //更新vpcip
                    Map<String, Object> data = new LinkedHashMap<String, Object>();
                    data.put("id", cloudHostVO.getId());
                    data.put("vpcIp", attachHostResult.getHostNetworkAddress());
                    cloudHostMapper.updateVpcIpById(data);
                }
                logger.info(
                    "attach host[" + cloudHostVO.getDisplayName() + "] to VPC " + attachHostResult);
            }
        }
        AppInconstant.cloudHostProgress.remove(cloudHostVO.getHostName());
        logger.info("CloudHostServiceImpl -> _handleFoundHost   update cloud_host end");

    }

    /**
     *
     */
    @Transactional(readOnly = false,noRollbackFor={Exception.class})
    public void handleNewlyCreatedCloudHost(int region,
        String realHostId, String hostName, JSONObject hostQueryInfoResult) {
        try {
            logger.info(
                "CloudHostServiceImpl.handleNewlyCreatedCloudHost() > [" + Thread.currentThread()
                    .getId() + "] 发现新创建的云主机, name[" + hostName + "], uuid[" + realHostId + "]");

            CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);

            // 查找云主机
            Map<String, Object> condition = new LinkedHashMap<String, Object>();
            condition.put("region", region);
            condition.put("hostName", hostName);
            CloudHostVO cloudHostVO = cloudHostMapper.getByRegionAndHostName(condition);
            if (cloudHostVO == null) {
                logger.warn("CloudHostServiceImpl.handleNewlyCreatedCloudHost() > [" + Thread
                    .currentThread().getId() + "] 数据库中没有发现名为[" + hostName + "]的云主机");
                return;
            }

            _handleFoundHost(realHostId, hostQueryInfoResult, cloudHostVO);
        } catch (Exception e) {
            throw AppException.wrapException(e);
        }
    }

    /**
     *
     */
    @Transactional(readOnly = false)
    public void handleCreatedFailedCloudHost(String hostName) {
        try {
            logger.info(
                "CloudHostServiceImpl.handleCreatedFailedCloudHost() > [" + Thread.currentThread()
                    .getId() + "] 发现创建失败的云主机, name[" + hostName + "]");

            CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);

            // 查找云主机
            CloudHostVO cloudHostVO = cloudHostMapper.getByHostName(hostName);

            if (cloudHostVO == null) {
                logger.warn("CloudHostServiceImpl.handleNewlyCreatedCloudHost() > [" + Thread
                    .currentThread().getId() + "] 数据库中没有发现名为[" + hostName + "]的云主机");
                return;
            } else {
                // 更新配置表的创建状态为失败
                CloudHostShoppingConfigMapper cloudHostShoppingConfigMapper =
                    this.sqlSession.getMapper(CloudHostShoppingConfigMapper.class);
                Map<String, Object> condition = new LinkedHashMap<String, Object>();
                condition.put("hostName", hostName);
                condition.put("processStatus",
                    AppConstant.CLOUD_HOST_SHOPPING_CONFIG_PROCESS_STATUS_FAIL);
                condition.put("processMessage", "cloud_host_created_failed");
                cloudHostShoppingConfigMapper.updateProcessStatusByHostName(condition);

            }

        } catch (Exception e) {
            throw AppException.wrapException(e);
        }
    }

    /**
     *
     */
    public List<CloudHostVO> getAllCloudHost() {
        CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
        List<CloudHostVO> allCloudHostVO = cloudHostMapper.getAllCloudHost();
        return allCloudHostVO;
    }

    /**
     *
     */
    public int getAllCloudHostCount(String userId) {
        CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, -7);
        String dayTime = DateUtil.dateToString(calendar.getTime(), "yyyyMMddHHmmssSSS");
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("userId", userId);
        data.put("time", dayTime);

        return cloudHostMapper.getAllCloudHostCount(data);
    }

    /**
     *
     */
    @Transactional(readOnly = false)
    public int updateRunningStatusByRealHostId(
        Map<String, Object> cloudHostData) {
        CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
        int n = cloudHostMapper.updateRunningStatusByRealHostId(cloudHostData);
        return n;
    }

    /**
     *
     */
    @Callable @Transactional(readOnly = false)
    public MethodResult startCloudHost(String hostId) {
        String newHostId = hostId;
        if (newHostId != null && newHostId.length() == 37) {
            hostId = hostId.substring(0, hostId.length() - 5);
        }
        Long begin = System.currentTimeMillis();
        Integer logStatus = AppConstant.OPER_LOG_FAIL;
        // HttpServletRequest request = RequestContext.getHttpRequest();
        // LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
        String _hostName = "";
        CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
        CloudHostVO cloudHost = cloudHostMapper.getById(hostId);
        try {
            _hostName = cloudHost.getDisplayName();
            String realHostId = cloudHost.getRealHostId();
            if (realHostId == null) {
                MethodResult result = new MethodResult(MethodResult.FAIL, "云主机尚未创建成功");
                result.put("hostId", hostId);
                return result;
            }
            CloudHostData myCloudHostData =
                CloudHostPoolManager.getCloudHostPool().getByRealHostId(cloudHost.getRealHostId());
            if (myCloudHostData != null && myCloudHostData.getRunningStatus() == 2) {
                Map<String, Object> hostData = new LinkedHashMap<String, Object>();
                //正在启动
                hostData.put("runningStatus", AppConstant.CLOUD_HOST_RUNNING_STATUS_RUNNING);
                hostData.put("realHostId", realHostId);
                cloudHostMapper.updateRunningStatusByRealHostId(hostData);
                logStatus = AppConstant.OPER_LOG_SUCCESS;
                MethodResult result = new MethodResult(MethodResult.SUCCESS, "启动成功");
                result.put("hostId", hostId);
                return result;
            }
            HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(cloudHost.getRegion());
            JSONObject startResullt = channel.hostStart(realHostId, 0, "");

            Map<String, Object> cloudHostData = new LinkedHashMap<String, Object>();
            cloudHostData.put("runningStatus", AppConstant.CLOUD_HOST_RUNNING_STATUS_STARTING);
            cloudHostData.put("realHostId", realHostId);
            if (HttpGatewayResponseHelper.isSuccess(startResullt)) {
                // 更新缓冲池值
                CloudHostData newCloudHostData = myCloudHostData.clone();
                newCloudHostData.setRunningStatus(AppConstant.CLOUD_HOST_RUNNING_STATUS_STARTING);
                newCloudHostData.setLastOper(AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTTING_DOWN);
                newCloudHostData
                    .setLastOperTime(StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
                CloudHostPoolManager.getCloudHostPool().put(newCloudHostData);

                cloudHostMapper.updateRunningStatusByRealHostId(cloudHostData);
                logger.info(
                    "CloudHostServiceImpl.startCloudHost() > [" + Thread.currentThread().getId()
                        + "] start host succeeded");
                logStatus = AppConstant.OPER_LOG_SUCCESS;
                MethodResult result = new MethodResult(MethodResult.SUCCESS, "启动成功");
                result.put("hostId", hostId);
                return result;
            } else {
                logger.warn("CloudHostServiceImpl.startCloudHost() > start host failed, message:["
                    + HttpGatewayResponseHelper.getMessage(startResullt) + "]");
                MethodResult result = new MethodResult(MethodResult.FAIL, "启动失败");
                result.put("hostId", hostId);
                return result;
            }
        } catch (ConnectException e) {
            logger.error(e);
            throw new AppException("启动失败");
        } catch (AppException e) {
            logger.error(e);
            throw new AppException("启动失败");
        } catch (Exception e) {
            logger.error(e);
            throw new AppException("启动失败");
        } finally {
            try {
                if (newHostId != null && newHostId.length() == 32) {
                    OperLogMapper operLogMapper = this.sqlSession.getMapper(OperLogMapper.class);
                    Map<String, Object> operLogData = new LinkedHashMap<String, Object>();
                    operLogData.put("id", StringUtil.generateUUID());
                    operLogData.put("userId", cloudHost.getUserId());
                    operLogData.put("content", "启动云主机:" + _hostName);
                    operLogData
                        .put("operTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
                    operLogData.put("status", logStatus);
                    operLogData.put("resourceName", "云主机:" + _hostName);
                    operLogData.put("operDuration", System.currentTimeMillis() - begin);
                    operLogMapper.addOperLog(operLogData);
                }
            } catch (Exception e) {
                logger.error(e);
            }

        }
    }

    /**
     *
     */
    @Callable @Transactional(readOnly = false)
    public MethodResult startCloudHostFromIsoImage(
        String hostId, String realIsoImageId) {
        try {
            if (StringUtil.isBlank(hostId)) {
                throw new AppException("hostId不能为空");
            }
            if (StringUtil.isBlank(realIsoImageId)) {
                throw new AppException("realIsoImageId不能为空");
            }

            CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
            CloudHostVO cloudHost = cloudHostMapper.getById(hostId);

            String realHostId = cloudHost.getRealHostId();
            if (realHostId == null) {
                return new MethodResult(MethodResult.FAIL, "云主机尚未创建成功");
            }

            HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(cloudHost.getRegion());

            JSONObject startResullt = channel.hostStart(realHostId, 1, realIsoImageId);
            if (HttpGatewayResponseHelper.isSuccess(startResullt) == false) {
                logger.warn(
                    "CloudHostServiceImpl.startCloudHostFromIsoImage() > [" + Thread.currentThread()
                        .getId() + "] start host failed, message:[" + HttpGatewayResponseHelper
                        .getMessage(startResullt) + "]");
                return new MethodResult(MethodResult.FAIL, "光盘启动失败");
            }
            Map<String, Object> cloudHostData = new LinkedHashMap<String, Object>();
            cloudHostData.put("runningStatus", AppConstant.CLOUD_HOST_RUNNING_STATUS_RUNNING);
            cloudHostData.put("realHostId", realHostId);
            cloudHostMapper.updateRunningStatusByRealHostId(cloudHostData);
            logger.info(
                "CloudHostServiceImpl.startCloudHostFromIsoImage() > [" + Thread.currentThread()
                    .getId() + "] restart host succeeded");
            return new MethodResult(MethodResult.SUCCESS, "光盘启动成功");
        } catch (ConnectException e) {
            // throw new AppException("连接http gateway失败", e);
            logger.error(e);
            throw new AppException("光盘启动失败");
        } catch (AppException e) {
            logger.error(e);
            throw new AppException("光盘启动失败");
        } catch (Exception e) {
            logger.error(e);
            throw new AppException("光盘启动失败");
        }
    }

    /**
     *
     */
    @Callable @Transactional(readOnly = false)
    public MethodResult haltCloudHost(String hostId) {
        String newHostId = hostId;
        LoginInfo loginInfo = null;
        Long begin = System.currentTimeMillis();
        Integer logStatus = AppConstant.OPER_LOG_FAIL;
        HttpServletRequest request = RequestContext.getHttpRequest();
        if (newHostId != null && newHostId.length() == 32) {
            loginInfo = LoginHelper.getLoginInfo(request);
        } else if (newHostId != null && newHostId.length() == 37) {
            hostId = hostId.substring(0, hostId.length() - 5);
        }
        String _hostName = "";
        try {
            CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
            CloudHostVO cloudHost = cloudHostMapper.getById(hostId);
            _hostName = cloudHost.getDisplayName();
            String realHostId = cloudHost.getRealHostId();
            if (realHostId == null) {
                return new MethodResult(MethodResult.FAIL, "云主机尚未创建成功");
            }
            CloudHostData myCloudHostData =
                CloudHostPoolManager.getCloudHostPool().getByRealHostId(cloudHost.getRealHostId());
            if (myCloudHostData != null && myCloudHostData.getRunningStatus() == 1) {
                Map<String, Object> hostData = new LinkedHashMap<String, Object>();
                hostData.put("runningStatus", AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTDOWN);
                hostData.put("realHostId", realHostId);
                cloudHostMapper.updateRunningStatusByRealHostId(hostData);
                logStatus = AppConstant.OPER_LOG_SUCCESS;
                return new MethodResult(MethodResult.SUCCESS, "关机成功");
            }
            HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(cloudHost.getRegion());
            JSONObject haltResullt = channel.hostHalt(realHostId);

            Map<String, Object> cloudHostData = new LinkedHashMap<String, Object>();
            //表明正在关机
            cloudHostData.put("runningStatus", AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTTING_DOWN);
            cloudHostData.put("realHostId", realHostId);
            if (HttpGatewayResponseHelper.isSuccess(haltResullt)) {

                CloudHostData newCloudHostData = myCloudHostData.clone();
                newCloudHostData.setRunningStatus(AppConstant.CLOUD_HOST_RUNNING_STATUS_STARTING);
                newCloudHostData.setLastOper(AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTTING_DOWN);
                newCloudHostData
                    .setLastOperTime(StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
                CloudHostPoolManager.getCloudHostPool().put(newCloudHostData);

                cloudHostMapper.updateRunningStatusByRealHostId(cloudHostData);
                logger.info(
                    "CloudHostServiceImpl.haltCloudHost() > [" + Thread.currentThread().getId()
                        + "] halt host succeeded");
                logStatus = AppConstant.OPER_LOG_SUCCESS;
                return new MethodResult(MethodResult.SUCCESS, "关机成功");
            } else {
                logger.warn(
                    "CloudHostServiceImpl.haltCloudHost() > [" + Thread.currentThread().getId()
                        + "] halt host failed, message:[" + HttpGatewayResponseHelper
                        .getMessage(haltResullt) + "]");
                return new MethodResult(MethodResult.FAIL, "关机失败");
            }
        } catch (ConnectException e) {
            // throw new AppException("连接http gateway失败", e);
            logger.error(e);
            throw new AppException("关机失败");
        } catch (AppException e) {
            logger.error(e);
            throw new AppException("关机失败");
        } catch (Exception e) {
            logger.error(e);
            throw new AppException("关机失败");
        } finally {
            try {
                if (newHostId != null && newHostId.length() == 32) {
                    OperLogMapper operLogMapper = this.sqlSession.getMapper(OperLogMapper.class);
                    Map<String, Object> operLogData = new LinkedHashMap<String, Object>();
                    operLogData.put("id", StringUtil.generateUUID());
                    operLogData.put("userId", loginInfo.getUserId());
                    operLogData.put("content", "强制关闭云主机:" + _hostName);
                    operLogData
                        .put("operTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
                    operLogData.put("status", logStatus);
                    operLogData.put("resourceName", "用户名:" + loginInfo.getAccount());
                    operLogData.put("operDuration", System.currentTimeMillis() - begin);
                    operLogMapper.addOperLog(operLogData);
                }
            } catch (Exception e) {
                logger.error(e);
            }

        }
    }

    /**
     *
     */
    @Callable @Transactional(readOnly = false)
    public MethodResult stopCloudHost(String hostId) {
        String newHostId = hostId;
        Long begin = System.currentTimeMillis();
        Integer logStatus = AppConstant.OPER_LOG_FAIL;
        HttpServletRequest request = RequestContext.getHttpRequest();
        LoginInfo loginInfo = null;
        if (newHostId != null && newHostId.length() == 32) {
            loginInfo = LoginHelper.getLoginInfo(request);
        } else if (newHostId != null && newHostId.length() == 37) {
            hostId = hostId.substring(0, hostId.length() - 5);
        }
        String _hostName = "";
        try {
            CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
            CloudHostVO cloudHost = cloudHostMapper.getById(hostId);
            _hostName = cloudHost.getDisplayName();
            String realHostId = cloudHost.getRealHostId();
            if (realHostId == null) {
                return new MethodResult(MethodResult.FAIL, "云主机尚未创建成功");
            }
            CloudHostData myCloudHostData =
                CloudHostPoolManager.getCloudHostPool().getByRealHostId(cloudHost.getRealHostId());
            if (myCloudHostData != null && myCloudHostData.getRunningStatus() == 1) {
                Map<String, Object> hostData = new LinkedHashMap<String, Object>();
                hostData.put("runningStatus", AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTDOWN);
                hostData.put("realHostId", realHostId);
                cloudHostMapper.updateRunningStatusByRealHostId(hostData);
                logStatus = AppConstant.OPER_LOG_SUCCESS;
                return new MethodResult(MethodResult.SUCCESS, "关机成功");
            }
            HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(cloudHost.getRegion());
            JSONObject haltResullt = channel.hostStop(realHostId);

            Map<String, Object> cloudHostData = new LinkedHashMap<String, Object>();
            cloudHostData.put("runningStatus", AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTTING_DOWN);
            cloudHostData.put("realHostId", realHostId);
            if (HttpGatewayResponseHelper.isSuccess(haltResullt)) {
                // 获取池里面已有的数据
                // 更新缓冲池值
                CloudHostData newCloudHostData = myCloudHostData.clone();
                newCloudHostData
                    .setRunningStatus(AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTTING_DOWN);
                newCloudHostData.setLastOper(AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTTING_DOWN);
                newCloudHostData
                    .setLastOperTime(StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
                CloudHostPoolManager.getCloudHostPool().put(newCloudHostData);
                //更新数据库值
                cloudHostMapper.updateRunningStatusByRealHostId(cloudHostData);
                logger.info(
                    "CloudHostServiceImpl.haltCloudHost() > [" + Thread.currentThread().getId()
                        + "] halt host succeeded");
                logStatus = AppConstant.OPER_LOG_SUCCESS;
                return new MethodResult(MethodResult.SUCCESS, "关机命令发送成功");
            } else {
                logger.warn(
                    "CloudHostServiceImpl.haltCloudHost() > [" + Thread.currentThread().getId()
                        + "] halt host failed, message:[" + HttpGatewayResponseHelper
                        .getMessage(haltResullt) + "]");
                return new MethodResult(MethodResult.FAIL, "关机失败");
            }
        } catch (ConnectException e) {
            // throw new AppException("连接http gateway失败", e);
            logger.error(e);
            throw new AppException("关机失败");
        } catch (AppException e) {
            // throw e;
            logger.error(e);
            throw new AppException("关机失败");
        } catch (Exception e) {
            logger.error(e);
            throw new AppException("关机失败");
        } finally {
            try {
                if (newHostId != null && newHostId.length() == 32) {
                    OperLogMapper operLogMapper = this.sqlSession.getMapper(OperLogMapper.class);
                    Map<String, Object> operLogData = new LinkedHashMap<String, Object>();
                    operLogData.put("id", StringUtil.generateUUID());
                    operLogData.put("userId", loginInfo.getUserId());
                    operLogData.put("content", "关闭云主机:" + _hostName);
                    operLogData
                        .put("operTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
                    operLogData.put("status", logStatus);
                    operLogData.put("resourceName", "用户名:" + loginInfo.getAccount());
                    operLogData.put("operDuration", System.currentTimeMillis() - begin);
                    operLogMapper.addOperLog(operLogData);
                }
            } catch (Exception e) {
                logger.error(e);
            }

        }
    }

    /**
     *
     */
    @Callable @Transactional(readOnly = false)
    public MethodResult restartCloudHost(String hostId) {
        String newHostId = hostId;
        Long begin = System.currentTimeMillis();
        Integer logStatus = AppConstant.OPER_LOG_FAIL;
        HttpServletRequest request = RequestContext.getHttpRequest();
        LoginInfo loginInfo = null;
        if (newHostId != null && newHostId.length() == 32) {
            loginInfo = LoginHelper.getLoginInfo(request);
        } else if (newHostId != null && newHostId.length() == 37) {
            hostId = hostId.substring(0, hostId.length() - 5);
        }
        String _hostName = "";
        try {
            CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
            CloudHostVO cloudHost = cloudHostMapper.getById(hostId);
            _hostName = cloudHost.getDisplayName();
            String realHostId = cloudHost.getRealHostId();
            if (realHostId == null) {
                MethodResult result = new MethodResult(MethodResult.FAIL, "云主机尚未创建成功");
                result.put("hostId", hostId);
                return result;
            }
            Map<String, Object> cloudHostData = new LinkedHashMap<String, Object>();
            CloudHostData myCloudHostData =
                CloudHostPoolManager.getCloudHostPool().getByRealHostId(cloudHost.getRealHostId());
            cloudHostData.put("realHostId", realHostId);
            if (myCloudHostData != null && myCloudHostData.getRunningStatus() == 1) {
                HttpGatewayChannelExt channel =
                    HttpGatewayManager.getChannel(cloudHost.getRegion());
                JSONObject startResullt = channel.hostStart(realHostId, 0, "");
                if (HttpGatewayResponseHelper.isSuccess(startResullt) == false) {
                    cloudHostData
                        .put("runningStatus", AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTDOWN);
                    cloudHostMapper.updateRunningStatusByRealHostId(cloudHostData);
                    logger.warn(
                        "CloudHostServiceImpl.restartCloudHost() > [" + Thread.currentThread()
                            .getId() + "] start host failed, message:[" + HttpGatewayResponseHelper
                            .getMessage(startResullt) + "]");
                    
                    MethodResult result = new MethodResult(MethodResult.FAIL, "重启失败");
                    result.put("hostId", hostId);
                    return result;
                }
                logStatus = AppConstant.OPER_LOG_SUCCESS;
                
                MethodResult result = new MethodResult(MethodResult.SUCCESS, "重启成功");
                result.put("hostId", hostId);
                return result;
            }
            HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(cloudHost.getRegion());

            JSONObject restarResullt = channel.hostRestart(realHostId, 0, "");

            if (HttpGatewayResponseHelper.isSuccess(restarResullt) == false) {
                logger.warn(
                    "CloudHostServiceImpl.restartCloudHost() > [" + Thread.currentThread().getId()
                        + "] halt host failed, message:[" + HttpGatewayResponseHelper
                        .getMessage(restarResullt) + "]");
                MethodResult result = new MethodResult(MethodResult.FAIL, "重启失败");
                result.put("hostId", hostId);
                return result;
            }

            cloudHostData
            .put("runningStatus", AppConstant.CLOUD_HOST_RUNNING_STATUS_RESTARTING);
            cloudHostMapper.updateRunningStatusByRealHostId(cloudHostData);
         // 更新缓冲池值
            CloudHostData newCloudHostData = myCloudHostData.clone();
            newCloudHostData.setRunningStatus(AppConstant.CLOUD_HOST_RUNNING_STATUS_RESTARTING);
            newCloudHostData.setLastOper(AppConstant.CLOUD_HOST_RUNNING_STATUS_RESTARTING);
            newCloudHostData.setLastOperTime(StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
            CloudHostPoolManager.getCloudHostPool().put(newCloudHostData);
            logger.info(
                "CloudHostServiceImpl.restartCloudHost() > [" + Thread.currentThread().getId()
                    + "] restart host succeeded");
            logStatus = AppConstant.OPER_LOG_SUCCESS;
            return new MethodResult(MethodResult.SUCCESS, "重启成功");
        } catch (ConnectException e) {
            // throw new AppException("连接http gateway失败", e);
            logger.error(e);
            throw new AppException("重启失败");
        } catch (AppException e) {
            logger.error(e);
            throw new AppException("重启失败");
        } catch (Exception e) {
            logger.error(e);
            throw new AppException("重启失败");
        } finally {
            try {
                if (newHostId != null && newHostId.length() == 32) {
                    OperLogMapper operLogMapper = this.sqlSession.getMapper(OperLogMapper.class);
                    Map<String, Object> operLogData = new LinkedHashMap<String, Object>();
                    operLogData.put("id", StringUtil.generateUUID());
                    operLogData.put("userId", loginInfo.getUserId());
                    operLogData.put("content", "重启云主机:" + _hostName);
                    operLogData
                        .put("operTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
                    operLogData.put("status", logStatus);
                    operLogData.put("resourceName", "用户名:" + loginInfo.getAccount());
                    operLogData.put("operDuration", System.currentTimeMillis() - begin);
                    operLogMapper.addOperLog(operLogData);
                }
            } catch (Exception e) {
                logger.error(e);
            }

        }
    }

    /**
     *
     */
    @Callable
    public MethodResult restartCloudHostFromIsoImage(String hostId,
        String realIsoImageId) {
        try {
            CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
            CloudHostVO cloudHost = cloudHostMapper.getById(hostId);

            String realHostId = cloudHost.getRealHostId();
            if (realHostId == null) {
                return new MethodResult(MethodResult.FAIL, "云主机尚未创建成功");
            }

            HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(cloudHost.getRegion());

            JSONObject haltResullt = channel.hostHalt(realHostId);
            if (HttpGatewayResponseHelper.isSuccess(haltResullt) == false) {
                logger.warn("CloudHostServiceImpl.restartCloudHostFromIsoImage() > [" + Thread
                    .currentThread().getId() + "] halt host failed, message:["
                    + HttpGatewayResponseHelper.getMessage(haltResullt) + "]");
                return new MethodResult(MethodResult.SUCCESS, "重启失败");
            }

            JSONObject restartResullt = channel.hostStart(realHostId, 1, realIsoImageId);
            if (HttpGatewayResponseHelper.isSuccess(restartResullt) == false) {
                logger.warn("CloudHostServiceImpl.restartCloudHostFromIsoImage() > [" + Thread
                    .currentThread().getId() + "] start host failed, message:["
                    + HttpGatewayResponseHelper.getMessage(restartResullt) + "]");
                return new MethodResult(MethodResult.FAIL, "重启失败");
            }

            logger.info(
                "CloudHostServiceImpl.restartCloudHostFromIsoImage() > [" + Thread.currentThread()
                    .getId() + "] restart host succeeded");
            return new MethodResult(MethodResult.SUCCESS, "重启成功");
        } catch (ConnectException e) {
            // throw new AppException("连接http gateway失败", e);
            logger.error(e);
            throw new AppException("重启失败");
        } catch (AppException e) {
            logger.error(e);
            throw new AppException("重启失败");
        } catch (Exception e) {
            logger.error(e);
            throw new AppException("重启失败");
        }
    }

    /**
     *
     */
    @Callable
    public MethodResult insertIsoImage(String hostId, String realIsoImageId) {
        try {
            if (StringUtil.isBlank(hostId)) {
                throw new AppException("hostId不能为空");
            }
            if (StringUtil.isBlank(realIsoImageId)) {
                throw new AppException("realIsoImageId不能为空");
            }

            CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
            CloudHostVO cloudHost = cloudHostMapper.getById(hostId);

            String realHostId = cloudHost.getRealHostId();
            if (realHostId == null) {
                return new MethodResult(MethodResult.FAIL, "云主机尚未创建成功");
            }

            HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(cloudHost.getRegion());

            // 先弹出iso镜像
            JSONObject ejectMediaResult = channel.hostEjectMedia(realHostId);
            if (HttpGatewayResponseHelper.isSuccess(ejectMediaResult)) {
                // 如果之前有加载光盘的话，需要sleep一下，要不然会有bug
                Thread.sleep(1000);
            } else {
                logger.warn(
                    "CloudHostServiceImpl.insertIsoImage() > [" + Thread.currentThread().getId()
                        + "] eject iso image failed, message:[" + HttpGatewayResponseHelper
                        .getMessage(ejectMediaResult) + "]");
            }

            // 再加载iso镜像
            JSONObject insertMediaResult = channel.hostInsertMedia(realHostId, realIsoImageId);
            if (HttpGatewayResponseHelper.isSuccess(insertMediaResult) == false) {
                logger.warn(
                    "CloudHostServiceImpl.insertIsoImage() > [" + Thread.currentThread().getId()
                        + "] insert iso image failed, message:[" + HttpGatewayResponseHelper
                        .getMessage(insertMediaResult) + "]");
                return new MethodResult(MethodResult.FAIL, "加载光盘镜像失败");
            }

            logger.info("CloudHostServiceImpl.insertIsoImage() > [" + Thread.currentThread().getId()
                + "] insert iso image succeeded");
            return new MethodResult(MethodResult.SUCCESS, "加载光盘镜像成功");
        } catch (ConnectException e) {
            // throw new AppException("连接http gateway失败", e);
            logger.error(e);
            throw new AppException("加载光盘镜像失败");
        } catch (AppException e) {
            logger.error(e);
            throw new AppException("加载光盘镜像失败");
        } catch (Exception e) {
            logger.error(e);
            throw new AppException("加载光盘镜像失败");
        }
    }

    /**
     *
     */
    @Callable
    public MethodResult popupIsoImage(String hostId) {
        try {
            if (StringUtil.isBlank(hostId)) {
                throw new AppException("hostId不能为空");
            }

            CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
            CloudHostVO cloudHost = cloudHostMapper.getById(hostId);

            String realHostId = cloudHost.getRealHostId();
            if (realHostId == null) {
                return new MethodResult(MethodResult.FAIL, "云主机尚未创建成功");
            }

            HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(cloudHost.getRegion());

            JSONObject ejectMediaResullt = channel.hostEjectMedia(realHostId);
            if (HttpGatewayResponseHelper.isSuccess(ejectMediaResullt) == false) {
                logger.warn(
                    "CloudHostServiceImpl.popupIsoImage() > [" + Thread.currentThread().getId()
                        + "] host eject media failed, message:[" + HttpGatewayResponseHelper
                        .getMessage(ejectMediaResullt) + "]");
                return new MethodResult(MethodResult.FAIL, "弹出光盘失败");
            }

            logger.info("CloudHostServiceImpl.popupIsoImage() > [" + Thread.currentThread().getId()
                + "] host eject media succeeded");
            return new MethodResult(MethodResult.SUCCESS, "弹出光盘成功");
        } catch (ConnectException e) {
            // throw new AppException("连接http gateway失败", e);
            logger.error(e);
            throw new AppException("弹出光盘失败");
        } catch (AppException e) {
            logger.error(e);
            throw new AppException("弹出光盘失败");
        } catch (Exception e) {
            logger.error(e);
            throw new AppException("弹出光盘失败");
        }
    }

    @Callable
    @Transactional(readOnly = false)
    public MethodResult updatePassword(Map<String, Object> parameter) {
        Long begin = System.currentTimeMillis();
        Integer logStatus = AppConstant.OPER_LOG_FAIL;
        HttpServletRequest request = RequestContext.getHttpRequest();
        LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
        logger.debug("CloudHostServiceImpl.updatePassword()");
        try {
            String password = (String) parameter.get("password");
            String hostId = (String) parameter.get("host_id");
            if (StringUtil.isBlank(hostId)) {
                throw new AppException("hostId不能为空");
            }
            if (StringUtil.isBlank(password)) {
                throw new AppException("监控密码不能为空");
            }

            CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);

            CloudHostVO cloudHost = cloudHostMapper.getById(hostId);

            String realHostId = cloudHost.getRealHostId();
            if (realHostId == null) {
                return new MethodResult(MethodResult.FAIL, "云主机尚未创建成功");
            }

            Map<String, Object> newCloudHostData = new LinkedHashMap<String, Object>();
            newCloudHostData.put("password", password);
            newCloudHostData.put("id", hostId);
            cloudHostMapper.updatePasswordById(newCloudHostData);

            HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(cloudHost.getRegion());
            JSONObject hostModifyResutl = channel
                .hostModify(cloudHost.getRealHostId(), "", 0, BigInteger.ZERO, new Integer[0],
                    new Integer[0], "", password, "", BigInteger.ZERO, BigInteger.ZERO);

            if (HttpGatewayResponseHelper.isSuccess(hostModifyResutl) == false) {
                logger.warn(
                    "CloudHostServiceImpl.updatePassword() > [" + Thread.currentThread().getId()
                        + "] modify host failed, message:[" + HttpGatewayResponseHelper
                        .getMessage(hostModifyResutl) + "], region:[" + cloudHost.getRegion()
                        + "]");
                throw new AppException("修改监控密码失败");
            }

            logger.info("CloudHostServiceImpl.updatePassword() > [" + Thread.currentThread().getId()
                + "] modify host succedded, message:[" + HttpGatewayResponseHelper
                .getMessage(hostModifyResutl) + "], region:[" + cloudHost.getRegion() + "]");
            logStatus = AppConstant.OPER_LOG_SUCCESS;
            return new MethodResult(MethodResult.SUCCESS, "修改成功");
        } catch (AppException e) {
            logger.error(e);
            throw new AppException("修改失败");
        } catch (Exception e) {
            logger.error(e);
            throw new AppException("修改失败");
        } finally {
            try {
                OperLogMapper operLogMapper = this.sqlSession.getMapper(OperLogMapper.class);
                Map<String, Object> operLogData = new LinkedHashMap<String, Object>();
                operLogData.put("id", StringUtil.generateUUID());
                operLogData.put("userId", loginInfo.getUserId());
                operLogData.put("content", "修改监控密码");
                operLogData
                    .put("operTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
                operLogData.put("status", logStatus);
                operLogData.put("resourceName", "用户名:" + loginInfo.getAccount());
                operLogData.put("operDuration", System.currentTimeMillis() - begin);
                operLogMapper.addOperLog(operLogData);
            } catch (Exception e) {
                logger.error(e);
            }

        }
    }

    /**
     * 获取创建云主机的信息
     */
    @Callable
    @Transactional(readOnly = false)
    public MethodResult getCloudHostCreationResult(
        String hostId) {
        try {
            CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
            CloudHostShoppingConfigMapper cloudHostShoppingConfigMapper =
                this.sqlSession.getMapper(CloudHostShoppingConfigMapper.class);
            Map<String, Object> choudHostShoppingConfigData = new LinkedHashMap<String, Object>();
            Map<String, Object> cloudHostData = new LinkedHashMap<String, Object>();
            CloudHostVO cloudHost = cloudHostMapper.getById(hostId);
            if (cloudHost == null) {
                //throw new AppException("找不到云主机");
                throw new AppException("cloud host not found, host_id:[" + hostId + "]");
            }
            if (cloudHost.getRealHostId() != null && cloudHost.getRealHostId().length() > 0) {

                MethodResult result = new MethodResult(MethodResult.SUCCESS, "");
                result.put("progress", 100);
                result.put("creation_status", true);
                return result;
            }
            if (cloudHost.getProcessStatus() != null && cloudHost.getProcessStatus() != 3) {
                if (cloudHost.getProcessStatus() == 2) {
                    MethodResult result = new MethodResult(MethodResult.SUCCESS, "");
                    result.put("progress", 0);
                    result.put("creation_status", false);
                    return result;
                } else if (cloudHost.getProcessStatus() == 1) {
                    MethodResult result = new MethodResult(MethodResult.SUCCESS, "");
                    result.put("progress", 100);
                    result.put("creation_status", true);
                    return result;
                } else {

                    return new MethodResult(MethodResult.FAIL, "尚未开始创建云主机");
                }
            }
            Object progress = AppInconstant.cloudHostProgress.get(cloudHost.getHostName());
            MethodResult result = new MethodResult(MethodResult.SUCCESS, "");
            if (progress == null) {

                HttpGatewayChannelExt channel =
                    HttpGatewayManager.getChannel(cloudHost.getRegion());
                JSONObject creationResult = channel.hostCreationResult(cloudHost.getHostName());
                if (HttpGatewayResponseHelper.isSuccess(creationResult) == false) {
                    logger.warn("CloudHostServiceImpl.getCloudHostCreationProgress() > [" + Thread
                        .currentThread().getId() + "] get host creation result failed, message:["
                        + HttpGatewayResponseHelper.getMessage(creationResult) + "]");
                    throw new AppException(
                        "get host creation result failed, message:[" + HttpGatewayResponseHelper
                            .getMessage(creationResult) + "]");
                }

                result.put("progress", JSONLibUtil.getInteger(creationResult, "progress", 0));
                result.put("creation_status",
                    JSONLibUtil.getBoolean(creationResult, "creation_status"));
                if (cloudHost.getRealHostId() == null || cloudHost.getRealHostId().length() <= 0) {
                    result.put("creation_status", null);
                }
                // result.put("creation_result",
                // JSONLibUtil.getJSONObject(creationResult, "creation_result"))
                // ;
                JSONObject hostInfo = JSONLibUtil.getJSONObject(creationResult, "creation_result");

            } else {
                result.put("progress", (int) Double.parseDouble(progress.toString()));
                result.put("creation_status", null);

            }

            return result;
        } catch (ConnectException e) {
            throw new AppException("连接http gateway失败", e);
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException(e);
        }
    }

    /**
     * 获取创建云主机需要的信息（name，余额，还有）
     */
    @Callable
    @Transactional(readOnly = false)
    public MethodResult getCreateInfo(String userId,
        String region) {
        try {
            CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
            List<CloudHostVO> cloudHostList = cloudHostMapper.getByUserId(userId);

            BigDecimal totalPrice = new BigDecimal("0");
            if (cloudHostList != null && cloudHostList.size() > 0) {
                for (CloudHostVO vo : cloudHostList) {
                    if (!(vo.getSummarizedStatusText().equals("创建失败") || vo
                        .getSummarizedStatusText().equals("停机")) && vo.getMonthlyPrice() != null) {
                        totalPrice = vo.getMonthlyPrice().add(totalPrice);
                    }
                }
            }

            MethodResult result = new MethodResult(MethodResult.SUCCESS, "");
            result.put("totalPrice", totalPrice);
            TerminalUserMapper terminalUserMapper =
                this.sqlSession.getMapper(TerminalUserMapper.class);
            TerminalUserVO user = terminalUserMapper.getBalanceById(userId);
            CloudHostBillDetailMapper cloudHostBillDetailMapper =
                this.sqlSession.getMapper(CloudHostBillDetailMapper.class);
            String now = StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS");
            Map<String, Object> cloudHostBillDetailData = new LinkedHashMap<String, Object>();
            cloudHostBillDetailData.put("userId", userId);
            cloudHostBillDetailData.put("beforeTime", now);
            List<CloudHostBillDetailVO> billList =
                cloudHostBillDetailMapper.getAllUndoneByUserId(cloudHostBillDetailData);
            BigDecimal totalConsumption = new BigDecimal("0");
            for (CloudHostBillDetailVO vo : billList) {
                Date d1 = StringUtil.stringToDate(now, "yyyyMMddHHmmssSSS");
                Date d2 = StringUtil.stringToDate(vo.getStartTime(), "yyyyMMddHHmmssSSS");
                long diff = d1.getTime() - d2.getTime();
                long seconds = diff / (1000 * 60);
                String daysecond = (30 * 24 * 60) + "";
                BigDecimal nowPrice = vo.getMonthlyPrice()
                    .divide(new BigDecimal(daysecond), 3, BigDecimal.ROUND_HALF_UP);
                nowPrice = nowPrice.multiply(new BigDecimal(seconds + ""))
                    .setScale(2, BigDecimal.ROUND_HALF_UP);
                totalConsumption = totalConsumption.add(nowPrice);

            }

            BigDecimal balance = user.getAccountBalance().subtract(totalConsumption);
            if (balance.compareTo(BigDecimal.ZERO) < 0) {
                balance = BigDecimal.ZERO;
            }
            result.put("balance", balance);

            result.put("name", getNewCloudHostNameByUserId(userId, region));
            return result;
        } catch (AppException e) {
            logger.error(e);
            throw new AppException("失败");
        } catch (Exception e) {
            logger.error(e);
            throw new AppException("失败");
        }
    }

    /**
     * 代理商为用户创建主机获取主机名
     */
    @Callable
    @Transactional(readOnly = false)
    public MethodResult getHostNameForAgentCreate(
        String userId, String region) {
        try {
            HttpServletRequest request = RequestContext.getHttpRequest();
            LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
            TerminalUserMapper terminalUserMapper =
                this.sqlSession.getMapper(TerminalUserMapper.class);
            CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
            AgentMapper agentMapper = this.sqlSession.getMapper(AgentMapper.class);

            AgentVO agent = agentMapper.getAgentById(loginInfo.getUserId());
            List<TerminalUserVO> terminalUserList =
                terminalUserMapper.getTerminalUserFromAgent(loginInfo.getUserId());

            BigDecimal totalPrice = BigDecimal.ZERO;
            if (terminalUserList != null && terminalUserList.size() > 0) {
                for (TerminalUserVO terminalUser : terminalUserList) {
                    List<CloudHostVO> cloudHostList =
                        cloudHostMapper.getByUserId(terminalUser.getId());
                    if (cloudHostList != null && cloudHostList.size() > 0) {
                        for (CloudHostVO cloudHost : cloudHostList) {
                            if (cloudHost.getType() == 2) {
                                if (!(cloudHost.getSummarizedStatusText().equals("创建失败")
                                    || cloudHost.getSummarizedStatusText().equals("停机"))
                                    && cloudHost.getMonthlyPrice() != null) {
                                    totalPrice = cloudHost.getMonthlyPrice().add(totalPrice);
                                }
                            }
                        }
                    }
                }
            }
            if (agent.getPercentOff() != null) {
                totalPrice =
                    totalPrice.multiply(new BigDecimal(100).subtract(agent.getPercentOff()))
                        .divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
            }
            //计算出每天的费用
            totalPrice = totalPrice.divide(new BigDecimal("31"), 0, BigDecimal.ROUND_HALF_UP);

            MethodResult result = new MethodResult(MethodResult.SUCCESS, "");
            result.put("name", getNewCloudHostNameByUserId(userId, region));
            result.put("totalPrice", totalPrice);
            return result;
        } catch (AppException e) {
            logger.error(e);
            throw new AppException("失败");
        }
    }

    /**
     * 申请停机
     */
    @Callable
    @Transactional(readOnly = false)
    public MethodResult inactivateCloudHost(
        String hostId) {
        Long begin = System.currentTimeMillis();
        Integer logStatus = AppConstant.OPER_LOG_FAIL;
        HttpServletRequest request = RequestContext.getHttpRequest();
        LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
        String cloudHostName = "";
        try {
            if (StringUtil.isBlank(hostId)) {
                throw new AppException("hostId不能为空");
            }

            CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);

            CloudHostVO cloudHostVO = cloudHostMapper.getById(hostId);
            if (cloudHostVO == null) {
                throw new AppException("cloud host not found with id[" + hostId + "]");
            }
            if (StringUtil.isBlank(cloudHostVO.getRealHostId())) {
                return new MethodResult(MethodResult.FAIL, "云主机还没创建成功，无法申请停机");
            }
            if (AppConstant.CLOUD_HOST_STATUS_2_HALT.equals(cloudHostVO.getStatus())) {
                return new MethodResult(MethodResult.FAIL, "该云主机当前已经是‘停机’状态");
            }
            if (AppConstant.CLOUD_HOST_STATUS_4_HALT_FOREVER.equals(cloudHostVO.getStatus())) {
                return new MethodResult(MethodResult.FAIL, "云主机已经永久停机，无法申请停机恢复");
            }
            cloudHostName = cloudHostVO.getDisplayName();
            Date now = new Date();

            // 发消息到http gateway进行关机
            HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(cloudHostVO.getRegion());
            JSONObject hostHaltResult = channel.hostStop(cloudHostVO.getRealHostId());
            if (HttpGatewayResponseHelper.isSuccess(hostHaltResult) == false) {
                logger.warn(
                    "CloudHostServiceImpl.inactivateCloudHost() > [" + Thread.currentThread()
                        .getId() + "] halt cloud host failed, message:[" + HttpGatewayResponseHelper
                        .getMessage(hostHaltResult) + "]");
            } else {
                logger.info(
                    "CloudHostServiceImpl.inactivateCloudHost() > [" + Thread.currentThread()
                        .getId() + "] halt cloud host succeeded, message:["
                        + HttpGatewayResponseHelper.getMessage(hostHaltResult) + "]");
            }
            // 结算该云主机所有未完成的账单
            new CloudHostBillingHelper(sqlSession)
                .settleAllUndoneCloudHostBills(hostId, now, false);

            // 修改云主机的状态
            Map<String, Object> cloudHostData = new LinkedHashMap<String, Object>();
            cloudHostData.put("status", AppConstant.CLOUD_HOST_STATUS_2_HALT);
            cloudHostData.put("inactivateTime", DateUtil.dateToString(now, "yyyyMMddHHmmssSSS"));
            cloudHostData.put("id", hostId);
            int n = cloudHostMapper.updateStatusById(cloudHostData);
            if (n <= 0) {
                return new MethodResult(MethodResult.FAIL, "申请停机失败");
            }
            logStatus = AppConstant.OPER_LOG_SUCCESS;
            return new MethodResult(MethodResult.SUCCESS, "停机成功");
        } catch (ConnectException e) {
            // throw new AppException("连接http gateway失败", e);
            logger.error(e);
            throw new AppException("停机失败");
        } catch (AppException e) {
            logger.error(e);
            throw new AppException("停机失败");
        } catch (Exception e) {
            logger.error(e);
            throw new AppException("停机失败");
        } finally {
            try {
                OperLogMapper operLogMapper = this.sqlSession.getMapper(OperLogMapper.class);
                Map<String, Object> operLogData = new LinkedHashMap<String, Object>();
                operLogData.put("id", StringUtil.generateUUID());
                operLogData.put("userId", loginInfo.getUserId());
                operLogData.put("content", "云主机" + cloudHostName + "已停用");
                operLogData
                    .put("operTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
                operLogData.put("status", logStatus);
                operLogData.put("resourceName", "云主机名:" + cloudHostName);
                operLogData.put("operDuration", System.currentTimeMillis() - begin);
                operLogMapper.addOperLog(operLogData);
            } catch (Exception e) {
                logger.error(e);
            }

        }
    }

    /**
     * 申请停机恢复
     */
    @Callable
    @Transactional(readOnly = false)
    public MethodResult reactivateCloudHost(
        String hostId) {
        Long begin = System.currentTimeMillis();
        Integer logStatus = AppConstant.OPER_LOG_FAIL;
        HttpServletRequest request = RequestContext.getHttpRequest();
        String userId = "";
        String cloudHostName = "";
        try {
            if (StringUtil.isBlank(hostId)) {
                throw new AppException("hostId不能为空");
            }

            CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
            TerminalUserMapper terminalUserMapper =
                this.sqlSession.getMapper(TerminalUserMapper.class);

            // 获取云主机信息
            CloudHostVO cloudHostVO = cloudHostMapper.getById(hostId);
            if (cloudHostVO == null) {
                throw new AppException("cloud host not found with id[" + hostId + "]");
            }

            // 判断云主机的状态
            if (StringUtil.isBlank(cloudHostVO.getRealHostId())) {
                return new MethodResult(MethodResult.FAIL, "云主机还没创建成功，无法申请停机恢复");
            }
            if (AppConstant.CLOUD_HOST_STATUS_1_NORNAL.equals(cloudHostVO.getStatus())) {
                return new MethodResult(MethodResult.FAIL, "该云主机当前已经是‘正常’状态，无需申请停机恢复");
            }
            if (AppConstant.CLOUD_HOST_STATUS_4_HALT_FOREVER.equals(cloudHostVO.getStatus())) {
                return new MethodResult(MethodResult.FAIL, "云主机已经永久停机，无法申请停机恢复");
            }
            cloudHostName = cloudHostVO.getDisplayName();
            Date now = new Date();

            // 获取用户的信息
            if (cloudHostVO.getType() == AppConstant.CLOUD_HOST_TYPE_2_AGENT) {
                // 计算余额能否支持一天，不足不创建主机
                //              LoginInfo loginInfo = LoginHelper.getLoginInfo(request, AppConstant.SYS_USER_TYPE_AGENT);
                AgentMapper agentMapper = this.sqlSession.getMapper(AgentMapper.class);
                AgentVO agentVO = agentMapper.getAgentByUserId(cloudHostVO.getUserId());
                if (agentVO != null) {

                    List<CloudHostVO> cloudHostList = cloudHostMapper.getByAgentId(agentVO.getId());
                    BigDecimal totalPrice = new BigDecimal("0");
                    if (cloudHostList != null && cloudHostList.size() > 0) {
                        for (CloudHostVO vo : cloudHostList) {
                            if (vo.getType() == 2) {
                                if (!(vo.getSummarizedStatusText().equals("创建失败") || vo
                                    .getSummarizedStatusText().equals("停机"))
                                    && vo.getMonthlyPrice() != null) {
                                    totalPrice = vo.getMonthlyPrice().add(totalPrice);
                                }
                            }
                        }

                    }
                    userId = agentVO.getId();
                    // 计算出每天的费用
                    totalPrice = totalPrice.add(cloudHostVO.getMonthlyPrice());
                    totalPrice =
                        totalPrice.divide(new BigDecimal("31"), 0, BigDecimal.ROUND_HALF_UP);
                    BigDecimal balance =
                        agentVO.getAccountBalance().setScale(0, BigDecimal.ROUND_HALF_UP);
                    if (balance.compareTo(totalPrice) < 0) {
                        if (totalPrice.subtract(balance).compareTo(BigDecimal.TEN) < 0) {
                            return new MethodResult(MethodResult.FAIL, "余额不足，至少充值10元");
                        } else {
                            return new MethodResult(MethodResult.FAIL,
                                "余额不足，至少充值" + totalPrice.subtract(balance) + "元");
                        }

                    }

                }
            } else {
                //              LoginInfo loginInfo = LoginHelper.getLoginInfo(request, AppConstant.SYS_USER_TYPE_TERMINAL_USER);

                TerminalUserVO terminalUserVO =
                    terminalUserMapper.getBaseInfoById(cloudHostVO.getUserId());
                if (terminalUserVO == null) {
                    logger.warn(
                        "CloudHostServiceImpl.reactivateCloudHost() > [" + Thread.currentThread()
                            .getId() + "] user  not found, id:[" + userId + "]");
                    return new MethodResult(MethodResult.FAIL, "找不到用户信息");
                }
                userId = cloudHostVO.getUserId();

                // 判断余额是否足够

                List<CloudHostVO> cloudHostList = cloudHostMapper.getByUserId(userId);
                BigDecimal totalPrice = new BigDecimal("0");
                if (cloudHostList != null && cloudHostList.size() > 0) {
                    for (CloudHostVO vo : cloudHostList) {
                        if (!(vo.getSummarizedStatusText().equals("创建失败") || vo
                            .getSummarizedStatusText().equals("停机"))
                            && vo.getMonthlyPrice() != null) {
                            totalPrice = vo.getMonthlyPrice().add(totalPrice);
                        }
                    }

                }
                // 计算出每天的费用
                totalPrice = totalPrice.add(cloudHostVO.getMonthlyPrice());
                totalPrice = totalPrice.divide(new BigDecimal("31"), 0, BigDecimal.ROUND_HALF_UP);
                if (terminalUserVO.getAccountBalance().compareTo(totalPrice) < 0) {
                    BigDecimal p = totalPrice.subtract(terminalUserVO.getAccountBalance());
                    if (p.compareTo(BigDecimal.TEN) < 0) {
                        p = BigDecimal.TEN;
                    }
                    return new MethodResult(MethodResult.FAIL,
                        "余额不足，至少充值<a href='page.do?userType=4&bean=accountBalanceService&method=toRechargePage' target='blank'>"
                            + p
                            + "</a>元<br/>多充多优惠，<a href='page.do?userType=4&bean=accountBalanceService&method=toRechargePage' target='blank'>点击进入充值</a>");

                }
                logger.info(
                    "CloudHostServiceImpl.reactivateCloudHost() > host_id " + cloudHostVO.getId()
                        + " reactivate, balance " + terminalUserVO.getAccountBalance()
                        + "  monthlyPrice " + cloudHostVO.getMonthlyPrice());
            }

            // 添加一条云主机的计费信息
            new CloudHostBillingHelper(sqlSession).startOneNewCloudHostBillDetail(cloudHostVO, now);

            // 修改云主机的状态
            Map<String, Object> cloudHostData = new LinkedHashMap<String, Object>();
            cloudHostData.put("status", AppConstant.CLOUD_HOST_STATUS_1_NORNAL);
            cloudHostData.put("reactivateTime", StringUtil.dateToString(now, "yyyyMMddHHmmssSSS"));
            cloudHostData.put("id", hostId);
            int n = cloudHostMapper.updateStatusById(cloudHostData);
            if (n <= 0) {
                throw new AppException("申请停机恢复失败");
            } else {
                logStatus = AppConstant.OPER_LOG_SUCCESS;
                return new MethodResult(MethodResult.SUCCESS, "停机恢复成功");
            }
        } catch (AppException e) {
            logger.error(e);
            throw new AppException("停机恢复失败");
        } catch (Exception e) {
            logger.error(e);
            throw new AppException("停机恢复失败");
        } finally {
            try {
                if (userId != "") {
                    OperLogMapper operLogMapper = this.sqlSession.getMapper(OperLogMapper.class);
                    Map<String, Object> operLogData = new LinkedHashMap<String, Object>();
                    operLogData.put("id", StringUtil.generateUUID());
                    operLogData.put("userId", userId);
                    operLogData.put("content", "云主机" + cloudHostName + "已从停用中恢复");
                    operLogData
                        .put("operTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
                    operLogData.put("status", logStatus);
                    operLogData.put("resourceName", "云主机名:" + cloudHostName);
                    operLogData.put("operDuration", System.currentTimeMillis() - begin);
                    operLogMapper.addOperLog(operLogData);
                }
            } catch (Exception e) {
                logger.error(e);
            }

        }
    }

    @CallWithoutLogin
    @Callable
    public CloudHostData refreshData(String cloudHostId) {
        CloudHostData cloudHostData = this.getMonitorData(cloudHostId);
        logger.info("CloudHostServiceImpl.refreshData()-> get " + cloudHostId + " data");
        return cloudHostData;
    }

    @Callable
    public String modifyAllocationPage(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("CloudHostServiceImpl.modifyAllocationPage()");
        LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
        // 检测运营商权限
        // if(loginInfo.getUserType()!=4&&loginInfo.hasPrivilege(PrivilegeConstant.operator_self_use_cloud_host_add_port_page)
        // == false )
        // {
        // return "/public/have_not_access_dialog.jsp";
        // }

        String hostId = request.getParameter("hostId");
        AgentMapper agentMapper = this.sqlSession.getMapper(AgentMapper.class);
        CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
        CpuPackageOptionMapper cpuPackageOptionMapper =
            this.sqlSession.getMapper(CpuPackageOptionMapper.class);
        MemoryPackageOptionMapper memoryPackageOptionMapper =
            this.sqlSession.getMapper(MemoryPackageOptionMapper.class);
        PackagePriceMapper packagePriceMapper = this.sqlSession.getMapper(PackagePriceMapper.class);
        CloudHostVO cloudHost = cloudHostMapper.getById(hostId);
        if (cloudHost == null || "创建失败".equals(cloudHost.getSummarizedStatusText()) || "创建中"
            .equals(cloudHost.getSummarizedStatusText())) {
            return "/security/agent/page_not_exists.jsp";
        }
        Integer region = cloudHost.getRegion();
        Map<String, Object> cpuCondition = new LinkedHashMap<String, Object>();
        cpuCondition.put("type", "1");
        cpuCondition.put("region", region);
        cpuCondition.put("start_row", 0);
        cpuCondition.put("row_count", 100);
        List<PackagePriceVO> cpu = packagePriceMapper.getPackagePrice(cpuCondition);
        List<Integer> cpuList = new ArrayList<Integer>();
        if (cpu != null && cpu.size() > 0) {
            for (PackagePriceVO packagePrice : cpu) {
                cpuList.add(packagePrice.getCpuCore());
            }
        }

        Map<String, Object> memoryCondition = new LinkedHashMap<String, Object>();
        memoryCondition.put("type", "2");
        memoryCondition.put("region", region);
        memoryCondition.put("start_row", 0);
        memoryCondition.put("row_count", 100);
        List<PackagePriceVO> memory = packagePriceMapper.getPackagePrice(memoryCondition);
        List<BigInteger> memoryList = new ArrayList<BigInteger>();
        if (memory != null && memory.size() > 0) {
            for (PackagePriceVO packagePrice : memory) {
                memoryList
                    .add((CapacityUtil.toGBValue(packagePrice.getMemory(), 0)).toBigInteger());
            }
        }

        Map<String, Object> bandwidthCondition = new LinkedHashMap<String, Object>();
        bandwidthCondition.put("type", "4");
        bandwidthCondition.put("region", region);
        bandwidthCondition.put("start_row", 0);
        bandwidthCondition.put("row_count", 100);
        List<PackagePriceVO> bandwidth = packagePriceMapper.getPackagePrice(bandwidthCondition);
        List<BigInteger> bandwidthList = new ArrayList<BigInteger>();
        if (bandwidth != null && bandwidth.size() > 0) {
            for (PackagePriceVO packagePrice : bandwidth) {
                bandwidthList
                    .add((FlowUtil.toMbpsValue(packagePrice.getBandwidth(), 0)).toBigInteger());
            }
        }

        request.setAttribute("cpuList", cpuList);
        request.setAttribute("memoryList", memoryList);
        request.setAttribute("bandwidthList", bandwidthList);
        // 获取CPU套餐项
        List<CpuPackageOptionVO> cpuOptions = cpuPackageOptionMapper.getAll();
        request.setAttribute("cpuOptions", cpuOptions);
        // 获取内存套餐项
        List<MemoryPackageOptionVO> memoryOptions = memoryPackageOptionMapper.getAll();
        request.setAttribute("memoryOptions", memoryOptions);
        request.setAttribute("cloudHost", cloudHost);
        if (loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_TERMINAL_USER) {
            this.operatorHostBeforeBackup(cloudHost.getRealHostId(), cloudHost.getRegion());
            return "/security/user/edit_server.jsp";
        } else if (loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_AGENT) {
            AgentVO agent = agentMapper.getAgentById(loginInfo.getUserId());
            request.setAttribute("agent", agent);
            return "/security/agent/modify_allocation.jsp";
        } else {
            return "/security/operator/modify_allocation.jsp";
        }
    }

    @Callable
    @Transactional(readOnly = false)
    public MethodResult modifyAllocation(Map<String, Object> parameter) {
        Long begin = System.currentTimeMillis();
        Integer logStatus = AppConstant.OPER_LOG_FAIL;
        HttpServletRequest request = RequestContext.getHttpRequest();
        LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
        String cloudHostName = null;
        String content = "";
        logger.debug("CloudHostServiceImpl.modifyAllocation()");
        try {
            // HttpServletRequest request = RequestContext.getHttpRequest();
            // LoginInfo loginInfo = LoginHelper.getLoginInfo(request);

            // 参数处理
            cloudHostName = StringUtil.trim(parameter.get("cloudHostName"));
            String region = StringUtil.trim(parameter.get("region"));
            String realCloudHostId = StringUtil.trim(parameter.get("realCloudHostId"));
            String cloudHostId = StringUtil.trim(parameter.get("cloudHostId"));
            String cpuCore = StringUtil.trim(parameter.get("cpu_core"));
            BigInteger memory =
                CapacityUtil.fromCapacityLabel(StringUtil.trim(parameter.get("memory") + "GB"));
            String bandWidth_str = StringUtil.trim(parameter.get("bandwidth"));
            if (bandWidth_str == null || bandWidth_str.equals("")) {

                bandWidth_str = StringUtil.trim(parameter.get("bandwidthDIY"));
            }
            String sysDisk = StringUtil.trim(parameter.get("sysDisk"));
            String dataDisk = StringUtil.trim(parameter.get("dataDisk"));
            if (StringUtil.isBlank(realCloudHostId)) {
                return new MethodResult(MethodResult.FAIL, "真实云主机ID不能为空");
            }
            if (StringUtil.isBlank(cloudHostId)) {
                return new MethodResult(MethodResult.FAIL, "云主机ID不能为空");
            }
            if (StringUtil.isBlank(cpuCore)) {
                return new MethodResult(MethodResult.FAIL, "请选择CPU核心数");
            }
            if (memory.equals("0")) {
                return new MethodResult(MethodResult.FAIL, "请选择内存");
            }
            if (StringUtil.isBlank(bandWidth_str)) {
                return new MethodResult(MethodResult.FAIL, "带宽不能为空");
            }
            BigInteger bandWidth = FlowUtil.fromFlowLabel(bandWidth_str + "Mbps");

            if (StringUtil.isBlank(sysDisk)) {
                return new MethodResult(MethodResult.FAIL, "系统磁盘不能为空");
            }
            if (StringUtil.isBlank(dataDisk)) {
                return new MethodResult(MethodResult.FAIL, "数据磁盘不能为空");
            }
            AgentMapper agentMapper = this.sqlSession.getMapper(AgentMapper.class);
            BigDecimal price = CloudHostPrice
                .getMonthlyPrice(Integer.parseInt(region), 3, Integer.parseInt(cpuCore), memory,
                    BigInteger.valueOf(Long.parseLong(StringUtil.trim(dataDisk))), bandWidth)
                .setScale(2, BigDecimal.ROUND_HALF_UP);

            TerminalUserMapper terminalUserMapper =
                this.sqlSession.getMapper(TerminalUserMapper.class);
            CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
            PriceUpdateMapper priceUpdateMapper =
                this.sqlSession.getMapper(PriceUpdateMapper.class);
            CloudHostVO cloudHost = cloudHostMapper.getById(cloudHostId);
            content = "修改主机" + cloudHost.getDisplayName() + "配置为CPU" + cpuCore + "核，内存" + StringUtil
                .trim(parameter.get("memory")) + "G，带宽" + bandWidth_str + "M";
            if (loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_AGENT) {
                AgentVO agentVO = agentMapper.getAgentById(loginInfo.getUserId());
                BigDecimal percentOff = agentVO.getPercentOff();
                price = price.multiply(new BigDecimal(100).subtract(percentOff))
                    .divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
                // 计算余额能否支持一天，不足不创建主机
                if (agentVO != null) {
                    List<CloudHostVO> cloudHostList =
                        cloudHostMapper.getByAgentId(loginInfo.getUserId());
                    BigDecimal totalPrice = new BigDecimal("0");
                    if (cloudHostList != null && cloudHostList.size() > 0) {
                        for (CloudHostVO vo : cloudHostList) {
                            if (vo.getType() == 2) {
                                if (!(vo.getSummarizedStatusText().equals("创建失败") || vo
                                    .getSummarizedStatusText().equals("停机"))
                                    && vo.getMonthlyPrice() != null && (!vo.getId()
                                    .equals(cloudHostId))) {
                                    totalPrice = vo.getMonthlyPrice().add(totalPrice);
                                }
                            }
                        }

                    }
                    // 计算出每天的费用
                    totalPrice = totalPrice.add(price);
                    totalPrice =
                        totalPrice.divide(new BigDecimal("31"), 0, BigDecimal.ROUND_HALF_UP);
                    BigDecimal balance =
                        agentVO.getAccountBalance().setScale(0, BigDecimal.ROUND_HALF_UP);
                    if (balance.compareTo(totalPrice) < 0) {
                        if (totalPrice.subtract(balance).compareTo(BigDecimal.TEN) < 0) {
                            return new MethodResult(MethodResult.FAIL, "余额不足，至少充值10元 ");
                        } else {
                            return new MethodResult(MethodResult.FAIL,
                                "余额不足，至少充值" + totalPrice.subtract(balance) + "元 ");
                        }

                    }


                }
            } else {
                // 计算余额能否支持一天，不足不创建主机
                TerminalUserVO terminalUserVO =
                    terminalUserMapper.getBalanceById(loginInfo.getUserId());
                if (terminalUserVO != null) {
                    List<CloudHostVO> cloudHostList =
                        cloudHostMapper.getByUserId(loginInfo.getUserId());
                    BigDecimal totalPrice = new BigDecimal("0");
                    if (cloudHostList != null && cloudHostList.size() > 0) {
                        for (CloudHostVO vo : cloudHostList) {
                            if (vo.getType() == 3) {
                                if (!(vo.getSummarizedStatusText().equals("创建失败") || vo
                                    .getSummarizedStatusText().equals("停机"))
                                    && vo.getMonthlyPrice() != null && (!vo.getId()
                                    .equals(cloudHostId))) {
                                    totalPrice = vo.getMonthlyPrice().add(totalPrice);
                                }
                            }
                        }
                    }
                    // 计算出每天的费用
                    totalPrice = totalPrice.add(price);
                    totalPrice =
                        totalPrice.divide(new BigDecimal("31"), 0, BigDecimal.ROUND_HALF_UP);
                    BigDecimal balance =
                        terminalUserVO.getAccountBalance().setScale(0, BigDecimal.ROUND_HALF_UP);
                    if (balance.compareTo(totalPrice) < 0) {
                        if (totalPrice.subtract(balance).compareTo(BigDecimal.TEN) < 0) {
                            return new MethodResult(MethodResult.FAIL, "余额不足，至少充值10元 ");
                        } else {
                            return new MethodResult(MethodResult.FAIL,
                                "余额不足，至少充值" + totalPrice.subtract(balance) + "元 ");
                        }
                    }
                }
            }
            HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(Integer.parseInt(region));
            JSONObject hostModifyResult = channel
                .hostModify(realCloudHostId, "", Integer.parseInt(cpuCore), memory,
                    new Integer[] {0, 1, 1}, new Integer[0], "", "", "", bandWidth, bandWidth);
            if (HttpGatewayResponseHelper.isSuccess(hostModifyResult) == false) {
                return new MethodResult(MethodResult.FAIL, "配置修改失败");
            } else {
                // 结算，结算之后产生新的账单
                Date now = new Date();
                new CloudHostBillingHelper(sqlSession)
                    .settleAllUndoneCloudHostBills(cloudHostId, now, true);
                Map<String, Object> newCloudHostData = new LinkedHashMap<String, Object>();
                newCloudHostData.put("id", cloudHostId);
                newCloudHostData.put("cpuCore", Integer.valueOf(cpuCore));
                newCloudHostData.put("memory", memory);
                newCloudHostData.put("bandwidth", bandWidth);
                newCloudHostData.put("price", price);
                int n = cloudHostMapper.updateClientCloudHostById(newCloudHostData);
                if (n > 0) {
                    logStatus = AppConstant.OPER_LOG_SUCCESS;
                    Map<String, Object> priceUpdateCondition = new LinkedHashMap<String, Object>();
                    priceUpdateCondition.put("id", StringUtil.generateUUID());
                    priceUpdateCondition.put("itemId", cloudHostId);
                    priceUpdateCondition.put("item", 1);
                    priceUpdateCondition.put("price", price);
                    priceUpdateCondition.put("beforePrice", cloudHost.getMonthlyPrice());
                    priceUpdateCondition.put("updateTime",
                        StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
                    priceUpdateMapper.addPriceUpdate(priceUpdateCondition);
                    cloudHostMapper.updatePackageIdById(cloudHostId);
                    return new MethodResult(MethodResult.SUCCESS, "配置修改成功");
                } else {
                    return new MethodResult(MethodResult.FAIL, "数据库配置修改失败");
                }
            }
        } catch (AppException e) {
            logger.error(e);
            throw new AppException("配置修改失败");
        } catch (Exception e) {
            logger.error(e);
            throw new AppException("配置修改失败");
        } finally {
            try {
                OperLogMapper operLogMapper = this.sqlSession.getMapper(OperLogMapper.class);
                Map<String, Object> operLogData = new LinkedHashMap<String, Object>();
                operLogData.put("id", StringUtil.generateUUID());
                operLogData.put("userId", loginInfo.getUserId());
                operLogData.put("content", content);
                operLogData
                    .put("operTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
                operLogData.put("status", logStatus);
                operLogData.put("resourceName", "云主机名:" + cloudHostName);
                operLogData.put("operDuration", System.currentTimeMillis() - begin);
                operLogMapper.addOperLog(operLogData);
            } catch (Exception e) {
                logger.error(e);
            }

        }
    }

    @Override
    public void getTotalPrice(HttpServletRequest request, HttpServletResponse response) {
        LoginInfo user = LoginHelper.getLoginInfo(request, AppConstant.SYS_USER_TYPE_TERMINAL_USER);
        CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
        List<CloudHostVO> cloudHostList = cloudHostMapper.getByUserId(user.getUserId());

        if (cloudHostList != null && cloudHostList.size() > 0) {
            BigDecimal totalPrice = new BigDecimal("");
            for (CloudHostVO vo : cloudHostList) {
                totalPrice = vo.getMonthlyPrice().add(totalPrice);
            }
            request.setAttribute("toalPrice", totalPrice);
        }

    }

    public String getNewCloudHostNameByUserId(String userId, String region) {
        SysUserMapper sysUserMapper = this.sqlSession.getMapper(SysUserMapper.class);
        CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);

        // 获取用户信息
        SysUserVO sysUser = sysUserMapper.getById(userId);

        // 获取这个用户名下的所有云主机的主机名
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("region", region);
        data.put("userId", userId);
        List<CloudHostVO> userCloudHostList = cloudHostMapper.getCloudHostForRegion(data);
        // List<CloudHostVO> userCloudHostList =
        // cloudHostMapper.getByUserId(userId);
        Set<String> hostNames = new HashSet<String>();
        Set<String> displayNames = new HashSet<>();
        for (CloudHostVO cloudHost : userCloudHostList) {
            if (cloudHost.getHostName() != null) {
                hostNames.add(StringUtil.trim(cloudHost.getHostName()));
                displayNames.add(StringUtil.trim(cloudHost.getDisplayName()));
            }
        }

        // 获取新的云主机名
        int len = hostNames.size() + 1; // 在size + 1个中，总有一个合适的名字
        String region_str = "";
        if ("1".equals(region)) {
            region_str = "GZ";
        } else if ("2".equals(region)) {
            region_str = "CD";

        } else if ("4".equals(region)) {
            region_str = "HK";

        }
        for (int i = 1; i <= len; i++) {
            String hostName =
                "T" + sysUser.getType() + "_" + region_str + "_" + sysUser.getAccount() + "_" + i;
            if (hostNames.contains(hostName) == false) {
                if (displayNames.contains(hostName) == false) {
                    return hostName;
                }
            }
        }

        throw new AppException("generate new cloud host name failed.");
    }

    @Callable
    @Transactional(readOnly = false)
    public MethodResult updateHostNameById(String hostId, String hostName) {
        Long begin = System.currentTimeMillis();
        Integer logStatus = AppConstant.OPER_LOG_FAIL;
        HttpServletRequest request = RequestContext.getHttpRequest();
        LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
        String content = "";
        logger.debug("CloudHostServiceImpl.updateHostNameById()");
        try {
            CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
            CloudHostVO host = cloudHostMapper.getById(hostId);
            content = "修改主机" + host.getDisplayName() + "为" + hostName;
            Map<String, Object> newCloudHostData = new LinkedHashMap<String, Object>();
            newCloudHostData.put("id", hostId);
            newCloudHostData.put("hostName", hostName);
            int n = cloudHostMapper.updateDisplayNameById(newCloudHostData);
            if (n > 0) {
                logStatus = AppConstant.OPER_LOG_SUCCESS;
                return new MethodResult(MethodResult.SUCCESS, "主机名修改成功");
            } else {
                return new MethodResult(MethodResult.FAIL, "主机名修改失败");
            }
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException(e);
        } finally {
            try {
                OperLogMapper operLogMapper = this.sqlSession.getMapper(OperLogMapper.class);
                Map<String, Object> operLogData = new LinkedHashMap<String, Object>();
                operLogData.put("id", StringUtil.generateUUID());
                operLogData.put("userId", loginInfo.getUserId());
                operLogData.put("content", content);
                operLogData
                    .put("operTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
                operLogData.put("status", logStatus);
                operLogData.put("resourceName", "云主机名:" + hostName);
                operLogData.put("operDuration", System.currentTimeMillis() - begin);
                operLogMapper.addOperLog(operLogData);
            } catch (Exception e) {
                logger.error(e);
            }

        }
    }

    @Callable
    public MethodResult getCloudHostStatus(String hostId) {
        try {
            CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
            if (hostId == null || hostId.isEmpty()) {
                return new MethodResult(MethodResult.FAIL, "云主机ID不能为空");
            }
            CloudHostVO cloudHost = cloudHostMapper.getById(hostId);
            if (cloudHost.getRunningStatus() == AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTDOWN) {
                return new MethodResult(MethodResult.SUCCESS, "关机成功");
            } else {
                return new MethodResult(MethodResult.FAIL, "关机失败");
            }
        } catch (Exception e) {
            throw new AppException(e);
        }
    }
    
    /**
     * 查询主机重启状态
    * <p>Title: getCloudHostRestartStatus</p> 
    * <p>Description: </p> 
    * @param hostId
    * @return 
    * @see com.zhicloud.op.service.CloudHostService#getCloudHostRestartStatus(java.lang.String)
     */
    @Callable
    public MethodResult getCloudHostRestartStatus(String hostId) {
        try {
            CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
            if (hostId == null || hostId.isEmpty()) {
                return new MethodResult(MethodResult.FAIL, "云主机ID不能为空");
            }
            CloudHostVO cloudHost = cloudHostMapper.getById(hostId); 
            MethodResult result =  new MethodResult(MethodResult.SUCCESS, "查询成功"); 
            result.put("runningstatus", cloudHost.getRunningStatus());
            return result;
        } catch (Exception e) {
            throw new AppException(e);
        }
    }

    @Callable
    public MethodResult getCloudHostStartStatus(String hostId) {
        try {
            CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
            if (hostId == null || hostId.isEmpty()) {
                return new MethodResult(MethodResult.FAIL, "云主机ID不能为空");
            }
            CloudHostVO cloudHost = cloudHostMapper.getById(hostId);
            if (cloudHost.getRunningStatus() == AppConstant.CLOUD_HOST_RUNNING_STATUS_RUNNING) {
                return new MethodResult(MethodResult.SUCCESS, "开机成功");
            } else {
                return new MethodResult(MethodResult.FAIL, "开机失败");
            }
        } catch (Exception e) {
            throw new AppException(e);
        }
    }

    @Transactional(readOnly = false)
    public void checkStopHost() {
        try {
            CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
            List<CloudHostVO> cloudHostList = cloudHostMapper.getAllCloudHost();
            for (CloudHostVO cloudHost : cloudHostList) {
                if ("停机".equals(cloudHost.getSummarizedStatusText())) {
                    CloudHostData myCloudHostData = CloudHostPoolManager.getCloudHostPool()
                        .getByRealHostId(cloudHost.getRealHostId());
                    if (myCloudHostData.getRunningStatus() == 2) {
                        HttpGatewayChannelExt channel =
                            HttpGatewayManager.getChannel(cloudHost.getRegion());
                        JSONObject haltResullt = channel.hostHalt(cloudHost.getRealHostId());
                        Map<String, Object> cloudHostData = new LinkedHashMap<String, Object>();
                        cloudHostData
                            .put("runningStatus", AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTDOWN);
                        cloudHostData.put("realHostId", cloudHost.getRealHostId());
                        if (HttpGatewayResponseHelper.isSuccess(haltResullt)) {
                            cloudHostMapper.updateRunningStatusByRealHostId(cloudHostData);
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new AppException(e);
        }
    }

    /**
     * 启动云主机资源监控 - 已弃用
     *
     * @param regionId
     * @param realHostId
     * @return
     * @throws MalformedURLException
     * @throws IOException
     */
    @Deprecated
    public boolean startMonitor(int regionId, String realHostId)
        throws MalformedURLException, IOException {
        // 向发送http_gateway发送资源监控请求
        // HttpGatewayAsyncChannel channel = null;
        // JSONObject result = null;
        // try {
        // channel = HttpGatewayManager.getAsyncChannel(regionId);
        // result = channel.hostStartMonitor(new String[] { realHostId });
        //
        // if (HttpGatewayResponseHelper.isSuccess(result) == false) {
        // channel.release();
        // return false;
        // }
        //
        // int task = result.getInt("task");
        // CloudHostData cloudHostData =
        // CloudHostPoolManager.getCloudHostPool().getByRealHostId(realHostId);
        // CloudHostMonitorPlusData host = null;
        // if (cloudHostData == null) {
        // // 维护channel
        // channel.release();
        // return false;
        // }
        // if (cloudHostData instanceof CloudHostMonitorPlusData) {
        // host = (CloudHostMonitorPlusData) cloudHostData;
        // } else {
        // host = new CloudHostMonitorPlusData();
        // host.copy(cloudHostData);
        // }
        //
        // // 维护cloud host pool内host信息
        // host.setTask(task);
        // host.setSessionId(channel.getSessionId());
        // host.updateFetchTime();
        //
        // CloudHostPoolManager.getCloudHostPool().put(host);
        //
        // } catch (IOException e) {
        // // 维护channel
        // channel.release();
        // throw e;
        // }

        return true;
    }

    /**
     * 停止云主机资源监控 - 已弃用
     */
    @Deprecated
    public boolean stopMonitor(String realHostId)
        throws MalformedURLException, IOException {
        // CloudHostData cloudHostData =
        // CloudHostPoolManager.getCloudHostPool().getByRealHostId(realHostId);
        // CloudHostMonitorPlusData host = null;
        // if (cloudHostData != null && cloudHostData instanceof
        // CloudHostMonitorPlusData) {
        // host = (CloudHostMonitorPlusData) cloudHostData;
        // } else {
        // return false;
        // }
        //
        // if (host.getSessionId() == null) {
        // return false;
        // }
        //
        // HttpGatewayAsyncChannel channel =
        // HttpGatewayManager.getActiveAsyncChannel(host.getSessionId());
        // if (channel == null) {
        // return false;
        // }
        //
        // try {
        // JSONObject result = channel.hostStopMonitor(host.getTask());
        // if (HttpGatewayResponseHelper.isSuccess(result) == false) {
        // return false;
        // }
        // } finally {
        // // 维护cloud host pool内host信息
        // host.setTask(0);
        // host.setSessionId(null);
        // CloudHostPoolManager.getCloudHostPool().put(host);
        // // 维护channel
        // channel.release();
        // }

        return true;
    }

    /**
     * 获得云主机资源监控数据
     *
     * @param realHostId
     * @return
     */
    public CloudHostData getMonitorData(String realHostId) {
        CloudHostData cloudHostData =
            CloudHostPoolManager.getCloudHostPool().getByRealHostId(realHostId);
        return cloudHostData;
    }

    @Callable
    public MethodResult sendPort(String port) {
        try {
            Socket socket = new Socket("127.0.0.1", 5950);
            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os);
            pw.write(port);
            pw.flush();
            socket.shutdownOutput();
            pw.close();
            os.close();
            socket.close();
            return new MethodResult(MethodResult.SUCCESS, "端口发送成功");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new MethodResult(MethodResult.FAIL, "端口发送失败");
    }

    @Callable
    public MethodResult startMonitoring(String region, String realHostId) {
        try {
            if (realHostId == null || realHostId.isEmpty()) {
                return new MethodResult(MethodResult.FAIL, "云主机ID不能为空");
            }
            if (region == null || region.isEmpty()) {
                return new MethodResult(MethodResult.FAIL, "云主机ID不能为空");
            }
            boolean flag = startMonitor(Integer.parseInt(region), realHostId);
            if (flag) {
                // System.out.println("-----success-----");
                return new MethodResult(MethodResult.SUCCESS, "开启成功");
            } else {
                // System.out.println("-----fail-----");
                return new MethodResult(MethodResult.FAIL, "开启失败");
            }
        } catch (Exception e) {
            throw new AppException(e);
        }
    }

    @Callable
    public MethodResult stopMyMonitor(String realHostId) {
        try {
            if (realHostId == null || realHostId.isEmpty()) {
                return new MethodResult(MethodResult.FAIL, "云主机ID不能为空");
            }
            boolean flag = stopMonitor(realHostId);
            if (flag) {
                // System.out.println("-----stop success-----");
                return new MethodResult(MethodResult.SUCCESS, "资源监控停止成功");
            } else {
                // System.out.println("-----stop fail-----");
                return new MethodResult(MethodResult.FAIL, "资源监控停止失败");
            }
        } catch (Exception e) {
            throw new AppException(e);
        }
    }

    @Callable
    public void getAllHostByVpcId(HttpServletRequest request, HttpServletResponse response) {
        try {
            String vpcId = request.getParameter("vpcId");
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json; charset=utf-8");
            CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
            List<CloudHostVO> cloudHostList = cloudHostMapper.getCloudHostInVpc(vpcId);
            List<Map<String, String>> hostList = new ArrayList<Map<String, String>>();
            if (cloudHostList != null && cloudHostList.size() > 0) {
                for (CloudHostVO cloudHost : cloudHostList) {
                    if (StringUtil.isBlank(cloudHost.getRealHostId())) {
                        continue;
                    }
                    Map<String, String> curMap = new HashMap<String, String>();
                    curMap.put("id", cloudHost.getId());
                    curMap.put("text", cloudHost.getDisplayName());
                    hostList.add(curMap);
                }
            }
            response.getWriter().write(JSONLibUtil.toJSONString(hostList));
        } catch (Exception e) {

        }

    }

    /**
     * 查询主机开通端口
     *
     * @see com.zhicloud.op.service.CloudHostService#queryCloudHostPorts(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Callable
    public void queryCloudHostPorts(HttpServletRequest request,
        HttpServletResponse response) {
        try {

            String hostId = request.getParameter("hostId");
            CloudHostOpenPortMapper cloudHostOpenPortMapper =
                this.sqlSession.getMapper(CloudHostOpenPortMapper.class);

            List<CloudHostOpenPortVO> ports = cloudHostOpenPortMapper.getByHostId(hostId);
            List<CloudHostOpenPortVO> portsNew = new ArrayList<CloudHostOpenPortVO>();
            for (CloudHostOpenPortVO port : ports) {
                port.setKeyValue(port.getProtocol() + "&" + port.getPort());
                portsNew.add(port);
            }
            // 输出json数据
            ServiceHelper
                .writeDatagridResultAsJsonTo(response.getOutputStream(), portsNew.size(), portsNew);
        } catch (IOException e) {

            // TODO Auto-generated catch block
            e.printStackTrace();

        }

    }

    /**
     * TODO 简单描述该方法的实现功能（可选）.
     *
     * @see com.zhicloud.op.service.CloudHostService#modifyDescriptionPage(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Callable
    public String modifyDescriptionPage(HttpServletRequest request,
        HttpServletResponse response) {
        String hostId = request.getParameter("hostId");
        CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
        CloudHostVO host = cloudHostMapper.getById(hostId);
        request.setAttribute("host", host);
        return "/security/operator/cloud_host_description.jsp";
    }

    /**
     * TODO 简单描述该方法的实现功能（可选）.
     *
     * @see com.zhicloud.op.service.CloudHostService#modifyDescription(java.util.Map)
     */
    @Callable
    @Transactional(readOnly = false)
    public MethodResult modifyDescription(
        Map<String, Object> parameter) {
        Long begin = System.currentTimeMillis();
        Integer logStatus = AppConstant.OPER_LOG_FAIL;
        HttpServletRequest request = RequestContext.getHttpRequest();
        LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
        String _hostName = "";
        logger.debug("CloudHostServiceImpl.modifyDescription()");
        try {
            // 参数处理
            String id = StringUtil.trim(parameter.get("id"));
            String description = StringUtil.trim(parameter.get("description"));

            CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);



            CloudHostVO cloudHost = cloudHostMapper.getById(id);
            _hostName = cloudHost.getDisplayName();

            Map<String, Object> data = new LinkedHashMap<String, Object>();
            data.put("id", id);
            data.put("description", description);
            cloudHostMapper.updateDescription(data);

            return new MethodResult(MethodResult.SUCCESS, "备注成功");

        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException(e);
        } finally {
            try {
                OperLogMapper operLogMapper = this.sqlSession.getMapper(OperLogMapper.class);
                Map<String, Object> operLogData = new LinkedHashMap<String, Object>();
                operLogData.put("id", StringUtil.generateUUID());
                operLogData.put("userId", loginInfo.getUserId());
                operLogData.put("content", "为主机" + _hostName + "添加备注");
                operLogData
                    .put("operTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
                operLogData.put("status", logStatus);
                operLogData.put("resourceName", "主机名:" + _hostName);
                operLogData.put("operDuration", System.currentTimeMillis() - begin);
                operLogMapper.addOperLog(operLogData);
            } catch (Exception e) {
                logger.error(e);
            }


        }
    }

    private void operatorHostBeforeBackup(String realHostId, Integer region) {
        // 从http gateway获取云主机的详细信息
        HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(region);
        try {

            JSONObject hostResult = channel.hostQueryInfo(realHostId);
            JSONObject hostInfo = (JSONObject) hostResult.get("host");
            Integer[] option = JSONLibUtil.getIntegerArray(hostInfo, "option");
            if (option.length > 3) {
                if (option[2] == 0) {
                    channel.hostModify(realHostId, "", 0, BigInteger.ZERO, new Integer[] {0, 1, 1},
                        new Integer[0], "", "", "", BigInteger.ZERO, BigInteger.ZERO);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Callable
    public List<CloudHostVO> queryCloudHostForExport(HttpServletRequest request,
        HttpServletResponse response) {
        logger.debug("CloudHostServiceImpl.queryCloudHostForExport()");
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json; charset=utf-8");
            // 获取参数
            String queryMark = StringUtil.trim(request.getParameter("query_by_mark"));
            String queryStatus = StringUtil.trim(request.getParameter("query_by_status"));
            String region = StringUtil.trim(request.getParameter("region"));
            String hostName = StringUtil.trim(request.getParameter("host_name"));
            String username = StringUtil.trim(request.getParameter("username"));
            String belongaccount = StringUtil.trim(request.getParameter("belongaccount"));
            if ("all".equals(queryStatus)) {
                queryStatus = null;
            }
            if ("all".equals(queryMark)) {
                queryMark = null;
            }
            if (StringUtil.isBlank(region)) {
                region = null;
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_MONTH, -7);
            String dayTime = DateUtil.dateToString(calendar.getTime(), "yyyyMMddHHmmssSSS");
            // 查询数据库
            CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
            Map<String, Object> condition = new LinkedHashMap<String, Object>();
            condition.put("hostName", "%" + hostName + "%");
            condition.put("start_row", 0);
            condition.put("markId", queryMark);
            condition.put("queryStatus", queryStatus);
            condition.put("region", region);
            condition.put("time", dayTime);
            condition.put("username", "%" + username + "%");
            condition.put("belongaccount", "%" + belongaccount + "%");
            int total = cloudHostMapper.queryPageCount(condition);
            condition.put("row_count", total);
            List<CloudHostVO> cloudHostList = cloudHostMapper.queryPage(condition);// 分页结果
            return cloudHostList;

        } catch (Exception e) {
            logger.error(e);
            throw new AppException("失败");
        }
    }


    @Callable
    public String backupHostPage(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("CloudHostServiceImpl.backupHostPage()");

        String uuid = StringUtil.trim(request.getParameter("uuid"));
        int region = Integer.parseInt(request.getParameter("region"));
        Long lastUpdateTime = null;
        String sessionId = null;
        int backupFlag = 0;
        int resumeFlag = 0;


        if ("backup_true".equals(AppInconstant.hostBackupProgress.get(uuid + "_backup"))) {
            backupFlag = 1;
        }

        if ("resume_true".equals(AppInconstant.hostBackupProgress.get(uuid + "_resume"))) {
            resumeFlag = 1;
        }

        //缓存超时清空
        @SuppressWarnings("unchecked") Map<String, Object> cacheData =
            (Map<String, Object>) AppInconstant.hostBackupProgress.get(uuid);

        if (cacheData != null) {
            if ((System.currentTimeMillis() - (Long) cacheData.get("last_update_time"))
                > 1 * 60 * 1000) {// 超过1分钟没有更新，则认为该数据不再使用.
                AppInconstant.hostBackupProgress.remove(uuid);
            }

            //从缓存中取出数据
            lastUpdateTime = (Long) cacheData.get("last_update_time");

        }

        try {
            if (lastUpdateTime == null) {

                Map<String, Object> params = new HashMap<String, Object>();
                params.put("uuid", uuid);
                params.put("region", region);
                params.put("command", "query");
                sessionId = this.hostBackup(params);
                if (sessionId == null) {
                    logger.warn(
                        "CloudHostServiceImpl.queryHostBackup() > [" + Thread.currentThread()
                            .getId() + "] query host backup failed");
                    throw new AppException("查询命令发送失败");
                }
                //存入缓存
                Map<String, Object> temporaryData = new LinkedHashMap<String, Object>();
                temporaryData.put("uuid", uuid);
                temporaryData.put("session_id", sessionId);
                temporaryData.put("last_update_time", new Date().getTime());
                AppInconstant.hostBackupProgress.put(uuid, temporaryData);
            }
        } catch (Exception e) {

            e.printStackTrace();
        }

        request.setAttribute("uuid", uuid);
        request.setAttribute("region", region);
        request.setAttribute("backup_flag", backupFlag);
        request.setAttribute("resume_flag", resumeFlag);

        return "/security/user/cloud_host_backup.jsp";
    }


    @Callable
    @Transactional(readOnly = false)
    public MethodResult backupHost(Map<String, Object> parameter) {

        String sessionId = null;
        String uuid = StringUtil.trim(parameter.get("uuid"));
        Integer mode = Integer.valueOf(StringUtil.trim(parameter.get("mode")));
        Integer disk = Integer.valueOf(StringUtil.trim(parameter.get("disk")));


        if ("backup_true".equals(AppInconstant.hostBackupProgress.get(uuid + "_backup"))) {
            throw new AppException("正在备份中 请完成后再进行此操作");
        }

        if ("resume_true".equals(AppInconstant.hostBackupProgress.get(uuid + "_resume"))) {
            throw new AppException("正在恢复中 请完成后再进行此操作");
        }

        if ("reset_true".equals(AppInconstant.hostResetProgress.get(uuid + "_reset"))) {
            throw new AppException("正在安装操作系统中 请完成后再进行此操作");
        }

        parameter.put("command", "backup");

        try {
            sessionId = this.hostBackup(parameter);
            if (sessionId == null) {
                logger.warn("CloudHostServiceImpl.backupHost() > [" + Thread.currentThread().getId()
                    + "] query host backup failed");
                throw new AppException("备份命令发送失败");
            }

            //存入缓存
            Map<String, Object> temporaryData = new LinkedHashMap<String, Object>();
            temporaryData.put("uuid", uuid);
            temporaryData.put("session_id", sessionId);
            AppInconstant.hostBackupProgress.put(uuid + "_backup_progress", temporaryData);

            //存入备份纪录
            BackupRecordMapper backupRecordMapper =
                this.sqlSession.getMapper(BackupRecordMapper.class);
            Map<String, Object> data = new LinkedHashMap<String, Object>();
            data.put("id", uuid);
            data.put("mode", mode);
            data.put("disk", disk);
            data.put("create_time", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
            backupRecordMapper.updateBackupRecord(data);

            return new MethodResult(MethodResult.SUCCESS, "备份命令发送成功");
        } catch (Exception e) {
            System.err.println("fail to send backup host request.");
            e.printStackTrace();
            return new MethodResult(MethodResult.FAIL, "备份命令发送失败");
        }
    }

    @Callable
    public MethodResult getBackupHostResult(String uuid) {
        try {
            @SuppressWarnings("unchecked") Map<String, Object> temporaryData =
                (Map<String, Object>) AppInconstant.hostBackupProgress
                    .get(uuid + "_backup_progress");
            if (temporaryData == null) {
                throw new AppException("无法获取备份信息");
            }

            HostBackupProgressData data =
                this.getProgressData(temporaryData.get("uuid").toString());
            if (data == null) {
                MethodResult message = new MethodResult(MethodResult.FAIL, "备份还未开始");
                return message;
            }

            if (!data.isReady()) {
                MethodResult message = new MethodResult(MethodResult.FAIL, "备份还未开始");
                return message;
            }

            MethodResult result = new MethodResult(MethodResult.SUCCESS, "备份成功");
            if (data.isFinished()) {
                if (data.isSuccess()) {
                    result.setProperty("progress", "100");
                    System.err.println("backup_status is true");
                    result.setProperty("backup_status", "true");
                } else {
                    result.setProperty("backup_status", "false");
                }
            } else {
                result.setProperty("backup_status", "pending");
                result.setProperty("progress", String.valueOf(data.getProgress()));
            }
            return result;

        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException(e);
        }
    }

    @Callable
    public MethodResult resumeHostBackup(Map<String, Object> parameter) {

        String sessionId = null;
        String uuid = (String) parameter.get("uuid");
        if ("backup_true".equals(AppInconstant.hostBackupProgress.get(uuid + "_backup"))) {
            throw new AppException("正在备份中 请完成后再进行此操作");
        }

        if ("resume_true".equals(AppInconstant.hostBackupProgress.get(uuid + "_resume"))) {
            throw new AppException("正在恢复中 请完成后再进行此操作");
        }

        if ("reset_true".equals(AppInconstant.hostResetProgress.get(uuid + "_reset"))) {
            throw new AppException("正在安装操作系统中 请完成后再进行此操作");
        }

        parameter.put("command", "resume");

        try {
            sessionId = this.hostBackup(parameter);
            if (sessionId == null) {
                logger.warn(
                    "CloudHostServiceImpl.resumeHostBackup() > [" + Thread.currentThread().getId()
                        + "] query host backup failed");
                throw new AppException("恢复命令发送失败");
            }

            //存入缓存
            Map<String, Object> temporaryData = new LinkedHashMap<String, Object>();
            temporaryData.put("uuid", uuid);
            temporaryData.put("session_id", sessionId);
            AppInconstant.hostBackupProgress.put(uuid + "_resume_progress", temporaryData);
            return new MethodResult(MethodResult.SUCCESS, "备份命令发送成功");
        } catch (Exception e) {
            System.err.println("fail to send backup host request.");
            e.printStackTrace();
            return new MethodResult(MethodResult.FAIL, "备份命令发送失败");
        }
    }

    @Callable
    public MethodResult getResumeHostResult(String uuid) {
        try {
            @SuppressWarnings("unchecked") Map<String, Object> temporaryData =
                (Map<String, Object>) AppInconstant.hostBackupProgress
                    .get(uuid + "_resume_progress");
            if (temporaryData == null) {
                throw new AppException("无法获取恢复信息");
            }

            HostBackupProgressData data =
                this.getProgressData(temporaryData.get("uuid").toString());
            if (data == null) {
                MethodResult message = new MethodResult(MethodResult.FAIL, "恢复还未开始");
                return message;
            }

            MethodResult result = new MethodResult(MethodResult.SUCCESS, "恢复成功");
            if (data.isReady()) {
                if (data.isFinished()) {
                    if (data.isSuccess()) {
                        result.put("progress", 100);
                        result.put("resume_status", true);
                    } else {
                        result.put("resume_status", false);
                    }
                } else {
                    result.put("progress", data.getProgress());
                }
                return result;
            }
            if (data.isFinished()) {
                if (!data.isSuccess()) {
                    result.put("resume_status", false);
                    return result;
                }
            }

            MethodResult message = new MethodResult(MethodResult.FAIL, "恢复还未开始");
            return message;
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException(e);
        }
    }

    public HostBackupProgressData getProgressData(String uuid) {
        HostBackupProgressPool pool = HostBackupProgressPoolManager.singleton().getPool();
        return pool.getDuplication(uuid);
    }

    @Callable
    public MethodResult queryHostBackup(Map<String, Object> parameter) {
        logger.debug("CloudHostServiceImpl.queryHostBackup()");

        String uuid = (String) parameter.get("uuid");

        HostBackupProgressPool pool = HostBackupProgressPoolManager.singleton().getPool();
        HostBackupProgressData hostBackup = null;

        int flag = 0;
        MethodResult result = null;

        while (true) {
            hostBackup = pool.get(uuid);
            if (hostBackup != null)
                break;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            flag++;
            if (flag > 5) {
                throw new AppException("服务器繁忙，请稍后再试");
            }
        }
        if (hostBackup.isAvaliable()) {
            result = new MethodResult(MethodResult.SUCCESS, "有可用备份");
            result.setProperty("mode", StringUtil.trim(hostBackup.getMode()));
            result.setProperty("disk", StringUtil.trim(hostBackup.getDisk()));
            result.setProperty("timestamp", hostBackup.getTimestamp());
            result.setProperty("avaliable", StringUtil.trim(hostBackup.isAvaliable()));
            return result;
        } else {
            result = new MethodResult(MethodResult.FAIL, "备份文件不可用或不存在");
            result.setProperty("avaliable", StringUtil.trim(hostBackup.isAvaliable()));
            return result;
        }

    }

    //连接http gateway
    public String hostBackup(Map<String, Object> parameter) throws Exception {

        String uuid = (String) parameter.get("uuid");
        Integer region = Integer.valueOf(String.valueOf(parameter.get("region")));
        String command = (String) parameter.get("command");


        HttpGatewayAsyncChannel channel = HttpGatewayManager.getAsyncChannel(region);

        if ("backup".equalsIgnoreCase(command)) {
            this.operatorHostBeforeBackup(uuid, region);
            Integer mode = Integer.valueOf(String.valueOf(parameter.get("mode")));
            Integer disk = Integer.valueOf(String.valueOf(parameter.get("disk")));

            try {
                JSONObject result = channel.hostBackup(uuid, mode, disk);
                if (HttpGatewayResponseHelper.isSuccess(result) == true) {
                    System.err.println("success to send backup host request.");
                    return channel.getSessionId();
                } else {
                    System.err.println("fail to send backup host request.");
                    channel.release();
                }
            } catch (Exception e) {
                System.err.println("fail to send backup host request.");
                channel.release();
                throw e;
            }
        } else if ("resume".equalsIgnoreCase(command)) {
            Integer mode = Integer.valueOf(String.valueOf(parameter.get("mode")));
            Integer disk = Integer.valueOf(String.valueOf(parameter.get("disk")));
            try {
                JSONObject result = channel.hostResume(uuid, mode, disk);
                if (HttpGatewayResponseHelper.isSuccess(result) == true) {
                    System.err.println("success to send resume host request.");
                    return channel.getSessionId();
                } else {
                    System.err.println("fail to send resume host request.");
                    channel.release();
                }
            } catch (Exception e) {
                System.err.println("fail to send resume host request.");
                channel.release();
                throw e;
            }
        } else if ("query".equalsIgnoreCase(command)) {
            try {
                JSONObject result = channel.hostQueryBackup(uuid);
                if (HttpGatewayResponseHelper.isSuccess(result) == true) {
                    System.err.println("success to send query host backup request.");
                    return channel.getSessionId();
                } else {
                    System.err.println("fail to send query host backup request.");
                    channel.release();
                }
            } catch (Exception e) {
                System.err.println("fail to send query host backup request.");
                channel.release();
                throw e;
            }
        }
        return null;
    }


    @Callable
    public String resetHostPage(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("CloudHostServiceImpl.resetHostPage()");

        String uuid = StringUtil.trim(request.getParameter("uuid"));
        int region = Integer.valueOf(StringUtil.trim(request.getParameter("region")));
        int resetFlag = 0;

        if ("reset_true".equals(AppInconstant.hostResetProgress.get(uuid + "_reset"))) {
            resetFlag = 1;
        }

        SysDiskImageMapper sysDiskImageMapper = this.sqlSession.getMapper(SysDiskImageMapper.class);
        List<SysDiskImageVO> sysDiskImageList = sysDiskImageMapper.getByRegion(region);

        Iterator<SysDiskImageVO> it = sysDiskImageList.iterator();
        while (it.hasNext()) {
            SysDiskImageVO sysDiskImageVO = it.next();
            if (sysDiskImageVO.getType() != 1 || sysDiskImageVO.getStatus() != 2) {
                it.remove();
            }
        }

        System.err.println(sysDiskImageList.size());

        request.setAttribute("uuid", uuid);
        request.setAttribute("region", region);
        request.setAttribute("reset_flag", resetFlag);
        request.setAttribute("sysDiskImageList", sysDiskImageList);

        return "/security/user/cloud_host_reset.jsp";
    }

    @Callable
    public MethodResult resetHost(Map<String, Object> parameter) {

        String uuid = StringUtil.trim(parameter.get("uuid"));
        String image = StringUtil.trim(parameter.get("image_id"));
        Integer region = Integer.valueOf(String.valueOf(parameter.get("region")));

        int mode = 0;
        int disk = 0;
        String sessionId = null;

        if ("backup_true".equals(AppInconstant.hostBackupProgress.get(uuid + "_backup"))) {
            throw new AppException("正在备份中 请完成后再进行此操作");
        }

        if ("resume_true".equals(AppInconstant.hostBackupProgress.get(uuid + "_resume"))) {
            throw new AppException("正在恢复中 请完成后再进行此操作");
        }

        if ("reset_true".equals(AppInconstant.hostResetProgress.get(uuid + "_reset"))) {
            throw new AppException("正在安装操作系统中 请完成后再进行此操作");
        }

        try {
            sessionId = this.hostReset(uuid, disk, mode, image, region);
            if (sessionId == null) {
                logger.warn("CloudHostServiceImpl.resetHost() > [" + Thread.currentThread().getId()
                    + "] rest host failed");
                throw new AppException("安装命令发送失败");
            }

            //存入缓存
            Map<String, Object> temporaryData = new LinkedHashMap<String, Object>();
            temporaryData.put("uuid", uuid);
            temporaryData.put("session_id", sessionId);
            AppInconstant.hostResetProgress.put(uuid + "_reset_progress", temporaryData);

            //存放镜像id和name
            SysDiskImageMapper sysDiskImageMapper =
                this.sqlSession.getMapper(SysDiskImageMapper.class);
            SysDiskImageVO sysDiskImageVO = sysDiskImageMapper.getByRealImageId(image);
            AppInconstant.hostResetProgress.put(uuid + "_image_id", sysDiskImageVO.getId());
            AppInconstant.hostResetProgress.put(uuid + "_image_name", sysDiskImageVO.getName());

            return new MethodResult(MethodResult.SUCCESS, "备份命令发送成功");
        } catch (Exception e) {
            System.err.println("fail to send reset host request.");
            e.printStackTrace();
            return new MethodResult(MethodResult.FAIL, "备份命令发送失败");
        }
    }

    public String hostReset(String uuid, int disk, int mode, String image, int region)
        throws MalformedURLException, IOException {
        HttpGatewayAsyncChannel channel = HttpGatewayManager.getAsyncChannel(region);
        try {

            JSONObject result = channel.hostFlushDiskImage(uuid, disk, mode, image);
            if (HttpGatewayResponseHelper.isSuccess(result) == true) {
                System.err.println("success to send flush disk image request.");
                return channel.getSessionId();
            } else {
                System.err.println("fail to send flush disk image request.");
                channel.release();
            }
        } catch (Exception e) {
            System.err.println("fail to send flush disk image request.");
            channel.release();
            throw e;
        }
        return null;
    }

    @Callable
    @Transactional(readOnly = false)
    public MethodResult getResetHostResult(String uuid) {

        try {
            @SuppressWarnings("unchecked") Map<String, Object> temporaryData =
                (Map<String, Object>) AppInconstant.hostResetProgress.get(uuid + "_reset_progress");
            if (temporaryData == null) {
                throw new AppException("无法获取安装信息");
            }


            HostResetProgressData data =
                this.getResetProgressData(temporaryData.get("uuid").toString());
            if (data == null) {
                MethodResult message = new MethodResult(MethodResult.FAIL, "安装还未开始");
                return message;
            }

            MethodResult result = new MethodResult(MethodResult.SUCCESS, "安装成功");
            if (data.isReady()) {
                if (data.isFinished()) {
                    if (data.isSuccess()) {
                        result.put("progress", 100);
                        result.put("reset_status", true);

                        Map<String, Object> imageData = new LinkedHashMap<>();
                        imageData.put("real_host_id", uuid);
                        imageData.put("sys_image_id", StringUtil
                            .trim(AppInconstant.hostResetProgress.get(uuid + "_image_id")));
                        imageData.put("sys_image_name", StringUtil
                            .trim(AppInconstant.hostResetProgress.get(uuid + "_image_name")));
                        CloudHostMapper cloudHostMapper =
                            this.sqlSession.getMapper(CloudHostMapper.class);
                        cloudHostMapper.updateImageByRealId(imageData);
                    } else {
                        result.put("reset_status", false);
                    }
                } else {
                    result.put("progress", data.getProgress());
                }
                return result;
            }

            MethodResult message = new MethodResult(MethodResult.FAIL, "安装还未开始");
            return message;
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException(e);
        }
    }

    public HostResetProgressData getResetProgressData(String uuid) {
        HostResetProgressPool pool = HostResetProgressPoolManager.singleton().getPool();
        return pool.getDuplication(uuid);
    }

    /**
     * 强制重启
    * <p>Title: forceRestartCloudHost</p> 
    * <p>Description: </p> 
    * @param hostId
    * @return 
    * @see com.zhicloud.op.service.CloudHostService#forceRestartCloudHost(java.lang.String)
     */
    @Callable
    @Transactional(readOnly = false)
    public MethodResult forceRestartCloudHost(String hostId) {
        String newHostId = hostId;
        Long begin = System.currentTimeMillis();
        Integer logStatus = AppConstant.OPER_LOG_FAIL;
        HttpServletRequest request = RequestContext.getHttpRequest();
        LoginInfo loginInfo = null;
        if (newHostId != null && newHostId.length() == 32) {
            loginInfo = LoginHelper.getLoginInfo(request);
        } else if (newHostId != null && newHostId.length() == 37) {
            hostId = hostId.substring(0, hostId.length() - 5);
        }
        String _hostName = "";
        try {
            CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
            CloudHostVO cloudHost = cloudHostMapper.getById(hostId);
            _hostName = cloudHost.getDisplayName();
            String realHostId = cloudHost.getRealHostId();
            if (realHostId == null) {
                MethodResult result = new MethodResult(MethodResult.FAIL, "云主机尚未创建成功");
                result.put("hostId", hostId);
                return result;
            }
            Map<String, Object> cloudHostData = new LinkedHashMap<String, Object>();
            CloudHostData myCloudHostData =
                CloudHostPoolManager.getCloudHostPool().getByRealHostId(cloudHost.getRealHostId());
            cloudHostData.put("realHostId", realHostId);
            if (myCloudHostData != null && myCloudHostData.getRunningStatus() == 1) {
                HttpGatewayChannelExt channel =
                    HttpGatewayManager.getChannel(cloudHost.getRegion());
                JSONObject startResullt = channel.hostStart(realHostId, 0, "");
                if (HttpGatewayResponseHelper.isSuccess(startResullt) == false) {
                    cloudHostData
                        .put("runningStatus", AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTDOWN);
                    cloudHostMapper.updateRunningStatusByRealHostId(cloudHostData);
                    logger.warn(
                        "CloudHostServiceImpl.restartCloudHost() > [" + Thread.currentThread()
                            .getId() + "] start host failed, message:[" + HttpGatewayResponseHelper
                            .getMessage(startResullt) + "]");
                    MethodResult result = new MethodResult(MethodResult.FAIL, "重启失败");
                    result.put("hostId", hostId);
                    return result;
                }
                logStatus = AppConstant.OPER_LOG_SUCCESS;
                MethodResult result = new MethodResult(MethodResult.SUCCESS, "重启成功");
                result.put("hostId", hostId);
                return result;
            }
            HttpGatewayAsyncChannel asyncChannel = HttpGatewayManager.getAsyncChannel(cloudHost.getRegion());

           JSONObject restarResullt = asyncChannel.hostReset(realHostId);

            if (HttpGatewayResponseHelper.isSuccess(restarResullt) == false) {
                logger.warn(
                    "CloudHostServiceImpl.forceRestartCloudHost() > [" + Thread.currentThread().getId()
                        + "] halt host failed, message:[" + HttpGatewayResponseHelper
                        .getMessage(restarResullt) + "]");
                MethodResult result = new MethodResult(MethodResult.FAIL, "重启失败");
                result.put("hostId", hostId);
                return result;
            }
            cloudHostData.put("runningStatus", AppConstant.CLOUD_HOST_RUNNING_STATUS_RESTARTING);
            cloudHostMapper.updateRunningStatusByRealHostId(cloudHostData);
            
         // 更新缓冲池值
            CloudHostData newCloudHostData = myCloudHostData.clone();
            newCloudHostData.setRunningStatus(AppConstant.CLOUD_HOST_RUNNING_STATUS_RESTARTING);
            newCloudHostData.setLastOper(AppConstant.CLOUD_HOST_RUNNING_STATUS_RESTARTING);
            newCloudHostData.setLastOperTime(StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
            CloudHostPoolManager.getCloudHostPool().put(newCloudHostData);
            
            logger.info(
                "CloudHostServiceImpl.forceRestartCloudHost() > [" + Thread.currentThread().getId()
                    + "] restart host succeeded");
            logStatus = AppConstant.OPER_LOG_SUCCESS;
            return new MethodResult(MethodResult.SUCCESS, "强制重启成功");
        } catch (ConnectException e) {
            // throw new AppException("连接http gateway失败", e);
            logger.error(e);
            throw new AppException("强制重启失败");
        } catch (AppException e) {
            logger.error(e);
            throw new AppException("强制重启失败");
        } catch (Exception e) {
            logger.error(e);
            throw new AppException("强制重启失败");
        } finally {
            try {
                if (newHostId != null && newHostId.length() == 32) {
                    OperLogMapper operLogMapper = this.sqlSession.getMapper(OperLogMapper.class);
                    Map<String, Object> operLogData = new LinkedHashMap<String, Object>();
                    operLogData.put("id", StringUtil.generateUUID());
                    operLogData.put("userId", loginInfo.getUserId());
                    operLogData.put("content", "强制重启云主机:" + _hostName);
                    operLogData
                        .put("operTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
                    operLogData.put("status", logStatus);
                    operLogData.put("resourceName", "用户名:" + loginInfo.getAccount());
                    operLogData.put("operDuration", System.currentTimeMillis() - begin);
                    operLogMapper.addOperLog(operLogData);
                }
            } catch (Exception e) {
                logger.error(e);
            }

        }
    } 

}

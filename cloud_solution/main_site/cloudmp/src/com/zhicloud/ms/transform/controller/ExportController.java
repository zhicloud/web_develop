package com.zhicloud.ms.transform.controller;

import com.zhicloud.ms.constant.AppConstant;
import com.zhicloud.ms.exception.AppException;
import com.zhicloud.ms.service.*;
import com.zhicloud.ms.transform.service.ManSysMenuService;
import com.zhicloud.ms.transform.service.ManSysRightService;
import com.zhicloud.ms.transform.service.ManSysRoleService;
import com.zhicloud.ms.transform.service.ManSysUserService;
import com.zhicloud.ms.transform.util.ExportExcelUtils;
import com.zhicloud.ms.transform.util.TransFormLoginHelper;
import com.zhicloud.ms.transform.util.TransFormLoginInfo;
import com.zhicloud.ms.transform.vo.ManSystemMenuVO;
import com.zhicloud.ms.transform.vo.ManSystemRightVO;
import com.zhicloud.ms.transform.vo.ManSystemRoleVO;
import com.zhicloud.ms.transform.vo.ManSystemUserVO;
import com.zhicloud.ms.vo.*;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: ExportController
 * @Description: 导出类
 * @author 张本缘 于 2015年9月24日 上午11:53:42
 */
@Controller
@RequestMapping(value = "/export")
public class ExportController {

    /* 日志 */
    public static final Logger logger = Logger.getLogger(ExportController.class);

    @Resource
    ManSysUserService manSysUserService;
    
    @Resource
    ManSysRoleService manSysRoleService;
    
    @Resource
    ManSysRightService manSysRightService;
    
    @Resource
    ManSysMenuService manSysMenuService;
    
    @Resource
    ICloudHostWarehouseService cloudHostWarehouseService;
    
    @Resource
    ICloudHostService cloudHostService;
    
    @Resource
    private ISysDiskImageService sysDiskImageService;
    
    @Resource
    private ITerminalBoxService terminalBoxService;
    
    @Resource
    private IBoxRealInfoService boxRealInfoService;
    
    @Resource
    private IVersionRecordService versionRecordService;
    
    @Resource
    private IBackUpDetailService backUpDetailService;
    
    @Resource
    private IOperLogService operLogService;
    
    @Resource
    CloudHostConfigModelService cloudHostConfigModelService;
    
    @Resource
    private ISetTimeOperationDetailService setTimeOperationDetailService;
    
    @Resource
    private ISysGroupService sysGroupService;
    
    @Resource
    private ITerminalUserService terminalUserService;
    
    @Resource
    private IQosService qosService;
    
    @Resource
    private IEmailConfigService emailConfigService;
    
    @Resource
    private IEmailTemplateService emailTemplateService;
    
    @Resource
    private IMessageRecordService messageRecordService;
    
    @Resource
    private ISmsConfigService smsConfigService;
    
    @Resource
    private ISmsTemplateService smsTemplateService;
    
    @Resource
    ICloudDiskService cloudDiskService;
    
    @Resource
    IVpcService vpcService;
    
    @Resource
    ItenantService tenantService;
    
    @Resource
    IClientMessageService clientMessageService;

    @Resource
    private ITerminalInformationPushService terminalInformationPushService;
    
    /**
     * @Description:用户管理导出数据
     * @param request
     * @param response
     */
    @RequestMapping(value = "/userdata")
    public void exportUserData(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("ExportController.exportUserData()");
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json; charset=utf-8");
            String[][] columns = new String[][] { {"用户账号", "Usercount" }, {"显示名称", "Displayname" },
                    {"用户类型", "Usertype_name" }, {"邮箱", "Email" }, {"联系电话", "Telphone" }, {"状态", "Status_name" },
                    {"创建时间", "Insert_date" } };
            ExportExcelUtils.export(request, response, manSysUserService.getAll(), "用户数据", columns,
                    ManSystemUserVO.class);
            operLogService.addLog("用户管理", "导出用户数据成功", "1", "1", request);
        } catch (Exception e) {
            logger.error("ExportController.exportUserData()", e);
            operLogService.addLog("用户管理", "导出用户数据失败", "1", "2", request);
            throw new AppException("导出失败");
        }
    }
    
    /**
     * @Description:角色管理导出数据
     * @param request
     * @param response
     */
    @RequestMapping(value = "/roledata")
    public void exportRoleData(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("ExportController.exportRoleData()");
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json; charset=utf-8");
            String[][] columns = new String[][] { {"角色名称", "Name" }, {"角色编码", "Code" }, {"创建时间", "Insert_date" } };
            ExportExcelUtils.export(request, response, manSysRoleService.getAllRole(), "角色数据", columns,
                    ManSystemRoleVO.class);
            operLogService.addLog("角色管理", "导出角色数据成功", "1", "1", request);
        } catch (Exception e) {
            logger.error("ExportController.exportRoleData()", e);
            operLogService.addLog("角色管理", "导出角色数据失败", "1", "2", request);
            throw new AppException("导出失败");
        }
    }
    
    /**
     * @Description:角色组管理导出数据
     * @param request
     * @param response
     */
    @RequestMapping(value = "/rolegroupdata")
    public void exportRoleGroupData(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("ExportController.exportRoleGroupData()");
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json; charset=utf-8");
            String[][] columns = new String[][] { {"角色名称", "Name" }, {"创建时间", "Insert_date" } };
            ExportExcelUtils.export(request, response, manSysRoleService.getAllGroup(), "角色组数据", columns,
                    ManSystemRoleVO.class);
            operLogService.addLog("角色组管理", "导出角色组数据成功", "1", "1", request);
        } catch (Exception e) {
            logger.error("ExportController.exportRoleGroupData()", e);
            operLogService.addLog("角色组管理", "导出角色组数据失败", "1", "2", request);
            throw new AppException("导出失败");
        }
    }
    
    /**
     * @Description:功能权限维护导出数据
     * @param request
     * @param response
     */
    @RequestMapping(value = "/rightdata")
    public void exportRightData(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("ExportController.exportRightData()");
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json; charset=utf-8");
            String[][] columns = new String[][] { {"权限名称", "Name" }, {"权限编码", "Code" }, {"已关联菜单", "Menuname" },
                    {"创建时间", "Insert_date" } };
            ExportExcelUtils.export(request, response, manSysRightService.getAll(), "功能权限数据", columns,
                    ManSystemRightVO.class);
            operLogService.addLog("功能权限维护", "导出功能权限数据成功", "1", "1", request);
        } catch (Exception e) {
            logger.error("ExportController.exportRightData()", e);
            operLogService.addLog("功能权限维护", "导出功能权限数据失败", "1", "2", request);
            throw new AppException("导出失败");
        }
    }
    
    /**
     * @Description:功能权限维护导出数据
     * @param request
     * @param response
     */
    @RequestMapping(value = "/menudata")
    public void exportMenuData(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("ExportController.exportMenuData()");
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json; charset=utf-8");
            String[][] columns = new String[][] { {"菜单名称", "Menuname" }, {"连接路径", "Linkname" }, {"顺序", "Sort" },
                    {"状态", "Status" }, {"备注", "Remark" }, {"创建时间", "Insert_date" } };
            List<ManSystemMenuVO> menus = manSysMenuService.getAll("2", "");
            for (ManSystemMenuVO menu : menus) {
                if ("0".equals(menu.getStatus())) {
                    menu.setStatus("正常");
                } else {
                    menu.setStatus("禁用");
                }
            }
            ExportExcelUtils.export(request, response, menus, "功能菜单数据", columns, ManSystemMenuVO.class);
            operLogService.addLog("功能菜单维护", "导出功能菜单数据成功", "1", "1", request);
        } catch (Exception e) {
            logger.error("ExportController.exportMenuData()", e);
            operLogService.addLog("功能菜单维护", "导出功能菜单数据失败", "1", "2", request);
            throw new AppException("导出失败");
        }
    }
    
    /**
     * @Description:功能权限维护导出子菜单数据
     * @param request
     * @param response
     */
    @RequestMapping(value = "/menuchildrendata/{id}", method = RequestMethod.GET)
    public void exportMenuChildrenData(@PathVariable String id, HttpServletRequest request, HttpServletResponse response) {
        logger.debug("ExportController.exportMenuChildrenData()");
        ManSystemMenuVO m = manSysMenuService.getMenuByID(id);
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json; charset=utf-8");
            String[][] columns = new String[][] { {"菜单名称", "Menuname" }, {"连接路径", "Linkname" }, {"顺序", "Sort" },
                    {"状态", "Status" }, {"备注", "Remark" }, {"创建时间", "Insert_date" } };
            List<ManSystemMenuVO> menus = manSysMenuService.getAll("3", id);
            for (ManSystemMenuVO menu : menus) {
                if ("0".equals(menu.getStatus())) {
                    menu.setStatus("正常");
                } else {
                    menu.setStatus("禁用");
                }
            }
            ExportExcelUtils
                    .export(request, response, menus, m.getMenuname() + "子菜单数据", columns, ManSystemMenuVO.class);
            operLogService.addLog("功能菜单维护", "导出" + m.getMenuname() + "子菜单数据成功", "1", "1", request);
        } catch (Exception e) {
            logger.error("ExportController.exportMenuChildrenData()", e);
            operLogService.addLog("功能菜单维护", "导出" + m.getMenuname() + "子菜单数据失败", "1", "2", request);
            throw new AppException("导出失败");
        }
    }
    
    /**
     * @Description:主机仓库管理导出数据
     * @param request
     * @param response
     */
    @RequestMapping(value = "/warehousedata")
    public void exportWarehouseData(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("ExportController.exportWarehouseData()");
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json; charset=utf-8");
            String[][] columns = new String[][] { {"仓库名称", "Name" }, {"主机类型", "HostTypeName" }, {"数量", "TotalAmount" },
                    {"已分配", "AssignedAmount" }, {"未分配", "RemainAmount" }, {"创建时间", "Insert_date" } };
            ExportExcelUtils.export(request, response, cloudHostWarehouseService.getAll(), "主机仓库管理数据", columns,
                    CloudHostWarehouse.class);
            operLogService.addLog("主机管理", "导出主机管理数据成功", "1", "1", request);
        } catch (Exception e) {
            logger.error("ExportController.exportWarehouseData()", e);
            operLogService.addLog("主机管理", "导出主机管理数据失败", "1", "2", request);
            throw new AppException("导出失败");
        }
    }
    
    /**
     * @Description:主机系统镜像管理导出数据
     * @param request
     * @param response
     */
    @RequestMapping(value = "/imagedata")
    public void exportImageData(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("ExportController.exportImageData()");
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json; charset=utf-8");
            Map<String, Object> condition = new LinkedHashMap<String, Object>();
            condition.put("name", null);
            condition.put("image_type", null);
            condition.put("status", null);
            condition.put("type", null);
            String[][] columns = new String[][] { {"镜像名称", "Name" }, {"显示名称", "DisplayName" }, {"类型", "TypeName" },
                    {"镜像用途", "ImageTypeName" }, {"创建时间", "Insert_date" },  {"状态", "IamgeStatus" } };
            ExportExcelUtils.export(request, response, sysDiskImageService.getSysDiskImageByMultiParams(condition), "主机系统镜像数据", columns,
                    SysDiskImageVO.class);
            operLogService.addLog("主机系统镜像管理", "导出主机系统镜像数据成功", "1", "1", request);
        } catch (Exception e) {
            logger.error("ExportController.exportImageData()", e);
            operLogService.addLog("主机系统镜像管理", "导出主机系统镜像数据失败", "1", "2", request);
            throw new AppException("导出失败");
        }
    }
    
    /**
     * @Description:终端盒子资产管理导出数据
     * @param request
     * @param response
     */
    @RequestMapping(value = "/boxdata")
    public void exportBoxData(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("ExportController.exportBoxData()");
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json; charset=utf-8");
            String[][] columns = new String[][] { {"编号", "SerialNumber" }, {"终端盒子名称", "Name" }, {"创建时间", "Insert_date" },
                    {"分配用户", "AllocateUser" }, {"分配时间", "Allocate_date" },  {"状态", "Status_name" } };
            ExportExcelUtils.export(request, response, terminalBoxService.getAll(), "终端盒子资产数据", columns,
                    TerminalBoxVO.class);
            operLogService.addLog("终端盒子资产管理", "导出终端盒子资产数据成功", "1", "1", request);
        } catch (Exception e) {
            logger.error("ExportController.exportBoxData()", e);
            operLogService.addLog("终端盒子资产管理", "导出终端盒子资产数据失败", "1", "2", request);
            throw new AppException("导出失败");
        }
    }
    
    /**
     * @Description:终端盒子实时情况导出数据
     * @param request
     * @param response
     */
    @RequestMapping(value = "/boxrealinfodata")
    public void exportBoxRealInfoData(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("ExportController.exportBoxRealInfoData()");
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json; charset=utf-8");
            String[][] columns = new String[][] { {"用户名", "UserName" }, {"盒子编号", "SerialNumber" }, {"IP", "Ip" },
                    {"网关", "Gateway" }, {"盒子MAC", "Mac" }, {"软件版本", "SoftwareVersion" }, {"硬件版本", "HardwareVersion" },
                    {"上次登录时间", "Lastlogin_date" }, {"上次ALIVE时间", "Lastalive_date" }, {"当前状态", "CaculateRealStatus" } };
            List<BoxRealInfoVO> lists = boxRealInfoService.getAllInfo();
            ExportExcelUtils.export(request, response, lists, "终端盒子实时情况", columns, BoxRealInfoVO.class);
            operLogService.addLog("终端盒子实时情况", "导出终端盒子实时情况数据成功", "1", "1", request);
        } catch (Exception e) {
            logger.error("ExportController.exportBoxRealInfoData()", e);
            operLogService.addLog("终端盒子实时情况", "导出终端盒子实时情况数据失败", "1", "2", request);
            throw new AppException("导出失败");
        }
    }
    
    /**
     * @Description:盒子固件升级导出数据
     * @param request
     * @param response
     */
    @RequestMapping(value = "/versiondata")
    public void exportVersionData(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("ExportController.exportVersionData()");
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json; charset=utf-8");
            String[][] columns = new String[][] { {"版本号", "VersionNumber" }, {"更新内容", "UpdateInfo" }, {"文件大小", "Filesize" },
                    {"创建时间", "Insert_date" } };
            ExportExcelUtils.export(request, response, versionRecordService.getAllVersion(), "盒子固件升级", columns, VersionRecordVO.class);
            operLogService.addLog("盒子固件升级", "导出盒子固件升级数据成功", "1", "1", request);
        } catch (Exception e) {
            logger.error("ExportController.exportVersionData()", e);
            operLogService.addLog("盒子固件升级", "导出盒子固件升级数据失败", "1", "2", request);
            throw new AppException("导出失败");
        }
    }
    
    /**
     * @Description:备份和恢复导出数据
     * @param request
     * @param response
     */
    @RequestMapping(value = "/backresumedata")
    public void exportBackresumeData(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("ExportController.exportBackresumeData()");
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json; charset=utf-8");
            String[][] columns = new String[][] { {"主机名", "DisplayName" }, {"所属用户", "UserName" }, {"备份完成时间", "BackUp_date" },
                    {"备份状态", "Status_name" } };
            ExportExcelUtils.export(request, response, backUpDetailService.getAllDetail(AppConstant.MESSAGE_TYPE_DESKTOP_BACKUP), "备份和恢复", columns, BackUpDetailVO.class);
            operLogService.addLog("备份和恢复", "导出备份和恢复数据成功", "1", "1", request);
        } catch (Exception e) {
            logger.error("ExportController.exportBackresumeData()", e);
            operLogService.addLog("备份和恢复", "导出备份和恢复数据失败", "1", "2", request);
            throw new AppException("导出失败");
        }
    }
    
    
    /**
     * @Description:预启动管理导出数据
     * @param request
     * @param response
     */
    @RequestMapping(value = "/settimeoperationdata")
    public void exportSettimeoperationData(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("ExportController.exportSettimeoperationData()");
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json; charset=utf-8");
            String[][] columns = new String[][] { {"主机名", "DisplayName" }, {"操作时间", "CreateTimeDate" },
                    {"操作", "Type_name" }, {"状态", "Status_name" } };
            ExportExcelUtils.export(request, response, setTimeOperationDetailService.getAllDetail(), "定时操作记录", columns,
                    SetTimeOperationDetailVO.class);
            operLogService.addLog("预启动管理", "导出定时操作记录数据成功", "1", "1", request);
        } catch (Exception e) {
            logger.error("ExportController.exportSettimeoperationData()", e);
            operLogService.addLog("预启动管理", "导出定时操作记录数据失败", "1", "2", request);
            throw new AppException("导出失败");
        }
    }
    
    /**
     * @Description:主机配置导出数据
     * @param request
     * @param response
     */
    @RequestMapping(value = "/chcmdata")
    public void exportChcmData(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("ExportController.exportChcmData()");
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json; charset=utf-8");
            String[][] columns = new String[][] { {"主机配置名称", "Name" }, {"配置", "Operate_property" },
                    {"镜像", "SysImageName" }, {"创建时间", "CurCreateDate" }, {"修改时间", "Modyfy_date" } };
            ExportExcelUtils.export(request, response, cloudHostConfigModelService.getAll(), "主机配置", columns,
                    CloudHostConfigModel.class);
            operLogService.addLog("主机配置", "导出主机配置数据成功", "1", "1", request);
        } catch (Exception e) {
            logger.error("ExportController.exportChcmData()", e);
            operLogService.addLog("主机配置", "导出主机配置数据失败", "1", "2", request);
            throw new AppException("导出失败");
        }
    }
    
    /**
     * @Description:桌面云-群组信息导出数据
     * @param request
     * @param response
     */
    @RequestMapping(value = "/groupdata")
    public void exportGroupData(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("ExportController.exportGroupData()");
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json; charset=utf-8");
            String[][] columns = new String[][] { {"分组名", "GroupName" }, {"成员数", "Amount" }, {"所属组名", "ParentName" },
                    {"分配云主机数", "CloudHostAmount" }, {"描述", "Description" } };
            ExportExcelUtils.export(request, response, sysGroupService.getCloudHostAmountInGroup(), "群组信息", columns,
                    SysGroupVO.class);
            operLogService.addLog("群组信息", "导出群组信息数据成功", "1", "1", request);
        } catch (Exception e) {
            logger.error("ExportController.exportGroupData()", e);
            operLogService.addLog("群组信息", "导出群组信息数据失败", "1", "2", request);
            throw new AppException("导出失败");
        }
    }
    
    /**
     * @Description:桌面云-终端用户管理导出数据
     * @param request
     * @param response
     */
    @RequestMapping(value = "/terminaluserdata")
    public void exportTerminaluserData(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("ExportController.exportTerminaluserData()");
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json; charset=utf-8");
            String[][] columns = new String[][] { {"用户名", "Username" },  {"显示名", "Name" },
                    {"所属分组", "GroupName" }, {"邮箱", "Email" }, {"电话", "Phone" }, {"用户状态", "Status_name" },
                    {"USB权限", "Usbstatus_name" }, {"已分配云主机", "CloudHostAmount" } };
            ExportExcelUtils.export(request, response, terminalUserService.queryAll(), "终端用户", columns,
                    TerminalUserVO.class);
            operLogService.addLog("终端用户管理", "导出终端用户数据成功", "1", "1", request);
        } catch (Exception e) {
            logger.error("ExportController.exportTerminaluserData()", e);
            operLogService.addLog("终端用户管理", "导出终端用户数据失败", "1", "2", request);
            throw new AppException("导出失败");
        }
    } 
    
    /**
     * @Description:桌面云-终端用户管理导出数据
     * @param request
     * @param response
     */
    @RequestMapping(value = "/terminaluserdetaildata/{id}")
    public void exportTerminaluserDetailData(@PathVariable String id,HttpServletRequest request, HttpServletResponse response) {
        logger.debug("ExportController.exportTerminaluserDetailData()");
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json; charset=utf-8");
            String[][] columns = new String[][] { {"云主机名称", "DisplayName" }, {"类型", "SysImageName" }, {"配置", "Properties" },
                    {"分配状态", "Userid_name" }, {"分配时间", "AssignDate" }, {"运行状态", "SummarizedStatusText" } };
            ExportExcelUtils.export(request, response, cloudHostService.getCloudHostByUserId(id), "终端用户详情", columns,
                    CloudHostVO.class);
            operLogService.addLog("终端用户管理", "导出终端用户详情数据成功", "1", "1", request);
        } catch (Exception e) {
            logger.error("ExportController.exportTerminaluserDetailData()", e);
            operLogService.addLog("终端用户管理", "导出终端用户详情数据失败", "1", "2", request);
            throw new AppException("导出失败");
        }
    } 
    
    /**
     * @Description:云服务器Qos设置导出数据
     * @param request
     * @param response
     */
    @RequestMapping(value = "/serverqosdata")
    public void exportServerQosData(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("ExportController.exportServerQosData()");
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json; charset=utf-8");
            String[][] columns = new String[][] { {"名称", "Name" }, {"云主机IP", "Ip" }, {"所属服务器", "ServerIp" },
                    {"带宽", "Bandwidth" }, {"硬盘", "MaxIops_name" }, {"VPCU优先级", "Priority_name" },
                    {"创建时间", "CreateTimeDate" } };
            ExportExcelUtils.export(request, response, qosService.getAll(AppConstant.HOST_TYPE_SERVER), "Qos设置",
                    columns, QosVO.class);
            operLogService.addLog("Qos设置", "导出Qos设置数据成功", "1", "1", request);
        } catch (Exception e) {
            logger.error("ExportController.exportServerQosData()", e);
            operLogService.addLog("Qos设置", "导出Qos设置数据失败", "1", "2", request);
            throw new AppException("导出失败");
        }
    }
    
    /**
     * @Description:邮件配置导出数据
     * @param request
     * @param response
     */
    @RequestMapping(value = "/emailconfiglistdata")
    public void exportEmailConfigListData(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("ExportController.exportEmailConfigListData()");
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json; charset=utf-8");
            String[][] columns = new String[][] { {"邮件配置名称", "Name" }, {"发送人", "Sender" }, {"发送人邮箱", "SenderAddress" },
                    {"创建时间", "CreateTimeDate" }, {"更新时间", "ModifiedTimeDate" } };
            ExportExcelUtils.export(request, response, emailConfigService.getAllConfig(), "邮件配置", columns,
                    EmailConfigVO.class);
            operLogService.addLog("消息服务管理", "导出邮件配置数据成功", "1", "1", request);
        } catch (Exception e) {
            logger.error("ExportController.exportEmailConfigListData()", e);
            operLogService.addLog("消息服务管理", "导出邮件配置数据失败", "1", "2", request);
            throw new AppException("导出失败");
        }
    }
    
    /**
     * @Description:邮件模板导出数据
     * @param request
     * @param response
     */
    @RequestMapping(value = "/emailtemplatelistdata")
    public void exportEmailTemplateListData(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("ExportController.exportEmailTemplateListData()");
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json; charset=utf-8");
            String[][] columns = new String[][] { {"邮件模板名称", "Name" }, {"邮件服务器", "ConfigName" },
                    {"发送人邮箱", "SenderAddress" }, {"收件人邮箱", "Recipient" }, {"代码", "Code" }, {"创建时间", "CreateTimeDate" },
                    {"更新时间", "ModifiedTimeDate" } };
            ExportExcelUtils.export(request, response, emailTemplateService.getAllTemplate(), "邮件模板", columns,
                    EmailTemplateVO.class);
            operLogService.addLog("消息服务管理", "导出邮件模板数据成功", "1", "1", request);
        } catch (Exception e) {
            logger.error("ExportController.exportEmailTemplateListData()", e);
            operLogService.addLog("消息服务管理", "导出邮件模板数据失败", "1", "2", request);
            throw new AppException("导出失败");
        }
    }
    
    /**
     * @Description:邮件发送记录导出数据
     * @param request
     * @param response
     */
    @RequestMapping(value = "/emailrecordlistdata")
    public void exportEmailRecordListData(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("ExportController.exportEmailRecordListData()");
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json; charset=utf-8");
            String[][] columns = new String[][] { {"发送人邮箱", "SenderAddress" }, {"收件人邮箱", "RecipientAddress" },
                    {"发送时间", "CreateTimeDate" } };
            ExportExcelUtils.export(request, response,
                    messageRecordService.getAllRecord(AppConstant.MESSAGE_TYPE_EMAIL), "邮件发送记录", columns,
                    MessageRecordVO.class);
            operLogService.addLog("消息服务管理", "导出邮件发送记录数据成功", "1", "1", request);
        } catch (Exception e) {
            logger.error("ExportController.exportEmailRecordListData()", e);
            operLogService.addLog("消息服务管理", "导出邮件发送记录数据失败", "1", "2", request);
            throw new AppException("导出失败");
        }
    }
    
    /**
     * @Description:短信配置导出数据
     * @param request
     * @param response
     */
    @RequestMapping(value = "/smsconfiglistdata")
    public void exportSmsConfigListData(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("ExportController.exportSmsConfigListData()");
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json; charset=utf-8");
            String[][] columns = new String[][] { {"短信配置名称", "ConfigName" }, {"发送账户", "Name" },
                    {"创建时间", "CreateTimeDate" }, {"更新时间", "ModifiedTimeDate" } };
            ExportExcelUtils.export(request, response, smsConfigService.getAllConfig(), "短信配置", columns,
                    SmsConfigVO.class);
            operLogService.addLog("消息服务管理", "导出短信配置数据成功", "1", "1", request);
        } catch (Exception e) {
            logger.error("ExportController.exportSmsConfigListData()", e);
            operLogService.addLog("消息服务管理", "导出短信配置数据失败", "1", "2", request);
            throw new AppException("导出失败");
        }
    }
    
    /**
     * @Description:短信模板导出数据
     * @param request
     * @param response
     */
    @RequestMapping(value = "/smstemplatelistdata")
    public void exportSmsTemplateListData(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("ExportController.exportSmsTemplateListData()");
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json; charset=utf-8");
            String[][] columns = new String[][] { {"短信模板名称", "Name" }, {"短信服务器", "ConfigName" },
                    {"收信人号码", "Recipient" }, {"代码", "Code" }, {"创建时间", "CreateTimeDate" },
                    {"更新时间", "ModifiedTimeDate" } };
            ExportExcelUtils.export(request, response, smsTemplateService.getAllTemplate(), "短信模板", columns,
                    SmsTemplateVO.class);
            operLogService.addLog("消息服务管理", "导出短信模板数据成功", "1", "1", request);
        } catch (Exception e) {
            logger.error("ExportController.exportSmsTemplateListData()", e);
            operLogService.addLog("消息服务管理", "导出短信模板数据失败", "1", "2", request);
            throw new AppException("导出失败");
        }
    }
    
    /**
     * @Description:短信发送记录导出数据
     * @param request
     * @param response
     */
    @RequestMapping(value = "/smsrecordlistdata")
    public void exportSmsRecordListData(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("ExportController.exportSmsRecordListData()");
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json; charset=utf-8");
            String[][] columns = new String[][] { {"发送短信服务", "Name" }, {"收信人号码", "ConfigName" }, {"发送时间", "Recipient" } };
            ExportExcelUtils.export(request, response, messageRecordService.getAllRecord(AppConstant.MESSAGE_TYPE_SMS),
                    "短信发送记录", columns, MessageRecordVO.class);
            operLogService.addLog("消息服务管理", "导出短信发送记录数据成功", "1", "1", request);
        } catch (Exception e) {
            logger.error("ExportController.exportSmsRecordListData()", e);
            operLogService.addLog("消息服务管理", "导出短信发送记录数据失败", "1", "2", request);
            throw new AppException("导出失败");
        }
    }
    
    /**
     * @Description:云服务器管理导出数据
     * @param request
     * @param response
     */
    @RequestMapping(value = "/cloudserverdata")
    public void exportCloudserverData(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("ExportController.exportCloudserverData()");
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json; charset=utf-8");
            String[][] columns = new String[][] { {"云服务器名", "DisplayName" }, {"租户", "TenantName" }, {"CPU", "Cpucore_name" }
            , {"内存", "Memory_name" }, {"磁盘", "Disk_name" }
            , {"CPU利用率", "Cpuusage" }, {"内存利用率", "Memeoryusage" }, {"磁盘利用率", "Diskusage" }
            , {"内网监控IP", "InnerIp" }, {"外网监控IP", "OuterIp" }, {"状态", "SummarizedStatusText" }};
            ExportExcelUtils.export(request, response, cloudHostService.getAllServer(),
                    "云服务器管理", columns, CloudHostVO.class);
            operLogService.addLog("云服务器管理", "导出云服务器数据成功", "1", "1", request);
        } catch (Exception e) {
            logger.error("ExportController.exportCloudserverData()", e);
            operLogService.addLog("云服务器管理", "导出云服务器数据失败", "1", "2", request);
            throw new AppException("导出失败");
        }
    }    
    
    /**
     * @Description:服务器配置管理导出数据
     * @param request
     * @param response
     */
    @RequestMapping(value = "/cscmdata")
    public void exportCscmData(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("ExportController.exportCscmData()");
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json; charset=utf-8");
            String[][] columns = new String[][] { {"主机配置名称", "Name" }, {"配置", "Operate_property" },
                    {"镜像", "SysImageNameFormat" }, {"创建时间", "CurCreateDate" }, {"修改时间", "Modyfy_date" } };
            ExportExcelUtils.export(request, response, cloudHostConfigModelService.getAllServer(), "服务器配置管理", columns,
                    CloudHostConfigModel.class);
            operLogService.addLog("服务器配置管理", "导出服务器配置数据成功", "1", "1", request);
        } catch (Exception e) {
            logger.error("ExportController.exportCscmData()", e);
            operLogService.addLog("服务器配置管理", "导出服务器配置数据失败", "1", "2", request);
            throw new AppException("导出失败");
        }
    }
    
    /**
     * @Description:镜像管理导出数据
     * @param request
     * @param response
     */
    @RequestMapping(value = "/serverimagedata")
    public void exportServerimageData(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("ExportController.exportServerimageData()");
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json; charset=utf-8");
            String[][] columns = new String[][] { {"镜像名称", "Name" }, {"显示名称", "DisplayName" },
                    {"类型", "TypeName" }, {"镜像用途", "ImageTypeName" }, {"创建时间", "CreateTimeDate" }
                    , {"状态", "Status_name" }};
            ExportExcelUtils.export(request, response, sysDiskImageService.querySysDiskImageByImageType(AppConstant.DISK_IMAGE_TYPE_SERVER), "镜像管理", columns,
                    SysDiskImageVO.class);
            operLogService.addLog("镜像管理", "导出镜像数据成功", "1", "1", request);
        } catch (Exception e) {
            logger.error("ExportController.exportServerimageData()", e);
            operLogService.addLog("镜像管理", "导出镜像数据失败", "1", "2", request);
            throw new AppException("导出失败");
        }
    }
    
    /**
     * @Description:备份和恢复导出数据
     * @param request
     * @param response
     */
    @RequestMapping(value = "/csbackresumedata")
    public void exportCsbackresumeData(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("ExportController.exportCsbackresumeData()");
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json; charset=utf-8");
            String[][] columns = new String[][] { {"主机名", "DisplayName" }, {"所属用户", "UserName" },
                    {"备份完成时间", "BackUpTimeDate" }, {"备份状态", "Status_name" } };
            ExportExcelUtils.export(request, response,
                    backUpDetailService.getAllDetail(AppConstant.MESSAGE_TYPE_SERVER_BACKUP), "备份记录", columns,
                    BackUpDetailVO.class);
            operLogService.addLog("备份和恢复", "导出备份记录数据成功", "1", "1", request);
        } catch (Exception e) {
            logger.error("ExportController.exportCsbackresumeData()", e);
            operLogService.addLog("备份和恢复", "导出备份记录数据失败", "1", "2", request);
            throw new AppException("导出失败");
        }
    }
    
    /**
     * @Description:云硬盘管理导出数据
     * @param request
     * @param response
     */
    @RequestMapping(value = "/cloudDiskdata")
    public void exportCloudDiskData(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("ExportController.exportCloudDiskData()");
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json; charset=utf-8");
            String[][] columns = new String[][] { {"云硬盘名", "Name" }, {"硬盘大小", "Disk_name" },
                    {"磁盘类型", "Type_name" }, {"用户", "Account" }, {"创建时间", "CreateTime" }
                    , {"运行状态", "Runningstatus_name" } , {"使用状态", "Status_name" } };
            ExportExcelUtils.export(request, response,
                    cloudDiskService.getAllCloudDisk(new CloudDisk()), "云硬盘管理", columns,
                    CloudDisk.class);
            operLogService.addLog("云硬盘管理", "导出云硬盘数据成功", "1", "1", request);
        } catch (Exception e) {
            logger.error("ExportController.exportCloudDiskData()", e);
            operLogService.addLog("云硬盘管理", "导出云硬盘数据失败", "1", "2", request);
            throw new AppException("导出失败");
        }
    }
    
    /**
     * @Description:专属云管理导出数据
     * @param request
     * @param response
     */
    @RequestMapping(value = "/vpcdata")
    public void exportVpcData(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("ExportController.exportVpcData()");
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json; charset=utf-8");
            String[][] columns = new String[][] { {"专属云名称", "DisplayName" }, {"IP个数", "IpAmount" },
                    {"主机个数", "HostAmount" }, {"描述", "Description" }, {"创建时间", "CraeteTimeFormat" },
                    {"状态", "StatusFormat" } };
            ExportExcelUtils.export(request, response, vpcService.getAllVpc(), "专属云管理", columns, VpcBaseInfoVO.class);
            operLogService.addLog("专属云管理", "导出专属云数据成功", "1", "1", request);
        } catch (Exception e) {
            logger.error("ExportController.exportVpcData()", e);
            operLogService.addLog("专属云管理", "导出专属云数据失败", "1", "2", request);
            throw new AppException("导出失败");
        }
    }
    
    /**
     * @Description:租户配置管理导出数据
     * @param request
     * @param response
     */
    @RequestMapping(value = "/tenantdata")
    public void exportTenantData(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("ExportController.exportTenantData()");
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json; charset=utf-8");
            String[][] columns = new String[][] { {"租户名称", "Name" }, {"用户量", "UseLevel" }, {"VCPU", "Cpu_name" },
                    {"内存", "MemStr_name" }, {"磁盘总量", "DiskStr_name" }, {"描述", "Remark" } };
            TransFormLoginInfo loginInfo = TransFormLoginHelper.getLoginInfo(request);
            List<SysTenant> list = new ArrayList<SysTenant>();
            if (loginInfo.getUserType() == 0) {
                list = tenantService.getAllSysTenant(new SysTenant());
            } else {
                list = tenantService.getAllSysTenantByUserId(loginInfo.getBillid());
            }
            ExportExcelUtils.export(request, response, list, "租户配置管理", columns, SysTenant.class);
            operLogService.addLog("租户配置管理", "导出租户配置数据成功", "1", "1", request);
        } catch (Exception e) {
            logger.error("ExportController.exportTenantData()", e);
            operLogService.addLog("租户配置管理", "导出租户配置数据失败", "1", "2", request);
            throw new AppException("导出失败");
        }
    }
    
    /**
     * @Description:反馈信息管理导出数据
     * @param request
     * @param response
     */
    @RequestMapping(value = "/clientmessagedata")
    public void exportClientmessageData(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("ExportController.exportClientmessageData()");
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json; charset=utf-8");
            String[][] columns = new String[][] { {"用户", "UserName" }, {"内容", "Content" }, {"类型", "TypeFormat" },
                    {"状态", "StatusFormat" }, {"反馈时间", "CreateTimeFormat" } };
            ExportExcelUtils.export(request, response, clientMessageService.getAll(null), "反馈信息管理", columns,
                    ClientMessageVO.class);
            operLogService.addLog("反馈信息管理", "导出反馈信息数据成功", "1", "1", request);
        } catch (Exception e) {
            logger.error("ExportController.exportClientmessageData()", e);
            operLogService.addLog("反馈信息管理", "导出反馈信息数据失败", "1", "2", request);
            throw new AppException("导出失败");
        }
    }
    
    /**
     * @Description:客户端消息推送导出数据
     * @param request
     * @param response
     */
    @RequestMapping(value = "/terminalinfodata")
    public void exportTerminalinfoData(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("ExportController.exportTerminalinfoData()");
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json; charset=utf-8");
            String[][] columns = new String[][] { {"标题", "Title" }, {"时间", "CreateDate" }, {"地区", "Region" },
                    {"行业", "Industry" }, {"内容", "Content" } };
            ExportExcelUtils.export(request, response, terminalInformationPushService.queryAll(), "消息记录", columns,
                    TerminalInformationPushVO.class);
            operLogService.addLog("客户端消息推送", "导出消息记录数据成功", "1", "1", request);
        } catch (Exception e) {
            logger.error("ExportController.exportTerminalinfoData()", e);
            operLogService.addLog("客户端消息推送", "导出消息记录数据失败", "1", "2", request);
            throw new AppException("导出失败");
        }
    }
    
    /**
     * @Description:云桌面Qos设置导出数据
     * @param request
     * @param response
     */
    @RequestMapping(value = "/desktopqosdata")
    public void exportDesktopQosData(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("ExportController.exportDesktopQosData()");
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json; charset=utf-8");
            String[][] columns = new String[][] { {"名称", "Name" }, {"云主机IP", "Ip" }, {"所属服务器", "ServerIp" },
                    {"带宽", "Bandwidth" }, {"硬盘", "MaxIops_name" }, {"VPCU优先级", "Priority_name" },
                    {"创建时间", "CreateTimeDate" } };
            ExportExcelUtils.export(request, response, qosService.getAll(AppConstant.HOST_TYPE_DESKTOP), "Qos设置",
                    columns, QosVO.class);
            operLogService.addLog("Qos设置", "导出Qos设置数据成功", "1", "1", request);
        } catch (Exception e) {
            logger.error("ExportController.exportDesktopQosData()", e);
            operLogService.addLog("Qos设置", "导出Qos设置数据失败", "1", "2", request);
            throw new AppException("导出失败");
        }
    }
}

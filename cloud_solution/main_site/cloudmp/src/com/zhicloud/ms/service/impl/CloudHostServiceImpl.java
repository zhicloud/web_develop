
/**
 * Project Name:CloudDeskTopMS
 * File Name:CloudHostServiceImpl.java
 * Package Name:com.zhicloud.ms.service.impl
 * Date:2015年3月16日上午11:04:10
 * 
 *
*/
/**
 * Project Name:CloudDeskTopMS
 * File Name:CloudHostServiceImpl.java
 * Package Name:com.zhicloud.ms.service.impl
 * Date:2015年3月16日上午11:04:10
 * 
 *
 */

package com.zhicloud.ms.service.impl;

 
import com.zhicloud.ms.app.pool.CloudHostData;
import com.zhicloud.ms.app.pool.CloudHostPool;
import com.zhicloud.ms.app.pool.CloudHostPoolManager;
import com.zhicloud.ms.app.pool.IsoImagePool.IsoImageData;
import com.zhicloud.ms.app.pool.IsoImagePoolManager;
import com.zhicloud.ms.app.pool.computePool.ComputeInfoExt;
import com.zhicloud.ms.app.pool.computePool.ComputeInfoPool;
import com.zhicloud.ms.app.pool.computePool.ComputeInfoPoolManager;
import com.zhicloud.ms.app.pool.hostMonitorInfoPool.HostMonitorInfo;
import com.zhicloud.ms.app.pool.hostMonitorInfoPool.HostMonitorInfoManager;
import com.zhicloud.ms.constant.AppConstant;
import com.zhicloud.ms.constant.AppInconstant;
import com.zhicloud.ms.exception.AppException;
import com.zhicloud.ms.exception.MyException;
import com.zhicloud.ms.httpGateway.HttpGatewayAsyncChannel;
import com.zhicloud.ms.httpGateway.HttpGatewayChannelExt;
import com.zhicloud.ms.httpGateway.HttpGatewayManager;
import com.zhicloud.ms.httpGateway.HttpGatewayResponseHelper;
import com.zhicloud.ms.mapper.*;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.ICloudHostService;
import com.zhicloud.ms.service.ICloudHostWarehouseService;
import com.zhicloud.ms.service.IOperLogService; 
import com.zhicloud.ms.transform.util.TransFormLoginHelper;
import com.zhicloud.ms.transform.util.TransFormLoginInfo;
import com.zhicloud.ms.util.CapacityUtil;
import com.zhicloud.ms.util.DateUtil;
import com.zhicloud.ms.util.FlowUtil;
import com.zhicloud.ms.util.NumberUtil;
import com.zhicloud.ms.util.RegionHelper;
import com.zhicloud.ms.util.RegionHelper.RegionData;
import com.zhicloud.ms.util.StringUtil;
import com.zhicloud.ms.util.json.JSONLibUtil;
import com.zhicloud.ms.vo.CloudHostConfigModel;
import com.zhicloud.ms.vo.CloudHostVO;
import com.zhicloud.ms.vo.CloudHostWarehouse;
import com.zhicloud.ms.vo.OperLogVO;
import com.zhicloud.ms.vo.SysDiskImageVO; 
import com.zhicloud.ms.vo.SysTenant;
import com.zhicloud.ms.vo.SysUser;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.math.BigInteger;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.text.ParseException;
import java.util.*;


  /**
 * ClassName: CloudHostServiceImpl 
 * Function: 云主机接口的实现层. 
 *  
 * date: 2015年3月16日 上午11:04:10 
 *
 * @author sasa
 * @version 
 * @since JDK 1.7
 */ 
@Transactional(readOnly=false)
public class CloudHostServiceImpl implements ICloudHostService {
    
    private static final Logger logger = Logger.getLogger(UserServiceImpl.class); 
    private SqlSession sqlSession;
    @Resource
    private IOperLogService operLogService;
    @Resource
    ICloudHostWarehouseService cloudHostWarehouseService;
    
    //资源池最大并发创建数存储常量
    public static List<CloudHostWarehouse> maxconcurrent_lists = null;
    
    public SqlSession getSqlSession() {
        return sqlSession;
    }

    public void setSqlSession(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public static Logger getLogger() {
        return logger;
    }
    
    /**
     * 为终端提供主机列表.
     * @see com.zhicloud.ms.service.ICloudHostService#queryCloudHostForTerminal(com.zhicloud.ms.vo.SysUser, boolean)
     */
    @Override
    public List<Map<Object, Object>> queryCloudHostForTerminal(SysUser user,boolean isInnerFlag) {
        if(user != null){
            CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
            Map<String,Object> data = new LinkedHashMap<String,Object>(); 
            data.put("userId", user.getId());
            //返回查询结果
            List<CloudHostVO> hostList = cloudHostMapper.getCloudHost(data);
            
            // 返回的云主机列表
            List<Map<Object, Object>> hostDatas = new ArrayList<Map<Object, Object>>();
            for (CloudHostVO cloudHost : hostList)
            {
                if ("创建中".equals(cloudHost.getSummarizedStatusText())||"创建失败".equals(cloudHost.getSummarizedStatusText())) 
                {
                    continue;
                }
                if("停机".equals(cloudHost.getSummarizedStatusText()) && cloudHost.getInactivateTime()!=null)
                {
                    Date date = null;
                    try {
                        date = StringUtil.stringToDate(cloudHost.getInactivateTime(),"yyyyMMddHHmmssSSS");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    calendar.add(Calendar.DAY_OF_MONTH,7);
                    String lastTime = StringUtil.dateToString(calendar.getTime(),"yyyyMMddHHmmssSSS");
                    String nowTime = StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS");
                    if(Long.parseLong(nowTime) - Long.parseLong(lastTime) > 0)
                    {
                        continue;
                    }
                }
                Map<Object, Object> hostData = new LinkedHashMap<Object, Object>();
                hostData.put("uuid",             cloudHost.getId());                                      
                hostData.put("host_name",        cloudHost.getDisplayName()); 
                if (cloudHost.getDisplayName()==null) 
                {
                    hostData.put("host_name",        cloudHost.getHostName());
                }
                hostData.put("account",          cloudHost.getAccount());
                hostData.put("password",         cloudHost.getPassword());
                hostData.put("cpu_core",         cloudHost.getCpuCore());                                    
                hostData.put("cpu_usage",        0);
                hostData.put("memory",           cloudHost.getMemory());        
                hostData.put("memory_usage",     0); 
                hostData.put("sys_disk",         cloudHost.getSysDisk());
                hostData.put("sys_disk_usage",   0);
                hostData.put("data_disk",        cloudHost.getDataDisk());
                hostData.put("data_disk_usage",  0);
                hostData.put("bandwidth",        cloudHost.getBandwidth()); 
                if(cloudHost.getRunningStatus() == AppConstant.CLOUD_HOST_RUNNING_STATUS_FAULT){
                    hostData.put("running_status",   2);                           
                }else{
                    hostData.put("running_status",   cloudHost.getRunningStatus());                           
                }
                hostData.put("status",           cloudHost.getStatus());
                if(isInnerFlag){                    
                    hostData.put("ip",         cloudHost.getInnerIp());                                 
                    hostData.put("port",       cloudHost.getInnerPort());                               
                }else{
                    hostData.put("ip",         cloudHost.getOuterIp());                                 
                    hostData.put("port",       cloudHost.getOuterPort());                                                   
                }
                hostData.put("sysImageName",     cloudHost.getSysImageName());
                if (cloudHost.getSysImageName()==null)
                {
                    hostData.put("sysImageName",     cloudHost.getSysImageNameOld());
                }  
                
 
                
                hostDatas.add(hostData);
            }
            
            return hostDatas;
        }
        
        return null;

    }

    /**
     * 通过主机id查询主机.
     * @see com.zhicloud.ms.service.ICloudHostService#queryCloudHostById(java.lang.String)
     */
    @Override
    public CloudHostVO queryCloudHostById(String cloudHostId) {
        CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
        Map<String,Object> data = new LinkedHashMap<String,Object>(); 
        data.put("id", cloudHostId);
        //返回查询结果
        CloudHostVO host = cloudHostMapper.getCloudHostById(data);
        
        return host;
    }

    /**
     * 操作主机开机、关机、重启和强制关机的操作.
     * @see com.zhicloud.ms.service.ICloudHostService#operatorCloudHost(java.lang.String, java.lang.String)
     */
    @Override
    public MethodResult operatorCloudHost(String cloudHostId, String operatorType) {
        // 开机
        if("1".equals(operatorType)){
            return this.startCloudHost(cloudHostId);
        }
        // 正常关闭
        if("2".equals(operatorType)){
            return this.stopCloudHost(cloudHostId);
        }
        // 重启
        if("3".equals(operatorType)){
            return this.restartCloudHost(cloudHostId);
        }
        // 强制关机
        if("4".equals(operatorType)){
            return this.haltCloudHost(cloudHostId);
        }
        // 强制重启
        if("5".equals(operatorType)){
            return this.resetCloudHost(cloudHostId);
        }
        return null;
        
    } 
     /**
     * 通过主机真实id，进行开关机操作
    * <p>Title: operatorCloudHostByRealHostId</p> 
    * <p>Description: </p> 
    * @param realHostId
    * @param operatorType
    * @return 
    * @see com.zhicloud.ms.service.ICloudHostService#operatorCloudHostByRealHostId(java.lang.String, java.lang.String)
     */
    public MethodResult operatorCloudHostByRealHostId(String realHostId, String operatorType) {
        // 开机
        if("1".equals(operatorType)){
            return this.startCloudHostByRealHostId(realHostId);
        }
        // 正常关闭
        if("2".equals(operatorType)){
            return this.stopCloudHostByRealHostId(realHostId);
        }
        // 重启
        if("3".equals(operatorType)){
            return this.restartCloudHostByRealHostId(realHostId);
        }
        // 强制关机
        if("4".equals(operatorType)){
            return this.haltCloudHostByRealHostId(realHostId);
        }
        // 强制重启
        if("5".equals(operatorType)){
            return this.resetCloudHostByRealHostId(realHostId);
        }
        return null;
        
    }  
    
    
    /**
     * 
     * haltCloudHost:强制关机.    
     *
     * @author sasa
     * @param hostId
     * @return MethodResult
     * @since JDK 1.7
     */
    @Transactional(readOnly=false)
    public MethodResult haltCloudHost(String hostId) { 
        try {
            CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
            Map<String, Object> data = new LinkedHashMap<String, Object>(); 
            data.put("id", hostId);
            CloudHostVO cloudHost = cloudHostMapper.getCloudHostById(data); 
            if(cloudHost == null){              
                MethodResult result = new MethodResult(MethodResult.FAIL, "not found host info");
                return result;
            }
            String realHostId = cloudHost.getRealHostId();
            if (realHostId == null) {
                return new MethodResult(MethodResult.FAIL, "云主机尚未创建成功");
            }
            CloudHostData myCloudHostData = CloudHostPoolManager.getCloudHostPool().getByRealHostId(cloudHost.getRealHostId());
            if (myCloudHostData != null && myCloudHostData.getRunningStatus() == 1) {
                Map<String, Object> hostData = new LinkedHashMap<String, Object>();
                hostData.put("runningStatus", AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTDOWN);
                hostData.put("realHostId", realHostId);
                cloudHostMapper.updateRunningStatusByRealHostId(hostData); 
                return new MethodResult(MethodResult.SUCCESS, "关机成功");
            }
            HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(cloudHost.getRegion());
            JSONObject haltResullt = channel.hostHalt(realHostId);

            Map<String, Object> cloudHostData = new LinkedHashMap<String, Object>();
            cloudHostData.put("runningStatus", AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTDOWN);
            cloudHostData.put("realHostId", realHostId);
            if (HttpGatewayResponseHelper.isSuccess(haltResullt)) {
                cloudHostMapper.updateRunningStatusByRealHostId(cloudHostData);
                logger.info("CloudHostServiceImpl.haltCloudHost() > "+ cloudHost.getDisplayName()+ "halt host succeeded"); 
                return new MethodResult(MethodResult.SUCCESS, "关机成功");
            } else {
                logger.warn("CloudHostServiceImpl.haltCloudHost() > [" + Thread.currentThread().getId() + "] halt host failed, message:[" + HttpGatewayResponseHelper.getMessage(haltResullt) + "]");
                return new MethodResult(MethodResult.FAIL, "关机失败");
            }
        } catch (ConnectException e) {
            // throw new AppException("连接http gateway失败", e);
            logger.error(e);
            throw new MyException("关机失败");
        } catch (MyException e) {
            logger.error(e);
            throw new MyException("关机失败");
        } catch (Exception e) {
            logger.error(e);
            throw new MyException("关机失败");
        } 
    } 
    /**
     * 
     * @Title: haltCloudHostByRealHostId 
    * @Description: 根据主机真实id强制关闭主机
    * @param @param realHostId
    * @param @return      
    * @return MethodResult     
    * @throws
     */
    @Transactional(readOnly=false)
    public MethodResult haltCloudHostByRealHostId(String realHostId) { 
        try {
            CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class); 
             if (realHostId == null) {
                return new MethodResult(MethodResult.FAIL, "参数错误");
            }
            CloudHostData myCloudHostData = CloudHostPoolManager.getCloudHostPool().getByRealHostId(realHostId);
            if (myCloudHostData != null && myCloudHostData.getRunningStatus() == 1) {
                Map<String, Object> hostData = new LinkedHashMap<String, Object>();
                hostData.put("runningStatus", AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTDOWN);
                hostData.put("realHostId", realHostId);
                cloudHostMapper.updateRunningStatusByRealHostId(hostData); 
                return new MethodResult(MethodResult.SUCCESS, "关机成功");
            }
            HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
            JSONObject haltResullt = channel.hostHalt(realHostId);

            Map<String, Object> cloudHostData = new LinkedHashMap<String, Object>();
            cloudHostData.put("runningStatus", AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTDOWN);
            cloudHostData.put("realHostId", realHostId);
            if (HttpGatewayResponseHelper.isSuccess(haltResullt)) {
                cloudHostMapper.updateRunningStatusByRealHostId(cloudHostData);
                logger.info("CloudHostServiceImpl.haltCloudHost() > "+ realHostId+ "halt host succeeded"); 
                return new MethodResult(MethodResult.SUCCESS, "关机成功");
            } else {
                logger.warn("CloudHostServiceImpl.haltCloudHost() > [" + Thread.currentThread().getId() + "] halt host failed, message:[" + HttpGatewayResponseHelper.getMessage(haltResullt) + "]");
                return new MethodResult(MethodResult.FAIL, "关机失败");
            }
        } catch (ConnectException e) {
            // throw new AppException("连接http gateway失败", e);
            logger.error(e);
            throw new MyException("关机失败");
        } catch (MyException e) {
            logger.error(e);
            throw new MyException("关机失败");
        } catch (Exception e) {
            logger.error(e);
            throw new MyException("关机失败");
        } 
    }
    /**
     * 
     * stopCloudHost:正常关机.   
     *
     * @author sasa
     * @param hostId
     * @return MethodResult
     * @since JDK 1.7
     */
    @Transactional(readOnly=false)
    public MethodResult stopCloudHost(String hostId) {  
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();

        try {
            CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
            Map<String, Object> data = new LinkedHashMap<String, Object>(); 
            data.put("id", hostId);
            CloudHostVO cloudHost = cloudHostMapper.getCloudHostById(data); 
            if(cloudHost == null){              
                operLogService.addLog("云主机", "关闭云主机失败-未找到主机信息", "1", "2", request);
                MethodResult result = new MethodResult(MethodResult.FAIL, "not found host info");
                return result;
            }
            String realHostId = cloudHost.getRealHostId();
            if (realHostId == null) {
                operLogService.addLog("云主机", "关闭云主机"+cloudHost.getDisplayName(), "1", "2", request);
                return new MethodResult(MethodResult.FAIL, "云主机尚未创建成功");
            }
            CloudHostData myCloudHostData = CloudHostPoolManager.getCloudHostPool().getByRealHostId(cloudHost.getRealHostId());
            if (myCloudHostData != null && myCloudHostData.getRunningStatus() == 1) {
                Map<String, Object> hostData = new LinkedHashMap<String, Object>();
                hostData.put("runningStatus", AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTDOWN);
                hostData.put("realHostId", realHostId);
                cloudHostMapper.updateRunningStatusByRealHostId(hostData); 
                operLogService.addLog("云主机", "关闭云主机"+cloudHost.getDisplayName(), "1", "1", request);
                return new MethodResult(MethodResult.SUCCESS, "关机成功");
            }
            HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(cloudHost.getRegion());
            JSONObject stopResullt = channel.hostStop(realHostId);

            //设置数据库主机状态为关机中
//            Map<String, Object> cloudHostData = new LinkedHashMap<String, Object>();
//            cloudHostData.put("runningStatus", AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTINGDOWM);
//            cloudHostData.put("realHostId", realHostId);
//            cloudHostMapper.updateRunningStatusByRealHostId(cloudHostData); 
            if (HttpGatewayResponseHelper.isSuccess(stopResullt)) {
                //更新缓存主机状态
//                CloudHostData newCloudHostData = myCloudHostData.clone();
//                newCloudHostData.setRunningStatus(AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTINGDOWM);
//                newCloudHostData.setLastStatus(AppConstant.CLOUD_HOST_RUNNING_STATUS_RUNNING);
//                newCloudHostData.setLastOperTime(StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
//                CloudHostPoolManager.getCloudHostPool().put(newCloudHostData);
                logger.info("CloudHostServiceImpl.haltCloudHost() > [" + Thread.currentThread().getId() + "] halt host succeeded");
                operLogService.addLog("云主机", "关闭云主机"+cloudHost.getDisplayName(), "1", "1", request);
                return new MethodResult(MethodResult.SUCCESS, "关机命令发送成功");
            } else {
                operLogService.addLog("云主机", "关闭云主机"+cloudHost.getDisplayName(), "1", "2", request);
                logger.warn("CloudHostServiceImpl.haltCloudHost() > [" + Thread.currentThread().getId() + "] halt host failed, message:[" + HttpGatewayResponseHelper.getMessage(stopResullt) + "]");
                return new MethodResult(MethodResult.FAIL, "关机失败");
            }
        } catch (ConnectException e) {
            // throw new AppException("连接http gateway失败", e);
            logger.error(e);
            throw new MyException("关机失败");
        } catch (MyException e) {
            // throw e;
            logger.error(e);
            throw new MyException("关机失败");
        } catch (Exception e) {
            logger.error(e);
            throw new MyException("关机失败");
        } 
    }
    /**
     * 
    * @Title: stopCloudHostByRealHostId 
    * @Description: 根据真实id关闭主机
    * @param @return      
    * @return MethodResult     
    * @throws
     */
    @Transactional(readOnly=false)
    public MethodResult stopCloudHostByRealHostId(String realHostId) {   
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        try {
            CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class); 
             if (realHostId == null) {
                 operLogService.addLog("云主机", "关闭云主机失败-未找到主机信息", "1", "2", request);
                return new MethodResult(MethodResult.FAIL, "参数不正确");
            }
            CloudHostData myCloudHostData = CloudHostPoolManager.getCloudHostPool().getByRealHostId(realHostId);
            if (myCloudHostData != null && myCloudHostData.getRunningStatus() == 1) {
                Map<String, Object> hostData = new LinkedHashMap<String, Object>();
                hostData.put("runningStatus", AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTDOWN);
                hostData.put("realHostId", realHostId);
                cloudHostMapper.updateRunningStatusByRealHostId(hostData);
                operLogService.addLog("云主机", "关闭云主机", "1", "1", request);
                return new MethodResult(MethodResult.SUCCESS, "关机成功");
            }
            HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
            JSONObject stopResullt = channel.hostStop(realHostId);

//            Map<String, Object> cloudHostData = new LinkedHashMap<String, Object>();
//            cloudHostData.put("runningStatus", AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTINGDOWM);
//            cloudHostData.put("realHostId", realHostId);
//            cloudHostMapper.updateRunningStatusByRealHostId(cloudHostData); 
            if (HttpGatewayResponseHelper.isSuccess(stopResullt)) {
              //更新缓存主机状态
//                CloudHostData newCloudHostData = myCloudHostData.clone();
//                newCloudHostData.setRunningStatus(AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTINGDOWM);
//                newCloudHostData.setLastStatus(AppConstant.CLOUD_HOST_RUNNING_STATUS_RUNNING);
//                newCloudHostData.setLastOperTime(StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
//                CloudHostPoolManager.getCloudHostPool().put(newCloudHostData);
                
                logger.info("CloudHostServiceImpl.haltCloudHost() > [" + Thread.currentThread().getId() + "] halt host succeeded");
                operLogService.addLog("云主机", "关闭云主机", "1", "1", request);
                return new MethodResult(MethodResult.SUCCESS, "关机命令发送成功");
            } else {
                operLogService.addLog("云主机", "关闭云主机", "1", "2", request);
                logger.warn("CloudHostServiceImpl.haltCloudHost() > [" + Thread.currentThread().getId() + "] halt host failed, message:[" + HttpGatewayResponseHelper.getMessage(stopResullt) + "]");
                return new MethodResult(MethodResult.FAIL, "关机失败");
            }
        } catch (ConnectException e) {
            // throw new AppException("连接http gateway失败", e);
            logger.error(e);
            throw new MyException("关机失败");
        } catch (MyException e) {
            // throw e;
            logger.error(e);
            throw new MyException("关机失败");
        } catch (Exception e) {
            logger.error(e);
            throw new MyException("关机失败");
        } 
    }
    /**
     * 
     * restartCloudHost:根据主机ID重启
     *
     * @author sasa
     * @param hostId
     * @return MethodResult
     * @since JDK 1.7
     */
    @Transactional(readOnly=false)
    public MethodResult restartCloudHost(String hostId) {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();

        try {
            CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
            Map<String, Object> data = new LinkedHashMap<String, Object>(); 
            data.put("id", hostId);
            CloudHostVO cloudHost = cloudHostMapper.getCloudHostById(data); 
            if(cloudHost == null){              
                operLogService.addLog("云主机", "重启云主机失败，未找到主机相关信息", "1", "2", request);
                MethodResult result = new MethodResult(MethodResult.FAIL, "not found host info");
                return result;
            }
            String realHostId = cloudHost.getRealHostId();
            if (realHostId == null) {
                operLogService.addLog("云主机", "重启云主机"+cloudHost.getDisplayName()+"失败，主机尚未创建成功", "1", "2", request);
                MethodResult result = new MethodResult(MethodResult.FAIL, "云主机尚未创建成功");
                result.put("hostId", hostId);
                return result;
            }
            Map<String, Object> cloudHostData = new LinkedHashMap<String, Object>();
            CloudHostData myCloudHostData = CloudHostPoolManager.getCloudHostPool().getByRealHostId(cloudHost.getRealHostId());
            cloudHostData.put("realHostId", realHostId);
            if (myCloudHostData != null && myCloudHostData.getRunningStatus() == 1) {
                HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(cloudHost.getRegion());
                JSONObject startResullt = channel.hostStart(realHostId, 0, "");
                if (HttpGatewayResponseHelper.isSuccess(startResullt) == false) {
                    operLogService.addLog("云主机", "重启云主机"+cloudHost.getDisplayName()+"失败", "1", "2", request);

                    cloudHostData.put("runningStatus", AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTDOWN);
                    cloudHostMapper.updateRunningStatusByRealHostId(cloudHostData);
                    logger.warn("CloudHostServiceImpl.restartCloudHost() > [" + Thread.currentThread().getId() + "] start host failed, message:[" + HttpGatewayResponseHelper.getMessage(startResullt) + "]");
                    MethodResult result = new MethodResult(MethodResult.FAIL, "重启失败");
                    result.put("hostId", hostId);
                    return result;
                } 
                operLogService.addLog("云主机", "重启云主机"+cloudHost.getDisplayName()+"成功", "1", "1", request);

                MethodResult result = new MethodResult(MethodResult.SUCCESS, "重启成功");
                result.put("hostId", hostId);
                return result;
            }
            HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(cloudHost.getRegion());

            JSONObject restarResullt = channel.hostRestart(realHostId, 0, "");

            if (HttpGatewayResponseHelper.isSuccess(restarResullt) == false) {
                operLogService.addLog("云主机", "重启云主机"+cloudHost.getDisplayName()+"失败", "1", "2", request);

                logger.warn("CloudHostServiceImpl.restartCloudHost() > [" + Thread.currentThread().getId() + "] halt host failed, message:[" + HttpGatewayResponseHelper.getMessage(restarResullt) + "]");
                MethodResult result = new MethodResult(MethodResult.FAIL, "重启失败");
                result.put("hostId", hostId);
                return result;
            } 
            //数据库设置成重启中
//            cloudHostData.put("runningStatus", AppConstant.CLOUD_HOST_RUNNING_STATUS_RESTARTING);
//            cloudHostMapper.updateRunningStatusByRealHostId(cloudHostData);
            //更新缓存主机状态
//            CloudHostData newCloudHostData = myCloudHostData.clone();
//            newCloudHostData.setRunningStatus(AppConstant.CLOUD_HOST_RUNNING_STATUS_RESTARTING);
//            newCloudHostData.setLastStatus(AppConstant.CLOUD_HOST_RUNNING_STATUS_RUNNING);
//            newCloudHostData.setLastOperTime(StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
//            CloudHostPoolManager.getCloudHostPool().put(newCloudHostData);
            operLogService.addLog("云主机", "重启云主机"+cloudHost.getDisplayName()+"成功", "1", "1", request);
            logger.info("CloudHostServiceImpl.restartCloudHost() > [" + Thread.currentThread().getId() + "] restart host succeeded");
            return new MethodResult(MethodResult.SUCCESS, "重启成功");
        } catch (ConnectException e) {
            // throw new AppException("连接http gateway失败", e);
            logger.error(e);
            throw new MyException("重启失败");
        } catch (MyException e) {
            logger.error(e);
            throw new MyException("重启失败");
        } catch (Exception e) {
            logger.error(e);
            throw new MyException("重启失败");
        }  
    }
    /**
     * 
    * @Title: restartCloudHostByRealHostId 
    * @Description: 根据主机真实id重启
    * @param @param realHostId
    * @param @return      
    * @return MethodResult     
    * @throws
     */
    @Transactional(readOnly=false)
    public MethodResult restartCloudHostByRealHostId(String realHostId) { 
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();

        try {
            CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class); 
            if (realHostId == null) {
                operLogService.addLog("云主机", "重启云主机失败，参数错误", "1", "2", request);
                MethodResult result = new MethodResult(MethodResult.FAIL, "参数传递错误");
                 return result;
            }
            Map<String, Object> cloudHostData = new LinkedHashMap<String, Object>();
            CloudHostData myCloudHostData = CloudHostPoolManager.getCloudHostPool().getByRealHostId(realHostId);
            cloudHostData.put("realHostId", realHostId);
            if (myCloudHostData != null && myCloudHostData.getRunningStatus() == 1) {
                HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
                JSONObject startResullt = channel.hostStart(realHostId, 0, "");
                if (HttpGatewayResponseHelper.isSuccess(startResullt) == false) {
                    cloudHostData.put("runningStatus", AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTDOWN);
                    cloudHostMapper.updateRunningStatusByRealHostId(cloudHostData);
                    logger.warn("CloudHostServiceImpl.restartCloudHost() > [" + Thread.currentThread().getId() + "] start host failed, message:[" + HttpGatewayResponseHelper.getMessage(startResullt) + "]");
                    MethodResult result = new MethodResult(MethodResult.FAIL, "重启失败");
                    operLogService.addLog("云主机", "重启云主机失败", "1", "2", request);

                     return result;
                } 
                operLogService.addLog("云主机", "重启云主机成功", "1", "1", request);
                MethodResult result = new MethodResult(MethodResult.SUCCESS, "重启成功");
                 return result;
            }
            HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);

            JSONObject restarResullt = channel.hostRestart(realHostId, 0, "");

            if (HttpGatewayResponseHelper.isSuccess(restarResullt) == false) {
                //数据库设置成重启中
//                cloudHostData.put("runningStatus", AppConstant.CLOUD_HOST_RUNNING_STATUS_RESTARTING);
//                cloudHostMapper.updateRunningStatusByRealHostId(cloudHostData);
                //更新缓存主机状态
//                CloudHostData newCloudHostData = myCloudHostData.clone();
//                newCloudHostData.setRunningStatus(AppConstant.CLOUD_HOST_RUNNING_STATUS_RESTARTING);
//                newCloudHostData.setLastStatus(AppConstant.CLOUD_HOST_RUNNING_STATUS_RESTARTING);
//                newCloudHostData.setLastOperTime(StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
//                CloudHostPoolManager.getCloudHostPool().put(newCloudHostData);
                
                logger.warn("CloudHostServiceImpl.restartCloudHost() > [" + Thread.currentThread().getId() + "] halt host failed, message:[" + HttpGatewayResponseHelper.getMessage(restarResullt) + "]");
                MethodResult result = new MethodResult(MethodResult.FAIL, "重启失败");
                operLogService.addLog("云主机", "重启云主机失败", "1", "2", request);
                 return result;
            } 
            logger.info("CloudHostServiceImpl.restartCloudHost() > [" + Thread.currentThread().getId() + "] restart host succeeded");
            operLogService.addLog("云主机", "重启云主机成功", "1", "1", request);
            return new MethodResult(MethodResult.SUCCESS, "重启成功");
        } catch (ConnectException e) {
            // throw new AppException("连接http gateway失败", e);
            logger.error(e);
            throw new MyException("重启失败");
        } catch (MyException e) {
            logger.error(e);
            throw new MyException("重启失败");
        } catch (Exception e) {
            logger.error(e);
            throw new MyException("重启失败");
        }  
    }
    
    /**
     * 
     * restartCloudHost:根据主机ID强制重启
     *
     * @author sasa
     * @param hostId
     * @return MethodResult
     * @since JDK 1.7
     */
    @Transactional(readOnly=false)
    public MethodResult resetCloudHost(String hostId) { 
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();

        try {
            CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
            Map<String, Object> data = new LinkedHashMap<String, Object>(); 
            data.put("id", hostId);
            CloudHostVO cloudHost = cloudHostMapper.getCloudHostById(data); 
            if(cloudHost == null){           
                operLogService.addLog("云主机", "强制重启云主机失败", "1", "2", request);
                MethodResult result = new MethodResult(MethodResult.FAIL, "not found host info");
                return result;
            }
            String realHostId = cloudHost.getRealHostId();
            if (realHostId == null) {
                operLogService.addLog("云主机", "强制重启云主机"+cloudHost.getDisplayName()+"失败，主机尚未创建成功", "1", "2", request);
                MethodResult result = new MethodResult(MethodResult.FAIL, "云主机尚未创建成功");
                result.put("hostId", hostId);
                return result;
            }
            Map<String, Object> cloudHostData = new LinkedHashMap<String, Object>();
            CloudHostData myCloudHostData = CloudHostPoolManager.getCloudHostPool().getByRealHostId(cloudHost.getRealHostId());
            cloudHostData.put("realHostId", realHostId); 
            
            HttpGatewayAsyncChannel asyncChannel = HttpGatewayManager.getAsyncChannel(Integer.valueOf(1));

            JSONObject restarResullt = asyncChannel.hostReset(realHostId);

            if (HttpGatewayResponseHelper.isSuccess(restarResullt) == false) {
                logger.warn("CloudHostServiceImpl.restartCloudHost() > [" + Thread.currentThread().getId() + "] reset host failed, message:[" + HttpGatewayResponseHelper.getMessage(restarResullt) + "]");
                MethodResult result = new MethodResult(MethodResult.FAIL, "重启失败");
                result.put("hostId", hostId);
                operLogService.addLog("云主机", "强制重启云主机"+cloudHost.getDisplayName()+"失败", "1", "2", request);
                return result;
            } 
            //数据库设置成重启中
//            cloudHostData.put("runningStatus", AppConstant.CLOUD_HOST_RUNNING_STATUS_RESTARTING);
//            cloudHostMapper.updateRunningStatusByRealHostId(cloudHostData);
            //更新缓存主机状态
//            CloudHostData newCloudHostData = myCloudHostData.clone();
//            newCloudHostData.setRunningStatus(AppConstant.CLOUD_HOST_RUNNING_STATUS_RESTARTING);
//            newCloudHostData.setLastStatus(AppConstant.CLOUD_HOST_RUNNING_STATUS_RUNNING);
//            newCloudHostData.setLastOperTime(StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
//            CloudHostPoolManager.getCloudHostPool().put(newCloudHostData);
             
            logger.info("CloudHostServiceImpl.restartCloudHost() > [" + Thread.currentThread().getId() + "] reset host succeeded");
            operLogService.addLog("云主机", "强制重启云主机"+cloudHost.getDisplayName()+"成功", "1", "1", request);
            return new MethodResult(MethodResult.SUCCESS, "重启成功");
        } catch (ConnectException e) {
            // throw new AppException("连接http gateway失败", e);
            logger.error(e);
            throw new MyException("强制重启失败");
        } catch (MyException e) {
            logger.error(e);
            throw new MyException("强制重启失败");
        } catch (Exception e) {
            logger.error(e);
            throw new MyException("强制重启失败");
        }  
    }
    /**
     * 
    * @Title: restartCloudHostByRealHostId 
    * @Description: 根据主机真实id重启
    * @param @param realHostId
    * @param @return      
    * @return MethodResult     
    * @throws
     */
    @Transactional(readOnly=false)
    public MethodResult resetCloudHostByRealHostId(String realHostId) { 
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();

        try {
            CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class); 
            if (realHostId == null) {
                operLogService.addLog("云主机", "强制重启云主机失败，参数错误", "1", "2", request);
                MethodResult result = new MethodResult(MethodResult.FAIL, "参数传递错误");
                 return result;
            }
            Map<String, Object> cloudHostData = new LinkedHashMap<String, Object>();
            CloudHostData myCloudHostData = CloudHostPoolManager.getCloudHostPool().getByRealHostId(realHostId);
            cloudHostData.put("realHostId", realHostId); 
            HttpGatewayAsyncChannel asyncChannel = HttpGatewayManager.getAsyncChannel(Integer.valueOf(1));

            JSONObject restarResullt = asyncChannel.hostReset(realHostId);

            if (HttpGatewayResponseHelper.isSuccess(restarResullt) == false) {
                //数据库设置成重启中
//                cloudHostData.put("runningStatus", AppConstant.CLOUD_HOST_RUNNING_STATUS_RESTARTING);
//                cloudHostMapper.updateRunningStatusByRealHostId(cloudHostData);
                //更新缓存主机状态
//                CloudHostData newCloudHostData = myCloudHostData.clone();
//                newCloudHostData.setRunningStatus(AppConstant.CLOUD_HOST_RUNNING_STATUS_RESTARTING);
//                newCloudHostData.setLastStatus(AppConstant.CLOUD_HOST_RUNNING_STATUS_RESTARTING);
//                newCloudHostData.setLastOperTime(StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
//                CloudHostPoolManager.getCloudHostPool().put(newCloudHostData);
                
                logger.warn("CloudHostServiceImpl.restartCloudHost() > [" + Thread.currentThread().getId() + "] reset host failed, message:[" + HttpGatewayResponseHelper.getMessage(restarResullt) + "]");
                operLogService.addLog("云主机", "强制重启云主机失败", "1", "2", request);

                MethodResult result = new MethodResult(MethodResult.FAIL, "强制重启失败");
                 return result;
            } 
            logger.info("CloudHostServiceImpl.restartCloudHost() > [" + Thread.currentThread().getId() + "] reset host succeeded");
            operLogService.addLog("云主机", "强制重启云主机成功", "1", "1", request);

            return new MethodResult(MethodResult.SUCCESS, "强制重启成功");
        } catch (ConnectException e) {
            // throw new AppException("连接http gateway失败", e);
            logger.error(e);
            throw new MyException("重启失败");
        } catch (MyException e) {
            logger.error(e);
            throw new MyException("重启失败");
        } catch (Exception e) {
            logger.error(e);
            throw new MyException("重启失败");
        }  
    }
    
    /**
     * 
     * startCloudHost:根据主机ID开机
     *
     * @author sasa
     * @param hostId
     * @return MethodResult
     * @since JDK 1.7
     */
    @Transactional(readOnly=false)
    public MethodResult startCloudHost(String hostId) {
    	HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        OperLogVO operLog = new OperLogVO();
        operLog.setId(StringUtil.generateUUID());
        operLog.setModule("云主机");
        operLog.setOperTime(StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
        try {
            CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
            Map<String, Object> data = new LinkedHashMap<String, Object>(); 
            data.put("id", hostId);
            CloudHostVO cloudHost = cloudHostMapper.getCloudHostById(data); 
            if(cloudHost == null){ 
                operLog.setContent("启动云主机失败-找不到相应云主机");
                operLog.setStatus(2);//1 ：成功 2：失败 3：异步
                operLogService.addLog(operLog, request);
                MethodResult result = new MethodResult(MethodResult.FAIL, "not found host info");
                return result;
            }
            String realHostId = cloudHost.getRealHostId();
            if (realHostId == null) {
                operLog.setContent("启动云主机失败-云主机尚未创建成功");
                operLog.setStatus(2);//1 ：成功 2：失败 3：异步
                operLogService.addLog(operLog, request);
                MethodResult result = new MethodResult(MethodResult.FAIL, "云主机尚未创建成功");
                result.put("hostId", hostId);
                return result;
            }
            CloudHostData myCloudHostData = CloudHostPoolManager.getCloudHostPool().getByRealHostId(cloudHost.getRealHostId());
            if (myCloudHostData != null && myCloudHostData.getRunningStatus() == 2) {
                Map<String, Object> hostData = new LinkedHashMap<String, Object>();
                hostData.put("runningStatus", AppConstant.CLOUD_HOST_RUNNING_STATUS_RUNNING);
                hostData.put("realHostId", realHostId);
                cloudHostMapper.updateRunningStatusByRealHostId(hostData); 
                MethodResult result = new MethodResult(MethodResult.SUCCESS, "启动成功");
                result.put("hostId", hostId);
                operLog.setContent("启动云主机成功"+cloudHost.getDisplayName());
                operLog.setStatus(1);//1 ：成功 2：失败 3：异步
                operLogService.addLog(operLog, request);
                return result;
            }
            HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(cloudHost.getRegion());
            JSONObject startResullt = channel.hostStart(realHostId, 0, "");

            Map<String, Object> cloudHostData = new LinkedHashMap<String, Object>();
            cloudHostData.put("runningStatus", AppConstant.CLOUD_HOST_RUNNING_STATUS_RUNNING);
            cloudHostData.put("realHostId", realHostId);
            if (HttpGatewayResponseHelper.isSuccess(startResullt)) { 
                //更新缓存主机状态
                CloudHostData newCloudHostData = myCloudHostData.clone();
                newCloudHostData.setRunningStatus(AppConstant.CLOUD_HOST_RUNNING_STATUS_RUNNING);
                newCloudHostData.setLastStatus(AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTDOWN);
                newCloudHostData.setLastOperTime(StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
                CloudHostPoolManager.getCloudHostPool().put(newCloudHostData);
                
                cloudHostMapper.updateRunningStatusByRealHostId(cloudHostData);
                logger.info("CloudHostServiceImpl.startCloudHost() > [" + Thread.currentThread().getId() + "] start host succeeded");
                MethodResult result = new MethodResult(MethodResult.SUCCESS, "启动成功");
                result.put("hostId", hostId);
                operLog.setContent("启动云主机成功"+cloudHost.getDisplayName());
                operLog.setStatus(1);//1 ：成功 2：失败 3：异步
                operLogService.addLog(operLog, request);
                return result;
            } else {
                logger.warn("CloudHostServiceImpl.startCloudHost() > start host failed, message:[" + HttpGatewayResponseHelper.getMessage(startResullt) + "]");
                MethodResult result = new MethodResult(MethodResult.FAIL, "启动失败");
                result.put("hostId", hostId);
                operLog.setContent("启动云主机成功"+cloudHost.getDisplayName());
                operLog.setStatus(2);//1 ：成功 2：失败 3：异步
                operLogService.addLog(operLog, request);
                return result;
            }
        } catch (ConnectException e) {
            logger.error(e);
            throw new MyException("启动失败");
        } catch (MyException e) {
            logger.error(e);
            throw new MyException("启动失败");
        } catch (Exception e) {
            logger.error(e);
            throw new MyException("启动失败");
        }  
    }
    /**
     * 
    * @Title: startCloudHostByRealHostId 
    * @Description: 通过真实ID启动云主机
    * @param @param realHostId
    * @param @return      
    * @return MethodResult     
    * @throws
     */
    @Transactional(readOnly=false)
    private MethodResult startCloudHostByRealHostId(String realHostId) {
        CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        try { 
             if (realHostId == null) {
                 operLogService.addLog("云主机", "启动主机", "1", "2", request);
                MethodResult result = new MethodResult(MethodResult.FAIL, "参数不正确");
                 return result;
            }
            CloudHostData myCloudHostData = CloudHostPoolManager.getCloudHostPool().getByRealHostId(realHostId);
            if (myCloudHostData != null && myCloudHostData.getRunningStatus() == 2) {
                 Map<String, Object> hostData = new LinkedHashMap<String, Object>();
                hostData.put("runningStatus", AppConstant.CLOUD_HOST_RUNNING_STATUS_RUNNING);
                hostData.put("realHostId", realHostId);
                cloudHostMapper.updateRunningStatusByRealHostId(hostData); 
                MethodResult result = new MethodResult(MethodResult.SUCCESS, "启动成功");
                operLogService.addLog("云主机", "启动主机", "1", "1", request);
                return result;
            }
            HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
            JSONObject startResullt = channel.hostStart(realHostId, 0, "");

            Map<String, Object> cloudHostData = new LinkedHashMap<String, Object>();
            cloudHostData.put("runningStatus", AppConstant.CLOUD_HOST_RUNNING_STATUS_RUNNING);
            cloudHostData.put("realHostId", realHostId);
            if (HttpGatewayResponseHelper.isSuccess(startResullt)) {
                cloudHostMapper.updateRunningStatusByRealHostId(cloudHostData);
              //更新缓存主机状态
                CloudHostData newCloudHostData = myCloudHostData.clone();
                newCloudHostData.setRunningStatus(AppConstant.CLOUD_HOST_RUNNING_STATUS_RUNNING);
                newCloudHostData.setLastStatus(AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTDOWN);
                newCloudHostData.setLastOperTime(StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
                CloudHostPoolManager.getCloudHostPool().put(newCloudHostData);
                
                logger.info("CloudHostServiceImpl.startCloudHost() > [" + Thread.currentThread().getId() + "] start host succeeded");
                
                operLogService.addLog("云主机", "启动主机", "1", "1", request);
                MethodResult result = new MethodResult(MethodResult.SUCCESS, "启动成功");
                 return result;
            } else {
                operLogService.addLog("云主机", "启动主机", "1", "2", request);
                logger.warn("CloudHostServiceImpl.startCloudHost() > start host failed, message:[" + HttpGatewayResponseHelper.getMessage(startResullt) + "]");
                MethodResult result = new MethodResult(MethodResult.FAIL, "启动失败");
                 return result;
            }
        } catch (ConnectException e) {
            logger.error(e);
            throw new MyException("启动失败");
        } catch (MyException e) {
            logger.error(e);
            throw new MyException("启动失败");
        } catch (Exception e) {
            logger.error(e);
            throw new MyException("启动失败");
        }  
    }
    
    
    
    /**
     * 
     * 从http-gateway同步主机信息到数据库.
     * @see com.zhicloud.ms.service.ICloudHostService#fetchNewestCloudHostFromHttpGateway()
     */
    @Transactional(readOnly = false)
    public MethodResult fetchNewestCloudHostFromHttpGateway() {
        MethodResult result = new MethodResult(MethodResult.SUCCESS);
        CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
        JSONObject hostQueryResult = null;
        RegionData[] regionDatas = RegionHelper.singleton.getAllResions();
        for (RegionData regionData : regionDatas) {
            try {
                HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(regionData.getId());
                // 获取默认计算资源池
                JSONObject allPool = channel.computePoolQuery();
                if("fail".equals(allPool.getString("status"))) {
                    return new MethodResult(MethodResult.FAIL, "获取资源失败");
                }
 
                JSONArray computerList = allPool.getJSONArray("compute_pools");

                for (int i = 0; i < computerList.size(); i ++) {
                    JSONObject computerObject = computerList.getJSONObject(i);
                    if (computerObject == null) {// 不能获取，则跳过该region
                        logger.error(String.format("fail to get default compute pool. region[%s].", regionData.getId()));
                        continue;
                    }
                    String poolId = (String) computerObject.get("uuid");
                    // 从http gateway获取所有的云主机
                    try{
                        hostQueryResult = channel.hostQuery(poolId);

                    }catch(Exception e){
                        logger.error(" fail to query hosts info from pool "+poolId);
                        continue;
                    }
                    if (HttpGatewayResponseHelper.isSuccess(hostQueryResult) == false) {// 失败则跳过该region
                        logger.info(String.format("fail to query host from http gateway. region[%s], message[%s]", regionData.getId(), HttpGatewayResponseHelper.getMessage(hostQueryResult)));
                        continue;
                    }
                    
                    JSONArray hosts = (JSONArray) hostQueryResult.get("hosts");
                    // 连接region成功后，维护该region的云主机资源监控信息
                    HostMonitorInfo hostMonitorInfo = HostMonitorInfoManager.singleton().getHostMonitorInfo(regionData.getId());
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
                    for (int j = 0; j < hosts.size(); j++) {
                        JSONObject host = (JSONObject) hosts.get(j);
                        String uuid = JSONLibUtil.getString(host, "uuid");
                        String name = JSONLibUtil.getString(host, "name");
                        
                        if (uuid == null) {// uuid不可以为空
                            logger.warn(String.format("found no uuid host when fetch host list from http gateway. Host name[%s], region[%s].", name, regionData.getId()));
                            continue;
                        }
                        
                        // 更新缓冲池的数据
                        hostList.add(uuid);
                        
                        if (originalHostList != null && originalHostList.contains(uuid)) {
                            // 上次已经处理过
                            continue;
                        } else {
                            isEqual = false;
                        }
                        
                        // 判断数据库中是否已经有了该云主机，没有则开始处理
                        CloudHostVO cloudHost = cloudHostMapper.getByRealHostId(uuid);
                        if (cloudHost == null) {
                            total++;
                            allHostNames.add(name);
                            _handleOrdinaryHostName(regionData.getId(), host);
                        }
//                        CloudHostPoolManager.getSingleton().updateRealCloudHost(regionData.getId(), host,poolId,cloudHost); 
                    }
                    logger.info(String.format("found new host. total[%s]: %s, region:[%s:%s]", total, allHostNames, regionData.getId(), regionData.getName()));
                    
                    if (isEqual && (originalHostList != null && originalHostList.size() != hostList.size())) {
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
                }   
            } catch (SocketException e) { 
                logger.error("connect to http gateway failed, exception:[" + e.getMessage() + "], region:[" + String.format("%s:%s", regionData.getId(), regionData.getName()) + "]");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(hostQueryResult);
                throw new MyException(e);
            }
        }

        result.status = MethodResult.SUCCESS;
        return result;
    }
    
    private void _handleOrdinaryHostName(Integer region, JSONObject host) throws MalformedURLException, IOException {
        String realHostId = JSONLibUtil.getString(host, "uuid");
        String hostName = JSONLibUtil.getString(host, "name");

        logger.info("CloudHostServiceImpl.fetchNewestCloudHostFromHttpGateway() > [" + Thread.currentThread().getId() + "] 发现新创建的云主机, name[" + hostName + "], uuid[" + realHostId + "]");

        CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);

        // 查找云主机
        Map<String, Object> condition = new LinkedHashMap<String, Object>(); 
        condition.put("hostName", hostName);
        CloudHostVO cloudHost = cloudHostMapper.getByHostName(condition);
        if (cloudHost == null) {
            logger.warn("CloudHostServiceImpl.fetchNewestCloudHostFromHttpGateway() > [" + Thread.currentThread().getId() + "] 数据库中没有发现名为[" + hostName + "]的云主机");
            return;
        }
        if(cloudHost.getRealHostId()!=null&&cloudHost.getRealHostId().length()>0){
            logger.warn("CloudHostServiceImpl.fetchNewestCloudHostFromHttpGateway() >   db have host [" + hostName + "] uuid [ "+cloudHost.getRealHostId()+"]");
            return;         
        }

        // 从http gateway获取云主机的详细信息
        HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(cloudHost.getRegion());
        JSONObject hostResult = channel.hostQueryInfo(realHostId);

        _handleFoundHost(realHostId, hostResult, cloudHost);
    }

    /**
     * @param realHostId
     *            :
     * @param hostQueryInfoResult
     *            : 调用channel.hostQueryInfo(realHostId)获取的结果
     * @param cloudHost
     *            ：mysql数据库里面对应的记录
     */
    @Transactional(readOnly=false)
    private void _handleFoundHost(String realHostId, JSONObject hostQueryInfoResult, CloudHostVO cloudHost) throws MalformedURLException, IOException {
        CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);     
        CloudHostWarehouseMapper cloudHostWarehouseMapper = this.sqlSession.getMapper(CloudHostWarehouseMapper.class);      
        String hostId = cloudHost.getId();
        String strNow = DateUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS");

        if (HttpGatewayResponseHelper.isSuccess(hostQueryInfoResult) == false) {
            // 失败，不处理
            logger.warn("CloudHostServiceImpl.fetchNewestCloudHostFromHttpGateway() > query host info fail, uuid[" + realHostId + "]");
            return;
        }

        JSONObject hostInfo = (JSONObject) hostQueryInfoResult.get("host");

        BigInteger[] diskVolume = JSONLibUtil.getBigIntegerArray(hostInfo, "disk_volume");
        String[] ip = JSONLibUtil.getStringArray(hostInfo, "ip"); 
        Integer[] displayPort = JSONLibUtil.getIntegerArray(hostInfo, "display_port");
        if(StringUtil.isBlank(cloudHost.getRealHostId())){
            //如何主机没有real_host_id更新仓库可用数量 +1
            Map<String, Object> wareData = new LinkedHashMap<String, Object>();
            wareData.put("id", cloudHost.getWarehouseId());
            cloudHostWarehouseMapper.updateWarehouseRemainByAddOne(wareData);
        }
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
        cloudHostData.put("runningStatus", "1");
        cloudHostData.put("status", "2");
        cloudHostMapper.updateRealHostIdById(cloudHostData);
        
        

        // 更新数据
        cloudHost.setRealHostId(realHostId);
        cloudHost.setSysDisk(diskVolume[0]);
        cloudHost.setDataDisk(diskVolume.length < 2 ? BigInteger.ZERO : diskVolume[1]);
        cloudHost.setInnerIp(ip[0]);
        cloudHost.setInnerPort(displayPort[0]);
        cloudHost.setOuterIp(ip[1]);
        cloudHost.setCreateTime(strNow); 
        //启动新线程来处理负载均衡的主机
        /*if(cloudHost.getType()!=null && cloudHost.getType()==4){
        	Thread t = new Thread(new LoadBalanceRunnable(realHostId,ip[0]));
        	t.setDaemon(false);
        	t.start();
        }*/
        if(cloudHost.getIsAutoStartup() == AppConstant.CLOUD_HOST_IS_AUTO_STARTUP_YES){
            HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
            JSONObject startResullt = channel.hostStart(realHostId, 0, "");

            Map<String, Object> data = new LinkedHashMap<String, Object>();
            data.put("runningStatus", AppConstant.CLOUD_HOST_RUNNING_STATUS_RUNNING);
            data.put("realHostId", realHostId);
            if (HttpGatewayResponseHelper.isSuccess(startResullt)) {
                cloudHostMapper.updateRunningStatusByRealHostId(data);
                CloudHostData myCloudHostData = CloudHostPoolManager.getCloudHostPool().getByRealHostId(cloudHost.getRealHostId());

                if(myCloudHostData == null){
                    myCloudHostData = new CloudHostData();
                }
              //更新缓存主机状态
                CloudHostData newCloudHostData = myCloudHostData.clone();
                newCloudHostData.setRunningStatus(AppConstant.CLOUD_HOST_RUNNING_STATUS_RUNNING);
                newCloudHostData.setLastStatus(AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTDOWN);
                newCloudHostData.setLastOperTime(StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
                CloudHostPoolManager.getCloudHostPool().put(newCloudHostData); 
            } 
        }

    }

    /**
     * 通过仓库ID筛选主机.
     * @see com.zhicloud.ms.service.ICloudHostService#getHostByWareId(java.lang.String)
     */
    @Override
    public List<CloudHostVO> getHostByWareId(String id) {
        CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);     
        Map<String, Object>  data = new LinkedHashMap<String, Object>();
        data.put("warehouseId", id);
        return cloudHostMapper.getCloudHostByWareId(data); 
    }

      /**
       *
       * getHostByTypeId:通过仓库ID以及多个参数筛选主机
       *
       * @author 张翔
       * @param parameter
       * @return MethodResult
       * @since JDK 1.7
       */
      @Override public List<CloudHostVO> getByWarehouseIdAndParams(Map<String, Object> parameter) {
          return this.sqlSession.getMapper(CloudHostMapper.class).queryByWarehouseIdAndParams(parameter);
      }

      /**
     * 选择一个未创建主机实现创建
     * @see com.zhicloud.ms.service.ICloudHostService#createOneCloudHost()
     */
    @Override
    @Transactional(readOnly = false)
    public MethodResult createOneCloudHost() {
        try {
            CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
            /* 循环资源池，获取没有达到最大主机并发创建数的资源池ID */
            JSONArray pools = getAllComputePool();
            if (pools != null && pools.size() > 0) {
                for (int i = 0; i < pools.size(); i++) {
                    JSONObject obj = pools.getJSONObject(i);
                    // 资源池名称必须是以desktop_pool开始的才处理
                    String pool_name = obj.getString("pool_name");
                    if (pool_name != null && pool_name.startsWith("desktop_pool")) {
                        String pool_id = null;
                        // 设置的最大值
                        Integer max_creating = obj.getInt("max_creating");
                        if (max_creating == 0) {
                            pool_id = obj.getString("pool_id");
                        } else {
                            // 正在创建的数量
                            Integer exists_creating = cloudHostMapper.countPoolCreatedHost(obj.getString("pool_id"));
                            if (exists_creating < max_creating) {
                                pool_id = obj.getString("pool_id");
                                logger.info("CloudHostServiceImpl.createOneCloudHost():资源池:【" + pool_id
                                        + "】未达到最大并发创建数,可以创建主机");
                            }
                        }

                        // 创建主机
                        if (pool_id != null) {
                            createHost(pool_id);
                        }
                    }
                }
            }
            return new MethodResult(MethodResult.SUCCESS, "success");

        } catch (SocketException e) {
            logger.error("CloudHostServiceImpl.createOneCloudHost():  connect to http gateway failed ");
            return new MethodResult(MethodResult.FAIL, "failed_to_connect_http_gateway");
        } catch (Exception e) {
            throw new MyException(e);
        }
    }

    /**
     * @Description:发送创建主机的请求
     * @param pool_id 资源池ID
     * @return  MethodResult
     * @throws MalformedURLException
     * @throws IOException
     */
    private MethodResult createHost(String pool_id) throws MalformedURLException, IOException {
        CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
        SysDiskImageMapper sysDiskImageMapper = this.sqlSession.getMapper(SysDiskImageMapper.class);
        // 获取未处理的主机
        CloudHostVO cloudHostVO = cloudHostMapper.getOneUncreatedCloudHostWithPoolID(pool_id);
        if (cloudHostVO == null) {
            logger.info("CloudHostServiceImpl.createOneCloudHost():   no_more_uncreated_host_exsit");
            return new MethodResult(MethodResult.SUCCESS, "no_more_uncreated_host_exsit");
        }
        Integer isUseDataDisk = 1;
        if (cloudHostVO.getDataDisk().compareTo(BigInteger.ZERO) == 0) {
            isUseDataDisk = 0;
        }
        String imageId = "";
        Integer[] options = new Integer[] {1, 1, 0, 0, 1, cloudHostVO.getSupportH264() };
        if (cloudHostVO.getSysImageId() == null) {
            options = new Integer[] {0, 1, 0, 0, 1, cloudHostVO.getSupportH264() };
        } else {
            // 获取订单里的磁盘镜像的信息
            SysDiskImageVO sysDiskImageVO = sysDiskImageMapper.getById(cloudHostVO.getSysImageId());
            if (sysDiskImageVO == null || StringUtil.isBlank(sysDiskImageVO.getRealImageId())) {
                logger.info("OrderInfoServiceImpl.createOneUserOrderedCloudHost():  sys_disk_image_not_found");
                // 磁盘镜像处理失败
                Map<String, Object> data = new LinkedHashMap<String, Object>();
                data.put("id", cloudHostVO.getId());
                data.put("status", 3);// 创建失败
                data.put("createTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
                cloudHostMapper.updateStautsById(data);
                return new MethodResult(MethodResult.FAIL, "sys_disk_image_not_found");
            } else {
                imageId = sysDiskImageVO.getRealImageId();
            }

        }

        // 从云主机仓库获取云主机失败，那么直接创建云主机
        HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(cloudHostVO.getRegion());
        channel.computePoolQuery();

        // 获取默认资源池
        JSONObject computePool = channel.computePoolQuery();
        if (computePool == null) {
            logger.warn("CloudHostServiceImpl.createOneCloudHost():  compute_pool_not_found");
            return new MethodResult(MethodResult.FAIL, "compute_pool_not_found");
        }

        // 发消息创建云主机
        JSONObject hostCreateResult = channel.hostCreate(cloudHostVO.getHostName(), cloudHostVO.getPoolId(),
                cloudHostVO.getCpuCore(), cloudHostVO.getMemory(),
                options, // options
                imageId, new BigInteger[] {cloudHostVO.getSysDisk(), cloudHostVO.getDataDisk() }, new Integer[] {},
                cloudHostVO.getUserId(), "terminal_user", cloudHostVO.getAccount(), cloudHostVO.getPassword(), // 获取之前创建的16位随机密码
                "", cloudHostVO.getBandwidth(), cloudHostVO.getBandwidth());

        if (HttpGatewayResponseHelper.isSuccess(hostCreateResult) == false) {
            logger.info("CloudHostServiceImpl.createOneCloudHost():   create cloud host fail, http gateway return: "
                    + HttpGatewayResponseHelper.getMessage(hostCreateResult));
            // 创建云主机失败
            Map<String, Object> data = new LinkedHashMap<String, Object>();
            data.put("id", cloudHostVO.getId());
            data.put("status", 3);// 创建失败
            data.put("createTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
            cloudHostMapper.updateStautsById(data);
            return new MethodResult(MethodResult.FAIL, "http gateway return: "
                    + HttpGatewayResponseHelper.getMessage(hostCreateResult));
        } else {
            logger.info("CloudHostServiceImpl.createOneCloudHost(): [" + Thread.currentThread().getId()
                    + "] user_not_found");
            Map<String, Object> data = new LinkedHashMap<String, Object>();
            data.put("id", cloudHostVO.getId());
            data.put("status", 1);// 创建中
            data.put("createTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
            cloudHostMapper.updateStautsById(data);
        }

        logger.info("CloudHostServiceImpl.createOneCloudHost(): [" + Thread.currentThread().getId()
                + "] create cloud host suceeded, message:[" + HttpGatewayResponseHelper.getMessage(hostCreateResult)
                + "]");
        return new MethodResult(MethodResult.SUCCESS, "success");

    }
    
    @Override
    public List<CloudHostVO> getAllHost() {
        CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
        List<CloudHostVO> cloudHostList = cloudHostMapper.getCloudHost(null);
        return cloudHostList;
    }

    @Override
    public List<CloudHostVO> getByWarehouseId(String id) {
        CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
        List<CloudHostVO> cloudHostList = cloudHostMapper.getByWarehouseId(id);
        return cloudHostList;
    }

    @SuppressWarnings("static-access")
	@Override
    @Transactional(readOnly=false)
    public MethodResult deleteById(String id) {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();

        CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
        TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
        CloudHostWarehouseMapper cloudHostWarehouseMapper = this.sqlSession.getMapper(CloudHostWarehouseMapper.class);
        Map<String,Object> data = new LinkedHashMap<String,Object>();
        data.put("id", id);
        //返回查询结果
        CloudHostVO host = cloudHostMapper.getCloudHostById(data);
        try {
			if(host == null){
	            operLogService.addLog("云主机", "删除云主机失败，未查找到主机信息", "1", "2", request);
			    return new MethodResult(MethodResult.FAIL, "未查找到主机信息");
			}else{
			    //如果底层存在先删除主机
			    if(!StringUtil.isBlank(host.getRealHostId())){
			        HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(host.getRegion());
			        JSONObject hostDeleteResult;
			            hostDeleteResult = channel.hostDelete(host.getRealHostId());
			            if (HttpGatewayResponseHelper.isSuccess(hostDeleteResult)) {
			                logger.info("CloudHostServiceImpl.deleteByIds() > [" + Thread.currentThread().getId() + "] delete host succeeded, uuid:[" + host.getRealHostId() + "], message:[" + HttpGatewayResponseHelper.getMessage(hostDeleteResult) + "]");
			            } else {
			            	if(hostDeleteResult.getString("message").contains("not exists")){
			            		//真实主机已不存在 啥都不干
			            	}else {
			            		// 删除失败，回滚事务
			            		logger.warn("CloudHostServiceImpl.deleteByIds() > [" + Thread.currentThread().getId() + "] delete host failed, uuid:[" + host.getRealHostId() + "], message:[" + HttpGatewayResponseHelper.getMessage(hostDeleteResult) + "]");
			                    operLogService.addLog("云主机", "删除云主机"+host.getDisplayName()+"失败", "1", "2", request);
			            		return new MethodResult(MethodResult.FAIL, "删除真实云主机失败");
			            	}
			            }
			        //删除资源池的主机
			        CloudHostPoolManager.getSingleton().getCloudHostPool().removeByRealHostId(host.getRealHostId());
			    }
			    // 主机创建成功已经分配
			    if(!StringUtil.isBlank(host.getUserId())){
			        Map<String,Object> userData = new HashMap<String,Object>();
			        userData.put("id", host.getUserId());
			        userData.put("modified_time", DateUtil.dateToString(new Date(),"yyyyMMddHHmmssSSS"));
			        terminalUserMapper.updateCloudHostAmountForDelete(userData); 
			    
			        cloudHostWarehouseMapper.updateWarehouseAmountForDeleteDispatchedHost(host.getWarehouseId());
			    } 
			    //主机正在创建中
			    else if(host.getStatus() == 1){ 
			        if(AppInconstant.cloudHostProgress.get(host.getHostName()) != null){
	                    operLogService.addLog("云主机", "删除云主机"+host.getDisplayName()+"失败", "1", "2", request);
	                    return new MethodResult(MethodResult.FAIL, "主机在创建中，无法删除");  
			        }

			    }
			    //主机创建未成功
			    else if(StringUtil.isBlank(host.getRealHostId())){
			        cloudHostWarehouseMapper.updateWarehouseAmountForDeleteNotCreatedHost(host.getWarehouseId());
			        
			    }else{      
			    //主机创建成功未分配
			        cloudHostWarehouseMapper.updateWarehouseAmountForDeleteHost(host.getWarehouseId()); 
			    }
			}
            cloudHostMapper.deleteById(id);
            // 删除关联的QoS规则
            String[] uuids = new String[1];
            uuids[0] = host.getRealHostId();
            this.sqlSession.getMapper(QosMapper.class).deleteQosByHostUuids(uuids);
            operLogService.addLog("云主机", "删除云主机"+host.getDisplayName()+"成功", "1", "2", request);

			return new MethodResult(MethodResult.SUCCESS, "删除成功");
		} catch (MalformedURLException e) {
			return new MethodResult(MethodResult.FAIL, "删除失败");
		} catch (IOException e) {
			return new MethodResult(MethodResult.FAIL, "删除失败");
		}
    }

    /**
     * 获取所有的主机
     * @see com.zhicloud.ms.service.ICloudHostService#getAllCloudHost()
     */
    @Override
    public List<CloudHostVO> getAllCloudHost() {
        
        CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
        List<CloudHostVO> allCloudHostVO = cloudHostMapper.getAllCloudHost();
        return allCloudHostVO;
    }
    /**
     * 
     * 更新主机的运行状态
     * @see com.zhicloud.ms.service.ICloudHostService#updateRunningStatusByRealHostId(java.util.Map)
     */
    @Transactional(readOnly=false)
    public int updateRunningStatusByRealHostId(Map<String, Object> cloudHostData) {
        CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
        int n = cloudHostMapper.updateRunningStatusByRealHostId(cloudHostData);
        return n;
    }

    /**
     * =根据用户id
     * @see com.zhicloud.ms.service.ICloudHostService#getCloudHostByUserId(java.lang.String)
     */
    @Override
    public List<CloudHostVO> getCloudHostByUserId(String userId) {
        CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
        return cloudHostMapper.getCloudHostByUserId(userId);        
    }

    /**
     * 解除主机与用户关联
     * @see com.zhicloud.ms.service.ICloudHostService#getDisassociationById(java.lang.String)
     */
    @Override
    public MethodResult getDisassociationById(String id) {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
        TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
        CloudHostWarehouseMapper chwMapper = this.sqlSession.getMapper(CloudHostWarehouseMapper.class);
        Map<String,Object> data = new LinkedHashMap<String,Object>(); 
        data.put("id", id);
        CloudHostVO host = cloudHostMapper.getCloudHostById(data);
        if(host == null){
            operLogService.addLog("云主机", "取消关联云主机失败，未找到到主机信息", "1", "2", request);
            return new MethodResult(MethodResult.FAIL, "未找到主机信息");
        }
        // 用户已分配减一
        data.put("id", host.getUserId());
        data.put("modified_time", DateUtil.dateToString(new Date(),"yyyyMMddHHmmssSSS"));
        terminalUserMapper.updateCloudHostAmountForDelete(data);
        //更新仓库可用数量和已分配数量
        data.put("id", host.getWarehouseId());
        chwMapper.updateWarehouseForRetrieveHost(data);
        //更新主机的userid 和assign_time
        data.put("id", id);
        cloudHostMapper.updateCloudHostUserIdById(data);
        operLogService.addLog("云主机", "取消关联云主机"+host.getDisplayName()+"成功", "1", "1", request);
        return new MethodResult(MethodResult.SUCCESS, "取消关联成功");
    }

    /**
     * 批量删除主机
     * @see com.zhicloud.ms.service.ICloudHostService#deleteByIds(java.lang.String)
     */
    @SuppressWarnings("static-access")
	@Override
    @Transactional(readOnly=false)
    public MethodResult deleteByIds(String ids) {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
        TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
        CloudHostWarehouseMapper cloudHostWarehouseMapper = this.sqlSession.getMapper(CloudHostWarehouseMapper.class);
        Map<String,Object> data = new LinkedHashMap<String,Object>();
        String [] hostIds = ids.split(",");
        String status = "";
        String message = "";
        
        for(String id:hostIds){
            data.put("id", id);
            //返回查询结果
            CloudHostVO host = cloudHostMapper.getCloudHostById(data);
            if(host == null){
                return new MethodResult(MethodResult.FAIL, "未查找到主机信息");
            }else{
                //如果底层存在先删除主机
                if(!StringUtil.isBlank(host.getRealHostId())){
                    HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(host.getRegion());
                    JSONObject hostDeleteResult;
                    try {
                        hostDeleteResult = channel.hostDelete(host.getRealHostId());
                        if (HttpGatewayResponseHelper.isSuccess(hostDeleteResult)) {
                            logger.info("CloudHostServiceImpl.deleteByIds() > [" + Thread.currentThread().getId() + "] delete host succeeded, uuid:[" + host.getRealHostId() + "], message:[" + HttpGatewayResponseHelper.getMessage(hostDeleteResult) + "]");
                        } else {
                        	if(hostDeleteResult.getString("message").contains("not exists")){
                        		//真实主机已不存在 啥都不干
                        	}else{
                        		// 删除失败，回滚事务
                        		logger.warn("CloudHostServiceImpl.deleteByIds() > [" + Thread.currentThread().getId() + "] delete host failed, uuid:[" + host.getRealHostId() + "], message:[" + HttpGatewayResponseHelper.getMessage(hostDeleteResult) + "]");
                        		return new MethodResult(MethodResult.FAIL, "删除真实云主机失败");
                        	}
                        }
                    } catch (IOException e) {                       
                        e.printStackTrace();
                        return new MethodResult(MethodResult.FAIL, "删除真实云主机失败");
                        
                    }
                  //删除资源池的主机
                    CloudHostPoolManager.getSingleton().getCloudHostPool().removeByRealHostId(host.getRealHostId());
                }
                // 主机创建成功已经分配
                if(!StringUtil.isBlank(host.getUserId())){
                    Map<String,Object> userData = new HashMap<String,Object>();
                    userData.put("id", host.getUserId());
                    userData.put("modified_time", DateUtil.dateToString(new Date(),"yyyyMMddHHmmssSSS"));
                    terminalUserMapper.updateCloudHostAmountForDelete(userData); 
                
                    cloudHostWarehouseMapper.updateWarehouseAmountForDeleteDispatchedHost(host.getWarehouseId());
                } 
                //主机正在创建中
                else if(host.getStatus() == 1){
                    message = "<br>部分主机正在创建中，无法删除";
                    continue;
                }
                //主机创建未成功
                else if(StringUtil.isBlank(host.getRealHostId())){
                    cloudHostWarehouseMapper.updateWarehouseAmountForDeleteNotCreatedHost(host.getWarehouseId());
                    
                }else{      
                //主机创建成功未分配
                    cloudHostWarehouseMapper.updateWarehouseAmountForDeleteHost(host.getWarehouseId()); 
                }
            }
            cloudHostMapper.deleteById(id);
            // 删除关联的QoS规则
            String[] uuids = new String[1];
            uuids[0] = host.getRealHostId();
            this.sqlSession.getMapper(QosMapper.class).deleteQosByHostUuids(uuids);
            status = "删除成功";
            
        }
        operLogService.addLog("云主机", "批量删除云主机成功", "1", "1", request);
        return new MethodResult(MethodResult.SUCCESS, status+message);
    }

    @Override
    public List<CloudHostVO> getAllServer() {
        CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
        List<CloudHostVO> cloudHostList = cloudHostMapper.getAllServer();
        List<CloudHostVO> hostServer = new ArrayList<CloudHostVO>();
        if(cloudHostList!=null && cloudHostList.size()>0){
            for(CloudHostVO cloudHost : cloudHostList){
                if(cloudHost.getRealHostId()!=null && !StringUtil.isBlank(cloudHost.getRealHostId())){
                    CloudHostData cloudHostData = CloudHostPoolManager.getCloudHostPool().getByRealHostId(cloudHost.getRealHostId());
                    if(cloudHostData != null){
                        cloudHost.setCpuUsage(cloudHostData.getCpuUsage());
                        cloudHost.setMemoryUsage(cloudHostData.getMemoryUsage());
                        cloudHost.setDiskUsage(cloudHostData.getDataDiskUsage());
                    }
                    
                }
                hostServer.add(cloudHost);
            }
        }
        return hostServer;
    }

    @Override
    @Transactional(readOnly=false)
    public MethodResult addServer(CloudHostVO server,HttpServletRequest request) {
 
        try{ 
            TransFormLoginInfo loginInfo = TransFormLoginHelper.getLoginInfo(request);
            CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
            SysDiskImageMapper sysDiskImageMapper = this.sqlSession.getMapper(SysDiskImageMapper.class);
            CloudHostConfigModelMapper chcmMapper = this.sqlSession.getMapper(CloudHostConfigModelMapper.class);
            CloudHostConfigModel chcm = null;
            if(server.getChcmId()!=null){
            	chcm = chcmMapper.getById(server.getChcmId());
            }
            Integer[] options = null;
            String realDiskImageId = null;
            String sysDiskImageName = null;
            BigInteger sysDisk = new BigInteger("10737418240");
            Integer isUseDataDisk = 1;
            Integer isAutoStart = 1;
            Integer supportH264 = 0;
            Integer isUseImage = 1;
            if(chcm != null){
                server.setDataDisk(chcm.getDataDisk());
                server.setCpuCore(chcm.getCpuCore());
                server.setSysImageId(chcm.getSysImageId());
                server.setMemory(chcm.getMemory());
                if(StringUtil.isBlank(server.getSysImageId())){
                    isUseImage = 0;
                }
                SysDiskImageVO sysDiskImageVO = sysDiskImageMapper.getById(server.getSysImageId());
                if(sysDiskImageVO != null){
                   realDiskImageId = sysDiskImageVO.getRealImageId();
                   sysDiskImageName = sysDiskImageVO.getDisplayName(); 
                } 
//                server.setSysDisk(sysDiskImageVO.getSize());
                server.setSysDisk(chcm.getSysDisk());
            }else{
            	server.setDataDisk(CapacityUtil.fromCapacityLabel((server.getDataDisk()==null?server.getDiskdiy():server.getDataDisk())+"GB"));
            	server.setMemory(CapacityUtil.fromCapacityLabel(server.getMemory()+"GB"));
            	if(server.getSysDiskType().equals("from_empty")){
            	    server.setSysDisk(CapacityUtil.fromCapacityLabel(server.getEmptyDisk()+"GB")); 
            	    isUseImage = 0;
                }else{
                    SysDiskImageVO sysDiskImageVO = sysDiskImageMapper.getById(server.getSysImageId());
                    if(sysDiskImageVO != null){
                       realDiskImageId = sysDiskImageVO.getRealImageId();
                       sysDiskImageName = sysDiskImageVO.getDisplayName(); 
                    } 
                    server.setSysDisk(sysDiskImageVO.getSize());
                    isUseImage = 1;
                  }
            }
            if(server.getDataDisk().compareTo(BigInteger.ZERO)==0){
                isUseDataDisk = 0;
            }
            if(server.getIsAutoStartup()==null || server.getIsAutoStartup()!=1){
                isAutoStart = 0;
            } 

            if(server.getSupportH264()!=0){
            	supportH264 = 1;
            } 
            options = new Integer[] { isUseImage, isUseDataDisk, isAutoStart,0,1,supportH264 }; 
            
            

            // 添加云主机进数据库
            if(StringUtil.isBlank(server.getId())){
                server.setId(StringUtil.generateUUID());
            }
            Map<String, Object> cloudHostData = new LinkedHashMap<String, Object>();
            cloudHostData.put("id", server.getId());
            cloudHostData.put("realHostId", null);
            cloudHostData.put("type", 2); //表示服务器主机
            cloudHostData.put("userId", "");
            cloudHostData.put("hostName", StringUtil.generateUUID());
            cloudHostData.put("displayName", server.getDisplayName());
            cloudHostData.put("account", loginInfo.getUsercount());
            cloudHostData.put("password", loginInfo.getUsercount());
            cloudHostData.put("cpuCore", server.getCpuCore());
            cloudHostData.put("memory", server.getMemory());
            cloudHostData.put("sysImageId", server.getSysImageId());
            cloudHostData.put("sysImageName", sysDiskImageName);
            cloudHostData.put("sysDisk", server.getSysDisk());
            cloudHostData.put("dataDisk", server.getDataDisk());
            cloudHostData.put("bandwidth", FlowUtil.fromFlowLabel(server.getBandwidth()+"Mbps"));
            cloudHostData.put("isAutoStartup", server.getIsAutoStartup()==1 ? AppConstant.CLOUD_HOST_IS_AUTO_STARTUP_YES : AppConstant.CLOUD_HOST_IS_AUTO_STARTUP_NO);
            cloudHostData.put("runningStatus", AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTDOWN);
            cloudHostData.put("status", AppConstant.CLOUD_HOST_STATUS_1_NORNAL);
            cloudHostData.put("poolId", server.getPoolId());
 
            int n = cloudHostMapper.insertCloudHost(cloudHostData);
            if (n <= 0) {
                operLogService.addLog("云主机", "创建服务器"+cloudHostData.get("hostName")+"失败", "1", "2", request);

                throw new Exception("添加失败");
            }

            // 从云主机缓冲池里面取，如果 
            try {
                // 添加开放端口
                CloudHostOpenPortMapper cloudHostOpenPortMapper = this.sqlSession.getMapper(CloudHostOpenPortMapper.class);
                List<String> portList = null;
                if(!StringUtil.isBlank(server.getPorts())){
                      String[] arrayStr = new String[] {};// 字符数组
                      arrayStr = server.getPorts().split(",");// 字符串转字符数组
                      portList = java.util.Arrays.asList(arrayStr);// 字符数组转list
                    for (String port : portList) {
                        String[] arr = port.split(":");
                        if (arr.length != 2) {
                            operLogService.addLog("云主机", "创建服务器"+cloudHostData.get("hostName")+"失败", "1", "2", request);

                            throw new Exception("端口格式错误, [" + port + "]");
                        }
                        Map<String, Object> cloudHostOpenPort = new LinkedHashMap<String, Object>();
                        cloudHostOpenPort.put("id", StringUtil.generateUUID());
                        cloudHostOpenPort.put("hostId", server.getId());
                        cloudHostOpenPort.put("protocol", Integer.parseInt(arr[0]));
                        cloudHostOpenPort.put("port", Integer.parseInt(arr[1]));
                        cloudHostOpenPortMapper.addCloudHostOpenPort(cloudHostOpenPort);
                    }
                }
                

                // 发消息到http gateway
                HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);

                // 获取默认资源池
//              JSONObject computePool = channel.getDefaultComputePool();
//              if (computePool == null) {
//                  logger.info("CloudHostServiceImpl.addCloudHost() > [" + Thread.currentThread().getId() + "] 无法获取计算资源池");
//                  throw new Exception("无法获取计算资源池");
//              }

                BigInteger [] disk_array = null ;
                if(server.getDataDisk() == BigInteger.ZERO){
                    disk_array = new BigInteger[] { sysDisk };
                }else{
                    disk_array = new BigInteger[] { sysDisk,server.getDataDisk()};
                }

                JSONObject createHostResult = channel.hostCreate(cloudHostData.get("hostName").toString(), (String) server.getPoolId(), Integer.valueOf(server.getCpuCore()),server.getMemory(), options, realDiskImageId, disk_array,
                        _formatToHttpGatewayFormatPorts(portList), loginInfo.getUsercount(), "T2", loginInfo.getUsercount(), loginInfo.getUsercount(), "", FlowUtil.fromFlowLabel(server.getBandwidth()+"Mbps"), FlowUtil.fromFlowLabel(server.getBandwidth()+"Mbps"));

                if (HttpGatewayResponseHelper.isSuccess(createHostResult) == false) {
                    logger.warn("CloudHostServiceImpl.addCloudHost() > [" + Thread.currentThread().getId() + "] create host '" + server.getDisplayName() + "' failed, message:[" + HttpGatewayResponseHelper.getMessage(createHostResult) + "]");
                    // return new MethodResult(MethodResult.FAIL,
                    // "http gateway创建云主机失败");
                    operLogService.addLog("云主机", "创建服务器"+cloudHostData.get("hostName")+"失败", "1", "2", request);

                    throw new Exception("http gateway创建云主机失败");
                }
                logger.warn("CloudHostServiceImpl.addCloudHost() > [" + Thread.currentThread().getId() + "] create host '" + server.getDisplayName() + "' succeeded, message:[" + HttpGatewayResponseHelper.getMessage(createHostResult) + "]");
            } catch (ConnectException e) {
                operLogService.addLog("云主机", "创建服务器"+cloudHostData.get("hostName")+"失败", "1", "2", request);

                throw new Exception("连接http gateway失败", e);
            }
            operLogService.addLog("云主机", "创建服务器"+cloudHostData.get("hostName")+"成功", "1", "1", request);

            return new MethodResult(MethodResult.SUCCESS, "添加成功");
        }catch(Exception e){
            e.printStackTrace();
            throw new AppException("添加失败");
        }
    }
    
    private Integer[] _formatToHttpGatewayFormatPorts(List<String> ports) {
        List<Integer> result = new ArrayList<Integer>();
        if(ports!=null){
            for (String port : ports) {
                String[] arr = port.split(":");
                result.add(Integer.valueOf(arr[0])); // protocol
                result.add(Integer.valueOf(arr[1])); // port
            }
        }
        
        return result.toArray(new Integer[0]);
    } 

    @Override
    public CloudHostVO getById(String id) {
        CloudHostMapper chMapper = this.sqlSession.getMapper(CloudHostMapper.class);
        CloudHostVO cloudHost = chMapper.getById(id);
        HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
        try {
        	JSONObject result = channel.hostQueryInfo(cloudHost.getRealHostId());
			JSONObject hostInfo = (JSONObject)result.get("host");
			if(hostInfo!=null){
				Integer[] option = JSONLibUtil.getIntegerArray(hostInfo, "option");
				if(option.length==5){
					cloudHost.setSupportH264(option[4]);
				}
			}else{
				cloudHost.setSupportH264(0);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return cloudHost;
    }

    @Override
    @Transactional(readOnly=false)
    public MethodResult modifyAllocation(CloudHostVO server) {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        CloudHostVO cloud = null;
        try {
            // 参数处理
            String bandwidth = "3";
            if(StringUtil.isBlank(server.getCpuCore().toString())){
                operLogService.addLog("云主机", "修改主机配置"+server.getDisplayName()+"失败", "1", "2", request);
                return new MethodResult(MethodResult.FAIL, "请选择CPU核数");
            }
            if(StringUtil.isBlank(server.getMemory().toString())){
                operLogService.addLog("云主机", "修改主机配置"+server.getDisplayName()+"失败", "1", "2", request);
                return new MethodResult(MethodResult.FAIL, "请选择内存大小");
            }
            if(StringUtil.isBlank(bandwidth)){
                operLogService.addLog("云主机", "修改主机配置"+server.getDisplayName()+"失败", "1", "2", request);
                return new MethodResult(MethodResult.FAIL, "请选择带宽大小");
            }
            CloudHostMapper chMapper = this.sqlSession.getMapper(CloudHostMapper.class);
            cloud = chMapper.getById(server.getId());
            HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
            JSONObject result = channel.hostQueryInfo(cloud.getRealHostId());
            //获取主机信息
			JSONObject hostInfo = (JSONObject)result.get("host");
			Integer[] options = new Integer[4];
			options[0] = 1;
			options[1] = 1;
			options[2] = 1; 
			if(hostInfo!=null && server.getSupportH264()!=null){ 
			    options[3] = server.getSupportH264();
			}else{
				options = new Integer[]{};
			}
            JSONObject hostModifyResult = channel.hostModify(cloud.getRealHostId(), "", server.getCpuCore(), CapacityUtil.fromCapacityLabel(server.getMemory()+"GB"), options, new Integer[0], "", "", "", new BigInteger("0"), new BigInteger("0"));
            if (HttpGatewayResponseHelper.isSuccess(hostModifyResult) == false) {
                operLogService.addLog("云主机", "修改主机配置"+cloud.getDisplayName()+"失败", "1", "2", request);
                return new MethodResult(MethodResult.FAIL, "配置修改失败");
            }
            Integer realCpu = server.getCpuCore();
            if(realCpu==null || realCpu==0){
            	realCpu = cloud.getCpuCore();
            }
            Map<String,Object> condition = new LinkedHashMap<String, Object>();
            condition.put("id",server.getId());
            condition.put("cpuCore", realCpu);
            condition.put("supportH264", server.getSupportH264());
            condition.put("memory", CapacityUtil.fromCapacityLabel(server.getMemory()+"GB"));
//          condition.put("bandwidth", FlowUtil.fromFlowLabel(bandwidth+"Mbps"));
            int n = chMapper.updateById(condition);
            if(n > 0){
                operLogService.addLog("云主机", "修改主机配置"+cloud.getDisplayName()+"成功", "1", "1", request);
                return new MethodResult(MethodResult.SUCCESS, "配置修改成功");
            }else{
                operLogService.addLog("云主机", "修改主机配置"+cloud.getDisplayName()+"失败", "1", "2", request);
                return new MethodResult(MethodResult.FAIL, "数据库修改失败");
            }
        } catch(Exception e) {
            operLogService.addLog("云主机", "修改主机配置"+cloud.getDisplayName()+"失败", "1", "2", request);
            return new MethodResult(MethodResult.FAIL, "配置修改失败");
        }
    }

    @Override
    public CloudHostData refreshData(String id) {
        CloudHostData cloudHostData = CloudHostPoolManager.getCloudHostPool().getByRealHostId(id);
        return cloudHostData;
    }

    @Override
    public CloudHostVO getByRealHostId(String realId) {
        CloudHostMapper chMapper = this.sqlSession.getMapper(CloudHostMapper.class);
        CloudHostVO cloudHost = chMapper.getByRealHostId(realId);
        return cloudHost;
    }
    
    /**
     * 根据终端反馈的结果更新缓存和数据库
    * <p>Title: updateRunningStatusFromTerminal</p> 
    * <p>Description: </p> 
    * @param hostId
    * @param runningStatus
    * @return 
    * @see com.zhicloud.ms.service.ICloudHostService#updateRunningStatusFromTerminal(java.lang.String, java.lang.String)
     */
    @Transactional(readOnly=false)
    public MethodResult updateRunningStatusFromTerminal(String hostId, String runningStatus) {

        
        try {
            CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
            Map<String, Object> data = new LinkedHashMap<String, Object>(); 
            data.put("id", hostId);
            CloudHostVO cloudHost = cloudHostMapper.getCloudHostById(data); 
            if(cloudHost == null){              
                MethodResult result = new MethodResult(MethodResult.FAIL, "not found host info");
                return result;
            }
            String realHostId = cloudHost.getRealHostId();
            if (realHostId == null) {
                MethodResult result = new MethodResult(MethodResult.FAIL, "云主机尚未创建成功");
                result.put("hostId", hostId);
                return result;
            }
            CloudHostData myCloudHostData = CloudHostPoolManager.getCloudHostPool().getByRealHostId(cloudHost.getRealHostId());
            if (myCloudHostData != null && myCloudHostData.getRunningStatus() == Integer.parseInt(runningStatus)) {
                logger.info("CloudHostServiceImpl.updateRunningStatusFromTerminal--> cache running_status and real_status is same ["+runningStatus+"] ,host"+ hostId);
            }else{
                myCloudHostData.setRunningStatus(Integer.parseInt(runningStatus));
                logger.info("CloudHostServiceImpl.updateRunningStatusFromTerminal--> cache running_status ["+myCloudHostData.getRunningStatus()+"] and real_status ["+runningStatus+"] is not same,host"+hostId);
            }
            
            if (cloudHost.getRunningStatus() == Integer.parseInt(runningStatus)) {
                logger.info("CloudHostServiceImpl.updateRunningStatusFromTerminal--> db running_status and real_status is same ["+runningStatus+"] ,host"+hostId  );
            }else{
                Map<String, Object> cloudHostData = new LinkedHashMap<String, Object>();
                cloudHostData.put("runningStatus", Integer.parseInt(runningStatus));
                cloudHostData.put("realHostId", cloudHost.getRealHostId());
                cloudHostMapper.updateRunningStatusByRealHostId(cloudHostData);
                 logger.info("CloudHostServiceImpl.updateRunningStatusFromTerminal--> cache running_status ["+myCloudHostData.getRunningStatus()+"] and real_status ["+runningStatus+"] is not same ,host"+hostId);
            } 
            
            MethodResult result = new MethodResult(MethodResult.SUCCESS, "状态更新成功");
            
            return result;
                
        } catch (Exception e) {
            logger.error(e);
            throw new MyException("更新失败");
        }  
    }
    

    /**
     * 查询所有已经分配但是未参与定时任务的主机 
    * <p>Title: getDesktopCloudHostNotInTimerBackUp</p> 
    * <p>Description: </p> 
    * @return 
    * @see com.zhicloud.ms.service.ICloudHostService#getDesktopCloudHostNotInTimerBackUp()
     */
    @Override
    public List<CloudHostVO> getDesktopCloudHostNotInTimerBackUp() {
        CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
        return cloudHostMapper.getDesktopCloudHostNotInTimerBackUp();
    }
    /**
     * 查询所有已经分配已经参与到定时任务的云桌面主机
    * <p>Title: getDesktopCloudHostInTimerBackUp</p> 
    * <p>Description: </p> 
    * @return 
    * @see com.zhicloud.ms.service.ICloudHostService#getDesktopCloudHostInTimerBackUp()
     */
    @Override
    public List<CloudHostVO> getDesktopCloudHostInTimerBackUp() {
        CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
        return cloudHostMapper.getDesktopCloudHostInTimerBackUp();
    }
    /**
     * 查询处于定时任务的开机云桌面主机
    * <p>Title: getDesktopCloudHostInTimerBackUpStart</p> 
    * <p>Description: </p> 
    * @return 
    * @see com.zhicloud.ms.service.ICloudHostService#getCloudHostInTimerBackUpStart()
     */
    public List<CloudHostVO> getCloudHostInTimerBackUpStart(String timerKey) {
        CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
        return cloudHostMapper.getCloudHostInTimerBackUpStart(timerKey);
    }
    /**
     * 获取规定条数的已关机的云桌面主机,且备份记录中的时间大于当前时间或者为null
    * <p>Title: getDesktopCloudHostInTimerBackUpStop</p> 
    * <p>Description: </p> 
    * @param limit
    * @return 
    * @see com.zhicloud.ms.service.ICloudHostService#getDesktopCloudHostInTimerBackUpStop(java.lang.Integer, java.lang.String)
     */
    public List<CloudHostVO> getCloudHostInTimerBackUpStop(Integer limit,String now,String timerKey) {
        try{
            CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
            Map<String, Object> relateData = new LinkedHashMap<String, Object>();
            relateData.put("limit", limit);
            relateData.put("now", now);
            relateData.put("timerKey", timerKey);
            return cloudHostMapper.getCloudHostInTimerBackUpStop(relateData);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
        
    }
    /**
     * 更新参与定时任务的桌面云主机
    * <p>Title: saveHostInTimer</p> 
    * <p>Description: </p> 
    * @param hostId
    * @return 
    * @see com.zhicloud.ms.service.ICloudHostService#saveHostInTimer(java.lang.String[], java.lang.String)
     */
    @Override
    @Transactional(readOnly=false)
    public MethodResult saveHostInTimer(String[] hostId, String key) {
        CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
        try {
            //先删除定时任务中的所有任务
            cloudHostMapper.deleteAllCloudHostInTimer(key);

             for (String id : hostId) {
                if ("".equals(id)) {
                    continue;
                }
                Map<String, Object> relateData = new LinkedHashMap<String, Object>();
                relateData.put("id", StringUtil.generateUUID());
                relateData.put("hostId", id);
                relateData.put("key",key);
                cloudHostMapper.addHostIntoTimer(relateData);
            }
        } catch (Exception e) {
            throw new AppException(e);
            
        }
        return new MethodResult(MethodResult.SUCCESS,"保存成功");
    }
    /**
     * 
    * @Title: updateHostBackUpTimeInTimer 
    * @Description: 更新定时备份云主机备份的时间 
    * @param @param hostId
    * @param @param now
    * @param @return      
    * @return MethodResult     
    * @throws
     */
    @Override
    public MethodResult updateHostBackUpTimeInTimer(String hostId, String now) {
        CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
        Map<String, Object> relateData = new LinkedHashMap<String, Object>();
        relateData.put("hostId", hostId);
        relateData.put("lastBackUpTime", now);
        cloudHostMapper.updateBackUpTimeByHostId(relateData);
        return new MethodResult(MethodResult.SUCCESS,"保存成功");
    }
    /**
     * 更新爪机状态为创建失败 
    * <p>Title: updateHostStatusToFailByName</p> 
    * <p>Description: </p> 
    * @param name 
    * @see com.zhicloud.ms.service.ICloudHostService#updateHostStatusToFailByName(java.lang.String)
     */
    @Override
    public void updateHostStatusToFailByName(String name) {
        
        logger.info("CloudHostServiceImpl.updateHostStatusToFailByName(): ["+name+"]");
        
        CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
                  
        // 将主机状态设置成创建失败
        Map<String, Object> data = new LinkedHashMap<String, Object>();
         data.put("status", 3);//创建失败
        data.put("hostName",name);
        cloudHostMapper.updateStautsByName(data);
        
    }
      /**
       * @function 查询没有参与定时任务的主机（已分配）
       * @author 张翔
       * @param key 定时器名
       * @return
       */
      @Override
      public List<CloudHostVO> getCloudHostNotInTimer(String key) {
          logger.info("CloudHostServiceImpl.getDesktopCloudHostNotInTimerStartup(): ["+key+"]");
          CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
          return cloudHostMapper.queryCloudHostNotInTimer(key);
      }
     /**
     * 从资源池新增云主机
    * <p>Title: addHostToServerByRealHostId</p> 
    * <p>Description: </p> 
    * @param realHostId
    * @return 
    * @see com.zhicloud.ms.service.ICloudHostService#addHostToServerByRealHostId(java.lang.String)
     */
    @Override
    public MethodResult addHostToServerByRealHostId(String realHostId) {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        try {
            CloudHostData myCloudHostData = CloudHostPoolManager.getCloudHostPool().getByRealHostId(realHostId);
            CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
            //获取用户名密码
            HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
            JSONObject hostResult =  channel.hostQueryInfo(realHostId);
            JSONObject hostInfo = (JSONObject) hostResult.get("host");

            String password = JSONLibUtil.getString(hostInfo, "authentication");
            String account = JSONLibUtil.getString(hostInfo, "display");  
            Map<String, Object> cloudHostData = new LinkedHashMap<String, Object>();
            cloudHostData.put("id", StringUtil.generateUUID());
            cloudHostData.put("realHostId", realHostId);
            cloudHostData.put("sysDisk", myCloudHostData.getSysDisk());
            cloudHostData.put("dataDisk", myCloudHostData.getDataDisk());
            cloudHostData.put("innerIp", myCloudHostData.getInnerIp());
            cloudHostData.put("innerPort", myCloudHostData.getInnerPort()); 
            cloudHostData.put("outerIp", myCloudHostData.getOuterIp());
            cloudHostData.put("outerPort", myCloudHostData.getOuterPort()); 
            cloudHostData.put("createTime", DateUtil.dateToString(new Date(),"yyyyMMddHHmmssSSS"));
            cloudHostData.put("runningStatus", myCloudHostData.getRunningStatus());
            cloudHostData.put("status", "2");
            cloudHostData.put("hostName", myCloudHostData.getHostName());
            cloudHostData.put("displayName", myCloudHostData.getHostName());
            cloudHostData.put("bandwidth", myCloudHostData.getBandwidth());
            cloudHostData.put("cpuCore", myCloudHostData.getCpuCore());
            cloudHostData.put("type", 2);
            cloudHostData.put("memory", myCloudHostData.getMemory());
            cloudHostData.put("account", account);
            cloudHostData.put("password", password);
            cloudHostMapper.insertCloudHost(cloudHostData);
            
           
            operLogService.addLog("云主机", "从资源池添加主机"+myCloudHostData.getHostName()+"成功", "1", "1", request);
            return new MethodResult(MethodResult.SUCCESS,"添加成功");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return null;

    }

      /**
       * @function 查询参与到定时任务的主机
       * @author 张翔
       * @param key 定时器名
       * @return
       */
      @Override
      public List<CloudHostVO> getCloudHostInTimer(String key) {
          logger.info("CloudHostServiceImpl.getDesktopCloudHostInTimerStartup(): ["+key+"]");
          CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
          return cloudHostMapper.queryCloudHostInTimer(key);
      }

    /**
     * 从资源池新增云桌面主机
    * <p>Title: addHostToDeskTopByRealHostId</p> 
    * <p>Description: </p> 
    * @param realHostId
    * @param wareHouseId
    * @return 
    * @see com.zhicloud.ms.service.ICloudHostService#addHostToDeskTopByRealHostId(java.lang.String, java.lang.String)
     */
    public MethodResult addHostToDeskTopByRealHostId(String realHostId, String wareHouseId,String poolId) {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();


        try {
            CloudHostData myCloudHostData = CloudHostPoolManager.getCloudHostPool().getByRealHostId(realHostId);
            CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
            CloudHostWarehouseMapper cloudHostWarehouseMapper = this.sqlSession.getMapper(CloudHostWarehouseMapper.class);

            HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
            JSONObject hostResult =  channel.hostQueryInfo(realHostId);
            JSONObject hostInfo = (JSONObject) hostResult.get("host");

            String password = JSONLibUtil.getString(hostInfo, "authentication");
            String account = JSONLibUtil.getString(hostInfo, "display");  
            Map<String, Object> cloudHostData = new LinkedHashMap<String, Object>();
            cloudHostData.put("id", StringUtil.generateUUID());
            cloudHostData.put("realHostId", realHostId);
            cloudHostData.put("sysDisk", myCloudHostData.getSysDisk());
            cloudHostData.put("dataDisk", myCloudHostData.getDataDisk());
            cloudHostData.put("innerIp", myCloudHostData.getInnerIp());
            cloudHostData.put("innerPort", myCloudHostData.getInnerPort()); 
            cloudHostData.put("outerIp", myCloudHostData.getOuterIp());
            cloudHostData.put("outerPort", myCloudHostData.getOuterPort()); 
            cloudHostData.put("createTime", DateUtil.dateToString(new Date(),"yyyyMMddHHmmssSSS"));
            cloudHostData.put("runningStatus", myCloudHostData.getRunningStatus());
            cloudHostData.put("status", "2");
            cloudHostData.put("hostName", myCloudHostData.getHostName());
            cloudHostData.put("displayName", myCloudHostData.getHostName());
            cloudHostData.put("bandwidth", myCloudHostData.getBandwidth());
            cloudHostData.put("cpuCore", myCloudHostData.getCpuCore());
            cloudHostData.put("type", 1);
            cloudHostData.put("memory", myCloudHostData.getMemory());
            cloudHostData.put("account", account);
            cloudHostData.put("password", password);
            cloudHostData.put("password", password);
            cloudHostData.put("warehouseId", wareHouseId);
            cloudHostData.put("poolId", poolId);
            cloudHostMapper.insertCloudHost(cloudHostData);
            
            //更新仓库数据
            
            cloudHostWarehouseMapper.updateWarehouseForAddNewHost(wareHouseId);
            
           
            operLogService.addLog("云主机", "从资源池添加主机"+myCloudHostData.getHostName()+"进入桌面云仓库成功", "1", "1", request);
            return new MethodResult(MethodResult.SUCCESS,"添加成功");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return null;
    }
    /**
     * (非 Javadoc) 
    * <p>Title: getAllAallocateDesktopHost</p> 
    * <p>Description: </p> 
    * @return 
    * @see com.zhicloud.ms.service.ICloudHostService#getAllAallocateDesktopHost()
     */
    @Override
    public List<CloudHostVO> getAllAallocateDesktopHost() {
        CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
        return cloudHostMapper.getAllAallocateDesktopHost();
    }
    /**
     * 获取所有未参与定时备份任务的云服务器
    * <p>Title: getServerCloudHostNotInTimerBackUp</p> 
    * <p>Description: </p> 
    * @return 
    * @see com.zhicloud.ms.service.ICloudHostService#getServerCloudHostNotInTimerBackUp()
     */
    @Override
    public List<CloudHostVO> getServerCloudHostNotInTimerBackUp() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * 获取所有参与定时备份任务的云服务器
    * <p>Title: getServerCloudHostInTimerBackUp</p> 
    * <p>Description: </p> 
    * @return 
    * @see com.zhicloud.ms.service.ICloudHostService#getServerCloudHostInTimerBackUp()
     */
    @Override
    public List<CloudHostVO> getServerCloudHostInTimerBackUp() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public List<CloudHostVO> getAllHostByVpcId(String vpcId){
    	CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
    	List<CloudHostVO> hostList = cloudHostMapper.getCloudHostInVpc(vpcId);
    	List<CloudHostVO> newList = new ArrayList<>();
    	if(hostList!=null){
    		for(CloudHostVO cloudHost : hostList){
    			if(StringUtil.isBlank(cloudHost.getRealHostId())){
    				continue;
    			}else{
    				newList.add(cloudHost);
    			}
    		}
    	}
    	return newList;
    }

    /**
     * 绑定云主机和租户
    * <p>Title: bindHostToTenant</p> 
    * <p>Description: </p> 
    * @param hostId
    * @param tenantId
    * @return 
    * @see com.zhicloud.ms.service.ICloudHostService#bindHostToTenant(java.lang.String, java.lang.String)
     */
    @Override
    public MethodResult bindHostToTenant(String hostId, String tenantId) {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
        SysTenantMapper sysTenantMapper = this.sqlSession.getMapper(SysTenantMapper.class); 
        List<CloudHostVO> hostList = cloudHostMapper.getCloudHostInTenant(tenantId);
        CloudHostVO hostVO = cloudHostMapper.getById(hostId); 
        hostList.add(hostVO);
        BigInteger totalMemory = BigInteger.ZERO;
        Integer totalCpu = 0;
        BigInteger totalBandwidth = BigInteger.ZERO;
        BigInteger totalDisk = BigInteger.ZERO;
        for(CloudHostVO host : hostList){
            totalCpu = totalCpu+ host.getCpuCore();
            totalMemory = totalMemory.add(host.getMemory());
            totalBandwidth =totalBandwidth.add(host.getBandwidth());
            totalDisk = totalDisk.add(host.getDataDisk());
        } 
        SysTenant tenant = sysTenantMapper.selectByPrimaryKey(tenantId); 
        if(tenant.getDisk().compareTo(totalDisk)<=0){
            operLogService.addLog("云主机", "主机"+hostVO.getDisplayName()+"绑定租户"+tenant.getName()+"失败", "1", "2", request);
            return new MethodResult(MethodResult.FAIL,"硬盘资源不足，绑定失败");
        }
        if(tenant.getMem().compareTo(totalMemory)<0){
            operLogService.addLog("云主机", "主机"+hostVO.getDisplayName()+"绑定租户"+tenant.getName()+"失败", "1", "2", request);
            return new MethodResult(MethodResult.FAIL,"内存资源不足，绑定失败");
        }
        if(tenant.getCpu()<totalCpu){
            operLogService.addLog("云主机", "主机"+hostVO.getDisplayName()+"绑定租户"+tenant.getName()+"失败", "1", "2", request);
            return new MethodResult(MethodResult.FAIL,"CPU资源不足，绑定失败");
        }
        Map<String, Object> cloudHostData = new LinkedHashMap<String, Object>();
        cloudHostData.put("hostId", hostId);
        cloudHostData.put("tenantId", tenantId);
        cloudHostMapper.bindHostToTenant(cloudHostData);
        operLogService.addLog("云主机", "主机"+hostVO.getDisplayName()+"绑定租户"+tenant.getName()+"成功", "1", "1", request);

        return new MethodResult(MethodResult.SUCCESS,"绑定成功");
    }
    
    public MethodResult bindHostToTenant(CloudHostVO hostVO, String tenantId) {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
        SysTenantMapper sysTenantMapper = this.sqlSession.getMapper(SysTenantMapper.class); 
        List<CloudHostVO> hostList = cloudHostMapper.getCloudHostInTenant(tenantId);
        hostList.add(hostVO);
        BigInteger totalMemory = BigInteger.ZERO;
        Integer totalCpu = 0;
        BigInteger totalBandwidth = BigInteger.ZERO;
        BigInteger totalDisk = BigInteger.ZERO;
        for(CloudHostVO host : hostList){
            totalCpu = totalCpu+ host.getCpuCore();
            totalMemory = totalMemory.add(host.getMemory());
            totalBandwidth =totalBandwidth.add(host.getBandwidth());
            totalDisk = totalDisk.add(host.getDataDisk());
        } 
        SysTenant tenant = sysTenantMapper.selectByPrimaryKey(tenantId); 
        if(tenant.getDisk().compareTo(totalDisk)<=0){
            operLogService.addLog("云主机", "主机"+hostVO.getDisplayName()+"绑定租户"+tenant.getName()+"失败", "1", "2", request);
            return new MethodResult(MethodResult.FAIL,"硬盘资源不足，绑定失败");
        }
        if(tenant.getMem().compareTo(totalMemory)<0){
            operLogService.addLog("云主机", "主机"+hostVO.getDisplayName()+"绑定租户"+tenant.getName()+"失败", "1", "2", request);
            return new MethodResult(MethodResult.FAIL,"内存资源不足，绑定失败");
        }
        if(tenant.getCpu()<totalCpu){
            operLogService.addLog("云主机", "主机"+hostVO.getDisplayName()+"绑定租户"+tenant.getName()+"失败", "1", "2", request);
            return new MethodResult(MethodResult.FAIL,"CPU资源不足，绑定失败");
        }
        Map<String, Object> cloudHostData = new LinkedHashMap<String, Object>();
        cloudHostData.put("hostId", hostVO.getId());
        cloudHostData.put("tenantId", tenantId);
        cloudHostMapper.bindHostToTenant(cloudHostData);
        operLogService.addLog("云主机", "主机"+hostVO.getDisplayName()+"绑定租户"+tenant.getName()+"成功", "1", "1", request);

        return new MethodResult(MethodResult.SUCCESS,"绑定成功");
    }

    /**
     * 解除绑定 
    * <p>Title: unboundHostInTenant</p> 
    * <p>Description: </p> 
    * @param hostId
    * @return 
    * @see com.zhicloud.ms.service.ICloudHostService#unboundHostInTenant(java.lang.String)
     */
    @Override
    public MethodResult unboundHostInTenant(String hostId) {
        CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
        cloudHostMapper.unboundHostInTenant(hostId);
        return new MethodResult(MethodResult.SUCCESS,"解绑成功");
    }

    /**
     * 根据租户id查询租户内的主机
    * <p>Title: getHostInTenant</p> 
    * <p>Description: </p> 
    * @param tenantId
    * @return 
    * @see com.zhicloud.ms.service.ICloudHostService#getHostInTenant(java.lang.String)
     */
    @Override
    public List<CloudHostVO> getHostInTenant(String tenantId) {
        CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
        return cloudHostMapper.getCloudHostInTenant(tenantId);
    }
    
    @SuppressWarnings("static-access")
	@Override
    @Transactional(readOnly=false)
    public MethodResult deleteByRealId(String realId) {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();

        CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
        TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
        CloudHostWarehouseMapper cloudHostWarehouseMapper = this.sqlSession.getMapper(CloudHostWarehouseMapper.class);
        //返回查询结果
        CloudHostVO host = cloudHostMapper.getByRealHostId(realId);
        try {
			if(host == null){
	            operLogService.addLog("云主机", "删除云主机失败，未查找到主机信息", "1", "2", request);
			    return new MethodResult(MethodResult.FAIL, "未查找到主机信息");
			}else{
		        //删除资源池的主机
		        CloudHostPoolManager.getSingleton().getCloudHostPool().removeByRealHostId(host.getRealHostId());
		        //如果是云桌面主机
		        if(host.getType()==1){
		        	// 主机创建成功已经分配
		        	if(!StringUtil.isBlank(host.getUserId())){
		        		Map<String,Object> userData = new HashMap<String,Object>();
		        		userData.put("id", host.getUserId());
		        		userData.put("modified_time", DateUtil.dateToString(new Date(),"yyyyMMddHHmmssSSS"));
		        		terminalUserMapper.updateCloudHostAmountForDelete(userData); 
		        		
		        		cloudHostWarehouseMapper.updateWarehouseAmountForDeleteDispatchedHost(host.getWarehouseId());
		        	} 
		        	//主机正在创建中
		        	else if(host.getStatus() == 1){ 
		        		operLogService.addLog("云主机", "删除云主机"+host.getDisplayName()+"失败", "1", "2", request);
		        		return new MethodResult(MethodResult.FAIL, "主机在创建中，无法删除");
		        	}
		        	//主机创建未成功
		        	else if(StringUtil.isBlank(host.getRealHostId())){
		        		cloudHostWarehouseMapper.updateWarehouseAmountForDeleteNotCreatedHost(host.getWarehouseId());
		        		
		        	}else{      
		        		//主机创建成功未分配
		        		cloudHostWarehouseMapper.updateWarehouseAmountForDeleteHost(host.getWarehouseId()); 
		        	}
		        }
			}
			cloudHostMapper.deleteById(host.getId());
            operLogService.addLog("云主机", "删除云主机"+host.getDisplayName()+"成功", "1", "2", request);

			return new MethodResult(MethodResult.SUCCESS, "删除成功");
		} catch (Exception e) {
			return new MethodResult(MethodResult.FAIL, "删除失败");
		}
    }

    /**
     * 根据主机真实id更新innerip和innerport
    * <p>Title: updateInnerIpByRealHostId</p> 
    * <p>Description: </p> 
    * @param parameter
    * @return 
    * @see com.zhicloud.ms.service.ICloudHostService#updateInnerIpByRealHostId(java.util.Map)
     */
    @Transactional(readOnly=false)
    public int updateInnerIpByRealHostId(Map<String, Object> parameter) {
        CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
        return cloudHostMapper.updateInnerIpByRealHostId(parameter);
     }

    /**
     * 从光盘启动云主机
    * <p>Title: startCloudHostFromIso</p> 
    * <p>Description: </p> 
    * @param cloudHostId
    * @param imageId
    * @return 
    * @see com.zhicloud.ms.service.ICloudHostService#startCloudHostFromIso(java.lang.String, java.lang.String)
     */
    @Transactional(readOnly=false)
    public MethodResult startCloudHostFromIso(String cloudHostId, String imageId) {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        OperLogVO operLog = new OperLogVO();
        operLog.setId(StringUtil.generateUUID());
        operLog.setModule("云主机");
        operLog.setOperTime(StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
        try {
            CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
            Map<String, Object> data = new LinkedHashMap<String, Object>(); 
            data.put("id", cloudHostId);
            CloudHostVO cloudHost = cloudHostMapper.getCloudHostById(data); 
            if(cloudHost == null){ 
                operLog.setContent("启动云主机失败-找不到相应云主机");
                operLog.setStatus(2);//1 ：成功 2：失败 3：异步
                operLogService.addLog(operLog, request);
                MethodResult result = new MethodResult(MethodResult.FAIL, "not found host info");
                return result;
            }
            String realHostId = cloudHost.getRealHostId();
            if (realHostId == null) {
                operLog.setContent("启动云主机失败-云主机尚未创建成功");
                operLog.setStatus(2);//1 ：成功 2：失败 3：异步
                operLogService.addLog(operLog, request);
                MethodResult result = new MethodResult(MethodResult.FAIL, "云主机尚未创建成功");
                result.put("hostId", cloudHostId);
                return result;
            }
            CloudHostData myCloudHostData = CloudHostPoolManager.getCloudHostPool().getByRealHostId(cloudHost.getRealHostId());
            if (myCloudHostData != null && myCloudHostData.getRunningStatus() == 2) {
                Map<String, Object> hostData = new LinkedHashMap<String, Object>();
                hostData.put("runningStatus", AppConstant.CLOUD_HOST_RUNNING_STATUS_RUNNING);
                hostData.put("realHostId", realHostId);
                cloudHostMapper.updateRunningStatusByRealHostId(hostData); 
                MethodResult result = new MethodResult(MethodResult.SUCCESS, "启动成功");
                result.put("hostId", cloudHostId);
                operLog.setContent("启动云主机"+cloudHost.getDisplayName());
                operLog.setStatus(1);//1 ：成功 2：失败 3：异步
                operLogService.addLog(operLog, request);
                return result;
            }
            HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(cloudHost.getRegion());
            JSONObject startResullt = channel.hostStart(realHostId, 1, imageId);
            //获取光盘镜像信息
            IsoImageData iamgeData = IsoImagePoolManager.getSingleton().getIsoImagePool().getByRealImageId(imageId);
            Map<String, Object> cloudHostData = new LinkedHashMap<String, Object>();
            cloudHostData.put("runningStatus", AppConstant.CLOUD_HOST_RUNNING_STATUS_RUNNING);
            cloudHostData.put("realHostId", realHostId);
            cloudHostData.put("id", cloudHost.getId());
            cloudHostData.put("sysImageId", imageId);
            cloudHostData.put("sysImageName", "光盘镜像"+iamgeData.getName());
            if (HttpGatewayResponseHelper.isSuccess(startResullt)) { 
                //更新缓存主机状态
                CloudHostData newCloudHostData = myCloudHostData.clone();
                newCloudHostData.setRunningStatus(AppConstant.CLOUD_HOST_RUNNING_STATUS_RUNNING);
                newCloudHostData.setLastStatus(AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTDOWN);
                newCloudHostData.setLastOperTime(StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
                CloudHostPoolManager.getCloudHostPool().put(newCloudHostData);
                //更新主机运行状态
                cloudHostMapper.updateRunningStatusByRealHostId(cloudHostData);
                //更新主机的镜像信息
                cloudHostMapper.updateImageIdById(cloudHostData);
                logger.info("CloudHostServiceImpl.startCloudHost() > [" + Thread.currentThread().getId() + "] start host succeeded");
                MethodResult result = new MethodResult(MethodResult.SUCCESS, "启动成功");
                result.put("hostId", cloudHostId);
                operLog.setContent("启动云主机"+cloudHost.getDisplayName());
                operLog.setStatus(1);//1 ：成功 2：失败 3：异步
                operLogService.addLog(operLog, request);
                return result;
            } else {
                logger.warn("CloudHostServiceImpl.startCloudHost() > start host failed, message:[" + HttpGatewayResponseHelper.getMessage(startResullt) + "]");
                MethodResult result = new MethodResult(MethodResult.FAIL, "启动失败");
                result.put("hostId", cloudHostId);
                operLog.setContent("启动云主机"+cloudHost.getDisplayName());
                operLog.setStatus(2);//1 ：成功 2：失败 3：异步
                operLogService.addLog(operLog, request);
                return result;
            }
        } catch (ConnectException e) {
            logger.error(e);
            throw new MyException("启动失败");
        } catch (MyException e) {
            logger.error(e);
            throw new MyException("启动失败");
        } catch (Exception e) {
            logger.error(e);
            throw new MyException("启动失败");
        }  
    }
    
    @Override
    public void updateCloudHostRunningStatus() {
        MethodResult result = new MethodResult(MethodResult.SUCCESS);
        CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
        JSONObject hostQueryResult = null;
        RegionData[] regionDatas = RegionHelper.singleton.getAllResions();
        CloudHostPool cloudHostPool = CloudHostPoolManager.getCloudHostPool();
        for (RegionData regionData : regionDatas) {
            try {
                HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(regionData.getId());
                // 获取默认计算资源池
                JSONObject allPool = channel.computePoolQuery();
                if("fail".equals(allPool.getString("status"))) {
                    logger.error("CloudHostServiceImpl.updateCloudHostRunningStatus-> fail to get pool  ");
                } else {
                    JSONArray computerList = allPool.getJSONArray("compute_pools");

                    for (int i = 0; i < computerList.size(); i ++) {
                        JSONObject computerObject = computerList.getJSONObject(i);
                        if (computerObject == null) {// 不能获取，则跳过该region
                            logger.error(String.format("fail to get default compute pool. region[%s].", regionData.getId()));
                            continue;
                        }

                        // 从http gateway获取所有的云主机
                        try{
                            hostQueryResult = channel.hostQuery((String) computerObject.get("uuid"));
                        }catch(Exception e){
                            logger.info("fail to get hosts info from pool");
                            continue;
                        }
                        if (HttpGatewayResponseHelper.isSuccess(hostQueryResult) == false) {// 失败则跳过该region
                            logger.info(String.format("fail to query host from http gateway. region[%s], message[%s]", regionData.getId(), HttpGatewayResponseHelper.getMessage(hostQueryResult)));
                            continue;
                        }

                        JSONArray hosts = (JSONArray) hostQueryResult.get("hosts");
                        // 循环这些云主机
                        for (int j = 0; j < hosts.size(); j++) {
                            JSONObject realCloudHost = (JSONObject) hosts.get(j);
                            String uuid = JSONLibUtil.getString(realCloudHost, "uuid");
                            String name = JSONLibUtil.getString(realCloudHost, "name");

                            if (uuid == null) {// uuid不可以为空
                                logger.warn(String.format("found no uuid host when fetch host list from http gateway. Host name[%s], region[%s].", name, regionData.getId()));
                                continue;
                            }
                            Integer cpuCount = JSONLibUtil.getInteger(realCloudHost, "cpu_count");
                            Double cpuUsage = JSONLibUtil.getDouble(realCloudHost, "cpu_usage");
                            BigInteger[] memory = JSONLibUtil.getBigIntegerArray(realCloudHost, "memory"); // [可用,总量]
                            Double memoryUsage = JSONLibUtil.getDouble(realCloudHost, "memory_usage");
                            BigInteger[] diskVolume = JSONLibUtil.getBigIntegerArray(realCloudHost, "disk_volume"); // [可用,总量]
                            Double diskUsage = JSONLibUtil.getDouble(realCloudHost, "disk_usage");
                            String[] ip = JSONLibUtil.getStringArray(realCloudHost, "ip"); // [宿主机ip,公网ip]，不分配则为""
                            Integer runningStatus = JSONLibUtil.getInteger(realCloudHost, "status"); // 0=正常,1=告警,2=故障,3=停止

                            // 获取池里面已有的数据
                            CloudHostData oldCloudHostData = cloudHostPool.getByRealHostId(uuid);

                            //                        CloudHostData oldCloudHostData =  new CloudHostPool().getByRealHostId(uuid);

                            CloudHostData newCloudHostData = null;
                            if (oldCloudHostData == null) {
                                newCloudHostData = new CloudHostData();
                                newCloudHostData.setRegion(1);
                                newCloudHostData.setRealHostId(uuid);
                            } else {
                                newCloudHostData = oldCloudHostData.clone();
                            }

                            newCloudHostData.setHostName(name);
                            newCloudHostData.setCpuCore(cpuCount);
                            newCloudHostData.setCpuUsage(cpuUsage);
                            newCloudHostData.setMemory(memory[1]);
                            newCloudHostData.setMemoryUsage(memoryUsage);
                            newCloudHostData.setDataDisk(diskVolume[1]);
                            newCloudHostData.setDataDiskUsage(diskUsage);
                            newCloudHostData.setInnerIp(ip[0]);
                            newCloudHostData.setOuterIp(ip[1]);
                            newCloudHostData.setRunningStatus(transforRunningStatus(runningStatus));
                            newCloudHostData.setLastOperStatus(0);
                            CloudHostPoolManager.getCloudHostPool().put(newCloudHostData);
                            // 如果running_status变了，则更新数据库
                            if (oldCloudHostData == null || (oldCloudHostData != null && NumberUtil.equals(newCloudHostData.getRunningStatus(), oldCloudHostData.getRunningStatus()) == false)) {
                                boolean update_flag = true;

                                if(update_flag){
                                    if(oldCloudHostData == null){
                                        oldCloudHostData = new CloudHostData();
                                        oldCloudHostData.setRunningStatus(null);
                                    }
                                    logger.info("CloudHostServiceImpl.updateCloudHostRunningStatus > [" + Thread.currentThread().getId() + "] 云主机状态发生变化, realHostId:[" + newCloudHostData.getRealHostId() + "], hostName:[" + newCloudHostData.getHostName() + "], oldRunningStatus:[" + oldCloudHostData.getRunningStatus()
                                        + "], newRunningStatus:[" + newCloudHostData.getRunningStatus() + "]");
                                    Map<String, Object> data = new LinkedHashMap<String, Object>();
                                    data.put("runningStatus", newCloudHostData.getRunningStatus());
                                    data.put("realHostId", newCloudHostData.getRealHostId());
                                    cloudHostMapper.updateRunningStatusByRealHostId(data);
                                    logger.info("CloudHostServiceImpl.updateCloudHostRunningStatus-> update end");

                                }
                            }
                            cloudHostPool.put(newCloudHostData);
                        }

                    }
                }

            } catch (SocketException e) { 
                logger.error("connect to http gateway failed, exception:[" + e.getMessage() + "], region:[" + String.format("%s:%s", regionData.getId(), regionData.getName()) + "]");
            } catch (Exception e) {
                logger.error(e);
                e.printStackTrace();
                System.out.println(hostQueryResult);
                throw new MyException(e);
            }
        }
 
        
    }
    
    private Integer transforRunningStatus(Integer runningStatus) {
        if (runningStatus == null) {
            return null;
        }
        if (runningStatus == 0) {
            return AppConstant.CLOUD_HOST_RUNNING_STATUS_RUNNING;
        } else if (runningStatus == 1) {
            return AppConstant.CLOUD_HOST_RUNNING_STATUS_ALARM;
        } else if (runningStatus == 2) {
            return AppConstant.CLOUD_HOST_RUNNING_STATUS_FAULT;
        } else if (runningStatus == 3) {
            return AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTDOWN;
        } else {
            throw new AppException("wrong value of runningStatus[" + runningStatus + "]");
        }
    }

    /**
     * @Description:获取所有资源池信息,并从数据库获取设置的最大并发创建数
     * @return JSONArray
     * @throws
     */
    public synchronized JSONArray getAllComputePool() {
        // 获取数据库保存信息
        if (maxconcurrent_lists == null) {
            maxconcurrent_lists = cloudHostWarehouseService.getAllConcurrent();
        }
        ComputeInfoPool pool = ComputeInfoPoolManager.singleton().getPool();
        Map<String, ComputeInfoExt> poolMap = pool.getAllComputePool();
        Set<String> pool_keys = poolMap.keySet();
        Iterator<String> its = pool_keys.iterator();
        JSONArray pool_arrays = new JSONArray();

        while (its.hasNext()) {
            ComputeInfoExt ext = poolMap.get(its.next());
            JSONObject obj = new JSONObject();
            obj.put("pool_id", ext.getUuid());
            obj.put("pool_name", ext.getName());
            obj.put("max_creating", AppInconstant.init_maxcreating); 
            // 循环比对
            for (CloudHostWarehouse cloud : maxconcurrent_lists) {
                if (ext.getUuid().equals(cloud.getPoolId())) {
                    obj.put("max_creating", cloud.getMax_creating()); 
                    break;
                }else{
                    //如果数据库中没有设置，需要给一个默认值2
                    obj.put("max_creating", AppInconstant.init_maxcreating); 
                }
            }
            pool_arrays.add(obj);
        }
        return pool_arrays;
    }

	@Override
	public MethodResult getHostByDisplayName(String displayName) {
		CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
		List<CloudHostVO> hostList = cloudHostMapper.getHostByDisplayName(displayName);
		if(hostList!=null && hostList.size()>0){
			return new MethodResult(MethodResult.FAIL,"显示名已存在");
		}
		return new MethodResult(MethodResult.SUCCESS,"显示名可用");
	}

	@Override
	@Transactional(readOnly=false)
	public MethodResult updateDisplayNameById(Map<String, Object> condition) {
		CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
		int n = cloudHostMapper.updateDisplayNameById(condition);
		if(n > 0){
			return new MethodResult(MethodResult.SUCCESS, "显示名修改成功");
		}
		return new MethodResult(MethodResult.FAIL, "显示名修改失败");
	}
}

 

package com.zhicloud.ms.service.impl;

import java.util.List;
import java.util.ArrayList;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.hyperic.sigar.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.text.DecimalFormat;

import javax.annotation.Resource;

import com.zhicloud.ms.util.CapacityUtil;
import com.zhicloud.ms.service.StateService;
import com.zhicloud.ms.vo.BcastVO;
import com.zhicloud.ms.vo.NetVO;
import com.zhicloud.ms.vo.ResUsageVO;
import com.zhicloud.ms.vo.ServerComponentVO;
import com.zhicloud.ms.vo.ServerVO;
import com.zhicloud.ms.vo.ServiceVO;
import com.zhicloud.ms.util.LinuxShellUtil;
import com.zhicloud.ms.app.propeties.AppProperties;
import com.zhicloud.ms.mapper.ServerComponentMapper;

/**
 * @ClassName: ResourceStatisticsServiceImpl
 * @Description: 资源信息处理
 * @author 梁绍辉 于 2015年9月9日 下午3:36:01
 */
@Service("stateService")
@Transactional
public class StateServiceImpl implements StateService {
    /* 日志 */
    private static final Logger logger = Logger.getLogger(LoginServiceImpl.class);
    
    @Resource
    private SqlSession sqlSession;

    public List<BcastVO> bcastList = new ArrayList<BcastVO>();   

    /**
     * @Description:获取服务器信息
     * @return ServerVO
     */
    public ServerVO queryServer() {
        logger.debug("StateServiceImpl.queryServer()");
        
        ServerVO serverVO = new ServerVO();
        Sigar sigar = null;
        try{
            sigar = new Sigar(); 
            // CPU core
            try{
                serverVO.setCpucore(String.valueOf(sigar.getCpuInfoList().length));
            }catch(Exception ex1){
                serverVO.setCpucore("Err");
            }
            // MEM
            try {
                Mem mem = sigar.getMem();
                serverVO.setMem(CapacityUtil.toGB(new BigDecimal(mem.getTotal()).toBigInteger(), 2));                
            }catch(Exception ex2){
                serverVO.setMem("Err");
            }
            // Host Name
            serverVO.setName(getPlatformName());
            // IP
            serverVO.setIp(getIp(sigar));
            // disk
            serverVO.setDisk(CapacityUtil.toGB(getDisk(sigar).toBigInteger(), 2));
            try{
                OperatingSystem OS = OperatingSystem.getInstance();           
                // type
                serverVO.setType(OS.getVendor());
                // model            
                serverVO.setModel(OS.getVendorName());
            }catch(Exception ex){
                serverVO.setType("Err");
                serverVO.setModel("Err");
            }            
            return serverVO;
        }catch(Exception ex){
            return serverVO;
        }finally{
            try{
                sigar.close();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    /**
     * @Description:获取使用率
     * @return ResUsageVO
     */
    public ResUsageVO queryUsage() {
        logger.debug("StateServiceImpl.queryUsage()");      
        Sigar sigar = new Sigar();
        ResUsageVO resUsageVO = new ResUsageVO();
        DecimalFormat df = new DecimalFormat("#.##");
        try{
            System.out.println(sigar.getFileSystemList());
            Mem mem = sigar.getMem();
            logger.info("get memory "+mem);
            resUsageVO.setMemper(df.format(mem.getUsedPercent()));
//            logger.info(mem.getUsedPercent());
        }catch(Exception ex){
            resUsageVO.setMemper("0");
        }
        try{
            CpuPerc cpu = sigar.getCpuPerc();
            logger.info("get cpu "+cpu);
            resUsageVO.setCpuper(df.format(cpu.getCombined() * 100)); //CpuPerc.format(cpu.getCombined()));
//            logger.info("hahahhahahahha1");
        } catch (Exception e) {
            e.printStackTrace();
            resUsageVO.setCpuper("0");
        }
        try{
            resUsageVO.setDiskper(getDiskPercent(sigar));
            logger.info(getDiskPercent(sigar));
//            logger.info("hahahhahahahha");
        }catch (Exception e) {
            e.printStackTrace();
            resUsageVO.setDiskper("0");
        }  
//        logger.info("heeeeeeeeeeeee");
        resUsageVO.setData(getBandwidth(sigar));
        return resUsageVO;
    }

    /**
     * @Description:获取服务信息
     * @return List<ServiceVO>
     */
    public List<ServiceVO> queryService() {
        logger.debug("StateServiceImpl.queryService()");        
        // 运行脚本模板
        String cmd = AppProperties.getValue("component_runtime_script", "ps -ef|grep {%%}|grep -v grep|awk '{print $2}'|xargs ps -o etime -p|sed -n '2p'");
        // pid文件路径
        //String pidPath=  AppProperties.getValue("component_pid_path", "/var/zhicloud/running");
        
        ServerComponentMapper serverComponentMapper = this.sqlSession.getMapper(ServerComponentMapper.class);
        List<ServerComponentVO> serverComponentList =  serverComponentMapper.queryPage();
        
        List<ServiceVO> serviceList = new ArrayList<ServiceVO>();
        
        for(ServerComponentVO componentvo:serverComponentList){
            ServiceVO sv = new ServiceVO();  
            
            sv.setId(componentvo.getName());
            sv.setName(componentvo.getRealname());
            // 通过进程判断组件是否存在
            String processid = AppProperties.getValue("find_process_id", "ps -ef|grep {%%}|grep -v grep");
            //List<String> processidList = LinuxShellUtil.getCallShell(processid.replace("{%%}", componentvo.getKeyword()));
            //根据关键字查询服务进程，存在模糊匹配，造成服务已停止了 但是还是会有结果，修改成根据路径来识别
            List<String> processidList = LinuxShellUtil.getCallShell(processid.replace("{%%}", componentvo.getPath()));
            if(processidList.size() > 0 && processidList.get(0).length() > 0){ // 有进程号
                sv.setStatus("0");
                String cmdCurrent = cmd.replace("{%%}", componentvo.getKeyword());
                List<String> retListStr = LinuxShellUtil.getCallShell(cmdCurrent);
                if(retListStr.size() != 1){
                    sv.setRuntime("Err");
                }else if (retListStr.get(0).trim().length() < 1){
                    sv.setRuntime("Err");
                }else{
                    sv.setRuntime(retListStr.get(0).trim().replace("-", "天"));
                } 
            }else{
                sv.setStatus("1");
                sv.setRuntime("");
            }
            // 判断是否有pid文件(有pid文件表示正在运行）
            /*
            String path = componentvo.getPath() + "/pid";
            File file = new File(path);
            if(file.exists()){   // 目录存在       
                if(searchFileByKeyword(pidPath,componentvo.getKeyword())){ // 找到相应的pid文件
                    sv.setStatus("0");
                    String cmdCurrent = cmd.replace("{%%}", componentvo.getProcessname());
                    List<String> retListStr = LinuxShellUtil.getCallShell(cmdCurrent);
                    if(retListStr.size() != 1){
                        sv.setRuntime("Err");
                    }else if (retListStr.get(0).trim().length() < 1){
                        sv.setRuntime("Err");
                    }else{
                        sv.setRuntime(retListStr.get(0).trim());
                    } 
                } // 无对应的pid文件
                else{
                    sv.setStatus("1");
                }                
            }
            else{
                sv.setStatus("1");
                sv.setRuntime("");
            }
            */
            serviceList.add(sv);
        } 
        return serviceList;
    }

    /**
     * @Description:获取广播信息
     * 请先执行方法：queryBcast()，将自动填充广播地址
     * @return List<BcastVO>
     */ 
    public List<BcastVO> queryBcast() {
        logger.debug("StateServiceImpl.queryBcast()"); 
        
        if (bcastList.size() > 0) return bcastList;
        Sigar sigar = new Sigar();
        bcastList = new ArrayList<BcastVO>();         
        try{
            String ifNames[] = sigar.getNetInterfaceList();
            for (int i = 0; i < ifNames.length; i++) {
                BcastVO bcastVO = new BcastVO();
                String name = ifNames[i];
                NetInterfaceConfig ifconfig = sigar.getNetInterfaceConfig(name);
                bcastVO.setId(String.valueOf(i+1));
                bcastVO.setName(name);
                try{
                    bcastVO.setBcastaddr(ifconfig.getBroadcast());
                }catch(Exception ex){
                    bcastVO.setBcastaddr("0.0.0.0");
                    logger.error("get Bcastaddr:" + ex.getMessage());
                }  
                if ( (ifconfig.getFlags() & 1L) <= 0L) {
                    bcastVO.setStatus("0");
                }else{
                    bcastVO.setStatus("1");
                }
                bcastList.add(bcastVO);
            }
        }catch(Exception ex){
            logger.error("queryBcast:" + ex.getMessage());
        }finally{
            try{
                sigar.close();
            }catch(Exception ex){}
        }
        return bcastList;
    }

    /**
     * @Description:获取网络信息
     * @return List<NetVO>
     */
    public List<NetVO> queryNet() {
        logger.debug("StateServiceImpl.queryNet()");
        
        List<NetVO> netList = new ArrayList<NetVO>();
        // if(netList.size() > 0) return netList;
        netList = new ArrayList<NetVO>();
        bcastList = new ArrayList<BcastVO>();
        Sigar sigar = new Sigar();
        try{
            String ifNames[] = sigar.getNetInterfaceList();
            for (int i = 0; i < ifNames.length; i++) {
                NetVO netVO = new NetVO();
                BcastVO bcastVO = new BcastVO();
                
                String name = ifNames[i];
                NetInterfaceConfig ifconfig = sigar.getNetInterfaceConfig(name);
                netVO.setId(String.valueOf(i+1));
                netVO.setName(name);
                netVO.setAddress(ifconfig.getAddress());
                netVO.setMask(ifconfig.getNetmask());
                
                bcastVO.setId(String.valueOf(i+1));
                bcastVO.setName(name);
                bcastVO.setBcastaddr(ifconfig.getBroadcast());
                
                try{
                    NetInterfaceStat stat = sigar.getNetInterfaceStat(name);
                    logger.debug("[" + name + "]State: " + stat);
                    logger.debug("[" + name + "]Speed: " + stat.getSpeed());
                    if(stat.getSpeed() == -1){
                        String networkSpeed=  AppProperties.getValue("network_bandwidth", "ethtool {%%}|grep Speed|awk '{print $2}'");
                        networkSpeed = networkSpeed.replace("{%%}", name);
                        List<String> speedList =  LinuxShellUtil.getCallShell(networkSpeed);
                        if(speedList.size() > 0){
                            netVO.setSpeed(speedList.get(0));                            
                        }else{
                            netVO.setSpeed("0Mb/s");
                        }
                    }else{
                        double speed = stat.getSpeed()/(1024*1024);
                        DecimalFormat df=new DecimalFormat(".##");
                        String speedStr = df.format(speed);
                        netVO.setSpeed( speedStr + "Mb/s");
                    }
                }catch(Exception ex){
                    netVO.setSpeed("0Mb/s");
                    logger.error("get Speed exception: " + ex.getMessage());
                }  
                if ( (ifconfig.getFlags() & 1L) <= 0L) {
                    netVO.setStatus("0");
                    bcastVO.setStatus("0");
                }else{
                    netVO.setStatus("1");
                    bcastVO.setStatus("1");
                }
                
                netList.add(netVO);
                if(bcastVO.getBcastaddr().length()>0 && !bcastVO.getBcastaddr().equals("0.0.0.0")){
                    bcastList.add(bcastVO);
                }
            }
        }catch(Exception ex){
            logger.error("queryNet:" + ex.getMessage());
        }finally{
            try{
                sigar.close();
            }catch(Exception ex){}
        }
        return netList;
    }

    
    /**
     * 获取磁盘信息
     * @return
     */
    private BigDecimal getDisk(Sigar sigar){
        BigDecimal total = new BigDecimal(0);
        try{
            FileSystem fslist[] = sigar.getFileSystemList();
            for (int i = 0; i < fslist.length; i++) {
                FileSystem fs = fslist[i];
                FileSystemUsage usage = null;
                try {
                    usage = sigar.getFileSystemUsage(fs.getDirName());
                } catch (SigarException e) {
                    if (fs.getType() == 2)
                        throw e;
                    continue;
                }
                switch (fs.getType()) {
                    case 0: // TYPE_UNKNOWN ：未知
                        break;
                    case 1: // TYPE_NONE
                        break;
                    case 2: // TYPE_LOCAL_DISK : 本地硬盘
                        // 文件系统总大小
                        BigDecimal use = BigDecimal.valueOf(usage.getTotal()*1024);
                        total = total.add(use);
                        break;
                    case 3:// TYPE_NETWORK ：网络
                        break;
                    case 4:// TYPE_RAM_DISK ：闪存
                        break;
                    case 5:// TYPE_CDROM ：光驱
                        break;
                    case 6:// TYPE_SWAP ：页面交换
                        break;
                }
            }
        }catch(Exception x){
            logger.error(x.getMessage());
        }
        return total;
    }

    /**
     * 获取磁盘信息
     * @return
     */
    private String getDiskPercent(Sigar sigar){
        long total = 0L;
        long use = 0L;
        try{
            FileSystem fslist[] = sigar.getFileSystemList();
            for (int i = 0; i < fslist.length; i++) {
                FileSystem fs = fslist[i];
                FileSystemUsage usage = null;
                try {
                    usage = sigar.getFileSystemUsage(fs.getDirName());
                } catch (SigarException e) {
                    if (fs.getType() == 2)
                        throw e;
                    continue;
                }
                switch (fs.getType()) {
                    case 0: // TYPE_UNKNOWN ：未知
                        break;
                    case 1: // TYPE_NONE
                        break;
                    case 2: // TYPE_LOCAL_DISK : 本地硬盘
                        // 文件系统总大小                        
                        total += usage.getTotal();
                        use += usage.getUsed();
                        break;
                    case 3:// TYPE_NETWORK ：网络
                        break;
                    case 4:// TYPE_RAM_DISK ：闪存
                        break;
                    case 5:// TYPE_CDROM ：光驱
                        break;
                    case 6:// TYPE_SWAP ：页面交换
                        break;
                }
            }
            double usePercent = use * 100.0/total;
            DecimalFormat df = new DecimalFormat("#.##");
            return df.format(usePercent);
        }catch(Exception x){
            logger.error(x.getMessage());
            return "0";
        }
    }

    
    /**
     * 获取主机名
     * @return
     */
    private String getPlatformName() {
        String hostname = "";
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (Exception exc) {
            Sigar sigar = new Sigar();
            try {
                hostname = sigar.getNetInfo().getHostName();
            } catch (SigarException e) {
                hostname = "localhost.unknown";
            } finally {
                sigar.close();
            }
        }
        return hostname;
    }
  
    /**
     * 获取IP地址
     * @return
     */
    private String getIp(Sigar sigar) {
        String ip = "0.0.0.0";
        try{
            sigar = new Sigar();
            String[] ifaces = sigar.getNetInterfaceList();
            for (int i = 0; i < ifaces.length; i++) {
                NetInterfaceConfig cfg = sigar.getNetInterfaceConfig(ifaces[i]);
                if (NetFlags.LOOPBACK_ADDRESS.equals(cfg.getAddress()) || NetFlags.ANY_ADDR.equals(cfg.getAddress())
                    || (cfg.getFlags() & NetFlags.IFF_LOOPBACK) != 0
                    || NetFlags.NULL_HWADDR.equals(cfg.getHwaddr())) {
                    continue;
                }
                ip = cfg.getAddress();//IP地址
                return ip;
            }
            return ip;
        } catch (Exception ex) {
            logger.error("getIp:" + ex.getMessage());
            return "Err";
        } 
    }
    
    private String getBandwidth(Sigar sigar){
        String bandwidth = "0";
        try{            
            String[] ifaces = sigar.getNetInterfaceList();
            for (int i = 0; i < ifaces.length; i++) {
                String name = ifaces[i];
                NetInterfaceConfig cfg = sigar.getNetInterfaceConfig(ifaces[i]);
                if (NetFlags.LOOPBACK_ADDRESS.equals(cfg.getAddress()) || NetFlags.ANY_ADDR.equals(cfg.getAddress())
                    || (cfg.getFlags() & NetFlags.IFF_LOOPBACK) != 0
                    || NetFlags.NULL_HWADDR.equals(cfg.getHwaddr())) {
                    continue;
                }
                long rxbps = 0L;

                try {
                    long start = System.currentTimeMillis();
                    NetInterfaceStat statStart = sigar.getNetInterfaceStat(name);
                    long rxBytesStart = statStart.getRxBytes();
                    Thread.sleep(1000);
                    long end = System.currentTimeMillis();
                    NetInterfaceStat statEnd = sigar.getNetInterfaceStat(name);
                    long rxBytesEnd = statEnd.getRxBytes();
                    rxbps = (rxBytesEnd - rxBytesStart)*8/(end-start)*1000;
                    bandwidth = CapacityUtil.toKB(new BigDecimal(rxbps).toBigInteger(), 2);
                    bandwidth = bandwidth.substring(0, bandwidth.length() - 2);
                    break;
                }catch(Exception ex){
                    logger.error("getBandwidth() exception:" + ex.getMessage());
                }                
            }
            return bandwidth;
        } catch (Exception ex) {
            logger.error("getBandwidth():" + ex.getMessage());
            return bandwidth;
        }
    }
    
    /**
     * 查找指定目录是否有包含关键字和pid的文件名
     * @param path
     * @param keyword
     * @return
     */
    private boolean searchFileByKeyword(String path, String keyword){
        File file = new File(path);
        File[] fileArray = null;
        if(file != null && file.isDirectory()){
            fileArray = file.listFiles();
            for(File f1:fileArray ){
                String fileName = f1.getName();
                if (fileName.indexOf(keyword) != -1 && fileName.indexOf("pid") != -1) {
                    return true;
                }
            }
        }
        return false;
    }
}

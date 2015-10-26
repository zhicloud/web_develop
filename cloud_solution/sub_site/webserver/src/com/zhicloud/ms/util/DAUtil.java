package com.zhicloud.ms.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.NetFlags;
import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.OperatingSystem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarNotImplementedException;
import org.hyperic.sigar.Swap;
import org.hyperic.sigar.Who;

import com.zhicloud.ms.vo.NetworkInfoVO;

public class DAUtil {

    public static void main(String[] args) throws Exception {
//        System.err.println(getCpuCount());
//        System.err.println("");
//
//        testCpuPerc();
//        System.err.println("");
//
//        testMem();
//        System.err.println("");
//
//        System.err.println(getPlatformName());
//        System.err.println("");
//
//        testGetOSInfo();
//        System.err.println("");
//
//        testWho();
//        System.err.println("");

//        testFileSystemInfo();
//        System.err.println("");
//
//        System.err.println(getFQDN());
//
//        System.err.println("");
//
//        System.err.println(getMAC());
//
//        testNetIfList();
//
//        System.err.println("");
//
        getEthernetInfo();


    }

    private static int getCpuCount() throws SigarException {
        Sigar sigar = new Sigar();
        CpuInfo infos[] = sigar.getCpuInfoList();
        for (int i = 0; i < infos.length; i++) {//不管是单块CPU还是多CPU都适用
            CpuInfo info = infos[i];
            System.err.println("mhz=" + info.getMhz());//CPU的总量MHz
            System.err.println("vendor=" + info.getVendor());//获得CPU的卖主，如：Intel
            System.err.println("model=" + info.getModel());//获得CPU的类别，如：Celeron
            System.err.println("cache size=" + info.getCacheSize());//缓冲存储器大小
            System.err.println("");
        }

        try {
            return sigar.getCpuInfoList().length;
        } finally {
            sigar.close();
        }
    }

    public static void testCpuPerc() {
        Sigar sigar = new Sigar();

        CpuPerc cpu;
        try {
            cpu = sigar.getCpuPerc();
            printCpuPerc(cpu);
        } catch (SigarException e) {
            e.printStackTrace();
        }

        System.err.println("-----------------------------------");
        CpuPerc cpuList[] = null;
        try {
            cpuList = sigar.getCpuPercList();
        } catch (SigarException e) {
            e.printStackTrace();
            return;
        }
        for (int i = 0; i < cpuList.length; i++) {
            printCpuPerc(cpuList[i]);
        }
    }

    private static void printCpuPerc(CpuPerc cpu) {
        System.err.println("User :" + CpuPerc.format(cpu.getUser()));// 用户使用率
        System.err.println("Sys :" + CpuPerc.format(cpu.getSys()));// 系统使用率
        System.err.println("Wait :" + CpuPerc.format(cpu.getWait()));// 当前等待率
        System.err.println("Nice :" + CpuPerc.format(cpu.getNice()));//
        System.err.println("Idle :" + CpuPerc.format(cpu.getIdle()));// 当前空闲率
        System.err.println("Total :" + CpuPerc.format(cpu.getCombined()));// 总的使用率
        System.err.println("");
    }

    public static void testMem() throws SigarException {
        Sigar sigar = new Sigar();
        Mem mem = sigar.getMem();
        DecimalFormat df = new DecimalFormat("#");

        // 内存总量
        System.err.println("Total = " + CapacityUtil.toGB(new BigDecimal(mem.getTotal()).toBigInteger(), 2) + " total");
        // 当前内存使用量
        System.err.println("Used = " + CapacityUtil.toGB(new BigDecimal(mem.getUsed()).toBigInteger(), 2) + " used");

        System.err.println("Used Percent= " + df.format(mem.getUsedPercent()));

        // 当前内存剩余量
        System.err.println("Free = " + CapacityUtil.toGB(new BigDecimal(mem.getFree()).toBigInteger(), 2) + " free");

        System.err.println("Free Percent = " + df.format(mem.getFreePercent()));


        System.err.println("");

        Swap swap = sigar.getSwap();
        // 交换区总量
        System.err.println("Total = " + CapacityUtil.toGB(new BigDecimal(swap.getTotal()).toBigInteger(), 2) + " total");
        // 当前交换区使用量
        System.err.println("Used = " + CapacityUtil.toGB(new BigDecimal(swap.getFree()).toBigInteger(), 2) + " used");
        // 当前交换区剩余量
        System.err.println("Free = " + CapacityUtil.toGB(new BigDecimal(swap.getUsed()).toBigInteger(), 2) + " free");
        System.err.println("");
    }

    private static String getPlatformName() {
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

    public static void testGetOSInfo() {
        OperatingSystem OS = OperatingSystem.getInstance();
        // 操作系统内核类型如： 386、486、586等x86
        System.err.println("OS.getArch() = " + OS.getArch());
        System.err.println("OS.getCpuEndian() = " + OS.getCpuEndian());
        System.err.println("OS.getDataModel() = " + OS.getDataModel());
        // 系统描述
        System.err.println("OS.getDescription() = " + OS.getDescription());
        System.err.println("OS.getMachine() = " + OS.getMachine());
        // 操作系统类型
        System.err.println("OS.getName() = " + OS.getName());
        System.err.println("OS.getPatchLevel() = " + OS.getPatchLevel());
        // 操作系统的卖主
        System.err.println("OS.getVendor() = " + OS.getVendor());
        // 卖主名称
        System.err.println("OS.getVendorCodeName() = " + OS.getVendorCodeName());
        // 操作系统名称
        System.err.println("OS.getVendorName() = " + OS.getVendorName());
        // 操作系统卖主类型
        System.err.println("OS.getVendorVersion() = " + OS.getVendorVersion());
        // 操作系统的版本号
        System.err.println("OS.getVersion() = " + OS.getVersion());
    }


    public static void testWho() {
        try {
            Sigar sigar = new Sigar();
            Who who[] = sigar.getWhoList();
            if (who != null && who.length > 0) {
                for (int i = 0; i < who.length; i++) {
                    System.err.println("\n~~~~~~~~~" + String.valueOf(i) + "~~~~~~~~~");
                    Who _who = who[i];
                    System.err.println ("getDevice() = " + _who.getDevice());
                    System.err.println ("getHost() = " + _who.getHost());
                    System.err.println ("getTime() = " + _who.getTime());
                    //当前系统进程表中的用户名
                    System.err.println ("getUser() = " + _who.getUser());
                }
            }
        } catch (SigarException e) {
            e.printStackTrace();
        }
    }

    public static void testFileSystemInfo() throws Exception {
        Sigar sigar = new Sigar();
        FileSystem fslist[] = sigar.getFileSystemList();
        String dir = System.getProperty("user.home");//当前用户文件夹路径
        for (int i = 0; i < fslist.length; i++) {
            System.err.println("\n~~~~~~~~~~" + i + "~~~~~~~~~~");
            FileSystem fs = fslist[i];
            // 分区的盘符名称
            System.err.println("fs.getDevName() = " + fs.getDevName());
            // 分区的盘符名称
            System.err.println("fs.getDirName() = " + fs.getDirName());
            System.err.println("fs.getFlags() = " + fs.getFlags());//
            // 文件系统类型，比如 FAT32、NTFS
            System.err.println("fs.getSysTypeName() = " + fs.getSysTypeName());
            // 文件系统类型名，比如本地硬盘、光驱、网络文件系统等
            System.err.println("fs.getTypeName() = " + fs.getTypeName());
            // 文件系统类型
            System.err.println("fs.getType() = " + fs.getType());
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
                    System.err.println(" Total = " + CapacityUtil.toGB(new BigDecimal(usage.getTotal()).toBigInteger(), 2));
                    // 文件系统剩余大小
                    System.err.println(" Free = " + CapacityUtil.toGB(new BigDecimal(usage.getFree()).toBigInteger(), 2));
                    // 文件系统可用大小
                    System.err.println(" Avail = " + CapacityUtil.toGB(new BigDecimal(usage.getAvail()).toBigInteger(), 2));
                    // 文件系统已经使用量
                    System.err.println(" Used = " + CapacityUtil.toGB(new BigDecimal(usage.getUsed()).toBigInteger(), 2));
                    double usePercent = usage.getUsePercent() * 100D;
                    DecimalFormat df = new DecimalFormat("#");
                    // 文件系统资源的利用率
                    System.err.println(" Usage = " + df.format(usePercent) + "%");
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
            System.err.println(" DiskReads = " + usage.getDiskReads());
            System.err.println(" DiskWrites = " + usage.getDiskWrites());
        }
        return;
    }

    public static String getFQDN(){
        try {
            return InetAddress.getLocalHost().getCanonicalHostName();
        } catch (UnknownHostException e) {
            Sigar sigar = new Sigar();
            try {
                return sigar.getFQDN();
            } catch (SigarException ex) {
                return null;
            } finally {
                sigar.close();
            }
        }
    }

    public static String getMAC() {
        Sigar sigar = null;
        try {
            sigar = new Sigar();
            String[] ifaces = sigar.getNetInterfaceList();
            String hwaddr = null;
            for (int i = 0; i < ifaces.length; i++) {
                NetInterfaceConfig cfg = sigar.getNetInterfaceConfig(ifaces[i]);
                if (NetFlags.LOOPBACK_ADDRESS.equals(cfg.getAddress())
                    || (cfg.getFlags() & NetFlags.IFF_LOOPBACK) != 0
                    || NetFlags.NULL_HWADDR.equals(cfg.getHwaddr())) {
                    continue;
                }

                hwaddr = cfg.getHwaddr();
                break;
            }
            return hwaddr != null ? hwaddr : null;
        } catch (Exception e) {
            return null;
        } finally {
            if (sigar != null)
                sigar.close();
        }
    }

    public static void testNetIfList() throws Exception {
        Sigar sigar = new Sigar();
        String ifNames[] = sigar.getNetInterfaceList();
        for (int i = 0; i < ifNames.length; i++) {
            String name = ifNames[i];
            NetInterfaceConfig ifconfig = sigar.getNetInterfaceConfig(name);
            System.err.println("name = " + name);//网络设备名
            System.err.println("Address = " + ifconfig.getAddress());//IP地址
            System.err.println("Netmask = " + ifconfig.getNetmask());//子网掩码
//            System.err.println("");
            populate(sigar, name);

            if ( (ifconfig.getFlags() & 1L) <= 0L) {
                System.err.println("!IFF_UP...skipping getNetInterfaceStat");
                continue;
            }
            try {
                NetInterfaceStat ifstat = sigar.getNetInterfaceStat(name);
                System.err.println("RxPackets = " + ifstat.getRxPackets());//接收的总包裹数
                System.err.println("TxPackets = " + ifstat.getTxPackets());//发送的总包裹数
                System.err.println("RxBytes = " + ifstat.getRxBytes());//接收到的总字节数
                System.err.println("TxBytes = " + ifstat.getTxBytes());//发送的总字节数
                System.err.println("RxErrors = " + ifstat.getRxErrors());//接收到的错误包数
                System.err.println("TxErrors = " + ifstat.getTxErrors());//发送数据包时的错误数
                System.err.println("RxDropped = " + ifstat.getRxDropped());//接收时丢弃的包数
                System.err.println("TxDropped = " + ifstat.getTxDropped());//发送时丢弃的包数
                System.err.println("");

            } catch (SigarNotImplementedException e) {
                e.printStackTrace();
            } catch (SigarException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<NetworkInfoVO> getEthernetInfo(){
    	Process pro;
    	Map<String,String> gatewayMap = new HashMap<>();
		try {
			pro = Runtime.getRuntime().exec("route -n");
			BufferedReader br = new BufferedReader(new InputStreamReader(pro.getInputStream()));
	        List<String> rowList = new ArrayList<>();
	        String temp;
	        while((temp = br.readLine()) != null){
	            rowList.add(temp);
	        }
	        for (String string : rowList) {
                Matcher mc = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}").matcher(string);
                //查询第一个匹配的
                if(mc.find()){
                	//判断是否为全0，如果全0则认为第二个为默认网关
                	if(mc.group().equals("0.0.0.0")){
                		String s = string.trim();
                		mc.find(1);
                		//获取网卡名称，存入Map
                		gatewayMap.put(s.substring(s.indexOf("eth")),mc.group());
                	}else{
                		continue;
                	}
                }else{
                    continue;
                }
	        }
	        br.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
        Sigar sigar = null;
        List<NetworkInfoVO> niList = new ArrayList<>();
        try {
            sigar = new Sigar();
            String[] ifaces = sigar.getNetInterfaceList();
            for (int i = 0; i < ifaces.length; i++) {
                NetInterfaceConfig cfg = sigar.getNetInterfaceConfig(ifaces[i]);
                if (NetFlags.LOOPBACK_ADDRESS.equals(cfg.getAddress()) || NetFlags.ANY_ADDR.equals(cfg.getAddress())
                    || (cfg.getFlags() & NetFlags.IFF_LOOPBACK) != 0
                    || NetFlags.NULL_HWADDR.equals(cfg.getHwaddr())) {
                    continue;
                }
                String command = "ethtool "+cfg.getName()+" | grep Speed | awk '{print $2}'";
                String []cmd = new String[]{"/bin/sh", "-c", command};
                Process pro1 = Runtime.getRuntime().exec(cmd);
    			BufferedReader br = new BufferedReader(new InputStreamReader(pro1.getInputStream()));
    	        List<String> rList = new ArrayList<>();
    	        String temp;
    	        while((temp = br.readLine()) != null){
    	            rList.add(temp);
    	        }
                NetworkInfoVO ni = new NetworkInfoVO();
                ni.setGateway(gatewayMap.get(cfg.getName())==null?"":gatewayMap.get(cfg.getName()));
                ni.setIp(cfg.getAddress()==null?"":cfg.getAddress());
                ni.setName(cfg.getName());
                ni.setNetmask(cfg.getNetmask()==null?"":cfg.getNetmask());
                ni.setMac(cfg.getHwaddr());
                ni.setSpeed(rList.get(0)==null?"0Mb/s":rList.get(0));
                niList.add(ni);
                br.close();
                /*System.err.println("cfg.getAddress() = " + cfg.getAddress());//IP地址
                System.err.println("cfg.getBroadcast() = " + cfg.getBroadcast());//网关广播地址
                System.err.println("cfg.getHwaddr() = " + cfg.getHwaddr());//网卡MAC地址
                System.err.println("cfg.getNetmask() = " + cfg.getNetmask());//子网掩码
                System.err.println("cfg.getDescription() = " + cfg.getDescription());//网卡描述信息
                System.err.println("cfg.getType() = " + cfg.getType());//
                System.err.println("cfg.getDestination() = " + cfg.getDestination());
                System.err.println("cfg.getFlags() = " + cfg.getFlags());//
                System.err.println("cfg.getMetric() = " + cfg.getMetric());
                System.err.println("cfg.getMtu() = " + cfg.getMtu());
                System.err.println("cfg.getName() = " + cfg.getName());
                System.err.println();*/
            }
            
            return niList;
        } catch (Exception e) {
        	System.err.println("Error while creating GUID" + e);
        	e.printStackTrace();
        	return null;
        } finally {
            if (sigar != null)
                sigar.close();
        }
    }

    public static void populate(Sigar sigar, String name) throws SigarException {
        long rxbps = 0L;
        long txbps = 0L;
        NetInterfaceStat stat;

        try {

            long start = System.currentTimeMillis();
            NetInterfaceStat statStart = sigar.getNetInterfaceStat(name);
            long rxBytesStart = statStart.getRxBytes();
            long txBytesStart = statStart.getTxBytes();
            Thread.sleep(1000);
            long end = System.currentTimeMillis();
            NetInterfaceStat statEnd = sigar.getNetInterfaceStat(name);
            long rxBytesEnd = statEnd.getRxBytes();
            long txBytesEnd = statEnd.getTxBytes();
            rxbps = (rxBytesEnd - rxBytesStart)*8/(end-start)*1000;
            txbps = (txBytesEnd - txBytesStart)*8/(end-start)*1000;
            stat = sigar.getNetInterfaceStat(name);

            System.err.println("rxbps = " + CapacityUtil.toKB(new BigDecimal(rxbps).toBigInteger(), 2) + "/s");
            System.err.println("txbps = " + CapacityUtil.toKB(new BigDecimal(txbps).toBigInteger(), 2) + "/s");
            System.err.println("stat = " + stat);
        } catch (SigarException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}

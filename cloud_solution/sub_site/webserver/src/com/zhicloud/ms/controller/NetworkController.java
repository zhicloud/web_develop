package com.zhicloud.ms.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.util.DAUtil;
import com.zhicloud.ms.vo.NetworkInfoVO;

@Controller
@RequestMapping("/net")
public class NetworkController {
    /* 日志 */
    public static final Logger logger = Logger.getLogger(NetworkController.class);
    
    @RequestMapping(value="/all",method=RequestMethod.GET)
    public String networkManagePage(Model model){
    	List<NetworkInfoVO> niList = DAUtil.getEthernetInfo();
    	model.addAttribute("niList", niList);
    	return "network/network_manage";
    }
    
    /**
     * 手动配置网卡
     * @param data
     * @return
     */
    @RequestMapping(value="/modifymanual",method=RequestMethod.POST)
    @ResponseBody
    public MethodResult modify(@RequestParam("data[]") String[] data){
    	//网络配置名
    	String name = data[0];
    	//网卡配置文件目录
    	String basePath = "/etc/sysconfig/network-scripts/ifcfg-";
    	//配置文件名称
    	String filePath = basePath + name;
    	//配置文件
    	File confFile = new File(filePath);
    	String line = "";
    	//存储读取的每行数据
    	StringBuilder sb = new StringBuilder();
    	//标志手动配置的项是否存在，避免重复输入
    	boolean isExist = false;
    	try {
			FileReader fr = new FileReader(confFile);
			BufferedReader br = new BufferedReader(fr);
			while((line = br.readLine())!=null){
				//判断是否为DHCP，如果是则改为手动配置
				if(line.contains("BOOTPROTO") && line.contains("dhcp")){
					sb.append("BOOTPROTO=static\n");
					//输入手动配置的信息
					sb.append("IPADDR="+data[1]+"\n");
					sb.append("NETMASK="+data[2]+"\n");
					sb.append("GATEWAY="+data[3]+"\n");
					isExist = true;
				}else{//如果不是则直接修改配置信息
					//如果该配置项已存在，则不再重复配置
					if(isExist){
						if(line.contains("IPADDR") || line.contains("NETMASK") || line.contains("GATEWAY")){
							continue;
						}
					}
					if(line.contains("IPADDR")){
						sb.append("IPADDR="+data[1]+"\n");
					}
					else if(line.contains("NETMASK")){
						sb.append("NETMASK="+data[2]+"\n");
					}
					else if(line.contains("GATEWAY")){
						sb.append("GATEWAY="+data[1]+"\n");
					}
					else {
						sb.append(line+"\n");
					}
				}
			}
			br.close();
			
			FileWriter fw = new FileWriter(confFile);
			fw.write(sb.toString());
			fw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return new MethodResult(MethodResult.FAIL,"配置修改失败");
		} catch (IOException e) {
			e.printStackTrace();
			return new MethodResult(MethodResult.FAIL,"配置修改失败");
		}
    	return new MethodResult(MethodResult.SUCCESS,"配置修改成功");
    }
    
    
    /**
     * DHCP
     * @param name
     * @return
     */
    @RequestMapping(value="/modifydhcp",method=RequestMethod.POST)
    @ResponseBody
    public MethodResult modify(@RequestParam("name") String name){
    	//网卡配置文件目录
    	String basePath = "/etc/sysconfig/network-scripts/ifcfg-";
    	//配置文件名称
    	String filePath = basePath + name;
    	//配置文件
    	File confFile = new File(filePath);
    	String line = "";
    	//存储读取的每行数据
    	StringBuilder sb = new StringBuilder();
    	try {
			FileReader fr = new FileReader(confFile);
			BufferedReader br = new BufferedReader(fr);
			while((line = br.readLine())!=null){
				//判断是否为DHCP，如果是则改为手动配置
				if(line.contains("BOOTPROTO") && line.contains("dhcp")){
					br.close();
					return new MethodResult(MethodResult.SUCCESS,"修改成功");
				}else{//如果不是则直接修改配置信息
					if(line.contains("BOOTPROTO")){
						sb.append("BOOTPROTO=dhcp\n");
					}
					else {
						sb.append(line+"\n");
					}
				}
			}
			br.close();
			FileWriter fw = new FileWriter(confFile);
			fw.write(sb.toString());
			fw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return new MethodResult(MethodResult.FAIL,"修改失败");
		} catch (IOException e) {
			e.printStackTrace();
			return new MethodResult(MethodResult.FAIL,"修改失败");
		}
    	return new MethodResult(MethodResult.SUCCESS,"修改成功");
    }
    
    @RequestMapping(value="/restart",method=RequestMethod.GET)
    @ResponseBody
    public MethodResult restart(){
    	try {
			Process process = Runtime.getRuntime().exec("/etc/init.d/network restart");
			int result = process.waitFor();
			if(0 != result){
				return new MethodResult(MethodResult.FAIL,"网络接口重启失败");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			return new MethodResult(MethodResult.FAIL,"网络接口重启失败");
		} catch (IOException e) {
			e.printStackTrace();
			return new MethodResult(MethodResult.FAIL,"网络接口重启失败");
		}
    	return new MethodResult(MethodResult.SUCCESS,"网络接口重启成功");
    }
}

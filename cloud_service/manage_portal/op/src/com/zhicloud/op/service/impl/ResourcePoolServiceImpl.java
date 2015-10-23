/**
 * Project Name:op
 * File Name:VpnServiceImpl.java
 * Package Name:com.zhicloud.op.service.impl
 * Date:2015年4月1日下午2:16:34
 * 
 *
*/ 

package com.zhicloud.op.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.zhicloud.op.app.helper.LoginHelper;
import com.zhicloud.op.app.helper.ServiceHelper;
import com.zhicloud.op.app.pool.addressPool.AddressExt;
import com.zhicloud.op.app.pool.addressPool.AddressPool;
import com.zhicloud.op.app.pool.addressPool.AddressPoolManager;
import com.zhicloud.op.app.pool.portPool.Port;
import com.zhicloud.op.exception.AppException;
import com.zhicloud.op.httpGateway.HttpGatewayChannel;
import com.zhicloud.op.httpGateway.HttpGatewayChannelExt;
import com.zhicloud.op.httpGateway.HttpGatewayManager;
import com.zhicloud.op.remote.BeanDirectCallableDefaultImpl;
import com.zhicloud.op.remote.Callable;
import com.zhicloud.op.service.ResourcePoolService;
import com.zhicloud.op.vo.AllResourceVO;
import com.zhicloud.op.vo.ComputerPoolVO;
import com.zhicloud.op.vo.StoragePoolVO;
 

public class ResourcePoolServiceImpl extends BeanDirectCallableDefaultImpl implements ResourcePoolService {


	@Callable
	public void queryComputerPool(HttpServletRequest request,HttpServletResponse response) {
		try {
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json; charset=utf-8");
			List<ComputerPoolVO> cList = new ArrayList<>();
			for(int region=1;region<5;region++){
				if(region==3){
					continue;
				}
				HttpGatewayChannelExt channel;
				try {
					channel = HttpGatewayManager.getChannel(region);
				} catch (Exception e1) {
					continue;
				}
				if(channel!=null){
					JSONObject result;
					try {
						result = channel.computePoolQuery();
					} catch (Exception e) {
						continue;
					}
					JSONArray computerList = result.getJSONArray("compute_pools");
					for (int i = 0; i < computerList.size(); i ++) {
						JSONObject computerObject = computerList.getJSONObject(i);
						String uuid = computerObject.getString("uuid");
						String name = computerObject.getString("name");
						int status = computerObject.getInt("status");
						Integer cpuCount = computerObject.getInt("cpu_count");
						BigDecimal cpuUsage = new BigDecimal(computerObject.getString("cpu_usage"));
						BigDecimal memoryUsage = new BigDecimal(computerObject.getString("memory_usage"));
						BigDecimal diskUsage = new BigDecimal(computerObject.getString("disk_usage"));
						
						JSONArray memoryList = computerObject.getJSONArray("memory");
						BigInteger[] mcount = new BigInteger[memoryList.size()];
						for(int j=0;j<memoryList.size();j++){
							mcount[j] = new BigInteger(memoryList.getString(j));
						}
						
						JSONArray diskList = computerObject.getJSONArray("disk_volume");
						BigInteger[] dcount = new BigInteger[diskList.size()];
						for(int j=0;j<diskList.size();j++){
							dcount[j] = new BigInteger(diskList.getString(j));
						}
						
						JSONArray nList = computerObject.getJSONArray("node");
						Integer[] ncount = new Integer[nList.size()];
						for(int j=0;j<nList.size();j++){
							ncount[j] = nList.getInt(j);
						}
						
						JSONArray hList = computerObject.getJSONArray("host");
						Integer[] hcount = new Integer[hList.size()];
						for(int j=0;j<hList.size();j++){
							hcount[j] = hList.getInt(j);
						}
						ComputerPoolVO computer = new ComputerPoolVO();
						computer.setCpuCount(cpuCount);
						computer.setCpuUsage(cpuUsage);
						computer.setDiskUsage(diskUsage);
						computer.setDiskVolume(dcount);
						computer.setHost(hcount);
						computer.setMemory(mcount);
						computer.setMemoryUsage(memoryUsage);
						computer.setName(name);
						computer.setNode(ncount);
						computer.setStatus(status);
						computer.setUuid(uuid);
						computer.setRegion(region);
						cList.add(computer);
					}
				}
			}
			
			int total = cList.size();
			// 输出json数据
			ServiceHelper.writeDatagridResultAsJsonTo(response.getOutputStream(), total, cList);
		} catch (Exception e) {
			throw new AppException("失败");
		}
	}

	@Callable
	public void queryStoragePool(HttpServletRequest request,HttpServletResponse response) {
		try {
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json; charset=utf-8");
			List<StoragePoolVO> cList = new ArrayList<>();
			for(int region=1;region<5;region++){
				if(region==3){
					continue;
				}
				HttpGatewayChannelExt channel;
				try {
					channel = HttpGatewayManager.getChannel(region);
				} catch (Exception e1) {
					continue;
				}
				if(channel!=null){
					JSONObject result;
					try {
						result = channel.storagePoolQuery();
					} catch (Exception e) {
						continue;
					}
					if(result.getString("status").equals("fail")){
						continue;
					}
					JSONArray allDiskList = result.getJSONArray("storagePools");
					for (int i = 0; i < allDiskList.size(); i ++) {
						JSONObject diskObject = allDiskList.getJSONObject(i);
						String uuid = diskObject.getString("uuid");
						String name = diskObject.getString("name");
						int status = diskObject.getInt("status");
						Integer cpuCount = diskObject.getInt("cpu_count");
						BigDecimal cpuUsage = new BigDecimal(diskObject.getString("cpu_usage"));
						BigDecimal memoryUsage = new BigDecimal(diskObject.getString("memory_usage"));
						BigDecimal diskUsage = new BigDecimal(diskObject.getString("disk_usage"));
						
						JSONArray memoryList = diskObject.getJSONArray("memory");
						BigInteger[] mcount = new BigInteger[memoryList.size()];
						for(int j=0;j<memoryList.size();j++){
							mcount[j] = new BigInteger(memoryList.getString(j));
						}
						
						JSONArray diskList = diskObject.getJSONArray("disk_volume");
						BigInteger[] dcount = new BigInteger[diskList.size()];
						for(int j=0;j<diskList.size();j++){
							dcount[j] = new BigInteger(diskList.getString(j));
						}
						
						JSONArray nList = diskObject.getJSONArray("node");
						Integer[] ncount = new Integer[nList.size()];
						for(int j=0;j<nList.size();j++){
							ncount[j] = nList.getInt(j);
						}
						
						JSONArray hList = diskObject.getJSONArray("disk");
						Integer[] hcount = new Integer[hList.size()];
						for(int j=0;j<hList.size();j++){
							hcount[j] = hList.getInt(j);
						}
						StoragePoolVO diskPool = new StoragePoolVO();
						diskPool.setCpuCount(cpuCount);
						diskPool.setCpuUsage(cpuUsage);
						diskPool.setDiskUsage(diskUsage);
						diskPool.setDiskVolume(dcount);
						diskPool.setDisk(hcount);
						diskPool.setMemory(mcount);
						diskPool.setMemoryUsage(memoryUsage);
						diskPool.setName(name);
						diskPool.setNode(ncount);
						diskPool.setStatus(status);
						diskPool.setUuid(uuid);
						diskPool.setRegion(region);
						cList.add(diskPool);
					}
				}
			}
			
			int total = cList.size();
			// 输出json数据
			ServiceHelper.writeDatagridResultAsJsonTo(response.getOutputStream(), total, cList);
		} catch (Exception e) {
			throw new AppException("失败");
		}
	}

	@Callable
	public void queryAddressPool(HttpServletRequest request,HttpServletResponse response) {
		try {
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json; charset=utf-8");
			AddressPool pool1 = AddressPoolManager.singleton().getPool("1");
			AddressPool pool2 = AddressPoolManager.singleton().getPool("2");
			AddressPool pool3 = AddressPoolManager.singleton().getPool("3");
			AddressPool pool4 = AddressPoolManager.singleton().getPool("4");
			List<AddressExt> addressList = new ArrayList<>();
			if(pool1!=null){
				List<AddressExt> addressList1 = pool1.getAll();
				addressList.addAll(addressList1);
			}
			if(pool2!=null){
				List<AddressExt> addressList2 = pool2.getAll();
				addressList.addAll(addressList2);
			}
			if(pool3!=null){
				List<AddressExt> addressList3 = pool3.getAll();
				addressList.addAll(addressList3);
			}
			if(pool4!=null){
				List<AddressExt> addressList4 = pool4.getAll();
				addressList.addAll(addressList4);
			}
			int total = addressList.size();
			// 输出json数据
			ServiceHelper.writeDatagridResultAsJsonTo(response.getOutputStream(), total, addressList);
		} catch (Exception e) {
			throw new AppException("失败");
		}
	}

	@Callable
	public void queryPortPool(HttpServletRequest request,HttpServletResponse response) {
		try {
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json; charset=utf-8");
			List<Port> pList = new ArrayList<>();
			for(int region=1;region<5;region++){
				HttpGatewayChannel channel;
				try {
					channel = HttpGatewayManager.getChannel(region);
				} catch (Exception e1) {
					continue;
				}
				if(channel!=null){
					JSONObject result;
					try {
						result = channel.portPoolQuery();
					} catch (Exception e) {
						continue;
					}
					JSONArray portList = result.getJSONArray("portPools");
					for (int i = 0; i < portList.size(); i ++) {
						JSONObject portObject = portList.getJSONObject(i);
						String uuid = portObject.getString("uuid");
						String name = portObject.getString("name");
						int status = portObject.getInt("status");
						JSONArray countList = portObject.getJSONArray("count");
						int[] count = new int[countList.size()];
						for(int j=0;j<countList.size();j++){
							count[j] = countList.getInt(j);
						}
						
						Port p = new Port();
						p.setName(name);
						p.setUuid(uuid);
						p.setStatus(status);
						p.setCount(count);
						p.setRegion(region);
						pList.add(p);
					}
				}
			}
			int total = pList.size();
			// 输出json数据
			ServiceHelper.writeDatagridResultAsJsonTo(response.getOutputStream(), total, pList);
		} catch (Exception e) {
			throw new AppException("失败");
		}
	}

	@Callable
	public String computerPoolPage(HttpServletRequest request,HttpServletResponse response) {
		request.setAttribute("userType", LoginHelper.getLoginInfo(request).getUserType());
		return "/security/operator/resource_pool_computer_manage.jsp";
	}

	@Callable
	public String storagePoolPage(HttpServletRequest request,HttpServletResponse response) {
		request.setAttribute("userType", LoginHelper.getLoginInfo(request).getUserType());
		return "/security/operator/resource_pool_storage_manage.jsp";
	}

	@Callable
	public String addressPoolPage(HttpServletRequest request,HttpServletResponse response) {
		request.setAttribute("userType", LoginHelper.getLoginInfo(request).getUserType());
		return "/security/operator/resource_pool_address_manage.jsp";
	}

	@Callable
	public String portPoolPage(HttpServletRequest request,HttpServletResponse response) {
		request.setAttribute("userType", LoginHelper.getLoginInfo(request).getUserType());
		return "/security/operator/resource_pool_port_manage.jsp";
	}
	
	public List<AllResourceVO> getAllResource(){
		List<AllResourceVO> AllList = new ArrayList<AllResourceVO>();
		for(int region=1;region<5;region++){
			HttpGatewayChannel channel;
			try {
				channel = HttpGatewayManager.getChannel(region);
			} catch (Exception e1) {
				continue;
			}
			if(channel!=null){
				JSONObject result;
				try {
					result = channel.resourcePoolQuery();
				} catch (Exception e) {
					continue;
				}
				JSONArray portList = result.getJSONArray("portPools");
				for (int i = 0; i < portList.size(); i ++) {
					JSONObject portObject = portList.getJSONObject(i);
					String uuid = portObject.getString("uuid");
					String name = portObject.getString("name");
					int status = portObject.getInt("status");
					JSONArray countList = portObject.getJSONArray("count");
					int[] count = new int[countList.size()];
					for(int j=0;j<countList.size();j++){
						count[j] = countList.getInt(j);
					}
					
					Port p = new Port();
					p.setName(name);
					p.setUuid(uuid);
					p.setStatus(status);
					p.setCount(count);
//					pList.add(p);
				}
			}
		}
		return null;
	}
}
 

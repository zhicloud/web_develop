package com.zhicloud.op.app.helper;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.zhicloud.op.app.propeties.AppProperties;
import com.zhicloud.op.common.util.CapacityUtil;
import com.zhicloud.op.common.util.DateUtil;
import com.zhicloud.op.common.util.FlowUtil;
import com.zhicloud.op.core.CoreSpringContextManager;
import com.zhicloud.op.service.PackageOptionService;
import com.zhicloud.op.vo.PackagePriceVO;

/**
 * @author ZYFTMX
 *
 */
public class CloudHostPrice
{
	private static PackageOptionService packageOptionService = CoreSpringContextManager.getPackageOptionService();
	private static final Map<String, BigDecimal> CPU__MONTHLY_PRICE = new LinkedHashMap<String, BigDecimal>();
	
	/**
	 * @param region
	 * @param cpu
	 * @param price
	 * 向Map中添加cpu和对应价格
	 */
	private static void putCpuMonthlyPrice(Integer region,Integer priceStatus, Integer cpu, BigDecimal price)
	{
		String key = region.toString()+priceStatus.toString()+cpu.toString();
		CPU__MONTHLY_PRICE.put(key, price);
	}
	
	
	/**
	 * @param region
	 * @param cpu
	 * @return
	 * 根据region和cpu核数组成的key获取对应价格
	 */
	public static BigDecimal getCpuMonthlyPrice(Integer region,Integer priceStatus, Integer cpu)
	{
		String key = region.toString()+priceStatus.toString()+cpu.toString();
		return CPU__MONTHLY_PRICE.get(key);
	}
	
	
	/**
	 * @param region
	 * @param cpu
	 * @return
	 * 
	 * 通过(每月的价格 / 30 / 24) 获取每小时cpu每小时的价格
	 */
	public static BigDecimal getCpuHourlyPrice(Integer region,Integer priceStatus, Integer cpu)
	{
		return divide(divide(getCpuMonthlyPrice(region,priceStatus, cpu), new BigDecimal(30)), new BigDecimal(24));
	}
	//初始化cpu的Map
	static {
		//从数据库获取所有cpu的选项
		List<PackagePriceVO> cpuList = packageOptionService.getPackagePrice("1");
		if(cpuList!=null && cpuList.size()>0){
			for(PackagePriceVO packagePrice : cpuList){
				//根据地域与cpu核数组成key并将对应价格存入Map
				if(packagePrice.getPriceStatus()==1){
					putCpuMonthlyPrice(packagePrice.getRegion(),3,packagePrice.getCpuCore(), packagePrice.getPrice());
					putCpuMonthlyPrice(packagePrice.getRegion(),2,packagePrice.getCpuCore(), packagePrice.getVpcPrice());
				}else if(packagePrice.getPriceStatus()==2){
					putCpuMonthlyPrice(packagePrice.getRegion(),2,packagePrice.getCpuCore(), packagePrice.getVpcPrice());
				}else {
					putCpuMonthlyPrice(packagePrice.getRegion(),3,packagePrice.getCpuCore(), packagePrice.getPrice());
				}
			}
		}
		putCpuMonthlyPrice(1,2,0, new BigDecimal(0));
		putCpuMonthlyPrice(1,3,0, new BigDecimal(0));
		putCpuMonthlyPrice(2,2,0, new BigDecimal(0));
		putCpuMonthlyPrice(2,3,0, new BigDecimal(0));
		putCpuMonthlyPrice(3,2,0, new BigDecimal(0));
		putCpuMonthlyPrice(3,3,0, new BigDecimal(0));
		putCpuMonthlyPrice(4,2,0, new BigDecimal(0));
		putCpuMonthlyPrice(4,3,0, new BigDecimal(0));
//		putCpuMonthlyPrice(4,0, new BigDecimal(0));
//		putCpuMonthlyPrice(4,1, new BigDecimal(48));
//		putCpuMonthlyPrice(4,2, new BigDecimal(128));
//		putCpuMonthlyPrice(4,4, new BigDecimal(240));
//		putCpuMonthlyPrice(4,8, new BigDecimal(512));
//		putCpuMonthlyPrice(4,12, new BigDecimal(800));
//		putCpuMonthlyPrice(4,16, new BigDecimal(1280));
//		putCpuMonthlyPrice(0,0, new BigDecimal(0));
//		putCpuMonthlyPrice(0,1, new BigDecimal(30));
//		putCpuMonthlyPrice(0,2, new BigDecimal(80));
//		putCpuMonthlyPrice(0,4, new BigDecimal(150));
//		putCpuMonthlyPrice(0,8, new BigDecimal(320));
//		putCpuMonthlyPrice(0,12, new BigDecimal(500));
//		putCpuMonthlyPrice(0,16, new BigDecimal(800));
	}
	//-------------------------
	private static final Map<String, BigDecimal> MEMORY__MONTHLY_PRICE = new LinkedHashMap<String, BigDecimal>();
	/**
	 * @param region
	 * @param memoryOfGB
	 * @param price
	 * 向Map中添加内存和对应价格
	 */
	private static void putMemoryMonthlyPrice(Integer region,Integer priceStatus, Integer memoryOfGB, BigDecimal price)
	{
		String key = region.toString()+priceStatus.toString()+memoryOfGB.toString();
		MEMORY__MONTHLY_PRICE.put(key, price);
	}
	
	/**
	 * @param region
	 * @param memoryOfGB
	 * @return
	 * 根据region和内存项取出相应内存的价格
	 */
	public static BigDecimal getMemoryMonthlyPrice(Integer region,Integer priceStatus, Integer memoryOfGB)
	{
		String key = region.toString()+priceStatus.toString()+memoryOfGB.toString();
		return MEMORY__MONTHLY_PRICE.get(key);
	}
	
	/**
	 * @param region
	 * @param memoryOfGB
	 * @return
	 * 算出每小时相应内存项的费用
	 */
	public static BigDecimal getMemoryHourlyPrice(Integer region,Integer priceStatus, Integer memoryOfGB)
	{
		return divide(divide(getMemoryMonthlyPrice(region,priceStatus, memoryOfGB), new BigDecimal(30)), new BigDecimal(24));
	}
	//初始化内存Map
	static {
		//从数据库获取所有内存项和价格
		List<PackagePriceVO> memoryList = packageOptionService.getPackagePrice("2");
		if(memoryList!=null && memoryList.size()>0){
			for(PackagePriceVO packagePrice : memoryList){
				if(packagePrice.getPriceStatus()==1){
					putMemoryMonthlyPrice(packagePrice.getRegion(),2,CapacityUtil.toGBValue(packagePrice.getMemory(),0,BigDecimal.ROUND_UP).intValue(), packagePrice.getVpcPrice());
					putMemoryMonthlyPrice(packagePrice.getRegion(),3,CapacityUtil.toGBValue(packagePrice.getMemory(),0,BigDecimal.ROUND_UP).intValue(), packagePrice.getPrice());
				}else if(packagePrice.getPriceStatus()==2){
					putMemoryMonthlyPrice(packagePrice.getRegion(),2,CapacityUtil.toGBValue(packagePrice.getMemory(),0,BigDecimal.ROUND_UP).intValue(), packagePrice.getVpcPrice());
				}else {
					putMemoryMonthlyPrice(packagePrice.getRegion(),3,CapacityUtil.toGBValue(packagePrice.getMemory(),0,BigDecimal.ROUND_UP).intValue(), packagePrice.getPrice());
				}
			}
		}
		putMemoryMonthlyPrice(1,2,0, new BigDecimal(0));
		putMemoryMonthlyPrice(1,3,0, new BigDecimal(0));
		putMemoryMonthlyPrice(2,2,0, new BigDecimal(0));
		putMemoryMonthlyPrice(2,3,0, new BigDecimal(0));
		putMemoryMonthlyPrice(3,2,0, new BigDecimal(0));
		putMemoryMonthlyPrice(3,3,0, new BigDecimal(0));
		putMemoryMonthlyPrice(4,2,0, new BigDecimal(0));
		putMemoryMonthlyPrice(4,3,0, new BigDecimal(0));
//		putMemoryMonthlyPrice(4,1, new BigDecimal(48));
//		putMemoryMonthlyPrice(4,2, new BigDecimal(120));
//		putMemoryMonthlyPrice(4,4, new BigDecimal(208));
//		putMemoryMonthlyPrice(4,6, new BigDecimal(304));
//		putMemoryMonthlyPrice(4,8, new BigDecimal(432));
//		putMemoryMonthlyPrice(4,12, new BigDecimal(560));
//		putMemoryMonthlyPrice(4,16, new BigDecimal(688));
//		putMemoryMonthlyPrice(4,32, new BigDecimal(1788.8));
//		putMemoryMonthlyPrice(4,64, new BigDecimal(3577.6));
//		putMemoryMonthlyPrice(0,0, new BigDecimal(0));
//		putMemoryMonthlyPrice(0,1, new BigDecimal(30));
//		putMemoryMonthlyPrice(0,2, new BigDecimal(75));
//		putMemoryMonthlyPrice(0,4, new BigDecimal(130));
//		putMemoryMonthlyPrice(0,6, new BigDecimal(190));
//		putMemoryMonthlyPrice(0,8, new BigDecimal(270));
//		putMemoryMonthlyPrice(0,12, new BigDecimal(350));
//		putMemoryMonthlyPrice(0,16, new BigDecimal(430));
//		putMemoryMonthlyPrice(0,32, new BigDecimal(1118));
//		putMemoryMonthlyPrice(0,64, new BigDecimal(2236));
	}
	/**
	 * @param region
	 * @param dataDiskOfGB
	 * @return
	 * 获得每个月相应硬盘的价格
	 */
	public static BigDecimal getDataDiskMonthlyPrice(Integer region,Integer priceStatus, Double dataDiskOfGB)
	{
		BigDecimal DATA_DISK_10GB_MONTHLY_PRICE_FOR_HONGKANG = new BigDecimal(AppProperties.getValue("package_price_disk_4", ""));
		BigDecimal DATA_DISK_10GB_MONTHLY_PRICE_FOR_OTHERS = new BigDecimal(AppProperties.getValue("package_price_disk", ""));
		BigDecimal DATA_DISK_10GB_MONTHLY_PRICE_FOR_CHENGDU = new BigDecimal(AppProperties.getValue("package_price_disk_2", ""));
		BigDecimal DATA_DISK_10GB_MONTHLY_PRICE_FOR_HONGKANG_VPC = new BigDecimal(AppProperties.getValue("package_price_disk_4_vpc", ""));
		BigDecimal DATA_DISK_10GB_MONTHLY_PRICE_FOR_OTHERS_VPC = new BigDecimal(AppProperties.getValue("package_price_disk_vpc", ""));
		BigDecimal DATA_DISK_10GB_MONTHLY_PRICE_FOR_CHENGDU_VPC = new BigDecimal(AppProperties.getValue("package_price_disk_2_vpc", ""));
		if(priceStatus == 2){
			if(region == 4){
				return DATA_DISK_10GB_MONTHLY_PRICE_FOR_HONGKANG.multiply(new BigDecimal(dataDiskOfGB));
			}else if(region == 2){
				return DATA_DISK_10GB_MONTHLY_PRICE_FOR_CHENGDU.multiply(new BigDecimal(dataDiskOfGB));
			}else{
				return DATA_DISK_10GB_MONTHLY_PRICE_FOR_OTHERS.multiply(new BigDecimal(dataDiskOfGB));
			}
		}else{
			if(region == 4){
				return DATA_DISK_10GB_MONTHLY_PRICE_FOR_HONGKANG.multiply(new BigDecimal(dataDiskOfGB));
			}else if(region == 2){
				return DATA_DISK_10GB_MONTHLY_PRICE_FOR_CHENGDU.multiply(new BigDecimal(dataDiskOfGB));
			}else{
				return DATA_DISK_10GB_MONTHLY_PRICE_FOR_OTHERS.multiply(new BigDecimal(dataDiskOfGB));
			}
		}
		
	}
	/**
	 * @param region
	 * @param dataDiskOfGB
	 * @return
	 * 获得每小时相应硬盘的价格
	 */
	public static BigDecimal getDataDiskHourlyPrice(Integer region,Integer priceStatus, Double dataDiskOfGB)
	{
		return divide(divide(getDataDiskMonthlyPrice(region,priceStatus,dataDiskOfGB), new BigDecimal(30)), new BigDecimal(24));
	}
	
	
	//-------------------
	
	
//	private static final Map<String, BigDecimal> BANDWIDTH_MONTHLY_PRICE = new LinkedHashMap<String, BigDecimal>();
	
//	private static void putBandwidthMonthlyPrice(Integer region, Integer bandwidthOfMbps, BigDecimal price)
//	{
//		String key = region.toString() + bandwidthOfMbps.toString();
//		BANDWIDTH_MONTHLY_PRICE.put(key, price);
//	}
	
	
//	static {
//		List<PackagePriceVO> bandwidthList = packageOptionService.getPackagePrice("4");
//		if(bandwidthList!=null){
//			for(PackagePriceVO packagePrice : bandwidthList){
//				Integer region = null;
//				if(packagePrice.getRegion()!=null && packagePrice.getRegion()!=4){
//					region = 0;
//				}else{
//					region = 4;
//				}
//				putBandwidthMonthlyPrice(region,FlowUtil.toMbpsValue(packagePrice.getBandwidth(),  0, BigDecimal.ROUND_UP).intValue(), packagePrice.getPrice());
//			}
//		}
//		putBandwidthMonthlyPrice(4,0, new BigDecimal(0));
//		putBandwidthMonthlyPrice(0,0, new BigDecimal(0));
//		putBandwidthMonthlyPrice(4,1, new BigDecimal(60));
//		putBandwidthMonthlyPrice(4,2, new BigDecimal(120));
//		putBandwidthMonthlyPrice(4,3, new BigDecimal(180));
//		putBandwidthMonthlyPrice(4,4, new BigDecimal(240));
//		putBandwidthMonthlyPrice(4,5, new BigDecimal(300));
//		putBandwidthMonthlyPrice(0,0, new BigDecimal(0));
//		putBandwidthMonthlyPrice(0,1, new BigDecimal(30));
//		putBandwidthMonthlyPrice(0,2, new BigDecimal(60));
//		putBandwidthMonthlyPrice(0,3, new BigDecimal(90));
//		putBandwidthMonthlyPrice(0,4, new BigDecimal(120));
//		putBandwidthMonthlyPrice(0,5, new BigDecimal(150));
//	}
	
	/**
	 * @param region
	 * @param bandwidthOfMbps
	 * @return
	 * 获得每个月相应带宽的价格
	 */
	public static BigDecimal getBandwidthMonthlyPrice(Integer region,Integer priceStatus, Integer bandwidthOfMbps)
	{
		BigDecimal LESS_THAN_5_OTHER = new BigDecimal(AppProperties.getValue("bandwidth_less_than_5", "30"));
		BigDecimal LESS_THAN_5_CD = new BigDecimal(AppProperties.getValue("bandwidth_less_than_5_2", "30"));
		BigDecimal LESS_THAN_5_HK = new BigDecimal(AppProperties.getValue("bandwidth_less_than_5_4", "60"));
		BigDecimal BASE_PRICE_5_OTHER = new BigDecimal(AppProperties.getValue("bandwidth_equal_5", "150"));
		BigDecimal BASE_PRICE_5_CD = new BigDecimal(AppProperties.getValue("bandwidth_equal_5_2", "150"));
		BigDecimal BASE_PRICE_5_HK = new BigDecimal(AppProperties.getValue("bandwidth_equal_5_4", "300"));
		BigDecimal BANDWIDTH_MONTHLY_PRICE_PER_1MBPS = new BigDecimal(AppProperties.getValue("bandwidth_base", ""));
		BigDecimal BANDWIDTH_MONTHLY_PRICE_PER_1MBPS_2 = new BigDecimal(AppProperties.getValue("bandwidth_base_2", ""));
		BigDecimal BANDWIDTH_MONTHLY_PRICE_PER_1MBPS_4 = new BigDecimal(AppProperties.getValue("bandwidth_base_4", ""));
		BigDecimal LESS_THAN_5_OTHER_VPC = new BigDecimal(AppProperties.getValue("bandwidth_less_than_5_vpc", "30"));
		BigDecimal LESS_THAN_5_CD_VPC = new BigDecimal(AppProperties.getValue("bandwidth_less_than_5_2_vpc", "30"));
		BigDecimal LESS_THAN_5_HK_VPC = new BigDecimal(AppProperties.getValue("bandwidth_less_than_5_4_vpc", "60"));
		BigDecimal BASE_PRICE_5_OTHER_VPC = new BigDecimal(AppProperties.getValue("bandwidth_equal_5_vpc", "150"));
		BigDecimal BASE_PRICE_5_CD_VPC = new BigDecimal(AppProperties.getValue("bandwidth_equal_5_2_vpc", "150"));
		BigDecimal BASE_PRICE_5_HK_VPC = new BigDecimal(AppProperties.getValue("bandwidth_equal_5_4_vpc", "300"));
		BigDecimal BANDWIDTH_MONTHLY_PRICE_PER_1MBPS_VPC = new BigDecimal(AppProperties.getValue("bandwidth_base_vpc", ""));
		BigDecimal BANDWIDTH_MONTHLY_PRICE_PER_1MBPS_2_VPC = new BigDecimal(AppProperties.getValue("bandwidth_base_2_vpc", ""));
		BigDecimal BANDWIDTH_MONTHLY_PRICE_PER_1MBPS_4_VPC = new BigDecimal(AppProperties.getValue("bandwidth_base_4_vpc", ""));
		if(priceStatus == 2){
			if( bandwidthOfMbps<=5 )
			{
				if(region==4){
					BigDecimal m = LESS_THAN_5_HK.multiply(new BigDecimal(bandwidthOfMbps)).subtract(new BigDecimal(10));
					return m.compareTo(new BigDecimal(0))>0?m:new BigDecimal(0);
				}else if(region==2){
					BigDecimal m = LESS_THAN_5_CD.multiply(new BigDecimal(bandwidthOfMbps)).subtract(new BigDecimal(10));
					return m.compareTo(new BigDecimal(0))>0?m:new BigDecimal(0);
				}else{
					BigDecimal m = LESS_THAN_5_OTHER.multiply(new BigDecimal(bandwidthOfMbps)).subtract(new BigDecimal(10));
					return m.compareTo(new BigDecimal(0))>0?m:new BigDecimal(0);
				}
			}
			else
			{
				if(region == 4){
					BigDecimal m = BASE_PRICE_5_HK.add( new BigDecimal(bandwidthOfMbps-5).multiply(BANDWIDTH_MONTHLY_PRICE_PER_1MBPS_4)).subtract(new BigDecimal(10));
					return m.compareTo(new BigDecimal(0))>0?m:new BigDecimal(0);
				}else if(region == 2){
					BigDecimal m = BASE_PRICE_5_CD.add( new BigDecimal(bandwidthOfMbps-5).multiply(BANDWIDTH_MONTHLY_PRICE_PER_1MBPS_2)).subtract(new BigDecimal(10));
					return m.compareTo(new BigDecimal(0))>0?m:new BigDecimal(0);
				}else{
					BigDecimal m = BASE_PRICE_5_OTHER.add( new BigDecimal(bandwidthOfMbps-5).multiply(BANDWIDTH_MONTHLY_PRICE_PER_1MBPS)).subtract(new BigDecimal(10));
					return m.compareTo(new BigDecimal(0))>0?m:new BigDecimal(0);
				}
			}
		}else{
			
			if( bandwidthOfMbps<=5 )
			{
				if(region==4){
					return LESS_THAN_5_HK.multiply(new BigDecimal(bandwidthOfMbps));
				}else if(region==2){
					return LESS_THAN_5_CD.multiply(new BigDecimal(bandwidthOfMbps));
				}else{
					return LESS_THAN_5_OTHER.multiply(new BigDecimal(bandwidthOfMbps));
				}
			}
			else
			{
				if(region == 4){
					return BASE_PRICE_5_HK.add( new BigDecimal(bandwidthOfMbps-5).multiply(BANDWIDTH_MONTHLY_PRICE_PER_1MBPS_4) );
				}else if(region == 2){
					return BASE_PRICE_5_CD.add( new BigDecimal(bandwidthOfMbps-5).multiply(BANDWIDTH_MONTHLY_PRICE_PER_1MBPS_2) );
				}else{
					return BASE_PRICE_5_OTHER.add( new BigDecimal(bandwidthOfMbps-5).multiply(BANDWIDTH_MONTHLY_PRICE_PER_1MBPS) );
				}
			}
		}
		
	}
	
	/**
	 * @param region
	 * @param bandwidthOfMbps
	 * @return
	 * 获取每小时相应带宽的价格
	 */
	public static BigDecimal getBandwidthHourlyPrice(Integer region,Integer priceStatus, Integer bandwidthOfMbps)
	{
		return divide(divide(getBandwidthMonthlyPrice(region,priceStatus,bandwidthOfMbps), new BigDecimal(30)), new BigDecimal(24));
	}
	
	
	//-------------------

	/**
	 * @param region
	 * @param cpuCore
	 * @param memory
	 * @param dataDisk
	 * @param bandwidth
	 * @return
	 * 获取套餐的价格
	 */
	public static BigDecimal getMonthlyPrice(Integer region,Integer priceStatus, Integer cpuCore, BigInteger memory, BigInteger dataDisk, BigInteger bandwidth)
	{
		Integer memoryOfGB      = CapacityUtil.toGBValue(memory,   0, BigDecimal.ROUND_UP).intValue();
		Double dataDiskOfGB     = CapacityUtil.toGBValue(dataDisk, 0, BigDecimal.ROUND_UP).doubleValue();
		Integer bandwidthOfMpbs = FlowUtil.toMbpsValue(bandwidth,  0, BigDecimal.ROUND_UP).intValue();
		
		return getMonthlyPrice(region,priceStatus, cpuCore, memoryOfGB, dataDiskOfGB, bandwidthOfMpbs);
	}

	/**
	 * @param region
	 * @param cpuCore
	 * @param memoryOfGB
	 * @param dataDiskOfGB
	 * @param bandwidthOfMpbs
	 * @return
	 * 获取套餐的价格
	 */
	public static BigDecimal getMonthlyPrice(Integer region,Integer priceStatus, Integer cpuCore, Integer memoryOfGB, Double dataDiskOfGB, Integer bandwidthOfMpbs)
	{
		BigDecimal cpuPrice       = getCpuMonthlyPrice(region,priceStatus, cpuCore);
 		BigDecimal memoryPrice    = getMemoryMonthlyPrice(region,priceStatus, memoryOfGB);
  		BigDecimal dataDiskPrice  = getDataDiskMonthlyPrice(region,priceStatus,dataDiskOfGB);
 		BigDecimal bandwidthPrice = getBandwidthMonthlyPrice(region,priceStatus,bandwidthOfMpbs);
 		
		return cpuPrice.add(memoryPrice).add(dataDiskPrice).add(bandwidthPrice);
	}

	/**
	 * 
	 */
	public static BigDecimal getPeriodPrice(Integer region,Integer priceStatus, Integer cpuCore, BigInteger memory, BigInteger dataDisk, BigInteger bandwidth, long periodOfMillisecond)
	{
		BigDecimal monthlyPrice = getMonthlyPrice(region,priceStatus, cpuCore, memory, dataDisk, bandwidth).setScale(2,   BigDecimal.ROUND_HALF_UP);
		return monthlyPrice.multiply(divide(new BigDecimal(periodOfMillisecond), new BigDecimal(DateUtil.MILLISECOND_PER_MONTH))).setScale(2,   BigDecimal.ROUND_HALF_UP);
	}
	
	/**
	 * 
	 */
	public static BigDecimal getPeriodPrice(Integer region,Integer priceStatus, Integer cpuCore, Integer memoryOfGB, Double dataDiskOfGB, Integer bandwidthOfMpbs, long periodOfMillisecond)
	{
		BigDecimal monthlyPrice = getMonthlyPrice(region,priceStatus, cpuCore, memoryOfGB, dataDiskOfGB, bandwidthOfMpbs);
		return monthlyPrice.multiply(divide(new BigDecimal(periodOfMillisecond), new BigDecimal(DateUtil.MILLISECOND_PER_MONTH)));
	}
	
	/**
	 * 
	 */
	public static BigDecimal getPeriodPrice(BigDecimal monthlyPrice, long periodOfMillisecond)
	{
		return monthlyPrice.multiply(divide(new BigDecimal(periodOfMillisecond), new BigDecimal(DateUtil.MILLISECOND_PER_MONTH)));
	}

	/**
	 * 
	 */
	private static BigDecimal divide(BigDecimal d1, BigDecimal d2)
	{
		return d1.divide(d2, 50, BigDecimal.ROUND_HALF_EVEN);
	}
	
	/**
	 * 当套餐项价格发生变化时调用此方法同步价格
	 */
	public static void updateAllOptions(){
		CPU__MONTHLY_PRICE.clear();
		MEMORY__MONTHLY_PRICE.clear();
//		BANDWIDTH_MONTHLY_PRICE.clear();
//		List<PackagePriceVO> bandwidthList = packageOptionService.getPackagePrice("4");
//		if(bandwidthList!=null){
//			for(PackagePriceVO packagePrice : bandwidthList){
//				Integer region = null;
//				if(packagePrice.getRegion()!=null && packagePrice.getRegion()!=4){
//					region = 0;
//				}else{
//					region = 4;
//				}
//				putBandwidthMonthlyPrice(region,FlowUtil.toMbpsValue(packagePrice.getBandwidth(),  0, BigDecimal.ROUND_UP).intValue(), packagePrice.getPrice());
//			}
//		}
//		putBandwidthMonthlyPrice(4,0, new BigDecimal(0));
//		putBandwidthMonthlyPrice(0,0, new BigDecimal(0));
		//
		List<PackagePriceVO> memoryList = packageOptionService.getPackagePrice("2");
		if(memoryList!=null){
			for(PackagePriceVO packagePrice : memoryList){
				if(packagePrice.getPriceStatus()==1){
					putMemoryMonthlyPrice(packagePrice.getRegion(),2,CapacityUtil.toGBValue(packagePrice.getMemory(),0,BigDecimal.ROUND_UP).intValue(), packagePrice.getVpcPrice());
					putMemoryMonthlyPrice(packagePrice.getRegion(),3,CapacityUtil.toGBValue(packagePrice.getMemory(),0,BigDecimal.ROUND_UP).intValue(), packagePrice.getPrice());
				}else if(packagePrice.getPriceStatus()==2){
					putMemoryMonthlyPrice(packagePrice.getRegion(),2,CapacityUtil.toGBValue(packagePrice.getMemory(),0,BigDecimal.ROUND_UP).intValue(), packagePrice.getVpcPrice());
				}else {
					putMemoryMonthlyPrice(packagePrice.getRegion(),3,CapacityUtil.toGBValue(packagePrice.getMemory(),0,BigDecimal.ROUND_UP).intValue(), packagePrice.getPrice());
				}
			}
		}
		putMemoryMonthlyPrice(1,2,0, new BigDecimal(0));
		putMemoryMonthlyPrice(1,3,0, new BigDecimal(0));
		putMemoryMonthlyPrice(2,2,0, new BigDecimal(0));
		putMemoryMonthlyPrice(2,3,0, new BigDecimal(0));
		putMemoryMonthlyPrice(3,2,0, new BigDecimal(0));
		putMemoryMonthlyPrice(3,3,0, new BigDecimal(0));
		putMemoryMonthlyPrice(4,2,0, new BigDecimal(0));
		putMemoryMonthlyPrice(4,3,0, new BigDecimal(0));
		//
		List<PackagePriceVO> cpuList = packageOptionService.getPackagePrice("1");
		if(cpuList!=null){
			for(PackagePriceVO packagePrice : cpuList){
				if(packagePrice.getPriceStatus()==1){
					putCpuMonthlyPrice(packagePrice.getRegion(),3,packagePrice.getCpuCore(), packagePrice.getPrice());
					putCpuMonthlyPrice(packagePrice.getRegion(),2,packagePrice.getCpuCore(), packagePrice.getVpcPrice());
				}else if(packagePrice.getPriceStatus()==2){
					putCpuMonthlyPrice(packagePrice.getRegion(),2,packagePrice.getCpuCore(), packagePrice.getVpcPrice());
				}else {
					putCpuMonthlyPrice(packagePrice.getRegion(),3,packagePrice.getCpuCore(), packagePrice.getPrice());
				}
			}
		}
		putCpuMonthlyPrice(1,2,0, new BigDecimal(0));
		putCpuMonthlyPrice(1,3,0, new BigDecimal(0));
		putCpuMonthlyPrice(2,2,0, new BigDecimal(0));
		putCpuMonthlyPrice(2,3,0, new BigDecimal(0));
		putCpuMonthlyPrice(3,2,0, new BigDecimal(0));
		putCpuMonthlyPrice(3,3,0, new BigDecimal(0));
		putCpuMonthlyPrice(4,2,0, new BigDecimal(0));
		putCpuMonthlyPrice(4,3,0, new BigDecimal(0));
	}
	//-----------------------

	/**
	 * 
	 */
	public static void main(String[] args)
	{
		System.out.println(getDataDiskMonthlyPrice(4,2, 30.0));
	}
	
}

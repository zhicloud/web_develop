package com.zhicloud.ms.constant;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import com.zhicloud.ms.common.util.CapacityUtil;
import com.zhicloud.ms.vo.StaticDetailVO;
import com.zhicloud.ms.vo.StaticSummaryVO;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @ClassName: StaticReportHandle
 * @Description: 统计报表异步数据处理
 * @author 张本缘 于 2015年10月20日 下午5:54:27
 */
public class StaticReportHandle {
    
    // 明细数据
    public static Map<Integer, StaticDetailVO> detailMap = Collections
            .synchronizedMap(new LinkedHashMap<Integer, StaticDetailVO>());
    // 概要数据
    public static Map<Integer, StaticSummaryVO> summaryMap = Collections
            .synchronizedMap(new LinkedHashMap<Integer, StaticSummaryVO>());

    // 目标ID
    public static String target = null;
    // 开始和截止日期
    public static String begin = null;
    public static String end = null;
    
    /**
     * @Description:更新明细数据到内存中
     * @param json gw异步回调数据
     * @param type 1、服务明细 2、操作明细
     */
    public static synchronized void updateDetailDataToMemory(JSONObject json, Integer type) {
        if (!json.isNullObject() && !json.isEmpty()) {
            StaticDetailVO detail = new StaticDetailVO();
            detail.setType(type);
            detail.setCpu_count(json.getInt("cpu_count"));
            detail.setTotal_volume(json.getString("total_volume"));
            detail.setUsed_volume(json.getString("used_volume"));
            detail.setTimestamp(json.getJSONArray("timestamp").toString().split(","));
            detail.setService_status(json.getJSONArray("service_status").toString().split(","));
            detail.setTotal_cpu_usage(json.getJSONArray("total_cpu_usage").toString().split(","));
            detail.setDisk_usage(json.getJSONArray("disk_usage").toString().split(","));
            detail.setMemory_usage(json.getJSONArray("memory_usage").toString().split(","));
            detail.setCpu_seconds(json.getJSONArray("cpu_seconds").toString().split(","));
            detail.setRead_request(json.getJSONArray("read_request").toString().split(","));
            detail.setRead_bytes(json.getJSONArray("read_bytes").toString().split(","));
            detail.setWrite_request(json.getJSONArray("write_request").toString().split(","));
            detail.setWrite_bytes(json.getJSONArray("write_bytes").toString().split(","));
            detail.setIo_error(json.getJSONArray("io_error").toString().split(","));
            detail.setRead_speed(json.getJSONArray("read_speed").toString().split(","));
            detail.setWrite_speed(json.getJSONArray("write_speed").toString().split(","));
            detail.setReceived_bytes(json.getJSONArray("received_bytes").toString().split(","));
            detail.setReceived_packets(json.getJSONArray("received_packets").toString().split(","));
            detail.setReceived_errors(json.getJSONArray("received_errors").toString().split(","));
            detail.setReceived_drop(json.getJSONArray("received_drop").toString().split(","));
            detail.setSent_bytes(json.getJSONArray("sent_bytes").toString().split(","));
            detail.setSent_packets(json.getJSONArray("sent_packets").toString().split(","));
            detail.setSent_errors(json.getJSONArray("sent_errors").toString().split(","));
            detail.setSent_drop(json.getJSONArray("sent_drop").toString().split(","));
            detail.setReceived_speed(json.getJSONArray("received_speed").toString().split(","));
            detail.setSent_speed(json.getJSONArray("sent_speed").toString().split(","));
            detailMap.put(type, detail);
        }
    }

    /**
     * @Description:更新概要数据到内存中
     * @param json gw异步回调数据
     * @param type 1、服务概要 2、操作概要
     */
    public static synchronized void updateSummaryDataToMemory(JSONObject json, Integer type) {
        if (!json.isNullObject() && !json.isEmpty()) {
            StaticSummaryVO summary = new StaticSummaryVO();
            summary.setType(type);
            summary.setRoom(json.getJSONArray("room").toString().split(","));
            summary.setRack(json.getJSONArray("rack").toString().split(","));
            summary.setServer_name(json.getJSONArray("server_name").toString().split(","));
            summary.setCpu_count(json.getJSONArray("cpu_count").toString().split(","));
            summary.setTotal_memory(json.getJSONArray("total_memory").toString().split(","));
            summary.setAvailable_memory(json.getJSONArray("available_memory").toString().split(","));
            summary.setTotal_volume(json.getJSONArray("total_volume").toString().split(","));
            summary.setUsed_volume(json.getJSONArray("used_volume").toString().split(","));
            summary.setCpu_seconds(json.getJSONArray("cpu_seconds").toString().split(","));
            summary.setRead_bytes(json.getJSONArray("read_bytes").toString().split(","));
            summary.setWrite_bytes(json.getJSONArray("write_bytes").toString().split(","));
            summary.setReceived_bytes(json.getJSONArray("received_bytes").toString().split(","));
            summary.setSent_bytes(json.getJSONArray("sent_bytes").toString().split(","));
            summaryMap.put(type, summary);
        }
    }
    
    /**
     * @Description:将秒转换为小时,并返回
     * @param seconds 时间秒数
     * @return String
     */
    public static String convertSecondsToHour(String seconds) {
        long hours = Long.valueOf(seconds);
        DecimalFormat df2 = new DecimalFormat("#.0");
        return df2.format(hours / 60.0 / 60.0);
    }
    /**
     * @Description:取得cpu在线时长数据
     * @param type 类型 1、服务明细 2、操作明细
     * @return JSONArray
     */
    public static JSONArray getCpuOnLine(Integer type) {
        StaticDetailVO detail = detailMap.get(type);
        if (detail != null) {
            JSONArray data = new JSONArray();
            String[] timestamp = detail.getTimestamp();
            String[] cpu_seconds = detail.getCpu_seconds();
            for (int i = 0; i < timestamp.length; i++) {
                JSONObject json = new JSONObject();
                json.put("name", timestamp[i]);
                json.put("value", convertSecondsToHour(cpu_seconds[i]));//将秒转化成小时
                data.add(json);
            }
            return data;
        }
        return null;
    }
    
    /**
     * @Description:取得读/写/发送/接收的复杂柱形图数据
     * @param type 类型 1、服务明细 2、操作明细
     * @return JSONArray
     */
    public static JSONArray getMultyBarData(Integer type) {
        StaticDetailVO detail = detailMap.get(type);
        if (detail != null) {
            JSONArray data = new JSONArray();
            String[] timestamp = detail.getTimestamp();
            String[] read_bytes = detail.getRead_bytes();
            String[] write_bytes = detail.getWrite_bytes();
            String[] receive_bytes = detail.getReceived_bytes();
            String[] sent_bytes = detail.getSent_bytes();
            for (int i = 0; i < timestamp.length; i++) {
                JSONObject json = new JSONObject();
                json.put("y", timestamp[i]);
                json.put("read", CapacityUtil.toMBValue(new BigInteger(read_bytes[i]), 0));
                json.put("write", CapacityUtil.toMBValue(new BigInteger(write_bytes[i]), 0));
                json.put("send", CapacityUtil.toMBValue(new BigInteger(sent_bytes[i]), 0));
                json.put("receive", CapacityUtil.toMBValue(new BigInteger(receive_bytes[i]), 0));
                data.add(json);
            }
            return data;
        }
        return null;
    }
    
    /**
     * @Description:取得服务状态数据
     * @param type 类型 1、服务明细 2、操作明细
     * @return JSONArray
     */
    public static JSONArray getServerStatus(Integer type) {
        StaticDetailVO detail = detailMap.get(type);
        if (detail != null) {
            JSONArray data = new JSONArray();
            String[] timestamp = detail.getTimestamp();
            String[] service_status = detail.getService_status();
            for (int i = 0; i < timestamp.length; i++) {
                JSONObject json = new JSONObject();
                json.put("name", timestamp[i]);
                json.put("status", service_status[i]);// 将秒转化成小时
                data.add(json);
            }
            return data;
        }
        return null;
    }
    
    /**
     * @Description:取得内存和磁盘数据
     * @param type 类型 1、服务明细 2、操作明细
     * @return JSONArray
     */
    public static JSONArray getMemoryAndDisk(Integer type){
        StaticDetailVO detail = detailMap.get(type);
        if (detail != null) {
            JSONArray data = new JSONArray();
            String[] timestamp = detail.getTimestamp();
            String[] disk_use = detail.getDisk_usage();
            String[] memory_use = detail.getMemory_usage();
            for (int i = 0; i < timestamp.length; i++) {
                JSONObject json = new JSONObject();
                json.put("y", timestamp[i]);
                json.put("memory_use", memory_use[i]);
                json.put("disk_use", disk_use[i]);
                data.add(json);
            }
            return data;
        }
        return null;
    }
    
    /**
     * @Description:取得读/写数据
     * @param type 类型 1、服务明细 2、操作明细
     * @throws
     */
    public static JSONArray getReadAndWrite(Integer type) {
        StaticDetailVO detail = detailMap.get(type);
        if (detail != null) {
            JSONArray data = new JSONArray();
            String[] timestamp = detail.getTimestamp();
            String[] read_bytes = detail.getRead_bytes();
            String[] write_bytes = detail.getWrite_bytes();
            String[] read_request = detail.getRead_request();
            String[] write_request = detail.getWrite_request();
            String[] read_speed = detail.getRead_speed();
            String[] write_speed = detail.getWrite_speed();
            for (int i = 0; i < timestamp.length; i++) {
                JSONObject json = new JSONObject();
                json.put("timestamp", timestamp[i]);
                json.put("read_byte", CapacityUtil.toMBValue(new BigInteger(read_bytes[i]), 0));
                json.put("write_byte", CapacityUtil.toMBValue(new BigInteger(write_bytes[i]), 0));
                json.put("read_request", read_request[i]);
                json.put("write_request", write_request[i]);
                json.put("read_speed", CapacityUtil.toMBValue(new BigInteger(read_speed[i]), 0));
                json.put("write_speed", CapacityUtil.toMBValue(new BigInteger(write_speed[i]), 0));
                data.add(json);
            }
            return data;
        }
        return null;
    }
    
    /**
     * @Description:取得接收数据
     * @param type 类型 1、服务明细 2、操作明细
     * @throws
     */
    public static JSONArray getReceiveData(Integer type) {
        StaticDetailVO detail = detailMap.get(type);
        if (detail != null) {
            JSONArray data = new JSONArray();
            String[] timestamp = detail.getTimestamp();
            String[] receive_bytes = detail.getReceived_bytes();
            String[] received_packets = detail.getReceived_packets();
            String[] received_errors = detail.getReceived_errors();
            String[] received_drop = detail.getReceived_drop();
            String[] received_speed = detail.getReceived_speed();
            for (int i = 0; i < timestamp.length; i++) {
                JSONObject json = new JSONObject();
                json.put("timestamp", timestamp[i]);
                json.put("received_bytes", CapacityUtil.toMBValue(new BigInteger(receive_bytes[i]), 0));
                json.put("received_packets", received_packets[i]);
                json.put("received_errors", received_errors[i]);
                json.put("received_drop", received_drop[i]);
                json.put("received_speed", received_speed[i]);
                data.add(json);
            }
            return data;
        }
        return null;
    }
    
    /**
     * @Description:取得发送数据
     * @param type 类型 1、服务明细 2、操作明细
     * @throws
     */
    public static JSONArray getSentData(Integer type) {
        StaticDetailVO detail = detailMap.get(type);
        if (detail != null) {
            JSONArray data = new JSONArray();
            String[] timestamp = detail.getTimestamp();
            String[] sent_bytes = detail.getSent_bytes();
            String[] sent_packets = detail.getSent_packets();
            String[] sent_errors = detail.getSent_errors();
            String[] sent_drop = detail.getSent_drop();
            String[] sent_speed = detail.getSent_speed();
            for (int i = 0; i < timestamp.length; i++) {
                JSONObject json = new JSONObject();
                json.put("timestamp", timestamp[i]);
                json.put("sent_bytes", CapacityUtil.toMBValue(new BigInteger(sent_bytes[i]), 0));
                json.put("sent_packets", sent_packets[i]);
                json.put("sent_errors", sent_errors[i]);
                json.put("sent_drop", sent_drop[i]);
                json.put("sent_speed", sent_speed[i]);
                data.add(json);
            }
            return data;
        }
        return null;
    }
    
}

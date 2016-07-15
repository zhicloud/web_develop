
package com.zhicloud.ms.service;


import net.sf.json.JSONArray;

/**
 * @ClassName: StaticReportService
 * @Description: 统计报表接口
 * @author 张本缘 于 2015年10月12日 上午11:17:56
 */
public interface StaticReportService {

    /**
     * @Description:取得统计接口的明细信息
     * @param target 目标ID
     * @param level 级别(0=month;1=day;2=hour;3=minute)
     * @param begin 开始日期
     * @param end 结束日期
     * @param type 类型(1、服务明细 2、操作明细)
     * @return JSONObject
     */
    public boolean sendAsyRequest(String target, Integer level, String begin, String end, Integer type);

    /**
     * @Description:获取cpu在线时长数据
     * @param target 目标ID
     * @param level 级别(0=month;1=day;2=hour;3=minute)
     * @param begin 开始日期
     * @param end 结束日期
     * @param type 类型(1、服务明细 2、操作明细)
     * @return JSONArray
     */
    public JSONArray getCpuOnlineData(String target, Integer level, String begin, String end, Integer type);

    /**
     * @Description:获取复杂柱形图数据
     * @param target 目标ID
     * @param level 级别(0=month;1=day;2=hour;3=minute)
     * @param begin 开始日期
     * @param end 结束日期
     * @param type 类型(1、服务明细 2、操作明细)
     * @return JSONArray
     */
    public JSONArray getMultyData(String target, Integer level, String begin, String end, Integer type);

    /**
     * @Description:获取服务状态数据
     * @param target 目标ID
     * @param level 级别(0=month;1=day;2=hour;3=minute)
     * @param begin 开始日期
     * @param end 结束日期
     * @param type 类型(1、服务明细 2、操作明细)
     * @return JSONArray
     */
    public JSONArray getServiceStatus(String target, Integer level, String begin, String end, Integer type);

    /**
     * @Description:获取内存和磁盘数据
     * @param target 目标ID
     * @param level 级别(0=month;1=day;2=hour;3=minute)
     * @param begin 开始日期
     * @param end 结束日期
     * @param type 类型(1、服务明细 2、操作明细)
     * @return JSONArray
     */
    public JSONArray getMemoryAndDisk(String target, Integer level, String begin, String end, Integer type);

    /**
     * @Description:获取读/写数据
     * @param target 目标ID
     * @param level 级别(0=month;1=day;2=hour;3=minute)
     * @param begin 开始日期
     * @param end 结束日期
     * @param type 类型(1、服务明细 2、操作明细)
     * @return JSONArray
     */
    public JSONArray getReadAndWrite(String target, Integer level, String begin, String end, Integer type);

    /**
     * @Description:获取接收数据
     * @param target 目标ID
     * @param level 级别(0=month;1=day;2=hour;3=minute)
     * @param begin 开始日期
     * @param end 结束日期
     * @param type 类型(1、服务明细 2、操作明细)
     * @return JSONArray
     */
    public JSONArray getReceiveData(String target, Integer level, String begin, String end, Integer type);

    /**
     * @Description:获取发送数据
     * @param target 目标ID
     * @param level 级别(0=month;1=day;2=hour;3=minute)
     * @param begin 开始日期
     * @param end 结束日期
     * @param type 类型(1、服务明细 2、操作明细)
     * @return JSONArray
     */
    public JSONArray getSentData(String target, Integer level, String begin, String end, Integer type);

}

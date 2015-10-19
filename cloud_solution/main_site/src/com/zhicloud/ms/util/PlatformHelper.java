package com.zhicloud.ms.util;

import com.zhicloud.ms.constant.AppInconstant;
import com.zhicloud.ms.exception.AppException;
import com.zhicloud.ms.httpGateway.HttpGatewayChannel;
import com.zhicloud.ms.httpGateway.HttpGatewayManager;
import com.zhicloud.ms.httpGateway.HttpGatewayResponseHelper;
import com.zhicloud.ms.util.json.JSONLibUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Created by sean on 6/29/15.
 */
public class PlatformHelper {

    private static final Logger logger = Logger.getLogger(PlatformHelper.class);

    public static PlatformHelper singleton = new PlatformHelper();

    private PlatformHelper()
    {
        logger.info("PlatformHelper.PlatformHelper() > ["+Thread.currentThread().getId()+"] initialized");
    }

    public void reflashCache() throws IOException {

        try{

            // 区域信息
            RegionHelper.RegionData[] regionDatas = RegionHelper.singleton.getAllResions();

            for (RegionHelper.RegionData regionData : regionDatas) {
                AppInconstant.nameMap.put(regionData.getId().toString(), regionData.getName());
                HttpGatewayChannel channel = HttpGatewayManager.getChannel(regionData.getId());
                // 获取默认计算资源池
                JSONObject defaultComputePool = channel.computePoolQuery();
                if (defaultComputePool == null) {
                    continue;
                }

                if (defaultComputePool == null) {
                    logger.error("PlatformHelper.reflashCache>>>获取资源池失败");
                    throw new AppException("获取资源池失败");
                }
                // 从http gateway获取所有的机房信息
                JSONObject roomQueryResult = channel.serverRoomQuery();
                if (HttpGatewayResponseHelper.isSuccess(roomQueryResult) == false) {
                    continue;
                }
                JSONArray rooms = (JSONArray) roomQueryResult.get("server_rooms");
                // 将机房数据存入常量
                AppInconstant.serverRoomInfo.put(regionData.getId(), rooms);
                // 获取机架信息
                for (int i = 0; i < rooms.size(); i++) {
                    JSONObject room = (JSONObject) rooms.get(i);
                    AppInconstant.nameMap.put(JSONLibUtil.getString(room, "uuid"), JSONLibUtil.getString(room, "name"));
                    JSONObject rackQueryResult = channel.serverRackQuery(JSONLibUtil.getString(room, "uuid"));
                    if (HttpGatewayResponseHelper.isSuccess(rackQueryResult) == false) {
                        continue;
                    }
                    JSONArray racks = (JSONArray) rackQueryResult.get("server_racks");
                    // 将机架数据存入常量
                    AppInconstant.serverRackInfo.put(JSONLibUtil.getString(room, "uuid"), racks);
                    // 获取服务器信息
                    for (int j = 0; j < racks.size(); j++) {
                        JSONObject rack = (JSONObject) racks.get(i);
                        AppInconstant.nameMap.put(JSONLibUtil.getString(rack, "uuid"),
                            JSONLibUtil.getString(rack, "name"));
                        JSONObject serverQueryResult = channel.serverQuery(JSONLibUtil.getString(rack, "uuid"));
                        if (HttpGatewayResponseHelper.isSuccess(serverQueryResult) == false) {
                            continue;
                        }
                        JSONArray servers = (JSONArray) serverQueryResult.get("servers");
                        // 将服务器数据存入常量
                        AppInconstant.serverInfo.put(JSONLibUtil.getString(rack, "uuid"), servers);
                    }

                }
            }


        } catch (Exception e) {

        }

    }
}

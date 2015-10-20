package com.zhicloud.ms.service.impl;

import com.zhicloud.ms.constant.AppInconstant;
import com.zhicloud.ms.service.IServerService;
import com.zhicloud.ms.util.PlatformHelper;
import com.zhicloud.ms.util.RegionHelper;
import com.zhicloud.ms.vo.ServerVO;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sean on 6/30/15.
 */

@Service("serverService")
@Transactional(readOnly=true)
public class ServerServiceImpl implements IServerService {

    @Override
    public List<ServerVO> getServers() throws IOException {

        if (AppInconstant.serverInfo.isEmpty()) {
            try {
                PlatformHelper.singleton.reflashCache();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        List<ServerVO> servers = new ArrayList<ServerVO>();
        RegionHelper.RegionData[] regionDatas = RegionHelper.singleton.getAllResions();
        for (RegionHelper.RegionData regionData : regionDatas) {
            JSONArray rooms = AppInconstant.serverRoomInfo.get(regionData.getId());
            if (rooms == null){
                continue;
            }
            for (int i = 0; i < rooms.size(); i++){
                JSONObject room = rooms.getJSONObject(i);
                servers.addAll(this.getServersByRoom(room.getString("uuid")));
            }
        }
        return servers;
    }

    private List<ServerVO> getServersByRoom(String uuid) throws IOException {

        List<ServerVO> servers = new ArrayList<ServerVO>();
        JSONArray racks = AppInconstant.serverRackInfo.get(uuid);
        if (racks == null){
            return servers;
        }
        for (int i = 0; i < racks.size(); i++) {
            JSONObject rack = racks.getJSONObject(i);
            servers.addAll(this.getServersByRack(rack.getString("uuid")));
        }

        return servers;
    }

    @Override
    public List<ServerVO> getServersByRack(String uuid) throws IOException {

        if (AppInconstant.serverInfo.isEmpty()) {
            try {
                PlatformHelper.singleton.reflashCache();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        List<ServerVO> serverList = new ArrayList<ServerVO>();
        JSONArray servers = AppInconstant.serverInfo.get(uuid);

        if (servers == null){
            return serverList;
        }

        for (int i = 0; i < servers.size(); i++) {
            JSONObject server = servers.getJSONObject(i);
            ServerVO serverVO = new ServerVO();
            serverVO.setUuid(server.getString("uuid"));
            serverVO.setName(server.getString("name"));
            serverVO.setCpuCount(server.getInt("cpu_count"));
            serverVO.setCpuUsage(server.getDouble("cpu_usage"));
            serverVO.setMemoryUsage(server.getDouble("memory_usage"));
            serverVO.setDiskUsage(server.getDouble("disk_usage"));
            serverVO.setStatus(server.getInt("status"));
            serverVO.setIp(server.getString("ip"));
            serverList.add(serverVO);
        }

        return serverList;
    }
}

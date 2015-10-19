package com.zhicloud.ms.service.impl;

import com.zhicloud.ms.constant.AppInconstant;
import com.zhicloud.ms.service.IServerRackService;
import com.zhicloud.ms.util.PlatformHelper;
import com.zhicloud.ms.util.RegionHelper;
import com.zhicloud.ms.vo.ServerRackVO;
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
@Service("serverRackService")
@Transactional(readOnly=true)
public class ServerRackServiceImpl implements IServerRackService {

    @Override
    public List<ServerRackVO> getServerRacks() throws IOException {

        if (AppInconstant.serverRackInfo.isEmpty()) {
            try {
                PlatformHelper.singleton.reflashCache();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        List<ServerRackVO> racks = new ArrayList<ServerRackVO>();

        RegionHelper.RegionData[] regionDatas = RegionHelper.singleton.getAllResions();
        for (RegionHelper.RegionData regionData : regionDatas) {
            JSONArray rooms = AppInconstant.serverRoomInfo.get(regionData.getId());
            if (rooms == null){
                continue;
            }
            for (int i = 0; i < rooms.size(); i++) {
                JSONObject room = rooms.getJSONObject(i);
                racks.addAll(this.getServerRacksByRoom(room.getString("uuid")));
            }
        }

        return racks;
    }

    @Override
    public List<ServerRackVO> getServerRacksByRoom(String uuid) throws IOException {

        if (AppInconstant.serverRackInfo.isEmpty()) {
            try {
                PlatformHelper.singleton.reflashCache();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        List<ServerRackVO> rackList = new ArrayList<ServerRackVO>();
        JSONArray racks = AppInconstant.serverRackInfo.get(uuid);

        if (racks == null){
            return rackList;
        }

        for (int i = 0; i < racks.size(); i++){
            JSONObject rack = racks.getJSONObject(i);
            ServerRackVO serverRack = new ServerRackVO();
            serverRack.setUuid(rack.getString("uuid"));
            serverRack.setName(rack.getString("name"));
            serverRack.setCpuCount(rack.getInt("cpu_count"));
            serverRack.setCpuUsage(rack.getDouble("cpu_usage"));
            serverRack.setMemoryUsage(rack.getDouble("memory_usage"));
            serverRack.setDiskUsage(rack.getDouble("disk_usage"));
            serverRack.setStatus(rack.getInt("status"));
            JSONArray servers = AppInconstant.serverInfo.get(rack.getString("uuid"));
            serverRack.setServerCount(servers.size());
            rackList.add(serverRack);
        }
        return rackList;
    }
}

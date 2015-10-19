package com.zhicloud.ms.service.impl;

import com.zhicloud.ms.constant.AppInconstant;
import com.zhicloud.ms.service.IServerRoomService;
import com.zhicloud.ms.util.PlatformHelper;
import com.zhicloud.ms.util.RegionHelper;
import com.zhicloud.ms.vo.ServerRoomVO;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sean on 6/26/15.
 */
@Service("serverRoomService")
@Transactional(readOnly=true)
public class ServerRoomServiceImpl implements IServerRoomService{

    @Override
    public List<ServerRoomVO> getServerRooms() throws IOException {

        if (AppInconstant.serverRoomInfo.isEmpty()) {
            try {
                PlatformHelper.singleton.reflashCache();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        List<ServerRoomVO> roomList = new ArrayList<ServerRoomVO>();
        RegionHelper.RegionData[] regionDatas = RegionHelper.singleton.getAllResions();
        for (RegionHelper.RegionData regionData : regionDatas) {
            JSONArray rooms = AppInconstant.serverRoomInfo.get(regionData.getId());
            if (rooms == null){
                continue;
            }
            for (int i = 0; i < rooms.size(); i++){
                JSONObject room = rooms.getJSONObject(i);
                ServerRoomVO roomVO = new ServerRoomVO();
                roomVO.setUuid(room.getString("uuid"));
                roomVO.setName(room.getString("name"));
                roomVO.setCpuCount(room.getInt("cpu_count"));
                roomVO.setCpuUsage(room.getDouble("cpu_usage"));
                roomVO.setMemoryUsage(room.getDouble("memory_usage"));
                roomVO.setDiskUsage(room.getDouble("disk_usage"));
                roomVO.setStatus(room.getInt("status"));
                JSONArray racks = AppInconstant.serverRackInfo.get(room.getString("uuid"));
                roomVO.setRackCount(racks.size());
                roomList.add(roomVO);
            }
        }

        return roomList;
    }


}

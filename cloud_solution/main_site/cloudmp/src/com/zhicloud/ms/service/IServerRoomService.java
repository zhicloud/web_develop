package com.zhicloud.ms.service;

import com.zhicloud.ms.vo.ServerRoomVO;

import java.io.IOException;
import java.util.List;

/**
 * Created by sean on 6/26/15.
 */
public interface IServerRoomService {

    public List<ServerRoomVO> getServerRooms() throws IOException;
}

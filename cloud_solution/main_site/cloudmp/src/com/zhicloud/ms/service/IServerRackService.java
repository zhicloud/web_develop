package com.zhicloud.ms.service;

import com.zhicloud.ms.vo.ServerRackVO;

import java.io.IOException;
import java.util.List;

/**
 * Created by sean on 6/30/15.
 */
public interface IServerRackService {

    public List<ServerRackVO> getServerRacks() throws IOException;

    public List<ServerRackVO> getServerRacksByRoom(String uuid) throws IOException;

}

package com.zhicloud.ms.service;

import com.zhicloud.ms.vo.ServerVO;

import java.io.IOException;
import java.util.List;

/**
 * Created by sean on 6/30/15.
 */
public interface IServerService {

    public List<ServerVO> getServers() throws IOException;

    public List<ServerVO> getServersByRack(String uuid) throws IOException;
}

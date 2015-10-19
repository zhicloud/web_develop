package com.zhicloud.ms.controller;

import com.zhicloud.ms.service.IServerRoomService;
import com.zhicloud.ms.transform.constant.TransFormPrivilegeConstant;
import com.zhicloud.ms.transform.util.TransFormPrivilegeUtil;
import com.zhicloud.ms.vo.ServerRoomVO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * Created by sean on 6/26/15.
 */

@Controller
@RequestMapping("/room")
public class ServerRoomController {

    @Resource
    IServerRoomService serverRoomService;

    @RequestMapping(value="/list",method= RequestMethod.GET)
    public String list(Model model,HttpServletRequest request) throws IOException {
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.server_room_manage_query)){
            return "not_have_access";
        }

        List<ServerRoomVO> rooms = serverRoomService.getServerRooms();

        model.addAttribute("rooms", rooms);

        return "/platform/server_room/server_room_manage";
    }
}

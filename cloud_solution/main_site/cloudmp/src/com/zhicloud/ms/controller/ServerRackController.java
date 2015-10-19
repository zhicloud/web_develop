package com.zhicloud.ms.controller;

import com.zhicloud.ms.service.IServerRackService;
import com.zhicloud.ms.transform.constant.TransFormPrivilegeConstant;
import com.zhicloud.ms.transform.util.TransFormPrivilegeUtil;
import com.zhicloud.ms.vo.ServerRackVO;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("/rack")
public class ServerRackController {

    public static final Logger logger = Logger.getLogger(ResourcePoolController.class);

    @Resource
    IServerRackService serverRackService;

    @RequestMapping(value="/list",method= RequestMethod.GET)
    public String list(Model model,HttpServletRequest request) throws IOException {
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.server_rack_manage_query)){
            return "not_have_access";
        }
        List<ServerRackVO> racks = serverRackService.getServerRacks();

        if (racks == null) {
            logger.error("ServerRackController.list>>>获取资源池失败");
            return "not_responsed";
        }

        model.addAttribute("racks", racks);

        return "/platform/server_rack/server_rack_manage";
    }

    @RequestMapping(value="/{uuid}/{room_name}/list",method= RequestMethod.GET)
    public String listByRoom(@PathVariable("uuid") String uuid, @PathVariable("room_name") String roomName, Model model,HttpServletRequest request) throws IOException {
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.server_rack_manage_query)){
            return "not_have_access";
        }
        List<ServerRackVO> racks = serverRackService.getServerRacksByRoom(uuid);

        if (racks == null) {
            logger.error("ServerRackController.listByRoom>>>获取资源池失败");
            return "not_responsed";
        }

        model.addAttribute("room_name", roomName);
        model.addAttribute("racks", racks);

        return "/platform/server_rack/show_server_rack";
    }
}

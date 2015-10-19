package com.zhicloud.ms.controller;

import com.zhicloud.ms.service.IServerService;
import com.zhicloud.ms.transform.constant.TransFormPrivilegeConstant;
import com.zhicloud.ms.transform.util.TransFormPrivilegeUtil;
import com.zhicloud.ms.vo.ServerVO;
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
@RequestMapping("/server")
public class ServerController {

    public static final Logger logger = Logger.getLogger(ResourcePoolController.class);

    @Resource
    IServerService serverService;

    @RequestMapping(value="/list",method= RequestMethod.GET)
    public String list(Model model,HttpServletRequest request) throws IOException {
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.servers_manage_query)){
            return "not_have_access";
        }

        List<ServerVO> servers = serverService.getServers();

        if (servers == null) {
            logger.error("ServerController.list>>>获取资源池失败");
            return "not_responsed";
        }
        model.addAttribute("servers", servers);

        return "/platform/server/server_manage";
    }

    @RequestMapping(value="/{uuid}/{rack_name}/list",method= RequestMethod.GET)
    public String listByRack(@PathVariable("uuid") String uuid, @PathVariable("rack_name") String rackName, Model model,HttpServletRequest request) throws IOException {
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.servers_manage_query)){
            return "not_have_access";
        }

        List<ServerVO> servers = serverService.getServersByRack(uuid);

        if (servers == null) {
            logger.error("ServerController.listByRack>>>获取资源池失败");
            return "not_responsed";
        }

        model.addAttribute("rack_name", rackName);
        model.addAttribute("servers", servers);

        return "/platform/server/show_server";
    }
}

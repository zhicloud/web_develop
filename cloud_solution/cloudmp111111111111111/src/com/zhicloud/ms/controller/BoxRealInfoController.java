package com.zhicloud.ms.controller;

import com.zhicloud.ms.service.IBoxRealInfoService;
import com.zhicloud.ms.transform.constant.TransFormPrivilegeConstant;
import com.zhicloud.ms.transform.util.TransFormPrivilegeUtil;
import com.zhicloud.ms.util.StringUtil;
import com.zhicloud.ms.vo.BoxRealInfoVO;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class BoxRealInfoController {
    
    public static final Logger logger = Logger.getLogger(ResourcePoolController.class);

    
    @Resource
    private IBoxRealInfoService boxRealInfoService;
    
    @RequestMapping(value="/boxrealinfo/list",method= RequestMethod.GET)
    public String list(Model model,HttpServletRequest request) throws IOException {
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.terminal_box_realinfo_query)){
            return "not_have_access";
        }

        String param = request.getParameter("param");
        String status = request.getParameter("status");

        if(StringUtil.isBlank(param)){
            param = null;
        }else{
            param = new String(request.getParameter("param").getBytes("ISO-8859-1"),"UTF-8");
            model.addAttribute("parameter", param);
            param = "%"+param+"%";
        }

        if(StringUtil.isBlank(status)){
            status = null;
        }

        model.addAttribute("status", status);

        Map<String, Object> condition = new LinkedHashMap<String, Object>();
        condition.put("user_name", param);

        List<BoxRealInfoVO> infoList = boxRealInfoService.getAllInfo(condition);
        Iterator<BoxRealInfoVO> it = infoList.iterator();

        if (!StringUtil.isBlank(status)) {
            if ("0".equals(status)) {
                while (it.hasNext()) {
                    if("在线".equals(it.next().caculateRealStatus())) {
                        it.remove();
                    }
                }
            }

            if ("1".equals(status)) {
                while (it.hasNext()) {
                    if("离线".equals(it.next().caculateRealStatus())) {
                        it.remove();
                    }
                }
            }
        }

        model.addAttribute("infoList", infoList);
        return "box/box_real_info_manage";
    }

}

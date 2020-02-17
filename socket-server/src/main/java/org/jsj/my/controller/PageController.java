package org.jsj.my.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author JSJ
 */
@Controller
public class PageController {

    /**
     * 首页
     * @return
     */
    @RequestMapping("/webSocket")
    public String page(){
        return "webSocket";
    }

}

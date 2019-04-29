package org.jsj.my.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 版本服务
 *
 * @author JSJ
 */
@RestController("VersionCtrl")
@RequestMapping("/my/public/version")
@Slf4j
@Api(description = "0.1.0 API", tags = {"Public"})
public class VersionController {
    @PostMapping("")
    @ApiOperation(value = "显示版本号",
            notes = "Server return the version ")
    public String version(HttpServletRequest servletRequest) {
        return "0.1.0";
    }
}

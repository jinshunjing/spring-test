package org.jsj.my.micro.controller;

import lombok.extern.slf4j.Slf4j;
import org.jsj.my.model.TxData;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 微服务
 *
 * @author JSJ
 */
@Slf4j
@RestController("TxCtrl")
@RequestMapping("/api/v1/tx")
public class TxController {

    @GetMapping("/{address}")
    public TxData queryByAddress(@PathVariable String address) {
        return null;
    }

    @PostMapping("/list")
    public List<TxData> listByAddress(@RequestBody String[] addrs){
        return null;
    }

    @PostMapping("/update")
    public Integer updateByAddress(@RequestBody TxData bean) {
        return 0;
    }

}

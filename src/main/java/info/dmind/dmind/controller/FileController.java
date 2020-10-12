package info.dmind.dmind.controller;

import info.dmind.dmind.domain.DmindObject;
import info.dmind.dmind.service.BaiduLacService;
import io.github.biezhi.ome.SendMailException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xmind.core.CoreException;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
@RequestMapping("/parse")
public class FileController {

    @Resource
    private BaiduLacService baiduLacService;

    @PostMapping(value = "/add",produces = "application/json;charset=UTF-8")
    public String add(@RequestBody DmindObject dmind) throws IOException, CoreException, SendMailException {
        baiduLacService.parse(dmind);
        return "OK";
    }

}

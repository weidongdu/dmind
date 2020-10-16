package info.dmind.dmind.controller;

import info.dmind.dmind.api.ApiResult;
import info.dmind.dmind.api.ApiResultManager;
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
import java.util.ArrayList;


@RestController
@RequestMapping("/parse")
public class FileController {

    @Resource
    private BaiduLacService baiduLacService;
    @Resource
    private ApiResultManager apiResultManager;

    @PostMapping(value = "/add",produces = "application/json;charset=UTF-8")
    public ApiResult add(@RequestBody DmindObject dmind) throws IOException, CoreException, SendMailException {
        baiduLacService.parse(dmind);
        return apiResultManager.success("ok");
    }

    @PostMapping(value = "/top",produces = "application/json;charset=UTF-8")
    public ApiResult top(@RequestBody DmindObject dmind) throws CoreException, SendMailException, IOException {
        ArrayList<String> list = baiduLacService.topN(dmind);
        return apiResultManager.success(list);
    }

}

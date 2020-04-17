package com.jackie.controller;

import com.jackie.com.jackie.entity.ConfigEntity;
import com.jackie.com.jackie.entity.ReplaceEntity;
import com.jackie.com.jackie.entity.Result;
import com.jackie.com.jackie.service.ConfigService;
import com.jackie.com.jackie.service.ReplaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("replaceConfig")
public class ReplaceController {

    @Autowired
    private ReplaceService replaceService;

    @Autowired
    private ConfigService configService;

    @GetMapping(value = "/config")
    public Result replace(@RequestBody ConfigEntity configEntity) {
        return configService.get(configEntity.getEnv(),configEntity.getGroupName(),configEntity.getServiceName());
    }

    @PostMapping(value = "/replaceConfig")
    public String replace(@RequestBody ReplaceEntity replaceEntity) {
        return replaceService.replaceConfig(replaceEntity);
    }
}

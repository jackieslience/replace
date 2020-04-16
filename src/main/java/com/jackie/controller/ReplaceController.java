package com.jackie.controller;

import com.jackie.com.jackie.entity.ReplaceEntity;
import com.jackie.com.jackie.service.ReplaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReplaceController {

    @Autowired
    private ReplaceService replaceService;

    @PostMapping(value = "/replaceConfig")
    public String replace(@RequestBody ReplaceEntity replaceEntity) {
        return replaceService.replaceConfig(replaceEntity);
    }
}

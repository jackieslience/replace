package com.jackie.controller;

import com.jackie.com.jackie.service.ReplaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReplaceController {

    @Autowired
    private ReplaceService replaceService;

    @PostMapping(value = "/replace")
    public Boolean replace(String json) {
        return replaceService.replaceConfig(json);
    }
}

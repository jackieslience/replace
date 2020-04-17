package com.jackie.com.jackie.service;

import com.jackie.com.jackie.entity.Result;

public interface ConfigService {

    Result get(String env, String groupName, String serviceName);
}

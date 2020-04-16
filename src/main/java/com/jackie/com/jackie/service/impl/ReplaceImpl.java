package com.jackie.com.jackie.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.jackie.com.jackie.entity.ReplaceEntity;
import com.jackie.com.jackie.service.ReplaceService;
import com.microsoft.azure.management.Azure;
import com.microsoft.azure.management.appservice.AppSetting;
import com.microsoft.azure.management.appservice.ConnectionStringType;
import com.microsoft.azure.management.appservice.WebApp;
import com.microsoft.rest.LogLevel;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

@Service
public class ReplaceImpl implements ReplaceService {

    private static final String DEV = "dev";
    private static final String INT = "int";
    private static final String PROD = "prod";
    private static final String ENV_DEV = "AZURE_AUTH_LOCATION_DEV";
    private static final String ENV_INT = "AZURE_AUTH_LOCATION_INT";
    private static final String ENV_PROD = "AZURE_AUTH_LOCATION_PROD";
    private static final String TYPE_APPSETTING = "appsetting";
    private static final String TYPE_DB = "db";

    @Override
    public String replaceConfig(ReplaceEntity replaceEntity) {
        String env = replaceEntity.getEnv();
        String groupName = replaceEntity.getGroupName();
        String serviceName = replaceEntity.getServiceName();
        String type = replaceEntity.getType();
        String json = replaceEntity.getJson();
        String model = replaceEntity.getModel();
        String environment = null;
        if (DEV.equals(env)) {
            environment = DEV;
        } else if (INT.equals(env)) {
            environment = INT;
        } else if (PROD.equals(env)) {
            environment = PROD;
        } else {
            return "env";
        }
//        String path = "classpath:Azureauth.properties_"+environment+".txt";
//        File credFile = null;
//        try {
//            credFile = ResourceUtils.getFile(path);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

        File credFile = new File(Class.class.getClass().getResource("/").getPath() +"Azureauth.properties_"+environment+".txt");
        if (TYPE_APPSETTING.equals(type)) {
            return replaceAppsetting(groupName, serviceName, json, credFile);
        } else if (TYPE_DB.equals(type)) {
            return replaceDB(groupName, serviceName, json, credFile);
        } else {
            return "type";
        }

    }

    private String replaceAppsetting(String groupName, String serviceName, String json, File credFile) {
        try {
            Azure azure = Azure.configure()
                    .withLogLevel(LogLevel.BASIC)
                    .authenticate(credFile)
                    .withDefaultSubscription();
            WebApp app = azure.webApps().getByResourceGroup(groupName, serviceName);
            Map<String, AppSetting> appSettings = app.getAppSettings();
            List<Map<String, String>> list = (List<Map<String, String>>) JSONObject.parse(json);
            list.forEach(temp -> {
                Map<String, String> map = temp;
                if (map.containsKey("name") && map.containsKey("value")) {
                    app.update()
                            .withAppSetting(map.get("name"), map.get("value"))
                            .apply();
                }
            });

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "success";

    }

    private String replaceDB(String groupName, String serviceName, String json, File credFile) {
        try {
            Azure azure = Azure.configure()
                    .withLogLevel(LogLevel.BASIC)
                    .authenticate(credFile)
                    .withDefaultSubscription();
            WebApp app = azure.webApps().getByResourceGroup(groupName, serviceName);
            List<Map<String, String>> list = (List<Map<String, String>>) JSONObject.parse(json);
            list.forEach(temp -> {
                Map<String, String> map = temp;
                if (map.containsKey("name") && map.containsKey("value")) {
                    app.update()
                            .withConnectionString(map.get("name"), map.get("value"),ConnectionStringType.fromString(map.get("type")))
                            .apply();
                }
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "success";
    }
}

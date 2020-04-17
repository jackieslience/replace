package com.jackie.com.jackie.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.jackie.com.jackie.entity.DevConfig;
import com.jackie.com.jackie.entity.IntConfig;
import com.jackie.com.jackie.entity.ProdConfig;
import com.jackie.com.jackie.entity.ReplaceEntity;
import com.jackie.com.jackie.service.ReplaceService;
import com.microsoft.azure.management.Azure;
import com.microsoft.azure.management.appservice.AppSetting;
import com.microsoft.azure.management.appservice.ConnectionStringType;
import com.microsoft.azure.management.appservice.WebApp;
import com.microsoft.rest.LogLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.Map;

@Service
public class ReplaceImpl implements ReplaceService {

    private static final String DEV = "dev";
    private static final String INT = "int";
    private static final String PROD = "prod";
    private static final String TYPE_APPSETTING = "appsetting";
    private static final String TYPE_DB = "db";

    @Autowired
    private IntConfig intConfig;

    @Autowired
    private ProdConfig prodConfig;

    @Autowired
    private DevConfig devConfig;

    @Override
    public String replaceConfig(ReplaceEntity replaceEntity) {
        String env = replaceEntity.getEnv();
        String groupName = replaceEntity.getGroupName();
        String serviceName = replaceEntity.getServiceName();
        String type = replaceEntity.getType();
        String json = replaceEntity.getJson();
        String model = replaceEntity.getModel();

        try {
            File credFile = readProperties(env);
            if(credFile==null)return "not found file";
            if (TYPE_APPSETTING.equals(type)) {
                replaceAppsetting(groupName, serviceName, json, credFile);
            } else if (TYPE_DB.equals(type)) {
                replaceDB(groupName, serviceName, json, credFile);
            } else {
                return "type invalid";
            }
            credFile.delete();
            return "success";
        } catch (IOException e) {
            return "IOException";
        }
    }

    private void replaceAppsetting(String groupName, String serviceName, String json, File credFile) {
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
                            .withAppSetting(map.get("name"), map.get("value"))
                            .apply();
                }
            });

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private void replaceDB(String groupName, String serviceName, String json, File credFile) {
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
                            .withConnectionString(map.get("name"), map.get("value"), ConnectionStringType.fromString(map.get("type")))
                            .apply();
                }
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private File readProperties(String env) throws IOException {
        File temp = File.createTempFile("tempfile", ".txt");
        PrintStream ps = new PrintStream(new FileOutputStream(temp));
        if (INT.equals(env)) {
            ps.println("subscription="+intConfig.getSubscription());
            ps.println("client="+intConfig.getClient());
            ps.println("key="+intConfig.getKey());
            ps.println("tenant="+intConfig.getTenant());
            ps.println("managementURI="+intConfig.getManagementURI());
            ps.println("baseURL="+intConfig.getBaseURL());
            ps.println("authURL="+intConfig.getAuthURL());
            ps.println("graphURL="+intConfig.getGraphURL());
            ps.close();
        } else if (PROD.equals(env)) {
            ps.println("subscription="+prodConfig.getSubscription());
            ps.println("client="+prodConfig.getClient());
            ps.println("key="+prodConfig.getKey());
            ps.println("tenant="+prodConfig.getTenant());
            ps.println("managementURI="+prodConfig.getManagementURI());
            ps.println("baseURL="+prodConfig.getBaseURL());
            ps.println("authURL="+prodConfig.getAuthURL());
            ps.println("graphURL="+prodConfig.getGraphURL());
            ps.close();
        } else if(DEV.equals(env)){
            ps.println("subscription="+devConfig.getSubscription());
            ps.println("client="+devConfig.getClient());
            ps.println("key="+devConfig.getKey());
            ps.println("tenant="+devConfig.getTenant());
            ps.println("managementURI="+devConfig.getManagementURI());
            ps.println("baseURL="+devConfig.getBaseURL());
            ps.println("authURL="+devConfig.getAuthURL());
            ps.println("graphURL="+devConfig.getGraphURL());
            ps.close();
        }else{
            return null;
        }

        return temp;
    }
}

package com.jackie.com.jackie.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.jackie.com.jackie.entity.DevConfig;
import com.jackie.com.jackie.entity.IntConfig;
import com.jackie.com.jackie.entity.ProdConfig;
import com.jackie.com.jackie.entity.Result;
import com.jackie.com.jackie.service.ConfigService;
import com.microsoft.azure.management.Azure;
import com.microsoft.azure.management.appservice.AppSetting;
import com.microsoft.azure.management.appservice.ConnectionString;
import com.microsoft.azure.management.appservice.WebApp;
import com.microsoft.rest.LogLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class ConfigImpl implements ConfigService {

    private static final String DEV = "dev";
    private static final String INT = "int";
    private static final String PROD = "prod";
    @Autowired
    private IntConfig intConfig;

    @Autowired
    private ProdConfig prodConfig;

    @Autowired
    private DevConfig devConfig;
    @Override
    public Result get(String env, String groupName, String serviceName) {
        Result result = new Result();
        try {
            File credFile = readProperties(env);
            if(credFile==null)return result;
            Azure azure = Azure.configure()
                    .withLogLevel(LogLevel.BASIC)
                    .authenticate(credFile)
                    .withDefaultSubscription();
            WebApp app = azure.webApps().getByResourceGroup(groupName, serviceName);
            Map<String, AppSetting> appSettings = app.getAppSettings();
            Map<String, ConnectionString> connectionStrings = app.getConnectionStrings();
            HashMap<String,String> appSettingsMap = new HashMap<>();
            appSettings.forEach((k,v)->{
                appSettingsMap.put(v.key(),v.value());
            });
            HashMap<String,String> connectionStringsMap = new HashMap<>();
            connectionStrings.forEach((k,v)->{
                connectionStringsMap.put(v.name(),v.value());
            });
            Object appSettingsJson = JSONObject.toJSON(appSettingsMap);
            Object connectionStringsJson = JSONObject.toJSON(connectionStringsMap);
            if(Objects.nonNull(appSettingsJson)){
                result.setAppSettingsJson(appSettingsJson.toString());
            }
            if(Objects.nonNull(connectionStringsJson)){
                result.setConnectionStringsJson(connectionStringsJson.toString());
            }
            credFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
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

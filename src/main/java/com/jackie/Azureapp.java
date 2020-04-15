package com.jackie;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.microsoft.azure.management.Azure;
import com.microsoft.azure.management.appservice.AppSetting;
import com.microsoft.azure.management.appservice.PricingTier;
import com.microsoft.azure.management.appservice.WebApp;
import com.microsoft.azure.management.resources.fluentcore.arm.Region;
import com.microsoft.rest.LogLevel;

import java.io.File;
import java.util.Map;

public class Azureapp {

    public static void main(String[] args) {
        try {
            final File credFile = new File(System.getenv("AZURE_AUTH_LOCATION_INT"));
            Azure azure = Azure.configure()
                    .withLogLevel(LogLevel.BASIC)
                    .authenticate(credFile)
                    .withDefaultSubscription();
            WebApp app = azure.webApps().getByResourceGroup("MyBMW-CE2-AfsStoreService-INT","mybmw-afsstoreservice-int");
            Map<String, AppSetting> appSettings = app.getAppSettings();
            System.out.println(JSON.toJSONString(appSettings));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}

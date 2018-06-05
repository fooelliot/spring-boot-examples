package com.andy.pay.modules.weixin.service;

import com.andy.pay.modules.weixin.config.AppProperty;
import com.andy.pay.modules.weixin.entity.WeChatUserInfo;
import com.andy.pay.common.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @Description:
 * @Author: Mr.lyon
 * @CreateBy: 2018-05-22 20:49
 **/
@Slf4j
@Service
public class WeChatServiceImpl {


    @Autowired
    private AppProperty appProperty;

    @Autowired
    private RestTemplate restTemplate;

    public WeChatUserInfo getUserInfo() {
        String code = getCode();
        String token = getToken(code);
        Map<String, String> jsonMap = JsonUtils.fromJson(token, Map.class);

        String error = jsonMap.get("errcode");
        String access_token = jsonMap.get("access_token");
        String openId = jsonMap.get("openid");

        String userInfoUrl = String.format(appProperty.getWeChat().getUrl().getUserInfoUrl(), access_token, openId);
        WeChatUserInfo userInfo = restTemplate.getForObject(userInfoUrl, WeChatUserInfo.class);
        log.info("微信获取用户信息的url:{}---->获取的user:{}", userInfoUrl, userInfo);
        return userInfo;
    }

    public String getCode() {
        String authUrl = String.format(appProperty.getWeChat().getUrl().getAuthCodeUrl(),
                appProperty.getWeChat().getAppid(), appProperty.getWeChat().getNotifyUrl());
        String code = restTemplate.getForObject(authUrl, String.class);
        log.info("微信获取授权码的url:{}---->获取的code:{}", authUrl, code);
        return code;
    }


    public String getToken(String code) {
        String tokenUrl = String.format(appProperty.getWeChat().getUrl().getTokenUrl(), appProperty.getWeChat().getAppid(), appProperty.getWeChat().getAppSecret(), code);
        String token = restTemplate.getForObject(tokenUrl, String.class);
        log.info("微信获取token的url:{}---->获取的token:{}", tokenUrl, token);
        return token;
    }
}
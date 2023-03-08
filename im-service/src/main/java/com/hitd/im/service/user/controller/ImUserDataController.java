package com.hitd.im.service.user.controller;

import com.hitd.im.common.ResponseVO;
import com.hitd.im.service.user.dao.ImUserDataEntity;
import com.hitd.im.service.user.model.req.GetUserInfoReq;
import com.hitd.im.service.user.model.req.ImportUserReq;
import com.hitd.im.service.user.model.req.ModifyUserInfoReq;
import com.hitd.im.service.user.model.req.UserId;
import com.hitd.im.service.user.model.resp.GetUserInfoResp;
import com.hitd.im.service.user.model.resp.ImportUserResp;
import com.hitd.im.service.user.service.ImUserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * @Author ZhangWeinan
 * @Date 2023/3/8 10:49
 * @DES
 * @Since Copyright(c)
 */
@RestController
@RequestMapping("v1/user/data")
public class ImUserDataController {

    @Resource
    private ImUserService imUserService;

    @RequestMapping("/getUserInfo")
    public ResponseVO<GetUserInfoResp> getUserInfo(@RequestBody GetUserInfoReq req, Integer appId){//@Validated
        req.setAppId(appId);
        return imUserService.getUserInfo(req);
    }

    @RequestMapping("/getSingleUserInfo")
    public ResponseVO<ImUserDataEntity> getSingleUserInfo(@RequestBody @Validated UserId req, Integer appId){
        req.setAppId(appId);
        return imUserService.getSingleUserInfo(req.getUserId(),req.getAppId());
    }

    @RequestMapping("/modifyUserInfo")
    public ResponseVO<?> modifyUserInfo(@RequestBody @Validated ModifyUserInfoReq req, Integer appId){
        req.setAppId(appId);
        return imUserService.modifyUserInfo(req);
    }
}

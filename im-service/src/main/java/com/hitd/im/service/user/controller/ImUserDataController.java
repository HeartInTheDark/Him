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
 * @DES 用户数据接口
 * @Since Copyright(c)
 */
@RestController
@RequestMapping("v1/user/data")
public class ImUserDataController {

    @Resource
    private ImUserService imUserService;

    /**
     * @author ZhangWeinan
     * @date 2023-03-09 21:43
     * @description 获取【多个】用户信息
     */
    @RequestMapping("/getUserInfo")
    public ResponseVO<GetUserInfoResp> getUserInfo(@RequestBody GetUserInfoReq req, Integer appId){//@Validated
        req.setAppId(appId);
        return imUserService.getUserInfo(req);
    }

    /**
     * @author ZhangWeinan
     * @date 2023-03-09 21:43
     * @description 获取一个用户信息
     */
    @RequestMapping("/getSingleUserInfo")
    public ResponseVO<ImUserDataEntity> getSingleUserInfo(@RequestBody @Validated UserId req, Integer appId){
        req.setAppId(appId);
        return imUserService.getSingleUserInfo(req.getUserId(),req.getAppId());
    }

    /**
     * @author ZhangWeinan
     * @date 2023-03-09 21:43
     * @description 修改用户信息
     */
    @RequestMapping("/modifyUserInfo")
    public ResponseVO<?> modifyUserInfo(@RequestBody @Validated ModifyUserInfoReq req, Integer appId){
        req.setAppId(appId);
        return imUserService.modifyUserInfo(req);
    }
}

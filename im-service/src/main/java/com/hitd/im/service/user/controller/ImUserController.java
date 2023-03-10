package com.hitd.im.service.user.controller;

import com.hitd.im.common.R;
import com.hitd.im.service.user.model.req.*;
import com.hitd.im.service.user.model.resp.ImportUserResp;
import com.hitd.im.service.user.service.ImUserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author ZhangWeinan
 * @date 2023-03-08 11:34
 * @description 用户接口
 */
@RestController
@RequestMapping("v1/user")
public class ImUserController {
    @Resource
    ImUserService imUserService;

    /**
     * @author ZhangWeinan
     * @date 2023-03-09 21:42
     * @description 导入用户
     */
    @PostMapping("importUser")
    public R<ImportUserResp> importUser(@RequestBody ImportUserReq req, Integer appId){
        req.setAppId(appId);
        return imUserService.importUser(req);
    }


    /**
     * @author ZhangWeinan
     * @date 2023-03-09 21:42
     * @description 删除用户
     */
    @RequestMapping("/deleteUser")
    public R<?> deleteUser(@RequestBody @Validated DeleteUserReq req, Integer appId) {
        req.setAppId(appId);
        return imUserService.deleteUser(req);
    }

    /**
     * @author ZhangWeinan
     * @date 2023-03-09 21:42
     * @description im的登录接口，返回im地址
     */
    @RequestMapping("/login")
    public R login(@RequestBody @Validated LoginReq req, Integer appId) {
        req.setAppId(appId);

//        ResponseVO login = imUserService.login(req);
//        if (login.isOk()) {
//            List<String> allNode = new ArrayList<>();
//            if (req.getClientType() == ClientType.WEB.getCode()) {
//                allNode = zKit.getAllWebNode();
//            } else {
//                allNode = zKit.getAllTcpNode();
//            }
//            String s = routeHandle.routeServer(allNode, req
//                    .getUserId());
//            RouteInfo parse = RouteInfoParseUtil.parse(s);
//            return ResponseVO.successResponse(parse);
//        }

        return R.errorResponse();
    }

    @RequestMapping("/getUserSequence")
    public R getUserSequence(@RequestBody @Validated
                                              GetUserSequenceReq req, Integer appId) {
        req.setAppId(appId);
        return imUserService.getUserSequence(req);
    }

    @RequestMapping("/subscribeUserOnlineStatus")
    public R subscribeUserOnlineStatus(@RequestBody @Validated
                                                        SubscribeUserOnlineStatusReq req, Integer appId, String identifier) {
//        req.setAppId(appId);
//        req.setOperater(identifier);
//        imUserStatusService.subscribeUserOnlineStatus(req);
        return R.successResponse();
    }

    @RequestMapping("/setUserCustomerStatus")
    public R setUserCustomerStatus(@RequestBody @Validated
                                                        SetUserCustomerStatusReq req, Integer appId, String identifier) {
//        req.setAppId(appId);
//        req.setOperater(identifier);
//        imUserStatusService.setUserCustomerStatus(req);
        return R.successResponse();
    }

    @RequestMapping("/queryFriendOnlineStatus")
    public R queryFriendOnlineStatus(@RequestBody @Validated
                                                      PullFriendOnlineStatusReq req, Integer appId, String identifier) {
//        req.setAppId(appId);
//        req.setOperater(identifier);
//        return ResponseVO.successResponse(imUserStatusService.queryFriendOnlineStatus(req));
        return null;
    }

    @RequestMapping("/queryUserOnlineStatus")
    public R queryUserOnlineStatus(@RequestBody @Validated
                                                      PullUserOnlineStatusReq req, Integer appId, String identifier) {
        req.setAppId(appId);
//        req.setOperater(identifier);
//        return ResponseVO.successResponse(imUserStatusService.queryUserOnlineStatus(req));
        return null;
    }



}

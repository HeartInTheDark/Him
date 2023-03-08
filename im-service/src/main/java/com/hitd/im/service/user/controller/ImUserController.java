package com.hitd.im.service.user.controller;

import com.hitd.im.common.ClientType;
import com.hitd.im.common.ResponseVO;
import com.hitd.im.service.user.model.req.*;
import com.hitd.im.service.user.model.resp.ImportUserResp;
import com.hitd.im.service.user.service.ImUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ZhangWeinan
 * @date 2023-03-08 11:34
 * @description
 */
@RestController
@RequestMapping("v1/user")
public class ImUserController {
    @Resource
    ImUserService imUserService;

    @PostMapping("importUser")
    public ResponseVO<ImportUserResp> importUser(@RequestBody ImportUserReq req, Integer appId){
        req.setAppId(appId);
        return imUserService.importUser(req);
    }


    @RequestMapping("/deleteUser")
    public ResponseVO<?>  deleteUser(@RequestBody @Validated DeleteUserReq req, Integer appId) {
        req.setAppId(appId);
        return imUserService.deleteUser(req);
    }

    /**
     * @param req
     * @return com.lld.im.common.ResponseVO
     * @description im的登录接口，返回im地址
     * @author chackylee
     */
    @RequestMapping("/login")
    public ResponseVO login(@RequestBody @Validated LoginReq req, Integer appId) {
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

        return ResponseVO.errorResponse();
    }

    @RequestMapping("/getUserSequence")
    public ResponseVO getUserSequence(@RequestBody @Validated
                                              GetUserSequenceReq req, Integer appId) {
        req.setAppId(appId);
        return imUserService.getUserSequence(req);
    }

    @RequestMapping("/subscribeUserOnlineStatus")
    public ResponseVO subscribeUserOnlineStatus(@RequestBody @Validated
                                                        SubscribeUserOnlineStatusReq req, Integer appId,String identifier) {
//        req.setAppId(appId);
//        req.setOperater(identifier);
//        imUserStatusService.subscribeUserOnlineStatus(req);
        return ResponseVO.successResponse();
    }

    @RequestMapping("/setUserCustomerStatus")
    public ResponseVO setUserCustomerStatus(@RequestBody @Validated
                                                        SetUserCustomerStatusReq req, Integer appId,String identifier) {
//        req.setAppId(appId);
//        req.setOperater(identifier);
//        imUserStatusService.setUserCustomerStatus(req);
        return ResponseVO.successResponse();
    }

    @RequestMapping("/queryFriendOnlineStatus")
    public ResponseVO queryFriendOnlineStatus(@RequestBody @Validated
                                                      PullFriendOnlineStatusReq req, Integer appId,String identifier) {
//        req.setAppId(appId);
//        req.setOperater(identifier);
//        return ResponseVO.successResponse(imUserStatusService.queryFriendOnlineStatus(req));
        return null;
    }

    @RequestMapping("/queryUserOnlineStatus")
    public ResponseVO queryUserOnlineStatus(@RequestBody @Validated
                                                      PullUserOnlineStatusReq req, Integer appId,String identifier) {
        req.setAppId(appId);
//        req.setOperater(identifier);
//        return ResponseVO.successResponse(imUserStatusService.queryUserOnlineStatus(req));
        return null;
    }



}

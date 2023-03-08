package com.hitd.im.service.friendship.controller;


import com.hitd.im.common.ResponseVO;
import com.hitd.im.service.friendship.dao.ImFriendShipRequestEntity;
import com.hitd.im.service.friendship.model.req.ApproverFriendRequestReq;
import com.hitd.im.service.friendship.model.req.GetFriendShipRequestReq;
import com.hitd.im.service.friendship.model.req.ReadFriendShipRequestReq;
import com.hitd.im.service.friendship.service.ImFriendShipRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("v1/friendshipRequest")
public class ImFriendShipRequestController {

    @Autowired
    ImFriendShipRequestService imFriendShipRequestService;

    @RequestMapping("/approveFriendRequest")
    public ResponseVO<?> approveFriendRequest(@RequestBody @Validated
                                           ApproverFriendRequestReq req, Integer appId, String identifier){
        req.setAppId(appId);
        req.setOperator(identifier);
        return imFriendShipRequestService.approveFriendRequest(req);
    }
    @RequestMapping("/getFriendRequest")
    public ResponseVO<List<ImFriendShipRequestEntity>> getFriendRequest(@RequestBody @Validated GetFriendShipRequestReq req, Integer appId){
        req.setAppId(appId);
        return imFriendShipRequestService.getFriendRequest(req.getFromId(),req.getAppId());
    }

    @RequestMapping("/readFriendShipRequestReq")
    public ResponseVO<?> readFriendShipRequestReq(@RequestBody @Validated ReadFriendShipRequestReq req, Integer appId){
        req.setAppId(appId);
        return imFriendShipRequestService.readFriendShipRequestReq(req);
    }


}

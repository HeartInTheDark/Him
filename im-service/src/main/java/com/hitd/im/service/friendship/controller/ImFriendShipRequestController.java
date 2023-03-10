package com.hitd.im.service.friendship.controller;


import com.hitd.im.common.R;
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

/**
 * @author ZhangWeinan
 * @date 2023-03-09 21:40
 * @description  添加好友接口
 */
@RestController
@RequestMapping("v1/friendshipRequest")
public class ImFriendShipRequestController {

    @Autowired
    ImFriendShipRequestService imFriendShipRequestService;

    /**
     * @author ZhangWeinan
     * @date 2023-03-09 22:04
     * @description 好友申请审核
     */
    @RequestMapping("/approveFriendRequest")
    public R<?> approveFriendRequest(@RequestBody @Validated
                                           ApproverFriendRequestReq req, Integer appId, String identifier){
        req.setAppId(appId);
        req.setOperator(identifier);
        return imFriendShipRequestService.approveFriendRequest(req);
    }

    /**
     * @author ZhangWeinan
     * @date 2023-03-09 22:05
     * @description 获取好友申请记录
     */
    @RequestMapping("/getFriendRequest")
    public R<List<ImFriendShipRequestEntity>> getFriendRequest(@RequestBody @Validated GetFriendShipRequestReq req, Integer appId){
        req.setAppId(appId);
        return imFriendShipRequestService.getFriendRequest(req.getFromId(),req.getAppId());
    }

    /**
     * @author ZhangWeinan
     * @date 2023-03-09 22:05
     * @description 查看好友申请
     */
    @RequestMapping("/readFriendShipRequestReq")
    public R<?> readFriendShipRequestReq(@RequestBody @Validated ReadFriendShipRequestReq req, Integer appId){
        req.setAppId(appId);
        return imFriendShipRequestService.readFriendShipRequestReq(req);
    }


}

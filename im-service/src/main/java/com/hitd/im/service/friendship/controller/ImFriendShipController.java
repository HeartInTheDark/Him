package com.hitd.im.service.friendship.controller;


import com.hitd.im.common.ResponseVO;
import com.hitd.im.service.friendship.dao.ImFriendShipEntity;
import com.hitd.im.service.friendship.model.req.*;
import com.hitd.im.service.friendship.model.resp.CheckFriendShipResp;
import com.hitd.im.service.friendship.model.resp.ImportFriendShipResp;
import com.hitd.im.service.friendship.service.ImFriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ZhangWeinan
 * @date 2023-03-09 21:39
 * @description 好友关系链接口
 */
@RestController
@RequestMapping("v1/friendship")
public class ImFriendShipController {

    @Autowired
    ImFriendService imFriendShipService;

    /**
     * @author ZhangWeinan
     * @date 2023-03-09 21:45
     * @description 导入好友关系
     */
    @RequestMapping("/importFriendShip")
    public ResponseVO<ImportFriendShipResp> importFriendShip(@RequestBody @Validated ImportFriendShipReq req, Integer appId){
        req.setAppId(appId);
        return imFriendShipService.importFriendShip(req);
    }

    /**
     * @author ZhangWeinan
     * @date 2023-03-09 21:45
     * @description 添加好友
     */
    @RequestMapping("/addFriend")
    public ResponseVO<?> addFriend(@RequestBody @Validated AddFriendReq req, Integer appId){
        req.setAppId(appId);
        return imFriendShipService.addFriend(req);
    }

    /**
     * @author ZhangWeinan
     * @date 2023-03-09 21:45
     * @description 修改好友关系
     */
    @RequestMapping("/updateFriend")
    public ResponseVO<?> updateFriend(@RequestBody @Validated UpdateFriendReq req, Integer appId){
        req.setAppId(appId);
        return imFriendShipService.updateFriend(req);
    }

    /**
     * @author ZhangWeinan
     * @date 2023-03-09 21:46
     * @description 修改好友
     */
    @RequestMapping("/deleteFriend")
    public ResponseVO<?> deleteFriend(@RequestBody @Validated DeleteFriendReq req, Integer appId){
        req.setAppId(appId);
        return imFriendShipService.deleteFriend(req);
    }

    /**
     * @author ZhangWeinan
     * @date 2023-03-09 21:46
     * @description 删除所有好友
     */
    @RequestMapping("/deleteAllFriend")
    public ResponseVO<?> deleteAllFriend(@RequestBody @Validated DeleteFriendReq req, Integer appId){
        req.setAppId(appId);
        return imFriendShipService.deleteAllFriend(req);
    }

    /**
     * @author ZhangWeinan
     * @date 2023-03-09 21:57
     * @description 获取所有好友
     */
    @RequestMapping("/getAllFriendShip")
    public ResponseVO<List<ImFriendShipEntity>> getAllFriendShip(@RequestBody @Validated GetAllFriendShipReq req, Integer appId){
        req.setAppId(appId);
        return imFriendShipService.getAllFriendShip(req);
    }

    /**
     * @author ZhangWeinan
     * @date 2023-03-09 21:58
     * @description 获取好友链
     */
    @RequestMapping("/getRelation")
    public ResponseVO<ImFriendShipEntity> getRelation(@RequestBody @Validated GetRelationReq req, Integer appId){
        req.setAppId(appId);
        return imFriendShipService.getRelation(req);
    }

    /**
     * @author ZhangWeinan
     * @date 2023-03-09 21:59
     * @description 检查好友 【1单向检查、2双向检查】
     */
    @RequestMapping("/checkFriend")
    public ResponseVO<List<CheckFriendShipResp>> checkFriend(@RequestBody @Validated CheckFriendShipReq req, Integer appId){
        req.setAppId(appId);
        return imFriendShipService.checkFriendship(req);
    }

    /**
     * @author ZhangWeinan
     * @date 2023-03-09 22:00
     * @description 加入黑名单
     */
    @RequestMapping("/addBlack")
    public ResponseVO<?> addBlack(@RequestBody @Validated AddFriendShipBlackReq req, Integer appId){
        req.setAppId(appId);
        return imFriendShipService.addBlack(req);
    }

    /**
     * @author ZhangWeinan
     * @date 2023-03-09 22:00
     * @description 移出黑名单
     */
    @RequestMapping("/deleteBlack")
    public ResponseVO<?> deleteBlack(@RequestBody @Validated DeleteBlackReq req, Integer appId){
        req.setAppId(appId);
        return imFriendShipService.deleteBlack(req);
    }

    /**
     * @author ZhangWeinan
     * @date 2023-03-09 22:02
     * @description 检查是否在黑名单
     */
    @RequestMapping("/checkBlack")
    public ResponseVO<?> checkBlack(@RequestBody @Validated CheckFriendShipReq req, Integer appId){
        req.setAppId(appId);
        return imFriendShipService.checkBlack(req);
    }


//    @RequestMapping("/syncFriendshipList")
//    public ResponseVO<?> syncFriendshipList(@RequestBody @Validated
//                                                 SyncReq req, Integer appId){
//        req.setAppId(appId);
//        return imFriendShipService.syncFriendshipList(req);
//    }
}

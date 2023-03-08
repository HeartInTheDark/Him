package com.hitd.im.service.friendship.controller;


import com.hitd.im.common.ResponseVO;
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

@RestController
@RequestMapping("v1/friendship")
public class ImFriendShipController {

    @Autowired
    ImFriendService imFriendShipService;

    @RequestMapping("/importFriendShip")
    public ResponseVO<ImportFriendShipResp> importFriendShip(@RequestBody @Validated ImportFriendShipReq req, Integer appId){
        req.setAppId(appId);
        return imFriendShipService.importFriendShip(req);
    }

    @RequestMapping("/addFriend")
    public ResponseVO<?> addFriend(@RequestBody @Validated AddFriendReq req, Integer appId){
        req.setAppId(appId);
        return imFriendShipService.addFriend(req);
    }

    @RequestMapping("/updateFriend")
    public ResponseVO<?> updateFriend(@RequestBody @Validated UpdateFriendReq req, Integer appId){
        req.setAppId(appId);
        return imFriendShipService.updateFriend(req);
    }

    @RequestMapping("/deleteFriend")
    public ResponseVO<?> deleteFriend(@RequestBody @Validated DeleteFriendReq req, Integer appId){
        req.setAppId(appId);
        return imFriendShipService.deleteFriend(req);
    }

    @RequestMapping("/deleteAllFriend")
    public ResponseVO<?> deleteAllFriend(@RequestBody @Validated DeleteFriendReq req, Integer appId){
        req.setAppId(appId);
        return imFriendShipService.deleteAllFriend(req);
    }

    @RequestMapping("/getAllFriendShip")
    public ResponseVO<?> getAllFriendShip(@RequestBody @Validated GetAllFriendShipReq req, Integer appId){
        req.setAppId(appId);
        return imFriendShipService.getAllFriendShip(req);
    }

    @RequestMapping("/getRelation")
    public ResponseVO<?> getRelation(@RequestBody @Validated GetRelationReq req, Integer appId){
        req.setAppId(appId);
        return imFriendShipService.getRelation(req);
    }

    @RequestMapping("/checkFriend")
    public ResponseVO<List<CheckFriendShipResp>> checkFriend(@RequestBody @Validated CheckFriendShipReq req, Integer appId){
        req.setAppId(appId);
        return imFriendShipService.checkFriendship(req);
    }
//
//    @RequestMapping("/addBlack")
//    public ResponseVO<?> addBlack(@RequestBody @Validated AddFriendShipBlackReq req, Integer appId){
//        req.setAppId(appId);
//        return imFriendShipService.addBlack(req);
//    }
//
//    @RequestMapping("/deleteBlack")
//    public ResponseVO<?> deleteBlack(@RequestBody @Validated DeleteBlackReq req, Integer appId){
//        req.setAppId(appId);
//        return imFriendShipService.deleteBlack(req);
//    }
//
//    @RequestMapping("/checkBlck")
//    public ResponseVO<?> checkBlck(@RequestBody @Validated CheckFriendShipReq req, Integer appId){
//        req.setAppId(appId);
//        return imFriendShipService.checkBlck(req);
//    }
//
//
//    @RequestMapping("/syncFriendshipList")
//    public ResponseVO<?> syncFriendshipList(@RequestBody @Validated
//                                                 SyncReq req, Integer appId){
//        req.setAppId(appId);
//        return imFriendShipService.syncFriendshipList(req);
//    }
}

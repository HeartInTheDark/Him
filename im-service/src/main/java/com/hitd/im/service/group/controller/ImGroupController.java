package com.hitd.im.service.group.controller;


import com.hitd.im.common.ResponseVO;
import com.hitd.im.service.group.model.req.*;
import com.hitd.im.service.group.service.ImGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ZhangWeinan
 * @date 2023-03-09 21:20
 * @description 群组接口
 */
@RestController
@RequestMapping("v1/group")
public class ImGroupController {

    @Autowired
    ImGroupService groupService;


    /**
     * @author ZhangWeinan
     * @date 2023-03-09 22:05
     * @description 导入群组
     */
    @RequestMapping("/importGroup")
    public ResponseVO<?> importGroup(@RequestBody @Validated ImportGroupReq req, Integer appId, String identifier)  {
        req.setAppId(appId);
        req.setOperator(identifier);
        return groupService.importGroup(req);
    }

    /**
     * @author ZhangWeinan
     * @date 2023-03-09 22:06
     * @description 创建群组
     */
    @RequestMapping("/createGroup")
    public ResponseVO<?> createGroup(@RequestBody @Validated CreateGroupReq req, Integer appId, String identifier)  {
        req.setAppId(appId);
        req.setOperator(identifier);
        return groupService.createGroup(req);
    }

    /**
     * @author ZhangWeinan
     * @date 2023-03-09 22:06
     * @description 获取群组信息
     */
    @RequestMapping("/getGroupInfo")
    public ResponseVO<?> getGroupInfo(@RequestBody @Validated GetGroupReq req, Integer appId)  {
        req.setAppId(appId);
        return groupService.getGroup(req);
    }

    /**
     * @author ZhangWeinan
     * @date 2023-03-09 22:06
     * @description 修改群组信息
     */
    @RequestMapping("/update")
    public ResponseVO<?> update(@RequestBody @Validated UpdateGroupReq req, Integer appId, String identifier)  {
        req.setAppId(appId);
        req.setOperator(identifier);
        return groupService.updateBaseGroupInfo(req);
    }

    /**
     * @author ZhangWeinan
     * @date 2023-03-09 22:06
     * @description 获取加入的群组
     */
    @RequestMapping("/getJoinedGroup")
    public ResponseVO<?> getJoinedGroup(@RequestBody @Validated GetJoinedGroupReq req, Integer appId, String identifier)  {
        req.setAppId(appId);
        req.setOperator(identifier);
        return groupService.getJoinedGroup(req);
    }


    /**
     * @author ZhangWeinan
     * @date 2023-03-09 22:06
     * @description 解散群组
     */
    @RequestMapping("/destroyGroup")
    public ResponseVO<?> destroyGroup(@RequestBody @Validated DestroyGroupReq req, Integer appId, String identifier)  {
        req.setAppId(appId);
        req.setOperator(identifier);
        return groupService.destroyGroup(req);
    }

    /**
     * @author ZhangWeinan
     * @date 2023-03-09 22:07
     * @description 转让群组
     */
    @RequestMapping("/transferGroup")
    public ResponseVO<?> transferGroup(@RequestBody @Validated TransferGroupReq req, Integer appId, String identifier)  {
        req.setAppId(appId);
        req.setOperator(identifier);
        return groupService.transferGroup(req);
    }

    /**
     * @author ZhangWeinan
     * @date 2023-03-09 22:15
     * @description 禁言群
     */
    @RequestMapping("/forbidSendMessage")
    public ResponseVO<?> forbidSendMessage(@RequestBody @Validated MuteGroupReq req, Integer appId, String identifier)  {
        req.setAppId(appId);
        req.setOperator(identifier);
        return groupService.muteGroup(req);
    }

//    @RequestMapping("/sendMessage")
//    public ResponseVO<?> sendMessage(@RequestBody @Validated SendGroupMessageReq
//                                              req, Integer appId,
//                                  String identifier)  {
//        req.setAppId(appId);
//        req.setOperator(identifier);
//        return ResponseVO.successResponse(groupMessageService.send(req));
//    }

//    @RequestMapping("/syncJoinedGroup")
//    public ResponseVO<?> syncJoinedGroup(@RequestBody @Validated SyncReq req, Integer appId, String identifier)  {
//        req.setAppId(appId);
//        return groupService.syncJoinedGroupList(req);
//    }

}

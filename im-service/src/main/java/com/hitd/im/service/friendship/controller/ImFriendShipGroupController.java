package com.hitd.im.service.friendship.controller;


import com.hitd.im.common.R;
import com.hitd.im.service.friendship.model.req.AddFriendShipGroupMemberReq;
import com.hitd.im.service.friendship.model.req.AddFriendShipGroupReq;
import com.hitd.im.service.friendship.model.req.DeleteFriendShipGroupMemberReq;
import com.hitd.im.service.friendship.model.req.DeleteFriendShipGroupReq;
import com.hitd.im.service.friendship.service.ImFriendShipGroupMemberService;
import com.hitd.im.service.friendship.service.ImFriendShipGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ZhangWeinan
 * @date 2023-03-09 21:40
 * @description 好友分组接口
 */
@RestController
@RequestMapping("v1/friendship/group")
public class ImFriendShipGroupController {

    @Autowired
    ImFriendShipGroupService imFriendShipGroupService;

    @Autowired
    ImFriendShipGroupMemberService imFriendShipGroupMemberService;


    /**
     * @author ZhangWeinan
     * @date 2023-03-09 22:03
     * @description 添加分组
     */
    @RequestMapping("/add")
    public R<?> add(@RequestBody @Validated AddFriendShipGroupReq req, Integer appId)  {
        req.setAppId(appId);
        return imFriendShipGroupService.addGroup(req);
    }

    /**
     * @author ZhangWeinan
     * @date 2023-03-09 22:03
     * @description 删除分组
     */
    @RequestMapping("/del")
    public R<?> del(@RequestBody @Validated DeleteFriendShipGroupReq req, Integer appId)  {
        req.setAppId(appId);
        return imFriendShipGroupService.deleteGroup(req);
    }

    /**
     * @author ZhangWeinan
     * @date 2023-03-09 22:04
     * @description 向分组添加好友
     */
    @RequestMapping("/member/add")
    public R<?> memberAdd(@RequestBody @Validated AddFriendShipGroupMemberReq req, Integer appId)  {
        req.setAppId(appId);
        return imFriendShipGroupMemberService.addGroupMember(req);
    }

    /**
     * @author ZhangWeinan
     * @date 2023-03-09 22:04
     * @description 从分组删除好友
     */
    @RequestMapping("/member/del")
    public R<?> memberDel(@RequestBody @Validated DeleteFriendShipGroupMemberReq req, Integer appId)  {
        req.setAppId(appId);
        return imFriendShipGroupMemberService.delGroupMember(req);
    }


}

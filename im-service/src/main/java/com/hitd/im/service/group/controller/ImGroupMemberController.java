package com.hitd.im.service.group.controller;

import com.hitd.im.common.R;
import com.hitd.im.service.group.model.req.*;
import com.hitd.im.service.group.model.resp.AddMemberResp;
import com.hitd.im.service.group.service.ImGroupMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ZhangWeinan
 * @date 2023-03-09 21:20
 * @description 群组成员接口
 */
@RestController
@RequestMapping("v1/group/member")
public class ImGroupMemberController {

    @Autowired
    ImGroupMemberService groupMemberService;

    /**
     * @author ZhangWeinan
     * @date 2023-03-09 22:08
     * @description 导入群成员
     */
    @RequestMapping("/importGroupMember")
    public R<List<AddMemberResp>> importGroupMember(@RequestBody @Validated ImportGroupMemberReq req, Integer appId, String identifier)  {
        req.setAppId(appId);
        req.setOperator(identifier);
        return groupMemberService.importGroupMember(req);
    }

    /**
     * @author ZhangWeinan
     * @date 2023-03-09 22:08
     * @description 添加群成员
     */
    @RequestMapping("/add")
    public R<?> addMember(@RequestBody @Validated AddGroupMemberReq req, Integer appId, String identifier)  {
        req.setAppId(appId);
        req.setOperator(identifier);
        return groupMemberService.addMember(req);
    }

    /**
     * @author ZhangWeinan
     * @date 2023-03-09 22:08
     * @description 移除群成员
     */
    @RequestMapping("/remove")
    public R<?> removeMember(@RequestBody @Validated RemoveGroupMemberReq req, Integer appId, String identifier)  {
        req.setAppId(appId);
        req.setOperator(identifier);
        return groupMemberService.removeMember(req);
    }

    /**
     * @author ZhangWeinan
     * @date 2023-03-09 22:08
     * @description 修改群成员信息
     */
    @RequestMapping("/update")
    public R<?> updateGroupMember(@RequestBody @Validated UpdateGroupMemberReq req, Integer appId, String identifier)  {
        req.setAppId(appId);
        req.setOperator(identifier);
        return groupMemberService.updateGroupMember(req);
    }

    /**
     * @author ZhangWeinan
     * @date 2023-03-09 22:16
     * @description 禁言群成员
     */
    @RequestMapping("/speak")
    public R<?> speak(@RequestBody @Validated SpeaMemberReq req, Integer appId, String identifier)  {
        req.setAppId(appId);
        req.setOperator(identifier);
        return groupMemberService.speak(req);
    }

}

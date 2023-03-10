package com.hitd.im.service.friendship.service;


import com.hitd.im.common.R;
import com.hitd.im.service.friendship.model.req.AddFriendShipGroupMemberReq;
import com.hitd.im.service.friendship.model.req.DeleteFriendShipGroupMemberReq;

/**
 * @author: Chackylee
 * @description:
 **/
public interface ImFriendShipGroupMemberService {

   R<?> addGroupMember(AddFriendShipGroupMemberReq req);

   R<?> delGroupMember(DeleteFriendShipGroupMemberReq req);

   int doAddGroupMember(Long groupId, String toId);

   int clearGroupMember(Long groupId);
}

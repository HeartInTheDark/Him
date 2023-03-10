package com.hitd.im.service.friendship.service;


import com.hitd.im.common.R;
import com.hitd.im.service.friendship.dao.ImFriendShipGroupEntity;
import com.hitd.im.service.friendship.model.req.AddFriendShipGroupReq;
import com.hitd.im.service.friendship.model.req.DeleteFriendShipGroupReq;

/**
 * @author: Chackylee
 * @description:
 **/
public interface ImFriendShipGroupService {

    R<?> addGroup(AddFriendShipGroupReq req);

    R<?> deleteGroup(DeleteFriendShipGroupReq req);

    R<ImFriendShipGroupEntity> getGroup(String fromId, String groupName, Integer appId);

    Long updateSeq(String fromId, String groupName, Integer appId);
}

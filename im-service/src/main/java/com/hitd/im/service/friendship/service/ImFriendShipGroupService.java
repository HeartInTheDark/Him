package com.hitd.im.service.friendship.service;


import com.hitd.im.common.ResponseVO;
import com.hitd.im.service.friendship.dao.ImFriendShipGroupEntity;
import com.hitd.im.service.friendship.model.req.AddFriendShipGroupReq;
import com.hitd.im.service.friendship.model.req.DeleteFriendShipGroupReq;

/**
 * @author: Chackylee
 * @description:
 **/
public interface ImFriendShipGroupService {

    ResponseVO<?> addGroup(AddFriendShipGroupReq req);

    ResponseVO<?> deleteGroup(DeleteFriendShipGroupReq req);

    ResponseVO<ImFriendShipGroupEntity> getGroup(String fromId, String groupName, Integer appId);

    Long updateSeq(String fromId, String groupName, Integer appId);
}

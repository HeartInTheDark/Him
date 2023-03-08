package com.hitd.im.service.friendship.service;


import com.hitd.im.common.ResponseVO;
import com.hitd.im.service.friendship.dao.ImFriendShipRequestEntity;
import com.hitd.im.service.friendship.model.req.ApproverFriendRequestReq;
import com.hitd.im.service.friendship.model.req.FriendDto;
import com.hitd.im.service.friendship.model.req.ReadFriendShipRequestReq;

import java.util.List;

public interface ImFriendShipRequestService {

    ResponseVO<?> addFriendshipRequest(String fromId, FriendDto dto, Integer appId);

    ResponseVO<?> approveFriendRequest(ApproverFriendRequestReq req);

    ResponseVO<?> readFriendShipRequestReq(ReadFriendShipRequestReq req);

    ResponseVO<List<ImFriendShipRequestEntity>> getFriendRequest(String fromId, Integer appId);
}

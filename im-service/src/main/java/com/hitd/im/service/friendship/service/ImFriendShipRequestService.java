package com.hitd.im.service.friendship.service;


import com.hitd.im.common.R;
import com.hitd.im.service.friendship.dao.ImFriendShipRequestEntity;
import com.hitd.im.service.friendship.model.req.ApproverFriendRequestReq;
import com.hitd.im.service.friendship.model.req.FriendDto;
import com.hitd.im.service.friendship.model.req.ReadFriendShipRequestReq;

import java.util.List;

public interface ImFriendShipRequestService {

    R<?> addFriendshipRequest(String fromId, FriendDto dto, Integer appId);

    R<?> approveFriendRequest(ApproverFriendRequestReq req);

    R<?> readFriendShipRequestReq(ReadFriendShipRequestReq req);

    R<List<ImFriendShipRequestEntity>> getFriendRequest(String fromId, Integer appId);
}

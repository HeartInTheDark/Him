package com.hitd.im.service.friendship.service;



import com.hitd.im.common.ResponseVO;
import com.hitd.im.common.model.RequestBase;
import com.hitd.im.service.friendship.model.req.*;
import com.hitd.im.service.friendship.model.resp.CheckFriendShipResp;
import com.hitd.im.service.friendship.model.resp.ImportFriendShipResp;

import java.util.List;


/**
 * @description:
 * @author: lld
 * @version: 1.0
 */
public interface ImFriendService {

    ResponseVO<ImportFriendShipResp> importFriendShip(ImportFriendShipReq req);

    ResponseVO<?> addFriend(AddFriendReq req);

    ResponseVO<?> doAddFriend(RequestBase requestBase, String fromId, FriendDto dto, Integer appId);

    ResponseVO<?> updateFriend(UpdateFriendReq req);

    ResponseVO<?> deleteFriend(DeleteFriendReq req);

    ResponseVO<?> deleteAllFriend(DeleteFriendReq req);

    ResponseVO<?> getAllFriendShip(GetAllFriendShipReq req);

    ResponseVO<?> getRelation(GetRelationReq req);

    ResponseVO<List<CheckFriendShipResp>> checkFriendship(CheckFriendShipReq req);

    ResponseVO<?> addBlack(AddFriendShipBlackReq req);

    ResponseVO<?> deleteBlack(DeleteBlackReq req);
}

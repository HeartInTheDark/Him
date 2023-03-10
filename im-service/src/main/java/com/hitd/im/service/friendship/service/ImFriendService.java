package com.hitd.im.service.friendship.service;



import com.hitd.im.common.R;
import com.hitd.im.common.model.RequestBase;
import com.hitd.im.service.friendship.dao.ImFriendShipEntity;
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

    R<ImportFriendShipResp> importFriendShip(ImportFriendShipReq req);

    R<?> addFriend(AddFriendReq req);

    R<?> doAddFriend(RequestBase requestBase, String fromId, FriendDto dto, Integer appId);

    R<?> updateFriend(UpdateFriendReq req);

    R<?> deleteFriend(DeleteFriendReq req);

    R<?> deleteAllFriend(DeleteFriendReq req);

    R<List<ImFriendShipEntity>> getAllFriendShip(GetAllFriendShipReq req);

    R<ImFriendShipEntity> getRelation(GetRelationReq req);

    R<List<CheckFriendShipResp>> checkFriendship(CheckFriendShipReq req);

    R<?> addBlack(AddFriendShipBlackReq req);

    R<?> deleteBlack(DeleteBlackReq req);

    R<?> checkBlack(CheckFriendShipReq req);
}

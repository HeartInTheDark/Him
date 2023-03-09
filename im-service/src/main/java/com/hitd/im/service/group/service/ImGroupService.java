package com.hitd.im.service.group.service;


import com.hitd.im.common.ResponseVO;
import com.hitd.im.service.group.dao.ImGroupEntity;
import com.hitd.im.service.group.model.req.*;

/**
 * @description:
 * @author: lld
 * @version: 1.0
 */
public interface ImGroupService {

    ResponseVO<?> importGroup(ImportGroupReq req);

    ResponseVO<?> createGroup(CreateGroupReq req);

    ResponseVO<?> updateBaseGroupInfo(UpdateGroupReq req);

    ResponseVO<?> getJoinedGroup(GetJoinedGroupReq req);

    ResponseVO<?> destroyGroup(DestroyGroupReq req);

    ResponseVO<?> transferGroup(TransferGroupReq req);

    ResponseVO<ImGroupEntity> getGroup(String groupId, Integer appId);

    ResponseVO<?> getGroup(GetGroupReq req);

    ResponseVO<?> muteGroup(MuteGroupReq req);

//    ResponseVO<?> syncJoinedGroupList(SyncReq req);

    Long getUserGroupMaxSeq(String userId, Integer appId);
}

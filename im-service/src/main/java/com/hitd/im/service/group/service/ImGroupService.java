package com.hitd.im.service.group.service;


import com.hitd.im.common.R;
import com.hitd.im.service.group.dao.ImGroupEntity;
import com.hitd.im.service.group.model.req.*;

/**
 * @description:
 * @author: lld
 * @version: 1.0
 */
public interface ImGroupService {

    R<?> importGroup(ImportGroupReq req);

    R<?> createGroup(CreateGroupReq req);

    R<?> updateBaseGroupInfo(UpdateGroupReq req);

    R<?> getJoinedGroup(GetJoinedGroupReq req);

    R<?> destroyGroup(DestroyGroupReq req);

    R<?> transferGroup(TransferGroupReq req);

    R<ImGroupEntity> getGroup(String groupId, Integer appId);

    R<?> getGroup(GetGroupReq req);

    R<?> muteGroup(MuteGroupReq req);

//    ResponseVO<?> syncJoinedGroupList(SyncReq req);

    Long getUserGroupMaxSeq(String userId, Integer appId);
}

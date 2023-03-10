package com.hitd.im.service.group.service;

import com.hitd.im.common.R;
import com.hitd.im.service.group.model.req.*;
import com.hitd.im.service.group.model.resp.AddMemberResp;
import com.hitd.im.service.group.model.resp.GetRoleInGroupResp;

import java.util.Collection;
import java.util.List;

/**
 * @description:
 * @author: lld
 * @version: 1.0
 */
public interface ImGroupMemberService {

    R<List<AddMemberResp>> importGroupMember(ImportGroupMemberReq req);

    R<?> addMember(AddGroupMemberReq req);

    R<?> removeMember(RemoveGroupMemberReq req);

    R<?> addGroupMember(String groupId, Integer appId, GroupMemberDto dto);

    R<?> removeGroupMember(String groupId, Integer appId, String memberId);

    R<GetRoleInGroupResp> getRoleInGroupOne(String groupId, String memberId, Integer appId);

    R<Collection<String>> getMemberJoinedGroup(GetJoinedGroupReq req);

    R<List<GroupMemberDto>> getGroupMember(String groupId, Integer appId);

    List<String> getGroupMemberId(String groupId, Integer appId);

    List<GroupMemberDto> getGroupManager(String groupId, Integer appId);

    R<?> updateGroupMember(UpdateGroupMemberReq req);

    R<?> transferGroupMember(String owner, String groupId, Integer appId);

    R<?> speak(SpeaMemberReq req);

    R<Collection<String>> syncMemberJoinedGroup(String operater, Integer appId);
}

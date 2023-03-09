package com.hitd.im.service.group.service;

import com.hitd.im.common.ResponseVO;
import com.hitd.im.service.group.model.req.*;
import com.hitd.im.service.group.model.resp.GetRoleInGroupResp;

import java.util.Collection;
import java.util.List;

/**
 * @description:
 * @author: lld
 * @version: 1.0
 */
public interface ImGroupMemberService {

    ResponseVO<?> importGroupMember(ImportGroupMemberReq req);

    ResponseVO<?> addMember(AddGroupMemberReq req);

    ResponseVO<?> removeMember(RemoveGroupMemberReq req);

    ResponseVO<?> addGroupMember(String groupId, Integer appId, GroupMemberDto dto);

    ResponseVO<?> removeGroupMember(String groupId, Integer appId, String memberId);

    ResponseVO<GetRoleInGroupResp> getRoleInGroupOne(String groupId, String memberId, Integer appId);

    ResponseVO<Collection<String>> getMemberJoinedGroup(GetJoinedGroupReq req);

    ResponseVO<List<GroupMemberDto>> getGroupMember(String groupId, Integer appId);

    List<String> getGroupMemberId(String groupId, Integer appId);

    List<GroupMemberDto> getGroupManager(String groupId, Integer appId);

    ResponseVO<?> updateGroupMember(UpdateGroupMemberReq req);

    ResponseVO<?> transferGroupMember(String owner, String groupId, Integer appId);

    ResponseVO<?> speak(SpeaMemberReq req);

    ResponseVO<Collection<String>> syncMemberJoinedGroup(String operater, Integer appId);
}

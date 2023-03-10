package com.hitd.im.service.group.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hitd.im.common.R;
import com.hitd.im.common.enums.GroupErrorCode;
import com.hitd.im.common.enums.GroupMemberRoleEnum;
import com.hitd.im.common.enums.GroupStatusEnum;
import com.hitd.im.common.enums.GroupTypeEnum;
import com.hitd.im.common.exception.ApplicationException;
import com.hitd.im.service.group.dao.ImGroupEntity;
import com.hitd.im.service.group.dao.ImGroupMemberEntity;
import com.hitd.im.service.group.dao.mapper.ImGroupMemberMapper;
import com.hitd.im.service.group.model.req.*;
import com.hitd.im.service.group.model.resp.AddMemberResp;
import com.hitd.im.service.group.model.resp.GetRoleInGroupResp;
import com.hitd.im.service.group.service.ImGroupMemberService;
import com.hitd.im.service.group.service.ImGroupService;
import com.hitd.im.service.user.dao.ImUserDataEntity;
import com.hitd.im.service.user.service.ImUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @description:
 * @author: lld
 * @version: 1.0
 */
@Service
@Slf4j
public class ImGroupMemberServiceImpl implements ImGroupMemberService {

    @Autowired
    ImGroupMemberMapper imGroupMemberMapper;

    @Autowired
    ImGroupService groupService;

    @Autowired
    ImGroupMemberService groupMemberService;


    @Autowired
    ImUserService imUserService;


    @Override
    public R<List<AddMemberResp>> importGroupMember(ImportGroupMemberReq req) {

        List<AddMemberResp> resp = new ArrayList<>();

        R<ImGroupEntity> groupResp = groupService.getGroup(req.getGroupId(), req.getAppId());
        if (!groupResp.isOk()) {
            return R.errorResponse(groupResp.getCode(),groupResp.getMsg());
        }

        for (GroupMemberDto memberId :
                req.getMembers()) {
            R<?> r = null;
            try {
                r = addGroupMember(req.getGroupId(), req.getAppId(), memberId);
            } catch (Exception e) {
                e.printStackTrace();
                r = R.errorResponse();
            }
            AddMemberResp addMemberResp = new AddMemberResp();
            addMemberResp.setMemberId(memberId.getMemberId());
            if (r.isOk()) {
                addMemberResp.setResult(0);
            } else if (r.getCode() == GroupErrorCode.USER_IS_JOINED_GROUP.getCode()) {
                addMemberResp.setResult(2);
            } else {
                addMemberResp.setResult(1);
            }
            resp.add(addMemberResp);
        }

        return R.successResponse(resp);
    }


    @Override
    @Transactional
    public R<?> addGroupMember(String groupId, Integer appId, GroupMemberDto dto) {

        R<ImUserDataEntity> singleUserInfo = imUserService.getSingleUserInfo(dto.getMemberId(), appId);
        if(!singleUserInfo.isOk()){
            return singleUserInfo;
        }

        if (dto.getRole() != null && GroupMemberRoleEnum.OWNER.getCode() == dto.getRole()) {
            QueryWrapper<ImGroupMemberEntity> queryOwner = new QueryWrapper<>();
            queryOwner.eq("group_id", groupId);
            queryOwner.eq("app_id", appId);
            queryOwner.eq("role", GroupMemberRoleEnum.OWNER.getCode());
            Integer ownerNum = imGroupMemberMapper.selectCount(queryOwner);
            if (ownerNum > 0) {
                return R.errorResponse(GroupErrorCode.GROUP_IS_HAVE_OWNER);
            }
        }

        QueryWrapper<ImGroupMemberEntity> query = new QueryWrapper<>();
        query.eq("group_id", groupId);
        query.eq("app_id", appId);
        query.eq("member_id", dto.getMemberId());
        ImGroupMemberEntity memberDto = imGroupMemberMapper.selectOne(query);

        long now = System.currentTimeMillis();
        if (memberDto == null) {
            //初次加群
            memberDto = new ImGroupMemberEntity();
            BeanUtils.copyProperties(dto, memberDto);
            memberDto.setGroupId(groupId);
            memberDto.setAppId(appId);
            memberDto.setJoinTime(now);
            int insert = imGroupMemberMapper.insert(memberDto);
            if (insert == 1) {
                return R.successResponse();
            }
            return R.errorResponse(GroupErrorCode.USER_JOIN_GROUP_ERROR);
        } else if (GroupMemberRoleEnum.LEAVE.getCode() == memberDto.getRole()) {
            //重新进群
            memberDto = new ImGroupMemberEntity();
            BeanUtils.copyProperties(dto, memberDto);
            memberDto.setJoinTime(now);
            int update = imGroupMemberMapper.update(memberDto, query);
            if (update == 1) {
                return R.successResponse();
            }
            return R.errorResponse(GroupErrorCode.USER_JOIN_GROUP_ERROR);
        }

        return R.errorResponse(GroupErrorCode.USER_IS_JOINED_GROUP);

    }

    /**
     * @param
     * @return com.lld.im.common.ResponseVO
     * @description: 删除群成员，内部调用
     * @author lld
     */
    @Override
    public R<?> removeGroupMember(String groupId, Integer appId, String memberId) {

        R<ImUserDataEntity> singleUserInfo = imUserService.getSingleUserInfo(memberId, appId);
        if(!singleUserInfo.isOk()){
            return singleUserInfo;
        }

        R<GetRoleInGroupResp> roleInGroupOne = getRoleInGroupOne(groupId, memberId, appId);
        if (!roleInGroupOne.isOk()) {
            return roleInGroupOne;
        }

        GetRoleInGroupResp data = roleInGroupOne.getData();
        ImGroupMemberEntity imGroupMemberEntity = new ImGroupMemberEntity();
        imGroupMemberEntity.setRole(GroupMemberRoleEnum.LEAVE.getCode());
        imGroupMemberEntity.setLeaveTime(System.currentTimeMillis());
        imGroupMemberEntity.setGroupMemberId(data.getGroupMemberId());
        imGroupMemberMapper.updateById(imGroupMemberEntity);
        return R.successResponse();
    }


    @Override
    public R<GetRoleInGroupResp> getRoleInGroupOne(String groupId, String memberId, Integer appId) {

        GetRoleInGroupResp resp = new GetRoleInGroupResp();

        QueryWrapper<ImGroupMemberEntity> queryOwner = new QueryWrapper<>();
        queryOwner.eq("group_id", groupId);
        queryOwner.eq("app_id", appId);
        queryOwner.eq("member_id", memberId);

        ImGroupMemberEntity imGroupMemberEntity = imGroupMemberMapper.selectOne(queryOwner);
        if (imGroupMemberEntity == null || imGroupMemberEntity.getRole() == GroupMemberRoleEnum.LEAVE.getCode()) {
            return R.errorResponse(GroupErrorCode.MEMBER_IS_NOT_JOINED_GROUP);
        }

        resp.setSpeakDate(imGroupMemberEntity.getSpeakDate());
        resp.setGroupMemberId(imGroupMemberEntity.getGroupMemberId());
        resp.setMemberId(imGroupMemberEntity.getMemberId());
        resp.setRole(imGroupMemberEntity.getRole());
        return R.successResponse(resp);
    }

    @Override
    public R<Collection<String>> getMemberJoinedGroup(GetJoinedGroupReq req) {

        if (req.getLimit() != null) {
            Page<ImGroupMemberEntity> objectPage = new Page<>(req.getOffset(), req.getLimit());
            QueryWrapper<ImGroupMemberEntity> query = new QueryWrapper<>();
            query.eq("app_id", req.getAppId());
            query.eq("member_id", req.getMemberId());
            IPage<ImGroupMemberEntity> imGroupMemberEntityPage = imGroupMemberMapper.selectPage(objectPage, query);

            Set<String> groupId = new HashSet<>();
            List<ImGroupMemberEntity> records = imGroupMemberEntityPage.getRecords();
            records.forEach(e -> {
                groupId.add(e.getGroupId());
            });

            return R.successResponse(groupId);
        } else {
            return R.successResponse(imGroupMemberMapper.getJoinedGroupId(req.getAppId(), req.getMemberId()));
        }
    }


    @Override
    public R<?> addMember(AddGroupMemberReq req) {

        List<AddMemberResp> resp = new ArrayList<>();

        boolean isAdmin = false;
        R<ImGroupEntity> groupResp = groupService.getGroup(req.getGroupId(), req.getAppId());
        if (!groupResp.isOk()) {
            return groupResp;
        }

        List<GroupMemberDto> memberDtos = req.getMembers();

        ImGroupEntity group = groupResp.getData();


        /**
         * 私有群（private）	类似普通微信群，创建后仅支持已在群内的好友邀请加群，且无需被邀请方同意或群主审批
         * 公开群（Public）	类似 QQ 群，创建后群主可以指定群管理员，需要群主或管理员审批通过才能入群
         * 群类型 1私有群（类似微信） 2公开群(类似qq）
         *
         */

        if (!isAdmin && GroupTypeEnum.PUBLIC.getCode() == group.getGroupType()) {
            throw new ApplicationException(GroupErrorCode.THIS_OPERATE_NEED_APPMANAGER_ROLE);
        }

        List<String> successId = new ArrayList<>();
        for (GroupMemberDto memberId :
                memberDtos) {
            R r = null;
            try {
                r = groupMemberService.addGroupMember(req.getGroupId(), req.getAppId(), memberId);
            } catch (Exception e) {
                e.printStackTrace();
                r = R.errorResponse();
            }
            AddMemberResp addMemberResp = new AddMemberResp();
            addMemberResp.setMemberId(memberId.getMemberId());
            if (r.isOk()) {
                successId.add(memberId.getMemberId());
                addMemberResp.setResult(0);
            } else if (r.getCode() == GroupErrorCode.USER_IS_JOINED_GROUP.getCode()) {
                addMemberResp.setResult(2);
                addMemberResp.setResultMessage(r.getMsg());
            } else {
                addMemberResp.setResult(1);
                addMemberResp.setResultMessage(r.getMsg());
            }
            resp.add(addMemberResp);
        }

        return R.successResponse(resp);
    }

    @Override
    public R<?> removeMember(RemoveGroupMemberReq req) {

        List<AddMemberResp> resp = new ArrayList<>();
        boolean isAdmin = false;
        R<ImGroupEntity> groupResp = groupService.getGroup(req.getGroupId(), req.getAppId());
        if (!groupResp.isOk()) {
            return groupResp;
        }

        ImGroupEntity group = groupResp.getData();

        if (!isAdmin) {
            if (GroupTypeEnum.PUBLIC.getCode() == group.getGroupType()) {

                //获取操作人的权限 是管理员or群主or群成员
                R<GetRoleInGroupResp> role = getRoleInGroupOne(req.getGroupId(), req.getOperator(), req.getAppId());
                if (!role.isOk()) {
                    return role;
                }

                GetRoleInGroupResp data = role.getData();
                Integer roleInfo = data.getRole();

                boolean isOwner = roleInfo == GroupMemberRoleEnum.OWNER.getCode();
                boolean isManager = roleInfo == GroupMemberRoleEnum.MAMAGER.getCode();

                if (!isOwner && !isManager) {
                    throw new ApplicationException(GroupErrorCode.THIS_OPERATE_NEED_MANAGER_ROLE);
                }

                //私有群必须是群主才能踢人
                if (!isOwner && GroupTypeEnum.PRIVATE.getCode() == group.getGroupType()) {
                    throw new ApplicationException(GroupErrorCode.THIS_OPERATE_NEED_OWNER_ROLE);
                }

                //公开群管理员和群主可踢人，但管理员只能踢普通群成员
                if (GroupTypeEnum.PUBLIC.getCode() == group.getGroupType()) {
//                    throw new ApplicationException(GroupErrorCode.THIS_OPERATE_NEED_MANAGER_ROLE);
                    //获取被踢人的权限
                    R<GetRoleInGroupResp> roleInGroupOne = this.getRoleInGroupOne(req.getGroupId(), req.getMemberId(), req.getAppId());
                    if (!roleInGroupOne.isOk()) {
                        return roleInGroupOne;
                    }
                    GetRoleInGroupResp memberRole = roleInGroupOne.getData();
                    if (memberRole.getRole() == GroupMemberRoleEnum.OWNER.getCode()) {
                        throw new ApplicationException(GroupErrorCode.GROUP_OWNER_IS_NOT_REMOVE);
                    }
                    //是管理员并且被踢人不是群成员，无法操作
                    if (isManager && memberRole.getRole() != GroupMemberRoleEnum.ORDINARY.getCode()) {
                        throw new ApplicationException(GroupErrorCode.THIS_OPERATE_NEED_OWNER_ROLE);
                    }
                }
            }
        }
        R<?> r = groupMemberService.removeGroupMember(req.getGroupId(), req.getAppId(), req.getMemberId());
        if(r.isOk()){


        }

        return r;
    }

    @Override
    public R<List<GroupMemberDto>> getGroupMember(String groupId, Integer appId) {
        List<GroupMemberDto> groupMember = imGroupMemberMapper.getGroupMember(appId, groupId);
        return R.successResponse(groupMember);
    }

    @Override
    public List<String> getGroupMemberId(String groupId, Integer appId) {
        return imGroupMemberMapper.getGroupMemberId(appId, groupId);
    }

    @Override
    public List<GroupMemberDto> getGroupManager(String groupId, Integer appId) {
        return imGroupMemberMapper.getGroupManager(groupId, appId);
    }

    @Override
    public R<?> updateGroupMember(UpdateGroupMemberReq req) {

        boolean isAdmin = false;

        R<ImGroupEntity> group = groupService.getGroup(req.getGroupId(), req.getAppId());
        if (!group.isOk()) {
            return group;
        }

        ImGroupEntity groupData = group.getData();
        if (groupData.getStatus() == GroupStatusEnum.DESTROY.getCode()) {
            throw new ApplicationException(GroupErrorCode.GROUP_IS_DESTROY);
        }

        //是否是自己修改自己的资料
        boolean isMeOperate = req.getOperator().equals(req.getMemberId());

        if (!isAdmin) {
            //昵称只能自己修改 权限只能群主或管理员修改
            if (StringUtils.isBlank(req.getAlias()) && !isMeOperate) {
                return R.errorResponse(GroupErrorCode.THIS_OPERATE_NEED_ONESELF);
            }
            //私有群不能设置管理员
            if (groupData.getGroupType() == GroupTypeEnum.PRIVATE.getCode() &&
                    req.getRole() != null && (req.getRole() == GroupMemberRoleEnum.MAMAGER.getCode() ||
                    req.getRole() == GroupMemberRoleEnum.OWNER.getCode())) {
                return R.errorResponse(GroupErrorCode.THIS_OPERATE_NEED_MANAGER_ROLE);
            }

            //如果要修改权限相关的则走下面的逻辑
            if(req.getRole() != null){
                //获取被操作人的是否在群内
                R<GetRoleInGroupResp> roleInGroupOne = this.getRoleInGroupOne(req.getGroupId(), req.getMemberId(), req.getAppId());
                if(!roleInGroupOne.isOk()){
                    return roleInGroupOne;
                }

                //获取操作人权限
                R<GetRoleInGroupResp> operateRoleInGroupOne = this.getRoleInGroupOne(req.getGroupId(), req.getOperator(), req.getAppId());
                if(!operateRoleInGroupOne.isOk()){
                    return operateRoleInGroupOne;
                }

                GetRoleInGroupResp data = operateRoleInGroupOne.getData();
                Integer roleInfo = data.getRole();
                boolean isOwner = roleInfo == GroupMemberRoleEnum.OWNER.getCode();
                boolean isManager = roleInfo == GroupMemberRoleEnum.MAMAGER.getCode();

                //不是管理员不能修改权限
                if(req.getRole() != null && !isOwner && !isManager){
                    return R.errorResponse(GroupErrorCode.THIS_OPERATE_NEED_MANAGER_ROLE);
                }

                //管理员只有群主能够设置
                if(req.getRole() != null && req.getRole() == GroupMemberRoleEnum.MAMAGER.getCode() && !isOwner){
                    return R.errorResponse(GroupErrorCode.THIS_OPERATE_NEED_OWNER_ROLE);
                }

            }
        }

        ImGroupMemberEntity update = new ImGroupMemberEntity();

        if (StringUtils.isNotBlank(req.getAlias())) {
            update.setAlias(req.getAlias());
        }

        //不能直接修改为群主
        if(req.getRole() != null && req.getRole() != GroupMemberRoleEnum.OWNER.getCode()){
            update.setRole(req.getRole());
        }

        UpdateWrapper<ImGroupMemberEntity> objectUpdateWrapper = new UpdateWrapper<>();
        objectUpdateWrapper.eq("app_id", req.getAppId());
        objectUpdateWrapper.eq("member_id", req.getMemberId());
        objectUpdateWrapper.eq("group_id", req.getGroupId());
        imGroupMemberMapper.update(update, objectUpdateWrapper);

        return R.successResponse();
    }

    @Override
    @Transactional
    public R<?> transferGroupMember(String owner, String groupId, Integer appId) {

        //更新旧群主
        ImGroupMemberEntity imGroupMemberEntity = new ImGroupMemberEntity();
        imGroupMemberEntity.setRole(GroupMemberRoleEnum.ORDINARY.getCode());
        UpdateWrapper<ImGroupMemberEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("app_id", appId);
        updateWrapper.eq("group_id", groupId);
        updateWrapper.eq("role", GroupMemberRoleEnum.OWNER.getCode());
        imGroupMemberMapper.update(imGroupMemberEntity, updateWrapper);

        //更新新群主
        ImGroupMemberEntity newOwner = new ImGroupMemberEntity();
        newOwner.setRole(GroupMemberRoleEnum.OWNER.getCode());
        UpdateWrapper<ImGroupMemberEntity> ownerWrapper = new UpdateWrapper<>();
        ownerWrapper.eq("app_id", appId);
        ownerWrapper.eq("group_id", groupId);
        ownerWrapper.eq("member_id", owner);
        imGroupMemberMapper.update(newOwner, ownerWrapper);

        return R.successResponse();
    }

    @Override
    public R<?> speak(SpeaMemberReq req) {

        R<ImGroupEntity> groupResp = groupService.getGroup(req.getGroupId(), req.getAppId());
        if (!groupResp.isOk()) {
            return groupResp;
        }

        boolean isAdmin = false;
        boolean isOwner = false;
        boolean isManager = false;
        GetRoleInGroupResp memberRole = null;

        if (!isAdmin) {

            //获取操作人的权限 是管理员or群主or群成员
            R<GetRoleInGroupResp> role = getRoleInGroupOne(req.getGroupId(), req.getOperator(), req.getAppId());
            if (!role.isOk()) {
                return role;
            }

            GetRoleInGroupResp data = role.getData();
            Integer roleInfo = data.getRole();

            isOwner = roleInfo == GroupMemberRoleEnum.OWNER.getCode();
            isManager = roleInfo == GroupMemberRoleEnum.MAMAGER.getCode();

            if (!isOwner && !isManager) {
                throw new ApplicationException(GroupErrorCode.THIS_OPERATE_NEED_MANAGER_ROLE);
            }

            //获取被操作的权限
            R<GetRoleInGroupResp> roleInGroupOne = this.getRoleInGroupOne(req.getGroupId(), req.getMemberId(), req.getAppId());
            if (!roleInGroupOne.isOk()) {
                return roleInGroupOne;
            }
            memberRole = roleInGroupOne.getData();
            //被操作人是群主只能app管理员操作
            if (memberRole.getRole() == GroupMemberRoleEnum.OWNER.getCode()) {
                throw new ApplicationException(GroupErrorCode.THIS_OPERATE_NEED_APPMANAGER_ROLE);
            }

            //是管理员并且被操作人不是群成员，无法操作
            if (isManager && memberRole.getRole() != GroupMemberRoleEnum.ORDINARY.getCode()) {
                throw new ApplicationException(GroupErrorCode.THIS_OPERATE_NEED_OWNER_ROLE);
            }
        }

        ImGroupMemberEntity imGroupMemberEntity = new ImGroupMemberEntity();
        if(memberRole == null){
            //获取被操作的权限
            R<GetRoleInGroupResp> roleInGroupOne = this.getRoleInGroupOne(req.getGroupId(), req.getMemberId(), req.getAppId());
            if (!roleInGroupOne.isOk()) {
                return roleInGroupOne;
            }
            memberRole = roleInGroupOne.getData();
        }

        imGroupMemberEntity.setGroupMemberId(memberRole.getGroupMemberId());
        if(req.getSpeakDate() > 0){
            imGroupMemberEntity.setSpeakDate(System.currentTimeMillis() + req.getSpeakDate());
        }else{
            imGroupMemberEntity.setSpeakDate(req.getSpeakDate());
        }

        int i = imGroupMemberMapper.updateById(imGroupMemberEntity);
        if(i == 1){
        }
        return R.successResponse();
    }

    @Override
    public R<Collection<String>> syncMemberJoinedGroup(String operater, Integer appId) {
        return R.successResponse(imGroupMemberMapper.syncJoinedGroupId(appId,operater,GroupMemberRoleEnum.LEAVE.getCode()));
    }


}

package com.hitd.im.service.group.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hitd.im.common.R;
import com.hitd.im.common.enums.GroupErrorCode;
import com.hitd.im.common.enums.GroupMemberRoleEnum;
import com.hitd.im.common.enums.GroupStatusEnum;
import com.hitd.im.common.enums.GroupTypeEnum;
import com.hitd.im.common.exception.ApplicationException;
import com.hitd.im.service.group.dao.ImGroupEntity;
import com.hitd.im.service.group.dao.mapper.ImGroupMapper;
import com.hitd.im.service.group.model.req.*;
import com.hitd.im.service.group.model.resp.GetGroupResp;
import com.hitd.im.service.group.model.resp.GetJoinedGroupResp;
import com.hitd.im.service.group.model.resp.GetRoleInGroupResp;
import com.hitd.im.service.group.service.ImGroupMemberService;
import com.hitd.im.service.group.service.ImGroupService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * @description:
 * @author: lld
 * @version: 1.0
 */
@Service
public class ImGroupServiceImpl implements ImGroupService {

    @Resource
    private ImGroupMapper imGroupDataMapper;

    @Autowired
    private ImGroupMemberService groupMemberService;

    @Override
    public R<?> importGroup(ImportGroupReq req) {

        checkGroupIdIsExist(req);

        ImGroupEntity imGroupEntity = new ImGroupEntity();

        if (req.getGroupType() == GroupTypeEnum.PUBLIC.getCode() && StringUtils.isBlank(req.getOwnerId())) {
            throw new ApplicationException(GroupErrorCode.PUBLIC_GROUP_MUST_HAVE_OWNER);
        }

        if (req.getCreateTime() == null) {
            imGroupEntity.setCreateTime(System.currentTimeMillis());
        }
        imGroupEntity.setStatus(GroupStatusEnum.NORMAL.getCode());
        BeanUtils.copyProperties(req, imGroupEntity);
        int insert = imGroupDataMapper.insert(imGroupEntity);

        if (insert != 1) {
            throw new ApplicationException(GroupErrorCode.IMPORT_GROUP_ERROR);
        }

        return R.successResponse();
    }

    private void checkGroupIdIsExist(GroupBaseReq req) {
        if (StringUtils.isEmpty(req.getGroupId())) {
            req.setGroupId(UUID.randomUUID().toString().replace("-", ""));
        } else {
            //1.?????????id????????????
            QueryWrapper<ImGroupEntity> query = new QueryWrapper<>();
            query.eq("group_id", req.getGroupId());
            query.eq("app_id", req.getAppId());
            Integer integer = imGroupDataMapper.selectCount(query);
            if (integer > 0) {
                throw new ApplicationException(GroupErrorCode.GROUP_IS_EXIST);
            }
        }
    }

    @Override
    @Transactional
    public R<?> createGroup(CreateGroupReq req) {

        boolean isAdmin = false;

        if (!isAdmin) {
            req.setOwnerId(req.getOperator());
        }

        checkGroupIdIsExist(req);

        if (req.getGroupType() == GroupTypeEnum.PUBLIC.getCode() && StringUtils.isBlank(req.getOwnerId())) {
            throw new ApplicationException(GroupErrorCode.PUBLIC_GROUP_MUST_HAVE_OWNER);
        }

        ImGroupEntity imGroupEntity = new ImGroupEntity();
        imGroupEntity.setCreateTime(System.currentTimeMillis());
        imGroupEntity.setStatus(GroupStatusEnum.NORMAL.getCode());
        BeanUtils.copyProperties(req, imGroupEntity);
        int insert = imGroupDataMapper.insert(imGroupEntity);

        GroupMemberDto groupMemberDto = new GroupMemberDto();
        groupMemberDto.setMemberId(req.getOwnerId());
        groupMemberDto.setRole(GroupMemberRoleEnum.OWNER.getCode());
        groupMemberDto.setJoinTime(System.currentTimeMillis());
        groupMemberService.addGroupMember(req.getGroupId(), req.getAppId(), groupMemberDto);

        //???????????????
        for (GroupMemberDto dto : req.getMember()) {
            groupMemberService.addGroupMember(req.getGroupId(), req.getAppId(), dto);
        }
        return R.successResponse();
    }


    @Override
    @Transactional
    public R<?> updateBaseGroupInfo(UpdateGroupReq req) {

        //1.?????????id????????????
        QueryWrapper<ImGroupEntity> query = new QueryWrapper<>();
        query.eq("group_id", req.getGroupId());
        query.eq("app_id", req.getAppId());
        ImGroupEntity imGroupEntity = imGroupDataMapper.selectOne(query);
        if (imGroupEntity == null) {
            throw new ApplicationException(GroupErrorCode.GROUP_IS_EXIST);
        }

        if(imGroupEntity.getStatus() == GroupStatusEnum.DESTROY.getCode()){
            throw new ApplicationException(GroupErrorCode.GROUP_IS_DESTROY);
        }

        boolean isAdmin = false;

        if (!isAdmin) {
            //????????????????????????????????????
            R<GetRoleInGroupResp> role = groupMemberService.getRoleInGroupOne(req.getGroupId(), req.getOperator(), req.getAppId());

            if (!role.isOk()) {
                return role;
            }

            GetRoleInGroupResp data = role.getData();
            Integer roleInfo = data.getRole();

            boolean isManager = roleInfo == GroupMemberRoleEnum.MAMAGER.getCode()
                    || roleInfo == GroupMemberRoleEnum.OWNER.getCode();

            //?????????????????????????????????
            if (!isManager && GroupTypeEnum.PUBLIC.getCode() == imGroupEntity.getGroupType()) {
                throw new ApplicationException(GroupErrorCode.THIS_OPERATE_NEED_MANAGER_ROLE);
            }

        }

        ImGroupEntity update = new ImGroupEntity();
        BeanUtils.copyProperties(req, update);
        update.setUpdateTime(System.currentTimeMillis());
        int row = imGroupDataMapper.update(update, query);
        if (row != 1) {
            throw new ApplicationException(GroupErrorCode.THIS_OPERATE_NEED_MANAGER_ROLE);
        }
        return R.successResponse();
    }


    @Override
    public R<?> getJoinedGroup(GetJoinedGroupReq req) {

        R<Collection<String>> memberJoinedGroup = groupMemberService.getMemberJoinedGroup(req);
        if (memberJoinedGroup.isOk()) {

            GetJoinedGroupResp resp = new GetJoinedGroupResp();

            if (CollectionUtils.isEmpty(memberJoinedGroup.getData())) {
                resp.setTotalCount(0);
                resp.setGroupList(new ArrayList<>());
                return R.successResponse(resp);
            }

            QueryWrapper<ImGroupEntity> query = new QueryWrapper<>();
            query.eq("app_id", req.getAppId());
            query.in("group_id", memberJoinedGroup.getData());

            if (CollectionUtils.isNotEmpty(req.getGroupType())) {
                query.in("group_type", req.getGroupType());
            }

            List<ImGroupEntity> groupList = imGroupDataMapper.selectList(query);
            resp.setGroupList(groupList);
            if (req.getLimit() == null) {
                resp.setTotalCount(groupList.size());
            } else {
                resp.setTotalCount(imGroupDataMapper.selectCount(query));
            }
            return R.successResponse(resp);
        } else {
            return memberJoinedGroup;
        }
    }



    @Override
    @Transactional
    public R<?> destroyGroup(DestroyGroupReq req) {

        boolean isAdmin = false;

        QueryWrapper<ImGroupEntity> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("group_id", req.getGroupId());
        objectQueryWrapper.eq("app_id", req.getAppId());
        ImGroupEntity imGroupEntity = imGroupDataMapper.selectOne(objectQueryWrapper);
        if (imGroupEntity == null) {
            throw new ApplicationException(GroupErrorCode.PRIVATE_GROUP_CAN_NOT_DESTORY);
        }

        if(imGroupEntity.getStatus() == GroupStatusEnum.DESTROY.getCode()){
            throw new ApplicationException(GroupErrorCode.GROUP_IS_DESTROY);
        }

        if (!isAdmin) {
            if (imGroupEntity.getGroupType() == GroupTypeEnum.PUBLIC.getCode()) {
                throw new ApplicationException(GroupErrorCode.THIS_OPERATE_NEED_OWNER_ROLE);
            }

            if (imGroupEntity.getGroupType() == GroupTypeEnum.PUBLIC.getCode() &&
                    !imGroupEntity.getOwnerId().equals(req.getOperator())) {
                throw new ApplicationException(GroupErrorCode.THIS_OPERATE_NEED_OWNER_ROLE);
            }
        }

        ImGroupEntity update = new ImGroupEntity();

        update.setStatus(GroupStatusEnum.DESTROY.getCode());
        int update1 = imGroupDataMapper.update(update, objectQueryWrapper);
        if (update1 != 1) {
            throw new ApplicationException(GroupErrorCode.UPDATE_GROUP_BASE_INFO_ERROR);
        }
        return R.successResponse();
    }

    @Override
    @Transactional
    public R<?> transferGroup(TransferGroupReq req) {

        R<GetRoleInGroupResp> roleInGroupOne = groupMemberService.getRoleInGroupOne(req.getGroupId(), req.getOperator(), req.getAppId());
        if (!roleInGroupOne.isOk()) {
            return roleInGroupOne;
        }

        if (roleInGroupOne.getData().getRole() != GroupMemberRoleEnum.OWNER.getCode()) {
            return R.errorResponse(GroupErrorCode.THIS_OPERATE_NEED_OWNER_ROLE);
        }

        R<GetRoleInGroupResp> newOwnerRole = groupMemberService.getRoleInGroupOne(req.getGroupId(), req.getOwnerId(), req.getAppId());
        if (!newOwnerRole.isOk()) {
            return newOwnerRole;
        }

        QueryWrapper<ImGroupEntity> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("group_id", req.getGroupId());
        objectQueryWrapper.eq("app_id", req.getAppId());
        ImGroupEntity imGroupEntity = imGroupDataMapper.selectOne(objectQueryWrapper);
        if(imGroupEntity.getStatus() == GroupStatusEnum.DESTROY.getCode()){
            throw new ApplicationException(GroupErrorCode.GROUP_IS_DESTROY);
        }

        ImGroupEntity updateGroup = new ImGroupEntity();
        updateGroup.setOwnerId(req.getOwnerId());
        UpdateWrapper<ImGroupEntity> updateGroupWrapper = new UpdateWrapper<>();
        updateGroupWrapper.eq("app_id", req.getAppId());
        updateGroupWrapper.eq("group_id", req.getGroupId());
        imGroupDataMapper.update(updateGroup, updateGroupWrapper);
        groupMemberService.transferGroupMember(req.getOwnerId(), req.getGroupId(), req.getAppId());

        return R.successResponse();
    }

    @Override
    public R<ImGroupEntity> getGroup(String groupId, Integer appId) {

        QueryWrapper<ImGroupEntity> query = new QueryWrapper<>();
        query.eq("app_id", appId);
        query.eq("group_id", groupId);
        ImGroupEntity imGroupEntity = imGroupDataMapper.selectOne(query);

        if (imGroupEntity == null) {
            return R.errorResponse(GroupErrorCode.GROUP_IS_NOT_EXIST);
        }
        return R.successResponse(imGroupEntity);
    }

    @Override
    public R<?> getGroup(GetGroupReq req) {

        R group = this.getGroup(req.getGroupId(), req.getAppId());

        if(!group.isOk()){
            return group;
        }

        GetGroupResp getGroupResp = new GetGroupResp();
        BeanUtils.copyProperties(group.getData(), getGroupResp);
        try {
            R<List<GroupMemberDto>> groupMember = groupMemberService.getGroupMember(req.getGroupId(), req.getAppId());
            if (groupMember.isOk()) {
                getGroupResp.setMemberList(groupMember.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.successResponse(getGroupResp);
    }

    @Override
    public R<?> muteGroup(MuteGroupReq req) {

        R<ImGroupEntity> groupResp = getGroup(req.getGroupId(), req.getAppId());
        if (!groupResp.isOk()) {
            return groupResp;
        }

        if(groupResp.getData().getStatus() == GroupStatusEnum.DESTROY.getCode()){
            throw new ApplicationException(GroupErrorCode.GROUP_IS_DESTROY);
        }

        boolean isAdmin = false;

        if (!isAdmin) {
            //????????????????????????????????????
            R<GetRoleInGroupResp> role = groupMemberService.getRoleInGroupOne(req.getGroupId(), req.getOperator(), req.getAppId());

            if (!role.isOk()) {
                return role;
            }

            GetRoleInGroupResp data = role.getData();
            Integer roleInfo = data.getRole();

            boolean isManager = roleInfo == GroupMemberRoleEnum.MAMAGER.getCode() || roleInfo == GroupMemberRoleEnum.OWNER.getCode();

            //?????????????????????????????????
            if (!isManager) {
                throw new ApplicationException(GroupErrorCode.THIS_OPERATE_NEED_MANAGER_ROLE);
            }
        }

        ImGroupEntity update = new ImGroupEntity();
        update.setMute(req.getMute());

        UpdateWrapper<ImGroupEntity> wrapper = new UpdateWrapper<>();
        wrapper.eq("group_id",req.getGroupId());
        wrapper.eq("app_id",req.getAppId());
        imGroupDataMapper.update(update,wrapper);

        return R.successResponse();
    }

//    @Override
//    public  ResponseVO<?> syncJoinedGroupList(SyncReq req) {
//        if(req.getMaxLimit() > 100){
//            req.setMaxLimit(100);
//        }
//
//        SyncResp<ImGroupEntity> resp = new SyncResp<>();
//
//        ResponseVO<Collection<String>> memberJoinedGroup = groupMemberService.syncMemberJoinedGroup(req.getOperater(), req.getAppId());
//        if(memberJoinedGroup.isOk()){
//
//            Collection<String> data = memberJoinedGroup.getData();
//            QueryWrapper<ImGroupEntity> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("app_id",req.getAppId());
//            queryWrapper.in("group_id",data);
//            queryWrapper.gt("sequence",req.getLastSequence());
//            queryWrapper.last(" limit " + req.getMaxLimit());
//            queryWrapper.orderByAsc("sequence");
//
//            List<ImGroupEntity> list = imGroupDataMapper.selectList(queryWrapper);
//
//            if(!CollectionUtils.isEmpty(list)){
//                ImGroupEntity maxSeqEntity
//                        = list.get(list.size() - 1);
//                resp.setDataList(list);
//                //????????????seq
//                Long maxSeq =
//                        imGroupDataMapper.getGroupMaxSeq(data, req.getAppId());
//                resp.setMaxSequence(maxSeq);
//                //????????????????????????
//                resp.setCompleted(maxSeqEntity.getSequence() >= maxSeq);
//                return ResponseVO.successResponse(resp);
//            }
//
//        }
//        resp.setCompleted(true);
//        return ResponseVO.successResponse(resp);
//    }

    @Override
    public Long getUserGroupMaxSeq(String userId, Integer appId) {

        R<Collection<String>> memberJoinedGroup = groupMemberService.syncMemberJoinedGroup(userId, appId);
        if(!memberJoinedGroup.isOk()){
            throw new ApplicationException(500,"");
        }
        Long maxSeq =
                imGroupDataMapper.getGroupMaxSeq(memberJoinedGroup.getData(),
                        appId);
        return maxSeq;
    }

}

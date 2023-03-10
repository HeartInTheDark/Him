package com.hitd.im.service.friendship.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hitd.im.common.R;
import com.hitd.im.common.enums.AllowFriendTypeEnum;
import com.hitd.im.common.enums.CheckFriendShipTypeEnum;
import com.hitd.im.common.enums.FriendShipErrorCode;
import com.hitd.im.common.enums.FriendShipStatusEnum;
import com.hitd.im.common.exception.ApplicationException;
import com.hitd.im.common.model.RequestBase;
import com.hitd.im.service.friendship.dao.ImFriendShipEntity;
import com.hitd.im.service.friendship.dao.mapper.ImFriendShipMapper;
import com.hitd.im.service.friendship.model.req.*;
import com.hitd.im.service.friendship.model.resp.CheckFriendShipResp;
import com.hitd.im.service.friendship.model.resp.ImportFriendShipResp;
import com.hitd.im.service.friendship.service.ImFriendService;
import com.hitd.im.service.friendship.service.ImFriendShipRequestService;
import com.hitd.im.service.user.dao.ImUserDataEntity;
import com.hitd.im.service.user.service.ImUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author ZhangWeinan
 * @Date 2023/3/8 12:06
 * @DES
 * @Since Copyright(c)
 */
@Service
public class ImFriendServiceImpl implements ImFriendService {

    @Resource
    private ImFriendShipMapper imFriendShipMapper;
    @Resource
    private ImUserService imUserService;

    @Resource
    private ImFriendShipRequestService imFriendShipRequestService;

    @Override
    public R<ImportFriendShipResp> importFriendShip(ImportFriendShipReq req) {
        if (req.getFriendItem().size() > 100) {
            return R.errorResponse(FriendShipErrorCode.IMPORT_SIZE_BEYOND);
        }
        ImportFriendShipResp resp = new ImportFriendShipResp();
        List<String> successId = new ArrayList<>();
        List<String> errorId = new ArrayList<>();

        for (ImportFriendShipReq.ImportFriendDto dto :
                req.getFriendItem()) {
            ImFriendShipEntity entity = new ImFriendShipEntity();
            BeanUtils.copyProperties(dto, entity);
            entity.setAppId(req.getAppId());
            entity.setFromId(req.getFromId());
            entity.setStatus(FriendShipStatusEnum.FRIEND_STATUS_NORMAL.getCode());
            try {
                int insert = imFriendShipMapper.insert(entity);
                if (insert == 1) {
                    successId.add(dto.getToId());
                } else {
                    errorId.add(dto.getToId());
                }
            } catch (Exception e) {
                e.printStackTrace();
                errorId.add(dto.getToId());
            }
        }
        resp.setErrorId(errorId);
        resp.setSuccessId(successId);

        return R.successResponse(resp);
    }

    @Override
    public R<?> addFriend(AddFriendReq req) {

        ImUserDataEntity data = checkAndReturn(req.getFromId(),req.getToItem().getToId(),req.getAppId()).getData();

        if(data.getFriendAllowType() != null && data.getFriendAllowType() == AllowFriendTypeEnum.NOT_NEED.getCode()){
            return doAddFriend(req,req.getFromId(), req.getToItem(), req.getAppId());
        }else{
            QueryWrapper<ImFriendShipEntity> query = new QueryWrapper<>();
            query.eq("app_id",req.getAppId());
            query.eq("from_id",req.getFromId());
            query.eq("to_id",req.getToItem().getToId());
            ImFriendShipEntity fromItem = imFriendShipMapper.selectOne(query);
            if(fromItem == null || fromItem.getStatus()
                    != FriendShipStatusEnum.FRIEND_STATUS_NORMAL.getCode()){
                //?????????????????????????????????
                R<?> r = imFriendShipRequestService.addFriendshipRequest(req.getFromId(), req.getToItem(), req.getAppId());
                if(!r.isOk()){
                    return r;
                }
            }else{
                return R.errorResponse(FriendShipErrorCode.TO_IS_YOUR_FRIEND);
            }
            return R.successResponse();
        }
    }

    private R<ImUserDataEntity> checkAndReturn(String fromId, String toId, Integer appId){
        R<ImUserDataEntity> fromInfo = imUserService.getSingleUserInfo(fromId,appId);
        if (!fromInfo.isOk()) {
            throw new ApplicationException(fromInfo.getCode(),fromInfo.getMsg());
        }

        R<ImUserDataEntity> toInfo = imUserService.getSingleUserInfo(toId, appId);
        if (!toInfo.isOk()) {
            throw new ApplicationException(toInfo.getCode(),toInfo.getMsg());
        }
        return toInfo;
    }

    @Override
    public R<?> updateFriend(UpdateFriendReq req) {
        checkAndReturn(req.getFromId(),req.getToItem().getToId(),req.getAppId());
        return doUpdate(req.getFromId(), req.getToItem(), req.getAppId());
    }

    @Override
    public R<?> deleteFriend(DeleteFriendReq req) {
        QueryWrapper<ImFriendShipEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("app_id", req.getAppId())
                .eq("from_id", req.getFromId())
                .eq("to_id", req.getToId());
        ImFriendShipEntity fromItem = imFriendShipMapper.selectOne(queryWrapper);
        if (fromItem == null) {
            // ??????????????????
            return R.errorResponse(FriendShipErrorCode.TO_IS_NOT_YOUR_FRIEND);
        } else {
            if (fromItem.getStatus() == FriendShipStatusEnum.FRIEND_STATUS_NORMAL.getCode()) {
                //??????
                ImFriendShipEntity update = new ImFriendShipEntity();
                update.setStatus(FriendShipStatusEnum.FRIEND_STATUS_DELETE.getCode());
                imFriendShipMapper.update(update, queryWrapper);
            } else {
                //??????????????????
                return R.errorResponse(FriendShipErrorCode.FRIEND_IS_DELETED);
            }
        }
        return R.successResponse();
    }

    @Override
    public R<?> deleteAllFriend(DeleteFriendReq req) {

        QueryWrapper<ImFriendShipEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("app_id", req.getAppId())
                .eq("from_id", req.getFromId())
                .eq("status", FriendShipStatusEnum.FRIEND_STATUS_NORMAL.getCode());

        ImFriendShipEntity update = new ImFriendShipEntity();
        update.setStatus(FriendShipStatusEnum.FRIEND_STATUS_DELETE.getCode());
        imFriendShipMapper.update(update, queryWrapper);
        return R.successResponse();
    }

    @Override
    public R<List<ImFriendShipEntity>> getAllFriendShip(GetAllFriendShipReq req) {
        QueryWrapper<ImFriendShipEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("app_id", req.getAppId())
                .eq("from_id", req.getFromId())
                .eq("status", FriendShipStatusEnum.FRIEND_STATUS_NORMAL.getCode());
        return R.successResponse(imFriendShipMapper.selectList(queryWrapper));
    }

    @Override
    public R<ImFriendShipEntity> getRelation(GetRelationReq req) {
        QueryWrapper<ImFriendShipEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("app_id", req.getAppId())
                .eq("from_id", req.getFromId())
                .eq("to_id", req.getToId())
                .eq("status", FriendShipStatusEnum.FRIEND_STATUS_NORMAL.getCode());
        ImFriendShipEntity entity = imFriendShipMapper.selectOne(queryWrapper);
        if (entity == null) {
            //?????????????????????
            return R.errorResponse(FriendShipErrorCode.REPEATSHIP_IS_NOT_EXIST);
        }
        return R.successResponse(entity);
    }

    @Override
    public R<List<CheckFriendShipResp>> checkFriendship(CheckFriendShipReq req) {
        Map<String, Integer> result
                = req.getToIds().stream()
                .collect(Collectors.toMap(Function.identity(), s -> 0));

        List<CheckFriendShipResp> resp;

        if (req.getCheckType() == CheckFriendShipTypeEnum.SINGLE.getType()) {
            resp = imFriendShipMapper.checkFriendShip(req);
        } else {
            resp = imFriendShipMapper.checkFriendShipBoth(req);
        }

        Map<String, Integer> collect = resp.stream()
                .collect(Collectors.toMap(CheckFriendShipResp::getToId
                        , CheckFriendShipResp::getStatus));
        result.keySet().forEach(e -> {
            if (!collect.containsKey(e)) {
                CheckFriendShipResp checkFriendShipResp = new CheckFriendShipResp();
                checkFriendShipResp.setFromId(req.getFromId());
                checkFriendShipResp.setToId(e);
                checkFriendShipResp.setStatus(result.get(e));
                resp.add(checkFriendShipResp);
            }
        });
        return R.successResponse(resp);
    }

    @Override
    public R<?> addBlack(AddFriendShipBlackReq req) {

        R<ImUserDataEntity> fromInfo = imUserService.getSingleUserInfo(req.getFromId(), req.getAppId());
        if(!fromInfo.isOk()){
            return fromInfo;
        }

        R<ImUserDataEntity> toInfo = imUserService.getSingleUserInfo(req.getToId(), req.getAppId());
        if(!toInfo.isOk()){
            return toInfo;
        }
        QueryWrapper<ImFriendShipEntity> query = new QueryWrapper<>();
        query.eq("app_id",req.getAppId());
        query.eq("from_id",req.getFromId());
        query.eq("to_id",req.getToId());

        ImFriendShipEntity fromItem = imFriendShipMapper.selectOne(query);
        Long seq = 0L;
        if(fromItem == null){
            fromItem = new ImFriendShipEntity();
            fromItem.setFromId(req.getFromId());
            fromItem.setToId(req.getToId());
            fromItem.setFriendSequence(seq);
            fromItem.setAppId(req.getAppId());
            fromItem.setBlack(FriendShipStatusEnum.BLACK_STATUS_BLACKED.getCode());
            fromItem.setCreateTime(System.currentTimeMillis());
            int insert = imFriendShipMapper.insert(fromItem);
            if(insert != 1){
                return R.errorResponse(FriendShipErrorCode.ADD_FRIEND_ERROR);
            }
        } else{
            //?????????????????????????????????????????????????????????????????????????????????????????????????????????
            if(fromItem.getBlack() != null && fromItem.getBlack() == FriendShipStatusEnum.BLACK_STATUS_BLACKED.getCode()){
                return R.errorResponse(FriendShipErrorCode.FRIEND_IS_BLACK);
            }

            else {
                ImFriendShipEntity update = new ImFriendShipEntity();
                update.setFriendSequence(seq);
                update.setBlack(FriendShipStatusEnum.BLACK_STATUS_BLACKED.getCode());
                int result = imFriendShipMapper.update(update, query);
                if(result != 1){
                    return R.errorResponse(FriendShipErrorCode.ADD_BLACK_ERROR);
                }
            }
        }
        return R.successResponse();
    }

    @Override
    public R<?> deleteBlack(DeleteBlackReq req) {
        QueryWrapper<ImFriendShipEntity> queryFrom = new QueryWrapper<>();
        queryFrom.eq("from_id", req.getFromId())
                .eq("app_id", req.getAppId())
                .eq("to_id", req.getToId());
        ImFriendShipEntity fromItem = imFriendShipMapper.selectOne(queryFrom);
        if (fromItem.getBlack() != null && fromItem.getBlack() == FriendShipStatusEnum.BLACK_STATUS_NORMAL.getCode()) {
            throw new ApplicationException(FriendShipErrorCode.FRIEND_IS_NOT_YOUR_BLACK);
        }

        ImFriendShipEntity update = new ImFriendShipEntity();
        update.setBlack(FriendShipStatusEnum.BLACK_STATUS_NORMAL.getCode());
        int update1 = imFriendShipMapper.update(update, queryFrom);
        return R.successResponse();
    }

    @Override
    public R<?> checkBlack(CheckFriendShipReq req) {
        Map<String, Integer> toIdMap
                = req.getToIds().stream().collect(Collectors
                .toMap(Function.identity(), s -> 0));
        List<CheckFriendShipResp> result;
        if (req.getCheckType() == CheckFriendShipTypeEnum.SINGLE.getType()) {
            result = imFriendShipMapper.checkFriendShipBlack(req);
        } else {
            result = imFriendShipMapper.checkFriendShipBlackBoth(req);
        }

        Map<String, Integer> collect = result.stream()
                .collect(Collectors
                        .toMap(CheckFriendShipResp::getToId,
                                CheckFriendShipResp::getStatus));
        for (String toId:
                toIdMap.keySet()) {
            if(!collect.containsKey(toId)){
                CheckFriendShipResp checkFriendShipResp = new CheckFriendShipResp();
                checkFriendShipResp.setToId(toId);
                checkFriendShipResp.setFromId(req.getFromId());
                checkFriendShipResp.setStatus(toIdMap.get(toId));
                result.add(checkFriendShipResp);
            }
        }

        return R.successResponse(result);
    }

    @Transactional
    public R<?> doUpdate(String fromId, FriendDto dto, Integer appId) {

        UpdateWrapper<ImFriendShipEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().set(ImFriendShipEntity::getAddSource, dto.getAddSource())
                .set(ImFriendShipEntity::getExtra, dto.getExtra())
                .set(ImFriendShipEntity::getRemark, dto.getRemark())
                .eq(ImFriendShipEntity::getAppId, appId)
                .eq(ImFriendShipEntity::getToId, dto.getToId())
                .eq(ImFriendShipEntity::getFromId, fromId);
        int update = imFriendShipMapper.update(null, updateWrapper);
        if (update == 1) {
            return R.successResponse();
        }
        return R.errorResponse();
    }

    @Transactional
    public R<?> doAddFriend(RequestBase requestBase, String fromId, FriendDto dto, Integer appId) {

        //A-B
        //Friend????????? A ??? B ????????????
        //??????????????????????????? ????????????????????????
        QueryWrapper<ImFriendShipEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("app_id", appId)
                .eq("from_id", fromId)
                .eq("to_id", dto.getToId());
        ImFriendShipEntity fromItem = imFriendShipMapper.selectOne(queryWrapper);
        if (fromItem == null) {
            //??????
            fromItem = new ImFriendShipEntity();
            BeanUtils.copyProperties(dto, fromItem);
            fromItem.setFromId(fromId);
            fromItem.setStatus(FriendShipStatusEnum.FRIEND_STATUS_NORMAL.getCode());
            fromItem.setCreateTime(System.currentTimeMillis());

            int result = imFriendShipMapper.insert(fromItem);
            if (result != 1) {
                return R.errorResponse(FriendShipErrorCode.ADD_FRIEND_ERROR);
            }
        } else {
            //???????????????????????????  ????????????????????????????????????
            if (fromItem.getStatus() == FriendShipStatusEnum.FRIEND_STATUS_NORMAL.getCode()) {
                return R.errorResponse(FriendShipErrorCode.TO_IS_YOUR_FRIEND);
            }

            if (fromItem.getStatus() == FriendShipStatusEnum.FRIEND_STATUS_DELETE.getCode()) {
                ImFriendShipEntity update = new ImFriendShipEntity();

                if (StringUtils.isNotBlank(dto.getAddSource())) {
                    update.setAddSource(dto.getAddSource());
                }

                if (StringUtils.isNotBlank(dto.getRemark())) {
                    update.setAddSource(dto.getRemark());
                }
                if (StringUtils.isNotBlank(dto.getExtra())) {
                    update.setAddSource(dto.getExtra());
                }
                update.setStatus(FriendShipStatusEnum.FRIEND_STATUS_NORMAL.getCode());
                int result = imFriendShipMapper.update(update, queryWrapper);
                if (result != 1) {
                    return R.errorResponse(FriendShipErrorCode.ADD_FRIEND_ERROR);
                }
            }
        }
        QueryWrapper<ImFriendShipEntity> toQuery = new QueryWrapper<>();
        toQuery.eq("app_id",appId);
        toQuery.eq("from_id",dto.getToId());
        toQuery.eq("to_id",fromId);
        ImFriendShipEntity toItem = imFriendShipMapper.selectOne(toQuery);
        if(toItem == null){
            toItem = new ImFriendShipEntity();
            toItem.setAppId(appId);
            toItem.setFromId(dto.getToId());
            BeanUtils.copyProperties(dto,toItem);
            toItem.setToId(fromId);
            toItem.setStatus(FriendShipStatusEnum.FRIEND_STATUS_NORMAL.getCode());
            toItem.setCreateTime(System.currentTimeMillis());
            int insert = imFriendShipMapper.insert(toItem);
        }else{
            if(FriendShipStatusEnum.FRIEND_STATUS_NORMAL.getCode() !=
                    toItem.getStatus()){
                ImFriendShipEntity update = new ImFriendShipEntity();
                update.setStatus(FriendShipStatusEnum.FRIEND_STATUS_NORMAL.getCode());
                imFriendShipMapper.update(update,toQuery);
            }
        }
        return R.successResponse();
    }
}

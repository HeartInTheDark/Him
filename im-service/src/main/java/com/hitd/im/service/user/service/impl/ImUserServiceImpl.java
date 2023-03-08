package com.hitd.im.service.user.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hitd.im.common.ResponseVO;
import com.hitd.im.common.enums.DelFlagEnum;
import com.hitd.im.common.enums.UserErrorCode;
import com.hitd.im.common.exception.ApplicationException;
import com.hitd.im.service.user.dao.ImUserDataEntity;
import com.hitd.im.service.user.dao.mapper.ImUserDataMapper;
import com.hitd.im.service.user.model.req.*;
import com.hitd.im.service.user.model.resp.GetUserInfoResp;
import com.hitd.im.service.user.model.resp.ImportUserResp;
import com.hitd.im.service.user.service.ImUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ZhangWeinan
 * @date 2023-03-08 10:36
 * @description
 */
@Service
public class ImUserServiceImpl implements ImUserService {

    @Resource
    private ImUserDataMapper imUserDataMapper;

    @Override
    public ResponseVO<ImportUserResp> importUser(ImportUserReq req) {
        if (req.getUserData().size() > 100){
            return ResponseVO.errorResponse(UserErrorCode.IMPORT_SIZE_BEYOND);
        }
        List<String> successIds = new ArrayList<>();
        List<String> errorIds = new ArrayList<>();
        req.getUserData().forEach(u ->{
            try{
                u.setAppId(req.getAppId());
                int insert = imUserDataMapper.insert(u);
                if (1 ==insert){
                    successIds.add(u.getUserId());
                }
            }catch (Exception e){
                e.printStackTrace();
                errorIds.add(u.getUserId());
            }

        });
        ImportUserResp importUserResp = new ImportUserResp();
        importUserResp.setErrorIds(errorIds);
        importUserResp.setSuccessIds(successIds);
        return ResponseVO.successResponse(importUserResp);
    }

    @Override
    public ResponseVO<GetUserInfoResp> getUserInfo(GetUserInfoReq req) {
        QueryWrapper<ImUserDataEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("app_id",req.getAppId());
        queryWrapper.in("user_id",req.getUserIds());
        queryWrapper.eq("del_flag", DelFlagEnum.NORMAL.getCode());

        List<ImUserDataEntity> userDataEntities = imUserDataMapper.selectList(queryWrapper);
        HashMap<String, ImUserDataEntity> map = new HashMap<>();

        for (ImUserDataEntity data:
                userDataEntities) {
            map.put(data.getUserId(),data);
        }

        List<String> failUser = new ArrayList<>();
        for (String uid:
                req.getUserIds()) {
            if(!map.containsKey(uid)){
                failUser.add(uid);
            }
        }

        GetUserInfoResp resp = new GetUserInfoResp();
        resp.setUserDataItem(userDataEntities);
        resp.setFailUser(failUser);
        return ResponseVO.successResponse(resp);
    }

    @Override
    public ResponseVO<ImUserDataEntity> getSingleUserInfo(String userId, Integer appId) {
        QueryWrapper<ImUserDataEntity> userDataEntityQueryWrapper = new QueryWrapper<>();
        userDataEntityQueryWrapper.eq("app_id",appId);
        userDataEntityQueryWrapper.eq("user_id",userId);
        userDataEntityQueryWrapper.eq("del_flag", DelFlagEnum.NORMAL.getCode());

        ImUserDataEntity ImUserDataEntity = imUserDataMapper.selectOne(userDataEntityQueryWrapper);
        if(ImUserDataEntity == null){
            return ResponseVO.errorResponse(UserErrorCode.USER_IS_NOT_EXIST);
        }

        return ResponseVO.successResponse(ImUserDataEntity);
    }

    @Override
    public ResponseVO<?> deleteUser(DeleteUserReq req) {
        ImUserDataEntity entity = new ImUserDataEntity();
        entity.setDelFlag(DelFlagEnum.DELETE.getCode());

        List<String> errorId = new ArrayList<>();
        List<String> successId = new ArrayList<>();

        for (String userId: req.getUserId()) {
            QueryWrapper<ImUserDataEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("app_id",req.getAppId());
            wrapper.eq("user_id",userId);
            wrapper.eq("del_flag",DelFlagEnum.NORMAL.getCode());
            int update = 0;
            try {
                update =  imUserDataMapper.update(entity, wrapper);
                if(update > 0){
                    successId.add(userId);
                }else{
                    errorId.add(userId);
                }
            }catch (Exception e){
                errorId.add(userId);
            }
        }

        ImportUserResp resp = new ImportUserResp();
        resp.setSuccessIds(successId);
        resp.setErrorIds(errorId);
        return ResponseVO.successResponse(resp);
    }

    @Override
    @Transactional
    public ResponseVO<?> modifyUserInfo(ModifyUserInfoReq req) {
        QueryWrapper<ImUserDataEntity> query = new QueryWrapper<>();
        query.eq("app_id",req.getAppId());
        query.eq("user_id",req.getUserId());
        query.eq("del_flag",DelFlagEnum.NORMAL.getCode());
        ImUserDataEntity user = imUserDataMapper.selectOne(query);
        if(user == null){
            throw new ApplicationException(UserErrorCode.USER_IS_NOT_EXIST);
        }

        ImUserDataEntity update = new ImUserDataEntity();
        BeanUtils.copyProperties(req,update);

        update.setAppId(null);
        update.setUserId(null);
        int update1 = imUserDataMapper.update(update, query);
//        if(update1 == 1){
//            UserModifyPack pack = new UserModifyPack();
//            BeanUtils.copyProperties(req,pack);
//            messageProducer.sendToUser(req.getUserId(),req.getClientType(),req.getImei(),
//                    UserEventCommand.USER_MODIFY,pack,req.getAppId());
//
//            if(appConfig.isModifyUserAfterCallback()){
//                callbackService.callback(req.getAppId(),
//                        Constants.CallbackCommand.ModifyUserAfter,
//                        JSONObject.toJSONString(req));
//            }
//            return ResponseVO.successResponse();
//        }
        throw new ApplicationException(UserErrorCode.MODIFY_USER_ERROR);
    }

    @Override
    public ResponseVO<?> login(LoginReq req) {
        return ResponseVO.successResponse();
    }

    @Override
    public ResponseVO<?> getUserSequence(GetUserSequenceReq req) {
//        Map<Object, Object> map = stringRedisTemplate.opsForHash().entries(req.getAppId() + ":" + Constants.RedisConstants.SeqPrefix + ":" + req.getUserId());
//        Long groupSeq = imGroupService.getUserGroupMaxSeq(req.getUserId(),req.getAppId());
//        map.put(Constants.SeqConstants.Group,groupSeq);
//        return ResponseVO.successResponse(map);
        return null;
    }
}


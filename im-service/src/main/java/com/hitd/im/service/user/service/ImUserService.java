package com.hitd.im.service.user.service;


import com.hitd.im.common.ResponseVO;
import com.hitd.im.service.user.dao.ImUserDataEntity;
import com.hitd.im.service.user.model.req.*;
import com.hitd.im.service.user.model.resp.GetUserInfoResp;
import com.hitd.im.service.user.model.resp.ImportUserResp;

/**
 * @author ZhangWeinan
 * @date 2023-03-08 10:35
 * @description
 */
public interface ImUserService {

    ResponseVO<ImportUserResp> importUser(ImportUserReq req);

    ResponseVO<GetUserInfoResp> getUserInfo(GetUserInfoReq req);

    ResponseVO<ImUserDataEntity> getSingleUserInfo(String userId , Integer appId);

    ResponseVO<?> deleteUser(DeleteUserReq req);

    ResponseVO<?> modifyUserInfo(ModifyUserInfoReq req);

    ResponseVO<?> login(LoginReq req);

    ResponseVO<?> getUserSequence(GetUserSequenceReq req);
}

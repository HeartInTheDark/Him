package com.hitd.im.service.user.service;


import com.hitd.im.common.R;
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

    R<ImportUserResp> importUser(ImportUserReq req);

    R<GetUserInfoResp> getUserInfo(GetUserInfoReq req);

    R<ImUserDataEntity> getSingleUserInfo(String userId , Integer appId);

    R<?> deleteUser(DeleteUserReq req);

    R<?> modifyUserInfo(ModifyUserInfoReq req);

    R<?> login(LoginReq req);

    R<?> getUserSequence(GetUserSequenceReq req);
}

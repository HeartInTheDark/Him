package com.hitd.im.service.user.model.resp;


import com.hitd.im.service.user.dao.ImUserDataEntity;
import lombok.Data;

import java.util.List;

/**
 * @author ZhangWeinan
 * @date 2023-03-08 11:24
 * @description
 */
@Data
public class GetUserInfoResp {

    private List<ImUserDataEntity> userDataItem;

    private List<String> failUser;


}

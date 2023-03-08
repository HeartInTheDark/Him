package com.hitd.im.service.user.model.req;

import com.hitd.im.common.model.RequestBase;
import com.hitd.im.service.user.dao.ImUserDataEntity;
import lombok.Data;

import java.util.List;

/**
 * @Author ZhangWeinan
 * @Date 2023/3/8 10:12
 * @DES
 * @Since Copyright(c)
 */
@Data
public class ImportUserReq extends RequestBase {

    private List<ImUserDataEntity> userData;
}

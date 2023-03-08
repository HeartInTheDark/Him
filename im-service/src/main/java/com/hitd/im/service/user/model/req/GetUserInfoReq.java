package com.hitd.im.service.user.model.req;


import com.hitd.im.common.model.RequestBase;
import lombok.Data;

import java.util.List;

/**
 * @author ZhangWeinan
 * @date 2023-03-08 11:24
 * @description 
 */
@Data
public class GetUserInfoReq extends RequestBase {

    private List<String> userIds;


}

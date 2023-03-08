package com.hitd.im.service.user.model.req;


import com.hitd.im.common.model.RequestBase;
import lombok.Data;

import java.util.List;

/**
 * @author ZhangWeinan
 * @date 2023-03-08 11:23
 * @description
 */
@Data
public class SubscribeUserOnlineStatusReq extends RequestBase {

    private List<String> subUserId;

    private Long subTime;


}

package com.hitd.im.service.friendship.model.req;


import com.hitd.im.common.model.RequestBase;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author ZhangWeinan
 * @date 2023-03-08 23:21
 * @description
 */
@Data
public class GetFriendShipRequestReq extends RequestBase {

    @NotBlank(message = "用户id不能为空")
    private String fromId;

}

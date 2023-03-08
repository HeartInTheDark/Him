package com.hitd.im.service.user.model.req;


import com.hitd.im.common.model.RequestBase;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author ZhangWeinan
 * @date 2023-03-08 11:24
 * @description 
 */
@Data
public class DeleteUserReq extends RequestBase {

    @NotEmpty(message = "用户id不能为空")
    private List<String> userId;
}

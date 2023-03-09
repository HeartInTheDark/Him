package com.hitd.im.service.group.model.req;

import com.hitd.im.common.model.RequestBase;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author ZhangWeinan
 * @date 2023-03-09 11:31
 * @description
 */
@Data
public class DestroyGroupReq extends RequestBase {

    @NotNull(message = "群id不能为空")
    private String groupId;

}

package com.hitd.im.service.group.model.req;

import com.hitd.im.common.model.RequestBase;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author ZhangWeinan
 * @date 2023-03-09 11:32
 * @description
 */
@Data
public class RemoveGroupMemberReq extends RequestBase {

    @NotBlank(message = "群id不能为空")
    private String groupId;

    private String memberId;

}

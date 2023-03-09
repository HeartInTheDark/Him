package com.hitd.im.service.group.model.req;

import com.hitd.im.common.model.RequestBase;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author ZhangWeinan
 * @date 2023-03-09 11:32
 * @description
 */
@Data
public class MuteGroupReq extends RequestBase {

    @NotBlank(message = "groupId不能为空")
    private String groupId;

    @NotNull(message = "mute不能为空")
    private Integer mute;

}

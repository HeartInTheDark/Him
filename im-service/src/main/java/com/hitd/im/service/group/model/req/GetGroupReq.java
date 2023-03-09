package com.hitd.im.service.group.model.req;

import com.hitd.im.common.model.RequestBase;
import lombok.Data;

/**
 * @author ZhangWeinan
 * @date 2023-03-09 11:31
 * @description
 */
@Data
public class GetGroupReq extends RequestBase {

    private String groupId;

}

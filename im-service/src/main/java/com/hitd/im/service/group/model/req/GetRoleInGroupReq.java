package com.hitd.im.service.group.model.req;

import com.hitd.im.common.model.RequestBase;
import lombok.Data;

import java.util.List;

/**
 * @author ZhangWeinan
 * @date 2023-03-09 11:31
 * @description
 */
@Data
public class GetRoleInGroupReq extends RequestBase {

    private String groupId;

    private List<String> memberId;
}

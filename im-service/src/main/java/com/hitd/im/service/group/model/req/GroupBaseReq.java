package com.hitd.im.service.group.model.req;

import com.hitd.im.common.model.RequestBase;
import lombok.Data;

/**
 * @Author ZhangWeinan
 * @Date 2023/3/10 16:17
 * @DES
 * @Since Copyright(c)
 */
@Data
public class GroupBaseReq extends RequestBase {
    private String groupId;
}

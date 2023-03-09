package com.hitd.im.service.group.model.resp;

import lombok.Data;

/**
 * @author ZhangWeinan
 * @date 2023-03-09 11:33
 * @description
 */
@Data
public class GetRoleInGroupResp {

    private Long groupMemberId;

    private String memberId;

    private Integer role;

    private Long speakDate;

}

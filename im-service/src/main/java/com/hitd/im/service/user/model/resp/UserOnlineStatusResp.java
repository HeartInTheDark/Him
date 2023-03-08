package com.hitd.im.service.user.model.resp;


import com.hitd.im.common.model.UserSession;
import lombok.Data;

import java.util.List;

/**
 * @author ZhangWeinan
 * @date 2023-03-08 11:24
 * @description 
 */
@Data
public class UserOnlineStatusResp {

    private List<UserSession> session;

    private String customText;

    private Integer customStatus;

}

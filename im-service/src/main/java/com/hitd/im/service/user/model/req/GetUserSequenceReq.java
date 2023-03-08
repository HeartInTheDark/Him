package com.hitd.im.service.user.model.req;


import com.hitd.im.common.model.RequestBase;
import lombok.Data;
/**
 * @author ZhangWeinan
 * @date 2023-03-08 11:24
 * @description 
 */
@Data
public class GetUserSequenceReq extends RequestBase {

    private String userId;

}

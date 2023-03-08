package com.hitd.im.service.friendship.model.req;

import lombok.Data;

/**
 * @author ZhangWeinan
 * @date 2023-03-08 21:53
 * @description 
 */
@Data
public class FriendDto {

    private String toId;

    private String remark;

    private String addSource;

    private String extra;

    private String addWording;

}

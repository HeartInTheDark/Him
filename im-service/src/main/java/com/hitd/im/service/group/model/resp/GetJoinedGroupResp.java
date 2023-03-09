package com.hitd.im.service.group.model.resp;


import com.hitd.im.service.group.dao.ImGroupEntity;
import lombok.Data;

import java.util.List;

/**
 * @author ZhangWeinan
 * @date 2023-03-09 11:33
 * @description
 */
@Data
public class GetJoinedGroupResp {

    private Integer totalCount;

    private List<ImGroupEntity> groupList;

}

package com.hitd.im.service.user.model.resp;

import lombok.Data;

import java.util.List;

/**
 * @Author ZhangWeinan
 * @Date 2023/3/8 10:43
 * @DES
 * @Since Copyright(c)
 */
@Data
public class ImportUserResp {

    private List<String> successIds;
    private List<String> errorIds;
}

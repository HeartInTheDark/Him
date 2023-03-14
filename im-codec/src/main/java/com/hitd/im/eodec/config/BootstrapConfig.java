package com.hitd.im.eodec.config;

import lombok.Data;

/**
 * @Author ZhangWeinan
 * @Date 2023/3/14 18:56
 * @DES
 * @Since Copyright(c)
 */
@Data
public class BootstrapConfig {
    private Him him;

    @Data
    public static class Him {
        private Integer tcpPort;
        private Integer websocketPort;
        private Integer bossThreadSize;
        private Integer workThreadSize;
        private Integer connectionQueue = 10240;
    }
}

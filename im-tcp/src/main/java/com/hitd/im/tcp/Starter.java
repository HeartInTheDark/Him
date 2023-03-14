package com.hitd.im.tcp;


import com.hitd.im.eodec.config.BootstrapConfig;
import com.hitd.im.tcp.server.HimServer;
import com.hitd.im.tcp.server.HimTcpServer;
import com.hitd.im.tcp.server.HimWebSocketServer;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * @Author ZhangWeinan
 * @Date 2023/3/14 17:59
 * @DES
 * @Since Copyright(c)
 */

public class Starter {

    public static void main(String[] args) {
        if (args.length > 0) {
            start(args[0]);
        } else {
            throw new NoSuchFieldError("No profile specified");
        }
    }

    public static void start(String path) {
        try {
            Yaml yaml = new Yaml();
            InputStream inputStream = Files.newInputStream(new File(path).toPath());
            BootstrapConfig bootstrapConfig = yaml.loadAs(inputStream, BootstrapConfig.class);
            HimServer himServer = new HimTcpServer(bootstrapConfig.getHim());
            himServer.start();
            HimServer himWebSocketServer = new HimWebSocketServer(bootstrapConfig.getHim());
            himWebSocketServer.start();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(5000);
        }

    }
}

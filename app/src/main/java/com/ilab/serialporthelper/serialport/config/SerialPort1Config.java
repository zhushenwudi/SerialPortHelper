package com.ilab.serialporthelper.serialport.config;

import top.keepempty.sph.library.SerialPortConfig;

/**
 * @author hoshizora-rin
 */
public class SerialPort1Config extends SerialPortConfig {
    private volatile static SerialPortConfig singleton;

    private SerialPort1Config() {
    }

    public static SerialPortConfig getSingleton() {
        if (singleton == null) {
            synchronized (SerialPort1Config.class) {
                if (singleton == null) {
                    singleton = new SerialPortConfig();
                    // 是否使用原始模式(Raw Mode)方式来通讯
                    singleton.mode = 0;
                    // 串口地址 [ttyS0 ~ ttyS6, ttyUSB0 ~ ttyUSB4]
                    singleton.path = "dev/ttyS3";
                    // 波特率
                    singleton.baudRate = 600;
                    // 数据位 [7, 8]
                    singleton.dataBits = 8;
                    // 检验类型 [N(无校验) ,E(偶校验), O(奇校验)] (大小写随意)
                    singleton.parity = 'n';
                    // 停止位 [1, 2]
                    singleton.stopBits = 1;
                }
            }
        }
        return singleton;
    }
}

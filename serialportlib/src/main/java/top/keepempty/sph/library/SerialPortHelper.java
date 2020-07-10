package top.keepempty.sph.library;

import android.util.Log;

/**
 * 串口助手
 *
 * @author ：frey
 * @date：2019/3/30 18:26
 */
public class SerialPortHelper {
    private SerialPortJNI serialPort = null;

    private final static String TAG = SerialPortHelper.class.getSimpleName();

    private boolean mIsOpen = false;

    private SerialPortConfig serialPortConfig;

    private SphThreads sphThreads;

    /**
     * 最大接收数据的长度
     */
    private int maxSize;

    /**
     * 是否需要返回最大数据接收长度
     */
    private boolean isReceiveMaxSize;

    /**
     * 数据回调
     */
    private SphResultCallback onResultCallback;

    /**
     * 数据处理
     */
    private SphDataProcess processingData;


    /**
     * 初始化串口操作
     *
     * @param maxSize 串口每次读取数据的最大长度
     */
    public SerialPortHelper(int maxSize) {
        this(maxSize, false);
    }

    /**
     * 初始化串口操作
     *
     * @param maxSize          串口每次读取数据的最大长度
     * @param isReceiveMaxSize 是否需要按最大接收数据进行返回
     */
    public SerialPortHelper(int maxSize, boolean isReceiveMaxSize) {
        this(maxSize, isReceiveMaxSize, new SerialPortConfig());
    }

    /**
     * 初始化串口操作
     *
     * @param maxSize          串口每次读取数据的最大长度
     * @param isReceiveMaxSize 是否需要按最大接收数据进行返回
     * @param serialPortConfig 串口数据
     */
    public SerialPortHelper(int maxSize, boolean isReceiveMaxSize, SerialPortConfig serialPortConfig) {
        this.maxSize = maxSize;
        this.isReceiveMaxSize = isReceiveMaxSize;
        this.serialPortConfig = serialPortConfig;
    }

    /**
     * 设置数据回调
     *
     * @param onResultCallback 数据回调
     */
    public void setSphResultCallback(SphResultCallback onResultCallback) {
        if (sphThreads == null) {
            this.onResultCallback = onResultCallback;
            return;
        }
        processingData.setSphResultCallback(onResultCallback);
    }

    /**
     * 串口设置
     */
    public void setConfigInfo(SerialPortConfig serialPortConfig) {
        this.serialPortConfig = serialPortConfig;
    }

    /**
     * 打开串口设备
     *
     * @param path     串口地址
     * @param baudRate 波特率
     */
    public boolean openDevice(String path, int baudRate) {
        this.serialPortConfig.path = path;
        this.serialPortConfig.baudRate = baudRate;
        return openDevice();
    }


    /**
     * 打开串口设备
     */
    public boolean openDevice() {
        if (serialPortConfig == null) {
            throw new IllegalArgumentException("'SerialPortConfig' must can not be null!!! ");
        }
        if (serialPortConfig.path == null) {
            throw new IllegalArgumentException("You not have setting the device path, " +
                    "you must 'new SerialPortHelper(String path)' or call 'openDevice(String path)' ");
        }
        if (serialPort == null) {
            serialPort = new SerialPortJNI();
        }

        int i = serialPort.openPort(
                this.serialPortConfig.path,
                this.serialPortConfig.baudRate,
                this.serialPortConfig.dataBits,
                this.serialPortConfig.stopBits,
                this.serialPortConfig.parity);

        // 是否设置原始模式(Raw Mode)方式来通讯
        if (serialPortConfig.mode != 0) {
            serialPort.setMode(serialPortConfig.mode);
        }

        // 打开串口成功
        if (i == 1) {
            mIsOpen = true;
            // 创建数据处理
            processingData = new SphDataProcess(serialPort, maxSize);
            processingData.setRecevieMaxSize(isReceiveMaxSize);
            processingData.setSphResultCallback(onResultCallback);
            // 开启读写线程
            sphThreads = new SphThreads(serialPort, processingData);
        } else {
            mIsOpen = false;
            Log.e(TAG, "cannot open the device !!! " +
                    "path:" + serialPortConfig.path);
        }
        return mIsOpen;
    }

    /**
     * 发送数据
     *
     * @param commands
     */
    public void send(byte[] commands) {
        if (!isOpenDevice()) {
            Log.e(TAG, "You not open device !!! ");
            return;
        }
        serialPort.writePort(commands);
    }

    /**
     * 发送Hex数据
     *
     * @param hexCommands
     */
    public void sendHex(String hexCommands) {
        if (!isOpenDevice()) {
            Log.e(TAG, "You not open device !!! ");
            return;
        }
        serialPort.writePort(DataConversion.decodeHexString(hexCommands));
    }

    /**
     * 关闭串口
     */
    public void closeDevice() {
        serialPort.closePort();
        if (sphThreads != null) {
            sphThreads.stop();
        }
    }

    /**
     * 判断串口是否打开
     */
    public boolean isOpenDevice() {
        return mIsOpen;
    }

}

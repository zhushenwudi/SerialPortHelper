package top.keepempty.sph.library;

/**
 * 串口读写线程
 *
 * @author：frey
 * @date：2019/3/30 18:45
 */
public class SphThreads {
    private SerialPortJNI serialPort;

    /**
     * 读数据线程
     */
    private Thread readThread;

    /**
     * 数据处理
     */
    private SphDataProcess processingData;

    public SphThreads(SerialPortJNI serialPort, SphDataProcess processingData) {
        this.serialPort = serialPort;
        this.processingData = processingData;
        readThread = new Thread(new ReadThread());
        readThread.start();
    }

    /**
     * 数据读取线程
     */
    public class ReadThread implements Runnable {

        @Override
        public void run() {
            while (!readThread.isInterrupted()) {
                // 创建数据接收数组
                processingData.createReadBuff();
                // 读取数据
                byte[] bytes = serialPort.readPort(processingData.getMaxSize());
                if (bytes != null) {
                    int revLength = bytes.length;
                    if (revLength > 0) {
                        processingData.processingRecData(bytes, revLength);
                    }
                }
            }
        }
    }

    /**
     * 停止线程
     */
    public void stop() {
        if (readThread != null) {
            readThread.interrupt();
        }
    }
}

package top.keepempty.sph.library;

/**
 * 串口数据回调
 *
 * @author ：frey
 * @date：2019/3/30 18:25
 */
public interface SphResultCallback {
    /**
     * 收到的数据
     *
     * @param data 串口收到的数据
     */
    void onReceiveData(byte[] data);

    /**
     * 发送，收取完成
     */
    void onComplete();

}

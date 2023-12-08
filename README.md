# SerialPortHelper

## 推荐由Kotlin重构的新串口通信库：https://github.com/zhushenwudi/serialport

<!--本程序基于不支持创建多实例的原项目
https://github.com/freyskill/SerialPortHelper  
根据issue #2中源码修改的 dearchun大神 所提供的lib包制作  

#### 接入方式
1.下载ZIP  
git clone https://github.com/zhushenwudi/SerialPortHelper.git  

2.依赖包名：serialportlib  

#### 示例
1.在Project build.gradle中添加  
	
```groovy
buildscript {
   ...
   dependencies {
      classpath 'com.github.dcendents:android-maven-gradle-plugin:2.1'
   }
}
```
 
2.配置SerialPort

	示例采用单例模式，文件位于 com.ilab.serialporthelper.serialport.config.SerialPort1Config
	
```java
singleton.mode = 0;              // 是否使用原始模式(Raw Mode)方式来通讯
singleton.path = "dev/ttyS3";    // 串口地址 [ttyS0 ~ ttyS6, ttyUSB0 ~ ttyUSB4]
singleton.baudRate = 600;        // 波特率
singleton.dataBits = 8;          // 数据位 [7, 8]
singleton.parity = 'n';          // 检验类型 [N(无校验) ,E(偶校验), O(奇校验)] (大小写随意)
singleton.stopBits = 1;          // 停止位 [1, 2]
```

3.SerialUtil工具类

```java
//实例化对象
serialPortHelper = new SerialPortHelper(16);
	
//配置实例参数
serialPortHelper.setConfigInfo(SerialPort1Config.getSingleton());
	
//打开串口，返回为true表示打开成功
serialPortHelper.openDevice();
	
//设置接收监听
serialPortHelper.setSphResultCallback(new SphResultCallback() {
    @Override
    public void onReceiveData(byte[] data) {
    	//接受到的数据
    	String str = Arrays.toString(data);
    }
	
    @Override
    public void onComplete() { }
});
	
//发送串口数据
serialPortHelper.sendHex("要发送的16进制字符串");
	
//判断串口是否打开
serialPortHelper.isOpenDevice();
	
//关闭串口通信
serialPortHelper.closeDevice();
```
	
4.主线程调用

```java
//实例化工具类
serial1Util = new Serial1Util();
	
//创建通信线程
serial1Util.start();
	
//关闭通信线程
serial1Util.stop();
```

5.Log截图

![Image text](https://raw.githubusercontent.com/zhushenwudi/SerialPortHelper/master/example.png)

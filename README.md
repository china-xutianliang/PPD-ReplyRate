# PPD-ReplyRate
拼多多刷高商家回复率（点对点自动聊天）

# 技术栈  JDK17   Gradle8.7
1. 模拟器可执行文件，比如ldconsole.exe、adb.exe(实例雷电)
    - ldconsole.exe: 根据设备名称自动拉起模拟器
    - adb.exe: 查询设备ID
2. appium(nodeJs),可根据设备ID拉起对应的App,appium是一个拉起app控制器，
    项目启动时，自动开启appium。appium一个实例，可拉起多个设备ID-App
3. 拉起成功后，返回AndroidDriver,根据这个对象，调用andrion sdk，去读取App页面元素操作
4. inno Setup 打包安装程序


# appium
  - 实际上是个客户端，启动appium后，根据Url+端口,拉起App,所以如果是开发调试，不面向用户，本地安装nodeJS,启动Appium即可。
  代码中在根据Url+端口进行拉起App。
  - 本项目面向的是用户，需要将appium单独打包成一个可执行文件.exe。用户启动项目的时候，自动拉起appium。
    1.使用nodeJS脚本，对appium进行打包，移除android环境配置,单独执行appium调试,拉起App会报缺少android SDK,需要将这些SDK也打包进来，并且指定appium的环境配置（虚拟）
       下载地址:https://dl.google.com/android/repository/platform-tools-latest-windows.zip?hl=zh-cn
    2.由于虚拟路径问题，硬代码无法修改。有几个文件指定
    C:\snapshot\appium\node_modules\appium\node_modules\appium-uiautomator2-server\apks
    该路径,需要将相关的文件进行拷贝到该路径
  

windows安装包链接: https://caiyun.139.com/m/i?1J5C260NHeBML  提取码:2wR9
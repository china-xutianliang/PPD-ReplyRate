const path = require('path');
const fs = require('fs');
const os = require('os');
const { main: appiumMain } = require('appium/build/lib/main');

// 设置临时目录
const tempDir = path.join(os.tmpdir(), 'appium');

if (!fs.existsSync(tempDir)) {
    fs.mkdirSync(tempDir, { recursive: true });
    // 将 adb.exe 和相关 DLL 文件从资源目录复制到临时目录
    fs.copyFileSync(path.resolve(__dirname, 'sdk/adb.exe'), path.join(tempDir, 'adb.exe'));
    fs.copyFileSync(path.resolve(__dirname, 'sdk/AdbWinApi.dll'), path.join(tempDir, 'AdbWinApi.dll'));
    fs.copyFileSync(path.resolve(__dirname, 'sdk/AdbWinUsbApi.dll'), path.join(tempDir, 'AdbWinUsbApi.dll'));
    fs.copyFileSync(path.resolve(__dirname, 'sdk/apksigner.jar'), path.join(tempDir, 'apksigner.jar'));

    // 将 settings_apk-debug.apk 复制到临时目录
    fs.copyFileSync(
        path.resolve(__dirname, 'node_modules/appium/node_modules/io.appium.settings/apks/settings_apk-debug.apk'),
        path.join(tempDir, 'settings_apk-debug.apk')
    );
    
    // 将 appium-uiautomator2-server-v4.27.0.apk 复制到临时目录
    fs.copyFileSync(
        path.resolve(__dirname, 'node_modules/appium/node_modules/appium-uiautomator2-server/apks/appium-uiautomator2-server-v4.27.0.apk'),
        path.join(tempDir, 'appium-uiautomator2-server-v4.27.0.apk')
    );
}

// 设置实际路径到环境变量
process.env.ANDROID_HOME = tempDir;
process.env.ANDROID_SDK_ROOT = process.env.ANDROID_HOME;
process.env.PATH = `${tempDir};${process.env.PATH}`;

// 强制覆盖 Appium 使用的 APK 路径
process.env.APPIUM_UIAUTOMATOR2_SERVER_APK = path.join(tempDir, 'appium-uiautomator2-server-v4.27.0.apk');
process.env.APPIUM_SETTINGS_APK_PATH = path.join(tempDir, 'settings_apk-debug.apk');
process.env.APPIUM_APKSIGNER_PATH = path.join(tempDir, 'apksigner.jar');

// 从命令行参数获取端口号，默认为 4723
const port = process.argv[2] ? parseInt(process.argv[2], 10) : 4723;

// 启动Appium服务
async function startAppiumService() {
    try {
        console.log('Attempting to start Appium server...');
        await appiumMain({
            port: port,
            loglevel: 'info',
            // 在启动Appium服务器时指定默认的能力参数
            defaultCapabilities: {
                'noSign': true
            },
            args: {
                'no-sign': true
            }
        });
        console.log(`Appium server started at port ${port}`);
    } catch (err) {
        console.error("Error starting Appium server:", err);
    }
}

startAppiumService().catch(console.error);

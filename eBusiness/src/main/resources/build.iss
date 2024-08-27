[Setup]
; 应用程序唯一标识符
AppId={{C1A7C55F-AD5A-42B6-8F8C-F7369B8BB8E1}
; 应用程序名称
AppName=eDS
; 版本号
AppVersion=1.0.0
; 发布者名称
AppPublisher=xtl
; 发布者网址
AppPublisherURL=874309708@qq.com
; 支持网址
AppSupportURL=874309708@qq.com
; 更新网址
AppUpdatesURL=874309708@qq.com
; 默认安装路径（Program Files）
DefaultDirName={pf}\eDS
; 允许的架构（x64）
ArchitecturesAllowed=x64compatible
; 64位模式安装
ArchitecturesInstallIn64BitMode=x64compatible
; 不显示程序组页面
DisableProgramGroupPage=yes
; 输出文件名
OutputBaseFilename=eDS
; 安装程序图标
SetupIconFile=D:\yunpu\eDS\eBusiness\src\main\resources\icons\main.ico
; 压缩算法
Compression=lzma
; 启用固体压缩
SolidCompression=yes
; 使用现代安装界面
WizardStyle=modern

; 设置输出目录
OutputDir=D:\yunpu\eDS


[Languages]
; 简体中文语言
Name: "chs"; MessagesFile: "compiler:Default.isl"

[Tasks]
; 创建桌面图标任务（默认未选中）
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked

[Files]
; 复制 eDS.exe 到安装目录
Source: "F:\eDS\eDS.exe"; DestDir: "{app}"; Flags: ignoreversion
; 复制 runtime 文件夹及子文件到安装目录
Source: "F:\eDS\runtime\*"; DestDir: "{app}\runtime"; Flags: ignoreversion recursesubdirs createallsubdirs
; 复制 app 文件夹及子文件到安装目录
Source: "F:\eDS\app\*"; DestDir: "{app}\app"; Flags: ignoreversion recursesubdirs createallsubdirs

[Icons]
; 开始菜单快捷方式
Name: "{autoprograms}\eDS"; Filename: "{app}\eDS.exe"
; 桌面快捷方式
Name: "{autodesktop}\eDS"; Filename: "{app}\eDS.exe"; Tasks: desktopicon

[Run]
; 安装完成后运行 eDS.exe
Filename: "{app}\eDS.exe"; Description: "{cm:LaunchProgram,eDS}"; Flags: nowait postinstall skipifsilent

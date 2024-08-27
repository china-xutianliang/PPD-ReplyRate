package com.xtl.ebusiness.service.simulator;


public interface Simulator {


    /**
     * 根据模拟器设备名称拉起模拟器（如果已启动则不拉起，解锁屏幕）
     * 启动后返回当前设备ID
     * @param simulatorType
     * @param simulatorName
     */
    String startSimulatorByDeviceName(int simulatorType,String simulatorName);


    /**
     * 根据设备名称查询设备ID
     * @param ldconsolePath
     * @param deviceName
     * @return
     */
    String getDeviceIdByName(String ldconsolePath, String deviceName);

}

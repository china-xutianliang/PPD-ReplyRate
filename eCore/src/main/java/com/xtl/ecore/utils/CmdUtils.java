package com.xtl.ecore.utils;

import com.xtl.ecore.exception.BusinessException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.function.Consumer;

@Slf4j
public class CmdUtils {

    @SneakyThrows
    public static String executeCommand(String... command) {
        StringBuilder output = new StringBuilder();
        execute(command, line -> output.append(line).append("\n"));
        return output.toString();
    }

    @SneakyThrows
    public static void executeCommandAsync(String... command) {
        new Thread(() -> {
            execute(command, line -> log.info("executeCommandAsync: {}", line));
        }).start();
    }

    private static void execute(String[] command, Consumer<String> outputConsumer) {
        try {
            // 打印即将执行的命令
            log.info("Executing command: {}", String.join(" ", command));

            // 在命令前添加 "cmd /c" 以启动新的命令行环境，但不弹出新的 CMD 窗口
            String[] newCommand = new String[command.length + 2];
            newCommand[0] = "cmd";
            newCommand[1] = "/c";
            System.arraycopy(command, 0, newCommand, 2, command.length);

            ProcessBuilder processBuilder = new ProcessBuilder(newCommand);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            // 手动指定编码为GBK
            Charset charset = Charset.forName("GBK");

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), charset));
            String line;
            while ((line = reader.readLine()) != null) {
                outputConsumer.accept(line);
            }
            int exitCode = process.waitFor();
            if (exitCode != 0 && exitCode!=1) {
                log.error("命令执行失败: {}", String.join(" ", command));
                throw new BusinessException("命令执行失败");
            }
        } catch (Exception e) {
            log.error("命令执行中断:{}  cause:{} ", String.join(" ", command), e.getMessage());
            throw new BusinessException("命令执行中断: " + e.getMessage());
        }
    }

}

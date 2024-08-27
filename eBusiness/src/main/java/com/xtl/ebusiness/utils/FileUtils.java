package com.xtl.ebusiness.utils;

import com.xtl.ecore.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Slf4j
public class FileUtils {

    public static void copyFileFromResources(String resourcePath, String targetDirectory) {
        try {
            // 获取 resources 目录下的文件
            ClassPathResource classPathResource = new ClassPathResource(resourcePath);

            // 创建目标目录（如果不存在）
            Path targetDirPath = Paths.get(targetDirectory);
            if (!Files.exists(targetDirPath)) {
                Files.createDirectories(targetDirPath);
            }

            // 获取目标文件路径
            Path targetFilePath = targetDirPath.resolve(Objects.requireNonNull(classPathResource.getFilename()));

            // 检查文件是否已经存在
            if (Files.exists(targetFilePath)) {
                log.info("文件已存在，跳过复制: {}", targetFilePath);
            } else {
                // 复制文件
                FileCopyUtils.copy(classPathResource.getInputStream(), Files.newOutputStream(targetFilePath));
                log.info("文件复制成功:{}", targetFilePath);
            }
        } catch (IOException e) {
            log.info("文件复制过程中发生错误:{}", e.getMessage());
            throw new BusinessException("文件复制发生错误");
        } catch (Exception e) {
            log.info("发生了一个非预期的错误:{}", e.getMessage());
            throw new BusinessException("文件复制发生错误");
        }
    }


}

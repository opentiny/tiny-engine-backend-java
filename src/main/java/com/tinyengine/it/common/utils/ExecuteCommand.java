package com.tinyengine.it.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * The type Execute command.
 *
 * @since 2024-10-20
 */
@Slf4j
public class ExecuteCommand {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws IOException the io exception
     */
    public static void main(String[] args) {
        // 指定运行 npm install 的目录
        Path path = Paths.get("C:\\forkWork\\tiny-engine-webservice-java\\src\\main\\java\\com\\tinyengine\\it\\js");
        // 指定 npm 的完整路径
        String npmCommand = "C:\\dev\\node\\npm.cmd";
        List<String> command = new ArrayList<>();
        command.add(npmCommand);
        command.add("install");
        executeCommand(command);
    }

    /**
     * Execute command string builder.
     *
     * @param command the command
     * @return the string builder
     */
    public static StringBuilder executeCommand(List<String> command) {
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        StringBuilder result = new StringBuilder();

        try {
            Process process = processBuilder.start();

            CompletableFuture<Void> outputFuture = CompletableFuture.runAsync(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line).append("\n");
                    }
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            });

            CompletableFuture<Void> errorFuture = CompletableFuture.runAsync(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        // Handle errors if necessary
                        log.error(line);
                    }
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            });

            // Wait for both output and error streams to complete
            CompletableFuture<Void> allOf = CompletableFuture.allOf(outputFuture, errorFuture);
            allOf.join();

            int exitCode = process.waitFor();

            if (exitCode == 0) {
                log.info("命令成功完成！");
            } else {
                log.error("命令运行失败，exitCode: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            log.error(e.getMessage());
        }

        return result;
    }
}

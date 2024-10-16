package com.tinyengine.it.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ExecuteCommand {
    private static final Logger logger = LoggerFactory.getLogger(ExecuteCommand.class);

    public static void main(String[] args) throws IOException {
        // 指定运行 npm install 的目录
        Path path = Paths.get("C:\\forkWork\\tiny-engine-webservice-java\\src\\main\\java\\com\\tinyengine\\it\\js");
        // 指定 npm 的完整路径
        String npmCommand = "C:\\dev\\node\\npm.cmd";
        List<String> command = new ArrayList<>();
        command.add(npmCommand);
        command.add("install");
        executeCommand(command);

    }

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
                    e.printStackTrace();
                }
            });

            CompletableFuture<Void> errorFuture = CompletableFuture.runAsync(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        // Handle errors if necessary
                        logger.error(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            // Wait for both output and error streams to complete
            CompletableFuture<Void> allOf = CompletableFuture.allOf(outputFuture, errorFuture);
            allOf.join();

            int exitCode = process.waitFor();

            if (exitCode == 0) {
                logger.info("命令成功完成！");
            } else {
                logger.error("命令运行失败，exitCode: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return result;
    }
}

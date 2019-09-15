package me;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ProcessRunnerImpl implements ProcessRunner, AutoCloseable {

    private final StringBuffer out = new StringBuffer();
    private Process process;
    private String id;
    private String command;

    public ProcessRunnerImpl(String id, String command) {
        this.id = id;
        this.command = command;
    }

    @Override
    public void start() throws Exception {
        process = runProcess(command);
        Thread.sleep(1000L);
        System.out.println("is process status: " + process.isAlive());
    }

    @Override
    public void stop() {
        process.destroy();
    }

    @Override
    public String getOutput() {
        return out.toString();
    }

    private Process runProcess(String command) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        StreamListener output = new StreamListener(process.getInputStream(), id + " OUTPUT");
        output.start();
        return process;
    }

    @Override
    public void close() {
        stop();
    }

    private class StreamListener extends Thread {
        private final InputStream inputStream;
        private final String type;

        private StreamListener(InputStream is, String type) {
            this.inputStream = is;
            this.type = type;
        }

        @Override
        public void run() {
            try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream)) {
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    out.append(type).append('>').append(line).append("\n");
                    System.out.println(type + ">" + line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

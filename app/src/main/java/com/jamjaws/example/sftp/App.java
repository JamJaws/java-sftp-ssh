package com.jamjaws.example.sftp;

import java.io.IOException;
import java.net.URISyntaxException;

public class App {
    public void run() throws IOException, URISyntaxException {
        SftpService sftpService = new SftpService();
        String file1Content = sftpService.getFile1Content();
        System.out.println(file1Content);
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        App app = new App();
        app.run();
    }
}

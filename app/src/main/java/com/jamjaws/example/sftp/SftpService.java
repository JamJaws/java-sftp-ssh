package com.jamjaws.example.sftp;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class SftpService {

    public String getFile1Content() throws IOException, URISyntaxException {
        SftpClient sftpClient = SftpClient.connect("localhost", 2222, "sftpuser", new File("../sftp/ssh/ssh_client_ed25519"), "secret123456");
        try {
            sftpClient.cd("share");
            String file1Content = sftpClient.getFileContent("file_1.txt");
            return file1Content;
        } finally {
            sftpClient.close();
        }
    }

}

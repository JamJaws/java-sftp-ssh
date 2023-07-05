package com.jamjaws.example.sftp;

import org.apache.commons.vfs2.FileSystemException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;

@Testcontainers
class SftpClientTest {

    @Nested
    class Ed25519 {

        @Container
        public GenericContainer sftpServer = new GenericContainer(DockerImageName.parse("atmoz/sftp:alpine"))
                .withExposedPorts(22)
                .withFileSystemBind("../sftp/ssh/ssh_host_ed25519", "/etc/ssh/ssh_host_ed25519_key", BindMode.READ_ONLY)
                .withFileSystemBind("../sftp/ssh/ssh_client_ed25519.pub", "/home/sftpuser/.ssh/keys/ssh_client_ed25519.pub", BindMode.READ_ONLY)
                .withFileSystemBind("../sftp/share", "/home/sftpuser/share", BindMode.READ_WRITE)
                .withCommand("sftpuser::1001");

        private SftpClient sftpClient;

        @BeforeEach
        void setUp() throws FileSystemException, URISyntaxException {
            sftpClient = SftpClient.connect(sftpServer.getHost(), sftpServer.getMappedPort(22), "sftpuser", new File("../sftp/ssh/ssh_client_ed25519"), "secret123456");
            sftpClient.cd("share");
        }

        @AfterEach
        void tearDown() throws FileSystemException {
            sftpClient.close();
        }

        @Test
        void listFiles() throws FileSystemException {
            List<String> files = sftpClient.listFiles();
            assertThat(files).containsExactly("file_1.txt", "file_2.txt");
        }

        @Test
        void getFile() throws IOException {
            String file1Content = sftpClient.getFileContent("file_1.txt");
            assertThat(file1Content).isEqualTo("alpha");
        }
    }

    @Nested
    class Rsa {

        @Container
        public GenericContainer sftpServer = new GenericContainer(DockerImageName.parse("atmoz/sftp:alpine"))
                .withExposedPorts(22)
                .withFileSystemBind("../sftp/ssh/ssh_host_rsa", "/etc/ssh/ssh_host_rsa_key", BindMode.READ_ONLY)
                .withFileSystemBind("../sftp/ssh/ssh_client_rsa.pub", "/home/sftpuser/.ssh/keys/ssh_client_rsa.pub", BindMode.READ_ONLY)
                .withFileSystemBind("../sftp/share", "/home/sftpuser/share", BindMode.READ_WRITE)
                .withCommand("sftpuser::1001");

        private SftpClient sftpClient;

        @BeforeEach
        void setUp() throws FileSystemException, URISyntaxException {
            sftpClient = SftpClient.connect(sftpServer.getHost(), sftpServer.getMappedPort(22), "sftpuser", new File("../sftp/ssh/ssh_client_rsa"), "secret123456");
            sftpClient.cd("share");
        }

        @AfterEach
        void tearDown() throws FileSystemException {
            sftpClient.close();
        }

        @Test
        void listFiles() throws FileSystemException {
            List<String> files = sftpClient.listFiles();
            assertThat(files).containsExactly("file_1.txt", "file_2.txt");
        }

        @Test
        void getFile() throws IOException {
            String file1Content = sftpClient.getFileContent("file_1.txt");
            assertThat(file1Content).isEqualTo("alpha");
        }
    }
}
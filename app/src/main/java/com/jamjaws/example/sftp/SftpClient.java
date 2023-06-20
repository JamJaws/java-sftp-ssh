package com.jamjaws.example.sftp;

import org.apache.commons.vfs2.*;
import org.apache.commons.vfs2.provider.sftp.IdentityInfo;
import org.apache.commons.vfs2.provider.sftp.IdentityProvider;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static org.apache.commons.vfs2.Selectors.SELECT_FILES;

public class SftpClient {

    public static final String SFTP = "sftp";

    /**
     * Holds instance of remote SFTP folder.
     */
    private FileObject fileObject;

    private SftpClient(FileObject fileObject) {
        this.fileObject = fileObject;
    }

    public static SftpClient connect(String host,
                                     Integer port,
                                     String userName,
                                     File sshKey,
                                     String passphrase) throws URISyntaxException, FileSystemException {
        URI sftpUri = createSftpUri(host, port, userName);

        FileSystemOptions options = new FileSystemOptions();
        SftpFileSystemConfigBuilder sftpConfigBuilder = SftpFileSystemConfigBuilder.getInstance();
        IdentityProvider identityInfo = new IdentityInfo(sshKey, passphrase.getBytes());
        sftpConfigBuilder.setIdentityProvider(options, identityInfo);

        return new SftpClient(VFS.getManager().resolveFile(sftpUri.toString(), options));
    }

    public void close() throws FileSystemException {
        VFS.getManager().closeFileSystem(fileObject.getFileSystem());
    }

    public void cd(String folderPath) throws FileSystemException {
        if (this.fileObject == null) {
            throw new RuntimeException("SFTP connection not initiated.");
        }
        FileObject newFolder = this.fileObject.resolveFile(folderPath);
        if (!newFolder.exists() || !newFolder.isFolder()) {
            throw new RuntimeException("Path does not exist or is not a folder.");
        }
        this.fileObject = newFolder;
    }

    public List<String> listFiles() throws FileSystemException {
        return ofNullable(this.fileObject.findFiles(SELECT_FILES))
                .map(Arrays::asList)
                .orElseGet(Collections::emptyList)
                .stream()
                .map(FileObject::getName)
                .map(FileName::getBaseName)
                .collect(Collectors.toList());
    }

    public String getFileContent(String fileName) throws IOException {
        FileObject fileToGet = fileObject.resolveFile(fileName);
        return new String(fileToGet.getContent().getByteArray());

    }

    private static URI createSftpUri(String host,
                                     Integer port,
                                     String userName) throws URISyntaxException {
        return new URI(SFTP, userName, host, Objects.requireNonNull(port), null, null, null);
    }
}

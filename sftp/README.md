# sftp server

Example for how to start with docker:

```shell
docker run --rm -it \
  -v "$(pwd)/ssh/ssh_host_ed25519:/etc/ssh/ssh_host_ed25519_key" \
  -v "$(pwd)/ssh/ssh_client_ed25519.pub:/home/sftpuser/.ssh/keys/ssh_client_ed25519.pub:ro" \
  -v "$(pwd)/share:/home/sftpuser/share" \
  -p 2222:22 atmoz/sftp:alpine sftpuser::1001
```

Connect via cli:

```shell
sftp -P 2222 -i ssh/ssh_client_ed25519 sftpuser@localhost
```

## Ed25519 / RSA

To run the server with RSA instead of Ed25519 then replace `_ed25519` with `_rsa` the commands above.
package org.apache.ftpserver.impl.node;

import static org.apache.ftpserver.util.PathUtils.resolve;
import org.apache.ftpserver.ftplet.FileSystemView;
import org.apache.ftpserver.ftplet.FtpFile;
import org.apache.ftpserver.ftplet.User;

public class FileSystemView2 implements FileSystemView {

    public String cwd = "/";

    public final FileSystemFactory2 factory;
    public final User user;

    public FileSystemView2(FileSystemFactory2 factory, User user) {
        this.factory = factory;
        this.user = user;
    }

    @Override
    public FtpFile getHomeDirectory() {
        return getFile("/");
    }

    @Override
    public FtpFile getWorkingDirectory() {
        return getFile(cwd);
    }

    @Override
    public boolean changeWorkingDirectory(String dir) {
        String next = resolve(cwd, dir);
        if (!getFile(next).isDirectory()) return false;
        cwd = next;
        return true;
    }

    @Override
    public FtpFile getFile(String file) {
        return new FtpFile2(this, resolve(cwd, file));
    }

    @Override
    public boolean isRandomAccessible() {
        return true;
    }

    @Override
    public void dispose() {
        // NOP
    }
    
}

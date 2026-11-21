package org.apache.ftpserver.impl.node;

import org.apache.ftpserver.ftplet.FileSystemFactory;
import org.apache.ftpserver.ftplet.FileSystemView;
import org.apache.ftpserver.ftplet.User;

public class FileSystemFactory2 implements FileSystemFactory {

    public final NodeTree tree = new NodeTree();

    public FileSystemFactory2() {
        createNode("/", true);
    }

    public FtpNode createNode(String path, boolean isDir) {
        FtpNode node = new FtpNode(isDir);
        tree.put(path, node);
        return node;
    }

    @Override
    public FileSystemView createFileSystemView(User user) {
        return new FileSystemView2(this, user);
    }
    
}

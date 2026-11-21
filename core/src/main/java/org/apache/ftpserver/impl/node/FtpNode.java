package org.apache.ftpserver.impl.node;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.channels.FileChannel;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;

public class FtpNode {

    public final boolean isDir;

    public long size;
    public long mtimeSec = System.currentTimeMillis() / 1000L;

    public FtpNode(boolean isDir) {
        this.isDir = isDir;
    }

    public InputStream openR() throws IOException {
        return null;
    }

    public RandomAccessFile openW() throws IOException {
        return null;
    }

    public void close() throws IOException {
        // NOP
    }

}

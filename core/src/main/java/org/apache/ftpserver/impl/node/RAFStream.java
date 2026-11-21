package org.apache.ftpserver.impl.node;

import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

public class RAFStream extends OutputStream {

    private final FtpNode node;
    private final RandomAccessFile raf;

    public RAFStream(FtpNode node, RandomAccessFile raf) {
        this.node = node;
        this.raf = raf;
    }

    @Override
    public void write(int b) throws IOException {
        raf.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        raf.write(b, off, len);
    }

    @Override
    public void close() throws IOException {
        try {
            raf.close();
        } finally {
            node.close();
        }
    }

}

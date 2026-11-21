package org.apache.ftpserver.impl.node;

import static org.apache.ftpserver.util.PathUtils.resolve;
import static org.apache.ftpserver.util.PathUtils.norm;
import org.apache.ftpserver.ftplet.FtpFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FtpFile2 implements FtpFile {

    public final FileSystemFactory2 factory;
    public final NodeTree tree;
    public final FileSystemView2 view;
    public final String path;

    public FtpFile2(FileSystemView2 view, String path) {
        this.factory = view.factory;
        this.tree = factory.tree;
        this.view = view;
        this.path = path;
    }

    @Override
    public String getAbsolutePath() {
        return path;
    }

    @Override
    public String getName() {
        if ("/".equals(path)) return "/";
        int idx = path.lastIndexOf('/');
        return path.substring(idx + 1);
    }

    @Override
    public boolean isHidden() {
        return false;
    }

    @Override
    public boolean isDirectory() {
        FtpNode n = tree.get(path);
        return n != null && n.isDir;
    }

    @Override
    public boolean isFile() {
        FtpNode n = tree.get(path);
        return n != null && !n.isDir;
    }

    @Override
    public boolean doesExist() {
        return tree.get(path) != null;
    }

    @Override
    public boolean isReadable() {
        return doesExist();
    }

    @Override
    public boolean isWritable() {
        return !"/".equals(path);
    }

    @Override
    public boolean isRemovable() {
        if ("/".equals(path) || !doesExist()) return false;
        if (isDirectory()) {
            return listFiles().isEmpty();
        }
        return true;
    }

    @Override
    public String getOwnerName() {
        return "System";
    }

    @Override
    public String getGroupName() {
        return "System";
    }

    @Override
    public int getLinkCount() {
        return isDirectory() ? 2 : 1;
    }

    @Override
    public long getLastModified() {
        FtpNode n = tree.get(path);
        return n != null ? n.mtimeSec * 1000L : 0L;
    }

    @Override
    public boolean setLastModified(long time) {
        FtpNode n = tree.get(path);
        if (n == null) return false;
        n.mtimeSec = time / 1000L;
        return true;
    }

    @Override
    public long getSize() {
        FtpNode n = tree.get(path);
        return n != null ? n.size : 0L;
    }

    @Override
    public Object getPhysicalFile() {
        return path;
    }

    @Override
    public boolean mkdir() {
        if (doesExist()) return false;
        factory.createNode(path, true);
        return true;
    }

    @Override
    public boolean delete() {
        if (!isRemovable()) return false;
        tree.remove(path);
        return true;
    }

    @Override
    public boolean move(FtpFile destination) {
        String normOld = this.path;
        String normNew = norm(destination.getAbsolutePath());

        FtpNode n = tree.remove(normOld);
        if (n == null) return false;

        tree.put(normNew, n);

        if (n.isDir) {
            String oldPrefix = normOld + "/";
            String newPrefix = normNew + "/";
            List<String> children = new ArrayList<>();
            for (String key : tree.keySet()) {
                if (key.startsWith(oldPrefix)) children.add(key);
            }
            for (String childKey : children) {
                String newChildPath = newPrefix + childKey.substring(oldPrefix.length());
                FtpNode child = tree.remove(childKey);
                tree.put(newChildPath, child);
            }
        }
        return true;
    }

    @Override
    public List<FtpFile> listFiles() {
        List<FtpFile> results = new ArrayList<>();
        FtpNode n = tree.get(path);
        if (n == null || !n.isDir) return results;

        for (String key : tree.keySet()) {
            if (!key.equals("/") && resolve(key, "..").equals(path)) {
                results.add(new FtpFile2(view, key));
            }
        }
        return results;
    }

    @Override
    public InputStream createInputStream(long offset) throws IOException {
        FtpNode n = tree.get(path);
        if (n == null || n.isDir)
            throw new FileNotFoundException(path);

        InputStream in = n.openR();
        if (offset > 0 && in.skip(offset) < offset) {
            in.close();
            throw new IOException("Unable to seek to offset " + offset);
        }
        return in;
    }

    @Override
    public OutputStream createOutputStream(long offset) throws IOException {
        FtpNode n = tree.get(path);
        if (n == null) {
            n = factory.createNode(path, false);
        } else if (n.isDir) {
            throw new IOException("Cannot open directory for writing: " + path);
        }

        RandomAccessFile raf = n.openW();
        if (offset > 0)
            raf.seek(offset);

        return new RAFStream(n, raf);
    }

}

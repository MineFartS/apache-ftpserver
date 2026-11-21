package org.apache.ftpserver.impl.node;

import static org.apache.ftpserver.util.PathUtils.norm;

import java.util.concurrent.ConcurrentHashMap;

public class NodeTree extends ConcurrentHashMap<String, FtpNode> {

    @Override
    public FtpNode get(Object key) {
        return super.get(norm((String) key));
    }

    @Override
    public FtpNode put(String key, FtpNode val) {
        return super.put(norm(key), val);
    }

    @Override
    public FtpNode remove(Object key) {
        return super.remove(norm((String) key));
    }

    @Override
    public boolean containsKey(Object key) {
        return super.containsKey(norm((String) key));
    }
    
}

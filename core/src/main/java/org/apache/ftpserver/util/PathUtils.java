package org.apache.ftpserver.util;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Deque;

public final class PathUtils {

    public static String norm(String path) {
        String p = path.replace('\\', '/');
        if (p.length() > 1 && p.endsWith("/")) {
            p = p.substring(0, p.length() - 1);
        }
        return p.isEmpty() ? "/" : p;
    }

    public static String resolve(String cwd, String input) {
        String p = norm(input);
        String combined = p.startsWith("/") ? p : cwd + "/" + p;
        String resolved = java.net.URI.create(combined).normalize().getPath();
        return (resolved == null || resolved.isEmpty()) ? "/" : resolved;
    }

}

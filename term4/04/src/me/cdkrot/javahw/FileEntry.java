package me.cdkrot.javahw;

public class FileEntry {
    public String path;
    public boolean isDir;

    public FileEntry() {}
    public FileEntry(String s, boolean b) {
        path = s;
        isDir = b;
    }

    public String toString() {
        return path;
    }
}

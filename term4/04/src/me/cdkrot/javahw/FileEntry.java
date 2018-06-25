package me.cdkrot.javahw;

/**
 * Represents a filesystem entry.
 */
public class FileEntry {
    /**
     * entry path
     */
    public String path;

    /**
     * set true if the respected entry is directory
     */
    public boolean isDir;

    /**
     * Constructs an unitialized Entry
     */
    public FileEntry() {}

    /**
     * Constructs an entry with specified data
     * @param path the path to set
     * @param isDir is the entry a directory
     */
    public FileEntry(String path, boolean isDir) {
        this.path = path;
        this.isDir = isDir;
    }

    @Override
    public String toString() {
        return path;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof FileEntry))
            return false;

        FileEntry entry = (FileEntry)other;
        return path.equals(entry.path) && isDir == entry.isDir;
    }
} 

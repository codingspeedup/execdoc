package io.github.codingspeedup.execdoc.apps.logminer;

import java.io.File;
import java.io.FileFilter;

public class LastModifiedFileFilter implements FileFilter {

    private final Long from;
    private final Long to;

    public LastModifiedFileFilter(Long from, Long to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean accept(File pathname) {
        if (from != null && pathname.lastModified() < from) {
            return false;
        }
        return to == null || pathname.lastModified() <= to;
    }

}

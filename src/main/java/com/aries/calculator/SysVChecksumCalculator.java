package com.aries.calculator;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

public class SysVChecksumCalculator extends AbstractChecksumCalculator {

    @Override
    public String calculateChecksumAndBlockSize(String file) throws IOException {
        Path path = file.equals("-") ? Paths.get("/dev/stdin") : Paths.get(file);
        int s = 0;
        long totalBytes = 0;
        byte[] buffer = new byte[8192];

        try (InputStream in = Files.newInputStream(path)) {
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                for (int i = 0; i < bytesRead; i++) {
                    s += buffer[i] & 0xff;
                }
                totalBytes += bytesRead;
            }
        }

        int r = (s & 0xffff) + ((s) >> 16);
        int checksum = (r & 0xffff) + (r >> 16);
        long blockSize = (totalBytes + 511) / 512;
        return String.format(Locale.ROOT, "%d %5d %s", checksum, blockSize, file);
    }
}
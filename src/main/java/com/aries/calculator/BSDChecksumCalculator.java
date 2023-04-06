package com.aries.calculator;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

public class BSDChecksumCalculator extends AbstractChecksumCalculator {

    @Override
    public String calculateChecksumAndBlockSize(String file) throws IOException {
        Path path = file.equals("-") ? Paths.get("/dev/stdin") : Paths.get(file);
        int checksum = 0;
        long totalBytes = 0;
        int ch;

        try (InputStream in = Files.newInputStream(path)) {
            while ((ch = in.read()) != -1) {
                totalBytes++;
                checksum = (checksum >> 1) + ((checksum & 1) << 15);
                checksum += ch;
                checksum &= 0xffff;
            }
        }

        long blockSize = (totalBytes + 1023) / 1024;
        return String.format(Locale.ROOT, "%05d %5d %s", checksum, blockSize, file);
    }
}
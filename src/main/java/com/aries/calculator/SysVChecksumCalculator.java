package com.aries.calculator;

import java.io.IOException;
import java.io.InputStream;

public class SysVChecksumCalculator extends AbstractChecksumCalculator {
    @Override
    protected String getAlgorithmName() {
        return "SysV";
    }

    @Override
    protected ChecksumResult calculateChecksumAndTotalBytes(InputStream in) throws IOException {
        int s = 0;
        long totalBytes = 0;
        byte[] buffer = new byte[8192];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) {
            for (int i = 0; i < bytesRead; i++) {
                s += buffer[i] & 0xff;
            }
            totalBytes += bytesRead;
        }

        int r = (s & 0xffff) + (s >> 16);
        int checksum = (r & 0xffff) + (r >> 16);

        return new ChecksumResult(checksum, totalBytes);
    }

    @Override
    protected int getBlockSizeAdjustment() {
        return 512;
    }
}

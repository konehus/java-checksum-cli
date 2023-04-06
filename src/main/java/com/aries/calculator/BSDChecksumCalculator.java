package com.aries.calculator;

import java.io.IOException;
import java.io.InputStream;

public class BSDChecksumCalculator extends AbstractChecksumCalculator {

    @Override
    protected String getAlgorithmName() {
        return "BSD";
    }
    @Override
    protected ChecksumResult calculateChecksumAndTotalBytes(InputStream in) throws IOException {
        int checksum = 0;
        long totalBytes = 0;
        int ch;

        while ((ch = in.read()) != -1) {
            totalBytes++;
            checksum = (checksum >> 1) + ((checksum & 1) << 15);
            checksum += ch;
            checksum &= 0xffff;
        }

        return new ChecksumResult(checksum, totalBytes);
    }

    @Override
    protected int getBlockSizeAdjustment() {
        return 1024;
    }
}

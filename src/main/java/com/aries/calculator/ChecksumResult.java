package com.aries.calculator;

class ChecksumResult {
    private final int checksum;
    private final long totalBytes;

    public ChecksumResult(int checksum, long totalBytes) {
        this.checksum = checksum;
        this.totalBytes = totalBytes;
    }

    public int getChecksum() {
        return checksum;
    }

    public long getTotalBytes() {
        return totalBytes;
    }
}

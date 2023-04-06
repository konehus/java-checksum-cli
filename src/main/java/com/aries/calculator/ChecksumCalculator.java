package com.aries.calculator;

import java.io.IOException;

public interface ChecksumCalculator {
    String calculateChecksumAndBlockSize(String file) throws IOException;
}
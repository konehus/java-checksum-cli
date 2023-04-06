/*
 * Checksum CLI - Calculates the checksum and block count for the provided files
 * using either the BSD or System V algorithm, depending on the given option.
 *
 * Copyright (C) 2023 Aries
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * Copy of the GNU General Public License, see <https://www.gnu.org/licenses/>.
 */

package com.aries;

import com.aries.calculator.BSDChecksumCalculator;
import com.aries.calculator.ChecksumCalculator;
import com.aries.calculator.SysVChecksumCalculator;

import java.io.IOException;

public class CheckSumCLI {

    // private static final String PROGRAM_NAME = "sum";
    // private static final String[] AUTHORS = {"Henok Haile"};
    // private static final String VERSION = "1.0";

    public static void main(String[] args) {
        CheckSumCLI cli = new CheckSumCLI();
        cli.run(args);
    }

    public void run(String[] args) {
        ChecksumCalculator calculator;
        boolean sysv = false;

        if (args.length == 0) {
            printUsageAndExit();
        }

        int fileArgStartIndex = 0;

        for (String arg : args) {
            if (arg.equals("-s") || arg.equals("--sysv")) {
                sysv = true;
                fileArgStartIndex++;
            } else if (arg.equals("-r") || arg.equals("--bsd")) {
                sysv = false;
                fileArgStartIndex++;
            } else {
                break;
            }
        }

        calculator = sysv ? new SysVChecksumCalculator() : new BSDChecksumCalculator();

        try {
            for (int i = fileArgStartIndex; i < args.length; i++) {
                System.out.println(calculator.calculateChecksumAndBlockSize(args[i]));
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }

    private void printUsageAndExit() {
        System.err.println("Usage: java ChecksumCLI [OPTION]... [FILE]...");
        System.err.println("  -r      use BSD sum algorithm, use 1K blocks");
        System.err.println("  -s, --sysv  use System V sum algorithm, use 512 bytes blocks");
        System.exit(1);
    }
}

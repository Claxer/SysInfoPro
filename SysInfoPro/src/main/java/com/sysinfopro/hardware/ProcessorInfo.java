package com.sysinfopro.hardware;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;

import java.text.DecimalFormat;

public class ProcessorInfo {

    private final CentralProcessor processor;
    private final DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

    public ProcessorInfo() {

        SystemInfo systemInfo = new SystemInfo();
        processor = systemInfo.getHardware().getProcessor();
    }

    // CPU NAME

    public String getProcessorName() {

        return processor.getProcessorIdentifier().getName();
    }

    // CPU MANUFACTURER

    public String getManufacturer() {

        return processor.getProcessorIdentifier().getVendor();
    }

    // CPU IDENTIFIER

    public String getIdentifier() {

        return processor.getProcessorIdentifier().getIdentifier();
    }

    // CPU ARCHITECTURE

    public String getArchitecture() {

        return processor.getProcessorIdentifier().isCpu64bit()
                ? "64-bit"
                : "32-bit";
    }

    // PHYSICAL CORES

    public int getPhysicalCores() {

        return processor.getPhysicalProcessorCount();
    }

    // LOGICAL CORES

    public int getLogicalCores() {

        return processor.getLogicalProcessorCount();
    }

    // PROCESSOR FAMILY

    public String getFamily() {

        return processor.getProcessorIdentifier().getFamily();
    }

    // PROCESSOR MODEL

    public String getModel() {

        return processor.getProcessorIdentifier().getModel();
    }

    // PROCESSOR STEPPING

    public String getStepping() {

        return processor.getProcessorIdentifier().getStepping();
    }

    // MAXIMUM FREQUENCY

    public String getMaximumFrequency() {

        long frequency = processor.getMaxFreq();

        if (frequency <= 0) {
            return "Not available";
        }

        return decimalFormat.format(frequency / 1_000_000_000.0) + " GHz";
    }

    // CURRENT AVERAGE FREQUENCY

    public String getCurrentFrequency() {

        long[] frequencies = processor.getCurrentFreq();

        if (frequencies == null || frequencies.length == 0) {
            return "Not available";
        }

        long total = 0;
        int count = 0;

        for (long frequency : frequencies) {

            if (frequency > 0) {
                total += frequency;
                count++;
            }
        }

        if (count == 0) {
            return "Not available";
        }

        double average = (double) total / count;

        return decimalFormat.format(
                average / 1_000_000_000.0
        ) + " GHz";
    }

    // PROCESSOR SUMMARY

    public String getProcessorSummary() {

        return getProcessorName()
                + " | "
                + getPhysicalCores()
                + " Cores | "
                + getLogicalCores()
                + " Threads";
    }
}

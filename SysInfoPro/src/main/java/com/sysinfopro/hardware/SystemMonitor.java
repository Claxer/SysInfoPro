package com.sysinfopro.hardware;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

import java.io.File;
import java.text.DecimalFormat;

public class SystemMonitor {

    private final SystemInfo systemInfo;
    private final HardwareAbstractionLayer hardware;
    private final CentralProcessor processor;
    private final GlobalMemory memory;
    private final OperatingSystem operatingSystem;

    private long[] previousTicks;

    private final DecimalFormat decimalFormat =
            new DecimalFormat("#,##0.00");

    public SystemMonitor() {

        systemInfo = new SystemInfo();

        hardware = systemInfo.getHardware();

        processor = hardware.getProcessor();

        memory = hardware.getMemory();

        operatingSystem = systemInfo.getOperatingSystem();

        previousTicks = processor.getSystemCpuLoadTicks();
    }

    // ==============================
    // CPU INFORMATION
    // ==============================

    public String getCPUName() {

        return processor.getProcessorIdentifier().getName();
    }

    public int getPhysicalCores() {

        return processor.getPhysicalProcessorCount();
    }

    public int getLogicalCores() {

        return processor.getLogicalProcessorCount();
    }

    public double getCPUUsage() {

        double usage = processor.getSystemCpuLoadBetweenTicks(
                previousTicks
        ) * 100;

        previousTicks = processor.getSystemCpuLoadTicks();

        return usage;
    }

    // ==============================
    // MEMORY INFORMATION
    // ==============================

    public double getTotalRAM() {

        return memory.getTotal() / (1024.0 * 1024 * 1024);
    }

    public double getAvailableRAM() {

        return memory.getAvailable() / (1024.0 * 1024 * 1024);
    }

    public double getUsedRAM() {

        return getTotalRAM() - getAvailableRAM();
    }

    public double getRAMUsage() {

        if (memory.getTotal() == 0) {
            return 0;
        }

        return (double) memory.getTotal() /
                memory.getTotal() * 100
                * getUsedRAM() / getTotalRAM();
    }

    // ==============================
    // STORAGE INFORMATION
    // ==============================

    public double getTotalStorage() {

        double total = 0;

        for (File root : File.listRoots()) {
            total += root.getTotalSpace();
        }

        return total / (1024.0 * 1024 * 1024);
    }

    public double getFreeStorage() {

        double free = 0;

        for (File root : File.listRoots()) {
            free += root.getUsableSpace();
        }

        return free / (1024.0 * 1024 * 1024);
    }

    public double getUsedStorage() {

        return getTotalStorage() - getFreeStorage();
    }

    public double getStorageUsage() {

        double total = getTotalStorage();

        if (total == 0) {
            return 0;
        }

        return (getUsedStorage() / total) * 100;
    }

    // ==============================
    // OPERATING SYSTEM
    // ==============================

    public String getOSName() {

        return operatingSystem.toString();
    }

    public String getOSVersion() {

        return operatingSystem.getVersionInfo().getVersion();
    }

    public String getOSArchitecture() {

        return System.getProperty("os.arch");
    }

    public String getComputerName() {

        return System.getProperty("user.name");
    }

    // ==============================
    // SYSTEM UPTIME
    // ==============================

    public String getSystemUptime() {

        long seconds = operatingSystem.getSystemUptime();

        long days = seconds / 86400;
        long hours = (seconds % 86400) / 3600;
        long minutes = (seconds % 3600) / 60;

        return days + "d " + hours + "h " + minutes + "m";
    }

    // ==============================
    // FORMATTING
    // ==============================

    public String formatGB(double value) {

        return decimalFormat.format(value) + " GB";
    }

    public String formatPercentage(double value) {

        return decimalFormat.format(value) + "%";
    }

}

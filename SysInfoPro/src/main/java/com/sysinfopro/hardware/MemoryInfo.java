package com.sysinfopro.hardware;

import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;

public class MemoryInfo {

    private final GlobalMemory memory;

    public MemoryInfo() {
        SystemInfo systemInfo = new SystemInfo();
        memory = systemInfo.getHardware().getMemory();
    }

    // Get total installed RAM
    public long getTotalRAM() {
        return memory.getTotal();
    }

    // Get available RAM
    public long getAvailableRAM() {
        return memory.getAvailable();
    }

    // Get used RAM
    public long getUsedRAM() {
        return getTotalRAM() - getAvailableRAM();
    }

    // Get RAM usage percentage
    public double getRAMUsage() {
        long total = getTotalRAM();

        if (total == 0) {
            return 0;
        }

        return ((double) getUsedRAM() / total) * 100;
    }

    // Convert bytes to gigabytes
    public String formatGB(long bytes) {
        double gb = bytes / (1024.0 * 1024.0 * 1024.0);
        return String.format("%.2f GB", gb);
    }

    // Get a complete memory summary
    public String getMemorySummary() {
        return "Total RAM: " + formatGB(getTotalRAM()) +
                "\nUsed RAM: " + formatGB(getUsedRAM()) +
                "\nAvailable RAM: " + formatGB(getAvailableRAM()) +
                "\nRAM Usage: " + String.format("%.1f%%", getRAMUsage());
    }
}

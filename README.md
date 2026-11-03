# SysInfo Pro — System Information Manager

SysInfo Pro is a modern desktop System Information and Monitoring application built with **Java, Java Swing, and OSHI**. It provides users with an overview of their computer's hardware, system specifications, and resource utilization through a clean, minimalist dark-themed interface.

The application is currently **under active development**, with additional monitoring modules, hardware information, database integration, and reporting features planned.

## Features

- **System Dashboard** — View an overview of your computer's hardware and system performance.
- **CPU Monitoring** — Monitor processor utilization and view CPU information.
- **RAM Monitoring** — Display memory utilization, total installed RAM, used RAM, and available RAM.
- **Memory Monitor** — Dedicated page displaying RAM statistics, utilization percentage, and a visual progress bar.
- **Storage Monitoring** — Monitor total storage capacity and disk utilization.
- **Processor Information** — View processor model, manufacturer, physical cores, logical processors, architecture, and other specifications.
- **CPU Frequency Information** — Display maximum and current average processor frequency when supported.
- **Operating System Information** — Display operating system details and system uptime.
- **Automatic Refresh** — Update CPU, RAM, and storage statistics every two seconds.
- **Navigation Sidebar** — Switch between the application's different monitoring sections.
- **Dark Interface** — Minimalist charcoal and black desktop interface.

## Technologies Used

- **Java 25** — Main programming language.
- **Java Swing** — Desktop graphical user interface.
- **OSHI** — Hardware and operating system information.
- **SQLite** — Database integration (planned).
- **JFreeChart** — Charts and performance visualization (planned).
- **Maven** — Project and dependency management.

## Project Structure

```text
SysInfoPro/
│
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── sysinfopro/
│       │           │
│       │           ├── Main.java
│       │           │
│       │           ├── ui/
│       │           │
│       │           ├── core/
│       │           │
│       │           ├── hardware/
│       │           │   ├── SystemMonitor.java
│       │           │   ├── ProcessorInfo.java
│       │           │   └── MemoryInfo.java
│       │           │
│       │           ├── database/
│       │           │
│       │           ├── reports/
│       │           │
│       │           └── utils/
│       │
│       └── resources/
│
├── reports/
│
├── pom.xml
│
└── README.md
```

## Current Development Progress

### Completed

- Initial Maven project setup.
- Project folder structure.
- Dark-themed desktop application interface.
- Navigation sidebar.
- System Dashboard.
- CPU usage monitoring.
- RAM usage monitoring.
- Storage usage monitoring.
- Automatic hardware information refresh.
- CPU model and manufacturer information.
- Physical and logical processor core information.
- CPU architecture information.
- Maximum and current average CPU frequency.
- Processor family, model, stepping, and identifier.
- Operating system information.
- System uptime display.
- Dedicated Processor page.
- Dedicated Memory Monitor page.
- Total, used, and available RAM information.
- RAM utilization percentage calculation.
- Visual memory usage progress bar.
- Automatic memory statistics refresh.

### In Development

- Detailed Storage Monitor.
- Operating System information page.
- Additional Hardware Information page.
- System Reports module.
- SQLite database integration.
- Performance charts and visualizations.

## How to Run

**Requirements:**
- Java Development Kit (JDK) 25
- Apache Maven
- Visual Studio Code

**1. Clone or download the project.**

**2. Open the project folder in Visual Studio Code.**

**3. Install Maven dependencies:**

```bash
mvn clean install
```

**4. Run the application:**

```bash
mvn clean compile exec:java
```

## Project Goals

SysInfo Pro aims to provide a convenient way to understand and monitor computer hardware and system performance. It is also designed as a learning project for exploring Java desktop development, hardware monitoring, database management, and system information retrieval.

## Development Status

**Version:** 1.0.0

**Status:** Under Active Development

New features, improvements, and additional monitoring modules are planned as development continues.

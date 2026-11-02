package com.sysinfopro;

import com.sysinfopro.hardware.SystemMonitor;
import com.sysinfopro.hardware.ProcessorInfo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Main {

    private JFrame frame;
    private JPanel contentPanel;
    private JPanel sidebar;

    private SystemMonitor monitor;
    private ProcessorInfo processorInfo;
    private Timer refreshTimer;

    private JLabel cpuValue;
    private JLabel ramValue;
    private JLabel storageValue;

    private JLabel cpuModel;
    private JLabel ramDetails;
    private JLabel storageDetails;
    private JLabel osDetails;
    private JLabel uptimeDetails;

    private JProgressBar cpuBar;
    private JProgressBar ramBar;
    private JProgressBar storageBar;

    private final Color BACKGROUND = new Color(18, 18, 18);
    private final Color SIDEBAR = new Color(23, 23, 23);
    private final Color PANEL = new Color(30, 30, 30);
    private final Color BORDER = new Color(48, 48, 48);
    private final Color WHITE = new Color(240, 240, 240);
    private final Color GRAY = new Color(155, 155, 155);
    private final Color ACCENT = new Color(110, 160, 255);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().initialize());
    }

    private void initialize() {

        monitor = new SystemMonitor();
        processorInfo = new ProcessorInfo();

        frame = new JFrame("SysInfo Pro");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1250, 780);
        frame.setMinimumSize(new Dimension(1050, 650));
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(BACKGROUND);
        frame.setLayout(new BorderLayout());

        createSidebar();
        createContentPanel();

        frame.add(sidebar, BorderLayout.WEST);
        frame.add(contentPanel, BorderLayout.CENTER);

        showDashboard();

        frame.setVisible(true);

        refreshTimer = new Timer(2000, e -> refreshDashboard());
        refreshTimer.start();

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                refreshTimer.stop();
            }
        });
    }

    // ==============================
    // SIDEBAR
    // ==============================

    private void createSidebar() {

        sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(230, 0));
        sidebar.setBackground(SIDEBAR);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(25, 15, 20, 15));

        JLabel logo = new JLabel("SYSINFO PRO");
        logo.setFont(new Font("SansSerif", Font.BOLD, 20));
        logo.setForeground(WHITE);
        logo.setBorder(new EmptyBorder(5, 10, 5, 5));

        JLabel subtitle = new JLabel("SYSTEM MONITOR");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 10));
        subtitle.setForeground(GRAY);
        subtitle.setBorder(new EmptyBorder(5, 10, 30, 5));

        sidebar.add(logo);
        sidebar.add(subtitle);

        addNavigationButton("Dashboard", "dashboard");
        addNavigationButton("Processor", "processor");
        addNavigationButton("Memory", "memory");
        addNavigationButton("Storage", "storage");
        addNavigationButton("Operating System", "os");
        addNavigationButton("Hardware", "hardware");

        sidebar.add(Box.createVerticalGlue());

        addNavigationButton("Reports", "reports");

        JLabel footer = new JLabel("SysInfo Pro v1.0.0");
        footer.setForeground(GRAY);
        footer.setFont(new Font("SansSerif", Font.PLAIN, 11));
        footer.setBorder(new EmptyBorder(20, 10, 5, 5));

        sidebar.add(footer);
    }

    private void addNavigationButton(String text, String page) {

        JButton button = new JButton(text);

        button.setFont(new Font("SansSerif", Font.PLAIN, 13));
        button.setForeground(GRAY);
        button.setBackground(SIDEBAR);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(13, 12, 13, 12));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addActionListener(e -> navigate(page));

        sidebar.add(button);
        sidebar.add(Box.createVerticalStrut(5));
    }

    // ==============================
    // CONTENT PANEL
    // ==============================

    private void createContentPanel() {

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(BACKGROUND);
        contentPanel.setBorder(new EmptyBorder(30, 35, 30, 35));
    }

    // ==============================
    // NAVIGATION
    // ==============================

    private void navigate(String page) {

        switch (page) {

            case "dashboard" -> showDashboard();

            case "processor" -> showProcessorPage();

            case "memory" -> showPlaceholder(
                    "Memory",
                    "RAM usage and memory statistics."
            );

            case "storage" -> showPlaceholder(
                    "Storage",
                    "Disk drives and storage capacity."
            );

            case "os" -> showPlaceholder(
                    "Operating System",
                    "Operating system information and uptime."
            );

            case "hardware" -> showPlaceholder(
                    "Hardware",
                    "Detailed computer hardware specifications."
            );

            case "reports" -> showPlaceholder(
                    "Reports",
                    "Generate and manage system reports."
            );
        }
    }

    // ==============================
    // DASHBOARD
    // ==============================

    private void showDashboard() {

        contentPanel.removeAll();

        JPanel dashboard = new JPanel();
        dashboard.setBackground(BACKGROUND);
        dashboard.setLayout(new BoxLayout(dashboard, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("System Dashboard");
        title.setFont(new Font("SansSerif", Font.BOLD, 27));
        title.setForeground(WHITE);

        JLabel description = new JLabel(
                "Monitor your computer's performance and hardware."
        );
        description.setFont(new Font("SansSerif", Font.PLAIN, 13));
        description.setForeground(GRAY);

        dashboard.add(title);
        dashboard.add(Box.createVerticalStrut(8));
        dashboard.add(description);
        dashboard.add(Box.createVerticalStrut(30));

        JPanel stats = new JPanel(new GridLayout(1, 3, 15, 15));
        stats.setBackground(BACKGROUND);
        stats.setMaximumSize(new Dimension(Integer.MAX_VALUE, 165));

        stats.add(createResourceCard("PROCESSOR", "CPU Usage", 0));
        stats.add(createResourceCard("MEMORY", "RAM Usage", 1));
        stats.add(createResourceCard("STORAGE", "Disk Usage", 2));

        dashboard.add(stats);
        dashboard.add(Box.createVerticalStrut(25));

        JPanel overview = createPanel();
        overview.setLayout(new BorderLayout());
        overview.setBorder(new EmptyBorder(22, 22, 22, 22));

        JLabel overviewTitle = new JLabel("System Overview");
        overviewTitle.setForeground(WHITE);
        overviewTitle.setFont(new Font("SansSerif", Font.BOLD, 18));

        JPanel details = new JPanel(new GridLayout(0, 2, 20, 22));
        details.setBackground(PANEL);
        details.setBorder(new EmptyBorder(20, 0, 0, 0));

        cpuModel = createDetailLabel();
        ramDetails = createDetailLabel();
        storageDetails = createDetailLabel();
        osDetails = createDetailLabel();
        uptimeDetails = createDetailLabel();

        details.add(createDetailItem("PROCESSOR MODEL", cpuModel));
        details.add(createDetailItem("INSTALLED MEMORY", ramDetails));
        details.add(createDetailItem("TOTAL STORAGE", storageDetails));
        details.add(createDetailItem("OPERATING SYSTEM", osDetails));
        details.add(createDetailItem("SYSTEM UPTIME", uptimeDetails));

        overview.add(overviewTitle, BorderLayout.NORTH);
        overview.add(details, BorderLayout.CENTER);

        dashboard.add(overview);

        contentPanel.add(dashboard, BorderLayout.CENTER);

        contentPanel.revalidate();
        contentPanel.repaint();

        refreshDashboard();
    }

    // ==============================
    // RESOURCE CARDS
    // ==============================

    private JPanel createResourceCard(
            String title,
            String description,
            int type
    ) {

        JPanel card = createPanel();

        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel heading = new JLabel(title);
        heading.setFont(new Font("SansSerif", Font.BOLD, 11));
        heading.setForeground(GRAY);

        JLabel value = new JLabel("0%");
        value.setFont(new Font("SansSerif", Font.BOLD, 27));
        value.setForeground(WHITE);

        JLabel subtitle = new JLabel(description);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 11));
        subtitle.setForeground(GRAY);

        JProgressBar progress = new JProgressBar(0, 100);
        progress.setValue(0);
        progress.setStringPainted(false);
        progress.setForeground(ACCENT);
        progress.setBackground(new Color(48, 48, 48));
        progress.setBorderPainted(false);
        progress.setPreferredSize(new Dimension(200, 7));
        progress.setMaximumSize(new Dimension(Integer.MAX_VALUE, 7));

        if (type == 0) {
            cpuValue = value;
            cpuBar = progress;
        } else if (type == 1) {
            ramValue = value;
            ramBar = progress;
        } else {
            storageValue = value;
            storageBar = progress;
        }

        card.add(heading);
        card.add(Box.createVerticalStrut(12));
        card.add(value);
        card.add(Box.createVerticalStrut(8));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(15));
        card.add(progress);

        return card;
    }

    // ==============================
    // SYSTEM DETAILS
    // ==============================

    private JPanel createDetailItem(String title, JLabel value) {

        JPanel item = new JPanel();
        item.setBackground(PANEL);
        item.setLayout(new BoxLayout(item, BoxLayout.Y_AXIS));

        JLabel heading = new JLabel(title);
        heading.setFont(new Font("SansSerif", Font.BOLD, 10));
        heading.setForeground(GRAY);

        value.setFont(new Font("SansSerif", Font.PLAIN, 13));
        value.setForeground(WHITE);

        item.add(heading);
        item.add(Box.createVerticalStrut(7));
        item.add(value);

        return item;
    }

    private JLabel createDetailLabel() {
        return new JLabel("Loading...");
    }

    // ==============================
    // REFRESH HARDWARE DATA
    // ==============================

    private void refreshDashboard() {

        if (cpuValue == null || ramValue == null || storageValue == null) {
            return;
        }

        try {

            double cpu = monitor.getCPUUsage();
            double ram = monitor.getRAMUsage();
            double storage = monitor.getStorageUsage();

            updateResource(cpuValue, cpuBar, cpu);
            updateResource(ramValue, ramBar, ram);
            updateResource(storageValue, storageBar, storage);

            cpuModel.setText(monitor.getCPUName());

            ramDetails.setText(
                    monitor.formatGB(monitor.getTotalRAM())
            );

            storageDetails.setText(
                    monitor.formatGB(monitor.getTotalStorage())
            );

            osDetails.setText(monitor.getOSName());

            uptimeDetails.setText(monitor.getSystemUptime());

        } catch (Exception ex) {

            System.err.println(
                    "Unable to refresh system information: " + ex.getMessage()
            );
        }
    }

    private void updateResource(
            JLabel label,
            JProgressBar bar,
            double usage
    ) {

        int percentage = (int) Math.round(usage);

        percentage = Math.max(0, Math.min(100, percentage));

        label.setText(percentage + "%");
        bar.setValue(percentage);
    }

    // ==============================
    // PROCESSOR PAGE
    // ==============================

    private void showProcessorPage() {

        contentPanel.removeAll();

        JPanel page = new JPanel();
        page.setBackground(BACKGROUND);
        page.setLayout(new BoxLayout(page, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Processor");
        title.setFont(new Font("SansSerif", Font.BOLD, 27));
        title.setForeground(WHITE);

        JLabel description = new JLabel(
                "Detailed processor specifications and hardware information."
        );
        description.setFont(new Font("SansSerif", Font.PLAIN, 13));
        description.setForeground(GRAY);

        page.add(title);
        page.add(Box.createVerticalStrut(8));
        page.add(description);
        page.add(Box.createVerticalStrut(25));

        JPanel overview = createPanel();
        overview.setLayout(new BoxLayout(overview, BoxLayout.Y_AXIS));
        overview.setBorder(new EmptyBorder(22, 22, 22, 22));

        JLabel overviewTitle = new JLabel("Processor Overview");
        overviewTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        overviewTitle.setForeground(WHITE);

        JLabel processorName = new JLabel(
                processorInfo.getProcessorName()
        );
        processorName.setFont(new Font("SansSerif", Font.BOLD, 17));
        processorName.setForeground(ACCENT);

        JLabel manufacturer = new JLabel(
                "Manufacturer: " + processorInfo.getManufacturer()
        );
        manufacturer.setFont(new Font("SansSerif", Font.PLAIN, 13));
        manufacturer.setForeground(GRAY);

        overview.add(overviewTitle);
        overview.add(Box.createVerticalStrut(20));
        overview.add(processorName);
        overview.add(Box.createVerticalStrut(10));
        overview.add(manufacturer);

        page.add(overview);
        page.add(Box.createVerticalStrut(20));

        JPanel specifications = createPanel();
        specifications.setLayout(new BorderLayout());
        specifications.setBorder(new EmptyBorder(22, 22, 22, 22));

        JLabel specificationsTitle = new JLabel(
                "Processor Specifications"
        );
        specificationsTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        specificationsTitle.setForeground(WHITE);

        JPanel details = new JPanel(new GridLayout(0, 2, 20, 22));
        details.setBackground(PANEL);
        details.setBorder(new EmptyBorder(22, 0, 0, 0));

        details.add(createDetailItem(
                "PHYSICAL CORES",
                new JLabel(String.valueOf(processorInfo.getPhysicalCores()))
        ));

        details.add(createDetailItem(
                "LOGICAL PROCESSORS",
                new JLabel(String.valueOf(processorInfo.getLogicalCores()))
        ));

        details.add(createDetailItem(
                "ARCHITECTURE",
                new JLabel(processorInfo.getArchitecture())
        ));

        details.add(createDetailItem(
                "MAXIMUM FREQUENCY",
                new JLabel(processorInfo.getMaximumFrequency())
        ));

        details.add(createDetailItem(
                "CURRENT AVERAGE FREQUENCY",
                new JLabel(processorInfo.getCurrentFrequency())
        ));

        details.add(createDetailItem(
                "PROCESSOR FAMILY",
                new JLabel(processorInfo.getFamily())
        ));

        details.add(createDetailItem(
                "PROCESSOR MODEL",
                new JLabel(processorInfo.getModel())
        ));

        details.add(createDetailItem(
                "STEPPING",
                new JLabel(processorInfo.getStepping())
        ));

        details.add(createDetailItem(
                "CPU IDENTIFIER",
                new JLabel(processorInfo.getIdentifier())
        ));

        specifications.add(specificationsTitle, BorderLayout.NORTH);
        specifications.add(details, BorderLayout.CENTER);

        page.add(specifications);

        contentPanel.add(page, BorderLayout.CENTER);

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // ==============================
    // GENERAL PANELS
    // ==============================

    private JPanel createPanel() {

        JPanel panel = new JPanel();

        panel.setBackground(PANEL);
        panel.setBorder(
                BorderFactory.createLineBorder(BORDER)
        );

        return panel;
    }

    // ==============================
    // PLACEHOLDER PAGES
    // ==============================

    private void showPlaceholder(
            String title,
            String description
    ) {

        contentPanel.removeAll();

        JPanel panel = new JPanel();
        panel.setBackground(BACKGROUND);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel heading = new JLabel(title);
        heading.setFont(new Font("SansSerif", Font.BOLD, 27));
        heading.setForeground(WHITE);

        JLabel subtitle = new JLabel(description);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitle.setForeground(GRAY);

        panel.add(heading);
        panel.add(Box.createVerticalStrut(10));
        panel.add(subtitle);

        contentPanel.add(panel, BorderLayout.CENTER);

        contentPanel.revalidate();
        contentPanel.repaint();
    }
}

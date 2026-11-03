package com.sysinfopro;

import com.sysinfopro.hardware.SystemMonitor;
import com.sysinfopro.hardware.ProcessorInfo;
import com.sysinfopro.hardware.MemoryInfo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.lang.reflect.Method;

public class Main extends JFrame {

    private final Color BACKGROUND = new Color(18, 18, 20);
    private final Color SIDEBAR = new Color(24, 24, 27);
    private final Color CARD = new Color(32, 32, 36);
    private final Color TEXT = new Color(240, 240, 240);
    private final Color MUTED = new Color(155, 155, 165);
    private final Color ACCENT = new Color(90, 145, 255);

    private final SystemMonitor monitor;
    private final ProcessorInfo processorInfo;
    private final MemoryInfo memoryInfo;

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contentPanel = new JPanel(cardLayout);

    private JLabel cpuValue;
    private JLabel ramValue;
    private JLabel storageValue;

    private JProgressBar cpuProgress;
    private JProgressBar ramProgress;
    private JProgressBar storageProgress;

    private JLabel overviewCPU;
    private JLabel overviewRAM;
    private JLabel overviewStorage;
    private JLabel overviewOS;
    private JLabel overviewUptime;

    private JLabel memoryTotal;
    private JLabel memoryUsed;
    private JLabel memoryAvailable;
    private JLabel memoryPercentage;
    private JProgressBar memoryProgress;

    private JTextArea processorDetails;

    private Timer refreshTimer;

    public Main() {
        monitor = new SystemMonitor();
        processorInfo = new ProcessorInfo();
        memoryInfo = new MemoryInfo();

        initialize();
    }

    private void initialize() {
        setTitle("SysInfo Pro - System Information Manager");
        setSize(1200, 750);
        setMinimumSize(new Dimension(1000, 650));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND);

        createSidebar();
        createContentPanel();

        refreshDashboard();
        refreshMemoryPage();
        refreshProcessorPage();

        refreshTimer = new Timer(2000, e -> {
            refreshDashboard();
            refreshMemoryPage();
            refreshProcessorPage();
        });

        refreshTimer.start();

        setVisible(true);
    }

    // SIDEBAR

    private void createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(230, 0));
        sidebar.setBackground(SIDEBAR);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(25, 15, 20, 15));

        JLabel logo = new JLabel("SysInfo Pro");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 23));
        logo.setForeground(TEXT);
        logo.setBorder(new EmptyBorder(0, 10, 35, 0));

        sidebar.add(logo);

        addNavigationButton(sidebar, "Dashboard", "dashboard");
        addNavigationButton(sidebar, "Processor", "processor");
        addNavigationButton(sidebar, "Memory", "memory");
        addNavigationButton(sidebar, "Storage", "storage");
        addNavigationButton(sidebar, "Operating System", "os");
        addNavigationButton(sidebar, "Hardware", "hardware");
        addNavigationButton(sidebar, "Reports", "reports");

        sidebar.add(Box.createVerticalGlue());

        JLabel version = new JLabel("Version 1.0.0");
        version.setForeground(MUTED);
        version.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        version.setBorder(new EmptyBorder(10, 10, 5, 0));

        sidebar.add(version);

        add(sidebar, BorderLayout.WEST);
    }

    private void addNavigationButton(
            JPanel sidebar, String name, String page) {

        JButton button = new JButton(name);

        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setForeground(TEXT);
        button.setBackground(SIDEBAR);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setMaximumSize(new Dimension(200, 45));
        button.setPreferredSize(new Dimension(200, 45));
        button.setBorder(new EmptyBorder(0, 15, 0, 0));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addActionListener(e -> navigate(page));

        sidebar.add(button);
        sidebar.add(Box.createVerticalStrut(5));
    }

    // CONTENT PANEL

    private void createContentPanel() {
        contentPanel.setBackground(BACKGROUND);

        contentPanel.add(showDashboard(), "dashboard");
        contentPanel.add(showProcessorPage(), "processor");
        contentPanel.add(showMemoryPage(), "memory");

        contentPanel.add(
                showPlaceholder("Storage Monitor",
                        "Storage monitoring will be available here."),
                "storage");

        contentPanel.add(
                showPlaceholder("Operating System",
                        "Operating system information will be available here."),
                "os");

        contentPanel.add(
                showPlaceholder("Hardware Information",
                        "Additional hardware information will be available here."),
                "hardware");

        contentPanel.add(
                showPlaceholder("Reports",
                        "System reports and export options will be available here."),
                "reports");

        add(contentPanel, BorderLayout.CENTER);
    }

    private void navigate(String page) {
        cardLayout.show(contentPanel, page);
    }

    // DASHBOARD

    private JPanel showDashboard() {
        JPanel panel = createPanel();
        panel.setLayout(new BorderLayout(20, 20));

        JPanel header = new JPanel();
        header.setBackground(BACKGROUND);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel(
                "Monitor your system performance in real time.");

        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(MUTED);

        header.add(title);
        header.add(Box.createVerticalStrut(5));
        header.add(subtitle);

        panel.add(header, BorderLayout.NORTH);

        JPanel cards = new JPanel(new GridLayout(1, 3, 15, 15));
        cards.setBackground(BACKGROUND);

        JPanel cpuCard = createResourceCard("CPU Usage");
        JPanel ramCard = createResourceCard("RAM Usage");
        JPanel storageCard = createResourceCard("Storage Usage");

        cpuValue = (JLabel) cpuCard.getClientProperty("value");
        cpuProgress = (JProgressBar) cpuCard.getClientProperty("progress");

        ramValue = (JLabel) ramCard.getClientProperty("value");
        ramProgress = (JProgressBar) ramCard.getClientProperty("progress");

        storageValue = (JLabel) storageCard.getClientProperty("value");
        storageProgress = (JProgressBar) storageCard.getClientProperty("progress");

        cards.add(cpuCard);
        cards.add(ramCard);
        cards.add(storageCard);

        JPanel overview = createPanel();
        overview.setLayout(new GridLayout(5, 2, 10, 12));

        JLabel overviewTitle = new JLabel("System Overview");
        overviewTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        overviewTitle.setForeground(TEXT);

        JPanel center = new JPanel(new BorderLayout(0, 15));
        center.setBackground(BACKGROUND);
        center.add(cards, BorderLayout.NORTH);

        JPanel details = new JPanel(new BorderLayout());
        details.setBackground(BACKGROUND);
        details.add(overviewTitle, BorderLayout.NORTH);

        JPanel detailGrid = new JPanel(new GridLayout(5, 2, 10, 12));
        detailGrid.setBackground(CARD);
        detailGrid.setBorder(new EmptyBorder(20, 20, 20, 20));

        overviewCPU = createDetailLabel();
        overviewRAM = createDetailLabel();
        overviewStorage = createDetailLabel();
        overviewOS = createDetailLabel();
        overviewUptime = createDetailLabel();

        addDetailRow(detailGrid, "Processor", overviewCPU);
        addDetailRow(detailGrid, "Installed Memory", overviewRAM);
        addDetailRow(detailGrid, "Total Storage", overviewStorage);
        addDetailRow(detailGrid, "Operating System", overviewOS);
        addDetailRow(detailGrid, "System Uptime", overviewUptime);

        details.add(detailGrid, BorderLayout.CENTER);

        center.add(details, BorderLayout.CENTER);

        panel.add(center, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createResourceCard(String name) {
        JPanel card = new JPanel();
        card.setBackground(CARD);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel(name);
        title.setForeground(MUTED);
        title.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel value = new JLabel("0%");
        value.setForeground(TEXT);
        value.setFont(new Font("Segoe UI", Font.BOLD, 30));

        JProgressBar progress = new JProgressBar(0, 100);
        progress.setValue(0);
        progress.setStringPainted(false);
        progress.setForeground(ACCENT);
        progress.setBackground(new Color(55, 55, 60));
        progress.setBorderPainted(false);
        progress.setPreferredSize(new Dimension(200, 8));
        progress.setMaximumSize(new Dimension(Integer.MAX_VALUE, 8));

        card.add(title);
        card.add(Box.createVerticalStrut(20));
        card.add(value);
        card.add(Box.createVerticalStrut(20));
        card.add(progress);

        card.putClientProperty("value", value);
        card.putClientProperty("progress", progress);

        return card;
    }

    private void addDetailRow(
            JPanel panel, String name, JLabel value) {

        JLabel label = new JLabel(name);
        label.setForeground(MUTED);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        panel.add(label);
        panel.add(value);
    }

    private JLabel createDetailLabel() {
        JLabel label = new JLabel("Loading...");
        label.setForeground(TEXT);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return label;
    }

    // DASHBOARD REFRESH

    private void refreshDashboard() {
        double cpu = getNumber(monitor, "getCPUUsage");
        double ram = memoryInfo.getRAMUsage();
        double storage = getNumber(monitor, "getStorageUsage");

        updateResource(cpuValue, cpuProgress, cpu);
        updateResource(ramValue, ramProgress, ram);
        updateResource(storageValue, storageProgress, storage);

        overviewCPU.setText(getText(
                monitor, "getCPUName", "getProcessorName"));

        overviewRAM.setText(
                memoryInfo.formatGB(memoryInfo.getTotalRAM()));

        overviewStorage.setText(getText(
                monitor, "getTotalStorage", "getStorageSummary"));

        overviewOS.setText(getText(
                monitor, "getOperatingSystem", "getOSName"));

        overviewUptime.setText(getText(
                monitor, "getUptime", "getSystemUptime"));
    }

    private void updateResource(
            JLabel label, JProgressBar progress, double value) {

        int percentage = (int) Math.round(value);
        percentage = Math.max(0, Math.min(100, percentage));

        label.setText(String.format("%.1f%%", value));
        progress.setValue(percentage);
    }

    // MEMORY PAGE

    private JPanel showMemoryPage() {
        JPanel panel = createPanel();
        panel.setLayout(new BorderLayout(20, 20));

        JPanel header = new JPanel();
        header.setBackground(BACKGROUND);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Memory Monitor");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel(
                "View your system's RAM usage and memory availability.");

        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(MUTED);

        header.add(title);
        header.add(Box.createVerticalStrut(5));
        header.add(subtitle);

        panel.add(header, BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setBackground(BACKGROUND);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        JPanel summaryCard = new JPanel();
        summaryCard.setBackground(CARD);
        summaryCard.setLayout(new BoxLayout(summaryCard, BoxLayout.Y_AXIS));
        summaryCard.setBorder(new EmptyBorder(25, 25, 25, 25));

        JLabel usageTitle = new JLabel("Memory Utilization");
        usageTitle.setForeground(MUTED);
        usageTitle.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        memoryPercentage = new JLabel("0%");
        memoryPercentage.setForeground(TEXT);
        memoryPercentage.setFont(new Font("Segoe UI", Font.BOLD, 42));

        memoryProgress = new JProgressBar(0, 100);
        memoryProgress.setValue(0);
        memoryProgress.setForeground(ACCENT);
        memoryProgress.setBackground(new Color(55, 55, 60));
        memoryProgress.setBorderPainted(false);
        memoryProgress.setStringPainted(false);
        memoryProgress.setPreferredSize(new Dimension(400, 12));
        memoryProgress.setMaximumSize(new Dimension(Integer.MAX_VALUE, 12));

        summaryCard.add(usageTitle);
        summaryCard.add(Box.createVerticalStrut(15));
        summaryCard.add(memoryPercentage);
        summaryCard.add(Box.createVerticalStrut(20));
        summaryCard.add(memoryProgress);

        center.add(summaryCard);
        center.add(Box.createVerticalStrut(20));

        JPanel informationCard = new JPanel(new GridLayout(3, 2, 15, 20));
        informationCard.setBackground(CARD);
        informationCard.setBorder(new EmptyBorder(25, 25, 25, 25));

        memoryTotal = createDetailLabel();
        memoryUsed = createDetailLabel();
        memoryAvailable = createDetailLabel();

        addDetailRow(informationCard, "Total Installed RAM", memoryTotal);
        addDetailRow(informationCard, "Used RAM", memoryUsed);
        addDetailRow(informationCard, "Available RAM", memoryAvailable);

        center.add(informationCard);

        panel.add(center, BorderLayout.CENTER);

        return panel;
    }

    private void refreshMemoryPage() {
        long total = memoryInfo.getTotalRAM();
        long used = memoryInfo.getUsedRAM();
        long available = memoryInfo.getAvailableRAM();

        double usage = memoryInfo.getRAMUsage();

        int percentage = (int) Math.round(usage);
        percentage = Math.max(0, Math.min(100, percentage));

        memoryTotal.setText(memoryInfo.formatGB(total));
        memoryUsed.setText(memoryInfo.formatGB(used));
        memoryAvailable.setText(memoryInfo.formatGB(available));

        memoryPercentage.setText(String.format("%.1f%%", usage));
        memoryProgress.setValue(percentage);
    }

    // PROCESSOR PAGE

    private JPanel showProcessorPage() {
        JPanel panel = createPanel();
        panel.setLayout(new BorderLayout(20, 20));

        JLabel title = new JLabel("Processor Information");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel(
                "Detailed information about your system processor.");

        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(MUTED);

        JPanel header = new JPanel();
        header.setBackground(BACKGROUND);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.add(title);
        header.add(Box.createVerticalStrut(5));
        header.add(subtitle);

        processorDetails = new JTextArea();
        processorDetails.setEditable(false);
        processorDetails.setLineWrap(true);
        processorDetails.setWrapStyleWord(true);
        processorDetails.setBackground(CARD);
        processorDetails.setForeground(TEXT);
        processorDetails.setFont(new Font("Consolas", Font.PLAIN, 14));
        processorDetails.setBorder(new EmptyBorder(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(processorDetails);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(CARD);

        panel.add(header, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void refreshProcessorPage() {
        String details = getText(
                processorInfo, "getProcessorSummary");

        processorDetails.setText(details);
        processorDetails.setCaretPosition(0);
    }

    // PLACEHOLDER PAGES

    private JPanel showPlaceholder(String titleText, String message) {
        JPanel panel = createPanel();
        panel.setLayout(new BorderLayout());

        JLabel title = new JLabel(titleText);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(TEXT);

        JLabel description = new JLabel(message);
        description.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        description.setForeground(MUTED);

        JPanel content = new JPanel();
        content.setBackground(BACKGROUND);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        content.add(title);
        content.add(Box.createVerticalStrut(10));
        content.add(description);

        panel.add(content, BorderLayout.NORTH);

        return panel;
    }

    // GENERAL HELPERS

    private JPanel createPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(BACKGROUND);
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));
        return panel;
    }

    private double getNumber(Object object, String methodName) {
        try {
            Method method = object.getClass().getMethod(methodName);
            Object result = method.invoke(object);

            if (result instanceof Number) {
                return ((Number) result).doubleValue();
            }
        } catch (Exception ignored) {
        }

        return 0;
    }

    private String getText(Object object, String... methodNames) {
        for (String methodName : methodNames) {
            try {
                Method method = object.getClass().getMethod(methodName);
                Object result = method.invoke(object);

                if (result != null) {
                    return result.toString();
                }
            } catch (Exception ignored) {
            }
        }

        return "Not available";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}

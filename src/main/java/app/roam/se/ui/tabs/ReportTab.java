package app.roam.se.ui.tabs;

import app.roam.se.models.reports.Report;
import app.roam.se.models.reports.Result;
import app.roam.se.models.reports.TestCaseReport;
import app.roam.se.ui.common.Loader;
import app.roam.se.ui.misc.Icons;
import app.roam.se.ui.misc.Theme;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

public class ReportTab {
    public JPanel mainPanel;
    private JList listTestCase;
    private JList listStep;
    private JPanel panelOverall;
    private JPanel screenshotPanel;
    private JLabel screenshot;
    private JPanel lblSteps;
    private JLabel lblDate;
    private JLabel lblTime;
    private JLabel lblBrowser;
    private JLabel lblOS;
    private JLabel lblBrowserVersion;
    private JLabel lblOSVersion;
    private JLabel lblStepBreakDown;
    Report report;
    TestCaseReport testCaseReport = null;

    public ReportTab(Report report) {
        this.report = report;
        listTestCase.setModel(new ListModel() {
            @Override
            public int getSize() {
                return report.getTestCases().size();
            }

            @Override
            public Object getElementAt(int index) {
                return report.getTestCases().get(index);
            }

            @Override
            public void addListDataListener(ListDataListener l) {

            }

            @Override
            public void removeListDataListener(ListDataListener l) {

            }
        });
        listTestCase.setCellRenderer(new ListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                TestCaseReport tcr = (TestCaseReport) value;
                testCaseReport = tcr;
                boolean passed = true;
                for (Result r : tcr.steps) {
                    if (!r.isPassed) {
                        passed = false;
                    }
                }
                JLabel label = new JLabel(tcr.testCaseName);
                label.setOpaque(true);
                label.setForeground((passed ? Theme.passedTextColor : Theme.failedTextColor));
                label.setBackground((isSelected ? Theme.selectedElementColor : Theme.defaultColor));
                return label;

            }
        });
        listTestCase.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (testCaseReport != null) {
                    listStep.setModel(new ListModel() {
                        @Override
                        public int getSize() {
                            return testCaseReport.steps.size();
                        }

                        @Override
                        public Object getElementAt(int index) {
                            return testCaseReport.steps.get(index);
                        }

                        @Override
                        public void addListDataListener(ListDataListener l) {

                        }

                        @Override
                        public void removeListDataListener(ListDataListener l) {

                        }
                    });
                }
            }
        });
        listStep.setCellRenderer(new ListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Result re = (Result) value;
                String sequence = (index + 1) + ". ";
                JLabel label = new JLabel(sequence + re.getStatement());
                label.setOpaque(true);
                label.setForeground((re.isPassed ? Theme.passedTextColor : Theme.failedTextColor));
                label.setBackground((isSelected ? Theme.selectedElementColor : Theme.defaultColor));

                return label;
            }
        });
        listStep.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                selectedStep = (Result) listStep.getSelectedValue();
                if (selectedStep != null) {
                    try {
                        File f = new File(selectedStep.screenshot);
                        BufferedImage scr = ImageIO.read(f);
                        Loader loader = new Loader("Loading image...");
                        screenshot.setText(f.getName());
                        int width = 640;
                        int height = 360;//* scr.getHeight() / scr.getWidth();
                        screenshot.setIcon(new ImageIcon(scr.getScaledInstance(width, height, Image.SCALE_SMOOTH)));
                        loader.dispose();
                    } catch (IOException ex) {
                        screenshot.setText("<No Screenshot>");
                        screenshot.setIcon(new ImageIcon(Icons.getBlankScreenshot()));
                        ex.printStackTrace();
                    }
                }
            }
        });
        updateOverAllChart();
    }

    private Result selectedStep;
    int stepsPassed = 0;
    int stepsFailed = 0;
    int testCasesPassed = 0;
    int testCasesFailed = 0;

    public void updateOverAllChart() {
        stepsPassed = 0;
        stepsFailed = 0;
        testCasesPassed = 0;
        testCasesFailed = 0;
        for (TestCaseReport t : report.testCases) {
            boolean passed = true;
            for (Result r : t.steps) {
                if (r.isPassed) {
                    stepsPassed += 1;
                } else {
                    passed = false;
                    stepsFailed += 1;
                }
            }
            testCasesPassed = passed ? testCasesPassed + 1 : testCasesPassed;
            testCasesFailed = !passed ? testCasesFailed + 1 : testCasesFailed;
        }
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Passed", testCasesPassed);
        dataset.setValue("Failed", testCasesFailed);
        JFreeChart pieChart = ChartFactory.createPieChart("Test Cases", dataset, true, false, false);
        ((PiePlot) pieChart.getPlot()).setSectionPaint("Passed", new Color(51, 255, 51));
        ((PiePlot) pieChart.getPlot()).setSectionPaint("Failed", new Color(255, 0, 0));
        panelOverall.add(new ChartPanel(pieChart));
        lblBrowser.setText(report.browser);
        lblBrowserVersion.setText(report.browserVersion);
        lblOS.setText(report.os);
        lblDate.setText(report.date);
        lblTime.setText(report.duration);
        lblStepBreakDown.setText(stepsPassed + "/" + (stepsFailed + stepsPassed));
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(0, 0));
        mainPanel.setPreferredSize(new Dimension(640, 480));
        final JSplitPane splitPane1 = new JSplitPane();
        splitPane1.setDividerLocation(200);
        mainPanel.add(splitPane1, BorderLayout.CENTER);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout(0, 0));
        panel1.setPreferredSize(new Dimension(160, 120));
        splitPane1.setLeftComponent(panel1);
        listTestCase = new JList();
        panel1.add(listTestCase, BorderLayout.CENTER);
        panelOverall = new JPanel();
        panelOverall.setLayout(new BorderLayout(0, 0));
        panelOverall.setPreferredSize(new Dimension(150, 150));
        panelOverall.setRequestFocusEnabled(false);
        panel1.add(panelOverall, BorderLayout.NORTH);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new BorderLayout(0, 0));
        splitPane1.setRightComponent(panel2);
        final JSplitPane splitPane2 = new JSplitPane();
        splitPane2.setDividerLocation(150);
        splitPane2.setOrientation(0);
        panel2.add(splitPane2, BorderLayout.CENTER);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(3, 4, new Insets(0, 0, 0, 0), -1, -1));
        splitPane2.setLeftComponent(panel3);
        lblSteps = new JPanel();
        lblSteps.setLayout(new GridBagLayout());
        panel3.add(lblSteps, new GridConstraints(0, 0, 3, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        lblSteps.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label1 = new JLabel();
        label1.setAlignmentX(0.5f);
        Font label1Font = this.$$$getFont$$$(null, Font.BOLD, 24, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setHorizontalAlignment(0);
        label1.setHorizontalTextPosition(0);
        label1.setText("100%");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        lblSteps.add(label1, gbc);
        lblStepBreakDown = new JLabel();
        lblStepBreakDown.setAlignmentX(0.5f);
        lblStepBreakDown.setHorizontalAlignment(0);
        lblStepBreakDown.setHorizontalTextPosition(0);
        lblStepBreakDown.setText("Label");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        lblSteps.add(lblStepBreakDown, gbc);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridBagLayout());
        panel3.add(panel4, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel4.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        lblTime = new JLabel();
        lblTime.setAlignmentX(0.5f);
        Font lblTimeFont = this.$$$getFont$$$(null, Font.BOLD, 24, lblTime.getFont());
        if (lblTimeFont != null) lblTime.setFont(lblTimeFont);
        lblTime.setHorizontalAlignment(0);
        lblTime.setHorizontalTextPosition(0);
        lblTime.setText("Label");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel4.add(lblTime, gbc);
        lblDate = new JLabel();
        lblDate.setAlignmentX(0.5f);
        lblDate.setHorizontalAlignment(0);
        lblDate.setHorizontalTextPosition(0);
        lblDate.setText("Label");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel4.add(lblDate, gbc);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridBagLayout());
        panel3.add(panel5, new GridConstraints(0, 2, 3, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel5.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        lblBrowser = new JLabel();
        Font lblBrowserFont = this.$$$getFont$$$(null, Font.BOLD, 18, lblBrowser.getFont());
        if (lblBrowserFont != null) lblBrowser.setFont(lblBrowserFont);
        lblBrowser.setHorizontalAlignment(0);
        lblBrowser.setHorizontalTextPosition(0);
        lblBrowser.setText("Label");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel5.add(lblBrowser, gbc);
        lblBrowserVersion = new JLabel();
        lblBrowserVersion.setText("Browser");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel5.add(lblBrowserVersion, gbc);
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridBagLayout());
        panel3.add(panel6, new GridConstraints(0, 3, 3, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel6.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        lblOS = new JLabel();
        Font lblOSFont = this.$$$getFont$$$(null, Font.BOLD, 18, lblOS.getFont());
        if (lblOSFont != null) lblOS.setFont(lblOSFont);
        lblOS.setHorizontalAlignment(0);
        lblOS.setHorizontalTextPosition(0);
        lblOS.setText("Label");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        panel6.add(lblOS, gbc);
        lblOSVersion = new JLabel();
        lblOSVersion.setAlignmentX(0.5f);
        lblOSVersion.setHorizontalAlignment(0);
        lblOSVersion.setHorizontalTextPosition(0);
        lblOSVersion.setText("OS");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel6.add(lblOSVersion, gbc);
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new BorderLayout(0, 0));
        splitPane2.setRightComponent(panel7);
        final JSplitPane splitPane3 = new JSplitPane();
        splitPane3.setDividerLocation(150);
        panel7.add(splitPane3, BorderLayout.CENTER);
        listStep = new JList();
        splitPane3.setLeftComponent(listStep);
        screenshotPanel = new JPanel();
        screenshotPanel.setLayout(new BorderLayout(0, 0));
        splitPane3.setRightComponent(screenshotPanel);
        screenshot = new JLabel();
        screenshot.setHorizontalAlignment(0);
        screenshot.setHorizontalTextPosition(0);
        screenshot.setRequestFocusEnabled(false);
        screenshot.setText("<Screenshot>");
        screenshot.setVerticalTextPosition(3);
        screenshotPanel.add(screenshot, BorderLayout.CENTER);
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}

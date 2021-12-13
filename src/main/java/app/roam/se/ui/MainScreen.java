package app.roam.se.ui;

import app.roam.se.models.reports.Report;
import app.roam.se.ui.common.UIUtil;
import app.roam.se.ui.dialogs.DatabaseDialog;
import app.roam.se.ui.dialogs.ProjectDialog;
import app.roam.se.ui.dialogs.ProjectSettingsDialog;
import app.roam.se.ui.dialogs.TestPlanner;
import app.roam.se.ui.dialogs.browserconfigs.ChromeConfigDialog;
import app.roam.se.ui.dialogs.browserconfigs.FirefoxConfigDialog;
import app.roam.se.ui.misc.Icons;
import app.roam.se.ui.tabs.*;
import app.roam.se.ui.views.CustomOutputStream;
import app.roam.se.utils.FilesUtil;
import app.roam.se.App;
import app.roam.se.models.browser.BrowserConfig;
import app.roam.se.ui.menu.TreePopUp;
import app.roam.se.utils.AppUtil;
import lombok.Data;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.PrintStream;
import java.time.Duration;

@Data
public class MainScreen {
    private JPanel mainpanel;
    private JTree tree1;

    private JPanel menu;
    private JPanel mainPanel;
    private JButton btnRun;
    private JButton btnToggleBrowser;
    private JComboBox cboBrowser;
    private JTextArea txtConsole;

    public MainScreen(JFrame frame) {
        JMenuBar bar = new JMenuBar();
        JMenu fileMenu = new JMenu("Project");
        JMenuItem newProject = new JMenuItem("New Project");
        JMenuItem closeProject = new JMenuItem("Close Project");
        JMenuItem settings = new JMenuItem("Settings");
        JMenuItem exitProject = new JMenuItem("Exit");
        closeProject.setEnabled(App.testProject == null ? false : true);
        settings.setEnabled(App.testProject == null ? false : true);

        newProject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ProjectDialog.showDialog();
            }
        });
        settings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ProjectSettingsDialog.showDialog();
            }
        });
        closeProject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AppUtil.closeProject();
                System.exit(0);

            }
        });
        exitProject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (UIUtil.showYesNoDialog(MainScreen.this.mainpanel, "Close Application", "Are you sure you like to close this application?")) {
                    System.exit(0);
                }
            }
        });
        fileMenu.add(newProject);
        //fileMenu.add(settings);
        fileMenu.add(closeProject);
        fileMenu.add(exitProject);
        bar.add(fileMenu);
        frame.setJMenuBar(bar);

        refreshUI();
        tabbedPane = new JTabbedPaneCloseButton();
        mainPanel.add(tabbedPane);
        cboBrowser.setRenderer(new ListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                if (value != null) {
                    File f = (File) value;
                    return new JLabel(FilesUtil.getFileName(f.getAbsolutePath()));
                }
                return new JLabel("<None>");
            }
        });
        btnToggleBrowser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (App.webDriver == null) {
                    App.webDriver = BrowserConfig.initializeBrowser((File) cboBrowser.getSelectedItem());
                    App.webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
                    App.webDriver.manage().timeouts().scriptTimeout(Duration.ofSeconds(3));
                } else {
                    App.webDriver.close();
                    App.webDriver.quit();
                    App.webDriver = null;
                }
                reactUI();
            }
        });
        btnRun.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TestPlanner.showDialog();
            }
        });
        PrintStream printStream = new PrintStream(new CustomOutputStream(txtConsole));
        System.setOut(printStream);
        System.setErr(printStream);
    }

    public void refreshUI() {
        TreePath selectedTreePath = null;
        if (tree1.getSelectionPath() != null) {
            selectedTreePath = tree1.getSelectionPath();
        }
        tree1.setModel(new MainTreeModel());
        tree1.setCellRenderer(new MainTreeCellRenderer());

        if (tree1.getMouseListeners().length < 2) {
            tree1.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    if (e.getClickCount() == 2 && tree1.getSelectionCount() == 1) {
                        File path = (File) tree1.getSelectionPath().getLastPathComponent();
                        if (path.getName().endsWith("Data")) {
                            DatabaseDialog.showDialog();
                        } else if (!path.getName().endsWith("Library") && path.getAbsolutePath().contains("Library")) {
                            showTab(path.getName(), Icons.library, new WebEntityTab(path.getAbsolutePath()).getMainPanel());

                        } else if (!path.getName().endsWith("Test Groups") && path.getAbsolutePath().contains("Test Groups") && new File(path.getAbsolutePath() + "/default.json").exists()) {
                            showTab(path.getName(), Icons.testFeature, new TestFeatureTab(path.getAbsolutePath()).getMainPanel());
                        } else if (!path.getName().endsWith("Test Cases") && path.getAbsolutePath().contains("Test Cases") && new File(path.getAbsolutePath() + "/default.json").exists()) {
                            showTab(path.getName(), Icons.testCase, new TestCaseTab(path.getAbsolutePath()).getMainPanel());
                        } else if (path.getName().contains(".json") && path.getAbsolutePath().contains("Browsers")) {

                            String raw = FilesUtil.readFile(path.getAbsolutePath());
                            JSONObject object = new JSONObject(raw);
                            String typ = object.getString(BrowserConfig.BROWSER_TYPE);
                            if (typ.equals("chrome")) {
                                ChromeConfigDialog.showDialog(false, path.getAbsolutePath());
                            } else if (typ.equals("firefox")) {
                                FirefoxConfigDialog.showDialog(false, path.getAbsolutePath());
                            }
                        } else if (path.getName().contains(".json") && path.getAbsolutePath().contains("Results")) {
                            Report report = new Report();
                            report.loadReport(path.getAbsolutePath());
                            showTab(path.getName(), Icons.result, new ReportTab(report).mainPanel);
                        }

                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    if (e.isPopupTrigger() && tree1.getSelectionCount() == 1) {
                        File path = (File) tree1.getSelectionPath().getLastPathComponent();
                        new TreePopUp(MainScreen.this, path).show(tree1, e.getX(), e.getY());

                    }
                }
            });
        }
        if (selectedTreePath != null) {
            tree1.setSelectionPath(selectedTreePath);
        }
        reactUI();
    }

    public void showTab(String name, Icon icon, JPanel panel) {

        boolean exist = false;
        if (tabbedPane != null) {
            for (int i = 0; i < tabbedPane.getTabCount(); i++) {
                if (tabbedPane.getTitleAt(i).trim().equals(name)) {
                    tabbedPane.setSelectedIndex(i);
                    exist = true;
                }
            }
            if (!exist) {
                tabbedPane.addTab(name, icon, panel);
                tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
            }
        }

    }

    JTabbedPaneCloseButton tabbedPane;

    private void createUIComponents() {
        // TODO: place custom component creation code here

    }

    public void reactUI() {


        cboBrowser.setModel(new DefaultComboBoxModel(FilesUtil.getFiles(App.testProject.getLocation() + "/Browsers")));
        btnToggleBrowser.setIcon(App.webDriver == null ? Icons.disconnect : Icons.connect);
        btnRun.setIcon(App.isRunning ? Icons.stop : Icons.play);
        btnRun.setEnabled(App.webDriver == null);
        btnToggleBrowser.setEnabled(!App.isRunning);
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
        mainpanel = new JPanel();
        mainpanel.setLayout(new BorderLayout(0, 0));
        menu = new JPanel();
        menu.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        mainpanel.add(menu, BorderLayout.NORTH);
        cboBrowser = new JComboBox();
        cboBrowser.setPreferredSize(new Dimension(80, 30));
        menu.add(cboBrowser);
        btnToggleBrowser = new JButton();
        btnToggleBrowser.setPreferredSize(new Dimension(30, 30));
        btnToggleBrowser.setText("");
        menu.add(btnToggleBrowser);
        final JSeparator separator1 = new JSeparator();
        separator1.setOrientation(1);
        separator1.setPreferredSize(new Dimension(3, 30));
        menu.add(separator1);
        btnRun = new JButton();
        btnRun.setPreferredSize(new Dimension(30, 30));
        btnRun.setText("");
        menu.add(btnRun);
        final JSplitPane splitPane1 = new JSplitPane();
        splitPane1.setDividerLocation(150);
        mainpanel.add(splitPane1, BorderLayout.CENTER);
        tree1 = new JTree();
        splitPane1.setLeftComponent(tree1);
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(0, 0));
        splitPane1.setRightComponent(mainPanel);
        final JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setPreferredSize(new Dimension(1, 100));
        mainPanel.add(scrollPane1, BorderLayout.SOUTH);
        scrollPane1.setBorder(BorderFactory.createTitledBorder(null, "Console", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        txtConsole = new JTextArea();
        txtConsole.setPreferredSize(new Dimension(1, 100));
        scrollPane1.setViewportView(txtConsole);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainpanel;
    }

}

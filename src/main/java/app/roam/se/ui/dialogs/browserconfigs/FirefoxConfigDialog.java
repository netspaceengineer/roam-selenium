package app.roam.se.ui.dialogs.browserconfigs;

import app.roam.se.App;
import app.roam.se.models.browser.FirefoxConfig;
import app.roam.se.ui.common.UIUtil;
import app.roam.se.ui.misc.Icons;
import app.roam.se.utils.FilesUtil;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FirefoxConfigDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField txtName;
    private JTextField txtBinary;
    private JCheckBox chkHeadless;
    private JButton btnDelArg;
    private JButton btnAddArg;
    private JList lstArgs;
    private JList lstPreferences;
    private JButton btnAddPref;
    private JButton btnDelPref;
    private boolean isNew = true;
    public String path = "";
    private List<String> firefoxArguments = new ArrayList<String>();
    private List<String> firefoxPreferences = new ArrayList<String>();

    public FirefoxConfigDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        setIconImage(Icons.getLogo());

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        txtBinary.setText(App.testProject.getLocation() + "/Resources/geckodriver.exe");
        btnAddArg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String c = UIUtil.showInputDialog(null, "Specify firefox arguments:");
                if (!c.isEmpty()) {
                    firefoxArguments.add(c);
                    refreshList();
                }
            }
        });
        btnDelArg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = lstArgs.getSelectedIndex();
                firefoxArguments.remove(index);
                refreshList();
            }
        });
        btnAddPref.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String c = UIUtil.showInputDialog(null, "Specify firefox preference (format must be <key>:<value>):");
                if (!c.isEmpty()) {
                    String[] value = c.split(":");
                    if (value.length == 2) {
                        firefoxPreferences.add(c);
                        refreshList();
                    } else {
                        UIUtil.showErrorMessage(null, "Invalid Preference", String.format("'%s' is not valid preference!", c));
                    }

                }
            }
        });
        btnDelPref.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = lstPreferences.getSelectedIndex();
                firefoxPreferences.remove(index);
                refreshList();

            }
        });
        lstPreferences.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                reactUI();
            }
        });
        lstArgs.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                reactUI();
            }
        });
    }

    private void onOK() {
        if (txtName.getText().isEmpty() || txtBinary.getText().isEmpty()) {
            UIUtil.showErrorMessage(null, "Invalid Input", "Config name and webdriver path cannot be empty!");
        } else if (!new File(txtBinary.getText()).exists()) {
            UIUtil.showErrorMessage(null, "Invalid WebDriver", "WebDriver does not exists");
        } else if (isNew && new File(App.testProject.getLocation() + "/Browsers/" + txtName.getText() + ".json").exists()) {
            UIUtil.showErrorMessage(null, "Duplicate Config", "Config with the same name already exists!");
        } else {
            if (!isNew) {
                new File(path).delete();
            }
            FirefoxConfig browserConfig = new FirefoxConfig();

            browserConfig.setConfigName(txtName.getText());
            browserConfig.setWebDriverLocation(txtBinary.getText());
            browserConfig.setHeadless(chkHeadless.isSelected());
            for (String a : firefoxArguments) {
                browserConfig.getFirefoxArguments().add(a);
            }
            for (String p : firefoxPreferences) {
                browserConfig.getFirefoxPreferences().add(p);
            }

            browserConfig.setLocation(App.testProject.getLocation() + "/Browsers/" + txtName.getText() + ".json");

            browserConfig.saveBrowser();
            dispose();
        }

    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void showDialog(boolean b, String parentPath) {
        FirefoxConfigDialog dialog = new FirefoxConfigDialog();
        dialog.isNew = b;
        dialog.path = parentPath;
        dialog.refreshUI();
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

    }

    private void reactUI() {
        btnDelPref.setEnabled(lstPreferences.getSelectedIndex() >= 0);
        btnDelArg.setEnabled(lstArgs.getSelectedIndex() >= 0);

    }

    private void refreshList() {
        DefaultListModel argsModel = new DefaultListModel();
        for (String c : firefoxArguments) {
            argsModel.addElement(c);
        }
        lstArgs.setModel(argsModel);


        DefaultListModel prefModel = new DefaultListModel();
        for (String c : firefoxPreferences) {
            prefModel.addElement(c);
        }
        lstPreferences.setModel(prefModel);
        reactUI();
    }

    private void refreshUI() {
        buttonOK.setText(isNew ? "Create" : "Save");
        setTitle(isNew ? "New Firefox Config" : "Modify Firefox Config: " + FilesUtil.getFileName(path));
        btnAddArg.setIcon(Icons.add);
        btnAddPref.setIcon(Icons.add);
        btnDelArg.setIcon(Icons.delete);
        btnDelPref.setIcon(Icons.delete);

        if (!isNew) {
            FirefoxConfig brc = new FirefoxConfig();
            brc.loadBrowser(path);
            txtName.setText(brc.getConfigName());
            txtBinary.setText(brc.getWebDriverLocation());
            chkHeadless.setSelected(brc.isHeadless());
            firefoxArguments = brc.getFirefoxArguments();
            firefoxPreferences = brc.getFirefoxPreferences();
            refreshList();
        }
        reactUI();
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
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(4, 1, new Insets(10, 10, 10, 10), -1, -1));
        contentPane.setPreferredSize(new Dimension(500, 500));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
        panel1.add(panel2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonOK = new JButton();
        buttonOK.setText("OK");
        panel2.add(buttonOK, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonCancel = new JButton();
        buttonCancel.setText("Cancel");
        panel2.add(buttonCancel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridBagLayout());
        panel3.setAlignmentX(0.0f);
        panel3.setAlignmentY(0.0f);
        contentPane.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Config Name:");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panel3.add(label1, gbc);
        txtName = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(txtName, gbc);
        txtBinary = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(txtBinary, gbc);
        final JLabel label2 = new JLabel();
        label2.setText("Binary/Executable:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(label2, gbc);
        chkHeadless = new JCheckBox();
        chkHeadless.setText("Headless");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(chkHeadless, gbc);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.BOTH;
        panel3.add(panel4, gbc);
        final JLabel label3 = new JLabel();
        label3.setText("Firefox Arguments:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel4.add(label3, gbc);
        btnDelArg = new JButton();
        btnDelArg.setPreferredSize(new Dimension(30, 30));
        btnDelArg.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel4.add(btnDelArg, gbc);
        btnAddArg = new JButton();
        btnAddArg.setPreferredSize(new Dimension(30, 30));
        btnAddArg.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel4.add(btnAddArg, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel4.add(spacer2, gbc);
        lstArgs = new JList();
        lstArgs.setPreferredSize(new Dimension(0, 100));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.BOTH;
        panel3.add(lstArgs, gbc);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridBagLayout());
        contentPane.add(panel5, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Preferences:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel5.add(label4, gbc);
        btnAddPref = new JButton();
        btnAddPref.setPreferredSize(new Dimension(30, 30));
        btnAddPref.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel5.add(btnAddPref, gbc);
        btnDelPref = new JButton();
        btnDelPref.setPreferredSize(new Dimension(30, 30));
        btnDelPref.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel5.add(btnDelPref, gbc);
        final JPanel spacer3 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel5.add(spacer3, gbc);
        lstPreferences = new JList();
        contentPane.add(lstPreferences, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}

package app.roam.se.ui.dialogs.browserconfigs;

import app.roam.se.App;
import app.roam.se.models.browser.BrowserConfig;
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

public class ChromeConfigDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField txtPath;
    private JList listOpt;
    private JList listExp;
    private JTextField txtName;
    private JButton btnAddOption;
    private JButton btnRmvOption;
    private JButton btnRmvExp;
    private JButton btnAddExp;
    private boolean isNew = true;
    private String path;
    private List<String> chromeOptions = new ArrayList<String>();
    private List<String> experimentalOptions = new ArrayList<String>();

    public ChromeConfigDialog() {
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


        listOpt.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                btnRmvOption.setEnabled(listOpt.getSelectedIndex() >= 0);
            }
        });
        refreshUI();
        btnAddOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newOption = UIUtil.showInputDialog(null, "Specify Chrome Option");

                if (!newOption.isEmpty()) {
                    chromeOptions.add(newOption);
                    DefaultListModel dlm = new DefaultListModel();
                    for (String c : chromeOptions) {
                        dlm.addElement(c);
                    }
                    listOpt.setModel(dlm);
                }
            }
        });
        btnRmvOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = listOpt.getSelectedIndex();
                chromeOptions.remove(index);
                DefaultListModel dlm = new DefaultListModel();
                for (String c : chromeOptions) {
                    dlm.addElement(c);
                }
                listOpt.setModel(dlm);
            }
        });
        txtPath.setText(App.testProject.getLocation() + "/Resources/chromedriver.exe");

    }

    public void refreshUI() {
        buttonOK.setText(isNew ? "Create" : "Save");

        btnAddOption.setIcon(Icons.add);

        btnRmvOption.setIcon(Icons.delete);
        setTitle(isNew ? "New Chrome Config" : "Modify Chrome Config: " + FilesUtil.getFileName(path));
        if (!isNew) {
            BrowserConfig brc = new BrowserConfig();
            brc.loadBrowser(path);
            txtName.setText(brc.getConfigName());
            txtPath.setText(brc.getWebDriverLocation());
            chromeOptions = brc.getChromeOptions();
            DefaultListModel dlm = new DefaultListModel();
            for (String c : chromeOptions) {
                dlm.addElement(c);
            }
            listOpt.setModel(dlm);
        }
    }


    private void onOK() {
        if (txtName.getText().isEmpty() || txtPath.getText().isEmpty()) {
            UIUtil.showErrorMessage(null, "Invalid Input", "Config name and webdriver path cannot be empty!");
        } else if (!new File(txtPath.getText()).exists()) {
            UIUtil.showErrorMessage(null, "Invalid WebDriver", "WebDriver does not exists");
        } else if (isNew && new File(App.testProject.getLocation() + "/Browsers/" + txtName.getText() + ".json").exists()) {
            UIUtil.showErrorMessage(null, "Duplicate Config", "Config with the same name already exists!");
        } else {
            if (!isNew) {
                new File(path).delete();
            }
            BrowserConfig browserConfig = new BrowserConfig();
            browserConfig.setBrowserType("chrome");
            browserConfig.setConfigName(txtName.getText());
            browserConfig.setWebDriverLocation(txtPath.getText());
            for (String o : chromeOptions) {
                browserConfig.getChromeOptions().add(o);
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

        ChromeConfigDialog dialog = new ChromeConfigDialog();
        dialog.path = parentPath;
        dialog.isNew = b;
        dialog.refreshUI();
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
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
        contentPane.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1));
        contentPane.setPreferredSize(new Dimension(500, 500));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
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
        panel3.setLayout(new GridLayoutManager(6, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Executable:");
        panel3.add(label1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtPath = new JTextField();
        panel3.add(txtPath, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        listOpt = new JList();
        panel3.add(listOpt, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Config Name:");
        panel3.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtName = new JTextField();
        panel3.add(txtName, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridBagLayout());
        panel3.add(panel4, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Chrome Options");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel4.add(label3, gbc);
        btnAddOption = new JButton();
        btnAddOption.setOpaque(false);
        btnAddOption.setPreferredSize(new Dimension(30, 30));
        btnAddOption.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel4.add(btnAddOption, gbc);
        btnRmvOption = new JButton();
        btnRmvOption.setEnabled(false);
        btnRmvOption.setOpaque(false);
        btnRmvOption.setPreferredSize(new Dimension(30, 30));
        btnRmvOption.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel4.add(btnRmvOption, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel4.add(spacer2, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}

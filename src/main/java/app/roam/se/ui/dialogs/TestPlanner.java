package app.roam.se.ui.dialogs;

import app.roam.se.ui.misc.Icons;
import app.roam.se.ui.misc.Theme;
import app.roam.se.utils.FilesUtil;
import app.roam.se.utils.TimeUtil;
import app.roam.se.App;
import app.roam.se.models.test.TestCase;
import app.roam.se.test.TestInitiator;
import app.roam.se.utils.DBUtil;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TestPlanner extends JDialog {

    private JButton buttonOK;
    private JButton buttonCancel;
    private JList list1;
    private JList selectedList;
    private JPanel configPanel;
    private JPanel mainPanel;
    private JButton btnAdd;
    private JButton btnRemove;
    private JComboBox cboBrowser;
    private List<File> left = new ArrayList<File>();
    private List<File> right = new ArrayList<File>();
    private List<JCheckBox> variantsCheckboxes = new ArrayList<JCheckBox>();

    public TestPlanner() {
        setContentPane(mainPanel);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setTitle("Run Test Cases");
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
        mainPanel.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        JCheckBox defCHK = new JCheckBox("default");
        defCHK.setName("default");

        variantsCheckboxes.add(defCHK);
        for (String f : DBUtil.getDistinctVariants()) {

            JCheckBox checkBox = new JCheckBox(f);
            checkBox.setName(f);
            variantsCheckboxes.add(checkBox);
        }
        for (JCheckBox c : variantsCheckboxes) {
            c.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    reactUI();
                }
            });
            configPanel.add(c);

        }
        btnAdd.setIcon(Icons.right);
        btnRemove.setIcon(Icons.left);
        list1.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                reactUI();
            }
        });
        selectedList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                reactUI();
            }
        });
        for (File s : FilesUtil.getFiles(App.testProject.getLocation() + "/Test Cases")) {
            left.add(s);
        }

        list1.setCellRenderer(renderer);
        selectedList.setCellRenderer(renderer);
        refreshUI();
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (list1.getSelectedIndex() >= 0) {
                    right.add(left.get(list1.getSelectedIndex()));
                    left.remove(list1.getSelectedIndex());
                    refreshUI();
                }
            }
        });
        btnRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedList.getSelectedIndex() >= 0) {
                    left.add(right.get(selectedList.getSelectedIndex()));
                    right.remove(selectedList.getSelectedIndex());
                    refreshUI();
                }
            }
        });
        cboBrowser.setRenderer(new ListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                File f = (File) value;
                return new JLabel(FilesUtil.getFileName(f.getAbsolutePath()));
            }
        });
        cboBrowser.setModel(new DefaultComboBoxModel(FilesUtil.getFiles(App.testProject.getLocation() + "/Browsers")));
    }

    private void onOK() {
        FilesUtil.cleanFolder(App.testProject.getLocation() + "/Suites");
        List<TestCase> testCases = new ArrayList<TestCase>();
        List<String> testCaseNames = new ArrayList<String>();
        for (File f : right) {
            testCaseNames.add(f.getName());
        }
        String browser = FilesUtil.getFileName(((File) cboBrowser.getSelectedItem()).getAbsolutePath());
        for (JCheckBox c : variantsCheckboxes) {
            if (c.isSelected()) {
                FilesUtil.generateTestCaseSuite("Test" + TimeUtil.getTimeStamp(), testCaseNames, browser, c.getName());
            }
        }
        new TestInitiator().initializeTest();
        dispose();
    }

    private ListCellRenderer renderer = new ListCellRenderer() {
        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = new JLabel(((File) value).getName());
            label.setOpaque(true);
            label.setBackground(isSelected ? Theme.selectedElementColor : Theme.defaultColor);
            return label;
        }
    };

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void showDialog() {
        TestPlanner dialog = new TestPlanner();
        dialog.refreshUI();
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

    }

    private void refreshUI() {
        list1.setModel(new ListModel() {
            @Override
            public int getSize() {
                return left.size();
            }

            @Override
            public Object getElementAt(int index) {
                return left.get(index);
            }

            @Override
            public void addListDataListener(ListDataListener l) {

            }

            @Override
            public void removeListDataListener(ListDataListener l) {

            }
        });
        selectedList.setModel(new ListModel() {
            @Override
            public int getSize() {
                return right.size();
            }

            @Override
            public Object getElementAt(int index) {
                return right.get(index);
            }

            @Override
            public void addListDataListener(ListDataListener l) {

            }

            @Override
            public void removeListDataListener(ListDataListener l) {

            }
        });
        reactUI();
    }

    public void reactUI() {
        boolean has_selected = false;
        for (JCheckBox c : variantsCheckboxes) {
            if (c.isSelected()) {
                has_selected = true;
            }
        }
        btnAdd.setEnabled(list1.getSelectedIndex() >= 0);
        btnRemove.setEnabled(selectedList.getSelectedIndex() >= 0);
        buttonOK.setEnabled(selectedList.getModel().getSize() > 0 && has_selected && cboBrowser.getSelectedIndex() >= 0);
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
        mainPanel.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1));
        mainPanel.setPreferredSize(new Dimension(640, 480));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(panel1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
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
        mainPanel.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.6;
        gbc.fill = GridBagConstraints.BOTH;
        panel3.add(panel4, gbc);
        panel4.setBorder(BorderFactory.createTitledBorder(null, "Test Cases", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        list1 = new JList();
        panel4.add(list1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridBagLayout());
        panel4.add(panel5, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnAdd = new JButton();
        btnAdd.setPreferredSize(new Dimension(30, 30));
        btnAdd.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        panel5.add(btnAdd, gbc);
        btnRemove = new JButton();
        btnRemove.setPreferredSize(new Dimension(30, 30));
        btnRemove.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        panel5.add(btnRemove, gbc);
        selectedList = new JList();
        panel4.add(selectedList, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        configPanel = new JPanel();
        configPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        configPanel.setAlignmentX(0.0f);
        configPanel.setAlignmentY(0.0f);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.3;
        gbc.fill = GridBagConstraints.BOTH;
        panel3.add(configPanel, gbc);
        configPanel.setBorder(BorderFactory.createTitledBorder(null, "Configurations", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weighty = 0.1;
        gbc.fill = GridBagConstraints.BOTH;
        panel3.add(panel6, gbc);
        panel6.setBorder(BorderFactory.createTitledBorder(null, "Browser", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        cboBrowser = new JComboBox();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel6.add(cboBrowser, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel6.add(spacer2, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}

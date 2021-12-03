package app.seleniumap.ui.dialogs;

import app.seleniumap.App;
import app.seleniumap.models.test.TestStep;
import app.seleniumap.models.test.WebEntity;
import app.seleniumap.models.test.webentities.*;
import app.seleniumap.ui.MainTreeCellRenderer;
import app.seleniumap.ui.misc.Icons;
import app.seleniumap.utils.ClassUtil;
import app.seleniumap.utils.FilesUtil;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WebEntitySelector extends JDialog {
    public static TestStep selected;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTree tree;
    private JComboBox cboAction;
    private JTextField txtData;

    public WebEntitySelector() {
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
        tree.setModel(new TreeModel() {
            @Override
            public Object getRoot() {
                return new File(App.testProject.getLocation() + "/Library");
            }

            @Override
            public Object getChild(Object parent, int index) {
                return FilesUtil.getViewableFiles(((File) parent).getAbsolutePath()).get(index);
            }

            @Override
            public int getChildCount(Object parent) {
                return FilesUtil.getViewableFiles(((File) parent).getAbsolutePath()).size();
            }

            @Override
            public boolean isLeaf(Object node) {
                return false;
            }

            @Override
            public void valueForPathChanged(TreePath path, Object newValue) {

            }

            @Override
            public int getIndexOfChild(Object parent, Object child) {
                return 0;
            }

            @Override
            public void addTreeModelListener(TreeModelListener l) {

            }

            @Override
            public void removeTreeModelListener(TreeModelListener l) {

            }
        });
        tree.setCellRenderer(new MainTreeCellRenderer());
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                File f = (File) tree.getSelectionPath().getLastPathComponent();
                WebEntity we = new WebEntity();
                we.initialize(f.getAbsolutePath(), "default");
                String type = we.getType();
                List<String> actions = new ArrayList<String>();
                Class cls = null;
                if (type.equals(WebEntity.TYPES.DOMAIN.toString())) {
                    cls = Domain.class;
                } else if (type.equals(WebEntity.TYPES.PAGE.toString())) {
                    cls = Page.class;
                } else if (type.equals(WebEntity.TYPES.SECTION.toString())) {
                    cls = Section.class;
                } else if (type.equals(WebEntity.TYPES.CLICKABLE.toString())) {
                    cls = Clickable.class;
                } else if (type.equals(WebEntity.TYPES.TEXTBOX.toString())) {
                    cls = TextBox.class;
                } else if (type.equals(WebEntity.TYPES.RADIOBOX.toString())) {
                    cls = RadioButton.class;
                } else if (type.equals(WebEntity.TYPES.CHECKBOX.toString())) {
                    cls = CheckBox.class;
                } else if (type.equals(WebEntity.TYPES.COLLECTION.toString())) {

                }
                DefaultComboBoxModel cbm = new DefaultComboBoxModel();
                for (String s : ClassUtil.getEntityActions(cls)) {
                    cbm.addElement(s);
                }
                cboAction.setModel(cbm);
            }
        });
    }

    private void onOK() {

        String selectedPath = ((File) tree.getSelectionPath().getLastPathComponent()).getAbsolutePath().replace(App.testProject.getLocation(), "");
        selected = new TestStep(selectedPath, (String) cboAction.getSelectedItem(), txtData.getText());
        dispose();
    }

    private void onCancel() {
        selected = null;
        dispose();
    }

    public static void showDialog() {
        WebEntitySelector dialog = new WebEntitySelector();
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
        contentPane.setPreferredSize(new Dimension(640, 480));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
        panel1.add(panel2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonOK = new JButton();
        buttonOK.setText("Select");
        panel2.add(buttonOK, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonCancel = new JButton();
        buttonCancel.setText("Cancel");
        panel2.add(buttonCancel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(6, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        tree = new JTree();
        panel3.add(tree, new GridConstraints(0, 0, 6, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel3.add(spacer2, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Action:");
        panel3.add(label1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel3.add(spacer3, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Data");
        panel3.add(label2, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtData = new JTextField();
        panel3.add(txtData, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        cboAction = new JComboBox();
        panel3.add(cboAction, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}

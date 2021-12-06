package app.roam.se.ui.dialogs;

import app.roam.se.models.test.TestData;
import app.roam.se.ui.common.UIUtil;
import app.roam.se.ui.misc.Icons;
import app.roam.se.utils.DBUtil;
import app.roam.se.utils.TextUtil;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class DataEditorDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox cboCat;
    private JComboBox cboSub;
    private JComboBox cboGrp;
    private JTextField txtVal;
    private JComboBox cboLbl;
    private boolean isNew = true;
    private int targetId;

    public DataEditorDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

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
        refreshUI();
        setIconImage(Icons.getLogo());
    }

    public void refreshUI() {
        buttonOK.setText(isNew ? "Add" : "Save");
        List<TestData> tds = DBUtil.getAllData();
        List<String> cat = new ArrayList<String>();
        List<String> sub = new ArrayList<String>();
        List<String> grp = new ArrayList<String>();
        List<String> lbl = new ArrayList<String>();
        for (TestData t : tds) {
            cat.add(t.getCategory());
            sub.add(t.getSubCategory());
            grp.add(t.getDataGroup());
            lbl.add(t.getLabel());
        }
        cat = TextUtil.removeDuplicates(cat);
        sub = TextUtil.removeDuplicates(sub);
        grp = TextUtil.removeDuplicates(grp);
        lbl = TextUtil.removeDuplicates(lbl);
        cboCat.setModel(new DefaultComboBoxModel(cat.toArray()));
        cboSub.setModel(new DefaultComboBoxModel(sub.toArray()));
        cboGrp.setModel(new DefaultComboBoxModel(grp.toArray()));
        cboLbl.setModel(new DefaultComboBoxModel(lbl.toArray()));
        if (!isNew) {
            TestData td = DBUtil.getData(targetId);
            cboCat.setSelectedItem(td.getCategory());
            cboSub.setSelectedItem(td.getSubCategory());
            cboGrp.setSelectedItem(td.getDataGroup());
            cboLbl.setSelectedItem(td.getLabel());
            txtVal.setText(td.getValue());
        }


    }

    private void onOK() {
        if (cboCat.getSelectedItem() == null
                || cboSub.getSelectedItem() == null
                || cboGrp.getSelectedItem() == null
                || cboLbl.getSelectedItem() == null

                || cboCat.getSelectedItem().toString().isEmpty()
                || cboSub.getSelectedItem().toString().isEmpty()
                || cboGrp.getSelectedItem().toString().isEmpty()
                || cboLbl.getSelectedItem().toString().isEmpty()
                || txtVal.getText().isEmpty()) {
            UIUtil.showErrorMessage(null, "Error data", "Please provide entry entries for all fields!");
        } else {

            TestData td = new TestData();
            td.setCategory((String) cboCat.getSelectedItem());
            td.setSubCategory((String) cboSub.getSelectedItem());
            td.setDataGroup((String) cboGrp.getSelectedItem());
            td.setLabel((String) cboLbl.getSelectedItem());
            td.setValue((String) txtVal.getText());
            td.setId(targetId);
            if (isNew) {
                UIUtil.showSuccessDialog(null, "Table Insert Status", DBUtil.insertData(td));
            } else {
                UIUtil.showSuccessDialog(null, "Table Update Status", DBUtil.updateData(td));
            }
            dispose();
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void showDialog(int dataID) {
        DataEditorDialog dialog = new DataEditorDialog();
        dialog.isNew = dataID < 0;
        dialog.targetId = dataID;
        dialog.refreshUI();

        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setTitle(dialog.isNew ? "Create Test Data" : "Modify Test Data");
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
        panel3.setLayout(new GridLayoutManager(5, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Category:");
        panel3.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTHEAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Sub Category:");
        panel3.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_NORTHEAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Data Group:");
        panel3.add(label3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_NORTHEAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Label:");
        panel3.add(label4, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_NORTHEAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Value:");
        panel3.add(label5, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_NORTHEAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cboCat = new JComboBox();
        cboCat.setEditable(true);
        panel3.add(cboCat, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        cboSub = new JComboBox();
        cboSub.setEditable(true);
        panel3.add(cboSub, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        cboGrp = new JComboBox();
        cboGrp.setEditable(true);
        panel3.add(cboGrp, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtVal = new JTextField();
        panel3.add(txtVal, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        cboLbl = new JComboBox();
        cboLbl.setEditable(true);
        panel3.add(cboLbl, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}

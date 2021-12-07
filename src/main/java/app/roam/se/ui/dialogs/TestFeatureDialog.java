package app.roam.se.ui.dialogs;

import app.roam.se.models.test.TestFeature;
import app.roam.se.ui.common.UIUtil;
import app.roam.se.ui.misc.Icons;
import app.roam.se.utils.FilesUtil;
import app.roam.se.utils.TextUtil;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class TestFeatureDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField txtFeatureName;
    private JTextArea txtDescription;
    private JLabel lblVariant;
    private String path;
    private boolean isNew = true;
    private boolean isVariant = false;

    public TestFeatureDialog() {
        setIconImage(Icons.getLogo());
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
    }

    private void onOK() {
        if (!TextUtil.isValidName(txtFeatureName.getText())) {
            UIUtil.showErrorMessage(null, "Invalid Feature Name", "Feature name provided is invalid!");
        } else {
            if (isVariant) {
                if (isNew) {
                    TestFeature tf = new TestFeature();
                    tf.setName(txtFeatureName.getText());
                    tf.setDescription(txtDescription.getText());
                    tf.saveTestFeature(path, tf.getName());
                } else {
                    new File(path).delete();
                    currentTestFeature.setName(txtFeatureName.getText());
                    currentTestFeature.setDescription(txtDescription.getText());
                    currentTestFeature.saveTestFeature(path, txtFeatureName.getText());
                }
            } else {
                if (isNew) {
                    TestFeature tf = new TestFeature();
                    tf.setName(txtFeatureName.getText());
                    tf.setDescription(txtDescription.getText());
                    FilesUtil.checkFolder(path + "/" + txtFeatureName.getText());
                    tf.saveTestFeature(path + "/" + txtFeatureName.getText(), "default");
                } else {
                    String originalPath = new File(path).getParent();
                    new File(path).renameTo(new File(originalPath + "/" + txtFeatureName.getText()));

                    currentTestFeature.setName(txtFeatureName.getText());
                    currentTestFeature.setDescription(txtDescription.getText());
                    currentTestFeature.saveTestFeature(originalPath + "/" + txtFeatureName.getText(), "default");
                }
            }


        }
        dispose();
    }

    private void onCancel() {

        dispose();
    }

    public static void showDialog(String path, boolean variant) {
        TestFeatureDialog dialog = new TestFeatureDialog();
        dialog.path = path;
        dialog.pack();
        dialog.isNew = new File(path).isDirectory();
        dialog.isVariant = variant;
        dialog.setTitle(variant ? (dialog.isNew ? "New" : "Modify") + " Test Feature Variant " : (dialog.isNew ? "New" : "Modify") + " Test Feature");
        dialog.refreshDialog();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

    }

    TestFeature currentTestFeature;

    private void refreshDialog() {
        isNew = new File(path).isDirectory();
        buttonOK.setText(isNew ? "Create" : "Save");
        lblVariant.setText(isVariant ? "Variant Name:" : "Feature Name:");

        if (!isNew) {
            currentTestFeature = new TestFeature();
            currentTestFeature.initialize(path);
            txtFeatureName.setText(currentTestFeature.getName());
            txtDescription.setText(currentTestFeature.getDescription());
        }
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
        contentPane.setPreferredSize(new Dimension(436, 200));
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
        panel3.setLayout(new GridBagLayout());
        contentPane.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        txtFeatureName = new JTextField();
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(txtFeatureName, gbc);
        lblVariant = new JLabel();
        lblVariant.setPreferredSize(new Dimension(300, 200));
        lblVariant.setText("Feature Name:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panel3.add(lblVariant, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel3.add(spacer2, gbc);
        final JLabel label1 = new JLabel();
        label1.setText("Description:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(label1, gbc);
        txtDescription = new JTextArea();
        txtDescription.setLineWrap(true);
        txtDescription.setPreferredSize(new Dimension(1, 80));
        txtDescription.setWrapStyleWord(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.BOTH;
        panel3.add(txtDescription, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}
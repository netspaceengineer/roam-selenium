package app.roam.se.ui.tabs;


import app.roam.se.ui.common.UIUtil;
import app.roam.se.ui.misc.Icons;
import app.roam.se.ui.misc.Theme;
import app.roam.se.App;
import app.roam.se.models.test.WebEntity;
import app.roam.se.ui.dialogs.QuickObjectTestDialog;
import lombok.Data;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

@Data
public class WebEntityTab {
    private JPanel mainPanel;
    private JButton btnAdd;
    private JButton btnModify;
    private JButton btnDelete;
    private JButton btnClone;

    private JList variantList;
    private JTextField txtVariant;

    private JTextArea txtLocation;

    private JButton btnCancel;
    private JLabel lblType;
    private JButton btnTestObject;
    private JButton btnRefresh;
    private String path = "";
    private int mode = 0;
    WebEntity entity;

    public WebEntityTab(String path) {
        this.path = path;
        refreshUI();
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (mode == 0) {
                    mode = 1;
                } else {
                    if (txtVariant.getText().toString().isEmpty() || txtVariant.getText().contains("default")) {
                        UIUtil.showErrorMessage(null, "Invalid Variant name!", "Variant cannot be blank or same as default variant");
                    } else {
                        boolean valid = true;
                        for (WebEntity we : entity.variants) {
                            if (we.getName().equals(txtVariant.getText())) {
                                valid = false;
                            }

                        }
                        if (!valid) {
                            UIUtil.showErrorMessage(null, "Duplicate Variant Name", "You cannot use existing variant name!");
                        } else {
                            WebEntity newWebEntity = new WebEntity();
                            newWebEntity.setName(txtVariant.getText());
                            newWebEntity.setType(lblType.getText());
                            newWebEntity.setLocation(txtLocation.getText());


                            newWebEntity.saveVariant(path, txtVariant.getText());
                            refreshUI();
                            mode = 0;
                        }
                    }

                }
                reactUI();

            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mode = 0;
                reactUI();
            }
        });
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WebEntity targetEntity = (WebEntity) variantList.getSelectedValue();
                if (UIUtil.showYesNoDialog(null, "Confirm Deletion", "Are you sure to delete variant '" + targetEntity.getName() + "'?")) {
                    WebEntity.deleteVariant(path, targetEntity.getName());
                    refreshUI();
                }

            }
        });
        btnModify.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (mode == 0) {
                    mode = 2;
                } else {

                    WebEntity newWebEntity = new WebEntity();
                    newWebEntity.setName(txtVariant.getText());
                    newWebEntity.setType(lblType.getText());

                    newWebEntity.setLocation(txtLocation.getText());


                    newWebEntity.saveVariant(path, variantList.getSelectedIndex() == 0 ? "default" : txtVariant.getText());
                    refreshUI();
                    mode = 0;

                }
                reactUI();
            }
        });
        btnClone.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WebEntity entityToClone = (WebEntity) variantList.getSelectedValue();
                String variantName = UIUtil.showInputDialog(null, "Specify name for new clone variant:");
                boolean valid = true;
                for (WebEntity we : entity.variants) {
                    if (we.getName().equals(variantName)) {
                        valid = false;
                    }

                }
                if (variantName.contains("default")) {
                    valid = false;
                }
                if (!valid) {
                    UIUtil.showErrorMessage(null, "Duplicate Variant Name", "You cannot use existing variant name!");
                } else {
                    entityToClone.setName(variantName);
                    entityToClone.saveVariant(path, variantName);
                    refreshUI();
                }

            }
        });
        btnTestObject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                QuickObjectTestDialog.showDialog(path, ((WebEntity) variantList.getSelectedValue()).getVariant());
            }
        });
        btnRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reactUI();
            }
        });
    }

    private void reactUI() {
        btnAdd.setIcon(mode == 1 ? Icons.save : Icons.add);
        btnModify.setIcon(mode == 2 ? Icons.save : Icons.modify);
        btnDelete.setIcon(Icons.delete);
        btnClone.setIcon(Icons.clone);
        btnRefresh.setIcon(Icons.refresh);
        btnTestObject.setIcon(Icons.testObject);
        btnTestObject.setEnabled(App.webDriver != null && variantList.getSelectedIndex() >= 0 && mode == 0);
        btnCancel.setIcon(Icons.close);
        btnCancel.setVisible(mode > 0);
        btnAdd.setEnabled(mode == 0 || mode == 1);
        btnModify.setEnabled((mode == 0 || mode == 2) && variantList.getSelectedIndex() >= 0);
        btnDelete.setEnabled(mode == 0 && variantList.getSelectedIndex() > 0);
        btnClone.setEnabled(mode == 0 && variantList.getSelectedIndex() >= 0);

        txtVariant.setEditable(mode > 0);
        txtLocation.setEditable(mode > 0);

        variantList.setEnabled(mode == 0);


        if (mode == 0) {
            if (variantList.getSelectedValue() != null) {
                WebEntity webEntity = (WebEntity) variantList.getSelectedValue();
                txtVariant.setText(webEntity.getName());
                lblType.setText(webEntity.getType());

                txtLocation.setText(webEntity.getLocation());

            }
        } else if (mode == 1) {
            txtVariant.setText("");


            txtLocation.setText("");

        }


    }

    private void refreshUI() {
        if (new File(path).exists()) {
            entity = new WebEntity();
            entity.initializeAll(path);
        }


        variantList.setModel(new ListModel() {
            @Override
            public int getSize() {
                if (entity.getVariants() == null) {
                    return 1;
                }
                return entity.getVariants().size() + 1;
            }

            @Override
            public Object getElementAt(int index) {
                if (index == 0) {
                    return entity;
                } else {
                    return entity.getVariants().get(index - 1);
                }
            }

            @Override
            public void addListDataListener(ListDataListener l) {

            }

            @Override
            public void removeListDataListener(ListDataListener l) {

            }
        });
        variantList.setCellRenderer(new ListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                WebEntity webEntity = (WebEntity) value;

                JButton label = new JButton(index == 0 ? "default" : webEntity.getName());
                label.setAlignmentX(0);
                label.setBackground(isSelected ? Theme.selectedElementColor : Theme.defaultColor);
                switch (webEntity.getType()) {
                    case "domain":
                        label.setIcon(Icons.domain);
                        break;
                    case "page":
                        label.setIcon(Icons.page);
                        break;
                    case "section":
                        label.setIcon(Icons.section);
                        break;
                    case "clickable":
                        label.setIcon(Icons.clickable);
                        break;
                    case "textbox":
                        label.setIcon(Icons.textBox);
                        break;
                    case "checkbox":
                        label.setIcon(Icons.checkBox);
                        break;
                    case "radiobox":
                        label.setIcon(Icons.radioBox);
                        break;
                    case "dropdown":
                        label.setIcon(Icons.dropdown);
                        break;
                    case "collection":
                        label.setIcon(Icons.collection);
                        break;
                }
                return label;
            }
        });
        variantList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                reactUI();
            }
        });
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
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(0, 0));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        mainPanel.add(panel1, BorderLayout.NORTH);
        btnAdd = new JButton();
        btnAdd.setMaximumSize(new Dimension(30, 30));
        btnAdd.setPreferredSize(new Dimension(30, 30));
        btnAdd.setText("");
        panel1.add(btnAdd);
        btnModify = new JButton();
        btnModify.setMaximumSize(new Dimension(30, 30));
        btnModify.setPreferredSize(new Dimension(30, 30));
        btnModify.setText("");
        panel1.add(btnModify);
        btnDelete = new JButton();
        btnDelete.setMaximumSize(new Dimension(30, 30));
        btnDelete.setPreferredSize(new Dimension(30, 30));
        btnDelete.setText("");
        panel1.add(btnDelete);
        btnClone = new JButton();
        btnClone.setMaximumSize(new Dimension(30, 30));
        btnClone.setPreferredSize(new Dimension(30, 30));
        btnClone.setText("");
        panel1.add(btnClone);
        btnCancel = new JButton();
        btnCancel.setPreferredSize(new Dimension(30, 30));
        btnCancel.setText("");
        panel1.add(btnCancel);
        final JSeparator separator1 = new JSeparator();
        separator1.setOrientation(1);
        separator1.setPreferredSize(new Dimension(3, 30));
        panel1.add(separator1);
        btnTestObject = new JButton();
        btnTestObject.setPreferredSize(new Dimension(30, 30));
        btnTestObject.setText("");
        panel1.add(btnTestObject);
        btnRefresh = new JButton();
        btnRefresh.setPreferredSize(new Dimension(30, 30));
        btnRefresh.setText("");
        panel1.add(btnRefresh);
        final JSplitPane splitPane1 = new JSplitPane();
        splitPane1.setDividerLocation(150);
        mainPanel.add(splitPane1, BorderLayout.CENTER);
        variantList = new JList();
        splitPane1.setLeftComponent(variantList);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridBagLayout());
        panel2.setAlignmentY(0.0f);
        panel2.setMinimumSize(new Dimension(154, 69));
        panel2.setRequestFocusEnabled(true);
        panel2.putClientProperty("html.disable", Boolean.FALSE);
        splitPane1.setRightComponent(panel2);
        txtVariant = new JTextField();
        txtVariant.setEditable(false);
        txtVariant.setEnabled(true);
        txtVariant.setText("");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(txtVariant, gbc);
        final JLabel label1 = new JLabel();
        label1.setText("Variant Name:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panel2.add(label1, gbc);
        final JLabel label2 = new JLabel();
        label2.setText("Location:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        panel2.add(label2, gbc);
        txtLocation = new JTextArea();
        txtLocation.setEditable(false);
        txtLocation.setEnabled(true);
        txtLocation.setLineWrap(true);
        txtLocation.setPreferredSize(new Dimension(340, 100));
        txtLocation.setWrapStyleWord(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.BOTH;
        panel2.add(txtLocation, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel2.add(spacer1, gbc);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        panel2.add(panel3, gbc);
        final JLabel label3 = new JLabel();
        label3.setText("Type:");
        panel3.add(label3);
        lblType = new JLabel();
        lblType.setText("Label");
        panel3.add(lblType);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}

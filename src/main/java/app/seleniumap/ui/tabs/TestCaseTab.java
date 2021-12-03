package app.seleniumap.ui.tabs;

import app.seleniumap.App;
import app.seleniumap.models.test.TestCase;
import app.seleniumap.models.test.TestFeature;
import app.seleniumap.ui.common.UIUtil;
import app.seleniumap.ui.dialogs.TestFeatureSelector;
import app.seleniumap.ui.misc.Icons;
import app.seleniumap.ui.misc.Theme;
import app.seleniumap.ui.views.FeatureListItem;
import app.seleniumap.utils.FilesUtil;
import lombok.Data;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

@Data
public class TestCaseTab {
    private JPanel mainPanel;
    private JButton btnAddFeature;
    private JTextArea txtDescription;
    private JButton btnSaveDescription;
    private JButton btnDeleteFeature;
    private JButton btnSortUp;
    private JButton btnSortDown;
    private JButton btnPlayFeature;
    private JList variantList;
    private JList featureList;
    private JButton btnAdd;
    private JButton btnDelete;
    private JButton btnClone;
    private JButton btnRename;
    private JButton btnTestCasePlay;
    private String location;
    private TestCase currentSelectedTestCase;

    public TestCaseTab(String path) {
        setLocation(path);
        refreshUI();
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newVariantName = UIUtil.showInputDialog(null, "Specify variant name:");
                if (new File(location + "/" + newVariantName + ".json").exists()) {
                    UIUtil.showErrorMessage(null, "Error Variant Name", "Variant name already exist!");
                } else if (!newVariantName.equals("")) {
                    TestCase testCase = new TestCase();
                    testCase.setName(newVariantName);
                    testCase.setDescription("");
                    testCase.saveTestCase(getLocation(), newVariantName);
                    refreshUI();
                }
            }
        });
        btnRename.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newVariantName = UIUtil.showInputDialog(null, "Specify new name for variant '" + currentSelectedTestCase.getName() + "':");
                if (new File(location + "/" + newVariantName + ".json").exists()) {
                    UIUtil.showErrorMessage(null, "Error Variant Name", "Variant name already exist!");
                } else if (!newVariantName.equals("")) {
                    File targetFile = new File(location + "/" + newVariantName + ".json");
                    new File((String) variantList.getSelectedValue()).renameTo(targetFile);
                    TestCase tc = new TestCase();
                    tc.initialize(targetFile.getAbsolutePath());
                    tc.setName(newVariantName);
                    tc.saveTestCase(targetFile.getParent(), newVariantName);

                    refreshUI();
                }
            }
        });
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File targetFile = new File((String) variantList.getSelectedValue());
                if (UIUtil.showYesNoDialog(null, "Delete Test Case Variant", "You are about to delete Test Case Variant '" + FilesUtil.getFileName((String) variantList.getSelectedValue()) + "'. Click 'Yes' to confirm.")) {
                    targetFile.delete();
                    refreshUI();
                }
            }
        });
        btnClone.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String newTestCaseName = UIUtil.showInputDialog(null, "You are about to clone a variant! Specify name for new variant '" + FilesUtil.getFileName((String) variantList.getSelectedValue()) + "':");
                if (new File(location + "/" + newTestCaseName + ".json").exists()) {
                    UIUtil.showErrorMessage(null, "Error Variant Name", "Variant name already exist!");
                } else if (!newTestCaseName.equals("")) {
                    TestCase tc = new TestCase();
                    tc.initialize((String) variantList.getSelectedValue());
                    tc.setName(newTestCaseName);
                    tc.saveTestCase(new File((String) variantList.getSelectedValue()).getParent(), newTestCaseName);
                    refreshUI();
                }
            }
        });
        txtDescription.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                btnSaveDescription.setVisible(currentSelectedTestCase != null && !currentSelectedTestCase.getDescription().equals(txtDescription));

            }
        });
        btnSaveDescription.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentSelectedTestCase.setDescription(txtDescription.getText());
                currentSelectedTestCase.saveTestCase((String) variantList.getSelectedValue());

                reactUI();
            }
        });
        btnAddFeature.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TestFeatureSelector.showDialog();
                if (TestFeatureSelector.selected != null) {
                    currentSelectedTestCase.addPath(TestFeatureSelector.selected);
                    currentSelectedTestCase.saveTestCase((String) variantList.getSelectedValue());
                    reactUI();
                    refreshFeatures();
                }
            }
        });
        btnDeleteFeature.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (UIUtil.showYesNoDialog(null, "Delete Feature Step", "You are about to delete step '" + (featureList.getSelectedIndex() + 1) + "'. Press 'Yes' to confirm.")) {
                    currentSelectedTestCase.removePath(featureList.getSelectedIndex());
                    currentSelectedTestCase.saveTestCase((String) variantList.getSelectedValue());
                    reactUI();
                    refreshFeatures();
                }

            }
        });
        featureList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                reactUI();

            }
        });
        btnSortUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = featureList.getSelectedIndex();
                currentSelectedTestCase.sortUp(index);
                currentSelectedTestCase.saveTestCase((String) variantList.getSelectedValue());
                refreshFeatures();
                featureList.setSelectedIndex(index - 1);
            }
        });
        btnSortDown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = featureList.getSelectedIndex();
                currentSelectedTestCase.sortDown(index);
                currentSelectedTestCase.saveTestCase((String) variantList.getSelectedValue());
                refreshFeatures();
                featureList.setSelectedIndex(index + 1);
            }
        });
    }

    public void refreshUI() {
        btnAdd.setIcon(Icons.add);
        btnDelete.setIcon(Icons.delete);
        btnRename.setIcon(Icons.modify);
        btnTestCasePlay.setIcon(Icons.steps);
        btnClone.setIcon(Icons.clone);
        btnAddFeature.setIcon(Icons.add);
        btnDeleteFeature.setIcon(Icons.delete);
        btnSortUp.setIcon(Icons.sortUp);
        btnSortDown.setIcon(Icons.sortDown);
        btnPlayFeature.setIcon(Icons.step);
        btnSaveDescription.setIcon(Icons.save);
        btnSaveDescription.setVisible(false);

        variantList.setModel(new ListModel() {
            @Override
            public int getSize() {
                return FilesUtil.getNonDefaultFiles(location).size() + 1;
            }

            @Override
            public Object getElementAt(int index) {
                if (index == 0) {
                    return location + "/default.json";
                } else {
                    return FilesUtil.getNonDefaultFiles(location).get(index - 1).getAbsolutePath();
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
                String name = FilesUtil.getFileName((String) value);
                JButton label = new JButton(name);
                label.setBackground(isSelected ? Theme.selectedElementColor : Theme.defaultColor);
                return label;
            }
        });
        variantList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (variantList.getSelectedIndex() >= 0) {
                    currentSelectedTestCase = new TestCase();
                    currentSelectedTestCase.initialize((String) variantList.getSelectedValue());
                    txtDescription.setText(currentSelectedTestCase.getDescription());
                    featureList.setModel(new ListModel() {
                        @Override
                        public int getSize() {
                            return currentSelectedTestCase.getFeaturePaths().size();
                        }

                        @Override
                        public Object getElementAt(int index) {
                            return currentSelectedTestCase.getFeaturePaths().get(index);
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
            }

        });
        featureList.setCellRenderer(new ListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                FeatureListItem featureListItem = new FeatureListItem();
                TestFeature testFeature = new TestFeature();
                testFeature.initialize(App.testProject.getLocation() + "/" + (String) value + "/" + App.testProject.getCurrentVariant());
                featureListItem.getLblSequence().setText(String.valueOf(index + 1));
                featureListItem.getLblpath().setText("Location: " + (String) value);
                featureListItem.getLblName().setText(new File((String) value).getName());
                featureListItem.getLblDescription().setText(testFeature.getDescription().equals("") ? "<No Description>" : testFeature.getDescription());
                featureListItem.getMainPanel().setBackground(isSelected ? Theme.selectedElementColor : Theme.defaultColor);
                return featureListItem.getMainPanel();
            }
        });
        reactUI();
    }

    private void reactUI() {

        btnRename.setEnabled(variantList.getSelectedIndex() > 0);
        btnDelete.setEnabled(variantList.getSelectedIndex() > 0);
        btnClone.setEnabled(variantList.getSelectedIndex() >= 0);
        btnAddFeature.setEnabled(variantList.getSelectedIndex() >= 0);
        btnDeleteFeature.setEnabled(featureList.getSelectedIndex() >= 0);
        btnSortUp.setEnabled(featureList.getSelectedIndex() > 0 && featureList.getModel().getSize() > 1);
        btnSortDown.setEnabled(featureList.getSelectedIndex() < featureList.getModel().getSize() - 1 && featureList.getModel().getSize() > 1);
        txtDescription.setEnabled(currentSelectedTestCase != null);
        btnSaveDescription.setVisible(currentSelectedTestCase != null && !currentSelectedTestCase.getDescription().equals(txtDescription.getText()));

    }

    public void refreshFeatures() {
        if (currentSelectedTestCase != null) {
            featureList.setModel(new ListModel() {
                @Override
                public int getSize() {
                    return currentSelectedTestCase.getFeaturePaths().size();
                }

                @Override
                public Object getElementAt(int index) {
                    return currentSelectedTestCase.getFeaturePaths().get(index);
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
        final JSplitPane splitPane1 = new JSplitPane();
        splitPane1.setDividerLocation(150);
        mainPanel.add(splitPane1, BorderLayout.CENTER);
        final JScrollPane scrollPane1 = new JScrollPane();
        splitPane1.setLeftComponent(scrollPane1);
        variantList = new JList();
        scrollPane1.setViewportView(variantList);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout(0, 0));
        splitPane1.setRightComponent(panel1);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridBagLayout());
        panel1.add(panel2, BorderLayout.EAST);
        btnAddFeature = new JButton();
        btnAddFeature.setPreferredSize(new Dimension(30, 30));
        btnAddFeature.setText("");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel2.add(btnAddFeature, gbc);
        btnDeleteFeature = new JButton();
        btnDeleteFeature.setPreferredSize(new Dimension(30, 30));
        btnDeleteFeature.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(btnDeleteFeature, gbc);
        btnSortUp = new JButton();
        btnSortUp.setPreferredSize(new Dimension(30, 30));
        btnSortUp.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(btnSortUp, gbc);
        btnSortDown = new JButton();
        btnSortDown.setPreferredSize(new Dimension(30, 30));
        btnSortDown.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(btnSortDown, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel2.add(spacer1, gbc);
        btnPlayFeature = new JButton();
        btnPlayFeature.setPreferredSize(new Dimension(30, 30));
        btnPlayFeature.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(btnPlayFeature, gbc);
        final JSeparator separator1 = new JSeparator();
        separator1.setPreferredSize(new Dimension(30, 3));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.BOTH;
        panel2.add(separator1, gbc);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new BorderLayout(0, 0));
        panel1.add(panel3, BorderLayout.NORTH);
        panel3.setBorder(BorderFactory.createTitledBorder(null, "Test Case Description:", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        txtDescription = new JTextArea();
        txtDescription.setPreferredSize(new Dimension(1, 80));
        txtDescription.setVerifyInputWhenFocusTarget(false);
        txtDescription.setWrapStyleWord(true);
        panel3.add(txtDescription, BorderLayout.CENTER);
        btnSaveDescription = new JButton();
        btnSaveDescription.setText("");
        panel3.add(btnSaveDescription, BorderLayout.EAST);
        featureList = new JList();
        panel1.add(featureList, BorderLayout.CENTER);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        mainPanel.add(panel4, BorderLayout.NORTH);
        btnAdd = new JButton();
        btnAdd.setHorizontalAlignment(0);
        btnAdd.setPreferredSize(new Dimension(30, 30));
        btnAdd.setText("");
        panel4.add(btnAdd);
        btnRename = new JButton();
        btnRename.setPreferredSize(new Dimension(30, 30));
        btnRename.setText("");
        panel4.add(btnRename);
        btnDelete = new JButton();
        btnDelete.setPreferredSize(new Dimension(30, 30));
        btnDelete.setText("");
        panel4.add(btnDelete);
        btnClone = new JButton();
        btnClone.setPreferredSize(new Dimension(30, 30));
        btnClone.setText("");
        panel4.add(btnClone);
        final JSeparator separator2 = new JSeparator();
        separator2.setOrientation(1);
        separator2.setPreferredSize(new Dimension(3, 30));
        panel4.add(separator2);
        btnTestCasePlay = new JButton();
        btnTestCasePlay.setPreferredSize(new Dimension(30, 30));
        btnTestCasePlay.setText("");
        btnTestCasePlay.setVisible(false);
        panel4.add(btnTestCasePlay);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}

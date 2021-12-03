package app.seleniumap.ui.tabs;

import app.seleniumap.App;
import app.seleniumap.misc.StepThreadRunner;
import app.seleniumap.models.reports.Result;
import app.seleniumap.models.test.TestFeature;
import app.seleniumap.models.test.TestStep;
import app.seleniumap.models.test.WebEntity;
import app.seleniumap.ui.common.UIUtil;
import app.seleniumap.ui.dialogs.WebEntitySelector;
import app.seleniumap.ui.misc.Icons;
import app.seleniumap.ui.misc.Theme;
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
public class TestFeatureTab {
    private JPanel mainPanel;
    private JList variantList;
    private JButton btnAddVariant;
    private JButton btnAddStep;
    private JButton btnRenameVariant;
    private JButton btnDeleteVariant;
    private JButton btnCloneVariant;
    private JButton btnDeleteStep;
    private JButton btnSortUp;
    private JButton btnSortDown;
    private JButton btnPlaySteps;
    private JButton btnPlayStep;
    private JList stepList;
    private JTextArea txtDescription;
    private JButton btnSaveDescription;
    private JButton btnRefresh;
    private String path;
    private TestFeature selectedVariantTestFeature;

    public TestFeatureTab(String path) {
        this.path = path;
        refreshUI();
        btnAddVariant.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newVariant = UIUtil.showInputDialog(null, "Specify Test Feature Variant name:");
                if (!newVariant.isEmpty()) {
                    if (new File(path + "/" + newVariant + ".json").exists()) {
                        UIUtil.showErrorMessage(null, "Invalid variant name", "Variant with a name '" + newVariant + "' already exists!");
                    } else {
                        TestFeature testFeatureVariant = new TestFeature();
                        testFeatureVariant.setName(newVariant);
                        testFeatureVariant.saveTestFeature(path, newVariant);
                        refreshUI();
                    }
                }
            }
        });
        btnRenameVariant.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File variantFile = new File((String) variantList.getSelectedValue());
                String newVariantName = UIUtil.showInputDialog(null, "Specify new name for variant '" + FilesUtil.getFileName(variantFile.getAbsolutePath()) + "':");
                if (!newVariantName.isEmpty()) {
                    if (new File(variantFile.getParent() + "/" + newVariantName + ".json").exists()) {
                        UIUtil.showErrorMessage(null, "Invalid variant name", "Variant with a name '" + newVariantName + "' already exists!");
                    } else {
                        TestFeature tf = new TestFeature();
                        tf.initialize(variantFile.getAbsolutePath());
                        tf.setName(newVariantName);
                        tf.saveTestFeature(variantFile.getParent(), newVariantName);
                        variantFile.delete();
                        refreshUI();
                    }

                }
            }
        });
        btnDeleteVariant.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File variantFile = new File((String) variantList.getSelectedValue());
                if (UIUtil.showYesNoDialog(null, "Delete Test Feature Variant", "Are you sure you like to delete the Test Feature Variant '" + FilesUtil.getFileName((String) variantList.getSelectedValue()) + "'?")) {
                    variantFile.delete();
                    refreshUI();
                }
            }
        });
        btnCloneVariant.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File variantFile = new File((String) variantList.getSelectedValue());
                String newVariantName = UIUtil.showInputDialog(null, "You are about to clone a variant! Specify name for new variant '" + FilesUtil.getFileName(variantFile.getAbsolutePath()) + "':");
                if (!newVariantName.isEmpty()) {
                    if (new File(variantFile.getParent() + "/" + newVariantName + ".json").exists()) {
                        UIUtil.showErrorMessage(null, "Invalid variant name", "Variant with a name '" + newVariantName + "' already exists!");
                    } else {
                        TestFeature tf = new TestFeature();
                        tf.initialize(variantFile.getAbsolutePath());
                        tf.setName(newVariantName);
                        tf.saveTestFeature(variantFile.getParent(), newVariantName);
                        refreshUI();
                    }

                }
            }
        });
        btnAddStep.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WebEntitySelector.showDialog();
                if (WebEntitySelector.selected != null) {
                    if (selectedVariantTestFeature != null) {
                        selectedVariantTestFeature.addStep(WebEntitySelector.selected);
                        selectedVariantTestFeature.saveTestFeature(path, FilesUtil.getFileName((String) variantList.getSelectedValue()));
                        refreshStep();
                    }
                }
            }
        });
        btnDeleteStep.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (UIUtil.showYesNoDialog(null, "Confirm step deletion", "You are about to delete step " + stepList.getSelectedIndex() + 1 + ". Click 'Yes' to confirm")) {
                    selectedVariantTestFeature.removeStep(stepList.getSelectedIndex());
                    selectedVariantTestFeature.saveTestFeature(path, FilesUtil.getFileName((String) variantList.getSelectedValue()));
                    refreshStep();

                }
            }
        });
        btnSortUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = stepList.getSelectedIndex();
                selectedVariantTestFeature.sortUp(index);
                selectedVariantTestFeature.saveTestFeature(path, FilesUtil.getFileName((String) variantList.getSelectedValue()));
                refreshStep();
                stepList.setSelectedIndex(index - 1);
            }
        });
        btnSortDown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = stepList.getSelectedIndex();
                selectedVariantTestFeature.sortDown(index);
                selectedVariantTestFeature.saveTestFeature(path, FilesUtil.getFileName((String) variantList.getSelectedValue()));
                refreshStep();
                stepList.setSelectedIndex(index + 1);
            }
        });
        txtDescription.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                btnSaveDescription.setVisible(!selectedVariantTestFeature.getDescription().equals(txtDescription.getText()));

            }
        });
        btnPlayStep.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (App.webDriver != null) {
                    Thread t = new Thread(new StepThreadRunner((TestStep) stepList.getSelectedValue(), App.webDriver, App.testProject.getCurrentVariant()));
                    t.run();
                } else {
                    UIUtil.showErrorMessage(null, "Webdriver not started!", "Webdriver is not initiated or has been closed.");
                }
            }
        });
        btnRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reactUI();
            }
        });
        btnSaveDescription.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedVariantTestFeature.setDescription(txtDescription.getText());
                selectedVariantTestFeature.saveTestFeature();
                reactUI();
            }
        });
    }

    public void refreshUI() {
        btnAddVariant.setIcon(Icons.add);
        btnAddStep.setIcon(Icons.add);
        btnRenameVariant.setIcon(Icons.modify);
        btnDeleteVariant.setIcon(Icons.delete);
        btnCloneVariant.setIcon(Icons.clone);
        btnDeleteStep.setIcon(Icons.delete);
        btnSortUp.setIcon(Icons.sortUp);
        btnSortDown.setIcon(Icons.sortDown);
        btnPlaySteps.setIcon(Icons.steps);
        btnPlayStep.setIcon(Icons.step);
        btnSaveDescription.setIcon(Icons.save);
        btnRefresh.setIcon(Icons.refresh);
        btnSaveDescription.setVisible(false);

        variantList.setModel(new ListModel() {
            @Override
            public int getSize() {
                return FilesUtil.getNonDefaultFiles(path).size() + 1;
            }

            @Override
            public Object getElementAt(int index) {
                if (index == 0) {
                    return path + "/default.json";
                } else {
                    return FilesUtil.getNonDefaultFiles(path).get(index - 1).getAbsolutePath();
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
                String featurePath = (String) value;
                TestFeature testFeature = new TestFeature();
                testFeature.initialize(featurePath);
                JButton jbutton = new JButton(FilesUtil.getFileName(featurePath));
                jbutton.setBackground(isSelected ? Theme.selectedElementColor : Theme.defaultColor);

                return jbutton;
            }
        });
        variantList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

                if (variantList.getSelectedIndex() >= 0) {
                    selectedVariantTestFeature = new TestFeature();

                    selectedVariantTestFeature.initialize((String) variantList.getSelectedValue());
                    txtDescription.setText(selectedVariantTestFeature.getDescription());
                    btnSaveDescription.setVisible(!txtDescription.getText().equals(selectedVariantTestFeature.getDescription()));
                    refreshStep();
                    stepList.setCellRenderer(new ListCellRenderer() {
                        @Override
                        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                            TestStep testStep = (TestStep) value;
                            WebEntity webEntity = new WebEntity();
                            webEntity.initialize(App.testProject.getLocation() + "/" + testStep.getPath(), App.testProject.getCurrentVariant());
                            Result r = new Result(testStep.getAction(), webEntity.getName(), testStep.getData());
                            JButton label = new JButton();
                            label.setText((index + 1) + ". " + r.getStatement());
                            label.setBackground(isSelected ? Theme.selectedElementColor : Theme.defaultColor);
                            label.setBackground(isSelected ? Theme.selectedElementColor : Theme.defaultColor);
                            label.setHorizontalAlignment(SwingConstants.LEFT);
                            return label;
                        }
                    });
                    stepList.addListSelectionListener(new ListSelectionListener() {
                        @Override
                        public void valueChanged(ListSelectionEvent e) {
                            reactUI();
                        }
                    });

                    reactUI();
                }
            }
        });
        reactUI();
    }

    public void reactUI() {
        btnCloneVariant.setEnabled(variantList.getSelectedIndex() >= 0);
        btnDeleteVariant.setEnabled(variantList.getSelectedIndex() > 0);
        btnRenameVariant.setEnabled(variantList.getSelectedIndex() > 0);
        btnDeleteStep.setEnabled(stepList.getSelectedIndex() >= 0);
        btnSortDown.setEnabled(stepList.getSelectedIndex() < stepList.getModel().getSize() - 1 && stepList.getModel().getSize() > 0);
        btnSortUp.setEnabled(stepList.getSelectedIndex() > 0 && stepList.getModel().getSize() > 0);
        btnAddStep.setEnabled(selectedVariantTestFeature != null);
        btnPlayStep.setEnabled(App.webDriver != null && stepList.getSelectedValue() != null);
    }

    private void refreshStep() {
        stepList.setModel(new ListModel() {
            @Override
            public int getSize() {
                return selectedVariantTestFeature.getSteps().size();
            }

            @Override
            public Object getElementAt(int index) {
                return selectedVariantTestFeature.getSteps().get(index);
            }

            @Override
            public void addListDataListener(ListDataListener l) {

            }

            @Override
            public void removeListDataListener(ListDataListener l) {

            }
        });
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
        btnAddVariant = new JButton();
        btnAddVariant.setPreferredSize(new Dimension(30, 30));
        btnAddVariant.setText("");
        panel1.add(btnAddVariant);
        btnRenameVariant = new JButton();
        btnRenameVariant.setPreferredSize(new Dimension(30, 30));
        btnRenameVariant.setText("");
        panel1.add(btnRenameVariant);
        btnDeleteVariant = new JButton();
        btnDeleteVariant.setPreferredSize(new Dimension(30, 30));
        btnDeleteVariant.setText("");
        panel1.add(btnDeleteVariant);
        btnCloneVariant = new JButton();
        btnCloneVariant.setPreferredSize(new Dimension(30, 30));
        btnCloneVariant.setText("");
        panel1.add(btnCloneVariant);
        final JSeparator separator1 = new JSeparator();
        separator1.setOrientation(1);
        separator1.setPreferredSize(new Dimension(3, 30));
        panel1.add(separator1);
        btnPlaySteps = new JButton();
        btnPlaySteps.setPreferredSize(new Dimension(30, 30));
        btnPlaySteps.setText("");
        btnPlaySteps.setVisible(false);
        panel1.add(btnPlaySteps);
        final JSplitPane splitPane1 = new JSplitPane();
        splitPane1.setContinuousLayout(false);
        splitPane1.setDividerLocation(150);
        mainPanel.add(splitPane1, BorderLayout.CENTER);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new BorderLayout(0, 0));
        splitPane1.setRightComponent(panel2);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panel3.setPreferredSize(new Dimension(35, 35));
        panel2.add(panel3, BorderLayout.EAST);
        btnAddStep = new JButton();
        btnAddStep.setPreferredSize(new Dimension(30, 30));
        btnAddStep.setText("");
        btnAddStep.setToolTipText("Add Step");
        panel3.add(btnAddStep);
        btnDeleteStep = new JButton();
        btnDeleteStep.setPreferredSize(new Dimension(30, 30));
        btnDeleteStep.setText("");
        btnDeleteStep.setToolTipText("Delete Step");
        panel3.add(btnDeleteStep);
        btnSortUp = new JButton();
        btnSortUp.setPreferredSize(new Dimension(30, 30));
        btnSortUp.setText("");
        btnSortUp.setToolTipText("Sort Up");
        panel3.add(btnSortUp);
        btnSortDown = new JButton();
        btnSortDown.setPreferredSize(new Dimension(30, 30));
        btnSortDown.setText("");
        btnSortDown.setToolTipText("Sort Down");
        panel3.add(btnSortDown);
        final JSeparator separator2 = new JSeparator();
        separator2.setOrientation(0);
        separator2.setPreferredSize(new Dimension(25, 3));
        panel3.add(separator2);
        btnPlayStep = new JButton();
        btnPlayStep.setPreferredSize(new Dimension(30, 30));
        btnPlayStep.setText("");
        panel3.add(btnPlayStep);
        btnRefresh = new JButton();
        btnRefresh.setPreferredSize(new Dimension(30, 30));
        btnRefresh.setText("");
        panel3.add(btnRefresh);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new BorderLayout(0, 0));
        panel4.setName("");
        panel4.setPreferredSize(new Dimension(340, 100));
        panel2.add(panel4, BorderLayout.NORTH);
        panel4.setBorder(BorderFactory.createTitledBorder(null, "Description:", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        txtDescription = new JTextArea();
        txtDescription.setLineWrap(true);
        txtDescription.setName("Add description of this feature here. You can you use gherkin format(Given, When, Then...)");
        txtDescription.setPreferredSize(new Dimension(340, 100));
        txtDescription.setToolTipText("Add description of this feature here. You can you use gherkin format(Given, When, Then...)");
        txtDescription.setWrapStyleWord(true);
        panel4.add(txtDescription, BorderLayout.CENTER);
        btnSaveDescription = new JButton();
        btnSaveDescription.setPreferredSize(new Dimension(30, 30));
        btnSaveDescription.setText("");
        panel4.add(btnSaveDescription, BorderLayout.EAST);
        final JScrollPane scrollPane1 = new JScrollPane();
        panel2.add(scrollPane1, BorderLayout.CENTER);
        stepList = new JList();
        scrollPane1.setViewportView(stepList);
        final JScrollPane scrollPane2 = new JScrollPane();
        splitPane1.setLeftComponent(scrollPane2);
        variantList = new JList();
        scrollPane2.setViewportView(variantList);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}

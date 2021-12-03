package app.seleniumap.ui.menu;

import app.seleniumap.models.test.TestCase;
import app.seleniumap.models.test.TestFeature;
import app.seleniumap.models.test.TestStep;
import app.seleniumap.models.test.WebEntity;
import app.seleniumap.ui.MainScreen;
import app.seleniumap.ui.common.UIUtil;
import app.seleniumap.ui.dialogs.WebEntityDialog;
import app.seleniumap.ui.dialogs.browserconfigs.ChromeConfigDialog;
import app.seleniumap.ui.misc.Icons;
import app.seleniumap.ui.tabs.TestFeatureTab;
import app.seleniumap.ui.tabs.WebEntityTab;
import app.seleniumap.utils.FilesUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class TreePopUp extends JPopupMenu {
    public JTree tree;
    public MainScreen mc;

    public TreePopUp(MainScreen mainScreen, Object path) {
        this.mc = mainScreen;
        File f = (File) path;
        if (f.getName().endsWith("Library")) {
            addMenuItem("New...", new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    WebEntityDialog.showDialog("New Page Object", true, "default", f.getAbsolutePath());
                }
            });
            addRefreshMenu();
        } else if (f.getAbsolutePath().contains("Library")) {
            addMenuItem("Open", new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    mainScreen.showTab(f.getName(), Icons.library, new WebEntityTab(f.getAbsolutePath()).getMainPanel());
                }
            });
            addMenuItem("New Child...", new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    WebEntityDialog.showDialog("New Page Object", true, "default", f.getAbsolutePath());
                }
            });
            addMenuItem("Modify..", new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    WebEntityDialog.showDialog("Modify Page Object", false, "default", f.getAbsolutePath());
                }
            });
            addMenuItem("Delete..", new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (UIUtil.showYesNoDialog(null, "Delete object", "Are you sure to delete object" + f.getName() + "?")) {
                        WebEntity.deleteEntity(f.getPath());
                    }
                }
            });
            addSeparator();
            addRefreshMenu();
        } else if (f.getName().endsWith("Test Groups")) {
            addMenuItem("New Test Group...", new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String groupName = UIUtil.showInputDialog(null, "Specify new Test Group name:");
                    if (!groupName.isEmpty() && groupName != null) {
                        if (new File(f.getAbsolutePath() + "/" + groupName).exists()) {
                            UIUtil.showErrorMessage(null, "Invalid Group Name", "Group name already exists!");
                        } else {
                            try {
                                new File(f.getAbsolutePath() + "/" + groupName).mkdirs();
                            } catch (Exception ex) {
                                UIUtil.showErrorMessage(null, "Error Creating Group Name", "Encountered an error in creatin group name!");
                            }
                        }
                    }
                }
            });
            addSeparator();
            addRefreshMenu();
        } else if (f.getAbsolutePath().contains("Test Groups")) {
            if (new File(f.getAbsolutePath() + "/default.json").exists()) {
                addMenuItem("Open", new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        mainScreen.showTab(f.getName(), Icons.testFeature,new TestFeatureTab(f.getAbsolutePath()).getMainPanel());
                    }
                });
                addMenuItem("Delete", new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(UIUtil.showYesNoDialog(null,"Delete Test Feature","You are about to delete test feature '" + f.getName()+"'. Click 'Yes' to confirm")){
                            FilesUtil.cleanFolder(f.getAbsolutePath());
                            f.delete();
                            mainScreen.refreshUI();
                        }
                    }
                });
                addSeparator();
                addRefreshMenu();
            } else {
                addMenuItem("New Feature...", new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
//                        TestFeatureDialog.showDialog(((File) mainScreen.getTree1().getSelectionPath().getLastPathComponent()).getAbsolutePath(),false);
//                        mainScreen.refreshUI();
                        String featureName = UIUtil.showInputDialog(null, "Specify new Test Feature Name:");
                        if (!featureName.isEmpty() && featureName != null) {
                            if (new File(f.getAbsolutePath() + "/" + featureName).exists()) {
                                UIUtil.showErrorMessage(null, "Invalid Test Feature Name", "Group name already exists!");
                            } else {
                                try {
                                    new File(f.getAbsolutePath() + "/" + featureName).mkdirs();
                                    TestFeature t = new TestFeature();
                                    t.setName(featureName);
                                    t.setVariant("default");
                                    t.setDescription("");
                                    t.setSteps(new ArrayList<TestStep>());
                                    t.saveTestFeature(f.getAbsolutePath() + "/" + featureName, "default");
                                    mainScreen.refreshUI();
                                } catch (Exception ex) {
                                    UIUtil.showErrorMessage(null, "Error Creating Group Name", "Encountered an error in creatin group name!");
                                }
                            }
                        }
                    }
                });
                boolean containsFeature = false;
                for (File cx : f.listFiles()) {
                    for (File cy : cx.listFiles()) {
                        if (cy.isFile()) {
                            containsFeature = true;
                        }
                    }
                }
                if (!containsFeature) {
                    addMenuItem("New Sub-Group...", new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String groupName = UIUtil.showInputDialog(null, "Specify new Test Group name:");
                            if (!groupName.isEmpty() && groupName != null) {
                                if (new File(f.getAbsolutePath() + "/" + groupName).exists()) {
                                    UIUtil.showErrorMessage(null, "Invalid Group Name", "Group name already exists!");
                                } else {
                                    try {
                                        new File(f.getAbsolutePath() + "/" + groupName).mkdirs();
                                    } catch (Exception ex) {
                                        UIUtil.showErrorMessage(null, "Error Creating Group Name", "Encountered an error in creatin group name!");
                                    }
                                }
                            }
                        }
                    });
                }
                addMenuItem("Rename", new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String groupName = UIUtil.showInputDialog(null, "Specify new Test Group name:");
                        if (!groupName.isEmpty() && groupName != null) {
                            if (new File(f.getAbsolutePath() + "/" + groupName).exists()) {
                                UIUtil.showErrorMessage(null, "Invalid Group Name", "Group name already exists!");
                            } else {
                                try {
                                    f.renameTo(new File(f.getParent() + "/" + groupName));
                                    mainScreen.refreshUI();
                                } catch (Exception ex) {
                                    UIUtil.showErrorMessage(null, "Error Creating Group Name", "Encountered an error in creatin group name!");
                                }
                            }
                        }
                    }
                });
                addMenuItem("Delete", new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (f.listFiles().length > 0) {
                            UIUtil.showErrorMessage(null,"Invalid action","This file contains some other files. Please delete the child files.");
                        } else {
                            f.delete();
                            mainScreen.refreshUI();
                        }
                    }
                });
                addSeparator();
                addRefreshMenu();
            }
        } else if (f.getName().endsWith("Test Cases")) {
            addMenuItem("New Test Case...", new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String testCaseName = UIUtil.showInputDialog(null,"Specify new Test Case name:");
                    if(!testCaseName.trim().equals("")){
                        String target = f.getAbsolutePath()+"/" + testCaseName;
                        FilesUtil.checkFolder(target);
                        TestCase testCase = new TestCase();
                        testCase.setName(testCaseName);
                        testCase.setDescription("");
                        testCase.saveTestCase(target,"default");
                        mainScreen.refreshUI();
                    }
                }
            });

            addSeparator();
            addRefreshMenu();
        } else if (f.getAbsolutePath().contains("Test Cases")) {
            addMenuItem("Remove Test Case", new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(UIUtil.showYesNoDialog(null,"Delete Test Case","You are about to delete test case '" + f.getName() + "'. Click 'Yes' to confirm.")){
                        FilesUtil.cleanFolder(f.getAbsolutePath());
                        f.delete();
                        mainScreen.refreshUI();
                    }
                }
            });
            addSeparator();
            addRefreshMenu();
        } else if (f.getName().endsWith("Browsers")) {
            addMenuItem("New Chrome Config...", new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ChromeConfigDialog.showDialog(true,f.getAbsolutePath());
                }
            });
            addMenuItem("New Firefox...(WIP)",null);
            addMenuItem("New IE Config...(WIP)",null);
            addMenuItem("New Edge Config...(WIP)",null);
            addSeparator();
            addRefreshMenu();
        } else if (f.getAbsolutePath().contains("Browsers")) {
            addMenuItem("Open", new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ChromeConfigDialog.showDialog(false,f.getAbsolutePath());
                }
            });
            addMenuItem("Delete", new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (UIUtil.showYesNoDialog(null, "Delete Browser Config", "You are about to delete browser config '" + FilesUtil.getFileName(f.getAbsolutePath()) + "'. Click 'Yes' to confirm.")) {
                        f.delete();
                        mainScreen.refreshUI();
                    }
                }
            });
            addSeparator();
            addRefreshMenu();
        }
    }

    public void addMenuItem(String label, ActionListener act) {
        JMenuItem mnuItem = new JMenuItem(label);
        mnuItem.addActionListener(act);
        add(mnuItem);
    }

    public void addRefreshMenu() {
        JMenuItem mntmRefresh = new JMenuItem("Refresh");
        mntmRefresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                mc.refreshUI();
            }
        });
        add(mntmRefresh);
    }

    public void addSeparator() {
        add(new JSeparator());
    }
}

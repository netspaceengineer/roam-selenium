package app.roam.se.ui;

import app.roam.se.models.test.WebEntity;
import app.roam.se.ui.misc.Icons;
import app.roam.se.ui.misc.Theme;

import javax.swing.*;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;
import java.io.File;

public class MainTreeCellRenderer implements TreeCellRenderer {
    private JButton label;
    private Color def;
    public MainTreeCellRenderer(){
        label = new JButton();
        label.setBorder(null);
        label.setForeground(UIManager.getColor("Label.foreground"));
        def = Color.WHITE;
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        File file = (File) value;
        label.setText(file.getName());

        label.setBackground(selected? Theme.selectedElementColor:Theme.defaultColor);
        if(file.getName().endsWith("Library")){
            label.setIcon(Icons.library);
        }else if(file.getName().endsWith("Test Groups")){
            label.setIcon(Icons.testGroup);
        }else if(file.getName().endsWith("Test Cases")){
            label.setIcon(Icons.testCase);
        }else if(file.getName().endsWith("Data")){
            label.setIcon(Icons.data);
        }else if(file.getName().endsWith("Results")) {
            label.setIcon(Icons.result);

        }else if(file.getAbsolutePath().contains("Library")) {
            File object = new File(file.getAbsolutePath() + "//default.json");
            if (object.exists()) {
                WebEntity entity = new WebEntity();
                entity.initialize(file.getAbsolutePath(), "default");
                switch (entity.getType()) {
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
            }
        }else if(file.getAbsolutePath().contains("Test Groups")){
            if(new File(file.getAbsolutePath() + "/default.json").exists()){
                label.setIcon(Icons.testFeature);
            }else{
                label.setIcon(Icons.testGroup);
            }
        }else if(file.getAbsolutePath().contains("Test Cases")) {
            label.setIcon(Icons.testCase);
        }else if(file.getAbsolutePath().contains("Browsers")){
            label.setIcon(Icons.browser);
        }else if(file.getAbsolutePath().contains("Resources")){
            label.setIcon(Icons.resources);
        }else{
            label.setIcon(Icons.project);
        }

        return label;
    }
}

package app.roam.se.ui;

import app.roam.se.App;
import app.roam.se.utils.FilesUtil;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.io.File;

public class MainTreeModel implements TreeModel {
    @Override
    public Object getRoot() {
        return new File(App.testProject.getLocation());
    }

    @Override
    public Object getChild(Object parent, int index) {
        File fParent = (File) parent;
        if (fParent.getAbsolutePath().equals(App.testProject.getLocation())) {
            switch (index){
                case 0:
                    return new File(App.testProject.getLocation()+"/Library");
                case 1:
                    return new File(App.testProject.getLocation()+"/Test Groups");
                case 2:
                    return new File(App.testProject.getLocation()+"/Test Cases");
                case 3:
                    return new File(App.testProject.getLocation()+"/Data");
                case 4:
                    return new File(App.testProject.getLocation()+"/Results");
                case 5:
                    return new File(App.testProject.getLocation()+"/Browsers");
                case 6:
                    return new File(App.testProject.getLocation()+"/Resources");
                default:
                    return null;
            }
        } else {
           return FilesUtil.getViewableFiles(((File) parent).getAbsolutePath()).get(index);

        }
    }

    @Override
    public int getChildCount(Object parent) {

        File fParent = (File) parent;
        if(fParent.getAbsolutePath().equals(App.testProject.getLocation())){
            return 7;
        }else if (fParent.isDirectory()) {
            return FilesUtil.getViewableFiles(((File) parent).getAbsolutePath()).size();
        }
        return 0;
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
        System.out.println(child);
        File file = (File) child;
        if(file.getName().equals("Library")){
            return 0;
        }else if(file.getName().equals("Test Groups")){
            return 1;
        }else if(file.getName().equals("Test Cases")){
            return 2;
        }else if(file.getName().equals("Data")){
            return 3;
        }else if(file.getName().equals("Results")){
            return 4;
        }else if(file.getName().equals("Browsers")){
            return 5;
        }else if(file.getName().equals("Resources")) {
            return 6;
        }
        return 0;
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {

    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {

    }
}

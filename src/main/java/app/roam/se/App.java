package app.roam.se;

import app.roam.se.ui.MainScreen;
import app.roam.se.ui.misc.Icons;
import app.roam.se.models.test.TestProject;
import app.roam.se.ui.dialogs.ProjectInitializerDialog;
import app.roam.se.utils.AppUtil;
import org.openqa.selenium.WebDriver;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class App {

    public static TestProject testProject;
    public static WebDriver webDriver;
    public static boolean isRunning=false;

    public static void main(String[] args){
        SwingUtilities.invokeLater(() ->  {
            try {
                testProject = AppUtil.getProjectDetails();
                testProject.initialize();
                JFrame frame = new JFrame("Selenium Forms");
                frame.setContentPane(new MainScreen(frame).getMainpanel());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setIconImage(Icons.getLogo());
                frame.pack();
                frame.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
                frame.setVisible(true);
                frame.addWindowListener(new WindowListener() {
                    @Override
                    public void windowOpened(WindowEvent e) {

                    }

                    @Override
                    public void windowClosing(WindowEvent e) {
                        if (App.webDriver != null) {
                            App.webDriver.close();
                            App.webDriver.quit();
                        }
                        System.exit(0);
                    }

                    @Override
                    public void windowClosed(WindowEvent e) {

                    }

                    @Override
                    public void windowIconified(WindowEvent e) {

                    }

                    @Override
                    public void windowDeiconified(WindowEvent e) {

                    }

                    @Override
                    public void windowActivated(WindowEvent e) {

                    }

                    @Override
                    public void windowDeactivated(WindowEvent e) {

                    }
                });
            }catch (Exception ex){
                ex.printStackTrace();
                ProjectInitializerDialog.showDialog();
            }
        });


    }

}

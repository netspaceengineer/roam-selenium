package app.roam.se.ui.common;

import javax.swing.*;

public class UIUtil {

    public static void showDialog(String name,JComponent jc){
        JFrame frame = new JFrame(name);
        frame.add(jc);
        frame.setVisible(true);
    }

    public static void showSuccessDialog(JComponent jc,String title,String message) {
        JOptionPane.showMessageDialog(jc, message,title,JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showErrorMessage(JComponent jc,String title,String message) {
        JOptionPane.showMessageDialog(jc, message,title,JOptionPane.ERROR_MESSAGE);
    }

    public static boolean showYesNoDialog(JComponent jc,String title, String message) {
        if(JOptionPane.showConfirmDialog(jc,message,title,JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
            return true;
        }
        return false;
    }

    public static String showInputDialog(JComponent jc ,  String message) {
        String text = JOptionPane.showInputDialog(jc,message);
        if(text!=null && !text.isEmpty()){
            return text;
        }
        return "";
    }
}

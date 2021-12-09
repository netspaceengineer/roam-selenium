package app.roam.se.ui.common;


import app.roam.se.ui.misc.Icons;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

public class Loader {

    private String message;
    JDialog window;
    JLabel label;

    public Loader(String message) {
        this.message = message;
        window = new JDialog();
        window.setModalExclusionType(Dialog.ModalExclusionType.NO_EXCLUDE);
        window.setModal(false);
        window.setUndecorated(true);
        label = new JLabel(message, SwingConstants.CENTER);
        window.setIconImage(Icons.getLogo());
        try {
            URL url = getClass().getResource("/loader.gif");
            ImageIcon icon = new ImageIcon(url);
            label.setIcon(icon);
        } catch (Exception e) {
            e.printStackTrace();
        }
        label.setForeground(Color.BLACK);
        label.setHorizontalTextPosition(JLabel.CENTER);
        label.setVerticalTextPosition(JLabel.BOTTOM);
        window.getContentPane().add(label);
        window.setBounds(500, 150, 200, 200);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setBackground(new Color(1.0f, 1.0f, 1.0f, 0.0f));
        window.setVisible(true);

    }
    public void dispose() {
        window.dispose();
    }
}

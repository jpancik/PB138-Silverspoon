package cz.muni.pb138.silverspoon_visualizer.gui;

import javax.swing.*;

/**
 */
public class Main {

    private JPanel main;
    private JTextArea textArea1;
    private JButton loadFromFileButton;
    private JRadioButton beagleBoneBlackRadioButton;
    private JRadioButton raspberryPiBRadioButton;
    private JRadioButton cubieBoard2RadioButton;
    private JButton generateButton;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Silverspoon Visualizer");
        frame.setContentPane(new Main().main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

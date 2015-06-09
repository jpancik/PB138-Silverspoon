package cz.muni.pb138.silverspoon.visualizer.gui;

import cz.muni.pb138.silverspoon.visualizer.parser.ParserException;
import cz.muni.pb138.silverspoon.visualizer.svgmaker.BeagleboneBlack;
import cz.muni.pb138.silverspoon.visualizer.svgmaker.Board;
import cz.muni.pb138.silverspoon.visualizer.svgmaker.Cubieboard2;
import cz.muni.pb138.silverspoon.visualizer.svgmaker.RaspberryPi;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Main class for GUI interface.
 *
 * @author juraj@pancik.com
 */
public class Main {

    private JPanel main;
    private JTextArea textData;
    private JButton loadFromFileButton;
    private JRadioButton beagleBoneBlackRadioButton;
    private JRadioButton raspberryPiBRadioButton;
    private JRadioButton cubieBoard2RadioButton;
    private JButton generateButton;
    private JScrollPane scrollPane;

    public Main() {
        /*
            Generate button binding.
         */
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //pick correct board
                Board board = null;
                if(beagleBoneBlackRadioButton.isSelected()) {
                    board = new BeagleboneBlack();
                } else if(raspberryPiBRadioButton.isSelected()) {
                    board = new RaspberryPi();
                } else if(cubieBoard2RadioButton.isSelected()) {
                    board = new Cubieboard2();
                } else {
                    throw new IllegalStateException("Selected board cannot be null!");
                }

                try {
                    //create svg doc
                    Document svgDocument = XMLUtils.createSVGDocument(board, textData.getText());

                    //save it as .svg
                    File svgOutputFile = new File("output.svg");
                    XMLUtils.createSVGFile(svgOutputFile, svgDocument);


                    //load template html
                    String templateHtmlData = FileUtils.loadFileIntoString(this.getClass().getResourceAsStream("/result-template.html"));

                    //fill it with path to svg, save it to disk
                    String resultHtmlData = templateHtmlData.replace("{file_path}", svgOutputFile.getAbsolutePath());
                    File resultHtmlFile = new File("result.html");
                    FileUtils.writeStringIntoFile(resultHtmlFile, resultHtmlData);

                    //open the resulting html
                    Desktop.getDesktop().open(resultHtmlFile);
                } catch (ParserException | TransformerException | IOException e1) {
                    showError("An error with generating svg has occurred. See java console for further details.");
                    e1.printStackTrace();
                } catch (IllegalArgumentException | IllegalStateException | NullPointerException e1) {
                    showError("An error with generating svg has occurred: " + e1.getMessage());
                    e1.printStackTrace();
                }
            }
        });

        /*
            Load from XML file button binding.
         */
        loadFromFileButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setFileFilter(new FileNameExtensionFilter("XML Files", "xml", "xml"));
            int returnVal = fileChooser.showOpenDialog(main);

            if(returnVal == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();

                if(selectedFile.exists()) {
                    try {
                        textData.setText(XMLUtils.loadXMLFileIntoString(selectedFile));
                    } catch (ParserConfigurationException | SAXException | IOException | TransformerException e1) {
                        showError("An error with input xml has occurred. See java console for further details.");
                        e1.printStackTrace();
                    }
                } else {
                    showError("Selected file doesn't exist.");
                }
            }
        });
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("Silverspoon Visualizer");
        frame.setContentPane(new Main().main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void createUIComponents() {
        textData = new JTextArea();

        scrollPane = new JScrollPane (textData, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    }

    public static void showError(String s){
        JOptionPane.showMessageDialog(null, s, "Error", JOptionPane.ERROR_MESSAGE);
    }
}

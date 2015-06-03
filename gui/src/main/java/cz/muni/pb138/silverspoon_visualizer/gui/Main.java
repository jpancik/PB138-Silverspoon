package cz.muni.pb138.silverspoon_visualizer.gui;

import cz.muni.pb138.silverspoon_visualizer.parser.Parser;
import cz.muni.pb138.silverspoon_visualizer.parser.ParserException;
import cz.muni.pb138.silverspoon_visualizer.parser.ParserImpl;
import cz.muni.pb138.silverspoon_visualizer.svgmaker.BeagleboneBlack;
import cz.muni.pb138.silverspoon_visualizer.svgmaker.Board;
import cz.muni.pb138.silverspoon_visualizer.svgmaker.Cubieboard2;
import cz.muni.pb138.silverspoon_visualizer.svgmaker.RaspberryPi;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
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
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Board board = null;
                if(beagleBoneBlackRadioButton.isSelected()) {
                    board = new BeagleboneBlack();
                } else if(raspberryPiBRadioButton.isSelected()) {
                    board = new RaspberryPi();
                } else if(cubieBoard2RadioButton.isSelected()) {
                    board = new Cubieboard2();
                } else if(board == null) {
                    throw new IllegalStateException("Selected board cannot be null!");
                }

                String data = textData.getText();

                Parser parser = new ParserImpl();
                try {
                    parser.setSource(new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8)));
                    parser.load();

                    if(parser.getRoutes().isEmpty()) throw new IllegalStateException("There has to be at least one route in given xml!");

                    board.drawRoute(parser.getRoutes().get(0));

                    Document document = board.getSVG();

                    Transformer transformer = TransformerFactory.newInstance().newTransformer();
                    File svgOutputFile = new File("output.svg");
                    Result svgOutput = new StreamResult(svgOutputFile);
                    transformer.transform(new DOMSource(document), svgOutput);


                    File resultTemplateFile = new File(getClass().getClassLoader().getResource("result-template.html").getFile());
                    StringBuilder resultTemplate = new StringBuilder("");
                    try (Scanner scanner = new Scanner(resultTemplateFile)) {

                        while (scanner.hasNextLine()) {
                            String line = scanner.nextLine();
                            resultTemplate.append(line).append("\n");
                        }

                        scanner.close();

                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                    String resultOutput = resultTemplate.toString().replace("{file_path}", svgOutputFile.getAbsolutePath());

                    File resultHtmlFile = new File("result.html");
                    Writer writer = new PrintWriter(resultHtmlFile);
                    writer.write(resultOutput);
                    writer.close();

                    Desktop.getDesktop().open(resultHtmlFile);
                } catch (ParserException | TransformerException | IOException e1) {
                    showError("An error with generating svg has occurred. See java console for further details.");
                    e1.printStackTrace();
                } catch (IllegalArgumentException e1) {
                    showError("An error with generating svg has occurred: " + e1.getMessage());
                    e1.printStackTrace();
                }
            }
        });
        loadFromFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setFileFilter(new FileNameExtensionFilter("XML Files", "xml", "xml"));
                int returnVal = fileChooser.showOpenDialog(main);

                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();

                    if(selectedFile.exists()) {
                        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder dBuilder = null;
                        try {
                            dBuilder = dbFactory.newDocumentBuilder();

                            Document document = dBuilder.parse(selectedFile);

                            TransformerFactory tFactory = TransformerFactory.newInstance();
                            Transformer transformer = tFactory.newTransformer();

                            ByteArrayOutputStream baos = new ByteArrayOutputStream();

                            DOMSource source = new DOMSource(document);
                            StreamResult result = new StreamResult(baos);
                            transformer.transform(source, result);

                            baos.close();
                            textData.setText(baos.toString(StandardCharsets.UTF_8.name()));
                        } catch (ParserConfigurationException | SAXException | IOException | TransformerException e1) {
                            showError("An error with input xml has occurred. See java console for further details.");
                            e1.printStackTrace();
                        }
                    } else {
                        showError("Selected file doesn't exist.");
                    }
                }
            }
        });
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
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

    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();
    }
}

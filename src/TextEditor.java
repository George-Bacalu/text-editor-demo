import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class TextEditor extends JFrame {
   private final JTextArea textArea;
   private final JScrollPane scrollPane;
   private final JSpinner fontSizeSpinner;
   private final JButton fontColorButton;
   private final JComboBox<String> fontBox;

   public TextEditor() {
      textArea = new JTextArea();
      textArea.setLineWrap(true);
      textArea.setWrapStyleWord(true);
      textArea.setFont(new Font("Arial", Font.PLAIN, 20));

      scrollPane = new JScrollPane(textArea);
      scrollPane.setPreferredSize(new Dimension(450, 450));
      scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

      fontSizeSpinner = new JSpinner();
      fontSizeSpinner.setPreferredSize(new Dimension(50, 25));
      fontSizeSpinner.setValue(20);
      fontSizeSpinner.addChangeListener(e -> textArea.setFont(new Font(textArea.getFont().getFamily(), Font.PLAIN, (int) fontSizeSpinner.getValue())));

      fontColorButton = new JButton("Color");
      fontColorButton.addActionListener((e) -> textArea.setForeground(JColorChooser.showDialog(null, "Choose a color", Color.BLACK)));

      fontBox = new JComboBox<>(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
      fontBox.setSelectedItem("Arial");
      fontBox.addActionListener((e) -> textArea.setFont(new Font((String) fontBox.getSelectedItem(), Font.PLAIN, textArea.getFont().getSize())));

      JMenuItem openItem = new JMenuItem("Open");
      openItem.addActionListener((e) -> {
         JFileChooser fileChooser = new JFileChooser();
         fileChooser.setCurrentDirectory(new File(".")); // the selected file path is the project root
         fileChooser.setFileFilter(new FileNameExtensionFilter("Text files", "txt"));

         if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
            String line;
            try {
               BufferedReader reader = new BufferedReader(new FileReader(file));
               if(file.isFile())
                  while((line = reader.readLine()) != null) textArea.append(line + "\n");
               reader.close();
            } catch(IOException ex) { ex.printStackTrace(); }
         }
      });

      JMenuItem saveItem = new JMenuItem("Save");
      saveItem.addActionListener((e) -> {
         JFileChooser fileChooser = new JFileChooser();
         fileChooser.setCurrentDirectory(new File("."));

         if(fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
            try {
               PrintWriter writer = new PrintWriter(file);
               writer.println(textArea.getText());
               writer.close();
            } catch (FileNotFoundException ex) { ex.printStackTrace(); }
         }
      });

      JMenuItem exitItem = new JMenuItem("Exit");
      exitItem.addActionListener((e) -> System.exit(0));

      JMenu fileMenu = new JMenu("File");
      fileMenu.add(openItem);
      fileMenu.add(saveItem);
      fileMenu.add(exitItem);

      JMenuBar menuBar = new JMenuBar();
      menuBar.add(fileMenu);

      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      this.setSize(500, 575);
      this.setTitle("Text editor");
      this.setLayout(new FlowLayout());
      this.setJMenuBar(menuBar);
      this.add(new JLabel("Font: "));
      this.add(fontSizeSpinner);
      this.add(fontColorButton);
      this.add(fontBox);
      this.add(scrollPane);
      this.setLocationRelativeTo(null);
      this.setVisible(true);
   }

   public static void main(String[] args) {
      new TextEditor();
   }
}
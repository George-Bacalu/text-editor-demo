import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class CustomJMenuBar extends JMenuBar {

   public CustomJMenuBar(JTextArea textArea) {
      JMenuItem openItem = new JMenuItem("Open");
      openItem.addActionListener((e) -> {
         JFileChooser fileChooser = new JFileChooser();
         fileChooser.setCurrentDirectory(new File(".")); // the selected file path is the project root
         fileChooser.setFileFilter(new FileNameExtensionFilter("Text files", "txt"));

         if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
            String line;
            try {
               BufferedReader reader = new BufferedReader(new FileReader(file));
               if (file.isFile()){
                  textArea.setText("");
                  while ((line = reader.readLine()) != null) textArea.append(line + "\n");
               }
               reader.close();
            } catch (IOException ex) {
               ex.printStackTrace();
            }
         }
      });

      JMenuItem saveItem = new JMenuItem("Save");
      saveItem.addActionListener((e) -> {
         JFileChooser fileChooser = new JFileChooser();
         fileChooser.setCurrentDirectory(new File("."));

         if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
            try {
               PrintWriter writer = new PrintWriter(file);
               writer.println(textArea.getText());
               writer.close();
            } catch (FileNotFoundException ex) {
               ex.printStackTrace();
            }
         }
      });

      JMenuItem exitItem = new JMenuItem("Exit");
      exitItem.addActionListener((e) -> System.exit(0));

      JMenu fileMenu = new JMenu("File");
      fileMenu.add(openItem);
      fileMenu.add(saveItem);
      fileMenu.add(exitItem);

      this.add(fileMenu);
   }
}
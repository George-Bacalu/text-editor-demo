import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class CustomJMenuBar extends JMenuBar implements ActionListener {
   private final JMenuItem openItem;
   private final JMenuItem saveItem;
   private final JMenuItem exitItem;
   private final JTextArea textArea;
   private String fileName;
   private boolean isChanged;

   public CustomJMenuBar(JTextArea textArea, String fileName, boolean isChanged) {
      this.textArea = textArea;
      this.fileName = fileName;
      this.isChanged = isChanged;

      JMenu fileMenu = new JMenu("File");
      fileMenu.setMnemonic(KeyEvent.VK_F);
      openItem = new JMenuItem("Open"); openItem.addActionListener(this); openItem.setMnemonic(KeyEvent.VK_O); fileMenu.add(openItem);
      saveItem = new JMenuItem("Save"); saveItem.addActionListener(this); saveItem.setMnemonic(KeyEvent.VK_S); fileMenu.add(saveItem);
      exitItem = new JMenuItem("Exit"); exitItem.addActionListener(this); exitItem.setMnemonic(KeyEvent.VK_E); fileMenu.add(exitItem);

      this.add(fileMenu);
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      if (e.getSource() == openItem) openFile();
      else if (e.getSource() == saveItem) saveFile();
      else if (e.getSource() == exitItem) exitFile();
   }

   private void openFile() {
      JFileChooser fileChooser = new JFileChooser();
      fileChooser.setCurrentDirectory(new File(".")); // the selected file path is the project root
      fileChooser.setFileFilter(new FileNameExtensionFilter("Text files", "txt"));

      if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
         File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
         String line;
         try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            if (file.isFile()) {
               textArea.setText("");
               isChanged = false;
               fileName = fileChooser.getSelectedFile().getPath();
               while ((line = reader.readLine()) != null) textArea.append(line + "\n");
            }
            reader.close();
         } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error at file opening!", ex.getMessage(), JOptionPane.WARNING_MESSAGE);
         }
      }
   }

   private void saveFile() {
      JFileChooser fileChooser = new JFileChooser();
      fileChooser.setCurrentDirectory(new File("."));

      if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
         File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
         try {
            PrintWriter writer = new PrintWriter(file);
            writer.println(textArea.getText());
            writer.close();
            isChanged = false;
         } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Error at file writing!", ex.getMessage(), JOptionPane.WARNING_MESSAGE);
         }
      }
   }

   private void exitFile() {
      if(JOptionPane.showConfirmDialog(
              null,
              "Do you want to save modification made to " + fileName + "?",
              "Save modifications",
              JOptionPane.YES_NO_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
         System.exit(0);
      }
   }
}
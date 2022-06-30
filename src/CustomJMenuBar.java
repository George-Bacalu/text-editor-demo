import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
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
   private final JMenu fileMenu, editMenu, helpMenu;
   private final JMenuItem newItem, openItem, saveItem, exitItem, cutItem, copyItem, pasteItem, undoItem, redoItem, aboutItem;
   private final TextEditor textEditor;
   private final JTextArea textArea;
   private String fileName;
   private boolean isChanged;

   private UndoAction undoAction;
   private RedoAction redoAction;
   private UndoManager undoManager;

   public CustomJMenuBar(TextEditor textEditor, JTextArea textArea, String fileName, boolean isChanged) {
      this.textEditor = textEditor;
      this.textArea = textArea;
      this.fileName = fileName;
      this.isChanged = isChanged;

      fileMenu = new JMenu("File");
      fileMenu.setMnemonic(KeyEvent.VK_F);
      newItem = new JMenuItem("New"); newItem.addActionListener(this); newItem.setMnemonic(KeyEvent.VK_N); fileMenu.add(newItem);
      openItem = new JMenuItem("Open"); openItem.addActionListener(this); openItem.setMnemonic(KeyEvent.VK_O); fileMenu.add(openItem);
      saveItem = new JMenuItem("Save"); saveItem.addActionListener(this); saveItem.setMnemonic(KeyEvent.VK_S); fileMenu.add(saveItem);
      exitItem = new JMenuItem("Exit"); exitItem.addActionListener(this); exitItem.setMnemonic(KeyEvent.VK_E); fileMenu.add(exitItem);

      editMenu = new JMenu("Edit");
      editMenu.setMnemonic(KeyEvent.VK_E);
      cutItem = new JMenuItem("Cut"); cutItem.addActionListener(this); cutItem.setMnemonic(KeyEvent.VK_X); editMenu.add(cutItem);
      copyItem = new JMenuItem("Copy"); copyItem.addActionListener(this); copyItem.setMnemonic(KeyEvent.VK_C); editMenu.add(copyItem);
      pasteItem = new JMenuItem("Paste"); pasteItem.addActionListener(this); pasteItem.setMnemonic(KeyEvent.VK_V); editMenu.add(pasteItem);
      undoItem = new JMenuItem("Undo"); undoItem.addActionListener(this); undoItem.setMnemonic(KeyEvent.VK_U); editMenu.add(undoItem);
      redoItem = new JMenuItem("Redo"); redoItem.addActionListener(this); redoItem.setMnemonic(KeyEvent.VK_R); editMenu.add(redoItem);

      helpMenu = new JMenu("Help");
      helpMenu.setMnemonic(KeyEvent.VK_H);
      aboutItem = new JMenuItem("About"); aboutItem.addActionListener(this); aboutItem.setMnemonic(KeyEvent.VK_B); helpMenu.add(aboutItem);

      this.add(fileMenu);
      this.add(editMenu);
      this.add(helpMenu);
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      if (e.getSource() == newItem) getNewFile();
      else if (e.getSource() == openItem) openFile();
      else if (e.getSource() == saveItem) saveFile();
      else if (e.getSource() == exitItem) exitFile();
      else if (e.getSource() == cutItem) textArea.cut();
      else if (e.getSource() == copyItem) textArea.copy();
      else if (e.getSource() == pasteItem) textArea.paste();
      else if (e.getSource() == undoItem) { undoAction = new UndoAction(); editMenu.add(undoAction); }
      else if (e.getSource() == redoItem) { redoAction = new RedoAction(); editMenu.add(redoAction); }
      else if (e.getSource() == aboutItem) JOptionPane.showMessageDialog(textEditor, "Developed by George Bacalu", "Custom Text Editor", JOptionPane.INFORMATION_MESSAGE);
   }

   private void getNewFile() {
      if (JOptionPane.showConfirmDialog(null, "Do you want to save modification made to " + fileName + "?", "Save modifications", JOptionPane.YES_NO_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
         textEditor.setTitle("Untitled - Custom Text Editor");
         textArea.setText("");
      }
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
         } catch (IOException ex) { JOptionPane.showMessageDialog(null, "Error at file opening!", ex.getMessage(), JOptionPane.WARNING_MESSAGE); }
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
         } catch (FileNotFoundException ex) { JOptionPane.showMessageDialog(null, "Error at file writing!", ex.getMessage(), JOptionPane.WARNING_MESSAGE); }
      }
   }

   private void exitFile() {
      if (JOptionPane.showConfirmDialog(null, "Do you want to save modification made to " + fileName + "?", "Save modifications", JOptionPane.YES_NO_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
         System.exit(0);
      }
   }

   class UndoAction extends AbstractAction {

      public UndoAction() { super("Undo"); setEnabled(false); }

      @Override
      public void actionPerformed(ActionEvent e) {
         try { undoManager.undo(); }
         catch (CannotUndoException ex) { System.out.println("Unable to undo: " + ex); ex.printStackTrace(); }
         updateUndoState(); redoAction.updateRedoState();
      }

      protected void updateUndoState() {
         setEnabled(undoManager.canUndo());
         putValue(Action.NAME, undoManager.canUndo() ? undoManager.getUndoPresentationName() : "Undo");
      }
   }

   class RedoAction extends AbstractAction {

      public RedoAction() { super("Redo"); setEnabled(false); }

      @Override
      public void actionPerformed(ActionEvent e) {
         try { undoManager.redo(); }
         catch (CannotRedoException ex) { System.out.println("Unable to redo: " + ex); ex.printStackTrace(); }
         updateRedoState(); undoAction.updateUndoState();
      }

      public void updateRedoState() {
         setEnabled(undoManager.canRedo());
         putValue(Action.NAME, undoManager.canRedo() ? undoManager.getRedoPresentationName() : "Redo");
      }
   }
}
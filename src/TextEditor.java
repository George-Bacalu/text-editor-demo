import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class TextEditor extends JFrame {
   private String fileName = "undefined";
   private boolean isChanged = false;

   public TextEditor() {
      JTextArea textArea = new JTextArea();
      textArea.setLineWrap(true);
      textArea.setWrapStyleWord(true);
      textArea.setFont(new Font("Arial", Font.PLAIN, 20));
      textArea.addKeyListener(new KeyListener() {
         @Override public void keyTyped(KeyEvent e) {
            if(e.getKeyCode() > 31 && e.getKeyCode() < 127) isChanged = true;
         }
         @Override public void keyPressed(KeyEvent e) {
         }
         @Override public void keyReleased(KeyEvent e) {
         }
      });

      JScrollPane scrollPane = new JScrollPane(textArea);
      scrollPane.setPreferredSize(new Dimension(450, 450));
      scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      this.setSize(500, 600);
      this.setTitle(isChanged ? "*" : "" + fileName + " - Custom Text Editor");
      this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
      this.setJMenuBar(new CustomJMenuBar(textArea, fileName, isChanged));
      this.add(new EditorFunctionsJPanel(textArea));
      this.add(scrollPane);
      this.setLocationRelativeTo(null);
      this.setVisible(true);
   }

   public static void main(String[] args) {
      new TextEditor();
   }
}
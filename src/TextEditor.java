import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import java.awt.Dimension;
import java.awt.Font;

public class TextEditor extends JFrame {
   private final String fileName;
   private boolean isChanged;

   public TextEditor() {
      fileName = "undefined";
      isChanged = false;

      JTextArea textArea = new JTextArea();
      textArea.setLineWrap(true);
      textArea.setWrapStyleWord(true);
      textArea.setFont(new Font("Arial", Font.PLAIN, 20));

      JScrollPane scrollPane = new JScrollPane(textArea);
      scrollPane.setPreferredSize(new Dimension(450, 450));
      scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

      Login login = new Login(this, textArea, scrollPane);

      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      this.setSize(500, 600);
      this.setTitle(isChanged ? "*" : "" + fileName + " - Custom Text Editor");
      this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
      this.add(login);
      this.setJMenuBar(new CustomJMenuBar(this, textArea, fileName, isChanged));
      this.add(new EditorFunctionsJPanel(textArea));
      this.add(scrollPane);
      if(login.isLoggedIn()) {
         this.dispose();
         JFrame newFrame = new JFrame();
         newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         newFrame.setSize(500, 600);
         newFrame.setTitle("Untitled - Custom Text Editor");
         this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
         this.setJMenuBar(new CustomJMenuBar(this, textArea, fileName, isChanged));
         this.add(new EditorFunctionsJPanel(textArea));
         this.add(scrollPane);
         newFrame.setLocationRelativeTo(null);
         newFrame.setVisible(true);
      }
      this.setLocationRelativeTo(null);
      this.setVisible(true);
   }

   public static void main(String[] args) {
      new TextEditor();
   }
}
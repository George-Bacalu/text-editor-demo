import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.StringTokenizer;

public class Register extends JPanel implements ActionListener {
   private final JLabel userLabel;
   private final JTextField userTextField;
   private final JLabel passwordLabel;
   private final JPasswordField passwordField;
   private final JLabel passwordConfirmLabel;
   private final JPasswordField passwordConfirmField;
   private final JButton backButton;
   private final JButton registerButton;
   private final JPanel loginPanel;

   private final JFrame frame;
   private final JTextArea textArea;
   private final JScrollPane scrollPane;

   public Register(JFrame frame, JTextArea textArea, JScrollPane scrollPane) {
      this.frame = frame;
      this.textArea = textArea;
      this.scrollPane = scrollPane;
      frame.setTitle("Register");

      userLabel = new JLabel("Choose a username: "); userTextField = new JTextField();
      passwordLabel = new JLabel("Password: "); passwordField = new JPasswordField();
      passwordConfirmLabel = new JLabel("Confirm password: "); passwordConfirmField = new JPasswordField();
      registerButton = new JButton("Register"); registerButton.addActionListener(this);
      backButton = new JButton("Back"); backButton.addActionListener(this);
      loginPanel = new JPanel();

      loginPanel.setLayout(new GridLayout(4, 2));
      loginPanel.add(userLabel);
      loginPanel.add(userTextField);
      loginPanel.add(passwordLabel);
      loginPanel.add(passwordField);
      loginPanel.add(passwordConfirmLabel);
      loginPanel.add(passwordConfirmField);
      loginPanel.add(registerButton);
      loginPanel.add(backButton);
      this.add(loginPanel);
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      if(e.getSource() == registerButton && passwordField.getPassword().length > 4 && userTextField.getText().length() > 4) {
         String password = new String(passwordField.getPassword());
         String confirmPassword = new String(passwordConfirmField.getPassword());
         if(password.equals(confirmPassword)) {
            try {
               BufferedReader reader = new BufferedReader(new FileReader("passwords.txt"));
               String line;
               while((line = reader.readLine()) != null)
                  if(userTextField.getText().equals(new StringTokenizer(line).nextToken())) { System.out.println("User already exists!"); return; }
               reader.close();

               // password encryption
               MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
               messageDigest.update(password.getBytes());
               byte[] byteData = messageDigest.digest();
               StringBuilder stringBuilder = new StringBuilder();
               for (byte byteDatum : byteData) stringBuilder.append(Integer.toString((byteDatum & 0xFF) + 0x100, 16).substring(1));

               BufferedWriter writer = new BufferedWriter(new FileWriter("passwords.txt", true)); // appends to the file
               writer.write(userTextField.getText() + " " + stringBuilder + "\n");
               writer.close();

               sendBackToLoginScreen();
            } catch (IOException | NoSuchAlgorithmException ex) { ex.printStackTrace(); }
         }
      }
      if(e.getSource() == backButton) sendBackToLoginScreen();
   }

   private void sendBackToLoginScreen() {
      frame.dispose();
      JFrame newFrame = new JFrame();
      newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      newFrame.setSize(500, 600);
      newFrame.setTitle("Login");
      newFrame.add(new Login(frame, textArea, scrollPane));
      newFrame.setLocationRelativeTo(null);
      newFrame.setVisible(true);
   }
}

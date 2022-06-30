import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.StringTokenizer;

public class Login extends JPanel implements ActionListener {
   private final JLabel userLabel;
   private final JTextField userTextField;
   private final JLabel passwordLabel;
   private final JPasswordField passwordField;
   private final JPanel loginPanel;
   private final JPanel panel;
   private final JButton loginButton;
   private final JButton registerButton;
   public final CardLayout cardLayout;

   private final JFrame frame;
   private final JTextArea textArea;
   private final JScrollPane scrollPane;

   private boolean isLoggedIn;

   public Login(JFrame frame, JTextArea textArea, JScrollPane scrollPane) {
      this.frame = frame;
      this.textArea = textArea;
      this.scrollPane = scrollPane;
      frame.setTitle("Login");

      userLabel = new JLabel("Username: "); userTextField = new JTextField();
      passwordLabel = new JLabel("Password: "); passwordField = new JPasswordField();
      loginPanel = new JPanel(new GridLayout(3, 2)); panel = new JPanel();
      loginButton = new JButton("Login"); loginButton.addActionListener(this);
      registerButton = new JButton("Register"); registerButton.addActionListener(this);
      isLoggedIn = false;

      this.setLayout(new CardLayout());
      loginPanel.add(userLabel);
      loginPanel.add(userTextField);
      loginPanel.add(passwordLabel);
      loginPanel.add(passwordField);
      loginPanel.add(loginButton);
      loginPanel.add(registerButton);
      panel.add(loginPanel);
      this.add(panel);
      cardLayout = (CardLayout) getLayout();
   }

   public boolean isLoggedIn() {
      return isLoggedIn;
   }

   public void setLoggedIn(boolean loggedIn) {
      isLoggedIn = loggedIn;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      if(e.getSource() == loginButton) {
         try {
            BufferedReader reader = new BufferedReader(new FileReader("passwords.txt"));
            String password = "";
            String line;
            while((line = reader.readLine()) != null) {
               StringTokenizer stringTokenizer = new StringTokenizer(line);
               if(userTextField.getText().equals(stringTokenizer.nextToken())) password = stringTokenizer.nextToken();
            }
            reader.close();

            // password encryption
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(new String(passwordField.getPassword()).getBytes());
            byte[] byteData = messageDigest.digest();
            StringBuilder stringBuilder = new StringBuilder();
            for (byte byteDatum : byteData) stringBuilder.append(Integer.toString((byteDatum & 0xFF) + 0x100, 16).substring(1));

            if(password.equals(stringBuilder.toString())) setLoggedIn(true);
         } catch (IOException | NoSuchAlgorithmException ex) { ex.printStackTrace(); }
      }
      if(e.getSource() == registerButton) {
         this.add(new Register(frame, textArea, scrollPane), "register");
         cardLayout.show(this, "register");
      }
   }
}

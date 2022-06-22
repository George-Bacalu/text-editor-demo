import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;

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

      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      this.setTitle("Text editor");
      this.setSize(500, 500);
      this.setLayout(new FlowLayout());
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
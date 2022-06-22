import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;

public class EditorFunctionsJPanel extends JPanel {
   private final JSpinner fontSizeSpinner;
   private final JButton fontColorButton;
   private final JComboBox<String> fontBox;

   public EditorFunctionsJPanel(JTextArea textArea) {
      fontSizeSpinner = new JSpinner();
      fontSizeSpinner.setPreferredSize(new Dimension(50, 25));
      fontSizeSpinner.setValue(20);
      fontSizeSpinner.addChangeListener(e -> textArea.setFont(new Font(textArea.getFont().getFamily(), Font.PLAIN, (int) fontSizeSpinner.getValue())));

      fontColorButton = new JButton("Color");
      fontColorButton.addActionListener((e) -> textArea.setForeground(JColorChooser.showDialog(null, "Choose a color", Color.BLACK)));

      fontBox = new JComboBox<>(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
      fontBox.setSelectedItem("Arial");
      fontBox.addActionListener((e) -> textArea.setFont(new Font((String) fontBox.getSelectedItem(), Font.PLAIN, textArea.getFont().getSize())));

      this.setPreferredSize(new Dimension(450, 50));
      this.add(new JLabel("Font: "));
      this.add(fontSizeSpinner);
      this.add(fontColorButton);
      this.add(fontBox);
   }
}
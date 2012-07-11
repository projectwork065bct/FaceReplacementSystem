package test;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

import javax.swing.JFrame;
import javax.swing.UIManager;

public class Main extends JFrame {
  public void paint(Graphics g) {
    String input = "this is a test.this is a test.\n\nKKKKthis is a test.this is a test.";

    AttributedString attributedString = new AttributedString(input);
    attributedString.addAttribute(TextAttribute.FONT, (Font) UIManager
        .get("Label.font"));
    Color color = (Color) UIManager.get("Label.foreground");

    attributedString.addAttribute(TextAttribute.FOREGROUND, color);

    super.paint(g);
    Graphics2D g2d = (Graphics2D) g;

    int width = getSize().width;
    int x = 10;
    int y = 30;

    AttributedCharacterIterator characterIterator = attributedString
        .getIterator();
    FontRenderContext fontRenderContext = g2d.getFontRenderContext();
    LineBreakMeasurer measurer = new LineBreakMeasurer(characterIterator,
        fontRenderContext);
    while (measurer.getPosition() < characterIterator.getEndIndex()) {
      TextLayout textLayout = measurer.nextLayout(width);
      y += textLayout.getAscent();
      textLayout.draw(g2d, x, y);
      y += textLayout.getDescent() + textLayout.getLeading();
    }
  }

  public static void main(String args[]) {
    JFrame frame = new Main();
    frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
    frame.setSize(20, 200);

    frame.setVisible(true);
  }
}
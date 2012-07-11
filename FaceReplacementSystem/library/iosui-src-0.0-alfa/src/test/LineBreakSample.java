package test;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedCharacterIterator;
import java.text.AttributedCharacterIterator.Attribute;
import java.text.AttributedString;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class LineBreakSample extends JPanel {

  private LineBreakMeasurer lineMeasurer;
  private int paragraphStart;
  private int paragraphEnd;

//  private static final Hashtable map = new Hashtable();
//  static {
//    map.put(TextAttribute.SIZE, new Float(18.0));
//  }

  private static String str = "Many\npeople believe that Vincent van Gogh painted his best works "
          + "during the two-year period he spent in Provence. Here is where he "
          + "painted The Starry Night--which some consider to be his greatest "
          + "work of all. However, as his artistic brilliance reached new heights "
          + "in Provence, his physical and mental health plummeted. "; 

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    setBackground(Color.white);

    Graphics2D graphics2D = (Graphics2D) g;

    Dimension size = getSize();
    float formatWidth = (float) size.width;

    float drawPosY = 0;

    Map map = new Hashtable();
    map.put(TextAttribute.SIZE, new Float(18.0));
    
    String[] lines = str.split("\\n");
    for(String strLine : lines) {
		AttributedString line = new AttributedString(strLine,map);    
	    AttributedCharacterIterator paragraph = line.getIterator();
	    paragraphStart = paragraph.getBeginIndex();
	    paragraphEnd = paragraph.getEndIndex();

	    lineMeasurer = new LineBreakMeasurer(paragraph,new FontRenderContext(null, true, false));
	    lineMeasurer.setPosition(paragraphStart);
	
	    while (lineMeasurer.getPosition() < paragraphEnd) {
	      TextLayout layout = lineMeasurer.nextLayout(formatWidth);
	      drawPosY += layout.getAscent();
	
	      float drawPosX;
	      if (layout.isLeftToRight()) {
	        drawPosX = 0;
	      } else {
	        drawPosX = formatWidth - layout.getAdvance();
	      }
	
	      layout.draw(graphics2D, drawPosX, drawPosY);
	
	      drawPosY += layout.getDescent() + layout.getLeading();
	    }
    }
  }
  public static void main(String[] args) {
    JFrame f = new JFrame("");

    f.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });

    LineBreakSample controller = new LineBreakSample();
    f.getContentPane().add(controller,"Center");
    f.setSize(new Dimension(400, 250));
    f.setVisible(true);
  }
}


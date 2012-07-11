package hu.droidzone.iosui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.io.Reader;
import java.io.StringReader;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

public class OldIOSUILabel extends IOSUIComponent {
	private static final long serialVersionUID = 1888484550256906836L;
	private static final boolean debugPainting = false;
	private String _title = "";
	private int horizontalAlignment;
	private int verticalAlignment;

	public String getTitle() {
		return _title;
	}
	public void setTitle(String title) {
		this._title = title;
		setPreferredSizeFor(_title);
		BasicHTML.updateRenderer(this, _title);
	}

	@Override
	public void setFont(Font font) {
		super.setFont(font);
		setPreferredSizeFor(_title);
		BasicHTML.updateRenderer(this, _title);
	}
	
	public OldIOSUILabel() {
		setupLabel();
	}

	@Override
	public void setForeground(Color fg) {
		super.setForeground(fg);
		BasicHTML.updateRenderer(this, _title);
	}

	private void setupLabel() {
		setFont(new Font("Verdana", Font.BOLD, 12));
		setForeground(Color.BLACK);
		setVerticalAlignment(SwingConstants.CENTER);
		setHorizontalAlignment(SwingConstants.LEFT);
	}

	public void setHorizontalAlignment(int horizontalAlignment) {
		this.horizontalAlignment = horizontalAlignment;
	}

	public void setVerticalAlignment(int verticalAlignment) {
		this.verticalAlignment = verticalAlignment;
	}


	public int getHorizontalAlignment() {
		return horizontalAlignment;
	}
	public int getVerticalAlignment() {
		return verticalAlignment;
	}

	public OldIOSUILabel(String title) {
		setupLabel();
		setTitle(title);
	}

	@Override
	protected void paintUI(Graphics2D g2) {
		g2.setColor(getBackground());
		g2.fillRect(0, 0, getWidth(), getHeight());
		g2.setFont(getFont());
		g2.setColor(getForeground());
		View v = (View) getClientProperty(BasicHTML.propertyKey);
		if (v != null) {
			v.paint(g2, new Rectangle(getWidth(), getHeight()));
			return;
		}
		LineBreakMeasurer lineMeasurer;
		int paragraphStart;
		int paragraphEnd;
//		Dimension size = getSize();
		float formatWidth = (float) getWidth();
		float formatHeight= (float) getHeight();

		float drawPosY = 0;
		float drawHeight = 0;

		Map<TextAttribute, Object> map = new Hashtable<TextAttribute, Object>();
		map.put(TextAttribute.FONT, getFont());
		if(_title.length() > 0) {
			String[] lines = _title.split("\\n");
	//        FontMetrics fm = g2.getFontMetrics(getFont());
	//        int hgt = fm.getHeight();
	//        drawHeight = hgt * lines.length;
			for(String strLine : lines) {
				AttributedString line = new AttributedString(strLine,map);    
				AttributedCharacterIterator paragraph = line.getIterator();
				paragraphStart = paragraph.getBeginIndex();
				paragraphEnd = paragraph.getEndIndex();
	
				lineMeasurer = new LineBreakMeasurer(paragraph,new FontRenderContext(null, true, false));
				lineMeasurer.setPosition(paragraphStart);
	
				while (lineMeasurer.getPosition() < paragraphEnd) {
					TextLayout layout = lineMeasurer.nextLayout(formatWidth);
					drawHeight += layout.getAscent();
					drawHeight += layout.getDescent() + layout.getLeading();
				}
			}
	
			switch(verticalAlignment) {
			case SwingConstants.TOP:
				drawPosY = 0;
				break;
			case SwingConstants.CENTER:
				drawPosY = (formatHeight - drawHeight)/2;
	//			System.out.println(String.format("FH %d, DH %d, DPY %d", (int)formatHeight,(int)drawHeight, (int)drawPosY));
	//			g2.setColor(Color.GREEN);
	//			g2.drawLine(0, (int)drawPosY, (int)formatWidth, (int)drawPosY);
	//			g2.setColor(getForeground());
				break;
			case SwingConstants.BOTTOM:
				drawPosY = formatHeight - drawHeight;
				break;
			}
	
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
	
					float drawPosX = 0;
					switch(horizontalAlignment) {
					case SwingConstants.LEFT:
						drawPosX = 0;
						break;
					case SwingConstants.CENTER:
						drawPosX = (formatWidth - layout.getAdvance())/2;
						break;
					case SwingConstants.RIGHT:
						drawPosX = formatWidth - layout.getAdvance();
						break;
					}
					
					layout.draw(g2, drawPosX, drawPosY);
	
					drawPosY += layout.getDescent() + layout.getLeading();
				}
			}
		}
		if(debugPainting) {
			g2.setColor(Color.RED);
			g2.drawRect(0, 0, getWidth()-1, getHeight()-1);
		}
	}
}

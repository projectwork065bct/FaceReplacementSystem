package hu.droidzone.iosui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.LayoutMap;

public class IOSUIView extends IOSUIComponent {
	private static final long serialVersionUID = -5013543634724323997L;
	private FormLayout fl;
	private CellConstraints cc;
	
	public static IOSUIView createUninitalized() {
		return new IOSUIView();
	}
	
	private IOSUIView() {
		setBackground(new Color(255,255,255,0));
	}
	
	public IOSUIView(int colCount, int rowCount) {
		String rowSpecs = "";
		String columnSpecs = "";
		for(int i = 0;i < colCount;i++) columnSpecs += "p,";
		for(int i = 0;i < rowCount;i++) rowSpecs += "p,";
		columnSpecs = columnSpecs.substring(0,columnSpecs.length() - 1);
		rowSpecs = rowSpecs.substring(0,rowSpecs.length() - 1);
		System.out.println(columnSpecs+" -- "+rowSpecs);
		initView(columnSpecs, rowSpecs);
	}
	
	public IOSUIView(String columnSpecs, String rowSpecs) {
		initView(columnSpecs, rowSpecs);
	}

	public void addXY(IOSUIComponent c, int x, int y) {
		add(c,cc.xy(x, y));
		setPreferredSize(fl.preferredLayoutSize(this));
	}
	
	public void addXY(IOSUIComponent c, int x, int y, String spec) {
		add(c,cc.xy(x, y, spec));
		setPreferredSize(fl.preferredLayoutSize(this));
	}
	
	public void addXYW(IOSUIComponent c, int x, int y, int w) {
		add(c,cc.xyw(x, y, w));
		setPreferredSize(fl.preferredLayoutSize(this));
	}
	
	public void addXYW(IOSUIComponent c, int x, int y, int w, String spec) {
		add(c,cc.xyw(x, y, w, spec));
		setPreferredSize(fl.preferredLayoutSize(this));
	}
	
	public void addXYWH(IOSUIComponent c, int x, int y, int w, int h) {
		add(c,cc.xywh(x, y, w, h));
		setPreferredSize(fl.preferredLayoutSize(this));
	}
	
	public void addXYWH(IOSUIComponent c, int x, int y, int w, int h, String spec) {
		add(c,cc.xywh(x, y, w, h, spec));
		setPreferredSize(fl.preferredLayoutSize(this));
	}
	
	private void initView(String columnSpecs, String rowSpecs) {
		fl = new FormLayout(columnSpecs, rowSpecs);
		setLayout(fl);
		cc = new CellConstraints();
		setPreferredSize(fl.preferredLayoutSize(this));
		setBackground(new Color(255,255,255,0));
		addComponentListener(new ComponentListener() {
			@Override
			public void componentShown(ComponentEvent e) {
			}
			
			@Override
			public void componentResized(ComponentEvent e) {
				doLayout();
			}
			
			@Override
			public void componentMoved(ComponentEvent e) {
			}
			
			@Override
			public void componentHidden(ComponentEvent e) {
			}
		});
	}
	
//	@Override
//	public void add(Component comp, Object constraints, int index) {
//		super.add(comp, constraints, index);
//	}
//	@Override
//	public Component add(Component comp) {
//		return super.add(comp);
//	}
//	@Override
//	public Component add(Component comp, int index) {
//		return super.add(comp, index);
//	}
//	@Override
//	public void add(Component comp, Object constraints) {
//		super.add(comp, constraints);
//	}
//	@Override
//	public Component add(String name, Component comp) {
//		return super.add(name, comp);
//	}
//	@Override
//	protected void addImpl(Component comp, Object constraints, int index) {
//		System.out.println("IOSUIView.addImpl()");
//		super.addImpl(comp, constraints, index);
//	}
	@Override
	protected void paintUI(Graphics2D g2) {
		g2.setColor(getBackground());
		g2.fillRect(0, 0, getWidth(), getHeight());
//		g2.setColor(Color.BLACK);
//		g2.drawRect(0, 0, getWidth(), getHeight());
	}

}

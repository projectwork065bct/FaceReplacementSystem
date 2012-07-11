package hu.droidzone.iosui.list;

import hu.droidzone.iosui.IOSUIComponent;
import hu.droidzone.iosui.list.IOSUIListView.RowMapItem;
import hu.droidzone.iosui.utils.IOSUIHelper;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.RoundRectangle2D;
import java.sql.Savepoint;

import sun.awt.SunGraphicsCallback.PaintHeavyweightComponentsCallback;

public class IOSUIListViewRow extends IOSUIComponent {
	private static final long serialVersionUID = -6678552136796363857L;
	private RowKind rk;
	
//	private IOSUIComponent mview;
//	private RowMapItem rmi;
//	private IOSUIListModel<?> model;
	private enum RowKind {
		SIMPLE, FOOTER, HEADER, HEADER_FOOTER;
	}

	private static final int GROUPPED_HEADER_TOP = 0; 
	private static final int GROUPPED_HEADER_BOTTOM = 0; 
	private static final int GROUPPED_FOOTER_TOP = 0; 
	private static final int GROUPPED_FOOTER_BOTTOM = 0;
	
	private static final int SEPARATED_HEADER_TOP = 2; 
	private static final int SEPARATED_HEADER_BOTTOM = 2; 
	private static final int SEPARATED_FOOTER_TOP = 2; 
	private static final int SEPARATED_FOOTER_BOTTOM = 2;
	private static final int SEPARATED_ROW_RIGHT = 18;
	private static final int SEPARATED_ROW_LEFT = 18;
	
	private int arcr = 10;

	private ListKind lk;
	private boolean selected = true;
	private IOSUIListView owner;
	
	public static RowMapItem measureRow(IOSUIListViewModel model, int g, int r, int lrb) {
		RowMapItem rmi = new RowMapItem();
		rmi.group = g;
		rmi.row = r;
		rmi.rowBegin = lrb;
		int rh = -1;
		int trh = -1;
		switch(model.getListKind()) {
		case GROUPPED:
			rh = model.getRowHeight(g,r);
			trh = rh;
			if(r == 0) {
				if(model.getGroupHeaderHeight(g) > 0) {
					trh += model.getGroupHeaderHeight(g);
					trh += GROUPPED_HEADER_TOP + GROUPPED_HEADER_BOTTOM;
				}
				rmi.groupHeader = true;
			}
			if(r == model.getRowCount(g) - 1) {
				if(model.getGroupFooterHeight(g) > 0) {
					trh += model.getGroupFooterHeight(g);
					trh += GROUPPED_FOOTER_TOP + GROUPPED_FOOTER_BOTTOM;
				}
				rmi.groupFooter= true;
			}
			rmi.rowEnd = lrb + trh;
			break;
		case SEPARATED:
			rh = model.getRowHeight(g,r);
			trh = rh;
			if(r == 0) {
				if(model.getGroupHeaderHeight(g) > 0) {
					trh += model.getGroupHeaderHeight(g);
					trh += SEPARATED_HEADER_TOP + SEPARATED_HEADER_BOTTOM;
				}
				rmi.groupHeader = true;
			}
			if(r == model.getRowCount(g) - 1) {
				if(model.getGroupFooterHeight(g) > 0) {
					trh += model.getGroupFooterHeight(g);
					trh += SEPARATED_FOOTER_TOP + SEPARATED_FOOTER_BOTTOM;
				}
				rmi.groupFooter= true;
			}
			rmi.rowEnd = lrb + trh;
			break;
		case SIMPLE:
			rh = model.getRowHeight(r);
			rmi.rowEnd = lrb + rh;
			break;
		}
		return rmi;
	}

	public IOSUIListViewRow(IOSUIListViewModel model, IOSUIComponent mview, RowMapItem rmi, IOSUIListView owner, boolean selected) {
		this.selected = selected;
		this.owner = owner;
		setBackground(owner.getBackground());
		createRowComponent(model,mview,rmi);
		lk = model.getListKind();
		addComponentListener(new ComponentListener() {
			@Override
			public void componentShown(ComponentEvent e) {
			}
			
			@Override
			public void componentResized(ComponentEvent e) {
				layoutComponent();
			}
			
			@Override
			public void componentMoved(ComponentEvent e) {
			}
			
			@Override
			public void componentHidden(ComponentEvent e) {
			}
		});
	}

	public void setSelected(boolean selected) {
		if(this.selected == selected) return;
		this.selected = selected;
	}
	
	public boolean isSelected() {
		return selected;
	}

	protected void layoutComponent() {
		for(Component cmp : getComponents()) {
			Rectangle cr = cmp.getBounds();
			if(lk == ListKind.SEPARATED) {
				cr.width = getWidth() - SEPARATED_ROW_LEFT - SEPARATED_ROW_RIGHT - 1;
			}
			else cr.width = getWidth();
			cmp.setBounds(cr);
		}
		
	}

	private void createRowComponent(IOSUIListViewModel model, IOSUIComponent mview, RowMapItem rmi) {
		int h = rmi.rowEnd-rmi.rowBegin;
//		this.mview = mview;
		if(model.getListKind() == ListKind.SIMPLE) {
			rk = RowKind.SIMPLE;
			setSize(10, h);
			mview.setBounds(0, 0, 10, h-2);
			add(mview);
		}
		else if(model.getListKind() == ListKind.GROUPPED) {
			if(rmi.groupHeader && rmi.groupFooter) {
				createGrouppedHeaderFooterRow(model,mview,rmi);
			} else if(rmi.groupHeader) {
				createGrouppedHeaderRow(model,mview,rmi);
			} else if(rmi.groupFooter) {
				createGrouppedFooterRow(model,mview,rmi);
			} else {
				createGrouppedSimpleRow(model,mview,rmi);
			}
		} else if(model.getListKind() == ListKind.SEPARATED) {
			mview.setBackground(new Color(255,255,255,0));
			if(rmi.groupHeader && rmi.groupFooter) {
				createSeparatedHeaderFooterRow(model,mview,rmi);
			} else if(rmi.groupHeader) {
				createSeparatedHeaderRow(model,mview,rmi);
			} else if(rmi.groupFooter) {
				createSeparatedFooterRow(model,mview,rmi);
			} else {
				createSeparatedSimpleRow(model,mview,rmi);
			}
		}
	}

	private int contentTop;
	private int contentBottom;
	private boolean addSubViews = true;
	
	//separated
	private void createSeparatedSimpleRow(IOSUIListViewModel model, IOSUIComponent mview, RowMapItem rmi) {
		rk = RowKind.SIMPLE;
		int ch = model.getRowHeight(rmi.group, rmi.row);
		mview.setBounds(SEPARATED_ROW_LEFT,0,10,ch);
		contentTop = 0;
		contentBottom = ch;
		if(addSubViews) add(mview);
	}

	private void createSeparatedFooterRow(IOSUIListViewModel model, IOSUIComponent mview, RowMapItem rmi) {
		rk = RowKind.FOOTER;
		int ch = model.getRowHeight(rmi.group, rmi.row);
		int hh = 0;
		mview.setBounds(SEPARATED_ROW_LEFT,hh,10,ch);
		contentTop = hh;
		contentBottom = hh+ch;
		hh += ch;
		if(addSubViews) add(mview);
		IOSUIComponent gfv = model.getGroupFooter(rmi.group);
		if(gfv != null) {
			ch = model.getGroupFooterHeight(rmi.group);
			gfv.setBounds(SEPARATED_ROW_LEFT,hh+SEPARATED_FOOTER_TOP,10,ch);
			if(addSubViews) add(gfv);
		}
	}

	private void createSeparatedHeaderRow(IOSUIListViewModel model, IOSUIComponent mview, RowMapItem rmi) {
		rk = RowKind.HEADER;
		IOSUIComponent ghv = model.getGroupHeader(rmi.group);
		int hh = SEPARATED_HEADER_TOP;
		if(ghv != null) {
			int ch = model.getGroupHeaderHeight(rmi.group);
			ghv.setBounds(SEPARATED_ROW_LEFT,hh,10,ch);
			if(addSubViews) add(ghv);
			hh += ch + SEPARATED_HEADER_BOTTOM; 
			ch = model.getRowHeight(rmi.group, rmi.row);
			mview.setBounds(SEPARATED_ROW_LEFT,hh,10,ch);
			contentTop = hh;
			contentBottom = hh+ch;
		} else {
			int ch = model.getRowHeight(rmi.group, rmi.row);
			contentTop = hh;
			contentBottom = hh+ch;
			mview.setBounds(SEPARATED_ROW_LEFT,hh,10,ch);
		}
		if(addSubViews) add(mview);
	}

	private void createSeparatedHeaderFooterRow(IOSUIListViewModel model, IOSUIComponent mview,	RowMapItem rmi) {
		rk = RowKind.HEADER_FOOTER;
		IOSUIComponent ghv = model.getGroupHeader(rmi.group);
		int hh = SEPARATED_HEADER_TOP;
		if(ghv != null) {
			int ch = model.getGroupHeaderHeight(rmi.group);
			ghv.setBounds(SEPARATED_ROW_LEFT,hh,10,ch);
			if(addSubViews) add(ghv);
			hh += ch + SEPARATED_HEADER_BOTTOM; 
			ch = model.getRowHeight(rmi.group, rmi.row);
			mview.setBounds(SEPARATED_ROW_LEFT,hh,10,ch);
			contentTop = hh;
			contentBottom = hh+ch;
			hh += ch;
		} else {
			int ch = model.getRowHeight(rmi.group, rmi.row);
			contentTop = hh;
			contentBottom = hh+ch;
			mview.setBounds(SEPARATED_ROW_LEFT,hh,10,ch);
			hh += ch;
		}
		if(addSubViews) add(mview);
		IOSUIComponent gfv = model.getGroupFooter(rmi.group);
		if(gfv != null) {
			int ch = model.getGroupFooterHeight(rmi.group);
			gfv.setBounds(SEPARATED_ROW_LEFT,hh+SEPARATED_FOOTER_TOP,10,ch);
			if(addSubViews) add(gfv);
		}
	}

	//groupped
	private void createGrouppedSimpleRow(IOSUIListViewModel model, IOSUIComponent mview, RowMapItem rmi) {
		rk = RowKind.SIMPLE;
		int ch = model.getRowHeight(rmi.group, rmi.row);
		contentTop = 0;
		contentBottom = ch;
		mview.setBounds(0,0,10,ch);
		if(addSubViews) add(mview);
	}

	private void createGrouppedFooterRow(IOSUIListViewModel model, IOSUIComponent mview, RowMapItem rmi) {
		rk = RowKind.FOOTER;
		int ch = model.getRowHeight(rmi.group, rmi.row);
		int hh = 0;
		mview.setBounds(0,hh,10,ch);
		contentTop = hh;
		contentBottom = hh+ch;
		hh += ch;
		if(addSubViews) add(mview);
		IOSUIComponent gfv = model.getGroupFooter(rmi.group);
		if(gfv != null) {
			ch = model.getGroupFooterHeight(rmi.group);
			gfv.setBounds(0,hh+GROUPPED_FOOTER_TOP,10,ch);
			if(addSubViews) add(gfv);
		}
	}

	private void createGrouppedHeaderRow(IOSUIListViewModel model, IOSUIComponent mview, RowMapItem rmi) {
		rk = RowKind.HEADER;
		IOSUIComponent ghv = model.getGroupHeader(rmi.group);
		int hh = GROUPPED_HEADER_TOP;
		if(ghv != null) {
			int ch = model.getGroupHeaderHeight(rmi.group);
			ghv.setBounds(0,hh,10,ch);
			if(addSubViews) add(ghv);
			hh += ch + GROUPPED_HEADER_BOTTOM; 
			ch = model.getRowHeight(rmi.group, rmi.row);
			mview.setBounds(0,hh,10,ch);
			contentTop = hh;
			contentBottom = hh+ch;
		} else {
			int ch = model.getRowHeight(rmi.group, rmi.row);
			contentTop = hh;
			contentBottom = hh+ch;
			mview.setBounds(0,hh,10,model.getRowHeight(rmi.group, rmi.row));
		}
		if(addSubViews) add(mview);
	}

	private void createGrouppedHeaderFooterRow(IOSUIListViewModel model, IOSUIComponent mview,RowMapItem rmi) {
		rk = RowKind.HEADER_FOOTER;
		IOSUIComponent ghv = model.getGroupHeader(rmi.group);
		int hh = GROUPPED_HEADER_TOP;
		if(ghv != null) {
			int ch = model.getGroupHeaderHeight(rmi.group);
			ghv.setBounds(0,hh,10,ch);
			if(addSubViews) add(ghv);
			hh += ch + GROUPPED_HEADER_BOTTOM; 
			ch = model.getRowHeight(rmi.group, rmi.row);
			mview.setBounds(0,hh,10,ch);
			contentTop = hh;
			contentBottom = hh+ch;
			hh += ch;
		} else {
			int ch = model.getRowHeight(rmi.group, rmi.row);
			contentTop = hh;
			contentBottom = hh+ch;
			mview.setBounds(0,hh,10,ch);
			hh += ch;
		}
		if(addSubViews) add(mview);
		IOSUIComponent gfv = model.getGroupFooter(rmi.group);
		if(gfv != null) {
			int ch = model.getGroupFooterHeight(rmi.group);
			gfv.setBounds(0,hh+GROUPPED_FOOTER_TOP,10,ch);
			if(addSubViews) add(gfv);
		}
	}

	@Override
	protected void paintUI(Graphics2D g2) {
		int h = getHeight();
		int w = getWidth();
		if(lk == ListKind.SIMPLE) {
			if(selected) {
				g2.setColor(selectedRowBackground);
			} else {
//				owner.getBackground()
				g2.setColor(getBackground());
			}
			g2.fillRect(0, 0, getWidth(), getHeight());
			
			g2.setColor(rowBorder);
			g2.drawLine(0, h-2, w, h-2);
			g2.setColor(rowBorder.brighter());
			g2.drawLine(0, h-1, w, h-1);
			return;
		}
		if(lk == ListKind.GROUPPED) {
			if(selected) {
				g2.setColor(getBackground());
				g2.fillRect(0, 0, getWidth(), getHeight());
				g2.setColor(selectedRowBackground);
				g2.fillRect(0,contentTop,w,contentBottom-contentTop);
			} else {
				g2.setColor(getBackground());
				g2.fillRect(0, 0, getWidth(), getHeight());
			}
			g2.setColor(rowBorder);
			g2.drawLine(0, h-2, w, h-2);
			g2.setColor(rowBorder.brighter());
			g2.drawLine(0, h-1, w, h-1);
			return;
		}
		g2.setColor(getBackground());
		g2.fillRect(0, 0, w, h);
		switch(rk) {
		case FOOTER:
			backupClipping(g2, new Rectangle(0,contentTop,w,contentBottom-contentTop-arcr));
			paintSimple(g2);
			restoreClipping(g2);
			paintFooter(g2);
			break;
		case HEADER:
			paintHeader(g2);
			backupClipping(g2, new Rectangle(0,contentTop+arcr,w,contentBottom-contentTop-arcr));
			paintSimple(g2);
			restoreClipping(g2);
			break;
		case HEADER_FOOTER:
			paintHeader(g2);
			backupClipping(g2, new Rectangle(0,contentTop+arcr,w,contentBottom-contentTop-arcr-arcr));
			paintSimple(g2);
			restoreClipping(g2);
			paintFooter(g2);
			break;
		case SIMPLE:
			paintSimple(g2);
			break;
		}
	}

	private Color _rowBackground = Color.WHITE;
	private Color rowBorder = Color.LIGHT_GRAY;
	private Color selectedRowBackgroundFrom = new Color(0,145,247);
	private Color selectedRowBackgroundTo = new Color(0,102,232);
	
	private Color selectedRowBackground = new Color(0,102,232);
	
	private void paintSimple(Graphics2D g2) {
		int h = getHeight();
		int w = getWidth();
		if(selected) {
			g2.setColor(selectedRowBackground);
		} else {
			g2.setColor(_rowBackground);
		}
		g2.fillRect(SEPARATED_ROW_LEFT, 0, w-SEPARATED_ROW_LEFT-SEPARATED_ROW_RIGHT,h);
		g2.setColor(rowBorder);
		g2.drawLine(SEPARATED_ROW_LEFT-1, 0, SEPARATED_ROW_LEFT-1, h);
		g2.drawLine(w-SEPARATED_ROW_RIGHT, 0, w-SEPARATED_ROW_RIGHT, h);
		g2.drawLine(SEPARATED_ROW_LEFT-1, h-1, w-SEPARATED_ROW_RIGHT, h-1);

//		g2.setColor(rowBorder.darker());
//		g2.drawLine(SEPARATED_ROW_LEFT, 0, SEPARATED_ROW_LEFT, h);
//		g2.setColor(rowBorder.brighter());
//		g2.drawLine(w-SEPARATED_ROW_RIGHT+1, 0, w-SEPARATED_ROW_RIGHT+1, h);

	}

	private void paintHeader(Graphics2D g2) {
		int h = getHeight();
		int w = getWidth();
		backupClipping(g2, new Rectangle(0, contentTop, w, arcr));
		RoundRectangle2D r2d = new RoundRectangle2D.Double(SEPARATED_ROW_LEFT-1, contentTop, w-SEPARATED_ROW_RIGHT-SEPARATED_ROW_LEFT+1, h, arcr+arcr, arcr+arcr);
		if(selected) {
			g2.setColor(selectedRowBackground);
		} else {
			g2.setColor(_rowBackground);
		}
		g2.fill(r2d);
		
		r2d = new RoundRectangle2D.Double(SEPARATED_ROW_LEFT-1, contentTop+1, w-SEPARATED_ROW_RIGHT-SEPARATED_ROW_LEFT+1, h, arcr+arcr, arcr+arcr);
		g2.setColor(IOSUIHelper.setAlpha(rowBorder.darker(),180));
		g2.draw(r2d);
		
		r2d = new RoundRectangle2D.Double(SEPARATED_ROW_LEFT-1, contentTop, w-SEPARATED_ROW_RIGHT-SEPARATED_ROW_LEFT+1, h, arcr+arcr, arcr+arcr);
		g2.setColor(rowBorder);
		g2.draw(r2d);
		restoreClipping(g2);
	}

	private void paintFooter(Graphics2D g2) {
		int h = getHeight();
		int w = getWidth();
		backupClipping(g2, new Rectangle(0, contentBottom-arcr, w, arcr+1));
		RoundRectangle2D r2d = new RoundRectangle2D.Double(SEPARATED_ROW_LEFT-1, contentTop, w-SEPARATED_ROW_RIGHT-SEPARATED_ROW_LEFT+1, contentBottom-contentTop, arcr+arcr,arcr+arcr);
		if(selected) {
			g2.setColor(selectedRowBackground);
		} else {
			g2.setColor(_rowBackground);
		}
		g2.fill(r2d);
		
		r2d = new RoundRectangle2D.Double(SEPARATED_ROW_LEFT-1, contentTop, w-SEPARATED_ROW_RIGHT-SEPARATED_ROW_LEFT+1, contentBottom-contentTop, arcr+arcr,arcr+arcr);		
		g2.setColor(rowBorder.brighter());
		g2.draw(r2d);
		
		r2d = new RoundRectangle2D.Double(SEPARATED_ROW_LEFT-1, contentTop, w-SEPARATED_ROW_RIGHT-SEPARATED_ROW_LEFT+1, contentBottom-contentTop-1, arcr+arcr,arcr+arcr);		
		g2.setColor(rowBorder);
		g2.draw(r2d);
		restoreClipping(g2);
	}

}

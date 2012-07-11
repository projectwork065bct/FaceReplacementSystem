package test;

import java.awt.geom.Arc2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import javax.swing.event.ListDataListener;

public class ListScrollTest {
	private static ListModel lm = new ListModel() {
		@Override
		public void removeListDataListener(ListDataListener l) {
		}
		
		@Override
		public int getSize() {
			return 1000;
		}
		
		@Override
		public Object getElementAt(int index) {
			return "Element "+index;
		}
		
		@Override
		public void addListDataListener(ListDataListener l) {
		}
	};
	public static void main(String[] args) {
		
		RoundRectangle2D rr2d = new RoundRectangle2D.Double(0, 0, 200, 100, 10, 10);
//		Arc2D a2d = new Arc2D.Double(x, y, w, h, start, extent, type)
		JFrame frm = new JFrame("ListScrollTest");
		JList lst = new JList(lm);
		JScrollPane sp = new JScrollPane(lst);
		sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		frm.getContentPane().add(sp);
		frm.setSize(200,400);
		frm.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frm.setLocationRelativeTo(null);
		frm.setVisible(true);
	}

}

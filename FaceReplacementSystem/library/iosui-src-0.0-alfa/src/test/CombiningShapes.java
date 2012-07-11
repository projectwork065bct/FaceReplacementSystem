package test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class CombiningShapes extends JComponent {
	private Shape mShapeOne, mShapeTwo;

	private JComboBox mOptions;

	public CombiningShapes() {
		mShapeOne = new Ellipse2D.Double(40, 20, 80, 80);
		mShapeTwo = new Rectangle2D.Double(60, 40, 80, 80);
		setBackground(Color.white);
		setLayout(new BorderLayout());

		JPanel controls = new JPanel();

		mOptions = new JComboBox(new String[] { "outline", "add",
				"intersection", "subtract", "exclusive or" });

		mOptions.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent ie) {
				repaint();
			}
		});
		controls.add(mOptions);
		add(controls, BorderLayout.SOUTH);
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		String option = (String) mOptions.getSelectedItem();
		if (option.equals("outline")) {
			// draw the outlines and return.
			g2.draw(mShapeOne);
			g2.draw(mShapeTwo);
			return;
		}

		// Create Areas from the shapes.
		Area areaOne = new Area(mShapeOne);
		Area areaTwo = new Area(mShapeTwo);
		// Combine the Areas according to the selected option.
		if (option.equals("add"))
			areaOne.add(areaTwo);
		else if (option.equals("intersection"))
			areaOne.intersect(areaTwo);
		else if (option.equals("subtract"))
			areaOne.subtract(areaTwo);
		else if (option.equals("exclusive or"))
			areaOne.exclusiveOr(areaTwo);

		// Fill the resulting Area.
		g2.setPaint(Color.orange);
		g2.fill(areaOne);
		// Draw the outline of the resulting Area.
		g2.setPaint(Color.black);
		g2.draw(areaOne);
	}
	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.add(new CombiningShapes());
		f.setSize(220, 220);
		f.setVisible(true);
	}

}


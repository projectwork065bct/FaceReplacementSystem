package hu.droidzone.iosui.gestures;

import java.awt.Point;

public interface GestureListener {
	void dragRecognized(int dx, int dy, long dt, int x, int y);
	void dragFinished(int dx, int dy, long dt, int x, int y);
	void swipeRecognized(int dx, int dy, long dt, double speed);
	void pinchRecognized();
	void wheelRotation(int wheelRotation);
	void mouseClicked(Point point);
}

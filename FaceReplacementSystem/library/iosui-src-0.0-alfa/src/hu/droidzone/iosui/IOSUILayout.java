package hu.droidzone.iosui;

import hu.droidzone.iosui.utils.AbstractContentHandler;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import com.sun.org.apache.xerces.internal.parsers.SAXParser;

public class IOSUILayout {
	SAXParser parser;
	ContentHandler xmlHandler = new AbstractContentHandler() {
		@Override
		public void startElement(String uri, String localName, String qName,Attributes atts) throws SAXException {
			
		}

		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			
		}
	};
	
	public IOSUILayout() {
		parser = new SAXParser();
		parser.setContentHandler(xmlHandler);
	}
	
}

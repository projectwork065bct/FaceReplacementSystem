package hu.droidzone.iosui.i18n;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

public class IOSI18N {
	Properties dp;
	Properties cp;
	
	private IOSI18N() {
		dp = new Properties();
		try {
			dp.load(IOSI18N.class.getResourceAsStream("en.properties"));
		} catch (IOException e) {
		}
		cp = dp;
	}
	
	private static IOSI18N inst = new IOSI18N();
	
	public static void load(String nm) {
		inst._load(nm);
	}
	
	private void _load(String nm) {
		cp = new Properties();
		try {
			Reader rdr = new InputStreamReader(IOSI18N.class.getResourceAsStream(nm), "UTF-8");
			cp.load(rdr);
			rdr.close();
		} catch (IOException e) {
			cp = dp;
		}
	}
	public static String get(String key) {
		return inst._get(key);
	}

	private String _get(String key) {
		String ret = cp.getProperty(key);
		if(ret == null) ret = dp.getProperty(key);
		if(ret == null) {
			ret = "**N/A**";
			System.out.println("Requested string missing! Key : "+key);
		}
		return ret;
	}
}

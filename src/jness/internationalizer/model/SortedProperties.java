package jness.internationalizer.model;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

public class SortedProperties extends Properties {
	private static final long serialVersionUID = 1L;

	public Enumeration keys() {
		Enumeration<Object> keysEnum = super.keys();
		Vector<String> keyList = new Vector<String>();
		
		while (keysEnum.hasMoreElements()) {
			keyList.add((String) keysEnum.nextElement());
		}
		
		Collections.sort(keyList);
		
		return keyList.elements();
	}
}

package languagelearning.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

public class Props {
	private Map<String,String> values = new HashMap<String,String>();
	private List<String> keys = new ArrayList<String>();
	
	public Props() {
		
	}
	
	public Props(File file) throws IOException {
		loadFromFile(file);
	}
	
	public boolean hasKey(String key) {
		return values.containsKey(key);
	}
	
	public String getStringValue(String key) {
		return values.get(key);
	}
	
	public int getIntValue(String key) {
		String str = getStringValue(key);
		if (str != null) {
			return Integer.valueOf(str).intValue();
		} else {
			return 0;
		}
	}
	
	public double getDoubleValue(String key) {
		String str = getStringValue(key);
		if (str != null) {
			return Double.valueOf(str).doubleValue();
		} else {
			return 0;
		}
	}
	
	public int[] getIntArrayValue(String key) {
		String str = getStringValue(key);
		if (str != null) {
			String[] strArray = str.split(",");
			int[] value = new int[strArray.length];
			for (int i = 0; i < strArray.length; i++) {
				value[i] = Integer.valueOf(strArray[i]);
			}
			return value;
		} else {
			return new int[0];
		}
	}
	
	public String[] getStringArrayValue(String key) {
		String str = getStringValue(key);
		if (str != null) {
			return str.split(",");
		} else {
			return new String[0];
		}
	}

	public boolean getBooleanValue(String key) {
		String str = getStringValue(key);
		if (str != null) {
			return Boolean.valueOf(str).booleanValue();
		} else {
			return false;
		}
	}
	
	public void addValue(String key,String value) {
		values.put(key, value);
		keys.add(key);
	}
	
	public void addValue(String key,int value) {
		addValue(key,String.valueOf(value));
	}
	
	public void addValue(String key,double value) {
		addValue(key,String.valueOf(value));
	}
	
	public void addValue(String key,int[] value) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < value.length; i++) {
			if (i > 0) {
				buffer.append(',');
			}
			buffer.append(value[i]);
		}
		addValue(key,buffer.toString());
	}
	
	public void addValue(String key,String[] value) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < value.length; i++) {
			if (i > 0) {
				buffer.append(',');
			}
			buffer.append(value[i]);
		}
		addValue(key,buffer.toString());
	}
	
	public void addValue(String key,boolean value) {
		addValue(key,String.valueOf(value));
	}
	
	private void loadFromFile(File file) throws IOException {
		List<String> lines = FileUtils.readLines(file, "UTF-8");
		for (String line: lines) {
			int sep = line.indexOf('=');
			String key = line.substring(0,sep).trim();
			String value = line.substring(sep+1).trim();
			addValue(key,value);
		}
	}
	
	public void saveToFile(File file) throws IOException {
		List<String> lines = new ArrayList<String>();
		for (String key: keys) {
			String value = getStringValue(key);
			String line = key + " = " + value;
			lines.add(line);
		}
		FileUtils.writeLines(file, "UTF-8", lines);
	}
}

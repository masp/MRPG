package masp.plugins.mlight.data.items;

import java.util.HashMap;
import java.util.Map;

public class MItemData {
	
	private Map<String, String> data = new HashMap<String, String>();
	
	public void addInteger(String key, int data) {
		this.data.put(key, Integer.toString(data));
	}
	
	public void addString(String key, String data) {
		this.data.put(key, data);
	}
	
	public void addDouble(String key, double data) {
		this.data.put(key, Double.toString(data));
	}
	
	public String getString(String key) {
		return data.get(key);
	}
	
	public int getInt(String key) {
		return Integer.parseInt(getString(key));
	}
	
	public double getDouble(String key) {
		return Double.parseDouble(getString(key));
	}
	
	public void setInteger(String key, int data) {
		addInteger(key, data);
	}
	
	public void setString(String key, String data) {
		addString(key, data);
	}
	
	public void setDouble(String key, double data) {
		addDouble(key, data);
	}
	
}

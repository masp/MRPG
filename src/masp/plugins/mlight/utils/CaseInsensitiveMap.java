package masp.plugins.mlight.utils;

import java.util.HashMap;

public class CaseInsensitiveMap<T> extends HashMap<String, T> {

	private static final long serialVersionUID = 1991560591028292483L;
	
	@Override
	public T put(String key, T value) {
		return super.put(key.toLowerCase(), value);
	}
	
	@Override
	public T get(Object key) {
		if (key instanceof String == false) {
			return null;
		}
		return super.get(((String) key).toLowerCase());
	}

}

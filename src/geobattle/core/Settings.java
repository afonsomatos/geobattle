package geobattle.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class Settings {
	
	private String file = "geobattle/config.properties";
	private Properties properties = new Properties();
	
	Settings() {

	}
	
	void loadDefault() throws IOException {
		try(InputStream input = Settings.class.getClassLoader().getResourceAsStream(file)) {
			load(input);
		}
	}
	
	void load(InputStream input) throws IOException {
		properties.load(input);
	}
	
	void save(OutputStream output) throws IOException {
		properties.store(output, null);
	}
	
	public String get(String name) {
		return properties.getProperty(name);
	}
	
	public int getInt(String name) {
		return Integer.valueOf(properties.getProperty(name));
	}
	
	public double getDouble(String name) {
		return Double.valueOf(properties.getProperty(name));
	}
	
	public boolean getBoolean(String name) {
		return Boolean.valueOf(properties.getProperty(name));
	}
	
}

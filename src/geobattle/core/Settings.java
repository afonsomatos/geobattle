package geobattle.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Maintains the properties of the application.
 */
public class Settings {

	private final static String FILE_NAME = "geobattle/config.properties";
	
	private Properties properties = new Properties();

	Settings() {

	}

	void loadDefault() throws IOException {
		try (InputStream input = Settings.class.getClassLoader()
				.getResourceAsStream(FILE_NAME)) {
			load(input);
		}
	}

	void load(InputStream input) throws IOException {
		properties.load(input);
	}

	void save(OutputStream output) throws IOException {
		properties.store(output, null);
	}

	/**
	 * Returns the {@code String} representation of the property with the given
	 * {@code name}.
	 * 
	 * @param name
	 * @return string value of the property
	 */
	public String get(String name) {
		return properties.getProperty(name);
	}

	/**
	 * Returns the {@code Integer} representation of the property with the given
	 * {@code name}.
	 * 
	 * @param name
	 * @return int value of the property
	 */
	public int getInt(String name) {
		return Integer.valueOf(properties.getProperty(name));
	}

	/**
	 * Returns the {@code Double} representation of the property with the given
	 * {@code name}.
	 * 
	 * @param name
	 * @return double value of the property
	 */
	public double getDouble(String name) {
		return Double.valueOf(properties.getProperty(name));
	}

	/**
	 * Returns the {@code Boolean} representation of the property with the given
	 * {@code name}.
	 * 
	 * @param name
	 * @return boolean value of the property
	 */
	public boolean getBoolean(String name) {
		return Boolean.valueOf(properties.getProperty(name));
	}

}

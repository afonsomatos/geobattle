package geobattle.util;

public final class Log {
	
	public static void v(String message) {
		log("VERBOSE", message);
	}
	
	public static void i(String message) {
		log("INFO", message);
	}
	
	public static void e(String message) {
		log("ERROR", message);
	}
	
	public static void w(String message) {
		log("WARNING", message);
	}
	
	private static void log(String tag, String message) {
		System.out.println("[" + tag + "] " + message);
	}
	
	private Log() {
		
	}
	
}

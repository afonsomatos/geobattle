package geobattle.util;

public final class Log {
	
	public static void v(Object message) {
		log("VERBOSE", message);
	}
	
	public static void i(Object message) {
		log("INFO", message);
	}
	
	public static void e(Object message) {
		log("ERROR", message);
	}
	
	public static void w(Object message) {
		log("WARNING", message);
	}
	
	private static void log(String tag, Object message) {
		System.out.println("[" + tag + "] " + message.toString());
	}
	
	private Log() {
		
	}
	
}

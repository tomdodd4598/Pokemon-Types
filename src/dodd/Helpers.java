package dodd;

import java.io.IOException;
import java.util.Locale;

public class Helpers {
	
	public static String read() {
		try {
			return Main.READER.readLine();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String lowerCase(String str) {
		return str.toLowerCase(Locale.ROOT);
	}
	
	public static int hcf(int a, int b) {
		return b == 0 ? a : hcf(b, a % b);
	}
	
	public static <T> T[] arr(T... varargs) {
		return varargs;
	}
}

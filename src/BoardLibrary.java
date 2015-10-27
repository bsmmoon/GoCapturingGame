
public class BoardLibrary {
	public static String removeDoubleSpacing(String str) {
		while (str.indexOf("  ") != -1) {
			str = str.replaceAll("  ", " ");
		}
		return str;
	}

}

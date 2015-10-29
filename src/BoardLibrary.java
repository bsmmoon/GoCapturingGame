import java.util.ArrayList;


public class BoardLibrary {
	public static String removeDoubleSpacing(String str) {
		while (str.indexOf("  ") != -1) {
			str = str.replaceAll("  ", " ");
		}
		return str;
	}

	public static ArrayList < int[] > findAdj4(int[] boardSize, int row, int col) {
		ArrayList < int[] > out = new ArrayList < int[] > ();
		if (col+1 < boardSize[1])
			out.add(new int[]{row, col+1});
		if (col-1 >= 0)
			out.add(new int[]{row, col-1});
		if (row+1 < boardSize[0])
			out.add(new int[]{row+1, col});
		if (row-1 >= 0)
			out.add(new int[]{row-1, col});
		return out;
	}
}

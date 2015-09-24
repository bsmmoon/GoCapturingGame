import java.util.*;

class Main {
	int[] boardSize = {20,20};
	BoardMaster bm;

	String[] menu = {
		"0. initialize map",
		"1. move",
		"9. exit game"
	};

	Random rnd = new Random();
	Scanner sc = new Scanner(System.in);

	public Main() {
		bm = new BoardMaster(boardSize);
	}

	public void run() {
		boolean power = true;
		while (power) {
			bm.initMap();

			ArrayList < int[] > nextMoves = bm.getNextMove();

			boolean command_chk;
			do {
				command_chk = true;
				bm.drawMap();
				bm.getPlayerPosition();

				System.out.println("< MENU >");
				for (int i = 0; i < menu.length; i++) {
					System.out.println(menu[i]);
				}
				System.out.println("Please choose the menu");
				String command = sc.next();
				switch (command) {
					case "0":
						bm.initMap();
						break;
					case "1":
						System.out.println("Please select the city:");
						for (int i = 0; i < nextMoves.size(); i++) {
							System.out.println(i+": {"+nextMoves.get(i)[0]+","+nextMoves.get(i)[1]+"}");
						}
						command = sc.next();
						break;
					case "9":
						power = false;
						break;
					default:
						System.out.println("Command not recognized");
						command_chk = false;
				}
			} while (!command_chk);
		}
		System.out.println("Bye");
	}

	public static void main(String[] args) {
		Main game = new Main();
		game.run();
	}
}
/*
	To Do List:
		- command system
		- undo!!
*/


import java.util.*;

class Go {
	boolean debugSwitch = false;

	boolean capturingRace = true;

	Scanner sc;
	int[] boardSize;
	int[][] board;
	boolean[][] boardMemo;

	boolean gameover;
	int turn;
	int[] countCaptured;

	public boolean debug(int input) {
		if (debugSwitch) {
			switch (input) {
				case 1: return true;
				case 2: return true;
			}
		}
		return false;
	}

	public Go() {
		sc = new Scanner(System.in);

		boardSize = new int[2];
		if (capturingRace) {
			boardSize[0] = boardSize[1] = 13;
			board = new int[boardSize[0]][boardSize[1]];
			board[5][5] = 1;
			board[6][6] = 1;
			board[5][6] = 2;
			board[6][5] = 2;
		}
		else {
			System.out.print("board size: ");
			boardSize[0] = boardSize[1] = sc.nextInt();
			board = new int[boardSize[0]][boardSize[1]];
		}

		boardMemo = new boolean[boardSize[0]][boardSize[1]];

		gameover = false;
		turn = 1;
		countCaptured = new int[]{0,0,0};
	}

	public String removeDoubleSpacing(String str) {
		while (str.indexOf("  ") != -1) {
			str = str.replaceAll("  ", " ");
		}
		return str;
	}

	public String readCommandLine() {
		System.out.print("Command: ");
		return sc.nextLine();
	}

	public String[] readCommandArr(String commandLine) {
		return removeDoubleSpacing(commandLine.trim()).split(" ");
	}

	public void executeCommand(String commandLine, int player) throws Exception {
		String[] commandArr = readCommandArr(commandLine);
		String command = commandArr[0];

		switch (command.toLowerCase()) {
			case "move":
				if (commandArr.length != 3) {
					if (commandArr.length > 3) System.out.println("Too many arguments");
					else System.out.println("Too little arguments");
					break;
				}
				int row = Integer.parseInt(commandArr[1])-1;
				int col = Integer.parseInt(commandArr[2])-1;
				if (board[row][col] != 0) {
					throw new Exception();
				}
				int[][] previous = board;
				board[row][col] = player;
				
				checkMove(row, col);

				printStatus();
				break;
			case "exit":
				break;
			case "undo":
				break;
			default:
				System.out.println("No such command ("+command+")");
		}
	}

	public void run() {
		int player = 1;
		while (!gameover) {
			try {
				printBoard();

				if (player == 1) System.out.print("Player1 ");
				else System.out.print("Player2 ");
				// System.out.print("next move: ");
				// int row = sc.nextInt()-1;
				// int col = sc.nextInt()-1;

				String command = readCommandLine();
				executeCommand(command, player);


				if (capturingRace) {
					if (countCaptured[1] > 0) {
						printBoard();
						System.out.println("Player1 Wins!");
						break;
					}
					else if (countCaptured[2] > 0) {
						printBoard();
						System.out.println("Player2 Wins!");
						break;
					}
				}

				if (player == 1) player = 2;
				else player = 1;
				turn++;
			} catch (Exception e) {
				System.out.println("Illegal Move");
				System.out.println();
			}
		}
	}

	public void makeMove(int player, int row, int col) throws Exception {
		if (board[row][col] != 0) {
			throw new Exception();
		}
		int[][] previous = board;
		board[row][col] = player;
		
		checkMove(row, col);

		printStatus();
	}

	public void checkMove(int row, int col) throws Exception {
		ArrayList < int[] > adj = findAdj4(row, col);

		boolean selfSurrounded = checkSurrounded(row, col);

		int player = board[row][col];
		clearSurrounded(false, player);

		boolean captured = false;
		for (int i = 0; i < adj.size(); i++) {
			int[] pos = adj.get(i);
			if (board[pos[0]][pos[1]] == board[row][col]) continue;
			boolean surrounded = checkSurrounded(pos[0],pos[1]);
			captured = captured || surrounded;
			clearSurrounded(surrounded, player);
		}

		if (!captured && selfSurrounded) {
			board[row][col] = 0;
			throw new Exception();
		}
	}

	public int clearSurrounded(boolean surrounded, int player) {
		if (surrounded && debug(1)) printMemoBoard();

		int count = 0;

		if (surrounded) {
			for (int i = 0; i < boardSize[0]; i++) {
				for (int j = 0; j < boardSize[1]; j++) {
					if (boardMemo[i][j]) {
						board[i][j] = 0;
						count++;
					}
				}
			}
		}
		
		countCaptured[player] += count;
		boardMemo = new boolean[boardSize[0]][boardSize[1]];

		return count;
	}

	public boolean checkSurrounded(int row, int col) {
		if (board[row][col] == 0) return false;
		return checkSurroundedRecurr(row, col);
	}

	public boolean checkSurroundedRecurr(int row, int col) {
		int player = board[row][col];
		if (boardMemo[row][col]) {
			System.out.println("wut");
		}
		else boardMemo[row][col] = true;

		ArrayList < int[] > adj = findAdj4(row, col);

		if (debug(2)) {
			System.out.print("["+(1+row)+","+(1+col)+"] ");
			for (int i = 0; i < adj.size(); i++) {
				int[] pos = adj.get(i);
				System.out.print("("+(1+pos[0])+","+(1+pos[1])+") ");
			}
			System.out.println();
		}

		for (int i = 0; i < adj.size(); i++) {
			int[] pos = adj.get(i);
			if (boardMemo[pos[0]][pos[1]]) continue;
			if (board[pos[0]][pos[1]] == 0) return false;
			else if (board[pos[0]][pos[1]] == player) {
				if (!checkSurroundedRecurr(pos[0], pos[1])) return false;
			}

		}

		return true;
	}

	public void printStatus() {
		System.out.println("Player1 Captured: "+countCaptured[1]);
		System.out.println("Player2 Captured: "+countCaptured[2]);
		System.out.println();
	}

	public void printMemoBoard() {
		System.out.print("00.");
		for (int col = 0; col < boardSize[0]; col++) {
			if (col < 9) System.out.print("0");
			System.out.print((1+col)+".");
		}
		System.out.println();
		for (int i = 0; i < boardSize[0]; i++) {
			if (i < 9) System.out.print("0");
			System.out.print((1+i)+".");
			for (int j = 0; j < boardSize[1]; j++) {
				if (boardMemo[i][j]) {
					System.out.print(" O ");
				}
				else {
					System.out.print(" X ");
				}
			}
			System.out.println();
		}
		System.out.println();
	}

	public void printBoard() {
		System.out.println();
		System.out.println("<Turn "+turn+">");
		System.out.print("00.");
		for (int col = 0; col < boardSize[0]; col++) {
			if (col < 9) System.out.print("0");
			System.out.print((1+col)+".");
		}
		System.out.println();
		for (int row = 0; row < boardSize[0]; row++) {
			if (row < 9) System.out.print("0");
			System.out.print((1+row)+".");
			for (int col = 0; col < boardSize[1]; col++) {
				if (board[row][col] == 0) {
					System.out.print(" . ");
				}
				else System.out.print(" "+board[row][col]+" ");
			}
			System.out.println();
		}
		System.out.println();

		printStatus();
	}

	public static void main(String[] args) {
		Go go = new Go();
		go.run();
	}

	public ArrayList < int[] > findAdj4(int row, int col) {
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
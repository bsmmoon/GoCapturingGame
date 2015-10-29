import java.util.ArrayList;

public class Board {
	int[] boardSize;
	int[][] board;
	boolean[][] boardMemo;
	
	int turn;
	int[] countCaptured;
	
	public Board(){
		boardSize = new int[2];
		boardSize[0] = boardSize[1] = 13;
		
		board = new int[boardSize[0]][boardSize[1]];
		board[5][5] = 1;
		board[6][6] = 1;
		board[5][6] = 2;
		board[6][5] = 2;
		
		boardMemo = new boolean[boardSize[0]][boardSize[1]];

		turn = 1;
		countCaptured = new int[]{0,0,0};
		
		printBoard();
	}
	
	public int getPlayer() {
		int player = turn % 2;
		if (player == 0) {
			player = 2;
		}
		return player;
	}
	
	public int[] getCountCaptured() {
		return this.countCaptured;
	}
	
	public void makeMove(int row, int col) throws Exception {
		if (board[row][col] != 0) {
			throw new Exception();
		}

		int player = getPlayer();
		board[row][col] = player;
		
		checkMove(row, col);
		turn++;
	}
	
	private void checkMove(int row, int col) throws Exception {
		ArrayList < int[] > adj = BoardLibrary.findAdj4(this.boardSize, row, col);

		boolean selfSurrounded = checkSurrounded(row, col);

		int player = board[row][col];

		boolean captured = false;
		for (int i = 0; i < adj.size(); i++) {
			int[] pos = adj.get(i);
			if (board[pos[0]][pos[1]] == board[row][col]) continue;
			boolean surrounded = checkSurrounded(pos[0],pos[1]);
			captured = captured || surrounded;
			
			if (surrounded) {
				clearSurrounded(player);
			}
			
			boardMemo = new boolean[boardSize[0]][boardSize[1]];
		}

		if (!captured && selfSurrounded) {
			board[row][col] = 0;
			throw new Exception();
		}
	}

	private boolean checkSurrounded(int row, int col) {
		if (board[row][col] == 0) return false;
		return checkSurroundedRecurr(row, col);
	}

	public boolean checkSurroundedRecurr(int row, int col) {
		int player = board[row][col];
		if (!boardMemo[row][col]) {
			boardMemo[row][col] = true;
		}
		
		ArrayList < int[] > adj = BoardLibrary.findAdj4(this.boardSize, row, col);

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
	
	private int clearSurrounded(int player) {
		int count = 0;

		for (int i = 0; i < boardSize[0]; i++) {
			for (int j = 0; j < boardSize[1]; j++) {
				if (boardMemo[i][j]) {
					board[i][j] = 0;
					count++;
				}
			}
		}
		
		countCaptured[player] += count;
		return count;
	}
	
	public void printGame() {
		printBoard();
		printStatus();
		printTurn();
	}

	public void printStatus() {
		System.out.println("Player1 Captured: "+countCaptured[1]);
		System.out.println("Player2 Captured: "+countCaptured[2]);
		System.out.println();
	}
	
	public void printBoard() {
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
	}
	
	public void printTurn() {
		System.out.println("<Turn "+turn+"> Player " + Integer.toString(getPlayer()));
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
}

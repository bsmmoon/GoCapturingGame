import java.util.ArrayList;
import java.util.Stack;

public class BoardMaster {
	int[] boardSize;
	int[][] board;
	Stack<int[][]> undoStack;
	Stack<int[][]> redoStack;
	boolean[][] boardMemo;
	boolean gameover;
	
	int turn;
	int[] countCaptured;
	
	public BoardMaster(){
		boardSize = new int[2];
		boardSize[0] = boardSize[1] = 13;
		
		board = new int[boardSize[0]][boardSize[1]];
		board[5][5] = 1;
		board[6][6] = 1;
		board[5][6] = 2;
		board[6][5] = 2;
		
		undoStack = new Stack<int[][]>();
		undoStack.push(board);
		redoStack = new Stack<int[][]>();

		boardMemo = new boolean[boardSize[0]][boardSize[1]];

		gameover = false;
		turn = 1;
		countCaptured = new int[]{0,0,0};
	}
	
	public boolean isGameOver() {
		return this.gameover;
	}

	
	public void makeMove(int row, int col) throws Exception {
		if (board[row][col] != 0) {
			throw new Exception();
		}
		undoStack.push(board);
		int player = turn % 2;
		if (player == 0) {
			player = 2;
		}

		board[row][col] = player;
		
		checkMove(row, col);
		turn++;
	}
	
	public void undo() {
		if (this.undoStack.empty()) {
			return;
		}
		int[][] previousBoard = this.undoStack.pop();
		this.board = previousBoard;
		this.redoStack.push(previousBoard);
		this.turn--;
	}
	
	public void redo() {
		if (this.redoStack.empty()) {
			return;
		}
		int[][] originalBoard = this.redoStack.pop();
		this.board = originalBoard;
		this.undoStack.push(originalBoard);
		this.turn++;
	}
	
	private void checkMove(int row, int col) throws Exception {
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

	private int clearSurrounded(boolean surrounded, int player) {
		if (surrounded && false) printMemoBoard();

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

	private boolean checkSurrounded(int row, int col) {
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

		if (false) {
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


public class GoMaster {
	private Board board;
	private BoardMaster boardMaster;
	private boolean gameOver = false;
	
	public GoMaster() {
		this.board = new Board();
		this.boardMaster = new BoardMaster();
		this.board.printGame();		
	}
	
	public boolean isGameOver() {
		// Capturing Game Rule
		return this.gameOver;
	}

	public void execCommand(GoCommand command) {
		try {
			this.board = command.execute(this.board, this.boardMaster);	
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		String winner = "";
		if (this.board.countCaptured[1] > 0) {
			winner = "1";
			this.gameOver = true;
		}
		else if (this.board.countCaptured[2] > 0) {
			winner = "2";
			this.gameOver = true;
		}
		
		this.board.printGame();
		if (this.gameOver) {
			System.out.println("Player" + winner + " Wins!");
		}
	}
}

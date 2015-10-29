
public class BoardMaster {
	private Board board;
	private boolean gameOver = false;
	
	public BoardMaster() {
		this.board = new Board();
		this.board.printGame();
	}
	
	public boolean isGameOver() {
		// Capturing Game Rule
		return this.gameOver;
	}

	public void execCommand(GoCommand command) {
		try {
			command.execute(this.board);	
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

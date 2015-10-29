
public class MoveCommand implements GoCommand {
	int row;
	int col;
	
	public MoveCommand(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	@Override
	public void execute(Board board, BoardMaster boardMaster) throws Exception {
		// TODO Auto-generated method stub
		boardMaster.pushToUndo(board);
		board.makeMove(this.row, this.col);
	}

}

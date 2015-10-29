
public class moveCommand implements GoCommand {
	int row;
	int col;
	
	public moveCommand(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	@Override
	public void execute(Board board) throws Exception {
		// TODO Auto-generated method stub
		board.makeMove(this.row, this.col);
	}

}

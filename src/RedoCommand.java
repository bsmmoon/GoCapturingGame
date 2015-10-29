
public class RedoCommand implements GoCommand {
	public RedoCommand() {
		
	}

	@Override
	public Board execute(Board board, BoardMaster boardMaster) throws Exception {
		// TODO Auto-generated method stub
		return boardMaster.redo(board);
	}

}

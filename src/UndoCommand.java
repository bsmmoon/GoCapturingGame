
public class UndoCommand implements GoCommand {
	public UndoCommand() {
		
	}
	
	@Override
	public Board execute(Board board, BoardMaster boardMaster) throws Exception {
		// TODO Auto-generated method stub
		return boardMaster.undo(board);
	}

}


public class UndoCommand implements GoCommand {
	public UndoCommand() {
		
	}
	
	@Override
	public void execute(Board board, BoardMaster boardMaster) throws Exception {
		// TODO Auto-generated method stub
		boardMaster.undo(board);
	}

}


public class RedoCommand implements GoCommand {
	public RedoCommand() {
		
	}

	@Override
	public void execute(Board board, BoardMaster boardMaster) throws Exception {
		// TODO Auto-generated method stub
		boardMaster.redo(board);
	}

}

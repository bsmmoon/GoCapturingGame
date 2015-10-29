import java.util.Stack;

public class BoardMaster {
	Stack<Board> undoStack;
	Stack<Board> redoStack;

	public BoardMaster() {
		undoStack = new Stack<Board>();
		redoStack = new Stack<Board>();
	}
	
	public void pushToUndo(Board board) {
		
		this.undoStack.push(board);
	}

	public void pushToRedo(Board board) {
		
		this.redoStack.push(board);
	}
	
	public void undo(Board board) {
		if (this.undoStack.empty()) {
			return;
		}
		pushToRedo(board);
		board = this.undoStack.pop();
	}

	public void redo(Board board) {
		if (this.redoStack.empty()) {
			return;
		}
		pushToUndo(board);
		board = this.redoStack.pop();
	}
}

import java.util.Stack;

public class BoardMaster {
	Stack<Board> undoStack;
	Stack<Board> redoStack;
	Stack<int[][]> test;

	public BoardMaster() {
		undoStack = new Stack<Board>();
		redoStack = new Stack<Board>();
	}
	
	public void pushToUndo(Board board) {
		this.undoStack.push(new Board(board));
	}

	public void pushToRedo(Board board) {
		this.redoStack.push(new Board(board));
	}
	
	public Board undo(Board board) {
		if (this.undoStack.empty()) {
			return board;
		}
		pushToRedo(board);
		return this.undoStack.pop();
	}

	public Board redo(Board board) {
		if (this.redoStack.empty()) {
			return board;
		}
		pushToUndo(board);
		return this.redoStack.pop();
	}
}

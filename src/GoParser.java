
public class GoParser {
	public static GoCommand parse(String input) throws Exception {
		GoCommand command = null;
		String[] inputArr = BoardLibrary.removeDoubleSpacing(input.trim()).split(" ");
		
		switch (inputArr[0].toLowerCase()) {
			case "move":
				if (inputArr.length != 3) {
					if (inputArr.length > 3) System.out.println("Too many arguments");
					else System.out.println("Too little arguments");
					break;
				}
				int row = Integer.parseInt(inputArr[1])-1;
				int col = Integer.parseInt(inputArr[2])-1;
				
				command = new MoveCommand(row, col);
				break;
			case "exit":
				command = new ExitCommand();
				break;
			case "undo":
				command = new UndoCommand();
				break;
			case "redo":
				command = new RedoCommand();
				break;
			default:
				throw new Exception("No such command ("+inputArr[0]+")");
		}

		return command;
	}
}

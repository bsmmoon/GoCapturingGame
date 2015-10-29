/*
	To Do List:
		- command system
		- undo!!
*/


import java.util.*;

class Go {
	boolean debugSwitch = false;
	boolean capturingRace = true;

	Scanner sc;
	BoardMaster boardMaster;

	public boolean debug(int input) {
		if (debugSwitch) {
			switch (input) {
				case 1: return true;
				case 2: return true;
			}
		}
		return false;
	}

	public Go() {
		sc = new Scanner(System.in);
		boardMaster = new BoardMaster();
	}
	
	public GoCommand readCommand() {
		
		
		System.out.print("Command: ");
		String input = sc.nextLine();
		String[] inputArr = BoardLibrary.removeDoubleSpacing(input.trim()).split(" ");
		
		String command = inputArr[0];
		switch (command.toLowerCase()) {
			case "move":
				if (inputArr.length != 3) {
					if (inputArr.length > 3) System.out.println("Too many arguments");
					else System.out.println("Too little arguments");
					break;
				}
				int row = Integer.parseInt(inputArr[1])-1;
				int col = Integer.parseInt(inputArr[2])-1;
				
				
				break;
			case "exit":
				break;
			case "undo":
				break;
			case "redo":
				break;
			default:
				System.out.println("No such command ("+command+")");
		}

	}

	public String readCommandLine() {
		System.out.print("Command: ");
		return sc.nextLine();
	}

	public String[] readCommandArr(String commandLine) {
		return BoardLibrary.removeDoubleSpacing(commandLine.trim()).split(" ");
	}

	public void executeCommand(String commandLine) throws Exception {
		String[] commandArr = readCommandArr(commandLine);
		String command = commandArr[0];

		switch (command.toLowerCase()) {
			case "move":
				if (commandArr.length != 3) {
					if (commandArr.length > 3) System.out.println("Too many arguments");
					else System.out.println("Too little arguments");
					break;
				}
				int row = Integer.parseInt(commandArr[1])-1;
				int col = Integer.parseInt(commandArr[2])-1;
				
				boardMaster.makeMove(row, col);
				boardMaster.printStatus();
				break;
			case "exit":
				break;
			case "undo":
				boardMaster.undo();
				break;
			case "redo":
				boardMaster.redo();
				break;
			default:
				System.out.println("No such command ("+command+")");
		}
	}

	public void run() {
		while (!boardMaster.isGameOver()) {
			try {
				boardMaster.printBoard();
				boardMaster.printTurn();
								
				String command = readCommandLine();
				executeCommand(command);

				if (capturingRace) {
					if (boardMaster.countCaptured[1] > 0) {
						boardMaster.printBoard();
						System.out.println("Player1 Wins!");
						break;
					}
					else if (boardMaster.countCaptured[2] > 0) {
						boardMaster.printBoard();
						System.out.println("Player2 Wins!");
						break;
					}
				}
			} catch (Exception e) {
				System.out.println("Illegal Move");
				System.out.println();
			}
		}
	}

}
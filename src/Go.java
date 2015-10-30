import java.util.*;

class Go {
	private boolean debugSwitch = false;
	
	private Scanner sc;
	private GoMaster boardMaster;

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
		boardMaster = new GoMaster();
	}
	
	public void run() {
		while (true) {
			try {
				GoCommand command = readCommand();
				this.boardMaster.execCommand(command);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

	public GoCommand readCommand() throws Exception {
		System.out.print("Command: ");
		String input = sc.nextLine();
		GoCommand command = GoParser.parse(input);
		return command;
	}
}
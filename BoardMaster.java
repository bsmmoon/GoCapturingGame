import java.util.*;

class BoardMaster {
	int[] boardSize;
	Location[][] board;
	int[] playerPosition;

	ArrayList < ArrayList < Location > > locaList;
	ArrayList < ArrayList < Integer > > cityAdjMatrix;
	int typeNumber = 4;
	/*
	0. plain
	1. city
	2. river
	3. road
	*/

	Random rnd = new Random();

	public BoardMaster(int[] boardSize) {
		this.boardSize = boardSize;
	}

	public Location[][] getBoard() {
		return this.board;
	}

	public void initMap() {
		/*
		C: city
		R: road
			- road width = 1
		else:
			M: mountain
			R: river
			F: forest
			...
		*/
		board = new Location[boardSize[0]][boardSize[1]];

		this.locaList = new ArrayList < ArrayList < Location > > ();
		for (int i = 0; i < typeNumber; i++) {
			this.locaList.add(new ArrayList < Location > ());
		}

		for (int row = 0; row < boardSize[0]; row++) {
			for (int col = 0; col < boardSize[1]; col++) {
				addLocation(0, row, col);
			}
		}

		formRiver();
		formCity();
		formRoad();
		formCityAdjMatrix();

		playerPosition = new int[] {rnd.nextInt(boardSize[0]),rnd.nextInt(boardSize[1])};
	}

	public ArrayList < int[] > getNextMove() {
		return findAdj4(playerPosition[0],playerPosition[1]);
	}

	public ArrayList < ArrayList < Integer > > formCityAdjMatrix() {
		for (int cityIndex = 0; cityIndex < locaList.get(1).size(); cityIndex++) {
			Location city = locaList.get(1).get(cityIndex);
			formCityAdjMatrixDFS(cityIndex, city.coordinate, 0);
		}

		return cityAdjMatrix;
	}

	public void formCityAdjMatrixDFS(int origin, int[] coordinate, int dist) {
		ArrayList < int[] > adj4 = findAdj4(coordinate[0], coordinate[1]);
		for (int[] position : adj4) {
			if (getLocation(position).typeCode == 1) {
				
			}
		}
	}

	public void getLocation(int[] position) {
		return board[position[0]][position[1]];
	}

	public Location[][] formRoad() {
		boolean[][] table = new boolean[boardSize[0]][boardSize[1]];

		System.out.println("number of cities: "+locaList.get(1).size());
		for (int cityIndex = 0; cityIndex < locaList.get(1).size(); cityIndex++) {
			PriorityQueue < LocBFS > pq = new PriorityQueue < LocBFS > ();

			Location city = locaList.get(1).get(cityIndex);
			if (table[city.coordinate[0]][city.coordinate[1]]) {
				continue;
			}
			else {
				table[city.coordinate[0]][city.coordinate[1]] = true;

				LocBFS locat = new LocBFS(city);

				pq.add(locat);

				// System.out.println(locat.loc.coordinate[0]+","+locat.loc.coordinate[1]);
				while (pq.size() > 0) {
					// System.out.print(".");
					LocBFS curr = pq.poll();
					// System.out.print("("+curr.loc.coordinate[0]+","+curr.loc.coordinate[1]+") ");
					ArrayList < int[] > adj4 = findAdj4(curr.loc.coordinate[0],curr.loc.coordinate[1]);

					for (int[] position : adj4) {
						if (!table[position[0]][position[1]]) {
							table[position[0]][position[1]] = true; // bull shit. make it recurr and make it properly
							
							ArrayList < int[] > pathUpdate;
							if (board[position[0]][position[1]].typeCode == 1) {
								// System.out.println(position[0]+","+position[1]);
								for (int[] road : curr.path) {
									board[road[0]][road[1]] = new Location(3, road);
									System.out.print("("+road[0]+","+road[1]+") ");
								}
								drawMap();
								pathUpdate = new ArrayList < int[] > ();
							}
							else if (board[position[0]][position[1]].typeCode == 0) {
								pathUpdate = new ArrayList < int[] > (curr.path);
								pathUpdate.add(position);
							}
							else {
								continue;
							}
							pq.add(new LocBFS(board[position[0]][position[1]], pathUpdate, curr.prio+10));
						}
						else if (board[position[0]][position[1]].typeCode == 3) {
							boolean flag = false;
							for (int[] road : curr.path) {
								if (road[0] == position[0] && road[1] == position[1]) {
									flag = true;
									break;
								}
							}
							if (!flag) {
								ArrayList < int[] > pathUpdate = new ArrayList < int[] > (curr.path);
								pathUpdate.add(position);
								pq.add(new LocBFS(board[position[0]][position[1]], pathUpdate, curr.prio+1));
							}
						}
					}
				}
			}
		}
		


		return board;
	}

	public Location[][] formCity() {
		int cityNum = rnd.nextInt((boardSize[0]+boardSize[1])/2)+3;

		int r1 = boardSize[0]/2;
		int r2 = r1 + boardSize[0]%2;
		int c1 = boardSize[1]/2;
		int c2 = c1 + boardSize[1]%2;

		int city = 0;
		for (int limit = 0; limit < cityNum*3; limit++) {
			int rpos = rnd.nextInt(r1)+rnd.nextInt(r2);
			int cpos = rnd.nextInt(c1)+rnd.nextInt(c2);

			if (board[rpos][cpos].typeCode != 0) continue;

			boolean flag = false;
			ArrayList < int[] > adj8 = findAdj8(rpos, cpos);
			for (int[] pos : adj8) {
				if (board[pos[0]][pos[1]].typeCode == 1) {
					flag = true;
					break;
				}
			}
			if (!flag) {
				addLocation(1, rpos, cpos);
				if ((city++) == cityNum) break;
			}
		}

		return board;
	}

	public void addLocation(int typeCode, int rpos, int cpos) {
		board[rpos][cpos] = new Location(typeCode, new int[] {rpos,cpos});
		locaList.get(typeCode).add(board[rpos][cpos]);
	}

	public Location[][] formRiver() {
		int rpos = 0;
		int cpos = 0;
		if (rnd.nextInt(2) == 0) {
			int r1 = boardSize[0]/3;
			int r2 = r1;
			int r3 = r1 + boardSize[0]%3;
			rpos = rnd.nextInt(r1-2)+rnd.nextInt(r2-2)+rnd.nextInt(r3-2)+3;
			if (rnd.nextInt(2) == 0) {
				addLocation(2, rpos, cpos);
				cpos++;
				addLocation(2, rpos, cpos);
			}
			else {
				cpos = boardSize[1]-1;
				addLocation(2, rpos, cpos);
				cpos--;
				addLocation(2, rpos, cpos);
			}
		}
		else {
			int c1 = boardSize[1]/3;
			int c2 = c1;
			int c3 = c1 + boardSize[1]%3;
			cpos = rnd.nextInt(c1-2)+rnd.nextInt(c2-2)+rnd.nextInt(c3-2)+3;
			if (rnd.nextInt(2) == 0) {
				addLocation(2, rpos, cpos);
				rpos++;
				addLocation(2, rpos, cpos);
			}
			else {
				rpos = boardSize[0]-1;
				addLocation(2, rpos, cpos);
				rpos--;
				addLocation(2, rpos, cpos);
			}
		}

		for (int i = 0; i < boardSize[0]*boardSize[1]; i++) {
			ArrayList < int[] > adj4 = findAdj4(rpos, cpos);
			if (adj4.size() < 4) break;

			int rposo = rpos;
			int cposo = cpos;
			if (rnd.nextInt(2) == 0) {
				if (rnd.nextInt(boardSize[0]) > rpos) rpos++;
				else rpos--;
			}
			else {
				if (rnd.nextInt(boardSize[1]) > cpos) cpos++;
				else cpos--;
			}

			if (board[rpos][cpos].typeCode == 2) {
				rpos = rposo;
				cpos = cposo;
				continue;
			}

			else {
				adj4 = findAdj4(rpos, cpos);
				boolean flag = false;
				for (int[] pos : adj4) {
					if (!(pos[0]==rposo && pos[1]==cposo)) {
						if (board[pos[0]][pos[1]].typeCode == 2) {
							flag = true;
							break;
						}
					}
				}
				if (!flag) {
					addLocation(2, rpos, cpos);
				}
				else {
					rpos = rposo;
					cpos = cposo;
				}
			}
		}

		return board;
	}

	public ArrayList < int[] > findAdj4(int row, int col) {
		ArrayList < int[] > out = new ArrayList < int[] > ();
		if (col+1 < boardSize[1])
			out.add(new int[]{row, col+1});
		if (col-1 >= 0)
			out.add(new int[]{row, col-1});
		if (row+1 < boardSize[0])
			out.add(new int[]{row+1, col});
		if (row-1 >= 0)
			out.add(new int[]{row-1, col});
		return out;
	}

	public ArrayList < int[] > findAdj8(int row, int col) {
		ArrayList < int[] > out = new ArrayList < int[] > ();
		if (col+1 < boardSize[1])
			out.add(new int[]{row, col+1});
		if (col-1 >= 0)
			out.add(new int[]{row, col-1});
		if (row+1 < boardSize[0])
			out.add(new int[]{row+1, col});
		if (row-1 >= 0)
			out.add(new int[]{row-1, col});
		if (row+1 < boardSize[0] &&
			col+1 < boardSize[1])
			out.add(new int[]{row+1, col+1});
		if (row+1 < boardSize[0] &&
			col-1 >= 0)
			out.add(new int[]{row+1, col-1});
		if (row-1 >= 0 &&
			col+1 < boardSize[1])
			out.add(new int[]{row-1, col+1});
		if (row-1 >= 0 &&
			col-1 >= 0)
			out.add(new int[]{row-1, col-1});
		return out;
	}

	public void getPlayerPosition() {
		String out = "Player is currently ";

		switch (board[playerPosition[0]][playerPosition[1]].typeCode) {
			case 3:
				out += "on an empty plane"; break;
			case 2:
				out += "on river"; break;
			case 1:
				out += "at a city"; break;
			default:
				out += "at somewhere unknown";
		}

		out += " at ("+playerPosition[0]+","+playerPosition[1]+")";

		System.out.println(out);
	}

	public void drawMap() {
		System.out.println();
		for (int col = -1; col < boardSize[1]; col++) {
			if (col == -1) {
				System.out.print("  ");
			}
			else {
				if (col<10) System.out.print(col+" ");
				else System.out.print((col%10)+" ");
			}
		}
		System.out.println();

		for (int row = 0; row < boardSize[0]; row++) {
			for (int col = -1; col < boardSize[1]; col++) {
				if (col == -1) {
					if (row<10) System.out.print(row+" ");
					else System.out.print((row%10)+" ");
				}
				else {
					System.out.print(board[row][col].type+" ");
				}
			}
			System.out.println();
		}
		System.out.println();
	}
}

class LocBFS implements Comparable < LocBFS > {
	Location loc;
	ArrayList < int[] > path;

	int prio;

	public LocBFS(Location loc) {
		this.loc = loc;
		this.path = new ArrayList < int[] > ();
		this.prio = 0;
	}

	public LocBFS(Location loc, ArrayList < int[] > path, int prio) {
		this.loc = loc;
		this.path = path;
		this.prio = prio;
	}

	public int compareTo(LocBFS o) {
		return this.prio - o.prio;
	}
}
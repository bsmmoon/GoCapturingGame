

class Location {
	String type;
	int typeCode;
	int[] coordinate = new int[2];

	int id;
	String name;

	public Location(int[] coordinate) {
		setType(0);
		this.coordinate = coordinate;
	}

	public Location(int typeCode, int[] coordinate) {
		setType(typeCode);
		this.coordinate = coordinate;
	}

	/*
	0. plain
	1. city
	2. river
	3. road
	*/
	public void setType(int typeCode) {
		this.typeCode = typeCode;
		switch (typeCode) {
			case 0:
				this.type = " "; break;
			case 1:
				this.type = "C"; break;
			case 2:
				this.type = "r"; break;
			case 3:
				this.type = "."; break;
			default:
				this.type = "err("+typeCode+")"; break;
		}
	}
}
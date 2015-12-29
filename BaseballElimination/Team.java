class Team {
	private String name;
	private int wins, losses, remaining;
	private int[] schedule;

	public Team(String n, int w, int l, int r, int[] s) {
		name = n;
		wins = w;
		losses = l;
		remaining = r;
		schedule = s;
	}

	public String name() {
		return name;
	}

	public int wins() {
		return wins;
	}

	public int losses() {
		return losses;
	}

	public int remaining() {
		return remaining;
	}

	public int[] schedule() {
		return schedule;
	}
}
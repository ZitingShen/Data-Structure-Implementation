public class Task {
	private int timeStamp; // When a print request arrives (in ticks since 0)
	private int pages; // # pages of the print job [1..20]

	public Task(int t) {
		timeStamp = t;
		pages = (int) (1 + 20 * Math.random());
	}

	public int getTimeStamp() {
		return timeStamp;
	}

	public int getPages() {
		return pages;
	}

	public int waitTime(int currentTime) {
		return currentTime - timeStamp;
	}

	public String toString() {
		return "Task@" + timeStamp + ", " + pages + " pages.";
	}
}
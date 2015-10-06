public class Printer {

	private int pageRate; // pages per minute (ppm)
	private Task currentTask; // current task on printer
	private int timeRemaining; // time remaining on current task

	public Printer(int ppm) {
		pageRate = ppm;
		currentTask = null;
		timeRemaining = 0;
	}

	public void tick() {
		if (currentTask != null) {
			timeRemaining--;
			if (timeRemaining == 0) {
				currentTask = null;
			}
		}
	}

	public boolean busy() {
		return currentTask != null;
	}

	public void startNext(Task newTask) {
		currentTask = newTask;
		timeRemaining = newTask.getPages() * 60 / pageRate;
	}

}
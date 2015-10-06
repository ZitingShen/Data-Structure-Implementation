import java.util.LinkedList;
import java.util.Queue;
import java.util.ArrayList;

public class PQSim {
	
	public static void main(String[] args) {
		double monochromeTime = multipleSimulate(3600, 10, 1000);
		double colorTime = multipleSimulate(3600, 5, 1000);
		int reasonableTime = 60;
		System.out.println("The reasonable wait time is " + 
		reasonableTime + " seconds.");
		System.out.println("The average wait time for the monochrome "
				+ "printer is " + monochromeTime +" seconds.");
		reasonableWait(monochromeTime, reasonableTime);
		System.out.println("The average wait time for the color printer is "
		+ colorTime + " seconds.");
		reasonableWait(colorTime, reasonableTime);
	}
	
	// Simulate for multiple times to get a stable average wait time.
	public static double multipleSimulate(int seconds, int ppm, int simulationTimes) {
		double simulationWait = 0;
		for(int i=0; i < simulationTimes; i++) {
			System.out.println(simulate(seconds, ppm));
			simulationWait += simulate(seconds, ppm);
		}
		return simulationWait/simulationTimes;
	}
	
//	public static void main(String[] args) {
//		System.out.println(simulate(3600, 10));
//	}

	// Simulate for time (Seconds) for a printer speed (ppm)
	public static double simulate(int seconds, int ppm) {
		int time = 0;
		int totalWaitTime = 0;
		double averageWaitTime = 0;
		Printer printer = new Printer(ppm);
		Queue<Task> printerQueue = new LinkedList<Task>();
		ArrayList<Integer> waitTime = new ArrayList<Integer>();
		
		while (time < seconds) {
			if (newPrintTask())
				printerQueue.add(new Task(time));
			if (!(printer.busy()) && !(printerQueue.isEmpty())) {
				Task nextTask = printerQueue.remove();
				//System.out.println(nextTask);
				waitTime.add(nextTask.waitTime(time));
				printer.startNext(nextTask);
			}
			printer.tick();
			time += 1;
		}
		
//		for(Integer i: waitTime){
//			System.out.println(i);
//			totalWaitTime += i;
//		}
		if (waitTime.size() != 0) {
			averageWaitTime = totalWaitTime * 1.0 / waitTime.size();
		}
		return averageWaitTime;
		//System.out.println("Average wait time: " + averageWaitTime);
	}

	// Add a new print task.
	public static boolean newPrintTask() {
		return (180 == ((int) (1 + 180 * Math.random())));
	}
	
	// Decide if the wait time is reasonable.
	public static void reasonableWait(double realTime, int reasonableTime) {
		if (realTime <= reasonableTime) {
			System.out.println("The wait time is reasonable.");
		} else {
			System.out.println("The wait time is too long!");
		}
	}
}

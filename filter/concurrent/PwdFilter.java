package filter.concurrent;

public class PwdFilter extends ConcurrentFilter {
	public PwdFilter() {
		super();
	}

	public void process() {
		isFinished = false;
		try {
			output.put(processLine(""));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		killFilter();
	}

	public String processLine(String line) {
		return ConcurrentREPL.currentWorkingDirectory;
	}

	public String toString() {
		return "Pwd";
	}
}

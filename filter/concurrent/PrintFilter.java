package filter.concurrent;

public class PrintFilter extends ConcurrentFilter {
	public PrintFilter() {
		super();
	}

	public void process() {
		isFinished = false;
		String line = null;
		while (line != ConcurrentFilter.poison) {
			if (line != null && !line.equals(ConcurrentFilter.poison)) {
				processLine(line);
			}
			try {
				line = input.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		killFilter();
	}

	public String processLine(String line) {
		System.out.println(line);
		return null;
	}

	public String toString() {
		return "Print";
	}
}

package filter.concurrent;

public class WcFilter extends ConcurrentFilter {
	private int linecount;
	private int wordcount;
	private int charcount;

	public WcFilter() {
		super();
	}

	public void process() {
		isFinished = false;
		String line = null;
		while (line != ConcurrentFilter.poison) {
			if (line != null) {
				processLine(line);
			}
			try {
				line = input.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (input.isEmpty() && prev.isDone()) {
			output.add(processLine(null));
		}
		killFilter();
	}

	public String processLine(String line) {
		//prints current result if ever passed a null
		if(line == null) {
			return linecount + " " + wordcount + " " + charcount;
		}

		if(prev.isDone()) {
			String[] wct = line.split(" ");
			wordcount += wct.length;
			String[] cct = line.split("|");
			charcount += cct.length;
			return ++linecount + " " + wordcount + " " + charcount;
		} else {
			linecount++;
			String[] wct = line.split(" ");
			wordcount += wct.length;
			String[] cct = line.split("|");
			charcount += cct.length;
			return null;
		}
	}

	public String toString() {
		return "Wc";
	}
}

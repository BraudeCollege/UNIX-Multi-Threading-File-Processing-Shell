package filter.concurrent;
import java.util.concurrent.LinkedBlockingQueue;
import filter.Filter;


public abstract class ConcurrentFilter extends Filter implements Runnable {
	protected LinkedBlockingQueue<String> input = new LinkedBlockingQueue<String>();
	protected LinkedBlockingQueue<String> output = new LinkedBlockingQueue<String>();
	protected boolean isFinished = false;  // indicate if this filter is completed
	protected boolean isLast = false; // indicate if this filter is the last in command
	// poison string indicates last string in output, to kill its next thread
	static final String poison = "!This is the poison string in LinkedBlockingQueue for next thread!";

	@Override
	public void setPrevFilter(Filter prevFilter) {
		prevFilter.setNextFilter(this);
	}

	@Override
	public void setNextFilter(Filter nextFilter) {
		if (nextFilter instanceof ConcurrentFilter){
			ConcurrentFilter sequentialNext = (ConcurrentFilter) nextFilter;
			this.next = sequentialNext;
			sequentialNext.prev = this;
			if (this.output == null){
				this.output = new LinkedBlockingQueue<String>();
			}
			sequentialNext.input = this.output;
		} else {
			throw new RuntimeException("Should not attempt to link dissimilar filter types.");
		}
	}

	public Filter getNext() {
		return next;
	}

	public void run() {
		process();
	}

	public void process(){
		isFinished = false;
		String line = null;
		while (line != poison) {
			if (line != null) {
				String processedLine = processLine(line);
				if (processedLine != null) {
					output.add(processedLine);
				}
			}
			try {
				line = input.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		killFilter();
	}

	@Override
	public boolean isDone() {
		return isFinished;
	}

	// mark the filter as finished, and emit a poison string
	public void killFilter() {
		output.add(poison);
		isFinished = true;
	}

	protected abstract String processLine(String line);
}

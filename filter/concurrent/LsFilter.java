package filter.concurrent;
import java.io.File;

public class LsFilter extends ConcurrentFilter{
	int counter;
	File folder;
	File[] flist;

	public LsFilter() {
		super();
		counter = 0;
		folder = new File(ConcurrentREPL.currentWorkingDirectory);
		flist = folder.listFiles();
	}

	@Override
	public void process() {
		isFinished = false;
		while(counter < flist.length) {
			output.add(processLine(""));
		}
		killFilter();
	}

	@Override
	public String processLine(String line) {
		return flist[counter++].getName();
	}

	public String toString() {
		return "Ls";
	}
}

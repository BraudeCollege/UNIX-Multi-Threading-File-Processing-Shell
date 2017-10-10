package filter.concurrent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import filter.Filter;
import filter.Message;

public class RedirectFilter extends ConcurrentFilter {
	private FileWriter fw;

	public RedirectFilter(String line) throws Exception {
		super();
		String[] param = line.split(">");
		if(param.length > 1) {
			if(param[1].trim().equals("")) {
				System.out.printf(Message.REQUIRES_PARAMETER.toString(), line.trim());
				throw new Exception();
			}
			try {
				String fileName = param[1].trim().split("\\&")[0].trim();
				fw = new FileWriter(new File(ConcurrentREPL.currentWorkingDirectory + Filter.FILE_SEPARATOR + fileName));
			} catch (IOException e) {
				System.out.printf(Message.FILE_NOT_FOUND.toString(), line);	//shouldn't really happen but just in case
				throw new Exception();
			}
		} else {
			System.out.printf(Message.REQUIRES_INPUT.toString(), line);
			throw new Exception();
		}
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
		try {
			fw.flush();
			fw.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		killFilter();
	}

	public String processLine(String line) {
		try {
			fw.append(line + "\n");
		} catch (IOException e) {
			System.out.printf(Message.FILE_NOT_FOUND.toString(), line);
		}
		return null;
	}

	public String toString() {
		return "Redirect";
	}
}

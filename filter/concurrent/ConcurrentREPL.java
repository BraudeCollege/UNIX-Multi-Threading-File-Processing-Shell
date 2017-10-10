package filter.concurrent;

import filter.Message;
import java.util.*;

public class ConcurrentREPL {
	static String currentWorkingDirectory;
	// a list of active commands mapped with threads
	static List<ThreadCommandBinder> activeCommands;

	public static void main(String[] args){
		currentWorkingDirectory = System.getProperty("user.dir");
		Scanner s = new Scanner(System.in);
		System.out.print(Message.WELCOME);
		String command;
		activeCommands = new LinkedList<ThreadCommandBinder>();

		while (true) {
			//obtaining the command from the user
			System.out.print(Message.NEWCOMMAND);
			command = s.nextLine();
			if(command.equals("exit")) {
				break;
			} else if (command.equals("repl_jobs")) {
				// read commands from active list
				// print alive thread's command, and remove dead thread
				int index = 0;
				for (int i = activeCommands.size() - 1; i >= 0; i--) {
					ThreadCommandBinder it = activeCommands.get(index);
					if (it.thread.getState() == Thread.State.TERMINATED) {
						activeCommands.remove(index);
						continue;
					}
					index += 1;
					System.out.println("\t" + index + ". " + it.command.trim());
				}
			} else if (!command.trim().equals("")) {
				//building the filters list from the command
				ConcurrentFilter filterlist = ConcurrentCommandBuilder.createFiltersFromCommand(command);
				Thread t = null;
				while (filterlist != null) {
					t = new Thread(filterlist);
					t.start();
					filterlist = (ConcurrentFilter) filterlist.getNext();
				}
				if (t != null) {
					// map the isLast thread with command string
					activeCommands.add(new ThreadCommandBinder(t, command));
					// join the last child thread to ensure correct '>' in console
					if (!command.substring(command.length() - 1).trim().equals("&")) {
						try {
							t.join();
						} catch (InterruptedException e) {
						}
					}
				}
			}
		}
		s.close();
		System.out.print(Message.GOODBYE);
	}
}

//a mapping between isLast Thread with command string
class ThreadCommandBinder {
	Thread thread;
	String command;
	ThreadCommandBinder(Thread t, String c) {
		this.thread = t;
		this.command = c;
	}
}

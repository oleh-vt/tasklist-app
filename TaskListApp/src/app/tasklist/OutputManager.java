package app.tasklist;

import java.util.Date;
import java.util.List;

import app.tasklist.model.Task;

public class OutputManager {

	public static void printMainMenu(){
		printMessage("=========================================================================\n");
		printMessage("1. Add a task");
		printMessage("2. Show tasks");
		printMessage("3. Exit");
	}
	
	public static void printSubmenu(){
		printMessage("1. Mark task as completed");
		printMessage("2. Show completed tasks");
		printMessage("3. Return to main menu");
	}
	
	public static void printMessage(String message){
		System.out.println(message);
	}
	
	public static void printError(String message){
		System.err.println(message);
	}
	
	public static void printTasks(List<Task> taskList){
		printTasks(taskList, false);
	}
	
	public static void printTasks(List<Task> taskList, boolean completedOnly){
		Date now = new Date();
		String overdue = "";
		for(Task t : taskList){
			overdue = (!completedOnly && now.after(t.getDeadline())) ? "overdue" : "";
			System.out.format("%1$5d  %2$-30s  %3$tY-%3$tm-%3$td %3$tH:%3$tM  %4$-10s %5$-10s", 
					t.getId(), t.getName(), t.getDeadline(), t.getPriority(), overdue);
			System.out.println();
		}
		System.out.println();
	}

}

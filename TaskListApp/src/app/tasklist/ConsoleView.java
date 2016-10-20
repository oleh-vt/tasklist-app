package app.tasklist;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import app.tasklist.model.Priority;
import app.tasklist.model.Task;

public class ConsoleView {
	
	private BufferedReader br;
	private String datePattern = "yyyy-MM-dd HH:mm";
	private Priority[] priorities;
	
	
	public ConsoleView(BufferedReader br, String datePattern, Priority[] priorities) {
		super();
		this.br = br;
		this.datePattern = datePattern;
		this.priorities = priorities;
	}

	public Task readTask(){
		printMessage("Task Name (30 symbols): ");
		String nameInput = getInputString();
		
		printMessage("Deadline (" + datePattern.toUpperCase() + "): ");
		String deadlineInput = getInputString();
		
		printMessage("Priority:");
		
		for(int i = 0; i < priorities.length; i++){
			printMessage(i+1 + " - " + priorities[i].name());
		}
		int priority = getUserChoice(1, priorities.length);
		
		if(nameInput == null || nameInput.trim().isEmpty()){
			printMessage("Name cannot be empty");
			return null;
		}
		
		SimpleDateFormat df = new SimpleDateFormat(datePattern);
		Date deadline = null;
		try {
			deadline = df.parse(deadlineInput);
		} catch (ParseException e) {
			printError(e.getMessage());
			return null;
		}
		
		return new Task(nameInput, deadline, priorities[priority-1]);
	}
	
	public void printMainMenu(){
		printMessage("=========================================================================\n");
		printMessage("1. Add a task");
		printMessage("2. Show tasks");
		printMessage("3. Exit");
	}
	
	public void printShowSubmenu(){
		printMessage("1. Mark task as completed");
		printMessage("2. Show completed tasks");
		printMessage("3. Return to main menu");
	}
	
	public void printMessage(String message){
		System.out.println(message);
	}
	
	public void printError(String message){
		System.err.println(message);
	}
	
	void printTasks(List<Task> taskList){
		printTasks(taskList, false);
	}
	
	void printTasks(List<Task> taskList, boolean completedOnly){
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
	
	public int getUserChoice(int firstOption, int lastOption){
		int choice = -1; boolean success = false;
		while(!success){
			choice = getInputInt();
			if(choice < firstOption || choice > lastOption){
				printError("Illegal input: Please select in a range [" + firstOption + ".." + lastOption + "]");
				continue;
			}
			success = true;
		}
		return choice;
	}
	
	public String getInputString(){
		String input = null;
		try{
			input = br.readLine();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		return input;
	}
	
	public int getInputInt(){
		int input = -1;
		boolean success = false;
		try{
			while(!success){
				String tmp = br.readLine();
				try{
					input = Integer.parseInt(tmp);
					success = true;
				}
				catch(NumberFormatException e){
					printError("Error: Integer numbers only!");
				}
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
		return input;
	}
	
}

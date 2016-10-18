package app.tasklist;

import java.util.Date;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import app.tasklist.model.Priority;
import app.tasklist.model.Task;

public class ConsoleView {
	
	private String datePattern = "yyyy-MM-dd HH:mm";
	private Priority[] priorities;
	
	
	public ConsoleView(String datePattern, Priority[] priorities) {
		super();
		this.datePattern = datePattern;
		this.priorities = priorities;
	}

	Map<String, String> readTask(){
		printMessage("Task Name (30 symbols): ");
		String name = getInputString();
		
		printMessage("Deadline (" + datePattern.toUpperCase() + "): ");
		String deadline = getInputString();
		
		printMessage("Priority:");
		
		//Priority[] priorities = Priority.values();
		for(int i = 0; i < priorities.length; i++){
			printMessage(i+1 + " - " + priorities[i].name());
		}
		int priority = getInputInt();
		
		Map<String, String> params = new HashMap<>();
		params.put("name", name);
		params.put("deadline", deadline);
		params.put("priority", Integer.toString(priority));
		
		return params;
		
//		SimpleDateFormat df = new SimpleDateFormat(datePattern);
//		Task t = new Task();
//		t.setName(name);
//		
//		t.setPriority(priorities[Integer.parseInt(priority)-1]);
//		try {
//			t.setDeadline(df.parse(deadline));
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		System.out.println(t.toString());
	}
	
	public void printMainMenu(){
		printMessage("1. Add a task");
		printMessage("2. Show tasks");
		printMessage("3. Exit");
	}
	
	public void printShowSubmenu(){
		printMessage("1. Mark task as completed");
		printMessage("2. Show completed tasks");
		printMessage("3. Return to main");
	}
	
	public void printMessage(String message){
		System.out.println(message);
	}
	
	public void printError(String message){
		System.err.println(message);
	}
	
	void printTasks(List<Task> taskList){
		Date now = new Date();
		String overdue = "";
		for(Task t : taskList){
			overdue = (!t.isCompleted() && now.after(t.getDeadline())) ? "overdue" : "";
			System.out.format("%1$5d  %2$-30s  %3$tY-%3$tm-%3$td %3$tH:%3$tM  %4$-10s %5$-10s", 
					t.getId(), t.getName(), t.getDeadline(), t.getPriority(), overdue);
			System.out.println();
		}
	}
	
	public int getUserChoice(int firstOption, int lastOption){
		Scanner scanner = new Scanner(System.in);
		int choice = -1; boolean success = false;
		while(!success){
			try{
				choice = scanner.nextInt();
				if(choice < firstOption || choice > lastOption)
					throw new Exception("Please select in a range [" + firstOption + lastOption + "]");
				success = true;
			}
			catch(Exception e){
				printError("Error: " + e.getMessage());
			}
			finally{
				scanner.nextLine();
			}

		}
		scanner.close();
		return choice;
	}
	
	public String getInputString(){
		Scanner scanner = new Scanner(System.in);
		String input = scanner.nextLine();
		scanner.nextLine();
		scanner.close();
		return input;
	}
	
	public int getInputInt(){
		Scanner scanner = new Scanner(System.in);
		boolean success = false;
		int input = 0;
		while (!success) {
			try {
				input = scanner.nextInt();
				success = true;
			} catch (InputMismatchException e) {
				printError("Error: Integer numbers only!");
			}
			finally{
				scanner.nextLine();
			}
		}
		scanner.close();
		return input;
	}
	
}

package app.tasklist;

import static app.tasklist.OutputManager.printError;
import static app.tasklist.OutputManager.printMainMenu;
import static app.tasklist.OutputManager.printMessage;
import static app.tasklist.OutputManager.printSubmenu;
import static app.tasklist.OutputManager.printTasks;

import java.io.BufferedReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import app.tasklist.commands.AddCommand;
import app.tasklist.commands.GetCommand;
import app.tasklist.commands.Result;
import app.tasklist.commands.UpdateCommand;
import app.tasklist.model.Priority;
import app.tasklist.model.Task;

public class ConsoleView {

	private Controller ctrl;
	private String datePattern = "yyyy-MM-dd HH:mm";
	private Priority[] priorities;
	private InputManager inputManager;
	
	public ConsoleView(BufferedReader br, String datePattern, Priority[] priorities) {
		super();
		this.datePattern = datePattern;
		this.priorities = priorities;
		String[] strPriorities = new String[priorities.length];
		for(int i = 0; i < priorities.length; i++){
			strPriorities[i] = priorities[i].name();
		}
		inputManager = new InputManager(br, strPriorities);
		
	}

	public void setController(Controller ctrl){
		this.ctrl = ctrl;
	}
	
	public void getUserCommand(){
		while(true){
			printMainMenu();
			int choice = inputManager.getUserChoice(1, 3);
			switch(choice){
			case 1:
				Task t = validateInput(inputManager.readTask(datePattern));
				if(t != null){
					Result<Boolean> r = (Result<Boolean>)ctrl.handleCommand(new AddCommand(t));
					boolean result = r.getData();
					if(result)
						printMessage("The task successfully added");
					else
						printError("Error occured. Try again later");
				}
				break;
			case 2:
				tasksSubmenu();
				break;
			case 3:
				printMessage("Bye-bye!");
				return;
			}
		}
	}
	
	private void tasksSubmenu(){
		while(true){
			boolean completedOnly = false;
			Result<List<Task>> r = (Result<List<Task>>)ctrl.handleCommand(new GetCommand(completedOnly));
			List<Task> taskList = r.getData();
			if(taskList == null){
				printError("Error occured. Try again later");
				return;
			}
			if(taskList.isEmpty()){
				printMessage("There are no any tasks in the list");
			}
			printTasks(taskList);
			printSubmenu();
			int ch = inputManager.getUserChoice(1, 3);
			
			switch(ch){
			case 1:
				printMessage("Enter task id: ");
				int id = inputManager.getInputInt();
				Result<Boolean> res = (Result<Boolean>)ctrl.handleCommand(new UpdateCommand(id));
				if(!res.getData())
					printError("Selected task has already been marked as done or provided ID is incorrect. Check entered ID");
				break;
			case 2:
				completedOnly = true;
				r = (Result<List<Task>>)ctrl.handleCommand(new GetCommand(completedOnly));
				taskList = r.getData();
				if(taskList == null){
					printError("Error occured. Try again later");
					return;
				}
				if(!taskList.isEmpty())
					printTasks(taskList, completedOnly);
				else
					printMessage("There are no completed tasks");
				printMessage("Press Enter to continue...");
				inputManager.getInputString();
				break;
			case 3:
				return;
			}
			
		}
	}
	
	private Task validateInput(Map<String, String> input){
		String nameVal = input.get("name");
		String deadlineVal = input.get("deadline");
		String priorityVal = input.get("priority");
		
		if(nameVal == null || nameVal.trim().isEmpty()){
			OutputManager.printMessage("Name cannot be empty");
			return null;
		}
		
		SimpleDateFormat df = new SimpleDateFormat(datePattern);
		Date deadline = null;
		try {
			deadline = df.parse(deadlineVal);
		} catch (ParseException e) {
			OutputManager.printError(e.getMessage());
			return null;
		}
		int tmp = Integer.parseInt(priorityVal);
		Priority priority = priorities[tmp-1];
		
		return new Task(nameVal, deadline, priority);
	}
	
//	private Task readTask(){
//		printMessage("Task Name (30 symbols): ");
//		String nameInput = getInputString();
//		
//		printMessage("Deadline (" + datePattern.toUpperCase() + "): ");
//		String deadlineInput = getInputString();
//		
//		printMessage("Priority:");
//		
//		for(int i = 0; i < priorities.length; i++){
//			printMessage(i+1 + " - " + priorities[i].name());
//		}
//		int priority = getUserChoice(1, priorities.length);
//		
//		if(nameInput == null || nameInput.trim().isEmpty()){
//			printMessage("Name cannot be empty");
//			return null;
//		}
//		
//		SimpleDateFormat df = new SimpleDateFormat(datePattern);
//		Date deadline = null;
//		try {
//			deadline = df.parse(deadlineInput);
//		} catch (ParseException e) {
//			printError(e.getMessage());
//			return null;
//		}
//		
//		return new Task(nameInput, deadline, priorities[priority-1]);
//	}
	
//	private void printMainMenu(){
//		printMessage("=========================================================================\n");
//		printMessage("1. Add a task");
//		printMessage("2. Show tasks");
//		printMessage("3. Exit");
//	}
//	
//	private void printShowSubmenu(){
//		printMessage("1. Mark task as completed");
//		printMessage("2. Show completed tasks");
//		printMessage("3. Return to main menu");
//	}
//	
//	private void printMessage(String message){
//		System.out.println(message);
//	}
//	
//	private void printError(String message){
//		System.err.println(message);
//	}
//	
//	private void printTasks(List<Task> taskList){
//		printTasks(taskList, false);
//	}
//	
//	private void printTasks(List<Task> taskList, boolean completedOnly){
//		Date now = new Date();
//		String overdue = "";
//		for(Task t : taskList){
//			overdue = (!completedOnly && now.after(t.getDeadline())) ? "overdue" : "";
//			System.out.format("%1$5d  %2$-30s  %3$tY-%3$tm-%3$td %3$tH:%3$tM  %4$-10s %5$-10s", 
//					t.getId(), t.getName(), t.getDeadline(), t.getPriority(), overdue);
//			System.out.println();
//		}
//		System.out.println();
//	}
	
//	private int getUserChoice(int firstOption, int lastOption){
//		int choice = -1; boolean success = false;
//		while(!success){
//			choice = getInputInt();
//			if(choice < firstOption || choice > lastOption){
//				printError("Illegal input: Please select in a range [" + firstOption + ".." + lastOption + "]");
//				continue;
//			}
//			success = true;
//		}
//		return choice;
//	}
//	
//	private String getInputString(){
//		String input = null;
//		try{
//			input = br.readLine();
//		}
//		catch(IOException e){
//			e.printStackTrace();
//		}
//		return input;
//	}
//	
//	private int getInputInt(){
//		int input = -1;
//		boolean success = false;
//		try{
//			while(!success){
//				String tmp = br.readLine();
//				try{
//					input = Integer.parseInt(tmp);
//					success = true;
//				}
//				catch(NumberFormatException e){
//					printError("Error: Integer numbers only!");
//				}
//			}
//		}
//		catch(IOException e){
//			e.printStackTrace();
//		}
//		return input;
//	}
	
}

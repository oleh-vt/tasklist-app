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
					boolean result = (Boolean)ctrl.handleCommand(new AddCommand(t));
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
			List<Task> taskList = (List<Task>)ctrl.handleCommand(new GetCommand(completedOnly));
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
				boolean res = (Boolean)ctrl.handleCommand(new UpdateCommand(id));
				if(!res)
					printError("Selected task has already been marked as done or provided ID is incorrect. Check entered ID");
				break;
			case 2:
				completedOnly = true;
				taskList = (List<Task>)ctrl.handleCommand(new GetCommand(completedOnly));
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
	
}

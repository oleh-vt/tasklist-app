package app.tasklist;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import app.tasklist.dao.DataSourceProvider;
import app.tasklist.dao.ITaskRepository;
import app.tasklist.dao.TaskRepositoryDB;
import app.tasklist.model.Priority;
import app.tasklist.model.Task;

public class Main {

	private static final String dateFormat = "yyyy-MM-dd HH:mm";
	
	private Service service;
	private ConsoleView cw;
	
	public static void main(String[] args) {
		ITaskRepository tr = new TaskRepositoryDB(DataSourceProvider.getMySqlDataSource());
		try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in))){
			Service service = new Service(tr);
			ConsoleView consoleView = new ConsoleView(br, dateFormat, Priority.values());
			Main m = new Main(service, consoleView);
			m.start(); 
		}
		catch(Exception e){/**/}
	}
	
	Main(Service service, ConsoleView cw) {
		super();
		this.service = service;
		this.cw = cw;
	}

	void start(){
		while(true){
			cw.printMainMenu();
			int choice = cw.getUserChoice(1, 3);
			switch(choice){
			case 1:
				Task t = cw.readTask();
				if(t != null){
					if(service.addTask(t))
						cw.printMessage("The task successfully added");
					else
						cw.printError("Error occured. Try again later");
				}
				break;
			case 2:
				tasksSubmenu();
				break;
			case 3:
				cw.printMessage("Bye-bye!");
				return;
			}
		}
	}
	
	 void tasksSubmenu(){
		while(true){
			List<Task> taskList = service.getTaskList();
			if(taskList == null){
				cw.printError("Error occured. Try again later");
				return;
			}
			if(taskList.isEmpty()){
				cw.printMessage("There are no any tasks in the list");
			}
			cw.printTasks(taskList);
			cw.printShowSubmenu();
			int ch = cw.getUserChoice(1, 3);
			
			switch(ch){
			case 1:
				cw.printMessage("Enter task id: ");
				int id = cw.getInputInt();
				if(!service.markCompleted(id))
					cw.printError("Selected task has already been marked as done or provided ID is incorrect. Check entered ID");
				break;
			case 2:
				boolean completedOnly = true;
				taskList = service.getTaskList(completedOnly);
				if(taskList == null){
					cw.printError("Error occured. Try again later");
					return;
				}
				if(!taskList.isEmpty())
					cw.printTasks(taskList);
				else
					cw.printMessage("There are no completed tasks");
				cw.printMessage("Press Enter to continue...");
				cw.getInputString();
				break;
			case 3:
				return;
			}
			
		}
	}
}

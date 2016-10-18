package app.tasklist;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import app.tasklist.dao.DataSourceProvider;
import app.tasklist.dao.ITaskRepository;
import app.tasklist.dao.TaskRepositoryJDBC;
import app.tasklist.model.Priority;
import app.tasklist.model.Task;

public class Main {

	private static final String dateFormat = "yyyy-MM-dd HH:mm";
	
	private Service service;
	private ConsoleView cw;
	
	public static void main(String[] args) {
		ITaskRepository tr = new TaskRepositoryJDBC(DataSourceProvider.getMySqlDataSource());
		try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in))){
			Service service = new Service(tr, dateFormat);
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
				Map<String, String> params = cw.readTask();
				try {
					boolean r = service.addTask(params);
					if(r)
						cw.printMessage("The task has been added to the list");
				} catch (Exception e) {
					cw.printError(e.getMessage());
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
			boolean completedOnly = false;
			List<Task> taskList = service.getTaskList(completedOnly);
			cw.printTasks(taskList);
			cw.printShowSubmenu();
			int ch = cw.getUserChoice(1, 3);
			switch(ch){
			case 1:
				cw.printMessage("Enter task id: ");
				int id = cw.getInputInt();
				if(!service.markCompleted(id))
					cw.printError("Selected task was not modified. Check entered ID");
				break;
			case 2:
				completedOnly = true;
				taskList = service.getTaskList(completedOnly);
				cw.printTasks(taskList);
				cw.printMessage("Press Enter to continue...");
				cw.getInputString();
				break;
			case 3:
				return;
			}
			
		}
	}
}

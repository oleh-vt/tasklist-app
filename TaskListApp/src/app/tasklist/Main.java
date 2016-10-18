package app.tasklist;

import java.util.List;
import java.util.Map;

import app.tasklist.dao.DataSourceProvider;
import app.tasklist.dao.ITaskRepository;
import app.tasklist.dao.TaskRepositoryJDBC;
import app.tasklist.model.Priority;
import app.tasklist.model.Task;

public class Main {

	private static final String dateFormat = "yyyy-MM-dd HH:mm";
	
	Service service;
	ConsoleView cw;
	
	public static void main(String[] args) {
		ITaskRepository tr = new TaskRepositoryJDBC(DataSourceProvider.getMySqlDataSource());
		Service service = new Service(tr, dateFormat);
		ConsoleView consoleView = new ConsoleView(dateFormat, Priority.values());
		Main m = new Main(service, consoleView);
		m.start();
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
					service.addTask(params);
				} catch (Exception e) {
					cw.printError(e.getMessage());
				}
				break;
			case 2:
				tasksSubmenu();
				break;
			case 3:
				System.exit(0);
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
				cw.printMessage("Press any key to return...");
				System.console().readLine();
//				try {
//					System.in.read();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
				break;
			case 3:
				return;
			}
			
		}
	}
	 
//	 void showTasks(Scanner s){
//		while(true){
//			boolean findCompleted = false;
//			List<Task> tasks = tr.getTasks(findCompleted);
//			printTasks(tasks);
//			cw.printShowSubmenu();
//			int i = s.nextInt();
//			switch(i){
//			case 1:
//				System.out.print("Enter task id: ");
//				int id = s.nextInt();
//				tr.markAsCompleted(id);
//				break;
//			case 2:
//				findCompleted = true;
//				List<Task> completedTasks = tr.getTasks(findCompleted);
//				printTasks(completedTasks);
//				System.out.print("Press any key to return...");
//				System.console().readLine();
//				break;
//			case 3:
//				return;
//			}
//			
//		}
//	}

}

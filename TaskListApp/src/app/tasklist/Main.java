package app.tasklist;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import app.tasklist.dao.DataSourceProvider;
import app.tasklist.dao.ITaskRepository;
import app.tasklist.dao.TaskRepositoryDB;
import app.tasklist.model.Priority;

public class Main {

	private static final String dateFormat = "yyyy-MM-dd HH:mm";
	
	public static void main(String[] args) {
		ITaskRepository tr = new TaskRepositoryDB(DataSourceProvider.getMySqlDataSource());
		try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in))){
			ConsoleView consoleView = new ConsoleView(br, dateFormat, Priority.values());
			Service service = new Service(tr);
			Controller ctrlr = new Controller(service, consoleView);
			ctrlr.start(); 
		}
		catch(Exception e){/**/}
	}
}

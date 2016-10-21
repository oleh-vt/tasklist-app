package app.tasklist;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class InputManager {

	private BufferedReader br;
	String[] priorities;
	
	public InputManager(BufferedReader br, String[] priorities) {
		this.br = br;
		this.priorities = priorities;
	}
	
	
	public Map<String, String> readTask(String datePattern){
		OutputManager.printMessage("Task Name (30 symbols): ");
		String nameInput = getInputString();
		
		OutputManager.printMessage("Deadline (" + datePattern.toUpperCase() + "): ");
		String deadlineInput = getInputString();
		
		OutputManager.printMessage("Priority:");
		
		for(int i = 0; i < priorities.length; i++){
			OutputManager.printMessage(i+1 + " - " + priorities[i]);
		}
		int priority = getUserChoice(1, priorities.length);
		
		Map<String, String> inputValues = new HashMap<>();
		inputValues.put("name", nameInput);
		inputValues.put("deadline", deadlineInput);
		inputValues.put("priority", Integer.toString(priority));
		
		return inputValues;
	}
	
	public int getUserChoice(int firstOption, int lastOption){
		int choice = -1; boolean success = false;
		while(!success){
			choice = getInputInt();
			if(choice < firstOption || choice > lastOption){
				OutputManager.printError("Illegal input: Please select in a range [" + firstOption + ".." + lastOption + "]");
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
					OutputManager.printError("Error: Integer numbers only!");
				}
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
		return input;
	}

}

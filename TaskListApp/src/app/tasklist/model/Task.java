package app.tasklist.model;

import java.util.Date;

public class Task {
	private int id;
	private String name;
	private Date deadline;
	private Priority priority = Priority.MEDIUM;
	private boolean completed;
	
	public Task() {
		super();
	}

	public Task(String name, Date deadline, Priority priority) {
		super();
		this.name = name;
		this.deadline = deadline;
		this.priority = priority;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	@Override
	public String toString() {
		return "ID: " + id + ", Name: " + name + ", Deadline: " + deadline.toString() +
				", Priority: " + priority.name() + ", Completed: " + completed;
	}

}

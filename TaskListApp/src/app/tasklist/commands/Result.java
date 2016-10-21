package app.tasklist.commands;

public class Result<T> {
	private T data;
	
	public Result(T data) {
		this.data = data;
	}

//	public void setData(T data){
//		this.data = data;
//	}
	
	public T getData(){
		return this.data;
	}
}

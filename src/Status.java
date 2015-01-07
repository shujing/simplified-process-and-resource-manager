import java.util.ArrayList;
import java.util.List;

public class Status {


public enum Type {
	RUNNING, BLOCKED, READY
}//有没有必要把active 和 suspend的状态分开？

	
	public Type type;
	//private String type;
	private List<PCB> list;

	public Status() {
		// TODO Auto-generated constructor stub
	}
/*
	void setType(String t) {
		type = t;
	}
	
	String getType(){
		return type;
	}
	*/
	void setList(List l){
		list = l;
	}

	List getList(){
		return list;
	}
}

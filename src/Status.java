import java.util.ArrayList;
import java.util.List;

public class Status {


public enum Type {
	RUNNING, BLOCKED, READY
}//��û�б�Ҫ��active �� suspend��״̬�ֿ���

	
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

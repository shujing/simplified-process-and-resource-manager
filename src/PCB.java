import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PCB {

	public PCB() {
		// TODO Auto-generated constructor stub
		//map.put(name, id);
		for(int i = 0; i < 4; i++){
			acquiredUnits[i] = 0;
		}
	}

	private int id;
	private String memory = "not yet done";
	private PCB parent;
	private List<PCB> children = new ArrayList<PCB>();
	int priority;
	List<RCB> otherRes = new ArrayList<RCB>();
	Status status = new Status();
	String name;
	int requestUnits;
	int[] acquiredUnits = new int[4];
	//int ac
	

	void setId(int id) {
		this.id = id;

	}

	int getId() {
		return id;
	}

	void setMem(String mem) {
		memory = mem;
	}

	String getMem() {
		return memory;
	}

	void setParent(PCB parent) {
		this.parent = parent;
	}

	PCB getParent() {
		return parent;
	}

	void addChild(PCB child) {
		this.children.add(child);
	}

	List<PCB> getChildren() {
		return children;
	}
	

}

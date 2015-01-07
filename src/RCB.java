import java.util.ArrayList;
import java.util.List;

public class RCB {

	public RCB() {
		// TODO Auto-generated constructor stub
		availableUnits=totalUnits;
	}

	int rid;
	int totalUnits;
	int availableUnits;
	int occupiedUnits = totalUnits - availableUnits;
	List<PCB> waitingList = new ArrayList<PCB>();
}

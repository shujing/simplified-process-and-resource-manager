import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Manager {

	static List<PCB> readyList = new ArrayList<PCB>();
	// static List<PCB> waitingList = new ArrayList<PCB>();
	static RCB[] rcbs = new RCB[4];// how many rcb do i have?
	PCB currentProcess;
	static int initialPID = 0;
	Map<String, PCB> map = new HashMap<String, PCB>();

	public Manager() {
		// initialize the RCBs
		for (int i = 0; i < rcbs.length; i++) {
			rcbs[i] = new RCB();
			rcbs[i].totalUnits = i + 1;
			rcbs[i].availableUnits = i + 1;
			rcbs[i].occupiedUnits = 0;
			// rcbs[i].available = true;
			// rcbs[i].waitingList = null;
		}

		init();
	}

	/**
	 * init function, because we don't want any one to change the initial
	 * process, we set it private
	 */
	private void init() {
		currentProcess = new PCB();
		currentProcess.name = "initial process";
		currentProcess.priority = 0;
		currentProcess.status.type = Status.Type.RUNNING;
		currentProcess.status.setList(readyList);
		readyList.add(currentProcess);

	}

	/** parent process create new process */
	int create(String name, int pi) {
		PCB p = new PCB();
		int pid = getNewPid();
		p.setId(pid);
		// p.setMem(memory);
		p.name = name;
		p.priority = pi;
		p.status.type = Status.Type.READY;// how to set the type?
		p.status.setList(readyList);
		p.setParent(currentProcess);

		// nameMap.put(name, pid);
		map.put(p.name, p);
		currentProcess.addChild(p);
		readyList.add(p);
		scheduler();

		// System.out.print("create process " + name + " with priority " + pi);
		return pid;

	}

	private int getNewPid() {
		// TODO Auto-generated method stub
		initialPID++;
		return initialPID;
	}

	/** remove a named process and all its descendants */
	void destroy(String pName) {

		PCB p = map.get(pName);
		if (p == null)
			System.out.println("The process to destroy does not exist");
		else {
			killTree(p);
			scheduler();
		}
	}

	void killTree(PCB p) {
		/*
		 * for (int i = 0; i < p.getChildren().size(); i++) { PCB q =
		 * p.getChildren().get(i); killTree(q); }
		 */
		while (p.getChildren().size() != 0) {
			killTree(p.getChildren().get(0));
			p.getChildren().remove(0);
		}

		if (p.status.type.equals(Status.Type.RUNNING))
			currentProcess = null;
		p.status.getList().remove(p);
		map.remove(p.name);
		for (int i = 0; i < 4; i++) {
			release(i, p.acquiredUnits[i]);
		}
		
		System.out.println(p.name + " is destroyed");
		// remove(status.getList(), this);
		// release(memory);
		// for (int i = 0; i < p.otherRes.size(); i++) {
		// p.otherRes.get(i).available = true;
		// release(p.otherRes.get(i).rid);
		// }
		// release(p.otherRes);
		// deletePCB(this);
	}

	void request(int rid, int units) {
		if (currentProcess.getId() == 0)
			System.out.println("there is no process running now");
		else {
			RCB rcb = rcbs[rid];
			currentProcess.requestUnits = units;
			if (rcb.availableUnits >= units) {
				currentProcess.otherRes.add(rcb);
				rcb.availableUnits -= units;
				currentProcess.acquiredUnits[rid] = units;

				currentProcess.requestUnits = 0;
			} else {
				currentProcess.status.type = Status.Type.BLOCKED;
				currentProcess.status.setList(rcb.waitingList);
				currentProcess.requestUnits = units;
				remove(readyList, currentProcess);
				rcb.waitingList.add(currentProcess);
				System.out.println("Process " + currentProcess.name
						+ " is blocked");
			}
			scheduler();
		}
	}

	/** remove a PCB from the readylist */
	private void remove(List<PCB> readyList2, PCB currentProcess2) {

		readyList2.remove(currentProcess2);
	}

	/** release a resource, whose index is rid */
	void release(int rid, int units) {
		RCB rcb = rcbs[rid];
		if (rcb.occupiedUnits < units) {
			// System.out.println("Only " + rcb.occupiedUnits + " of R" + rid
			// + " are occupied.");
			rcb.availableUnits = rcb.totalUnits;
		} else {
			rcb.availableUnits += units;
		}

		if (rcb.waitingList.size() > 0) {
			PCB p = rcb.waitingList.get(0);
			if (p.requestUnits <= rcb.availableUnits) {
				rcb.waitingList.remove(p);
				p.status.type = Status.Type.READY;
				readyList.add(p);
				p.status.setList(readyList);
			}
		}
		
	//	System.out.println("R"+(rid+1)+" is released");
	}

	void scheduler() {
		// get the highest priority process
		PCB priorest = getHighestPriorityProcess();
		// System.out.println("scheduler " + priorest.name
		// + " has highesr priority");
		if ((currentProcess == null)
				|| (currentProcess.priority < priorest.priority)
				|| (!currentProcess.status.type.equals(Status.Type.RUNNING))) {
			preempt(priorest, currentProcess);
		}
		System.out.println("Process " + currentProcess.name + " is RUNNING.");
	}

	/** make p run, add q to readyList */
	private void preempt(PCB p, PCB q) {
		// TODO Auto-generated method stub
		p.status.type = Status.Type.RUNNING;
		currentProcess = p;
		// readyList.remove(p);

		if ((q != null) && (!q.status.type.equals(Status.Type.BLOCKED))) {
			q.status.type = Status.Type.READY;
			readyList.remove(q);
			readyList.add(q);
		}
	}

	void timeOut() {
		readyList.remove(currentProcess);
		currentProcess.status.type = Status.Type.READY;
		readyList.add(currentProcess);
		currentProcess = null;
		scheduler();
	}

	PCB getHighestPriorityProcess() {
		PCB priorest = readyList.get(0);
		for (int i = 1; i < readyList.size(); i++) {
			if (priorest.priority < readyList.get(i).priority)
				priorest = readyList.get(i);
		}
		return priorest;
	}

	/** list all the processes and their status */
	void allProcess() {
		for (int i = 0; i < readyList.size(); i++) {
			System.out.println(readyList.get(i).name + " "
					+ readyList.get(i).status.type);
		}
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < rcbs[i].waitingList.size(); j++)
				System.out.println("R" + (i + 1) + " waiting list: "
						+ rcbs[i].waitingList.get(j).name);
		}
		/*
		 * for (int i = 0; i < waitingList.size(); i++) {
		 * System.out.println(waitingList.get(i).name + " " +
		 * waitingList.get(i).status.type); }
		 */
	}

	/** list all the resources and their status */
	void allResource() {
		for (int i = 0; i < 4; i++) {
			System.out.print("Resource R" + (i + 1) + " has "
					+ rcbs[i].totalUnits + " units");
			System.out.print(" , " + rcbs[i].availableUnits
					+ " of them are available now.");
			for (int j = 0; j < rcbs[i].waitingList.size(); j++)
				System.out.print(" Process" + rcbs[i].waitingList.get(j).name
						+ ", ");
			System.out.println("is waiting for this resource");
		}
	}
}

// maybe i should make a function to add a process to a ready list when i set
// its status to ready, just check whether it has already been in it
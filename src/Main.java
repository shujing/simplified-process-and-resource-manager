import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {

	public Main() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Manager m = new Manager();
		String name;
		Scanner in = new Scanner(System.in);

		do {

			name = in.nextLine();

			try {

				String[] instructions = name.split(" ");

				String firstIns = instructions[0];

				// to make sure that a create instruction look like this: cr x 1
				if (firstIns.equals("cr")
						&& (Pattern.compile("1|2").matcher(instructions[2])
								.matches())) {
					String pName = instructions[1];
					int pi = Integer.parseInt(instructions[2]);
					m.create(pName, pi);
				} else if (firstIns.equals("to"))
					m.timeOut();

				else if (firstIns.equals("req") && (instructions.length == 3)
						&& (instructions[1].charAt(0) == 'R')) {
					int rid = instructions[1].charAt(1) - '1';
					int count = Integer.parseInt(instructions[2]);

					m.request(rid, count);

				} else if (firstIns.equals("rel") && (instructions.length == 3)
						&& (instructions[1].charAt(0) == 'R'))
					m.release(instructions[1].charAt(1)-'1',
							Integer.parseInt(instructions[2]));
				else if (firstIns.equals("de") && (instructions.length == 2))
					m.destroy(instructions[1]);
				else if (name.equals("p"))
					m.allProcess();
				else if (name.equals("r"))
					m.allResource();
				/*
				 * else System.out.println("ILLEGAL INPUT");
				 */
			} catch (Exception e) {

				System.out.println("ILLEGAL INPUT");
				e.printStackTrace();
			}
		} while (name != null);
	}

}

package config;

import java.util.*;
import java.util.regex.Pattern;
import java.io.*;
import java.math.BigInteger;

public class BundleDiffChecker {
	static final long mod = (int) 1e9 + 7;
	static int[][] dp;
	static int[][] path;
	static ArrayList<String> a;
	static ArrayList<String> b;

	public static void diff(String masterFile, String branchFile) throws Exception {

		System.out.println(branchFile);
	}

	public static void DifferenceChecker(String masterFile, String branchFile) throws Exception {

		File master = new File(masterFile);
		File branch = new File(branchFile);

		Scanner ina = new Scanner(master);
		Scanner inb = new Scanner(branch);

		a = new ArrayList();
		b = new ArrayList();
		while (ina.hasNextLine())
			a.add(ina.nextLine());
		while (inb.hasNextLine())
			b.add(inb.nextLine());

//		System.out.println("Test - " + a.size() + " " + b.size());

		if (a.size() > 8000) {
			System.out.println("Test - " + a.size() + " - " + branchFile);
			return;
		}
		dp = new int[b.size() + 1][a.size() + 1];

		path = new int[b.size() + 1][a.size() + 1];

		Arrays.fill(path[0], 2);
		
		for (int i = 1; i <= b.size(); i++)
			path[i][0] = 1;
		
		for (int i = 1; i <= b.size(); i++) {
			for (int j = 1; j <= a.size(); j++) {
				if (dp[i][j - 1] >= dp[i - 1][j]) {
					dp[i][j] = dp[i][j - 1];
					path[i][j] = 2;
				} else {
					dp[i][j] = dp[i - 1][j];
					path[i][j] = 1;
				}
				if (match(a.get(j - 1), b.get(i - 1))) {
					if (dp[i - 1][j - 1] + 1 > dp[i][j - 1] && dp[i - 1][j - 1] + 1 > dp[i - 1][j]) {
						dp[i][j] = dp[i - 1][j - 1] + 1;
						path[i][j] = 3;
					}
				}
			}
		}

		String lines = find(b.size(), a.size(), path, a, b);

		try {

			BufferedWriter writer = new BufferedWriter(new FileWriter("./src/main/java/files/output.txt"));
			writer.write(lines);
			writer.close();
		} catch (Exception e) {
			System.out.println( " Line 74 in budle diff check" + e.getMessage());
		}

		BufferedReader br1 = null;
		BufferedReader br2 = null;
		BufferedReader br3 = null;
		String sCurrentLine;
		List<String> list1 = new LinkedList<String>();
		List<String> master1 = new LinkedList<String>();
		List<String> branch1 = new LinkedList<String>();

		try {
			br1 = new BufferedReader(new FileReader(new File(masterFile)));

			while ((sCurrentLine = br1.readLine()) != null) {
				master1.add(sCurrentLine);
			}

		} catch (Exception e) {
//			System.out.println(e.getMessage());
		}

		try {
			br2 = new BufferedReader(new FileReader(new File(branchFile)));

			while ((sCurrentLine = br2.readLine()) != null) {
				branch1.add(sCurrentLine);
			}

		} catch (Exception e) {
			System.out.println(" Line 104 in bundle diff check " +e.getMessage());
		}

		try {
			br3 = new BufferedReader(new FileReader("./src/main/java/files/output.txt"));

			while ((sCurrentLine = br3.readLine()) != null) {
				list1.add(sCurrentLine);
			}

		} catch (Exception e) {
//			System.out.println(e.getMessage());

		}

		BufferedWriter writer = null;
		BufferedWriter writer1 = null;
		for (int i = 0, j = 1, k = 1; i < list1.size(); i++) {

			String line = list1.get(i);
			try {
				if (line.contains("//added")) { 

					try {

						writer = new BufferedWriter(new FileWriter("./src/main/java/files/positiveDifference.txt",true));
						writer1 = new BufferedWriter(new FileWriter("./src/main/java/files/occurence.txt",true));

					} catch (FileNotFoundException e) {
						System.out.println(e.getMessage());
					}

					for (; j < branch1.size(); j++) {
						try {
							if (branch1.get(j).contains(line.split("//added")[0]))
								break;
						} catch (Exception e) {
						}
					}
					int p = j + 1; 
					if (Pattern.matches(".*[a-z].*", line.split("//added")[0])) {
						
						String ts = branchFile + "----" + p + "----" + line.split("//added")[0]; 
						writer.write(ts+"\n"); 
						writer1.write(ts+"\n");
						
					}
					writer.close();
					writer1.close();
				}
			} catch (Exception e) {
				System.out.println(" Line 154 in budle diff check" +e.getMessage());
			}
			try {
				if (line.contains("//deleted")) {
					try {

						writer = new BufferedWriter(new FileWriter("./src/main/java/files/negativeDifference.txt",true));
						writer1 = new BufferedWriter(new FileWriter("./src/main/java/files/occurence.txt",true));
						
					} catch (FileNotFoundException e) {
						System.out.println(e.getMessage());
					}

					for (; k < master1.size(); k++) {

						if (master1.get(k).contains(line.split("//deleted")[0]))
							break;

					}
					int p = k + 1;
					if (Pattern.matches(".*[a-z].*", line.split("//deleted")[0])) {
						String st = masterFile + "----" + p + "----" + master1.get(k);
						writer.write(st+"\n");
						writer1.write(st+"\n");
					}

					writer.close();
					writer1.close();
				}
			} catch (Exception e) {
//				System.out.println(e.getMessage());
			}
		}
	}

	public static String find(int i, int j, int[][] path, ArrayList<String> a, ArrayList<String> b) {
		if (i == 0 && j == 0)
			return "";

		if (path[i][j] == 1)
			return find(i - 1, j, path, a, b) + b.get(i - 1) + "//added \n";

		if (path[i][j] == 2)
			return find(i, j - 1, path, a, b) + a.get(j - 1) + "//deleted \n";

		return find(i - 1, j - 1, path, a, b) + a.get(j - 1) + "\n";
	}

	static boolean match(String a, String b) {
		if (a.length() > b.length() || a.length() < b.length())
			return false;

		for (int i = 0; i < a.length(); i++)
			if (a.charAt(i) != b.charAt(i))
				return false;

		return true;
	}
}

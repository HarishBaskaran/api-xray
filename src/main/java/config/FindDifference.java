package config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import utils.FileOperations;

public class FindDifference {

	static File rootFolders1 = null;
	static File rootFolders2 = null;

	public FindDifference(File rootFolders1, File rootFolders2) {
		this.rootFolders1 = rootFolders1;
		this.rootFolders2 = rootFolders2;

	}

	public static boolean findBundleDiff() throws Exception {

//		findPositiveDifferenceRecursively(rootFolders1);

//		findNegativeDifferenceRecursively(rootFolders2);

		return findBundleDifferenceRecursively(rootFolders1);

	}

	private static void findPositiveDifferenceRecursively(final File folder) throws Exception {

		for (File file : folder.listFiles()) {

			if (file.isDirectory()) {
				findPositiveDifferenceRecursively(file);

			} else if (file.isFile()) {

				String file1 = file.toString().replace(rootFolders1.toString(), rootFolders2.toString());
				System.out.println(file + " --- " + file1);
				File f = new File(file1);
				if (f.exists()) {
				} else if (file1.contains(".java") && !file1.contains("\\src\\test\\java")) {

					BufferedReader br1 = null;
					String sCurrentLine;

					try {
						br1 = new BufferedReader(new FileReader(file));
						int i = 1;
						while ((sCurrentLine = br1.readLine()) != null) {
//							System.out.println(file + "----" + i++ + "----" + sCurrentLine);
							FileOperations.writeFiles(new File("./src/main/java/files/positiveDifference.txt"),
									file + "----" + i++ + "----" + sCurrentLine + "\n", true);
						}
					} catch (Exception e) {
//						System.out.println(e.getMessage());
					}

				}

			}
		}
	}

	private static void findNegativeDifferenceRecursively(final File folder) throws Exception {

		for (File file : folder.listFiles()) {

			if (file.isDirectory()) {
				findNegativeDifferenceRecursively(file);

			} else if (file.isFile()) {
				String file1 = file.toString().replace(rootFolders2.toString(), rootFolders1.toString());
				File f = new File(file1);
				if (f.exists()) {
				} else if (file1.contains(".java") && !file1.contains("\\src\\test\\java")) {
					PrintStream out = null;
					BufferedReader br1 = null;
					String sCurrentLine;

					try {
						br1 = new BufferedReader(new FileReader(file));
						int i = 1;
						while ((sCurrentLine = br1.readLine()) != null) {
							
								
								FileOperations.writeFiles(new File("./src/main/java/files/positiveDifference.txt"),
										file + "----" + i++ + "----" + sCurrentLine + "\n", true);
						
						}
					} catch (Exception e) {
//						System.out.println(e.getMessage());
					}
					out.flush();
					out.close();
				}
			}
		}
	}

	static int y = 1;

	private static boolean findBundleDifferenceRecursively(final File folder) throws Exception {

		try {
			for (File file : folder.listFiles()) {

				if (file.isDirectory()) {
					findBundleDifferenceRecursively(file);

				} else if (file.isFile()) {
					String file1 = file.toString().replace(rootFolders1.toString(), rootFolders2.toString());
					File f = new File(file1);
					if (f.exists() && file.isFile() && !file.getAbsolutePath().contains("src\\test\\")
							&& !file.getAbsolutePath().contains(".git") && !file.getAbsolutePath().contains(".json")
							&& !file.getAbsolutePath().contains(".js") && file.getAbsolutePath().endsWith(".java")) {

						BundleDiffChecker.DifferenceChecker(file.getCanonicalPath().toString(),
								f.getCanonicalPath().toString());

					}
				}
			}
			return true;

		} catch (Exception e) {
			System.out.println(folder + " not found for diff check");
			return false;
		}
	}
}
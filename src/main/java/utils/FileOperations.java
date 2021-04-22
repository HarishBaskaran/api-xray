package utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileOperations {

	public static List<String> read(String path) throws Exception {

		File master = new File(path);

		Scanner ina = new Scanner(master);

		List<String> a = new ArrayList();

		while (ina.hasNextLine())
			a.add(ina.nextLine());

		if (a.size() < 1) {
			System.out.println("There is nothing in file " + path);
		}
		System.out.println("There are " + a.size() + " changes happened");
		return a;

	}

	public static void writeFiles(File file, String s, boolean append) {

		try {
			FileOutputStream fileOutputStream = new FileOutputStream(file, append);
			fileOutputStream.write(s.getBytes(), 0, s.length());

		} catch (IOException e1) {
			System.out.println(e1);
		}

	}

	public static void cleanFolders(File dir) {

		for (File file : dir.listFiles())
			if (!file.isDirectory()) {
				file.delete();
				System.out.print(file.getName() + ", ");
			}
		System.out.println(" deleted");
	}
}

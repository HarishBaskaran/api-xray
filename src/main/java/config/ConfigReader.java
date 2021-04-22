package config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigReader {

	static {

//		fileClean("./src/main/java/files/occurence.txt");
		fileClean("./src/main/java/files/output.txt");

	}

	public static void fileClean(String file) {
		String lines = "";
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(lines);
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	static List<File> getRootFolders() throws IOException {
		BufferedReader pathsReader = new BufferedReader(new FileReader("./src/main/java/files/paths/search-paths.txt"));
		String line;
		List<File> folders = new ArrayList<File>(5);
		while (pathsReader.ready()) {
			line = pathsReader.readLine();
			if (line == null)
				break;

			folders.add(new File(line));
		}
		pathsReader.close();
		return folders;
	}

	public static File getMasterFolders() throws IOException {
		BufferedReader pathsReader = new BufferedReader(
				new FileReader("./src/main/java/files/paths/search-master.txt"));
		String line;
		File folders = null;
		while (pathsReader.ready()) {
			line = pathsReader.readLine();
			if (line == null)
				break;

			folders = new File(line);
		}
		pathsReader.close();
		return folders;
	}

	public static File getBundleFolders() throws IOException {
		BufferedReader pathsReader = new BufferedReader(
				new FileReader("./src/main/java/files/paths/search-bundle.txt"));
		String line;
		File folders = null;
		while (pathsReader.ready()) {
			line = pathsReader.readLine();
			if (line == null)
				break;

			folders = new File(line);
		}
		pathsReader.close();
		return folders;
	}
	
	public static File getInterfaceFolders() throws IOException {
		BufferedReader pathsReader = new BufferedReader(
				new FileReader("./src/main/java/files/paths/search-interface.txt"));
		String line;
		File folders = null;
		while (pathsReader.ready()) {
			line = pathsReader.readLine();
			if (line == null)
				break;

			folders = new File(line);
		}
		pathsReader.close();
		return folders;
	}
}

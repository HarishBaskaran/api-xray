package config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Matches {

	private static List<File> rootFolders;
	private static List<String> words;

	public void findMatches(List<String> words, File file) throws IOException {

		int number = 0;
		rootFolders = ConfigReader.getRootFolders();
		this.words = words;

		while (true) {

			try {
				words.get(number++);
			} catch (IndexOutOfBoundsException e) {
				break;
			}

			for (File root : rootFolders) {
				findRecursively(root, file);
			}

		}

	}

	private static void findRecursively(final File folder, File output) {

		for (File file : folder.listFiles()) {

			if (file.isDirectory()) {
				findRecursively(file, output);
			} else if (file.isFile() && !file.getAbsolutePath().contains("src\\test\\")
					&& !file.getAbsolutePath().contains(".git") && !file.getAbsolutePath().contains(".json")
					&& !file.getAbsolutePath().contains(".js") && file.getAbsolutePath().endsWith(".java")) {
				checkContent(file, output);
			}
		}
	}

	private static void checkContent(File file, File output) {

		StringBuilder message = new StringBuilder();

		BufferedReader reader;
		String line;
		int lineNumber;
		try {
			reader = new BufferedReader(new FileReader(file));
			lineNumber = 0;

			while (reader.ready()) {
				lineNumber++;
				line = reader.readLine();
				if (line == null) {
					break;
				}
				if (matchesAnyWord(line)) {
					message.append(file.getAbsolutePath() + "----" + lineNumber + "----" + line.trim() + "\n");
				}
			}

			reader.close();

		} catch (Exception e) {
		}

		if (message.length() > 0) {

			write(message.toString(),output);

		}
	}
	
	private synchronized static void write(String message ,File output) {
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(output, true);
			fileOutputStream.write(message.toString().getBytes(), 0, message.length());
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	private static boolean matchesAnyWord(String line) {
		if (words.isEmpty()) {
			return true;
		}

		for (String word : words) {
			if (line.contains(word) && isContain(line, word)) {
				return true;
			}
		}
		return false;
	}

	private static boolean isContain(String source, String subItem) {
		String pattern = "\\b" + subItem + "\\b";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(source);
		return m.find();
	}

}

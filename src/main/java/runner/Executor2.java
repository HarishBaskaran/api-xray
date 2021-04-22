package runner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

import com.github.javaparser.ast.body.MethodDeclaration;

import config.ConfigReader;
import config.Matches;
import config.MethodFinder;

public class Executor2 {

	int count = 0;
	ArrayList<String> a;
	HashSet<String> b = new HashSet<String>();

	public void runner(String... args) throws Exception {

		count = 0;
		Executor2 executor = new Executor2();

		cleanFolders(new File("./src/main/java/files/method/"));
		cleanFolders(new File("./src/main/java/files/match/"));

		executor.read("./src/main/java/files/occurence.txt");

		do {
			b = new HashSet<String>();
			executor.findMethods();
			executor.recurse(executor);
			if (count > 2) {
				int f = count - 2;
				int s = count - 1;
				if (sameContent(Paths.get("./src/main/java/files/method/method" + f + ".txt"),
						Paths.get("./src/main/java/files/method/method" + s + ".txt")))
					break;
			}
		} while (b.size() > 0);

		executor.findRecursively(new File(args[0]), "./result.txt", executor);
	}

	private static boolean sameContent(Path file1, Path file2) throws IOException {
		final long size = Files.size(file1);
		if (size != Files.size(file2))
			return false;

		if (size < 4096)
			return Arrays.equals(Files.readAllBytes(file1), Files.readAllBytes(file2));

		try (InputStream is1 = Files.newInputStream(file1); InputStream is2 = Files.newInputStream(file2)) {

			int data;
			while ((data = is1.read()) != -1)
				if (data != is2.read())
					return false;
		}

		return true;
	}

	private File createMethodFiles() throws IOException {

		String path = "./src/main/java/files/method/" + "method" + count + ".txt";
		File file = new File(path);
		if (file.createNewFile()) {
//			System.out.println(path + " File Created");
		} else {
			String s = "";
			try {
				FileOutputStream fileOutputStream = new FileOutputStream(file, false);

				fileOutputStream.write(s.getBytes(), 0, s.length());

			} catch (IOException e1) {
				System.out.println(e1);
			}
		}

		return file;
	}

	private File createMatchFiles() throws IOException {

		String path = "./src/main/java/files/match/" + "match" + count++ + ".txt";
		File file = new File(path);
		if (file.createNewFile()) {
//			System.out.println(path + " File Created");
		} else {
			String s = "";
			try {
				FileOutputStream fileOutputStream = new FileOutputStream(file, false);

				fileOutputStream.write(s.getBytes(), 0, s.length());

			} catch (IOException e1) {
				System.out.println(e1);
			}
		}

		return file;
	}

	private static void cleanFolders(File dir) {

		for (File file : dir.listFiles())
			if (!file.isDirectory()) {
				file.delete();
				System.out.print(file.getName() + ", ");
			}
		System.out.println(" deleted");
	}

	public void writeFiles(File file, String s) {

		try {
			FileOutputStream fileOutputStream = new FileOutputStream(file, true);

			fileOutputStream.write(s.getBytes(), 0, s.length());

		} catch (IOException e1) {
			System.out.println(e1);
		}

	}

	public void recurse(Executor2 executor) throws Exception {

		File f = createMethodFiles();
		System.out.println("./src/main/java/files/method/method" + count + ".txt - opened");

		a = new ArrayList();
		b.forEach((x) -> {
			writeFiles(f, x + "\n");
			a.add(x.split("----")[1]);
		});

		File match = createMatchFiles();

		new Matches().findMatches(a, match);
		executor.read(match.getAbsolutePath());

	}

	public void read(String path) throws Exception {

		File master = new File(path);

		Scanner ina = new Scanner(master);

		a = new ArrayList();
		while (ina.hasNextLine())
			a.add(ina.nextLine());
		if (a.size() < 1) {
			System.out.println("There is nothing in file " + path);
		}
	}

	public void findMethods() {

		for (int i = 0; i < a.size(); i++) {

			if (a.get(i).split("----")[0].contains(".java")) {

				String method = new MethodFinder().printMethodsAndLines(a.get(i).split("----")[0],
						Integer.parseInt(a.get(i).split("----")[1]));

				String file = a.get(i).split("----")[0];
				int line = Integer.parseInt(a.get(i).split("----")[1]);

				if (null != method) {
					b.add(file + "----" + "." + method);
				}
			}
		}
	}

	public void findRecursively(final File folder, String output, Executor2 executor) throws Exception {

		File result = null;

		try {
			result = new File(output);
		} catch (Exception e) {
			System.out.println(e);
		}

		for (File file : folder.listFiles()) {

			if (file.isDirectory()) {
				findRecursively(file, output, executor);
			} else if (file.isFile() && !file.getAbsolutePath().contains("src\\test\\")
					&& !file.getAbsolutePath().contains(".git") && !file.getAbsolutePath().contains(".json")
					&& !file.getAbsolutePath().contains(".js") && file.getAbsolutePath().endsWith(".java")) {
				findInterfaceMethods(file, result, executor);
			}
		}
	}

	public void findInterfaceMethods(File file, File output, Executor2 executor) throws Exception {

		executor.read("./src/main/java/files/method/method" + (count - 1) + ".txt");

		List<MethodDeclaration> list = new ArrayList<MethodDeclaration>();
		HashSet<MethodDeclaration> methods = new HashSet<MethodDeclaration>();

		for (int i = 0; i < a.size(); i++) {

			list.addAll(new MethodFinder().printMethods(file, a.get(i).split("----.")[1]));

		}

		for (MethodDeclaration m : list) {
			methods.add(m);
		}

		String s = ("\n-------------------------------------------------\n");

		for (MethodDeclaration m : methods) {
			executor.writeFiles(output, m + s);
		}
	}
}

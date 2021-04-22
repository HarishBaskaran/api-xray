package runner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.github.javaparser.ast.body.MethodDeclaration;

import config.Matches;
import config.MethodFinder;

public class Executor3 {

	static int count = 0;
	static ArrayList<String> a;
	static HashSet<String> b = new HashSet<String>();
	static Executor3 executor = new Executor3();

	public static void main(String... args) throws Exception {

		count = 0;

		cleanFolders(new File("./src/main/java/files/method/"));
		cleanFolders(new File("./src/main/java/files/match/"));

		executor.read("./src/main/java/files/occurence.txt");

		do {
//			b = new HashSet<String>();
//			executor.findMethods();
			executor.findMethodsInParallel();
			executor.recurse();
			if (count > 2) {
				int f = count - 2;
				int s = count - 1;
				if (sameContent(Paths.get("./src/main/java/files/method/method" + f + ".txt"),
						Paths.get("./src/main/java/files/method/method" + s + ".txt")))
					break;
			}
		} while (b.size() > 0);

		executor.findRecursively(new File(args[0]), "./result.txt");
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

	public void recurse() throws Exception {

		File f = createMethodFiles();
		System.out.println("./src/main/java/files/method/method" + count + ".txt - opened");

		a = new ArrayList();
		b.forEach((x) -> {
			writeFiles(f, x + "\n");
			a.add(x.split("----")[1]);

		});

		File match = createMatchFiles();
		Refresh(a, match);
//		new Matches().findMatches(a, match);
		System.out.println("finnished");
		executor.read(match.getAbsolutePath());

	}

	public static void Refresh(List list, File file) {

		for (int x = 0; x < list.size(); x++) {
			MyThread31 temp = new MyThread31(x, list.get(x), file);
			try {
				temp.start();
				temp.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	void read(String path) throws Exception {

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

	public void findMethodsInParallel() throws FileNotFoundException {

		MyThread30 temp = new MyThread30();

		for (int i = 0; i < a.size(); i++) {

			temp.setMyThread1(i, a.get(i));
			Thread thread = new Thread(temp);
			try {
				thread.start();
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		File master = new File("./src/main/java/files/methods.txt");

		Scanner ina = new Scanner(master);
		b = new HashSet<String>();
		while (ina.hasNextLine()) {
			String te = ina.nextLine();
			b.add(te);

		}

		String s123 = " ";
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(master, false);

			fileOutputStream.write(s123.getBytes(), 0, s123.length());

		} catch (IOException e1) {
			System.out.println(e1);
		}

	}

	public void findRecursively(final File folder, String output) throws Exception {

		File result = null;

		try {
			result = new File(output);
		} catch (Exception e) {
			System.out.println(e);
		}

		for (File file : folder.listFiles()) {

			if (file.isDirectory()) {
				findRecursively(file, output);
			} else if (file.isFile() && !file.getAbsolutePath().contains("src\\test\\")
					&& !file.getAbsolutePath().contains(".git") && !file.getAbsolutePath().contains(".json")
					&& !file.getAbsolutePath().contains(".js") && file.getAbsolutePath().endsWith(".java")) {
				findInterfaceMethods(file, result);
			}
		}
	}

	public void findInterfaceMethods(File file, File output) throws Exception {

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

class MyThread30 implements Runnable {
	int k;
	String a = null;
	private volatile String method;

	public void setMyThread1(int i, String a) {

		this.a = a;
		k = i;
	}

	@Override
	public void run() {

		if (a.split("----")[0].contains(".java")) {

			method = new MethodFinder().printMethodsAndLines(a.split("----")[0], Integer.parseInt(a.split("----")[1]));

			String file1 = a.split("----")[0];

			if (null != method) {
				String s = file1 + "----" + "." + method;
				Singleton.getInstance().write(s);
//				Executor3.write(s);
			}

		}

	}

}

class MyThread31 extends Thread {
	int k;
	List<String> a1 = new ArrayList<String>();
	File match;

	public MyThread31(int i, Object object, File match) {

		a1.add((String) object);
		k = i;
		this.match = match;
	}

	@Override
	public void run() {
		try {

			new Matches().findMatches(a1, match);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class Singleton {

	private static final Singleton inst = new Singleton();

	static FileOutputStream fileOutputStream = null;

	private Singleton() {
		super();
	}

	public synchronized static void write(String s) {

		try {
			fileOutputStream = new FileOutputStream(new File("./src/main/java/files/methods.txt"), true);

			fileOutputStream.write(s.getBytes(), 0, s.length());
			fileOutputStream.write("\n".getBytes(), 0, "\n".length());

		} catch (IOException e1) {
			System.out.println(e1);
		}
	}

	public static Singleton getInstance() {
		return inst;
	}

}

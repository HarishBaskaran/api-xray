package runner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;

import config.ConfigReader;
import utils.Compilers;
import utils.FileOperations;
import utils.InterfaceFinders;
import utils.MatchFinders;
import utils.MethodFinders;

public class Executor4 {

	static List<String> a = new ArrayList<String>();
	static HashSet<MethodDeclaration> b = new HashSet<MethodDeclaration>();
	static List<CompilationUnit> allCus = new ArrayList<CompilationUnit>();
	static List<CompilationUnit> allCus1 = new ArrayList<CompilationUnit>();;

	static int i = 1;
	static int k = 1;
	static Map<MethodDeclaration, String> matches1 = new HashMap<MethodDeclaration, String>();
	static Map<MethodDeclaration, String> matches2 = new HashMap<MethodDeclaration, String>();
	static Map<MethodDeclaration, String> matches = new HashMap<MethodDeclaration, String>();
	static Map<MethodDeclaration, String> find = new HashMap<MethodDeclaration, String>();
	static Map<MethodDeclaration, String> find1 = new HashMap<MethodDeclaration, String>();
	static Map<MethodDeclaration, String> results = new HashMap<MethodDeclaration, String>();

	public static void clearAll() {
		matches1 = new HashMap<MethodDeclaration, String>();
		matches2 = new HashMap<MethodDeclaration, String>();
		matches = new HashMap<MethodDeclaration, String>();
		find = new HashMap<MethodDeclaration, String>();
		find1 = new HashMap<MethodDeclaration, String>();
		results = new HashMap<MethodDeclaration, String>();
		allCus = new ArrayList<CompilationUnit>();
		allCus1 = new ArrayList<CompilationUnit>();
		a = new ArrayList<String>();
		b = new HashSet<MethodDeclaration>();
	}

	public static Map<MethodDeclaration, String> runner() throws Exception {

		Executor4.clearAll();

		a = FileOperations.read("./src/main/java/files/positiveDifference.txt");

		allCus = new Compilers().compile(ConfigReader.getBundleFolders());
		allCus1 = new Compilers().compile(ConfigReader.getInterfaceFolders());

		System.out.println("WELCOME TO MY WORLD");
		Executor4.findMethodsInParallel();
		Executor4.findInterfaces();
		return results;

	}

	private static void writeFiles(boolean bool, File file, String s) {

		try {
			FileOutputStream fileOutputStream = new FileOutputStream(file, bool);

			fileOutputStream.write(s.getBytes(), 0, s.length());

		} catch (IOException e1) {
			System.out.println(e1);
		}

	}

	private static void findMethodsInParallel() throws Exception {

		for (String line : a) {

			if (line.split("----")[0].contains(".java")) {

				int lineNumber = Integer.parseInt(line.split("----")[1]);
				String className = line.split("----")[0];

				MethodDeclaration method = new MatchFinders().findMethods(allCus, className, lineNumber);

				if (null != method) {
//					System.out.println(i++ + " -- " + method.getNameAsString() + " -- " + className);
					matches1.put(method, className);
					matches2.put(method, className);
					b.add(method);
				}
			}
		}
		i = 1;
		System.out.println(
				"\n-------------------------------------------------------------------------------------------\n");
		b.forEach(y -> {
			System.out.println(i + " -- " + y.getNameAsString());
			writeFiles(true, new File("./innerMethods.txt"), i++ + " -- " + y.getNameAsString() + "\n");
		});
		System.out.println(
				"\n-------------------------------------------------------------------------------------------\n");

	}

	public static void findInterfaces() throws Exception {
		k = 1;
		try {
			do {
				matches1.entrySet().forEach(result -> {
					matches.put(result.getKey(), result.getValue());
					System.out.println(
							"\n-------------------------------------------------------------------------------------------\n");

					FileOperations.writeFiles(new File("./trace.txt"),
							"\n-------------------------------------------------------------------------------------------\n",
							true);

					System.out.println(k + " -- " + matches.size() + " -- " + result.getKey().getNameAsString() + " -- "
							+ result.getValue());
					FileOperations.writeFiles(new File("./trace.txt"),
							k++ + " -- " + matches.size() + " -- " + result.getKey().getNameAsString() + "\n", true);

					i = 1;
					do {
						try {
							Executor4.recurse();
						} catch (Exception e) {
							e.printStackTrace();
						}
					} while (matches.size() > 0);
					matches2.remove(result.getKey(), result.getValue());
				});

			} while (matches2.size() > 0);
		} catch (Exception e) {

		}

	}

	public static void recurse() throws Exception {

		Map<MethodDeclaration, String> methods = new HashMap<MethodDeclaration, String>();
		if (i++ > 9) {
			matches.clear();
		}
		matches.entrySet().forEach(r -> {
			System.out.println("\t" + r.getKey().getNameAsString() + " --- " + r.getValue());
			FileOperations.writeFiles(new File("./trace.txt"),
					"\t" + r.getKey().getNameAsString() + " --- " + r.getValue() + "\n", true);
			methods.putAll(new MethodFinders().findMethods(allCus, r.getKey(), r.getValue()));
			methods.putAll(new MethodFinders().findMethodsExp(allCus, r.getKey(), r.getValue()));
			methods.putAll(new MethodFinders().findMethodsRet(allCus, r.getKey(), r.getValue()));
			if (methods.size() > 0) {
				find.putAll(methods);
				methods.entrySet().forEach(x -> {
					System.out.println("\t\t - " + x.getKey().getNameAsString() + "----" + x.getValue());
					FileOperations.writeFiles(new File("./trace.txt"),
							"\t\t - " + x.getKey().getNameAsString() + "----" + x.getValue() + "\n", true);
				});

			} else {
				System.out
						.println("\t\t - Entered interface - " + r.getKey().getNameAsString() + " --- " + r.getValue());
				FileOperations.writeFiles(new File("./trace.txt"),
						"\t\t - Entered interface - " + r.getKey().getNameAsString() + " --- " + r.getValue() + "\n",
						true);

				try {
					results.putAll(
							InterfaceFinders.findMethods(allCus1, allCus, r.getKey().getNameAsString(), r.getValue()));

					results.putAll(InterfaceFinders.findMethodsRandomly(allCus1, allCus, r.getKey().getNameAsString(),
							r.getValue()));

				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		});
		methods.entrySet().stream().forEach(d -> {
			if (!d.getKey().getNameAsString().toLowerCase().contains("test")
					&& !d.getValue().toLowerCase().contains("test")) {
				find1.put(d.getKey(), d.getValue());
			}
		});
		if (matches.equals(find1)) {
			System.out.println("yesy same");
			FileOperations.writeFiles(new File("./trace.txt"), "yesy same\n", true);

			find.clear();
			methods.entrySet().stream().forEach(r -> {
				System.out
						.println("\t\t - Entered interface - " + r.getKey().getNameAsString() + " --- " + r.getValue());
				FileOperations.writeFiles(new File("./trace.txt"),
						"\t\t - Entered interface - " + r.getKey().getNameAsString() + " --- " + r.getValue() + "\n",
						true);

				try {
					results.putAll(
							InterfaceFinders.findMethods(allCus1, allCus, r.getKey().getNameAsString(), r.getValue()));

					results.putAll(InterfaceFinders.findMethodsRandomly(allCus1, allCus, r.getKey().getNameAsString(),
							r.getValue()));

				} catch (IOException e) {
					e.printStackTrace();
				}

			});
		}
		methods.clear();
		matches.clear();
		System.out.println("## " + find.size());
		FileOperations.writeFiles(new File("./trace.txt"), "## " + find.size() + "\n", true);
		find.entrySet().stream().forEach(d -> {
			if (!d.getKey().getNameAsString().toLowerCase().contains("test")
					&& !d.getValue().toLowerCase().contains("test")) {
				matches.put(d.getKey(), d.getValue());
			}
		});
		find.clear();
//		System.out.println("\tmatches count " + matches.size());
	}
}

package runner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.github.javaparser.utils.SourceRoot;

import config.ConfigReader;
import config.MethodFinder;
import utils.FileOperations;
import utils.InterfaceFinders;
import utils.MatchFinders;
import utils.MethodFinders;

public class Executor5 {

	static int count = 0;
	static List<String> a;
	static HashSet<MethodDeclaration> b = new HashSet<MethodDeclaration>();
	static List<CompilationUnit> allCus;
	static List<CompilationUnit> allCus1;

	public static Map<MethodDeclaration, String> runner() throws Exception {

		count = 0;

		a = FileOperations.read("./src/main/java/files/positiveDifference.txt");
//		FileOperations.writeFiles(new File("./result.txt"), " ", false);

		File file = ConfigReader.getBundleFolders();

		CombinedTypeSolver typeSolver = new CombinedTypeSolver(new ReflectionTypeSolver(),
				new JavaParserTypeSolver(file));
		// Use our Symbol Solver while parsing
		ParserConfiguration parserConfiguration = new ParserConfiguration()
				.setSymbolResolver(new JavaSymbolSolver(typeSolver));

		SourceRoot sourceRoot = new SourceRoot(file.toPath());

		sourceRoot.setParserConfiguration(parserConfiguration);
		List<ParseResult<CompilationUnit>> parseResults = sourceRoot.tryToParse("");
		// Now get all compilation units
		allCus = parseResults.stream().filter(ParseResult::isSuccessful).map(r -> r.getResult().get())
				.collect(Collectors.toList());

		File file1 = ConfigReader.getInterfaceFolders();

		CombinedTypeSolver typeSolver1 = new CombinedTypeSolver(new ReflectionTypeSolver(),
				new JavaParserTypeSolver(file1));
		// Use our Symbol Solver while parsing
		ParserConfiguration parserConfiguration1 = new ParserConfiguration()
				.setSymbolResolver(new JavaSymbolSolver(typeSolver1));

		SourceRoot sourceRoot1 = new SourceRoot(file1.toPath());

		sourceRoot1.setParserConfiguration(parserConfiguration1);
		List<ParseResult<CompilationUnit>> parseResults1 = sourceRoot1.tryToParse("");
		// Now get all compilation units
		allCus1 = parseResults1.stream().filter(ParseResult::isSuccessful).map(r -> r.getResult().get())
				.collect(Collectors.toList());

		System.out.println("entered");
		Executor5.findMethodsInParallel();
return result;
//		for (String line : a)
//			InterfaceFinders.findMethods(allCus, line.split("----")[1]);
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
					b.add(method);
				}
			}
		}
		i = 1;
		System.out.println(
				"\n-------------------------------------------------------------------------------------------\n");
		b.forEach(y -> {
			System.out.println(i++ + " -- " + y.getNameAsString());
		});
		System.out.println(
				"\n-------------------------------------------------------------------------------------------\n");

		
		try {
			do {
				matches1.entrySet().forEach(result -> {
					matches.put(result.getKey(), result.getValue());
					System.out.println(
							"\n-------------------------------------------------------------------------------------------\n");

					System.out.println(i++ + " -- " + matches.size() + " -- " + result.getKey().getNameAsString()
							+ " -- " + result.getValue());
					i = 1;
					do {
						try {
							Executor5.recurse();
						} catch (Exception e) {
							e.printStackTrace();
						}
					} while (matches.size() > 0);
					matches1.remove(result.getKey(), result.getValue());
				});

			} while (matches1.size() > 0);
		} catch (Exception e) {

		}

		String s = "\n----------------------------------------------------\n";

//		result.entrySet().forEach(res -> {
//			FileOperations.writeFiles(new File("./result.txt"), res.getKey().getNameAsString() + s, true);
//		});
	}

	static int i = 1;
	static Map<MethodDeclaration, String> matches1 = new HashMap<MethodDeclaration, String>();
	static Map<MethodDeclaration, String> matches = new HashMap<MethodDeclaration, String>();
	static Map<MethodDeclaration, String> find = new HashMap<MethodDeclaration, String>();
	static Map<MethodDeclaration, String> result = new HashMap<MethodDeclaration, String>();
	static Map<MethodDeclaration, String> results = new HashMap<MethodDeclaration, String>();

	public static void recurse() throws Exception {

		Map<MethodDeclaration, String> methods = new HashMap<MethodDeclaration, String>();
		a = new ArrayList();
		if(i++ > 12)
			matches.clear();
		
		matches.entrySet().forEach(r -> {
			System.out.println("\t" + r.getKey().getNameAsString() + " --- " + r.getValue());
			methods.putAll(new MethodFinders().findMethodsExp(allCus, r.getKey(), r.getValue()));
			if (methods.size() > 0) {
				find.putAll(methods);
				methods.entrySet().forEach(x -> {
					System.out.println("\t\t - " + x.getKey().getNameAsString() + "----" + x.getValue());
					a.add(x.getValue() + "----" + x.getKey().getNameAsString());
				});
				
			} else {
//				System.out.println("Entered interface " + results.size() + " - " + r.getKey().getNameAsString()
//						+ " --- " + r.getValue());
				try {
					results.clear();
					results.putAll(
							InterfaceFinders.findMethods(allCus1, allCus, r.getKey().getNameAsString(), r.getValue()));
//					System.out.println("\t - COUNT - " + results.size());

					results.putAll(InterfaceFinders.findMethodsRandomly(allCus1, allCus, r.getKey().getNameAsString(),
							r.getValue()));
//					System.out.println("\t - COUNT - " + results.size());

					if (results.size() > 0) {
						result.putAll(results);
//						i = 1;
//						results.entrySet().forEach(res -> {
//							System.out.println(i++ + " " + res.getKey().getNameAsString() + " --- " + res.getValue());
//						});
						results.clear();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		});
		if(matches.equals(methods)) {
			System.out.println("yesy same" );
			find.clear();
		}
		methods.clear();
		matches.clear();
		System.out.println("## " + find.size());
		find.entrySet().stream().forEach(d -> {
//			System.out.println("@ " + d.getKey().getNameAsString());
			if (!d.getKey().getNameAsString().toLowerCase().contains("test") && !d.getValue().toLowerCase().contains("test")) {
//				System.out.println("\t@ " + d.getKey().getNameAsString());
				matches.put(d.getKey(), d.getValue());
			}
		});
//		matches.putAll(find);
		find.clear();
		System.out.println("\tmatches count " + matches.size());
	}
}

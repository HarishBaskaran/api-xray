package runner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.javaparser.Navigator;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.github.javaparser.utils.SourceRoot;

import config.ConfigReader;
import utils.FileOperations;
import utils.InterfaceFinders;
import utils.MethodFinders;

public class Rough2 {

	static List<CompilationUnit> allCus1;

	static List<CompilationUnit> allCus;

	static Map<MethodDeclaration, String> matches1 = new HashMap<MethodDeclaration, String>();
	static Map<MethodDeclaration, String> matches = new HashMap<MethodDeclaration, String>();
	static Map<MethodDeclaration, String> find = new HashMap<MethodDeclaration, String>();
	static Map<MethodDeclaration, String> result = new HashMap<MethodDeclaration, String>();

	public static void main(String[] args) throws Exception {
//		Executor4.runner();
		Rough2.check();
	}
	
	public static void check() throws Exception {
		
		FileOperations.writeFiles(new File("./result.txt"), " ", false);

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
		
		
			
		result.putAll(InterfaceFinders.findMethods(allCus1, allCus, "getGeneralAvailabilityByBuildings", "com.agilysys.pms.property.service.PropertyAvailabilityImplementation"));
				
		result.entrySet().stream().forEach(entry -> {
			System.out.println(entry.getKey().getAnnotations().size());
		});
		
	}
}
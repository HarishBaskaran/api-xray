package runner;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
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

public class Rough {

	public static void main(String[] args) throws Exception {
		
		String line = "com\\agilysys\\pms\\property\\api\\PropertyAvailabilityInterface";
		String file = "C:\\Harish\\BundleVersions\\70033\\propertyinterface\\src\\main\\java\\com\\agilysys\\pms\\property\\api\\PropertyAvailabilityInterface.java";
		
		if(file.contains(line)) {
			System.out.println("true");
		}
	}
	public static void main1(String[] args) throws Exception {

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
		List<CompilationUnit> allCus = parseResults.stream().filter(ParseResult::isSuccessful)
				.map(r -> r.getResult().get()).collect(Collectors.toList());

//		Navigator.findMethodCall(node, methodName)

//		long n = getNodes(allCus, MethodDeclaration.class).stream()
//				.filter(m -> ((CallableDeclaration<MethodDeclaration>) m).getParameters().size() > 3).count();
//		System.out.println("N of methods with 3+ params: " + n);

//		getNodes(allCus, ClassOrInterfaceDeclaration.class).stream()
//				.filter(c -> !((ClassOrInterfaceDeclaration) c).isInterface())
//				.sorted(Comparator.comparingInt(o -> -1 * ((Class) o).getMethods().length)).limit(3)
//				.forEach(c -> System.out.println(i++ + ((NodeWithSimpleName<MethodDeclaration>) c).getNameAsString()));

		allCus.forEach(cu -> {
			j = 1;
//			System.out.print(i++ + " ");

			cu.findAll(ClassOrInterfaceDeclaration.class).stream()
//			.filter(c -> !((ClassOrInterfaceDeclaration) c).isInterface() && c.getFullyQualifiedName().toString().contains("GroupAllocationManager"))
			.forEach(c -> {
				System.out.println("-------------------------------------------------------------");
				System.out.println(c.getFullyQualifiedName().toString() + " - " + c.getName() + "\n");
				
				cu.findAll(MethodDeclaration.class).stream().forEach(m -> {
					System.out.println("\t" + j++ + " " + m.getNameAsString());
//					if(Navigator.findMethodCall(m, "validateCreate").isPresent()) {
//					
//						System.out.println(m.getNameAsString());
//						m.getTokenRange();
//						m.getModifiers();
//						m.getAnnotations();
//						m.getTypeParameters();
//						m.getName();
//						m.getParameters();
//						m.getThrownExceptions();
//						m.getReceiverParameter();
//						
//						
//					}
					System.out.println("\t\t" + m.asMethodDeclaration().getBegin().toString());
					m.getBody().get().getChildNodes().iterator().forEachRemaining(r -> {
						System.out.println("\t\t\t" + r);
						r.findAll(ExpressionStmt.class).stream().forEachOrdered(a -> {
							 a.findAll(MethodCallExpr.class).iterator().forEachRemaining(b -> {
								 System.out.println("\t\t\t\t" + b.getChildNodes());
								 System.out.println("\t\t\t\t" + b.getToStringPrettyPrinterConfiguration().getVisitorFactory());
								 
							 });
						});
						System.out.println();
					});
				});
			});
			

//			cu.findAll(MethodDeclaration.class).stream().forEach(m -> {
//				System.out.println("\t" + j++ + " " + m.getNameAsString());
//				if(Navigator.findMethodCall(m, "validateCreate").isPresent()) {
//					System.out.println(m.getNameAsString());
//				}
//				System.out.println("\t\t" + m.asMethodDeclaration().getBegin().toString());
//				m.getBody().get().getChildNodes().iterator().forEachRemaining(r -> {
//					System.out.println("\t\t\t" + r);
//					r.findAll(ExpressionStmt.class).stream().forEachOrdered(a -> {
//						 a.findAll(MethodCallExpr.class).iterator().forEachRemaining(b -> {
//							 System.out.println("\t\t\t\t" + b.getChildNodes());
//							 System.out.println("\t\t\t\t" + b.getToStringPrettyPrinterConfiguration().getVisitorFactory());
//							 
//						 });
//					});
//					System.out.println();
//				});
//			});

		});

	}

	static int i = 1;
	static int j = 1;

	public static List getNodes(List cus, Class nodeClass) {
		List res = new LinkedList();
		cus.forEach(cu -> res.addAll(((Node) cu).findAll(nodeClass)));
		return res;
	}

	private List<MethodDeclaration> methodIdentifier1(File f) throws FileNotFoundException {

		int i = 1;

		List<MethodDeclaration> list = new ArrayList<MethodDeclaration>();

		CompilationUnit compilationUnit = StaticJavaParser.parse(f);

		compilationUnit.findAll(MethodDeclaration.class).stream().forEach((x) -> {

			System.out.println();
		});

		return list;
	}

}

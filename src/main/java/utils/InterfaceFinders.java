package utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.github.javaparser.utils.SourceRoot;

public class InterfaceFinders {

	static Map<MethodDeclaration, String> methods = new HashMap<MethodDeclaration, String>();
	static NodeList<ClassOrInterfaceType> nodes = null;
	static int j = 1;

	public static Map<MethodDeclaration, String> findMethods(List<CompilationUnit> allCus1,
			List<CompilationUnit> allCus, String method, String className) throws IOException {

		String line[] = className.split("\\.");

		allCus.forEach(cu -> {
			cu.findAll(ClassOrInterfaceDeclaration.class).stream()
					.filter(c -> !((ClassOrInterfaceDeclaration) c).isInterface()).forEach(m -> {
						if (m.getNameAsString().equals(line[(line.length) - 1])) {
							nodes = m.getImplementedTypes();
						}
					});
		});
		try {
			if (0 < nodes.size())
				for (ClassOrInterfaceType name : nodes) {
					j = 1;
					allCus1.forEach(cu -> {
						cu.findAll(ClassOrInterfaceDeclaration.class).stream().forEach(m -> {
							if (m.getNameAsString().equals(name.getNameAsString())) {
								System.out.println("\t\t\t" + m.getNameAsString());
								FileOperations.writeFiles(new File("./trace.txt"),
										"\t\t\t" + m.getNameAsString() + "\n", true);

								cu.findAll(MethodDeclaration.class).stream().forEachOrdered(r -> {
									if (r.getNameAsString().equals(method)) {
										System.out.println("\t\t\t\t" + j++ + " " + r.getNameAsString());
										FileOperations.writeFiles(new File("./trace.txt"),
												"\t\t\t\t" + j++ + " " + r.getNameAsString() + "\n", true);

										methods.put(r, m.getFullyQualifiedName().get().toString());
									}
								});
							}
						});
					});
				}
		} catch (Exception e) {

		}
		nodes = null;
		return methods;
	}

	public static Map<MethodDeclaration, String> findMethodsRandomly(List<CompilationUnit> allCus1,
			List<CompilationUnit> allCus, String method, String className) throws IOException {

		String line[] = className.split("\\.");

		j = 1;
		allCus1.forEach(cu -> {
			cu.findAll(ClassOrInterfaceDeclaration.class).stream().forEach(m -> {
				cu.findAll(MethodDeclaration.class).stream().forEachOrdered(r -> {
					if (r.getNameAsString().equals(method)) {
						System.out.println("\t\t\t" + m.getNameAsString());
						FileOperations.writeFiles(new File("./trace.txt"), "\t\t\t" + m.getNameAsString() + "\n", true);

						System.out.println("\t\t\t\t" + j++ + " " + r.getNameAsString());
						FileOperations.writeFiles(new File("./trace.txt"),
								"\t\t\t\t" + j++ + " " + r.getNameAsString() + "\n", true);

						methods.put(r, m.getFullyQualifiedName().get().toString());
					}
				});
			});
		});

		allCus.forEach(cu -> {
			cu.findAll(ClassOrInterfaceDeclaration.class).stream()
					.filter(c -> ((ClassOrInterfaceDeclaration) c).isInterface()).forEach(m -> {
						if (m.isInterface()) {
							cu.findAll(MethodDeclaration.class).stream().forEachOrdered(r -> {
								if (r.getNameAsString().equals(method)) {
									System.out.println("\t\t\t" + m.getNameAsString());
									FileOperations.writeFiles(new File("./trace.txt"),
											"\t\t\t" + m.getNameAsString() + "\n", true);

									System.out.println("\t\t\t\t" + j++ + " " + r.getNameAsString());
									FileOperations.writeFiles(new File("./trace.txt"),
											"\t\t\t\t" + j++ + " " + r.getNameAsString() + "\n", true);

									methods.put(r, m.getFullyQualifiedName().get().toString());
								}
							});
						}
					});
		});

		return methods;
	}

}
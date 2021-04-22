package utils;

import java.util.List;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

public class MatchFinders {

	MethodDeclaration method = null;
	ConstructorDeclaration cons = null;

	public MethodDeclaration findMethods(List<CompilationUnit> allCus, String className, int lineNumber) {
		
		String line = className.replace("\\", ".");
		String name[] = line.split("\\.");

		allCus.forEach(cu -> {
			cu.findAll(ClassOrInterfaceDeclaration.class).stream()
					.filter(c -> !((ClassOrInterfaceDeclaration) c).isInterface()
							&& !c.getClass().getName().contains("test")
							&& c.getFullyQualifiedName().toString().contains(name[(name.length)-2]))
					.forEach(x -> {
						x.findAll(MethodDeclaration.class).stream().forEach(m -> {
							int[] list = new int[2];
							list[0] = (int) m.getRange().map(r -> r.begin.line).orElse(-1);
							list[1] = (int) m.getRange().map(r -> r.end.line).orElse(-1);

							if (list[0] <= lineNumber && list[1] >= lineNumber) {
								method = m;
							}

						});
					});
		});
		return method;
	}

	public ConstructorDeclaration findConstructors(List<CompilationUnit> allCus, String className, int lineNumber) {

		allCus.forEach(cu -> {
			cu.findAll(ClassOrInterfaceDeclaration.class).stream()
					.filter(c -> !((ClassOrInterfaceDeclaration) c).isInterface()
							&& c.getFullyQualifiedName().toString().contains(className))
					.forEach(x -> {
						x.findAll(ConstructorDeclaration.class).stream().forEach(m -> {
							int[] list = new int[2];
							list[0] = (int) m.getRange().map(r -> r.begin.line).orElse(-1);
							list[1] = (int) m.getRange().map(r -> r.end.line).orElse(-1);

							if (list[0] <= lineNumber && list[1] >= lineNumber) {
								cons = m;
							}

						});
					});
		});
		return cons;
	}

}

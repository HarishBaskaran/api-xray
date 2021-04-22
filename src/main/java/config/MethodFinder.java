package config;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;

public class MethodFinder {

	int lineNumber = 0;
	String method = null;
	MethodDeclaration methodName = null;

	public String printMethodsAndLines(String javaFilepath, int lineNumber) {

		this.lineNumber = lineNumber;

		try {
			return methodIdentifier(new File(javaFilepath));
		} catch (FileNotFoundException e) {
			System.out.println(javaFilepath + " no such file found");
			return null;
		}

	}

	private String methodIdentifier(File f) throws FileNotFoundException {

		CompilationUnit compilationUnit = StaticJavaParser.parse(f);

		compilationUnit.findAll(MethodDeclaration.class).stream().forEach((x) -> {

			int[] list = new int[2];
			list[0] = (int) x.getRange().map(r -> r.begin.line).orElse(-1);
			list[1] = (int) x.getRange().map(r -> r.end.line).orElse(-1);
			String methodName = x.getName().asString();

			if (list[0] <= lineNumber && list[1] >= lineNumber) {
				method = methodName;
			}

		});

		return method;
	}

	public List<MethodDeclaration> printMethods(File javaFilepath, String method) throws FileNotFoundException {

		this.method = method;

		return methodIdentifier1(javaFilepath);

	}

	private List<MethodDeclaration> methodIdentifier1(File f) throws FileNotFoundException {
		
		List<MethodDeclaration> list = new ArrayList<MethodDeclaration>();

		CompilationUnit compilationUnit = StaticJavaParser.parse(f);

		compilationUnit.findAll(MethodDeclaration.class).stream().forEach((x) -> {

			if (x.getName().asString().equalsIgnoreCase(method)) {

				list.add(x);
			}
		});

		return list;
	}
}

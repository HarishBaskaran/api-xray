package utils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.github.javaparser.utils.SourceRoot;

public class Compilers {

	static List<CompilationUnit> allCus;

	public List<CompilationUnit> compile(File folder) throws IOException {
		
		File file = folder;

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
		
		return allCus;
	}

}

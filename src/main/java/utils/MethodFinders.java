package utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.symbolsolver.javaparser.Navigator;

public class MethodFinders {

	Map<MethodDeclaration, String> methods = new HashMap<MethodDeclaration, String>();
	boolean flag = false;
	int count = -1;
	String s = ("\n-------------------------------------------------\n");

	public Map<MethodDeclaration, String> findMethods(List<CompilationUnit> allCus, MethodDeclaration method,
			String className) {

		allCus.forEach(cu -> {
			cu.findAll(ClassOrInterfaceDeclaration.class).stream()
					.filter(c -> !((ClassOrInterfaceDeclaration) c).isInterface()
							&& !c.getFullyQualifiedName().get().contains("test")
							&& !c.getFullyQualifiedName().get().contains("IT"))
					.forEach(c -> {
//							System.out.println("--------------------------------------------------------------------------------------------------------------------------");
						cu.findAll(MethodDeclaration.class).stream().forEach(m -> {

							if (Navigator.findMethodCall(m, method.getName().toString()).isPresent()) {
//								System.out.println(m.getName().toString() + " " + c.getFullyQualifiedName().toString());

								m.getBody().get().getChildNodes().iterator().forEachRemaining(r -> {
//									System.out.println("\t" + r);
//									a.findAll(ExpressionStmt.class).stream().forEachOrdered(r -> {
//										System.out.println("\t\t" + a);
									r.findAll(MethodCallExpr.class).iterator().forEachRemaining(b -> {

//											System.out.println("\t\t\t" + b.getChildNodes());
//											System.out.print("\t");
										flag = false;
										count = -1;
										for (Node n : b.getChildNodes()) {
//												System.out.print(n.toString() + " - ");
											if (n.toString().equals(method.getNameAsString()) || flag) {
												if (n.toString().equals(method.getNameAsString()) && false == flag) {
													flag = true;
												}
												if (flag)
													count++;
											}
										}
//											System.out.println(count + " " + method.getParameters().size());
										if (method.getParameters().size() == count && flag) {
//											System.out.println("yes - " + c.getFullyQualifiedName().toString() + " - "
//													+ m.getNameAsString() + " - " + m.getBegin() + " - " + m.getEnd());
//											if (className.replace("\\", ".")
//													.contains(c.getFullyQualifiedName().get())) {
//												System.out.println("\t\t\t " + className.replace("\\", ".") + " " + c.getFullyQualifiedName().get());
//												methods.put(m, c.getFullyQualifiedName().get().toString());
//											}
											cu.findAll(ImportDeclaration.class).stream().forEach(i -> {
												if (className.replace("\\", ".").contains(i.getNameAsString()))
//														System.out.println("CLASSNAME - " + className);
													methods.put(m, c.getFullyQualifiedName().get().toString());

											});
										}

									});
								});
//								});
							}

						});
					});
		});

		return methods;
	}

	public Map<MethodDeclaration, String> findMethodsExp(List<CompilationUnit> allCus, MethodDeclaration method,
			String className) {

		allCus.forEach(cu -> {
			cu.findAll(ClassOrInterfaceDeclaration.class).stream()
					.filter(c -> !((ClassOrInterfaceDeclaration) c).isInterface()
							&& !c.getFullyQualifiedName().get().contains("test")
							&& !c.getFullyQualifiedName().get().contains("IT"))
					.forEach(c -> {
//							System.out.println("--------------------------------------------------------------------------------------------------------------------------");
						cu.findAll(MethodDeclaration.class).stream().forEach(m -> {

							if (Navigator.findMethodCall(m, method.getName().toString()).isPresent()) {
//								System.out.println(m.getName().toString() + " " + c.getFullyQualifiedName().toString());

								m.getBody().get().getChildNodes().iterator().forEachRemaining(a -> {
//									System.out.println("\t" + r);
									a.findAll(ExpressionStmt.class).stream().forEachOrdered(r -> {
//										System.out.println("\t\t" + a);
										r.findAll(MethodCallExpr.class).iterator().forEachRemaining(b -> {

//											System.out.println("\t\t\t" + b.getChildNodes());
//											System.out.print("\t");
											flag = false;
											count = -1;
											for (Node n : b.getChildNodes()) {
//												System.out.print(n.toString() + " - ");
												if (n.toString().equals(method.getNameAsString()) || flag) {
													if (n.toString().equals(method.getNameAsString())
															&& false == flag) {
														flag = true;
													}
													if (flag)
														count++;
												}
											}
//											System.out.println(count + " " + method.getParameters().size());
											if (method.getParameters().size() == count && flag) {
//											System.out.println("yes - " + c.getFullyQualifiedName().toString() + " - "
//													+ m.getNameAsString() + " - " + m.getBegin() + " - " + m.getEnd());
												if (className.replace("\\", ".")
														.contains(c.getFullyQualifiedName().get())) {
//													System.out.println("\t\t\t " + className.replace("\\", ".") + " "
//															+ c.getFullyQualifiedName().get());
													methods.put(m, c.getFullyQualifiedName().get().toString());
												}
												cu.findAll(ImportDeclaration.class).stream().forEach(i -> {
													if (className.replace("\\", ".").contains(i.getNameAsString()))
//														System.out.println("CLASSNAME - " + className);
														methods.put(m, c.getFullyQualifiedName().get().toString());

												});
											}

										});
									});
								});
							}

						});
					});
		});

		return methods;
	}
	public Map<MethodDeclaration, String> findMethodsRet(List<CompilationUnit> allCus, MethodDeclaration method,
			String className) {

		allCus.forEach(cu -> {
			cu.findAll(ClassOrInterfaceDeclaration.class).stream()
					.filter(c -> !((ClassOrInterfaceDeclaration) c).isInterface()
							&& !c.getFullyQualifiedName().get().contains("test")
							&& !c.getFullyQualifiedName().get().contains("IT"))
					.forEach(c -> {
//							System.out.println("--------------------------------------------------------------------------------------------------------------------------");
						cu.findAll(MethodDeclaration.class).stream().forEach(m -> {

							if (Navigator.findMethodCall(m, method.getName().toString()).isPresent()) {
//								System.out.println(m.getName().toString() + " " + c.getFullyQualifiedName().toString());

								m.getBody().get().getChildNodes().iterator().forEachRemaining(a -> {
//									System.out.println("\t" + r);
									a.findAll(ReturnStmt.class).stream().forEachOrdered(r -> {
//										System.out.println("\t\t" + a);
										r.findAll(MethodCallExpr.class).iterator().forEachRemaining(b -> {

//											System.out.println("\t\t\t" + b.getChildNodes());
//											System.out.print("\t");
											flag = false;
											count = -1;
											for (Node n : b.getChildNodes()) {
//												System.out.print(n.toString() + " - ");
												if (n.toString().equals(method.getNameAsString()) || flag) {
													if (n.toString().equals(method.getNameAsString())
															&& false == flag) {
														flag = true;
													}
													if (flag)
														count++;
												}
											}
//											System.out.println(count + " " + method.getParameters().size());
											if (method.getParameters().size() == count && flag) {
//											System.out.println("yes - " + c.getFullyQualifiedName().toString() + " - "
//													+ m.getNameAsString() + " - " + m.getBegin() + " - " + m.getEnd());
												if (className.replace("\\", ".")
														.contains(c.getFullyQualifiedName().get())) {
//													System.out.println("\t\t\t " + className.replace("\\", ".") + " "
//															+ c.getFullyQualifiedName().get());
													methods.put(m, c.getFullyQualifiedName().get().toString());
												}
												cu.findAll(ImportDeclaration.class).stream().forEach(i -> {
													if (className.replace("\\", ".").contains(i.getNameAsString()))
//														System.out.println("CLASSNAME - " + className);
														methods.put(m, c.getFullyQualifiedName().get().toString());

												});
											}

										});
									});
								});
							}

						});
					});
		});

		return methods;
	}

}

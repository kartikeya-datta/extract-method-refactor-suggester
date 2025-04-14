error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9495.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9495.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9495.java
text:
```scala
S@@tring result = this.newCodeFormatter.format(source, positions, constructorDeclaration);

/*******************************************************************************
 * Copyright (c) 2003 International Business Machines Corp. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0 
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/
package org.eclipse.jdt.internal.formatter;

import java.util.Locale;
import java.util.Map;

import org.eclipse.jdt.core.CodeFormatter;
import org.eclipse.jdt.core.ICodeFormatter;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.DefaultErrorHandlingPolicies;
import org.eclipse.jdt.internal.compiler.ast.AstNode;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.ast.ConstructorDeclaration;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.batch.CompilationUnit;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.problem.DefaultProblemFactory;
import org.eclipse.jdt.internal.compiler.problem.ProblemReporter;

public class DefaultCodeFormatter extends CodeFormatter implements ICodeFormatter {
	
	private int[] positionsMapping;

	public static final boolean DEBUG = false;
	
	private CodeFormatterVisitor newCodeFormatter;
	private FormattingPreferences preferences;

	private static AstNode[] parseClassBodyDeclarations(char[] source, Map settings) {
		
		if (source == null) {
			throw new IllegalArgumentException();
		}
		CompilerOptions compilerOptions = new CompilerOptions(settings);
		final ProblemReporter problemReporter = new ProblemReporter(
					DefaultErrorHandlingPolicies.proceedWithAllProblems(), 
					compilerOptions, 
					new DefaultProblemFactory(Locale.getDefault()));
					
		CodeFormatterParser parser =
			new CodeFormatterParser(problemReporter, false);

		ICompilationUnit sourceUnit = 
			new CompilationUnit(
				source, 
				"", //$NON-NLS-1$
				compilerOptions.defaultEncoding);

		return parser.parseClassBodyDeclarations(source, new CompilationUnitDeclaration(problemReporter, new CompilationResult(sourceUnit, 0, 0, compilerOptions.maxProblemsPerUnit), source.length));
	}

	private static CompilationUnitDeclaration parseCompilationUnit(char[] source, Map settings) {
		
		if (source == null) {
			throw new IllegalArgumentException();
		}
		CompilerOptions compilerOptions = new CompilerOptions(settings);
		CodeFormatterParser parser =
			new CodeFormatterParser(
				new ProblemReporter(
					DefaultErrorHandlingPolicies.proceedWithAllProblems(), 
					compilerOptions, 
					new DefaultProblemFactory(Locale.getDefault())),
			false);
		ICompilationUnit sourceUnit = 
			new CompilationUnit(
				source, 
				"", //$NON-NLS-1$
				compilerOptions.defaultEncoding);
		CompilationUnitDeclaration compilationUnitDeclaration = parser.dietParse(sourceUnit, new CompilationResult(sourceUnit, 0, 0, compilerOptions.maxProblemsPerUnit));
		
		if (compilationUnitDeclaration.ignoreMethodBodies) {
			compilationUnitDeclaration.ignoreFurtherInvestigation = true;
			// if initial diet parse did not work, no need to dig into method bodies.
			return compilationUnitDeclaration; 
		}
		
		//fill the methods bodies in order for the code to be generated
		//real parse of the method....
		parser.scanner.setSource(source);
		org.eclipse.jdt.internal.compiler.ast.TypeDeclaration[] types = compilationUnitDeclaration.types;
		if (types != null) {
			for (int i = types.length; --i >= 0;) {
				types[i].parseMethod(parser, compilationUnitDeclaration);
			}
		}
		return compilationUnitDeclaration;
	}

	private static Expression parseExpression(char[] source, Map settings) {
		
		if (source == null) {
			throw new IllegalArgumentException();
		}
		CompilerOptions compilerOptions = new CompilerOptions(settings);
		final ProblemReporter problemReporter = new ProblemReporter(
					DefaultErrorHandlingPolicies.proceedWithAllProblems(), 
					compilerOptions, 
					new DefaultProblemFactory(Locale.getDefault()));
					
		CodeFormatterParser parser =
			new CodeFormatterParser(problemReporter, false);

		ICompilationUnit sourceUnit = 
			new CompilationUnit(
				source, 
				"", //$NON-NLS-1$
				compilerOptions.defaultEncoding);

		return parser.parseExpression(source, new CompilationUnitDeclaration(problemReporter, new CompilationResult(sourceUnit, 0, 0, compilerOptions.maxProblemsPerUnit), source.length));
	}

	private static ConstructorDeclaration parseStatements(char[] source, Map settings) {
		
		if (source == null) {
			throw new IllegalArgumentException();
		}
		CompilerOptions compilerOptions = new CompilerOptions(settings);
		final ProblemReporter problemReporter = new ProblemReporter(
					DefaultErrorHandlingPolicies.proceedWithAllProblems(), 
					compilerOptions, 
					new DefaultProblemFactory(Locale.getDefault()));
		CodeFormatterParser parser = new CodeFormatterParser(problemReporter, false);
		ICompilationUnit sourceUnit = 
			new CompilationUnit(
				source, 
				"", //$NON-NLS-1$
				compilerOptions.defaultEncoding);

		final CompilationResult compilationResult = new CompilationResult(sourceUnit, 0, 0, compilerOptions.maxProblemsPerUnit);
		CompilationUnitDeclaration compilationUnitDeclaration = new CompilationUnitDeclaration(problemReporter, compilationResult, source.length);		

		ConstructorDeclaration constructorDeclaration = new ConstructorDeclaration(compilationResult);
		constructorDeclaration.sourceEnd  = -1;
		constructorDeclaration.declarationSourceEnd = source.length + 1;
		
		parser.scanner.setSource(CharOperation.concat('{', source, '}'));
		parser.parse(constructorDeclaration, compilationUnitDeclaration);
		
		return constructorDeclaration;
	}
	
	public DefaultCodeFormatter() {
		this.preferences = FormattingPreferences.getDefault();
	}
	
	public DefaultCodeFormatter(FormattingPreferences preferences) {
		this.preferences = preferences;
	}

	/**
	 * @see CodeFormatter#format(String, int, int[], String, int)
	 */
	public String format(
			int kind,
			String source,
			int indentationLevel,
			int[] positions,
			String lineSeparator,
			Map options) {
				
		switch(kind) {
			case K_CLASS_BODY_DECLARATIONS :
				return formatClassBodyDeclarations(source, indentationLevel, positions, lineSeparator, options);
			case K_COMPILATION_UNIT :
				return formatCompilationUnit(source, indentationLevel, positions, lineSeparator, options);
			case K_EXPRESSION :
				return formatExpression(source, indentationLevel, positions, lineSeparator, options);
			case K_STATEMENTS :
				return formatStatements(source, indentationLevel, positions, lineSeparator, options);
		}
		this.positionsMapping = positions;
		return source;
	}
	
	public String format(
		String string,
		int start,
		int end,
		int indentationLevel,
		int[] positions,
		String lineSeparator) {
		// TODO: Auto-generated method stub
		return null;
	}
	
	/**
	 * @see org.eclipse.jdt.core.ICodeFormatter#format(String, int, int[], String)
	 */
	public String format(
		String source,
		int indentationLevel,
		int[] positions,
		String lineSeparator) {
			return format(K_COMPILATION_UNIT, source, indentationLevel, positions, lineSeparator, JavaCore.getOptions());
	}

	private String formatClassBodyDeclarations(String source, int indentationLevel, int[] positions, String lineSeparator, Map options) {
		AstNode[] bodyDeclarations = parseClassBodyDeclarations(source.toCharArray(), options);
		
		this.preferences.line_delimiter = lineSeparator;
		this.preferences.initial_indentation_level = indentationLevel;

		this.newCodeFormatter = new CodeFormatterVisitor(this.preferences, options);
		
		String result = this.newCodeFormatter.format(source, positions, bodyDeclarations);
		if (positions != null) {
			System.arraycopy(this.newCodeFormatter.scribe.mappedPositions, 0, positions, 0, positions.length);
		}
		return result;
	}

	private String formatCompilationUnit(String source, int indentationLevel, int[] positions, String lineSeparator, Map options) {
		CompilationUnitDeclaration compilationUnitDeclaration = parseCompilationUnit(source.toCharArray(), options);
		
		this.preferences.line_delimiter = lineSeparator;
		this.preferences.initial_indentation_level = indentationLevel;

		this.newCodeFormatter = new CodeFormatterVisitor(this.preferences, options);
		
		String result = this.newCodeFormatter.format(source, positions, compilationUnitDeclaration);
		if (positions != null) {
			System.arraycopy(this.newCodeFormatter.scribe.mappedPositions, 0, positions, 0, positions.length);
		}
		return result;
	}

	private String formatExpression(String source, int indentationLevel, int[] positions, String lineSeparator, Map options) {
		Expression expression = parseExpression(source.toCharArray(), options);
		
		this.preferences.line_delimiter = lineSeparator;
		this.preferences.initial_indentation_level = indentationLevel;

		this.newCodeFormatter = new CodeFormatterVisitor(this.preferences, options);
		
		String result = this.newCodeFormatter.format(source, positions, expression);
		if (positions != null) {
			System.arraycopy(this.newCodeFormatter.scribe.mappedPositions, 0, positions, 0, positions.length);
		}
		return result;
	}

	private String formatStatements(String source, int indentationLevel, int[] positions, String lineSeparator, Map options) {
		ConstructorDeclaration constructorDeclaration = parseStatements(source.toCharArray(), options);
		
		this.preferences.line_delimiter = lineSeparator;
		this.preferences.initial_indentation_level = indentationLevel;

		this.newCodeFormatter = new CodeFormatterVisitor(this.preferences, options);
		
		String result = this.newCodeFormatter.format(source, positions, constructorDeclaration);;
		if (positions != null) {
			System.arraycopy(this.newCodeFormatter.scribe.mappedPositions, 0, positions, 0, positions.length);
		}
		return result;
	}
	
	public String getDebugOutput() {
		return this.newCodeFormatter.scribe.toString();
	}
	
	public int[] getMappedPositions() {
		return this.positionsMapping;
	}
}
```

```



#### Error stacktrace:

```
com.thoughtworks.qdox.parser.impl.Parser.yyerror(Parser.java:2025)
	com.thoughtworks.qdox.parser.impl.Parser.yyparse(Parser.java:2147)
	com.thoughtworks.qdox.parser.impl.Parser.parse(Parser.java:2006)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:232)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:190)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:94)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:89)
	com.thoughtworks.qdox.library.SortedClassLibraryBuilder.addSource(SortedClassLibraryBuilder.java:162)
	com.thoughtworks.qdox.JavaProjectBuilder.addSource(JavaProjectBuilder.java:174)
	scala.meta.internal.mtags.JavaMtags.indexRoot(JavaMtags.scala:48)
	scala.meta.internal.metals.SemanticdbDefinition$.foreachWithReturnMtags(SemanticdbDefinition.scala:97)
	scala.meta.internal.metals.Indexer.indexSourceFile(Indexer.scala:489)
	scala.meta.internal.metals.Indexer.$anonfun$indexWorkspaceSources$7(Indexer.scala:361)
	scala.meta.internal.metals.Indexer.$anonfun$indexWorkspaceSources$7$adapted(Indexer.scala:356)
	scala.collection.IterableOnceOps.foreach(IterableOnce.scala:619)
	scala.collection.IterableOnceOps.foreach$(IterableOnce.scala:617)
	scala.collection.AbstractIterator.foreach(Iterator.scala:1306)
	scala.collection.parallel.ParIterableLike$Foreach.leaf(ParIterableLike.scala:938)
	scala.collection.parallel.Task.$anonfun$tryLeaf$1(Tasks.scala:52)
	scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
	scala.util.control.Breaks$$anon$1.catchBreak(Breaks.scala:97)
	scala.collection.parallel.Task.tryLeaf(Tasks.scala:55)
	scala.collection.parallel.Task.tryLeaf$(Tasks.scala:49)
	scala.collection.parallel.ParIterableLike$Foreach.tryLeaf(ParIterableLike.scala:935)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:169)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal$(Tasks.scala:156)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.internal(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:149)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute$(Tasks.scala:148)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.compute(Tasks.scala:304)
	java.base/java.util.concurrent.RecursiveAction.exec(RecursiveAction.java:194)
	java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:373)
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9495.java
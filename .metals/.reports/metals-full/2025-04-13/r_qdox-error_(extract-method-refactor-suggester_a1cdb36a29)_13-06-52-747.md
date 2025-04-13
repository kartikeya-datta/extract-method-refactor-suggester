error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/174.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/174.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/174.java
text:
```scala
i@@f (p == Partition.EXPRESSION || p == Partition.TYPE_DECLARATION) {

/*******************************************************************************
 * Copyright (c) 2005, 2007 committers of openArchitectureWare and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     committers of openArchitectureWare - initial API and implementation
 *******************************************************************************/

package org.eclipse.internal.xtend.xtend.codeassist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.internal.xtend.expression.codeassist.LazyVar;
import org.eclipse.internal.xtend.xtend.XtendFile;
import org.eclipse.internal.xtend.xtend.ast.Around;
import org.eclipse.internal.xtend.xtend.ast.Extension;
import org.eclipse.internal.xtend.xtend.types.AdviceContextType;
import org.eclipse.xtend.expression.AnalysationIssue;
import org.eclipse.xtend.expression.ExecutionContext;
import org.eclipse.xtend.expression.ExpressionFacade;
import org.eclipse.xtend.expression.ResourceManager;
import org.eclipse.xtend.expression.Variable;
import org.eclipse.xtend.typesystem.ParameterizedType;
import org.eclipse.xtend.typesystem.Type;

public class FastAnalyzer {

	private static final Pattern ENDS_WITH_SINGLE_LINE_COMMENT_PATTERN = Pattern.compile("//.*$");

	private static final Pattern CONTAINS_SINGLE_LINE_COMMENT_PATTERN = Pattern.compile("//.*$", Pattern.MULTILINE);
	
	private static final Pattern BEGIN_MULTI_LINE_COMMENT_PATTERN = Pattern.compile("/\\*", Pattern.MULTILINE);

	private static final Pattern COMPLETE_MULTI_LINE_COMMENT_PATTERN = Pattern.compile("/\\*.*\\*/", Pattern.MULTILINE | Pattern.DOTALL);
		
	private static final Pattern PARAM_PATTERN = Pattern
			.compile("([\\[\\]:\\w]+)\\s+([\\w]+)");

	private final static Pattern IMPORT_PATTERN = Pattern
			.compile("import\\s+([\\w\\:]+)\\s*;");

	private final static Pattern INCOMPLETE_IMPORT_PATTERN = Pattern
			.compile("import\\s+[\\w\\:]*\\z");

	private final static Pattern EXTENSION_PATTERN = Pattern
			.compile("extension\\s+([\\w\\:]+)\\s*(reexport)?\\s*;");

	private final static Pattern INCOMPLETE_EXTENSION_PATTERN = Pattern
			.compile("extension\\s+[\\w\\:]*\\z");

	private final static Pattern FUNCTION_PATTERN = Pattern
			.compile("((\\w+)\\s+)?(create\\s+([\\w:]+)(\\s+(\\w+))?\\s+)?([\\w:]+)\\s*\\(\\s*([\\[\\]:\\w\\s\\,]+)?\\s*\\)\\s*:\\s*[^;]*\\z");

	private final static Pattern AROUND_PATTERN = Pattern
			.compile("around\\s+([\\w:*]+)\\s*\\(\\s*([\\[\\]:\\w\\s\\,]+)?[\\s,*]*\\)\\s*:\\s*[^;]*\\z");

	private final static Pattern TYPEDECL_PATTERN = Pattern
			.compile("(;|\\A)\\s*\\w+(\\s*\\[\\w+\\]*)?\\s*\\w+\\s*\\(([\\[\\]:\\w\\s,]*)\\z");

	private final static Pattern TYPEDECL_PARAM_PATTERN = Pattern
			.compile("(,|\\(|\\A)\\s*[\\[\\]:\\w]*\\z");

	private FastAnalyzer() {
	}

	public static boolean isInsideTypeDeclaration(final String s) {
		final Matcher m = TYPEDECL_PATTERN.matcher(s);
		if (m.find())
			return TYPEDECL_PARAM_PATTERN.matcher(m.group(3)).find();
		return false;
	}

	public static boolean isInsideExtensionImport(final String s) {
		final Matcher m = INCOMPLETE_EXTENSION_PATTERN.matcher(s);
		return m.find();
	}

	public static boolean isInsideImport(final String s) {
		final Matcher m = INCOMPLETE_IMPORT_PATTERN.matcher(s);
		return m.find();
	}

	public static boolean isInsideExpression(final String s) {
		final Matcher m1 = AROUND_PATTERN.matcher(s);
		if (!m1.find()) {
			final Matcher m = FUNCTION_PATTERN.matcher(s);
			return m.find();
		}
		return true;
	}

	public static boolean isInsideComment(final String input) {
		final Matcher singleLineCommentMatcher = ENDS_WITH_SINGLE_LINE_COMMENT_PATTERN.matcher(input);
		if(singleLineCommentMatcher.find()) {
			return true;
		}
		final Matcher removeSingleLineCommentsMatcher = CONTAINS_SINGLE_LINE_COMMENT_PATTERN.matcher(input);
		String inputWithoutSingleLineComments = removeSingleLineCommentsMatcher.replaceAll("\n");
		final Matcher beinMultiLineCommentMatcher = BEGIN_MULTI_LINE_COMMENT_PATTERN.matcher(inputWithoutSingleLineComments);
		if(beinMultiLineCommentMatcher.find()) {
			int lastBeginMultiLineComment = -1;
			do {
				lastBeginMultiLineComment = beinMultiLineCommentMatcher.start();
			} while (beinMultiLineCommentMatcher.find());
			final Matcher completeMultiLineCommentMatcher = COMPLETE_MULTI_LINE_COMMENT_PATTERN.matcher(inputWithoutSingleLineComments);
			// if completeMultiLineComment does not match at the last beginMultiLineComment position,
			// we're inside a multiline comment
			return !completeMultiLineCommentMatcher.find(lastBeginMultiLineComment);
		}
		return false;
	}
	
	public final static List<String> findImports(final String template) {
		final Matcher m = IMPORT_PATTERN.matcher(template);
		final List<String> result = new ArrayList<String>();
		while (m.find()) {
			result.add(m.group(1));
		}
		return result;
	}

	public final static List<String> findExtensions(final String template) {
		final Matcher m = EXTENSION_PATTERN.matcher(template);
		final List<String> result = new ArrayList<String>();
		while (m.find()) {
			result.add(m.group(1));
		}
		return result;
	}

	public final static Stack<Set<LazyVar>> computeStack(String toAnalyze) {
		Matcher m = AROUND_PATTERN.matcher(toAnalyze);
		Pattern p = AROUND_PATTERN;
		final Set<LazyVar> vars = new HashSet<LazyVar>();
		if (!m.find()) {
			m = FUNCTION_PATTERN.matcher(toAnalyze);
			p = FUNCTION_PATTERN;
			if (m.find()) {
				final int start = m.start();
				toAnalyze = toAnalyze.substring(start);
				m = p.matcher(toAnalyze);
				m.find();
				if (m.group(4) != null) {
					final LazyVar v = new LazyVar();
					v.typeName = m.group(4);
					v.name = m.group(6);
					if (v.name == null)
						v.name = "this";
					vars.add(v);
				}
				fillParams(vars, m.group(8));
			}
		} else {
			fillParams(vars, m.group(2));
			final LazyVar v = new LazyVar();
			v.typeName = AdviceContextType.TYPE_NAME;
			v.name = Around.CONTEXT_PARAM_NAME;
			vars.add(v);
		}
		final Stack<Set<LazyVar>> stack = new Stack<Set<LazyVar>>();
		stack.push(vars);

		return stack;
	}

	private static void fillParams(final Set<LazyVar> vars, final String params) {
		Matcher m;
		if (params != null && !"".equals(params.trim())) {
			final StringTokenizer st = new StringTokenizer(params, ",");
			while (st.hasMoreTokens()) {
				final String param = st.nextToken();
				m = PARAM_PATTERN.matcher(param);
				if (m.find()) {
					final LazyVar v = new LazyVar();
					v.typeName = m.group(1);
					v.name = m.group(2);
					vars.add(v);
				}
			}
		}
	}

	public final static Partition computePartition(final String str) {
		if (isInsideComment(str))
			return Partition.COMMENT;
		
		if (isInsideImport(str))
			return Partition.NAMESPACE_IMPORT;

		if (isInsideExtensionImport(str))
			return Partition.EXTENSION_IMPORT;

		if (isInsideTypeDeclaration(str))
			return Partition.TYPE_DECLARATION;

		if (isInsideExpression(str))
			return Partition.EXPRESSION;

		return Partition.DEFAULT;
	}

	public final static ExecutionContext computeExecutionContext(
			final String str, ExecutionContext ctx,
			final List<Extension> extensions) {
		final Partition p = computePartition(str);
		if (p == Partition.EXPRESSION || p == Partition.TYPE_DECLARATION || p == Partition.DEFAULT) {

			final List<String> imports = findImports(str);
			final List<String> extensionImports = findExtensions(str);
			final XtendFile tpl = new XtendFile() {

				private String fqn;

				public String getFullyQualifiedName() {
					return fqn;
				}

				public void setFullyQualifiedName(String fqn) {
					this.fqn = fqn;
				}

				public String[] getImportedNamespaces() {
					return imports.toArray(new String[imports.size()]);
				}

				public String[] getImportedExtensions() {
					return extensionImports.toArray(new String[extensionImports
							.size()]);
				}

				public List<Extension> getExtensions() {
					return extensions;
				}

				public void analyze(ExecutionContext ctx,
						Set<AnalysationIssue> issues) {
					// TODO Auto-generated method stub

				}

				public List<Extension> getPublicExtensions(ResourceManager rm, ExecutionContext ctx) {
					return extensions;
				}

				public List<Extension> getPublicExtensions(
						ResourceManager resourceManager,ExecutionContext ctx,
						Set<String> flowoverCache) {
					return extensions;
				}

				public List<Around> getArounds() {
					return Collections.emptyList();
				}

			};

			ctx = ctx.cloneWithResource(tpl);
			final Stack<Set<LazyVar>> s = computeStack(str);

			for (final Iterator<Set<LazyVar>> iter = s.iterator(); iter
					.hasNext();) {
				final Set<LazyVar> vars = iter.next();
				for (final Iterator<LazyVar> iterator = vars.iterator(); iterator
						.hasNext();) {
					final LazyVar v = iterator.next();
					Type vType = null;
					if (v.typeName != null) {
						vType = ctx.getTypeForName(v.typeName);
					} else {
						vType = new ExpressionFacade(ctx).analyze(v.expression,
								new HashSet<AnalysationIssue>());
						if (v.forEach) {
							if (vType instanceof ParameterizedType) {
								vType = ((ParameterizedType) vType)
										.getInnerType();
							} else {
								vType = null;
							}
						}
					}
					ctx = ctx.cloneWithVariable(new Variable(v.name, vType));
				}
			}
		}
		return ctx;

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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/174.java
error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14508.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14508.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14508.java
text:
```scala
i@@f (!(o instanceof Collection<?>))

/*******************************************************************************
 * Copyright (c) 2005, 2009 committers of openArchitectureWare and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     committers of openArchitectureWare - initial API and implementation
 *******************************************************************************/

package org.eclipse.internal.xpand2.ast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.internal.xpand2.type.IteratorType;
import org.eclipse.internal.xpand2.type.XpandIterator;
import org.eclipse.internal.xtend.expression.ast.Expression;
import org.eclipse.internal.xtend.expression.ast.Identifier;
import org.eclipse.xpand2.XpandExecutionContext;
import org.eclipse.xtend.expression.AnalysationIssue;
import org.eclipse.xtend.expression.EvaluationException;
import org.eclipse.xtend.expression.ExecutionContext;
import org.eclipse.xtend.expression.Variable;
import org.eclipse.xtend.typesystem.ParameterizedType;
import org.eclipse.xtend.typesystem.Type;

/**
 * *
 * 
 * @author Sven Efftinge (http://www.efftinge.de) *
 */
public class ForEachStatement extends StatementWithBody {

	private Expression target;

	private Expression separator;

	private Identifier variable;

	private Identifier iteratorName;

	public ForEachStatement(final Identifier variable, final Expression target,
			final Statement[] body, final Expression separator, final Identifier iterator) {
		super(body);
		this.variable = variable;
		this.target = target;
		this.separator = separator;
		iteratorName = iterator;
	}

	public Expression getSeparator() {
		return separator;
	}

	public Expression getTarget() {
		return target;
	}

	public Identifier getVariable() {
		return variable;
	}

	public Identifier getIteratorName() {
		return iteratorName;
	}

	@Override
	public void analyzeInternal(XpandExecutionContext ctx, final Set<AnalysationIssue> issues) {
		Type t = getTarget().analyze(ctx, issues);
		if (getSeparator() != null) {
			final Type sepT = getSeparator().analyze(ctx, issues);
			if (!ctx.getStringType().isAssignableFrom(sepT)) {
				issues.add(new AnalysationIssue(AnalysationIssue.INCOMPATIBLE_TYPES, "String expected!", target));
			}
		}
		if (t != null) {
			if (ctx.getCollectionType(ctx.getObjectType()).isAssignableFrom(t)) {
				if (t instanceof ParameterizedType) {
					t = ((ParameterizedType) t).getInnerType();
				} else {
					t = ctx.getObjectType();
				}
			} else {
				issues.add(new AnalysationIssue(AnalysationIssue.INCOMPATIBLE_TYPES, "Collection type expected!", target));
				return;
			}
		}
		ctx = (XpandExecutionContext) ctx.cloneWithVariable(new Variable(getVariable().getValue(), t));
		if (iteratorName != null) {
			ctx = (XpandExecutionContext) ctx.cloneWithVariable(new Variable(iteratorName.getValue(), ctx.getTypeForName(IteratorType.TYPE_NAME)));
		}
		for (int i = 0; i < getBody().length; i++) {
			getBody()[i].analyze(ctx, issues);
		}
	}

	@Override
	public void evaluateInternal(XpandExecutionContext ctx) {
		Object o = getTarget().evaluate(ctx);
		if (o == null)
			o = new ArrayList<Object>();

		if (!(o instanceof Collection))
			throw new EvaluationException("Collection expected!", getTarget(), ctx);
		final Collection<?> col = (Collection<?>) o;
		final String sep = (String) (getSeparator() != null ? getSeparator().evaluate(ctx) : null);
		final XpandIterator iterator = new XpandIterator(col.size());

		if (iteratorName != null) {
			ctx = (XpandExecutionContext) ctx.cloneWithVariable(new Variable(iteratorName.getValue(), iterator));
		}
		for (final Iterator<?> iter = col.iterator(); iter.hasNext();) {
			final Object element = iter.next();
			ctx = (XpandExecutionContext) ctx.cloneWithVariable(new Variable(getVariable().getValue(), element));
			ctx.preTask(this);
			for (int i = 0; i < getBody().length; i++) {
				getBody()[i].evaluate(ctx);
			}
			ctx.postTask(this);
			if (sep != null && iter.hasNext()) {
				ctx.getOutput().write(sep);
			}
			iterator.increment();
		}
	}

	@Override
	public String getNameString(ExecutionContext context) {
		return "FOREACH";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14508.java
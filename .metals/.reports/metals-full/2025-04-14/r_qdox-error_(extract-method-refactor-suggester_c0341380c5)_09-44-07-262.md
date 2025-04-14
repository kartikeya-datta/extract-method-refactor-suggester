error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2435.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2435.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,17]

error in qdox parser
file content:
```java
offset: 17
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2435.java
text:
```scala
protected final L@@ist<GlobalVarDef> globalVarDefs = new ArrayList<GlobalVarDef>();

/*******************************************************************************
 * Copyright (c) 2005, 2006 committers of openArchitectureWare and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     committers of openArchitectureWare - initial API and implementation
 *******************************************************************************/
package org.eclipse.xtend.expression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.emf.mwe.core.WorkflowContext;
import org.eclipse.emf.mwe.core.WorkflowInterruptedException;
import org.eclipse.emf.mwe.core.issues.Issues;
import org.eclipse.emf.mwe.core.lib.AbstractWorkflowComponent;
import org.eclipse.emf.mwe.core.monitor.ProgressMonitor;
import org.eclipse.internal.xtend.expression.ast.SyntaxElement;
import org.eclipse.internal.xtend.util.Pair;
import org.eclipse.xtend.typesystem.MetaModel;

public abstract class AbstractExpressionsUsingWorkflowComponent extends
		AbstractWorkflowComponent {
	protected final Log log = LogFactory.getLog(getClass());

	protected final List<MetaModel> metaModels = new ArrayList<MetaModel>();

	private List<GlobalVarDef> globalVarDefs = new ArrayList<GlobalVarDef>();

	public void addMetaModel(final MetaModel metaModel) {
		assert metaModel != null;
		metaModels.add(metaModel);
	}

	public static class GlobalVarDef {
		private String name;

		private String value;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}

	public void addGlobalVarDef(GlobalVarDef def) {
		globalVarDefs.add(def);
	}

	protected Map<String, Variable> getGlobalVars(WorkflowContext ctx) {
		final Map<String, Variable> result = new HashMap<String, Variable>();

		ExecutionContextImpl ec = new ExecutionContextImpl();
		for (String slot : ctx.getSlotNames()) {
			ec = (ExecutionContextImpl) ec.cloneWithVariable(new Variable(slot,
					ctx.get(slot)));
		}
		for (MetaModel mm : metaModels) {
			ec.registerMetaModel(mm);
		}

		final ExpressionFacade ef = new ExpressionFacade(ec);
		for (GlobalVarDef def : globalVarDefs) {
			final Object value = ef.evaluate(def.getValue());
			result.put(def.getName(), new Variable(def.getName(), value));
		}

		return result;
	}

	protected ExecutionContextImpl getExecutionContext(final WorkflowContext ctx) {
		final ExecutionContextImpl executionContext = new ExecutionContextImpl(
				new ResourceManagerDefaultImpl(), null, new TypeSystemImpl(),
				new HashMap<String, Variable>(), getGlobalVars(ctx), null,
				null, null, getNullEvaluationHandler(),null);
		for (MetaModel mm : metaModels) {
			executionContext.registerMetaModel(mm);
		}
		return executionContext;
	}

	public NullEvaluationHandler getNullEvaluationHandler() {
		if (exceptionsOnNullEvaluation)
			return new ExceptionRaisingNullEvaluationHandler();
		return null;
	}

	public void checkConfiguration(Issues issues) {
		if (metaModels.isEmpty()) {
			issues
					.addWarning("no metamodels specified (use 'metaModel' property)!");
		}
	}

	private List<Debug> debugExpressions = new ArrayList<Debug>();

	public void addDebug(Debug expr) {
		this.debugExpressions.add(expr);
	}

	private boolean dumpContext = false;

	public void setDumpContext(boolean dumpContext) {
		this.dumpContext = dumpContext;
	}

	public static class Debug {
		private String expression;

		private boolean dumpContext = false;

		public void setDumpContext(boolean dumpContext) {
			this.dumpContext = dumpContext;
		}

		public boolean isDumpContext() {
			return dumpContext;
		}

		public void setExpression(String expression) {
			this.expression = expression;
		}

		public String getExpression() {
			return expression;
		}
	}

	@Override
	protected void invokeInternal(WorkflowContext ctx, ProgressMonitor monitor,
			Issues issues) {
		try {
			invokeInternal2(ctx, monitor, issues);
		} catch (EvaluationException e) {
			log.error("Error in Component" + (getId()==null?" ":" "+getId()) + " of type "
					+ getClass().getName() + ": \n\t" +
							"" +toString(e, debugExpressions));
			throw new WorkflowInterruptedException();
		}
	}

	public String toString(EvaluationException ex, List<Debug> debugEntries) {
		StringBuffer result = new StringBuffer("EvaluationException : "
				+ ex.getMessage() + "\n");
		int widest = 0;
		for (Pair<SyntaxElement, ExecutionContext> ele : ex.getXtendStackTrace()) {
			int temp = EvaluationException.getLocationString(ele.getFirst())
					.length();
			if (temp > widest)
				widest = temp;
		}
		String indent = "";
		for (int l = 0; l < widest + 7; l++)
			indent += " ";

		for (int i = 0, x = ex.getXtendStackTrace().size(); i < x; i++) {
			Pair<SyntaxElement, ExecutionContext> ele = ex.getXtendStackTrace()
					.get(i);
			StringBuffer msg = new StringBuffer(EvaluationException
					.getLocationString(ele.getFirst()));
			for (int j = msg.length(); j < widest; j++) {
				msg.append(" ");
			}
			if (debugEntries.size() > i
					&& debugEntries.get(i).getExpression() != null) {
				Debug d = debugEntries.get(i);
				try {
					msg.append(" -- debug '").append(d.getExpression()).append(
							"' = ");
					msg.append(new ExpressionFacade(ele.getSecond())
							.evaluate("let x = " + d.getExpression()
									+ " : x!=null ? x.toString() : 'null'"));
				} catch (Exception e) {
					msg.append("Exception : ").append(e.getMessage());
				}
				msg.append("\n");
			}
			if (dumpContext || debugEntries.size() > i
					&& debugEntries.get(i).isDumpContext()) {
				ExpressionFacade f = new ExpressionFacade(ele.getSecond());
				msg.append(" -- context dump : ");

				Iterator<String> iter = ele.getSecond().getVisibleVariables()
						.keySet().iterator();
				while (iter.hasNext()) {
					String v = iter.next();
					msg.append(v).append(" = ").append(
							f
									.evaluate(v + "!=null?" + v
											+ ".toString():'null'"));
					if (iter.hasNext()) {
						msg.append(", \n");
					}
				}
				msg.append("\n");
			}
			// formatting
			String[] evals = msg.toString().split("\n");
			for (int j = 0; j < evals.length; j++) {
				String string = evals[j];
				result.append(string);
				if (j + 1 < evals.length) {
					result.append("\n").append(indent);
				}
			}
			result.append("\n");
		}
		return result.toString();
	}

	protected void invokeInternal2(WorkflowContext ctx,
			ProgressMonitor monitor, Issues issues) {
	};

	protected boolean exceptionsOnNullEvaluation = false;

	public void setExceptionsOnNullEvaluation(boolean exceptionsOnNullEvaluation) {
		this.exceptionsOnNullEvaluation = exceptionsOnNullEvaluation;
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2435.java
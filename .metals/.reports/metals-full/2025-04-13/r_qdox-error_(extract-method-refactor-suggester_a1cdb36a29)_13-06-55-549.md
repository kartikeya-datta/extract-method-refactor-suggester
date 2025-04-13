error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4408.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4408.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4408.java
text:
```scala
final S@@tringBuilder buff = new StringBuilder("(");

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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.internal.xpand2.model.XpandDefinition;
import org.eclipse.internal.xpand2.model.XpandResource;
import org.eclipse.internal.xtend.expression.ast.DeclaredParameter;
import org.eclipse.internal.xtend.expression.ast.Identifier;
import org.eclipse.internal.xtend.expression.ast.SyntaxElement;
import org.eclipse.xpand2.XpandExecutionContext;
import org.eclipse.xtend.expression.AnalysationIssue;
import org.eclipse.xtend.expression.ExecutionContext;
import org.eclipse.xtend.expression.Variable;
import org.eclipse.xtend.typesystem.Type;

public abstract class AbstractDefinition extends SyntaxElement implements XpandDefinition {

	private Template owner = null;

	private DeclaredParameter[] params;

	private Identifier name;

	private Identifier type;

	private Statement[] body;

	protected String _stringRepresentation = null;

	protected boolean wildParams = false;

	public AbstractDefinition(final Identifier name, final Identifier type, final DeclaredParameter[] params,
			final Statement[] body) {
		this.name = name;
		this.type = type;
		this.params = params;
		this.body = body;
	}

	public XpandResource getOwner() {
		return owner;
	}

	public void setOwner(final Template owner) {
		this.owner = owner;
	}

	public DeclaredParameter[] getParams() {
		return params;
	}

	public List<DeclaredParameter> getParamsAsList() {
		return Arrays.asList(params);
	}

	public Identifier getType() {
		return type;
	}

	public String getTargetType() {
		return type.getValue();
	}

	public Identifier getDefName() {
		return name;
	}

	public String getName() {
		return name.getValue();
	}

	public String getQualifiedName() {
		if (getFileName() != null) {
			String prefix = getFileName().replaceAll("/", "::");
			prefix = prefix.substring(0, prefix.length() - 4);
			return prefix + "::" + getName();
		}
		return getName();
	}

	public String getParamString(boolean typesOnly) {
		if (params == null || params.length == 0)
			return wildParams ? "(*)" : "";
		final StringBuffer buff = new StringBuffer("(");
		for (int i = 0; i < params.length; i++) {
			final DeclaredParameter p = params[i];
			buff.append(p.getType().getValue());
			if (!typesOnly) {
				buff.append(" ").append(p.getName().getValue());
			}
			if (i + 1 < params.length) {
				buff.append(",");
			}
		}
		if (wildParams) {
			buff.append(",*");
		}
		return buff.append(")").toString();
	}

	public Statement[] getBody() {
		return body;
	}

	public List<Statement> getBodyAsList() {
		return Arrays.asList(body);
	}

	public void analyze(XpandExecutionContext ctx, final Set<AnalysationIssue> issues) {
		try {
			if (ctx.getCallback() != null) {
				if(!ctx.getCallback().pre(this, ctx)) {
					return;
				}
			}
			final Type thisType = ctx.getTypeForName(getType().getValue());
			if (thisType == null) {
				issues.add(new AnalysationIssue(AnalysationIssue.TYPE_NOT_FOUND, "Couldn't find "
						+ getType().getValue(), getType()));
			}
			ctx = (XpandExecutionContext) ctx.cloneWithVariable(new Variable(ExecutionContext.IMPLICIT_VARIABLE,
					thisType));
			for (int i = 0; i < params.length; i++) {
				final DeclaredParameter param = params[i];
				Type paramType = ctx.getTypeForName(param.getType().getValue());
				if (paramType == null) {
					issues.add(new AnalysationIssue(AnalysationIssue.TYPE_NOT_FOUND, "Couldn't find "
							+ param.getType().getValue(), param.getType()));
					paramType = ctx.getObjectType();
				}
				final String name = param.getName().getValue();
				ctx = (XpandExecutionContext) ctx.cloneWithVariable(new Variable(name, paramType));
			}
			for (int i = 0; i < getBody().length; i++) {
				Statement stmt = getBody()[i];
				try {
					stmt.analyze(ctx, issues);
				}
				catch (RuntimeException ex) {
					Map<String, Object> info = new HashMap<String, Object>();
					info.put("body", stmt);
					ctx.handleRuntimeException(ex, this, info);
				}
			}
		}
		finally {
			if (ctx.getCallback() != null) {
				ctx.getCallback().post(this, ctx, null);
			}
		}
	}

	public void evaluate(XpandExecutionContext ctx, Object _this, Object...params) {
		try {
			ctx = (XpandExecutionContext) ctx.cloneWithResource(getOwner());
			ctx =  prepareDeclaredParameters(_this, ctx, params);
			if (ctx.getCallback() != null) {
				if (!ctx.getCallback().pre(this, ctx)) {
					return;
				}
			}
			for (int i = 0; i < getBody().length; i++) {
				Statement stmt = getBody()[i];
				try {
					stmt.evaluate(ctx);
				}
				catch (RuntimeException ex) {
					Map<String, Object> info = new HashMap<String, Object>();
					info.put("body", stmt);
					ctx.handleRuntimeException(ex, this, info);
				}
			}
		}
		finally {
			if (ctx.getCallback() != null) {
				ctx.getCallback().post(this, ctx, null);
			}
		}
	}

	protected XpandExecutionContext prepareDeclaredParameters(Object _this, XpandExecutionContext context, Object... params) {
		if (_this != null) {
		    context = (XpandExecutionContext) context.cloneWithVariable(new Variable(
		            ExecutionContext.IMPLICIT_VARIABLE, _this));
		}
		if (params != null) {
			DeclaredParameter[] params2 = getParams();
		    for (int i = 0, x = params2.length; i < x;i++) {
		        final Object o = params[i];
		        final String name = params2[i].getName().getValue();
		        context = (XpandExecutionContext) context.cloneWithVariable(new Variable(name, o));
		    }
		}
		return context;
	}

	@Override
	public String toString() {
		if (_stringRepresentation == null) {
			_stringRepresentation = name.getValue() + getParamString(false) + " : " + getType().getValue();
		}

		return _stringRepresentation;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((toString() == null) ? 0 : toString().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (obj == null || toString() == null)
			return false;

		if (getClass() != obj.getClass())
			return false;

		AbstractDefinition other = (AbstractDefinition) obj;
		return toString().equals(other.toString());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4408.java
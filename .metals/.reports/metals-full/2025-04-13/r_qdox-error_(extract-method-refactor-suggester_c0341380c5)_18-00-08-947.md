error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8433.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8433.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8433.java
text:
```scala
_@@stringRepresentation = getName() + getParamString(false)

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

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.internal.xpand2.model.XpandAdvice;
import org.eclipse.internal.xpand2.model.XpandDefinition;
import org.eclipse.internal.xpand2.type.DefinitionType;
import org.eclipse.internal.xtend.expression.ast.DeclaredParameter;
import org.eclipse.internal.xtend.expression.ast.Identifier;
import org.eclipse.xpand2.XpandExecutionContext;
import org.eclipse.xtend.expression.AnalysationIssue;
import org.eclipse.xtend.expression.ExecutionContext;
import org.eclipse.xtend.expression.Variable;
import org.eclipse.xtend.typesystem.Type;

/**
 * @author Sven Efftinge (http://www.efftinge.de) *
 */
public class Advice extends AbstractDefinition implements XpandAdvice {

	public final static String DEF_VAR_NAME = "targetDef";

	public Advice(final Identifier pointCut, final Identifier type, final DeclaredParameter[] params, final boolean wildParams, final Statement[] body) {
		super(pointCut, type, params, body);
		this.wildParams = wildParams;
	}

	public Identifier getPointCut() {
		return getDefName();
	}

	@Override
	public void analyze(XpandExecutionContext ctx, final Set<AnalysationIssue> issues) {
		ctx = (XpandExecutionContext) ctx.cloneWithVariable(new Variable(DEF_VAR_NAME, ctx.getTypeForName(DefinitionType.TYPE_NAME)));
		super.analyze(ctx, issues);
	}

	private Pattern p = null;

	public boolean isWildcardParams () {
	    return wildParams;
	}
	
	public boolean matches(final XpandDefinition def, XpandExecutionContext ctx) {
		if (p == null) {
			p = Pattern.compile(getName().replaceAll("\\*", ".*"));
		}
		final Matcher m = p.matcher(def.getQualifiedName());
		if (m.matches()) {
			ctx = (XpandExecutionContext) ctx.cloneWithResource(def.getOwner());
			final Type t = ctx.getTypeForName(def.getTargetType());
			final Type[] paramTypes = new Type[def.getParams().length];
			for (int i = 0; i < paramTypes.length; i++) {
				paramTypes[i] = ctx.getTypeForName(def.getParams()[i].getType().getValue());
			}
			if (getParams().length == paramTypes.length || (wildParams && getParams().length <= paramTypes.length)) {

				ctx = (XpandExecutionContext) ctx.cloneWithResource(def.getOwner());
				final Type at = ctx.getTypeForName(getTargetType());
				if (at.isAssignableFrom(t)) {
					for (int i = 0; i < getParams().length; i++) {
						final Type pt = ctx.getTypeForName(getParams()[i].getType().getValue());
						if (!pt.isAssignableFrom(paramTypes[i]))
							return false;
					}
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public String getNameString(ExecutionContext context) {
		return "AROUND";
	}

	@Override
	public String toString() {
		if (_stringRepresentation == null) {
			_stringRepresentation = getOwner().getFullyQualifiedName() + ": " + getName() + getParamString(false)
					+ " : " + getType().getValue();
		}

		return _stringRepresentation;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8433.java
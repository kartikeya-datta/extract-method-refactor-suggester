error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4457.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4457.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4457.java
text:
```scala
S@@tringBuilder sb = new StringBuilder();

/*
 * Copyright 2004-2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.expression.spel.ast;

import org.antlr.runtime.Token;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.spel.SpelException;
import org.springframework.expression.spel.ExpressionState;

/**
 * Represents a DOT separated expression sequence, such as 'property1.property2.methodOne()'
 * 
 * @author Andy Clement
 * 
 */
public class CompoundExpression extends SpelNode {

	public CompoundExpression(Token payload) {
		super(payload);
	}

	/**
	 * Evalutes a compound expression. This involves evaluating each piece in turn and the return value from each piece
	 * is the active context object for the subsequent piece.
	 * 
	 * @param state the state in which the expression is being evaluated
	 * @return the final value from the last piece of the compound expression
	 */
	@Override
	public Object getValue(ExpressionState state) throws EvaluationException {
		Object result = null;
		SpelNode nextNode = null;
		try {
			nextNode = getChild(0);
			result = nextNode.getValue(state);
			for (int i = 1; i < getChildCount(); i++) {
				try {
					state.pushActiveContextObject(result);
					nextNode = getChild(i);
					result = nextNode.getValue(state);
				} finally {
					state.popActiveContextObject();
				}
			}
		} catch (SpelException ee) {
			// Correct the position for the error before rethrowing
			ee.setPosition(nextNode.getCharPositionInLine());
			throw ee;
		}
		return result;
	}

	@Override
	public void setValue(ExpressionState state, Object value) throws EvaluationException {
		if (getChildCount() == 1) {
			getChild(0).setValue(state, value);
			return;
		}
		Object ctx = getChild(0).getValue(state);
		for (int i = 1; i < getChildCount() - 1; i++) {
			try {
				state.pushActiveContextObject(ctx);
				ctx = getChild(i).getValue(state);
			} finally {
				state.popActiveContextObject();
			}
		}
		try {
			state.pushActiveContextObject(ctx);
			getChild(getChildCount() - 1).setValue(state, value);
		} finally {
			state.popActiveContextObject();
		}
	}

	@Override
	public boolean isWritable(ExpressionState state) throws EvaluationException {
		if (getChildCount() == 1) {
			return getChild(0).isWritable(state);
		}
		Object ctx = getChild(0).getValue(state);
		for (int i = 1; i < getChildCount() - 1; i++) {
			try {
				state.pushActiveContextObject(ctx);
				ctx = getChild(i).getValue(state);
			} finally {
				state.popActiveContextObject();
			}
		}
		try {
			state.pushActiveContextObject(ctx);
			return getChild(getChildCount() - 1).isWritable(state);
		} finally {
			state.popActiveContextObject();
		}
	}

	@Override
	public String toStringAST() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < getChildCount(); i++) {
			sb.append(getChild(i).toStringAST());
		}
		return sb.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4457.java
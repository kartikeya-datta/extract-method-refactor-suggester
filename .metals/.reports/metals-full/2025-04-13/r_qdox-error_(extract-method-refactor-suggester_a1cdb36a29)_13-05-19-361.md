error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9886.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9886.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[14,1]

error in qdox parser
file content:
```java
offset: 656
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9886.java
text:
```scala
public class ProposalFactoryDefaultImpl extends AbstractProposalFactory {

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

p@@ackage org.eclipse.internal.xtend.expression.codeassist;

import org.eclipse.internal.xtend.xtend.ast.Extension;
import org.eclipse.xtend.expression.Variable;
import org.eclipse.xtend.typesystem.Operation;
import org.eclipse.xtend.typesystem.Property;
import org.eclipse.xtend.typesystem.StaticProperty;
import org.eclipse.xtend.typesystem.Type;

public class ProposalFactoryDefaultImpl extends AbstractProposalFactory implements ProposalFactory {

	public Object createPropertyProposal(final Property p, final String prefix, final boolean onCollection) {
		return new ProposalImpl(prefix, p.getName(), p.toString(), p);
	}

	public Object createStaticPropertyProposal(final StaticProperty p, final String prefix, final boolean onCollection) {
		return new ProposalImpl(prefix, p.getName(), p.toString(), p);
	}

	public Object createOperationProposal(final Operation p, final String prefix, final boolean onCollection) {
		return new ProposalImpl(prefix, p.getName(), p.toString(), p);
	}

	public Object createExtensionOnMemberPositionProposal(final Extension p, final String prefix,
			final boolean onCollection) {
		return new ProposalImpl(prefix, p.getName(), p.toString(), p);
	}

	public Object createCollectionSpecificOperationProposal(final String insertString, final String displayString,
			final String prefix, final int cursor, final int marked) {
		return new ProposalImpl(prefix, insertString, displayString, displayString);
	}

	public Object createExtensionProposal(final Extension p, final String prefix) {
		return new ProposalImpl(prefix, p.getName(), p.toString(), p);
	}

	public Object createVariableProposal(final String name, final Type t, final String prefix) {
		return new ProposalImpl(prefix, name, name, new Variable(name, t));
	}

	public Object createTypeProposal(final String insertString, final Type type, final String prefix) {
		return new ProposalImpl(prefix, insertString, type.getName(), type);
	}

	public Object createStatementProposal(final String insertString, final String displayString, final String prefix,
			final int cursor, final int marked) {
		return new ProposalImpl(prefix, insertString, displayString, displayString);
	}

	public Object createStatementProposal(final String insertString, final String displayString, final String prefix) {
		return new ProposalImpl(prefix, insertString, displayString, displayString);
	}

	public Object createKeywordProposal(final String insertString, final String displayString, final String prefix) {
		return new ProposalImpl(prefix, insertString, displayString, displayString);
	}

	public Object createExtensionImportProposal(String insertStr, String displayStr, String prefix, int cursor,
			int marked) {
		return new ProposalImpl(prefix, insertStr, displayStr, displayStr);
	}

	public Object createNamespaceProposal(String insertStr, String displayStr, String prefix) {
		return new ProposalImpl(prefix, insertStr, displayStr, displayStr);
	}

	public Object createDefinitionProposal(String insertStr, String displayStr, String prefix) {
		return new ProposalImpl(prefix, insertStr, displayStr, displayStr);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9886.java
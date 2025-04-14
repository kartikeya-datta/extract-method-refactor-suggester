error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8211.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8211.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8211.java
text:
```scala
protected S@@tring computeReturnType(Type returnType, final boolean onOperation) {

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
package org.eclipse.xtend.shared.ui.expression.editor.codeassist;

import java.util.List;

import org.eclipse.internal.xtend.expression.codeassist.AbstractProposalFactory;
import org.eclipse.internal.xtend.xtend.ast.Extension;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.swt.graphics.Image;
import org.eclipse.xpand2.XpandUtil;
import org.eclipse.xtend.expression.TypeNameUtil;
import org.eclipse.xtend.shared.ui.expression.editor.EditorImages;
import org.eclipse.xtend.typesystem.Operation;
import org.eclipse.xtend.typesystem.ParameterizedType;
import org.eclipse.xtend.typesystem.Property;
import org.eclipse.xtend.typesystem.StaticProperty;
import org.eclipse.xtend.typesystem.Type;

public class ProposalFactoryEclipseImpl extends AbstractProposalFactory {

	public int offset;

	public ProposalFactoryEclipseImpl(final int offset) {
		this.offset = offset;
	}

	public ICompletionProposal createCollectionSpecificOperationProposal(final String insertString, final String displayString,
			final String prefix, final int cursor, final int marked) {
		final String displayStr = displayString;
		final String insertStr = insertString;
		final Image img = EditorImages.getImage(EditorImages.OPERATION);
		return new TextSelectingProposal(insertStr, offset - prefix.length(), prefix.length(), cursor, marked, img,
				displayStr, null, null);
	}

	public ICompletionProposal createPropertyProposal(final Property p, final String prefix, final boolean onOperation) {
		final String returnType = computeReturnType(p.getReturnType(), onOperation);
		final String displayStr = p.getName() + " " + returnType + " - "
				+ TypeNameUtil.getSimpleName(p.getOwner().getName());
		final String insertStr = p.getName();
		final Image img = EditorImages.getImage(EditorImages.PROPERTY);
		return new CompletionProposal(insertStr, offset - prefix.length(), prefix.length(), insertStr.length(), img,
				displayStr, null, null);
	}

	/**
	 * @see ProposalFactory#createStaticPropertyProposal(StaticProperty, String,
	 *      boolean)
	 */
	public ICompletionProposal createStaticPropertyProposal(final StaticProperty p, final String prefix, final boolean onOperation) {
		final String returnType = computeReturnType(p.getReturnType(), onOperation);
		final String displayStr = p.getName() + " " + returnType + " - "
				+ TypeNameUtil.getSimpleName(p.getOwner().getName());
		final String insertStr = p.getName();
		final Image img = EditorImages.getImage(EditorImages.STATICPROPERTY);
		return new CompletionProposal(insertStr, offset - prefix.length(), prefix.length(), insertStr.length(), img,
				displayStr, null, null);
	}

	private String computeReturnType(Type returnType, final boolean onOperation) {
		if (returnType == null) {
			return "unknown";
		}
		if (onOperation) {
			if (returnType instanceof ParameterizedType) {
				returnType = ((ParameterizedType) returnType).getInnerType();
			}
			return "List[" + XpandUtil.getLastSegment(returnType.getName()) + "]";
		}
		StringBuilder result = new StringBuilder();
		computeReturnType(returnType, result);
		return result.toString();
	}
	
	private void computeReturnType(Type returnType, StringBuilder result) {
		if (returnType == null) {
			result.append("unknown");
			return;
		}
			
		if (returnType instanceof ParameterizedType) {
			result.append(TypeNameUtil.getSimpleName(returnType.getName()));
			result.append('[');
			computeReturnType(((ParameterizedType) returnType).getInnerType(), result);
			result.append(']');
		} else {
			result.append(TypeNameUtil.getSimpleName(returnType.getName()));
		}
	}

	public ICompletionProposal createOperationProposal(final Operation p, final String prefix, final boolean onOperation) {
		final String returnType = computeReturnType(p.getReturnType(), onOperation);
		final String displayStr = p.getName() + toParamString(p.getParameterTypes()) + " " + returnType + " - "
				+ TypeNameUtil.getSimpleName(p.getOwner().getName());
		final String insertStr = p.getName() + "()";
		int x = insertStr.length();
		if (p.getParameterTypes().size() > 0) {
			x--;
		}
		final Image img = EditorImages.getImage(EditorImages.OPERATION);
		return new CompletionProposal(insertStr, offset - prefix.length(), prefix.length(), x, img, displayStr, null,
				null);
	}

	public ICompletionProposal createExtensionProposal(final Extension p, final String prefix) {
		final String displayStr = p.getName() + toParamString(p, false);
		final String insertStr = p.getName() + "()";
		int x = insertStr.length();
		if (p.getFormalParameters().size() > 0) {
			x--;
		}
		final Image img = EditorImages.getImage(EditorImages.EXTENSION);
		return new CompletionProposal(insertStr, offset - prefix.length(), prefix.length(), x, img, displayStr, null,
				null);
	}

	public ICompletionProposal createExtensionOnMemberPositionProposal(final Extension p, final String prefix,
			final boolean onOperation) {
		final String displayStr = p.getName() + toParamString(p, true) + " - "
				+ (p.getFormalParameters().get(0)).getType();
		final String insertStr = p.getName() + "()";
		int x = insertStr.length();
		if (p.getFormalParameters().size() > 1) {
			x--;
		}
		final Image img = EditorImages.getImage(EditorImages.EXTENSION);
		return new CompletionProposal(insertStr, offset - prefix.length(), prefix.length(), x, img, displayStr, null,
				null);
	}

	private String toParamString(final List<Type> parameterTypes) {
		final StringBuffer b = new StringBuffer("(");
		for (int i = 0, x = parameterTypes.size(); i < x; i++) {
			b.append(TypeNameUtil.getSimpleName(parameterTypes.get(i).getName()));
			if (i + 1 < x) {
				b.append(",");
			}
		}
		b.append(")");
		return b.toString();
	}

	private String toParamString(final Extension p, final boolean member) {
		final StringBuffer b = new StringBuffer("(");
		int i = member ? 1 : 0;
		for (final int x = p.getFormalParameters().size(); i < x; i++) {
			b.append((p.getFormalParameters().get(i)).toString());
			if (i + 1 < x) {
				b.append(",");
			}
		}
		b.append(")");
		return b.toString();
	}

	public ICompletionProposal createVariableProposal(final String name, final Type t, final String prefix) {
		final String displayStr = name + " " + computeReturnType(t, false);
		final String insertStr = name;
		final Image img = EditorImages.getImage(EditorImages.VARIABLE);
		return new CompletionProposal(insertStr, offset - prefix.length(), prefix.length(), insertStr.length(), img,
				displayStr, null, null);
	}

	public ICompletionProposal createTypeProposal(final String insertString, final Type type, final String prefix) {
		String displayStr = TypeNameUtil.getSimpleName(type.getName());
		final String packName = TypeNameUtil.withoutLastSegment(type.getName());
		if (packName != null) {
			displayStr += " - " + packName;
		}

		final String insertStr = insertString;
		final Image img = EditorImages.getImage(EditorImages.TYPE);
		return new CompletionProposal(insertStr, offset - prefix.length(), prefix.length(), insertStr.length(), img,
				displayStr, null, null);
	}

	public ICompletionProposal createExtensionImportProposal(final String insertStr, final String displayStr, final String prefix,
			final int cursor, final int marked) {
		final Image img = EditorImages.getImage(EditorImages.EXT_IMPORT);
		return new CompletionProposal(insertStr, offset - prefix.length(), prefix.length(), insertStr.length(), img,
				displayStr, null, null);
	}

	public ICompletionProposal createNamespaceProposal(final String insertStr, final String displayStr, final String prefix) {
		final Image img = EditorImages.getImage(EditorImages.PACKAGE);
		return new CompletionProposal(insertStr, offset - prefix.length(), prefix.length(), insertStr.length(), img,
				displayStr, null, null);
	}

	public ICompletionProposal createDefinitionProposal(final String insertStr, final String displayStr, final String prefix) {
		final Image img = EditorImages.getImage(EditorImages.XPAND_DEFINE);
		return new CompletionProposal(insertStr, offset - prefix.length(), prefix.length(), insertStr.length(), img,
				displayStr, null, null);
	}

	public ICompletionProposal createStatementProposal(final String insertStr, final String displayStr, final String prefix,
			final int cursor, final int marked) {
		final Image img = EditorImages.getImage(EditorImages.EXT_IMPORT);
		return new CompletionProposal(insertStr, offset - prefix.length(), prefix.length(), insertStr.length(), img,
				displayStr, null, null);
	}

	public ICompletionProposal createStatementProposal(final String insertString, final String displayString, final String prefix) {
		throw new UnsupportedOperationException();
	}

	public ICompletionProposal createKeywordProposal(final String insertString, final String displayString, final String prefix) {
		throw new UnsupportedOperationException();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8211.java
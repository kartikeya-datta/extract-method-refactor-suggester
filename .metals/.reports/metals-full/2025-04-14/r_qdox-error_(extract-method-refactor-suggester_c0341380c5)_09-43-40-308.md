error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9928.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9928.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9928.java
text:
```scala
X@@pandExecutionContext ctx = (XpandExecutionContext) Activator.getExecutionContext(getJavaProject());

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

package org.eclipse.xpand.ui.editor.codeassist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.internal.xpand2.codeassist.ExpandProposalComputer;
import org.eclipse.internal.xpand2.codeassist.FastAnalyzer;
import org.eclipse.internal.xpand2.codeassist.KeywordProposalComputer;
import org.eclipse.internal.xpand2.codeassist.NamespaceProposalComputer;
import org.eclipse.internal.xpand2.codeassist.StatementProposalComputer;
import org.eclipse.internal.xpand2.codeassist.XpandPartition;
import org.eclipse.internal.xpand2.codeassist.XpandTokens;
import org.eclipse.internal.xpand2.model.XpandDefinition;
import org.eclipse.internal.xtend.expression.codeassist.ExpressionProposalComputer;
import org.eclipse.internal.xtend.expression.codeassist.ExtensionImportProposalComputer;
import org.eclipse.internal.xtend.expression.codeassist.ProposalFactory;
import org.eclipse.internal.xtend.expression.codeassist.TypeProposalComputer;
import org.eclipse.internal.xtend.xtend.XtendFile;
import org.eclipse.internal.xtend.xtend.codeassist.Partition;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.ui.IEditorPart;
import org.eclipse.xpand.ui.XpandEditorPlugin;
import org.eclipse.xpand.ui.core.IXpandResource;
import org.eclipse.xpand.ui.internal.XpandLog;
import org.eclipse.xpand2.XpandExecutionContext;
import org.eclipse.xtend.shared.ui.Activator;
import org.eclipse.xtend.shared.ui.core.IXtendXpandProject;
import org.eclipse.xtend.shared.ui.core.IXtendXpandResource;
import org.eclipse.xtend.shared.ui.expression.editor.codeassist.AbstractExtXptContentAssistProcessor;
import org.eclipse.xtend.shared.ui.expression.editor.codeassist.ProposalComparator;

/**
 * Computes the Code Completion Proposals when CTRL+SPACE is pressed.
 * 
 * @author Sven Efftinge (http://www.efftinge.de)
 * @since 4.0
 */
public class XpandContentAssistProcessor extends AbstractExtXptContentAssistProcessor {

	public XpandContentAssistProcessor(final IEditorPart editor) {
		super(editor);
	}

	@Override
	protected ICompletionProposal[] internalComputeCompletionProposals(final ITextViewer viewer,
			final int documentOffset) {
		try {
			final String txt = viewer.getDocument().get().substring(0, documentOffset);
			XpandDefinition[] defs = new XpandDefinition[0];
			final IFile file = getFile();

			final IXpandResource tpl = (IXpandResource) Activator.getExtXptModelManager().findExtXptResource(file);
			if (tpl != null) {
				defs = tpl.getDefinitions();
			}
			XpandExecutionContext ctx = XpandEditorPlugin.getExecutionContext(getJavaProject());
			final Partition p = FastAnalyzer.computePartition(txt);

			// Shortcut: No proposals within comments
			if (p == Partition.COMMENT) {
				return new ICompletionProposal[0];
			}

			List<Object> proposals = new ArrayList<Object>();
			final ProposalFactory f = new XpandProposalFactoryEclipseImpl(documentOffset);

			if (p == Partition.TYPE_DECLARATION) {
				ctx = FastAnalyzer.computeExecutionContext(txt, ctx, defs);
				proposals = new TypeProposalComputer().computeProposals(txt, ctx, f);
			} else if (p == Partition.EXPRESSION) {
				ctx = FastAnalyzer.computeExecutionContext(txt, ctx, defs);
				// the current expression begins at the last opening Xpand
				// bracket
				final String expression = txt.substring(txt.lastIndexOf(XpandTokens.LT_CHAR));
				proposals.addAll(new ExpressionProposalComputer().computeProposals(expression, ctx, f));
				proposals.addAll(new KeywordProposalComputer().computeProposals(txt, ctx, f));
			} else if (p == XpandPartition.EXPAND_STATEMENT) {
				ctx = FastAnalyzer.computeExecutionContext(txt, ctx, defs);
				proposals.addAll(new ExpandProposalComputer().computeProposals(txt, ctx, f));
				proposals.add(new org.eclipse.jface.text.contentassist.CompletionProposal(XpandTokens.LT
						+ XpandTokens.RT, documentOffset, 0, 1));
			} else if (p == Partition.NAMESPACE_IMPORT) {
				ctx = FastAnalyzer.computeExecutionContext(txt, ctx, defs);
				proposals.addAll(new NamespaceProposalComputer().computeProposals(txt, ctx, f));
			} else if (p == Partition.EXTENSION_IMPORT) {
				IXtendXpandProject project = Activator.getExtXptModelManager().findProject(getFile());
				IXtendXpandResource[] resources = project.getAllRegisteredResources();
				Set<String> extensionNames = new HashSet<String>();
				for (IXtendXpandResource resource : resources) {
					if (resource instanceof XtendFile) {
						extensionNames.add(resource.getFullyQualifiedName());
					}
				}
				List<Object> extensionProposals = new ExtensionImportProposalComputer().computeProposals(txt, ctx, f, extensionNames);
				proposals.addAll(extensionProposals);
			} else if (p == Partition.DEFAULT) {
				ctx = FastAnalyzer.computeExecutionContext(txt, ctx, defs);
				proposals.addAll(new StatementProposalComputer().computeProposals(txt, ctx, f));
				proposals.add(new org.eclipse.jface.text.contentassist.CompletionProposal(XpandTokens.LT
						+ XpandTokens.RT, documentOffset, 0, 1));
			}
			Collections.sort(proposals, new ProposalComparator());
			return proposals.toArray(new ICompletionProposal[proposals.size()]);
		} catch (final Exception e) {
			XpandLog.logError(e);
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public IContextInformation[] computeContextInformation(final ITextViewer viewer, final int documentOffset) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public char[] getCompletionProposalAutoActivationCharacters() {
		return new char[] { '.', XpandTokens.LT_CHAR };
	}

	/**
	 * {@inheritDoc}
	 */
	public char[] getContextInformationAutoActivationCharacters() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getErrorMessage() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public IContextInformationValidator getContextInformationValidator() {
		return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9928.java
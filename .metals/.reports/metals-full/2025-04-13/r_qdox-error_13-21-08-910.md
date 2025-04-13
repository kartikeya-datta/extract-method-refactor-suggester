error id: <WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3341.java
<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3341.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3341.java
text:
```scala
r@@eturn true;

/*******************************************************************************
 * Copyright (c) 2005 - 2007 committers of openArchitectureWare and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     committers of openArchitectureWare - initial API and implementation
 *******************************************************************************/
package org.eclipse.xtend.shared.ui.editor.search.query;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.ISearchResult;
import org.eclipse.search.ui.text.AbstractTextSearchResult;
import org.eclipse.search.ui.text.Match;
import org.eclipse.xtend.shared.ui.Messages;
import org.eclipse.xtend.shared.ui.core.IXtendXpandProject;
import org.eclipse.xtend.shared.ui.core.search.SearchMatch;

import com.ibm.icu.text.MessageFormat;

/**
 * <code>XtendXpandSearchQuery</code> is a base class for search queries. Queries
 * contain the search parameters for the search and the code that performs the
 * query as well.
 * 
 * @author Peter Friese
 */
public abstract class XtendXpandSearchQuery implements ISearchQuery {

	private final String identifier;
	private final IXtendXpandProject project;
	private XtendXpandSearchResult result;

	/**
	 * Creates a new <code>XtendXpandSearchQuery</code>.
	 * 
	 * @param project
	 *            The project to run this query on.
	 * @param identifier
	 *            The identifier to look for.
	 */
	public XtendXpandSearchQuery(IXtendXpandProject project, String identifier) {
		this.project = project;
		this.identifier = identifier;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean canRerun() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean canRunInBackground() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getLabel() {
		return Messages.XtendXpandSearchQuery_Label;
	}

	/**
	 * Returns the label that will be displayed on top of the search result
	 * view.
	 * 
	 * @param matchCount
	 *            The number of search matches.
	 * @return The text for the search result view label.
	 */
	public String getResultLabel(int matchCount) {
		String result = null;
		String searchText = identifier;
		String scope = "project";
		if (matchCount == 1) {
			result = MessageFormat.format("''{0}'' - 1 match in {1}",new Object[]{ searchText, scope});
		} else if (matchCount >= 2) {
			result = MessageFormat.format("''{0}'' - {1} matches in {2}", new Object[]{ searchText, matchCount, scope});
		} else {
			result = MessageFormat.format("''{0}'' - no match found in {1}", new Object[]{ searchText, scope});
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	public ISearchResult getSearchResult() {
		if (result == null) {
			result = new XtendXpandSearchResult(this);
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	public IStatus run(IProgressMonitor monitor) throws OperationCanceledException {
		ISearchResult searchResult = getSearchResult();
		if (searchResult instanceof AbstractTextSearchResult) {
			AbstractTextSearchResult result = (AbstractTextSearchResult) searchResult;
			result.removeAll();
		}

		List<SearchMatch> matches = performSearch();
		for (SearchMatch searchMatch : matches) {
			result.addMatch(new Match(searchMatch.getFile(), searchMatch.getOffSet() - 1, searchMatch.getLength()));
		}
		return Status.OK_STATUS;
	}

	/**
	 * @return
	 */
	protected abstract List<SearchMatch> performSearch();

	public String getIdentifier() {
		return identifier;
	}

	/**
	 * Retrurns the project this query is operating on.
	 * 
	 * @return The project this query is operating on.
	 */
	public IXtendXpandProject getProject() {
		return project;
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
	scala.meta.internal.mtags.MtagsIndexer.index(MtagsIndexer.scala:21)
	scala.meta.internal.mtags.MtagsIndexer.index$(MtagsIndexer.scala:20)
	scala.meta.internal.mtags.JavaMtags.index(JavaMtags.scala:38)
	scala.meta.internal.tvp.IndexedSymbols.javaSymbols(IndexedSymbols.scala:111)
	scala.meta.internal.tvp.IndexedSymbols.workspaceSymbolsFromPath(IndexedSymbols.scala:120)
	scala.meta.internal.tvp.IndexedSymbols.$anonfun$workspaceSymbols$2(IndexedSymbols.scala:146)
	scala.collection.concurrent.TrieMap.getOrElseUpdate(TrieMap.scala:960)
	scala.meta.internal.tvp.IndexedSymbols.$anonfun$workspaceSymbols$1(IndexedSymbols.scala:146)
	scala.meta.internal.tvp.IndexedSymbols.withTimer(IndexedSymbols.scala:71)
	scala.meta.internal.tvp.IndexedSymbols.workspaceSymbols(IndexedSymbols.scala:143)
	scala.meta.internal.tvp.FolderTreeViewProvider.$anonfun$projects$9(MetalsTreeViewProvider.scala:306)
	scala.collection.Iterator$$anon$9.next(Iterator.scala:584)
	scala.collection.Iterator$$anon$10.nextCur(Iterator.scala:594)
	scala.collection.Iterator$$anon$10.hasNext(Iterator.scala:608)
	scala.collection.Iterator$$anon$6.hasNext(Iterator.scala:477)
	scala.collection.Iterator$$anon$10.hasNext(Iterator.scala:601)
	scala.collection.Iterator$$anon$8.hasNext(Iterator.scala:562)
	scala.collection.immutable.List.prependedAll(List.scala:155)
	scala.collection.immutable.List$.from(List.scala:685)
	scala.collection.immutable.List$.from(List.scala:682)
	scala.collection.SeqFactory$Delegate.from(Factory.scala:306)
	scala.collection.immutable.Seq$.from(Seq.scala:42)
	scala.collection.IterableOnceOps.toSeq(IterableOnce.scala:1473)
	scala.collection.IterableOnceOps.toSeq$(IterableOnce.scala:1473)
	scala.collection.AbstractIterator.toSeq(Iterator.scala:1306)
	scala.meta.internal.tvp.ClasspathTreeView.children(ClasspathTreeView.scala:62)
	scala.meta.internal.tvp.FolderTreeViewProvider.getProjectRoot(MetalsTreeViewProvider.scala:390)
	scala.meta.internal.tvp.MetalsTreeViewProvider.$anonfun$children$1(MetalsTreeViewProvider.scala:84)
	scala.collection.immutable.List.map(List.scala:247)
	scala.meta.internal.tvp.MetalsTreeViewProvider.children(MetalsTreeViewProvider.scala:84)
	scala.meta.internal.metals.WorkspaceLspService.$anonfun$treeViewChildren$1(WorkspaceLspService.scala:705)
	scala.concurrent.Future$.$anonfun$apply$1(Future.scala:687)
	scala.concurrent.impl.Promise$Transformation.run(Promise.scala:467)
	java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
	java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
	java.base/java.lang.Thread.run(Thread.java:840)
```
#### Short summary: 

QDox parse error in <WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3341.java
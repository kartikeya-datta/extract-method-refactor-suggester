error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9771.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9771.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9771.java
text:
```scala
private L@@ist<String> loadContent() throws IOException {

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
package org.eclipse.xtend.util.stdlib.texttest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.mwe.core.resources.ResourceLoaderFactory;

public class Handle {

	private String filename;
	private List<String> lines;
	private String contentsAsString;

	public Handle(String filename ) {
		this.filename = filename;
	}
	
	@Override
	public String toString() {
		return filename;
	}

	public void sameContentAs(Handle compareFile) {
		List<String> lines1 = getContents();
		List<String> lines2 = compareFile.getContents();
		int count = lines1.size() < lines2.size() ? lines1.size() : lines2.size();
		for (int i = 0; i < count; i++) {
			String s1 = lines1.get(i);
			String s2 = lines2.get(i);
			if ( !s1.equals(s2)) throw new Failed(filename+" does not have the same contents as "+compareFile.filename+". First difference in line "+i+": should be: '"+s2+"', but is '"+s1+"'");
		}
	}

	public List<String> getContents() {
		if ( lines == null )
			try {
				lines = loadContent();
			} catch (IOException e) {
				throw new RuntimeException(e);  
			}
		return lines;
	}

	private List loadContent() throws IOException {
		List<String> l = new ArrayList<String>();
		BufferedReader br = new BufferedReader( new InputStreamReader( getFileIS(filename) ) );
		while (br.ready()) l.add( br.readLine());
		br.close();
		return l;
	}

	public void contains(String substring) {
		if ( getContentsAsString().indexOf( substring ) < 0 ) throw new Failed(filename+" does not contain '"+substring+"'");
	}
	
	public int lineOf( String substring ) {
		for (int i = 0; i < getContents().size(); i++) {
			String line = getContents().get(i);
			if ( line.indexOf(substring) >= 0 ) return i;
		}
		throw new Failed("the substring '"+substring+"' can't be found in the content of "+filename);
	}

	private String getContentsAsString() {
		if ( contentsAsString == null ) {
			StringBuffer bf = new StringBuffer();
			for (String s : getContents()) {
				bf.append(s+"\n");
			}
			contentsAsString = bf.toString();
		}
		return contentsAsString;
	}

	public void removeBlankLines() {
		for (int i = getContents().size()-1; i>= 0; i--) {
			String line = getContents().get(i);
			if ( line.trim().equals("") ) getContents().remove(i);
		}
		contentsAsString = null;
	}

	public void containsInLine(int i, String string) {
		if ( getContents().get(i).indexOf(string) < 0 ) throw new Failed("'"+string+"' not found in line "+i+" of file "+filename);
	}
	
    private InputStream getFileIS(String fn) {
		final InputStream in = ResourceLoaderFactory.createResourceLoader().getResourceAsStream(fn);
        return in;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9771.java
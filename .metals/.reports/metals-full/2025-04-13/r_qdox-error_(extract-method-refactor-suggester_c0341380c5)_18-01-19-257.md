error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/592.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/592.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/592.java
text:
```scala
r@@eturn binaryWeave(inpath,insource,expErrors,expWarnings,xlinterror,"");

/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Andy Clement - initial implementation
 *******************************************************************************/
package org.aspectj.systemtest.ajc150;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.aspectj.bridge.IMessage;
import org.aspectj.tools.ajc.AjcTestCase;
import org.aspectj.tools.ajc.CompilationResult;

public abstract class TestUtils extends AjcTestCase {
	private static final boolean verbose = false;
	protected File baseDir;
	
	protected CompilationResult binaryWeave(String inpath,String insource,int expErrors,int expWarnings) {
		return binaryWeave(inpath,insource,expErrors,expWarnings,false);
	}

	protected CompilationResult binaryWeave(String inpath, String insource,int expErrors,int expWarnings,boolean xlinterror) {
		return binaryWeave(inpath,insource,expErrors,expWarnings,false,"");
	}
	
	protected CompilationResult binaryWeave(String inpath, String insource,int expErrors,int expWarnings,String extraOption) {
		return binaryWeave(inpath,insource,expErrors,expWarnings,false,extraOption);
	}
	
	protected CompilationResult binaryWeave(String inpath, String insource,int expErrors,int expWarnings,boolean xlinterror,String extraOption) {
		String[] args = null;
		if (xlinterror) {
			if (extraOption!=null && extraOption.length()>0) 
				args = new String[] {"-inpath",inpath,insource,"-showWeaveInfo","-proceedOnError","-Xlint:warning",extraOption};
			else 
				args = new String[] {"-inpath",inpath,insource,"-showWeaveInfo","-proceedOnError","-Xlint:warning"};
		} else {
			if (extraOption!=null && extraOption.length()>0) 
				args = new String[] {"-inpath",inpath,insource,"-showWeaveInfo","-proceedOnError",extraOption};
			else
				args = new String[] {"-inpath",inpath,insource,"-showWeaveInfo","-proceedOnError"};
		}
		CompilationResult result = ajc(baseDir,args);
		if (verbose || result.hasErrorMessages()) System.out.println(result);
		assertTrue("Expected "+expErrors+" errors but got "+result.getErrorMessages().size()+":\n"+
				   formatCollection(result.getErrorMessages()),result.getErrorMessages().size()==expErrors);
		assertTrue("Expected "+expWarnings+" warnings but got "+result.getWarningMessages().size()+":\n"+
				   formatCollection(result.getWarningMessages()),result.getWarningMessages().size()==expWarnings);
		return result;
	}
	

	private String formatCollection(Collection s) {
		StringBuffer sb = new StringBuffer();
		for (Iterator iter = s.iterator(); iter.hasNext();) {
			Object element = (Object) iter.next();
			sb.append(element).append("\n");
		}
		return sb.toString();
	}
	
	protected List getWeavingMessages(List msgs) {
		List result = new ArrayList();
		for (Iterator iter = msgs.iterator(); iter.hasNext();) {
			IMessage element = (IMessage) iter.next();
			if (element.getKind()==IMessage.WEAVEINFO) {
				result.add(element.toString());
			}
		}
		return result;
	}

	protected void verifyWeavingMessagesOutput(CompilationResult cR,String[] expected) {
		List weavingmessages = getWeavingMessages(cR.getInfoMessages());
		dump(weavingmessages);
		for (int i = 0; i < expected.length; i++) {
			boolean found = weavingmessages.contains(expected[i]);
			if (found) {
				weavingmessages.remove(expected[i]);
			} else {
				System.err.println(dump(getWeavingMessages(cR.getInfoMessages())));
				fail("Expected message not found.\nExpected:\n"+expected[i]+"\nObtained:\n"+dump(getWeavingMessages(cR.getInfoMessages())));
			}
		}
		if (weavingmessages.size()!=0) {
			fail("Unexpected messages obtained from program:\n"+dump(weavingmessages));
		}
	}
	
	
	private String dump(List l) {
		StringBuffer sb = new StringBuffer();
		int i =0;
		sb.append("--- Weaving Messages ---\n");
		for (Iterator iter = l.iterator(); iter.hasNext();) {
			sb.append(i+") "+iter.next()+"\n");
		}
		sb.append("------------------------\n");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/592.java
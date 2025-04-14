error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13848.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13848.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13848.java
text:
```scala
protected F@@ormalBinding[] bindings;

/* *******************************************************************
 * Copyright (c) 2002 Palo Alto Research Center, Incorporated (PARC).
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Common Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html 
 *  
 * Contributors: 
 *     PARC     initial implementation 
 * ******************************************************************/


package org.aspectj.weaver.patterns;

import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.IMessageHandler;
import org.aspectj.bridge.ISourceLocation;
import org.aspectj.bridge.Message;
import org.aspectj.bridge.SourceLocation;
import org.aspectj.weaver.IHasPosition;
import org.aspectj.weaver.ResolvedTypeX;
import org.aspectj.weaver.TypeX;
import org.aspectj.weaver.World;

public class SimpleScope implements IScope {

    private World world;
    private ResolvedTypeX enclosingType;
    private FormalBinding[] bindings;

	private String[] importedPrefixes = javaLangPrefixArray;
	private String[] importedNames = ZERO_STRINGS;
    
    public SimpleScope(World world, FormalBinding[] bindings) {
        super();
        this.world = world;
        this.bindings = bindings;
    }
	
	// ---- impl

	//XXX doesn't report any problems
	public TypeX lookupType(String name, IHasPosition location) {
		for (int i=0; i<importedNames.length; i++) {
			String importedName = importedNames[i];
			if (importedName.endsWith(name)) {
				return world.resolve(importedName);
			}
		}
		
		for (int i=0; i<importedPrefixes.length; i++) {
			String importedPrefix = importedPrefixes[i];
			TypeX tryType = world.resolve(TypeX.forName(importedPrefix + name), true);
			if (tryType != ResolvedTypeX.MISSING) {
				return tryType;
			}
		}

		return world.resolve(TypeX.forName(name), true);
	}


    public IMessageHandler getMessageHandler() {
        return world.getMessageHandler();
    }
    public FormalBinding lookupFormal(String name) {
        for (int i = 0, len = bindings.length; i < len; i++) {
            if (bindings[i].getName().equals(name)) return bindings[i];
        }
        return null;
    }
    public FormalBinding getFormal(int i) {
        return bindings[i];
    }
    
    public int getFormalCount() {
    	return bindings.length;
    }

	public String[] getImportedNames() {
		return importedNames;
	}
	public String[] getImportedPrefixes() {
		return importedPrefixes;
	}
	
	public void setImportedNames(String[] importedNames) {
		this.importedNames = importedNames;
	}
	public void setImportedPrefixes(String[] importedPrefixes) {
		this.importedPrefixes = importedPrefixes;
	}
	
	public static FormalBinding[] makeFormalBindings(TypeX[] types, String[] names) {
        int len = types.length;
        FormalBinding[] bindings = new FormalBinding[len];
        for (int i = 0; i < len; i++) {
            bindings[i] = new FormalBinding(types[i], names[i], i);
        }
        return bindings;
	}
	
	// ---- fields
	
	public static final String[] ZERO_STRINGS = new String[0];
	
	public static final String[] javaLangPrefixArray =
		new String[] { "java.lang.", };



	public ISourceLocation makeSourceLocation(IHasPosition location) {
		return new SourceLocation(ISourceLocation.NO_FILE, 0);
	}

	public void message(
		IMessage.Kind kind,
		IHasPosition location1,
		IHasPosition location2,
		String message) {
			message(kind, location1, message);
			message(kind, location2, message);
	}

	public void message(
		IMessage.Kind kind,
		IHasPosition location,
		String message) {
			getMessageHandler()
			.handleMessage(new Message(message, kind, null, makeSourceLocation(location)));

	}
	
	public World getWorld() {
		return world;
	}

	public ResolvedTypeX getEnclosingType() {
		return enclosingType;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13848.java
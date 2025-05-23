error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9679.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9679.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,6]

error in qdox parser
file content:
```java
offset: 6
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9679.java
text:
```scala
@see b@@sh.ReflectManager

/*****************************************************************************
 *                                                                           *
 *  This file is part of the BeanShell Java Scripting distribution.          *
 *  Documentation and updates may be found at http://www.beanshell.org/      *
 *                                                                           *
 *  Sun Public License Notice:                                               *
 *                                                                           *
 *  The contents of this file are subject to the Sun Public License Version  *
 *  1.0 (the "License"); you may not use this file except in compliance with *
 *  the License. A copy of the License is available at http://www.sun.com    * 
 *                                                                           *
 *  The Original Code is BeanShell. The Initial Developer of the Original    *
 *  Code is Pat Niemeyer. Portions created by Pat Niemeyer are Copyright     *
 *  (C) 2000.  All Rights Reserved.                                          *
 *                                                                           *
 *  GNU Public License Notice:                                               *
 *                                                                           *
 *  Alternatively, the contents of this file may be used under the terms of  *
 *  the GNU Lesser General Public License (the "LGPL"), in which case the    *
 *  provisions of LGPL are applicable instead of those above. If you wish to *
 *  allow use of your version of this file only under the  terms of the LGPL *
 *  and not to allow others to use your version of this file under the SPL,  *
 *  indicate your decision by deleting the provisions above and replace      *
 *  them with the notice and other provisions required by the LGPL.  If you  *
 *  do not delete the provisions above, a recipient may use your version of  *
 *  this file under either the SPL or the LGPL.                              *
 *                                                                           *
 *  Patrick Niemeyer (pat@pat.net)                                           *
 *  Author of Learning Java, O'Reilly & Associates                           *
 *  http://www.pat.net/~pat/                                                 *
 *                                                                           *
 *****************************************************************************/

package bsh;

import java.util.Hashtable;

/**
	The map of extended features supported by the runtime in which we live.
	<p>

	This class should be independent of all other bsh classes!
	<p>

	Note that tests for class existence here do *not* use the 
	BshClassManager, as it may require other optional class files to be 
	loaded.  
*/
public class Capabilities 
{
	private static boolean accessibility = false;

	public static boolean haveSwing() {
		// classExists caches info for us
		return classExists( "javax.swing.JButton" );
	}

	public static boolean canGenerateInterfaces() {
		// classExists caches info for us
		return classExists( "java.lang.reflect.Proxy" );
	}

	/**
		If accessibility is enabled
		determine if the accessibility mechanism exists and if we have
		the optional bsh package to use it.
		Note that even if both are true it does not necessarily mean that we 
		have runtime permission to access the fields... Java security has
	 	a say in it.
		@see ReflectManager.java
	*/
	public static boolean haveAccessibility() 
	{
		// classExists caches the tests for us
		return ( accessibility 
			&& classExists( "java.lang.reflect.AccessibleObject" )
			&& classExists("bsh.reflect.ReflectManagerImpl") 
		);
	}

	public static void setAccessibility( boolean b ) { accessibility = b; }

	private static Hashtable classes = new Hashtable();
	/**
		Use direct Class.forName() to test for the existence of a class.
		We should not use BshClassManager here because:
			a) the systems using these tests would probably not load the
			classes through it anyway.
			b) bshclassmanager is heavy and touches other class files.  
			this capabilities code must be light enough to be used by any
			system including the remote applet.
	*/
	public static boolean classExists( String name ) 
	{
		Object c = classes.get( name );

		if ( c == null ) {
			try {
				/*
					Note: do *not* change this to 
					BshClassManager.plainClassForName() or equivalent.
					This class must not touch any other bsh classes.
				*/
				c = Class.forName( name );
			} catch ( ClassNotFoundException e ) { }

			if ( c != null )
				classes.put(c,"unused");
		}

		return c != null;
	}

	/**
		An attempt was made to use an unavailable capability supported by
		an optional package.  The normal operation is to test before attempting
		to use these packages... so this is runtime exception.
	*/
	public static class Unavailable extends RuntimeException 
	{
		public Unavailable(String s ){ super(s); }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9679.java
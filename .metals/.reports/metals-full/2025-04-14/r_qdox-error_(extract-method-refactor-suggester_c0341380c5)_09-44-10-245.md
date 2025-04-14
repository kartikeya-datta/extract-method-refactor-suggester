error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7165.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7165.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7165.java
text:
```scala
C@@lass dummy = Class.forName("com.ibm.uvm.tools.DebugSupport");

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software
 * License version 1.1, a copy of which has been included with this
 * distribution in the LICENSE.txt file.  */

// Contributors: Mathias Rupprecht <mmathias.rupprecht@fja.com>

package org.apache.log4j.spi;

import java.io.StringWriter;
import java.io.PrintWriter;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.Layout;
import org.apache.log4j.helpers.Loader;

/**
   The internal representation of caller location information.
     
   @since 0.8.3
*/
public class LocationInfo implements java.io.Serializable {
    
  /**
     Caller's line number.
  */
  transient String lineNumber;
  /**
     Caller's file name.
  */
  transient String fileName;
  /**
     Caller's fully qualified class name.
  */
  transient String className;
  /**
     Caller's method name.
  */
  transient String methodName;
  /**
     All available caller information, in the format
     <code>fully.qualified.classname.of.caller.methodName(Filename.java:line)</code>
    */
  public String fullInfo;

  private static StringWriter sw = new StringWriter();
  private static PrintWriter pw = new PrintWriter(sw);

  /**
     When location information is not available the constant
     <code>NA</code> is returned. Current value of this string
     constant is <b>?</b>.  */
  public final static String NA = "?";

  static final long serialVersionUID = -1325822038990805636L;


  // Check if we are running in IBM's visual age.
  static boolean inVisualAge = false;
  static {
    try {
      Class dummy = Loader.loadClass("com.ibm.uvm.tools.DebugSupport");
      inVisualAge = true;
      LogLog.debug("Detected IBM VisualAge environment.");
    } catch(Throwable e) { 
      // nothing to do
    }
  }

  /**
     Instantiate location information based on a Throwable. We
     expect the Throwable <code>t</code>, to be in the format

       <pre>
        java.lang.Throwable
        ...
          at org.apache.log4j.PatternLayout.format(PatternLayout.java:413)
          at org.apache.log4j.FileAppender.doAppend(FileAppender.java:183)
        at org.apache.log4j.Category.callAppenders(Category.java:131)
        at org.apache.log4j.Category.log(Category.java:512)
        at callers.fully.qualified.className.methodName(FileName.java:74)
	...
       </pre>

       <p>However, we can also deal with JIT compilers that "lose" the
       location information, especially between the parentheses.

    */
    public LocationInfo(Throwable t, String fqnOfCallingClass) {
      if(t == null)
	return;

      String s;
      // Protect against multiple access to sw.
      synchronized(sw) {
	t.printStackTrace(pw);
	s = sw.toString();
	sw.getBuffer().setLength(0);
      }
      //System.out.println("s is ["+s+"].");
      int ibegin, iend;

      // Given the current structure of the package, the line
      // containing "org.apache.log4j.Category." should be printed just
      // before the caller.

      // This method of searching may not be fastest but it's safer
      // than counting the stack depth which is not guaranteed to be
      // constant across JVM implementations.      
      ibegin = s.lastIndexOf(fqnOfCallingClass);
      if(ibegin == -1) 
	return;


      ibegin = s.indexOf(Layout.LINE_SEP, ibegin); 
      if(ibegin == -1) 
	return;
      ibegin+= Layout.LINE_SEP_LEN;
      
      // determine end of line
      iend = s.indexOf(Layout.LINE_SEP, ibegin); 
      if(iend == -1) 
	return;

      // VA has a different stack trace format which doesn't 
      // need to skip the inital 'at'
      if(!inVisualAge) {
	// back up to first blank character
	ibegin = s.lastIndexOf("at ", iend); 
	if(ibegin == -1) 
	  return;
	// Add 3 to skip "at ";
	ibegin += 3;
      }
      // everything between is the requested stack item
      this.fullInfo = s.substring(ibegin, iend);
    }

    /**
       Return the fully qualified class name of the caller making the
       logging request.
    */
    public
    String getClassName() {      
      if(fullInfo == null) return NA;      
      if(className == null) {
	// Starting the search from '(' is safer because there is
	// potentially a dot between the parentheses.
	int iend = fullInfo.lastIndexOf('(');
	if(iend == -1) 
	  className = NA;
	else {
	  iend =fullInfo.lastIndexOf('.', iend);

	  // This is because a stack trace in VisualAge looks like:

          //java.lang.RuntimeException
	  //  java.lang.Throwable()
	  //  java.lang.Exception()
	  //  java.lang.RuntimeException()
	  //  void test.test.B.print()
	  //  void test.test.A.printIndirect()
	  //  void test.test.Run.main(java.lang.String [])
          int ibegin = 0;
	  if (inVisualAge) {
	    ibegin = fullInfo.lastIndexOf(' ', iend)+1;
          }

	  if(iend == -1) 
	    className = NA;
	  else
	    className = this.fullInfo.substring(ibegin, iend);
	}
      }
      return className;
    }

    /**
       Return the file name of the caller.

       <p>This information is not always available.
    */
    public 
    String getFileName() {
      if(fullInfo == null) return NA;
      
      if(fileName == null) {
	int iend = fullInfo.lastIndexOf(':');	
	if(iend == -1)
	  fileName = NA;
	else {
	  int ibegin = fullInfo.lastIndexOf('(', iend - 1);
	  fileName = this.fullInfo.substring(ibegin + 1, iend);
	}	
      }
      return fileName;
    }

    /**
       Returns the line number of the caller.

       <p>This information is not always available.       
    */
    public 
    String getLineNumber() {
      if(fullInfo == null) return NA;
      
      if(lineNumber == null) {
	int iend = fullInfo.lastIndexOf(')');	
	int ibegin = fullInfo.lastIndexOf(':', iend -1);
	if(ibegin == -1)
	  lineNumber = NA;
	else 
	  lineNumber = this.fullInfo.substring(ibegin + 1, iend);	
      }
      return lineNumber;
    }

    /**
       Returns the method name of the caller.
    */
    public
    String getMethodName() {
      if(fullInfo == null) return NA;            
      if(methodName == null) {
	int iend = fullInfo.lastIndexOf('(');	
	int ibegin = fullInfo.lastIndexOf('.', iend);
	if(ibegin == -1)
	  methodName = NA;
	else
	  methodName = this.fullInfo.substring(ibegin + 1, iend);	
      }
      return methodName;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7165.java
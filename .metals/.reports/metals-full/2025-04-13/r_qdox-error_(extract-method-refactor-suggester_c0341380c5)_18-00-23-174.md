error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4422.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4422.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4422.java
text:
```scala
S@@tring catKey = (cat == Logger.getRootLogger())

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software
 * License version 1.1, a copy of which has been included with this
 * distribution in the LICENSE.txt file.
 */

package org.apache.log4j.config;

import java.io.*;
import java.util.*;
import org.apache.log4j.*;

/**
   Prints the configuration of the log4j default hierarchy
   (which needs to be auto-initialized) as a propoperties file
   on a {@link PrintWriter}.
   
   @author  Anders Kristensen
 */
public class PropertyPrinter implements PropertyGetter.PropertyCallback {
  protected int numAppenders = 0;
  protected Hashtable appenderNames = new Hashtable();
  protected Hashtable layoutNames   = new Hashtable();
  protected PrintWriter out;
  protected boolean doCapitalize;
  
  public
  PropertyPrinter(PrintWriter out) {
    this(out, false);
  }
  
  public
  PropertyPrinter(PrintWriter out, boolean doCapitalize) {
    this.out = out;
    this.doCapitalize = doCapitalize;
    
    //print(out);
    out.flush();
  }
  
  protected
  String genAppName() {
    return "A" + numAppenders++;
  }
  
  /**
     Returns true if the specified appender name is considered to have
     been generated, i.e. if it is of the form A[0-9]+.
  */
  protected
  boolean isGenAppName(String name) {
    if (name.length() < 2 || name.charAt(0) != 'A') return false;
    
    for (int i = 0; i < name.length(); i++) {
      if (name.charAt(i) < '0' || name.charAt(i) > '9') return false;
    }
    return true;
  }
  
  /**
   * Prints the configuration of the default log4j hierarchy as a Java
   * properties file on the specified Writer.
   * 
   * <p>N.B. print() can be invoked only once!
   */
  //public void print(PrintWriter out) {
  //printOptions(out, Category.getRoot());
  //
  //  Enumeration cats = Category.getCurrentCategories();
  //while (cats.hasMoreElements()) {
  //  printOptions(out, (Category) cats.nextElement());
  //}
  //}
  
  protected
  void printOptions(PrintWriter out, Category cat) {
    Enumeration appenders = cat.getAllAppenders();
    Level prio = cat.getLevel();
    String appenderString = (prio == null ? "" : prio.toString());
    
    while (appenders.hasMoreElements()) {
      Appender app = (Appender) appenders.nextElement();
      String name;
      
      if ((name = (String) appenderNames.get(app)) == null) {
      
        // first assign name to the appender
        if ((name = app.getName()) == null || isGenAppName(name)) {
            name = genAppName();
        }
        appenderNames.put(app, name);
        
        printOptions(out, app, "log4j.appender."+name);
        if (app.getLayout() != null) {
          printOptions(out, app.getLayout(), "log4j.appender."+name+".layout");
        }
      }
      appenderString += ", " + name;
    }
    String catKey = (cat == Category.getRoot())
        ? "log4j.rootCategory"
        : "log4j.category." + cat.getName();
    if (appenderString != "") {
      out.println(catKey + "=" + appenderString);
    }
  }
  
  protected
  void printOptions(PrintWriter out, Object obj, String fullname) {
    out.println(fullname + "=" + obj.getClass().getName());
    PropertyGetter.getProperties(obj, this, fullname + ".");
  }
  
  public void foundProperty(Object obj, String prefix, String name, Object value) {
    // XXX: Properties encode value.toString()
    if (obj instanceof Appender && "name".equals(name)) {
      return;
    }
    if (doCapitalize) {
      name = capitalize(name);
    }
    out.println(prefix + name + "=" + value.toString());
  }
  
  public static String capitalize(String name) {
    if (Character.isLowerCase(name.charAt(0))) {
      if (name.length() == 1 || Character.isLowerCase(name.charAt(1))) {
        StringBuffer newname = new StringBuffer(name);
        newname.setCharAt(0, Character.toUpperCase(name.charAt(0)));
        return newname.toString();
      }
    }
    return name;
  }
  
  // for testing
  public static void main(String[] args) {
    new PropertyPrinter(new PrintWriter(System.out));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4422.java
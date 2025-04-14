error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5841.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5841.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5841.java
text:
```scala
I@@BM Corporation - initial API and implementation

/**********************************************************************
Copyright (c) 2002 IBM Corp. and others.
All rights reserved.   This program and the accompanying materials
are made available under the terms of the Common Public License v0.5
which accompanies this distribution, and is available at
http://www.eclipse.org/legal/cpl-v05.html
 
Contributors:
    Philippe Mulet - Initial API and implementation
**********************************************************************/

package org.eclipse.jdt.core.compiler;
 
/**
 * Maps each terminal symbol in the java-grammar into a unique integer. 
 * This integer is used to represent the terminal when computing a parsing action. 
 * 
 * Disclaimer : These constant values are generated automatically using a Java 
 * grammar, therefore their actual values are subject to change if new keywords 
 * were added to the language (i.e. 'assert' keyword in 1.4).
 * 
 * @see IScanner
 * @since 2.0
 */
public interface ITerminalSymbols {

    public final static int

      TokenNameIdentifier = 5,
      TokenNameabstract = 98,
      TokenNameassert = 118,
      TokenNameboolean = 18,
      TokenNamebreak = 119,
      TokenNamebyte = 19,
      TokenNamecase = 211,
      TokenNamecatch = 225,
      TokenNamechar = 20,
      TokenNameclass = 165,
      TokenNamecontinue = 120,
      TokenNamedefault = 212,
      TokenNamedo = 121,
      TokenNamedouble = 21,
      TokenNameelse = 213,
      TokenNameextends = 243,
      TokenNamefalse = 37,
      TokenNamefinal = 99,
      TokenNamefinally = 226,
      TokenNamefloat = 22,
      TokenNamefor = 122,
      TokenNameif = 123,
      TokenNameimplements = 267,
      TokenNameimport = 191,
      TokenNameinstanceof = 65,
      TokenNameint = 23,
      TokenNameinterface = 180,
      TokenNamelong = 24,
      TokenNamenative = 100,
      TokenNamenew = 32,
      TokenNamenull = 38,
      TokenNamepackage = 214,
      TokenNameprivate = 101,
      TokenNameprotected = 102,
      TokenNamepublic = 103,
      TokenNamereturn = 124,
      TokenNameshort = 25,
      TokenNamestatic = 94,
      TokenNamestrictfp = 104,
      TokenNamesuper = 34,
      TokenNameswitch = 125,
      TokenNamesynchronized = 85,
      TokenNamethis = 35,
      TokenNamethrow = 126,
      TokenNamethrows = 227,
      TokenNametransient = 105,
      TokenNametrue = 39,
      TokenNametry = 127,
      TokenNamevoid = 26,
      TokenNamevolatile = 106,
      TokenNamewhile = 117,
      TokenNameIntegerLiteral = 40,
      TokenNameLongLiteral = 41,
      TokenNameFloatingPointLiteral = 42,
      TokenNameDoubleLiteral = 43,
      TokenNameCharacterLiteral = 44,
      TokenNameStringLiteral = 45,
      TokenNamePLUS_PLUS = 1,
      TokenNameMINUS_MINUS = 2,
      TokenNameEQUAL_EQUAL = 33,
      TokenNameLESS_EQUAL = 66,
      TokenNameGREATER_EQUAL = 67,
      TokenNameNOT_EQUAL = 36,
      TokenNameLEFT_SHIFT = 14,
      TokenNameRIGHT_SHIFT = 11,
      TokenNameUNSIGNED_RIGHT_SHIFT = 12,
      TokenNamePLUS_EQUAL = 168,
      TokenNameMINUS_EQUAL = 169,
      TokenNameMULTIPLY_EQUAL = 170,
      TokenNameDIVIDE_EQUAL = 171,
      TokenNameAND_EQUAL = 172,
      TokenNameOR_EQUAL = 173,
      TokenNameXOR_EQUAL = 174,
      TokenNameREMAINDER_EQUAL = 175,
      TokenNameLEFT_SHIFT_EQUAL = 176,
      TokenNameRIGHT_SHIFT_EQUAL = 177,
      TokenNameUNSIGNED_RIGHT_SHIFT_EQUAL = 178,
      TokenNameOR_OR = 80,
      TokenNameAND_AND = 79,
      TokenNamePLUS = 3,
      TokenNameMINUS = 4,
      TokenNameNOT = 71,
      TokenNameREMAINDER = 9,
      TokenNameXOR = 63,
      TokenNameAND = 62,
      TokenNameMULTIPLY = 8,
      TokenNameOR = 70,
      TokenNameTWIDDLE = 72,
      TokenNameDIVIDE = 10,
      TokenNameGREATER = 68,
      TokenNameLESS = 69,
      TokenNameLPAREN = 7,
      TokenNameRPAREN = 86,
      TokenNameLBRACE = 110,
      TokenNameRBRACE = 95,
      TokenNameLBRACKET = 15,
      TokenNameRBRACKET = 166,
      TokenNameSEMICOLON = 64,
      TokenNameQUESTION = 81,
      TokenNameCOLON = 154,
      TokenNameCOMMA = 90,
      TokenNameDOT = 6,
      TokenNameEQUAL = 167,
      TokenNameEOF = 158,
      TokenNameERROR = 307;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5841.java
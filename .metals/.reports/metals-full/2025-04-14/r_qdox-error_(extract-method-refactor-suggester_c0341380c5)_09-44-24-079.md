error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17311.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17311.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17311.java
text:
```scala
n@@ew TimeZoneInfo("UTC", "Universal Coordinated Time", "GMT",        0 * ONE_HOUR + 0 * THIRTY_MIN),

/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.zxing.web.generator.client;

/**
 * A class containing a list of timezones, with their full names, and time
 * offset.
 * 
 * @author Yohann Coppel
 */
public class TimeZoneList {
  public static class TimeZoneInfo {
    String abreviation;
    String longName;
    String GMTRelative;
    long gmtDiff;
    public TimeZoneInfo(String abreviation, String longName, String relative, long gmtDiff) {
      super();
      GMTRelative = relative;
      this.abreviation = abreviation;
      this.gmtDiff = gmtDiff;
      this.longName = longName;
    }
  }
  
  private static final long ONE_HOUR = 60L*60*1000;
  private static final long THIRTY_MIN = 30L*60*1000;
  
  public static final TimeZoneInfo[] TIMEZONES = {
    new TimeZoneInfo("GMT", "Greenwich Mean Time", "GMT",               0 * ONE_HOUR + 0 * THIRTY_MIN), // 0
    new TimeZoneInfo("UTC", "Universal Coordinated Time", "GMT",        1 * ONE_HOUR + 0 * THIRTY_MIN),
    new TimeZoneInfo("ECT", "European Central Time", "GMT+1:00",        1 * ONE_HOUR + 0 * THIRTY_MIN),
    new TimeZoneInfo("EET", "Eastern European Time", "GMT+2:00",        2 * ONE_HOUR + 0 * THIRTY_MIN),
    new TimeZoneInfo("ART", "(Arabic) Egypt Standard Time", "GMT+2:00", 2 * ONE_HOUR + 0 * THIRTY_MIN),
    new TimeZoneInfo("EAT", "Eastern African Time", "GMT+3:00",         3 * ONE_HOUR + 0 * THIRTY_MIN), // 5
    new TimeZoneInfo("MET", "Middle East Time", "GMT+3:30",             3 * ONE_HOUR + 1 * THIRTY_MIN),
    new TimeZoneInfo("NET", "Near East Time", "GMT+4:00",               4 * ONE_HOUR + 0 * THIRTY_MIN),
    new TimeZoneInfo("PLT", "Pakistan Lahore Time", "GMT+5:00",         5 * ONE_HOUR + 0 * THIRTY_MIN),
    new TimeZoneInfo("IST", "India Standard Time", "GMT+5:30",          5 * ONE_HOUR + 1 * THIRTY_MIN),
    new TimeZoneInfo("BST", "Bangladesh Standard Time", "GMT+6:00",     6 * ONE_HOUR + 0 * THIRTY_MIN), // 10
    new TimeZoneInfo("VST", "Vietnam Standard Time", "GMT+7:00",        7 * ONE_HOUR + 0 * THIRTY_MIN),
    new TimeZoneInfo("CTT", "China Taiwan Time", "GMT+8:00",            8 * ONE_HOUR + 0 * THIRTY_MIN),
    new TimeZoneInfo("JST", "Japan Standard Time", "GMT+9:00",          9 * ONE_HOUR + 0 * THIRTY_MIN),
    new TimeZoneInfo("ACT", "Australia Central Time", "GMT+9:30",       9 * ONE_HOUR + 1 * THIRTY_MIN),
    new TimeZoneInfo("AET", "Australia Eastern Time", "GMT+10:00",     10 * ONE_HOUR + 0 * THIRTY_MIN), // 15
    new TimeZoneInfo("SST", "Solomon Standard Time", "GMT+11:00",      11 * ONE_HOUR + 0 * THIRTY_MIN),
    new TimeZoneInfo("NST", "New Zealand Standard Time", "GMT+12:00",  12 * ONE_HOUR + 0 * THIRTY_MIN),
    new TimeZoneInfo("MIT", "Midway Islands Time", "GMT-11:00",       -11 * ONE_HOUR - 0 * THIRTY_MIN),
    new TimeZoneInfo("HST", "Hawaii Standard Time", "GMT-10:00",      -10 * ONE_HOUR - 0 * THIRTY_MIN),
    new TimeZoneInfo("AST", "Alaska Standard Time", "GMT-9:00",        -9 * ONE_HOUR - 0 * THIRTY_MIN), // 20
    new TimeZoneInfo("PST", "Pacific Standard Time", "GMT-8:00",       -8 * ONE_HOUR - 0 * THIRTY_MIN),
    new TimeZoneInfo("PNT", "Phoenix Standard Time", "GMT-7:00",       -7 * ONE_HOUR - 0 * THIRTY_MIN),
    new TimeZoneInfo("MST", "Mountain Standard Time", "GMT-7:00",      -7 * ONE_HOUR - 0 * THIRTY_MIN),
    new TimeZoneInfo("CST", "Central Standard Time", "GMT-6:00",       -6 * ONE_HOUR - 0 * THIRTY_MIN),
    new TimeZoneInfo("EST", "Eastern Standard Time", "GMT-5:00",       -5 * ONE_HOUR - 0 * THIRTY_MIN), // 25
    new TimeZoneInfo("IET", "Indiana Eastern Standard Time", "GMT-5:00", -5 * ONE_HOUR - 0 * THIRTY_MIN),
    new TimeZoneInfo("PRT", "Puerto Rico and US Virgin Islands Time", "GMT-4:00", -4 * ONE_HOUR - 0 * THIRTY_MIN),
    new TimeZoneInfo("CNT", "Canada Newfoundland Time", "GMT-3:30",    -3 * ONE_HOUR - 1 * THIRTY_MIN),
    new TimeZoneInfo("AGT", "Argentina Standard Time", "GMT-3:00",     -3 * ONE_HOUR - 0 * THIRTY_MIN),
    new TimeZoneInfo("BET", "Brazil Eastern Time", "GMT-3:00",         -3 * ONE_HOUR - 0 * THIRTY_MIN), // 30
    new TimeZoneInfo("CAT", "Central African Time", "GMT-1:00",        -1 * ONE_HOUR - 0 * THIRTY_MIN),
  };
  
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17311.java
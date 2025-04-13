error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/417.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/417.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/417.java
text:
```scala
public static v@@oid main(String args[]) {

package org.apache.lucene.analysis.icu;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import com.ibm.icu.text.UnicodeSet;
import com.ibm.icu.text.UnicodeSetIterator;
import com.ibm.icu.util.VersionInfo;

/** creates a macro to augment jflex's unicode wordbreak support for > BMP */
public class GenerateJFlexSupplementaryMacros {
  private static final UnicodeSet BMP = new UnicodeSet("[\u0000-\uFFFF]");
  private static final String NL = System.getProperty("line.separator");
  private static final DateFormat DATE_FORMAT = DateFormat.getDateTimeInstance
    (DateFormat.FULL, DateFormat.FULL, Locale.US);
  static {
    DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
  }
  
  private static final String APACHE_LICENSE 
    = "/*" + NL
      + " * Copyright 2010 The Apache Software Foundation." + NL
      + " *" + NL
      + " * Licensed under the Apache License, Version 2.0 (the \"License\");" + NL
      + " * you may not use this file except in compliance with the License." + NL
      + " * You may obtain a copy of the License at" + NL
      + " *" + NL
      + " *      http://www.apache.org/licenses/LICENSE-2.0" + NL
      + " *" + NL
      + " * Unless required by applicable law or agreed to in writing, software" + NL
      + " * distributed under the License is distributed on an \"AS IS\" BASIS," + NL
      + " * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied." + NL
      + " * See the License for the specific language governing permissions and" + NL
      + " * limitations under the License." + NL
      + " */" + NL + NL;
    
  
  public static void main(String args[]) throws Exception {
    outputHeader();
    outputMacro("ALetterSupp",         "[:WordBreak=ALetter:]");
    outputMacro("FormatSupp",          "[:WordBreak=Format:]");
    outputMacro("ExtendSupp",          "[:WordBreak=Extend:]");
    outputMacro("NumericSupp",         "[:WordBreak=Numeric:]");
    outputMacro("KatakanaSupp",        "[:WordBreak=Katakana:]");
    outputMacro("MidLetterSupp",       "[:WordBreak=MidLetter:]");
    outputMacro("MidNumSupp",          "[:WordBreak=MidNum:]");
    outputMacro("MidNumLetSupp",       "[:WordBreak=MidNumLet:]");
    outputMacro("ExtendNumLetSupp",    "[:WordBreak=ExtendNumLet:]");
    outputMacro("ExtendNumLetSupp",    "[:WordBreak=ExtendNumLet:]");
    outputMacro("ComplexContextSupp",  "[:LineBreak=Complex_Context:]");
    outputMacro("HanSupp",             "[:Script=Han:]");
    outputMacro("HiraganaSupp",        "[:Script=Hiragana:]");
  }
  
  static void outputHeader() {
    System.out.print(APACHE_LICENSE);
    System.out.print("// Generated using ICU4J " + VersionInfo.ICU_VERSION.toString() + " on ");
    System.out.println(DATE_FORMAT.format(new Date()));
    System.out.println("// by " + GenerateJFlexSupplementaryMacros.class.getName());
    System.out.print(NL + NL);
  }
  
  // we have to carefully output the possibilities as compact utf-16
  // range expressions, or jflex will OOM!
  static void outputMacro(String name, String pattern) {
    UnicodeSet set = new UnicodeSet(pattern);
    set.removeAll(BMP);
    System.out.println(name + " = (");
    // if the set is empty, we have to do this or jflex will barf
    if (set.isEmpty()) {
      System.out.println("\t  []");
    }
    
    HashMap<Character,UnicodeSet> utf16ByLead = new HashMap<Character,UnicodeSet>();
    for (UnicodeSetIterator it = new UnicodeSetIterator(set); it.next();) {    
      char utf16[] = Character.toChars(it.codepoint);
      UnicodeSet trails = utf16ByLead.get(utf16[0]);
      if (trails == null) {
        trails = new UnicodeSet();
        utf16ByLead.put(utf16[0], trails);
      }
      trails.add(utf16[1]);
    }
    
    boolean isFirst = true;
    for (Character c : utf16ByLead.keySet()) {
      UnicodeSet trail = utf16ByLead.get(c);
      System.out.print( isFirst ? "\t  " : "\t| ");
      isFirst = false;
      System.out.println("([\\u" + Integer.toHexString(c) + "]" + trail.getRegexEquivalent() + ")");
    }
    System.out.println(")");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/417.java
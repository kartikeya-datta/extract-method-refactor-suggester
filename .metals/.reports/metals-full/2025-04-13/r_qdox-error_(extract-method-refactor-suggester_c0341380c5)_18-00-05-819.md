error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2101.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2101.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,24]

error in qdox parser
file content:
```java
offset: 24
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2101.java
text:
```scala
protected synchronized D@@ocData getNextDocData() throws NoMoreDataException, Exception {

package org.apache.lucene.benchmark.byTask.feeds;

/**
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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.zip.GZIPInputStream;

import org.apache.lucene.benchmark.byTask.utils.Config;


/**
 * A DocMaker using the (compressed) Trec collection for its input.
 * <p>
 * Config properties:<ul>
 * <li>work.dir=&lt;path to the root of docs and indexes dirs| Default: work&gt;</li>
 * <li>docs.dir=&lt;path to the docs dir| Default: trec&gt;</li>
 * </ul>
 */
public class TrecDocMaker extends BasicDocMaker {

  private static final String newline = System.getProperty("line.separator");
  
  private ThreadLocal dateFormat = new ThreadLocal();
  private File dataDir = null;
  private ArrayList inputFiles = new ArrayList();
  private int nextFile = 0;
  private int iteration=0;
  private BufferedReader reader;
  private GZIPInputStream zis;
  
  private static final String DATE_FORMATS [] = {
    "EEE, dd MMM yyyy kk:mm:ss z", //Tue, 09 Dec 2003 22:39:08 GMT
    "EEE MMM dd kk:mm:ss yyyy z",  //Tue Dec 09 16:45:08 2003 EST
    "EEE, dd-MMM-':'y kk:mm:ss z", //Tue, 09 Dec 2003 22:39:08 GMT
    "EEE, dd-MMM-yyy kk:mm:ss z", //Tue, 09 Dec 2003 22:39:08 GMT
  };
  
  /* (non-Javadoc)
   * @see SimpleDocMaker#setConfig(java.util.Properties)
   */
  public void setConfig(Config config) {
    super.setConfig(config);
    File workDir = new File(config.get("work.dir","work"));
    String d = config.get("docs.dir","trec");
    dataDir = new File(workDir,d);
    collectFiles(dataDir,inputFiles);
    if (inputFiles.size()==0) {
      throw new RuntimeException("No txt files in dataDir: "+dataDir.getAbsolutePath());
    }
 }

  private void openNextFile() throws NoMoreDataException, Exception {
    closeInputs();
    int retries = 0;
    while (true) {
      File f = null;
      synchronized (this) {
        if (nextFile >= inputFiles.size()) { 
          // exhausted files, start a new round, unless forever set to false.
          if (!forever) {
            throw new NoMoreDataException();
          }
          nextFile = 0;
          iteration++;
        }
        f = (File) inputFiles.get(nextFile++);
      }
      System.out.println("opening: "+f+" length: "+f.length());
      try {
        zis = new GZIPInputStream(new BufferedInputStream(new FileInputStream(f)));
        reader = new BufferedReader(new InputStreamReader(zis));
        return;
      } catch (Exception e) {
        retries++;
        if (retries<20) {
          System.out.println("Skipping 'bad' file "+f.getAbsolutePath()+"  #retries="+retries);
          continue;
        } else {
          throw new NoMoreDataException();
        }
      }
    }
  }

  private void closeInputs() {
    if (zis!=null) {
      try {
        zis.close();
      } catch (IOException e) {
        System.out.println("closeInputs(): Ingnoring error: "+e);
        e.printStackTrace();
      }
      zis = null;
    }
    if (reader!=null) { 
      try {
        reader.close();
      } catch (IOException e) {
        System.out.println("closeInputs(): Ingnoring error: "+e);
        e.printStackTrace();
      }
      reader = null;
    }
  }
  
  // read until finding a line that starts with the specified prefix
  private StringBuffer read (String prefix, StringBuffer sb, boolean collectMatchLine, boolean collectAll) throws Exception {
    sb = (sb==null ? new StringBuffer() : sb);
    String sep = "";
    while (true) {
      String line = reader.readLine();
      if (line==null) {
        openNextFile();
        continue;
      }
      if (line.startsWith(prefix)) {
        if (collectMatchLine) {
          sb.append(sep+line);
          sep = newline;
        }
        break;
      }
      if (collectAll) {
        sb.append(sep+line);
        sep = newline;
      }
    }
    //System.out.println("read: "+sb);
    return sb;
  }
  
  protected DocData getNextDocData() throws NoMoreDataException, Exception {
    if (reader==null) {
      openNextFile();
    }
    // 1. skip until doc start
    read("<DOC>",null,false,false); 
    // 2. name
    StringBuffer sb = read("<DOCNO>",null,true,false);
    String name = sb.substring("<DOCNO>".length());
    name = name.substring(0,name.indexOf("</DOCNO>"))+"_"+iteration;
    // 3. skip until doc header
    read("<DOCHDR>",null,false,false); 
    // 4. date
    sb = read("Date: ",null,true,false);
    String dateStr = sb.substring("Date: ".length());
    // 5. skip until end of doc header
    read("</DOCHDR>",null,false,false); 
    // 6. collect until end of doc
    sb = read("</DOC>",null,false,true);
    // this is the next document, so parse it 
    Date date = parseDate(dateStr);
    HTMLParser p = getHtmlParser();
    DocData docData = p.parse(name, date, sb, getDateFormat(0));
    addBytes(sb.length()); // count char length of parsed html text (larger than the plain doc body text). 
    
    return docData;
  }

  private DateFormat getDateFormat(int n) {
    DateFormat df[] = (DateFormat[]) dateFormat.get();
    if (df == null) {
      df = new SimpleDateFormat[DATE_FORMATS.length];
      for (int i = 0; i < df.length; i++) {
        df[i] = new SimpleDateFormat(DATE_FORMATS[i],Locale.US);
        df[i].setLenient(true);
      }
      dateFormat.set(df);
    }
    return df[n];
  }

  private Date parseDate(String dateStr) {
    Date date = null;
    for (int i=0; i<DATE_FORMATS.length; i++) {
      try {
        date = getDateFormat(i).parse(dateStr.trim());
        return date;
      } catch (ParseException e) {
      }
    }
    // do not fail test just because a date could not be parsed
    System.out.println("ignoring date parse exception (assigning 'now') for: "+dateStr);
    date = new Date(); // now 
    return date;
  }


  /*
   *  (non-Javadoc)
   * @see DocMaker#resetIinputs()
   */
  public synchronized void resetInputs() {
    super.resetInputs();
    closeInputs();
    nextFile = 0;
    iteration = 0;
  }

  /*
   *  (non-Javadoc)
   * @see DocMaker#numUniqueTexts()
   */
  public int numUniqueTexts() {
    return inputFiles.size();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2101.java
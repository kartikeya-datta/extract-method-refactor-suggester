error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9359.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9359.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9359.java
text:
```scala
o@@f.parseBytes(contents.getBytes()); // TODO - charset?

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.jmeter.monitor.model.benchmark;

public class ParseBenchmark {

    /**
     * 
     */
    public ParseBenchmark() {
        super();
    }

    public static void main(String[] args) {
        if (args.length == 3) {
            int parser = 0;
            String file = null;
            int loops = 1000;
            if (args[0] != null) {
                if (!args[0].equals("jaxb")) {
                    parser = 1;
                }
            }
            if (args[1] != null) {
                file = args[1];
            }
            if (args[2] != null) {
                loops = Integer.parseInt(args[2]);
            }

            java.io.File infile = new java.io.File(file);
            java.io.FileInputStream fis = null;
            java.io.InputStreamReader isr = null;
            StringBuilder buf = new StringBuilder();
            try {
                fis = new java.io.FileInputStream(infile);
                isr = new java.io.InputStreamReader(fis);
                java.io.BufferedReader br = new java.io.BufferedReader(isr);
                String line = null;
                while ((line = br.readLine()) != null) {
                    buf.append(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            long start = 0;
            long end = 0;
            String contents = buf.toString().trim();
            System.out.println("start test: " + loops + " iterations");
            System.out.println("content:");
            System.out.println(contents);

            if (parser == 0) {
                /**
                 * try { JAXBContext jxbc = new
                 * org.apache.jorphan.tomcat.manager.ObjectFactory();
                 * Unmarshaller mar = jxbc.createUnmarshaller();
                 * 
                 * start = System.currentTimeMillis(); for (int idx=0; idx <
                 * loops; idx++){ StreamSource ss = new StreamSource( new
                 * ByteArrayInputStream(contents.getBytes())); Object ld =
                 * mar.unmarshal(ss); } end = System.currentTimeMillis();
                 * System.out.println("elapsed Time: " + (end - start)); } catch
                 * (JAXBException e){ }
                 */
            } else {
                org.apache.jmeter.monitor.model.ObjectFactory of = org.apache.jmeter.monitor.model.ObjectFactory
                        .getInstance();
                start = System.currentTimeMillis();
                for (int idx = 0; idx < loops; idx++) {
                    // NOTUSED org.apache.jmeter.monitor.model.Status st =
                    of.parseBytes(contents.getBytes());
                }
                end = System.currentTimeMillis();
                System.out.println("elapsed Time: " + (end - start));
            }

        } else {
            System.out.println("missing paramters:");
            System.out.println("parser file iterations");
            System.out.println("example: jaxb status.xml 1000");
        }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9359.java
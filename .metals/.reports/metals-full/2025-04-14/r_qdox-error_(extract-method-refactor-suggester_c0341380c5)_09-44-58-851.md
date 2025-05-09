error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1366.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1366.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,46]

error in qdox parser
file content:
```java
offset: 46
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1366.java
text:
```scala
"The delimiter for outputting the dictionary")@@.withShortName("l").create();

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

package org.apache.mahout.utils.vectors.lucene;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.builder.ArgumentBuilder;
import org.apache.commons.cli2.builder.DefaultOptionBuilder;
import org.apache.commons.cli2.builder.GroupBuilder;
import org.apache.commons.cli2.commandline.Parser;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.mahout.common.CommandLineUtil;
import org.apache.mahout.math.VectorWritable;
import org.apache.mahout.utils.vectors.TermInfo;
import org.apache.mahout.utils.vectors.io.JWriterTermInfoWriter;
import org.apache.mahout.utils.vectors.io.JWriterVectorWriter;
import org.apache.mahout.utils.vectors.io.SequenceFileVectorWriter;
import org.apache.mahout.utils.vectors.io.VectorWriter;
import org.apache.mahout.vectorizer.TF;
import org.apache.mahout.vectorizer.TFIDF;
import org.apache.mahout.vectorizer.Weight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Driver {
  private static final Logger log = LoggerFactory.getLogger(Driver.class);

  private Driver() { }

  public static void main(String[] args) throws IOException {

    DefaultOptionBuilder obuilder = new DefaultOptionBuilder();
    ArgumentBuilder abuilder = new ArgumentBuilder();
    GroupBuilder gbuilder = new GroupBuilder();

    Option inputOpt = obuilder.withLongName("dir").withRequired(true).withArgument(
      abuilder.withName("dir").withMinimum(1).withMaximum(1).create())
        .withDescription("The Lucene directory").withShortName("d").create();

    Option outputOpt = obuilder.withLongName("output").withRequired(true).withArgument(
      abuilder.withName("output").withMinimum(1).withMaximum(1).create()).withDescription("The output file")
        .withShortName("o").create();

    Option fieldOpt = obuilder.withLongName("field").withRequired(true).withArgument(
      abuilder.withName("field").withMinimum(1).withMaximum(1).create()).withDescription(
      "The field in the index").withShortName("f").create();

    Option idFieldOpt = obuilder.withLongName("idField").withRequired(false).withArgument(
      abuilder.withName("idField").withMinimum(1).withMaximum(1).create()).withDescription(
      "The field in the index containing the index.  If null, then the Lucene internal doc "
          + "id is used which is prone to error if the underlying index changes").withShortName("i").create();

    Option dictOutOpt = obuilder.withLongName("dictOut").withRequired(true).withArgument(
      abuilder.withName("dictOut").withMinimum(1).withMaximum(1).create()).withDescription(
      "The output of the dictionary").withShortName("t").create();

    Option weightOpt = obuilder.withLongName("weight").withRequired(false).withArgument(
      abuilder.withName("weight").withMinimum(1).withMaximum(1).create()).withDescription(
      "The kind of weight to use. Currently TF or TFIDF").withShortName("w").create();

    Option delimiterOpt = obuilder.withLongName("delimiter").withRequired(false).withArgument(
      abuilder.withName("delimiter").withMinimum(1).withMaximum(1).create()).withDescription(
      "The delimiter for outputing the dictionary").withShortName("l").create();

    Option powerOpt = obuilder.withLongName("norm").withRequired(false).withArgument(
      abuilder.withName("norm").withMinimum(1).withMaximum(1).create()).withDescription(
      "The norm to use, expressed as either a double or \"INF\" if you want to use the Infinite norm.  "
          + "Must be greater or equal to 0.  The default is not to normalize").withShortName("n").create();

    Option maxOpt = obuilder.withLongName("max").withRequired(false).withArgument(
      abuilder.withName("max").withMinimum(1).withMaximum(1).create()).withDescription(
      "The maximum number of vectors to output.  If not specified, then it will loop over all docs")
        .withShortName("m").create();

    Option outWriterOpt = obuilder.withLongName("outputWriter").withRequired(false).withArgument(
      abuilder.withName("outputWriter").withMinimum(1).withMaximum(1).create()).withDescription(
      "The VectorWriter to use, either seq "
          + "(SequenceFileVectorWriter - default) or file (Writes to a File using JSON format)")
        .withShortName("e").create();

    Option minDFOpt = obuilder.withLongName("minDF").withRequired(false).withArgument(
      abuilder.withName("minDF").withMinimum(1).withMaximum(1).create()).withDescription(
      "The minimum document frequency.  Default is 1").withShortName("md").create();

    Option maxDFPercentOpt = obuilder.withLongName("maxDFPercent").withRequired(false).withArgument(
      abuilder.withName("maxDFPercent").withMinimum(1).withMaximum(1).create()).withDescription(
      "The max percentage of docs for the DF.  Can be used to remove really high frequency terms."
          + "  Expressed as an integer between 0 and 100. Default is 99.").withShortName("x").create();

    Option helpOpt = obuilder.withLongName("help").withDescription("Print out help").withShortName("h")
        .create();

    Group group = gbuilder.withName("Options").withOption(inputOpt).withOption(idFieldOpt).withOption(
      outputOpt).withOption(delimiterOpt).withOption(helpOpt).withOption(fieldOpt).withOption(maxOpt)
        .withOption(dictOutOpt).withOption(powerOpt).withOption(outWriterOpt).withOption(maxDFPercentOpt)
        .withOption(weightOpt).withOption(minDFOpt).create();

    try {
      Parser parser = new Parser();
      parser.setGroup(group);
      CommandLine cmdLine = parser.parse(args);

      if (cmdLine.hasOption(helpOpt)) {
        
        CommandLineUtil.printHelp(group);
        return;
      }
      // Springify all this
      if (cmdLine.hasOption(inputOpt)) { // Lucene case
        File file = new File(cmdLine.getValue(inputOpt).toString());
        if (!file.isDirectory()) {
          throw new IllegalArgumentException("Lucene directory: " + file.getAbsolutePath()
              +  " does not exist or is not a directory");
        }

        long maxDocs = Long.MAX_VALUE;
        if (cmdLine.hasOption(maxOpt)) {
          maxDocs = Long.parseLong(cmdLine.getValue(maxOpt).toString());
        }
        if (maxDocs < 0) {
          throw new IllegalArgumentException("maxDocs must be >= 0");
        }

        Directory dir = FSDirectory.open(file);
        IndexReader reader = IndexReader.open(dir, true);

        Weight weight;
        if (cmdLine.hasOption(weightOpt)) {
          String wString = cmdLine.getValue(weightOpt).toString();
          if ("tf".equalsIgnoreCase(wString)) {
            weight = new TF();
          } else if ("tfidf".equalsIgnoreCase(wString)) {
            weight = new TFIDF();
          } else {
            throw new OptionException(weightOpt);
          }
        } else {
          weight = new TFIDF();
        }

        String field = cmdLine.getValue(fieldOpt).toString();

        int minDf = 1;
        if (cmdLine.hasOption(minDFOpt)) {
          minDf = Integer.parseInt(cmdLine.getValue(minDFOpt).toString());
        }

        int maxDFPercent = 99;
        if (cmdLine.hasOption(maxDFPercentOpt)) {
          maxDFPercent = Integer.parseInt(cmdLine.getValue(maxDFPercentOpt).toString());
        }

        TermInfo termInfo = new CachedTermInfo(reader, field, minDf, maxDFPercent);
        VectorMapper mapper = new TFDFMapper(reader, weight, termInfo);

        double norm = LuceneIterable.NO_NORMALIZING;
        if (cmdLine.hasOption(powerOpt)) {
          String power = cmdLine.getValue(powerOpt).toString();
          if ("INF".equals(power)) {
            norm = Double.POSITIVE_INFINITY;
          } else {
            norm = Double.parseDouble(power);
          }
        }

        String idField = null;
        if (cmdLine.hasOption(idFieldOpt)) {
          idField = cmdLine.getValue(idFieldOpt).toString();
        }

        LuceneIterable iterable;
        if (norm == LuceneIterable.NO_NORMALIZING) {
          iterable = new LuceneIterable(reader, idField, field, mapper, LuceneIterable.NO_NORMALIZING);
        } else {
          iterable = new LuceneIterable(reader, idField, field, mapper, norm);
        }

        String outFile = cmdLine.getValue(outputOpt).toString();
        log.info("Output File: {}", outFile);

        VectorWriter vectorWriter;
        if (cmdLine.hasOption(outWriterOpt)) {
          String outWriter = cmdLine.getValue(outWriterOpt).toString();
          if ("file".equals(outWriter)) {
            BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
            vectorWriter = new JWriterVectorWriter(writer);
          } else {
            vectorWriter = getSeqFileWriter(outFile);
          }
        } else {
          vectorWriter = getSeqFileWriter(outFile);
        }

        long numDocs = vectorWriter.write(iterable, maxDocs);
        vectorWriter.close();
        log.info("Wrote: {} vectors", numDocs);

        String delimiter = cmdLine.hasOption(delimiterOpt) ? cmdLine.getValue(delimiterOpt).toString() : "\t";
        
        File dictOutFile = new File(cmdLine.getValue(dictOutOpt).toString());
        log.info("Dictionary Output file: {}", dictOutFile);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
            new FileOutputStream(dictOutFile), Charset.forName("UTF8")));
        JWriterTermInfoWriter tiWriter = new JWriterTermInfoWriter(writer, delimiter, field);
        tiWriter.write(termInfo);
        tiWriter.close();
        writer.close();

      }
    } catch (OptionException e) {
      log.error("Exception", e);
      CommandLineUtil.printHelp(group);
    }
  }

  private static VectorWriter getSeqFileWriter(String outFile) throws IOException {
    Path path = new Path(outFile);
    Configuration conf = new Configuration();
    FileSystem fs = FileSystem.get(conf);
    // TODO: Make this parameter driven

    SequenceFile.Writer seqWriter = SequenceFile.createWriter(fs, conf, path, LongWritable.class,
      VectorWritable.class);

    return new SequenceFileVectorWriter(seqWriter);
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1366.java
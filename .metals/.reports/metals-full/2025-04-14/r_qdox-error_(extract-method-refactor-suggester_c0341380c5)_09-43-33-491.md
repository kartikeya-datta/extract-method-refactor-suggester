error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11266.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11266.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11266.java
text:
```scala
r@@eturn path.contains(":\\") || path.startsWith("\\\\");

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.as.cli.handlers;

import java.io.File;
import java.util.List;

import org.jboss.as.cli.CommandContext;
import org.jboss.as.cli.Util;

/**
 *
 * @author Alexey Loubyansky
 */
public class WindowsFilenameTabCompleter extends FilenameTabCompleter {


   public WindowsFilenameTabCompleter(CommandContext ctx) {
        super(ctx);
    }

/* (non-Javadoc)
    * @see org.jboss.as.cli.CommandLineCompleter#complete(org.jboss.as.cli.CommandContext,
    * java.lang.String, int, java.util.List)
    */
   @Override
   public int complete(CommandContext ctx, String buffer, int cursor, List<String> candidates) {

       boolean openQuote = false;
       boolean dontCloseQuote = false;
       if(buffer.length() >= 2 && buffer.charAt(0) == '"') {
           int lastQuote = buffer.lastIndexOf('"');
           if(lastQuote >= 0) {
               StringBuilder buf = new StringBuilder();
               buf.append(buffer.substring(1, lastQuote));
               if(lastQuote != buffer.length() - 1) {
                   buf.append(buffer.substring(lastQuote + 1));
               }
               buffer = buf.toString();
               openQuote = true;
               dontCloseQuote = cursor <= lastQuote;
           }
       }

       int result = getCandidates(buffer, candidates);

       final String path;
       if(buffer.length() == 0) {
           path = null;
       } else {
           final int lastSeparator = buffer.lastIndexOf(File.separatorChar);
           if(lastSeparator > 0) {
               path = buffer.substring(0, lastSeparator + 1);
           } else {
               path = null;
           }
       }

       if(path != null && !openQuote) {
           openQuote = path.indexOf(' ') >= 0;
      }

       if(candidates.size() == 1) {
           final String candidate = candidates.get(0);
           if(!openQuote) {
               openQuote = candidate.indexOf(' ') >= 0;
           }
           if(openQuote) {
               StringBuilder buf = new StringBuilder();
               buf.append('"');
               if(path != null) {
                   buf.append(path);
               }
               buf.append(candidate);
               if(!dontCloseQuote) {
                   buf.append('"');
               }
               candidates.set(0, buf.toString());
           }
       } else {
           final String common = Util.getCommonStart(candidates);
           if(!openQuote && common != null) {
               openQuote = common.indexOf(' ') >= 0;
           }
           if(openQuote) {
               for(int i = 0; i < candidates.size(); ++i) {
                   StringBuilder buf = new StringBuilder();
                   buf.append('"');
                   if(path != null) {
                       buf.append(path);
                   }

                   if(common == null) {
                       if(!dontCloseQuote) {
                           buf.append('"');
                       }
                       buf.append(candidates.get(i));
                   } else {
                       buf.append(common);
                       if(!dontCloseQuote) {
                           buf.append('"');
                       }
                       buf.append(candidates.get(i).substring(common.length()));
                   }

                   candidates.set(i, buf.toString());
               }
           }
       }

       if(openQuote) {
           return 0;
       }

       return result;
   }

   @Override
   protected boolean startsWithRoot(String path) {
       return path.contains(":\\");
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11266.java
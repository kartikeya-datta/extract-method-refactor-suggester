error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/952.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/952.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/952.java
text:
```scala
S@@tring argsStr = ctx.getArgumentsString();

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
package org.jboss.as.cli.handlers.batch;

import java.util.List;

import org.jboss.as.cli.CommandLineCompleter;
import org.jboss.as.cli.CommandContext;
import org.jboss.as.cli.batch.Batch;
import org.jboss.as.cli.batch.BatchManager;
import org.jboss.as.cli.batch.BatchedCommand;
import org.jboss.as.cli.handlers.CommandHandlerWithHelp;
import org.jboss.as.cli.operation.OperationFormatException;

/**
 *
 * @author Alexey Loubyansky
 */
public class BatchEditLineHandler extends CommandHandlerWithHelp {

    public BatchEditLineHandler() {
        super("batch-edit-line", new CommandLineCompleter() {
            @Override
            public int complete(CommandContext ctx, String buffer, int cursor, List<String> candidates) {

                final BatchManager batchManager = ctx.getBatchManager();
                if(!batchManager.isBatchActive()) {
                    return -1;
                }

                int nextCharIndex = 0;
                while (nextCharIndex < buffer.length()) {
                    if (!Character.isWhitespace(buffer.charAt(nextCharIndex))) {
                        break;
                    }
                    ++nextCharIndex;
                }

                if(nextCharIndex == buffer.length()) {
                    candidates.add("--help");
                    return nextCharIndex;
                }

                int nextWsIndex = nextCharIndex + 1;
                while(nextWsIndex < buffer.length()) {
                    if(Character.isWhitespace(buffer.charAt(nextWsIndex))) {
                        break;
                    }
                    ++nextWsIndex;
                }

                if(nextWsIndex == buffer.length()) {
                    return -1;
                }

                String lineNumberStr = buffer.substring(nextCharIndex, nextWsIndex);
                if("--help".startsWith(lineNumberStr)) {
                    candidates.add("--help");
                    return nextCharIndex;
                }

                final int lineNumber;
                try {
                    lineNumber = Integer.parseInt(lineNumberStr);
                } catch(NumberFormatException e) {
                    return -1;
                }

                final Batch batch = batchManager.getActiveBatch();
                int batchSize = batch.size();

                if(lineNumber < 1 || lineNumber > batchSize) {
                    return -1;
                }

                nextCharIndex = nextWsIndex + 1;
                while (nextCharIndex < buffer.length()) {
                    if (!Character.isWhitespace(buffer.charAt(nextCharIndex))) {
                        break;
                    }
                    ++nextCharIndex;
                }

                String cmd = buffer.substring(nextCharIndex);
                if("--help".startsWith(cmd)) {
                    candidates.add("--help");
                }

                int cmdResult = ctx.getDefaultCommandCompleter().complete(ctx, cmd, 0, candidates);

                final String batchedCmd = batch.getCommands().get(lineNumber - 1).getCommand();
                if(cmd.isEmpty() || batchedCmd.startsWith(cmd)) {
                    int lastWsIndex = cmd.lastIndexOf(' ');
                    if(lastWsIndex > 0) {
                        candidates.add(batchedCmd.substring(lastWsIndex + 1).trim());
                    } else {
                        candidates.add(batchedCmd);
                    }
                }

                if(cmdResult < 0) {
                    return candidates.isEmpty() ? -1 : nextCharIndex;
                }
                return nextCharIndex + cmdResult;
            }});
    }

    @Override
    public boolean isAvailable(CommandContext ctx) {
        if(!super.isAvailable(ctx)) {
            return false;
        }
        return ctx.isBatchMode();
    }

    /* (non-Javadoc)
     * @see org.jboss.as.cli.handlers.CommandHandlerWithHelp#doHandle(org.jboss.as.cli.CommandContext)
     */
    @Override
    protected void doHandle(CommandContext ctx) {

        BatchManager batchManager = ctx.getBatchManager();
        if(!batchManager.isBatchActive()) {
            ctx.printLine("No active batch.");
            return;
        }

        Batch batch = batchManager.getActiveBatch();
        final int batchSize = batch.size();
        if(batchSize == 0) {
            ctx.printLine("The batch is empty.");
            return;
        }

        String argsStr = ctx.getCommandArguments();
        if(argsStr == null) {
            ctx.printLine("Missing line number.");
            return;
        }

        int i = 0;
        while(i < argsStr.length()) {
            if(Character.isWhitespace(argsStr.charAt(i))) {
                break;
            }
            ++i;
        }

        if(i == argsStr.length()) {
            ctx.printLine("Missing the new command line after the index.");
            return;
        }

        String intStr = argsStr.substring(0, i);
        int lineNumber;
        try {
            lineNumber = Integer.parseInt(intStr);
        } catch(NumberFormatException e) {
            ctx.printLine("Failed to parse line number '" + intStr + "': " + e.getLocalizedMessage());
            return;
        }

        if(lineNumber < 1 || lineNumber > batchSize) {
            ctx.printLine(lineNumber + " isn't in range [1.." + batchSize + "].");
            return;
        }

        String editedLine = argsStr.substring(i).trim();
        if(editedLine.length() == 0) {
            ctx.printLine("Missing the new command line after the index.");
            return;
        }

        try {
            BatchedCommand newCmd = ctx.toBatchedCommand(editedLine);
            batch.set(lineNumber - 1, newCmd);
        } catch (OperationFormatException e) {
            ctx.printLine("Failed to process command line '" + editedLine + "': " + e.getLocalizedMessage());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/952.java
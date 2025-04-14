error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8541.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8541.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8541.java
text:
```scala
w@@eaverOption.messageHandler.dontIgnore(IMessage.INFO);

/*******************************************************************************
 * Copyright (c) 2005 Contributors.
 * All rights reserved.
 * This program and the accompanying materials are made available
 * under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution and is available at
 * http://eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Alexandre Vasseur         initial implementation
 *******************************************************************************/
package org.aspectj.weaver.loadtime;

import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.IMessageHandler;
import org.aspectj.bridge.Message;
import org.aspectj.util.LangUtil;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * A class that hanldes LTW options.
 * Note: AV - I choosed to not reuse AjCompilerOptions and alike since those implies too many dependancies on
 * jdt and ajdt modules.
 *
 * @author <a href="mailto:alex AT gnilux DOT com">Alexandre Vasseur</a>
 */
public class Options {

    private final static String OPTION_15 = "-1.5";
    private final static String OPTION_lazyTjp = "-XlazyTjp";
    private final static String OPTION_noWarn = "-nowarn";
    private final static String OPTION_noWarnNone = "-warn:none";
    private final static String OPTION_proceedOnError = "-proceedOnError";
    private final static String OPTION_verbose = "-verbose";
    private final static String OPTION_reweavable = "-Xreweavable";//notReweavable is default for LTW
    private final static String OPTION_noinline = "-Xnoinline";
    private final static String OPTION_hasMember = "-XhasMember";
    private final static String OPTION_pinpoint = "-Xdev:pinpoint";
    private final static String OPTION_showWeaveInfo = "-showWeaveInfo";
    private final static String OPTIONVALUED_messageHandler = "-XmessageHandlerClass:";
    private static final String OPTIONVALUED_Xlintfile = "-Xlintfile:";
    private static final String OPTIONVALUED_Xlint = "-Xlint:";


    public static WeaverOption parse(String options, ClassLoader laoder, IMessageHandler imh) {
        WeaverOption weaverOption = new WeaverOption(imh);

        if (LangUtil.isEmpty(options)) {
            return weaverOption;
        }
        // the first option wins
        List flags = LangUtil.anySplit(options, " ");
        Collections.reverse(flags);

        // do a first round on the message handler since it will report the options themselves
        for (Iterator iterator = flags.iterator(); iterator.hasNext();) {
            String arg = (String) iterator.next();
            if (arg.startsWith(OPTIONVALUED_messageHandler)) {
                if (arg.length() > OPTIONVALUED_messageHandler.length()) {
                    String handlerClass = arg.substring(OPTIONVALUED_messageHandler.length()).trim();
                    try {
                        Class handler = Class.forName(handlerClass, false, laoder);
                        weaverOption.messageHandler = ((IMessageHandler) handler.newInstance());
                    } catch (Throwable t) {
                        weaverOption.messageHandler.handleMessage(
                                new Message(
                                        "Cannot instantiate message handler " + handlerClass,
                                        IMessage.ERROR,
                                        t,
                                        null
                                )
                        );
                    }
                }
            }
        }

        // configure the other options
        for (Iterator iterator = flags.iterator(); iterator.hasNext();) {
            String arg = (String) iterator.next();
            if (arg.equals(OPTION_15)) {
                weaverOption.java5 = true;
            } else if (arg.equalsIgnoreCase(OPTION_lazyTjp)) {
                weaverOption.lazyTjp = true;
            } else if (arg.equalsIgnoreCase(OPTION_noinline)) {
                weaverOption.noInline = true;
            } else if (arg.equalsIgnoreCase(OPTION_noWarn) || arg.equalsIgnoreCase(OPTION_noWarnNone)) {
                weaverOption.noWarn = true;
            } else if (arg.equalsIgnoreCase(OPTION_proceedOnError)) {
                weaverOption.proceedOnError = true;
            } else if (arg.equalsIgnoreCase(OPTION_reweavable)) {
                weaverOption.notReWeavable = false;
            } else if (arg.equalsIgnoreCase(OPTION_showWeaveInfo)) {
                weaverOption.showWeaveInfo = true;
            } else if (arg.equalsIgnoreCase(OPTION_hasMember)) {
                weaverOption.hasMember = true;
            }  else if (arg.equalsIgnoreCase(OPTION_verbose)) {
                weaverOption.verbose = true;
            } else if (arg.equalsIgnoreCase(OPTION_pinpoint)) {
                weaverOption.pinpoint = true;
            } else if (arg.startsWith(OPTIONVALUED_messageHandler)) {
                ;// handled in first round
            } else if (arg.startsWith(OPTIONVALUED_Xlintfile)) {
                if (arg.length() > OPTIONVALUED_Xlintfile.length()) {
                    weaverOption.lintFile = arg.substring(OPTIONVALUED_Xlintfile.length()).trim();
                }
            } else if (arg.startsWith(OPTIONVALUED_Xlint)) {
                if (arg.length() > OPTIONVALUED_Xlint.length()) {
                    weaverOption.lint = arg.substring(OPTIONVALUED_Xlint.length()).trim();
                }
            } else {
                weaverOption.messageHandler.handleMessage(
                        new Message(
                                "Cannot configure weaver with option '" + arg + "': unknown option",
                                IMessage.WARNING,
                                null,
                                null
                        )
                );
            }
        }

        // refine message handler configuration
        if (weaverOption.noWarn) {
            weaverOption.messageHandler.dontIgnore(IMessage.WARNING);
        }
        if (weaverOption.verbose) {
            weaverOption.messageHandler.dontIgnore(IMessage.DEBUG);
        }
        if (weaverOption.showWeaveInfo) {
            weaverOption.messageHandler.dontIgnore(IMessage.WEAVEINFO);
        }

        return weaverOption;
    }

    public static class WeaverOption {
        boolean java5;
        boolean lazyTjp;
        boolean hasMember;
        boolean noWarn;
        boolean proceedOnError;
        boolean verbose;
        boolean notReWeavable = true;//default to notReweavable for LTW (faster)
        boolean noInline;
        boolean showWeaveInfo;
        boolean pinpoint;
        IMessageHandler messageHandler;
        String lint;
        String lintFile;

        public WeaverOption(IMessageHandler imh) {
//            messageHandler = new DefaultMessageHandler();//default
            this.messageHandler = imh;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8541.java
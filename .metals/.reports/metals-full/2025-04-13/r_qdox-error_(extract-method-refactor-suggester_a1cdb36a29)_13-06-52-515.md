error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4003.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4003.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4003.java
text:
```scala
a@@ddHandler("STRING_IN_PARENTHESIS", '(', ')');

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
package org.jboss.as.cli.parsing;

import java.util.HashMap;
import java.util.Map;

import org.jboss.as.cli.CommandFormatException;
import org.jboss.as.cli.operation.OperationFormatException;

/**
 *
 * @author Alexey Loubyansky
 */
public class GlobalCharacterHandlers {

    private static final Map<Character, CharacterHandler> handlers = new HashMap<Character, CharacterHandler>();

    static final CharacterHandlerMap GLOBAL_ENTER_STATE_HANDLERS = new CharacterHandlerMap() {
        @Override
        public CharacterHandler getHandler(char ch) {
            return GlobalCharacterHandlers.getHandler(ch, null);
        }

        @Override
        public void putHandler(char ch, CharacterHandler handler) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void removeHandler(char ch) {
            throw new UnsupportedOperationException();
        }};

    public static final CharacterHandler NOOP_CHARACTER_HANDLER = new CharacterHandler(){
        @Override
        public void handle(ParsingContext ctx)
                throws OperationFormatException {
        }
        public String toString() {return "NOOPHANDLER";}
    };

    public static final CharacterHandler CONTENT_CHARACTER_HANDLER = new CharacterHandler() {

        @Override
        public void handle(ParsingContext ctx)
                throws CommandFormatException {
            ctx.getCallbackHandler().character(ctx);
        }
    };

    public static final CharacterHandler LEAVE_STATE_HANDLER = new CharacterHandler() {

        @Override
        public void handle(ParsingContext ctx)
                throws CommandFormatException {
            ctx.leaveState();
        }
    };

    static CharacterHandler getHandler(char ch, CharacterHandler defaultHandler) {
        CharacterHandler handler = handlers.get(ch);
        if(handler == null) {
            return defaultHandler;
        }
        return handler;
    }

    static CharacterHandler getHandler(char ch) {
        return getHandler(ch, CONTENT_CHARACTER_HANDLER);
    }

    private static void addHandler(String id, char start, char end) {
        addHandler(start, new DefaultStateWithEndCharacter(id, end, true, false, GLOBAL_ENTER_STATE_HANDLERS));
    }

    private static void addHandler(char start, ParsingState state) {
        handlers.put(start, new EnterStateCharacterHandler(state));
    }

    //private static void addHandler(char start, )
    static {
        addHandler("STRING_IN_PARANTHESIS", '(', ')');
        addHandler("STRING_IN_BRACKETS", '[', ']');
        addHandler("STRING_IN_BRACES", '{', '}');
        //addHandler("STRING_IN_CHEVRONS", '<', '>', "The closing '>' is missing.");
        addHandler('\\', EscapeCharacterState.INSTANCE);
        addHandler('"', QuotesState.QUOTES_EXCLUDED);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4003.java
error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11741.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11741.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11741.java
text:
```scala
t@@hrow new CommandFormatException("'" + value + "' doesn't follow format {rollout server_group_list [rollback-across-groups]}");

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
package org.jboss.as.cli;


import java.util.List;

import org.jboss.as.cli.operation.ParsedOperationRequestHeader;
import org.jboss.as.cli.operation.impl.DefaultCallbackHandler;
import org.jboss.as.cli.parsing.DefaultParsingState;
import org.jboss.as.cli.parsing.ParserUtil;
import org.jboss.as.cli.parsing.operation.HeaderListState;
import org.jboss.dmr.ModelNode;


/**
 *
 * @author Alexey Loubyansky
 */
public interface ArgumentValueConverter {

    abstract class DMRWithFallbackConverter implements ArgumentValueConverter {
        @Override
        public ModelNode fromString(String value) throws CommandFormatException {
            if(value == null) {
                return new ModelNode();
            }
            try {
                return ModelNode.fromString(value);
            } catch(Exception e) {
                return fromNonDMRString(value);
            }
        }

        protected abstract ModelNode fromNonDMRString(String value) throws CommandFormatException;
    }

    ArgumentValueConverter DEFAULT = new ArgumentValueConverter() {
        @Override
        public ModelNode fromString(String value) throws CommandFormatException {
            if (value == null) {
                return new ModelNode();
            }
            ModelNode toSet = null;
            try {
                toSet = ModelNode.fromString(value);
            } catch (Exception e) {
                // just use the string
                toSet = new ModelNode().set(value);
            }
            return toSet;
        }
    };

    ArgumentValueConverter LIST = new DMRWithFallbackConverter() {
        @Override
        protected ModelNode fromNonDMRString(String value) throws CommandFormatException {
            // strip [] if they are present
            if(value.length() >= 2 && value.charAt(0) == '[' && value.charAt(value.length() - 1) == ']') {
                value = value.substring(1, value.length() - 1);
            }
            final ModelNode list = new ModelNode();
            for (String item : value.split(",")) {
                list.add(new ModelNode().set(item));
            }
            return list;
        }
    };

    ArgumentValueConverter PROPERTIES = new DMRWithFallbackConverter() {
        @Override
        protected ModelNode fromNonDMRString(String value) throws CommandFormatException {
            // strip [] if they are present
            if(value.length() >= 2 && value.charAt(0) == '[' && value.charAt(value.length() - 1) == ']') {
                value = value.substring(1, value.length() - 1);
            }
            final String[] props = value.split(",");
            final ModelNode list = new ModelNode();
            for (String prop : props) {
                int equals = prop.indexOf('=');
                if (equals == -1) {
                    throw new CommandFormatException("Property '" + prop + "' in '" + value + "' is missing the equals sign.");
                }
                String propName = prop.substring(0, equals);
                if (propName.isEmpty()) {
                    throw new CommandFormatException("Property name is missing for '" + prop + "' in '" + value + "'");
                }
                list.add(propName, prop.substring(equals + 1));
            }
            return list;
        }
    };

    ArgumentValueConverter OBJECT = new DMRWithFallbackConverter() {
        @Override
        protected ModelNode fromNonDMRString(String value)
                throws CommandFormatException {
            // strip {} if they are present
            if(value.length() >= 2 && value.charAt(0) == '{' && value.charAt(value.length() - 1) == '}') {
                value = value.substring(1, value.length() - 1);
            }

            final String[] props = value.split(",");
            final ModelNode o = new ModelNode();
            for (String prop : props) {
                int equals = prop.indexOf('=');
                if (equals == -1) {
                    throw new CommandFormatException("Property '" + prop + "' in '" + value + "' is missing the equals sign.");
                }
                String propName = prop.substring(0, equals);
                if (propName.isEmpty()) {
                    throw new CommandFormatException("Property name is missing for '" + prop + "' in '" + value + "'");
                }
                o.get(propName).set(prop.substring(equals + 1));
            }
            return o;
        }
    };

    ArgumentValueConverter ROLLOUT_PLAN = new DMRWithFallbackConverter() {
        private final DefaultCallbackHandler callback = new DefaultCallbackHandler();
        private final DefaultParsingState initialState = new DefaultParsingState("INITIAL_STATE");
        {
            initialState.enterState('{', HeaderListState.INSTANCE);
        }

        @Override
        protected ModelNode fromNonDMRString(String value) throws CommandFormatException {
            callback.reset();
            ParserUtil.parse(value, callback, initialState);
            final List<ParsedOperationRequestHeader> headers = callback.getHeaders();
            if(headers.isEmpty()) {
                return null;
            }
            if(headers.size() > 1) {
                throw new CommandFormatException("Too many headers: " + headers);
            }
            final ParsedOperationRequestHeader header = headers.get(0);
            final ModelNode node = new ModelNode();
            header.addTo(null, node);
            return node;
        }
    };

    ModelNode fromString(String value) throws CommandFormatException;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11741.java
error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4073.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4073.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4073.java
text:
```scala
i@@f(ch == ':' || ch == '/' || ch == '=') {

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
package org.jboss.as.cli.operation;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.jboss.as.cli.CommandContext;
import org.jboss.as.cli.operation.impl.DefaultOperationCallbackHandler;
import org.jboss.as.cli.operation.impl.DefaultOperationRequestAddress;

import jline.Completor;

/**
 *
 * @author Alexey Loubyansky
 */
public class OperationRequestCompleter implements Completor {

    private final CommandContext ctx;

    public OperationRequestCompleter(CommandContext ctx) {
        this.ctx = ctx;
    }

    /* (non-Javadoc)
     * @see jline.Completor#complete(java.lang.String, int, java.util.List)
     */
    @SuppressWarnings("rawtypes" )
    @Override
    public int complete(String buffer, int cursor, List candidates) {

        return doComplete(buffer, candidates, true);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public int doComplete(String buffer, List candidates, boolean requireSlashOrDotSlash) {
        int firstCharIndex = 0;
        while(firstCharIndex < buffer.length()) {
            if(!Character.isWhitespace(buffer.charAt(firstCharIndex))) {
                break;
            }
            ++firstCharIndex;
        }

        if(requireSlashOrDotSlash) {
            if(firstCharIndex == buffer.length()) {
                return -1;
            }

            if (!(buffer.startsWith("./", firstCharIndex)
 buffer.charAt(firstCharIndex) == ':'
 buffer.charAt(firstCharIndex) == '/' || buffer
                    .startsWith("..", firstCharIndex))) {
                return -1;
            }
        }

        // if it ends on .. then the completion shouldn't happen
        // since the node is selected and it doesn't end on a separator
        if(buffer.endsWith("..")) {
            return 0;
        }

        DefaultOperationCallbackHandler handler = new DefaultOperationCallbackHandler(new DefaultOperationRequestAddress(ctx.getPrefix()));

        try {
            ctx.getOperationRequestParser().parse(buffer, handler);
        } catch (OperationFormatException e1) {
            return -1;
        }

        if(handler.isRequestComplete()) {
            return -1;
        }

        if(handler.hasProperties() || handler.endsOnPropertyListStart()) {

            if(handler.endsOnPropertyValueSeparator()) {
                // no value completion
                return -1;
            }

            OperationCandidatesProvider provider = ctx.getOperationCandidatesProvider();

            List<String> propertyNames = provider.getPropertyNames(handler.getOperationName(), handler.getAddress());
            if(propertyNames.isEmpty()) {
                if(handler.endsOnPropertyListStart()) {
                    candidates.add(")");
                    return buffer.length();
                }
                return -1;
            }

            if(handler.endsOnPropertyListStart()) {
                if(propertyNames.size() == 1) {
                    candidates.add(propertyNames.get(0) + '=');
                } else {
                    candidates.addAll(propertyNames);
                    Collections.sort(candidates);
                }
                //return handler.getLastSeparatorIndex() + 1;
                return buffer.length();
            }

            Set<String> specifiedNames = handler.getPropertyNames();

            String chunk = null;
            for(String specifiedName : specifiedNames) {
                String value = handler.getPropertyValue(specifiedName);
                if(value == null) {
                    chunk = specifiedName;
                } else {
                    propertyNames.remove(specifiedName);
                }
            }

            if(chunk == null) {
                if(handler.endsOnPropertySeparator()) {
                    if(propertyNames.size() == 1) {
                        candidates.add(propertyNames.get(0) + '=');
                    } else {
                        candidates.addAll(propertyNames);
                        Collections.sort(candidates);
                    }
                } else if(propertyNames.isEmpty()) {
                    candidates.add(")");
                }
                return buffer.length();
                //return handler.getLastSeparatorIndex() + 1;
            }

            for(String candidate : propertyNames) {
                if(candidate.startsWith(chunk)) {
                    candidates.add(candidate);
                }
            }

            if(candidates.size() == 1) {
                candidates.set(0, (String)candidates.get(0) + '=');
            } else {
                Collections.sort(candidates);
            }
            return handler.getLastSeparatorIndex() + 1;
        }

        if(handler.hasOperationName() || handler.endsOnAddressOperationNameSeparator()) {

            if(handler.getAddress().endsOnType()) {
                return -1;
            }
            OperationCandidatesProvider provider = ctx.getOperationCandidatesProvider();
            final List<String> names = provider.getOperationNames(handler.getAddress());
            if(names.isEmpty()) {
                return -1;
            }

            final String chunk = handler.getOperationName();
            if(chunk == null) {
                candidates.addAll(names);
            } else {
                for (String name : names) {
                    if (chunk == null || name.startsWith(chunk)) {
                        candidates.add(name);
                    }
                }
            }

            Collections.sort(candidates);
            return handler.getLastSeparatorIndex() + 1;
        }

        final OperationRequestAddress address = handler.getAddress();

        final String chunk;
        if (address.isEmpty() || handler.endsOnNodeSeparator()
 handler.endsOnNodeTypeNameSeparator()
 address.equals(ctx.getPrefix())
                // TODO this is not nice
 buffer.endsWith("..")) {
            chunk = null;
        } else if (address.endsOnType()) {
            chunk = address.getNodeType();
            address.toParentNode();
        } else {
            chunk = address.toNodeType();
        }

        OperationCandidatesProvider provider = ctx.getOperationCandidatesProvider();
        final List<String> names;
        if(address.endsOnType()) {
            names = provider.getNodeNames(address);
        } else {
            names = provider.getNodeTypes(address);
        }

        if(names.isEmpty()) {
            return -1;
        }

        if(chunk == null) {
            candidates.addAll(names);
        } else {
            for (String name : names) {
                if (chunk == null || name.startsWith(chunk)) {
                    candidates.add(name);
                }
            }
        }

        if(candidates.size() == 1) {
            if(address.endsOnType()) {
                candidates.set(0, formatName((String) candidates.get(0)));
            } else {
                String onlyType = (String) candidates.get(0);
                address.toNodeType(onlyType);
                List<String> childNames = provider.getNodeNames(address);
                if (!childNames.isEmpty()) {
                    candidates.clear();
                    if(childNames.size() == 1) {
                        candidates.add(onlyType  + '=' + formatName(childNames.get(0)));
                    } else {
                        for (String name : childNames) {
                            candidates.add(onlyType + '=' + name);
                        }
                    }
                }
            }
        }

        Collections.sort(candidates);
        return handler.getLastSeparatorIndex() + 1;
    }

    private static String formatName(String name) {
        for(int i = 0; i < name.length(); ++i) {
            char ch = name.charAt(i);
            if(ch == ':' || ch == '/') {
                // now escape the quotes
                StringBuilder builder = new StringBuilder();
                builder.append('"');
                for(int j = 0; j < name.length(); ++j) {
                    ch = name.charAt(j);
                    if(ch == '"') {
                        builder.append('\\');
                    }
                    builder.append(ch);
                }
                builder.append('"');
                return builder.toString();
            }
        }
        return name;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4073.java
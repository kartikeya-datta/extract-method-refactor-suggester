error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3376.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3376.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[24,1]

error in qdox parser
file content:
```java
offset: 1100
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3376.java
text:
```scala
public class OperationParamListStateTestCase extends BaseStateParserTest {

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
p@@ackage org.jboss.as.cli.parsing.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.jboss.as.cli.operation.OperationFormatException;
import org.jboss.as.cli.operation.parsing.PropertyListState;
import org.jboss.as.cli.operation.parsing.StateParser;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Alexey Loubyansky
 */
public class ParamListStateTestCase extends BaseStateParserTest {

    @Test
    public void testParamSimple() throws Exception {
        parseAsParamList("a", "b");
    }

    @Test
    public void testParamSimpleQuotes() throws Exception {
        parseAsParamList("simple-quotes", "\"simple quotes\"");
    }

    @Test
    public void testParamSimpleBrackets() {
        parseAsParamList("simple-brackets", "[simple brackets]");
    }

    @Test
    public void testParamSimpleParenthesis() {
        parseAsParamList("simple-parenthesis", "(simple parenthesis)");
    }

    @Test
    public void testParamSimpleBraces() {
        parseAsParamList("simple-braces", "{simple braces}");
    }

    @Test
    public void testParamSteps() {
        parseAsParamList("steps", "[{\"operation\"=>\"add-system-property\",\"name\"=>\"test\",\"value\"=\"newValue\"},{\"operation\"=>\"add-system-property\",\"name\"=>\"test2\",\"value\"=>\"test2\"}]");
    }

    @Test
    public void testAllParams() {
        parseAsParamList(Param.allParams());
        assertNotNull(result);
        assertNull(result.buffer);
        assertEquals(1, result.children.size());

        ParsedTerm params = result.children.get(0);
        assertNotNull(params);
        assertNull(params.buffer);
        assertEquals(Param.all.size(), params.children.size());

        for(int i = 0; i < Param.all.size(); ++i) {
            Param param = Param.all.get(i);
            assertParam(param.name, param.value, params.children.get(i));
        }
    }

    protected void parseAsParamList(String name, String value) {

        Param param = new Param(name, value);

        parseAsParamList('(' + param.name + '=' + param.value + ')');

        assertNotNull(result);
        assertNull(result.buffer);
        assertEquals(1, result.children.size());

        ParsedTerm params = result.children.get(0);
        assertNotNull(params);
        assertNull(params.buffer);
        assertEquals(1, params.children.size());

        assertParam(param.name, param.value, params.children.get(0));
    }

    protected void parseAsParamList(String str) {

        StateParser parser = new StateParser();
        parser.addState('(', PropertyListState.INSTANCE);
        try {
            parser.parse(str, callbackHandler);
        } catch (OperationFormatException e) {
            Assert.fail(e.getLocalizedMessage());
        }
    }

    protected void assertParam(String name, String value, ParsedTerm param) {
        assertNotNull(param);
        assertNotNull(param.buffer);
        assertEquals(name, param.buffer.toString().trim());
        assertEquals(1, param.children.size());
        ParsedTerm paramValue = param.children.get(0);
        assertNotNull(paramValue);
        assertEquals(value, paramValue.valueAsString());
        //assertEquals(0, paramValue.children.size());
    }

    static class Param {
        static final List<Param> all = new ArrayList<Param>();

        static String allParams() {
            StringBuilder builder = new StringBuilder();
            builder.append('(');
            for(int i = 0; i < all.size(); ++i) {
                Param p = all.get(i);
                if(i > 0) {
                    builder.append(", ");
                }
                builder.append(p.name).append('=').append(p.value);
            }
            builder.append(')');
            return builder.toString();
        }

        final String name;
        final String value;

        Param(String name, String value) {
            this.name = name;
            this.value = value;
            all.add(this);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3376.java
error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7466.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7466.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7466.java
text:
```scala
private L@@ist<OrderPath.Token> tokens = new ArrayList<>();

/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.search.aggregations.support;

import org.elasticsearch.search.aggregations.AggregationExecutionException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 *
 */
public class PathTests {

    @Test
    public void testInvalidPaths() throws Exception {
        assertInvalidPath("[foo]", "brackets at the beginning of the token expression");
        assertInvalidPath("foo[bar", "open brackets without closing at the token expression");
        assertInvalidPath("foo[", "open bracket at the end of the token expression");
        assertInvalidPath("foo[]", "empty brackets in the token expression");
        assertInvalidPath("foo[bar]baz", "brackets not enclosing at the end of the token expression");
        assertInvalidPath(".foo", "dot separator at the beginning of the token expression");
        assertInvalidPath("foo.", "dot separator at the end of the token expression");
    }

    @Test
    public void testValidPaths() throws Exception {
        assertValidPath("foo>bar", tokens().add("foo").add("bar"));
        assertValidPath("foo.bar", tokens().add("foo", "bar"));
        assertValidPath("foo[bar]", tokens().add("foo", "bar"));
        assertValidPath("foo[bar]>baz", tokens().add("foo", "bar").add("baz"));
        assertValidPath("foo[bar]>baz[qux]", tokens().add("foo", "bar").add("baz", "qux"));
        assertValidPath("foo[bar]>baz.qux", tokens().add("foo", "bar").add("baz", "qux"));
        assertValidPath("foo.bar>baz.qux", tokens().add("foo", "bar").add("baz", "qux"));
        assertValidPath("foo.bar>baz[qux]", tokens().add("foo", "bar").add("baz", "qux"));
    }

    private void assertInvalidPath(String path, String reason) {
        try {
            OrderPath.parse(path);
            fail("Expected parsing path [" + path + "] to fail - " + reason);
        } catch (AggregationExecutionException aee) {
            // expected
        }
    }

    private void assertValidPath(String path, Tokens tokenz) {
        OrderPath.Token[] tokens = tokenz.toArray();
        OrderPath p = OrderPath.parse(path);
        assertThat(p.tokens.length, equalTo(tokens.length));
        for (int i = 0; i < p.tokens.length; i++) {
            OrderPath.Token t1 = p.tokens[i];
            OrderPath.Token t2 = tokens[i];
            assertThat(t1, equalTo(t2));
        }
    }

    private static Tokens tokens() {
        return new Tokens();
    }

    private static class Tokens {

        private List<OrderPath.Token> tokens = new ArrayList<OrderPath.Token>();

        Tokens add(String name) {
            tokens.add(new OrderPath.Token(name, name, null));
            return this;
        }

        Tokens add(String name, String key) {
            if (Math.random() > 0.5) {
                tokens.add(new OrderPath.Token(name + "." + key, name, key));
            } else {
                tokens.add(new OrderPath.Token(name + "[" + key + "]", name, key));
            }
            return this;
        }

        OrderPath.Token[] toArray() {
            return tokens.toArray(new OrderPath.Token[tokens.size()]);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7466.java
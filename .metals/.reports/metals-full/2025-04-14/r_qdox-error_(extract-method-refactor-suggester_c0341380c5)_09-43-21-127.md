error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9644.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9644.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9644.java
text:
```scala
s@@uper();

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
package org.jboss.as.cli.completion.address.test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import junit.framework.Assert;

import org.jboss.as.cli.completion.mock.MockNode;
import org.jboss.as.cli.operation.OperationRequestAddress;
import org.junit.Test;

/**
 *
 * @author Alexey Loubyansky
 */
public abstract class AbstractGeneratedAddressCompleterTest extends AbstractAddressCompleterTest {

    protected AbstractGeneratedAddressCompleterTest() {
        init();
    }

    protected void init() {

        super.init();
        initModel();

        int prefixLevel = getPrefixLevel();
        if(prefixLevel > 0) {
            OperationRequestAddress prefix = ctx.getPrefix();
            for(int i = 1; i <= prefixLevel; ++i) {
                if(i % 2 == 0) {
                    prefix.toNode("link" + i);
                } else {
                    prefix.toNodeType("link" + i);
                }
            }
        }
    }

    protected void initModel() {

        MockNode parent = this.root;
        for (int i = 1; i <= getModelDepth(); ++i) {
            parent.addChild("last" + i);
            parent.addChild("other" + i);
            parent = parent.addChild("link" + i);
        }
    }

    protected int getModelDepth() {
        return getBufferLevel() + getPrefixLevel();
    }

    @Test
    public void testAllCandidates() {
        List<String> actual = fetchCandidates(getBufferPrefix());
        List<String> expected = getAllCandidates();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSelectedCandidates() {
        List<String> actual = fetchCandidates(getBufferPrefix() + getSelectCandidates());
        List<String> expected = getSelectedCandidates();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testNoMatch() {
        Assert.assertEquals(Collections.emptyList(), fetchCandidates(getNoMatch()));
    }

    protected List<String> getAllCandidates() {
        int level = getPrefixLevel() + getBufferLevel();
        return Arrays.asList("last" + level, "link" + level, "other" + level);
    }

    protected List<String> getSelectedCandidates() {
        int level = getPrefixLevel() + getBufferLevel();
        return Arrays.asList("last" + level, "link" + level);
    }

    protected String getSelectCandidates() {
        return "l";
    }

    protected int getBufferLevel() {
        return 1;
    }

    private String getBufferPrefix() {
        int bufferLevel = getBufferLevel();
        if(bufferLevel < 2) {
            return "./";
        }

        StringBuilder sb = new StringBuilder("./");
        for(int i = getPrefixLevel() + 1; i < getPrefixLevel() + bufferLevel; ++i) {
            sb.append("link").append(i);
            if(i % 2 == 0) {
                sb.append('/');
            } else {
                sb.append('=');
            }
        }
        return sb.toString();
    }

    protected int getPrefixLevel() {
        return 0;
    }

    protected String getNoMatch() {
        return "nomatch";
    }

/*    private List<String> applyLevel(List<String> candidates) {
        List<String> expected = new ArrayList<String>(candidates.size());
        String levelPrefix = getBufferPrefix();
        for(String local : candidates) {
            expected.add(levelPrefix + local);
        }
        return expected;
    }
*/
    protected void assertAllCandidates(List<String> expected) {
        Assert.assertEquals(expected, getAllCandidates());
    }

    protected void assertSelectedCandidates(List<String> expected) {
        Assert.assertEquals(expected, getSelectedCandidates());
    }

    protected void assertBufferPrefix(String expected) {
        Assert.assertEquals(expected, getBufferPrefix());
    }

    protected void assertContextPrefix(String expected) {
        Assert.assertEquals(expected, ctx.getPrefixFormatter().format(ctx.getPrefix()));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9644.java
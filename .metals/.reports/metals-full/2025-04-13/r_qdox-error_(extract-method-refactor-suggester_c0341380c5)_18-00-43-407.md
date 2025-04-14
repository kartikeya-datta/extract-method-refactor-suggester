error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8370.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8370.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8370.java
text:
```scala
L@@ist<DeploymentAspect> das = WSDeploymentAspectParser.parse(is, this.getClass().getClassLoader());

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.as.webservices.parser;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import junit.framework.TestCase;

import org.jboss.wsf.spi.deployment.DeploymentAspect;
import org.junit.Test;

/**
 * @author alessio.soldano@jboss.com
 * @since 18-Jan-2011
 */
public class WSDeploymentAspectParserTestCase extends TestCase {

    @Test
    public void test() throws Exception {
        InputStream is = getXmlUrl("jbossws-deployment-aspects-example.xml").openStream();
        try {
            List<DeploymentAspect> das = WSDeploymentAspectParser.parse(is);
            assertEquals(4, das.size());
            boolean da1Found = false;
            boolean da2Found = false;
            boolean da3Found = false;
            boolean da4Found = false;
            for (DeploymentAspect da : das) {
                if (da instanceof TestDA2) {
                    da2Found = true;
                    TestDA2 da2 = (TestDA2) da;
                    assertEquals("myString", da2.getTwo());
                } else if (da instanceof TestDA3) {
                    da3Found = true;
                    TestDA3 da3 = (TestDA3) da;
                    assertNotNull(da3.getList());
                    assertTrue(da3.getList().contains("One"));
                    assertTrue(da3.getList().contains("Two"));
                } else if (da instanceof TestDA4) {
                    da4Found = true;
                    TestDA4 da4 = (TestDA4) da;
                    assertEquals(true, da4.isBool());
                    assertNotNull(da4.getMap());
                    assertEquals(1, (int) da4.getMap().get("One"));
                    assertEquals(3, (int) da4.getMap().get("Three"));
                } else if (da instanceof TestDA1) {
                    da1Found = true;
                }
            }
            assertTrue(da1Found);
            assertTrue(da2Found);
            assertTrue(da3Found);
            assertTrue(da4Found);
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    URL getXmlUrl(String xmlName) {
        return getResourceUrl("parser/" + xmlName);
    }

    URL getResourceUrl(String resourceName) {
        URL url = Thread.currentThread().getContextClassLoader().getResource(resourceName);
        assertNotNull("URL not null for: " + resourceName, url);
        return url;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8370.java
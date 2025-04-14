error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14386.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14386.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14386.java
text:
```scala
v@@alidateAppliesTo(patchConfig, "1.2.3");

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

package org.jboss.as.patching.generator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import org.jboss.as.patching.metadata.ModificationType;
import org.jboss.as.patching.metadata.Patch;
import org.junit.Test;

/**
 * @author Brian Stansberry (c) 2012 Red Hat Inc.
 */
public class PatchConfigXmlUnitTestCase {

    @Test
    public void testCumulativeGenerated() throws Exception {

        final InputStream is = getResource("test-config01.xml");
        final PatchConfig patchConfig = PatchConfigXml.parse(is);
        // Patch
        assertNotNull(patchConfig);
        assertEquals("patch-12345", patchConfig.getPatchId());
        assertEquals("patch description", patchConfig.getDescription());
        assertNotNull(patchConfig.getPatchType());
        assertEquals(Patch.PatchType.CUMULATIVE, patchConfig.getPatchType());
        assertTrue(patchConfig.isGenerateByDiff());
        assertEquals("2.3.4", patchConfig.getResultingVersion());

        validateAppliesTo(patchConfig, "1.2.3", "1.2.4");

        validateInRuntimeUse(patchConfig);
    }

    @Test
    public void testOneOffGenerated() throws Exception {

        final InputStream is = getResource("test-config02.xml");
        final PatchConfig patchConfig = PatchConfigXml.parse(is);
        // Patch
        assertNotNull(patchConfig);
        assertEquals("patch-12345", patchConfig.getPatchId());
        assertEquals("patch description", patchConfig.getDescription());
        assertNotNull(patchConfig.getPatchType());
        assertEquals(Patch.PatchType.ONE_OFF, patchConfig.getPatchType());
        assertTrue(patchConfig.isGenerateByDiff());
        assertNull(patchConfig.getResultingVersion());

        validateAppliesTo(patchConfig, "1.2.3", "1.2.4");

        validateInRuntimeUse(patchConfig);
    }

    @Test
    public void testCumulativeSpecified() throws Exception {

        specifiedContentTest("test-config03.xml");
    }

    @Test
    public void testOneOffSpecified() throws Exception {

        specifiedContentTest("test-config04.xml");
    }

    private void specifiedContentTest(String configFile) throws Exception {

        final InputStream is = getResource(configFile);
        final PatchConfig patchConfig = PatchConfigXml.parse(is);

        Map<ModificationType, Set<String>> content = new HashMap<ModificationType, Set<String>>();
        Set<String> adds = new HashSet<String>(Arrays.asList("modules/org/jboss/as/test/main", "modules/org/jboss/as/test/prod",
                "bundles/org/jboss/as/test/main", "bundles/org/jboss/as/test/prod", "test/file", "test/file2"));
        content.put(ModificationType.ADD, adds);
        Set<String> mods = new HashSet<String>(Arrays.asList("modules/org/jboss/as/test2/main", "modules/org/jboss/as/test2/prod",
                "bundles/org/jboss/as/test2/main", "bundles/org/jboss/as/test2/prod", "test/file3", "test/file4"));
        content.put(ModificationType.MODIFY, mods);
        Set<String> rems = new HashSet<String>(Arrays.asList("modules/org/jboss/as/test3/main", "modules/org/jboss/as/test3/prod",
                "bundles/org/jboss/as/test3/main", "bundles/org/jboss/as/test3/prod", "test/file5", "test/file6"));
        content.put(ModificationType.REMOVE, rems);

        for (Map<ModificationType, SortedSet<DistributionContentItem>> map : patchConfig.getSpecifiedContent().values()) {
            for (Map.Entry<ModificationType, SortedSet<DistributionContentItem>> entry : map.entrySet()) {
                for (DistributionContentItem item : entry.getValue()) {
                    String path = item.getPath();
                    assertTrue(path, content.get(entry.getKey()).remove(path));
                }
            }
        }

        for (Set<String> set : content.values()) {
            assertTrue(set.toString(), set.isEmpty());
        }

        Set<String> validInUse = new HashSet<String>(Arrays.asList("test/file3", "test/file5"));
        validateInRuntimeUse(patchConfig, validInUse);
    }

    private static InputStream getResource(String name) throws IOException {
        final URL resource = PatchConfigXmlUnitTestCase.class.getClassLoader().getResource(name);
        assertNotNull(name, resource);
        return resource.openStream();
    }

    private static void validateAppliesTo(final PatchConfig patchConfig, String... expected) {
        Set<String> expectedAppliesTo = new HashSet<String>(Arrays.asList(expected));
        Set<String> actualAppliesTo = new HashSet<String>(patchConfig.getAppliesTo());

        assertEquals(expectedAppliesTo, actualAppliesTo);
    }

    private static void validateInRuntimeUse(final PatchConfig patchConfig) {
        Set<String> validInUse = new HashSet<String>(Arrays.asList("test", "test/file", "test/file/file1"));
        validateInRuntimeUse(patchConfig, validInUse);
    }

    private static void validateInRuntimeUse(final PatchConfig patchConfig, Set<String> validInUse) {
        for (DistributionContentItem item : patchConfig.getInRuntimeUseItems()) {
            String path = item.getPath();
            assertTrue(path + " is valid", validInUse.remove(path));
        }
        assertEquals("Expected in-runtime-use item not found", Collections.emptySet(), validInUse);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14386.java
error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1930.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1930.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1930.java
text:
```scala
S@@ystem.out.println("testExit successful.");

/*
 * Copyright  2003-2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package org.apache.tools.ant.types;

import junit.framework.TestCase;

import org.apache.tools.ant.ExitException;

/**
 * JUnit 3 testcases for org.apache.tools.ant.types.Permissions.
 *
 */
public class PermissionsTest extends TestCase {

    Permissions perms;

    public PermissionsTest(String name) {
        super(name);
    }

    public void setUp() {
        perms = new Permissions();
        Permissions.Permission perm = new Permissions.Permission();
        // Grant extra permissions to read and write the user.* properties and read to the
        // java.home property
        perm.setActions("read, write");
        perm.setName("user.*");
        perm.setClass("java.util.PropertyPermission");
        perms.addConfiguredGrant(perm);

        perm = new Permissions.Permission();
        perm.setActions("read");
        perm.setName("java.home");
        perm.setClass("java.util.PropertyPermission");
        perms.addConfiguredGrant(perm);

        perm = new Permissions.Permission();
        perm.setActions("read");
        perm.setName("file.encoding");
        perm.setClass("java.util.PropertyPermission");
        perms.addConfiguredGrant(perm);

        // Revoke permission to write user.home (granted above via user.*), still able to read though.
        // and the default granted permission to read os.name.
        perm = new Permissions.Permission();
        perm.setActions("write");
        perm.setName("user.home");
        perm.setClass("java.util.PropertyPermission");
        perms.addConfiguredRevoke(perm);

        perm = new Permissions.Permission();
        perm.setActions("read");
        perm.setName("os.*");
        perm.setClass("java.util.PropertyPermission");
        perms.addConfiguredRevoke(perm);
    }

    /** Tests a permission that is granted per default. */
    public void testDefaultGranted() {
        perms.setSecurityManager();
        try {
            String s = System.getProperty("line.separator");
        } finally {
            perms.restoreSecurityManager();
        }
    }

    /** Tests a permission that has been granted later via wildcard. */
    public void testGranted() {
        perms.setSecurityManager();
        try {
            String s = System.getProperty("user.name");
            System.setProperty("user.name", s);
        } finally {
            perms.restoreSecurityManager();
        }
    }

    /** Tests a permission that has been granted and revoked later. */
    public void testGrantedAndRevoked() {
        perms.setSecurityManager();
        try {
            String s = System.getProperty("user.home");
            System.setProperty("user.home", s);
            fail("Could perform an action that should have been forbidden.");
        } catch (SecurityException e){
            // Was expected, test passes
        } finally {
            perms.restoreSecurityManager();
        }
    }

    /** Tests a permission that is granted as per default but revoked later via wildcard. */
    public void testDefaultRevoked() {
        perms.setSecurityManager();
        try {
            System.getProperty("os.name");
            fail("Could perform an action that should have been forbidden.");
        } catch (SecurityException e){
            // Was expected, test passes
        } finally {
            perms.restoreSecurityManager();
        }
    }
    /** Tests a permission that has not been granted or revoked. */
    public void testOther() {
        String ls = System.getProperty("line.separator");
        perms.setSecurityManager();
        try {
            String s = System.setProperty("line.separator",ls);
            fail("Could perform an action that should have been forbidden.");
        } catch (SecurityException e){
            // Was expected, test passes
        } finally {
            perms.restoreSecurityManager();
        }
    }

    /** Tests an exit condition. */
    public void testExit() {
        perms.setSecurityManager();
        try {
            System.out.println("If this is the last line on standard out the testExit f.a.i.l.e.d");
            System.exit(3);
            fail("Totaly impossible that this fail is ever executed. Please let me know if it is!");
        } catch (ExitException e) {
            if (e.getStatus() != 3) {
                fail("Received wrong exit status in Exit Exception.");
            }
            System.out.println("testExit successfull.");
        } finally {
            perms.restoreSecurityManager();
        }
    }


    public void tearDown() {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1930.java
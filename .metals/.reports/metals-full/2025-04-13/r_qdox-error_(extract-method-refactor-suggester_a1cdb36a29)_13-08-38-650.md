error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6513.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6513.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6513.java
text:
```scala
a@@ssertTrue("Expected to find the "+USER_NAME+" in the list of known enabled users",values.getEnabledKnownUsers().contains(USER_NAME));

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

package org.jboss.as.domain.management.security.adduser;

import org.jboss.as.domain.management.security.adduser.AddUser;
import org.jboss.as.domain.management.security.adduser.AddUser.FileMode;
import org.jboss.msc.service.StartException;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Properties;

import static java.lang.System.getProperty;
import static org.junit.Assert.assertTrue;

/**
 * Test the property file finder.
 *
 * @author <a href="mailto:flemming.harms@gmail.com">Flemming Harms</a>
 */
public class PropertyFileFinderTestCase extends PropertyTestHelper {


    @Before
    public void setup() throws IOException {
        values.setFileMode(FileMode.MANAGEMENT);
        values.getOptions().setJBossHome(getProperty("java.io.tmpdir"));
    }

    private File createPropertyFile(String filename, String mode) throws IOException {

        File domainDir = new File(getProperty("java.io.tmpdir")+File.separator+mode);
        domainDir.mkdir();
        domainDir.deleteOnExit();
        File propertyUserFile = new File(domainDir, filename);
        propertyUserFile.createNewFile();
        propertyUserFile.deleteOnExit();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(propertyUserFile),"UTF8"));
        try {
          Properties domainPropeties = new Properties();
          domainPropeties.setProperty(USER_NAME,"mypassword");
          domainPropeties.store(bw,"");
        } finally {
           bw.close();
        }
        return propertyUserFile;
    }

    @Test
    public void overridePropertyfileLocationRead() throws IOException {
        File domainMgmtUserFile = createPropertyFile("mgmt-users.properties", "domain");
        File standaloneMgmtUserFile = createPropertyFile("mgmt-users.properties", "standalone");

        System.setProperty("jboss.server.config.user.dir", standaloneMgmtUserFile.getParent());
        System.setProperty("jboss.domain.config.user.dir", domainMgmtUserFile.getParent());
        State propertyFileFinder = new PropertyFileFinder(consoleMock, values);
        State nextState = propertyFileFinder.execute();
        assertTrue(nextState instanceof PromptRealmState);
        assertTrue("Expected to find the "+USER_NAME+" in the list of known users",values.getKnownUsers().contains(USER_NAME));
        assertTrue("Expected the values.getPropertiesFiles() contained the "+standaloneMgmtUserFile,values.getUserFiles().contains(standaloneMgmtUserFile));
        assertTrue("Expected the values.getPropertiesFiles() contained the "+domainMgmtUserFile,values.getUserFiles().contains(domainMgmtUserFile));
    }

    @Test
    public void overridePropertyfileLocationWrite() throws IOException, StartException {
        File domainUserFile = createPropertyFile("application-users.properties", "domain");
        File standaloneUserFile = createPropertyFile("application-users.properties", "standalone");
        File domainRolesFile = createPropertyFile("application-roles.properties", "domain");
        File standaloneRolesFile = createPropertyFile("application-roles.properties", "standalone");

        String newUserName = "Hugh.Jackman";
        values.setGroups(null);
        values.setUserName(newUserName);
        values.setFileMode(FileMode.APPLICATION);
        System.setProperty("jboss.server.config.user.dir", domainUserFile.getParent());
        System.setProperty("jboss.domain.config.user.dir", standaloneUserFile.getParent());
        State propertyFileFinder = new PropertyFileFinder(consoleMock, values);
        State nextState = propertyFileFinder.execute();
        assertTrue(nextState instanceof PromptRealmState);

        File locatedDomainPropertyFile = values.getUserFiles().get(values.getUserFiles().indexOf(domainUserFile));
        File locatedStandalonePropertyFile = values.getUserFiles().get(values.getUserFiles().indexOf(standaloneUserFile));
        UpdateUser updateUserState = new UpdateUser(consoleMock, values);

        AssertConsoleBuilder consoleBuilder = new AssertConsoleBuilder().
                expectedDisplayText(updateUserState.consoleUserMessage(locatedDomainPropertyFile.getCanonicalPath())).
                expectedDisplayText(AddUser.NEW_LINE).
                expectedDisplayText(updateUserState.consoleUserMessage(locatedStandalonePropertyFile.getCanonicalPath())).
                expectedDisplayText(AddUser.NEW_LINE);
        consoleMock.setResponses(consoleBuilder);
        updateUserState.update(values);

        assertUserPropertyFile(newUserName);
        consoleBuilder.validate();
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6513.java
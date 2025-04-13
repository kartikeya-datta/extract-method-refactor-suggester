error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7480.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7480.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[24,9]

error in qdox parser
file content:
```java
offset: 1116
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7480.java
text:
```scala
@PersistenceContext(type = PersistenceContextType.EXTENDED, unitName = "H2DS",

/*
 * JBoss, Home of Professional Open Source.
 * Copyright (c) 2011, Red Hat, Inc., and individual contributors
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
package o@@rg.jboss.as.demos.jpa.archive;

import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.PersistenceProperty;
import javax.persistence.TransactionRequiredException;

/**
 * @author <a href="mailto:cdewolf@redhat.com">Carlo de Wolf</a>
 * @author Scott Marlow
 */
@Stateful
public class SimpleStatefulSessionBean implements SimpleStatefulSessionLocal {
    private String state;

    @PersistenceContext(type = PersistenceContextType.TRANSACTION, unitName = "H2DS",
        properties = @PersistenceProperty(name = "hibernate.hbm2ddl.auto", value = "create-drop"))
    private EntityManager entityManager;

    public String echo(String msg) {
        System.out.println("Called echo on " + this);
        System.out.println("call the entity manager");

        SimpleEntity entity = new SimpleEntity();
        entity.setId(1);
        entity.setName("Douglas Adams");
        entityManager.persist(entity);
        System.out.println("saved new Entity for " + entity.getName());
        entity = entityManager.find(SimpleEntity.class, new Integer(1));
        System.out.println("read back Entity for " + entity.getName());

        return "Echo " + msg + ":" + state + " entitymanager find should return 'Douglas Adams', it returned = " + entity;
    }

    @TransactionAttribute(TransactionAttributeType.NEVER)
    public String echoNoTx(String msg) {
        System.out.println("Called echo on " + this);
        System.out.println("call the entity manager");

        SimpleEntity entity = new SimpleEntity();
        entity.setId(2);
        entity.setName("Douglas Adams");
        Throwable throwable = null;
        try {
            entityManager.persist(entity);      // should throw TransactionRequiredException
        } catch (Exception expected) {
            throwable = expected;
        }

        while (throwable != null && !(throwable instanceof TransactionRequiredException))
            throwable = throwable.getCause();
        if (throwable != null)
            return "echoNoTx succeeded, got expected TransactionRequiredException exception while trying to persist" +
                " without a transaction active";
        else
            return "echoNoTx failed, attempting to persist an entity should of thrown a TransactionRequiredException but didn't";
    }


    public void setState(String s) {
        System.out.println("Called setState on " + this);
        this.state = s;
    }

    @Override
    public String getState() {
        return this.state;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7480.java
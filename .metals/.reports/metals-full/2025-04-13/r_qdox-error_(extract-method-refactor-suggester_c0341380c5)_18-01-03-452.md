error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1391.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1391.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1391.java
text:
```scala
public v@@oid ejbHomeResetId() {

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
package org.jboss.as.test.integration.ejb.entity.cmp.commerce;

import java.util.Collection;
import java.util.Set;
import javax.ejb.CreateException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.FinderException;

public abstract class OrderBean implements EntityBean {
    transient private EntityContext ctx;

    private static long id = 0;

    public Long ejbCreate() throws CreateException {
        setOrdernumber(id++);
        return null;
    }

    public void ejbPostCreate() {
    }

    public Long ejbCreate(Long id) throws CreateException {
        setOrdernumber(id);
        return null;
    }

    public void ejbPostCreate(Long id) {
    }

    public abstract Long getOrdernumber();

    public abstract void setOrdernumber(Long ordernumber);

    public abstract Card getCreditCard();

    public abstract void setCreditCard(Card card);

    public abstract String getOrderStatus();

    public abstract void setOrderStatus(String orderStatus);

    public abstract Customer getCustomer();

    public abstract void setCustomer(Customer c);

    public abstract Collection getLineItems();

    public abstract void setLineItems(Collection lineItems);

    public abstract Address getShippingAddress();

    public abstract void setShippingAddress(Address shippingAddress);

    public abstract Address getBillingAddress();

    public abstract void setBillingAddress(Address billingAddress);

    public abstract Set ejbSelectOrdersShippedToCA() throws FinderException;

    public abstract Set ejbSelectOrdersShippedToCA2() throws FinderException;

    public abstract Collection ejbSelectOrderShipToStates()
            throws FinderException;

    public abstract Collection ejbSelectOrderShipToStates2()
            throws FinderException;

    public abstract Set ejbSelectAddressesInCA() throws FinderException;

    public abstract Set ejbSelectAddressesInCA2() throws FinderException;

    public Set getOrdersShippedToCA() throws FinderException {
        return ejbSelectOrdersShippedToCA();
    }

    public Set getOrdersShippedToCA2() throws FinderException {
        return ejbSelectOrdersShippedToCA2();
    }

    public Collection getStatesShipedTo() throws FinderException {
        return ejbSelectOrderShipToStates();
    }

    public Collection getStatesShipedTo2() throws FinderException {
        return ejbSelectOrderShipToStates2();
    }

    public Set getAddressesInCA() throws FinderException {
        return ejbSelectAddressesInCA();
    }

    public Set getAddressesInCA2() throws FinderException {
        return ejbSelectAddressesInCA2();
    }

    public Set ejbHomeGetStuff(String jbossQl, Object[] arguments)
            throws FinderException {
        return ejbSelectGeneric(jbossQl, arguments);
    }

    public Set ejbHomeSelectLazy(String jbossQl, Object[] arguments)
            throws FinderException {
        return ejbSelectLazy(jbossQl, arguments);
    }

    public abstract Set ejbSelectGeneric(String jbossQl, Object[] arguments)
            throws FinderException;

    public abstract Set ejbSelectLazy(String jbossQl, Object[] arguments)
            throws FinderException;

    public void setEntityContext(EntityContext ctx) {
        this.ctx = ctx;
    }

    public void unsetEntityContext() {
        this.ctx = null;
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public void ejbLoad() {
    }

    public void ejbStore() {
    }

    public void ejbRemove() {
    }

    void ejbHomeResetId() {
        id = 0;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1391.java
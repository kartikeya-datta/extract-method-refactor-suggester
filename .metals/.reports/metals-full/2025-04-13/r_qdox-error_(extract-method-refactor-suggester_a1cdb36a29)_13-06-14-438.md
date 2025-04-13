error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1565.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1565.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1565.java
text:
```scala
.@@addHandlesReferralsFor("ldap://" + HOST_NAME + ":" + SLAVE_LDAP_PORT)

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2014, Red Hat, Inc., and individual contributors
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

package org.jboss.as.domain.management.security.realms;

import static org.jboss.as.domain.management.security.realms.LdapTestSuite.HOST_NAME;
import static org.jboss.as.domain.management.security.realms.LdapTestSuite.MASTER_LDAP_PORT;
import static org.jboss.as.domain.management.security.realms.LdapTestSuite.SLAVE_LDAP_PORT;

import java.util.List;

import org.jboss.as.domain.management.connections.ldap.LdapConnectionResourceDefinition.ReferralHandling;
import org.jboss.as.domain.management.security.operations.OutboundConnectionAddBuilder;
import org.jboss.dmr.ModelNode;


/**
 * An extension of {@link BaseLdapSuiteTest} that instead adds two LDAP connections so that referrals can be tested.
 *
 * @author <a href="mailto:darran.lofthouse@jboss.com">Darran Lofthouse</a>
 */
public abstract class BaseLdapSuiteReferralsTest extends BaseLdapSuiteTest {

    public static final String SLAVE_CONNECTION_NAME = "SlaveConnection";
    public static final String DEFAULT_SEARCH_DN = "uid=wildfly,dc=simple,dc=wildfly,dc=org";
    public static final String DEFAULT_SEARCH_CREDENTIAL = "wildfly_password";

    @Override
    protected void addAddOutboundConnectionOperations(List<ModelNode> bootOperations) throws Exception {
        bootOperations.add(OutboundConnectionAddBuilder.builder(MASTER_CONNECTION_NAME)
                .setUrl("ldap://" + HOST_NAME + ":" + MASTER_LDAP_PORT)
                .setSearchDn(getSearchDn())
                .setSearchCredential(getSearchCredential())
                .setReferrals(getReferrals())
                .build());

        bootOperations.add(OutboundConnectionAddBuilder.builder(SLAVE_CONNECTION_NAME)
                .setUrl("ldap://" + HOST_NAME + ":" + SLAVE_LDAP_PORT)
                .setSearchDn(DEFAULT_SEARCH_DN)
                .setSearchCredential(DEFAULT_SEARCH_CREDENTIAL)
                .addHandlesReferralsFor("ldap://" + HOST_NAME + ":" + MASTER_LDAP_PORT)
                .addHandlesReferralsFor("ldap://dummy:389")
                .build());
    }

    /*
     * These default values with with ReferralHandling set to FOLLOW as both
     * master and slave have the same identity defined.
     *
     * Tests can override all three of these methods to force it into THROW mode.
     */

    protected String getSearchDn() {
        return DEFAULT_SEARCH_DN;
    }

    protected String getSearchCredential() {
        return DEFAULT_SEARCH_CREDENTIAL;
    }

    protected ReferralHandling getReferrals() {
        return ReferralHandling.FOLLOW;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1565.java
error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4800.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4800.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4800.java
text:
```scala
public v@@oid injectJtaManager(JtaManager jtaManager) {

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

package org.jboss.as.jpa.hibernate4;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.jboss.as.jpa.spi.JtaManager;
import org.jboss.as.jpa.spi.PersistenceProviderAdaptor;
import org.jboss.as.jpa.spi.PersistenceUnitMetadata;
import org.jboss.as.naming.deployment.ContextNames;
import org.jboss.as.naming.deployment.JndiName;
import org.jboss.msc.service.ServiceName;

import java.util.ArrayList;
import java.util.Map;

/**
 * Implements the PersistenceProviderAdaptor for Hibernate
 *
 * @author Scott Marlow
 */
public class HibernatePersistenceProviderAdaptor implements PersistenceProviderAdaptor {

    private JtaManager jtaManager;

    @Override
    public void setJtaManager(JtaManager jtaManager) {
        this.jtaManager = jtaManager;
    }

    @Override
    public void addProviderProperties(Map properties, PersistenceUnitMetadata pu) {
        properties.put(Configuration.USE_NEW_ID_GENERATOR_MAPPINGS, "true");
        properties.put(org.hibernate.ejb.AvailableSettings.SCANNER, "org.jboss.as.jpa.hibernate4.HibernateAnnotationScanner");
        properties.put(AvailableSettings.APP_CLASSLOADER, pu.getClassLoader());
        properties.put(AvailableSettings.JTA_PLATFORM, new JBossAppServerJtaPlatform(jtaManager));
    }

    @Override
    public Iterable<ServiceName> getProviderDependencies(PersistenceUnitMetadata pu) {
        String cacheManager;
        // AS7-680 Add BinderService dependency for infinispan hibernate 2LC
        if ((cacheManager = pu.getProperties().getProperty("hibernate.cache.infinispan.cachemanager")) != null) {
            ArrayList<ServiceName> result = new ArrayList<ServiceName>();
            result.add(adjustJndiName(cacheManager));
            return result;
        }
        return null;
    }

    private ServiceName adjustJndiName(String jndiName) {
        jndiName = toJndiName(jndiName).toString();
        int index = jndiName.indexOf("/");
        String namespace = (index > 5) ? jndiName.substring(5, index) : null;
        String binding = (index > 5) ? jndiName.substring(index + 1) : jndiName.substring(5);
        ServiceName naming = (namespace != null) ? ContextNames.JAVA_CONTEXT_SERVICE_NAME.append(namespace) : ContextNames.JAVA_CONTEXT_SERVICE_NAME;
        return naming.append(binding);
    }

    private static JndiName toJndiName(String value) {
        return value.startsWith("java:") ? JndiName.of(value) : JndiName.of("java:jboss").append(value.startsWith("/") ? value.substring(1) : value);
    }


    @Override
    public void beforeCreateContainerEntityManagerFactory(PersistenceUnitMetadata pu) {
        // set backdoor annotation scanner access to pu
        HibernateAnnotationScanner.setThreadLocalPersistenceUnitMetadata(pu);
    }

    @Override
    public void afterCreateContainerEntityManagerFactory(PersistenceUnitMetadata pu) {
        // clear backdoor annotation scanner access to pu
        HibernateAnnotationScanner.clearThreadLocalPersistenceUnitMetadata();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4800.java
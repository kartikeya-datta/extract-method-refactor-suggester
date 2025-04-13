error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11651.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11651.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11651.java
text:
```scala
C@@LUSTERED_CACHE_REF("clustered-cache-ref"),

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
package org.jboss.as.ejb3.subsystem;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jaikiran Pai
 */
public enum EJB3SubsystemXMLAttribute {
    UNKNOWN(null),

    ALIASES("aliases"),

    BEAN_CACHE("bean-cache"),

    CACHE_CONTAINER("cache-container"),
    CACHE_REF("cache-ref"),
    @Deprecated CLIENT_MAPPINGS_CACHE("client-mappings-cache"),
    @Deprecated CLUSTERED_CACHE_REF("clustered-cache-ref"),
    CONNECTOR_REF("connector-ref"),
    CORE_THREADS("core-threads"),

    DEFAULT_ACCESS_TIMEOUT("default-access-timeout"),
    DEFAULT_DATA_STORE("default-data-store"),
    DATABASE("database"),
    DATASOURCE_JNDI_NAME("datasource-jndi-name"),

    ENABLED("enabled"),
    ENABLE_BY_DEFAULT("enable-by-default"),

    @Deprecated GROUPS_PATH("groups-path"),

    @Deprecated IDLE_TIMEOUT("idle-timeout"),
    @Deprecated IDLE_TIMEOUT_UNIT("idle-timeout-unit"),
    INSTANCE_ACQUISITION_TIMEOUT("instance-acquisition-timeout"),
    INSTANCE_ACQUISITION_TIMEOUT_UNIT("instance-acquisition-timeout-unit"),

    KEEPALIVE_TIME("keepalive-time"),

    MAX_POOL_SIZE("max-pool-size"),
    MAX_SIZE("max-size"),
    MAX_THREADS("max-threads"),

    NAME("name"),

    PARTITION("partition"),
    PASS_BY_VALUE("pass-by-value"),
    @Deprecated PASSIVATE_EVENTS_ON_REPLICATE("passivate-events-on-replicate"),
    PASSIVATION_DISABLED_CACHE_REF("passivation-disabled-cache-ref"),
    PASSIVATION_STORE_REF("passivation-store-ref"),
    PATH("path"),
    POOL_NAME("pool-name"),

    RELATIVE_TO("relative-to"),
    RESOURCE_ADAPTER_NAME("resource-adapter-name"),

    @Deprecated SESSIONS_PATH("sessions-path"),
    @Deprecated SUBDIRECTORY_COUNT("subdirectory-count"),

    THREAD_POOL_NAME("thread-pool-name"),
    TYPE("type"),

    USE_QUALIFIED_NAME("use-qualified-name"),

    VALUE("value"),
    ;

    private final String name;

    EJB3SubsystemXMLAttribute(final String name) {
        this.name = name;
    }

    /**
     * Get the local name of this attribute.
     *
     * @return the local name
     */
    public String getLocalName() {
        return name;
    }

    private static final Map<String, EJB3SubsystemXMLAttribute> MAP;

    static {
        final Map<String, EJB3SubsystemXMLAttribute> map = new HashMap<String, EJB3SubsystemXMLAttribute>();
        for (EJB3SubsystemXMLAttribute element : values()) {
            final String name = element.getLocalName();
            if (name != null)
                map.put(name, element);
        }
        MAP = map;
    }

    public static EJB3SubsystemXMLAttribute forName(String localName) {
        final EJB3SubsystemXMLAttribute element = MAP.get(localName);
        return element == null ? UNKNOWN : element;
    }

    @Override
    public String toString() {
        return getLocalName();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11651.java
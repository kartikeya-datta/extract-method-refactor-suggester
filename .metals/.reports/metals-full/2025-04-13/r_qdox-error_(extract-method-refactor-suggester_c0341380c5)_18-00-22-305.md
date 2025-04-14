error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3512.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3512.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3512.java
text:
```scala
R@@OLE_ALLOWED("role-allowed"),

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
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

package org.jboss.as.metadata.parser.jbossweb;

import java.util.HashMap;
import java.util.Map;

/**
 * An enumeration of all the possible XML elements in the jboss-web schema, by name.
 *
 * @author Remy Maucherat
 */
public enum Element {
    // must be first
    UNKNOWN(null),

    // jboss-web elements in alpha order
    ANNOTATION("annotation"),

    BACKUPS("backups"),

    CACHE_NAME("cache-name"),
    CLASS_LOADING("class-loading"),
    CLASS_NAME("class-name"),
    CONFIG_FILE("config-file"),
    CONFIG_NAME("config-name"),
    CONTEXT_ROOT("context-root"),

    DEPENDS("depends"),

    EMPTY_ROLE_SEMANTIC("empty-role-semantic"),

    HTTP_METHOD_CONSTRAINT("http-method-constraint"),

    JACC_STAR_ROLE_ALLOW("jacc-star-role-allow"),

    LOADER_REPOSITORY("loader-repository"),
    LOADER_REPOSITORY_CONFIG("loader-repository-config"),

    MAX_ACTIVE_SESSIONS("max-active-sessions"),
    MAX_UNREPLICATED_INTERVAL("max-unreplicated-interval"),
    MESSAGE_DESTINATION("message-destination"),
    METHOD("method"),
    MULTIPART_CONFIG("multipart-config"),

    PASSIVATION_CONFIG("passivation-config"),
    PASSIVATION_MAX_IDLE_TIME("passivation-max-idle-time"),
    PASSIVATION_MIN_IDLE_TIME("passivation-min-idle-time"),

    REPLICATION_CONFIG("replication-config"),
    REPLICATION_GRANULARITY("replication-granularity"),
    REPLICATION_MODE("replication-mode"),
    REPLICATION_TRIGGER("replication-trigger"),
    ROLES_ALLOWED("roles-allowed"),
    RUN_AS("run-as"),
    RUN_AS_PRINCIPAL("run-as-principal"),

    SECURITY_DOMAIN("security-domain"),
    SECURITY_ROLE("security-role"),
    SERVLET("servlet"),
    SERVLET_NAME("servlet-name"),
    SERVLET_SECURITY("servlet-security"),
    SESSION_NOTIFICATION_POLICY("session-notification-policy"),
    SNAPSHOT_INTERVAL("snapshot-interval"),
    SNAPSHOT_MODE("snapshot-mode"),

    TRANSPORT_GUARANTEE("transport-guarantee"),

    USE_JK("use-jk"),
    USE_SESSION_COOKIES("use-session-cookies"),
    USE_SESSION_PASSIVATION("use-session-passivation"),

    VIRTUAL_HOST("virtual-host"),

    WEBSERVICE_DESCRIPTION("webservice-description"),
    WEBSERVICE_DESCRIPTION_NAME("webservice-description-name"),
    WSDL_PUBLISH_LOCATION("wsdl-publish-location"),
    ;

    private final String name;

    Element(final String name) {
        this.name = name;
    }

    /**
     * Get the local name of this element.
     *
     * @return the local name
     */
    public String getLocalName() {
        return name;
    }

    private static final Map<String, Element> MAP;

    static {
        final Map<String, Element> map = new HashMap<String, Element>();
        for (Element element : values()) {
            final String name = element.getLocalName();
            if (name != null) map.put(name, element);
        }
        MAP = map;
    }

    public static Element forName(String localName) {
        final Element element = MAP.get(localName);
        return element == null ? UNKNOWN : element;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3512.java
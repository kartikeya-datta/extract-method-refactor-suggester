error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10966.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10966.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10966.java
text:
```scala
R@@EQUIRES_ADDRESSABLE("requires-addressable"),

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

package org.jboss.as.controller.parsing;

import java.util.HashMap;
import java.util.Map;

/**
 * An enumeration of all the recognized core configuration XML attributes, by local name.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public enum Attribute {
    // always first
    UNKNOWN(null),

    // xsi attributes in alpha order
    NO_NAMESPACE_SCHEMA_LOCATION("noNamespaceSchemaLocation"),
    SCHEMA_LOCATION("schemaLocation"),

    // domain attributes in alpha order
    ALIAS("alias"),
    ALLOW_EMPTY_PASSWORDS("allow-empty-passwords"),
    ALLOWED_USERS("allowed-users"),
    APPLICATION("application"),
    ATTRIBUTE("attribute"),
    AUTO_START("auto-start"),
    BASE_DN("base-dn"),
    BASE_ROLE("base-role"),
    BOOT_TIME("boot-time"),
    CODE("code"),
    CONNECTION("connection"),
    CONNECTOR("connector"),
    CONSOLE_ENABLED("console-enabled"),
    CONTENT("content"),
    DEFAULT_INTERFACE("default-interface"),
    DEFAULT_USER("default-user"),
    DEBUG_ENABLED("debug-enabled"),
    DEBUG_OPTIONS("debug-options"),
    DEPLOYMENT("deployment"),
    DEPLOYMENT_OVERLAY("deployment-overlay"),
    DESTINATION_ADDRESS("destination-address"),
    DIRECTORY_GROUPING("directory-grouping"),
    DESTINATION_PORT("destination-port"),
    ENABLED("enabled"),
    ENV_CLASSPATH_IGNORED("env-classpath-ignored"),
    FILE("file"),
    FILTER("filter"),
    FIXED_PORT("fixed-port"),
    FIXED_SOURCE_PORT("fixed-source-port"),
    GROUP("group"),
    HOST("host"),
    HTTP("http"),
    HTTP_UPGRADE_ENABLED("http-upgrade-enabled"),
    HTTPS("https"),
    IGNORE_UNUSED_CONFIG("ignore-unused-configuration"),
    INITIAL_CONTEXT_FACTORY("initial-context-factory"),
    INTERFACE("interface"),
    JAVA_HOME("java-home"),
    KEY_PASSWORD("key-password"),
    KEYSTORE_PASSWORD("keystore-password"),
    MANAGEMENT_SUBSYSTEM_ENDPOINT("management-subsystem-endpoint"),
    MAP_GROUPS_TO_ROLES("map-groups-to-roles"),
    MAX_SIZE("max-size"),
    MAX_THREADS("max-threads"),
    MECHANISM("mechanism"),
    MODULE("module"),
    MULTICAST_ADDRESS("multicast-address"),
    MULTICAST_PORT("multicast-port"),
    NAME("name"),
    NATIVE("native"),
    PASSWORD("password"),
    PATH("path"),
    PATTERN("pattern"),
    PLAIN_TEXT("plain-text"),
    PORT("port"),
    PORT_OFFSET("port-offset"),
    PREFIX("prefix"),
    PROFILE("profile"),
    PROTOCOL("protocol"),
    PROVIDER("provider"),
    REALM("realm"),
    RECURSIVE("recursive"),
    REF("ref"),
    RELATIVE_TO("relative-to"),
    REQUIRES_ACCESS("requires-access"),
    REQUIRES_READ("requires-read"),
    REQUIRES_WRITE("requires-write"),
    RUNTIME_NAME("runtime-name"),
    SEARCH_CREDENTIAL("search-credential"),
    SEARCH_DN("search-dn"),
    SECURE_PORT("secure-port"),
    SECURITY_REALM("security-realm"),
    SHA1("sha1"),
    SIZE("size"),
    SOCKET_BINDING_GROUP("socket-binding-group"),
    SOCKET_BINDING_REF("socket-binding-ref"),
    SOURCE_INTERFACE("source-interface"),
    SOURCE_NETWORK("source-network"),
    SOURCE_PORT("source-port"),
    TYPE("type"),
    URL("url"),
    USE_REALM_ROLES("use-realm-roles"),
    USER("user"),
    USER_DN("user-dn"),
    USERNAME("username"),
    USERNAME_ATTRIBUTE("username-attribute"),
    VALUE("value"),
    WILDCARD("wildcard")
    ;

    private final String name;

    Attribute(final String name) {
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

    private static final Map<String, Attribute> MAP;

    static {
        final Map<String, Attribute> map = new HashMap<String, Attribute>();
        for (Attribute element : values()) {
            final String name = element.getLocalName();
            if (name != null) map.put(name, element);
        }
        MAP = map;
    }

    public static Attribute forName(String localName) {
        final Attribute element = MAP.get(localName);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10966.java
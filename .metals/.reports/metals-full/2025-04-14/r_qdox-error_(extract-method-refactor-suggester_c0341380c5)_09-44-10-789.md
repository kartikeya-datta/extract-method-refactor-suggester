error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6858.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6858.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6858.java
text:
```scala
I@@GNORED_RESOURCE("ignored-resources"),

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
 * An enumeration of all the recognized XML element local names, by name.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public enum Element {
    // must be first
    UNKNOWN(null),

    // Domain 1.0 elements in alpha order
    ADVANCED_FILTER("advanced-filter"),
    AGENT_LIB("agent-lib"),
    AGENT_PATH("agent-path"),
    ANY("any"),
    ANY_ADDRESS("any-address"),
    ANY_IPV4_ADDRESS("any-ipv4-address"),
    ANY_IPV6_ADDRESS("any-ipv6-address"),
    AUTHENTICATION("authentication"),

    CLIENT_MAPPING("client-mapping"),
    CONTENT("content"),

    DOMAIN("domain"),
    DOMAIN_CONTROLLER("domain-controller"),
    DEPLOYMENT("deployment"),
    DEPLOYMENTS("deployments"),
    DEPLOYMENT_REPOSITORY("deployment-repository"),

    ENVIRONMENT_VARIABLES("environment-variables"),
    EXTENSION("extension"),
    EXTENSIONS("extensions"),

    FS_ARCHIVE("fs-archive"),
    FS_EXPLODED("fs-exploded"),

    HEAP("heap"),
    HOST("host"),
    HTTP_INTERFACE("http-interface"),

    IGNORED_RESOURCE("ignored-resource"),
    INCLUDE("include"),
    INSTANCE("instance"),
    INET_ADDRESS("inet-address"),
    INTERFACE("interface"),
    INTERFACE_SPECS("interface-specs"),
    INTERFACES("interfaces"),

    JAAS("jaas"),
    JAVA_AGENT("java-agent"),
    JVM("jvm"),
    JVMS("jvms"),
    JVM_OPTIONS("jvm-options"),

    KEYSTORE("keystore"),

    LDAP("ldap"),
    LINK_LOCAL_ADDRESS("link-local-address"),
    LOCAL("local"),
    LOCAL_DESTINATION("local-destination"),
    LOOPBACK("loopback"),
    LOOPBACK_ADDRESS("loopback-address"),

    MANAGEMENT("management"),
    MANAGEMENT_CLIENT_CONTENT("management-client-content"),
    MANAGEMENT_INTERFACES("management-interfaces"),
    MULTICAST("multicast"),

    NAME("name"),
    NATIVE_INTERFACE("native-interface"),
    NATIVE_REMOTING_INTERFACE("native-remoting-interface"),
    NIC("nic"),
    NIC_MATCH("nic-match"),
    NOT("not"),

    OPTION("option"),
    OUTBOUND_CONNECTIONS("outbound-connections"),
    OUTBOUND_SOCKET_BINDING("outbound-socket-binding"),


    PASSWORD("password"),
    PATH("path"),
    PATHS("paths"),

    POINT_TO_POINT("point-to-point"),
    PERMGEN("permgen"),
    PROFILE("profile"),
    PROFILES("profiles"),
    PROPERTY("property"),
    PROPERTIES("properties"),
    PUBLIC_ADDRESS("public-address"),

    REMOTE("remote"),
    REMOTE_DESTINATION("remote-destination"),
    ROLLOUT_PLANS("rollout-plans"),

    SECRET("secret"),
    SECURITY_REALM("security-realm"),
    SECURITY_REALMS("security-realms"),
    SERVER("server"),
    SERVER_IDENTITIES("server-identities"),
    SERVERS("servers"),
    SERVER_GROUP("server-group"),
    SERVER_GROUPS("server-groups"),
    SITE_LOCAL_ADDRESS("site-local-address"),
    SOCKET("socket"),
    SOCKET_BINDING("socket-binding"),
    SOCKET_BINDINGS("socket-bindings"),
    SOCKET_BINDING_GROUP("socket-binding-group"),
    SOCKET_BINDING_GROUPS("socket-binding-groups"),
    SSL("ssl"),
    STACK("stack"),
    STANDALONE("standalone"),
    SUBNET_MATCH("subnet-match"),
    SUBSYSTEM("subsystem"),
    SYSTEM_PROPERTIES("system-properties"),

    TRUSTSTORE("truststore"),

    UP("up"),
    USER("user"),
    USERNAME_FILTER("username-filter"),
    USERS("users"),

    VARIABLE("variable"),
    VIRTUAL("virtual"),
    VAULT("vault"),
    VAULT_OPTION("vault-option")
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6858.java
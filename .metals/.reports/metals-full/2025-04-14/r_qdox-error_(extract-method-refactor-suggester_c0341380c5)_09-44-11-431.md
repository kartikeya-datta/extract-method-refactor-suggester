error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11397.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11397.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11397.java
text:
```scala
S@@tring VAULT_OPTIONS = "vault-options";

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

package org.jboss.as.security;

/**
 * Attributes used by the security subsystem.
 *
 * @author <a href="mailto:darran.lofthouse@jboss.com">Darran Lofthouse</a>
 */
public interface Constants {

    String ACL = "acl";
    String ACL_MODULE = "acl-module";
    String ACL_MODULES = "acl-modules";
    String ADDITIONAL_PROPERTIES = "additional-properties";
    String ALGORITHM = "algorithm";
    String AUDIT = "audit";
    String AUDIT_MANAGER_CLASS_NAME = "audit-manager-class-name";
    String AUTH_MODULE = "auth-module";
    String AUTH_MODULES = "auth-modules";
    String AUTHENTICATION = "authentication";
    String AUTHENTICATION_JASPI = "authentication-jaspi";
    String AUTHENTICATION_MANAGER_CLASS_NAME = "authentication-manager-class-name";
    String AUTHORIZATION = "authorization";
    String AUTHORIZATION_MANAGER_CLASS_NAME = "authorization-manager-class-name";
    String CACHE_TYPE = "cache-type";
    String CIPHER_SUITES = "cipher-suites";
    String CLASSIC = "classic";
    String CLIENT_ALIAS = "client-alias";
    String CLIENT_AUTH = "client-auth";
    String CODE = "code";
    String DEEP_COPY_SUBJECT_MODE = "deep-copy-subject-mode";
    String DEFAULT_CALLBACK_HANDLER_CLASS_NAME = "default-callback-handler-class-name";
    String EXTENDS = "extends";
    String FLAG = "flag";
    String IDENTITY_TRUST = "identity-trust";
    String IDENTITY_TRUST_MANAGER_CLASS_NAME = "identity-trust-manager-class-name";
    String JASPI = "jaspi";
    String JSSE = "jsse";
    String KEY_MANAGER = "key-manager";
    String KEY_MANAGER_FACTORY_ALGORITHM = "key-manager-factory-algorithm";
    String KEY_MANAGER_FACTORY_PROVIDER = "key-manager-factory-provider";
    String KEYSTORE = "keystore";
    String KEYSTORE_PASSWORD = "keystore-password";
    String KEYSTORE_PROVIDER = "keystore-provider";
    String KEYSTORE_PROVIDER_ARGUMENT = "keystore-provider-argument";
    String KEYSTORE_TYPE = "keystore-type";
    String KEYSTORE_URL = "keystore-url";
    String LOGIN_MODULES = "login-modules";
    String LOGIN_MODULE = "login-module";
    String LOGIN_MODULE_STACK = "login-module-stack";
    String LOGIN_MODULE_STACK_REF = "login-module-stack-ref";
    String MAPPING = "mapping";
    String MAPPING_MANAGER_CLASS_NAME = "mapping-manager-class-name";
    String MAPPING_MODULE = "mapping-module";
    String MAPPING_MODULES = "mapping-modules";
    String MODULE = "module";
    String MODULE_OPTIONS = "module-options";
    String NAME = "name";
    String OPTIONAL = "optional";
    String PASSWORD = "password";
    String POLICY_MODULE = "policy-module";
    String POLICY_MODULES = "policy-modules";
    String PROTOCOLS = "protocols";
    String PROPERTY= "property";
    String PROVIDER = "provider";
    String PROVIDER_ARGUMENT = "provider-argument";
    String PROVIDER_MODULE = "provider-module";
    String PROVIDER_MODULES = "provider-modules";
    String REQUIRED = "required";
    String REQUISITE = "requisite";
    String SECURITY_MANAGEMENT = "security-management";
    String SECURITY_DOMAIN = "security-domain";
    String SECURITY_PROPERTIES = "security-properties";
    String SERVER_ALIAS = "server-alias";
    String SERVICE_AUTH_TOKEN = "service-auth-token";
    String SUBJECT_FACTORY = "subject-factory";
    String SUBJECT_FACTORY_CLASS_NAME = "subject-factory-class-name";
    String SUFFICIENT = "sufficient";
    String TRUST_MANAGER = "trust-manager";
    String TRUST_MANAGER_FACTORY_ALGORITHM = "trust-manager-factory-algorithm";
    String TRUST_MANAGER_FACTORY_PROVIDER = "trust-manager-factory-provider";
    String TRUST_MODULE = "trust-module";
    String TRUST_MODULES = "trust-modules";
    String TRUSTSTORE = "truststore";
    String TRUSTSTORE_PASSWORD = "truststore-password";
    String TRUSTSTORE_PROVIDER = "truststore-provider";
    String TRUSTSTORE_PROVIDER_ARGUMENT = "truststore-provider-argument";
    String TRUSTSTORE_TYPE = "truststore-type";
    String TRUSTSTORE_URL = "truststore-url";
    String TYPE = "type";
    String URL = "url";
    String VALUE = "value";
    String VAULT = "vault";
    String VAULT_OPTION = "vault-option";
    String OPTIONS = "options";
    String LIST_CACHED_PRINCIPALS = "list-cached-principals";
    String FLUSH_CACHE = "flush-cache";
    String PRINCIPAL_ARGUMENT = "principal";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11397.java
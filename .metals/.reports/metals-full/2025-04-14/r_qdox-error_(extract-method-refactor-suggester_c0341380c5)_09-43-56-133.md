error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11681.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11681.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11681.java
text:
```scala
S@@tring CONTAINER_CONFIG = CONFIGURATION;

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

package org.jboss.as.web;

/**
 * @author Emanuel Muckenhuber
 */
interface Constants {

    String ACCESS_LOG = "access-log";
    String ALIAS = "alias";
    String CA_CERTIFICATE_FILE = "ca-certificate-file";
    String CA_CERTIFICATE_PASSWORD = "ca-certificate-password";
    String CA_REVOCATION_URL = "ca-revocation-url";
    String CACHE_CONTAINER = "cache-container";
    String CACHE_NAME = "cache-name";
    String CERTIFICATE_FILE = "certificate-file";
    String CERTIFICATE_KEY_FILE = "certificate-key-file";
    String CHECK_INTERVAL = "check-interval";
    String CIPHER_SUITE = "cipher-suite";
    String CONDITION = "condition";
    String CONFIGURATION = "configuration";
    String CONNECTOR = "connector";
    String CONTAINER = "container";
    String CONTAINER_CONFIG = "configuration";
    String DEFAULT_VIRTUAL_SERVER = "default-virtual-server";
    String DEFAULT_WEB_MODULE = "default-web-module";
    String DEVELOPMENT = "development";
    String DIRECTORY = "directory";
    String DISABLED = "disabled";
    String DISPLAY_SOURCE_FRAGMENT = "display-source-fragment";
    String DOMAIN = "domain";
    String DUMP_SMAP = "dump-smap";
    String ENABLED = "enabled";
    String ENABLE_LOOKUPS = "enable-lookups";
    String ENABLE_WELCOME_ROOT = "enable-welcome-root";
    String ERROR_ON_USE_BEAN_INVALID_CLASS_ATTRIBUTE = "error-on-use-bean-invalid-class-attribute";
    String EXECUTOR = "executor";
    String EXTENDED = "extended";
    String FILE_ENCONDING = "file-encoding";
    String FLAGS = "flags";
    String GENERATE_STRINGS_AS_CHAR_ARRAYS = "generate-strings-as-char-arrays";
    String INSTANCE_ID = "instance-id";
    String JAVA_ENCODING = "java-encoding";
    String JSP_CONFIGURATION = "jsp-configuration";
    String KEEP_GENERATED = "keep-generated";
    String KEY_ALIAS = "key-alias";
    String KEYSTORE_TYPE = "keystore-type";
    String LISTINGS = "listings";
    String MAPPED_FILE = "mapped-file";
    String MAX_CONNECTIONS = "max-connections";
    String MAX_DEPTH = "max-depth";
    String MAX_POST_SIZE = "max-post-size";
    String MAX_SAVE_POST_SIZE = "max-save-post-size";
    String MIME_MAPPING = "mime-mapping";
    String MODIFIFICATION_TEST_INTERVAL = "modification-test-interval";
    String NAME = "name";
    String NATIVE = "native";
    String PASSWORD = "password";
    String PATH = "path";
    String PATTERN = "pattern";
    String PREFIX = "prefix";
    String PROTOCOL = "protocol";
    String PROXY_NAME = "proxy-name";
    String PROXY_PORT = "proxy-port";
    String REAUTHENTICATE = "reauthenticate";
    String READ_ONLY = "read-only";
    String RECOMPILE_ON_FAIL = "recompile-on-fail";
    String REDIRECT_PORT = "redirect-port";
    String RELATIVE_TO = "relative-to";
    String RESOLVE_HOSTS = "resolve-hosts";
    String REWRITE = "rewrite";
    String ROTATE = "rotate";
    String SCHEME = "scheme";
    String SCRATCH_DIR = "scratch-dir";
    String SECRET = "secret";
    String SECURE = "secure";
    String SENDFILE = "sendfile";
    String SESSION_CACHE_SIZE = "session-cache-size";
    String SESSION_TIMEOUT = "session-timeout";
    String SMAP = "smap";
    String SOCKET_BINDING = "socket-binding";
    String SOURCE_VM = "source-vm";
    String SSL = "ssl";
    String SSO = "sso";
    String STATIC_RESOURCES = "static-resources";
    String SUBSTITUTION = "substitution";
    String SUBSYSTEM = "subsystem";
    String TAG_POOLING = "tag-pooling";
    String TARGET_VM = "target-vm";
    String TEST = "test";
    String TRIM_SPACES = "trim-spaces";
    String TRUSTSTORE_TYPE = "truststore-type";
    String VALUE = "value";
    String VERIFY_CLIENT = "verify-client";
    String VERIFY_DEPTH = "verify-depth";
    String VIRTUAL_SERVER = "virtual-server";
    String WEBDAV = "webdav";
    String WELCOME_FILE = "welcome-file";
    String X_POWERED_BY = "x-powered-by";

    /* Connect stats attributes */
    String BYTES_SENT = "bytesSent";
    String BYTES_RECEIVED = "bytesReceived";
    String PROCESSING_TIME = "processingTime";
    String ERROR_COUNT = "errorCount";
    String MAX_TIME = "maxTime";
    String REQUEST_COUNT = "requestCount";


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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11681.java
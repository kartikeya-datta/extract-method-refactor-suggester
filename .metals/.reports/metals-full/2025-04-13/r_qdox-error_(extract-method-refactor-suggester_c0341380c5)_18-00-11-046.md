error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8345.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8345.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8345.java
text:
```scala
public static final i@@nt ARQUILLIAN_RUNWITH_ANNOTATION_PROCESSOR     = PARSE + 0xF00;

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

package org.jboss.as.deployment;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public final class Phase {
    private Phase() {}

    public static final int STRUCTURE       = 0x00100000;

    public static final int NESTED_JAR_INLINE_PROCESSOR                 = STRUCTURE + 0x100;
    public static final int RA_NESTED_JAR_INLINE_PROCESSOR              = STRUCTURE + 0x200;
    public static final int WAR_DEPLOYMENT_INITIALIZING_PROCESSOR       = STRUCTURE + 0x300;

    public static final int VALIDATE        = 0x00200000;

    public static final int WAR_STRUCTURE_DEPLOYMENT_PROCESSOR          = VALIDATE + 0x100;

    public static final int PARSE           = 0x00300000;

    public static final int MANIFEST_ATTACHMENT_PROCESSOR               = PARSE + 0x100;
    public static final int OSGI_MANIFEST_ATTACHMENT_PROCESSOR          = PARSE + 0x200;
    public static final int OSGI_BUNDLE_INFO_ATTACHMENT_PROCESSOR       = PARSE + 0x250;
    public static final int ANNOTATION_INDEX_PROCESSOR                  = PARSE + 0x300;
    public static final int WAR_ANNOTATION_INDEX_PROCESSOR              = PARSE + 0x400;
    public static final int SERVICE_ACTIVATION_DEPENDENCY_PROCESSOR     = PARSE + 0x500;
    public static final int WEB_PARSING_DEPLOYMENT_PROCESSOR            = PARSE + 0x600;
    public static final int WEB_FRAGMENT_PARSING_DEPLOYMENT_PROCESSOR   = PARSE + 0x700;
    public static final int JBOSS_WEB_PARSING_DEPLOYMENT_PROCESSOR      = PARSE + 0x800;
    public static final int TLD_PARSING_DEPLOYMENT_PROCESSOR            = PARSE + 0x900;
    public static final int RA_DEPLOYMENT_PARSING_PROCESSOR             = PARSE + 0xA00;
    public static final int SERVICE_DEPLOYMENT_PARSING_PROCESSOR        = PARSE + 0xB00;
    public static final int MC_BEAN_DEPLOYMENT_PARSING_PROCESSOR        = PARSE + 0xB80;
    public static final int IRON_JACAMAR_DEPLOYMENT_PARSING_PROCESSOR   = PARSE + 0xC00;
    public static final int RESOURCE_ADAPTERS_ATTACHING_PROCESSOR       = PARSE + 0xD00;
    public static final int DATA_SOURCES_ATTACHMENT_PROCESSOR           = PARSE + 0xE00;
    public static final int ARQUILLIAN_MANIFEST_PROCESSOR               = PARSE + 0xF00;

    public static final int DEPENDENCIES    = 0x00400000;

    public static final int MODULE_DEPENDENCY_PROCESSOR                 = DEPENDENCIES + 0x100;
    public static final int DS_DEPENDENCY_PROCESSOR                     = DEPENDENCIES + 0x200;
    public static final int RAR_CONFIG_PROCESSOR                        = DEPENDENCIES + 0x300;
    public static final int MANAGED_BEAN_DEPENDENCY_PROCESSOR           = DEPENDENCIES + 0x400;
    public static final int SAR_MODULE_DEPENDENCY_PROCESSOR             = DEPENDENCIES + 0x500;
    public static final int WAR_CLASSLOADING_DEPENDENCY_PROCESSOR       = DEPENDENCIES + 0x600;
    public static final int ARQUILLIAN_DEPENDENCY_PROCESSOR             = DEPENDENCIES + 0x700;

    public static final int MODULARIZE      = 0x00500000;

    public static final int WAR_MODULE_CONFIG_PROCESSOR                 = MODULARIZE + 0x100;
    public static final int MODULE_CONFIG_PROCESSOR                     = MODULARIZE + 0x200;
    public static final int DEPLOYMENT_MODULE_LOADER_PROCESSOR          = MODULARIZE + 0x300;
    public static final int MODULE_DEPLOYMENT_PROCESSOR                 = MODULARIZE + 0x400;

    public static final int POST_MODULE     = 0x00600000;

    public static final int MANAGED_BEAN_ANNOTATION_PROCESSOR           = POST_MODULE + 0x100;
    public static final int WAR_ANNOTATION_DEPLOYMENT_PROCESSOR         = POST_MODULE + 0x200;
    public static final int ARQUILLIAN_JUNIT_ANNOTATION_PROCESSOR       = POST_MODULE + 0x300;


    public static final int INSTALL         = 0x00700000;

    public static final int MODULE_CONTEXT_PROCESSOR                    = INSTALL + 0x100;
    public static final int SERVICE_ACTIVATOR_PROCESSOR                 = INSTALL + 0x200;
    public static final int OSGI_ATTACHMENTS_DEPLOYMENT_PROCESSOR       = INSTALL + 0x300;
    public static final int WAR_META_DATA_PROCESSOR                     = INSTALL + 0x400;
    public static final int PARSED_RA_DEPLOYMENT_PROCESSOR              = INSTALL + 0x500;
    public static final int PARSED_SERVICE_DEPLOYMENT_PROCESSOR         = INSTALL + 0x600;
    public static final int PARSED_MC_BEAN_DEPLOYMENT_PROCESSOR         = INSTALL + 0x680;

    public static final int RA_XML_DEPLOYMENT_PROCESSOR                 = INSTALL + 0x700;
    public static final int DS_DEPLOYMENT_PROCESSOR                     = INSTALL + 0x800;
    public static final int MANAGED_BEAN_DEPLOYMENT_PROCESSOR           = INSTALL + 0x900;
    public static final int SERVLET_CONTAINER_INITIALIZER_DEPLOYMENT_PROCESSOR  = INSTALL + 0xA00;
    public static final int WAR_DEPLOYMENT_PROCESSOR                    = INSTALL + 0xB00;
    public static final int ARQUILLIAN_DEPLOYMENT_PROCESSOR             = INSTALL + 0xC00;

    public static final int CLEANUP         = 0x00800000;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8345.java
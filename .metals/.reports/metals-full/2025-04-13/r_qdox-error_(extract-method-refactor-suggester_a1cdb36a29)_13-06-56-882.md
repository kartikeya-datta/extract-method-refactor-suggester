error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14862.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14862.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14862.java
text:
```scala
public static final i@@nt REMOTE_EXCEPTION_TRANSFORMER                            = 0x100;

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
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
package org.jboss.as.ee.component.interceptors;

/**
 * Class that maintains interceptor ordering for various interceptor chains
 *
 * @author Stuart Douglas
 */
public class
        InterceptorOrder {

    private InterceptorOrder() {

    }


    public static final class Component {


        public static final int INITIAL_INTERCEPTOR                                         = 0x100;
        public static final int TCCL_INTERCEPTOR                                            = 0x200;
        public static final int JNDI_NAMESPACE_INTERCEPTOR                                  = 0x300;
        public static final int TIMEOUT_INVOCATION_CONTEXT_INTERCEPTOR                      = 0x310;
        public static final int CDI_REQUEST_SCOPE                                           = 0x320;
        public static final int BMT_TRANSACTION_INTERCEPTOR                                 = 0x400;
        public static final int TIMEOUT_CMT_INTERCEPTOR                                     = 0x410;
        public static final int SYNCHRONIZATION_INTERCEPTOR                                 = 0x500;
        public static final int REENTRANCY_INTERCEPTOR                                      = 0x501;
        public static final int JPA_SESSION_BEAN_INTERCEPTOR                                = 0x600;
        public static final int SINGLETON_CONTAINER_MANAGED_CONCURRENCY_INTERCEPTOR         = 0x700;
        public static final int CMP_RELATIONSHIP_INTERCEPTOR                                = 0x800;

        /**
         * All user level interceptors are added with the same priority, so they execute
         * in the order that they are added.
         */
        public static final int USER_INTERCEPTORS                                           = 0x900;
        public static final int CDI_INTERCEPTORS                                            = 0xA00;
        public static final int TERMINAL_INTERCEPTOR                                        = 0xB00;

        private Component() {
        }

    }

    public static final class ComponentPostConstruct {

        public static final int TCCL_INTERCEPTOR = 0x100;
        public static final int EJB_SESSION_CONTEXT_INTERCEPTOR = 0x200;
        public static final int EJB_CLIENT_CONTEXT_INTERCEPTOR = 0x250;
        public static final int TRANSACTION_INTERCEPTOR = 0x300;
        public static final int JPA_SFSB_PRE_CREATE = 0x400;
        public static final int JNDI_NAMESPACE_INTERCEPTOR = 0x500;
        public static final int INSTANTIATION_INTERCEPTORS = 0x600;
        public static final int RESOURCE_INJECTION_INTERCEPTORS = 0x700;
        public static final int EJB_SET_SESSION_CONTEXT_METHOD_INVOCATION_INTERCEPTOR = 0x800;
        public static final int WELD_INJECTION = 0x900;
        public static final int JPA_SFSB_CREATE = 0xA00;
        public static final int DEPENDENCY_INJECTION_COMPLETE = 0xA50;
        public static final int USER_INTERCEPTORS = 0xB00;
        public static final int CDI_INTERCEPTORS = 0xC00;
        public static final int SFSB_INIT_METHOD = 0xD00;
        public static final int SETUP_CONTEXT = 0xE00;
        public static final int TERMINAL_INTERCEPTOR = 0xF00;

        private ComponentPostConstruct() {
        }

    }

    public static final class ComponentPreDestroy {

        public static final int TCCL_INTERCEPTOR = 0x100;
        public static final int EJB_SESSION_CONTEXT_INTERCEPTOR = 0x200;
        public static final int EJB_CLIENT_CONTEXT_INTERCEPTOR = 0x250;
        public static final int TRANSACTION_INTERCEPTOR = 0x300;
        public static final int JNDI_NAMESPACE_INTERCEPTOR = 0x400;
        public static final int JPA_SFSB_DESTROY = 0x500;
        public static final int UNINJECTION_INTERCEPTORS = 0x600;
        public static final int DESTRUCTION_INTERCEPTORS = 0x700;
        public static final int USER_INTERCEPTORS = 0x800;
        public static final int CDI_INTERCEPTORS = 0x900;
        public static final int TERMINAL_INTERCEPTOR = 0xA00;

        private ComponentPreDestroy() {
        }

    }

    public static final class View {

        public static final int NOT_BUSINESS_METHOD                                     = 0x000;
        public static final int NO_SUCH_OBJECT_TRANSFORMER                              = 0x100;
        public static final int SECURITY_CONTEXT                                        = 0x150;
        public static final int EJB_SECURITY_AUTHORIZATION_INTERCEPTOR                  = 0x200;
        public static final int EJB_CLIENT_CONTEXT                                      = 0x250;
        public static final int INVOCATION_CONTEXT_INTERCEPTOR                          = 0x300;
        // should happen before the CMT/BMT interceptors
        public static final int REMOTE_TRANSACTION_PROPOGATION_INTERCEPTOR              = 0x350;
        public static final int CMT_TRANSACTION_INTERCEPTOR                             = 0x400;
        public static final int ASSOCIATING_INTERCEPTOR                                 = 0x500;
        public static final int JPA_SFSB_INTERCEPTOR                                    = 0x600;
        public static final int SESSION_REMOVE_INTERCEPTOR                              = 0x700;
        public static final int HOME_CREATE_INTERCEPTOR                                 = 0x800;
        public static final int COMPONENT_DISPATCHER                                    = 0x900;


        private View() {
        }
    }

    public static final class Client {

        public static final int TO_STRING = 0x100;
        public static final int LOCAL_ASYNC_INVOCATION = 0x200;
        public static final int ASSOCIATING_INTERCEPTOR = 0x300;
        public static final int EJB_EQUALS_HASHCODE = 0x400;
        public static final int WRITE_REPLACE = 0x500;
        public static final int CLIENT_DISPATCHER = 0x600;

        private Client() {
        }
    }

    public static final class ClientPreDestroy {

        public static final int INSTANCE_DESTROY = 0x100;
        public static final int TERMINAL_INTERCEPTOR = 0x200;

        private ClientPreDestroy() {
        }
    }

    public static final class ClientPostConstruct {

        public static final int INSTANCE_CREATE      = 0x100;
        public static final int TERMINAL_INTERCEPTOR = 0x200;

        private ClientPostConstruct() {
        }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14862.java
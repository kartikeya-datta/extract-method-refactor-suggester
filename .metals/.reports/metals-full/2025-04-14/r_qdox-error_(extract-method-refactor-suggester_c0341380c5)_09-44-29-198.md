error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5555.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5555.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5555.java
text:
```scala
final E@@EModuleDescription desc = componentDescription.getModuleDescription();

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

package org.jboss.as.ejb3.component;

import org.jboss.as.ee.component.ComponentConfiguration;
import org.jboss.as.ee.component.ComponentDescription;
import org.jboss.as.ee.component.EEModuleDescription;
import org.jboss.as.ee.component.InjectionSource;
import org.jboss.as.ee.component.ViewConfiguration;
import org.jboss.as.ee.component.ViewDescription;
import org.jboss.as.ejb3.remote.RemoteViewInjectionSource;
import org.jboss.invocation.proxy.ProxyFactory;
import org.jboss.msc.service.ServiceName;

/**
 * EJB specific view description.
 *
 * @author <a href="mailto:ropalka@redhat.com">Richard Opalka</a>
 */
public class EJBViewDescription extends ViewDescription {

    private final MethodIntf methodIntf;
    private final boolean hasJNDIBindings;

    /**
     * Should be set to true if this view corresponds to a EJB 2.x
     * local or remote view
     */
    private boolean ejb2xView = false;

    public EJBViewDescription(final ComponentDescription componentDescription, final String viewClassName, final MethodIntf methodIntf) {
        super(componentDescription, viewClassName);
        this.methodIntf = methodIntf;
        hasJNDIBindings = initHasJNDIBindings(methodIntf);
    }

    public MethodIntf getMethodIntf() {
        return methodIntf;
    }

    @Override
    public ViewConfiguration createViewConfiguration(final Class<?> viewClass, final ComponentConfiguration componentConfiguration, final ProxyFactory<?> proxyFactory) {
        return new EJBViewConfiguration(viewClass, componentConfiguration, getServiceName(), proxyFactory, getMethodIntf());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EJBViewDescription that = (EJBViewDescription) o;

        // since the views are added to the component description, that should already be equal
        if (hasJNDIBindings != that.hasJNDIBindings) return false;
        if (methodIntf != that.methodIntf) return false;
        if (!getViewClassName().equals(that.getViewClassName())) return false;

        return true;
    }

    @Override
    protected InjectionSource createInjectionSource(final ServiceName serviceName) {
        if(methodIntf != MethodIntf.REMOTE) {
            return super.createInjectionSource(serviceName);
        } else {
            final EJBComponentDescription componentDescription = getComponentDescription();
            EEModuleDescription desc = componentDescription.getModuleDescription();
            return new RemoteViewInjectionSource(serviceName, desc.getApplicationName(), desc.getModuleName(), desc.getDistinctName(), componentDescription.getComponentName(), getViewClassName() , componentDescription.isStateful());
        }
    }

    @Override
    public EJBComponentDescription getComponentDescription() {
        return (EJBComponentDescription)super.getComponentDescription();
    }

    @Override // TODO: what to do in JNDI if multiple views are available for no interface view ?
    public ServiceName getServiceName() {
        return super.getServiceName().append(methodIntf.toString());
    }

    @Override
    public int hashCode() {
        int result = methodIntf.hashCode();
        result = 31 * result + (hasJNDIBindings ? 1 : 0);
        result = 31 * result + getViewClassName().hashCode();
        return result;
    }

    public boolean hasJNDIBindings() {
        return hasJNDIBindings;
    }

    private boolean initHasJNDIBindings(final MethodIntf methodIntf) {
        if (methodIntf == MethodIntf.MESSAGE_ENDPOINT) {
            return false;
        }
        if (methodIntf == MethodIntf.SERVICE_ENDPOINT) {
            return false;
        }
        if (methodIntf == MethodIntf.TIMER) {
            return false;
        }

        return true;
    }

    public boolean isEjb2xView() {
        return ejb2xView;
    }

    public void setEjb2xView(final boolean ejb2xView) {
        this.ejb2xView = ejb2xView;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5555.java
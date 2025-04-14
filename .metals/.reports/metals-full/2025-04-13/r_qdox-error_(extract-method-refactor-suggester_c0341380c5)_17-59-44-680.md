error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11644.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11644.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11644.java
text:
```scala
B@@eansXml.EMPTY_BEANS_XML, module, getClass().getName() + ADDITIONAL_CLASSES_BDA_SUFFIX, false);

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

package org.jboss.as.weld.deployment;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.enterprise.inject.spi.Extension;

import org.jboss.as.server.deployment.AttachmentKey;
import org.jboss.as.weld.WeldModuleResourceLoader;
import org.jboss.as.weld.services.bootstrap.ProxyServicesImpl;
import org.jboss.modules.Module;
import org.jboss.weld.bootstrap.api.ServiceRegistry;
import org.jboss.weld.bootstrap.api.helpers.SimpleServiceRegistry;
import org.jboss.weld.bootstrap.spi.BeanDeploymentArchive;
import org.jboss.weld.bootstrap.spi.BeansXml;
import org.jboss.weld.bootstrap.spi.Deployment;
import org.jboss.weld.bootstrap.spi.Metadata;
import org.jboss.weld.resources.spi.ResourceLoader;
import org.jboss.weld.serialization.spi.ProxyServices;

/**
 * Abstract implementation of {@link Deployment}.
 * <p>
 * Thread safety: This class is thread safe, and does not require a happens before action between construction and usage
 *
 * @author Stuart Douglas
 *
 */
public class WeldDeployment implements Deployment {

    public static final AttachmentKey<WeldDeployment> ATTACHMENT_KEY = AttachmentKey.create(WeldDeployment.class);

    public static final String ADDITIONAL_CLASSES_BDA_SUFFIX = ".additionalClasses";

    private final Set<BeanDeploymentArchiveImpl> beanDeploymentArchives;

    /**
     * The bean deployment archive used for classes added through the SPI that are not present in a existing bean archive
     */
    private final BeanDeploymentArchiveImpl additionalBeanDeploymentArchive;

    /**
     * This bean deployment archive does not expose any classes, however it has visibility to every BDA in the deployment
     */
    private final BeanDeploymentArchiveImpl topLevelBeanDeploymentArchive;

    private final Set<Metadata<Extension>> extensions;

    private final ServiceRegistry serviceRegistry;

    private final Module module;

    /**
     * Maps class names to bean archives.
     *
     * The spec does not allow for the same class to be deployed in multiple bean archives
     */
    private final Map<String, BeanDeploymentArchiveImpl> beanDeploymentsByClassName;

    public WeldDeployment(Set<BeanDeploymentArchiveImpl> beanDeploymentArchives, BeanDeploymentArchiveImpl rootBda,
            Set<Metadata<Extension>> extensions,
            Module module) {
        this.additionalBeanDeploymentArchive = new BeanDeploymentArchiveImpl(Collections.<String> emptySet(),
                BeansXml.EMPTY_BEANS_XML, module, getClass().getName() + ADDITIONAL_CLASSES_BDA_SUFFIX);

        this.topLevelBeanDeploymentArchive = rootBda;
        this.beanDeploymentArchives = new HashSet<BeanDeploymentArchiveImpl>(beanDeploymentArchives);
        this.extensions = new HashSet<Metadata<Extension>>(extensions);
        this.serviceRegistry = new SimpleServiceRegistry();
        this.beanDeploymentsByClassName = new HashMap<String, BeanDeploymentArchiveImpl>();
        this.module = module;

        // add static services
        this.serviceRegistry.add(ProxyServices.class, new ProxyServicesImpl(module));
        this.serviceRegistry.add(ResourceLoader.class, new WeldModuleResourceLoader(module));

        // set up the additional bean archives accessibility rules
        // and map class names to bean deployment archives
        for (BeanDeploymentArchiveImpl bda : beanDeploymentArchives) {
            bda.addBeanDeploymentArchive(additionalBeanDeploymentArchive);
            for (String className : bda.getBeanClasses()) {
                beanDeploymentsByClassName.put(className, bda);
            }
        }
        additionalBeanDeploymentArchive.addBeanDeploymentArchives(this.beanDeploymentArchives);
        topLevelBeanDeploymentArchive.addBeanDeploymentArchives(this.beanDeploymentArchives);
        this.beanDeploymentArchives.add(topLevelBeanDeploymentArchive);
    }

    /** {@inheritDoc} */
    public Collection<BeanDeploymentArchive> getBeanDeploymentArchives() {
        return Collections.unmodifiableSet(new HashSet<BeanDeploymentArchive>(beanDeploymentArchives));
    }

    /** {@inheritDoc} */
    public Iterable<Metadata<Extension>> getExtensions() {
        return Collections.unmodifiableSet(extensions);
    }

    /** {@inheritDoc} */
    public ServiceRegistry getServices() {
        return serviceRegistry;
    }

    /** {@inheritDoc} */
    public synchronized BeanDeploymentArchive loadBeanDeploymentArchive(Class<?> beanClass) {
        if (beanDeploymentsByClassName.containsKey(beanClass.getName())) {
            return beanDeploymentsByClassName.get(beanClass.getName());
        }
        // if this is a class we have not seen before add it to the additional classes BDA
        additionalBeanDeploymentArchive.addBeanClass(beanClass);
        beanDeploymentsByClassName.put(beanClass.getName(), additionalBeanDeploymentArchive);
        return additionalBeanDeploymentArchive;
    }

    public BeanDeploymentArchiveImpl getAdditionalBeanDeploymentArchive() {
        return additionalBeanDeploymentArchive;
    }

    public BeanDeploymentArchiveImpl getTopLevelBeanDeploymentArchive() {
        return topLevelBeanDeploymentArchive;
    }

    public Module getModule() {
        return module;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11644.java
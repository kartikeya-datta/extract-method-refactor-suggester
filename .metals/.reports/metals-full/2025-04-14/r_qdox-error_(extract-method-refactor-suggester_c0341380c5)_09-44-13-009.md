error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4796.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4796.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4796.java
text:
```scala
private static final T@@hreadLocal<PersistenceUnitMetadata> persistenceUnitMetadataTLS = new ThreadLocal<PersistenceUnitMetadata>();

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

package org.jboss.as.jpa.hibernate3;

import org.hibernate.ejb.packaging.NamedInputStream;
import org.hibernate.ejb.packaging.Scanner;
import org.jboss.as.jpa.spi.PersistenceUnitMetadata;
import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;
import org.jboss.jandex.Index;
import org.jboss.logging.Logger;
import org.jboss.vfs.VFS;
import org.jboss.vfs.VirtualFile;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Annotation scanner for Hibernate
 *
 * @author Scott Marlow (forked from Ales Justin's ScannerImpl in AS6)
 */
public class HibernateAnnotationScanner implements Scanner {

    private static ThreadLocal<PersistenceUnitMetadata> persistenceUnitMetadataTLS = new ThreadLocal<PersistenceUnitMetadata>();
    private static final Logger log = Logger.getLogger("org.jboss.jpa");

    public static void setThreadLocalPersistenceUnitMetadata(final PersistenceUnitMetadata pu) {
        persistenceUnitMetadataTLS.set(pu);
    }

    public static void clearThreadLocalPersistenceUnitMetadata() {
        persistenceUnitMetadataTLS.remove();
    }

    @Override
    public Set<Package> getPackagesInJar(URL jartoScan, Set<Class<? extends Annotation>> annotationsToLookFor) {

        log.tracef("getPackagesInJar url=%s annotations=%s", jartoScan.getPath(), annotationsToLookFor);
        Set<Class<?>> resultClasses = new HashSet<Class<?>>();

        if (annotationsToLookFor.size() > 0) {  // Hibernate doesn't pass any annotations currently
            resultClasses = getClassesInJar(jartoScan, annotationsToLookFor);
        } else {
            PersistenceUnitMetadata pu = persistenceUnitMetadataTLS.get();
            if (pu == null) {
                throw new RuntimeException("Missing PersistenceUnitMetadataImpl (thread local wasn't set)");
            }
            Index index = getJarFileIndex(jartoScan, pu);
            if (index == null) {
                throw new RuntimeException("Missing annotation index to scan entity classes");
            }
            if (jartoScan == null) {
                throw new IllegalArgumentException("Null jar to scan url");
            }
            Collection<ClassInfo> allClasses = index.getKnownClasses();
            for (ClassInfo classInfo : allClasses) {
                String className = classInfo.name().toString();
                try {
                    resultClasses.add(pu.getClassLoader().loadClass(className));
                    // TODO:  fix temp classloader (get CFNE on entity class)
                    //result.add(pu.getNewTempClassLoader().loadClass(className));
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException("could not load entity class '" +
                        className + "' with PersistenceUnitInfo.getNewTempClassLoader()", e);
                }
            }
        }

        Map<String, Package> uniquePackages = new HashMap<String, Package>();
        for (Class classWithAnnotation : resultClasses) {
            Package classPackage = classWithAnnotation.getPackage();
            if (classPackage != null) {
                log.tracef("getPackagesInJar found package %s", classPackage);
                uniquePackages.put(classPackage.getName(), classPackage);
            }
        }
        return new HashSet<Package>(uniquePackages.values());
    }

    private Index getJarFileIndex(final URL jartoScan, final PersistenceUnitMetadata pu) {
        return pu.getAnnotationIndex().get(jartoScan);
    }

    @Override
    public Set<Class<?>> getClassesInJar(URL jartoScan, Set<Class<? extends Annotation>> annotationsToLookFor) {
        log.tracef("getClassesInJar url=%s annotations=%s", jartoScan.getPath(), annotationsToLookFor);
        PersistenceUnitMetadata pu = persistenceUnitMetadataTLS.get();
        if (pu == null) {
            throw new RuntimeException("Missing PersistenceUnitMetadataImpl (thread local wasn't set)");
        }
        Index index = getJarFileIndex(jartoScan, pu);
        if (index == null) {
            throw new RuntimeException("Missing annotation index to scan entity classes");
        }
        if (jartoScan == null) {
            throw new IllegalArgumentException("Null jar to scan url");
        }
        if (annotationsToLookFor == null) {
            throw new IllegalArgumentException("Null annotations to look for");
        }
        if (annotationsToLookFor.size() == 0) {
            throw new IllegalArgumentException("Zero annotations to look for");
        }

        Set<Class<?>> result = new HashSet<Class<?>>();

        for (Class<? extends Annotation> annClass : annotationsToLookFor) {
            DotName annotation = DotName.createSimple(annClass.getName());
            List<AnnotationInstance> classesWithAnnotation = index.getAnnotations(annotation);
            for (AnnotationInstance annotationInstance : classesWithAnnotation) {
                String className = annotationInstance.target().toString();
                try {
                    log.tracef("getClassesInJar found class %s with annotation %s", className, annClass.getName());
                    result.add(pu.getClassLoader().loadClass(className));
                    // TODO:  fix temp classloader (get CFNE on entity class)
                    //result.add(pu.getNewTempClassLoader().loadClass(className));
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException("could not load entity class '" +
                        className + "' with PersistenceUnitInfo.getNewTempClassLoader()", e);
                }
            }
        }
        return result;
    }

    @Override
    public Set<NamedInputStream> getFilesInJar(URL jartoScan, Set<String> filePatterns) {
        if (jartoScan == null)
            throw new IllegalArgumentException("Null jar to scan url");
        if (filePatterns == null)
            throw new IllegalArgumentException("Null file patterns to look for");

        Set<NamedInputStream> result = new HashSet<NamedInputStream>();
        Map<String, Set<NamedInputStream>> map;
        map = new HashMap<String, Set<NamedInputStream>>();
        findFiles(jartoScan, filePatterns, map, result);
        return result;
    }

    private void findFiles(URL jartoScan, Set<String> filePatterns, Map<String, Set<NamedInputStream>> map, Set<NamedInputStream> result) {
        if (filePatterns.isEmpty()) {
            for (Set<NamedInputStream> nims : map.values())
                result.addAll(nims);
        } else {
            VirtualFile root = null;
            for (String pattern : filePatterns) {
                Set<NamedInputStream> niss = map.get(pattern);
                if (niss == null) {
                    if (root == null)
                        root = getFile(jartoScan);

                    try {
                        List<VirtualFile> children = root.getChildrenRecursively(new HibernatePatternFilter(pattern));
                        niss = toNIS(children);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (niss != null)
                    result.addAll(niss);
            }
        }
    }

    private Set<NamedInputStream> toNIS(Iterable<VirtualFile> files) {
        Set<NamedInputStream> result = new HashSet<NamedInputStream>();
        for (VirtualFile file : files) {
            NamedInputStream nis = new HibernateVirtualFileNamedInputStream(file);
            result.add(nis);
        }
        return result;
    }

    @Override
    public Set<NamedInputStream> getFilesInClasspath(Set<String> filePatterns) {
        throw new RuntimeException("Not yet implemented");  // not currently called
    }

    @Override
    public String getUnqualifiedJarName(URL jarUrl) {
        VirtualFile file = getFile(jarUrl);
        return file.getName();
    }

    private VirtualFile getFile(URL url) {
        try {
            return VFS.getChild(url.toURI());
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4796.java
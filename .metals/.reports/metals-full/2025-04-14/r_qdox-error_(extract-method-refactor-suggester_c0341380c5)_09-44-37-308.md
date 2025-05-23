error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8243.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8243.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[24,1]

error in qdox parser
file content:
```java
offset: 1052
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8243.java
text:
```scala
public final class FrameworkUtils {

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
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
p@@ackage org.jboss.as.test.osgi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Assert;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.FrameworkListener;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.VersionRange;
import org.osgi.framework.startlevel.FrameworkStartLevel;
import org.osgi.framework.wiring.FrameworkWiring;
import org.osgi.util.tracker.ServiceTracker;


/**
 * OSGi integration test support.
 *
 * @author thomas.diesler@jboss.com
 * @since 24-May-2011
 */
public class FrameworkUtils {

    // Hide ctor
    private FrameworkUtils() {
    }

    public static Bundle[] getBundles(BundleContext context, String symbolicName, VersionRange versionRange) {
        List<Bundle> result = new ArrayList<Bundle>();
        if (Constants.SYSTEM_BUNDLE_SYMBOLICNAME.equals(symbolicName) && versionRange == null) {
            result.add(context.getBundle(0));
        } else {
            for (Bundle aux : context.getBundles()) {
                if (symbolicName == null || symbolicName.equals(aux.getSymbolicName())) {
                    if (versionRange == null || versionRange.includes(aux.getVersion())) {
                        result.add(aux);
                    }
                }
            }
        }
        return !result.isEmpty() ? result.toArray(new Bundle[result.size()]) : null;
    }

    public static int getFrameworkStartLevel(BundleContext context)  {
        return context.getBundle().adapt(FrameworkStartLevel.class).getStartLevel();
    }

    public static void setFrameworkStartLevel(BundleContext context, int level) throws InterruptedException, TimeoutException {
        setFrameworkStartLevel(context, level, 10, TimeUnit.SECONDS);
    }

    public static void setFrameworkStartLevel(BundleContext context, final int level, long timeout, TimeUnit units) throws InterruptedException, TimeoutException {
        final FrameworkStartLevel startLevel = context.getBundle().adapt(FrameworkStartLevel.class);
        if (level != startLevel.getStartLevel()) {
            final CountDownLatch latch = new CountDownLatch(1);
            FrameworkListener listener = new FrameworkListener() {
                public void frameworkEvent(FrameworkEvent event) {
                    if (event.getType() == FrameworkEvent.STARTLEVEL_CHANGED && level == startLevel.getStartLevel()) {
                        latch.countDown();
                    }
                }
            };
            startLevel.setStartLevel(level, listener);
            if (latch.await(timeout, units) == false)
                throw new TimeoutException("Timeout changing start level");
        }
    }

    public static void refreshBundles(BundleContext context, Collection<Bundle> bundles, long timeout, TimeUnit units) throws InterruptedException, TimeoutException {
        final CountDownLatch latch = new CountDownLatch(1);
        FrameworkListener listener = new FrameworkListener() {
            @Override
            public void frameworkEvent(FrameworkEvent event) {
                if (event.getType() == FrameworkEvent.PACKAGES_REFRESHED) {
                    latch.countDown();
                }
            }
        };
        FrameworkWiring fwrkWiring = context.getBundle().adapt(FrameworkWiring.class);
        fwrkWiring.refreshBundles(bundles, listener);
        latch.await(10, TimeUnit.SECONDS);
    }

    public static <T> T waitForService(BundleContext context, Class<T> clazz) {
        return waitForService(context, clazz, 10, TimeUnit.SECONDS);
    }

    public static <T> T waitForService(BundleContext context, Class<T> clazz, long timeout, TimeUnit unit) {
        ServiceReference<T> sref = waitForServiceReference(context, clazz, timeout, unit);
        T service = sref != null ? context.getService(sref) : null;
        Assert.assertNotNull("Service registered: " + clazz.getName(), service);
        return service;
    }

    public static <T> ServiceReference<T> waitForServiceReference(BundleContext context, Class<T> clazz) {
        return waitForServiceReference(context, clazz, 10, TimeUnit.SECONDS);
    }

    public static <T> ServiceReference<T> waitForServiceReference(BundleContext context, Class<T> clazz, long timeout, TimeUnit unit) {
        ServiceTracker tracker = new ServiceTracker(context, clazz.getName(), null);
        tracker.open();

        ServiceReference<T> sref = null;
        try {
            if (tracker.waitForService(unit.toMillis(timeout)) != null) {
                sref = context.getServiceReference(clazz);
            }
        } catch (InterruptedException e) {
            // service will be null
        } finally {
            tracker.close();
        }

        Assert.assertNotNull("Service registered: " + clazz.getName(), sref);
        return sref;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8243.java
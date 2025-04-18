error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7419.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7419.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7419.java
text:
```scala
l@@ist.add(new ServerModelDeploymentStartUpdate(element.getUniqueName()));

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

package org.jboss.as.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.as.model.socket.InterfaceAdd;
import org.jboss.as.model.socket.InterfaceElement;
import org.jboss.as.model.socket.SocketBindingAdd;
import org.jboss.as.model.socket.SocketBindingElement;
import org.jboss.as.model.socket.SocketBindingGroupElement;
import org.jboss.as.model.socket.SocketBindingGroupUpdate;
import org.jboss.as.server.deployment.ServerModelDeploymentAdd;
import org.jboss.as.server.deployment.ServerModelDeploymentStartUpdate;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public final class ServerFactory {

    private ServerFactory() {
    }

    /**
     * Combine a domain model and a host model to generate a list of bootstrap updates for a server to run.
     *
     * @param domainModel the domain model
     * @param hostModel the host model
     * @param serverName the name of the server to bootstrap
     * @param list the list to which the updates should be appended
     */
    @SuppressWarnings({ "RawUseOfParameterizedType" })
    public static void combine(DomainModel domainModel, HostModel hostModel, String serverName, List<AbstractServerModelUpdate<?>> list) {
        // Validate the model
        final ServerElement serverElement = hostModel.getServer(serverName);
        if (serverElement == null) {
            throw new IllegalArgumentException("Host model does not contain a server named '" + serverName + "'");
        }
        final String serverGroupName = serverElement.getServerGroup();
        final ServerGroupElement serverGroup = domainModel.getServerGroup(serverGroupName);
        if (serverGroup == null) {
            throw new IllegalArgumentException("Domain model does not contain a server group named '" + serverGroupName + "'");
        }
        final String profileName = serverGroup.getProfileName();
        final ProfileElement leafProfile = domainModel.getProfile(profileName);
        if (profileName == null) {
            throw new IllegalArgumentException("Domain model does not contain a profile named '" + profileName + "'");
        }

        list.add(new ServerNameUpdate(serverName));

        // Merge extensions
        final Set<String> extensionNames = new LinkedHashSet<String>();
        for (String name : domainModel.getExtensions()) {
            extensionNames.add(name);
        }
        for (String name : hostModel.getExtensions()) {
            extensionNames.add(name);
        }
        for (String name : extensionNames) {
            list.add(new ServerExtensionAdd(name));
        }

        // Merge paths
        final Set<String> unspecifiedPaths = new HashSet<String>();
        final Map<String, ServerPathAdd> paths = new HashMap<String, ServerPathAdd>();
        for(final PathElement path : domainModel.getPaths()) {
            if(! path.isSpecified()) {
                unspecifiedPaths.add(path.getName());
            } else {
                paths.put(path.getName(), new ServerPathAdd(path));
            }
        }
        for(final PathElement path : hostModel.getPaths()) {
            unspecifiedPaths.remove(path.getName());
            paths.put(path.getName(), new ServerPathAdd(path));
        }
        for(final PathElement path : serverElement.getPaths()) {
            unspecifiedPaths.remove(path.getName());
            paths.put(path.getName(), new ServerPathAdd(path));
        }
        if(unspecifiedPaths.size() > 0) {
            throw new IllegalStateException("unspecified paths " + unspecifiedPaths);
        }

        // Merge interfaces
        // TODO: modify to merge each interface instead of replacing duplicates
        Set<String> unspecifiedInterfaces = new HashSet<String>();
        Map<String, InterfaceElement> interfaces = new HashMap<String, InterfaceElement>();
        for (InterfaceElement ie : domainModel.getInterfaces()) {
            if (ie.isFullySpecified()) {
                interfaces.put(ie.getName(), ie);
            } else {
                unspecifiedInterfaces.add(ie.getName());
            }
        }
        for (InterfaceElement ie : hostModel.getInterfaces()) {
            interfaces.put(ie.getName(), ie);
            unspecifiedInterfaces.remove(ie.getName());
        }
        for (InterfaceElement ie : serverElement.getInterfaces()) {
            interfaces.put(ie.getName(), ie);
            unspecifiedInterfaces.remove(ie.getName());
        }
        // TODO: verify that all required interfaces were specified

        for (InterfaceElement interfaceElement : interfaces.values()) {
            list.add(new ServerModelInterfaceAdd(new InterfaceAdd(interfaceElement)));
        }

        // Merge socket bindings
        String bindingRef = serverElement.getSocketBindingGroupName();
        int portOffset = serverElement.getSocketBindingPortOffset();
        if (bindingRef == null) {
            bindingRef = serverGroup.getSocketBindingGroupName();
            portOffset = serverGroup.getSocketBindingPortOffset();
        }
        list.add(new ServerPortOffsetUpdate(portOffset));

        // TODO: add check for duplicate socket bindings
        SocketBindingGroupElement domainBindings = domainModel.getSocketBindingGroup(bindingRef);
        if (domainBindings == null) {
            domainBindings = new SocketBindingGroupElement("domainBindings");
        }
        list.add(new ServerSocketBindingGroupUpdate(new SocketBindingGroupUpdate(domainBindings.getName(), domainBindings.getDefaultInterface(), Collections.<String>emptySet())));
        processSocketBindings(domainBindings, list);
        for(final String socketInclude : domainBindings.getIncludedSocketBindingGroups()) {
            final SocketBindingGroupElement include = domainModel.getSocketBindingGroup(socketInclude);
            if(include == null) {
                throw new IllegalStateException("failed to resolve binding-group " + socketInclude);
            }
            processSocketBindings(include, list);
        }

        list.add(new ServerProfileUpdate(serverGroup.getProfileName()));

        // Merge subsystems from leafProfile and any parent profiles
        Set<String> processedSubsystems = new HashSet<String>();
        Set<String> processedProfiles = new HashSet<String>();
        processProfile(domainModel, leafProfile, list, processedProfiles, processedSubsystems);

        // Merge deployments
        for (ServerGroupDeploymentElement element : serverGroup.getDeployments()) {
            final ServerModelDeploymentAdd add = new ServerModelDeploymentAdd(element.getUniqueName(), element.getRuntimeName(), element.getSha1Hash());
            list.add(add);
            if (element.isStart()) {
                list.add(new ServerModelDeploymentStartUpdate(element.getUniqueName(), element.getRuntimeName(), element.getSha1Hash()));
            }
        }

        // Merge system properties
        // todo after PropertiesElement exposes flags as to whether individual properties
        // are passed to java.lang.Process or go through the model

        // TODO add domain deployment repository
        // BES 2010/10/12 -- why?

    }

    private static void processProfile(DomainModel domainModel, ProfileElement profile,
            List<AbstractServerModelUpdate<?>> list, Set<String> processedProfiles,
            Set<String> processedSubsystems) {

        if (! processedProfiles.add(profile.getName())) {
            // we already hit this one via another path
            return;
        }

        // Parent profile subsystems first
        for (String included : profile.getIncludedProfiles()) {
            processProfile(domainModel, domainModel.getProfile(included), list, processedProfiles, processedSubsystems);
        }

        for (AbstractSubsystemElement<? extends AbstractSubsystemElement<?>> subsystemElement : profile.getSubsystems()) {
            String namespaceURI = subsystemElement.getElementName().getNamespaceURI();
            if (processedSubsystems.contains(namespaceURI)) {
                // FIXME catch this problem in domain model parsing
                throw findDuplicateProfile(namespaceURI, profile.getName(), processedProfiles, domainModel);
            }
            // todo: find a better way around this generics issue
            processSubsystem((AbstractSubsystemElement) subsystemElement, list);
            processedSubsystems.add(namespaceURI);
        }
    }

    private static RuntimeException findDuplicateProfile(String namespaceURI, String currentProfile, Set<String> processedProfiles,
            DomainModel domainModel) {
        String duplicate = null;
        for (String profile : processedProfiles) {
            if (profile.equals(currentProfile)) {
                continue;
            }
            ProfileElement pe = domainModel.getProfile(profile);
            if (pe.getSubsystem(namespaceURI) != null) {
                duplicate = profile;
                break;
            }
        }
        return new IllegalStateException(String.format("Subsystem with namespace %s " +
                "is declared in multiple profiles that are related to each " +
                "other via inclusion. Profiles are %s and %s. A subsystem can " +
                "only appear once in a given server runtime, so declaring the " +
                "same subsystem in multiple related profiles is illegal.", namespaceURI, duplicate, currentProfile));
    }

    private static void processSocketBindings(final SocketBindingGroupElement group, List<AbstractServerModelUpdate<?>> list) {
        for(final SocketBindingElement binding : group.getSocketBindings()) {
            final SocketBindingAdd update = new SocketBindingAdd(binding);
            list.add(new ServerSocketBindingUpdate(update));
        }

    }

    private static <E extends AbstractSubsystemElement<E>> void processSubsystem(E subsystemElement, List<AbstractServerModelUpdate<?>> list) {
        final AbstractSubsystemAdd<E> subsystemAdd = subsystemElement.getAdd();
        if (subsystemAdd == null) {
            throw new IllegalStateException(subsystemElement + " did not provide an " + AbstractSubsystemAdd.class.getSimpleName());
        }
        list.add(new ServerSubsystemAdd(subsystemAdd));
        final List<AbstractSubsystemUpdate<E, ?>> subsystemList = new ArrayList<AbstractSubsystemUpdate<E,?>>();
        subsystemElement.getUpdates(subsystemList);
        for (AbstractSubsystemUpdate<E, ?> update : subsystemList) {
            list.add(ServerSubsystemUpdate.create(update));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7419.java
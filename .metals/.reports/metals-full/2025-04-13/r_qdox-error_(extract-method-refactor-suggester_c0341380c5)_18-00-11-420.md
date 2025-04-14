error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2652.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2652.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2652.java
text:
```scala
l@@ogger.trace("Failed to find local host", e);

/*
 * Licensed to Elastic Search and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Elastic Search licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.common.network;

import org.elasticsearch.ElasticSearchIllegalStateException;
import org.elasticsearch.common.collect.Lists;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.os.OsUtils;

import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author kimchy (shay.banon)
 */
public abstract class NetworkUtils {

    private final static ESLogger logger = Loggers.getLogger(NetworkUtils.class);

    public static enum StackType {
        IPv4, IPv6, Unknown
    }

    public static final String IPv4_SETTING = "java.net.preferIPv4Stack";
    public static final String IPv6_SETTING = "java.net.preferIPv6Addresses";

    public static final String NON_LOOPBACK_ADDRESS = "non_loopback_address";

    private final static InetAddress localAddress;

    static {
        InetAddress localAddressX = null;
        try {
            localAddressX = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            logger.warn("Failed to find local host", e);
        }
        localAddress = localAddressX;
    }

    public static Boolean defaultReuseAddress() {
        return OsUtils.WINDOWS ? null : true;
    }

    public static boolean isIPv4() {
        return System.getProperty("java.net.preferIPv4Stack") != null && System.getProperty("java.net.preferIPv4Stack").equals("true");
    }

    public static InetAddress getIPv4Localhost() throws UnknownHostException {
        return getLocalhost(StackType.IPv4);
    }

    public static InetAddress getIPv6Localhost() throws UnknownHostException {
        return getLocalhost(StackType.IPv6);
    }

    public static InetAddress getLocalAddress() {
        return localAddress;
    }

    public static InetAddress getLocalhost(StackType ip_version) throws UnknownHostException {
        if (ip_version == StackType.IPv4)
            return InetAddress.getByName("127.0.0.1");
        else
            return InetAddress.getByName("::1");
    }

    public static boolean canBindToMcastAddress() {
        return OsUtils.LINUX || OsUtils.SOLARIS || OsUtils.HP;
    }


    /**
     * Returns the first non-loopback address on any interface on the current host.
     *
     * @param ip_version Constraint on IP version of address to be returned, 4 or 6
     */
    public static InetAddress getFirstNonLoopbackAddress(StackType ip_version) throws SocketException {
        InetAddress address = null;

        Enumeration intfs = NetworkInterface.getNetworkInterfaces();

        List<NetworkInterface> intfsList = Lists.newArrayList();
        while (intfs.hasMoreElements()) {
            intfsList.add((NetworkInterface) intfs.nextElement());
        }

        // order by index, assuming first ones are more interesting
        try {
            final Method getIndexMethod = NetworkInterface.class.getDeclaredMethod("getIndex");
            getIndexMethod.setAccessible(true);

            Collections.sort(intfsList, new Comparator<NetworkInterface>() {
                @Override public int compare(NetworkInterface o1, NetworkInterface o2) {
                    try {
                        return ((Integer) getIndexMethod.invoke(o1)).intValue() - ((Integer) getIndexMethod.invoke(o2)).intValue();
                    } catch (Exception e) {
                        throw new ElasticSearchIllegalStateException("failed to fetch index of network interface");
                    }
                }
            });
        } catch (Exception e) {
            // ignore
        }

        for (NetworkInterface intf : intfsList) {
            try {
                if (!intf.isUp() || intf.isLoopback())
                    continue;
            } catch (Exception e) {
                // might happen when calling on a network interface that does not exists
                continue;
            }
            address = getFirstNonLoopbackAddress(intf, ip_version);
            if (address != null) {
                return address;
            }
        }

        return null;
    }


    /**
     * Returns the first non-loopback address on the given interface on the current host.
     *
     * @param intf      the interface to be checked
     * @param ipVersion Constraint on IP version of address to be returned, 4 or 6
     */
    public static InetAddress getFirstNonLoopbackAddress(NetworkInterface intf, StackType ipVersion) throws SocketException {
        if (intf == null)
            throw new IllegalArgumentException("Network interface pointer is null");

        for (Enumeration addresses = intf.getInetAddresses(); addresses.hasMoreElements(); ) {
            InetAddress address = (InetAddress) addresses.nextElement();
            if (!address.isLoopbackAddress()) {
                if ((address instanceof Inet4Address && ipVersion == StackType.IPv4) ||
                        (address instanceof Inet6Address && ipVersion == StackType.IPv6))
                    return address;
            }
        }
        return null;
    }

    /**
     * A function to check if an interface supports an IP version (i.e has addresses
     * defined for that IP version).
     *
     * @param intf
     * @return
     */
    public static boolean interfaceHasIPAddresses(NetworkInterface intf, StackType ipVersion) throws SocketException, UnknownHostException {
        boolean supportsVersion = false;
        if (intf != null) {
            // get all the InetAddresses defined on the interface
            Enumeration addresses = intf.getInetAddresses();
            while (addresses != null && addresses.hasMoreElements()) {
                // get the next InetAddress for the current interface
                InetAddress address = (InetAddress) addresses.nextElement();

                // check if we find an address of correct version
                if ((address instanceof Inet4Address && (ipVersion == StackType.IPv4)) ||
                        (address instanceof Inet6Address && (ipVersion == StackType.IPv6))) {
                    supportsVersion = true;
                    break;
                }
            }
        } else {
            throw new UnknownHostException("network interface not found");
        }
        return supportsVersion;
    }

    /**
     * Tries to determine the type of IP stack from the available interfaces and their addresses and from the
     * system properties (java.net.preferIPv4Stack and java.net.preferIPv6Addresses)
     *
     * @return StackType.IPv4 for an IPv4 only stack, StackYTypeIPv6 for an IPv6 only stack, and StackType.Unknown
     *         if the type cannot be detected
     */
    public static StackType getIpStackType() {
        boolean isIPv4StackAvailable = isStackAvailable(true);
        boolean isIPv6StackAvailable = isStackAvailable(false);

        // if only IPv4 stack available
        if (isIPv4StackAvailable && !isIPv6StackAvailable) {
            return StackType.IPv4;
        }
        // if only IPv6 stack available
        else if (isIPv6StackAvailable && !isIPv4StackAvailable) {
            return StackType.IPv6;
        }
        // if dual stack
        else if (isIPv4StackAvailable && isIPv6StackAvailable) {
            // get the System property which records user preference for a stack on a dual stack machine
            if (Boolean.getBoolean(IPv4_SETTING)) // has preference over java.net.preferIPv6Addresses
                return StackType.IPv4;
            if (Boolean.getBoolean(IPv6_SETTING))
                return StackType.IPv6;
            return StackType.IPv6;
        }
        return StackType.Unknown;
    }


    public static boolean isStackAvailable(boolean ipv4) {
        Collection<InetAddress> allAddrs = getAllAvailableAddresses();
        for (InetAddress addr : allAddrs)
            if (ipv4 && addr instanceof Inet4Address || (!ipv4 && addr instanceof Inet6Address))
                return true;
        return false;
    }


    public static List<NetworkInterface> getAllAvailableInterfaces() throws SocketException {
        List<NetworkInterface> allInterfaces = new ArrayList<NetworkInterface>(10);
        NetworkInterface intf;
        for (Enumeration en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
            intf = (NetworkInterface) en.nextElement();
            allInterfaces.add(intf);
        }
        return allInterfaces;
    }

    public static Collection<InetAddress> getAllAvailableAddresses() {
        Set<InetAddress> retval = new HashSet<InetAddress>();
        Enumeration en;

        try {
            en = NetworkInterface.getNetworkInterfaces();
            if (en == null)
                return retval;
            while (en.hasMoreElements()) {
                NetworkInterface intf = (NetworkInterface) en.nextElement();
                Enumeration<InetAddress> addrs = intf.getInetAddresses();
                while (addrs.hasMoreElements())
                    retval.add(addrs.nextElement());
            }
        } catch (SocketException e) {
            logger.warn("Failed to derive all available interfaces", e);
        }

        return retval;
    }


    private NetworkUtils() {

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2652.java
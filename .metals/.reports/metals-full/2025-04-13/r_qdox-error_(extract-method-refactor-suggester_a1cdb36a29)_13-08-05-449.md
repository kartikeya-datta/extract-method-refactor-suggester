error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2775.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2775.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2775.java
text:
```scala
m@@bean.addInformationFromZip(provZip.toURL().toExternalForm());

/**
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.aries.jmx.provisioning;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.osgi.service.provisioning.ProvisioningService.PROVISIONING_AGENT_CONFIG;
import static org.osgi.service.provisioning.ProvisioningService.PROVISIONING_REFERENCE;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Dictionary;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;

import javax.management.InstanceNotFoundException;
import javax.management.ObjectName;
import javax.management.openmbean.TabularData;

import org.apache.aries.jmx.AbstractIntegrationTest;
import org.apache.aries.jmx.codec.PropertyData;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.ops4j.pax.exam.CoreOptions;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.osgi.jmx.JmxConstants;
import org.osgi.jmx.service.provisioning.ProvisioningServiceMBean;
import org.osgi.service.provisioning.ProvisioningService;
import org.osgi.util.tracker.ServiceTracker;

/**
 * 
 * 
 * @version $Rev$ $Date$
 */
public class ProvisioningServiceMBeanTest extends AbstractIntegrationTest {


    @Configuration
    public static Option[] configuration() {
        Option[] options = CoreOptions
                .options(
                        CoreOptions.equinox(),
                        //provision("file:///<local>"),
                        mavenBundle("org.ops4j.pax.logging", "pax-logging-api"), 
                        mavenBundle("org.ops4j.pax.logging", "pax-logging-service"), 
                        mavenBundle("org.osgi", "org.osgi.compendium"), 
                        mavenBundle("org.apache.aries.jmx", "org.apache.aries.jmx"));
        options = updateOptions(options);
        return options;
    }

    @Before
    public void doSetUp() throws Exception {
        super.setUp();
        int i=0;
        while (true) {
            try {
                mbeanServer.getObjectInstance(new ObjectName(ProvisioningServiceMBean.OBJECTNAME));
                break;
            } catch (InstanceNotFoundException e) {
                if (i == 5) {
                    throw new Exception("ProvisioningServiceMBean not available after waiting 5 seconds");
                }
            }
            i++;
            Thread.sleep(1000);
        }
       
      
    }
    
  

    @Ignore("For now.. Cannot find public repo for org.eclipse.equinox.ip")
    @Test
    @SuppressWarnings("unchecked")
    public void testMBeanInterface() throws Exception {

        ProvisioningServiceMBean mbean = getMBean(ProvisioningServiceMBean.OBJECTNAME, ProvisioningServiceMBean.class);
        assertNotNull(mbean);
        
        ServiceTracker tracker = new ServiceTracker(bundleContext, ProvisioningService.class.getName(), null);
        tracker.open();
        ProvisioningService ps = (ProvisioningService) tracker.getService();
        assertNotNull(ps);
        
        Dictionary<String, Object> info;
        
        // add information URL (create temp zip file)
        
        File  provZip = File.createTempFile("Prov-jmx-itests", ".zip");
        Manifest man = new Manifest();
        man.getMainAttributes().putValue("Manifest-Version", "1.0");
        man.getMainAttributes().putValue("Content-Type", "application/zip");
        JarOutputStream jout = new JarOutputStream(new FileOutputStream(provZip), man);
        ZipEntry entry = new ZipEntry(PROVISIONING_AGENT_CONFIG);
        jout.putNextEntry( entry );
        jout.write(new byte[] { 10, 20, 30 });
        jout.closeEntry();
        jout.flush();
        jout.close();
        
        provZip.deleteOnExit();
        
        mbean.addInformation(provZip.toURL().toExternalForm());
        
        //check the info has been added
        
        info = ps.getInformation();
        assertNotNull(info);
        assertTrue(info.size() >= 1);
        byte[] config = (byte[]) info.get(PROVISIONING_AGENT_CONFIG);
        assertNotNull(config);
        assertArrayEquals(new byte[] { 10, 20, 30 }, config);
        
        
        // test list information
        
        TabularData data = mbean.listInformation();
        assertNotNull(data);
        assertEquals(JmxConstants.PROPERTIES_TYPE, data.getTabularType());
        assertTrue(data.values().size() >= 1);
        PropertyData<byte[]> configEntry = PropertyData.from(data.get(new Object[] {PROVISIONING_AGENT_CONFIG }));
        assertNotNull(configEntry);
        assertArrayEquals(new byte[] { 10, 20, 30 }, configEntry.getValue());

        
        // test add information
        
        PropertyData<String> reference = PropertyData.newInstance(PROVISIONING_REFERENCE, "rsh://0.0.0.0/provX");
        data.put(reference.toCompositeData());
        
        mbean.addInformation(data);
        
        info = ps.getInformation();
        assertNotNull(info);
        assertTrue(info.size() >= 2);
        config = (byte[]) info.get(PROVISIONING_AGENT_CONFIG);
        assertNotNull(config);
        assertArrayEquals(new byte[] { 10, 20, 30 }, config);
        String ref = (String) info.get(PROVISIONING_REFERENCE);
        assertNotNull(ref);
        assertEquals("rsh://0.0.0.0/provX", ref);
        
        
        // test set information
        
        data.clear();
        PropertyData<String> newRef = PropertyData.newInstance(PROVISIONING_REFERENCE, "rsh://0.0.0.0/newProvRef");
        data.put(newRef.toCompositeData());
        
        mbean.setInformation(data);
        info = ps.getInformation();
        assertNotNull(info);
        assertTrue(info.size() >= 1);
        assertNull(info.get(PROVISIONING_AGENT_CONFIG));
       
        ref = (String) info.get(PROVISIONING_REFERENCE);
        assertNotNull(ref);
        assertEquals("rsh://0.0.0.0/newProvRef", ref);
        
        
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2775.java
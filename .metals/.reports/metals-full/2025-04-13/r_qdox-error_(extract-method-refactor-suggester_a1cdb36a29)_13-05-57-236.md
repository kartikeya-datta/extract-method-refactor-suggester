error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3476.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3476.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3476.java
text:
```scala
public static v@@oid createKeytab(final String principalName, final String passPhrase, final File keytabFile) throws IOException {

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
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
package org.jboss.as.test.integration.security.loginmodules.negotiation;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.directory.server.kerberos.shared.crypto.encryption.KerberosKeyFactory;
import org.apache.directory.server.kerberos.shared.keytab.Keytab;
import org.apache.directory.shared.kerberos.KerberosTime;
import org.apache.directory.shared.kerberos.codec.types.EncryptionType;
import org.apache.directory.shared.kerberos.components.EncryptionKey;
import org.apache.log4j.Logger;
import org.jboss.as.arquillian.api.ServerSetupTask;
import org.jboss.as.arquillian.container.ManagementClient;
import org.jboss.as.network.NetworkUtils;
import org.jboss.as.test.integration.security.common.Utils;

/**
 * This server setup task creates a krb5.conf file and generates KeyTab files for the HTTP server and users hnelson and jduke.
 * The task also sets system properties
 * <ul>
 * <li>"java.security.krb5.conf" - path to the newly created krb5.conf is set</li>
 * <li>"sun.security.krb5.debug" - true is set (Kerberos debugging for Oracle Java)</li>
 * </ul>
 * 
 * @author Josef Cacek
 */
public class Krb5ConfServerSetupTask implements ServerSetupTask {
    private static Logger LOGGER = Logger.getLogger(Krb5ConfServerSetupTask.class);

    private static final File WORK_DIR = new File("SPNEGO-workdir");
    private static final String KRB5_CONF = "krb5.conf";
    private static final File KRB5_CONF_FILE = new File(WORK_DIR, KRB5_CONF);

    public static final File HTTP_KEYTAB_FILE = new File(WORK_DIR, "http.keytab");
    public static final File HNELSON_KEYTAB_FILE = new File(WORK_DIR, "hnelson.keytab");
    public static final File JDUKE_KEYTAB_FILE = new File(WORK_DIR, "jduke.keytab");

    private String origKrb5Conf;
    private String origKrbDebug;

    // Public methods --------------------------------------------------------

    /**
     * 
     * @param managementClient
     * @param containerId
     * @throws Exception
     * @see org.jboss.as.arquillian.api.ServerSetupTask#setup(org.jboss.as.arquillian.container.ManagementClient,
     *      java.lang.String)
     */
    public void setup(ManagementClient managementClient, String containerId) throws Exception {
        LOGGER.info("(Re)Creating workdir: " + WORK_DIR.getAbsolutePath());
        FileUtils.deleteDirectory(WORK_DIR);
        WORK_DIR.mkdirs();
        final String cannonicalHost = NetworkUtils.formatPossibleIpv6Address(Utils.getCannonicalHost(managementClient));
        final Map<String, String> map = new HashMap<String, String>();
        map.put("hostname", cannonicalHost);
        FileUtils.write(KRB5_CONF_FILE,
                StrSubstitutor.replace(IOUtils.toString(getClass().getResourceAsStream(KRB5_CONF), "UTF-8"), map), "UTF-8");

        createKeytab("HTTP/" + cannonicalHost + "@JBOSS.ORG", "httppwd", HTTP_KEYTAB_FILE);
        createKeytab("hnelson@JBOSS.ORG", "secret", HNELSON_KEYTAB_FILE);
        createKeytab("jduke@JBOSS.ORG", "theduke", JDUKE_KEYTAB_FILE);

        LOGGER.info("Setting Kerberos configuration: " + KRB5_CONF_FILE);
        origKrb5Conf = Utils.setSystemProperty("java.security.krb5.conf", KRB5_CONF_FILE.getAbsolutePath());
        origKrbDebug = Utils.setSystemProperty("sun.security.krb5.debug", "true");

    }

    /**
     * Removes working directory with Kerberos related generated files.
     * 
     * @param managementClient
     * @param containerId
     * @throws Exception
     * @see org.jboss.as.arquillian.api.ServerSetupTask#tearDown(org.jboss.as.arquillian.container.ManagementClient,
     *      java.lang.String)
     */
    public void tearDown(ManagementClient managementClient, String containerId) throws Exception {
        FileUtils.deleteDirectory(WORK_DIR);
        Utils.setSystemProperty("java.security.krb5.conf", origKrb5Conf);
        Utils.setSystemProperty("sun.security.krb5.debug", origKrbDebug);
    }

    /**
     * Returns an absolute path to krb5.conf file.
     * 
     * @return
     */
    public static final String getKrb5ConfFullPath() {
        return KRB5_CONF_FILE.getAbsolutePath();
    }

    /**
     * Returns an absolute path to a keytab with JBoss AS credentials (HTTP/host@JBOSS.ORG).
     * 
     * @return
     */
    public static final String getKeyTabFullPath() {
        return HTTP_KEYTAB_FILE.getAbsolutePath();
    }

    // Private methods -------------------------------------------------------

    /**
     * Creates a keytab file for given principal.
     * 
     * @param principalName
     * @param passPhrase
     * @param keytabFile
     * @throws IOException
     */
    private void createKeytab(final String principalName, final String passPhrase, final File keytabFile) throws IOException {
        LOGGER.info("Principal name: " + principalName);
        final KerberosTime timeStamp = new KerberosTime();

        DataOutputStream dos = null;
        try {
            dos = new DataOutputStream(new FileOutputStream(keytabFile));
            dos.write(Keytab.VERSION_0X502_BYTES);

            for (Map.Entry<EncryptionType, EncryptionKey> keyEntry : KerberosKeyFactory.getKerberosKeys(principalName,
                    passPhrase).entrySet()) {
                final EncryptionKey key = keyEntry.getValue();
                final byte keyVersion = (byte) key.getKeyVersion();
                // entries.add(new KeytabEntry(principalName, principalType, timeStamp, keyVersion, key));

                final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                DataOutputStream entryDos = new DataOutputStream(baos);
                // handle principal name
                String[] spnSplit = principalName.split("@");
                String nameComponent = spnSplit[0];
                String realm = spnSplit[1];

                String[] nameComponents = nameComponent.split("/");
                try {
                    // increment for v1
                    entryDos.writeShort((short) nameComponents.length);
                    entryDos.writeUTF(realm);
                    // write components
                    for (String component : nameComponents) {
                        entryDos.writeUTF(component);
                    }

                    entryDos.writeInt(1); // principal type: KRB5_NT_PRINCIPAL
                    entryDos.writeInt((int) (timeStamp.getTime() / 1000));
                    entryDos.write(keyVersion);

                    entryDos.writeShort((short) key.getKeyType().getValue());

                    byte[] data = key.getKeyValue();
                    entryDos.writeShort((short) data.length);
                    entryDos.write(data);
                } finally {
                    IOUtils.closeQuietly(entryDos);
                }
                final byte[] entryBytes = baos.toByteArray();
                dos.writeInt(entryBytes.length);
                dos.write(entryBytes);
            }
            // } catch (IOException ioe) {
        } finally {
            IOUtils.closeQuietly(dos);
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3476.java
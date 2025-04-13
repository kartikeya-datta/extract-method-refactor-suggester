error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15696.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15696.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15696.java
text:
```scala
i@@f (index >= startIndex && index <= endIndex) {

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.jmeter.util.keystore;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;

import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * Use this Keystore for JMeter specific KeyStores.
 *
 */
public final class JmeterKeyStore {

    private static final Logger LOG = LoggingManager.getLoggerForClass();

    private final KeyStore store;
    private final int startIndex;
    private final int endIndex;

    private X509Certificate[][] certChains;
    private PrivateKey[] keys;
    private String[] names = new String[0]; // default empty array to prevent NPEs

    //@GuardedBy("this")
    private int last_user;

    private JmeterKeyStore(String type, int startIndex, int endIndex) throws Exception {
        if (startIndex < 0 || endIndex < 0 || endIndex < startIndex) {
            throw new IllegalArgumentException("Invalid index(es). Start="+startIndex+", end="+endIndex);
        }
        this.store = KeyStore.getInstance(type);
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    /**
     * Process the input stream
     */
    public void load(InputStream is, String pword) throws Exception {
        char pw[] = pword==null ? null : pword.toCharArray();
        store.load(is, pw);
    
        ArrayList<String> v_names = new ArrayList<String>();
        ArrayList<PrivateKey> v_keys = new ArrayList<PrivateKey>();
        ArrayList<X509Certificate[]> v_certChains = new ArrayList<X509Certificate[]>();
    
        if (null != is){ // No point checking an empty keystore
            PrivateKey _key = null;
            int index = 0;
            Enumeration<String> aliases = store.aliases();
            while (aliases.hasMoreElements()) {
                String alias = aliases.nextElement();
                if (store.isKeyEntry(alias)) {
                    if ((index >= startIndex && index <= endIndex)) {
                        _key = (PrivateKey) store.getKey(alias, pw);
                        if (null == _key) {
                            throw new Exception("No key found for alias: " + alias); // Should not happen
                        }
                        Certificate[] chain = store.getCertificateChain(alias);
                        if (null == chain) {
                            throw new Exception("No certificate chain found for alias: " + alias);
                        }
                        v_names.add(alias);
                        v_keys.add(_key);
                        X509Certificate[] x509certs = new X509Certificate[chain.length];
                        for (int i = 0; i < x509certs.length; i++) {
                            x509certs[i] = (X509Certificate)chain[i];
                        }
                        v_certChains.add(x509certs);
                    }
                }
                index++;
            }
    
            if (null == _key) {
                throw new Exception("No key(s) found");
            }
            if (index <= endIndex-startIndex) {
                LOG.warn("Did not find all requested aliases. Start="+startIndex+", end="+endIndex+", found="+index);
            }
        }
    
        /*
         * Note: if is == null, the arrays will be empty
         */
        int v_size = v_names.size();
    
        this.names = new String[v_size];
        this.names = v_names.toArray(names);
    
        this.keys = new PrivateKey[v_size];
        this.keys = v_keys.toArray(keys);
    
        this.certChains = new X509Certificate[v_size][];
        this.certChains = v_certChains.toArray(certChains);
    }


    /**
     * Get the ordered certificate chain for a specific alias.
     */
    public X509Certificate[] getCertificateChain(String alias) {
        int entry = findAlias(alias);
        if (entry >=0) {
            return this.certChains[entry];
        }
        return null;
    }

    /**
     * Get the next or only alias.
     * @return the next or only alias.
     */
    public String getAlias() {
        int length = this.names.length;
        if (length == 0) { // i.e. is == null
            return null;
        }
        return this.names[getIndexAndIncrement(length)];
    }

    public int getAliasCount() {
        return this.names.length;
    }

    public String getAlias(int index) {
        int length = this.names.length;
        if (length == 0 && index == 0) { // i.e. is == null
            return null;
        }
        if (index >= length || index < 0) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        return this.names[index];
    }

    /**
     * Return the private Key for a specific alias
     */
    public PrivateKey getPrivateKey(String alias) {
        int entry = findAlias(alias);
        if (entry >=0) {
            return this.keys[entry];
        }
        return null;
    }

    /**
     * Create a keystore which returns a range of aliases (if available)
     * @param type store type (e.g. JKS)
     * @param startIndex first index (from 0)
     * @param endIndex last index (to count -1)
     * @return the keystore
     * @throws Exception
     */
    public static JmeterKeyStore getInstance(String type, int startIndex, int endIndex) throws Exception {
        return new JmeterKeyStore(type, startIndex, endIndex);
    }

    /**
     * Create a keystore which returns the first alias only.
     * @param type e.g. JKS
     * @return the keystore
     * @throws Exception
     */
    public static JmeterKeyStore getInstance(String type) throws Exception {
        return new JmeterKeyStore(type, 0, 0);
    }
    
    private int findAlias(String alias) {
        for(int i = 0; i < names.length; i++) {
            if (alias.equals(names[i])){
                return i;
            }
        }
        return -1;
    }

    /**
     * Gets current index and increment by rolling if index is equal to length
     * @param length Number of keys to roll
     */
    private int getIndexAndIncrement(int length) {
        synchronized(this) {
            int result = last_user++;
            if (last_user >= length) {
                last_user = 0;
            }
            return result;
        }
    }

    /**
     * Compiles the list of all client aliases with a private key.
     * TODO Currently, keyType and issuers are both ignored.
     *
     * @param keyType the key algorithm type name (RSA, DSA, etc.)
     * @param issuers  the CA certificates we are narrowing our selection on.
     * 
     * @return the array of aliases; may be empty
     */
    public String[] getClientAliases(String keyType, Principal[] issuers) {
        int count = getAliasCount();
        String[] aliases = new String[count];
        for(int i = 0; i < aliases.length; i++) {
//            if (keys[i].getAlgorithm().equals(keyType)){
//                
//            }
            aliases[i] = this.names[i];
        }
        return aliases;
    }

}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15696.java
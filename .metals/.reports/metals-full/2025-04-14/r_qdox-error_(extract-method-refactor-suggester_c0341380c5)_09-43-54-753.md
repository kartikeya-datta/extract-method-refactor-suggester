error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13625.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13625.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13625.java
text:
```scala
F@@ile javaHome = SystemUtils.getJavaHome();

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

package org.apache.jorphan.exec;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.JavaVersion;
import org.apache.commons.lang3.SystemUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.util.JOrphanUtils;
import org.apache.log.Logger;

/**
 * Utilities for working with Java keytool
 */
public class KeyToolUtils {
    private static final Logger log = LoggingManager.getLoggerForClass();

    // The DNAME which is used if none is provided
    private static final String DEFAULT_DNAME = "cn=JMeter Proxy (DO NOT TRUST)";  // $NON-NLS-1$

    // N.B. It seems that Opera needs a chain in order to accept server keys signed by the intermediate CA
    // Opera does not seem to like server keys signed by the root (self-signed) cert.

    private static final String DNAME_ROOT_CA_KEY;

    private static final String KEYTOOL_DIRECTORY = "keytool.directory"; // $NON-NLS-1$

    /**
     * Where to find the keytool application.
     * If null, then keytool cannot be found.
     */
	private static final String KEYTOOL_PATH;

    private static void addElement(StringBuilder sb, String prefix, String value) {
        if (value != null) {
            sb.append(", ");
            sb.append(prefix);
            sb.append(value);
        }
    }

    static {
        StringBuilder sb = new StringBuilder();
        sb.append("CN=_ DO NOT INSTALL unless this is your certificate (JMeter root CA)"); // $NON-NLS-1$
        addElement(sb, "OU=Username: ", System.getProperty("user.name")); // $NON-NLS-1$ $NON-NLS-2$
        addElement(sb, "C=", System.getProperty("user.country")); // $NON-NLS-1$ $NON-NLS-2$
        DNAME_ROOT_CA_KEY = sb.toString();

        // Try to find keytool application
        // N.B. Cannot use JMeter property from jorphan jar.
        final String keytoolDir = System.getProperty(KEYTOOL_DIRECTORY);

        String keytoolPath; // work field
        if (keytoolDir != null) {
        	keytoolPath = new File(new File(keytoolDir),"keytool").getPath(); // $NON-NLS-1$
			if (!checkKeytool(keytoolPath)) { // $NON-NLS-1$
        		log.error("Cannot find keytool using property " + KEYTOOL_DIRECTORY + "="+keytoolDir);
        		keytoolPath = null; // don't try anything else if the property is provided
        	}
        } else {
        	keytoolPath = "keytool"; // $NON-NLS-1$
        	if (!checkKeytool(keytoolPath)) { // Not found on PATH, check Java Home
        		File javaHome = JOrphanUtils.getJavaHome();
        		if (javaHome != null) {
        			keytoolPath = new File(new File(javaHome,"bin"),"keytool").getPath(); // $NON-NLS-1$ $NON-NLS-2$
					if (!checkKeytool(keytoolPath)) {
            			keytoolPath = null;
            		}
        		} else {
        			keytoolPath = null;
        		}
        	}
        }
        if (keytoolPath == null) {
        	log.error("Unable to find keytool application. Check PATH or define system property " + KEYTOOL_DIRECTORY);
        } else {
        	log.info("keytool found at '" + keytoolPath + "'");
        }
    	KEYTOOL_PATH = keytoolPath;
    }

    private static final String DNAME_INTERMEDIATE_CA_KEY  = "cn=DO NOT INSTALL THIS CERTIFICATE (JMeter Intermediate CA)"; // $NON-NLS-1$

    public static final String ROOT_CACERT_CRT_PFX = "ApacheJMeterTemporaryRootCA"; // $NON-NLS-1$ (do not change)
    private static final String ROOT_CACERT_CRT = ROOT_CACERT_CRT_PFX + ".crt"; // $NON-NLS-1$ (Firefox and Windows)
    private static final String ROOT_CACERT_USR = ROOT_CACERT_CRT_PFX + ".usr"; // $NON-NLS-1$ (Opera)

    private static final String ROOTCA_ALIAS = ":root_ca:";  // $NON-NLS-1$
    private static final String INTERMEDIATE_CA_ALIAS = ":intermediate_ca:";  // $NON-NLS-1$

    /** Does this class support generation of host certificates? */
    public static final boolean SUPPORTS_HOST_CERT = SystemUtils.isJavaVersionAtLeast(JavaVersion.JAVA_1_7);
    // i.e. does keytool support -gencert and -ext ?

    private KeyToolUtils() {
        // not instantiable
    }

    /**
     * Generate a self-signed keypair using the algorithm "RSA".
     * Requires Java 7 or later if the "ext" parameter is not null.
     *
     * @param keystore the keystore; if it already contains the alias the command will fail
     * @param alias the alias to use, not null
     * @param password the password to use for the store and the key
     * @param validity the validity period in days, greater than 0
     * @param dname the dname value, if omitted use "cn=JMeter Proxy (DO NOT TRUST)"
     * @param ext if not null, the extension (-ext) to add (e.g. "bc:c"). This requires Java 7.
     *
     * @throws IOException
     */
    public static void genkeypair(final File keystore, String alias, final String password, int validity, String dname, String ext)
            throws IOException {
        final File workingDir = keystore.getParentFile();
        final SystemCommand nativeCommand = new SystemCommand(workingDir, null);
        final List<String> arguments = new ArrayList<String>();
        arguments.add(getKeyToolPath());
        arguments.add("-genkeypair"); // $NON-NLS-1$
        arguments.add("-alias"); // $NON-NLS-1$
        arguments.add(alias);
        arguments.add("-dname"); // $NON-NLS-1$
        arguments.add(dname == null ? DEFAULT_DNAME : dname);
        arguments.add("-keyalg"); // $NON-NLS-1$
        arguments.add("RSA"); // $NON-NLS-1$

        arguments.add("-keystore"); // $NON-NLS-1$
        arguments.add(keystore.getName());
        arguments.add("-storepass"); // $NON-NLS-1$
        arguments.add(password);
        arguments.add("-keypass"); // $NON-NLS-1$
        arguments.add(password);
        arguments.add("-validity"); // $NON-NLS-1$
        arguments.add(Integer.toString(validity));
        if (ext != null) { // Requires Java 7
            arguments.add("-ext"); // $NON-NLS-1$
            arguments.add(ext);
        }
        try {
            int exitVal = nativeCommand.run(arguments);
            if (exitVal != 0) {
                throw new IOException("Command :'"+formatCommand(arguments)+"' failed, code: " + exitVal + "\n" + nativeCommand.getOutResult());
            }
        } catch (InterruptedException e) {
            throw new IOException("Command was interrupted\n" + nativeCommand.getOutResult(), e);
        }
    }

    /**
     * Formats arguments
     * @param arguments
     * @return String command line
     */
    private static String formatCommand(List<String> arguments) {
        StringBuilder builder = new StringBuilder();
        for (String string : arguments) {
            builder.append("\"").append(string).append("\"").append(" ");
        }
        if(arguments.size()>0) {
            builder.setLength(builder.length()-1);
        }
        return builder.toString();
    }

    /**
     * Creates a self-signed Root CA certificate and an intermediate CA certificate
     * (signed by the Root CA certificate) that can be used to sign server certificates.
     * The Root CA certificate file is exported to the same directory as the keystore
     * in formats suitable for Firefox/Chrome/IE (.crt) and Opera (.usr).
     * Requires Java 7 or later.
     *
     * @param keystore the keystore in which to store everything
     * @param password the password for keystore and keys
     * @param validity the validity period in days, must be greater than 0
     *
     * @throws IOException
     */
    public static void generateProxyCA(File keystore, String password,  int validity) throws IOException {
        File caCert_crt = new File(ROOT_CACERT_CRT);
        File caCert_usr = new File(ROOT_CACERT_USR);
        boolean fileExists = false;
        if (!keystore.delete() && keystore.exists()) {
            log.warn("Problem deleting the keystore '" + keystore + "'");
            fileExists = true;
        }
        if (!caCert_crt.delete() && caCert_crt.exists()) {
            log.warn("Problem deleting the certificate file '" + caCert_crt + "'");
            fileExists = true;
        }
        if (!caCert_usr.delete() && caCert_usr.exists()) {
            log.warn("Problem deleting the certificate file '" + caCert_usr + "'");
            fileExists = true;
        }
        if (fileExists) {
            log.warn("If problems occur when recording SSL, delete the files manually and retry.");
        }
        // Create the self-signed keypairs (requires Java 7 for -ext flag)
        KeyToolUtils.genkeypair(keystore, ROOTCA_ALIAS, password, validity, DNAME_ROOT_CA_KEY, "bc:c");
        KeyToolUtils.genkeypair(keystore, INTERMEDIATE_CA_ALIAS, password, validity, DNAME_INTERMEDIATE_CA_KEY, "bc:c");

        // Create cert for CA using root (requires Java 7 for gencert)
        ByteArrayOutputStream certReqOut = new ByteArrayOutputStream();
        // generate the request
        KeyToolUtils.keytool("-certreq", keystore, password, INTERMEDIATE_CA_ALIAS, null, certReqOut);

        // generate the certificate and store in output file
        InputStream certReqIn = new ByteArrayInputStream(certReqOut.toByteArray());
        ByteArrayOutputStream genCertOut = new ByteArrayOutputStream();
        KeyToolUtils.keytool("-gencert", keystore, password, ROOTCA_ALIAS, certReqIn, genCertOut, "-ext", "BC:0");

        // import the signed CA cert into the store (root already there) - both are needed to sign the domain certificates
        InputStream genCertIn = new ByteArrayInputStream(genCertOut.toByteArray());
        KeyToolUtils.keytool("-importcert", keystore, password, INTERMEDIATE_CA_ALIAS, genCertIn, null);

        // Export the Root CA for Firefox/Chrome/IE
        KeyToolUtils.keytool("-exportcert", keystore, password, ROOTCA_ALIAS, null, null, "-rfc", "-file", ROOT_CACERT_CRT);
        // Copy for Opera
        FileUtils.copyFile(caCert_crt, caCert_usr);
    }

    /**
     * Create a host certificate signed with the CA certificate.
     * Requires Java 7 or later.
     *
     * @param keystore the keystore to use
     * @param password the password to use for the keystore and keys
     * @param host the host, e.g. jmeter.apache.org or *.apache.org; also used as the alias
     * @param validity the validity period for the generated keypair
     *
     * @throws IOException
     *
     */
    public static void generateHostCert(File keystore, String password, String host, int validity) throws IOException {
        // generate the keypair for the host
        generateSignedCert(keystore, password, validity,
                host,  // alias
                host); // subject
    }

    private static void generateSignedCert(File keystore, String password,
            int validity, String alias, String subject) throws IOException {
        String dname = "cn=" + subject + ", o=JMeter Proxy (TEMPORARY TRUST ONLY)";
        KeyToolUtils.genkeypair(keystore, alias, password, validity, dname, null);
        //rem generate cert for DOMAIN using CA (requires Java7 for gencert) and import it

        // get the certificate request
        ByteArrayOutputStream certReqOut = new ByteArrayOutputStream();
        KeyToolUtils.keytool("-certreq", keystore, password, alias, null, certReqOut);

        // create the certificate
        //rem ku:c=dig,keyE means KeyUsage:criticial=digitalSignature,keyEncipherment
        InputStream certReqIn = new ByteArrayInputStream(certReqOut.toByteArray());
        ByteArrayOutputStream certOut = new ByteArrayOutputStream();
        KeyToolUtils.keytool("-gencert", keystore, password, INTERMEDIATE_CA_ALIAS, certReqIn, certOut, "-ext", "ku:c=dig,keyE");

        // inport the certificate
        InputStream certIn = new ByteArrayInputStream(certOut.toByteArray());
        KeyToolUtils.keytool("-importcert", keystore, password, alias, certIn, null, "-noprompt");
    }

    /**
     * List the contents of a keystore
     *
     * @param keystore the keystore file
     * @param storePass the keystore password
     * @return the output from the command "keytool -list -v"
     */
    public static String list(final File keystore, final String storePass) throws IOException {
        final File workingDir = keystore.getParentFile();
        final SystemCommand nativeCommand = new SystemCommand(workingDir, null);
        final List<String> arguments = new ArrayList<String>();
        arguments.add(getKeyToolPath());
        arguments.add("-list"); // $NON-NLS-1$
        arguments.add("-v"); // $NON-NLS-1$

        arguments.add("-keystore"); // $NON-NLS-1$
        arguments.add(keystore.getName());
        arguments.add("-storepass"); // $NON-NLS-1$
        arguments.add(storePass);
        try {
            int exitVal = nativeCommand.run(arguments);
            if (exitVal != 0) {
                throw new IOException("Command failed, code: " + exitVal + "\n" + nativeCommand.getOutResult());
            }
        } catch (InterruptedException e) {
            throw new IOException("Command was interrupted\n" + nativeCommand.getOutResult(), e);
        }
        return nativeCommand.getOutResult();
    }

    /**
     * Returns a list of the CA aliases that should be in the keystore.
     *
     * @return the aliases that are used for the keystore
     */
    public static String[] getCAaliases() {
        return new String[]{ROOTCA_ALIAS, INTERMEDIATE_CA_ALIAS};
    }

    /**
     * Get the root CA alias; needed to check the serial number and fingerprint
     *
     * @return the alias
     */
    public static String getRootCAalias() {
        return ROOTCA_ALIAS;
    }

    /**
     * Helper method to simplify chaining keytool commands.
     *
     * @param command the command, not null
     * @param keystore the keystore, not nill
     * @param password the password used for keystore and key, not null
     * @param alias the alias, not null
     * @param input where to source input, may be null
     * @param output where to send output, may be null
     * @param parameters additional parameters to the command, may be null
     * @throws IOException
     */
    private static void keytool(String command, File keystore, String password, String alias,
            InputStream input, OutputStream output, String ... parameters)
            throws IOException {
        final File workingDir = keystore.getParentFile();
        final SystemCommand nativeCommand = new SystemCommand(workingDir, 0L, 0, null, input, output, null);
        final List<String> arguments = new ArrayList<String>();
        arguments.add(getKeyToolPath());
        arguments.add(command);
        arguments.add("-keystore"); // $NON-NLS-1$
        arguments.add(keystore.getName());
        arguments.add("-storepass"); // $NON-NLS-1$
        arguments.add(password);
        arguments.add("-keypass"); // $NON-NLS-1$
        arguments.add(password);
        arguments.add("-alias"); // $NON-NLS-1$
        arguments.add(alias);
        for (String parameter : parameters) {
            arguments.add(parameter);
        }

        try {
            int exitVal = nativeCommand.run(arguments);
            if (exitVal != 0) {
                throw new IOException("Command failed, code: " + exitVal + "\n" + nativeCommand.getOutResult());
            }
        } catch (InterruptedException e) {
            throw new IOException("Command was interrupted\n" + nativeCommand.getOutResult(), e);
        }
    }

    public static boolean haveKeytool() {
    	return KEYTOOL_PATH != null;
    }

    private static String getKeyToolPath() throws IOException {
    	if (KEYTOOL_PATH == null) {
    		throw new IOException("keytool application cannot be found");
    	}
    	return "keytool";
    }

    /**
     * Check if keytool can be found
     * @param keytoolPath the path to check
     */
	private static boolean checkKeytool(String keytoolPath) {
        final SystemCommand nativeCommand = new SystemCommand(null, null);
        final List<String> arguments = new ArrayList<String>();
        arguments.add(keytoolPath);
        arguments.add("-help"); // $NON-NLS-1$
        try {
            int status = nativeCommand.run(arguments);
            return status == 0;
        } catch (IOException ioe) {
        	return false;
        } catch (InterruptedException e) {
        	log.error("Command was interrupted\n" + nativeCommand.getOutResult(), e);
        	return false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13625.java
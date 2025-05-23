error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10628.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10628.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10628.java
text:
```scala
private static final S@@tring MASKED_MYPASSWORD_VALUE = "MASK-UWB5tlhOmKYzJVl9KZaPN";

/*
 *
 *  JBoss, Home of Professional Open Source.
 *  Copyright 2013, Red Hat, Inc., and individual contributors
 *  as indicated by the @author tags. See the copyright.txt file in the
 *  distribution for a full listing of individual contributors.
 *
 *  This is free software; you can redistribute it and/or modify it
 *  under the terms of the GNU Lesser General Public License as
 *  published by the Free Software Foundation; either version 2.1 of
 *  the License, or (at your option) any later version.
 *
 *  This software is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this software; if not, write to the Free
 *  Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 *  02110-1301 USA, or see the FSF site: http://www.fsf.org.
 * /
 */

package org.jboss.as.security;

import static java.util.Collections.addAll;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URLDecoder;
import java.security.Permission;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.as.security.vault.MockRuntimeVaultReader;
import org.jboss.as.security.vault.VaultTool;
import org.jboss.as.server.services.security.VaultReaderException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class VaultToolTestCase {

  private static final String KEYSTORE_PASSWORD = "mypassword";
  private static final String BLOCK_NAME = "myblock";
  private static final String ATTRIBUTE_NAME = "the_attribute_I_want_to_store";
  private static final String VALUE_TO_STORE = "the_value";
  private static final String KEYSTORE_ALIAS_VALUE = "vault";
  private static final String ITERATION_COUNT_VALUE = "12";
  private static final String CODE_LOCATION = VaultToolTestCase.class.getProtectionDomain().getCodeSource().getLocation().getFile();
  private static final String KEYSTORE_URL_VALUE = getKeystorePath();
  private static final String ENC_FILE_DIR_VALUE = CODE_LOCATION + "test_vault_dir";
  private static final String MASKED_MYPASSWORD_VALUE = "MASK-0UWB5tlhOmKYzJVl9KZaPN";
  private static final String SALT_VALUE = "bdfbdf12";
  private static final ByteArrayOutputStream SYSTEM_OUT = new ByteArrayOutputStream();

  private static final String getKeystorePath() {
     try {
       return new String(URLDecoder.decode( CODE_LOCATION, "UTF-8" ) + "org/jboss/as/security/vault.keystore");
     } catch (Exception e) {
       throw new Error("Unable to decode url", e);
     }
  }

  @Test
  public void testVaultTool() throws IOException, VaultReaderException {
    // Replaces standard output with a ByteArrayOutputStream to parse the output later.
    System.setOut(new PrintStream(SYSTEM_OUT));

    try {
      String[] args = generateArgs(); // Generate the required arguments
      VaultTool.main(args);
    } catch (ExitException e) {
      Assert.assertEquals("Exit status is equal to 0", 0, e.status);
    }
    SYSTEM_OUT.flush();
    String ouput = new String(SYSTEM_OUT.toByteArray());
    String[] outputLines = ouput.split("\n");

    String vaultSharedKey = getStoredAttributeSharedKey(outputLines);
    Assert.assertNotNull("VaultTool did not return a line starting with VAULT::", vaultSharedKey);
    MockRuntimeVaultReader rvr = new MockRuntimeVaultReader();
    Map<String, Object> options = generateVaultOptionsMap();
    rvr.createVault("", options);
    String retrievedValueFromVault = rvr.retrieveFromVault(vaultSharedKey);
    Assert.assertEquals("The value retrieved from vault is not the same as the one initially stored", VALUE_TO_STORE,
        retrievedValueFromVault);
  }

  private Map<String, Object> generateVaultOptionsMap() {
    Map<String, Object> options = new HashMap<String, Object>();
    options.put("KEYSTORE_URL", KEYSTORE_URL_VALUE);
    options.put("KEYSTORE_PASSWORD", MASKED_MYPASSWORD_VALUE);
    options.put("SALT", SALT_VALUE);
    options.put("ITERATION_COUNT", ITERATION_COUNT_VALUE);
    options.put("KEYSTORE_ALIAS", KEYSTORE_ALIAS_VALUE);
    options.put("ENC_FILE_DIR", ENC_FILE_DIR_VALUE);
    return options;
  }

  private String getStoredAttributeSharedKey(String[] linesOfOutput) {
    String vaultSharedKey = null;
    for (String line : linesOfOutput) {
      if (line.startsWith("VAULT::")) {
        vaultSharedKey = line;
        break;
      }
    }
    return vaultSharedKey;
  }

  /**
   * Generates arguments to create security vault and store a password inside.
   * @return
   */
  private String[] generateArgs() {
    List<String> args = new ArrayList<String>();
    addAll(args, "-k", KEYSTORE_URL_VALUE);
    addAll(args, "-p", KEYSTORE_PASSWORD);
    addAll(args, "-e", ENC_FILE_DIR_VALUE);
    addAll(args, "-s", SALT_VALUE);
    addAll(args, "-i", ITERATION_COUNT_VALUE);
    addAll(args, "-b", BLOCK_NAME);
    addAll(args, "-a", ATTRIBUTE_NAME);
    addAll(args, "-x", VALUE_TO_STORE);
    return args.toArray(new String[0]);
  }

  protected static class ExitException extends SecurityException {

    public final int status;

    public ExitException(int status) {
      super("There is no escape!");
      this.status = status;
    }
  }
  private static class NoExitSecurityManager extends SecurityManager {

    @Override
    public void checkPermission(Permission perm) {
      // allow anything.
    }

    @Override
    public void checkPermission(Permission perm, Object context) {
      // allow anything.
    }

    @Override
    public void checkExit(int status) {
      super.checkExit(status);
      throw new ExitException(status);
    }
  }

  @Before
  public void setUp() throws Exception {
    cleanDirectory(ENC_FILE_DIR_VALUE);
    System.setSecurityManager(new NoExitSecurityManager());
  }

  @After
  public void tearDown() throws Exception {
    System.setSecurityManager(null); // or save and restore original
    cleanDirectory(ENC_FILE_DIR_VALUE);
  }

  /**
   * Clean given directory.
   * 
   * @param directory
   */
  private static void cleanDirectory(String dir) {
    File directory = new File(dir);  
    if (directory.exists()) {
      for (File f : directory.listFiles()) {
        f.delete();
       }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10628.java
error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1022.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1022.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1022.java
text:
```scala
S@@tring password = cmdLine.getOptionValue(SEC_ATTR_VALUE_PARAM, "password");

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
package org.jboss.as.security.vault;

import java.io.Console;
import java.util.Scanner;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.jboss.security.vault.SecurityVault;

/**
 * Command Line Tool for the default implementation of the {@link SecurityVault}
 *
 * @author Anil Saldhana
 * @author Peter Skopek
 */
public class VaultTool {

    public static final String KEYSTORE_PARAM = "keystore";
    public static final String KEYSTORE_PASSWORD_PARAM = "keystore-password";
    public static final String ENC_DIR_PARAM = "enc-dir";
    public static final String SALT_PARAM = "salt";
    public static final String ITERATION_PARAM = "iteration";
    public static final String ALIAS_PARAM = "alias";
    public static final String VAULT_BLOCK_PARAM = "vault-block";
    public static final String ATTRIBUTE_PARAM = "attribute";
    public static final String SEC_ATTR_VALUE_PARAM = "sec-attr";
    public static final String CHECK_SEC_ATTR_EXISTS_PARAM = "check-sec-attr";
    public static final String HELP_PARAM = "help";

    private VaultInteractiveSession session = null;
    private VaultSession nonInteractiveSession = null;

    private Options options = null;
    private CommandLineParser parser = null;
    private CommandLine cmdLine = null;

    public void setSession(VaultInteractiveSession sess) {
        session = sess;
    }

    public VaultInteractiveSession getSession() {
        return session;
    }

    public static void main(String[] args) {

        VaultTool tool = null;

        if (args != null && args.length > 0) {
            int returnVal = 0;
            try {
                tool = new VaultTool(args);
                returnVal = tool.execute();
                if (returnVal != 100)
                    tool.summary();
            } catch (Exception e) {
                System.err.println("Problem occured:");
                e.printStackTrace(System.err);
                System.exit(1);
            }
            System.exit(returnVal);
        } else {
            tool = new VaultTool();

            System.out.println("**********************************");
            System.out.println("****  JBoss Vault  ***************");
            System.out.println("**********************************");

            Console console = System.console();

            if (console == null) {
                System.err.println("No console.");
                System.exit(1);
            }

            Scanner in = new Scanner(System.in);
            while (true) {
                String commandStr = "Please enter a Digit::   0: Start Interactive Session "
                        + " 1: Remove Interactive Session " + " 2: Exit";

                System.out.println(commandStr);
                int choice = in.nextInt();
                switch (choice) {
                    case 0:
                        System.out.println("Starting an interactive session");
                        VaultInteractiveSession vsession = new VaultInteractiveSession();
                        tool.setSession(vsession);
                        vsession.start();
                        break;
                    case 1:
                        System.out.println("Removing the current interactive session");
                        tool.setSession(null);
                        break;
                    default:
                        System.exit(0);
                }
            }

        }

    }

    public VaultTool(String[] args) {
        initOptions();
        parser = new PosixParser();
        try {
            cmdLine = parser.parse(options, args, true);
        } catch (ParseException e) {
            System.out.println("Problem while parsing command line parameters:");
            e.printStackTrace(System.err);
            System.exit(2);
        }
    }

    public VaultTool() {
    }

    /**
     * Build options for non-interactive VaultTool usage scenario.
     *
     * @return
     */
    private void initOptions() {
        options = new Options();
        options.addOption("k", KEYSTORE_PARAM, true, "Keystore URL");
        options.addOption("p", KEYSTORE_PASSWORD_PARAM, true, "Keystore password");
        options.addOption("e", ENC_DIR_PARAM, true, "Directory containing encrypted files");
        options.addOption("s", SALT_PARAM, true, "8 character salt");
        options.addOption("i", ITERATION_PARAM, true, "Iteration count");
        options.addOption("v", ALIAS_PARAM, true, "Vault keystore alias");
        options.addOption("b", VAULT_BLOCK_PARAM, true, "Vault block");
        options.addOption("a", ATTRIBUTE_PARAM, true, "Attribute name");

        OptionGroup og = new OptionGroup();
        Option x = new Option("x", SEC_ATTR_VALUE_PARAM, true, "Secured attribute value (such as password) to store");
        Option c = new Option("c", CHECK_SEC_ATTR_EXISTS_PARAM, false, "Check whether the secured attribute already exists in the vault");
        Option h = new Option("h", HELP_PARAM, false, "Help");
        og.addOption(x);
        og.addOption(c);
        og.addOption(h);
        og.setRequired(true);
        options.addOptionGroup(og);
    }

    private int execute() throws Exception {

        if (cmdLine.hasOption(HELP_PARAM)) {
            printUsage();
            return 100;
        }

        String keystoreURL = cmdLine.getOptionValue(KEYSTORE_PARAM, "vault.keystore");
        String keystorePassword = cmdLine.getOptionValue(KEYSTORE_PASSWORD_PARAM, "");
        String encryptionDirectory = cmdLine.getOptionValue(ENC_DIR_PARAM, "vault");
        String salt = cmdLine.getOptionValue(SALT_PARAM, "12345678");
        int iterationCount = Integer.parseInt(cmdLine.getOptionValue(ITERATION_PARAM, "23"));

        nonInteractiveSession = new VaultSession(keystoreURL, keystorePassword, encryptionDirectory, salt, iterationCount);

        nonInteractiveSession.startVaultSession(cmdLine.getOptionValue("alias", "vault"));

        String vaultBlock = cmdLine.getOptionValue(VAULT_BLOCK_PARAM, "vb");
        String attributeName = cmdLine.getOptionValue(ATTRIBUTE_PARAM, "password");

        if (cmdLine.hasOption(CHECK_SEC_ATTR_EXISTS_PARAM)) {
            // check password
            if (nonInteractiveSession.checkSecuredAttribute(vaultBlock, attributeName)) {
                System.out.println("Password already exists.");
                return 0;
            } else {
                System.out.println("Password doesn't exist.");
                return 5;
            }
        } else {
            // add password
            String password = cmdLine.getOptionValue(ATTRIBUTE_PARAM, "password");
            nonInteractiveSession.addSecuredAttributeWithDisplay(vaultBlock, attributeName, password.toCharArray());
            return 0;
        }
    }

    private void summary() {
        nonInteractiveSession.vaultConfigurationDisplay();
    }

    private void printUsage() {
        HelpFormatter help = new HelpFormatter();
        String suffix = (VaultTool.isWindows() ? ".bat" : ".sh");
        help.printHelp("vault" + suffix + " <empty> | ", options, true);
    }

    public static boolean isWindows() {
        String opsys = System.getProperty("os.name").toLowerCase();
        return (opsys.indexOf("win") >= 0);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1022.java
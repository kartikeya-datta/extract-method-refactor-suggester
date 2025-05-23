error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7731.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7731.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7731.java
text:
```scala
c@@md.setExecutable(JavaEnvUtils.getJdkExecutable("keytool"));

/*
 * Copyright  2000,2002,2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.apache.tools.ant.taskdefs;

import java.util.Enumeration;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.util.JavaEnvUtils;

/**
 * Generates a key in a keystore.
 *
 * @since Ant 1.2
 *
 * @ant.task name="genkey" category="java"
 */
public class GenerateKey extends Task {

    public static class DnameParam {
        private String name;
        private String value;

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static class DistinguishedName {
        private Vector params = new Vector();

        public Object createParam() {
            DnameParam param = new DnameParam();
            params.addElement(param);

            return param;
        }

        public Enumeration getParams() {
            return params.elements();
        }

        public String toString() {
            final int size = params.size();
            final StringBuffer sb = new StringBuffer();
            boolean firstPass = true;

            for (int i = 0; i < size; i++) {
                if (!firstPass) {
                    sb.append(" ,");
                }
                firstPass = false;

                final DnameParam param = (DnameParam) params.elementAt(i);
                sb.append(encode(param.getName()));
                sb.append('=');
                sb.append(encode(param.getValue()));
            }

            return sb.toString();
        }

        public String encode(final String string) {
            int end = string.indexOf(',');

            if (-1 == end) {
              return string;
            }

            final StringBuffer sb = new StringBuffer();

            int start = 0;

            while (-1 != end) {
                sb.append(string.substring(start, end));
                sb.append("\\,");
                start = end + 1;
                end = string.indexOf(',', start);
            }

            sb.append(string.substring(start));

            return sb.toString();
        }
    }

    /**
     * The alias of signer.
     */
    protected String alias;

    /**
     * The name of keystore file.
     */
    protected String keystore;
    protected String storepass;
    protected String storetype;
    protected String keypass;

    protected String sigalg;
    protected String keyalg;
    protected String dname;
    protected DistinguishedName expandedDname;
    protected int keysize;
    protected int validity;
    protected boolean verbose;

    /**
     * Distinguished name list.
     *
     * @return Distinguished name container.
     * @throws BuildException If specified more than once or dname
     *                        attribute is used.
     */
    public DistinguishedName createDname() throws BuildException {
        if (null != expandedDname) {
            throw new BuildException("DName sub-element can only be "
                                     + "specified once.");
        }
        if (null != dname) {
            throw new BuildException("It is not possible to specify dname "
                                    + " both as attribute and element.");
        }
        expandedDname = new DistinguishedName();
        return expandedDname;
    }

    /**
     * The distinguished name for entity.
     *
     * @param dname distinguished name
     */
    public void setDname(final String dname) {
        if (null != expandedDname) {
            throw new BuildException("It is not possible to specify dname "
                                    + " both as attribute and element.");
        }
        this.dname = dname;
    }

    /**
     * The alias to add under.
     *
     * @param alias alias to add under
     */
    public void setAlias(final String alias) {
        this.alias = alias;
    }

    /**
     * Keystore location.
     *
     * @param keystore location
     */
    public void setKeystore(final String keystore) {
        this.keystore = keystore;
    }

    /**
     * Password for keystore integrity.
     * Must be at least 6 characters long.
     * @param storepass password
     */
    public void setStorepass(final String storepass) {
        this.storepass = storepass;
    }

    /**
     * Keystore type.
     *
     * @param storetype type
     */
    public void setStoretype(final String storetype) {
        this.storetype = storetype;
    }

    /**
     * Password for private key (if different).
     *
     * @param keypass password
     */
    public void setKeypass(final String keypass) {
        this.keypass = keypass;
    }

    /**
     * The algorithm to use in signing.
     *
     * @param sigalg algorithm
     */
    public void setSigalg(final String sigalg) {
        this.sigalg = sigalg;
    }

    /**
     * The method to use when generating name-value pair.
     * @param keyalg algorithm
     */
    public void setKeyalg(final String keyalg) {
        this.keyalg = keyalg;
    }

    /**
     * Indicates the size of key generated.
     *
     * @param keysize size of key
     * @throws BuildException If not an Integer
     * @todo Could convert this to a plain Integer setter.
     */
    public void setKeysize(final String keysize) throws BuildException {
        try {
            this.keysize = Integer.parseInt(keysize);
        } catch (final NumberFormatException nfe) {
            throw new BuildException("KeySize attribute should be a integer");
        }
    }

    /**
     * Indicates how many days certificate is valid.
     *
     * @param validity days valid
     * @throws BuildException If not an Integer
     */
    public void setValidity(final String validity) throws BuildException {
        try {
            this.validity = Integer.parseInt(validity);
        } catch (final NumberFormatException nfe) {
            throw new BuildException("Validity attribute should be a integer");
        }
    }

    /**
     * If true, verbose output when signing.
     * @param verbose verbose or not
     */
    public void setVerbose(final boolean verbose) {
        this.verbose = verbose;
    }

    public void execute() throws BuildException {

        if (null == alias) {
            throw new BuildException("alias attribute must be set");
        }

        if (null == storepass) {
            throw new BuildException("storepass attribute must be set");
        }

        if (null == dname && null == expandedDname) {
            throw new BuildException("dname must be set");
        }

        final StringBuffer sb = new StringBuffer();

        sb.append("-genkey ");

        if (verbose) {
            sb.append("-v ");
        }

        sb.append("-alias \"");
        sb.append(alias);
        sb.append("\" ");

        if (null != dname) {
            sb.append("-dname \"");
            sb.append(dname);
            sb.append("\" ");
        }

        if (null != expandedDname) {
            sb.append("-dname \"");
            sb.append(expandedDname);
            sb.append("\" ");
        }

        if (null != keystore) {
            sb.append("-keystore \"");
            sb.append(keystore);
            sb.append("\" ");
        }

        if (null != storepass) {
            sb.append("-storepass \"");
            sb.append(storepass);
            sb.append("\" ");
        }

        if (null != storetype) {
            sb.append("-storetype \"");
            sb.append(storetype);
            sb.append("\" ");
        }

        sb.append("-keypass \"");
        if (null != keypass) {
            sb.append(keypass);
        } else {
            sb.append(storepass);
        }
        sb.append("\" ");

        if (null != sigalg) {
            sb.append("-sigalg \"");
            sb.append(sigalg);
            sb.append("\" ");
        }

        if (null != keyalg) {
            sb.append("-keyalg \"");
            sb.append(keyalg);
            sb.append("\" ");
        }


        if (0 < keysize) {
            sb.append("-keysize \"");
            sb.append(keysize);
            sb.append("\" ");
        }

        if (0 < validity) {
            sb.append("-validity \"");
            sb.append(validity);
            sb.append("\" ");
        }

        log("Generating Key for " + alias);
        final ExecTask cmd = (ExecTask) getProject().createTask("exec");
        cmd.setExecutable("keytool");
        Commandline.Argument arg = cmd.createArg();
        arg.setLine(sb.toString());
        cmd.setFailonerror(true);
        cmd.setTaskName(getTaskName());
        cmd.execute();
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7731.java
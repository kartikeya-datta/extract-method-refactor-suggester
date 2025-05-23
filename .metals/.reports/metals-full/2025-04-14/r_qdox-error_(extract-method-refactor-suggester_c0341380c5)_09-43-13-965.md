error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17847.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17847.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17847.java
text:
```scala
private synchronized v@@oid openFile() {

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

package org.apache.jmeter.functions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.testelement.TestListener;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.util.JMeterStopThreadException;
import org.apache.log.Logger;

/**
 * StringFromFile Function to read a String from a text file.
 *
 * Parameters:
 * - file name
 * - variable name (optional - defaults to StringFromFile_)
 *
 * Returns:
 * - the next line from the file
 * - or **ERR** if an error occurs
 * - value is also saved in the variable for later re-use.
 *
 * Ensure that different variable names are used for each call to the function
 *
 *
 * Notes:
 * <ul>
 * <li>JMeter instantiates a single copy of each function for every reference in the test plan</li>
 * <li>Function instances are shared between threads.</li>
 * <li>Each StringFromFile instance reads the file independently. The output variable can be used to save the
 * value for later use in the same thread.</li>
 * <li>The file name is resolved at file (re-)open time; the file is initially opened on first execution (which could be any thread)</li>
 * <li>the output variable name is resolved every time the function is invoked</li>
 * </ul>
 * Because function instances are shared, it does not make sense to use the thread number as part of the file name.
 * @since 1.9
 */
public class StringFromFile extends AbstractFunction implements TestListener {
    private static final Logger log = LoggingManager.getLoggerForClass();

    // Only modified by static block so no need to synchronize subsequent read-only access
    private static final List<String> desc = new LinkedList<String>();

    private static final String KEY = "__StringFromFile";//$NON-NLS-1$

    static final String ERR_IND = "**ERR**";//$NON-NLS-1$

    static {
        desc.add(JMeterUtils.getResString("string_from_file_file_name"));//$NON-NLS-1$
        desc.add(JMeterUtils.getResString("function_name_paropt"));//$NON-NLS-1$
        desc.add(JMeterUtils.getResString("string_from_file_seq_start"));//$NON-NLS-1$
        desc.add(JMeterUtils.getResString("string_from_file_seq_final"));//$NON-NLS-1$
    }

    private static final int MIN_PARAM_COUNT = 1;

    private static final int PARAM_NAME = 2;

    private static final int PARAM_START = 3;

    private static final int PARAM_END = 4;

    private static final int MAX_PARAM_COUNT = 4;

    private static final int COUNT_UNUSED = -2;

    // @GuardedBy("this")
    private Object[] values;

    // @GuardedBy("this")
    private BufferedReader myBread = null; // Buffered reader

    // @GuardedBy("this")
    private boolean firstTime = false; // should we try to open the file?

    // @GuardedBy("this")
    private String fileName; // needed for error messages

    // @GuardedBy("this")
    private int myStart = COUNT_UNUSED;

    // @GuardedBy("this")
    private int myCurrent = COUNT_UNUSED;

    // @GuardedBy("this")
    private int myEnd = COUNT_UNUSED;

    public StringFromFile() {
        if (log.isDebugEnabled()) {
            log.debug("++++++++ Construct " + this);
        }
    }

    /**
     * Close file and log
     */
    private synchronized void closeFile() {
        if (myBread == null) {
            return;
        }
        String tn = Thread.currentThread().getName();
        log.info(tn + " closing file " + fileName);//$NON-NLS-1$
        try {
            myBread.close();
        } catch (IOException e) {
            log.error("closeFile() error: " + e.toString(), e);//$NON-NLS-1$
        }
    }
    
    private void openFile() {
        String tn = Thread.currentThread().getName();
        fileName = ((CompoundVariable) values[0]).execute();

        String start = "";
        if (values.length >= PARAM_START) {
            start = ((CompoundVariable) values[PARAM_START - 1]).execute();
            try {
                myStart = Integer.valueOf(start).intValue();
            } catch (NumberFormatException e) {
                myStart = COUNT_UNUSED;// Don't process invalid numbers
            }
        }
        // Have we used myCurrent yet?
        // Set to 1 if start number is missing (to allow for end without start)
        if (myCurrent == COUNT_UNUSED) {
            myCurrent = myStart == COUNT_UNUSED ? 1 : myStart;
        }

        if (values.length >= PARAM_END) {
            String tmp = ((CompoundVariable) values[PARAM_END - 1]).execute();
            try {
                myEnd = Integer.valueOf(tmp).intValue();
            } catch (NumberFormatException e) {
                myEnd = COUNT_UNUSED;// Don't process invalid numbers
                                        // (including "")
            }

        }

        if (values.length >= PARAM_START) {
            log.info(tn + " Start = " + myStart + " Current = " + myCurrent + " End = " + myEnd);//$NON-NLS-1$
            if (myEnd != COUNT_UNUSED) {
                if (myCurrent > myEnd) {
                    log.info(tn + " No more files to process, " + myCurrent + " > " + myEnd);//$NON-NLS-1$
                    myBread = null;
                    return;
                }
            }
            /*
             * DecimalFormat adds the number to the end of the format if there
             * are no formatting characters, so we need a way to prevent this
             * from messing up the file name.
             *
             */
            if (myStart != COUNT_UNUSED) // Only try to format if there is a
                                            // number
            {
                log.info(tn + " using format " + fileName);
                try {
                    DecimalFormat myFormatter = new DecimalFormat(fileName);
                    fileName = myFormatter.format(myCurrent);
                } catch (NumberFormatException e) {
                    log.warn("Bad file name format ", e);
                }
            }
            myCurrent++;// for next time
        }

        log.info(tn + " opening file " + fileName);//$NON-NLS-1$
        try {
            myBread = new BufferedReader(new FileReader(fileName));
        } catch (Exception e) {
            log.error("openFile() error: " + e.toString());//$NON-NLS-1$
            myBread = null;
        }
    }

    /** {@inheritDoc} */
    @Override
    public synchronized String execute(SampleResult previousResult, Sampler currentSampler)
            throws InvalidVariableException {
        String myValue = ERR_IND;
    	String myName = "StringFromFile_";//$NON-NLS-1$
        if (values.length >= PARAM_NAME) {
            myName = ((CompoundVariable) values[PARAM_NAME - 1]).execute().trim();
        }

        /*
         * To avoid re-opening the file repeatedly after an error, only try to
         * open it in the first execute() call (It may be re=opened at EOF, but
         * that will cause at most one failure.)
         */
        if (firstTime) {
            openFile();
            firstTime = false;
        }

        if (null != myBread) { // Did we open the file?
            try {
                String line = myBread.readLine();
                if (line == null) { // EOF, re-open file
                    String tn = Thread.currentThread().getName();
                    log.info(tn + " EOF on  file " + fileName);//$NON-NLS-1$
                    closeFile();
                    openFile();
                    if (myBread != null) {
                        line = myBread.readLine();
                    } else {
                        line = ERR_IND;
                        if (myEnd != COUNT_UNUSED) {// Are we processing a file
                                                    // sequence?
                            log.info(tn + " Detected end of sequence.");
                            throw new JMeterStopThreadException("End of sequence");
                        }
                    }
                }
                myValue = line;
            } catch (IOException e) {
                String tn = Thread.currentThread().getName();
                log.error(tn + " error reading file " + e.toString());//$NON-NLS-1$
            }
        } else { // File was not opened successfully
            if (myEnd != COUNT_UNUSED) {// Are we processing a file sequence?
                String tn = Thread.currentThread().getName();
                log.info(tn + " Detected end of sequence.");
                throw new JMeterStopThreadException("End of sequence");
            }
        }

        if (myName.length() > 0) {
            JMeterVariables vars = getVariables();
            if (vars != null) {// Can be null if called from Config item testEnded() method
                vars.put(myName, myValue);
            }
        }

        if (log.isDebugEnabled()) {
            String tn = Thread.currentThread().getName();
            log.debug(tn + " name:" //$NON-NLS-1$
                    + myName + " value:" + myValue);//$NON-NLS-1$
        }

        return myValue;

    }

    /** {@inheritDoc} */
    @Override
    public synchronized void setParameters(Collection<CompoundVariable> parameters) throws InvalidVariableException {

        log.debug(this + "::StringFromFile.setParameters()");//$NON-NLS-1$
        checkParameterCount(parameters, MIN_PARAM_COUNT, MAX_PARAM_COUNT);
        values = parameters.toArray();

        StringBuilder sb = new StringBuilder(40);
        sb.append("setParameters(");//$NON-NLS-1$
        for (int i = 0; i < values.length; i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(((CompoundVariable) values[i]).getRawParameters());
        }
        sb.append(")");//$NON-NLS-1$
        log.info(sb.toString());

        // N.B. setParameters is called before the test proper is started,
        // and thus variables are not interpreted at this point
        // So defer the file open until later to allow variable file names to be
        // used.
        firstTime = true;
    }

    /** {@inheritDoc} */
    @Override
    public String getReferenceKey() {
        return KEY;
    }

    /** {@inheritDoc} */
    public List<String> getArgumentDesc() {
        return desc;
    }

    /** {@inheritDoc} */
    public void testStarted() {
        //
    }

    /** {@inheritDoc} */
    public void testStarted(String host) {
        //
    }

    /** {@inheritDoc} */
    public void testEnded() {
        this.testEnded(""); //$NON-NLS-1$
    }

    /** {@inheritDoc} */
    public void testEnded(String host) {
    	closeFile();
    }

    /** {@inheritDoc} */
    public void testIterationStart(LoopIterationEvent event) {
        //
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17847.java
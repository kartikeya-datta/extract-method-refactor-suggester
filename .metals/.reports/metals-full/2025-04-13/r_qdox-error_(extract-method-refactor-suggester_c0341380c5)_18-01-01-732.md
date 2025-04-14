error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14751.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14751.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14751.java
text:
```scala
a@@ssertEquals("TFFFTFFFFFFT", results);

/*
 * Copyright  2000-2002,2004 The Apache Software Foundation
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

package org.apache.tools.ant.types.selectors;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.util.*;
import org.apache.tools.ant.BuildFileTest;
import org.apache.tools.ant.types.Parameter;

import junit.framework.TestCase;
import junit.framework.AssertionFailedError;

/**
 * Tests Size Selectors
 *
 */
public class SizeSelectorTest extends BaseSelectorTest {

    private Project project;

    public SizeSelectorTest(String name) {
        super(name);
    }

    /**
     * Factory method from base class. This is overriden in child
     * classes to return a specific Selector class.
     */
    public BaseSelector getInstance() {
        return new SizeSelector();
    }

    /**
     * Test the code that validates the selector.
     */
    public void testValidate() {
        SizeSelector s = (SizeSelector)getInstance();
        try {
            s.isSelected(basedir,filenames[0],files[0]);
            fail("SizeSelector did not check for required fields");
        } catch (BuildException be1) {
            assertEquals("The value attribute is required, and must "
                    + "be positive", be1.getMessage());
        }

        s = (SizeSelector)getInstance();
        s.setValue(-10);
        try {
            s.isSelected(basedir,filenames[0],files[0]);
            fail("SizeSelector did not check for value being in the "
                    + "allowable range");
        } catch (BuildException be2) {
            assertEquals("The value attribute is required, and must "
                    + "be positive", be2.getMessage());
        }

        s = (SizeSelector)getInstance();
        Parameter param = new Parameter();
        param.setName("garbage in");
        param.setValue("garbage out");
        Parameter[] params = {param};
        s.setParameters(params);
        try {
            s.isSelected(basedir,filenames[0],files[0]);
            fail("SizeSelector did not check for valid parameter element");
        } catch (BuildException be3) {
            assertEquals("Invalid parameter garbage in", be3.getMessage());
        }

        s = (SizeSelector)getInstance();
        param = new Parameter();
        param.setName("value");
        param.setValue("garbage out");
        params[0] = param;
        s.setParameters(params);
        try {
            s.isSelected(basedir,filenames[0],files[0]);
            fail("SizeSelector accepted bad value as parameter");
        } catch (BuildException be4) {
            assertEquals("Invalid size setting garbage out",
                    be4.getMessage());
        }

        s = (SizeSelector)getInstance();
        Parameter param1 = new Parameter();
        Parameter param2 = new Parameter();
        param1.setName("value");
        param1.setValue("5");
        param2.setName("units");
        param2.setValue("garbage out");
        params = new Parameter[2];
        params[0] = param1;
        params[1] = param2;
        try {
            s.setParameters(params);
            s.isSelected(basedir,filenames[0],files[0]);
            fail("SizeSelector accepted bad units as parameter");
        } catch (BuildException be5) {
            assertEquals("garbage out is not a legal value for this attribute",
                    be5.getMessage());
        }

    }

    /**
     * Tests to make sure that the selector is selecting files correctly.
     */
    public void testSelectionBehaviour() {
        SizeSelector s;
        String results;

        SizeSelector.ByteUnits kilo = new SizeSelector.ByteUnits();
        kilo.setValue("K");
        SizeSelector.ByteUnits kibi = new SizeSelector.ByteUnits();
        kibi.setValue("Ki");
        SizeSelector.ByteUnits tibi = new SizeSelector.ByteUnits();
        tibi.setValue("Ti");
        SizeSelector.SizeComparisons less = new SizeSelector.SizeComparisons();
        less.setValue("less");
        SizeSelector.SizeComparisons equal = new SizeSelector.SizeComparisons();
        equal.setValue("equal");
        SizeSelector.SizeComparisons more = new SizeSelector.SizeComparisons();
        more.setValue("more");


        try {
            makeBed();

            s = (SizeSelector)getInstance();
            s.setValue(10);
            s.setWhen(less);
            results = selectionString(s);
            assertEquals("TFFFFFFFFFFT", results);

            s = (SizeSelector)getInstance();
            s.setValue(10);
            s.setWhen(more);
            results = selectionString(s);
            assertEquals("TTTTTTTTTTTT", results);

            s = (SizeSelector)getInstance();
            s.setValue(32);
            s.setWhen(equal);
            results = selectionString(s);
            assertEquals("TTFFTFFFFFFT", results);

            s = (SizeSelector)getInstance();
            s.setValue(7);
            s.setWhen(more);
            s.setUnits(kilo);
            results = selectionString(s);
            assertEquals("TFTFFTTTTTTT", results);

            s = (SizeSelector)getInstance();
            s.setValue(7);
            s.setWhen(more);
            s.setUnits(kibi);
            results = selectionString(s);
            assertEquals("TFTFFFTTFTTT", results);

            s = (SizeSelector)getInstance();
            s.setValue(99999);
            s.setWhen(more);
            s.setUnits(tibi);
            results = selectionString(s);
            assertEquals("TFFFFFFFFFFT", results);

            s = (SizeSelector)getInstance();
            Parameter param1 = new Parameter();
            Parameter param2 = new Parameter();
            Parameter param3 = new Parameter();
            param1.setName("value");
            param1.setValue("20");
            param2.setName("units");
            param2.setValue("Ki");
            param3.setName("when");
            param3.setValue("more");
            Parameter[] params = {param1,param2,param3};
            s.setParameters(params);
            results = selectionString(s);
            assertEquals("TFFFFFFTFFTT", results);
        }
        finally {
            cleanupBed();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14751.java
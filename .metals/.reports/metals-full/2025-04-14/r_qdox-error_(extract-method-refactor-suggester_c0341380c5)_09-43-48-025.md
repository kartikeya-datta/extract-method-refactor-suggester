error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1530.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1530.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1530.java
text:
```scala
r@@eturn getDynamicSection(connection.holdability());

/*

   Derby - Class org.apache.derby.client.am.SectionManager

   Copyright (c) 2001, 2005 The Apache Software Foundation or its licensors, where applicable.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

*/

package org.apache.derby.client.am;

import org.apache.derby.shared.common.reference.JDBC30Translation;


public class SectionManager {
    String collection_;
    Agent agent_;

    // Cursor holdability attributes used as package cluster indices.
    // Not defined in PackageBindOptions because this attribute is part of the
    // declare cursor [with hold] sql string based on section binds.
    // By convention, we bind all sections in the same package with
    // the same holdability.
    final static int HOLD = 0;
    final static int NO_HOLD = 1;

    // The following stack of available sections is
    // for pooling and recycling previously used sections.
    // For performance, the section objects themselves are pooled,
    // rather than just keeping track of free section numbers;
    // this way, we don't have to new-up a section if one is available in the pool.
    java.util.Stack freeSectionsNonHold_ = null;
    java.util.Stack freeSectionsHold_ = null;

    int nextAvailableSectionNumber_ = 1;

    // store package consistency token information and initialized in
    // setPKGNAMCBytes
    // holdPKGNAMCBytes stores PKGNAMCBytes when holdability is hold
    // noHoldPKGNAMCBytes stores PKGNAMCBytes when holdability is no hold
    public static byte[] holdPKGNAMCBytes = null;
    public static byte[] noHoldPKGNAMCBytes = null;


    final static String packageNameWithHold__ = "SYSLH000";
    final static String packageNameWithNoHold__ = "SYSLN000";

    final static String cursorNamePrefixWithHold__ = "SQL_CURLH000C";
    final static String cursorNamePrefixWithNoHold__ = "SQL_CURLN000C";

    // Jdbc 1 positioned updates are implemented via
    // sql scan for "...where current of <users-cursor-name>",
    // the addition of mappings from cursor names to query sections,
    // and the subtitution of <users-cursor-name> with <canned-cursor-name> in the pass-thru sql string
    // "...where current of <canned-cursor-name>" when user-defined cursor names are used.
    // Both "canned" cursor names (from our jdbc package set) and user-defined cursor names are mapped.
    // Statement.cursorName_ is initialized to null until the cursor name is requested or set.
    // When set (s.setCursorName()) with a user-defined name, then it is added to the cursor map at that time;
    // When requested (rs.getCursorName()), if the cursor name is still null,
    // then is given the canned cursor name as defined by our jdbc package set and added to the cursor map.
    // Still need to consider how positioned updates should interact with multiple result sets from a stored.
    private java.util.Hashtable positionedUpdateCursorNameToQuerySection_ = new java.util.Hashtable();

    // Cursor name to ResultSet mapping is needed for positioned updates to check whether
    // a ResultSet is scrollable.  If so, exception is thrown.
    private java.util.Hashtable positionedUpdateCursorNameToResultSet_ = new java.util.Hashtable();

    String databaseName;

    int maxNumSections_ = 32768;

    public SectionManager(String collection, Agent agent, String databaseName) {
        collection_ = collection;
        agent_ = agent;
        this.databaseName = databaseName;
        freeSectionsNonHold_ = new java.util.Stack();
        freeSectionsHold_ = new java.util.Stack();
    }

    /**
     * Store the Packagename and consistency token information This is called from Section.setPKGNAMCBytes
     *
     * @param b                    bytearray that has the PKGNAMC information to be stored
     * @param resultSetHoldability depending on the holdability store it in the correct byte array packagename and
     *                             consistency token information for when holdability is set to HOLD_CURSORS_OVER_COMMIT
     *                             is stored in holdPKGNAMCBytes and in noHoldPKGNAMCBytes when holdability is set to
     *                             CLOSE_CURSORS_AT_COMMIT
     */
    public void setPKGNAMCBytes(byte[] b, int resultSetHoldability) {
        if (resultSetHoldability == JDBC30Translation.HOLD_CURSORS_OVER_COMMIT) {
            agent_.sectionManager_.holdPKGNAMCBytes = b;
        } else if (resultSetHoldability == JDBC30Translation.CLOSE_CURSORS_AT_COMMIT) {
            agent_.sectionManager_.noHoldPKGNAMCBytes = b;
        }
    }


    //------------------------entry points----------------------------------------

    // Get a section for either a jdbc update or query statement.
    public Section getDynamicSection(int resultSetHoldability) throws SqlException {
        int cursorHoldIndex;
        if (resultSetHoldability == JDBC30Translation.HOLD_CURSORS_OVER_COMMIT) {
            return getSection(freeSectionsHold_, packageNameWithHold__, cursorNamePrefixWithHold__, resultSetHoldability);
        } else if (resultSetHoldability == JDBC30Translation.CLOSE_CURSORS_AT_COMMIT) {
            return getSection(freeSectionsNonHold_, packageNameWithNoHold__, cursorNamePrefixWithNoHold__, resultSetHoldability);
        } else {
            throw new SqlException(agent_.logWriter_, "resultSetHoldability property " + resultSetHoldability + " not supported");
        }
    }

    protected Section getSection(java.util.Stack freeSections, String packageName, String cursorNamePrefix, int resultSetHoldability) throws SqlException {
        if (!freeSections.empty()) {
            return (Section) freeSections.pop();
        } else if (nextAvailableSectionNumber_ < (maxNumSections_ - 1)) {
            String cursorName = cursorNamePrefix + nextAvailableSectionNumber_;
            Section section = new Section(agent_, packageName, nextAvailableSectionNumber_, cursorName, resultSetHoldability);
            nextAvailableSectionNumber_++;
            return section;
        } else
        // unfortunately we have run out of sections
        {
            throw new SqlException(agent_.logWriter_, "Run out of sections to use,sections limited to 32k currently");
        }
    }

    public void freeSection(Section section, int resultSetHoldability) {
        if (resultSetHoldability == JDBC30Translation.HOLD_CURSORS_OVER_COMMIT) {
            this.freeSectionsHold_.push(section);
        } else if (resultSetHoldability == JDBC30Translation.CLOSE_CURSORS_AT_COMMIT) {
            this.freeSectionsNonHold_.push(section);
        }
    }

    // Get a section for a jdbc 2 positioned update/delete for the corresponding query.
    // A positioned update section must come from the same package as its query section.
    Section getPositionedUpdateSection(Section querySection) throws SqlException {
        Connection connection = agent_.connection_;
        return getDynamicSection(connection.resultSetHoldability_);
    }

    // Get a section for a jdbc 1 positioned update/delete for the corresponding query.
    // A positioned update section must come from the same package as its query section.
    Section getPositionedUpdateSection(String cursorName, boolean useExecuteImmediateSection) throws SqlException {
        Section querySection = (Section) positionedUpdateCursorNameToQuerySection_.get(cursorName);

        // If querySection is null, then the user must have provided a bad cursor name.
        // Otherwise, get a new section and save the client's cursor name and the server's
        // cursor name to the new section.
        if (querySection != null) {
            Section section = getPositionedUpdateSection(querySection);
            // getPositionedUpdateSection gets the next available section from the query
            // package, and it has a different cursor name associated with the section.
            // We need to save the client's cursor name and server's cursor name to the
            // new section.
            section.setClientCursorName(querySection.getClientCursorName());
            section.serverCursorNameForPositionedUpdate_ = querySection.getServerCursorName();
            return section;
        } else {
            return null;
        }
    }

    void mapCursorNameToQuerySection(String cursorName, Section section) {
        positionedUpdateCursorNameToQuerySection_.put(cursorName, section);
    }

    void mapCursorNameToResultSet(String cursorName, ResultSet resultSet) {
        positionedUpdateCursorNameToResultSet_.put(cursorName, resultSet);
    }

    ResultSet getPositionedUpdateResultSet(String cursorName) throws SqlException {
        ResultSet rs = (ResultSet) positionedUpdateCursorNameToResultSet_.get(cursorName);
        if (rs == null) {
            throw new SqlException(agent_.logWriter_, "ResultSet for cursor " +
                    cursorName + " is closed.");
        }
        return (rs.resultSetType_ == java.sql.ResultSet.TYPE_FORWARD_ONLY) ? null : rs;
    }

    void removeCursorNameToResultSetMapping(String clientCursorName,
                                            String serverCursorName) {
        if (clientCursorName != null) {
            positionedUpdateCursorNameToResultSet_.remove(clientCursorName);
        }
        if (serverCursorName != null) {
            positionedUpdateCursorNameToResultSet_.remove(serverCursorName);
        }
    }

    void removeCursorNameToQuerySectionMapping(String clientCursorName,
                                               String serverCursorName) {
        if (clientCursorName != null) {
            positionedUpdateCursorNameToQuerySection_.remove(clientCursorName);
        }
        if (serverCursorName != null) {
            positionedUpdateCursorNameToQuerySection_.remove(serverCursorName);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1530.java
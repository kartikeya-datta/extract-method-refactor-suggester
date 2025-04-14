error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2824.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2824.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2824.java
text:
```scala
L@@ogs.reportMessage("CSLOOK_TriggersHeader");

/*

   Licensed Materials - Property of IBM
   Cloudscape - Package org.apache.derby.impl.tools.cslook
   (C) Copyright IBM Corp. 2004. All Rights Reserved.
   US Government Users Restricted Rights - Use, duplication or
   disclosure restricted by GSA ADP Schedule Contract with IBM Corp.

 */

package org.apache.derby.impl.tools.cslook;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.HashMap;
import java.util.StringTokenizer;

import org.apache.derby.tools.dblook;

public class DB_Trigger {

	/* 
		IBM Copyright &copy notice.
	*/
	/**
		IBM Copyright &copy notice.
	*/

	public static final String copyrightNotice = org.apache.derby.iapi.reference.Copyright.SHORT_2004;

	/* ************************************************
	 * Generate the DDL for all triggers in a given
	 * database.
	 * @param conn Connection to the source database.
	 * @return The DDL for the triggers has been written
	 *  to output via Logs.java.
	 ****/

	public static void doTriggers (Connection conn)
		throws SQLException
	{

		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT TRIGGERNAME, SCHEMAID, " +
			"EVENT, FIRINGTIME, TYPE, TABLEID, REFERENCEDCOLUMNS, " + 
			"TRIGGERDEFINITION, REFERENCINGOLD, REFERENCINGNEW, OLDREFERENCINGNAME, " +
			"NEWREFERENCINGNAME FROM SYS.SYSTRIGGERS WHERE STATE != 'D'");

		boolean firstTime = true;
		while (rs.next()) {

			String trigName = dblook.addQuotes(
				dblook.expandDoubleQuotes(rs.getString(1)));
			String trigSchema = dblook.lookupSchemaId(rs.getString(2));

			if (dblook.isIgnorableSchema(trigSchema))
				continue;

			trigName = trigSchema + "." + trigName;
			String tableName = dblook.lookupTableId(rs.getString(6));

			// We'll write the DDL for this trigger if either 1) it is on
			// a table in the user-specified list, OR 2) the trigger text
			// contains a reference to a table in the user-specified list.

			if (!dblook.stringContainsTargetTable(rs.getString(8)) &&
				(dblook.isExcludedTable(tableName)))
				continue;

			if (firstTime) {
				Logs.reportString("----------------------------------------------");
				Logs.reportMessage("CSLOOK_Header", "triggers");
				Logs.reportString("----------------------------------------------\n");
			}

			String createTrigString = createTrigger(trigName,
				tableName, rs);

			Logs.writeToNewDDL(createTrigString);
			Logs.writeStmtEndToNewDDL();
			Logs.writeNewlineToNewDDL();
			firstTime = false;

		}

		rs.close();
		stmt.close();

	}

	/* ************************************************
	 * Generate DDL for a specific trigger.
	 * @param trigName Name of the trigger.
	 * @param tableName Name of the table on which the trigger
	 *  fires.
	 * @param aTrig Information about the trigger.
	 * @return The DDL for the current trigger is returned
	 *  as a String.
	 ****/

	private static String createTrigger(String trigName, String tableName,
		ResultSet aTrig) throws SQLException
	{

		StringBuffer sb = new StringBuffer ("CREATE TRIGGER ");
		sb.append(trigName);

		// Firing time.
		if (aTrig.getString(4).charAt(0) == 'A')
			sb.append(" AFTER ");
		else
			sb.append(" NO CASCADE BEFORE ");

		// Event.
		switch (aTrig.getString(3).charAt(0)) {
			case 'I':	sb.append("INSERT");
						break;
			case 'D':	sb.append("DELETE");
						break;
			case 'U':	sb.append("UPDATE");
						String updateCols = aTrig.getString(7);
						if (!aTrig.wasNull()) {
							sb.append(" OF ");
							sb.append(dblook.getColumnListFromDescription(
								aTrig.getString(6), updateCols));
						}
						break;
			default:	// shouldn't happen.
						Logs.debug("INTERNAL ERROR: unexpected trigger event: " + 
							aTrig.getString(3), (String)null);
						break;
		}

		// On table...
		sb.append(" ON ");
		sb.append(tableName);

		// Referencing...
		char trigType = aTrig.getString(5).charAt(0);
		String oldReferencing = aTrig.getString(11);
		String newReferencing = aTrig.getString(12);
		if ((oldReferencing != null) || (newReferencing != null)) {
			sb.append(" REFERENCING");
			if (aTrig.getBoolean(9)) {
				sb.append(" OLD");
				if (trigType == 'S')
				// Statement triggers work on tables.
					sb.append("_TABLE AS ");
				else
				// don't include "ROW" keyword (DB2 doesn't).
					sb.append(" AS ");
				sb.append(oldReferencing);
			}
			if (aTrig.getBoolean(10)) {
				sb.append(" NEW");
				if (trigType == 'S')
				// Statement triggers work on tables.
					sb.append("_TABLE AS ");
				else
				// don't include "ROW" keyword (DB2 doesn't).
					sb.append(" AS ");
				sb.append(newReferencing);
			}
		}

		// Trigger type (row/statement).
		sb.append(" FOR EACH ");
		if (trigType == 'S')
			sb.append("STATEMENT ");
		else
			sb.append("ROW ");

		// DB2 requires the following keywords in order to work.
		sb.append("MODE DB2SQL ");

		// Finally, the trigger action.
		sb.append(dblook.removeNewlines(aTrig.getString(8)));
		return sb.toString();

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2824.java
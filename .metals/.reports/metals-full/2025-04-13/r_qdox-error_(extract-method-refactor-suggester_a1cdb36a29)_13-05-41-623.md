error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2829.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2829.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2829.java
text:
```scala
L@@ogs.reportMessage("CSLOOK_JarsHeader");

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

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.derby.tools.dblook;

public class DB_Jar {

	/* 
		IBM Copyright &copy notice.
	*/
	/**
		IBM Copyright &copy notice.
	*/

	public static final String copyrightNotice = org.apache.derby.iapi.reference.Copyright.SHORT_2004;

	/* ************************************************
	 * Generate the DDL for all jars in a given
	 * database.
	 * @param dbName Name of the database (for locating the jar).
	 * @param conn Connection to the source database.
	 * @return The DDL for the jars has been written
	 *  to output via Logs.java.
	 ****/

	public static void doJars(String dbName, Connection conn)
		throws SQLException
	{

		String separator = System.getProperty("file.separator");
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT FILENAME, SCHEMAID, " +
			"GENERATIONID FROM SYS.SYSFILES");

		boolean firstTime = true;
		while (rs.next()) {

			String jarName = dblook.addQuotes(
				dblook.expandDoubleQuotes(rs.getString(1)));
			String schemaId = rs.getString(2);
			String schemaName = dblook.lookupSchemaId(schemaId);
			if (dblook.isIgnorableSchema(schemaName))
				continue;

			if (firstTime) {
				Logs.reportString("----------------------------------------------");
				Logs.reportMessage("CSLOOK_Header", "jars");
				Logs.reportMessage("CSLOOK_Jar_Note");
				Logs.reportString("----------------------------------------------\n");
			}

			String genID = rs.getString(3);

			String schemaWithoutQuotes = dblook.stripQuotes(schemaName);
			StringBuffer jarFullName = new StringBuffer(separator);
			jarFullName.append(dblook.stripQuotes(jarName));
			jarFullName.append(".jar.G");
			jarFullName.append(genID);

			StringBuffer oldJarPath = new StringBuffer();
			oldJarPath.append(dbName);
			oldJarPath.append(separator);
			oldJarPath.append("jar");
			oldJarPath.append(separator);
			oldJarPath.append(schemaWithoutQuotes);
			oldJarPath.append(jarFullName);

			// Copy jar file to CSJARS directory.
			String absJarDir = null;
			try {

				// Create the CSJARS directory.
				File jarDir = new File(System.getProperty("user.dir") +
					separator + "CSJARS" + separator + schemaWithoutQuotes);
				absJarDir = jarDir.getAbsolutePath();
				jarDir.mkdirs();

				// Create streams.
				FileInputStream oldJarFile =
					new FileInputStream(oldJarPath.toString());
				FileOutputStream newJarFile =
					new FileOutputStream(absJarDir + jarFullName);

				// Copy.
				int st = 0;
				while (true) {
					if (oldJarFile.available() == 0)
						break;
					byte[] bAr = new byte[oldJarFile.available()];
					oldJarFile.read(bAr);
					newJarFile.write(bAr);
				}

				newJarFile.close();
				oldJarFile.close();

			} catch (Exception e) {
				Logs.debug("CSLOOK_FailedToLoadJar",
					absJarDir + jarFullName.toString());
				Logs.debug(e);
				firstTime = false;
				continue;
			}

			// Now, add the DDL to read the jar from CSJARS.
			StringBuffer loadJarString = new StringBuffer();
			loadJarString.append("CALL SQLJ.INSTALL_JAR('file:");
			loadJarString.append(absJarDir);
			loadJarString.append(jarFullName);
			loadJarString.append("', '");
			loadJarString.append(schemaName);
			loadJarString.append(".");
			loadJarString.append(jarName);
			loadJarString.append("', 0)");

			Logs.writeToNewDDL(loadJarString.toString());
			Logs.writeStmtEndToNewDDL();
			Logs.writeNewlineToNewDDL();
			firstTime = false;

		}

		stmt.close();
		rs.close();

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2829.java
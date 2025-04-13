error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7980.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7980.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7980.java
text:
```scala
s@@f.getErrorCodes("Oracle").getCustomTranslations()[0];

/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.jdbc.support;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Arrays;

import javax.sql.DataSource;

import junit.framework.TestCase;

import org.easymock.MockControl;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Tests for SQLErrorCodes loading.
 *
 * @author Rod Johnson
 * @author Thomas Risberg
 */
public class SQLErrorCodesFactoryTests extends TestCase {

	/**
	 * Check that a default instance returns empty error codes for an unknown database.
	 */
	public void testDefaultInstanceWithNoSuchDatabase() {
		SQLErrorCodes sec = SQLErrorCodesFactory.getInstance().getErrorCodes("xx");
		assertTrue(sec.getBadSqlGrammarCodes().length == 0);
		assertTrue(sec.getDataIntegrityViolationCodes().length == 0);
	}

	/**
	 * Check that a known database produces recognizable codes.
	 */
	public void testDefaultInstanceWithOracle() {
		SQLErrorCodes sec = SQLErrorCodesFactory.getInstance().getErrorCodes("Oracle");
		assertIsOracle(sec);
	}

	private void assertIsOracle(SQLErrorCodes sec) {
		assertTrue(sec.getBadSqlGrammarCodes().length > 0);
		assertTrue(sec.getDataIntegrityViolationCodes().length > 0);
		// These had better be a Bad SQL Grammar code
		assertTrue(Arrays.binarySearch(sec.getBadSqlGrammarCodes(), "942") >= 0);
		assertTrue(Arrays.binarySearch(sec.getBadSqlGrammarCodes(), "6550") >= 0);
		// This had better NOT be
		assertFalse(Arrays.binarySearch(sec.getBadSqlGrammarCodes(), "9xx42") >= 0);
	}

	private void assertIsSQLServer(SQLErrorCodes sec) {
		assertThat(sec.getDatabaseProductName(), equalTo("Microsoft SQL Server"));

		assertTrue(sec.getBadSqlGrammarCodes().length > 0);

		assertTrue(Arrays.binarySearch(sec.getBadSqlGrammarCodes(), "156") >= 0);
		assertTrue(Arrays.binarySearch(sec.getBadSqlGrammarCodes(), "170") >= 0);
		assertTrue(Arrays.binarySearch(sec.getBadSqlGrammarCodes(), "207") >= 0);
		assertTrue(Arrays.binarySearch(sec.getBadSqlGrammarCodes(), "208") >= 0);
		assertTrue(Arrays.binarySearch(sec.getBadSqlGrammarCodes(), "209") >= 0);
		assertFalse(Arrays.binarySearch(sec.getBadSqlGrammarCodes(), "9xx42") >= 0);

		assertTrue(sec.getPermissionDeniedCodes().length > 0);
		assertTrue(Arrays.binarySearch(sec.getPermissionDeniedCodes(), "229") >= 0);

		assertTrue(sec.getDuplicateKeyCodes().length > 0);
		assertTrue(Arrays.binarySearch(sec.getDuplicateKeyCodes(), "2601") >= 0);
		assertTrue(Arrays.binarySearch(sec.getDuplicateKeyCodes(), "2627") >= 0);

		assertTrue(sec.getDataIntegrityViolationCodes().length > 0);
		assertTrue(Arrays.binarySearch(sec.getDataIntegrityViolationCodes(), "544") >= 0);
		assertTrue(Arrays.binarySearch(sec.getDataIntegrityViolationCodes(), "8114") >= 0);
		assertTrue(Arrays.binarySearch(sec.getDataIntegrityViolationCodes(), "8115") >= 0);

		assertTrue(sec.getDataAccessResourceFailureCodes().length > 0);
		assertTrue(Arrays.binarySearch(sec.getDataAccessResourceFailureCodes(), "4060") >= 0);

		assertTrue(sec.getCannotAcquireLockCodes().length > 0);
		assertTrue(Arrays.binarySearch(sec.getCannotAcquireLockCodes(), "1222") >= 0);

		assertTrue(sec.getDeadlockLoserCodes().length > 0);
		assertTrue(Arrays.binarySearch(sec.getDeadlockLoserCodes(), "1205") >= 0);
	}

	private void assertIsHsql(SQLErrorCodes sec) {
		assertTrue(sec.getBadSqlGrammarCodes().length > 0);
		assertTrue(sec.getDataIntegrityViolationCodes().length > 0);
		// This had better be a Bad SQL Grammar code
		assertTrue(Arrays.binarySearch(sec.getBadSqlGrammarCodes(), "-22") >= 0);
		// This had better NOT be
		assertFalse(Arrays.binarySearch(sec.getBadSqlGrammarCodes(), "-9") >= 0);
	}

	private void assertIsDB2(SQLErrorCodes sec) {
		assertTrue(sec.getBadSqlGrammarCodes().length > 0);
		assertTrue(sec.getDataIntegrityViolationCodes().length > 0);

		assertFalse(Arrays.binarySearch(sec.getBadSqlGrammarCodes(), "942") >= 0);
		// This had better NOT be
		assertTrue(Arrays.binarySearch(sec.getBadSqlGrammarCodes(), "-204") >= 0);
	}

	public void testLookupOrder() {
		class TestSQLErrorCodesFactory extends SQLErrorCodesFactory {
			private int lookups = 0;
			protected Resource loadResource(String path) {
				++lookups;
				if (lookups == 1) {
					assertEquals(SQLErrorCodesFactory.SQL_ERROR_CODE_DEFAULT_PATH, path);
					return null;
				}
				else {
					// Should have only one more lookup
					assertEquals(2, lookups);
					assertEquals(SQLErrorCodesFactory.SQL_ERROR_CODE_OVERRIDE_PATH, path);
					return null;
				}
			}
		}

		// Should have failed to load without error
		TestSQLErrorCodesFactory sf = new TestSQLErrorCodesFactory();
		assertTrue(sf.getErrorCodes("XX").getBadSqlGrammarCodes().length == 0);
		assertTrue(sf.getErrorCodes("Oracle").getDataIntegrityViolationCodes().length == 0);
	}

	/**
	 * Check that user defined error codes take precedence.
	 */
	public void testFindUserDefinedCodes() {
		class TestSQLErrorCodesFactory extends SQLErrorCodesFactory {
			protected Resource loadResource(String path) {
				if (SQLErrorCodesFactory.SQL_ERROR_CODE_OVERRIDE_PATH.equals(path)) {
					return new ClassPathResource("test-error-codes.xml", SQLErrorCodesFactoryTests.class);
				}
				return null;
			}
		}

		// Should have loaded without error
		TestSQLErrorCodesFactory sf = new TestSQLErrorCodesFactory();
		assertTrue(sf.getErrorCodes("XX").getBadSqlGrammarCodes().length == 0);
		assertEquals(2, sf.getErrorCodes("Oracle").getBadSqlGrammarCodes().length);
		assertEquals("1", sf.getErrorCodes("Oracle").getBadSqlGrammarCodes()[0]);
		assertEquals("2", sf.getErrorCodes("Oracle").getBadSqlGrammarCodes()[1]);
	}

	public void testInvalidUserDefinedCodeFormat() {
		class TestSQLErrorCodesFactory extends SQLErrorCodesFactory {
			protected Resource loadResource(String path) {
				if (SQLErrorCodesFactory.SQL_ERROR_CODE_OVERRIDE_PATH.equals(path)) {
					// Guaranteed to be on the classpath, but most certainly NOT XML
					return new ClassPathResource("SQLExceptionTranslator.class", SQLErrorCodesFactoryTests.class);
				}
				return null;
			}
		}

		// Should have failed to load without error
		TestSQLErrorCodesFactory sf = new TestSQLErrorCodesFactory();
		assertTrue(sf.getErrorCodes("XX").getBadSqlGrammarCodes().length == 0);
		assertEquals(0, sf.getErrorCodes("Oracle").getBadSqlGrammarCodes().length);
	}

	/**
	 * Check that custom error codes take precedence.
	 */
	public void testFindCustomCodes() {
		class TestSQLErrorCodesFactory extends SQLErrorCodesFactory {
			protected Resource loadResource(String path) {
				if (SQLErrorCodesFactory.SQL_ERROR_CODE_OVERRIDE_PATH.equals(path)) {
					return new ClassPathResource("custom-error-codes.xml", SQLErrorCodesFactoryTests.class);
				}
				return null;
			}
		}

		// Should have loaded without error
		TestSQLErrorCodesFactory sf = new TestSQLErrorCodesFactory();
		assertEquals(1, sf.getErrorCodes("Oracle").getCustomTranslations().length);
		CustomSQLErrorCodesTranslation translation =
				(CustomSQLErrorCodesTranslation) sf.getErrorCodes("Oracle").getCustomTranslations()[0];
		assertEquals(CustomErrorCodeException.class, translation.getExceptionClass());
		assertEquals(1, translation.getErrorCodes().length);
	}

	public void testDataSourceWithNullMetadata() throws Exception {

		MockControl ctrlConnection = MockControl.createControl(Connection.class);
		Connection mockConnection = (Connection) ctrlConnection.getMock();
		mockConnection.getMetaData();
		ctrlConnection.setReturnValue(null);
		mockConnection.close();
		ctrlConnection.setVoidCallable();
		ctrlConnection.replay();

		MockControl ctrlDataSource = MockControl.createControl(DataSource.class);
		DataSource mockDataSource = (DataSource) ctrlDataSource.getMock();
		mockDataSource.getConnection();
		ctrlDataSource.setDefaultReturnValue(mockConnection);
		ctrlDataSource.replay();

		SQLErrorCodes sec = SQLErrorCodesFactory.getInstance().getErrorCodes(mockDataSource);
		assertIsEmpty(sec);

		ctrlConnection.verify();
		ctrlDataSource.verify();
	}

	public void testGetFromDataSourceWithSQLException() throws Exception {

		SQLException expectedSQLException = new SQLException();

		MockControl ctrlDataSource = MockControl.createControl(DataSource.class);
		DataSource mockDataSource = (DataSource) ctrlDataSource.getMock();
		mockDataSource.getConnection();
		ctrlDataSource.setThrowable(expectedSQLException);
		ctrlDataSource.replay();

		SQLErrorCodes sec = SQLErrorCodesFactory.getInstance().getErrorCodes(mockDataSource);
		assertIsEmpty(sec);

		ctrlDataSource.verify();
	}

	private void assertIsEmpty(SQLErrorCodes sec) {
		// Codes should be empty
		assertEquals(0, sec.getBadSqlGrammarCodes().length);
		assertEquals(0, sec.getDataIntegrityViolationCodes().length);
	}

	private SQLErrorCodes getErrorCodesFromDataSource(String productName, SQLErrorCodesFactory factory) throws Exception {
		MockControl mdControl = MockControl.createControl(DatabaseMetaData.class);
		DatabaseMetaData md = (DatabaseMetaData) mdControl.getMock();
		md.getDatabaseProductName();
		mdControl.setReturnValue(productName);
		mdControl.replay();

		MockControl ctrlConnection = MockControl.createControl(Connection.class);
		Connection mockConnection = (Connection) ctrlConnection.getMock();
		mockConnection.getMetaData();
		ctrlConnection.setReturnValue(md);
		mockConnection.close();
		ctrlConnection.setVoidCallable();
		ctrlConnection.replay();

		MockControl ctrlDataSource = MockControl.createControl(DataSource.class);
		DataSource mockDataSource = (DataSource) ctrlDataSource.getMock();
		mockDataSource.getConnection();
		ctrlDataSource.setDefaultReturnValue(mockConnection);
		ctrlDataSource.replay();

		SQLErrorCodesFactory secf = null;
		if (factory != null) {
			secf = factory;
		}
		else {
			secf = SQLErrorCodesFactory.getInstance();
		}

		SQLErrorCodes sec = secf.getErrorCodes(mockDataSource);

		mdControl.verify();
		ctrlConnection.verify();
		ctrlDataSource.verify();

		SQLErrorCodes sec2 = secf.getErrorCodes(mockDataSource);
		assertSame("Cached per DataSource", sec2, sec);

		return sec;
	}

	public void testSQLServerRecognizedFromMetadata() throws Exception {
		SQLErrorCodes sec = getErrorCodesFromDataSource("MS-SQL", null);
		assertIsSQLServer(sec);
	}

	public void testOracleRecognizedFromMetadata() throws Exception {
		SQLErrorCodes sec = getErrorCodesFromDataSource("Oracle", null);
		assertIsOracle(sec);
	}

	public void testHsqlRecognizedFromMetadata() throws Exception {
		SQLErrorCodes sec = getErrorCodesFromDataSource("HSQL Database Engine", null);
		assertIsHsql(sec);
	}

	public void testDB2RecognizedFromMetadata() throws Exception {
		SQLErrorCodes sec = getErrorCodesFromDataSource("DB2", null);
		assertIsDB2(sec);
		sec = getErrorCodesFromDataSource("DB2/", null);
		assertIsDB2(sec);
		sec = getErrorCodesFromDataSource("DB-2", null);
		assertIsEmpty(sec);
	}

	/**
	 * Check that wild card database name works.
	 */
	public void testWildCardNameRecognized() throws Exception {
		class WildcardSQLErrorCodesFactory extends SQLErrorCodesFactory {
			protected Resource loadResource(String path) {
				if (SQLErrorCodesFactory.SQL_ERROR_CODE_OVERRIDE_PATH.equals(path)) {
					return new ClassPathResource("wildcard-error-codes.xml", SQLErrorCodesFactoryTests.class);
				}
				return null;
			}
		}

		WildcardSQLErrorCodesFactory factory = new WildcardSQLErrorCodesFactory();
		SQLErrorCodes sec = getErrorCodesFromDataSource("DB2", factory);
		assertIsDB2(sec);
		sec = getErrorCodesFromDataSource("DB2 UDB for Xxxxx", factory);
		assertIsDB2(sec);

		sec = getErrorCodesFromDataSource("DB3", factory);
		assertIsDB2(sec);
		sec = getErrorCodesFromDataSource("DB3/", factory);
		assertIsDB2(sec);
		sec = getErrorCodesFromDataSource("/DB3", factory);
		assertIsDB2(sec);
		sec = getErrorCodesFromDataSource("/DB3", factory);
		assertIsDB2(sec);
		sec = getErrorCodesFromDataSource("/DB3/", factory);
		assertIsDB2(sec);
		sec = getErrorCodesFromDataSource("DB-3", factory);
		assertIsEmpty(sec);

		sec = getErrorCodesFromDataSource("DB1", factory);
		assertIsDB2(sec);
		sec = getErrorCodesFromDataSource("DB1/", factory);
		assertIsDB2(sec);
		sec = getErrorCodesFromDataSource("/DB1", factory);
		assertIsEmpty(sec);
		sec = getErrorCodesFromDataSource("/DB1/", factory);
		assertIsEmpty(sec);

		sec = getErrorCodesFromDataSource("DB0", factory);
		assertIsDB2(sec);
		sec = getErrorCodesFromDataSource("/DB0", factory);
		assertIsDB2(sec);
		sec = getErrorCodesFromDataSource("DB0/", factory);
		assertIsEmpty(sec);
		sec = getErrorCodesFromDataSource("/DB0/", factory);
		assertIsEmpty(sec);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7980.java
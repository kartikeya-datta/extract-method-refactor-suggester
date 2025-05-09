error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1010.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1010.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1010.java
text:
```scala
i@@f (e.getMessage() == null) {

/*
 
 Derby - Class org.apache.derbyTesting.system.nstest.utils.DbUtil
 
 Licensed to the Apache Software Foundation (ASF) under one or more
 contributor license agreements.  See the NOTICE file distributed with
 this work for additional information regarding copyright ownership.
 The ASF licenses this file to You under the Apache License, Version 2.0
 (the "License"); you may not use this file except in compliance with
 the License.  You may obtain a copy of the License at
 
 http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 
 */

package org.apache.derbyTesting.system.nstest.utils;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Random;

import org.apache.derbyTesting.system.nstest.NsTest;

/**
 * DbUtil - a database utility class for all IUD and Select operations
 */
public class DbUtil {
	private String threadName = "";
	
	public static final int TCHAR = 0;
	
	public static final int TDATE = 1;
	
	public static final int TDECIMAL = 2;
	
	public static final int TDECIMALNN = 3;
	
	public static final int TDOUBLE = 4;
	
	public static final int TFLOAT = 5;
	
	public static final int TINT = 6;
	
	public static final int TLONGINT = 7;
	
	public static final int TNUMERICLARGE = 8;
	
	public static final int TREAL = 9;
	
	public static final int TSMALLINT = 10;
	
	public static final int TTIME = 11;
	
	public static final int TTIMESTAMP = 12;
	
	public static final int TVARCHAR = 13;
	
	public static final int NUMTYPES = 14;
	
	public static String[] colnames = { "t_char", "t_date", "t_decimal",
		"t_decimal_nn", "t_double", "t_float", "t_int", "t_longint",
		"t_numeric_large", "t_real", "t_smallint", "t_time", "t_timestamp",
	"t_varchar" };
	
	public DbUtil(String thName) {
		threadName = thName;
	}
	
	/*
	 * Add a row for each iteration
	 */
	
	public int add_one_row(Connection conn, String thread_id) throws Exception {
		
		PreparedStatement ps = null;
		int rowsAdded = 0;
		
		try {
			// autoincrement feature added, so we need to specify the column
			// name
			// for prepared statement, otherwise auto increment column will
			// think
			// it is trying to update/insert a null value to the column.
			
			ps = conn
			.prepareStatement(" insert into nstesttab (id, t_char,"
					+ " t_date, t_decimal, t_decimal_nn, t_double, "
					+ " t_float, t_int, t_longint, t_numeric_large,"
					+ " t_real, t_smallint, t_time, t_timestamp,"
					+ " t_varchar,t_clob,t_blob) values ("
					+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,cast('00000000000000000000000000000000031' as clob(1K)),cast(X'000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000031' as blob(10K)))");
			
			Random rand = new Random();
			
			int ind = rand.nextInt();
			int id_ind = ind;
			
			Date dt = new Date(1);
			Time tt = new Time(1);
			Timestamp ts = new Timestamp(1);
			String cs = "asdf qwerqwer 12341234 ZXCVZXCVZXCV !@#$!@#$ asdfasdf 1 q a z asdf ASDF qwerasdfzxcvasdfqwer1234asd#";
			
			// Integer ji = null;
			
			// Set value of column "id"
			ps.setInt(1, ind);
			// System.out.println("set int col 1 to " + ind);
			
			// Set value of column "t_char"
			// scramble the string
			int i1 = Math.abs(ind % 100);
			String cs2 = cs.substring(i1, 99) + cs.substring(0, i1);
			int i2 = i1 < 89 ? i1 + 10 : i1;
			ps.setString(2, cs2.substring(0, i2));
			// System.out.println("set t_Char to " + cs2.substring(0,i2));
			
			// System.out.println("now setting date");
			// Set value of column "t_date"
			dt.setTime(Math.abs(rand.nextLong() / 150000));
			ps.setDate(3, dt);
			// System.out.println("set t_date to " + dt.toString());
			
			// Set value of column "t_decimal"
			// double t_dec = rand.nextDouble() *
			// Math.pow(10,Math.abs(rand.nextInt()%18));
			// double t_dec = rand.nextDouble();
			double t_dec = rand.nextDouble()
			* Math.pow(10, Math.abs(rand.nextInt() % 6));
			ps.setDouble(4, t_dec);
			// System.out.println("set t_decimal to "+ t_dec);
			
			// Set value of column "t_decimal_nn"
			double t_dec_nn = rand.nextDouble();
			ps.setDouble(5, t_dec_nn);
			// System.out.println("set t_decimal_nn " + t_dec_nn);
			
			// Set value of column "t_double"
			double t_doub = rand.nextDouble()
			* Math.pow(10, Math.abs(rand.nextInt() % 300));
			ps.setDouble(6, t_doub);
			// System.out.println("set t_double to "+ t_doub);
			
			// Set value of column "t_float"
			float t_flt = rand.nextFloat()
			* (float) Math.pow(10, Math.abs(rand.nextInt() % 30));
			ps.setFloat(7, t_flt);
			// System.out.println("set t_float to " + t_flt);
			
			// Set value of column "t_int"
			int t_intval = rand.nextInt();
			ps.setInt(8, t_intval);
			// System.out.println("set t_int to " + t_intval);
			
			// Set value of column "t_longint"
			long t_longval = rand.nextLong();
			ps.setLong(9, t_longval);
			// System.out.println("set t_longint " + t_longval);
			
			// Set value of column "t_numeric_large"
			double t_num_lrg = rand.nextDouble()
			* Math.pow(10, Math.abs(rand.nextInt() % 20));
			ps.setDouble(10, t_num_lrg);
			// System.out.println("set t_numeric large to " + t_num_lrg);
			
			// Set value of column "t_real"
			float t_fltval = rand.nextFloat()
			* (float) Math.pow(10, Math.abs(rand.nextInt() % 7));
			ps.setFloat(11, t_fltval);
			// System.out.println("set t_real to " + t_fltval);
			
			// Set value of column "t_smallint"
			int t_smlint = rand.nextInt() % (256 * 128);
			ps.setInt(12, t_smlint);
			// System.out.println("set t_smallint to " + t_smlint);
			
			// Set value of column "t_time"
			tt.setTime(Math.abs(rand.nextInt()));
			ps.setTime(13, tt);
			// System.out.println("set t_time to " + tt.toString());
			
			// Set value of column "t_timestamp"
			ts.setTime(Math.abs(rand.nextLong() / 50000));
			ps.setTimestamp(14, ts);
			// System.out.println("set t_timestamp to " + ts.toString());
			
			// Set value of column "t_varchar"
			ps.setString(15, cs.substring(Math.abs(rand.nextInt() % 100)));
			// System.out.println("set t_varchar, now executing update stmt");
			try {
				rowsAdded = ps.executeUpdate();
			} catch (SQLException sqe) {
				if (sqe.getSQLState().equalsIgnoreCase("40XL1")) {
					System.out
					.println("LOCK TIMEOUT obatained during insert - add_one_row() "
							+ sqe.getSQLState());
					
				} else {
					throw sqe;
				}
				
			}
			if (rowsAdded == 1) {
				System.out.println(thread_id + " inserted 1 row with id "
						+ id_ind + NsTest.SUCCESS);
				
			} else
				System.out.println("FAIL: " + thread_id + " insert failed");
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out
			.println("Exception when preparing or executing insert prepared stmt");
			printException("executing/preparing insert stmt in dbUtil", e);
			e.printStackTrace();
			// ps.close();
		}
		
	
		return rowsAdded;
	}
	
	/*
	 * Update a random row. This method is common to all the worker threads
	 */
	
	public int update_one_row(Connection conn, String thread_id)
	throws Exception {
		
		PreparedStatement ps2 = null;
		String column = null;
		int ind = 0;
		Random rand = new Random();
		int rowsUpdated = 0;
		conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		long skey = pick_one(conn, thread_id);
		if (skey == 0) { // means we did not find a row
			System.out.println(thread_id
					+ " could not find a row to update or there was an error.");
			return rowsUpdated;
		}
		
		ind = Math.abs(rand.nextInt());
		
		column = colnames[ind % NUMTYPES]; // randomly gets one of the columns
		// of the table
		
		try {
			
			ps2 = conn.prepareStatement(" update nstesttab set " + column
					+ " = ? " + " where serialkey = " + skey);
			
		} catch (Exception e) {
			printException(
					"closing update prepared stmt in dbUtil.update_one_row() ",
					e);
			return rowsUpdated;
		}
		
		String ds2 = null;
		String cs = "asdf qwerqwer 12341234 ZXCVZXCVZXCV !@#$!@#$ asdfasdf 1 q a z asdf ASDF qwerasdfzxcvasdfqwer1234asd#";
		double d = 0.0;
		float f = 0;
		int type = (ind % NUMTYPES);
		
		switch (type) {
		
		case TCHAR:
			ds2 = cs.substring(Math.abs(rand.nextInt() % 100));
			ps2.setString(1, ds2);
			break;
			
		case TDATE:
			Date dt = new Date(1);
			dt.setTime(Math.abs(rand.nextLong() / 150000));
			dt.setTime(Math.abs(rand.nextLong() / 150000));
			ps2.setDate(1, dt);
			ds2 = dt.toString();
			break;
			
		case TDECIMAL:
			d = rand.nextDouble() * Math.pow(10, rand.nextInt() % 18);
			ps2.setDouble(1, d);
			ds2 = String.valueOf(d);
			break;
			
		case TDECIMALNN:
			d = rand.nextDouble();
			ps2.setDouble(1, d);
			ds2 = String.valueOf(d);
			break;
			
		case TDOUBLE:
			d = rand.nextDouble() * Math.pow(10, rand.nextInt() % 300);
			ps2.setDouble(1, d);
			ds2 = String.valueOf(d);
			break;
			
		case TFLOAT:
			f = rand.nextFloat() * (float) Math.pow(10, rand.nextInt() % 30);
			ps2.setFloat(1, f);
			ds2 = String.valueOf(f);
			break;
			
		case TINT:
			int i = rand.nextInt();
			ds2 = String.valueOf(i);
			ps2.setInt(1, i);
			break;
			
		case TLONGINT:
			long l = rand.nextLong();
			ds2 = String.valueOf(l);
			ps2.setLong(1, l);
			break;
			
		case TNUMERICLARGE:
			d = rand.nextDouble() * Math.pow(10, rand.nextInt() % 20);
			ps2.setDouble(1, d);
			ds2 = String.valueOf(d);
			break;
			
		case TREAL:
			f = rand.nextFloat() * (float) Math.pow(10, rand.nextInt() % 7);
			ps2.setFloat(1, f);
			ds2 = String.valueOf(f);
			break;
			
		case TSMALLINT:
			i = rand.nextInt() % (256 * 128);
			short si = (short) i;
			ps2.setShort(1, si);
			ds2 = String.valueOf(si);
			break;
			
		case TTIME:
			Time tt = new Time(1);
			tt.setTime(Math.abs(rand.nextInt()));
			ps2.setTime(1, tt);
			ds2 = tt.toString();
			break;
			
		case TTIMESTAMP:
			Timestamp ts = new Timestamp(1);
			ts.setTime(Math.abs(rand.nextLong() / 50000));
			ps2.setTimestamp(1, ts);
			ds2 = ts.toString();
			break;
			
		case TVARCHAR:
			ds2 = cs.substring(Math.abs(rand.nextInt() % 100));
			ps2.setString(1, ds2);
			break;
			
		} // end of switch(type)
		
		System.out.println(thread_id + " attempting  to update col " + column
				+ " to " + ds2);
		try {
			rowsUpdated = ps2.executeUpdate();
		} catch (SQLException sqe) {
			System.out.println(sqe.getSQLState() + " " + sqe.getErrorCode()
					+ " " + sqe.getMessage());
			sqe.printStackTrace();
		} catch (Exception e) {
			printException("Error in update_one_row()", e);
			e.printStackTrace();
		} finally {
			conn
			.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
		}
		
		if (rowsUpdated > 0)
			System.out.println(thread_id + " updated " + rowsUpdated
					+ " row with serialkey " + skey + NsTest.SUCCESS);
		else
			System.out
			.println(thread_id + " update failed, no such row exists");
		
	
		return rowsUpdated;
	}
	
	//
	// Delete one row from the table. The row to be deleted is chosen randomly
	// using the
	// pick_one method which randomly returns a number between the max of
	// serialkey and
	// the minimum serialkey value that is untouched (nstest.NUM_UNTOUCHED_ROWS)
	//
	public int delete_one_row(Connection conn, String thread_id)
	throws Exception {
		
		PreparedStatement ps = null;
		int rowsDeleted = 0;
		conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		try {
			
			ps = conn
			.prepareStatement(" delete from nstesttab where serialkey = ?");
		} catch (Exception e) {
			System.out
			.println("Unexpected error preparing the statement in delete_one()");
			printException("delete_one_row prepare ", e);
			return rowsDeleted;
		}
		
		long skey = pick_one(conn, thread_id);
		System.out.println(thread_id
				+ " attempting  to delete a row with serialkey = " + skey);
		if (skey == 0) { // means we did not find a row
			System.out.println(thread_id
					+ " could not find a row to delete or there was an error.");
			return rowsDeleted;
		}
		
		try {
			ps.setLong(1, skey);
			rowsDeleted = ps.executeUpdate();
		} catch (Exception e) {
			System.out
			.println("Error in delete_one(): either with setLong() or executeUpdate");
			printException("failure to execute delete stmt", e);
		} finally {
			conn
			.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			// set it back to read uncommitted
		}
		
		if (rowsDeleted > 0)
			System.out.println(thread_id + " deleted row with serialkey "
					+ skey + NsTest.SUCCESS);
		else
			System.out.println(thread_id + " delete for serialkey " + skey
					+ " failed, no such row exists.");
		
		return rowsDeleted;
	}// end of method delete_one()
	
	//
	// get a random serialkey value that matches the criteria:
	// - should not be one of the "protected" rows (set by
	// nstest.NUM_UNTOUCHED_ROWS)
	// - should be less than the current value of the max(serialkey)
	//
	public long pick_one(Connection conn, String thread_id) throws Exception {
		
		PreparedStatement ps = null;
		// ResultSet rs = null;
		
		Random rand = new Random();
		
		try {
			
			ps = conn
			.prepareStatement("select max(serialkey) from nstesttab where serialkey > ?");
		} catch (Exception e) {
			System.out
			.println("Unexpected error creating the select prepared statement in pick_one()");
			printException("failure to prepare select stmt in pick_one()", e);
			return (0);
		}
		
		long minVal = NsTest.NUM_UNTOUCHED_ROWS + 1;
		// long maxVal = nstest.MAX_INITIAL_ROWS * nstest.INIT_THREADS; //the
		// max we start with
		long maxVal = NsTest.numInserts;// this is an almost accurate count of
		// the max serialkey
		// since it keeps a count of the num of inserts made so far
		
		// Now choose a random value between minVal and maxVal. We use this
		// value even if
		// the row does not exist (i.e. in a situation where some other thread
		// has deleted this row).
		// The test should just complain and exit with a row not found exception
		long rowToReturn = (minVal + 1)
		+ (Math.abs(rand.nextLong()) % (maxVal - minVal));
		try {
			ps.setLong(1, rowToReturn);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				if (rs.getLong(1) > 0) {
					rowToReturn = rs.getLong(1);
					System.out
					.println(getThreadName()
							+ " dbutil.pick_one() -> Obtained row from the table "
							+ rowToReturn);
				} else {
					System.out
					.println(getThreadName()
							+ " dbutil.pick_one() -> Returning random serialkey of "
							+ rowToReturn);
				}
			}
		} catch (SQLException sqe) {
			System.out.println(sqe + " while selecting a random row");
			sqe.printStackTrace();
		}
		
	
		
		return rowToReturn;
		
	}//of method pick_one(...)
	
	// ** This method abstracts exception message printing for all exception
	// messages. You may want to change
	// ****it if more detailed exception messages are desired.
	// ***Method is synchronized so that the output file will contain sensible
	// stack traces that are not
	// ****mixed but rather one exception printed at a time
	public synchronized void printException(String where, Exception e) {
		System.out.println(e.toString());
		if (e instanceof SQLException) {
			SQLException se = (SQLException) e;
			
			if (se.getSQLState().equals("40001"))
				System.out.println(getThreadName()
						+ " dbUtil --> deadlocked detected");
			if (se.getSQLState().equals("40XL1"))
				System.out.println(getThreadName()
						+ " dbUtil --> lock timeout exception");
			if (se.getSQLState().equals("23500"))
				System.out.println(getThreadName()
						+ " dbUtil --> duplicate key violation");
			if (se.getNextException() != null) {
				String m = se.getNextException().getSQLState();
				System.out.println(se.getNextException().getMessage()
						+ " SQLSTATE: " + m);
				System.out.println(getThreadName()
						+ " dbUtil ---> Details of exception: " + se.toString()
						+ " " + se.getErrorCode());
			}
		}
		if (e.getMessage().equals(null)) {
			System.out.println(getThreadName()
					+ " dbUtil --> NULL error message detected");
			System.out
			.println(getThreadName()
					+ " dbUtil --> Here is the NULL exection - "
					+ e.toString());
			System.out.println(getThreadName()
					+ " dbUtil --> Stack trace of the NULL exception - ");
			e.printStackTrace(System.out);
		}
		System.out.println(getThreadName() + " dbUtil ----> During " + where
				+ ", exception thrown was : " + e.toString());
	}
	
	public String getThreadName() {
		return threadName;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1010.java
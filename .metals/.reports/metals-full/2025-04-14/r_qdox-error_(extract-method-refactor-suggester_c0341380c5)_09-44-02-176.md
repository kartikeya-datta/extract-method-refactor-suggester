error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2138.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2138.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2138.java
text:
```scala
i@@tems[orderItemCount - 1] = 2334432;

/*
 *
 * Derby - Class org.apache.derbyTesting.system.oe.client.Submitter
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on an 
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific 
 * language governing permissions and limitations under the License.
 */
package org.apache.derbyTesting.system.oe.client;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import org.apache.derbyTesting.system.oe.util.OERandom;

/**
 * Class that submits Order Entry transactions
 * to a database through an instance of an Operations class.
 * This class is responsible for the mix of transactions
 * and the generation of the random input values.
 * 
 * Sub-classes can override the mix.
 */
public class Submitter {
    
    /**
     * Offset of Stock Level transaction in returned
     * arrays with run information.
     */
    public static final int STOCK_LEVEL = 0;
    /**
     * Offset of Order Status by Name transaction in returned
     * arrays with run information.
     */    
    public static final int ORDER_STATUS_BY_NAME = 1;
    /**
     * Offset of Order Status by Id transaction in returned
     * arrays with run information.
     */
    public static final int ORDER_STATUS_BY_ID = 2;
    
     /**
      * Offset of Payment by Name transaction in returned
      * arrays with run information.
      */
    public static final int PAYMENT_BY_NAME = 3;
    
    /**
     * Offset of Payement by ID transaction in returned
     * arrays with run information.
     */
    public static final int PAYMENT_BY_ID = 4;
    
    /**
     * Offset of Delivery transaction in returned
     * arrays with run information.
     */
    public static final int DELIVERY_SCHEDULE = 5;
    
    /**
     * Offset of New Order transaction in returned
     * arrays with run information.
     */
    public static final int NEW_ORDER = 6;
    
    /**
     * Offset of New Order transaction that rolled back in returned
     * arrays with run information.
     */
    public static final int NEW_ORDER_ROLLBACK = 7;
    
    /**
     * Display to write the output to.
     */
    private final Display display;
    
    /**
     * How the business transactions are implemented.
     */
    private final Operations ops;
    
    /**
     * My own random number generator.
     */
    private final OERandom rand;
    
    /**
     * Scale of the database.
     */
    private final short maxW;
    
    /**
     * Record of how many transactions are implemented.
     */
    private final int[] transactionCount;
    
    /**
     * Generate a new random number generator
     * that follows the rules according to 2.1.6.1
     * @param conn
     * @return rand number generator
     * @throws SQLException
     */
    public static OERandom getRuntimeRandom(Connection conn)
        throws SQLException
    {
        OERandom rand = new OERandom(-1);
        
        ResultSet rs = conn.createStatement().executeQuery(
                "SELECT CLOAD FROM C");
        rs.next();
        int cload = rs.getInt(1);
        rs.close();
        
        for (;;)
        {
            int c = rand.randomInt(0, 255);
            int delta = Math.abs(cload - c);
            if (delta == 96 || delta == 112)
                continue;
            if (delta < 65 || delta > 119)
                continue;
            
            rand = new OERandom(c);
            break;
        }
        
        return rand;
    }
    
    /**
     * Return a Submitter than only executes stock level transactions.
     */
    public static Submitter stockLevelOnly(Display display,
    		Operations ops, OERandom rand,
            short maxW)
    {
    	return new Submitter(display, ops, rand, maxW)
		{
			protected int mixType(final int chooseType)
			{
				return Submitter.STOCK_LEVEL;
			}
		};  	
    }
    /**
     * Return a Submitter than only executes order
     * status by identifier transactions.
     */
    public static Submitter orderStatusByIdOnly(Display display,
    		Operations ops, OERandom rand,
            short maxW)
    {
    	return new Submitter(display, ops, rand, maxW)
		{
			protected int mixType(final int chooseType)
			{
				return Submitter.ORDER_STATUS_BY_ID;
			}
		};  	
    }
    /**
     * Return a Submitter than only executes order
     * status by name transactions.
     */
    public static Submitter orderStatusByNameOnly(Display display,
    		Operations ops, OERandom rand,
            short maxW)
    {
    	return new Submitter(display, ops, rand, maxW)
		{
			protected int mixType(final int chooseType)
			{
				return Submitter.ORDER_STATUS_BY_NAME;
			}
		};  	
    }    
    /**
     * Return a Submitter than only executes payment
     * by identifier transactions.
     */
    public static Submitter paymentByIdOnly(Display display,
    		Operations ops, OERandom rand,
            short maxW)
    {
    	return new Submitter(display, ops, rand, maxW)
		{
			protected int mixType(final int chooseType)
			{
				return Submitter.PAYMENT_BY_ID;
			}
		};  	
    }
    /**
     * Return a Submitter than only executes payment
     * by name transactions.
     */
    public static Submitter paymentByNameOnly(Display display,
    		Operations ops, OERandom rand,
            short maxW)
    {
    	return new Submitter(display, ops, rand, maxW)
		{
			protected int mixType(final int chooseType)
			{
				return Submitter.PAYMENT_BY_NAME;
			}
		};  	
    }    
    /**
     * Return a Submitter than only executes new order
     * transactions with no rollback
     */
    public static Submitter newOrderOnly(Display display,
    		Operations ops, OERandom rand,
            short maxW)
    {
    	return new Submitter(display, ops, rand, maxW)
		{
			protected int mixType(final int chooseType)
			{
				return Submitter.NEW_ORDER;
			}
		};  	
    }        
    
    /**
     * Create a submitter that has a fixed mix of transactions
     * at input time.
     * 
     * @see Submitter#mixType(int)
     */
    public Submitter(Display display, Operations ops, OERandom rand,
            short maxW)
    {
        this.display = display;
        this.ops = ops;
        this.rand = rand;
        this.maxW = maxW;
        
        transactionCount = new int[NEW_ORDER_ROLLBACK+1];
    }
    
    /**
     * Reset the transaction counts to zero.
     */
    public void clearTransactionCount()
    {
        Arrays.fill(transactionCount, 0);
    }
    
    /**
     * Run a fixed number of transactions returning the
     * time in milli-seconds required to execute all of them.
     * @param displayData Passed onto Display calls
     * @param count Number of transactions to run
     * @return Elapsed time in ms to run count transactions
     * @throws Exception
     */
    public long runTransactions(final Object displayData, final int count)
    throws Exception
    {
        long startms = System.currentTimeMillis();
        for (int i = 0; i < count; i++)
            runTransaction(displayData);
        long endms = System.currentTimeMillis();
        
        return endms - startms;
    }
    
    /**
     * Run an order entry transaction picking the specific
     * transaction at random with a hard-coded mix.
     * @param displayData Passed onto Display calls
     * @throws Exception Error executing the transaction
     */
    public void runTransaction(final Object displayData) throws Exception
    {       
        int chooseType = rand.randomInt(1, 100);
        
        int type = mixType(chooseType);
        
        switch (type)
        {
        case Submitter.DELIVERY_SCHEDULE:
            runScheduleDelivery(displayData);
            break;
        case Submitter.NEW_ORDER:
            runNewOrder(displayData, false);
            break;
        case Submitter.NEW_ORDER_ROLLBACK:
            runNewOrder(displayData, true);
            break;
        case Submitter.ORDER_STATUS_BY_ID:
            runOrderStatus(displayData, false);
            break;
        case Submitter.ORDER_STATUS_BY_NAME:
            runOrderStatus(displayData, true);
            break;
        case Submitter.PAYMENT_BY_ID:
            runPayment(displayData, false);
            break;
        case Submitter.PAYMENT_BY_NAME:
            runPayment(displayData, true);
            break;
        case Submitter.STOCK_LEVEL:
            runStockLevel(displayData);
            break;
        }
        
        transactionCount[type]++;
    }
    
    /**
     * Return one of transaction constants to run that transaction.
     * This mix in not correct for TPC-C specification.
     * With the official spec the final mix of transactions
     * must match a certain profile. With this setup the
     * mix is fixed upon input following the TPC-C final ratios.
     * This will give approximately correct results, but the final
     * mix will not be in line with TPC-C rules. This is because
     * different transactions have different execution times.
     * @param chooseType Random number between 1 and 100 inclusive.
     * @return A transaction constant from this class.
     */
    protected int mixType(final int chooseType)
    {
        if (chooseType <= 43)
        {
            boolean byName = rand.randomInt(1, 100) <= 60;
            return byName ?
                Submitter.PAYMENT_BY_NAME : Submitter.PAYMENT_BY_ID;       
        }
        else if (chooseType <= (43 + 4))
        {
            boolean byName = rand.randomInt(1, 100) <= 60;
            return byName ?
                Submitter.ORDER_STATUS_BY_NAME : Submitter.ORDER_STATUS_BY_ID;
        }
        else if (chooseType <= (43 + 4 + 4))
            return Submitter.DELIVERY_SCHEDULE;
        else if (chooseType <= (43 + 4 + 4 + 4))
            return Submitter.STOCK_LEVEL;
        else
        {
            // 1% rollback
            boolean rollback = rand.randomInt(1, 100) == 1;
            return rollback ?
               Submitter.NEW_ORDER_ROLLBACK : Submitter.NEW_ORDER;
        }
    }
    
    protected void runNewOrder(Object displayData, boolean forRollback)
        throws Exception
   {
    	short homeWarehouse = warehouse();
    	
    	final int orderItemCount = rand.randomInt(5, 15);
    	
    	int[] items = new int[orderItemCount];
    	short[] quantities = new short[orderItemCount];
    	short[] supplyW = new short[orderItemCount];
    	
    	for (int i = 0; i < orderItemCount; i++)
    	{
    		// Section 2.4.1.5
    		
    		// 1)
    		items[i] = rand.NURand8191();
    		
    		// 2)
    		if (maxW == 1 || rand.randomInt(1, 100) > 1)
    		{
    			supplyW[i] = homeWarehouse;
    		}
    		else
    		{
    			short sw = warehouse();
    			while (sw == homeWarehouse)
    				sw = warehouse();
    			supplyW[i] = sw;
    		}
    		supplyW[i] = rand.randomInt(1, 100) > 1 ?
    				homeWarehouse : warehouse();
    		
    		// 3) 
    		quantities[i] = (short) rand.randomInt(1, 10);
    	}
    	
    	// Section 2.4.1.4
    	if (forRollback)
    	{
    		items[orderItemCount] = 2334432;
    	}
    	
        ops.newOrder(display, displayData,
        		homeWarehouse, rand.district(),
        		rand.NURand1023(),
        		items, quantities, supplyW);
        		
    }

    protected void runScheduleDelivery(Object displayData) {
        // TODO Auto-generated method stub
    }

    /**
     * Run a payment transaction with random input values.
     */
    protected void runPayment(Object displayData,
            boolean byName) throws Exception {
        
        if (byName)
        {
            ops.payment(display, displayData,
                    warehouse(), rand.district(),
                    warehouse(), rand.district(),
                    rand.randomCLast(), rand.payment().toString());
        }
        else
        {
            ops.payment(display, displayData,
                warehouse(), rand.district(),
                warehouse(), rand.district(),
                rand.NURand1023(), rand.payment().toString());
        }
     }

    /**
     * Return a random warehouse
     * @return a random warehouse
     */
    private final short warehouse() {
    	if (maxW == 1)
    		return 1;
        return (short) rand.randomInt(1, maxW);
    }

    /**
     * Run a stock level transaction with random input values.
     */
    protected void runStockLevel(Object displayData) throws Exception
    {
        ops.stockLevel(display, displayData,
                warehouse(), rand.district(), rand.threshold());
    }
    
    /**
     * Run an order status transaction with random input values.
     */
    protected void runOrderStatus(Object displayData, boolean byName) throws Exception {

        if (byName)
        {
            ops.orderStatus(display, displayData,
                    warehouse(), rand.district(), rand.randomCLast());
        }
        else
        {
            ops.orderStatus(display, displayData,
                warehouse(), rand.district(), rand.NURand1023());
        }

    }
    
    /**
     * Print a simple report of the activity.
     * @param out
     */
    public void printReport(PrintStream out) {
             
        int total = 0;
        for (int i = 0; i < transactionCount.length; i++)
            total += transactionCount[i];
        
        out.println("Total Transactions: " + total);
        
        int noTotal = transactionCount[NEW_ORDER] +
            transactionCount[NEW_ORDER_ROLLBACK];       
        int pyCount = transactionCount[PAYMENT_BY_NAME] +
            transactionCount[PAYMENT_BY_ID];
        int osCount = transactionCount[ORDER_STATUS_BY_NAME] +
            transactionCount[ORDER_STATUS_BY_ID];

        if (noTotal != 0)
            out.println(transactionCount("New Order         ", noTotal, total));
        
        if (pyCount != 0) {
            out.println(transactionCount("Payment           ",  pyCount, total));
            out.println(transactionCount("    By Name       ",  transactionCount[PAYMENT_BY_NAME], total));
            out.println(transactionCount("    By Identifier ",  transactionCount[PAYMENT_BY_ID], total));
        }
        
        if (osCount != 0) {
            out.println(transactionCount("Order Status      ",  osCount, total));
            out.println(transactionCount("    By Name       ",  transactionCount[ORDER_STATUS_BY_NAME], total));
            out.println(transactionCount("    By Identifier ",  transactionCount[ORDER_STATUS_BY_ID], total));
        }
        
        if (transactionCount[STOCK_LEVEL] != 0)
            out.println(transactionCount("Stock Level       ", 
                transactionCount[STOCK_LEVEL], total));
        
        if (transactionCount[DELIVERY_SCHEDULE] != 0)
            out.println(transactionCount("Schedule Delivery ", 
                transactionCount[DELIVERY_SCHEDULE], total));
    }
    
    private String transactionCount(String name, int count, int total) 
    {
        return name + " : " + percent(count, total) +
           "(" + count + ")" ;
        
    }
    
    private String percent(int count, int total)
    {
        BigDecimal c = new BigDecimal((long) count * 100L);
        BigDecimal t = new BigDecimal((long) total);
        
        BigDecimal p = c.divide(t, 2, BigDecimal.ROUND_DOWN);
        
        return p.toString().concat("%");
    }

    /**
     * Get the executed transaction counts.
     * 
     * @return transactionCount
     */
    public int[] getTransactionCount() {
        return transactionCount;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2138.java
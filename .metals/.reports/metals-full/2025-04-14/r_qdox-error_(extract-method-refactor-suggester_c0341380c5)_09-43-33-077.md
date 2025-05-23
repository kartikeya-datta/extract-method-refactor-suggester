error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3259.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3259.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3259.java
text:
```scala
r@@es = ResourceBundle.getBundle(messageFileName);

/*

   Derby - Class org.apache.derby.iapi.tools.i18n.LocalizedResource

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
package org.apache.derby.iapi.tools.i18n;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;

import java.util.ResourceBundle;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.DecimalFormat;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.FieldPosition;

import java.sql.Timestamp;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;


public final class LocalizedResource  implements java.security.PrivilegedAction {

	private static final boolean SUPPORTS_BIG_DECIMAL_CALLS;
	
	static {
		boolean supportsBigDecimalCalls;
		try {
			// This class attempts to make a call to a 
			// ResultSet.getBigDecimal method, which may not be available.
			// For instance, java.math.BigDecimal is not available with
			// J2ME/CDC/Foundation 1.0 profile.
			Class.forName("java.math.BigDecimal");
			supportsBigDecimalCalls = true;
			// And no methods using BigDecimal are available with JSR169 spec.
			Method getbd = ResultSet.class.getMethod("getBigDecimal", new Class[] {int.class});
			supportsBigDecimalCalls = true;
		} catch (Throwable t) {
			supportsBigDecimalCalls = false;
		}
		SUPPORTS_BIG_DECIMAL_CALLS = supportsBigDecimalCalls;
	}
	
	private ResourceBundle res;
	private Locale locale;
	private String encode;
	private final static String MESSAGE_FILE = "org.apache.derby.loc.toolsmessages";
	private final static String ENV_CODESET = "derby.ui.codeset";
	private final static String ENV_LOCALE = "derby.ui.locale";
	private String messageFileName;
	private String resourceKey;
	private LocalizedOutput out;
	private LocalizedInput in;
	private boolean enableLocalized;
	private boolean unicodeEscape;
	private static LocalizedResource local;
	private int dateSize;
	private int timeSize;
	private int timestampSize;
	private DateFormat formatDate;
	private DateFormat formatTime;
	private DateFormat formatTimestamp;
	private NumberFormat formatNumber;
    private DecimalFormat formatDecimal;
	public LocalizedResource(){
		init();
	}
	public LocalizedResource(String encStr, String locStr, String msgF){
		init(encStr,locStr,msgF);
	}
	public static LocalizedResource getInstance(){
		if (local == null){
			local = new  LocalizedResource();
		}
		return local;
	}
	public void init(){
		init(null,null,null);
	}
	public void init (String encStr, String locStr, String msgF){
		if (encStr != null){
			encode = encStr;
		}
		//then get encoding string from environment
		if (encode == null) {
			String eEncode = getEnvProperty(ENV_CODESET);
			if ( eEncode != null ){
				encode = eEncode;
			}
		}
		
		// If null at this point then the default encoding
		// will be always used.

		//get locale string from the caller first
		locale = getNewLocale(locStr);

		//if null, get locale again from the environment variable
		if (locale==null) {
			String s = getEnvProperty(ENV_LOCALE);
			locale = getNewLocale(s);
		}
		//get the default locale if forced
		if (locale==null){
			locale = Locale.getDefault();
		}
		if (msgF != null) {
			messageFileName = msgF;
		}
		else {
			messageFileName = MESSAGE_FILE;
		}
		//create default in/out
		out = getNewOutput(System.out);
		in = getNewInput(System.in);

		//for faster code: get the format objs
		if (enableLocalized && locale != null){
			formatDecimal = (DecimalFormat)DecimalFormat.getInstance(locale);
			formatNumber = NumberFormat.getInstance(locale);
			formatDate = DateFormat.getDateInstance(DateFormat.LONG,locale);
			formatTime = DateFormat.getTimeInstance(DateFormat.LONG,locale);
			formatTimestamp = DateFormat.getDateTimeInstance(DateFormat.LONG,
													DateFormat.LONG, locale);
		}
		else {
			formatDecimal = (DecimalFormat)DecimalFormat.getInstance();
			formatNumber = NumberFormat.getInstance();
			formatDate = DateFormat.getDateInstance(DateFormat.LONG);
			formatTime = DateFormat.getTimeInstance(DateFormat.LONG);
			formatTimestamp = DateFormat.getDateTimeInstance(DateFormat.LONG,
													DateFormat.LONG);
		}
		//initialize display sizes for columns
		initMaxSizes2();
	}
	//get the message file resource according to the locale
	//fall back to English message file if locale message file is not found
	private void setResource(){
		if (res != null){
			return;
		}
		if ( locale == null || locale.toString().equals("none") ){
			res = ResourceBundle.getBundle(MESSAGE_FILE);
		}
		else
		try {
			res = ResourceBundle.getBundle(messageFileName,locale);
		}
		catch(java.util.MissingResourceException e){
			res = ResourceBundle.getBundle(messageFileName,Locale.ENGLISH);
		}
	}
	private void initMaxSizes2(){
		dateSize = 0;
		timeSize = 0;
		timestampSize = 0;

		int len;

		// check the date & timestamp max length
		// 3900/01/28 !! original devloper thought they were getting 2000/01/28
		Date d = new Date(60907276800000L);
		Timestamp t = new Timestamp(d.getTime());
		for(int month  = 0 ;  month <=11 ; month++, d.setTime(d.getTime() + (30L * 24L * 60L * 60L * 1000L))) {
			len=getDateAsString(d).length();

			if(len > dateSize ) {
				dateSize=len;
			}

			t.setTime(d.getTime() + ((((21L * 60L) + 59L) * 60L) + 59L));
			len=getTimestampAsString(t).length();

			if(len > timestampSize) {
				timestampSize=len;
			}
		}

		// set the time max length
		// minimum of 18 because the old buggy code always used 18
		len = 18;
		for (int hour = 0 ; hour < 24; hour++) {

			long secs = (hour * 3600L) + (59 * 60L) + 59L;

			long ms = secs * 1000L;

			Date td = new Date(ms);

			String fd = formatTime.format(td);

			if (fd.length() > len)
				len = fd.length();
		}
		timeSize=len;

	}

	public LocalizedInput getNewInput(InputStream i) {
		try {
			if (encode != null)
			    return new LocalizedInput(i,encode);
		}
		catch (UnsupportedEncodingException e){
			
		}
		return new LocalizedInput(i);
	}

	public LocalizedInput getNewEncodedInput(InputStream i, String encoding) {
		try {
	          return new LocalizedInput(i,encoding);
		}
		catch (UnsupportedEncodingException e){
			
		}
		return new LocalizedInput(i);
        }

	public LocalizedOutput getNewOutput(OutputStream o){
		try {
			if (encode != null)
			    return new LocalizedOutput(o,encode);
		}
		catch(UnsupportedEncodingException e){
		}
		return new LocalizedOutput(o);
	}
	/**
	 * Get a new LocalizedOutput with the given encoding.
	 * @throws UnsupportedEncodingException
	 */
	public LocalizedOutput getNewEncodedOutput(OutputStream o,
			String encoding) throws UnsupportedEncodingException{
		return new LocalizedOutput(o, encoding);
	}
	public String getTextMessage(String key ) {
		if ( res == null){
			setResource();
		}
		String s = key;
		try{
			s = res.getString(key);
		} catch (Exception e) {
			s = key;
		}
		//System.out.println(local.toString());
		//System.out.println("GetKey:"+key+"="+s);
		return s;
	}
	public String getTextMessage(String key, Object o){
			Object [] att=new Object[] {o};
			return getTextMessage(key,att);
	}
	public String getTextMessage(String key, Object o1, Object o2){
			Object [] att=new Object[] {o1,o2};
			return getTextMessage(key,att);
	}
	public String getTextMessage(String key, Object o1, Object o2, Object o3){
			Object [] att=new Object[] {o1,o2,o3};
			return getTextMessage(key,att);
	}
	public String getTextMessage(String key, Object o1, Object o2, Object o3, Object o4){
			Object [] att=new Object[] {o1,o2,o3,o4};
			return getTextMessage(key,att);
	}
	private Locale getNewLocale(String locStr){
			String l="", r="", v="";
			StringTokenizer st;
			if (locStr==null) {
				return null;
			}
			st=new StringTokenizer(locStr, "_");
			try {
				l=st.nextToken();
				if(st.hasMoreTokens()==true)
					r=st.nextToken();
				if(st.hasMoreTokens()==true)
					v=st.nextToken();
				return new Locale(l,r,v);
			} catch (Exception e) {
				return null;
			}
	}
	public String getTextMessage(String key, Object [] objectArr) {
		if (res == null){
			setResource();
		}
			try{
				return MessageFormat.format(res.getString(key), objectArr);
			} catch (Exception e) {
					String tmpFormat = key;
					for (int i=0; i<objectArr.length; i++)
						tmpFormat = tmpFormat + ", <{" + (i) + "}>";
					return MessageFormat.format(tmpFormat, objectArr);
			}
	}
	public String getLocalizedString(ResultSet rs,
										ResultSetMetaData rsm,
										int columnNumber) throws SQLException{
			if (!enableLocalized){
				return rs.getString(columnNumber);
			}
			int type = rsm.getColumnType(columnNumber);
			if ( type == Types.DATE ) {
				return getDateAsString(rs.getDate(columnNumber));
			}
			else if ( type == Types.INTEGER ||	type == Types.SMALLINT ||
					type == Types.BIGINT ||	type == Types.TINYINT ) {
				return getNumberAsString(rs.getLong(columnNumber));
			}
			else if (type == Types.REAL || 	type == Types.FLOAT ||
					type == Types.DOUBLE ) {
				return getNumberAsString(rs.getDouble(columnNumber));
			}
			else if (SUPPORTS_BIG_DECIMAL_CALLS && (type == Types.NUMERIC || type == Types.DECIMAL)) {
				return	getNumberAsString(rs.getBigDecimal(columnNumber,
											rsm.getScale(columnNumber)));
			}
			else if (type == Types.TIME ) {
				return getTimeAsString(rs.getTime(columnNumber));
			}
			else if (type == Types.TIMESTAMP ) {
				return getTimestampAsString(rs.getTimestamp(columnNumber));
			}
			return rs.getString(columnNumber);
		}

	public String getDateAsString(Date d){
		if (!enableLocalized){
			return d.toString();
		}
		return formatDate.format(d);
	}
	public String getTimeAsString(Date t){
		if (!enableLocalized){
			return t.toString();
		}
		return formatTime.format(t,	new StringBuffer(),
									  new java.text.FieldPosition(0)).toString();
	}
	public String getNumberAsString(int o){
		if (enableLocalized){
			return formatNumber.format(o);
		}
		else {
			return String.valueOf(o);
		}
	}
	public String getNumberAsString(long o){
		if (enableLocalized){
			return formatNumber.format(o);
		}
		else{
			return String.valueOf(o);
		}
	}
	public String getNumberAsString(Object o){
		if (enableLocalized){
			return formatNumber.format(o, new StringBuffer(),
										new FieldPosition(0)).toString();
		}
		else {
			return o.toString();
		}
	}
	public String getNumberAsString(double o){
		if (!enableLocalized) {
			return String.valueOf(o);
		}
		return formatDecimal.format(o);
	}
	public String getTimestampAsString(Timestamp t){
		if (!enableLocalized){
			return t.toString();
		}
		return formatTime.format(t,	new StringBuffer(),
									new java.text.FieldPosition(0)).toString();
	}
	public int getColumnDisplaySize(ResultSetMetaData rsm,
										int columnNumber) throws SQLException{
		  if (!enableLocalized){
				return rsm.getColumnDisplaySize(columnNumber);
		  }
		  int type = rsm.getColumnType(columnNumber);
		  if (type == Types.DATE)
					return dateSize;
		  if (type == Types.TIME)
					return timeSize;
		  if (type == Types.TIMESTAMP)
					return timestampSize;
		  return rsm.getColumnDisplaySize(columnNumber);
	}
	public String getStringFromDate(String dateStr)
		throws ParseException{
			if (!enableLocalized){
				return dateStr;
			}
			Date d = formatDate.parse(dateStr);
			return new java.sql.Date(d.getTime()).toString();
	}
	public String getStringFromTime(String timeStr)
		throws ParseException{
			if (!enableLocalized){
				return timeStr;
			}
			Date t = formatTime.parse(timeStr);
			return new java.sql.Time(t.getTime()).toString();
	}
	public String getStringFromValue(String val)
		throws ParseException{
			if (!enableLocalized){
				return val;
			}
			return formatNumber.parse(val).toString();
	}
	public String getStringFromTimestamp(String timestampStr)
		throws ParseException{
			if (!enableLocalized){
				return timestampStr;
			}
			Date ts = formatTimestamp.parse(timestampStr);
			return new java.sql.Timestamp(ts.getTime()).toString();
	}
	public Locale getLocale(){
			return locale;
	}

	private final synchronized String getEnvProperty(String key) {
		String s;
		 try
		  {
				resourceKey =  key;
				s = (String) java.security.AccessController.doPrivileged(this);
		}
		catch (SecurityException se) {
			s = null;
		}
		//System.out.println("{"+resourceKey+"="+s+"}");
		return s;
	}
	public final Object run() {
		String s = System.getProperty(resourceKey);
		return s;
	}
	public static boolean enableLocalization(boolean mode) {
		getInstance().enableLocalized = mode;
		//re-initialized locale
		getInstance().init();
		return mode;
	}
	public boolean isLocalized(){
		return getInstance().enableLocalized;
	}
	public static String getMessage(String key){
		return getInstance().getTextMessage(key);
	}
	public static String getMessage(String key, Object o1){
		return getInstance().getTextMessage(key,o1);
	}
	public static String getMessage(String key, Object o1, Object o2){
		return getInstance().getTextMessage(key,o1,o2);
	}
	public static String getMessage(String key, Object o1, Object o2, Object o3){
		return getInstance().getTextMessage(key,o1,o2,o3);
	}
	public static String getMessage(String key, Object o1, Object o2, Object o3, Object o4){
		return getInstance().getTextMessage(key,o1,o2,o3,o4);
	}
	public static LocalizedOutput OutputWriter(){
		return getInstance().out;
	}
	public static LocalizedInput InputReader(){
		return getInstance().in;
	}
	public static String getNumber(long o){
		return getInstance().getNumberAsString(o);
	}
	public static String getNumber(int o){
		return getInstance().getNumberAsString(o);
	}
	public static void setUnicodeEscape(boolean u){
		getInstance().unicodeEscape = u;
	}
	public static boolean getUnicodeEscape(){
		return getInstance().unicodeEscape;
	}
	public String toString(){
		String s = "toString(){\n" +
			"locale=" + (locale==null?"null":locale.toString()) + "\n" +
			"encode=" + encode + "\n" +
			"messageFile=" + messageFileName + "\n" +
			"resourceKey=" + resourceKey + "\n" +
			"enableLocalized=" + enableLocalized + " \n" +
			"unicodeEscape=" + unicodeEscape + "\n" +
			"dateSize=" + dateSize + "\n" +
			"timeSize=" + timeSize + "\n" +
			"timestampSize="+timestampSize+ "\n}";
			return s;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3259.java
error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13791.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13791.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13791.java
text:
```scala
private static final S@@tring FILEVERSION = "661482"; // Expected value $NON-NLS-1$

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

package org.apache.jmeter.save;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import java.nio.charset.Charset;

import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.util.JMeterError;
import org.apache.jorphan.util.JOrphanUtils;
import org.apache.log.Logger;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XppDriver;
import com.thoughtworks.xstream.mapper.CannotResolveClassException;
import com.thoughtworks.xstream.mapper.Mapper;
import com.thoughtworks.xstream.mapper.MapperWrapper;
import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.DataHolder;
import com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider;

/**
 * Handles setting up XStream serialisation.
 * The class reads alias definitions from saveservice.properties.
 * 
 */
public class SaveService {
	
	private static final Logger log = LoggingManager.getLoggerForClass();

	public static final String SAMPLE_EVENT_OBJECT = "SampleEvent"; // $NON-NLS-1$

    private static final XStream saver = new XStream(new PureJavaReflectionProvider()){
    	// Override wrapMapper in order to insert the Wrapper in the chain
    	protected MapperWrapper wrapMapper(MapperWrapper next) {
    		// Provide our own aliasing using strings rather than classes
            return new MapperWrapper(next){
    		// Translate alias to classname and then delegate to wrapped class
    	    public Class realClass(String alias) {
    	    	String fullName = aliasToClass(alias);
    	    	return super.realClass(fullName == null ? alias : fullName);
    	    }
    		// Translate to alias and then delegate to wrapped class
    	    public String serializedClass(Class type) {
    	    	if (type == null) {
    	    		return super.serializedClass(null); // was type, but that caused FindBugs warning
    	    	}
    	    	String alias = classToAlias(type.getName());
                return alias == null ? super.serializedClass(type) : alias ;
    	        }
            };
        }
    };

	// The XML header, with placeholder for encoding, since that is controlled by property
	private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"<ph>\"?>"; // $NON-NLS-1$

    // Default file name
    private static final String SAVESERVICE_PROPERTIES_FILE = "/bin/saveservice.properties"; // $NON-NLS-1$

    // Property name used to define file name
    private static final String SAVESERVICE_PROPERTIES = "saveservice_properties"; // $NON-NLS-1$

    // Define file format property names
    private static final String FILE_FORMAT = "file_format"; // $NON-NLS-1$
    private static final String FILE_FORMAT_TESTPLAN = "file_format.testplan"; // $NON-NLS-1$
    private static final String FILE_FORMAT_TESTLOG = "file_format.testlog"; // $NON-NLS-1$

    // Define file format versions
	private static final String VERSION_2_0 = "2.0";  // $NON-NLS-1$
    //NOT USED private static final String VERSION_2_1 = "2.1";  // $NON-NLS-1$
    private static final String VERSION_2_2 = "2.2";  // $NON-NLS-1$

    // Default to overall format, and then to version 2.2
    public static final String TESTPLAN_FORMAT
        = JMeterUtils.getPropDefault(FILE_FORMAT_TESTPLAN
        , JMeterUtils.getPropDefault(FILE_FORMAT, VERSION_2_2));
    
    public static final String TESTLOG_FORMAT
        = JMeterUtils.getPropDefault(FILE_FORMAT_TESTLOG
        , JMeterUtils.getPropDefault(FILE_FORMAT, VERSION_2_2));

    private static final boolean IS_TESTPLAN_FORMAT_20
        = VERSION_2_0.equals(TESTPLAN_FORMAT);
    
    private static final boolean IS_TESTLOG_FORMAT_20
    = VERSION_2_0.equals(TESTLOG_FORMAT);

    private static final boolean IS_TESTPLAN_FORMAT_22
        = VERSION_2_2.equals(TESTPLAN_FORMAT);

    // Holds the mappings from the saveservice properties file
    private static final Properties aliasToClass = new Properties();

    // Holds the reverse mappings
    private static final Properties classToAlias = new Properties();
	
    // Version information for test plan header
    // This is written to JMX files by ScriptWrapperConverter
    // Also to JTL files by ResultCollector
	private static final String VERSION = "1.2"; // $NON-NLS-1$

    // This is written to JMX files by ScriptWrapperConverter
	private static String propertiesVersion = "";// read from properties file; written to JMX files
    private static final String PROPVERSION = "2.1";// Expected version $NON-NLS-1$

    // Internal information only
    private static String fileVersion = ""; // read from properties file// $NON-NLS-1$
	private static final String FILEVERSION = "656027"; // Expected value $NON-NLS-1$
	private static String fileEncoding = ""; // read from properties file// $NON-NLS-1$

    static {
        log.info("Testplan (JMX) version: "+TESTPLAN_FORMAT+". Testlog (JTL) version: "+TESTLOG_FORMAT);
        initProps();
        checkVersions();
    }

	// Helper method to simplify alias creation from properties
	private static void makeAlias(String alias, String clazz) {
        aliasToClass.setProperty(alias,clazz);
        Object oldval=classToAlias.setProperty(clazz,alias);
        if (oldval != null) {
            log.error("Duplicate alias detected for "+clazz+": "+alias+" & "+oldval);
        }
	}

    public static Properties loadProperties() throws IOException{
        Properties nameMap = new Properties();
        FileInputStream fis = null;
        try {
			fis = new FileInputStream(JMeterUtils.getJMeterHome()
			             + JMeterUtils.getPropDefault(SAVESERVICE_PROPERTIES, SAVESERVICE_PROPERTIES_FILE));
			nameMap.load(fis);
		} finally {
			JOrphanUtils.closeQuietly(fis);
		}
        return nameMap;
    }
	private static void initProps() {
		// Load the alias properties
		try {
			Properties nameMap = loadProperties();
            // now create the aliases
			Iterator it = nameMap.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry me = (Map.Entry) it.next();
				String key = (String) me.getKey();
				String val = (String) me.getValue();
				if (!key.startsWith("_")) {
					makeAlias(key, val);
				} else {
					// process special keys
					if (key.equalsIgnoreCase("_version")) { // $NON-NLS-1$
                        propertiesVersion = val;
						log.info("Using SaveService properties version " + propertiesVersion);
                    } else if (key.equalsIgnoreCase("_file_version")) { // $NON-NLS-1$
                            fileVersion = extractVersion(val);
                            log.info("Using SaveService properties file version " + fileVersion);
                    } else if (key.equalsIgnoreCase("_file_encoding")) { // $NON-NLS-1$
                        fileEncoding = val;
                        log.info("Using SaveService properties file encoding " + fileEncoding);
                    } else {
						key = key.substring(1);// Remove the leading "_"
						try {
							if (val.trim().equals("collection")) { // $NON-NLS-1$
								saver.registerConverter((Converter) Class.forName(key).getConstructor(
										new Class[] { Mapper.class }).newInstance(
										new Object[] { saver.getMapper() }));
							} else if (val.trim().equals("mapping")) { // $NON-NLS-1$
								saver.registerConverter((Converter) Class.forName(key).getConstructor(
										new Class[] { Mapper.class }).newInstance(
										new Object[] { saver.getMapper() }));
							} else {
								saver.registerConverter((Converter) Class.forName(key).newInstance());
							}
						} catch (IllegalAccessException e1) {
							log.warn("Can't register a converter: " + key, e1);
						} catch (InstantiationException e1) {
							log.warn("Can't register a converter: " + key, e1);
						} catch (ClassNotFoundException e1) {
							log.warn("Can't register a converter: " + key, e1);
						} catch (IllegalArgumentException e1) {
							log.warn("Can't register a converter: " + key, e1);
						} catch (SecurityException e1) {
							log.warn("Can't register a converter: " + key, e1);
						} catch (InvocationTargetException e1) {
							log.warn("Can't register a converter: " + key, e1);
						} catch (NoSuchMethodException e1) {
							log.warn("Can't register a converter: " + key, e1);
						}
					}
				}
			}
		} catch (IOException e) {
			log.fatalError("Bad saveservice properties file", e);
			throw new JMeterError("JMeter requires the saveservice properties file to continue");
		}
	}

    // For converters to use
    public static String aliasToClass(String s){
        String r = aliasToClass.getProperty(s);
        return r == null ? s : r;
    }
    
    // For converters to use
    public static String classToAlias(String s){
        String r = classToAlias.getProperty(s);
        return r == null ? s : r;
    }
    
    // Called by Save function
	public static void saveTree(HashTree tree, OutputStream out) throws IOException {
		// Get the OutputWriter to use
		OutputStreamWriter outputStreamWriter = getOutputStreamWriter(out);
		writeXmlHeader(outputStreamWriter);
		// Use deprecated method, to avoid duplicating code
		ScriptWrapper wrapper = new ScriptWrapper();
		wrapper.testPlan = tree;
		saver.toXML(wrapper, outputStreamWriter);
		outputStreamWriter.write('\n');// Ensure terminated properly
		outputStreamWriter.close();
	}

	// Used by Test code
	public static void saveElement(Object el, OutputStream out) throws IOException {
		// Get the OutputWriter to use
		OutputStreamWriter outputStreamWriter = getOutputStreamWriter(out);
		writeXmlHeader(outputStreamWriter);
		// Use deprecated method, to avoid duplicating code
		saver.toXML(el, outputStreamWriter);
		outputStreamWriter.close();
	}

	// Used by Test code
	public static Object loadElement(InputStream in) throws IOException {
		// Get the InputReader to use
		InputStreamReader inputStreamReader = getInputStreamReader(in);
		// Use deprecated method, to avoid duplicating code
		Object element = saver.fromXML(inputStreamReader);
		inputStreamReader.close();
		return element;
	}

    /**
     * Save a sampleResult to an XML output file using XStream.
     * 
     * @param evt sampleResult wrapped in a sampleEvent
     * @param writer output stream which must be created using {@link #getFileEncoding(String)}
     */
	// Used by ResultCollector#recordResult()
	public synchronized static void saveSampleResult(SampleEvent evt, Writer writer) throws IOException {
		DataHolder dh = saver.newDataHolder();
		dh.put(SAMPLE_EVENT_OBJECT, evt);
		// This is effectively the same as saver.toXML(Object, Writer) except we get to provide the DataHolder
		// Don't know why there is no method for this in the XStream class
		saver.marshal(evt.getResult(), new XppDriver().createWriter(writer), dh);
		writer.write('\n');
	}

    /**
     * @param elem test element
     * @param writer output stream which must be created using {@link #getFileEncoding(String)}
     */
	// Used by ResultCollector#recordStats()
	public synchronized static void saveTestElement(TestElement elem, Writer writer) throws IOException {
		saver.toXML(elem, writer);
		writer.write('\n');
	}

	private static boolean versionsOK = true;

	// Extract version digits from String of the form #Revision: n.mm #
	// (where # is actually $ above)
	private static final String REVPFX = "$Revision: ";
	private static final String REVSFX = " $"; // $NON-NLS-1$

	private static String extractVersion(String rev) {
		if (rev.length() > REVPFX.length() + REVSFX.length()) {
			return rev.substring(REVPFX.length(), rev.length() - REVSFX.length());
		}
		return rev;
	}

//	private static void checkVersion(Class clazz, String expected) {
//
//		String actual = "*NONE*"; // $NON-NLS-1$
//		try {
//			actual = (String) clazz.getMethod("getVersion", null).invoke(null, null);
//			actual = extractVersion(actual);
//		} catch (Exception ignored) {
//			// Not needed
//		}
//		if (0 != actual.compareTo(expected)) {
//			versionsOK = false;
//			log.warn("Version mismatch: expected '" + expected + "' found '" + actual + "' in " + clazz.getName());
//		}
//	}

    // Routines for TestSaveService
    static boolean checkPropertyVersion(){
        return SaveService.PROPVERSION.equals(SaveService.propertiesVersion);
    }
    
    static boolean checkFileVersion(){
        return SaveService.FILEVERSION.equals(SaveService.fileVersion);
    }

    static boolean checkVersions() {
		versionsOK = true;
		// Disable converter version checks as they are more of a nuisance than helpful
//		checkVersion(BooleanPropertyConverter.class, "493779"); // $NON-NLS-1$
//		checkVersion(HashTreeConverter.class, "514283"); // $NON-NLS-1$
//		checkVersion(IntegerPropertyConverter.class, "493779"); // $NON-NLS-1$
//		checkVersion(LongPropertyConverter.class, "493779"); // $NON-NLS-1$
//		checkVersion(MultiPropertyConverter.class, "514283"); // $NON-NLS-1$
//		checkVersion(SampleResultConverter.class, "571992"); // $NON-NLS-1$
//
//        // Not built until later, so need to use this method:
//        try {
//            checkVersion(
//                    Class.forName("org.apache.jmeter.protocol.http.util.HTTPResultConverter"), // $NON-NLS-1$
//                    "514283"); // $NON-NLS-1$
//        } catch (ClassNotFoundException e) {
//            versionsOK = false;
//            log.warn(e.getLocalizedMessage());
//        }
//		checkVersion(StringPropertyConverter.class, "493779"); // $NON-NLS-1$
//		checkVersion(TestElementConverter.class, "549987"); // $NON-NLS-1$
//		checkVersion(TestElementPropertyConverter.class, "549987"); // $NON-NLS-1$
//		checkVersion(ScriptWrapperConverter.class, "514283"); // $NON-NLS-1$
//		checkVersion(TestResultWrapperConverter.class, "514283"); // $NON-NLS-1$
//        checkVersion(SampleSaveConfigurationConverter.class,"549936"); // $NON-NLS-1$

        if (!PROPVERSION.equalsIgnoreCase(propertiesVersion)) {
			log.warn("Bad _version - expected " + PROPVERSION + ", found " + propertiesVersion + ".");
		}
        if (!FILEVERSION.equalsIgnoreCase(fileVersion)) {
            log.warn("Bad _file_version - expected " + FILEVERSION + ", found " + fileVersion +".");
        }
		if (versionsOK) {
			log.info("All converter versions present and correct");
		}
        return versionsOK;
	}

	public static TestResultWrapper loadTestResults(InputStream reader) throws Exception {
		// Get the InputReader to use
		InputStreamReader inputStreamReader = getInputStreamReader(reader);
		TestResultWrapper wrapper = (TestResultWrapper) saver.fromXML(inputStreamReader);
		inputStreamReader.close();
		return wrapper;
	}

	public static HashTree loadTree(InputStream reader) throws Exception {
		if (!reader.markSupported()) {
			reader = new BufferedInputStream(reader);
		}
		reader.mark(Integer.MAX_VALUE);
		ScriptWrapper wrapper = null;
		try {
			// Get the InputReader to use
			InputStreamReader inputStreamReader = getInputStreamReader(reader);
			wrapper = (ScriptWrapper) saver.fromXML(inputStreamReader);
			inputStreamReader.close();
			if (wrapper == null){
				log.error("Problem loading new style: see above.");
				return null;
			}
			return wrapper.testPlan;
		} catch (CannotResolveClassException e) {
			log.warn("Problem loading new style: " + e.getLocalizedMessage());
			reader.reset();
			return OldSaveService.loadSubTree(reader);
		} catch (NoClassDefFoundError e) {
			log.error("Missing class "+e);
			return null;
		} catch (ConversionException e) {
            log.error("Conversion error "+e);
            return null;		    
		}
	}
	
	private static InputStreamReader getInputStreamReader(InputStream inStream) {
		// Check if we have a encoding to use from properties
		Charset charset = getFileEncodingCharset();
		if(charset != null) {
			return new InputStreamReader(inStream, charset);
		}
		else {
			// We use the default character set encoding of the JRE
			return new InputStreamReader(inStream);
		}
	}

	private static OutputStreamWriter getOutputStreamWriter(OutputStream outStream) {
		// Check if we have a encoding to use from properties
		Charset charset = getFileEncodingCharset();
		if(charset != null) {
			return new OutputStreamWriter(outStream, charset);
		}
		else {
			// We use the default character set encoding of the JRE
			return new OutputStreamWriter(outStream);
		}
	}
	
	/**
	 * Returns the file Encoding specified in saveservice.properties or the default
	 * @param dflt value to return if file encoding was not provided
	 * 
	 * @return file encoding or default
	 */
	// Used by ResultCollector when creating output files
	public static String getFileEncoding(String dflt){
		if(fileEncoding != null && fileEncoding.length() > 0) {
			return fileEncoding;
		}
		else {
			return dflt;
		}		
	}
	
	private static Charset getFileEncodingCharset() {
		// Check if we have a encoding to use from properties
		if(fileEncoding != null && fileEncoding.length() > 0) {
			return Charset.forName(fileEncoding);
		}
		else {
			// We use the default character set encoding of the JRE
			return null;
		}
	}
	
	private static void writeXmlHeader(OutputStreamWriter writer) throws IOException {
		// Write XML header if we have the charset to use for encoding
		Charset charset = getFileEncodingCharset();
		if(charset != null) {
			// We do not use getEncoding method of Writer, since that returns
			// the historical name
			String header = XML_HEADER.replaceAll("<ph>", charset.name());
			writer.write(header);
			writer.write('\n');
		}
	}

	public static boolean isSaveTestPlanFormat20() {
		return IS_TESTPLAN_FORMAT_20;
	}

	public static boolean isSaveTestLogFormat20() {
		return IS_TESTLOG_FORMAT_20;
	}

    // New test format - more compressed class names
    public static boolean isSaveTestPlanFormat22() {
        return IS_TESTPLAN_FORMAT_22;
    }

    
//  Normal output
//  ---- Debugging information ----
//  required-type       : org.apache.jorphan.collections.ListedHashTree 
//  cause-message       : WebServiceSampler : WebServiceSampler 
//  class               : org.apache.jmeter.save.ScriptWrapper 
//  message             : WebServiceSampler : WebServiceSampler 
//  line number         : 929 
//  path                : /jmeterTestPlan/hashTree/hashTree/hashTree[4]/hashTree[5]/WebServiceSampler 
//  cause-exception     : com.thoughtworks.xstream.alias.CannotResolveClassException 
//  -------------------------------

    /**
     * Simplify getMessage() output from XStream ConversionException
     * @param ce - ConversionException to analyse
     * @return string with details of error
     */
    public static String CEtoString(ConversionException ce){
        String msg = 
            "XStream ConversionException at line: " + ce.get("line number")
            + "\n" + ce.get("message")
            + "\nPerhaps a missing jar? See log file.";
        return msg;
    }

    public static String getPropertiesVersion() {
        return propertiesVersion;
    }

    public static String getVERSION() {
        return VERSION;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13791.java
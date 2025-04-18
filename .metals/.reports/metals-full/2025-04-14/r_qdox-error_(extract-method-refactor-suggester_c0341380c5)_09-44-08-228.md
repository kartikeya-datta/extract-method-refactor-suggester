error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3425.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3425.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3425.java
text:
```scala
r@@unTest(task,null,MessageHolderChecker.INFOS);

/* *******************************************************************
 * Copyright (c) 1999-2001 Xerox Corporation, 
 *               2002 Palo Alto Research Center, Incorporated (PARC)
 *               2003 Contributors.
 *               2005 Contributors
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 *  
 * Contributors: 
 *     Xerox/PARC     initial implementation
 *     IBM	            ongoing maintenance 
 * ******************************************************************/

package org.aspectj.tools.ant.taskdefs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import junit.framework.TestCase;

import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.BuildListener;
import org.apache.tools.ant.Location;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.selectors.FilenameSelector;
import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.IMessageHolder;
import org.aspectj.bridge.MessageHandler;
import org.aspectj.bridge.MessageUtil;
import org.aspectj.util.FileUtil;
import org.aspectj.util.LangUtil;

/**
 * AjcTask test cases.
 * Please put new ones with others between ------- comments.
 * 
 * Some API tests, but mostly functional tests driving
 * the task execute using data in ../taskdefs/testdata.
 * This will re-run in forked mode for any nonfailing
 * compile if aspectjtools-dist is built into
 * ../aj-build/dist/tools/lib/aspectjtools.jar.
 */
public class AjcTaskTest extends TestCase {

	private static final Class NO_EXCEPTION = null;
	private static final String NOFILE = "NOFILE";

	private static final File tempDir;
	private static final String aspectjtoolsJar;
	private static final String testdataDir;
	private static final StringBuffer MESSAGES = new StringBuffer();
	/** accept writable .class files */
	private static FileFilter PICK_CLASS_FILES;

	static {
		tempDir = new File("IncrementalAjcTaskTest-temp");
		String toolsPath = "../aj-build/dist/tools/lib/aspectjtools.jar";
		File toolsjar = new File(toolsPath);
		if (toolsjar.canRead()) {
			aspectjtoolsJar = toolsjar.getAbsolutePath();
		} else {
			aspectjtoolsJar = null;
			String s =
				"AjcTaskTest not forking - build aspectjtools-dist to get "
					+ toolsPath;
			System.out.println(s);
		}
		File dir = new File("../taskdefs/testdata");
		if (dir.canRead() && dir.isDirectory()) {
			testdataDir = dir.getAbsolutePath();
		} else {
			testdataDir = null;
		}
		PICK_CLASS_FILES = new FileFilter() {
			public boolean accept(File file) {
				return (
					(null != file)
						&& file.isFile()
						&& file.canWrite()
						&& file.getPath().endsWith(".class"));
			}

		};
	}

	/** 
	  * Check that aspectjtools are found on the classpath,
	  * reporting any errors to System.err.
	  * 
	  * Run multiple times with different classpaths.
	  * This should find variants
	  * aspectjtools.jar,
	  * aspectj-tools.jar,
	  * aspectj-tools-1.1.jar, and
	  * aspectjtools-1.0.6.jar
	  * but not
	  * aspectjrt.jar or
	  * aspectj/tools.jar.
	  * XXX use testing aspect to stub out 
	  * <code>System.getProperty("java.class.path")</code>
	  * @param args a String[], first is expected path, if any
	  */
	public static void main(String[] args) {
		java.io.File toolsjar = AjcTask.findAspectjtoolsJar();
		if ((null == args) || (0 == args.length)) {
			if (null != toolsjar) {
				System.err.println("FAIL - not expected: " + toolsjar);
			}
		} else if ("-help".equals(args[0])) {
			System.out.println(
				"java "
					+ AjcTaskTest.class.getName()
					+ " <expectedPathToAspectjtoolsJar>");
		} else if (null == toolsjar) {
			System.err.println("FAIL - expected: " + args[0]);
		} else {
			String path = toolsjar.getAbsolutePath();
			if (!path.equals(args[0])) {
				System.err.println(
					"FAIL - expected: " + args[0] + " actual: " + path);
			}
		}
	}

	public static void collectMessage(String s) {
		MESSAGES.append(s);
	}

	private static void deleteTempDir() {
		if ((null != tempDir) && tempDir.exists()) {
			FileUtil.deleteContents(tempDir);
			tempDir.delete();
			// when tempDir not used...
			if (null != testdataDir) {
				File dataDir = new File(testdataDir);
				if (dataDir.canRead()) {
					FileUtil.deleteContents(dataDir, PICK_CLASS_FILES, false);
				}
			}
		}
	}
	private static final File getTempDir() {
		return tempDir;
	}

	public AjcTaskTest(String name) {
		super(name);
	}

	public void tearDown() {
		deleteTempDir();
		MESSAGES.setLength(0);
	}
    private void checkRun(AjcTask task, String exceptionString) {
        try {
            task.execute();
            assertTrue(null == exceptionString);
        } catch (BuildException e) {
            if (null == exceptionString) {
                assertTrue("unexpected " + e.getMessage(), false);
            } else {
                String m = e.getMessage();
                if (null == m) {
                    assertTrue("not " + exceptionString, false);
                } else if (-1 == m.indexOf(exceptionString)) {
                    assertEquals(exceptionString, e.getMessage());
                }
            }
        }

    }

    private void checkContains(String[] cmd, String option, boolean contains) {
        for (int i = 0; i < cmd.length; i++) {
            if (option.equals(cmd[i])) {
                if (contains) {
                    return;
                } else {
                    assertTrue(
                        "not expecting " + option + " in " + Arrays.asList(cmd),
                        false);
                }
            }
        }
        if (contains) {
            assertTrue(
                "expecting " + option + " in " + Arrays.asList(cmd),
                false);
        }
    }
    protected AjcTask getTask(String input) {
        return getTask(input, getTempDir());
    }

    protected AjcTask getTask(String input, File destDir) {
        AjcTask task = new AjcTask();
        Project p = new Project();
        task.setProject(p);
        if (null != destDir) {
            task.setDestdir(destDir);
        }
        if (NOFILE.equals(input)) {
            // add nothing
        } else if (input.endsWith(".lst")) {
            if (-1 != input.indexOf(",")) {
                throw new IllegalArgumentException(
                    "lists not supported: " + input);
            } else if (null == testdataDir) {
                throw new Error("testdata not found - run in ../taskdefs");
            } else {
                String path = testdataDir + File.separator + input;
                task.setArgfiles(new Path(task.getProject(), path));
            }
        } else if ((input.endsWith(".java") || input.endsWith(".aj"))) {
            FilenameSelector fns = new FilenameSelector();
            fns.setName(input);
            task.addFilename(fns);
        } else {
            String path = testdataDir + File.separator + input;
            task.setSourceRoots(new Path(task.getProject(), path));
        }
        task.setClasspath(new Path(p, "../lib/test/aspectjrt.jar"));
        return task;
    }

    /** used in testMessageHolderClassName */
    public static class InfoHolder extends MessageHandler {
        public InfoHolder() {
        }
        public boolean handleMessage(IMessage message) {
            if (0 == IMessage.INFO.compareTo(message.getKind())) {
                AjcTaskTest.collectMessage(message.getMessage());
            }
            return true;
        }
    }

    /** used in testMessageHolderClassName */
    public static class Holder extends MessageHandler {
        public Holder() {
        }
        public boolean handleMessage(IMessage message) {
            IMessage.Kind kind = message.getKind();
            if (IMessage.ERROR.isSameOrLessThan(kind)) {
                String m = kind.toString();
                AjcTaskTest.collectMessage(m.substring(0, 1));
            }
            return true;
        }
    }

    // ------------------------------------------------------
    // ------------------------------------------------------
    // ------------------------------------------------------
    // ------------------------------------------------------
    // ------------------------------------------------------
    // ------------------------------------------------------
    // Start of test cases
    
	public void testNullDestDir() {
		AjcTask task = getTask(NOFILE, null);
		String[] cmd = task.makeCommand();

		for (int i = 0; i < cmd.length; i++) {
			assertTrue(!"-d".equals(cmd[i]));
		}
	}
	
	public void testOutputRequirement() {
		AjcTask task = getTask("default.lst");
		checkRun(task, null);

		// copyInJars now just emits warning b/c unused
		task = getTask("default.lst", null);
		task.setCopyInjars(true);
		checkRun(task, null);

		// sourceRootCopyFilter requires destDir
		task = getTask("default.lst", null);
		task.setSourceRootCopyFilter("**/*.java");
		checkRun(task, "sourceRoot");
	}

	public void testSourceRootCopyFilter() {
		// sourceRootCopyFilter works..
		File destDir = getTempDir();
		assertTrue(
			"unable to create " + destDir,
			destDir.canRead() || destDir.mkdirs());
		AjcTask task = getTask("sourceroot", destDir);
		task.setSourceRootCopyFilter("doNotCopy,**/*.txt");
		File file = new File(destDir, "Default.java").getAbsoluteFile();
		assertTrue(file + ".canRead() prematurely", !file.canRead());
		checkRun(task, null);
		// got expected resources
		assertTrue(file + ".canRead() failed", file.canRead());
		File pack = new File(destDir, "pack");
		file = new File(pack, "Pack.java").getAbsoluteFile();
		assertTrue(file + ".canRead() failed", file.canRead());
		file = new File(pack, "includeme").getAbsoluteFile();
		assertTrue(file + ".canRead() failed", file.canRead());

		// didn't get unexpected resources
		file = new File(pack, "something.txt");
		assertTrue(file + ".canRead() passed", !file.canRead());
		file = new File(destDir, "doNotCopy");
		assertTrue(file + ".canRead() passed", !file.canRead());
		file = new File(destDir, "skipTxtFiles.txt");
		assertTrue(file + ".canRead() passed", !file.canRead());
	}

    public void testInpathDirCopyFilter() {
        // inpathDirCopyFilter works with output directory
        File destDir = getTempDir();
        assertTrue(
            "unable to create " + destDir,
            destDir.canRead() || destDir.mkdirs());
        AjcTask task = getTask(NOFILE, destDir);        
        Project p = task.getProject();
        Path indirs = new Path(p);
        File dir = new File(testdataDir, "inpathDirs").getAbsoluteFile();
        indirs.addExisting(new Path(p, new File(dir, "inpathDirOne").getAbsolutePath()));
        indirs.addExisting(new Path(p, new File(dir, "inpathDirTwo").getAbsolutePath()));
        task.setInpath(indirs);
        task.setInpathDirCopyFilter("doNotCopy,**/*.txt");
        
        File file = new File(destDir, "Default.java").getAbsoluteFile();
        assertTrue(file + ".canRead() prematurely", !file.canRead());
        checkRun(task, null);

        // got expected resources
        File pack = new File(destDir, "pack");
        file = new File(pack, "includeme").getAbsoluteFile();
        assertTrue(file + ".canRead() failed", file.canRead());
        file = new File(pack, "Pack.class").getAbsoluteFile();
        assertTrue(file + ".canRead() failed", file.canRead());
        file = new File(destDir, "copyMe.htm").getAbsoluteFile();
        assertTrue(file + ".canRead() failed", file.canRead());
        file = new File(destDir, "Default.class").getAbsoluteFile();
        assertTrue(file + ".canRead() failed", file.canRead());

        // didn't get unexpected resources
        file = new File(pack, "something.txt");
        assertTrue(file + ".canRead() passed", !file.canRead());
        file = new File(destDir, "doNotCopy");
        assertTrue(file + ".canRead() passed", !file.canRead());
        file = new File(destDir, "skipTxtFiles.txt");
        assertTrue(file + ".canRead() passed", !file.canRead());
    }
    
    public void testInpathDirCopyFilterWithJar() throws IOException {
    	checkInpathCopy("testInpathDirCopyFilterWithJar-out.jar");
    }

    // test resource copying for oddball jar files that don't end in .jar
    public void testInpathDirCopyFilterWithOddjar() throws IOException {
    	checkInpathCopy("testInpathDirCopyFilterWithJar-outJarFile");
    }
    
    private void checkInpathCopy(String outjarFileStr) throws IOException {
        // inpathDirCopyFilter works with output jar
        File destDir = getTempDir();
        assertTrue(
            "unable to create " + destDir,
            destDir.canRead() || destDir.mkdirs());
        AjcTask task = getTask(NOFILE, null);        
        File destJar = new File(destDir, outjarFileStr);
        task.setOutjar(destJar);
        Project p = task.getProject();
        Path indirs = new Path(p);
        File dir = new File(testdataDir, "inpathDirs").getAbsoluteFile();
        indirs.addExisting(new Path(p, new File(dir, "inpathDirOne").getAbsolutePath()));
        indirs.addExisting(new Path(p, new File(dir, "inpathDirTwo").getAbsolutePath()));
        task.setInpath(indirs);
        task.setInpathDirCopyFilter("doNotCopy,**/*.txt,**/*.class");
        
        checkRun(task, null);

        JarFile jarFile = new JarFile(destJar);
        String[] expected = {"copyMe.htm", "pack/includeme", 
                "pack/Pack.class", "Default.class"};
        String[] unexpected = {"doNotCopy", "skipTxtFiles.txt", "pack/something.txt"};
        for (int i = 0; i < expected.length; i++) {
            JarEntry entry = jarFile.getJarEntry(expected[i]);
            assertTrue(expected[i] + " not found", null != entry);
        }
        for (int i = 0; i < unexpected.length; i++) {
            JarEntry entry = jarFile.getJarEntry(unexpected[i]);
            assertTrue(unexpected[i] + " found", null == entry);
        }
    }

    public void testInpathDirCopyFilterError() {
        // inpathDirCopyFilter fails with no output directory or jar iff specified
        AjcTask task = getTask(NOFILE, null);        
        Project p = task.getProject();
        Path indirs = new Path(p);
        File dir = new File(testdataDir, "inpathDirs").getAbsoluteFile();
        indirs.addExisting(new Path(p, new File(dir, "inpathDirOne").getAbsolutePath()));
        indirs.addExisting(new Path(p, new File(dir, "inpathDirTwo").getAbsolutePath()));
        task.setInpath(indirs);
        task.setInpathDirCopyFilter("doNotCopy,**/*.txt,**/*.class");

        // expecting error
        checkRun(task, "inpathDirCopyFilter");
    }

	// this test method submitted by patch from Andrew Huff (IBM)
	// verifies that the log attribute of AjcTask writes output to the given log file
	public void testLoggingMode() {
		AjcTask task = getTask("default.lst");
		task.setFailonerror(false);
		File logFile = new File("testLogFile1.txt");
		String s = logFile.getAbsolutePath();
		logFile.delete();
		long initialLength = logFile.length();
		task.setLog(logFile);
		runTest(task,null,null);
		long newLength = logFile.length();
		assertTrue(newLength > initialLength);
		logFile.delete();
	}


	public void testCommandEditor() {
		String className = VerboseCommandEditor.class.getName();
		System.setProperty(AjcTask.COMMAND_EDITOR_NAME, className);
		assertEquals(
			className,
			System.getProperty(AjcTask.COMMAND_EDITOR_NAME));
		AjcTask task = getTask(NOFILE);
		task.setCommandEditor(new VerboseCommandEditor());
		String[] cmd = task.makeCommand();
		assertEquals(VerboseCommandEditor.VERBOSE, cmd[0]);

		task = getTask(NOFILE);
		task.setCommandEditorClass(VerboseCommandEditor.class.getName());
		cmd = task.makeCommand();
		assertEquals(VerboseCommandEditor.VERBOSE, cmd[0]);
	}
	//    public void testStaticCommandEditor() {
	//        // XXX need to test COMMAND_EDITOR, but can't require property when run
	//    }

	public void testLimitTo() {
		int numArgs = 100;
		String arg = "123456789";
		String[] args = new String[numArgs];
		for (int i = 0; i < args.length; i++) {
			args[i] = arg;
		}
		// no limit
		int max = numArgs * (arg.length() + 1);
		Location location = new Location("AjcTaskTest.java");
		String[] newArgs = AjcTask.GuardedCommand.limitTo(args, max, location);
		assertTrue("same", args == newArgs);

		// limited - read file and verify arguments
		max--;
		newArgs = AjcTask.GuardedCommand.limitTo(args, max, location);
		assertTrue("not same", args != newArgs);
		assertTrue("not null", null != newArgs);
		String label = "newArgs " + Arrays.asList(newArgs);
		assertTrue("size 2" + label, 2 == newArgs.length);
		assertEquals("-argfile", newArgs[0]);
		File file = new File(newArgs[1]);
		assertTrue("readable newArgs[1]" + label, file.canRead());
		FileReader fin = null;
		try {
			fin = new FileReader(file);
			BufferedReader reader = new BufferedReader(fin);
			String line;
			int i = 0;
			while (null != (line = reader.readLine())) {
				assertEquals(i + ": ", args[i++], line);
			}
			assertEquals("num entries", i, args.length);
		} catch (IOException e) {
			assertTrue("IOException " + e.getMessage(), false);
		} finally {
			if (null != fin) {
				try {
					fin.close();
				} catch (IOException e) {
					// ignore
				}
			}
			file.delete();
		}
	}

	public void testFindAspectjtoolsJar() {
		File toolsJar = AjcTask.findAspectjtoolsJar();
		if (null != toolsJar) {
			assertNull("tools jar found?: " + toolsJar, toolsJar);
		}
		// not found when unit testing b/c not on system classpath
		// so just checking for exceptions.
		// XXX need aspect to stub out System.getProperty(..) 
	}


	public void testMessageHolderClassName() {
		AjcTask task = getTask("compileError.lst");
		task.setFailonerror(false);
		MESSAGES.setLength(0);
		runTest(
			task,
			null,
			MessageHolderChecker.ONE_ERROR,
			Holder.class.getName());
		String result = MESSAGES.toString();
		MESSAGES.setLength(0);
		assertEquals("messages", "e", result);
	}

    // TODO skipped test - works locally but not on build machine?
    public void skip_testMessageHolderClassWithDoneSignal() {
        AjcTask task = getTask("default.lst");
        task.setFailonerror(false);
        String DONE = "This is a unique message, not confused with others.";
        task.setXDoneSignal(DONE);
        MESSAGES.setLength(0);
        runTest(
            task,
            null,
            MessageHolderChecker.INFOS,
            InfoHolder.class.getName());
        final String result = MESSAGES.toString();
        String temp = new String(result);
        MESSAGES.setLength(0);
        if (!temp.endsWith(DONE)) {
            if (temp.length() > 20) {
                temp = "..." + temp.substring(temp.length()-20, temp.length());
            }
            assertTrue(DONE + " is not suffix of \"" + temp + "\"", false);
        }
        // exactly one such message
        temp = new String(result);
        temp = temp.substring(0, temp.length()-DONE.length());
        if (temp.endsWith(DONE)) {
            temp = new String(result);
            if (temp.length() > 20) {
                temp = "..." + temp.substring(temp.length()-20, temp.length());
            }
            assertTrue(DONE + " signalled twice: \"" + temp + "\"", false);
        }
    }

	public void testDefaultListForkedNoTools() {
		AjcTask task = getTask("default.lst");
		task.setFork(true);
		boolean passed = false;
		try {
			runTest(task, BuildException.class, MessageHolderChecker.NONE);
			passed = true;
		} finally {
			if (!passed) {
				String m =
					"AjcTaskTest.testDefaultListForkedNoTools()"
						+ " fails if aspectjtools.jar is on the classpath";
				System.err.println(m);
			}
		}
	}

	public void testDefaultListForkedIncremental() {
		AjcTask task = getTask("default.lst");
		task.setFork(true);
		task.setIncremental(true);
		runTest(task, BuildException.class, MessageHolderChecker.NONE);
	}

	/** failonerror should default to true, unlike other booleans */
	public void testCompileErrorFailOnErrorDefault() {
		AjcTask task = getTask("compileError.lst");
        final PrintStream serr = System.err;
        try {
            System.setErr(new PrintStream(new java.io.ByteArrayOutputStream()));
            runTest(task, BuildException.class, MessageHolderChecker.ONE_ERROR);
        } finally {
            System.setErr(serr);
        }
	}
    public void testCompileErrorListDefaultHolder() {
        AjcTask task = getTask("compileError.lst");
        final PrintStream serr = System.err;
        try {
            System.setErr(new PrintStream(new java.io.ByteArrayOutputStream()));
            task.execute();
            fail("expected BuildException from failed compile by default");
        } catch (BuildException t) {
            // ok
        } finally {
            System.setErr(serr);
            deleteTempDir();
        }
    }

	public void testDefaultList() {
		AjcTask task = getTask("default.lst");
		runTest(task, NO_EXCEPTION, MessageHolderChecker.INFOS);
	}

	public void testCompileErrorList() {
		AjcTask task = getTask("compileError.lst");
		task.setFailonerror(false);
		runTest(task, NO_EXCEPTION, MessageHolderChecker.ONE_ERROR);
	}
	
	public void testShowWeaveInfo() {
		AjcTask task = getTask("showweaveinfo.lst");
		task.setShowWeaveInfo(true);
		MessageHandler mh = new MessageHandler(false);
		mh.dontIgnore(IMessage.WEAVEINFO);
		MessageHolderChecker mhc = new MessageHolderChecker(0,0,0,0,MessageHolderChecker.IGNORE);
		mhc.weaveinfos = 2; // Expect 2 weaving messages
		runTest(task,NO_EXCEPTION,mhc);
		mhc.weaveinfos = MessageHolderChecker.IGNORE;
	}

	public void testCompileWarningList() {
		AjcTask task = getTask("compileWarning.lst");
		runTest(task, NO_EXCEPTION, MessageHolderChecker.ONE_WARNING);
	}

	public void testNoSuchFileList() {
		AjcTask task = getTask("NoSuchFile.lst");
		task.setFailonerror(false);
		runTest(task, NO_EXCEPTION, MessageHolderChecker.ONE_ERROR_ONE_ABORT);
	}

	public void testVersions() {
        String[] inputs = AjcTask.TARGET_INPUTS;
		for (int i = 0; i < inputs.length; i++) {
			AjcTask task = getTask(NOFILE);
            task.setTarget(inputs[i]);
			String[] cmd = task.makeCommand();
            checkContains(cmd, "-target", true);
            checkContains(cmd, inputs[i], true);
		}

        inputs = AjcTask.SOURCE_INPUTS;
        for (int i = 0; i < inputs.length; i++) {
            AjcTask task = getTask(NOFILE);
            task.setSource(inputs[i]);
            String[] cmd = task.makeCommand();
            checkContains(cmd, "-source", true);
            checkContains(cmd, inputs[i], true);
        }

        inputs = AjcTask.COMPLIANCE_INPUTS;
        for (int i = 0; i < inputs.length; i++) {
            AjcTask task = getTask(NOFILE);
            task.setCompliance(inputs[i]);
            String[] cmd = task.makeCommand();
            checkContains(cmd, inputs[i], true);
        }
	}
    
	public void testClasspath() {
		AjcTask task = getTask(NOFILE);
		String[] cmd = task.makeCommand();
        checkContains(cmd, "-bootclasspath", false);
		String classpath = null;
		for (int i = 0; i < cmd.length; i++) {
			if ("-classpath".equals(cmd[i])) {
				classpath = cmd[i + 1];
                break;
			}
		}
		assertTrue(
			"expecting aspectj in classpath",
			(-1 != classpath.indexOf("aspectjrt.jar")));
	}

	// ---------------------------------------- sourcefile
	// XXX need to figure out how to specify files directly programmatically
	//    public void testDefaultFile() {
	//        AjcTask task = getTask("testdata/Default.java");
	//        runTest(task, NO_EXCEPTION, MessageHolderChecker.INFOS);
	//    }

	public void testNoFile() {
		AjcTask task = getTask(NOFILE);
		task.setFailonerror(false);
		runTest(task, NO_EXCEPTION, MessageHolderChecker.ONE_ERROR_ONE_ABORT);
	}

	public void testCompileErrorFile() {
		AjcTask task = getTask("compileError.lst");
		task.setFailonerror(false);
		runTest(task, NO_EXCEPTION, MessageHolderChecker.ONE_ERROR);
	}

	public void testCompileWarningFile() {
		AjcTask task = getTask("compileWarning.lst");
		task.setFailonerror(false);
		runTest(task, NO_EXCEPTION, MessageHolderChecker.ONE_WARNING);
	}

	public void testNoSuchFile() {
		AjcTask task = getTask("NoSuchFile.lst");
		task.setFailonerror(false);
		runTest(task, NO_EXCEPTION, MessageHolderChecker.ONE_ERROR_ONE_ABORT);
	}

    public void testDefaultFileComplete() {
        AjcTask task = getTask("default.lst");
        defaultSettings(task);
        runTest(task, NO_EXCEPTION, MessageHolderChecker.INFOS);
    }
    private void defaultSettings(AjcTask task) {
        task.setDebugLevel("none");
        task.setDeprecation(true);
        task.setFailonerror(false);
        task.setNoExit(true); // ok to override Ant?
        task.setNoImportError(true);
        task.setNowarn(true);
        task.setXTerminateAfterCompilation(true);
        task.setPreserveAllLocals(true);
        task.setProceedOnError(true);
        task.setReferenceInfo(true);
        task.setSource("1.3");
        task.setTarget("1.1");
        task.setTime(true);
        task.setVerbose(true);
        task.setXlint("info");        
    }
    
    public void testLogCommand() {
        final String DEFAULT = "default.lst";
        AjcTask task = getTask(DEFAULT);
        defaultSettings(task);
        task.setVerbose(false);
        task.setLogCommand(true);
        LogListener listener = new LogListener(Project.MSG_INFO);
        task.getProject().addBuildListener(listener);
        runTest(task, NO_EXCEPTION, MessageHolderChecker.INFOS);
        String[] results = listener.getResults();
        boolean matched = false;
        for (int i = 0; !matched && (i < results.length); i++) {
            String s = results[i];
            matched = (null != s) && (-1 != s.indexOf(DEFAULT));
        }
        if (!matched) {
            fail(DEFAULT + " not found in " + Arrays.asList(results));
        }
    }

	public void testXOptions() {
		String[] xopts = new String[] {
			"serializableAspects",
			"lazyTjp",
			"reweavable",
			"reweavable:compress",
			"noInline"
		};
		for (int i = 0; i < xopts.length; i++) {
			AjcTask task = getTask(NOFILE);
			task.setX(xopts[i]);
			String[] cmd = task.makeCommand();
			checkContains(cmd,"-X" + xopts[i],true);
		}
		
	}
	
	public void testOutxml () {
        File destDir = getTempDir();
        assertTrue(
            "unable to create " + destDir,
            destDir.canRead() || destDir.mkdirs());
		AjcTask task = getTask("showweaveinfo.lst",destDir);
		task.setOutxml(true);
		checkRun(task,null);
		File outxmlFile = new File(destDir,"META-INF/aop.xml");
		assertTrue("META-INF/aop.xml missing",outxmlFile.exists());
	}
	
	public void testOutxmlFile () {
		String customName = "custom/aop.xml"; 
        File destDir = getTempDir();
        assertTrue(
            "unable to create " + destDir,
            destDir.canRead() || destDir.mkdirs());
		AjcTask task = getTask("showweaveinfo.lst",destDir);
		task.setOutxmlfile(customName);
		checkRun(task,null);
		File outxmlFile = new File(destDir,customName);
		assertTrue(customName + " missing",outxmlFile.exists());
	}

    // End of test cases
    // ------------------------------------------------------
    // ------------------------------------------------------
    // ------------------------------------------------------
    // ------------------------------------------------------
    // ------------------------------------------------------
    // ------------------------------------------------------
	protected void runTest(
		AjcTask task,
		Class exceptionType,
		MessageHolderChecker checker,
		String messageHolderClass) {
		task.setMessageHolderClass(messageHolderClass);
		runTest(task, exceptionType, checker, (MessageHandler) null);
	}

	protected void runTest(
		AjcTask task,
		Class exceptionType,
		MessageHolderChecker checker) {
		MessageHandler holder = new MessageHandler();
		task.setMessageHolder(holder);
		runTest(task, exceptionType, checker, holder);
	}

	protected void runTest(
		AjcTask task,
		Class exceptionType,
		MessageHolderChecker checker,
		MessageHandler holder) {
		Throwable thrown = null;
		// re-run forked iff tools.jar and expect to pass
		boolean rerunForked =
			((null != aspectjtoolsJar)
				&& (null == exceptionType)
				&& ((null == checker) || !checker.expectFail()));
		String label = "same-vm ";
		while (true) { // same vm, then perhaps forked   
			try {
				task.execute();
			} catch (Throwable t) {
				thrown = t;
			} finally {
				deleteTempDir();
			}
			if (null == exceptionType) {
				if (null != thrown) {
					assertTrue(label + "thrown: " + render(thrown), false);
				}
			} else if (null == thrown) {
				assertTrue(
					label + "expected " + exceptionType.getName(),
					false);
			} else if (!(exceptionType.isAssignableFrom(thrown.getClass()))) {
				assertTrue(
					label
						+ "expected "
						+ exceptionType.getName()
						+ " got "
						+ render(thrown),
					false);
			}
			if (null != holder) {
				if (null == checker) {
					checker = MessageHolderChecker.NONE;
				}
				checker.check(holder, label);
			}
			if (!rerunForked) {
				break;
			} else {
				label = "other-vm ";
				rerunForked = false;
				// can't reset without losing values...
				task.setFork(true);
				task.setFailonerror(true);
				task.setForkclasspath(
					new Path(task.getProject(), aspectjtoolsJar));
			}
		}
	}

	protected String render(Throwable thrown) {
		return LangUtil.renderException(thrown);
	}

	static class MessageHolderChecker { // XXX export to testing-utils
		/** use as value to ignore results */
		static int IGNORE = Integer.MIN_VALUE;

		static MessageHolderChecker NONE =
			new MessageHolderChecker(0, 0, 0, 0, 0);
		/** any number (0+) of info messages */
		static MessageHolderChecker INFOS =
			new MessageHolderChecker(0, 0, 0, 0, IGNORE);
		/** one error, any number of info messages */
		static MessageHolderChecker ONE_ERROR =
			new MessageHolderChecker(0, 0, 1, 0, IGNORE);
		static MessageHolderChecker ONE_ERROR_ONE_ABORT =
			new MessageHolderChecker(1, 0, 1, 0, IGNORE);
		/** one warning, any number of info messages */
		static MessageHolderChecker ONE_WARNING =
			new MessageHolderChecker(0, 0, 0, 1, IGNORE);
		

		int aborts, fails, errors, warnings, infos;
		int weaveinfos;
		public MessageHolderChecker(
			int aborts,
			int fails,
			int errors,
			int warnings,
			int infos) {
			this.aborts = aborts;
			this.fails = fails;
			this.errors = errors;
			this.warnings = warnings;
			this.infos = infos;
			this.weaveinfos = IGNORE;
		}

		public boolean expectFail() {
			return (0 < (aborts + fails + errors));
		}

		public void check(IMessageHolder holder, String label) {
			boolean failed = true;
			try {
				check(holder, aborts, IMessage.ABORT);
				check(holder, fails, IMessage.FAIL);
				check(holder, errors, IMessage.ERROR);
				check(holder, warnings, IMessage.WARNING);
				check(holder, infos, IMessage.INFO);
				check(holder, weaveinfos, IMessage.WEAVEINFO);
				failed = false;
			} finally {
				if (failed) {
					MessageUtil.print(System.err, holder, label + "failed?");
				}
			}
		}

		private void check(
			IMessageHolder holder,
			int num,
			IMessage.Kind kind) {
			if (num != IGNORE) {
				int actual = holder.numMessages(kind, false);
				if (num != actual) {
					if (actual > 0) {
						MessageUtil.print(
							System.err,
							holder,
							kind + " expected " + num + " got " + actual);
					}
					assertEquals(kind.toString(), num, actual);
				}
			}
		}
	}
    private static class LogListener implements BuildListener {
        private final ArrayList results = new ArrayList();
        private final int priority;
        private LogListener(int priority) {
            this.priority = priority;
        }
        public void buildFinished(BuildEvent event) {}
        public void buildStarted(BuildEvent event) {}
        public void messageLogged(BuildEvent event) {
            if (priority == event.getPriority()) {
                results.add(event.getMessage());
            }
        }
        public void targetFinished(BuildEvent event) {}
        public void targetStarted(BuildEvent event) {}
        public void taskFinished(BuildEvent event) {}
        public void taskStarted(BuildEvent event) {}
        String[] getResults() {
            return (String[]) results.toArray(new String[0]);
        }
    }
}
class SnoopingCommandEditor implements ICommandEditor {
	private static final String[] NONE = new String[0];
	String[] lastCommand;
	public String[] editCommand(String[] command) {
		lastCommand = (String[]) LangUtil.safeCopy(command, NONE);
		return command;
	}
	public String[] lastCommand() {
		return (String[]) LangUtil.safeCopy(lastCommand, NONE);
	}
}
class VerboseCommandEditor implements ICommandEditor {
	public static final String VERBOSE = "-verbose";
	public String[] editCommand(String[] command) {
		for (int i = 0; i < command.length; i++) {
			if (VERBOSE.equals(command[i])) {
				return command;
			}
		}

		String[] result = new String[1 + command.length];
		result[0] = VERBOSE;
		System.arraycopy(result, 1, command, 0, command.length);
		return result;
	}
}

class AppendingCommandEditor implements ICommandEditor {
	private static String[] NONE = new String[0];
	public static ICommandEditor VERBOSE =
		new AppendingCommandEditor(new String[] { "-verbose" }, NONE);
	public static ICommandEditor INVALID =
		new AppendingCommandEditor(NONE, new String[] { "-invalidOption" });

	final String[] prefix;
	final String[] suffix;

	public AppendingCommandEditor(String[] prefix, String[] suffix) {
		this.prefix = prefix;
		this.suffix = suffix;
	}

	public String[] editCommand(String[] command) {
		int len = command.length + prefix.length + suffix.length;
		String[] result = new String[len];
		System.arraycopy(result, 0, prefix, 0, prefix.length);
		System.arraycopy(result, prefix.length, command, 0, command.length);
		System.arraycopy(
			result,
			prefix.length + command.length,
			suffix,
			0,
			suffix.length);
		return result;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3425.java
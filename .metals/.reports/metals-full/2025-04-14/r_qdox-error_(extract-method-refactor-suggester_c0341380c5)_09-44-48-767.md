error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15028.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15028.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15028.java
text:
```scala
a@@ddNextAsClasspath = true;

/* *******************************************************************
 * Copyright (c) 1999-2001 Xerox Corporation, 
 *               2002 Palo Alto Research Center, Incorporated (PARC).
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Common Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html 
 *  
 * Contributors: 
 *     Xerox/PARC     initial implementation 
 *     Mik Kersten	  port to AspectJ 1.1+ code base
 * ******************************************************************/

package org.aspectj.tools.ajdoc;

import java.io.*;
import java.util.*;

import java.io.FileFilter;

import org.aspectj.bridge.Version;
import org.aspectj.util.FileUtil;

/**
 * This is an old implementation of ajdoc that does not use an OO style.  However, it 
 * does the job, and should serve to evolve a lightweight ajdoc implementation until
 * we can make a properly extended javadoc implementation.
 * 
 * @author Mik Kersten
 */
public class Main implements Config {

	private static final String FAIL_MESSAGE =  "> compile failed, exiting ajdoc";
	
    static SymbolManager symbolManager = null;

    /** Command line options. */
    static Vector options;

    /** Options to pass to ajc. */
    static Vector ajcOptions;

    /** All of the files to be processed by ajdoc. */
    static Vector filenames;

    /** List of files to pass to javadoc. */
    static Vector fileList;

    /** List of packages to pass to javadoc. */
    static Vector packageList;

    /** Default to package visiblity. */
    static String docModifier = "package";

    static Vector sourcepath = new Vector();

    static boolean verboseMode = false;
    static boolean packageMode = false;
    static boolean authorStandardDocletSwitch = false;
    static boolean versionStandardDocletSwitch = false;
    static File    rootDir       = null;
    static Hashtable declIDTable   = new Hashtable();
    static String  docDir          = ".";
    
    private static boolean deleteTempFilesOnExit = true;
    
	private static boolean aborted = false;

    public static void clearState() {
        symbolManager = null;
        options = new Vector();
        ajcOptions = new Vector();
        filenames = new Vector();
        fileList= new Vector();
        packageList = new Vector();
        docModifier = "package";
        sourcepath = new Vector();
        verboseMode = false;
        packageMode = false;
        rootDir       = null;
        declIDTable   = new Hashtable();
        docDir          = ".";
    }

    public static void main(String[] args) {
    	aborted = false;
    	deleteTempFilesOnExit = true;
   
        filenames = new Vector();
        fileList= new Vector();
        packageList = new Vector();
        options = new Vector();
        ajcOptions = new Vector();
//    	if (!JavadocRunner.has14ToolsAvailable()) {
//    		System.err.println("ajdoc requires a JDK 1.4 or later tools jar - exiting");
//    		aborted = true;
//    		return;
//    	}
    	  
        // STEP 1: parse the command line and do other global setup
        sourcepath.addElement("."); // add the current directory to the classapth
        parseCommandLine(args);  
        rootDir = getRootDir();
        symbolManager = SymbolManager.getDefault();
        File[] inputFiles      = new File[filenames.size()];
        File[] signatureFiles  = new File[filenames.size()];
        try {
            // create the workingdir if it doesn't exist
            if ( !(new File( Config.WORKING_DIR ).isDirectory()) ) {
                File dir = new File( Config.WORKING_DIR );
                dir.mkdir();
                if (deleteTempFilesOnExit) dir.deleteOnExit();
            }

            for (int i = 0; i < filenames.size(); i++) {
                inputFiles[i]     = findFile((String)filenames.elementAt(i));
                //signatureFiles[i] = createSignatureFile(inputFiles[i]);
            }

            // PHASE 0: call ajc
            ajcOptions.addElement("-noExit");
			ajcOptions.addElement("-XjavadocsInModel");  	// TODO: wrong option to force model gen
            ajcOptions.addElement("-d"); 
            ajcOptions.addElement(rootDir.getAbsolutePath());
			String[] argsToCompiler = new String[ajcOptions.size() + inputFiles.length];
            int i = 0;
            for ( ; i < ajcOptions.size(); i++ ) {
                argsToCompiler[i] = (String)ajcOptions.elementAt(i);
            }
            for ( int j = 0; j < inputFiles.length; j++) {
                argsToCompiler[i] = inputFiles[j].getAbsolutePath();
                //System.out.println(">> file to ajc: " + inputFiles[j].getAbsolutePath());
                i++;
            }

//            System.out.println(Arrays.asList(argsToCompiler));
            System.out.println( "> Calling ajc..." );
            CompilerWrapper.main(argsToCompiler);
            if (CompilerWrapper.hasErrors()) {
            	System.out.println(FAIL_MESSAGE);
            	aborted = true;
            	return;
            }
/*
            for (int ii = 0; ii < inputFiles.length; ii++) {
                String tempFP = inputFiles[ii].getAbsolutePath();
                tempFP = tempFP.substring(0, tempFP.length()-4);
                tempFP += "ajsym";
                System.out.println( ">> checking: " + tempFP);
                File tempF = new File(tempFP);
                if ( !tempF.exists() ) System.out.println( ">>> doesn't exist!" );
            }
*/
            for (int ii = 0; ii < filenames.size(); ii++) {
                signatureFiles[ii] = createSignatureFile(inputFiles[ii]);
            }

            // PHASE 1: generate Signature files (Java with DeclIDs and no bodies).
            System.out.println( "> Building signature files..." );
            StubFileGenerator.doFiles(declIDTable, symbolManager, inputFiles, signatureFiles);

            // PHASE 2: let Javadoc generate HTML (with DeclIDs)
            System.out.println( "> Calling javadoc..." );
            String[] javadocargs = null;
            if ( packageMode ) {
                int numExtraArgs = 2;
                if (authorStandardDocletSwitch) numExtraArgs++;
                if (versionStandardDocletSwitch) numExtraArgs++;
                javadocargs = new String[numExtraArgs + options.size() + packageList.size() +
                                         fileList.size() ];
                javadocargs[0] = "-sourcepath";
                javadocargs[1] = Config.WORKING_DIR;
                int argIndex = 2;
                if (authorStandardDocletSwitch) {
                    javadocargs[argIndex] = "-author";
                    argIndex++;
                }
                if (versionStandardDocletSwitch) {
                    javadocargs[argIndex] = "-version";
                }
                //javadocargs[1] = getSourcepathAsString();
                for (int k = 0; k < options.size(); k++) {
                    javadocargs[numExtraArgs+k] = (String)options.elementAt(k);
                }
                for (int k = 0; k < packageList.size(); k++) {
                    javadocargs[numExtraArgs+options.size() + k] = (String)packageList.elementAt(k);
                }
                for (int k = 0; k < fileList.size(); k++) {
                    javadocargs[numExtraArgs+options.size() + packageList.size() + k] = (String)fileList.elementAt(k);
                }
            }
            else {
                javadocargs = new String[options.size() + signatureFiles.length];
                for (int k = 0; k < options.size(); k++) {
                    javadocargs[k] = (String)options.elementAt(k);
                }
                for (int k = 0; k < signatureFiles.length; k++) {
                    javadocargs[options.size() + k] = signatureFiles[k].getCanonicalPath();
                }
            }
         
            JavadocRunner.callJavadoc(javadocargs);
            //for ( int o = 0; o < inputFiles.length; o++ ) {
            //    System.out.println( "file: " + inputFiles[o] );
            //}

            // PHASE 3: add AspectDoc specific stuff to the HTML (and remove the DeclIDS).
            /** We start with the known HTML files (the ones that correspond directly to the
            * input files.)  As we go along, we may learn that Javadoc split one .java file
            * into multiple .html files to handle inner classes or local classes.  The html
            * file decorator picks that up.
            */
            System.out.println( "> Decorating html files..." );
            HtmlDecorator.decorateHTMLFromInputFiles(declIDTable,
                                              rootDir,
                                              symbolManager,
                                              inputFiles,
                                              docModifier); 
            
            System.out.println( "> Removing generated tags (this may take a while)..." );
            removeDeclIDsFromFile("index-all.html", true);
            removeDeclIDsFromFile("serialized-form.html", true);
            if (packageList.size() > 0) {
	            for (int p = 0; p < packageList.size(); p++) {
	                removeDeclIDsFromFile(((String)packageList.elementAt(p)).replace('.','/') +
	                                       Config.DIR_SEP_CHAR +
	                                       "package-summary.html", true);
	            }
            } else {
            	File[] files = FileUtil.listFiles(rootDir, new FileFilter() {
            		public boolean accept(File f) {
						return f.getName().equals("package-summary.html");
					}
            	});
            	for (int j = 0; j < files.length; j++) {
            		removeDeclIDsFromFile(files[j].getAbsolutePath(), false);
            	}
            }
            System.out.println( "> Finished." );
        } catch (Throwable e) {
            handleInternalError(e);
            exit(-2);
        }
    }
    
    private static void removeDeclIDsFromFile(String filename, boolean relativePath) {
        // Remove the decl ids from "index-all.html"
        File indexFile;
        if (relativePath) {
        	indexFile = new File(docDir + Config.DIR_SEP_CHAR + filename);
        } else {
        	indexFile = new File(filename);
        }
        try {
        if ( indexFile.exists() ) {
            BufferedReader indexFileReader = new BufferedReader( new FileReader( indexFile ) );
            String indexFileBuffer = "";
            String line = indexFileReader.readLine();
            while ( line != null ) {
              int indexStart = line.indexOf( Config.DECL_ID_STRING );
              int indexEnd   = line.indexOf( Config.DECL_ID_TERMINATOR );
              if ( indexStart != -1 && indexEnd != -1 ) {
                line = line.substring( 0, indexStart ) +
                       line.substring( indexEnd+Config.DECL_ID_TERMINATOR.length() );
              }
              indexFileBuffer += line;
              line = indexFileReader.readLine();
            }
            FileOutputStream fos = new FileOutputStream( indexFile );
            fos.write( indexFileBuffer.getBytes() );
        }
        }
        catch (IOException ioe) {
              // be siltent
        }
    }

    /**
     * If the file doesn't exist on the specified path look for it in all the other
     * paths specified by the "sourcepath" option.
     */
    static File findFile( String filename ) throws IOException {

        return new File( filename );
        /*
        File file = new File(filename);
        if (file.exists()) {
            return file;
        }
        else {
            for ( int i = 0; i < sourcePath.size(); i++ ) {
                File currPath = new File((String)sourcePath.elementAt(i));
                File currFile = new File(currPath + "/" + filename); // !!!
                if ( file.exists()) {
                    return file;
                }
            }
        }
        throw new IOException("couldn't find source file: " + filename);
        */
    }

    static Vector getSourcePath() {
        Vector  sourcePath = new Vector();
        boolean found      = false;
        for ( int i = 0; i < options.size(); i++ ) {
            String currOption = (String)options.elementAt(i);
            if (found && !currOption.startsWith("-")) {
                sourcePath.add(currOption);
            }
            if (currOption.equals("-sourcepath")) {
                found = true;
            }
        }
        return sourcePath;
    }

    static File getRootDir() {
        File rootDir = new File( "." );
        for ( int i = 0; i < options.size(); i++ ) {
            if ( ((String)options.elementAt(i)).equals( "-d" ) ) {
                rootDir = new File((String)options.elementAt(i+1));
                if ( !rootDir.exists() ) {
                    System.out.println( "Destination directory not found: " +
                                        (String)options.elementAt(i+1) );
                    System.exit( -1 );
                }
            }
        }
        return rootDir;
    }

    static File createSignatureFile(File inputFile) throws IOException {
    	String packageName = StructureUtil.getPackageDeclarationFromFile(inputFile);
    	
        String filename    = "";
        if ( packageName != null ) {
            String pathName =  Config.WORKING_DIR + '/' + packageName.replace('.', '/');
            File packageDir = new File(pathName);
            if ( !packageDir.exists() ) {
                packageDir.mkdirs();
                if (deleteTempFilesOnExit) packageDir.deleteOnExit();
            }
            //verifyPackageDirExists(packageName, null);
            packageName = packageName.replace( '.','/' ); // !!!
            filename = Config.WORKING_DIR + Config.DIR_SEP_CHAR + packageName +
                       Config.DIR_SEP_CHAR + inputFile.getName();
        }
        else {
            filename = Config.WORKING_DIR + Config.DIR_SEP_CHAR + inputFile.getName();
        }
        File signatureFile = new File( filename );
        if (deleteTempFilesOnExit) signatureFile.deleteOnExit();
        return signatureFile;
    }


//    static void verifyPackageDirExists( String packageName, String offset ) {
//        System.err.println(">>> name: " + packageName + ", offset: " + offset);
//        if ( packageName.indexOf( "." ) != -1 ) {
//            File tempFile = new File("c:/aspectj-test/d1/d2/d3");
//            tempFile.mkdirs();
//            String currPkgDir = packageName.substring( 0, packageName.indexOf( "." ) );
//            String remainingPkg = packageName.substring( packageName.indexOf( "." )+1 );
//            String filePath = null;
//            if ( offset != null ) {
//                filePath = Config.WORKING_DIR + Config.DIR_SEP_CHAR +
//                           offset + Config.DIR_SEP_CHAR + currPkgDir ;
//            }
//            else {
//                filePath = Config.WORKING_DIR + Config.DIR_SEP_CHAR + currPkgDir;
//            }
//            File packageDir = new File( filePath );
//            if ( !packageDir.exists() ) {
//                packageDir.mkdir();
//                if (deleteTempFilesOnExit) packageDir.deleteOnExit();
//            }
//            if ( remainingPkg != "" ) {
//                verifyPackageDirExists( remainingPkg, currPkgDir );
//            }
//        }
//        else {
//            String filePath = null;
//            if ( offset != null ) {
//                filePath = Config.WORKING_DIR + Config.DIR_SEP_CHAR + offset + Config.DIR_SEP_CHAR + packageName;
//            }
//            else {
//                filePath = Config.WORKING_DIR + Config.DIR_SEP_CHAR + packageName;
//            }
//            File packageDir = new File( filePath );
//            if ( !packageDir.exists() ) {
//                packageDir.mkdir();
//                if (deleteTempFilesOnExit) packageDir.deleteOnExit();
//            }
//        }
//    }

    /**
     * Can read Eclipse-generated single-line arg
     */
    static void parseCommandLine(String[] args) {
        if (args.length == 0) {
            displayHelpAndExit( null );
        } else if (args.length == 1 && args[0].startsWith("@")) {
        	String argFile = args[0].substring(1);
        	System.out.println("> Using arg file: " + argFile);  
        	BufferedReader br;
			try {
				br = new BufferedReader(new FileReader(argFile));
				String line = "";
	        	line = br.readLine();
	        	StringTokenizer st = new StringTokenizer(line, " ");
	        	List argList = new ArrayList();
	        	while(st.hasMoreElements()) {
	        		argList.add((String)st.nextElement());
	        	}
	        	//System.err.println(argList);
	        	args = new String[argList.size()];
	        	int counter = 0;
	        	for (Iterator it = argList.iterator(); it.hasNext(); ) {
	        		args[counter] = (String)it.next();
	        		counter++;
	        	}
			} catch (FileNotFoundException e) {
				System.err.println("> could not read arg file: " + argFile);
				e.printStackTrace();
			} catch (IOException ioe) {
				System.err.println("> could not read arg file: " + argFile);
				ioe.printStackTrace();
			}

        	
        }
        List vargs = new LinkedList(Arrays.asList(args));

        parseArgs(vargs, new File( "." ));  // !!!

        if (filenames.size() == 0) {
            displayHelpAndExit( "ajdoc: No packages or classes specified" );
        }
    }

    static void setSourcepath(String arg) {
           sourcepath.clear();
           arg = arg + ";"; // makes things easier for ourselves
           StringTokenizer tokenizer = new StringTokenizer(arg, ";");
           while (tokenizer.hasMoreElements()) {
                 sourcepath.addElement(tokenizer.nextElement());
           }
    }

    static String getSourcepathAsString() {
       String cPath = "";
       for (int i = 0; i < sourcepath.size(); i++) {
           cPath += (String)sourcepath.elementAt(i) + Config.DIR_SEP_CHAR + Config.WORKING_DIR;
           if (i != sourcepath.size()-1) {
              cPath += ";";
           }
       }
       return cPath;
    }

    static void parseArgs(List vargs, File currentWorkingDir) {
        boolean addNextAsOption     = false;
        boolean addNextAsArgFile    = false;
        boolean addNextToAJCOptions = false;
        boolean addNextAsDocDir     = false;
        boolean addNextAsClasspath  = false;
        boolean ignoreArg           = false;  // used for discrepancy betwen class/sourcepath in ajc/javadoc
        boolean addNextAsSourcePath = false;
        if ( vargs.size() == 0 ) {
            displayHelpAndExit( null );
        }
        for (int i = 0; i < vargs.size() ; i++) {
            String arg = (String)vargs.get(i);
            ignoreArg = false;
            if ( addNextToAJCOptions ) {
                ajcOptions.addElement( arg );
                addNextToAJCOptions = false;
            }
            if ( addNextAsDocDir ) {
                docDir = arg;
                addNextAsDocDir = false;
            }
            if ( addNextAsClasspath ) {
                addNextAsClasspath = false;
            }
            if ( addNextAsSourcePath ) {
                setSourcepath( arg );
                addNextAsSourcePath = false;
                ignoreArg = true;
            }
            if ( arg.startsWith("@") ) {
                expandAtSignFile(arg.substring(1), currentWorkingDir);
            }
            else if ( arg.equals( "-argfile" ) ) {
                addNextAsArgFile = true;
            }
            else if ( addNextAsArgFile ) {
                expandAtSignFile(arg, currentWorkingDir);
                addNextAsArgFile = false;
            }
            else if (arg.equals("-d") ) {
                addNextAsOption = true;
                options.addElement(arg);
                addNextAsDocDir = true;
            }
            else if ( arg.equals( "-bootclasspath" ) ) {
                addNextAsOption = true;
                addNextToAJCOptions = true;
                options.addElement( arg );
                ajcOptions.addElement( arg );
            } 
            else if ( arg.equals( "-source" ) ) {
                addNextAsOption = true;
                addNextToAJCOptions = true;
                addNextAsClasspath = false;
                options.addElement(arg);
                ajcOptions.addElement(arg);
            }
            else if ( arg.equals( "-classpath" ) ) {
                addNextAsOption = true;
                addNextToAJCOptions = true;
                addNextAsClasspath = true;
                options.addElement( arg );
                ajcOptions.addElement( arg );
            }
            else if ( arg.equals( "-sourcepath" ) ) {
                addNextAsSourcePath = true;
                //options.addElement( arg );
                //ajcOptions.addElement( arg );
            }
            else if (arg.equals("-XajdocDebug")) {
            	deleteTempFilesOnExit = false;
            } 
            else if (arg.equals("-use")) {
            	System.out.println("> Ignoring unsupported option: -use");
            } 
            else if (arg.equals("-splitindex")) {
            	// passed to javadoc
            } 
            else if (arg.startsWith("-") || addNextAsOption) {
                if ( arg.equals( "-private" ) ) {
                    docModifier = "private";
                }else if ( arg.equals( "-package" ) ) {
                    docModifier = "package";
                } else if ( arg.equals( "-protected" ) ) {
                    docModifier = "protected";
                } else if ( arg.equals( "-public" ) ) {
                    docModifier = "public";
                } else if ( arg.equals( "-verbose" ) ) {
                    verboseMode = true;
                } else if ( arg.equals( "-author" ) ) {
                    authorStandardDocletSwitch = true;
                } else if ( arg.equals( "-version" ) ) {
                    versionStandardDocletSwitch = true;
                } else if ( arg.equals( "-v" ) ) {
                    System.out.println(getVersion());
                    exit(0);
                } else if ( arg.equals( "-help" ) ) {
                    displayHelpAndExit( null );
                } else if ( arg.equals( "-doclet" ) || arg.equals( "-docletpath" ) ) {
                    System.out.println( "The doclet and docletpath options are not currently supported    \n" +
                                        "since ajdoc makes assumptions about the behavior of the standard \n" +
                                        "doclet. If you would find this option useful please email us at: \n" +
                                        "                                                                 \n" +
                                        "       aspectj-dev@eclipse.org                            \n" +
                                        "                                                                 \n" );
                    exit(0);
                } else if (arg.equals("-nonavbar")
 arg.equals("-noindex")) {
                	// pass through 
                	//System.err.println("> ignoring unsupported option: " + arg);
                } else if ( addNextAsOption ) {
                    //  pass through
                } else {
                	System.err.println("> unrecognized argument: " + arg);
                    displayHelpAndExit( null );
                }  
                options.addElement(arg);
                addNextAsOption = false;
            } 
            else {
                // check if this is a file or a package
                if ( arg.indexOf( ".java" ) == arg.length() - 5 ||
                     arg.indexOf( ".lst" ) == arg.length() - 4 &&
                     arg != null ) {
                    File f = new File(arg);
                    if (f.isAbsolute()) {
                         filenames.addElement(arg);
                    }
                    else {
                         filenames.addElement( currentWorkingDir + Config.DIR_SEP_CHAR + arg );
                    }
                    fileList.addElement( arg );
                }

                // PACKAGE MODE STUFF
                else if (!ignoreArg) {
                    packageMode = true;
                    packageList.addElement( arg );
                    arg = arg.replace( '.', '/' );  // !!!

                    // do this for every item in the classpath
                    for ( int c = 0; c < sourcepath.size(); c++ ) {
                      String path = (String)sourcepath.elementAt(c) + Config.DIR_SEP_CHAR + arg;
                      File pkg = new File(path);
                      if ( pkg.isDirectory() ) {
                          String[] files = pkg.list( new FilenameFilter() {
                              public boolean accept( File dir, String name )  {
                                  int index1 = name.lastIndexOf( "." );
                                  int index2 = name.length();
                                  if ( (index1 >= 0 && index2 >= 0 ) &&
                                        (name.substring(index1, index2).equals( ".java" ) ) ) {
                                      return true;
                                  }
                                  else  {
                                      return false;
                                  }
                              }
                             } );
                          for ( int j = 0; j < files.length; j++ ) {
                              filenames.addElement( (String)sourcepath.elementAt(c) +
                                                    Config.DIR_SEP_CHAR +
                                                    arg + Config.DIR_SEP_CHAR + files[j] );
                          }
                      }
                      else if (c == sourcepath.size() ) { // last element on classpath
                          System.out.println( "ajdoc: No package, class, or source file " +
                                              "found named " + arg + "." );
                      }
                      else {
                           // didn't find it on that element of the classpath but that's ok
                      }
                    }  
                }
            }
        }
        // set the default visibility as an option to javadoc option
        if ( !options.contains( "-private" ) &&
             !options.contains( "-package" ) &&
             !options.contains( "-protected" ) &&
             !options.contains( "-public" ) ) {
            options.addElement( "-package" );
        }

    }

    static void expandAtSignFile(String filename, File currentWorkingDir) {
        List result = new LinkedList();

        File atFile = qualifiedFile(filename, currentWorkingDir);
        String atFileParent = atFile.getParent();
        File myWorkingDir = null;
        if (atFileParent != null) myWorkingDir = new File(atFileParent);

        try {
            BufferedReader stream = new BufferedReader(new FileReader(atFile));
            String line = null;
            while ( (line = stream.readLine()) != null) {
                // strip out any comments of the form # to end of line
                int commentStart = line.indexOf("//");
                if (commentStart != -1) {
                    line = line.substring(0, commentStart);
                }

                // remove extra whitespace that might have crept in
                line = line.trim();
                // ignore blank lines
                if (line.length() == 0) continue;
                result.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error while reading the @ file " + atFile.getPath() + ".\n"
                               + e);
            System.exit( -1 );
        }

        parseArgs(result, myWorkingDir);
    }

    static File qualifiedFile(String name, File currentWorkingDir) {
        name = name.replace('/', File.separatorChar);
        File file = new File(name);
        if (!file.isAbsolute() && currentWorkingDir != null) {
            file = new File(currentWorkingDir, name);
        }
        return file;
    }


    static void displayHelpAndExit(String message) {
        if (message != null) System.err.println(message);
        System.err.println();
        System.err.println(Config.USAGE);
        exit(0);
    }

    static protected void exit(int value) {
        System.out.flush();
        System.err.flush();
        System.exit(value);
    }

    /* This section of code handles errors that occur during compilation */
    static final String internalErrorMessage =
                                              "Please copy the following text into an email message and send it,\n" +
                                              "along with any additional information you can add to:            \n" +
                                              "                                                                 \n" +
                                              "       aspectj-dev@eclipse.org                           \n" +
                                              "                                                                 \n";

    static public void handleInternalError(Throwable uncaughtThrowable) {
        System.err.println("An internal error occured in ajdoc");
        System.err.println(internalErrorMessage);
        System.err.println(uncaughtThrowable.toString());
        uncaughtThrowable.printStackTrace();
        System.err.println();
    }

    static String getVersion() {
        return "ajdoc version " + Version.text;
    }
    
	public static boolean hasAborted() {
		return aborted;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15028.java
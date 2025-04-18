error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9046.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9046.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9046.java
text:
```scala
static b@@oolean tryJikes=false;

/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999 The Apache Software Foundation.  All rights 
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer. 
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:  
 *       "This product includes software developed by the 
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Tomcat", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written 
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */
package org.apache.tomcat.facade;

import javax.servlet.*;
import javax.servlet.http.*;

import javax.servlet.jsp.HttpJspPage;
import javax.servlet.jsp.JspFactory;

import java.util.*;
import java.io.*;
import java.net.*;

import org.apache.tomcat.util.log.Log;
import org.apache.tomcat.util.res.StringManager;
import org.apache.tomcat.util.depend.*;
import org.apache.tomcat.util.compat.*;

import org.apache.jasper.*;
import org.apache.jasper.Constants;
import org.apache.jasper.runtime.*;
import org.apache.jasper.compiler.*;
import org.apache.jasper.compiler.Compiler;
import org.apache.tomcat.core.*;
import org.apache.tomcat.facade.*;

import org.apache.tomcat.util.compat.Jdk11Compat;
/**
 * Plug in the JSP engine (a.k.a Jasper)!
 * Tomcat uses a "built-in" mapping for jsps ( *.jsp -> jsp ). "jsp"
 * can be either a real servlet (JspServlet) that compiles the jsp
 * and include the resource, or we can "intercept" and do the
 * compilation and mapping in requestMap stage.
 *
 * JspInterceptor will be invoked once per jsp, and will add an exact
 * mapping - all further invocation are identical with servlet invocations
 * with direct maps, with no extra overhead.
 *
 * Future - better abstraction for jsp->java converter ( jasper ), better
 * abstraction for java->class, plugin other jsp implementations,
 * better scalability.
 *
 * @author Anil K. Vijendran
 * @author Harish Prabandham
 * @author Costin Manolache
 */
public class JspInterceptor extends BaseInterceptor {
    static final String JIKES="org.apache.jasper.compiler.JikesJavaCompiler";
    static final String JSP_SERVLET="org.apache.jasper.servlet.JspServlet";
    
    Properties args=new Properties(); // args for jasper
    boolean useJspServlet=false; 
    String jspServletCN=JSP_SERVLET;
    String runtimePackage;
    
    // -------------------- Jasper options --------------------
    // Options that affect jasper functionality. Will be set on
    // JspServlet ( if useJspServlet="true" ) or TomcatOptions.
    // IMPORTANT: periodically test for new jasper options
    
    /**
     * Are we keeping generated code around?
     */
    public void setKeepGenerated( String s ) {
	args.put( "keepgenerated", s );
    }

    /**
     * Are we supporting large files?
     */
    public void setLargeFile( String s ) {
	args.put( "largefile", s );
    }

    /**
     * Are we supporting HTML mapped servlets?
     */
    public void setMappedFile( String s ) {
	args.put( "mappedfile", s );
    }

    /**
     * Should errors be sent to client or thrown into stderr?
     */
    public void setSendErrToClient( String s ) {
	args.put( "sendErrToClient", s );
    }

    /**
     * Class ID for use in the plugin tag when the browser is IE. 
     */
    public void setIEClassId( String s ) {
	args.put( "ieClassId", s );
    }

    /**
     * What classpath should I use while compiling the servlets
     * generated from JSP files?
     */
    public void setClassPath( String s ) {
	args.put( "classpath", s );
    }

    /**
     * What is my scratch dir?
     */
    public void setScratchdir( String s ) {
	args.put( "scratchdir", s );
    }

    /**
     * Path of the compiler to use for compiling JSP pages.
     */
    public void setJspCompilerPath( String s ) {
	args.put( "jspCompilerPath", s );
    }

    /**
     * What compiler plugin should I use to compile the servlets
     * generated from JSP files?
     * @deprecated Use setJavaCompiler instead
     */
    public void setJspCompilerPlugin( String s ) {
	args.put( "jspCompilerPlugin", s );
    }

    /** Include debug information in generated classes
     */
    public void setClassDebugInfo( String s ) {
	args.put("classDebugInfo", s );
    }
    
    public void setProperty( String n, String v ) {
	args.put( n, v );
    }
    // -------------------- JspInterceptor properties --------------------

    /** Use the old JspServlet to execute Jsps, instead of the
	new code. Note that init() never worked (AFAIK) and it'll
	be slower - but given the stability of JspServlet it may
	be a safe option. This will significantly slow down jsps.
	Default is false.
    */
    public void setUseJspServlet( boolean b ) {
	useJspServlet=b;
    }

    /** Specify the implementation class of the jsp servlet.
     */
    public void setJspServlet( String  s ) {
	jspServletCN=s;
    }

    /**
     * What compiler should I use to compile the servlets
     * generated from JSP files? Default is "javac" ( you can use
     * "jikes" as a shortcut ).
     */
    public void setJavaCompiler( String type ) {
	if( "jikes".equals( type ) )
	    type=JIKES;
	if( "javac".equals( type ) )
	    type="org.apache.jasper.compiler.SunJavaCompiler";
		
	args.put( "jspCompilerPlugin", type );
    }

    int pageContextPoolSize=JspFactoryImpl.DEFAULT_POOL_SIZE;
    /** Set the PageContext pool size for jasper factory.
	0 will disable pooling of PageContexts.
     */
    public void setPageContextPoolSize(int i) {
	pageContextPoolSize=i;
    }

    /** The generator will produce code using a different
	runtime ( default is org.apache.jasper.runtime ).
	The runtime must use the same names for classes as the
	default one, so the code will compile.
    */
    public void setRuntimePackage(String rp ) {
	runtimePackage=rp;
    }
    
    // -------------------- Hooks --------------------

    /**
     * Jasper-specific initializations, add work dir to classpath,
     */
    public void addContext(ContextManager cm, Context ctx)
	throws TomcatException 
    {
	if( runtimePackage!=null ) {
	    Constants.JSP_RUNTIME_PACKAGE=runtimePackage;
	    Constants.JSP_SERVLET_BASE=runtimePackage+".HttpJspBase";
	}

	JspFactoryImpl factory=new JspFactoryImpl(pageContextPoolSize);
	
	JspFactory.setDefaultFactory(factory);

	// jspServlet uses it's own loader. We need to add workdir
	// to the context classpath to use URLLoader and normal
	// operation
	// XXX alternative: use WEB-INF/classes for generated files 
	if( ! useJspServlet ) {
	    try {
		// Note: URLClassLoader in JDK1.2.2 doesn't work with file URLs
		// that contain '\' characters.  Insure only '/' is used.
		// jspServlet uses it's own mechanism
		URL url=new URL( "file", null,
		 ctx.getWorkDir().getAbsolutePath().replace('\\','/') + "/");
		ctx.addClassPath( url );
		if( debug > 9 ) log( "Added to classpath: " + url );
	    } catch( MalformedURLException ex ) {
	    }
	}
    }

    /** Do the needed initialization if jspServlet is used.
     *  It must be called after Web.xml is read ( WebXmlReader ).
     */
    public void contextInit(Context ctx)
	throws TomcatException
    {
	if( useJspServlet ) {
	    // prepare jsp servlet. 
	    Handler jasper=ctx.getServletByName( "jsp" );
	    if ( debug>10) log( "Got jasper servlet " + jasper );

	    ServletHandler jspServlet=(ServletHandler)jasper;
	    if( jspServlet.getServletClassName() == null ) {
		log( "Jsp already defined in web.xml " +
		     jspServlet.getServletClassName() );
		return;
	    }
	    if( debug>-1)
		log( "jspServlet=" +  jspServlet.getServletClassName());
	    Enumeration enum=args.keys();
	    while( enum.hasMoreElements() ) {
		String s=(String)enum.nextElement();
		String v=(String)args.get(s);
		if( debug>0 ) log( "Setting " + s + "=" + v );
		jspServlet.getServletInfo().addInitParam(s, v );
	    }
	    
	    if( debug > 0 ) {
		//enable jasperServlet logging
		log( "Seetting debug on jsp servlet");
		org.apache.jasper.Constants.jasperLog=
		    loghelper;
		// 		org.apache.jasper.Constants.jasperLog.
		// 		    setVerbosityLevel("debug");
	    }

	    jspServlet.setServletClassName(jspServletCN);
	} else {
	    ctx.addServlet( new JspPrecompileH());
	}
    }

    /** Set the HttpJspBase classloader before init,
     *  as required by Jasper
     */
    public void preServletInit( Context ctx, Handler sw )
	throws TomcatException
    {
	if( ! (sw instanceof ServletHandler) )
	    return;
	try {
	    // requires that everything is compiled
	    Servlet theServlet = ((ServletHandler)sw).getServlet();
	    if (theServlet instanceof HttpJspBase)  {
		if( debug > 9 )
		    log( "PreServletInit: HttpJspBase.setParentClassLoader" +
			 sw );
		HttpJspBase h = (HttpJspBase) theServlet;
		h.setClassLoader(ctx.getClassLoader());
	    }
	} catch(Exception ex ) {
	    throw new TomcatException( ex );
	}
    }

    //-------------------- Main hook - compile the jsp file if needed
    
    /** Detect if the request is for a JSP page and if it is find
	the associated servlet name and compile if needed.

	That insures that init() will take place on the equivalent
	servlet - and behave exactly like a servlet.

	A request is for a JSP if:
	- the handler is a ServletHandler ( i.e. defined in web.xml
	or dynamically loaded servlet ) and it has a "path" instead of
	class name
	- the handler has a special name "jsp". That means a *.jsp -> jsp
	needs to be defined. This is a tomcat-specific mechanism ( not
	part of the standard ) and allow users to associate other extensions
	with JSP by using the "fictious" jsp handler.

	An (cleaner?) alternative for mapping other extensions would be
	to set them on JspInterceptor.
    */
    public int requestMap( Request req ) {
	if( useJspServlet ) {
	    // no further processing - jspServlet will take care
	    // of the processing as before ( all processing
	    // will happen in the handle() pipeline.
	    return 0;
	}

	Handler wrapper=req.getHandler();
	
	if( wrapper==null )
	    return 0;

	// It's not a jsp if it's not "*.jsp" mapped or a servlet
	if( (! "jsp".equals( wrapper.getName())) &&
	    (! (wrapper instanceof ServletHandler)) ) {
	    return 0;
	}

	ServletHandler handler=null;
	String jspFile=null;

	// There are 2 cases: extension mapped and exact map with
	// a <servlet> with file-name declaration

	// note that this code is called only the first time
	// the jsp page is called - all other calls will treat the jsp
	// as a regular servlet, nothing is special except the initial
	// processing.

	// XXX deal with jsp_compile
	
	if( "jsp".equals( wrapper.getName())) {
	    // if it's an extension mapped file, construct and map a handler
	    jspFile=req.servletPath().toString();
	    // extension mapped jsp - define a new handler,
	    // add the exact mapping to avoid future overhead
	    handler= mapJspPage( req.getContext(), jspFile );
	    req.setHandler( handler );
	} else if( wrapper instanceof ServletHandler) {
	    // if it's a simple servlet, we don't care about it
	    handler=(ServletHandler)wrapper;
	    jspFile=handler.getServletInfo().getJspFile();
	    if( jspFile==null )
		return 0; // not a jsp
	}

	// if it's a jsp_precompile request, don't execute - just
	// compile ( if needed ). Since we'll compile the jsp on
	// the first request the only special thing is to not
	// execute the jsp if jsp_precompile param is in parameters.
	String qString=req.queryString().toString();
	// look for ?jsp_precompile or &jsp_precompile

	// quick test to see if we need to worry about params
	// ( preserve lazy eval for parameters )
	boolean pre_compile=false;
	int i=(qString==null) ? -1: qString.indexOf( "jsp_precompile" );
	if( i>= 0 ) {
	    // Probably we are in the problem case. 
	    req.parameters().handleQueryParameters();
	    String p=req.parameters().getParameter( "jsp_precompile");
	    if( p==null || p.equalsIgnoreCase("true")) {
		pre_compile=true;
	    }
	}
	
	// Each .jsp file is compiled to a servlet, and will
	// have a dependency to check if it's expired
	Dependency dep= handler.getServletInfo().getDependency();
	if( dep!=null && ! dep.isExpired() ) {
	    // if the jspfile is older than the class - we're ok
	    // this happens if the .jsp file was compiled in a previous
	    // run of tomcat.
	    return 0;
	}

	// we need to compile... ( or find previous .class )
	JasperLiaison liasion=new JasperLiaison(getLog(), debug);
	liasion.processJspFile(req, jspFile, handler, args);

	if( pre_compile ) {
	    // we may have compiled the page ( if needed ), but
	    // we can't execute it. The handler will just
	    // report that we detected the trick.

	    // Future: detail information about compile results
	    // and if indeed we had to do something or not
	    req.setHandler(  ctx.
			     getServletByName( "tomcat.jspPrecompileHandler"));
	}
	
	return 0;
    }

    // -------------------- Utils --------------------
    
    private static final String SERVLET_NAME_PREFIX="TOMCAT/JSP";
    
    /** Add an exact map that will avoid *.jsp mapping and intermediate
     *  steps. It's equivalent with declaring
     *  <servlet-name>tomcat.jsp.[uri]</>
     *  <servlet-mapping><servlet-name>tomcat.jsp.[uri]</>
     *                   <url-pattern>[uri]</></>
     */
    private ServletHandler mapJspPage( Context ctx, String uri)
    {
	String servletName= SERVLET_NAME_PREFIX + uri;

	if( debug>0)
	    log( "mapJspPage " + ctx + " " + " " + servletName + " " +  uri  );

	Handler h=ctx.getServletByName( servletName );
	if( h!= null ) {
	    log( "Name already exists " + servletName +
		 " while mapping " + uri);
	    return (ServletHandler)h; // exception ?
	}
	
	ServletHandler wrapper=new ServletHandler();
	wrapper.setModule( this );
	wrapper.setContext(ctx);
	wrapper.setName(servletName);
	wrapper.getServletInfo().setJspFile( uri );
	
	// add the mapping - it's a "invoker" map ( i.e. it
	// can be removed to keep memory under control.
	// The memory usage is smaller than JspSerlvet anyway, but
	// can be further improved.
	try {
	    ctx.addServlet( wrapper );
	    ctx.addServletMapping( uri ,
				   servletName );
	    if( debug > 0 )
		log( "Added mapping " + uri + " path=" + servletName );
	} catch( TomcatException ex ) {
	    log("mapJspPage: ctx=" + ctx +
		", servletName=" + servletName, ex);
	    return null;
	}
	return wrapper;
    }

}

// -------------------- Jsp_precompile handler --------------------

/** What to do for jsp precompile
 */
class JspPrecompileH extends Handler {
    static StringManager sm=StringManager.
	getManager("org.apache.tomcat.resources");
    
    JspPrecompileH() {
	name="tomcat.jspPrecompileHandler";
    }

    public void doService(Request req, Response res)
	throws Exception
    {
	res.setContentType("text/html");	

	String msg="<h1>Jsp Precompile Done</h1>";

	res.setContentLength(msg.length());

	res.getBuffer().write( msg );
    }
}




// -------------------- The main Jasper Liaison --------------------

final class JasperLiaison {
    Log log;
    final int debug;
    
    JasperLiaison( Log log, int debug ) {
	this.log=log;
	this.debug=debug;
    }
    
    /** Generate mangled names, check for previous versions,
     *  generate the .java file, compile it - all the expensive
     *  operations. This happens only once ( or when the jsp file
     *  changes ). 
     */
    int processJspFile(Request req, String jspFile,
		       ServletHandler handler, Properties args)
    {
	// ---------- Expensive part - compile and load
	
	// If dep==null, the handler was never used - we need
	// to either compile it or find the previous compiled version
	// If dep.isExpired() we need to recompile.

	if( debug > 10 ) log.log( "Before compile sync  " + jspFile );
	synchronized( handler ) {
	    
	    // double check - maybe another thread did that for us
	    Dependency dep= handler.getServletInfo().getDependency();
	    if( dep!=null && ! dep.isExpired() ) {
		// if the jspfile is older than the class - we're ok
		return 0;
	    }

	    Context ctx=req.getContext();
	    
	    // Mangle the names - expensive operation, but nothing
	    // compared with a compilation :-)
	    JasperMangler mangler=
		new JasperMangler(ctx.getWorkDir().getAbsolutePath(),
			       ctx.getAbsolutePath(),
			       jspFile );

	    // register the handler as dependend of the jspfile 
	    if( dep==null ) {
		dep=setDependency( ctx, mangler, handler );
		// update the servlet class name
		handler.setServletClassName( mangler.getServletClassName() );

		// check again - maybe we just found a compiled class from
		// a previous run
		if( ! dep.isExpired() )
		    return 0;
	    }

	    //	    if( debug > 3) 
	    ctx.log( "Compiling: " + jspFile + " to " +
		     mangler.getServletClassName());
	    
	    //XXX old servlet -  destroy(); 
	    
	    // jump version number - the file needs to be recompiled
	    // reset the handler error, un-initialize the servlet
	    handler.setErrorException( null );
	    handler.setState( Handler.STATE_ADDED );
	    
	    // Move to the next class name
	    mangler.nextVersion();

	    // record time of attempted translate-and-compile
	    // if the compilation fails, we'll not try again
	    // until the jsp file changes
	    dep.setLastModified( System.currentTimeMillis() );

	    // Update the class name in wrapper
	    if( debug> 1 )
		log.log( "Update class Name " + mangler.getServletClassName());
	    handler.setServletClassName( mangler.getServletClassName() );

	    // May be called from include, we need to set the context class loader
	    // for jaxp1.1 to work using the container class loader
	    ClassLoader savedContextCL= containerCCL( req.getClass().getClassLoader());
	    
	    try {
		Options options=new JasperOptionsImpl(args); 
		JspCompilationContext ctxt=createCompilationContext(req,
								    jspFile,
								    options,
								    mangler);
		jsp2java( mangler, ctxt );

		javac( options, ctxt, mangler );
	    
		if(debug>0)log.log( "Generated " +
				    mangler.getClassFileName() );
            } catch ( java.io.FileNotFoundException fnfex ){
		containerCCL( savedContextCL );
		return 404;
	    } catch( Exception ex ) {
		if( ctx!=null )
		    ctx.log("compile error: req="+req, ex);
		else
		    log.log("compile error: req="+req, ex);
		handler.setErrorException(ex);
		handler.setState(Handler.STATE_DISABLED);
		// until the jsp cahnges, when it'll be enabled again
		containerCCL( savedContextCL );
		return 500;
	    }

	    containerCCL( savedContextCL );
	    
	    dep.setExpired( false );
	    
	}

	return 0;
    }

    static final Jdk11Compat jdk11Compat=Jdk11Compat.getJdkCompat();
    
    ClassLoader containerCCL( ClassLoader cl ) {
	ClassLoader orig= jdk11Compat.getContextClassLoader();
	jdk11Compat.setContextClassLoader( cl );
	return orig;
    }

    /** Convert the .jsp file to a java file, then compile it to class
     */
    void jsp2java(JasperMangler mangler,  JspCompilationContext ctxt)
	throws Exception
    {
	if( debug > 0 ) log.log( "Generating " + mangler.getJavaFileName());
	// make sure we have the directories
	String javaFileName=mangler.getJavaFileName();
	
	File javaFile=new File(javaFileName);
	
	// make sure the directory is created
	new File( javaFile.getParent()).mkdirs();
	
	Compiler compiler=new Compiler(ctxt);
	compiler.setMangler( mangler );
	// we will compile ourself
	compiler.setJavaCompiler( null );
	
	
	synchronized ( mangler ) {
	    compiler.compile();
	}
	if( debug > 0 ) {
	    File f = new File( mangler.getJavaFileName());
	    log.log( "Created file : " + f +  " " + f.lastModified());
	    
	}
    }
    
    String javaEncoding = "UTF8";           // perhaps debatable?
    static String sep = System.getProperty("path.separator");

    private void prepareCompiler( JavaCompiler javac,
				  Options options, 
				  JspCompilationContext ctxt )
	throws JasperException
    {
	String compilerPath = options.getJspCompilerPath();
        if (compilerPath != null)
            javac.setCompilerPath(compilerPath);

	javac.setClassDebugInfo(options.getClassDebugInfo());

	javac.setEncoding(javaEncoding);
	String cp=System.getProperty("java.class.path")+ sep + 
	    ctxt.getClassPath() + sep + ctxt.getOutputDir();
        javac.setClasspath( cp );
	javac.setOutputDir(ctxt.getOutputDir());

	if( debug>5) log.log( "ClassPath " + cp);
    }

    static boolean tryJikes=true;
    static Class jspCompilerPlugin = null;
    
    /** Compile a java to class. This should be moved to util, togheter
	with JavaCompiler - it's a general purpose code, no need to
	keep it part of jasper
    */
    void javac(Options options, JspCompilationContext ctxt,
	       Mangler mangler)
	throws JasperException
    {
	String javaFileName = mangler.getJavaFileName();
	if( debug>0 ) log.log( "Compiling java file " + javaFileName);

	boolean status=true;
	if( jspCompilerPlugin == null ) {
	    jspCompilerPlugin=options.getJspCompilerPlugin();
	}
	// If no explicit compiler, and we never tried before
	if( jspCompilerPlugin==null && tryJikes ) {
	    ByteArrayOutputStream out = new ByteArrayOutputStream (256);
	    try {

		jspCompilerPlugin=Class.
		    forName("org.apache.jasper.compiler.JikesJavaCompiler");
		JavaCompiler javaC=createJavaCompiler( jspCompilerPlugin );
		
		prepareCompiler( javaC, options, ctxt );
		javaC.setMsgOutput(out);
		status = javaC.compile(javaFileName);
	    } catch( Exception ex ) {	
		log.log("Guess java compiler: no jikes " + ex.toString());
		status=false;
	    }
	    if( status==false ) {
		log.log("Guess java compiler: no jikes ");
		log.log("Guess java compiler: OUT " + out.toString());
		jspCompilerPlugin=null;
		tryJikes=false;
	    } else {
		log.log("Guess java compiler: using jikes ");
	    }
	}

	JavaCompiler javaC=createJavaCompiler( jspCompilerPlugin );
	prepareCompiler( javaC, options, ctxt );
	ByteArrayOutputStream out = new ByteArrayOutputStream (256);
	javaC.setMsgOutput(out);
	
	status = javaC.compile(javaFileName);

        if (!ctxt.keepGenerated()) {
            File javaFile = new File(javaFileName);
            javaFile.delete();
        }
    
        if (status == false) {
            String msg = out.toString ();
            throw new JasperException("Unable to compile "
                                      + msg);
        }
	if( debug > 0 ) log.log("Compiled ok");
    }

    /** tool for customizing javac.
     */
    public JavaCompiler createJavaCompiler(Class jspCompilerPlugin )
	throws JasperException
    {
        JavaCompiler javac;

	if (jspCompilerPlugin != null) {
            try {
                javac = (JavaCompiler) jspCompilerPlugin.newInstance();
            } catch (Exception ex) {
		Constants.message("jsp.warning.compiler.class.cantcreate",
				  new Object[] { jspCompilerPlugin, ex }, 
				  Log.FATAL);
                javac = new SunJavaCompiler();
	    }
	} else {
            javac = new SunJavaCompiler();
	}

	return javac;
    }

    private String computeClassPath(Context ctx) {
	String separator = System.getProperty("path.separator", ":");
	URL classP[]=ctx.getClassPath();
        String cpath = "";
        cpath+=extractClassPath(classP);
        Jdk11Compat jdkProxy=Jdk11Compat.getJdkCompat();
        URL appsCP[];
        URL commonCP[];
        ClassLoader loader=ctx.getClassLoader();
        //The next will be the container classpath in trusted apps
        appsCP=jdkProxy.getURLs(loader,1);
        commonCP=jdkProxy.getURLs(loader,2);
	if( appsCP!=null ) 
	    cpath+=separator+extractClassPath(appsCP);
	if( commonCP!=null ) 
	    cpath+=separator+extractClassPath(commonCP);
//        System.out.println("classpath: " + cpath );
	return cpath;
    }
    String extractClassPath(URL urls[]){
	String separator = System.getProperty("path.separator", ":");
        String cpath="";
        for(int i=0; i< urls.length; i++ ) {
            URL cp = urls[i];
	    if( cp == null ) {
		continue;
	    }
            File f = new File( cp.getFile());
            if (cpath.length()>0) cpath += separator;
            cpath += f;
        }
        return cpath;
    }

    private JspCompilationContext createCompilationContext( Request req,
							    String jspFile,
							    Options opt,
							    Mangler mangler)
    {
	JasperEngineContext ctxt = new JasperEngineContext();
	ctxt.setServletClassName( mangler.getClassName());
	//	ctxt.setJspFile( req.servletPath().toString());
	ctxt.setJspFile( jspFile );
	ctxt.setClassPath( computeClassPath( req.getContext()) );
//        System.out.println("computeClasspath:"+ctxt.getClassPath());
	ctxt.setServletContext( req.getContext().getFacade());
	ctxt.setOptions( opt );
	ctxt.setClassLoader( req.getContext().getClassLoader());
	ctxt.setOutputDir(req.getContext().getWorkDir().getAbsolutePath());
	return ctxt;
    }

    // Add an "expire check" to the generated servlet.
    private Dependency setDependency( Context ctx, JasperMangler mangler,
				      ServletHandler handler )
    {
	ServletInfo info=handler.getServletInfo();
	// create a lastModified checker.
	if( debug>0) log.log("Registering dependency for " + handler );
	Dependency dep=new Dependency();
	dep.setOrigin( new File(mangler.getJspFilePath()) );
	dep.setTarget( handler );
	dep.setLocal( true );
	File f=new File( mangler.getClassFileName() );
	if( mangler.getVersion() > 0 ) {
	    // it has a previous version
	    dep.setLastModified(f.lastModified());
	    // update the "expired" variable
	    dep.checkExpiry();
	} else {
	    dep.setLastModified( -1 );
	    dep.setExpired( true );
	}
	if( debug>0 )
	    log.log( "file = " + mangler.getClassFileName() + " " +
		     f.lastModified() );
	if( debug>0 )
	    log.log("origin = " + dep.getOrigin() + " " +
		    dep.getOrigin().lastModified());
	try {
	    DependManager dm=(DependManager)ctx.getContainer().
		getNote("DependManager");
	    if( dm!=null ) {
		dm.addDependency( dep );
	    }
	} catch( TomcatException ex ) {
	    ex.printStackTrace();
	}
	info.setDependency( dep );
	return dep;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9046.java
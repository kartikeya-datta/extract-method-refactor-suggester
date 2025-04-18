error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9378.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9378.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9378.java
text:
```scala
r@@et[i] = types[i].getName();

/* *******************************************************************
 * Copyright (c) 2002 Palo Alto Research Center, Incorporated (PARC).
 * All rights reserved.
 * This program and the accompanying materials are made available
 * under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     PARC     initial implementation
 *     Alexandre Vasseur    perClause support for @AJ aspects
 * ******************************************************************/


package org.aspectj.weaver.bcel;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import org.aspectj.apache.bcel.classfile.Attribute;
import org.aspectj.apache.bcel.classfile.ClassParser;
import org.aspectj.apache.bcel.classfile.JavaClass;
import org.aspectj.apache.bcel.classfile.Method;
import org.aspectj.apache.bcel.classfile.annotation.Annotation;
import org.aspectj.apache.bcel.generic.ANEWARRAY;
import org.aspectj.apache.bcel.generic.ConstantPoolGen;
import org.aspectj.apache.bcel.generic.FieldInstruction;
import org.aspectj.apache.bcel.generic.GETSTATIC;
import org.aspectj.apache.bcel.generic.INVOKEINTERFACE;
import org.aspectj.apache.bcel.generic.INVOKESPECIAL;
import org.aspectj.apache.bcel.generic.INVOKESTATIC;
import org.aspectj.apache.bcel.generic.Instruction;
import org.aspectj.apache.bcel.generic.InstructionHandle;
import org.aspectj.apache.bcel.generic.InvokeInstruction;
import org.aspectj.apache.bcel.generic.MONITORENTER;
import org.aspectj.apache.bcel.generic.MONITOREXIT;
import org.aspectj.apache.bcel.generic.MULTIANEWARRAY;
import org.aspectj.apache.bcel.generic.NEWARRAY;
import org.aspectj.apache.bcel.generic.ObjectType;
import org.aspectj.apache.bcel.generic.PUTSTATIC;
import org.aspectj.apache.bcel.generic.Type;
import org.aspectj.apache.bcel.util.ClassLoaderRepository;
import org.aspectj.apache.bcel.util.ClassPath;
import org.aspectj.apache.bcel.util.Repository;
import org.aspectj.bridge.IMessageHandler;
import org.aspectj.weaver.Advice;
import org.aspectj.weaver.AdviceKind;
import org.aspectj.weaver.AjAttribute;
import org.aspectj.weaver.BCException;
import org.aspectj.weaver.ConcreteTypeMunger;
import org.aspectj.weaver.ICrossReferenceHandler;
import org.aspectj.weaver.Member;
import org.aspectj.weaver.MemberImpl;
import org.aspectj.weaver.ReferenceType;
import org.aspectj.weaver.ReferenceTypeDelegate;
import org.aspectj.weaver.ResolvedMember;
import org.aspectj.weaver.ResolvedMemberImpl;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.ResolvedTypeMunger;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.World;
import org.aspectj.weaver.AjAttribute.Aspect;
import org.aspectj.weaver.asm.AsmDelegate;
import org.aspectj.weaver.patterns.FormalBinding;
import org.aspectj.weaver.patterns.PerClause;
import org.aspectj.weaver.patterns.Pointcut;
import org.aspectj.weaver.patterns.SimpleScope;

public class BcelWorld extends World implements Repository {
	private ClassPathManager classPath;

    private Repository delegate;
    
    
	//private ClassPathManager aspectPath = null;
	// private List aspectPathEntries;
	
    // ---- constructors

	public BcelWorld() {
		this("");		
	}
	
	public BcelWorld(String cp) {
		this(makeDefaultClasspath(cp), IMessageHandler.THROW, null);
	}
	
	private static List makeDefaultClasspath(String cp) {
		List classPath = new ArrayList();
		classPath.addAll(getPathEntries(cp));
		classPath.addAll(getPathEntries(ClassPath.getClassPath()));
		//System.err.println("classpath: " + classPath);
		return classPath;
		
	}
	

    
		
	private static List getPathEntries(String s) {
		List ret = new ArrayList();
		StringTokenizer tok = new StringTokenizer(s, File.pathSeparator);

      	while(tok.hasMoreTokens()) ret.add(tok.nextToken());
      	
      	return ret;
	}
	
	public BcelWorld(List classPath, IMessageHandler handler, ICrossReferenceHandler xrefHandler) {
		//this.aspectPath = new ClassPathManager(aspectPath, handler);
		this.classPath = new ClassPathManager(classPath, handler);
		setMessageHandler(handler);	
		setCrossReferenceHandler(xrefHandler);
		// Tell BCEL to use us for resolving any classes
        delegate = this;
		// TODO Alex do we need to call org.aspectj.apache.bcel.Repository.setRepository(delegate);
        // if so, how can that be safe in J2EE ?? (static stuff in Bcel)
	}
	
	public BcelWorld(ClassPathManager cpm, IMessageHandler handler, ICrossReferenceHandler xrefHandler) {
		this.classPath = cpm;
		setMessageHandler(handler);
		setCrossReferenceHandler(xrefHandler);
		// Tell BCEL to use us for resolving any classes
        delegate = this;
        // TODO Alex do we need to call org.aspectj.apache.bcel.Repository.setRepository(delegate);
        // if so, how can that be safe in J2EE ?? (static stuff in Bcel)
	}

    /**
     * Build a World from a ClassLoader, for LTW support
     *
     * @param loader
     * @param handler
     * @param xrefHandler
     */
    public BcelWorld(ClassLoader loader, IMessageHandler handler, ICrossReferenceHandler xrefHandler) {
        this.classPath = null;
        setMessageHandler(handler);
        setCrossReferenceHandler(xrefHandler);
        // Tell BCEL to use us for resolving any classes
        delegate = new ClassLoaderRepository(loader);
        // TODO Alex do we need to call org.aspectj.apache.bcel.Repository.setRepository(delegate);
        // if so, how can that be safe in J2EE ?? (static stuff in Bcel)
    }

	public void addPath (String name) {
		classPath.addPath(name, this.getMessageHandler());
	}

    /**
     * Parse a string into advice.
     * 
     * <blockquote><pre>
     * Kind ( Id , ... ) : Pointcut -> MethodSignature
     * </pre></blockquote>
     */
    public Advice shadowMunger(String str, int extraFlag) {
        str = str.trim();
        int start = 0;
        int i = str.indexOf('(');
        AdviceKind kind = 
            AdviceKind.stringToKind(str.substring(start, i));
        start = ++i;
        i = str.indexOf(')', i);
        String[] ids = parseIds(str.substring(start, i).trim());
        //start = ++i;
        
        
        
        i = str.indexOf(':', i);        
        start = ++i;        
        i = str.indexOf("->", i);
        Pointcut pointcut = Pointcut.fromString(str.substring(start, i).trim());
        Member m = MemberImpl.methodFromString(str.substring(i+2, str.length()).trim());

        // now, we resolve
        UnresolvedType[] types = m.getParameterTypes();
        FormalBinding[] bindings = new FormalBinding[ids.length];
        for (int j = 0, len = ids.length; j < len; j++) {
            bindings[j] = new FormalBinding(types[j], ids[j], j, 0, 0, "fromString");
        }

        Pointcut p =
        	pointcut.resolve(new SimpleScope(this, bindings));

        return new BcelAdvice(kind, p, m, extraFlag, 0, 0, null, null);
    }
    
    private String[] parseIds(String str) {
        if (str.length() == 0) return ZERO_STRINGS;
        List l = new ArrayList();
        int start = 0;
        while (true) {
            int i = str.indexOf(',', start);
            if (i == -1) {
                l.add(str.substring(start).trim());
                break;
            }
            l.add(str.substring(start, i).trim());
            start = i+1;
        }
        return (String[]) l.toArray(new String[l.size()]);
    }
    
    // ---- various interactions with bcel

    public static Type makeBcelType(UnresolvedType type) {
        return Type.getType(type.getErasureSignature());
    }

    static Type[] makeBcelTypes(UnresolvedType[] types) {
        Type[] ret = new Type[types.length];
        for (int i = 0, len = types.length; i < len; i++) {
            ret[i] = makeBcelType(types[i]);
        }
        return ret;
    }

    static String[] makeBcelTypesAsClassNames(UnresolvedType[] types) {
        String[] ret = new String[types.length];
        for (int i = 0, len = types.length; i < len; i++) {
            ret[i] = types[i].getClassName();//makeBcelType(types[i]);
        }
        return ret;
    }
    
    public static UnresolvedType fromBcel(Type t) {
        return UnresolvedType.forSignature(t.getSignature());
    }

    static UnresolvedType[] fromBcel(Type[] ts) {
        UnresolvedType[] ret = new UnresolvedType[ts.length];
        for (int i = 0, len = ts.length; i < len; i++) {
            ret[i] = fromBcel(ts[i]);
        }
        return ret;
    }
    
    public ResolvedType resolve(Type t) {
        return resolve(fromBcel(t));
    } 
    
    // SECRETAPI: used for testing ASM loading of delegates...
    public boolean fallbackToLoadingBcelDelegatesForAspects = true;
    
    private int packageRestrictionsForFastDelegates = 0; // 0=dontknow 1=no  2=yes
    private List packagePrefixRestrictionList = null;
    
    public boolean isNotOnPackageRestrictedList(String s) {
    	if (packageRestrictionsForFastDelegates==0) {
    		Properties p = getExtraConfiguration();
    		String possiblePackageRestrictions = (p==null?null:p.getProperty("fastDelegateRestrictions"));
    		if (possiblePackageRestrictions==null) {
    			packageRestrictionsForFastDelegates=1;
    		} else {
    			packageRestrictionsForFastDelegates=2;
    			packagePrefixRestrictionList=new ArrayList();
	    		StringTokenizer st = new StringTokenizer(possiblePackageRestrictions,":");
	    		while (st.hasMoreTokens()) {
	    			packagePrefixRestrictionList.add(st.nextToken());
	    		}
    		}
    	}
    	if (packageRestrictionsForFastDelegates==1) return true;
    	if (packageRestrictionsForFastDelegates==2) {
    		for (Iterator iter = packagePrefixRestrictionList.iterator(); iter.hasNext();) {
				String element = (String) iter.next();
				if (s.startsWith(element)) {
//					System.err.println("Not creating fast delegate for "+s);
					return false;
				}
			}
    	}
    	return true;
    }

	protected ReferenceTypeDelegate resolveDelegate(ReferenceType ty) {
        String name = ty.getName();
        JavaClass jc = null;
    	    ensureAdvancedConfigurationProcessed();
        //UnwovenClassFile classFile = (UnwovenClassFile)sourceJavaClasses.get(name);
        //if (classFile != null) jc = classFile.getJavaClass();
        if (isFastDelegateSupportEnabled() && classPath!=null && !ty.needsModifiableDelegate() && isNotOnPackageRestrictedList(name)) {
	        ClassPathManager.ClassFile cf = classPath.find(ty);
	        if (cf==null) {
	        	return null;
	        } else {
	        	ReferenceTypeDelegate delegate =  buildAsmDelegate(ty,cf);
	        	if (fallbackToLoadingBcelDelegatesForAspects && delegate.isAspect()) {
	        		// bugger - pr135001 - we can't inline around advice from an aspect because we don't load the instructions.
	        		// fixing this quick to get AJDT upgraded with a good 1.5.2dev build.
	        		// other fixes would be:
	        		// 1. record that we are loading the superclass for an aspect, so we know to make it a BCEL delegate
	        		//
	        		// the 'fix' here is only reasonable because there are many less aspects than classes!
	        		
	        		// Create a BCEL delegate
	        		if (jc == null) jc = lookupJavaClass(classPath, name);
	    	        if (jc == null) return delegate; // worrying situation ?!?
	    	        else            return buildBcelDelegate(ty, jc, false);
	        	} else {
	        		return delegate;
	        	}
	        }
        } else {
	        if (jc == null) {
	        	jc = lookupJavaClass(classPath, name);
	        }       
	        if (jc == null) {
	        	return null;
	        } else {
	        	return buildBcelDelegate(ty, jc, false);
	        }
	    }
	}
	
    private ReferenceTypeDelegate buildAsmDelegate(ReferenceType type,ClassPathManager.ClassFile t) {
    	AsmDelegate asmDelegate;
		try {
			asmDelegate = new AsmDelegate(type,t.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return asmDelegate;
	}
    
	public BcelObjectType buildBcelDelegate(ReferenceType resolvedTypeX, JavaClass jc, boolean exposedToWeaver) {
		BcelObjectType ret = new BcelObjectType(resolvedTypeX, jc, exposedToWeaver);
		return ret;
	}
	
	
	private JavaClass lookupJavaClass(ClassPathManager classPath, String name) {
        if (classPath == null) {
            try {
                return delegate.loadClass(name);
            } catch (ClassNotFoundException e) {
                return null;
            }
        }

		try {
	        ClassPathManager.ClassFile file = classPath.find(UnresolvedType.forName(name));
	        if (file == null) return null;
	        
	        ClassParser parser = new ClassParser(file.getInputStream(), file.getPath());
	        
	        JavaClass jc = parser.parse();
			file.close();
			return jc;
		} catch (IOException ioe) {
			return null;
		}
	}
	
	
	public BcelObjectType addSourceObjectType(JavaClass jc) {
		BcelObjectType ret = null;
		String signature = UnresolvedType.forName(jc.getClassName()).getSignature();
		
		Object fromTheMap = typeMap.get(signature);
		
		if (fromTheMap!=null && !(fromTheMap instanceof ReferenceType)) {
			// what on earth is it then? See pr 112243
			StringBuffer exceptionText = new StringBuffer();
			exceptionText.append("Found invalid (not a ReferenceType) entry in the type map. ");
			exceptionText.append("Signature=["+signature+"] Found=["+fromTheMap+"] Class=["+fromTheMap.getClass()+"]");
			throw new BCException(exceptionText.toString());
		}
		
		ReferenceType nameTypeX = (ReferenceType)fromTheMap;
        
        if (nameTypeX == null) {        	
		    if (jc.isGeneric() && isInJava5Mode()) {
		    
		    	nameTypeX =  ReferenceType.fromTypeX(UnresolvedType.forRawTypeName(jc.getClassName()),this);
		        ret = buildBcelDelegate(nameTypeX, jc, true);
		    	ReferenceType genericRefType = new ReferenceType(
		    			UnresolvedType.forGenericTypeSignature(signature,ret.getDeclaredGenericSignature()),this);
				nameTypeX.setDelegate(ret);
		    	genericRefType.setDelegate(ret);
		    	nameTypeX.setGenericType(genericRefType);
		       	typeMap.put(signature, nameTypeX);
		    } else {
	        	nameTypeX = new ReferenceType(signature, this);
	            ret = buildBcelDelegate(nameTypeX, jc, true);
	           	typeMap.put(signature, nameTypeX);
		    }
        } else {
            ret = buildBcelDelegate(nameTypeX, jc, true);
        }
		return ret;
	}
	
	void deleteSourceObjectType(UnresolvedType ty) {
		typeMap.remove(ty.getSignature());
	}

    public static Member makeFieldJoinPointSignature(LazyClassGen cg, FieldInstruction fi) {
    	ConstantPoolGen cpg = cg.getConstantPoolGen();
        return            
            	  MemberImpl.field(
            			fi.getClassName(cpg),
            			(fi instanceof GETSTATIC || fi instanceof PUTSTATIC)
            			? Modifier.STATIC: 0, 
            			fi.getName(cpg),
            			fi.getSignature(cpg));
    }
	
//    public static Member makeFieldSetSignature(LazyClassGen cg, FieldInstruction fi) {
//    	ConstantPoolGen cpg = cg.getConstantPoolGen();
//        return 
//            MemberImpl.field(
//                fi.getClassName(cpg),
//                (fi instanceof GETSTATIC || fi instanceof PUTSTATIC)
//                ? Modifier.STATIC
//                : 0, 
//                fi.getName(cpg),
//                "(" + fi.getSignature(cpg) + ")" +fi.getSignature(cpg));
//    }

	public Member makeJoinPointSignature(LazyMethodGen mg) {
		return makeJoinPointSignatureFromMethod(mg, null);
	}

	
	public Member makeJoinPointSignatureFromMethod(LazyMethodGen mg, MemberImpl.Kind kind) {
		Member ret = mg.getMemberView();
		if (ret == null) {
	        int mods = mg.getAccessFlags();
	        if (mg.getEnclosingClass().isInterface()) {
	            mods |= Modifier.INTERFACE;
	        }
	        if (kind == null) {
		        if (mg.getName().equals("<init>")) {
		        	kind = Member.CONSTRUCTOR;
		        } else if (mg.getName().equals("<clinit>")) {
		        	kind = Member.STATIC_INITIALIZATION;
		        } else {
		        	kind = Member.METHOD;
		        }
	        }
	        return new ResolvedMemberImpl(kind,
	                UnresolvedType.forName(mg.getClassName()), 
	                mods,
	                fromBcel(mg.getReturnType()),
	                mg.getName(),
	                fromBcel(mg.getArgumentTypes())
	                );
		} else {
			return ret;
		}
        
    }
	
	public Member makeJoinPointSignatureForMonitorEnter(LazyClassGen cg,InstructionHandle h) {
		MONITORENTER i = (MONITORENTER)h.getInstruction();
		return MemberImpl.monitorEnter();
	}

	public Member makeJoinPointSignatureForMonitorExit(LazyClassGen cg,InstructionHandle h) {
		MONITOREXIT i = (MONITOREXIT)h.getInstruction();
		return MemberImpl.monitorExit();
	}
	
	public Member makeJoinPointSignatureForArrayConstruction(LazyClassGen cg, InstructionHandle handle) {
		Instruction i = handle.getInstruction();
		ConstantPoolGen cpg = cg.getConstantPoolGen();
		Member retval = null;

		if (i instanceof ANEWARRAY) {
			ANEWARRAY arrayInstruction = (ANEWARRAY)i;
			ObjectType ot = arrayInstruction.getLoadClassType(cpg);
			UnresolvedType ut = fromBcel(ot);
			ut = UnresolvedType.makeArray(ut,1);
			retval = MemberImpl.method(ut, Modifier.PUBLIC, ResolvedType.VOID, "<init>", new ResolvedType[]{ResolvedType.INT});
			
		} else if (i instanceof MULTIANEWARRAY) {
			MULTIANEWARRAY arrayInstruction = (MULTIANEWARRAY)i;
			UnresolvedType ut = null;
			short dimensions = arrayInstruction.getDimensions();
			ObjectType ot = arrayInstruction.getLoadClassType(cpg);
			if (ot!=null) {
				ut = fromBcel(ot);
				ut = UnresolvedType.makeArray(ut,dimensions);
			} else {
				Type t = arrayInstruction.getType(cpg);
				ut = fromBcel(t);
			}
			ResolvedType[] parms = new ResolvedType[dimensions];
			for (int ii=0;ii<dimensions;ii++) parms[ii] = ResolvedType.INT;
			retval = MemberImpl.method(ut, Modifier.PUBLIC, ResolvedType.VOID, "<init>", parms);
			
		} else if (i instanceof NEWARRAY) {
			NEWARRAY arrayInstruction = (NEWARRAY)i;
			Type ot = arrayInstruction.getType();
			UnresolvedType ut = fromBcel(ot);
			retval = MemberImpl.method(ut, Modifier.PUBLIC, ResolvedType.VOID, "<init>", new ResolvedType[]{ResolvedType.INT});
		} else {
			throw new BCException("Cannot create array construction signature for this non-array instruction:"+i);
		}
		return retval;
	}

    public Member makeJoinPointSignatureForMethodInvocation(LazyClassGen cg, InvokeInstruction ii) {
    	ConstantPoolGen cpg = cg.getConstantPoolGen();
    	String name = ii.getName(cpg);
        String declaring = ii.getClassName(cpg);
        UnresolvedType declaringType = null;
        
        String signature = ii.getSignature(cpg);
       
        int modifier = 
            (ii instanceof INVOKEINTERFACE)
            ? Modifier.INTERFACE
            : (ii instanceof INVOKESTATIC)
              ? Modifier.STATIC
              : (ii instanceof INVOKESPECIAL && ! name.equals("<init>"))
                ? Modifier.PRIVATE
                : 0;

        // in Java 1.4 and after, static method call of super class within subclass method appears
        // as declared by the subclass in the bytecode - but they are not
        // see #104212
        if (ii instanceof INVOKESTATIC) {
            ResolvedType appearsDeclaredBy = resolve(declaring);
            // look for the method there
            for (Iterator iterator = appearsDeclaredBy.getMethods(); iterator.hasNext();) {
                ResolvedMember method = (ResolvedMember) iterator.next();
                if (method.isStatic()) {
                    if (name.equals(method.getName()) && signature.equals(method.getSignature())) {
                        // we found it
                        declaringType = method.getDeclaringType();
                        break;
                    }
                }

            }
        }
        
        if (declaringType == null) {
        	if (declaring.charAt(0)=='[') declaringType = UnresolvedType.forSignature(declaring);
        	else 						  declaringType = UnresolvedType.forName(declaring);
        }
        return MemberImpl.method(declaringType, modifier, name, signature);
    }  

    public static Member makeMungerMethodSignature(JavaClass javaClass, Method method) {
        int mods = 0;
        if (method.isStatic()) mods = Modifier.STATIC;
        else if (javaClass.isInterface()) mods = Modifier.INTERFACE;
        else if (method.isPrivate()) mods = Modifier.PRIVATE;
        return MemberImpl.method(
            UnresolvedType.forName(javaClass.getClassName()), mods, method.getName(), method.getSignature()); 
    }
    
    private static final String[] ZERO_STRINGS = new String[0];
    
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("BcelWorld(");
		//buf.append(shadowMungerMap);
		buf.append(")");
		return buf.toString();
	}

    public Advice createAdviceMunger(
       	AjAttribute.AdviceAttribute attribute,
    	Pointcut pointcut,
        Member signature)
    {
    	//System.err.println("concrete advice: " + signature + " context " + sourceContext);
        return new BcelAdvice(attribute, pointcut, signature, null);
    }
    
    public ConcreteTypeMunger concreteTypeMunger(
        ResolvedTypeMunger munger, ResolvedType aspectType) 
    {
        return new BcelTypeMunger(munger, aspectType);
    }
    
	public ConcreteTypeMunger makeCflowStackFieldAdder(ResolvedMember cflowField) {
		return new BcelCflowStackFieldAdder(cflowField);
	}
	
	public ConcreteTypeMunger makeCflowCounterFieldAdder(ResolvedMember cflowField) {
		return new BcelCflowCounterFieldAdder(cflowField);
	}

    /**
     * Register a munger for perclause @AJ aspect so that we add aspectOf(..) to them as needed
     *
     * @param aspect
     * @param kind
     * @return munger
     */
    public ConcreteTypeMunger makePerClauseAspect(ResolvedType aspect, PerClause.Kind kind) {
        return new BcelPerClauseAspectAdder(aspect, kind);
    }

    /**
     * Retrieve a bcel delegate for an aspect - this will return NULL if the
     * delegate is an EclipseSourceType and not a BcelObjectType - this happens
     * quite often when incrementally compiling.
     */
	public static BcelObjectType getBcelObjectType(ResolvedType concreteAspect) {
		ReferenceTypeDelegate rtDelegate = ((ReferenceType)concreteAspect).getDelegate();
		if (rtDelegate instanceof BcelObjectType) {
			return (BcelObjectType)rtDelegate;
		} else {
			return null;
		}
	}

	public void tidyUp() {
	    // At end of compile, close any open files so deletion of those archives is possible
		classPath.closeArchives();
		typeMap.report();
		ResolvedType.resetPrimitives();
	}

	
	/// The repository interface methods

	public JavaClass findClass(String className) {
		return lookupJavaClass(classPath,className);
	}

	public JavaClass loadClass(String className) throws ClassNotFoundException {
		return lookupJavaClass(classPath,className);
	}

	public void storeClass(JavaClass clazz) {
		throw new RuntimeException("Not implemented");
	}

	public void removeClass(JavaClass clazz) {
		throw new RuntimeException("Not implemented");
	}

	public JavaClass loadClass(Class clazz) throws ClassNotFoundException {
		throw new RuntimeException("Not implemented");
	}

	public void clear() {
		throw new RuntimeException("Not implemented");
	}

    // @Override
    /** 
     * The aim of this method is to make sure a particular type is 'ok'.  Some
     * operations on the delegate for a type modify it and this method is
     * intended to undo that...  see pr85132
     */
	public void validateType(UnresolvedType type) {
		ResolvedType result = typeMap.get(type.getSignature());
		if (result==null) return; // We haven't heard of it yet
		if (!result.isExposedToWeaver()) return; // cant need resetting
		ReferenceType rt = (ReferenceType)result;
		rt.getDelegate().ensureDelegateConsistent();
		// If we want to rebuild it 'from scratch' then:
//		ClassParser cp = new ClassParser(new ByteArrayInputStream(newbytes),new String(cs));
//		try {
//			rt.setDelegate(makeBcelObjectType(rt,cp.parse(),true));
//		} catch (ClassFormatException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

    /**
     * Checks if given bytecode is an @AspectJ aspect
     *
     * @param name
     * @param bytes
     * @return true if so
     */
    public boolean isAnnotationStyleAspect(String name, byte[] bytes) {
        try {
            ClassParser cp = new ClassParser(new ByteArrayInputStream(bytes), null);
            JavaClass jc = cp.parse();
            if (!jc.isClass()) {
                return false;
            }
            Annotation anns[] = jc.getAnnotations();
            if (anns.length == 0) {
                return false;
            }
            boolean couldBeAtAspectJStyle = false;
            for (int i = 0; i < anns.length; i++) {
                Annotation ann = anns[i];
                if ("Lorg/aspectj/lang/annotation/Aspect;".equals(ann.getTypeSignature())) {
                    couldBeAtAspectJStyle = true;
                }
            }
            
            if (!couldBeAtAspectJStyle) return false;
            
            // ok, so it has the annotation, but it could have been put
            // on a code style aspect by the annotation visitor
            Attribute[] attributes = jc.getAttributes();
            for (int i = 0; i < attributes.length; i++) {
            	if (attributes[i].getName().equals(Aspect.AttributeName)) {
            		return false;
            	}
            }
            return true;
        } catch (IOException e) {
            // assume it is one as a best effort
            return true;
        }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9378.java
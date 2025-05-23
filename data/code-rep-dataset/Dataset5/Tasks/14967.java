appendFiles( files, fs.getDirectoryScanner() );

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.apache.tools.ant.taskdefs.optional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.myrmidon.api.TaskException;
import org.apache.myrmidon.framework.Os;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.taskdefs.exec.ExecTask;
import org.apache.tools.ant.types.FileSet;

/**
 * Create a CAB archive.
 *
 * @author Roger Vaughn <a href="mailto:rvaughn@seaconinc.com">
 *      rvaughn@seaconinc.com</a>
 */

public class Cab extends MatchingTask
{
    private ArrayList filesets = new ArrayList();
    private boolean doCompress = true;
    private boolean doVerbose = false;

    protected String archiveType = "cab";

    private File baseDir;

    private File cabFile;
    private String cmdOptions;

    /**
     * This is the base directory to look in for things to cab.
     *
     * @param baseDir The new Basedir value
     */
    public void setBasedir( File baseDir )
    {
        this.baseDir = baseDir;
    }

    /**
     * This is the name/location of where to create the .cab file.
     *
     * @param cabFile The new Cabfile value
     */
    public void setCabfile( File cabFile )
    {
        this.cabFile = cabFile;
    }

    /**
     * Sets whether we want to compress the files or only store them.
     *
     * @param compress The new Compress value
     */
    public void setCompress( boolean compress )
    {
        doCompress = compress;
    }

    /**
     * Sets additional cabarc options that aren't supported directly.
     *
     * @param options The new Options value
     */
    public void setOptions( String options )
    {
        cmdOptions = options;
    }

    /**
     * Sets whether we want to see or suppress cabarc output.
     *
     * @param verbose The new Verbose value
     */
    public void setVerbose( boolean verbose )
    {
        doVerbose = verbose;
    }

    /**
     * Adds a set of files (nested fileset attribute).
     *
     * @param set The feature to be added to the Fileset attribute
     */
    public void addFileset( FileSet set )
    {
        filesets.add( set );
    }

    public void execute()
        throws TaskException
    {

        checkConfiguration();

        ArrayList files = getFileList();

        // quick exit if the target is up to date
        if( isUpToDate( files ) )
            return;

        getLogger().info( "Building " + archiveType + ": " + cabFile.getAbsolutePath() );

        if( !Os.isFamily( "windows" ) )
        {
            getLogger().debug( "Using listcab/libcabinet" );

            StringBuffer sb = new StringBuffer();

            Iterator fileEnum = files.iterator();

            while( fileEnum.hasNext() )
            {
                sb.append( fileEnum.next() ).append( "\n" );
            }
            sb.append( "\n" ).append( cabFile.getAbsolutePath() ).append( "\n" );

            try
            {
                Process p = Runtime.getRuntime().exec( "listcab" );
                OutputStream out = p.getOutputStream();
                out.write( sb.toString().getBytes() );
                out.flush();
                out.close();
            }
            catch( IOException ex )
            {
                String msg = "Problem creating " + cabFile + " " + ex.getMessage();
                throw new TaskException( msg );
            }
        }
        else
        {
            try
            {
                File listFile = createListFile( files );
                ExecTask exec = createExec();
                File outFile = null;

                // die if cabarc fails
                exec.setDir( baseDir );

                if( !doVerbose )
                {
                    outFile = File.createTempFile( "ant", "", getBaseDirectory() );
                    exec.setOutput( outFile );
                }

                setupCommand( listFile, exec );
                exec.execute();

                if( outFile != null )
                {
                    outFile.delete();
                }

                listFile.delete();
            }
            catch( IOException ioe )
            {
                String msg = "Problem creating " + cabFile + " " + ioe.getMessage();
                throw new TaskException( msg );
            }
        }
    }

    /**
     * Get the complete list of files to be included in the cab. Filenames are
     * gathered from filesets if any have been added, otherwise from the
     * traditional include parameters.
     *
     * @return The FileList value
     * @exception TaskException Description of Exception
     */
    protected ArrayList getFileList()
        throws TaskException
    {
        ArrayList files = new ArrayList();

        if( filesets.size() == 0 )
        {
            // get files from old methods - includes and nested include
            appendFiles( files, super.getDirectoryScanner( baseDir ) );
        }
        else
        {
            // get files from filesets
            for( int i = 0; i < filesets.size(); i++ )
            {
                FileSet fs = (FileSet)filesets.get( i );
                if( fs != null )
                {
                    appendFiles( files, fs.getDirectoryScanner( getProject() ) );
                }
            }
        }

        return files;
    }

    /**
     * Check to see if the target is up to date with respect to input files.
     *
     * @param files Description of Parameter
     * @return true if the cab file is newer than its dependents.
     */
    protected boolean isUpToDate( ArrayList files )
    {
        boolean upToDate = true;
        for( int i = 0; i < files.size() && upToDate; i++ )
        {
            String file = files.get( i ).toString();
            if( new File( baseDir, file ).lastModified() >
                cabFile.lastModified() )
                upToDate = false;
        }
        return upToDate;
    }

    /**
     * Append all files found by a directory scanner to a vector.
     *
     * @param files Description of Parameter
     * @param ds Description of Parameter
     */
    protected void appendFiles( ArrayList files, DirectoryScanner ds )
    {
        String[] dsfiles = ds.getIncludedFiles();

        for( int i = 0; i < dsfiles.length; i++ )
        {
            files.add( dsfiles[ i ] );
        }
    }

    /*
     * I'm not fond of this pattern: "sub-method expected to throw
     * task-cancelling exceptions".  It feels too much like programming
     * for side-effects to me...
     */
    protected void checkConfiguration()
        throws TaskException
    {
        if( baseDir == null )
        {
            throw new TaskException( "basedir attribute must be set!" );
        }
        if( !baseDir.exists() )
        {
            throw new TaskException( "basedir does not exist!" );
        }
        if( cabFile == null )
        {
            throw new TaskException( "cabfile attribute must be set!" );
        }
    }

    /**
     * Create the cabarc command line to use.
     *
     * @param listFile Description of Parameter
     * @return Description of the Returned Value
     */
    protected void setupCommand( File listFile, ExecTask exec )
        throws TaskException
    {
        exec.setExecutable( "cabarc" );
        exec.createArg().setValue( "-r" );
        exec.createArg().setValue( "-p" );

        if( !doCompress )
        {
            exec.createArg().setValue( "-m" );
            exec.createArg().setValue( "none" );
        }

        if( cmdOptions != null )
        {
            exec.createArg().setLine( cmdOptions );
        }

        exec.createArg().setValue( "n" );
        exec.createArg().setFile( cabFile );
        exec.createArg().setValue( "@" + listFile.getAbsolutePath() );
    }

    /**
     * Create a new exec delegate. The delegate task is populated so that it
     * appears in the logs to be the same task as this one.
     *
     * @return Description of the Returned Value
     * @exception TaskException Description of Exception
     */
    protected ExecTask createExec()
        throws TaskException
    {
        return (ExecTask)getProject().createTask( "exec" );
    }

    /**
     * Creates a list file. This temporary file contains a list of all files to
     * be included in the cab, one file per line.
     *
     * @param files Description of Parameter
     * @return Description of the Returned Value
     * @exception IOException Description of Exception
     */
    protected File createListFile( ArrayList files )
        throws IOException
    {
        File listFile = File.createTempFile( "ant", "", getBaseDirectory() );

        PrintWriter writer = new PrintWriter( new FileOutputStream( listFile ) );

        for( int i = 0; i < files.size(); i++ )
        {
            writer.println( files.get( i ).toString() );
        }
        writer.close();

        return listFile;
    }
}
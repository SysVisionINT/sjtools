/*
 * SJTools - SysVision Java Tools
 * 
 * Copyright (C) 2006 SysVision - Consultadoria e Desenvolvimento em Sistemas de Inform�tica, Lda.  
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
 */
package net.java.sjtools.classloader;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import net.java.sjtools.util.TextUtil;

public class SuperClassLoader extends ClassLoader {
    private List loaderList = new ArrayList();

    public SuperClassLoader(ClassLoader cl) {
        super(cl);
    }

    public SuperClassLoader(List list, ClassLoader cl) {
        super(cl);

        for (Iterator i = list.iterator(); i.hasNext();) {
            addLoader((Loader) i.next());
        }
    }

    public void addLoader(Loader loader) {
        loaderList.add(loader);

        if (loader instanceof ZIPLoader) {
            if (loader.getName().toLowerCase().endsWith(".jar")) {
                processJARClasspath(loader.getName());
            }
        }
    }

    private void processJARClasspath(String name) {
        JarFile zf = null;
        String classPath = null;

        try {
            zf = new JarFile(name);
            Manifest mf = zf.getManifest();
            classPath = mf.getMainAttributes().getValue(Attributes.Name.CLASS_PATH);
        } catch (Exception e) {} finally {
            try {
                zf.close();
            } catch (Exception e1) {}
        }
        
        if (classPath != null && classPath.length() >0) {
            File home = getHomeDir(name);
            List classpathList = TextUtil.split(classPath, " ");
            File resourceFile = null;
            String resource = null;
            
            for (Iterator i = classpathList.iterator(); i.hasNext();) {
                resource = (String)i.next();
                resourceFile = new File(home, resource);
                
                if (resource.endsWith("/")) {
                    addLoader(new DirectoryLoader(resourceFile.getAbsolutePath()));
                } else {
                    addLoader(new ZIPLoader(resourceFile.getAbsolutePath()));
                }
            }
        }
    }

    public List getLoaderList() {
        return loaderList;
    }

    public Class findClass(String className) throws ClassNotFoundException {
        Class result = null;
        byte[] classBytes = null;

        Loader loader = null;

        for (Iterator i = loaderList.iterator(); i.hasNext();) {
            loader = (Loader) i.next();

            classBytes = loader.loadClassBytes(className);

            if (classBytes != null) {
                break;
            }
        }

        if (classBytes == null) {
            throw new ClassNotFoundException();
        }

        result = defineClass(className, classBytes, 0, classBytes.length);

        if (result == null) {
            throw new ClassFormatError();
        }

        return result;
    }

    public URL findResource(String name) {
        Loader loader = null;
        URL ret = null;

        for (Iterator i = loaderList.iterator(); i.hasNext();) {
            loader = (Loader) i.next();

            ret = loader.findResource(name);

            if (ret != null) {
                break;
            }
        }

        if (ret == null) {
            return getSystemResource(name);
        }

        return ret;
    }
    
    private File getHomeDir(String fileName) {
        File file = new File(fileName);
        
        return file.getParentFile();
    }    
}
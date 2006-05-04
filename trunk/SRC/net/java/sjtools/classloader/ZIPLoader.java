/*
 * SJTools - SysVision Java Tools
 * 
 * Copyright (C) 2006 SysVision - Consultadoria e Desenvolvimento em Sistemas de Informática, Lda.  
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
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZIPLoader implements Loader {
   private String zipName = null;

    public ZIPLoader(String zipName) {
        this.zipName = zipName;
    }

    public byte[] loadClassBytes(String className) {
        InputStream is = null;
        ZipFile zf = null;
        byte[] classBytes = null;

        try {
            zf = new ZipFile(zipName);
            String classPath = className.replace('.', '/') + ".class";
            ZipEntry ze = zf.getEntry(classPath);

            if (ze != null) {
                is = zf.getInputStream(ze);

                int len = (int) ze.getSize();
                classBytes = new byte[len];

                int readed = 0;
                int offset = 0;

                while (readed < len) {
                    len -= readed;
                    offset += readed;
                    readed = is.read(classBytes, offset, len);

                    if (readed == -1) {
                        throw new ClassNotFoundException(classPath);
                    }
                }

            }
        } catch (Exception e) {} finally {
            try {
                is.close();
            } catch (Exception e1) {}

            try {
                zf.close();
            } catch (Exception e1) {}
        }

        return classBytes;
    }

    public URL findResource(String name) {
        ZipFile zf = null;

        try {
            zf = new ZipFile(zipName);
            ZipEntry ze = zf.getEntry(name);

            if (ze != null) {
                File file = new File(zipName);

                if (file.exists()) {
                    try {
                        if (!name.startsWith("/")) {
                            name = "/" + name;
                        }

                        return new URL("jar:" + file.toURL().toString() + "!" + name);
                    } catch (MalformedURLException e) {}
                }
            }
        } catch (Exception e) {}

        return null;
    }
    
    public String getName() {
        return zipName;
    }
}
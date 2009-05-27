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
package net.java.sjtools.enterprise.ldap;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import net.java.sjtools.error.ApplicationError;
import net.java.sjtools.error.UnexpectedError;
import net.java.sjtools.logging.Log;
import net.java.sjtools.logging.LogFactory;
import net.java.sjtools.util.ContextUtil;
import net.java.sjtools.util.TextUtil;

public class LDAPUtil {
	private static Log log = LogFactory.getLog(LDAPUtil.class);

	private static final String NAME = "displayName";
	private static final String MAIL = "mail";
	private static final String GROUPS = "memberOf";
	private static final String[] returnedAtts = { NAME, MAIL, GROUPS };

	public static LDAPData getUserData(String userName, LDAPConfig config) throws ApplicationError {
		if (log.isDebugEnabled()) {
			log.debug("getUserData(" + userName + ", ...)");
		}

		LDAPData data = new LDAPData();

		data.setUserName(getUserName(userName));

		Hashtable env = new Hashtable();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, config.getUrl());
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, config.getLogin());
		env.put(Context.SECURITY_CREDENTIALS, config.getPassword());
		env.put(Context.REFERRAL, "follow");

		DirContext ctx = null;

		try {
			ctx = new InitialDirContext(env);

			SearchControls searchCtls = new SearchControls();
			searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			searchCtls.setReturningAttributes(returnedAtts);

			NamingEnumeration answer = ctx
					.search(config.getSearchBase(), getUserFilter(data.getUserName()), searchCtls);

			while (answer.hasMoreElements()) {
				SearchResult sr = (SearchResult) answer.next();

				Attributes attrs = sr.getAttributes();

				NamingEnumeration enumeration = attrs.getIDs();

				while (enumeration.hasMoreElements()) {
					String attrId = (String) enumeration.nextElement();

					if (attrId.equals(NAME)) {
						data.setName((String) attrs.get(attrId).get());
					}

					if (attrId.equals(MAIL)) {
						data.setMail((String) attrs.get(attrId).get());
					}

					if (attrId.equals(GROUPS)) {
						NamingEnumeration groupEnumeration = attrs.get(attrId).getAll();

						while (groupEnumeration.hasMoreElements()) {
							String groupAttribute = (String) groupEnumeration.nextElement();

							List groupPropertyList = TextUtil.split(groupAttribute, ",");

							for (Iterator i = groupPropertyList.iterator(); i.hasNext();) {
								String groupProperty = (String) i.next();

								if (groupProperty.startsWith("CN=")) {
									data.addGroup(groupProperty.substring(3));
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("Erro ao tentar obter os dados referentes ao user " + userName, e);
			throw new UnexpectedError(e);
		} finally {
			ContextUtil.close(ctx);
		}

		return data;
	}

	private static String getUserFilter(String userName) {
		StringBuffer buffer = new StringBuffer();

		buffer.append("(&(userPrincipalName=");
		buffer.append(userName);
		buffer.append("@*))");

		return buffer.toString();
	}

	public static String getUserName(String name) {
		int pos = name.indexOf("\\");

		if (pos != -1) {
			return name.substring(pos + 1);
		}

		return name;
	}
}

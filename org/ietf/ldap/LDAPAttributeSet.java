/* **************************************************************************
 * $OpenLDAP$
 *
 * Copyright (C) 1999, 2000, 2001 Novell, Inc. All Rights Reserved.
 *
 * THIS WORK IS SUBJECT TO U.S. AND INTERNATIONAL COPYRIGHT LAWS AND
 * TREATIES. USE, MODIFICATION, AND REDISTRIBUTION OF THIS WORK IS SUBJECT
 * TO VERSION 2.0.1 OF THE OPENLDAP PUBLIC LICENSE, A COPY OF WHICH IS
 * AVAILABLE AT HTTP://WWW.OPENLDAP.ORG/LICENSE.HTML OR IN THE FILE "LICENSE"
 * IN THE TOP-LEVEL DIRECTORY OF THE DISTRIBUTION. ANY USE OR EXPLOITATION
 * OF THIS WORK OTHER THAN AS AUTHORIZED IN VERSION 2.0.1 OF THE OPENLDAP
 * PUBLIC LICENSE, OR OTHER PRIOR WRITTEN CONSENT FROM NOVELL, COULD SUBJECT
 * THE PERPETRATOR TO CRIMINAL AND CIVIL LIABILITY.
 ******************************************************************************/

package org.ietf.ldap;

import com.novell.ldap.client.ArrayEnumeration;
import java.util.Enumeration;
import java.util.NoSuchElementException;

/**
 *  Represents a collection of LDAPAttributes, either used to construct an entry
 *  to be added to a directory or returned in an entry on a search or read
 *  operation.
 *
 * @see com.novell.ldap.LDAPAttributeSet
 */
public class LDAPAttributeSet implements Cloneable
{
    private com.novell.ldap.LDAPAttributeSet set;

    /**
     * Constructs a new set of attributes. using an existing attribute set
     */
    /* package */
    LDAPAttributeSet( com.novell.ldap.LDAPAttributeSet attrSet)
    {
        set = attrSet;
        return;
    }

    /**
     * Constructs a new set of attributes. This set is initially empty.
     *
     * @see com.novell.ldap.LDAPAttributeSet#LDAPAttributeSet()
     */
    public LDAPAttributeSet()
    {
        set = new com.novell.ldap.LDAPAttributeSet();
        return;
    }

    /**
     * Gets the com.novell.ldap.LDAPAttributeSet object
     */
    /* package */
    com.novell.ldap.LDAPAttributeSet getWrappedObject()
    {
        return set;
    }

    /**
     * Adds the specified attribute to this attribute set.
     *
     * @see com.novell.ldap.LDAPAttributeSet#add(com.novell.ldap.LDAPAttribute)
     */
    public void add(LDAPAttribute attr)
    {
        com.novell.ldap.LDAPAttribute a = null;
        if( attr != null) {
            a = attr.getWrappedObject();
        }
        set.add( a);
        return;
    }

    /**
     * Returns a deep copy of this attribute set.
     *
     * @see com.novell.ldap.LDAPAttributeSet#clone()
     */
    public Object clone()
    {
        return new LDAPAttributeSet(
                (com.novell.ldap.LDAPAttributeSet)set.clone());
    }

    /**
     * Returns the attribute at the position specified by the index. The
     * index is 0-based.
     *
     * @see com.novell.ldap.LDAPAttributeSet#elementAt(int)
     */
    public LDAPAttribute elementAt(int index)
            throws ArrayIndexOutOfBoundsException
    {
        return new LDAPAttribute( set.elementAt( index));
    }

    /**
     * Returns the attribute matching the specified attrName.
     *
     * @see com.novell.ldap.LDAPAttributeSet#getAttribute(String)
     */
    public LDAPAttribute getAttribute(String attrName)
    {
        return new LDAPAttribute( set.getAttribute(attrName));
    }

    /**
     * Returns a single best-match attribute, or null if no match is
     * available in the entry.
     *
     * @see com.novell.ldap.LDAPAttributeSet#getAttribute(String, String)
     */
    public LDAPAttribute getAttribute(String attrName, String lang)
    {
        return new LDAPAttribute( set.getAttribute(attrName, lang));
    }

    /**
     * Returns an enumeration of the attributes in this attribute set.
     *
     * @see com.novell.ldap.LDAPAttributeSet#getAttributes()
     */
    public Enumeration getAttributes()
    {
        class ASetEnumWrapper implements Enumeration
        {
            private Enumeration enum;
            ASetEnumWrapper( Enumeration enum)
            {
                this.enum = enum;
                return;
            }
            public boolean hasMoreElements()
            {
                return enum.hasMoreElements();
            }
            public Object nextElement() throws NoSuchElementException
            {
                return new LDAPAttribute(
                            (com.novell.ldap.LDAPAttribute)enum.nextElement());
            }
        }
        return new ASetEnumWrapper( set.getAttributes());
    }

    /**
     * Creates a new attribute set containing only the attributes that have
     * the specified subtypes.
     *
     * @see com.novell.ldap.LDAPAttributeSet#getSubset(String)
     */
    public LDAPAttributeSet getSubset(String subtype)
    {
        return new LDAPAttributeSet( set.getSubset( subtype));
    }

    /**
     * Removes the specified attribute from the set. If the attribute is not
     * a member of the set, nothing happens.
     *
     * @see com.novell.ldap.LDAPAttributeSet#remove(String)
     */
    public void remove(String name)
    {
        set.remove( name);
        return;
    }

    /**
     * Removes the attribute at the position specified by the index.  The
     * index is 0-based.
     *
     * @see com.novell.ldap.LDAPAttributeSet#removeElementAt(int)
     */
    public void removeElementAt(int index)
    {
        set.removeElementAt( index);
        return;
    }

    /**
     * Returns the number of attributes in this set.
     *
     * @see com.novell.ldap.LDAPAttributeSet#size()
     */
    public int size()
    {
        return set.size();
    }
}

// $OpenLDAP$
//
/******************************************************************************
 * Copyright (C) 1999, 2000, 2001 Novell, Inc. All Rights Reserved.
 *
 * THIS WORK IS SUBJECT TO U.S. AND INTERNATIONAL COPYRIGHT LAWS AND
 * TREATIES. USE, MODIFICATION, AND REDISTRIBUTION OF THIS WORK IS SUBJECT
 * TO VERSION 2.0.1 OF THE OPENLDAP PUBLIC LICENSE, A COPY OF WHICH IS
 * AVAILABLE AT HTTP://WWW.OPENLDAP.ORG/LICENSE.HTML OR IN THE FILE "LICENSE"
 * IN THE TOP-LEVEL DIRECTORY OF THE DISTRIBUTION. ANY USE OR EXPLOITATION
 * OF THIS WORK OTHER THAN AS AUTHORIZED IN VERSION 2.0.7 OF THE OPENLDAP
 * PUBLIC LICENSE, OR OTHER PRIOR WRITTEN CONSENT FROM NOVELL, COULD SUBJECT
 * THE PERPETRATOR TO CRIMINAL AND CIVIL LIABILITY.
 ******************************************************************************
 */
/**
 * Class to return elements of an array as an Enumeration
 */
package com.novell.ldap.client;

import java.util.Enumeration;
import java.util.NoSuchElementException;

public class ArrayEnumeration implements Enumeration
{
    private Object[] eArray; 
    private int index = 0;
    /**
     * Constructor to create the Enumeration
     *
     * @param array the array to use for the Enumeration
     */
    public ArrayEnumeration( Object[] eArray)
    {
        this.eArray = eArray;
    }

    public boolean hasMoreElements()
    {
        return (index < eArray.length);
    }

    public Object nextElement() throws NoSuchElementException
    {
        if( index >= eArray.length) {
            throw new NoSuchElementException();
        }
        return eArray[index++];
    }
}

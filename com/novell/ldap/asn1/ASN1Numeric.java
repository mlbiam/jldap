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

package com.novell.ldap.asn1;

/**
 * This is an abstract class that is the base class 
 * for all ASN.1 numeric (integral) types. These include
 * ASN1Integer and ASN1Enumerated.
 */
abstract class ASN1Numeric extends ASN1Simple {

   protected Long content;

   /* ASN1Numeric specific methods
    */

   /**
    * Returns the content of this ASN1Numeric object as an int.
    */
   public int getInt()
   {
      return (int)getLong();
   }

   /**
    * Returns the content of this ASN1Numeric object as a long.
    */
   public long getLong()
   {
      return content.longValue();
   }

}


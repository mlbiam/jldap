/* **************************************************************************
 * $Novell: /ldap/src/jldap/com/novell/ldap/asn1/ASN1Null.java,v 1.4 2000/09/11 21:05:52 vtag Exp $
 *
 * Copyright (C) 1999, 2000, 2001 Novell, Inc. All Rights Reserved.
 *
 * THIS WORK IS SUBJECT TO U.S. AND INTERNATIONAL COPYRIGHT LAWS AND
 * TREATIES. USE, MODIFICATION, AND REDISTRIBUTION OF THIS WORK IS SUBJECT
 * TO VERSION 2.0.7 OF THE OPENLDAP PUBLIC LICENSE, A COPY OF WHICH IS
 * AVAILABLE AT HTTP://WWW.OPENLDAP.ORG/LICENSE.HTML OR IN THE FILE "LICENSE"
 * IN THE TOP-LEVEL DIRECTORY OF THE DISTRIBUTION. ANY USE OR EXPLOITATION
 * OF THIS WORK OTHER THAN AS AUTHORIZED IN VERSION 2.0.7 OF THE OPENLDAP
 * PUBLIC LICENSE, OR OTHER PRIOR WRITTEN CONSENT FROM NOVELL, COULD SUBJECT
 * THE PERPETRATOR TO CRIMINAL AND CIVIL LIABILITY.
 ******************************************************************************/

package com.novell.ldap.asn1;

import java.io.*;

/**
 * Represents the ASN.1 NULL type.
 */
public class ASN1Null extends ASN1Simple {

   /**
    * ASN.1 NULL tag definition.
    */
   public static final int TAG = 0x05;

   //*************************************************************************
   // Constructor for ASN1Null
   //*************************************************************************

   /**
    * Constructs an ASN1Null object.
    */
   public ASN1Null()
   {
      id = new ASN1Identifier(ASN1Identifier.UNIVERSAL, false, TAG);
   }

   //*************************************************************************
   // ASN1Object implementation
   //*************************************************************************

   /**
    * Encodes this Null directly to an output stream.
    */
   public void encode(ASN1Encoder enc, OutputStream out)
      throws IOException
   {
      enc.encode(this, out);
   }

   //*************************************************************************
   // ASN1Null specific methods
   //*************************************************************************

   /**
    * Return a String representation of this ASN1Object.
    */
   public String toString()
   {
      return super.toString() + "NULL: \"\"";
   }

}


/* **************************************************************************
 * $Novell: /ldap/src/jldap/com/novell/ldap/asn1/ASN1GeneralString.java,v 1.4 2000/09/11 21:05:52 vtag Exp $
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
 * Represents the ASN.1 GeneralString type.
 */
public class ASN1GeneralString extends ASN1CharacterString {

   /**
    * ASN.1 GeneralString tag definition.
    */
   public static final int TAG = 0x1b;

   //*************************************************************************
   // Constructors for ASN1GeneralString
   //*************************************************************************

   /**
    * Constructs an ASN1GeneralString object using a String value.
    */
   public ASN1GeneralString(String content)
   {
      id = new ASN1Identifier(ASN1Identifier.UNIVERSAL, false, TAG);
      this.content = content;
   }

   /**
    * Constructs an ASN1GeneralString object by decoding data from an input
    * stream.
    */
   public ASN1GeneralString(ASN1Decoder dec, InputStream in, int len)
      throws IOException
   {
      id = new ASN1Identifier(ASN1Identifier.UNIVERSAL, false, TAG);
      content = (String)dec.decodeCharacterString(in, len);
   }

   //*************************************************************************
   // ASN1GeneralString specific methods
   //*************************************************************************

   /**
    * Return a String representation of this ASN1GeneralString.
    */
   public String toString()
   {
      return super.toString() + "GeneralString: " + content;
   }

}


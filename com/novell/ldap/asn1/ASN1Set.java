/* **************************************************************************
 * $Novell: /ldap/src/jldap/src/com/novell/ldap/asn1/ASN1Set.java,v 1.3 2000/09/03 19:55:55 smerrill Exp $
 *
 * Copyright (C) 1999, 2000 Novell, Inc. All Rights Reserved.
 ***************************************************************************/

package com.novell.ldap.asn1;

import java.io.*;
import java.util.Vector;

/**
 * The ASN1Set class can hold an unordered collection of components with
 * distinct type.
 */
public class ASN1Set extends ASN1Structured {

   /**
    * ASN.1 SET tag definition.
    */
   public static final int TAG = 0x11;

   //*************************************************************************
   // Constructors for ASN1Set
   //*************************************************************************

   /**
    * Constructs an ASN1Set.
    */
   public ASN1Set()
   {
      this(5);
   }

   /**
    * Constructs an ASN1Set.
    *
    * @param size Specifies the initial size of the collection.
    */
   public ASN1Set(int size)
   {
      id = new ASN1Identifier(ASN1Identifier.UNIVERSAL, true, TAG);
      content = new Vector(size);
   }

   /**
    * Constructs an ASN1Set object by decoding data from an input stream.
    */
   public ASN1Set(ASN1Decoder dec, InputStream in, int len)
      throws IOException
   {
      id = new ASN1Identifier(ASN1Identifier.UNIVERSAL, true, TAG);
      decodeStructured(dec, in, len);
   }

   //*************************************************************************
   // ASN1Set specific methods
   //*************************************************************************

   /**
    * Return a String representation of this ASN1Set.
    */
   public String toString()
   {
      return super.toString("SET: { ");
   }

}


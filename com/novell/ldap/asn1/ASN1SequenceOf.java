/* **************************************************************************
 * $Novell: /ldap/src/jldap/com/novell/ldap/asn1/ASN1SequenceOf.java,v 1.4 2000/09/11 21:05:53 vtag Exp $
 *
 * Copyright (C) 1999, 2000 Novell, Inc. All Rights Reserved.
 ***************************************************************************/

package com.novell.ldap.asn1;

import java.io.*;
import com.novell.ldap.client.ArrayList;
import java.util.Enumeration;

/**
 * The ASN1SequenceOf class can hold an ordered collection of components with
 * identical type.
 */
public class ASN1SequenceOf extends ASN1Structured {

   /**
    * ASN.1 SEQUENCE OF tag definition.
    */
   public static final int TAG = 0x10;

   //*************************************************************************
   // Constructors for ASN1SequenceOf
   //*************************************************************************

   /**
    * Constructs an ASN1SequenceOf.
    */
   public ASN1SequenceOf()
   {
      this(5);
      return;
   }

   /**
    * Constructs an ASN1SequenceOf.
    *
    * @param size Specifies the initial size of the collection.
    */
   public ASN1SequenceOf(int size)
   {
      id = new ASN1Identifier(ASN1Identifier.UNIVERSAL, true, TAG);
      content = new ArrayList(size);
      return;
   }

   /**
    * A copy constructor which creates an ASN1SequenceOf from an
    * instance of ASN1Sequence.
    *
    * Since SEQUENCE and SEQUENCE_OF have the same identifier, the decoder
    * will always return a SEQUENCE object when it detects that identifier.
    * In order to take advantage of the ASN1SequenceOf type, we need to be
    * able to construct this object when knowingly receiving an
    * ASN1Sequence.
    */
   public ASN1SequenceOf(ASN1Sequence sequence)
   {
      id = new ASN1Identifier(ASN1Identifier.UNIVERSAL, true, TAG);
      content = new ArrayList(sequence.size());
      Enumeration e = sequence.elements();
      while(e.hasMoreElements()) {
         add((ASN1Object)e.nextElement());
      }
      return;
   }

   /**
    * Constructs an ASN1SequenceOf object by decoding data from an input
    * stream.
    */
   public ASN1SequenceOf(ASN1Decoder dec, InputStream in, int len)
      throws IOException
   {
      id = new ASN1Identifier(ASN1Identifier.UNIVERSAL, true, TAG);
      decodeStructured(dec, in, len);
      return;
   }

   //*************************************************************************
   // ASN1SequenceOf specific methods
   //*************************************************************************

   /**
    * Return a String representation of this ASN1SequenceOf.
    */
   public String toString()
   {
      return super.toString("SEQUENCE OF: { ");
   }
}

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
 * OF THIS WORK OTHER THAN AS AUTHORIZED IN VERSION 2.0.7 OF THE OPENLDAP
 * PUBLIC LICENSE, OR OTHER PRIOR WRITTEN CONSENT FROM NOVELL, COULD SUBJECT
 * THE PERPETRATOR TO CRIMINAL AND CIVIL LIABILITY.
 ******************************************************************************/

package com.novell.ldap.asn1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * This class encapsulates the ASN.1 ENUMERATED type.
 */
public class ASN1Enumerated extends ASN1Numeric {

   /**
    * ASN.1 tag definition for ENUMERATED
    */
   public static final int TAG = 0x0a;

   /**
    * ID is added for Optimization. id needs only be one Value for every instance
    * Thus we create it only once.
    */
    public static final ASN1Identifier ID =
        new ASN1Identifier(ASN1Identifier.UNIVERSAL, false, TAG);
   /* Constructors for ASN1Enumerated
    */

   /**
    * Call this constructor to construct an ASN1Enumerated
    * object from an integer value.
    *
    * @param content The integer value to be contained in the
    * this ASN1Enumerated object
    */
   public ASN1Enumerated(int content)
   {
      id = ID;
      this.content = new Long(content);
   }

   /**
    * Call this constructor to construct an ASN1Enumerated
    * object from a long value.
    *
    * @param content The long value to be contained in the
    * this ASN1Enumerated object
    */
   public ASN1Enumerated(long content)
   {
      id = ID;
      this.content = new Long(content);
   }

   /**
    * Constructs an ASN1Enumerated object by decoding data from an
    * input stream.
    *
    * @param dec The decoder object to use when decoding the
    * input stream.  Sometimes a developer might want to pass
    * in his/her own decoder object<br>
    *
    * @param in A byte stream that contains the encoded ASN.1
    *
    */
   public ASN1Enumerated(ASN1Decoder dec, InputStream in, int len)
      throws IOException
   {
      id = ID;
      content = (Long)dec.decodeNumeric(in, len);
   }


   /**
    * Call this method to encode the current instance into the
    * specified output stream using the specified encoder object.
    *
    * @param enc Encoder object to use when encoding self.<br>
    *
    * @param out The output stream onto which the encoded byte
    * stream is written.
    */
   public void encode(ASN1Encoder enc, OutputStream out)
      throws IOException
   {
      enc.encode(this, out);
   }

   /**
    * Return a String representation of this ASN1Enumerated.
    */
   public String toString()
   {
      return super.toString() + "ENUMERATED: " + content;
   }
}

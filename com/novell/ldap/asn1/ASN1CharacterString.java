/* **************************************************************************
 * $Novell: /ldap/src/jldap/src/com/novell/ldap/asn1/ASN1CharacterString.java,v 1.3 2000/09/03 06:43:06 smerrill Exp $
 *
 * Copyright (C) 1999, 2000 Novell, Inc. All Rights Reserved.
 ***************************************************************************/
 
package com.novell.ldap.asn1;

import java.io.*;

/**
 * Base class for all ASN.1 STRING types.
 */
abstract class ASN1CharacterString extends ASN1Simple {

   protected String content;

   //*************************************************************************
   // ASN1Object implementation
   //*************************************************************************

   /**
    * Encodes the contents of this ASN1CharacterString directly to an output
    * stream.
    */
   public void encode(ASN1Encoder enc, OutputStream out)
      throws IOException
   {
      enc.encode(this, out);
   }

   //*************************************************************************
   // ASN1CharacterString specific methods
   //*************************************************************************

   /**
    * Returns the content of this ASN1CharacterString as a String.
    */
   public String getContent()
   {
      return content;
   }

}


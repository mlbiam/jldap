/* **************************************************************************
 * $Novell: /ldap/src/jldap/src/com/novell/ldap/asn1/ASN1Numeric.java,v 1.3 2000/09/03 06:43:07 smerrill Exp $
 *
 * Copyright (C) 1999, 2000 Novell, Inc. All Rights Reserved.
 ***************************************************************************/

package com.novell.ldap.asn1;

import java.io.*;

/**
 * Base class for all ASN.1 numeric (integral) types.
 */
abstract class ASN1Numeric extends ASN1Simple {

   protected Long content;

   //*************************************************************************
   // ASN1Numeric specific methods
   //*************************************************************************

   /**
    * Returns the content of this ASN1Integer as an int.
    */
   public int getInt()
   {
      return (int)getLong();
   }

   /**
    * Returns the content of this ASN1Integer as a long.
    */
   public long getLong()
   {
      return content.longValue();
   }

}


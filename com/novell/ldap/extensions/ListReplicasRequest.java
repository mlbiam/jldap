/* **************************************************************************
 * $Id: ListReplicasRequest.java,v 1.8 2000/10/10 19:17:30 judy Exp $
 *
 * Copyright (C) 1999, 2000 Novell, Inc. All Rights Reserved.
 *
 * THIS WORK IS SUBJECT TO U.S. AND INTERNATIONAL COPYRIGHT LAWS AND
 * TREATIES. USE, MODIFICATION, AND REDISTRIBUTION OF THIS WORK IS SUBJECT
 * TO VERSION 2.0.1 OF THE OPENLDAP PUBLIC LICENSE, A COPY OF WHICH IS
 * AVAILABLE AT HTTP://WWW.OPENLDAP.ORG/LICENSE.HTML OR IN THE FILE "LICENSE"
 * IN THE TOP-LEVEL DIRECTORY OF THE DISTRIBUTION. ANY USE OR EXPLOITATION
 * OF THIS WORK OTHER THAN AS AUTHORIZED IN VERSION 2.0.1 OF THE OPENLDAP
 * PUBLIC LICENSE, OR OTHER PRIOR WRITTEN CONSENT FROM NOVELL, COULD SUBJECT
 * THE PERPETRATOR TO CRIMINAL AND CIVIL LIABILITY.
 ***************************************************************************/
package com.novell.ldap.extensions;

import com.novell.ldap.*;
import com.novell.ldap.asn1.*;
import java.io.*;

/**
 *
 *  Lists all the replicas that reside on the the specified directory server.
 *
 *  <p>To list replicas, you must create an instance
 *  of this class and then call the extendedOperation method with this
 *  object as the required LDAPExtendedOperation parameter.</p>
 *
 *  <p>The listReplicaRequest extension uses the following OID:<br>
 *  &nbsp;&nbsp;&nbsp;2.16.840.1.113719.1.27.100.19</p>
 *
 *  <p>The requestValue has the following format:<br>
 *
 *  requestValue ::=<br>
 *  &nbsp;&nbsp;&nbsp;&nbsp;  serverName &nbsp;&nbsp;&nbsp; LDAPDN</p>
 */
public class ListReplicasRequest extends LDAPExtendedOperation {

/**
 *  Constructs an extended operation object for listing replicas.
 *
 * @param serverName The server which contains replicas.
 *
 * @exception LDAPException A general exception which includes an error
 *                          message and an LDAP error code.
 */
 public ListReplicasRequest(String serverName)
                throws LDAPException {

        super(NamingContextConstants.LIST_REPLICAS_REQ, null);

        try {

            if (serverName == null)
                throw new LDAPException(LDAPExceptionMessageResource.PARAM_ERROR,
                                    LDAPException.PARAM_ERROR);

         ByteArrayOutputStream encodedData = new ByteArrayOutputStream();
         LBEREncoder encoder  = new LBEREncoder();

          ASN1OctetString asn1_serverName = new ASN1OctetString(serverName);

            asn1_serverName.encode(encoder, encodedData);

            setValue(encodedData.toByteArray());

        }
      catch(IOException ioe) {
         throw new LDAPException(LDAPExceptionMessageResource.ENCODING_ERROR,
                                 LDAPException.ENCODING_ERROR);
      }
   }

}

/* **************************************************************************
 * $Id: SchemaSyncRequest.java,v 1.1 2000/07/31 16:00:37 javed Exp $
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
package com.novell.ldap.ext; 

import com.novell.ldap.*;
import com.novell.ldap.client.protocol.lber.*;
import java.io.IOException;
 
/**
 *  public class SchemaSyncRequest
 *
 *      This class inherits from the LDAPExtendedOperation class
 *  and is used to synchronize the schema.
 *
 *  The OID used for this extended operation is:
 *      "2.16.840.1.113719.1.27.100.27"
 *
 *  The RequestValue has the folling ASN:
 *
 *  requestValue ::=
 *          serverName      LDAPDN
 *          delay           INTEGER
 */
public class SchemaSyncRequest extends LDAPExtendedOperation {
   
/**
 *  public SchemaSyncRequest()
 *
 *      The constructor takes four parameters:
 *
 *      String  serverName:     The server to sync from
 *
 *
 *      int delay:              The time in seconds after which the synchronization 
 *                              should start.
 *
 */   
 public SchemaSyncRequest(String serverName, int delay) 
                throws LDAPException {
        
        super(NamingContextConstants.SCHEMA_SYNC_REQ, null);
        
        try {
            // ber encode the parameters and set the requestValue
            LberEncoder requestlber = new LberEncoder();
            
            if (serverName == null)
                throw new LDAPException("Invalid parameter",
				                        LDAPException.PARAM_ERROR);
				                        
            requestlber.encodeString(serverName, true);  
            requestlber.encodeInt(delay);
                    
            setValue(requestlber.getTrimmedBuf());
            
        }
		catch(IOException ioe) {
			throw new LDAPException("Encoding Error",
				                     LDAPException.ENCODING_ERROR);
		}
   }

}
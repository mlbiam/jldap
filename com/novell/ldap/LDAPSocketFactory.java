/* **************************************************************************
 * $Novell: /ldap/src/jldap/com/novell/ldap/LDAPSocketFactory.java,v 1.4 2000/08/28 22:18:59 vtag Exp $
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
 
package com.novell.ldap;

import java.net.*;
import java.io.IOException;

/*
 * 4.36 public interface LDAPSocketFactory
 */
 
/**
 *
 *  Used to construct a socket connection for use in an LDAPConnection.
 *  An implementation of this interface may, for example, provide a
 *  TLSSocket connected to a secure server.
 */
public interface LDAPSocketFactory {

   /*
    * 4.36.1 makeSocket
    */

   /**
    * Returns a socket connected using the provided host name and port
    * number.
    *
    *  @param host     The hostname or dotted string representing
    *                  the IP address of a host running an LDAP server
    *                  to connect to.
    *<br><br>
    *  @param port     The TCP or UDP port number to connect to
    *                  or contact. The default LDAP port is 389.
    *
    * @exception IOException The socket to the specified host and port
    *                        could not be created.
    * 
    * @exception UnknownHostException The specified host could not be found.
    */
   public Socket makeSocket(String host, int port)
      throws IOException, UnknownHostException;

}

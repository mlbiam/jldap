/* **************************************************************************
 * $Novell: /ldap/src/jldap/com/novell/ldap/LDAPResponse.java,v 1.25 2001/02/26 19:58:24 vtag Exp $
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

package com.novell.ldap;

import java.io.IOException;
import java.util.Vector;

import com.novell.ldap.asn1.*;
import com.novell.ldap.rfc2251.*;
import com.novell.ldap.client.*;

/**
 *  Represents the a message received from an LDAPServer
 *  in response to an asynchronous request.
 */

 /*
  * Note: Exceptions generated by the reader thread are returned
  * to the application as an exception in an LDAPResponse.  Thus
  * if <code>exception</code> has a value, it is not a server response,
  * but instad an exception returned to the application from the API.
  */
public class LDAPResponse extends LDAPMessage
{
    private LocalException exception = null;
    private String[] referralList;
    private String activeReferral;

    /**
     * Creates an LDAPMessage using an LDAPException
     *
     *  @param message  The exception
     * <br><br>
     *  @param refeerralException  True if exception came from a connection
     *                             created to follow referrals.
     */
    public LDAPResponse( LocalException ex,
                         String[] referralList,
                         String activeReferral)
    {
        exception = ex;
        this.referralList = referralList;
        this.activeReferral = activeReferral;
        if( Debug.LDAP_DEBUG) {
            Debug.trace( Debug.messages,
                "new LDAPResponse: referral " + (this.referralList != null) +
                "\n\texception:" +  ex.toString());
        }

        return;
    }

    /**
     * Creates an LDAPMessage when receiving an asynchronous response from a
     * server.
     *
     *  @param message  The RfcLDAPMessage from a server.
     */
    /*package*/
    LDAPResponse( RfcLDAPMessage message)
    {
        super(message);
        return;
    }

    /**
     * Returns any error message in the response.
     *
     * @return Any error message in the response.
     */
    public String getErrorMessage()
    {
        if( exception != null) {
            return( exception.getLDAPErrorMessage());
        }
        return ((RfcResponse)message.getProtocolOp()).getErrorMessage().getString();
    }

    /**
     * Returns the partially matched DN field from the server response,
     * if the response contains one.
     *
     * @return The partially matched DN field, if the response contains one.
     *
     */
    public String getMatchedDN()
    {
        if( exception != null) {
            return exception.getMatchedDN();
        }
        return ((RfcResponse)message.getProtocolOp()).getMatchedDN().getString();
    }

    /**
     * Returns all referrals in a server response, if the response contains any.
     *
     * @return All the referrals in the server response.
     */
    public String[] getReferrals()
    {
        if( exception != null) {
            // No referral exceptions returned from this source
            return null;
        }

        String[] referrals = null;
        RfcReferral ref = ((RfcResponse)message.getProtocolOp()).getReferral();

        if(ref != null) {
            // convert RFC 2251 Referral to String[]
            int size = ref.size();
            referrals = new String[size];
            for(int i=0; i<size; i++) {
                referrals[i] = new String(((ASN1OctetString)ref.get(i)).getContent());
            }
        }
        return referrals;
   }

    /**
     * Returns the result code in a server response.
     *
     * <p> For a list of result codes, see the LDAPException class. </p>
     *
     * @return The result code.
     */
    public int getResultCode()
    {
        if( exception != null) {
            return exception.getLDAPResultCode();
        }
        return ((RfcResponse)message.getProtocolOp()).getResultCode().getInt();
    }

    /**
     * Checks the resultCode and throws the appropriate exception.
     */
    /* package */
    void chkResultCode() throws LDAPException
    {
        if( exception != null) {
            throw exception;
        } else {
            if( Debug.LDAP_DEBUG) {
                Debug.trace( Debug.messages, "LDAPResponse: message(" +
                    getMessageID() + ") result code " + getResultCode());
            }
            LDAPException ex = getResultException();
            if( ex != null) {
                throw ex;
            }
            return;
        }
    }

    /**
     * Checks the resultCode and generates the appropriate exception or
     * null if success.
     */
    /* package */
    LDAPException getResultException()
    {
        LDAPException ex = null;
        switch(getResultCode()) {
        case LDAPException.SUCCESS:
            break;
        case LDAPException.OPERATIONS_ERROR:
        case LDAPException.PROTOCOL_ERROR:
        case LDAPException.TIME_LIMIT_EXCEEDED:
        case LDAPException.SIZE_LIMIT_EXCEEDED:
        case LDAPException.AUTH_METHOD_NOT_SUPPORTED:
        case LDAPException.STRONG_AUTH_REQUIRED:
        case LDAPException.LDAP_PARTIAL_RESULTS:
        case LDAPException.ADMIN_LIMIT_EXCEEDED:
        case LDAPException.UNAVAILABLE_CRITICAL_EXTENSION:
        case LDAPException.CONFIDENTIALITY_REQUIRED:
        case LDAPException.SASL_BIND_IN_PROGRESS:
        case LDAPException.NO_SUCH_ATTRIBUTE:
        case LDAPException.UNDEFINED_ATTRIBUTE_TYPE:
        case LDAPException.INAPPROPRIATE_MATCHING:
        case LDAPException.CONSTRAINT_VIOLATION:
        case LDAPException.ATTRIBUTE_OR_VALUE_EXISTS:
        case LDAPException.INVALID_ATTRIBUTE_SYNTAX:
        case LDAPException.NO_SUCH_OBJECT:
        case LDAPException.ALIAS_PROBLEM:
        case LDAPException.INVALID_DN_SYNTAX:
        case LDAPException.IS_LEAF:
        case LDAPException.ALIAS_DEREFERENCING_PROBLEM:
        case LDAPException.INAPPROPRIATE_AUTHENTICATION:
        case LDAPException.INVALID_CREDENTIALS:
        case LDAPException.INSUFFICIENT_ACCESS_RIGHTS:
        case LDAPException.BUSY:
        case LDAPException.UNAVAILABLE:
        case LDAPException.UNWILLING_TO_PERFORM:
        case LDAPException.LOOP_DETECT:
        case LDAPException.NAMING_VIOLATION:
        case LDAPException.OBJECT_CLASS_VIOLATION:
        case LDAPException.NOT_ALLOWED_ON_NONLEAF:
        case LDAPException.NOT_ALLOWED_ON_RDN:
        case LDAPException.ENTRY_ALREADY_EXISTS:
        case LDAPException.OBJECT_CLASS_MODS_PROHIBITED:
        case LDAPException.AFFECTS_MULTIPLE_DSAS:
        case LDAPException.OTHER:
        case LDAPException.SERVER_DOWN:
        case LDAPException.LOCAL_ERROR:
        case LDAPException.ENCODING_ERROR:
        case LDAPException.DECODING_ERROR:
        case LDAPException.LDAP_TIMEOUT:
        case LDAPException.AUTH_UNKNOWN:
        case LDAPException.FILTER_ERROR:
        case LDAPException.USER_CANCELLED:
        case LDAPException.PARAM_ERROR:
        case LDAPException.NO_MEMORY:
        case LDAPException.CONNECT_ERROR:
        case LDAPException.LDAP_NOT_SUPPORTED:
        case LDAPException.CONTROL_NOT_FOUND:
        case LDAPException.NO_RESULTS_RETURNED:
        case LDAPException.MORE_RESULTS_TO_RETURN:
        case LDAPException.CLIENT_LOOP:
        case LDAPException.REFERRAL_LIMIT_EXCEEDED:
            ex = new LDAPException(getErrorMessage(),
                getResultCode(), getMatchedDN());
            break;
        case LDAPException.REFERRAL:
            // only get here if automatic referral handling is not enabled.
            String[] refs = getReferrals();
            if( Debug.LDAP_DEBUG ) {
                Debug.trace( Debug.messages, "LDAPResponse: Generating RfcReferral Exception");
                for( int i = 0; i < refs.length; i++) {
                    Debug.trace( Debug.messages, "LDAPResponse: \t" + refs[i]);
                }
            }
            ex = new LDAPReferralException(
                    "Automatic referral following not enabled");
            ((LDAPReferralException)ex).setReferrals( refs);
            break;
        default: // unknown
            ex = new LDAPException(getErrorMessage(),
                getResultCode(), getMatchedDN());
            break;
        }
        return ex;
    }

    /* Methods from LDAPMessage */

    /**
     * Returns any controls in the message.
     *
     * @see com.novell.ldap.LDAPMessage#getControls()
     */
    public LDAPControl[] getControls() {
        if( exception != null) {
            return null;
        }
        return super.getControls();
    }
    /**
     * Returns the message ID.
     *
     * @see com.novell.ldap.LDAPMessage#getMessageID()
     */
    public int getMessageID() {
        if( exception != null) {
            return exception.getMessageID();
       }
        return super.getMessageID();
    }

    /**
     * Returns the LDAP operation type of the message.
     *
     * @return The operation type of the message.
     *
     * @see com.novell.ldap.LDAPMessage#getType()
     */
    public int getType()
	{
        if( exception != null) {
           return exception.getReplyType();
        }
		return super.getType();
    }

    /**
     * Indicates if this response is an embedded exception response
     *
     * @return true if contains an embedded LDAPexception
     */
     /*package*/
     boolean hasException()
     {
        return (exception != null);
     }

    /**
     * Returns an embedded exception response
     *
     * @return an embedded exception if any
     */
     /*package*/
     LDAPException getException()
     {
        return exception;
     }

    /**
     * Returns the list of referals if
     * connection created to follow referrals.
     *
     * @return list of referrals
     */
     /*package*/
     String[] getReferralList()
     {
        return referralList;
     }

    /**
     * Indicates the referral instance being followed if the
     * connection created to follow referrals.
     *
     * @return the referral being followed
     */
     /*package*/
     String getActiveReferral()
     {
        return activeReferral;
     }
}

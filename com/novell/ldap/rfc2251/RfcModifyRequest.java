
package com.novell.ldap.rfc2251;

import com.novell.ldap.asn1.*;
import com.novell.ldap.*;

/**
 *       ModifyRequest ::= [APPLICATION 6] SEQUENCE {
 *               object          LDAPDN,
 *               modification    SEQUENCE OF SEQUENCE {
 *                       operation       ENUMERATED {
 *                                               add     (0),
 *                                               delete  (1),
 *                                               replace (2) },
 *                       modification    AttributeTypeAndValues } }
 */
public class RfcModifyRequest extends ASN1Sequence implements RfcRequest {

	//*************************************************************************
	// Constructor for ModifyRequest
	//*************************************************************************

	/**
	 *
	 */
	public RfcModifyRequest(RfcLDAPDN object, ASN1SequenceOf modification)
	{
		super(2);
		add(object);
		add(modification);
	}

	//*************************************************************************
	// Accessors
	//*************************************************************************

	/**
	 * Override getIdentifier to return an application-wide id.
	 */
	public ASN1Identifier getIdentifier()
	{
		return new ASN1Identifier(ASN1Identifier.APPLICATION, true,
			                       RfcProtocolOp.MODIFY_REQUEST);
	}

    public RfcRequest dupRequest(String base, String filter, Integer scope)
            throws LDAPException
    {
        throw new LDAPException(
                    LDAPExceptionMessageResource.NO_DUP_REQUEST,
                    new Object[] { "modify" },
                    LDAPException.LDAP_NOT_SUPPORTED);
    }
}

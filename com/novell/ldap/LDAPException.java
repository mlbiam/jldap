/* **************************************************************************
 * $Novell: /ldap/src/jldap/com/novell/ldap/LDAPException.java,v 1.30 2001/06/29 15:48:13 vtag Exp $
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

import com.novell.ldap.client.Debug;
import com.novell.ldap.resources.*;
import java.util.Locale;

/**
 *
 *  Thrown to indicate that an LDAP error has occurred. This is a general
 *  exception which includes an error message and an LDAP error code.
 *
 *  <p>An LDAPException can result from physical problems (such as
 *  network errors) as well as problems with LDAP operations (for
 *  example, if the LDAP add operation fails because of a
 *  duplicate entry).</p>
 *
 *  <p>Five possible sources of information are available from LDAPException:
 *	<dl>
 *		<dt>Error Code:</dt>
 *		<dd>The getLDAPResultCode method returns a result code, which can be
 *		compared against standard LDAP result codes. </dd>
 *  	<dt>Error Message:</dt>
 *		<dd>The getMessage method returns localized error message from the
 *      message resource. This is specified by the client API.
 * 		</dd>
 *		<dt>LDAP server Message:</dt>
 *		<dd>The getLDAPMessage returns any message returned by the LDAP
 *			server. </dd>
 *		<dt>Matched DN:</dt>
 *		<dd>The getMatchedDN method retrieves the part of a submitted
 *			distinguished name which could be matched by the server</dd>
 *		<dt>Root Cause:</dt>
 *		<dd>The getCause method returns the a nested exeption that was the
 *			original cause for the error. </dd>
 *  </dl>
 * 	</p>
 *
 *  <p>The following lists the standard LDAP result codes.
 *  See RFC2251 for a discussion of the meanings of the result codes.
 *  The corresponding ASN.1 definition from RFC2251 is provided in parentheses.</p>
 *
 *   <li> ADMIN_LIMIT_EXCEEDED (adminLimitExceeded)</li>
 *   <li> AFFECTS_MULTIPLE_DSAS (affectsMultipleDSAs</li>
 *   <li> ALIAS_DEREFERENCING_PROBLEM (aliasDereferencingProblem)</li>
 *   <li> ALIAS_PROBLEM (aliasProblem)</li>
 *   <li> ATTRIBUTE_OR_VALUE_EXISTS (AttributeOrValueExists)</li>
 *   <li> AUTH_METHOD_NOT_SUPPORTED (authMethodNotSupported)</li>
 *   <li> BUSY (busy)</li>
 *   <li> COMPARE_FALSE (compareFalse)</li>
 *   <li> COMPARE_TRUE (compareTrue)</li>
 *   <li> CONFIDENTIALITY_REQUIRED (confidentialityRequired)</li>
 *   <li> CONSTRAINT_VIOLATION (constraintViolation)</li>
 *   <li> ENTRY_ALREADY_EXISTS (entryAlreadyExists)</li>
 *   <li> INAPPROPRIATE_AUTHENTICATION (inappropriateAuthentication)</li>
 *   <li> INAPPROPRIATE_MATCHING (inappropriateMatching)</li>
 *   <li> INSUFFICIENT_ACCESS_RIGHTS (insufficientAccessRights)</li>
 *   <li> INVALID_ATTRIBUTE_SYNTAX (invalidAttributeSyntax)</li>
 *   <li> INVALID_CREDENTIALS (invalidCredentials)</li>
 *   <li> INVALID_DN_SYNTAX (invalidDNSyntax)</li>
 *   <li> IS_LEAF (isLeaf)</li>
 *   <li> LOOP_DETECT (loopDetect)</li>
 *   <li> NAMING_VIOLATION (namingViolation)</li>
 *   <li> NO_SUCH_ATTRIBUTE (noSuchAttribute)</li>
 *   <li> NO_SUCH_OBJECT (noSuchObject)</li>
 *   <li> NOT_ALLOWED_ON_NONLEAF (notAllowedOnNonLeaf)</li>
 *   <li> NOT_ALLOWED_ON_RDN (notAllowedOnRDN)</li>
 *   <li> OBJECT_CLASS_MODS_PROHIBITED (objectClassModsProhibited)</li>
 *   <li> OBJECT_CLASS_VIOLATION (objectClassViolation)</li>
 *   <li> OPERATIONS_ERROR (operationsError)</li>
 *   <li> OTHER (other)</li>
 *   <li> PROTOCOL_ERROR (protocolError)</li>
 *   <li> REFERRAL (referral)</li>
 *   <li> SASL_BIND_IN_PROGRESS (saslBindInProgress)</li>
 *   <li> SIZE_LIMIT_EXCEEDED (sizeLimitExceeded)</li>
 *   <li> STRONG_AUTH_REQUIRED (strongAuthRequired)</li>
 *   <li> SUCCESS (success)</li>
 *   <li> TIME_LIMIT_EXCEEDED (timeLimitExceeded)</li>
 *   <li> UNAVAILABLE (unavailable)</li>
 *   <li> UNAVAILABLE_CRITICAL_EXTENSION (unavailableCriticalExtension)</li>
 *   <li> UNDEFINED_ATTRIBUTE_TYPE (undefinedAttributeType)</li>
 *   <li> UNWILLING_TO_PERFORM (unwillingToPerform)</li>
 *
 *  <p>Local errors, resulting from actions other than an operation on a
 *  server, are among the following, listed in draft for the ldap-c-api:</p>
 *
 *   <li> AUTH_UNKNOWN (0x56)</li>
 *   <li> CLIENT_LOOP (0x60)</li>
 *   <li> CONNECT_ERROR (0x5d)</li>
 *   <li> CONTROL_NOT_FOUND( 0x54)</li>
 *   <li> DECODING_ERROR (0x54)</li>
 *   <li> ENCODING_ERROR (0x53)</li>
 *   <li> FILTER_ERROR (0x57)</li>
 *   <li> LOCAL_ERROR (0x52)</li>
 *   <li> LDAP_NOT_SUPPORTED (0x5c)</li>
 *   <li> LDAP_TIMEOUT (0x55)</li>
 *   <li> MORE_RESULTS_TO_RETURN (0x5f)</li>
 *   <li> NO_MEMORY (0x5a)</li>
 *   <li> NO_RESULTS_RETURNED (0x5e)</li>
 *   <li> PARAM_ERROR (0x59)</li>
 *   <li> REFERRAL_LIMIT_EXCEEDED (0x61)</li>
 *   <li> SERVER_DOWN (0x51)</li>
 *   <li> USER_CANCELLED (0x58)</li>
 *   <li> TLS_NOT_SUPPORTED (0x70)</li>
 */

public class LDAPException extends Exception
{

   private int resultCode = 0;
   private String messageOrKey = null;
   private Object[] arguments = null;
   private String matchedDN = null;
   private Throwable rootException = null;
   private String serverMessage = null;

  /**
   *Indicates the requested client operation completed successfully.
   *
   * <p>SUCCESS = 0<p/>
   */
   public final static int SUCCESS = 0;

  /**
   * Indicates an internal error.
   *
   * <p>The server is unable to respond with a more specific error and is
   * also unable to properly respond to a request. It does not indicate
   * that the client has sent an erroneous message.</p>
   *
   *<p>OPERATIONS_ERROR = 1</p>
   */
   public final static int OPERATIONS_ERROR = 1;

  /**
   * Indicates that the server has received an invalid or malformed request
   * from the client.
   *
   *<p>PROTOCOL_ERROR = 2</p>
   */
   public final static int PROTOCOL_ERROR = 2;

  /**
   * Indicates that the operation's time limit specified by either the
   * client or the server has been exceeded.
   *
   * <p>On search operations, incomplete results are returned.</p>
   *
   *<p>TIME_LIMIT_EXCEEDED = 3</p>
   */
   public final static int TIME_LIMIT_EXCEEDED = 3;

  /**
   * Indicates that in a search operation, the size limit specified by
   * the client or the server has been exceeded. Incomplete results are
   * returned.
   *
   * <p>SIZE_LIMIT_EXCEEDED = 4</p>
   */
   public final static int SIZE_LIMIT_EXCEEDED = 4;

  /**
   * Does not indicate an error condition. Indicates that the results of
   * a compare operation are false.
   *
   * <p>COMPARE_FALSE = 5</p>
   */
   public final static int COMPARE_FALSE = 5;

  /**
   * Does not indicate an error condition. Indicates that the results of a
   * compare operation are true.
   *
   * <p>COMPARE_TRUE = 6</p>
   */
   public final static int COMPARE_TRUE = 6;

  /**
   * Indicates that during a bind operation the client requested an
   * authentication method not supported by the LDAP server.
   *
   * <p>AUTH_METHOD_NOT_SUPPORTED = 7</p>
   */
   public final static int AUTH_METHOD_NOT_SUPPORTED = 7;

  /**
   *Indicates a problem with the level of authentication.
   *
   * <p>One of the following has occurred:
   *<ul>
   * <li>In bind requests, the LDAP server accepts only strong
   *     authentication.</li>
   * <li>In a client request, the client requested an operation such as delete
   *     that requires strong authentication.</li>
   * <li>In an unsolicited notice of disconnection, the LDAP server discovers
   *     the security protecting the communication between the client and
   *     server has unexpectedly failed or been compromised.</li>
   *</ul>
   * <p>STRONG_AUTH_REQUIRED = 8</p>
   */
   public final static int STRONG_AUTH_REQUIRED = 8;

   /**
   * Returned by some LDAP servers to LDAPv2 clients to indicate that a referral
   * has been returned in the error string.
   *
   * <p>LDAP_PARTIAL_RESULTS = 9</p>
   */
   public final static int LDAP_PARTIAL_RESULTS = 9;

  /**
   * Does not indicate an error condition. In LDAPv3, indicates that the server
   * does not hold the target entry of the request, but that the servers in the
   * referral field may.
   *
   * <p>REFERRAL = 10</p>
   */
   public final static int REFERRAL = 10;

  /**
   * Indicates that an LDAP server limit set by an administrative authority
   * has been exceeded.
   *
   * <p>ADMIN_LIMIT_EXCEEDED = 11</p>
   */
   public final static int ADMIN_LIMIT_EXCEEDED = 11;

  /**
   * Indicates that the LDAP server was unable to satisfy a request because
   * one or more critical extensions were not available.
   *
   * <p>Either the server does not support the control or the control is not
   *  appropriate for the operation type.</p>
   *
   * <p>UNAVAILABLE_CRITICAL_EXTENSION = 12</p>
   */
   public final static int UNAVAILABLE_CRITICAL_EXTENSION = 12;

  /**
   * Indicates that the session is not protected by a protocol such as
   * Transport Layer Security (TLS), which provides session confidentiality.
   *
   * <p>CONFIDENTIALITY_REQUIRED = 13</p>
   */
   public final static int CONFIDENTIALITY_REQUIRED = 13;

  /**
   * Does not indicate an error condition, but indicates that the server is
   * ready for the next step in the process. The client must send the server
   * the same SASL mechanism to continue the process.
   *
   * <p>SASL_BIND_IN_PROGRESS = 14</p>
   */
   public final static int SASL_BIND_IN_PROGRESS = 14;

  /**
   * Indicates that the attribute specified in the modify or compare
   * operation does not exist in the entry.
   *
   * <p>NO_SUCH_ATTRIBUTE = 16</p>
   */
   public final static int NO_SUCH_ATTRIBUTE = 16;

  /**
   * Indicates that the attribute specified in the modify or add operation
   * does not exist in the LDAP server's schema.
   *
   * <p>UNDEFINED_ATTRIBUTE_TYPE = 17</p>
   */
   public final static int UNDEFINED_ATTRIBUTE_TYPE = 17;

  /**
   * Indicates that the matching rule specified in the search filter does
   * not match a rule defined for the attribute's syntax.
   *
   * <p>INAPPROPRIATE_MATCHING = 18</p>
   */
   public final static int INAPPROPRIATE_MATCHING = 18;

  /**
   * Indicates that the attribute value specified in a modify, add, or
   * modify DN operation violates constraints placed on the attribute. The
   * constraint can be one of size or content (for example, string only,
   * no binary data).
   *
   * <p>CONSTRAINT_VIOLATION = 19</p>
   */
   public final static int CONSTRAINT_VIOLATION = 19;

  /**
   * Indicates that the attribute value specified in a modify or add
   * operation already exists as a value for that attribute.
   *
   * <p>ATTRIBUTE_OR_VALUE_EXISTS = 20</p>
   */
   public final static int ATTRIBUTE_OR_VALUE_EXISTS = 20;

  /**
   * Indicates that the attribute value specified in an add, compare, or
   * modify operation is an unrecognized or invalid syntax for the attribute.
   *
   * <p>INVALID_ATTRIBUTE_SYNTAX = 21</p>
   */
   public final static int INVALID_ATTRIBUTE_SYNTAX = 21;

  /**
   * Indicates the target object cannot be found.
   *
   * <p>This code is not returned on the following operations:</p>
   * <ul>
   * <li>Search operations that find the search base but cannot find any
   *     entries that match the search filter.</li>
   * <li>Bind operations.</li>
   * </ul>
   * <p>NO_SUCH_OBJECT = 32</p>
   */
   public final static int NO_SUCH_OBJECT = 32;

  /**
   * Indicates that an error occurred when an alias was dereferenced.
   *
   * <p>ALIAS_PROBLEM = 33</p>
   */
   public final static int ALIAS_PROBLEM = 33;

  /**
   * Indicates that the syntax of the DN is incorrect.
   *
   * <p>If the DN syntax is correct, but the LDAP server's structure
   *  rules do not permit the operation, the server returns
   *  LDAP_UNWILLING_TO_PERFORM. </p>
   *
   * <p>INVALID_DN_SYNTAX = 34</p>
   */
   public final static int INVALID_DN_SYNTAX = 34;

  /**
   * Indicates that the specified operation cannot be performed on a
   * leaf entry.
   *
   * <p>This code is not currently in the LDAP specifications, but is
   * reserved for this constant.</p>
   *
   * <p>IS_LEAF = 35</p>
   */
   public final static int IS_LEAF = 35;

  /**
   * Indicates that during a search operation, either the client does not
   * have access rights to read the aliased object's name or dereferencing
   * is not allowed.
   *
   * <p>ALIAS_DEREFERENCING_PROBLEM = 36</p>
   */
   public final static int ALIAS_DEREFERENCING_PROBLEM = 36;

  /**
   * Indicates that during a bind operation, the client is attempting to use
   * an authentication method that the client cannot use correctly.
   *
   *<p> For example, either of the following cause this error:</p>
   * <ul>
   * <li>The client returns simple credentials when strong credentials are
   *     required.
   * <li>The client returns a DN and a password for a simple bind when the
         entry does not have a password defined.
   * </ul>
   * <p>INAPPROPRIATE_AUTHENTICATION = 48</p>
   */
   public final static int INAPPROPRIATE_AUTHENTICATION = 48;

  /**
   * Indicates that invalid information was passed during a bind operation.
   *
   * <p>One of the following occurred:
   *<ul>
   * <li> The client passed either an incorrect DN or password.</li>
   * <li> The password is incorrect because it has expired, intruder detection
   *       has locked the account, or some other similar reason.</li>
   *</ul>
   * <p>INVALID_CREDENTIALS = 49</p>
   */
   public final static int INVALID_CREDENTIALS = 49;

  /**
   * Indicates that the caller does not have sufficient rights to perform
   * the requested operation.
   *
   * <p>INSUFFICIENT_ACCESS_RIGHTS = 50</p>
   */
   public final static int INSUFFICIENT_ACCESS_RIGHTS = 50;

  /**
   * Indicates that the LDAP server is too busy to process the client request
   * at this time, but if the client waits and resubmits the request, the
   * server may be able to process it then.
   *
   * <p>BUSY = 51</p>
   */
   public final static int BUSY = 51;

  /**
   * Indicates that the LDAP server cannot process the client's bind
   * request, usually because it is shutting down.
   *
   * <p>UNAVAILABLE = 52</p>
   */
   public final static int UNAVAILABLE = 52;

  /**
   * Indicates that the LDAP server cannot process the request because of
   * server-defined restrictions.
   *
   * <p>This error is returned for the following reasons:</p>
   * <ul>
   * <li>The add entry request violates the server's structure rules.</li>
   * <li>The modify attribute request specifies attributes that users
   *     cannot modify.</li>
   * </ul>
   * <p>UNWILLING_TO_PERFORM = 53</p>
   */
   public final static int UNWILLING_TO_PERFORM = 53;

  /**
   * Indicates that the client discovered an alias or referral loop,
   * and is thus unable to complete this request.
   *
   * <p>LOOP_DETECT = 54</p>
   */
   public final static int LOOP_DETECT = 54;

  /**
   * Indicates that the add or modify DN operation violates the schema's
   * structure rules.
   *
   * <p>For example,</p>
   *<ul>
   * <li>The request places the entry subordinate to an alias.</li>
   * <li>The request places the entry subordinate to a container that
   *     is forbidden by the containment rules.</li>
   * <li>The RDN for the entry uses a forbidden attribute type.</li>
   *
   * <p>NAMING_VIOLATION = 64</p>
   */
   public final static int NAMING_VIOLATION = 64;

  /**
   * Indicates that the add, modify, or modify DN operation violates the
   * object class rules for the entry.
   *
   * <p>For example, the following types of request return this error:</p>
   * <ul>
   * <li>The add or modify operation tries to add an entry without a value
   *     for a required attribute.</li>
   * <li>The add or modify operation tries to add an entry with a value for
   *     an attribute which the class definition does not contain.</li>
   * <li>The modify operation tries to remove a required attribute without
   *     removing the auxiliary class that defines the attribute as required.</li>
   *</ul>
   * <p>OBJECT_CLASS_VIOLATION = 65</p>
   */
   public final static int OBJECT_CLASS_VIOLATION = 65;

  /**
   * Indicates that the requested operation is permitted only on leaf entries.
   *
   * <p>For example, the following types of requests return this error:</p>
   * <ul>
   * <li>The client requests a delete operation on a parent entry.</li>
   * <li> The client request a modify DN operation on a parent entry.</li>
   * </ul>
   * <p>NOT_ALLOWED_ON_NONLEAF = 66</p>
   */
   public final static int NOT_ALLOWED_ON_NONLEAF = 66;

  /**
   * Indicates that the modify operation attempted to remove an attribute
   * value that forms the entry's relative distinguished name.
   *
   * <p>NOT_ALLOWED_ON_RDN = 67</p>
   */
   public final static int NOT_ALLOWED_ON_RDN = 67;

  /**
   * Indicates that the add operation attempted to add an entry that already
   * exists, or that the modify operation attempted to rename an entry to the
   * name of an entry that already exists.
   *
   * <p>ENTRY_ALREADY_EXISTS = 68</p>
   */
   public final static int ENTRY_ALREADY_EXISTS = 68;

  /**
   * Indicates that the modify operation attempted to modify the structure
   * rules of an object class.
   *
   * <p>OBJECT_CLASS_MODS_PROHIBITED = 69</p>
   */
   public final static int OBJECT_CLASS_MODS_PROHIBITED = 69;

  /**
   * Indicates that the modify DN operation moves the entry from one LDAP
   * server to another and thus requires more than one LDAP server.
   *
   * <p>AFFECTS_MULTIPLE_DSAS = 71</p>
   */
   public final static int AFFECTS_MULTIPLE_DSAS = 71;

  /**
   * Indicates an unknown error condition.
   *
   * <p>OTHER = 80</p>
   */
   public final static int OTHER = 80;

  /**
   * Indicates that the LDAP libraries cannot establish an initial connection
   * with the LDAP server. Either the LDAP server is down or the specified
   * host name or port number is incorrect.
   *
   * <p>SERVER_DOWN = 81</p>
   */
   public final static int SERVER_DOWN = 81;

  /**
   * Indicates that the LDAP client has an error. This is usually a failed
   * dynamic memory allocation error.
   *
   * <p>LOCAL_ERROR = 82</p>
   */
   public final static int LOCAL_ERROR = 82;

  /**
   * Indicates that the LDAP client encountered errors when encoding an
   * LDAP request intended for the LDAP server.
   *
   * <p>ENCODING_ERROR = 83</p>
   */
   public final static int ENCODING_ERROR = 83;

  /**
   * Indicates that the LDAP client encountered errors when decoding an
   * LDAP response from the LDAP server.
   *
   * <p>DECODING_ERROR = 84</p>
   */
   public final static int DECODING_ERROR = 84;

  /**
   * Indicates that the time limit of the LDAP client was exceeded while
   * waiting for a result.
   *
   * <p>LDAP_TIMEOUT = 85</p>
   */
   public final static int LDAP_TIMEOUT = 85;

  /**
   * Indicates that a bind method was called with an unknown
   * authentication method.
   *
   * <p>AUTH_UNKNOWN = 86</p>
   */
   public final static int AUTH_UNKNOWN = 86;

  /**
   * Indicates that the search method was called with an invalid
   * search filter.
   *
   * <p>FILTER_ERROR = 87</p>
   */
   public final static int FILTER_ERROR = 87;

  /**
   * Indicates that the user cancelled the LDAP operation.
   *
   * <p>USER_CANCELLED = 88</p>
   */
   public final static int USER_CANCELLED = 88;

  /**
   * Indicates that an LDAP function was called with an invalid
   * parameter value.
   *
   * <p>PARAM_ERROR = 89</p>
   *
   * @deprecated No longer used - invalid parameters generate a RuntimeException
   */
   public final static int PARAM_ERROR = 89;

  /**
   * Indicates that a dynamic memory allocation method failed when calling
   * an LDAP method.
   *
   * <p>NO_MEMORY = 90</p>
   */
   public final static int NO_MEMORY = 90;

  /**
   * Indicates that the LDAP client has lost either its connection or
   * cannot establish a connection to the LDAP server.
   *
   * <p>CONNECT_ERROR = 91</p>
   */
   public final static int CONNECT_ERROR = 91;

  /**
   * Indicates that the requested functionality is not supported by the
   * client. For example, if the LDAP client is established as an LDAPv2
   * client, the libraries set this error code when the client requests
   * LDAPv3 functionality.
   *
   * <p>LDAP_NOT_SUPPORTED = 92</p>
   */
   public final static int LDAP_NOT_SUPPORTED = 92;

  /**
   * Indicates that the client requested a control that the libraries
   * cannot find in the list of supported controls sent by the LDAP server.
   *
   * <p>CONTROL_NOT_FOUND = 93</p>
   */
   public final static int CONTROL_NOT_FOUND = 93;

  /**
   * Indicates that the LDAP server sent no results.
   *
   * <p>NO_RESULTS_RETURNED = 94</p>
   */
   public final static int NO_RESULTS_RETURNED = 94;

  /**
   * Indicates that more results are chained in the result message.
   *
   * <p>MORE_RESULTS_TO_RETURN = 95</p>
   */
   public final static int MORE_RESULTS_TO_RETURN = 95;

  /**
   * Indicates the LDAP libraries detected a loop. Usually this happens
   * when following referrals.
   *
   * <p>CLIENT_LOOP = 96</p>
   */
   public final static int CLIENT_LOOP = 96;

  /**
   * Indicates that the referral exceeds the hop limit. The default hop
   * limit is ten.
   *
   * <p>The hop limit determines how many servers the client can hop through
   * to retrieve data. For example, suppose the following conditions:</p>
   * <ul>
   * <li>Suppose the hop limit is two.</li>
   * <li> If the referral is to server D which can be contacted only through
          server B (1 hop) which contacts server C (2 hops) which contacts
          server D (3 hops).</li>
   * </ul>
   * <p> With these conditions, the hop limit is exceeded and the LDAP
   * libraries set this code.</p>
   *
   * <p>REFERRAL_LIMIT_EXCEEDED = 97</p>
   */
   public final static int REFERRAL_LIMIT_EXCEEDED = 97;

   /**
    * Indicates that the server response to a request is invalid
    *
    * <p>INVALID_RESPONSE = 100</p>
    */
   public final static int INVALID_RESPONSE = 100;

   /**
    * Indicates that the server response to a request is ambiguous
    *
    * <p>AMBIGUOUS_RESPONSE = 101</p>
    */
   public final static int AMBIGUOUS_RESPONSE = 101;

   /**
    * Indicates that TLS is not supported on the server.
    *
    * <p>TLS_NOT_SUPPORTED = 112</p>
    */
   public final static int TLS_NOT_SUPPORTED = 112;

   /*
    * Note: Error strings have been pulled out into
    * ResultCodeMessages.properties
    */

   /*
    * Constructs a default exception with no specific error information.
    */
   public LDAPException()
   {
      super();
      return;
   }

   /**
    * Constructs an exception with with a detail message obtained from the
    * specified <code>MessageOrKey</code> String and the result code.
    * <br>
    * The String is used either as a message key to obtain a localized
    * messsage from ExceptionMessages, or if there is no key in the
    * resource matching the text, it is used as the detailed message itself.
    *
    *  @param messageOrKey  Key to addition result information, a key into
    *                       ExceptionMessages, or the information
    *                       itself if the key doesn't exist.
    *<br><br>
    *  @param resultCode    The result code returned.
    */
   public LDAPException(String messageOrKey, int resultCode)
   {
      this( messageOrKey, null, resultCode, null, null);
      return;
   }

   /**
    * Constructs an exception with with a detail message obtained from the
    * specified <code>MessageOrKey</code> String and modifying arguments.
    * Additional parameters specify the result code.
    * <br>
    * The String is used either as a message key to obtain a localized
    * messsage from ExceptionMessages, or if there is no key in the
    * resource matching the text, it is used as the detailed message itself.
    * <br>
    * The message in the default locale is built with the supplied arguments,
    * which are saved to be used for building messages for other locales.
    *
    *  @param messageOrKey  Key to addition result information, a key into
    *                       ExceptionMessages, or the information
    *                       itself if the key doesn't exist.
    *<br><br>
    *  @param arguments    The modifying arguments to be included in the
    *                       message string.
    *<br><br>
    *  @param resultCode    The result code returned.
    */
   public LDAPException( String messageOrKey,
                         Object[] arguments,
                         int resultCode)
   {
      this( messageOrKey, arguments, resultCode, null, null);
      return;
   }

   /**
    * Constructs an exception with with a detail message obtained from the
    * specified <code>MessageOrKey</code> String.
    * Additional parameters specify the result code and a rootException which
    * is the underlying cause of an error on the client.
    * <br>
    * The String is used either as a message key to obtain a localized
    * messsage from ExceptionMessages, or if there is no key in the
    * resource matching the text, it is used as the detailed message itself.
    *
    *  @param messageOrKey  Key to addition result information, a key into
    *                       ExceptionMessages, or the information
    *                       itself if the key doesn't exist.
    *<br><br>
    *  @param resultCode    The result code returned.
    *<br><br>
    *  @param rootException  A throwable which is the underlying cause
    *                        of the LDAPException.
    */
   public LDAPException( String messageOrKey,
                         int resultCode,
                         Throwable rootException)
   {
      this( messageOrKey, null, resultCode, null, rootException);
      return;
   }

   /**
    * Constructs an exception with with a detail message obtained from the
    * specified <code>MessageOrKey</code> String and modifying arguments.
    * Additional parameters specify the result code
    * and a rootException which is the underlying cause of an error
    * on the client.
    * <br>
    * The String is used either as a message key to obtain a localized
    * messsage from ExceptionMessages, or if there is no key in the
    * resource matching the text, it is used as the detailed message itself.
    * <br>
    * The message in the default locale is built with the supplied arguments,
    * which are saved to be used for building messages for other locales.
    *
    *  @param messageOrKey  Key to addition result information, a key into
    *                       ExceptionMessages, or the information
    *                       itself if the key doesn't exist.
    *<br><br>
    *  @param arguments    The modifying arguments to be included in the
    *                       message string.
    *<br><br>
    *  @param resultCode    The result code returned.
    *<br><br>
    *  @param rootException  A throwable which is the underlying cause
    *                        of the LDAPException.
    */
   public LDAPException( String messageOrKey,
                         Object[] arguments,
                         int resultCode,
                         Throwable rootException)
   {
      this( messageOrKey, arguments, resultCode, null, rootException);
      return;
   }

   /**
    * Constructs an exception with with a detail message obtained from the
    * specified <code>MessageOrKey</code> String.
    * Additional parameters specify the result code, and a matchedDN returned
    * from the server.
    * <br>
    * The String is used either as a message key to obtain a localized
    * messsage from ExceptionMessages, or if there is no key in the
    * resource matching the text, it is used as the detailed message itself.
    *
    *  @param messageOrKey  Key to addition result information, a key into
    *                       ExceptionMessages, or the information
    *                       itself if the key doesn't exist.
    *<br><br>
    *  @param resultCode    The result code returned.
    *<br><br>
    *  @param matchedDN      The maximal subset of a specified DN which could
    *                        be matched by the server on a search operation.
    */
   public LDAPException( String messageOrKey,
                         int resultCode,
                         String matchedDN)
   {
      this( messageOrKey, null, resultCode, matchedDN, null);
      return;
   }

   /**
    * Constructs an exception with with a detail message obtained from the
    * specified <code>MessageOrKey</code> String and modifying arguments.
    * Additional parameters specify the result code, and a matchedDN returned
    * from the server.
    * <br>
    * The String is used either as a message key to obtain a localized
    * messsage from ExceptionMessages, or if there is no key in the
    * resource matching the text, it is used as the detailed message itself.
    * <br>
    * The message in the default locale is built with the supplied arguments,
    * which are saved to be used for building messages for other locales.
    *
    *  @param messageOrKey  Key to addition result information, a key into
    *                       ExceptionMessages, or the information
    *                       itself if the key doesn't exist.
    *<br><br>
    *  @param arguments    The modifying arguments to be included in the
    *                       message string.
    *<br><br>
    *  @param resultCode    The result code returned.
    *<br><br>
    *  @param matchedDN      The maximal subset of a specified DN which could
    *                        be matched by the server on a search operation.
    */
   public LDAPException( String messageOrKey,
                         Object[] arguments,
                         int resultCode,
                         String matchedDN)
   {
      this( messageOrKey, arguments, resultCode, matchedDN, null);
      return;
   }

   /**
    * Constructs an exception with with a detail message obtained from the
    * specified <code>MessageOrKey</code> String and modifying arguments.
    * Additional parameters specify the result code, a matchedDN returned from
    * the server, and a rootException which is the underlying cause of an error
    * on the client.
    * <br>
    * The String is used either as a message key to obtain a localized
    * messsage from ExceptionMessages, or if there is no key in the
    * resource matching the text, it is used as the detailed message itself.
    * <br>
    * The message in the default locale is built with the supplied arguments,
    * which are saved to be used for building messages for other locales.
    *
    *  @param messageOrKey  Key to addition result information, a key into
    *                       ExceptionMessages, or the information
    *                       itself if the key doesn't exist.
    *<br><br>
    *  @param arguments    The modifying arguments to be included in the
    *                       message string.
    *<br><br>
    *  @param resultCode    The result code returned.
    *<br><br>
    *  @param rootException  A throwable which is the underlying cause
    *                        of the LDAPException.
    *<br><br>
    *  @param matchedDN      The maximal subset of a specified DN which could
    *                        be matched by the server on a search operation.
    */
   /* package */
   LDAPException(String messageOrKey,
                        Object[] arguments,
                        int resultCode,
                        String matchDN,
                        Throwable rootException)
   {
      super(ResourcesHandler.getMessage(messageOrKey, arguments));
      this.messageOrKey = messageOrKey;
      this.arguments = arguments;
      this.resultCode = resultCode;
      this.rootException = rootException;
      this.matchedDN = matchedDN;
      if( Debug.LDAP_DEBUG) {
       Debug.trace( Debug.messages, "new LDAPException(Result=" +
            resultCode + ") " + super.getMessage());
      }
      return;
   }

   /**
    * Returns a string representing the internal error code, in the default
    * locale.
    *
    * @return The message for the result code in the LDAPException object.
    */
   public String errorCodeToString()
   {
       return ResourcesHandler.getResultString( resultCode);
   }

   /**
    * Returns a string representing an arbitrary error code, in the default
    * locale, or null if there is no such code.
    *
    * @param code  The result code for which a message is to be returned.
    *
    * @return The message corresponding to the specified error code, or
    *         null if the error code doesn't exist.
    */
   public static String errorCodeToString( int code )
   {
       return ResourcesHandler.getResultString( code);
   }

   /**
    * Returns a string representing the internal error code, in the
    * specified locale, or null if a string representation is not available
    * for the requested locale.
    *
    * @param locale The locale in which to render the error message.
    *
    * @return A message corresponding to the error code, in the
    * specified locale, or null if the message is not available
    * for the requested locale.

    */
   public String errorCodeToString( Locale locale )
   {
        return ResourcesHandler.getResultString( resultCode, locale);
   }

   /**
    * Returns a string representing an arbitrary error code, in the
    * specified locale, or null if there is no such code or if a string
    * representation is not available for the requested Locale.
    *
    *  @param code     The result code for which a message is to be
    *                  returned.
    *<br><br>
    *  @param locale   The locale in which to render the message.
    *
    *  @return A message corresponding to the error code, in the
    *  specified locale, or null if the message is not available
    *  for the requested locale or the error code doesn't exist.
    */
   public static String errorCodeToString( int code, Locale locale )
   {
        return ResourcesHandler.getResultString( code, locale);
   }

   /**
    * Sets the server error message, if this message is available (that is, if
    * this message was returned by the server).
    */
   /*package*/
   void setLDAPErrorMessage( String serverMessage)
   {
      this.serverMessage = serverMessage;
      return;
   }

   /**
    * Returns the error message from the LDAP server, if this message is
    * available (that is, if this message was set). If the message was not set,
    * this method returns null.
    *
    * @return The error message or null if the message was not set.
    *
    */
    public String getLDAPErrorMessage()
    {
        return serverMessage;
    }

    /**
     * Returns the lower level Exception which caused the failure, if any.
     * For example, an IOException with additional information may be returned
     * on a CONNECT_ERROR failure.
     */
    public Throwable getCause()
    {
        return rootException;
    }


   /**
    * Returns the result code from the exception.
    *
    * <p>The codes are defined as <code>public final static int</code> members
    * of the LDAP Exception class. If the exception is a
    * result of error information returned from a directory operation, the
    * code will be one of those defined for the class. Otherwise, a local error
    * code is returned. </p>
    */
   public int getLDAPResultCode()
   {
      return resultCode;
   }

   /**
    * Returns the part of a submitted distinguished name which could be
    * matched by the server.
    *
    * </p>If the exception was caused by a local error, such as no server
    * available, the return value is null. If the exception resulted from
    * an operation being executed on a server, the value is an empty string
    * except when the result of the operation was one of the following:</p>
    * <ul>
    *  <li>NO_SUCH_OBJECT</li>
    *  <li>ALIAS_PROBLEM</li>
    *  <li>INVALID_DN_SYNTAX</li>
    *  <li>ALIAS_DEREFERENCING_PROBLEM</li>
    * </ul>
    *
    *@return The part of a submitted distinguished name which could be
    * matched by the server or null if the error is a local error.
    */
   public String getMatchedDN()
   {
      return matchedDN;
   }

    /**
     * When debugging an object class, converts the integer value
     * to a string, in the default locale.
     */
    public String toString()
    {
        String msg = ResourcesHandler.getMessage("TOSTRING",
                                                new Object[] { super.toString(),
                                                        new Integer(resultCode),
                                                        errorCodeToString()
                                                             });
        if( msg.equalsIgnoreCase( "TOSTRING")) {
            msg = super.toString() + " : (" + resultCode + ") " +
                    errorCodeToString();
        }
        return msg;
    }
}

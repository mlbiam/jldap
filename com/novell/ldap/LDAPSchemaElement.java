/* **************************************************************************
 * $Novell: /ldap/src/jldap/com/novell/ldap/LDAPSchemaElement.java,v 1.6 2000/09/11 22:47:50 judy Exp $
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

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import com.novell.ldap.client.AttributeQualifier;

/*
 * 4.30 public abstract class LDAPSchemaElement
 */
 
/**
 *  Represents schema elements.
 *  
 */
public abstract class LDAPSchemaElement {

   /**
    * The name for the schema element.
    */
    protected String name;
    
   /**
    * The OID for the schema element.
    */
	protected String oid;

   /**
    * The description for the schema element.
    */
	protected String description;

   /**
    * A string array of alternative names for the schema element.
    */
	protected String[] aliases;

   /**
    * If present, indicates that the element is obsolete.
    */
    protected boolean obsolete;
    
   /**
    * A string array of optional, or vendor-specific, qualifiers for the 
    * schema element.
    *
    * <p> These optional qualifiers begin with "X-"; the NDS-specific qualifiers
    * begin with "X-NDS". </p>
    */
	protected String[] qualifier;
    
   /**
    * A string value for the schema element in a format that can be used to add 
    * the element to the directory.
    */
	protected String value;
    
   /**
   * 
   */
    protected Hashtable hashQualifier = new Hashtable();

   /*
    * 4.22.1 getAliases
    */

   /**
    * Returns an array of alternative names for the element, or null if
    * none is found. 
    *
    * <p>With respect to the protocol-level schema element
    * syntax definition, the array consists of all values but the
    * first of the NAME qualifier. </p>
    *
    *  @return An array of alternative names for the element, or null if none
    *          is found.
    */
   public String[] getAliases() {
    if( aliases != null){
      String[] retVal = new String[aliases.length];
      for( int i = 0; i < aliases.length; i++ )
        retVal[i] = aliases[i];
      return retVal;
    }
    return null;
   }

   /*
    * 4.22.2 getDescription
    */

   /**
    * Returns the description of the element. 
    *
    * <p>With respect to the protocol-level schema element syntax definition,
    * the value is that of the DESC qualifier.</p>
    *
    * @return The description of the element.
    * 
    */
   public String getDescription() {
      return description;
   }

   /*
    * 4.22.3 getName
    */

   /**
    * Returns the name of the element. 
    *
    * <p>With respect to the protocol-level schema element syntax definition, 
    * the value is that of the first NAME qualifier.</p>
    *
    *  @return The name of the element. 
    */
   public String getName() {
      return name;
   }

   /*
    * 4.22.4 getID
    */

   /**
    * Returns the unique object identifier (OID) of the element.
    *
    * @return The OID of the element.
    */
   public String getID() {
      return oid;
   }

   /*
    * 4.22.5 getQualifier
    */

   /**
    * Returns an array of all values of a specified optional or non-
    * standard qualifier of the element. 
    *
    * <p>The getQualifier method may be used to access the values of
    * vendor-specific qualifiers (which begin with "X-").</p>
    *
    *  @param name      The name of the qualifier, case-sensitive.
    *
    *  @return An array of values for the specified non-standard qualifier.
    */
   public String[] getQualifier(String name) {
      AttributeQualifier attr = (AttributeQualifier) hashQualifier.get(name);
      if(attr != null){
        return attr.getValues();
      }
      return null;
   }

   /*
    * 4.22.6 getQualifierNames
    */

   /**
    * Returns an enumeration of all qualifiers of the element which are 
    * not standard.
    *
    *@return An enumeration of all qualifiers of the element.
    */
   public Enumeration getQualifierNames() {
      int size;
      Vector qualNames = new Vector();
      if((size = hashQualifier.size()) > 0){
        Enumeration en = hashQualifier.elements();
        for( int i = 0; en.hasMoreElements(); i++){
          qualNames.addElement( ((AttributeQualifier)en.nextElement()).getName());
        }
      }
      return qualNames.elements();
   }

   /*
    * 4.22.7 isObsolete
    */

   /**
    * Returns true if the element has the OBSOLETE qualifier
    * in its LDAP definition.
    *
    * @return True if the LDAP definition contains the OBSOLETE qualifier; 
    *         false if OBSOLETE qualifier is not present.
    */
   public boolean isObsolete() {
      return obsolete;
   }

   /*
    * 4.22.8 getValue
    */

   /**
    * Returns a string in a format suitable for directly adding to a
    * directory, as a value of the particular schema element attribute.
    *
    * @return A string that can be used to add the element to the directory.
    */
   public String getValue() {
      return value;
   }

   /*
    * 4.22.9 setQualifier
    */

   /**
    * Sets the values of a specified optional or non-standard qualifier of
    * the element. 
    *
    * <p>The setQualifier method is used to set the values of vendor-
    * specific qualifiers (which begin with "X-").
    *
    *  @param name           The name of the qualifier, case-sensitive.
    *<br><br>
    *  @param values         The values to set for the qualifier.
    */
   public void setQualifier(String name, String[] values) {
    AttributeQualifier attrQualifier = new AttributeQualifier( name );
    if(values != null){
    	for(int i = 0; i < values.length; i++){
		attrQualifier.addValue(values[i]);
    	}
    }
    hashQualifier.put(name, attrQualifier);
   }

   /*
    * 4.22.10 add
    */

   /**
    * Adds the definition to a directory. An exception is thrown if the
    * definition cannot be added.
    *
    *  @param ld       An open connection to a directory server.
    *                  Typically the connection must have been
    *                  authenticated to add a schema definition.
    *
    *  @exception LDAPException A general exception which includes an error 
    *                           message and an LDAP error code.
    */
   public void add(LDAPConnection ld) throws LDAPException {
   }

   /**
    * Adds the definition to a directory. An exception is thrown if the
    * definition cannot be added.
    *
    *  @param ld       An open connection to a directory server.
    *                  Typically the connection must have been
    *                  authenticated to add a schema definition.
    *<br><br>
    *  @param dn       The entry at which to determine the SubschemaSubentry
    *                  to which the schema element is to be added.
    *
    *  @exception LDAPException A general exception which includes an error 
    *                           message and an LDAP error code.
    */
   public void add(LDAPConnection ld, String dn) throws LDAPException {
   }

   /*
    * 4.22.11 remove
    */

   /**
    * Removes the definition from a directory. An exception is thrown if
    * the definition cannot be removed.
    *
    *  @param ld       An open connection to a directory server.
    *                  Typically the connection must have been
    *                  authenticated to remove a schema definition.
    *
    *  @exception LDAPException A general exception which includes an error \
    *                           message and an LDAP error code.
    */
   public void remove(LDAPConnection ld) throws LDAPException {
   }

   /**
    * Removes the definition from a directory. An exception is thrown if
    * the definition cannot be removed.
    *
    *  @param ld       An open connection to a directory server.
    *                  Typically the connection must have been
    *                  authenticated to remove a schema definition.
    *<br><br>
    *  @param dn       The entry at which to determine the SubschemaSubentry
    *                  to remove the schema element from.
    *
    *  @exception LDAPException A general exception which includes an error 
    *                           message and an LDAP error code.
    */
   public void remove(LDAPConnection ld, String dn) throws LDAPException {
   }

   /*
    * 4.22.12 modify
    */

   /**
    * Replaces a single value of the schema element definition in the
    * schema. An exception is thrown if the definition cannot be modified.
    *
    *  @param ld       An open connection to a directory server.
    *                  Typically the connection must have been
    *                  authenticated to modify a schema definition.
    *<br><br>
    *  @param newValue  The new schema element value.
    *
    *  @exception LDAPException A general exception which includes an error 
    *                           message and an LDAP error code.
    */
   public void modify(LDAPConnection ld,
                      LDAPSchemaElement newValue) throws LDAPException {
   }

   /**
    * Replaces a single value of the schema element definition in the
    * schema. An exception is thrown if the definition cannot be modified.
    *
    *  @param ld       An open connection to a directory server.
    *                  Typically the connection must have been
    *                  authenticated to modify a schema definition.
    *<br><br>
    *  @param newValue  The new schema element value.
    *<br><br>
    *  @param dn       The entry at which to determine the SubschemaSubentry
    *                  to store the schema change in.
    *
    *  @exception LDAPException A general exception which includes an error
    *                           message and an LDAP error code.
    */
   public void modify(LDAPConnection ld,
                      LDAPSchemaElement newValue,
                      String dn) throws LDAPException {
   }

}
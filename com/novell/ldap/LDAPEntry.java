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
 * OF THIS WORK OTHER THAN AS AUTHORIZED IN VERSION 2.0.1 OF THE OPENLDAP
 * PUBLIC LICENSE, OR OTHER PRIOR WRITTEN CONSENT FROM NOVELL, COULD SUBJECT
 * THE PERPETRATOR TO CRIMINAL AND CIVIL LIABILITY.
 ******************************************************************************/

package com.novell.ldap;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.novell.ldap.util.LDAPXMLHandler;
import com.novell.ldap.util.SAXEventMultiplexer;

import com.novell.ldap.util.Base64;
import java.util.Iterator;
/**
 * Represents a single entry in a directory, consisting of
 * a distinguished name (DN) and zero or more attributes.
 *
 * <p>An instance of
 * LDAPEntry is created in order to add an entry to a directory, and
 * instances of LDAPEntry are returned on a search by enumerating an
 * LDAPSearchResults.
 *
 * @see LDAPAttribute
 * @see LDAPAttributeSet
 */
public class LDAPEntry implements java.lang.Comparable,java.io.Serializable
{
    protected String dn;
    protected LDAPAttributeSet attrs;

    /**
     * Constructs an empty entry.
     */
    public LDAPEntry()
    {
        this(null,null);
    }

    /**
     * Constructs a new entry with the specified distinguished name and with
     * an empty attribute set.
     *
     *  @param dn  The distinguished name of the entry. The
     *                  value is not validated. An invalid distinguished
     *                  name will cause operations using this entry to fail.
     *
     */
    public LDAPEntry(String dn)
    {
        this( dn, null);
    }

    /**
     * Constructs a new entry with the specified distinguished name and set
     * of attributes.
     *
     *  @param dn       The distinguished name of the new entry. The
     *                  value is not validated. An invalid distinguished
     *                  name will cause operations using this entry to fail.
     *<br><br>
     *  @param attrs    The initial set of attributes assigned to the
     *                  entry.
     */
    public LDAPEntry(String dn, LDAPAttributeSet attrs)
    {
        if( dn == null) {
            dn = "";
        }
        if( attrs == null) {
            attrs = new LDAPAttributeSet();
        }
        this.dn = dn;
        this.attrs = attrs;
        return;
    }

   /**
    * Returns the attributes matching the specified attrName.
    *
    * @param attrName The name of the attribute or attributes to return.
    * <br><br>
    * @return An array of LDAPAttribute objects.
    */
   public LDAPAttribute getAttribute(String attrName)
   {
		return attrs.getAttribute(attrName);
   }

    /**
     * Returns the attribute set of the entry.
     *
     * <p>All base and subtype variants of all attributes are
     * returned. The LDAPAttributeSet returned may be
     * empty if there are no attributes in the entry. </p>
     *
     * @return The attribute set of the entry.
     */
    public LDAPAttributeSet getAttributeSet()
    {
        return attrs;
    }


    /**
     * Returns an attribute set from the entry, consisting of only those
     * attributes matching the specified subtypes.
     *
     * <p>The getAttributeSet method can be used to extract only
     * a particular language variant subtype of each attribute,
     * if it exists. The "subtype" may be, for example, "lang-ja", "binary",
     * or "lang-ja;phonetic". If more than one subtype is specified, separated
     * with a semicolon, only those attributes with all of the named
     * subtypes will be returned. The LDAPAttributeSet returned may be
     * empty if there are no matching attributes in the entry. </p>
     *
     *  @param subtype  One or more subtype specification(s), separated
     *                  with semicolons. The "lang-ja" and
     *                  "lang-en;phonetic" are valid subtype
     *                  specifications.
     *
     * @return An attribute set from the entry with the attributes that
     *         match the specified subtypes or an empty set if no attributes
     *         match.
     */
    public LDAPAttributeSet getAttributeSet(String subtype)
    {
		return attrs.getSubset(subtype);
    }

    /**
     * Returns the distinguished name of the entry.
     *
     * @return The distinguished name of the entry.
     */
    public String getDN()
    {
        return dn;
    }

    /**
     * Compares this object with the specified object for order.
     *
     * <p>Ordering is determined by comparing normalized DN values
     * (see {@link LDAPEntry#getDN() } and
     * {@link LDAPDN#normalize(java.lang.String)}) using the
     * compareTo method of the String class.  </p>
     *
     * @param entry     Entry to compare to
     *
     * @return          A negative integer, zero, or a positive integer as this
     * object is less than, equal to, or greater than the specified object.
     */
    public int compareTo(Object entry){
        return LDAPDN.normalize( this.dn ).compareTo(
                    LDAPDN.normalize( ((LDAPEntry)entry).dn ) );
    }

    /**
     * Returns a string representation of this LDAPEntry
     *
     * @return a string representation of this LDAPEntry
     */ 
    public String toString()
    {
       StringBuffer result = new StringBuffer("LDAPEntry: ");
        if( dn != null) {
            result.append(dn + "; ");
        }
        if( attrs != null) {
            result.append(attrs.toString());
        }
        return result.toString();
    }

    void newLine(int indentTabs,java.io.Writer out) throws java.io.IOException
    {
        String tabString = "    ";    
        
        out.write("\n");
        for (int i=0; i< indentTabs; i++){
            out.write(tabString);
        }
        
    }
    
    private void writeAttribute(LDAPAttribute attr,java.io.Writer out) throws java.io.IOException
    {
        newLine(2,out);
        out.write("<attr name=\"");
        out.write(attr.getName());
        out.write("\">");
        String values[] = attr.getStringValueArray();
        byte bytevalues[][] = attr.getByteValueArray();
        for(int i=0; i<values.length; i++){
            newLine(3,out);
            if (Base64.isValidUTF8(bytevalues[i], false)){
                out.write("<value>");
                out.write(values[i]);
                out.write("</value>");
            } else {
                out.write("<value xsi:type=\"xsd:base64Binary\">");
                out.write(Base64.encode(bytevalues[i]));
                out.write("</value>");
            }

        }
        newLine(2,out);
        out.write("</attr>");
        
    }

    /**
     * This method does DSML serialization of the instance.
     *
     * @param oout Outputstream where the serialzed data has to be written
     *
     * @throws IOException if write fails on OutputStream 
     */    
    public void writeDSML(java.io.OutputStream oout) throws java.io.IOException
    {
        java.io.Writer out=new java.io.OutputStreamWriter(oout,"UTF-8");
        out.write("<LDAPEntry dn=\"");
        out.write(getDN());
        out.write("\">");
        Iterator i = getAttributeSet().iterator();
        while (i.hasNext()){
            writeAttribute( (LDAPAttribute) i.next(),out);
        }
        newLine(0,out);
        out.write("</LDAPEntry>");    
        out.close();
    }    
	/**
	* This method is used to deserialize the DSML encoded representation of
	* this class.
	* @param input InputStream for the DSML formatted data. 
	* @return Deserialized form of this class.
	* @throws IOException when serialization fails.
	*/     
	public static Object readDSML(InputStream input)throws IOException    
		 {
		 SAXEventMultiplexer xmlreader = new SAXEventMultiplexer();
		 xmlreader.setLDAPXMLHandler(getXMLHandler("LDAPEntry",null));		
		 return (LDAPEntry) xmlreader.parseXML(input);
		 }
	/**
	* This method return the LDAPHandler which handles the XML (DSML) tags
	* for this class
	* @param tagname Name of the Root tag used to represent this class.
	* @param parenthandler Parent LDAPXMLHandler for this tag.
	* @return LDAPXMLHandler to handle this element.
	*/    		 
	static LDAPXMLHandler getXMLHandler(String tagname,LDAPXMLHandler parenthandler) {
		return new LDAPXMLHandler(tagname, parenthandler) {
			String dn;
			List valuelist = new ArrayList();
			protected void initHandler() {
				//set LDAPAttribute handler.
				setchildelement(LDAPAttribute.getXMLHandler("attr",this));
			}
			protected void endElement() {
				LDAPAttributeSet attrset = new LDAPAttributeSet();
				attrset.addAll(valuelist);
				LDAPEntry entry = new LDAPEntry(dn,attrset);				
				setObject(entry);
			}
			protected void addValue(String tag, Object value) {
				if (tag.equals("attr")) {
					valuelist.add(value);
				}
			}
			protected void handleAttributes(Attributes attributes)throws SAXException {
					dn = attributes.getValue("dn");
					if (dn== null)
						throw new SAXException("invalid entry Tag, dn is mandatory element: ");
						}
    		
			};
		};
    /**
    *  Writes the object state to a stream in standard Default Binary format
    *  This function wraps ObjectOutputStream' s defaultWriteObject() to write
    *  the non-static and non-transient fields of the current class to the stream
    *   
    *  @param objectOStrm  The OutputSteam where the Object need to be written
    */
    private void writeObject(java.io.ObjectOutputStream objectOStrm)
	    throws java.io.IOException {
		objectOStrm.defaultWriteObject();
    }
    
    /**
    *  Reads the serialized object from the underlying input stream.
    *  This function wraps ObjectInputStream's  defaultReadObject() function
    *
    *  @param objectIStrm  InputStream used to recover those objects previously serialized. 
    */
    private void readObject(java.io.ObjectInputStream objectIStrm)
         throws java.io.IOException, ClassNotFoundException
    {
	  objectIStrm.defaultReadObject();
    }    
}

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

import com.novell.ldap.util.LDAPXMLHandler;
import com.novell.ldap.util.SAXEventMultiplexer;
import com.novell.ldap.util.ValueXMLhandler;

import com.novell.ldap.util.Base64;
/**
 * Encapsulates an ID which uniquely identifies a particular extended
 * operation, known to a particular server, and the data associated
 * with that extended operation.
 *
 * @see LDAPConnection#extendedOperation
 */
public class LDAPExtendedOperation implements Cloneable,java.io.Serializable
{

    private String oid;
    private byte[] vals;

    /**
     * Constructs a new object with the specified object ID and data.
     *
     *  @param oid     The unique identifier of the operation.
     *
     *  @param vals    The operation-specific data of the operation.
     */
    public LDAPExtendedOperation(String oid, byte[] vals) {
       this.oid = oid;
       this.vals = vals;
    }

    /**
     * Returns a clone of this object.
     *
     * @return clone of this object.
     */
    public Object clone()
    {
        try {
            Object newObj = super.clone();
            System.arraycopy( this.vals, 0,
                              ((LDAPExtendedOperation)newObj).vals, 0,
                              this.vals.length);
            return newObj;
        } catch( CloneNotSupportedException ce) {
            throw new RuntimeException("Internal error, cannot create clone");
        }
    }

    /**
     * Returns the unique identifier of the operation.
     *
     * @return The OID (object ID) of the operation.
     */
    public String getID() {
       return oid;
    }

    /**
     * Returns a reference to the operation-specific data.
     *
     * @return The operation-specific data.
     */
    public byte[] getValue() {
       return vals;
    }

    /**
     *  Sets the value for the operation-specific data.
     *
     *  @param newVals  The byte array of operation-specific data.
     */
    protected void setValue(byte[] newVals) {
         this.vals = newVals;
         return;
    }

    /**
     *  Resets the OID for the operation to a new value
     *
     *  @param newoid  The new OID for the operation
     */
    protected void setID(String newoid) {
         this.oid = newoid;
         return;
    }

    void newLine(int indentTabs,java.io.Writer out) throws java.io.IOException
    {
        String tabString = "    ";    
        
        out.write("\n");
        for (int i=0; i< indentTabs; i++){
            out.write(tabString);
        }
        
    }

    /**
     * This method does DSML serialization of the instance.
     *
     * @param oout Outputstream where the serialzed data has to be written
     *
     * @throws IOException if write fails on OutputStream 
     */    
    public void writeDSML(java.io.OutputStream  oout) throws java.io.IOException
    {
    	java.io.Writer out=new java.io.OutputStreamWriter(oout,"UTF-8");
        out.write("<extendedRequest>");
        newLine(1,out);
        out.write("<requestName>");
        out.write(getID());
        out.write("</requestName>");
        byte[] vals=getValue();
        if( vals != null)
        {
            newLine(1,out);
            out.write("<requestValue xsi:type=\"xsd:base64Binary\">");
            out.write(Base64.encode(vals));
            out.write("</requestValue>");
        }

        newLine(0,out);
        out.write("</extendedRequest>");
        out.close();
    }
  /**
  * This method is used to deserialize the DSML encoded representation of
  * this class.
  * @param input InputStream for the DSML formatted data. 
  * @return Deserialized form of this class.
  * @throws IOException when serialization fails.
  */
  public static Object readDSML(InputStream input) throws IOException {
    SAXEventMultiplexer xmlreader = new SAXEventMultiplexer();
    xmlreader.setLDAPXMLHandler(getXMLHandler("LDAPExtendedOperation", null));
    return (LDAPExtendedOperation) xmlreader.parseXML(input);
  }

  /**
  * This method return the LDAPHandler which handles the XML (DSML) tags
  * for this class
  * @param tagname Name of the Root tag used to represent this class.
  * @param parenthandler Parent LDAPXMLHandler for this tag.
  * @return LDAPXMLHandler to handle this element.
  */
  static LDAPXMLHandler getXMLHandler(
    String tagname,
    LDAPXMLHandler parenthandler) {
    return new LDAPXMLHandler(tagname, parenthandler) {
      String requestName;
      byte[] requestValue;
      protected void initHandler() {
        //set value handler.
        setchildelement(new ValueXMLhandler("requestName", this));
        setchildelement(new ValueXMLhandler("requestValue", this));
      }

      protected void endElement() {
        LDAPExtendedOperation op =
          new LDAPExtendedOperation(requestName, requestValue);
        setObject(op);
      }
      protected void addValue(String tag, Object value) {
        if (tag.equals("requestName")) {
          requestName = new String((byte[]) value);

        } else if (tag.equals("requestValue")) {
          requestValue = (byte[]) value;
        }
      }

    };

  }
	/**
	* Returns a valid string representation of this LDAP URL.
	*
	* @return The string representation of the LDAP URL.
	*/
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("LDAPExtendedOperation:");
		buf.append("((requestName="+getID());
		buf.append("),(requestValue="+getValue()+"))");
		return buf.toString();
	}
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

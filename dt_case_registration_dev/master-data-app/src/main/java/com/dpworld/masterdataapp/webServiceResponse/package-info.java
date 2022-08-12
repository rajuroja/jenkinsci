/**
 * 
 */
/**
 * @author 271983
 *
 */
//@XmlSchema( 
//    namespace = "http://www.dpworld.com", 
//    elementFormDefault = XmlNsForm.QUALIFIED) 

@XmlSchema(
	    xmlns = { 
	        @XmlNs(prefix = "", namespaceURI = "http://www.dpworld.com"),
	        @XmlNs(prefix = "", namespaceURI = "http://www.dpworld.org"),
	        @XmlNs(prefix = "", namespaceURI = "http://www.example.org")
	    }
	)
package com.dpworld.masterdataapp.webServiceResponse;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlSchema;
//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.06.12 at 11:20:41 AM CEST 
//


package com.eon.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DatosCIE complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DatosCIE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CieElectronico" type="{http://localhost/elegibilidad}X1"/>
 *         &lt;element name="CIEPapel" type="{http://localhost/elegibilidad}CIEPapel" minOccurs="0"/>
 *         &lt;element name="CIEElectronico" type="{http://localhost/elegibilidad}CIEElectronico" minOccurs="0"/>
 *         &lt;element name="ValidezCIE" type="{http://localhost/elegibilidad}AmbitoValidezCIE" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DatosCIE", propOrder = {
    "rCieElectronico",
    "ciePapel",
    "cieElectronico",
    "validezCIE"
})
public class DatosCIE {

    @XmlElement(name = "CieElectronico", required = true)
    protected String rCieElectronico;
    @XmlElement(name = "CIEPapel")
    protected CIEPapel ciePapel;
    @XmlElement(name = "CIEElectronico")
    protected CIEElectronico cieElectronico;
    @XmlElement(name = "ValidezCIE")
    protected AmbitoValidezCIE validezCIE;

    /**
     * Gets the value of the rCieElectronico property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRCieElectronico() {
        return rCieElectronico;
    }

    /**
     * Sets the value of the rCieElectronico property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRCieElectronico(String value) {
        this.rCieElectronico = value;
    }

    /**
     * Gets the value of the ciePapel property.
     * 
     * @return
     *     possible object is
     *     {@link CIEPapel }
     *     
     */
    public CIEPapel getCIEPapel() {
        return ciePapel;
    }

    /**
     * Sets the value of the ciePapel property.
     * 
     * @param value
     *     allowed object is
     *     {@link CIEPapel }
     *     
     */
    public void setCIEPapel(CIEPapel value) {
        this.ciePapel = value;
    }

    /**
     * Gets the value of the cieElectronico property.
     * 
     * @return
     *     possible object is
     *     {@link CIEElectronico }
     *     
     */
    public CIEElectronico getCIEElectronico() {
        return cieElectronico;
    }

    /**
     * Sets the value of the cieElectronico property.
     * 
     * @param value
     *     allowed object is
     *     {@link CIEElectronico }
     *     
     */
    public void setCIEElectronico(CIEElectronico value) {
        this.cieElectronico = value;
    }

    /**
     * Gets the value of the validezCIE property.
     * 
     * @return
     *     possible object is
     *     {@link AmbitoValidezCIE }
     *     
     */
    public AmbitoValidezCIE getValidezCIE() {
        return validezCIE;
    }

    /**
     * Sets the value of the validezCIE property.
     * 
     * @param value
     *     allowed object is
     *     {@link AmbitoValidezCIE }
     *     
     */
    public void setValidezCIE(AmbitoValidezCIE value) {
        this.validezCIE = value;
    }

}

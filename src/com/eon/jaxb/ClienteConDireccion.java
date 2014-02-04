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
 * <p>Java class for ClienteConDireccion complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ClienteConDireccion">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="IdCliente" type="{http://localhost/elegibilidad}IdCliente"/>
 *         &lt;element name="Nombre" type="{http://localhost/elegibilidad}NombreCliente"/>
 *         &lt;element name="Fax" type="{http://localhost/elegibilidad}TelefonoInternacional" minOccurs="0"/>
 *         &lt;element name="Telefono" type="{http://localhost/elegibilidad}TelefonoInternacional" minOccurs="0"/>
 *         &lt;element name="IndicadorTipoDireccion">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="F"/>
 *               &lt;enumeration value="S"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Direccion" type="{http://localhost/elegibilidad}Direccion" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ClienteConDireccion", propOrder = {
    "idCliente",
    "nombre",
    "fax",
    "telefono",
    "indicadorTipoDireccion",
    "direccion"
})
public class ClienteConDireccion {

    @XmlElement(name = "IdCliente", required = true)
    protected IdCliente idCliente;
    @XmlElement(name = "Nombre", required = true)
    protected NombreCliente nombre;
    @XmlElement(name = "Fax")
    protected TelefonoInternacional fax;
    @XmlElement(name = "Telefono")
    protected TelefonoInternacional telefono;
    @XmlElement(name = "IndicadorTipoDireccion", required = true)
    protected String indicadorTipoDireccion;
    @XmlElement(name = "Direccion")
    protected Direccion direccion;

    /**
     * Gets the value of the idCliente property.
     * 
     * @return
     *     possible object is
     *     {@link IdCliente }
     *     
     */
    public IdCliente getIdCliente() {
        return idCliente;
    }

    /**
     * Sets the value of the idCliente property.
     * 
     * @param value
     *     allowed object is
     *     {@link IdCliente }
     *     
     */
    public void setIdCliente(IdCliente value) {
        this.idCliente = value;
    }

    /**
     * Gets the value of the nombre property.
     * 
     * @return
     *     possible object is
     *     {@link NombreCliente }
     *     
     */
    public NombreCliente getNombre() {
        return nombre;
    }

    /**
     * Sets the value of the nombre property.
     * 
     * @param value
     *     allowed object is
     *     {@link NombreCliente }
     *     
     */
    public void setNombre(NombreCliente value) {
        this.nombre = value;
    }

    /**
     * Gets the value of the fax property.
     * 
     * @return
     *     possible object is
     *     {@link TelefonoInternacional }
     *     
     */
    public TelefonoInternacional getFax() {
        return fax;
    }

    /**
     * Sets the value of the fax property.
     * 
     * @param value
     *     allowed object is
     *     {@link TelefonoInternacional }
     *     
     */
    public void setFax(TelefonoInternacional value) {
        this.fax = value;
    }

    /**
     * Gets the value of the telefono property.
     * 
     * @return
     *     possible object is
     *     {@link TelefonoInternacional }
     *     
     */
    public TelefonoInternacional getTelefono() {
        return telefono;
    }

    /**
     * Sets the value of the telefono property.
     * 
     * @param value
     *     allowed object is
     *     {@link TelefonoInternacional }
     *     
     */
    public void setTelefono(TelefonoInternacional value) {
        this.telefono = value;
    }

    /**
     * Gets the value of the indicadorTipoDireccion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIndicadorTipoDireccion() {
        return indicadorTipoDireccion;
    }

    /**
     * Sets the value of the indicadorTipoDireccion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIndicadorTipoDireccion(String value) {
        this.indicadorTipoDireccion = value;
    }

    /**
     * Gets the value of the direccion property.
     * 
     * @return
     *     possible object is
     *     {@link Direccion }
     *     
     */
    public Direccion getDireccion() {
        return direccion;
    }

    /**
     * Sets the value of the direccion property.
     * 
     * @param value
     *     allowed object is
     *     {@link Direccion }
     *     
     */
    public void setDireccion(Direccion value) {
        this.direccion = value;
    }

}

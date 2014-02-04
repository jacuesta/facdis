//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.06.12 at 11:20:41 AM CEST 
//


package com.eon.jaxb;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for ContratoPasoMRAMLTarifa2ConCambios complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ContratoPasoMRAMLTarifa2ConCambios">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="IdContrato" type="{http://localhost/elegibilidad}IdContrato"/>
 *         &lt;choice>
 *           &lt;element name="FechaFinalizacion" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *           &lt;element name="Duracion" type="{http://localhost/elegibilidad}Decimal2"/>
 *         &lt;/choice>
 *         &lt;element name="TipoContratoATR" type="{http://localhost/elegibilidad}TipoContrato"/>
 *         &lt;element name="CondicionesContractuales2n" type="{http://localhost/elegibilidad}CondicionesContractuales2n"/>
 *         &lt;element name="Contacto" type="{http://localhost/elegibilidad}Contacto" minOccurs="0"/>
 *         &lt;element name="DireccionCorrespondencia" type="{http://localhost/elegibilidad}DireccionCorrespondencia"/>
 *         &lt;element name="CuentaBancaria" type="{http://localhost/elegibilidad}CuentaBancaria" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ContratoPasoMRAMLTarifa2ConCambios", propOrder = {
    "idContrato",
    "fechaFinalizacion",
    "duracion",
    "tipoContratoATR",
    "condicionesContractuales2N",
    "contacto",
    "direccionCorrespondencia",
    "cuentaBancaria"
})
public class ContratoPasoMRAMLTarifa2ConCambios {

    @XmlElement(name = "IdContrato", required = true)
    protected IdContrato idContrato;
    @XmlElement(name = "FechaFinalizacion")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar fechaFinalizacion;
    @XmlElement(name = "Duracion")
    protected BigInteger duracion;
    @XmlElement(name = "TipoContratoATR", required = true)
    protected String tipoContratoATR;
    @XmlElement(name = "CondicionesContractuales2n", required = true)
    protected CondicionesContractuales2N condicionesContractuales2N;
    @XmlElement(name = "Contacto")
    protected Contacto contacto;
    @XmlElement(name = "DireccionCorrespondencia", required = true)
    protected DireccionCorrespondencia direccionCorrespondencia;
    @XmlElement(name = "CuentaBancaria")
    protected CuentaBancaria cuentaBancaria;

    /**
     * Gets the value of the idContrato property.
     * 
     * @return
     *     possible object is
     *     {@link IdContrato }
     *     
     */
    public IdContrato getIdContrato() {
        return idContrato;
    }

    /**
     * Sets the value of the idContrato property.
     * 
     * @param value
     *     allowed object is
     *     {@link IdContrato }
     *     
     */
    public void setIdContrato(IdContrato value) {
        this.idContrato = value;
    }

    /**
     * Gets the value of the fechaFinalizacion property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    /**
     * Sets the value of the fechaFinalizacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaFinalizacion(XMLGregorianCalendar value) {
        this.fechaFinalizacion = value;
    }

    /**
     * Gets the value of the duracion property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getDuracion() {
        return duracion;
    }

    /**
     * Sets the value of the duracion property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setDuracion(BigInteger value) {
        this.duracion = value;
    }

    /**
     * Gets the value of the tipoContratoATR property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoContratoATR() {
        return tipoContratoATR;
    }

    /**
     * Sets the value of the tipoContratoATR property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoContratoATR(String value) {
        this.tipoContratoATR = value;
    }

    /**
     * Gets the value of the condicionesContractuales2N property.
     * 
     * @return
     *     possible object is
     *     {@link CondicionesContractuales2N }
     *     
     */
    public CondicionesContractuales2N getCondicionesContractuales2N() {
        return condicionesContractuales2N;
    }

    /**
     * Sets the value of the condicionesContractuales2N property.
     * 
     * @param value
     *     allowed object is
     *     {@link CondicionesContractuales2N }
     *     
     */
    public void setCondicionesContractuales2N(CondicionesContractuales2N value) {
        this.condicionesContractuales2N = value;
    }

    /**
     * Gets the value of the contacto property.
     * 
     * @return
     *     possible object is
     *     {@link Contacto }
     *     
     */
    public Contacto getContacto() {
        return contacto;
    }

    /**
     * Sets the value of the contacto property.
     * 
     * @param value
     *     allowed object is
     *     {@link Contacto }
     *     
     */
    public void setContacto(Contacto value) {
        this.contacto = value;
    }

    /**
     * Gets the value of the direccionCorrespondencia property.
     * 
     * @return
     *     possible object is
     *     {@link DireccionCorrespondencia }
     *     
     */
    public DireccionCorrespondencia getDireccionCorrespondencia() {
        return direccionCorrespondencia;
    }

    /**
     * Sets the value of the direccionCorrespondencia property.
     * 
     * @param value
     *     allowed object is
     *     {@link DireccionCorrespondencia }
     *     
     */
    public void setDireccionCorrespondencia(DireccionCorrespondencia value) {
        this.direccionCorrespondencia = value;
    }

    /**
     * Gets the value of the cuentaBancaria property.
     * 
     * @return
     *     possible object is
     *     {@link CuentaBancaria }
     *     
     */
    public CuentaBancaria getCuentaBancaria() {
        return cuentaBancaria;
    }

    /**
     * Sets the value of the cuentaBancaria property.
     * 
     * @param value
     *     allowed object is
     *     {@link CuentaBancaria }
     *     
     */
    public void setCuentaBancaria(CuentaBancaria value) {
        this.cuentaBancaria = value;
    }

}

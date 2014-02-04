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
 * <p>Java class for MedidaC2 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MedidaC2">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ControlPotenciaPropiedad" type="{http://localhost/elegibilidad}IndicativoNCD"/>
 *         &lt;element name="ControlPotenciaInstalacion" type="{http://localhost/elegibilidad}IndicativoYCD"/>
 *         &lt;element name="EquipoAportadoCliente" type="{http://localhost/elegibilidad}IndicativoNCD"/>
 *         &lt;element name="EquipoInstaladoCliente" type="{http://localhost/elegibilidad}IndicativoYCD"/>
 *         &lt;element name="TipoEquipoMedida" type="{http://localhost/elegibilidad}TiposEquipoMedida" minOccurs="0"/>
 *         &lt;element name="ModelosAparato" type="{http://localhost/elegibilidad}ModelosAparato"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MedidaC2", propOrder = {
    "controlPotenciaPropiedad",
    "controlPotenciaInstalacion",
    "equipoAportadoCliente",
    "equipoInstaladoCliente",
    "tipoEquipoMedida",
    "modelosAparato"
})
public class MedidaC2 {

    @XmlElement(name = "ControlPotenciaPropiedad", required = true)
    protected String controlPotenciaPropiedad;
    @XmlElement(name = "ControlPotenciaInstalacion", required = true)
    protected String controlPotenciaInstalacion;
    @XmlElement(name = "EquipoAportadoCliente", required = true)
    protected String equipoAportadoCliente;
    @XmlElement(name = "EquipoInstaladoCliente", required = true)
    protected String equipoInstaladoCliente;
    @XmlElement(name = "TipoEquipoMedida")
    protected TiposEquipoMedida tipoEquipoMedida;
    @XmlElement(name = "ModelosAparato", required = true)
    protected ModelosAparato modelosAparato;

    /**
     * Gets the value of the controlPotenciaPropiedad property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getControlPotenciaPropiedad() {
        return controlPotenciaPropiedad;
    }

    /**
     * Sets the value of the controlPotenciaPropiedad property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setControlPotenciaPropiedad(String value) {
        this.controlPotenciaPropiedad = value;
    }

    /**
     * Gets the value of the controlPotenciaInstalacion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getControlPotenciaInstalacion() {
        return controlPotenciaInstalacion;
    }

    /**
     * Sets the value of the controlPotenciaInstalacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setControlPotenciaInstalacion(String value) {
        this.controlPotenciaInstalacion = value;
    }

    /**
     * Gets the value of the equipoAportadoCliente property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEquipoAportadoCliente() {
        return equipoAportadoCliente;
    }

    /**
     * Sets the value of the equipoAportadoCliente property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEquipoAportadoCliente(String value) {
        this.equipoAportadoCliente = value;
    }

    /**
     * Gets the value of the equipoInstaladoCliente property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEquipoInstaladoCliente() {
        return equipoInstaladoCliente;
    }

    /**
     * Sets the value of the equipoInstaladoCliente property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEquipoInstaladoCliente(String value) {
        this.equipoInstaladoCliente = value;
    }

    /**
     * Gets the value of the tipoEquipoMedida property.
     * 
     * @return
     *     possible object is
     *     {@link TiposEquipoMedida }
     *     
     */
    public TiposEquipoMedida getTipoEquipoMedida() {
        return tipoEquipoMedida;
    }

    /**
     * Sets the value of the tipoEquipoMedida property.
     * 
     * @param value
     *     allowed object is
     *     {@link TiposEquipoMedida }
     *     
     */
    public void setTipoEquipoMedida(TiposEquipoMedida value) {
        this.tipoEquipoMedida = value;
    }

    /**
     * Gets the value of the modelosAparato property.
     * 
     * @return
     *     possible object is
     *     {@link ModelosAparato }
     *     
     */
    public ModelosAparato getModelosAparato() {
        return modelosAparato;
    }

    /**
     * Sets the value of the modelosAparato property.
     * 
     * @param value
     *     allowed object is
     *     {@link ModelosAparato }
     *     
     */
    public void setModelosAparato(ModelosAparato value) {
        this.modelosAparato = value;
    }

}

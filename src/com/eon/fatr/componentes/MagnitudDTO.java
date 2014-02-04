package com.eon.fatr.componentes;

public class MagnitudDTO {

	private String idTipoMagnitud;

	/**
	 * Constructor del objeto.
	 * A cada propiedad del objeto le damos un valor por defecto.
	 */
	public MagnitudDTO() {
		this.idTipoMagnitud		= "";
	}

	public MagnitudDTO(String idTipoMagnitud) {
		super();
		this.idTipoMagnitud = idTipoMagnitud;
	}

	public String getIdTipoMagnitud() {
		return idTipoMagnitud;
	}

	public void setIdTipoMagnitud(String idTipoMagnitud) {
		this.idTipoMagnitud = idTipoMagnitud;
	}
}
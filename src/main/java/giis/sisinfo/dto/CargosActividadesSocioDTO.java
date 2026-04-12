package giis.sisinfo.dto;

public class CargosActividadesSocioDTO extends CargosSocioDTO{
	int idActividad;
	String NombreActividad;
	String ConceptoPago;
	String Fecha;
	String Estado;
	double importe;
	
	public CargosActividadesSocioDTO(){}
	
	public CargosActividadesSocioDTO(int idActividad, String nombreActividad, String conceptoPago, String fecha,
			String estado, double importe) {
		super();
		this.idActividad = idActividad;
		NombreActividad = nombreActividad;
		ConceptoPago = conceptoPago;
		Fecha = fecha;
		Estado = estado;
		this.importe = importe;
	}


	public int getIdActividad() {return idActividad;}
	public void setIdActividad(int idActividad) {this.idActividad = idActividad;}
	public String getNombreActividad() {return NombreActividad;}
	public void setNombreActividad(String nombreActividad) {NombreActividad = nombreActividad;}
	public String getConceptoPago() {return ConceptoPago;}
	public void setConceptoPago(String conceptoPago) {ConceptoPago = conceptoPago;}
	public String getFecha() {return Fecha;}
	public void setFecha(String fecha) {Fecha = fecha;}
	public String getEstado() {return Estado;}
	public void setEstado(String estado) {Estado = estado;}
	public double getImporte() {return importe;}
	public void setImporte(double importe) {this.importe = importe;}

}

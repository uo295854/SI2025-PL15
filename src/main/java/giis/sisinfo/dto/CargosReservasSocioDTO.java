package giis.sisinfo.dto;

public class CargosReservasSocioDTO extends CargosSocioDTO{
	int idReserva;
	String NombreInstalacion;
	String ConceptoPago;
	String Fecha;
	String Estado;
	double importe;
	
	public CargosReservasSocioDTO(){}
	
	public CargosReservasSocioDTO(int idReserva, String nombreInstalacion, String conceptoPago, String fecha,
			String estado, double importe) {
		this.idReserva = idReserva;
		NombreInstalacion = nombreInstalacion;
		ConceptoPago = conceptoPago;
		Fecha = fecha;
		Estado = estado;
		this.importe = importe;
	}


	public int getIdReserva() {return idReserva;}
	public void setIdReserva(int idActividad) {this.idReserva = idActividad;}
	public String getNombreInstalacion() {return NombreInstalacion;}
	public void getNombreInstalacion(String nombreActividad) {NombreInstalacion = nombreActividad;}
	public String getConceptoPago() {return ConceptoPago;}
	public void setConceptoPago(String conceptoPago) {ConceptoPago = conceptoPago;}
	public String getFecha() {return Fecha;}
	public void setFecha(String fecha) {Fecha = fecha;}
	public String getEstado() {return Estado;}
	public void setEstado(String estado) {Estado = estado;}
	public double getImporte() {return importe;}
	public void setImporte(double importe) {this.importe = importe;}

}

package giis.sisinfo.dto;

public class DiaReservaSocioDTO {

	
	   private String dia;
	    private String fecha;
	    private String estado;

	    public DiaReservaSocioDTO(String dia, String fecha, String estado) {
	        this.dia = dia;
	        this.fecha = fecha;
	        this.estado = estado;
	    }

	    public String getDia() { return dia; }
	    public String getFecha() { return fecha; }
	    public String getEstado() { return estado; }
}

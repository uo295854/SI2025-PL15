package giis.sisinfo.dto;

public class PeriodoOficialItemDTO {
	private int idPeriodoOficial;
	private String nombre;   // SEPTIEMBRE/ENERO/JUNIO
	private String fechaIni; // yyyy-MM-dd (en BD es TEXT)
	private String fechaFin; // yyyy-MM-dd

	public PeriodoOficialItemDTO() { }

	public int getIdPeriodoOficial() { return idPeriodoOficial; }
	public void setIdPeriodoOficial(int idPeriodoOficial) { this.idPeriodoOficial = idPeriodoOficial; }

	public String getNombre() { return nombre; }
	public void setNombre(String nombre) { this.nombre = nombre; }

	public String getFechaIni() { return fechaIni; }
	public void setFechaIni(String fechaIni) { this.fechaIni = fechaIni; }

	public String getFechaFin() { return fechaFin; }
	public void setFechaFin(String fechaFin) { this.fechaFin = fechaFin; }

	@Override
	public String toString() {
		// Para que el JComboBox muestre algo “bonito”
		if (nombre == null) return "";
		String n = nombre.substring(0, 1) + nombre.substring(1).toLowerCase();
		return n; // "Septiembre", "Enero", "Junio"
	}
}
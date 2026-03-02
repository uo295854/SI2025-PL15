package giis.sisinfo.dto;

public class InstalacionDTO {
	String nombreInstalacion;
	String tipoDeporte;
	String tipoInstalacion;
	int aforoMaximo;
	
	public InstalacionDTO() {};
	public InstalacionDTO(String nNI, String nTD, String nTI, int nAM) {
		this.nombreInstalacion=nNI;
		this.tipoDeporte=nTD;
		this.tipoInstalacion=nTI;
		this.aforoMaximo=nAM;
	}
	
	public String getNombreInstalacion() {
		return nombreInstalacion;
	}
	public void setNombreInstalacion(String nombreInstalacion) {
		this.nombreInstalacion = nombreInstalacion;
	}
	public String getTipoDeporte() {
		return tipoDeporte;
	}
	public void setTipoDeporte(String tipoDeporte) {
		this.tipoDeporte = tipoDeporte;
	}
	public String getTipoInstalacion() {
		return tipoInstalacion;
	}
	public void setTipoInstalacion(String tipoInstalacion) {
		this.tipoInstalacion = tipoInstalacion;
	}
	public int getAforoMaximo() {
		return aforoMaximo;
	}
	public void setAforoMaximo(int aforoMaximo) {
		this.aforoMaximo = aforoMaximo;
	}
	
	
	
}

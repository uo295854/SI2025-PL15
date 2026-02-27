package giis.sisinfo.dto;

public class HoraReservaSocioDTO {
	
    private String horaInicio;
    private String horaFin;
    private String estado;
    private String motivo;

    public HoraReservaSocioDTO(String horaInicio, String horaFin, String estado, String motivo) {
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.estado = estado;
        this.motivo = motivo;
    }

    public String getHoraInicio() { return horaInicio; }
    public String getHoraFin() { return horaFin; }
    public String getEstado() { return estado; }
    public String getMotivo() { return motivo; }

}

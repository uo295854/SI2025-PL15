package giis.sisinfo.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ResguardoReservaSocioDTO {

    private String centro;
    private String direccion;
    private String municipio;
    private String provincia;

    private String deporte;
    private String instalacion;
    private LocalDate fecha;
    private List<LocalTime> horas;
    private double cuota;
    private String estadoPago;
    private LocalDate fechaEmision;

    public ResguardoReservaSocioDTO(String centro, String direccion, String municipio, String provincia,
            String deporte, String instalacion, LocalDate fecha, List<LocalTime> horas,
            double cuota, String estadoPago, LocalDate fechaEmision) {

        this.centro = centro;
        this.direccion = direccion;
        this.municipio = municipio;
        this.provincia = provincia;
        this.deporte = deporte;
        this.instalacion = instalacion;
        this.fecha = fecha;
        this.horas = horas;
        this.cuota = cuota;
        this.estadoPago = estadoPago;
        this.fechaEmision = fechaEmision;
    }

    public String getCentro() {
        return centro;
    }

    public void setCentro(String centro) {
        this.centro = centro;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getDeporte() {
        return deporte;
    }

    public void setDeporte(String deporte) {
        this.deporte = deporte;
    }

    public String getInstalacion() {
        return instalacion;
    }

    public void setInstalacion(String instalacion) {
        this.instalacion = instalacion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public List<LocalTime> getHoras() {
        return horas;
    }

    public void setHoras(List<LocalTime> horas) {
        this.horas = horas;
    }

    public double getCuota() {
        return cuota;
    }

    public void setCuota(double cuota) {
        this.cuota = cuota;
    }

    public String getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(String estadoPago) {
        this.estadoPago = estadoPago;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }
}
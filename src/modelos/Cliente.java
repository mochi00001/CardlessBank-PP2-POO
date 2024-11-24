package modelos;

import java.util.ArrayList;
import java.util.List;

public abstract class Cliente {
    private String nombre;
    private long identificacion;
    private String numTelefono;
    private String correoElectronico;
    protected transient List<Cuenta> misCuentas;

    public Cliente(String nombre, long identificacion, String numTelefono, String correoElectronico) {
        this.nombre = nombre;
        this.identificacion = identificacion;
        this.numTelefono = numTelefono;
        this.correoElectronico = correoElectronico;
        this.misCuentas = new ArrayList<>();
    }

    public List<Cuenta> getMisCuentas() {
        return misCuentas;
    }

    public void mostrarCuentas() {
        StringBuilder sb = new StringBuilder();
        sb.append("Cuentas asociadas al cliente ").append(nombre)
                .append(" (ID: ").append(identificacion).append("):\n");
        for (Cuenta cuenta : misCuentas) {
            sb.append("Cuenta: ").append(cuenta.getCodigo())
                    .append(", Saldo: ").append(cuenta.getSaldo()).append("\n");
        }
        System.out.println(sb.toString());
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public long getIdentificacion() {
        return identificacion;
    }

    public String getNumTelefono() {
        return numTelefono;
    }

    public void setNumTelefono(String numTelefono) {
        this.numTelefono = numTelefono;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public void agregarCuenta(Cuenta cuenta) {
        misCuentas.add(cuenta);
    }

    public abstract String getTipo();

    // Sobreescritura del método
    @Override
    public String toString() {
        return "Cliente{" +
                "nombre='" + nombre + '\'' +
                ", identificacion=" + identificacion +
                ", numTelefono='" + numTelefono + '\'' +
                ", correoElectronico='" + correoElectronico + '\'' +
                ", misCuentas=" + misCuentas.size() + " cuentas" +
                '}';
    }
}

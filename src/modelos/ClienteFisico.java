package modelos;

import java.time.LocalDate;
import java.util.ArrayList;

public class ClienteFisico extends Cliente {
    private transient LocalDate fechaNacimiento;
    private int maxCuentas;
    private int contador;
    private ArrayList<Cuenta> misCuentas;

    public ClienteFisico(String nombre, long identificacion, String numTelefono, String correoElectronico,
            LocalDate fechaNacimiento, int maxCuentas) {
        super(nombre, identificacion, numTelefono, correoElectronico);
        this.fechaNacimiento = fechaNacimiento;
        this.maxCuentas = maxCuentas;
        this.contador = 1;
        this.misCuentas = new ArrayList<>();
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public int getMaxCuentas() {
        return maxCuentas;
    }

    @Override
    public String getTipo() {
        return "Físico";
    }

    @Override
    public void agregarCuenta(Cuenta cuenta) {
        if (contador < maxCuentas) {
            misCuentas.add(cuenta);
        }
    }

    @Override
    public String toString() {
        return super.toString() + ", Fecha de nacimiento: " + fechaNacimiento + ", Max cuentas: " + maxCuentas;
    }
}

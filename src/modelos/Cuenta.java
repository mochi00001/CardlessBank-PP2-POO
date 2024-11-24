package modelos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cuenta implements Comparable<Cuenta> {
    private String codigo;
    private static int cantidadCuentas;
    private transient LocalDate fechaCreacion;
    private String estatus;
    private double saldo;
    private String pin;
    private transient Cliente miCliente;
    private transient List<Transaccion> transacciones;

    private int intentosValidacion = 0;
    private int usosPin = 0;
    private double sumaRetiros = 0; // Verificar uso
    public int cantidadTransacciones = 0; // Contador de transacciones para cada cuenta

    public Cuenta(double saldo, String pin, Cliente cliente) {
        this.codigo = "cta-" + String.valueOf(Cliente.getCantidadCuentasDelSistema() + 1);
        Cliente.setCantidadCuentasDelSistema(Cliente.getCantidadCuentasDelSistema() + 1);
        this.saldo = saldo;
        this.pin = pin;
        this.miCliente = cliente;
        this.transacciones = new ArrayList<>();
        this.estatus = "Activa";
        this.fechaCreacion = LocalDate.now();
    }

    public Cuenta(double saldo, String codigo, String pin, Cliente cliente, String estatus) {
        this(saldo, pin, cliente);
        this.estatus = estatus;
    }

    public List<Transaccion> getTransacciones() {
        return transacciones;
    }

    public void agregarTransaccion(String tipo, double monto) {
        cantidadTransacciones++;
        Transaccion transaccion;
        if ((tipo.equalsIgnoreCase("deposito") || tipo.equalsIgnoreCase("retiro")) && cantidadTransacciones > 5) {
            monto += Transaccion.calcularComision(monto, cantidadTransacciones);
            transaccion = new Transaccion(tipo, monto, codigo, true);
        } else {
            transaccion = new Transaccion(tipo, monto, codigo, false);
        }
        transacciones.add(transaccion);

        if (tipo.equalsIgnoreCase("deposito")) {
            saldo += monto;
        } else {
            saldo -= monto;
        }
    }

    public int getCantidadTransacciones() {
        return cantidadTransacciones;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public String getPin() {
        return this.pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public double getSaldo() {
        return saldo;
    }

    public String getSaldoFormateado() {
        return String.format("%.2f", saldo);
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public Cliente getMiCliente() {
        return miCliente;
    }

    public void setCliente(Cliente cliente) {
        this.miCliente = cliente;
    }

    public long getIdentificacionCliente() {
        return miCliente != null ? miCliente.getIdentificacion() : -1; // Devuelve -1 si no hay cliente
    }

    public String getNombreCompleto() {
        return miCliente != null ? miCliente.getNombre() : "Cliente no asociado";
    }

    public String getNumTelefono() {
        return miCliente != null ? miCliente.getNumTelefono() : "Teléfono no disponible";
    }

    public void agregarTransaccion(Transaccion transaccion) {
        transacciones.add(transaccion);
        cantidadTransacciones++;
    }

    public boolean verificarPin(String pin) {
        return this.pin.equals(pin);
    }

    // Métodos básicos para crédito y débito
    public void acreditar(double monto) {
        this.saldo += monto;
        agregarTransaccion("deposito", monto); // Cambiado a "deposito"
    }

    public void debitar(double monto) {
        if (saldo >= monto) {
            this.saldo -= monto;
            agregarTransaccion("retiro", monto); // Cambiado a "retiro"
        } else {
            throw new IllegalArgumentException("Fondos insuficientes");
        }
    }

    public boolean retirar(double monto) {
        if (saldo >= monto) {
            // El método agregarTransaccion ya aplica la comisión si corresponde
            agregarTransaccion("retiro", monto);
            return true;
        }
        return false;
    }

    public void depositar(double monto) {
        saldo += monto;
        agregarTransaccion("deposito", monto); // Cambiado a "deposito"
    }

    public static String generarCodigoAleatorio() {
        return String.valueOf((int) (Math.random() * 9000) + 1000);
    }

    // Implementación del método compareTo para ordenar por saldo ascendente
    @Override
    public int compareTo(Cuenta otraCuenta) {
        return Double.compare(this.saldo, otraCuenta.getSaldo());
    }
}

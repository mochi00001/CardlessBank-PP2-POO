package modelos;

import java.time.LocalDate;

public class Transaccion {
    private static final int LIMITE_TRANSACCIONES_SIN_COMISION = 5;
    private static final double PORCENTAJE_COMISION = 0.02;
    private double montoComision;
    private String tipo;
    private transient LocalDate fecha;
    private double monto;
    private String codigoCuenta;

    public Transaccion(String tipo, double monto, String codigoCuenta, int cantidadTransacciones) {
        this.tipo = tipo;
        this.monto = monto;
        this.codigoCuenta = codigoCuenta;
        this.fecha = LocalDate.now();
        this.montoComision = calcularComision(monto, cantidadTransacciones);
    }

    // Calcula la comisión en función de la cantidad de transacciones
    private double calcularComision(double montoTransaccion, int cantidadTransacciones) {
        if (cantidadTransacciones >= LIMITE_TRANSACCIONES_SIN_COMISION) {
            return PORCENTAJE_COMISION * montoTransaccion;
        }
        return 0; // No hay comisión para las primeras 5 transacciones
    }

    // Devuelve verdadero si hay una comisión aplicada en esta transacción
    public boolean isComision() {
        return montoComision > 0;
    }

    // Getters
    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getTipo() {
        return tipo;
    }

    public double getMonto() {
        return monto;
    }

    public double getMontoComision() {
        return montoComision;
    }

    public String getCodigoCuenta() {
        return codigoCuenta;
    }

    // Representación en cadena para visualización
    @Override
    public String toString() {
        return "Cuenta: " + codigoCuenta + ", Monto: " + monto + ", Fecha: " + fecha +
                (isComision() ? ", Comisión: " + montoComision : ", Sin comisión");
    }
}

package modelos;

import java.time.LocalDate;

public class Transaccion {
    private static final int LIMITE_TRANSACCIONES_SIN_COMISION = 5;
    private static final double PORCENTAJE_COMISION_SOBRE_DEPOSITOS_Y_RETIROS = 0.02;
    private boolean comision;
    private String tipo;
    private transient LocalDate fecha;
    private double monto;
    private String codigoCuenta;

    public Transaccion(String tipo, double monto, String codigoCuenta, boolean comision) {
        this.tipo = tipo;
        this.monto = monto;
        this.codigoCuenta = codigoCuenta;
        this.fecha = LocalDate.now();
        this.comision = comision;
    }

    // Calcula la comisión en función de la cantidad de transacciones
    public static double calcularComision(double montoTransaccion, int cantidadTransacciones) {
        if (cantidadTransacciones > LIMITE_TRANSACCIONES_SIN_COMISION) {
            return PORCENTAJE_COMISION_SOBRE_DEPOSITOS_Y_RETIROS * montoTransaccion;
        }
        return 0; // No hay comisión para las primeras 5 transacciones
    }

    // Devuelve verdadero si hay una comisión aplicada en esta transacción
    public boolean getComision() {
        return comision;
    }

    public double getMontoComision() {
        if (comision) {
            return ((PORCENTAJE_COMISION_SOBRE_DEPOSITOS_Y_RETIROS / monto) - monto);
        }
        return 0;
    }

    public void setComicion(boolean comision) {
        this.comision = comision;
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

    public String getCodigoCuenta() {
        return codigoCuenta;
    }

    // Representación en cadena para visualización
    @Override
    public String toString() {
        return "Cuenta: " + codigoCuenta + ", Monto: " + monto + ", Fecha: " + fecha +
                (comision ? ", Comisión: " + comision : ", Sin comisión");
    }
}

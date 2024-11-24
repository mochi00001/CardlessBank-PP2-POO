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
    private double montoComision;

    public Transaccion(String tipo, double monto, String codigoCuenta, boolean comision) {
        this.tipo = tipo;
        this.monto = monto;
        this.codigoCuenta = codigoCuenta;
        this.fecha = LocalDate.now();
        this.comision = comision;
        // Calcular y almacenar la comisión si corresponde
        if (comision) {
            this.montoComision = calcularComision(monto);
            System.out.println("Monto de comisión: " + this.montoComision);
        } else {
            this.montoComision = 0.0;
        }
    }

    // Calcula la comisión como el 2% del monto de la transacción
    public double calcularComision(double montoTransaccion) {
        return montoTransaccion * PORCENTAJE_COMISION_SOBRE_DEPOSITOS_Y_RETIROS;
    }

    // Devuelve verdadero si hay una comisión aplicada en esta transacción
    public boolean getComision() {
        return comision;
    }

    public double getMontoComision() {
        return montoComision;
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

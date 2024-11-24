package controladores;

import java.util.ArrayList;
import java.util.List;

import modelos.Cliente;
import modelos.Cuenta;
import modelos.Transaccion;
import servicios.MensajeSMS;
import servicios.PersistenciaDatos;
import servicios.TipoDeCambioBCCR;

public class TransaccionesControlador {

    private List<Cliente> clientes;
    private String palabraGenerada;

    public TransaccionesControlador(List<Cliente> clientes) {
        this.clientes = clientes;
    }

    public List<Transaccion> obtenerTransaccionesPorCuenta(String numeroCuenta) {
        List<Transaccion> transaccionesPorCuenta;
        transaccionesPorCuenta = new ArrayList<>();
        for (Cliente cliente : clientes) {
            for (Cuenta cuenta : cliente.getMisCuentas()) {
                if (cuenta.getCodigo().equals(numeroCuenta)) {
                    transaccionesPorCuenta = cuenta.getTransacciones();
                    break;
                }
            }
        }
        return transaccionesPorCuenta;
    }

    private void actualizarSaldo(Cuenta cuenta, double monto) {
        double nuevoSaldo = cuenta.getSaldo() + monto;
        cuenta.setSaldo(nuevoSaldo);
        PersistenciaDatos.guardarDatos(clientes);
    }

    private void actualizarSaldoRetiro(Cuenta cuenta, double monto) {
        if (monto > 0 && monto <= cuenta.getSaldo()) {
            double nuevoSaldo = cuenta.getSaldo() - monto;
            cuenta.setSaldo(nuevoSaldo);
            PersistenciaDatos.guardarDatos(clientes);
        } else {
            throw new IllegalArgumentException("El monto debe ser positivo y no puede exceder el saldo actual.");
        }
    }

    public String realizarDepositoColones(String numeroCuenta, int monto) {
        if (!verificarCuenta(numeroCuenta)) {
            return "Error: Número de cuenta no registrado.";
        }
        if (monto <= 0) {
            return "Error: El monto debe ser mayor a cero.";
        }

        Cuenta cuenta = null;
        for (Cliente cliente : clientes) {
            for (Cuenta c : cliente.getMisCuentas()) {
                if (c.getCodigo().equals(numeroCuenta)) {
                    cuenta = c;
                    break;
                }
            }
            if (cuenta != null) {
                break;
            }
        }

        if (cuenta == null) {
            return "Error: No se encontró la cuenta.";
        }
        Transaccion transaccion = new Transaccion("Depósito en Colones", monto, numeroCuenta,
                true);
        cuenta.agregarTransaccion(transaccion);
        double montoRealDepositado = monto - transaccion.getMontoComision();

        actualizarSaldo(cuenta, montoRealDepositado);
        PersistenciaDatos.guardarDatos(clientes);

        registrarTransaccion("Depósito en Colones", monto, numeroCuenta, montoRealDepositado, cuenta);
        return String.format("Depósito realizado exitosamente de %d colones.\n\n" +
                "El monto real depositado a su cuenta %s es de %.2f colones\n" +
                "El monto cobrado por concepto de comisión fue de %.2f colones, que fueron rebajados automáticamente de su saldo actual.",
                monto,
                numeroCuenta,
                montoRealDepositado,
                transaccion.getMontoComision());
    }

    public String realizarDepositoDolares(String numeroCuenta, double montoUSD) {

        Cuenta cuenta = null;

        for (Cliente cliente : clientes) {
            for (Cuenta c : cliente.getMisCuentas()) {
                if (c.getCodigo().equals(numeroCuenta)) {
                    cuenta = c;
                    break;
                }
            }
        }
        if (montoUSD <= 0 || montoUSD % 1 != 0) {
            return "Error: El monto debe ser un número entero mayor a cero.";
        }

        double tipoCambio = TipoDeCambioBCCR.getTipoCambioCompra();
        double montoColones = montoUSD * tipoCambio;

        // Crear y agregar la transacción, incluyendo el cálculo de la comisión
        Transaccion transaccion = new Transaccion("Depósito en Dólares", montoColones, numeroCuenta,
                true);
        cuenta.agregarTransaccion(transaccion);
        double montoRealDepositado = montoColones - transaccion.getMontoComision();

        actualizarSaldo(cuenta, montoRealDepositado);
        PersistenciaDatos.guardarDatos(clientes);

        registrarTransaccion("Depósito en Dólares", montoColones, numeroCuenta, montoRealDepositado, cuenta);
        return String.format("Depósito realizado exitosamente de %.2f dólares.\n\n" +
                "El monto real depositado a su cuenta %s es de %.2f colones\n" +
                "El monto cobrado por concepto de comisión fue de %.2f colones, que fueron rebajados automáticamente de su saldo actual.",
                montoUSD,
                numeroCuenta,
                montoRealDepositado,
                transaccion.getMontoComision());
    }

    public String enviarPalabraVerificacion(String numeroCuenta) {

        Cuenta cuenta = null;

        for (Cliente cliente : clientes) {
            for (Cuenta c : cliente.getMisCuentas()) {
                if (c.getCodigo().equals(numeroCuenta)) {
                    cuenta = c;
                    break;
                }
            }
            if (cuenta != null) {
                break;
            }
        }
        MensajeSMS mensajeSMS = new MensajeSMS();
        this.palabraGenerada = mensajeSMS.generarPalabraVerificacion();

        String numeroDestino = "+506" + cuenta.getNumTelefono();
        // System.out.println("El número de destino es: " + numeroDestino + "\nLa
        // palabra generada es: " + palabraGenerada);
        boolean mensajeEnviado = mensajeSMS.enviarMensajeVerificacion(numeroDestino, palabraGenerada);

        return mensajeEnviado ? palabraGenerada : null; // Devolvemos la palabra generada si el mensaje fue enviado
                                                        // exitosamente
    }

    /**
     * Método para validar la palabra clave ingresada por el usuario.
     *
     * @param palabraIngresada La palabra que el usuario ingresó.
     * @return true si la palabra ingresada coincide con la palabra generada
     *         previamente, false en caso contrario.
     */
    public boolean validarPalabraClave(String palabraIngresada) {
        // Verificar si la palabra generada es igual a la palabra ingresada
        if (palabraGenerada == null) {
            // No hay una palabra generada, no se puede validar
            return false;
        }
        return palabraGenerada.equals(palabraIngresada);
    }

    public String realizarRetiroEnColones(String numeroCuenta, String pin, String palabraIngresada,
            double montoRetiro) {

        Cuenta cuenta = null;
        for (Cliente cliente : clientes) {
            for (Cuenta c : cliente.getMisCuentas()) {
                if (c.getCodigo().equals(numeroCuenta)) {
                    cuenta = c;
                    break;
                }
            }
            if (cuenta != null) {
                break;
            }
        }

        if (!validarPinCuenta(numeroCuenta, pin)) {
            return "Error: PIN incorrecto.";
        }

        if (!palabraIngresada.equals(palabraGenerada)) {
            return "Error: La palabra ingresada no coincide.";
        }

        if (montoRetiro <= 0 || montoRetiro % 1 != 0) {
            return "Error: El monto de retiro debe ser un número entero mayor a cero.";
        }
        if (montoRetiro > cuenta.getSaldo()) {
            return "Error: Fondos insuficientes para realizar el retiro.";
        }

        actualizarSaldoRetiro(cuenta, montoRetiro);
        registrarTransaccion("Retiro en Colones", montoRetiro, numeroCuenta, montoRetiro, cuenta);

        return "Estimado usuario: " + cuenta.getNombreCompleto() + ", el monto de este retiro de su cuenta "
                + numeroCuenta + " es " + montoRetiro + " colones, por favor tome el dinero dispensado.\n\n" +
                "El monto cobrado por concepto de comisión fue de 0.00 colones, que fueron rebajados automáticamente de su saldo actual";
    }

    public String realizarRetiroEnDolares(String numeroCuenta, String pin, String palabraIngresada, int montoRetiro) {
        Cuenta cuenta = null;
        for (Cliente cliente : clientes) {
            for (Cuenta c : cliente.getMisCuentas()) {
                if (c.getCodigo().equals(numeroCuenta)) {
                    cuenta = c;
                    break;
                }
            }
            if (cuenta != null) {
                break;
            }
        }

        if (!validarPinCuenta(numeroCuenta, pin)) {
            return "Error: PIN incorrecto.";
        }

        if (!palabraIngresada.equals(palabraGenerada)) {
            return "Error: La palabra ingresada no coincide.";
        }

        if (montoRetiro <= 0 || montoRetiro % 1 != 0) {
            return "Error: El monto de retiro debe ser un número entero mayor a cero.";
        }

        double tipoCambio = TipoDeCambioBCCR.getTipoCambioVenta();
        double montoEnColones = montoRetiro * tipoCambio;

        if (montoEnColones > cuenta.getSaldo()) {
            return "Error: Fondos insuficientes para realizar el retiro.";
        }

        actualizarSaldoRetiro(cuenta, montoEnColones);
        registrarTransaccion("Retiro en Dólares", montoRetiro, numeroCuenta, montoEnColones, cuenta);

        return "Estimado usuario: " + cuenta.getNombreCompleto() + "\n" +
                "El monto de este retiro de su cuenta " + numeroCuenta + " es " + montoRetiro + " dólares.\n" +
                "Por favor tome el dinero dispensado.\n\n" +
                "Según el BCCR, el tipo de cambio de venta del dólar de hoy es: " + tipoCambio + "\n" +
                "El monto equivalente de su retiro es " + montoEnColones + " colones\n" +
                "El monto cobrado por concepto de comisión fue de 0.00 colones, que fueron rebajados automáticamente de su saldo actual";
    }

    public String validarCuentaDestino(String numeroCuentaOrigen, String numeroCuentaDestino) {

        Cuenta cuentaOrigen = null;
        Cuenta cuentaDestino = null;
        // Validar cuentas
        for (Cliente cliente : clientes) {
            for (Cuenta cuenta : cliente.getMisCuentas()) {
                if (cuenta.getCodigo().equals(numeroCuentaOrigen)) {
                    cuentaOrigen = cuenta;
                } else if (cuenta.getCodigo().equals(numeroCuentaDestino)) {
                    cuentaDestino = cuenta;
                }
            }
        }

        if (cuentaOrigen.getMiCliente().getIdentificacion() != cuentaDestino.getMiCliente().getIdentificacion()) {
            return "La cuenta destino no pertenece al mismo dueño de la cuenta origen.";
        }

        return "SUCCESS";
    }

    public String realizarTransferencia(String numeroCuentaOrigen, String pinCuentaOrigen, String numeroCuentaDestino,
            double montoTransferencia) {
        // Validar cuenta origen y PIN
        if (!validarPinCuenta(numeroCuentaOrigen, pinCuentaOrigen)) {
            return "Error: PIN incorrecto.";
        }

        Cuenta cuentaOrigen = null;
        Cuenta cuentaDestino = null;
        // Validar cuentas
        for (Cliente cliente : clientes) {
            for (Cuenta cuenta : cliente.getMisCuentas()) {
                if (cuenta.getCodigo().equals(numeroCuentaOrigen)) {
                    cuentaOrigen = cuenta;
                } else if (cuenta.getCodigo().equals(numeroCuentaDestino)) {
                    cuentaDestino = cuenta;
                }
            }
        }

        // Validar que sean del mismo dueño
        if (cuentaOrigen.getMiCliente().getIdentificacion() != cuentaDestino.getMiCliente().getIdentificacion()) {
            return "La cuenta destino no pertenece al mismo dueño de la cuenta origen.";
        }

        // Validar monto
        if (montoTransferencia <= 0 || montoTransferencia % 1 != 0) {
            return "Error: El monto debe ser un número entero mayor a cero.";
        }

        // Validar fondos suficientes
        if (montoTransferencia > cuentaOrigen.getSaldo()) {
            return "Error: Fondos insuficientes en la cuenta de origen.";
        }

        // Calcular comisión si aplica
        double comision = cuentaOrigen.cantidadTransacciones >= 5 ? montoTransferencia * 0.02 : 0; // Ejemplo de 2% de
                                                                                                   // comisión
        double montoTotalDebitado = montoTransferencia + comision;

        // Actualizar saldos
        cuentaOrigen.setSaldo(cuentaOrigen.getSaldo() - montoTotalDebitado);
        cuentaDestino.setSaldo(cuentaDestino.getSaldo() + montoTransferencia);

        // Registrar transacciones
        registrarTransaccion("Transferencia", montoTransferencia, numeroCuentaDestino, montoTransferencia,
                cuentaOrigen);
        registrarTransaccion("Transferencia", montoTransferencia, numeroCuentaOrigen, montoTransferencia,
                cuentaDestino);

        // Guardar cambios en persistencia
        PersistenciaDatos.guardarDatos(clientes);

        // Formatear mensaje de respuesta
        String mensaje = String.format(
                "Estimado usuario: %s, la transferencia de fondos se ejecutó satisfactoriamente.\n" +
                        "El monto retirado de la cuenta origen %s y depositado en la cuenta destino %s es de %.2f colones.\n"
                        +
                        "[El monto cobrado por concepto de comisión a la cuenta origen fue de %.2f colones, que fueron rebajados automáticamente de su saldo actual]",
                cuentaOrigen.getNombreCompleto(),
                numeroCuentaOrigen,
                numeroCuentaDestino,
                montoTransferencia,
                comision);

        return mensaje;
    }

    public List<Transaccion> consultarTransacciones(String numeroCuenta, String pin, String palabraIngresada) {
        if (!verificarCuenta(numeroCuenta)) {
            throw new IllegalArgumentException("Error: La cuenta no existe.");
        }

        if (!validarPinCuenta(numeroCuenta, pin)) {
            throw new IllegalArgumentException("Error: PIN incorrecto.");
        }

        // Validar que la palabra ingresada por el usuario corresponda con la palabra
        // enviada
        if (!palabraIngresada.equals(palabraGenerada)) {
            throw new IllegalArgumentException("Error: La palabra ingresada no coincide.");
        }

        List<Transaccion> transacciones = obtenerTransaccionesPorCuenta(numeroCuenta);
        if (transacciones.isEmpty()) {
            throw new IllegalArgumentException(
                    "No se han encontrado transacciones para la cuenta " + numeroCuenta + ".");
        }

        return transacciones;
    }

    private void registrarTransaccion(String tipo, double monto, String numeroCuenta, double montoReal, Cuenta cuenta) {
        Transaccion transaccion = new Transaccion(tipo, monto, numeroCuenta, false);
        List<Transaccion> transacciones = cuenta.getTransacciones();
        transacciones.add(transaccion);
        PersistenciaDatos.guardarDatos(clientes);
    }

    public boolean validarPinCuenta(String numeroCuenta, String pin) {
        for (Cliente cliente : clientes) {
            for (Cuenta cuenta : cliente.getMisCuentas()) {
                if (cuenta.getCodigo().equals(numeroCuenta) && cuenta.verificarPin(pin)) {
                    return true;
                }
            }
        }

        return false;

    }

    public boolean verificarCuenta(String numeroCuenta) {
        for (Cliente cliente : clientes) {
            for (Cuenta cuenta : cliente.getMisCuentas()) {
                if (cuenta.getCodigo().equals(numeroCuenta)) {
                    return true;
                }
            }
        }
        return false;
    }

}

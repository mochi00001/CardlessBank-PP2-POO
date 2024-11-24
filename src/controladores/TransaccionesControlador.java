package controladores;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import modelos.Cuenta;
import modelos.Transaccion;
import servicios.MensajeSMS;
import servicios.PersistenciaDatos;
import servicios.TipoDeCambioBCCR;

public class TransaccionesControlador {

    private CuentaControlador cuentaControlador;
    private List<Transaccion> transacciones;
    private String palabraGenerada;

    public TransaccionesControlador(CuentaControlador cuentaControlador) {
        this.cuentaControlador = cuentaControlador;
        this.transacciones = new ArrayList<>();
        cargarTransacciones();
    }

    private void cargarTransacciones() {
        List<Transaccion> transaccionesDesdeArchivo = PersistenciaDatos.cargarTransacciones();
        transaccionesDesdeArchivo.stream()
                .filter(t -> cuentaControlador.obtenerCuentaPorNumero(t.getCodigoCuenta()).isPresent())
                .forEach(transacciones::add);
    }

    public List<Transaccion> obtenerTransaccionesPorCuenta(String numeroCuenta) {
        List<Transaccion> transaccionesPorCuenta = new ArrayList<>();
        for (Transaccion transaccion : transacciones) {
            if (transaccion.getCodigoCuenta().equals(numeroCuenta)) {
                transaccionesPorCuenta.add(transaccion);
            }
        }
        return transaccionesPorCuenta;
    }

    private void actualizarSaldo(Cuenta cuenta, double monto) {
        double nuevoSaldo = cuenta.getSaldo() + monto;
        cuenta.setSaldo(nuevoSaldo);
        List<Cuenta> cuentasList = cuentaControlador.getCuentas();
        PersistenciaDatos.guardarCuentas(cuentasList);
    }

    private void actualizarSaldoRetiro(Cuenta cuenta, double monto) {
        if (monto > 0 && monto <= cuenta.getSaldo()) {
            double nuevoSaldo = cuenta.getSaldo() - monto;
            cuenta.setSaldo(nuevoSaldo);

            List<Cuenta> cuentasList = cuentaControlador.getCuentas();
            PersistenciaDatos.guardarCuentas(cuentasList);
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

        Optional<Cuenta> cuentaOpt = cuentaControlador.obtenerCuentaPorNumero(numeroCuenta);
        if (!cuentaOpt.isPresent()) {
            return "Error: No se encontró la cuenta.";
        }
        Cuenta cuenta = cuentaOpt.get();

        Transaccion transaccion = new Transaccion("Depósito en Colones", monto, numeroCuenta,
                cuenta.getTransacciones().size());
        cuenta.agregarTransaccion(transaccion);

        double comision = (cuenta.cantidadTransacciones >= 5) ? transaccion.getMontoComision() : 0;
        double montoRealDepositado = monto - comision;

        actualizarSaldo(cuenta, montoRealDepositado);
        PersistenciaDatos.guardarCuentas(cuentaControlador.getCuentas());

        registrarTransaccion("Depósito en Colones", monto, numeroCuenta, montoRealDepositado, cuenta);
        return String.format("Depósito realizado exitosamente de %d colones.\n\n" +
                "El monto real depositado a su cuenta %s es de %.2f colones\n" +
                "El monto cobrado por concepto de comisión fue de %.2f colones, que fueron rebajados automáticamente de su saldo actual.",
                monto,
                numeroCuenta,
                montoRealDepositado,
                comision);
    }

    public String realizarDepositoDolares(String numeroCuenta, double montoUSD) {
        Optional<Cuenta> cuentaOpt = cuentaControlador.obtenerCuentaPorNumero(numeroCuenta);
        if (!cuentaOpt.isPresent()) {
            return "Error: Número de cuenta no registrado.";
        }
        Cuenta cuenta = cuentaOpt.get();

        if (montoUSD <= 0 || montoUSD % 1 != 0) {
            return "Error: El monto debe ser un número entero mayor a cero.";
        }

        double tipoCambio = TipoDeCambioBCCR.obtenerTipoCambioCompra();
        double montoColones = montoUSD * tipoCambio;

        // Crear y agregar la transacción, incluyendo el cálculo de la comisión
        Transaccion transaccion = new Transaccion("Depósito en Dólares", montoColones, numeroCuenta,
                cuenta.getTransacciones().size());
        cuenta.agregarTransaccion(transaccion);

        double comision = (cuenta.cantidadTransacciones >= 5) ? transaccion.getMontoComision() : 0;
        double montoRealDepositado = montoColones - comision;

        actualizarSaldo(cuenta, montoRealDepositado);
        PersistenciaDatos.guardarCuentas(cuentaControlador.getCuentas());

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
        Optional<Cuenta> cuentaOpt = cuentaControlador.obtenerCuentaPorNumero(numeroCuenta);
        if (!cuentaOpt.isPresent()) {
            return null; // Devolvemos null si la cuenta no está registrada
        }

        Cuenta cuenta = cuentaOpt.get();
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
        Optional<Cuenta> cuentaOpt = cuentaControlador.obtenerCuentaPorNumero(numeroCuenta);
        if (!cuentaOpt.isPresent()) {
            return "Error: Número de cuenta no registrado.";
        }
        Cuenta cuenta = cuentaOpt.get();
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
        Optional<Cuenta> cuentaOpt = cuentaControlador.obtenerCuentaPorNumero(numeroCuenta);
        if (!cuentaOpt.isPresent()) {
            return "Error: Número de cuenta no registrado.";
        }
        Cuenta cuenta = cuentaOpt.get();

        if (!validarPinCuenta(numeroCuenta, pin)) {
            return "Error: PIN incorrecto.";
        }

        if (!palabraIngresada.equals(palabraGenerada)) {
            return "Error: La palabra ingresada no coincide.";
        }

        if (montoRetiro <= 0 || montoRetiro % 1 != 0) {
            return "Error: El monto de retiro debe ser un número entero mayor a cero.";
        }

        double tipoCambio = TipoDeCambioBCCR.obtenerTipoCambioVenta();
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
        Optional<Cuenta> cuentaOrigenOpt = cuentaControlador.obtenerCuentaPorNumero(numeroCuentaOrigen);
        Optional<Cuenta> cuentaDestinoOpt = cuentaControlador.obtenerCuentaPorNumero(numeroCuentaDestino);

        if (!cuentaDestinoOpt.isPresent()) {
            return "La cuenta destino no existe.";
        }

        Cuenta cuentaOrigen = cuentaOrigenOpt.get();
        Cuenta cuentaDestino = cuentaDestinoOpt.get();

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

        // Validar cuentas
        Optional<Cuenta> cuentaOrigenOpt = cuentaControlador.obtenerCuentaPorNumero(numeroCuentaOrigen);
        Optional<Cuenta> cuentaDestinoOpt = cuentaControlador.obtenerCuentaPorNumero(numeroCuentaDestino);

        if (!cuentaOrigenOpt.isPresent()) {
            return "Error: La cuenta de origen no está registrada.";
        }
        if (!cuentaDestinoOpt.isPresent()) {
            return "Error: La cuenta de destino no está registrada.";
        }

        Cuenta cuentaOrigen = cuentaOrigenOpt.get();
        Cuenta cuentaDestino = cuentaDestinoOpt.get();

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
        PersistenciaDatos.guardarCuentas(cuentaControlador.getCuentas());

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
        Optional<Cuenta> cuentaOpt = cuentaControlador.obtenerCuentaPorNumero(numeroCuenta);
        if (!cuentaOpt.isPresent()) {
            throw new IllegalArgumentException("Error: La cuenta no está registrada.");
        }
        // Cuenta cuenta = cuentaOpt.get();

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
        Transaccion transaccion = new Transaccion(tipo, monto, numeroCuenta, cuenta.getCantidadTransacciones());
        cuenta.agregarTransaccion(transaccion);
        transacciones.add(transaccion);
        PersistenciaDatos.guardarTransacciones(transacciones);
    }

    public boolean validarPinCuenta(String numeroCuenta, String pin) {
        Optional<Cuenta> cuentaOpt = cuentaControlador.obtenerCuentaPorNumero(numeroCuenta);
        return cuentaOpt.isPresent() && cuentaOpt.get().verificarPin(pin);
    }

    public boolean verificarCuenta(String numeroCuenta) {
        Optional<Cuenta> cuentaOpt = cuentaControlador.obtenerCuentaPorNumero(numeroCuenta);
        return cuentaOpt.isPresent();
    }

}
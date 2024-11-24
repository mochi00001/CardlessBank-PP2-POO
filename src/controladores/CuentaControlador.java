package controladores;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import modelos.Cliente;
import modelos.ClienteFisico;
import modelos.Cuenta;
import modelos.Transaccion;
import servicios.PersistenciaDatos;
import servicios.TipoDeCambioBCCR;

public class CuentaControlador {
    private List<Cliente> clientes;
    private TransaccionesControlador transaccionesControlador;

    public CuentaControlador(List<Cliente> clientes) {
        this.clientes = clientes;
        transaccionesControlador = new TransaccionesControlador(clientes);
    }

    public TransaccionesControlador getTransaccionesControlador() {
        return transaccionesControlador;
    }

    public Optional<Cuenta> obtenerCuentaPorNumero(String numeroCuenta) {
        for (Cliente cliente : clientes) {
            for (Cuenta cuenta : cliente.getMisCuentas()) {
                if (cuenta.getCodigo().equals(numeroCuenta)) {
                    return Optional.of(cuenta);
                }
            }
        }
        return Optional.empty();
    }

    public String crearCuenta(double saldoInicial, String pin, Cliente cliente) {
        try {
            // Validar que el cliente no haya alcanzado el máximo de cuentas permitidas (si
            // aplica)
            if (cliente instanceof ClienteFisico) {
                ClienteFisico clienteFisico = (ClienteFisico) cliente;
                if (clienteFisico.getMisCuentas().size() >= clienteFisico.getMaxCuentas()) {
                    throw new IllegalArgumentException("El cliente ha alcanzado el máximo de cuentas permitidas.");
                }
            }

            Cuenta nuevaCuenta = new Cuenta(saldoInicial, pin, cliente);
            cliente.agregarCuenta(nuevaCuenta);
            PersistenciaDatos.guardarDatos(clientes);
            return nuevaCuenta.getCodigo();
        } catch (Exception e) {
            // System.err.println("Error al crear la cuenta: " + e.getMessage());
            return null;
        }
    }

    public boolean cambiarPinCuenta(Cuenta cuenta, String nuevoPin) {
        try {
            cuenta.setPin(nuevoPin);
            PersistenciaDatos.guardarDatos(clientes);
            return true;
        } catch (Exception e) {
            System.err.println("Error al cambiar el PIN: " + e.getMessage());
            return false;
        }
    }

    public double consultarSaldo(String numeroCuenta, String pin) {
        Optional<Cuenta> cuentaOpt = obtenerCuentaPorNumero(numeroCuenta);

        if (!cuentaOpt.isPresent()) {
            throw new IllegalArgumentException("Error: Número de cuenta no registrado.");
        }
        Cuenta cuenta = cuentaOpt.get();
        if (!cuenta.getPin().equals(pin)) {
            throw new IllegalArgumentException("Error: El PIN es incorrecto.");
        }
        return cuenta.getSaldo();
    }

    public String consultarSaldoDivisaExtranjera(String numeroCuenta, String pin) {
        Optional<Cuenta> cuentaOpt = obtenerCuentaPorNumero(numeroCuenta);

        if (!cuentaOpt.isPresent()) {
            return null;
        }
        Cuenta cuenta = cuentaOpt.get();
        if (!cuenta.getPin().equals(pin)) {
            return null;
        }
        double saldo = cuenta.getSaldo();
        double tipoCambio = TipoDeCambioBCCR.getTipoCambioCompra();
        double saldoEnDivisaExtranjera = saldo / tipoCambio;
        return String.format(
                "Estimado usuario: %s el saldo actual de su cuenta %s es de %.2f dólares.\n\n"
                        + "Para esta conversión se utilizó el tipo de cambio del dólar -precio de compra- "
                        + "Según el BCCR, el tipo de cambio de compra del dólar de hoy es de: %.2f",
                cuenta.getMiCliente().getNombre(),
                cuenta.getCodigo(),
                saldoEnDivisaExtranjera,
                tipoCambio);
    }

    public Map<String, Object> estadoCuenta(String numeroCuenta, String pin) {
        Map<String, Object> resultado = new HashMap<>();

        Optional<Cuenta> cuentaOpt = obtenerCuentaPorNumero(numeroCuenta);

        if (!cuentaOpt.isPresent()) {
            resultado.put("error", "Cuenta no encontrada.");
            return resultado;
        }

        Cuenta cuenta = cuentaOpt.get();

        if (!cuenta.verificarPin(pin)) {
            resultado.put("error", "PIN incorrecto.");
            return resultado;
        }

        Cliente cliente = cuenta.getMiCliente();

        // Información del cliente y la cuenta
        resultado.put("nombreCompleto", cliente.getNombre());
        resultado.put("identificacion", cliente.getIdentificacion());
        resultado.put("numeroTelefono", cliente.getNumTelefono());
        resultado.put("correoElectronico", cliente.getCorreoElectronico());
        resultado.put("numeroCuenta", cuenta.getCodigo());
        resultado.put("saldo", cuenta.getSaldo());
        resultado.put("estatus", cuenta.getEstatus());
        resultado.put("fechaCreacion", cuenta.getFechaCreacion().toString());

        // Obtener el tipo de cambio actual
        double tipoCambioCompra = TipoDeCambioBCCR.getTipoCambioCompra();
        double saldoEnDolares = cuenta.getSaldo() / tipoCambioCompra;

        // Formatear la fecha actual
        LocalDate fechaActual = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String fechaFormateada = fechaActual.format(formatter);

        // Agregar los nuevos datos
        resultado.put("saldoEnDolares", saldoEnDolares);
        resultado.put("tipoCambioCompra", tipoCambioCompra);
        resultado.put("fechaTipoCambio", fechaFormateada);

        return resultado;
    }

    public String estadoCuentaDolares(String numeroCuenta, String pin) {
        Cuenta cuenta = null;
        for (Cliente cliente : clientes) {
            for (Cuenta c : cliente.getMisCuentas()) {
                if (c.getCodigo().equals(numeroCuenta)) {
                    cuenta = c;
                    break;
                }
            }
        }

        if (cuenta == null) {
            return "Cuenta no encontrada.";
        }
        if (!cuenta.verificarPin(pin)) {
            return "PIN incorrecto.";
        }

        TipoDeCambioBCCR.obtenerTipoCambioHoy();
        double tasaDeCambio = TipoDeCambioBCCR.getTipoCambioCompra();
        double saldoColones = cuenta.getSaldo();
        double saldoDolares = saldoColones / tasaDeCambio;
        Cliente cliente = cuenta.getMiCliente();
        String estatus = cuenta.getEstatus();

        return String.format("Cliente:\n" +
                "Nombre: %s\n" +
                "Identificación: %s\n" +
                "Número de Teléfono: %s\n" +
                "Correo: %s\n" +
                "Número de Cuenta: %s\n" +
                "Saldo: $%.2f\n" +
                "Estatus: %s",
                cliente.getNombre(),
                cliente.getIdentificacion(),
                cliente.getNumTelefono(),
                cliente.getCorreoElectronico(),
                cuenta.getCodigo(),
                saldoDolares,
                estatus);
    }

    public String consultarEstatusCuenta(String numeroCuenta) {
        Optional<Cuenta> cuentaOpt = obtenerCuentaPorNumero(numeroCuenta);

        if (!cuentaOpt.isPresent()) {
            return "Error: La cuenta número " + numeroCuenta + " no está registrada en el sistema.";
        }
        Cuenta cuenta = cuentaOpt.get();
        String nombrePropietario = cuenta.getMiCliente().getNombre();
        String estatus = cuenta.getEstatus();

        return "La cuenta número " + numeroCuenta + " a nombre de " + nombrePropietario + " tiene estatus de " + estatus
                + ".";
    }

    public String transferir(String cuentaOrigenId, String cuentaDestinoId, double monto) {
        Cuenta cuentaOrigen = null;
        Cuenta cuentaDestino = null;

        for (Cliente cliente : clientes) {
            for (Cuenta c : cliente.getMisCuentas()) {
                if (c.getCodigo().equals(cuentaOrigenId)) {
                    cuentaOrigen = c;
                } else if (c.getCodigo().equals(cuentaDestinoId)) {
                    cuentaDestino = c;
                }
            }
        }

        if (cuentaOrigen == null || cuentaDestino == null) {
            return "Error: Cuenta de origen o destino no encontrada.";
        }

        if (monto <= 0) {
            return "Error: El monto de transferencia debe ser mayor a cero.";
        }

        if (cuentaOrigen.retirar(monto)) {
            cuentaDestino.depositar(monto);
            PersistenciaDatos.guardarDatos(clientes);
            return "Transferencia realizada con éxito.";
        } else {
            return "Error: Saldo insuficiente en la cuenta de origen.";
        }
    }

    public boolean eliminarCuenta(String numeroCuenta) {
        for (Cliente cliente : clientes) {
            for (Cuenta cuenta : cliente.getMisCuentas()) {
                if (cuenta.getCodigo().equals(numeroCuenta)) {
                    cuenta.setEstatus("Eliminada");
                    cuenta.setSaldo(0);
                    eliminarTransaccionesAsociadas(numeroCuenta);
                    PersistenciaDatos.guardarDatos(clientes);
                    return true;
                }
            }
        }

        return false;
    }

    private void eliminarTransaccionesAsociadas(String numeroCuenta) {
        List<Transaccion> transacciones = PersistenciaDatos.cargarTransacciones();
        Iterator<Transaccion> iterator = transacciones.iterator();

        while (iterator.hasNext()) {
            Transaccion transaccion = iterator.next();
            if (transaccion.getCodigoCuenta().equals(numeroCuenta)) {
                iterator.remove();
            }
        }
        PersistenciaDatos.guardarTransacciones(transacciones);
    }

}
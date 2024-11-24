package main.resources;

import static spark.Spark.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import controladores.ClienteControlador;
import controladores.CuentaControlador;
import controladores.TransaccionesControlador;
import modelos.Cliente;
import modelos.Cuenta;
import modelos.Transaccion;
import servicios.LocalDateAdapter;
import servicios.TipoDeCambioBCCR;

public class App {

    public static void main(String[] args) {
        ClienteControlador clienteControlador = new ClienteControlador();
        CuentaControlador cuentaControlador = new CuentaControlador(clienteControlador);
        TransaccionesControlador transaccionesControlador = new TransaccionesControlador(cuentaControlador);
        TipoDeCambioBCCR.obtenerTipoCambioHoy();
        // Llamar al método para iniciar el servidor
        iniciarServidor(clienteControlador, cuentaControlador, transaccionesControlador);
    }

    private static void iniciarServidor(
            ClienteControlador clienteControlador,
            CuentaControlador cuentaControlador,
            TransaccionesControlador transaccionesControlador) {

        // Configurar el puerto (por defecto es 4567)
        port(4567);

        // Configurar archivos estáticos (si tienes una carpeta con tu frontend)
        staticFiles.location("/public");

        // Habilitar CORS si es necesario
        enableCORS("*", "*", "*");

        // Inicializar Gson para manejar JSON
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();

        // Definir los endpoints
        definirEndpoints(clienteControlador, cuentaControlador, transaccionesControlador, gson);

    }

    private static void definirEndpoints(
            ClienteControlador clienteControlador,
            CuentaControlador cuentaControlador,
            TransaccionesControlador transaccionesControlador,
            Gson gson) {

        // Manejar solicitudes OPTIONS para CORS preflight
        options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        // Endpoint para crear cliente físico
        post("/clientes/fisico", (req, res) -> {
            res.type("application/json");

            ClienteFisicoData data = gson.fromJson(req.body(), ClienteFisicoData.class);

            // Validaciones de datos
            if (data.nombre == null || data.nombre.isEmpty()) {
                return gson.toJson(new StandardResponse(StatusResponse.ERROR, "El nombre es obligatorio."));
            }
            long id = Long.parseLong(String.valueOf(data.identificacion));
            if (id <= 0) {
                return gson.toJson(
                        new StandardResponse(StatusResponse.ERROR, "La identificación debe ser un número positivo."));
            }
            if (data.numTelefono == null || !data.numTelefono.matches("\\d{8}")) {
                return gson.toJson(
                        new StandardResponse(StatusResponse.ERROR, "El número de teléfono debe tener 8 dígitos."));
            }
            if (data.correoElectronico == null || !data.correoElectronico.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
                return gson.toJson(new StandardResponse(StatusResponse.ERROR,
                        "El correo electrónico no tiene un formato válido."));
            }
            if (data.fechaNacimiento == null || data.fechaNacimiento.isEmpty()) {
                return gson
                        .toJson(new StandardResponse(StatusResponse.ERROR, "La fecha de nacimiento es obligatoria."));
            }

            // Convertir fecha de nacimiento
            LocalDate fechaNacimiento;
            try {
                fechaNacimiento = LocalDate.parse(data.fechaNacimiento);
                if (fechaNacimiento.isAfter(LocalDate.now())) {
                    return gson.toJson(new StandardResponse(StatusResponse.ERROR,
                            "La fecha de nacimiento no puede ser una fecha futura."));
                }
            } catch (DateTimeParseException e) {
                return gson.toJson(new StandardResponse(StatusResponse.ERROR,
                        "La fecha de nacimiento no tiene un formato válido."));
            }

            // Crear el cliente físico
            boolean creado = clienteControlador.crearClienteFisico(
                    data.nombre,
                    id,
                    data.numTelefono,
                    data.correoElectronico,
                    fechaNacimiento,
                    data.maxCuentas);

            if (creado) {
                // Crear un mensaje detallado con todos los datos del cliente
                String mensaje = String.format(
                        "Se ha creado un nuevo cliente en el sistema, los datos del nuevo Cliente Físico son: \n" +
                                "Código del cliente: %d\n" +
                                "Nombre completo: %s\n" +
                                "Número de teléfono: %s\n" +
                                "Dirección de correo electrónico: %s\n" +
                                "Identificación: %d\n" +
                                "Cantidad máxima de cuentas que desea crear: %d\n" +
                                "Fecha de nacimiento: %s",
                        data.identificacion,
                        data.nombre,
                        data.numTelefono,
                        data.correoElectronico,
                        data.identificacion,
                        data.maxCuentas,
                        data.fechaNacimiento);

                return gson.toJson(new StandardResponse(StatusResponse.SUCCESS, mensaje));
            } else {
                return gson.toJson(new StandardResponse(StatusResponse.ERROR,
                        "No se pudo crear el cliente. Posiblemente ya existe."));
            }
        });

        // Endpoint para crear cliente jurídico
        post("/clientes/juridico", (req, res) -> {
            res.type("application/json");

            ClienteJuridicoData data = gson.fromJson(req.body(), ClienteJuridicoData.class);

            // Validaciones de datos
            if (data.nombre == null || data.nombre.isEmpty()) {
                return gson.toJson(new StandardResponse(StatusResponse.ERROR,
                        "El nombre del apoderado generalísimo es obligatorio."));
            }
            if (data.numTelefono == null || !data.numTelefono.matches("\\d{8}")) {
                return gson.toJson(
                        new StandardResponse(StatusResponse.ERROR, "El número de teléfono debe tener 8 dígitos."));
            }
            if (data.correoElectronico == null || !data.correoElectronico.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
                return gson.toJson(new StandardResponse(StatusResponse.ERROR,
                        "El correo electrónico no tiene un formato válido."));
            }
            if (data.tipoNegocio == null || data.tipoNegocio.isEmpty()) {
                return gson.toJson(new StandardResponse(StatusResponse.ERROR, "El tipo de negocio es obligatorio."));
            }
            if (data.razonSocial == null || data.razonSocial.isEmpty()) {
                return gson.toJson(new StandardResponse(StatusResponse.ERROR, "La razón social es obligatoria."));
            }
            long id = Long.parseLong(String.valueOf(data.identificacion));
            // Crear el cliente jurídico
            boolean creado = clienteControlador.crearClienteJuridico(
                    data.nombre,
                    id,
                    data.numTelefono,
                    data.correoElectronico,
                    data.tipoNegocio,
                    data.razonSocial);

            if (creado) {
                String mensaje = String.format(
                        "Se ha creado un nuevo cliente en el sistema, los datos del nuevo Cliente Jurídico son: \n" +
                                "Código del cliente: %d\n" +
                                "Nombre completo: %s\n" +
                                "Número de teléfono: %s\n" +
                                "Dirección de correo electrónico: %s\n" +
                                "Tipo de negocio: %s\n" +
                                "Cédula Jurídica: %d\n" +
                                "Razón Social: %s",
                        data.identificacion,
                        data.nombre,
                        data.numTelefono,
                        data.correoElectronico,
                        data.tipoNegocio,
                        data.identificacion,
                        data.razonSocial);
                return gson.toJson(new StandardResponse(StatusResponse.SUCCESS, mensaje));
            } else {
                return gson.toJson(new StandardResponse(StatusResponse.ERROR,
                        "No se pudo crear el cliente. Posiblemente ya existe."));
            }
        });

        // Endpoint para actualizar el teléfono
        put("/clientes/actualizarTelefono", (req, res) -> {
            res.type("application/json");

            ActualizarTelefonoData data = gson.fromJson(req.body(), ActualizarTelefonoData.class);

            if (data.identificacion <= 0 || data.nuevoTelefono == null) {
                return gson
                        .toJson(new StandardResponse(StatusResponse.ERROR, "Datos inválidos para la actualización."));
            }

            try {
                Optional<Cliente> clienteOpt = clienteControlador.buscarClientePorIdentificacion(data.identificacion);
                if (clienteOpt.isPresent()) {
                    Cliente cliente = clienteOpt.get();
                    String telefonoAnterior = cliente.getNumTelefono(); // Guardamos el número antiguo
                    boolean actualizado = clienteControlador.actualizarTelefono(data.identificacion,
                            data.nuevoTelefono);

                    if (actualizado) {
                        JsonObject result = new JsonObject();
                        result.addProperty("nombre", cliente.getNombre());
                        result.addProperty("telefonoAnterior", telefonoAnterior);
                        return gson.toJson(new StandardResponse(StatusResponse.SUCCESS,
                                "Teléfono actualizado exitosamente.", result));
                    } else {
                        return gson.toJson(new StandardResponse(StatusResponse.ERROR,
                                "No se pudo actualizar el teléfono. Cliente no encontrado."));
                    }
                } else {
                    return gson.toJson(new StandardResponse(StatusResponse.ERROR, "Cliente no encontrado."));
                }
            } catch (IllegalArgumentException e) {
                return gson.toJson(new StandardResponse(StatusResponse.ERROR, e.getMessage()));
            }
        });

        // Endpoint para actualizar el correo electrónico
        put("/clientes/cambiarCorreo", (req, res) -> {
            res.type("application/json");

            ActualizarCorreoData data = gson.fromJson(req.body(), ActualizarCorreoData.class);

            if (data.identificacion <= 0) {
                return gson.toJson(
                        new StandardResponse(StatusResponse.ERROR, "La identificación debe ser un número positivo."));
            }
            if (data.nuevoCorreo == null || !data.nuevoCorreo.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
                return gson.toJson(new StandardResponse(StatusResponse.ERROR,
                        "El nuevo correo electrónico no tiene un formato válido."));
            }

            Optional<Cliente> clienteOpt = clienteControlador.buscarClientePorIdentificacion(data.identificacion);
            if (clienteOpt.isPresent()) {
                Cliente cliente = clienteOpt.get();
                String correoAnterior = cliente.getCorreoElectronico(); // Guardar correo anterior para la respuesta
                boolean actualizado = clienteControlador.actualizarCorreo(data.identificacion, data.nuevoCorreo);

                if (actualizado) {
                    String mensaje = String.format(
                            "Estimado usuario: %s, usted ha cambiado la dirección de correo %s por %s.",
                            cliente.getNombre(), correoAnterior, data.nuevoCorreo);
                    return gson.toJson(new StandardResponse(StatusResponse.SUCCESS, mensaje, cliente));
                } else {
                    return gson.toJson(new StandardResponse(StatusResponse.ERROR,
                            "No se pudo actualizar el correo electrónico. Cliente no encontrado."));
                }
            } else {
                return gson.toJson(new StandardResponse(StatusResponse.ERROR, "Cliente no encontrado."));
            }
        });

        // Gestion de Cuentas

        // Crear cuenta

        // Endpoint para validar si un cliente existe y devolver sus datos
        get("/clientes/validarCliente/:identificacion", (req, res) -> {
            res.type("application/json");
            long identificacion = Long.parseLong(req.params(":identificacion"));

            Optional<Cliente> clienteOpt = clienteControlador.buscarClientePorIdentificacion(identificacion);
            // System.out.println("Validando cliente: " + clienteOpt.toString());
            if (clienteOpt.isPresent()) {
                Cliente cliente = clienteOpt.get();
                return gson.toJson(new StandardResponse(StatusResponse.SUCCESS, "Cliente encontrado.", cliente));
            } else {
                return gson.toJson(new StandardResponse(StatusResponse.ERROR, "El cliente no existe."));
            }
        });

        // Endpoint para crear una cuenta
        post("/cuentas/crearCuenta", (req, res) -> {
            // System.out.println("Endpoint para crear una cuenta");
            res.type("application/json");
            CuentaData data = gson.fromJson(req.body(), CuentaData.class);
            // System.out.println("Validando los datos.");
            // Validar datos
            long id = Long.parseLong(String.valueOf(data.identificacion));
            if (id <= 0) {
                return gson.toJson(new StandardResponse(StatusResponse.ERROR, "La identificación es obligatoria."));
            }
            if (data.saldoInicial < 0) {
                return gson
                        .toJson(new StandardResponse(StatusResponse.ERROR, "El saldo inicial no puede ser negativo."));
            }
            if (data.pin == null || data.pin.isEmpty()) {
                return gson.toJson(new StandardResponse(StatusResponse.ERROR, "El PIN es obligatorio."));
            }

            // Buscar el cliente
            // System.out.println("Buscando al cliente");
            Optional<Cliente> clienteOpt = clienteControlador.buscarClientePorIdentificacion(id);
            if (!clienteOpt.isPresent()) {
                return gson.toJson(new StandardResponse(StatusResponse.ERROR, "El cliente no existe."));
            }

            Cliente cliente = clienteOpt.get();

            // System.out.println("Creando la cuenta");
            // Crear la cuenta
            String cuentaCreada = cuentaControlador.crearCuenta(data.saldoInicial, data.pin, cliente);
            // System.out.println("Cuenta creada: " + cuentaCreada);
            if (cuentaCreada != null) {
                String mensaje = String.format(
                        "Se ha creado una nueva cuenta en el sistema.\nNúmero de cuenta: %s\nEstatus de la cuenta: Activa\nSaldo actual: %.2f colones\nTipo de cliente: %s\nNombre del dueño o apoderado generalísimo de la cuenta: %s",
                        cuentaCreada, data.saldoInicial, cliente.getTipo(), cliente.getNombre());

                return gson.toJson(new StandardResponse(StatusResponse.SUCCESS, mensaje));
            } else {
                return gson.toJson(new StandardResponse(StatusResponse.ERROR, "No se pudo crear la cuenta."));
            }
        });

        // Cambiar pin de la cuenta

        // Endpoint para validar si una cuenta existe
        get("/cuentas/validarCuenta/:numeroCuenta", (req, res) -> {
            res.type("application/json");
            String numeroCuenta = req.params(":numeroCuenta");

            Optional<Cuenta> cuentaOpt = cuentaControlador.obtenerCuentaPorNumero(numeroCuenta);
            // System.out.println("Validando cuenta: " + cuentaOpt.get().getCodigo());
            if (cuentaOpt.isPresent()) {
                Cuenta cuenta = cuentaOpt.get();
                String mensaje = ("Cuenta encontrada: " + cuenta.getCodigo() + "\nPropietario: "
                        + cuenta.getNombreCompleto());
                // System.out.println(mensaje);
                StandardResponse response = new StandardResponse(StatusResponse.SUCCESS, mensaje);
                String jsonResponse = gson.toJson(response);
                // System.out.println("Respuesta JSON enviada al cliente: " + jsonResponse);
                return jsonResponse;
            } else {
                return gson.toJson(new StandardResponse(StatusResponse.ERROR, "La cuenta no existe."));
            }
        });

        // Endpoint para cambiar el PIN de la cuenta
        post("/cuentas/cambiarPin", (req, res) -> {
            res.type("application/json");
            CambioPinData data = gson.fromJson(req.body(), CambioPinData.class);

            // Validar datos
            if (data.numeroCuenta == null || data.numeroCuenta.isEmpty()) {
                return gson.toJson(new StandardResponse(StatusResponse.ERROR, "El número de cuenta es obligatorio."));
            }
            if (data.pinActual == null || data.pinActual.isEmpty()) {
                return gson.toJson(new StandardResponse(StatusResponse.ERROR, "El PIN actual es obligatorio."));
            }
            if (data.nuevoPin == null || data.nuevoPin.length() < 4) {
                return gson.toJson(
                        new StandardResponse(StatusResponse.ERROR, "El nuevo PIN debe tener al menos 4 caracteres."));
            }

            // Buscar la cuenta
            Optional<Cuenta> cuentaOpt = cuentaControlador.obtenerCuentaPorNumero(data.numeroCuenta);
            if (!cuentaOpt.isPresent()) {
                return gson.toJson(new StandardResponse(StatusResponse.ERROR, "La cuenta no existe."));
            }

            Cuenta cuenta = cuentaOpt.get();

            // Verificar el PIN actual
            if (!cuenta.getPin().equals(data.pinActual)) {
                return gson.toJson(new StandardResponse(StatusResponse.ERROR, "El PIN actual es incorrecto."));
            }

            // Cambiar el PIN
            boolean cambiado = cuentaControlador.cambiarPinCuenta(cuenta, data.nuevoPin);
            if (cambiado) {
                String mensaje = String.format(
                        "Estimado usuario: %s, le informamos que se ha cambiado satisfactoriamente el PIN de su cuenta %s.",
                        cuenta.getMiCliente().getNombre(), cuenta.getCodigo());

                return gson.toJson(new StandardResponse(StatusResponse.SUCCESS, mensaje));
            } else {
                return gson.toJson(
                        new StandardResponse(StatusResponse.ERROR, "No se pudo cambiar el PIN. Inténtelo de nuevo."));
            }
        });

        // Eliminar cuenta
        // Endpoint para validar la cuenta antes de eliminar
        get("/cuentas/validarCuentaParaEliminar/:numeroCuenta/:pin", (req, res) -> {
            res.type("application/json");
            String numeroCuenta = req.params(":numeroCuenta");
            String pin = req.params(":pin");

            Optional<Cuenta> cuentaOpt = cuentaControlador.obtenerCuentaPorNumero(numeroCuenta);
            if (cuentaOpt.isPresent()) {
                Cuenta cuenta = cuentaOpt.get();
                if (cuenta.verificarPin(pin)) {
                    Map<String, Object> cuentaData = new HashMap<>();
                    cuentaData.put("nombreCompleto", cuenta.getNombreCompleto());
                    cuentaData.put("saldo", cuenta.getSaldo());
                    // System.out.println("Cuenta verificada: " + cuentaData);
                    return gson.toJson(
                            new StandardResponse(StatusResponse.SUCCESS, "Cuenta verificada con éxito.", cuentaData));
                } else {
                    return gson.toJson(new StandardResponse(StatusResponse.ERROR, "El PIN es incorrecto."));
                }
            } else {
                return gson.toJson(new StandardResponse(StatusResponse.ERROR, "La cuenta no existe."));
            }
        });

        // Endpoint para eliminar la cuenta
        delete("/cuentas/eliminarCuenta/:numeroCuenta", (req, res) -> {
            res.type("application/json");
            String numeroCuenta = req.params(":numeroCuenta");

            Optional<Cuenta> cuentaOpt = cuentaControlador.obtenerCuentaPorNumero(numeroCuenta);
            if (cuentaOpt.isPresent()) {
                Cuenta cuenta = cuentaOpt.get();
                double saldo = cuenta.getSaldo(); // Guardamos el saldo antes de eliminar

                boolean eliminada = cuentaControlador.eliminarCuenta(numeroCuenta);
                if (eliminada) {
                    Map<String, Object> cuentaData = new HashMap<>();
                    cuentaData.put("saldo", saldo);

                    return gson.toJson(
                            new StandardResponse(StatusResponse.SUCCESS, "Cuenta eliminada exitosamente.", cuentaData));
                }
            }

            return gson.toJson(
                    new StandardResponse(StatusResponse.ERROR, "No se pudo eliminar la cuenta. Verifique los datos."));
        });

        // Operaciones financieras

        // Endpoint para realizar un Deposito en colones

        post("/transacciones/depositoColones", (req, res) -> {
            res.type("application/json");
            TransaccionRequest request = gson.fromJson(req.body(), TransaccionRequest.class);
            String resultado = transaccionesControlador.realizarDepositoColones(request.getNumeroCuenta(),
                    request.getMonto());
            if (resultado.startsWith("Error")) {
                return gson.toJson(new StandardResponse(StatusResponse.ERROR, resultado));
            } else {
                return gson.toJson(new StandardResponse(StatusResponse.SUCCESS, resultado));
            }
        });

        // Deposito en dolar
        // Endpoint para realizar un Deposito en dólares
        post("/transacciones/depositoDolares", (req, res) -> {
            res.type("application/json");
            TransaccionRequest request = gson.fromJson(req.body(), TransaccionRequest.class);
            String resultado = transaccionesControlador.realizarDepositoDolares(request.getNumeroCuenta(),
                    request.getMonto());
            if (resultado.startsWith("Error")) {
                return gson.toJson(new StandardResponse(StatusResponse.ERROR, resultado));
            } else {
                return gson.toJson(new StandardResponse(StatusResponse.SUCCESS, resultado));
            }
        });

        // Retiro en colones

        // Endpoint para validar si una cuenta existe y el PIN es correcto
        // Validar Cuenta y PIN para Retiro
        get("/cuentas/validarCuentaParaRetiro/:numeroCuenta/:pin", (req, res) -> {
            res.type("application/json");
            String numeroCuenta = req.params(":numeroCuenta");
            String pin = req.params(":pin");

            boolean esValido = transaccionesControlador.validarPinCuenta(numeroCuenta, pin);

            if (esValido) {
                return gson.toJson(new StandardResponse(StatusResponse.SUCCESS, "Cuenta y PIN válidos."));
            } else {
                return gson.toJson(new StandardResponse(StatusResponse.ERROR, "Número de cuenta o PIN incorrectos."));
            }
        });

        // Endpoint para enviar palabra de verificación
        // Enviar Palabra de Verificación
        get("/transacciones/enviarPalabraVerificacion/:numeroCuenta", (req, res) -> {
            res.type("application/json");
            String numeroCuenta = req.params(":numeroCuenta");

            String palabraGenerada = transaccionesControlador.enviarPalabraVerificacion(numeroCuenta);

            if (palabraGenerada != null) {
                return gson.toJson(new StandardResponse(StatusResponse.SUCCESS, "Palabra clave enviada."));
            } else {
                return gson.toJson(new StandardResponse(StatusResponse.ERROR, "Error al enviar la palabra clave."));
            }
        });

        // Validar Palabra Clave
        get("/transacciones/validarPalabraClave/:numeroCuenta/:palabraIngresada", (req, res) -> {
            res.type("application/json");
            String palabraIngresada = req.params(":palabraIngresada");

            boolean esValida = transaccionesControlador.validarPalabraClave(palabraIngresada);

            if (esValida) {
                return gson.toJson(new StandardResponse(StatusResponse.SUCCESS, "Palabra clave válida."));
            } else {
                return gson.toJson(new StandardResponse(StatusResponse.ERROR, "La palabra ingresada no coincide."));
            }
        });

        // Endpoint para realizar retiro en colones
        // Realizar Retiro en Colones
        post("/transacciones/retiroColones", (req, res) -> {
            res.type("application/json");
            RetiroRequest request = gson.fromJson(req.body(), RetiroRequest.class);
            String resultado = transaccionesControlador.realizarRetiroEnColones(
                    request.getNumeroCuenta(),
                    request.getPin(),
                    request.getPalabraIngresada(),
                    request.getMontoRetiro());
            if (resultado.startsWith("Error")) {
                return gson.toJson(new StandardResponse(StatusResponse.ERROR, resultado));
            } else {
                return gson.toJson(new StandardResponse(StatusResponse.SUCCESS, resultado));
            }
        });

        // Retiro en dolares

        // Realizar Retiro en Dólares
        post("/transacciones/retiroDolares", (req, res) -> {
            res.type("application/json");
            RetiroRequest request = gson.fromJson(req.body(), RetiroRequest.class);
            String resultado = transaccionesControlador.realizarRetiroEnDolares(
                    request.getNumeroCuenta(),
                    request.getPin(),
                    request.getPalabraIngresada(),
                    request.getMontoRetiro());
            if (resultado.startsWith("Error")) {
                return gson.toJson(new StandardResponse(StatusResponse.ERROR, resultado));
            } else {
                return gson.toJson(new StandardResponse(StatusResponse.SUCCESS, resultado));
            }
        });

        // Transferencia entre cuentas del mismo duenio

        // Validar Cuenta Destino y que sea del mismo dueño
        get("/cuentas/validarCuentaDestino/:numeroCuentaOrigen/:numeroCuentaDestino", (req, res) -> {
            res.type("application/json");
            String numeroCuentaOrigen = req.params(":numeroCuentaOrigen");
            String numeroCuentaDestino = req.params(":numeroCuentaDestino");

            String resultado = transaccionesControlador.validarCuentaDestino(numeroCuentaOrigen, numeroCuentaDestino);

            if (resultado.equals("SUCCESS")) {
                return gson.toJson(
                        new StandardResponse(StatusResponse.SUCCESS, "Cuenta destino válida y del mismo dueño."));
            } else {
                return gson.toJson(new StandardResponse(StatusResponse.ERROR, resultado));
            }
        });

        // Realizar Transferencia
        post("/transacciones/transferencia", (req, res) -> {
            res.type("application/json");
            TransferenciaRequest request = gson.fromJson(req.body(), TransferenciaRequest.class);
            String resultado = transaccionesControlador.realizarTransferencia(
                    request.getNumeroCuentaOrigen(),
                    request.getPinCuentaOrigen(),
                    request.getNumeroCuentaDestino(),
                    request.getMontoTransferencia());
            if (resultado.startsWith("Error")) {
                return gson.toJson(new StandardResponse(StatusResponse.ERROR, resultado));
            } else {
                return gson.toJson(new StandardResponse(StatusResponse.SUCCESS, resultado));
            }
        });

        // Consultas y reportes

        // 1. Consultar transacciones de un cliente

        // Endpoint para consultar transacciones de una cuenta
        post("/cuentas/transacciones", (req, res) -> {
            res.type("application/json");
            ConsultaTransaccionesRequest request = gson.fromJson(req.body(), ConsultaTransaccionesRequest.class);

            String numeroCuenta = request.getNumeroCuenta();
            String pin = request.getPin();
            String palabraIngresada = request.getPalabraIngresada();

            // Validar PIN y palabra clave
            boolean esCuentaValida = transaccionesControlador.validarPinCuenta(numeroCuenta, pin);
            if (!esCuentaValida) {
                return gson.toJson(new StandardResponse(StatusResponse.ERROR, "Número de cuenta o PIN incorrectos."));
            }

            boolean esPalabraClaveValida = transaccionesControlador.validarPalabraClave(palabraIngresada);
            if (!esPalabraClaveValida) {
                return gson.toJson(new StandardResponse(StatusResponse.ERROR, "La palabra ingresada no coincide."));
            }

            // Obtener las transacciones
            List<Transaccion> transacciones = transaccionesControlador.obtenerTransaccionesPorCuenta(numeroCuenta);

            // Preparar datos de respuesta
            List<Map<String, Object>> transaccionesData = new ArrayList<>();
            for (Transaccion transaccion : transacciones) {
                Map<String, Object> transaccionData = new HashMap<>();
                transaccionData.put("tipo", transaccion.getTipo());
                transaccionData.put("monto", transaccion.getMonto());
                transaccionData.put("fecha", transaccion.getFecha().toString());
                transaccionData.put("comision", transaccion.isComision());
                transaccionesData.add(transaccionData);
            }

            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("transacciones", transaccionesData);

            return gson.toJson(new StandardResponse(StatusResponse.SUCCESS, "Consulta de transacciones exitosa.", respuesta));
        });


        // 2. Consultar tipo de cambio de compra
        // Endpoint para obtener el tipo de cambio de compra
        get("/tipoCambio/compra", (req, res) -> {
            res.type("application/json");
            TipoDeCambioBCCR.obtenerTipoCambioHoy(); // Asegura que se obtengan los datos más recientes
            double tipoCambioCompra = TipoDeCambioBCCR.obtenerTipoCambioCompra();
            String fecha = TipoDeCambioBCCR.obtenerFechaTipoCambioHoy();
            TipoCambioData data = new TipoCambioData(tipoCambioCompra, fecha);
            return gson.toJson(new StandardResponse(StatusResponse.SUCCESS, gson.toJsonTree(data)));
        });

        // Endpoint para obtener el tipo de cambio de venta
        get("/tipoCambio/venta", (req, res) -> {
            res.type("application/json");
            TipoDeCambioBCCR.obtenerTipoCambioHoy(); // Asegura que se obtengan los datos más recientes
            double tipoCambioVenta = TipoDeCambioBCCR.obtenerTipoCambioVenta();
            String fecha = TipoDeCambioBCCR.obtenerFechaTipoCambioHoy();
            TipoCambioData data = new TipoCambioData(tipoCambioVenta, fecha);
            return gson.toJson(new StandardResponse(StatusResponse.SUCCESS, gson.toJsonTree(data)));
        });

        // Endpoint para obtener el saldo en dolares
        // Consultar Saldo Actual en Dólares
        get("/cuentas/consultarSaldoDolares/:numeroCuenta/:pin", (req, res) -> {
            res.type("application/json");
            String numeroCuenta = req.params(":numeroCuenta");
            String pin = req.params(":pin");

            String resultado = cuentaControlador.consultarSaldoDivisaExtranjera(numeroCuenta, pin);

            if (resultado == null) {
                return gson.toJson(new StandardResponse(StatusResponse.ERROR,
                        "Error al consultar el saldo en divisa extrangera."));
            } else {
                return gson.toJson(new StandardResponse(StatusResponse.SUCCESS, "Consulta exitosa.", resultado));
            }
        });

        // Endpoint para consultar el estado de cuenta sin transacciones
        get("/cuentas/estadoCuenta/:numeroCuenta/:pin", (req, res) -> {
            res.type("application/json");
            String numeroCuenta = req.params(":numeroCuenta");
            String pin = req.params(":pin");

            Map<String, Object> resultado = cuentaControlador.estadoCuenta(numeroCuenta, pin);

            if (resultado.containsKey("error")) {
                return gson.toJson(new StandardResponse(StatusResponse.ERROR, (String) resultado.get("error")));
            } else {
                return gson.toJson(new StandardResponse(StatusResponse.SUCCESS, "Consulta exitosa.", resultado));
            }
        });

        // Endpoint para consultar estatus de una cuenta
        get("/cuentas/consultarEstatus/:numeroCuenta", (req, res) -> {
            res.type("application/json");
            String numeroCuenta = req.params(":numeroCuenta");

            String resultado = cuentaControlador.consultarEstatusCuenta(numeroCuenta);

            if (resultado.startsWith("Error:")) {
                return gson.toJson(new StandardResponse(StatusResponse.ERROR, resultado));
            } else {
                return gson.toJson(new StandardResponse(StatusResponse.SUCCESS, resultado));
            }
        });

        // Endpoint para consultas las cuentas de un usuario
        // App.java
        get("/clientes/cuentas/:identificacion", (req, res) -> {
            res.type("application/json");
            long identificacion = Long.parseLong(req.params(":identificacion"));
            Optional<Cliente> clienteOpt = clienteControlador.buscarClientePorIdentificacion(identificacion);

            if (!clienteOpt.isPresent()) {
                return gson.toJson(new StandardResponse(StatusResponse.ERROR, "Cliente no registrado en el sistema."));
            }

            Cliente cliente = clienteOpt.get();
            List<Cuenta> cuentasCliente = cliente.getMisCuentas();

            // Datos del cliente
            Map<String, Object> datosCliente = new HashMap<>();
            datosCliente.put("nombre", cliente.getNombre());
            datosCliente.put("identificacion", cliente.getIdentificacion());
            datosCliente.put("numTelefono", cliente.getNumTelefono());
            datosCliente.put("correoElectronico", cliente.getCorreoElectronico());

            // Datos de cuentas
            List<Map<String, Object>> cuentasData = new ArrayList<>();
            for (Cuenta cuenta : cuentasCliente) {
                Map<String, Object> cuentaData = new HashMap<>();
                cuentaData.put("numeroCuenta", cuenta.getCodigo());
                cuentaData.put("saldo", cuenta.getSaldo());
                cuentasData.add(cuentaData);
            }

            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("cliente", datosCliente);
            respuesta.put("cuentas", cuentasData);

            return gson.toJson(new StandardResponse(StatusResponse.SUCCESS, "Consulta de cuentas exitosa.", respuesta));
        });

        // Endpoint para listar todos los clientes registrados ordenados por nombre
        get("/clientes/listar", (req, res) -> {
            res.type("application/json");
            List<Cliente> clientesOrdenados = clienteControlador.obtenerClientesRegistrados();
            ArrayList<ClienteData> clientesData = new ArrayList<>();
            for (Cliente cliente : clientesOrdenados) {
                clientesData.add(new ClienteData(cliente.getNombre(), cliente.getIdentificacion(),
                        cliente.getNumTelefono(), cliente.getCorreoElectronico(), cliente.getTipo()));
            }
            return gson.toJson(
                    new StandardResponse(StatusResponse.SUCCESS, "Listado de clientes exitoso.", clientesData));
        });

        // Endpoint para listar todas las cuentas del sistema ordenadas por saldo
        // ascendente
        get("/cuentas/listar", (req, res) -> {
            res.type("application/json");
            // Obtener la lista de cuentas del controlador
            List<Cuenta> cuentas = cuentaControlador.getCuentas();
            // Ordenar las cuentas por saldo ascendente
            cuentas.sort(Comparator.comparingDouble(Cuenta::getSaldo));

            // Convertir la lista de cuentas a CuentasData
            ArrayList<CuentasData> cuentasData = new ArrayList<>();
            for (Cuenta cuenta : cuentas) {
                cuentasData.add(new CuentasData(cuenta.getCodigo(), cuenta.getEstatus(),
                        cuenta.getMiCliente().getNombre(), cuenta.getSaldo()));
            }
            return gson
                    .toJson(new StandardResponse(StatusResponse.SUCCESS, "Listado de cuentas exitoso.", cuentasData));
        });
    }

    public static class CuentasData {
        String numeroCuenta;
        String estatus;
        String nombreUsuario;
        double saldo;

        public CuentasData(String numeroCuenta, String estatus, String nombreUsuario, double saldo) {
            this.numeroCuenta = numeroCuenta;
            this.estatus = estatus;
            this.nombreUsuario = nombreUsuario;
            this.saldo = saldo;
        }
    }

    public static class ClienteData {
        String nombre;
        long identificacion;
        String numTelefono;
        String correoElectronico;
        String tipo;

        private ClienteData(String nombre, long identificacion, String numTelefono, String correoElectronico,
                String tipo) {
            this.nombre = nombre;
            this.identificacion = identificacion;
            this.numTelefono = numTelefono;
            this.correoElectronico = correoElectronico;
            this.tipo = tipo;
        }
    }

    // Clase para datos de cliente físico
    public static class ClienteFisicoData {
        String nombre;
        long identificacion;
        String numTelefono;
        String correoElectronico;
        String fechaNacimiento; // Formato YYYY-MM-DD
        int maxCuentas;
        String tipoCuenta;
    }

    // Clase para datos de cliente jurídico
    public static class ClienteJuridicoData {
        String nombre;
        long identificacion;
        String numTelefono;
        String correoElectronico;
        String tipoNegocio;
        String razonSocial;
    }

    // Clase para actualizar teléfono
    private static class ActualizarTelefonoData {
        long identificacion;
        String nuevoTelefono;
    }

    // Clase para actualizar correo electrónico
    private static class ActualizarCorreoData {
        long identificacion;
        String nuevoCorreo;
    }

    public class CuentaData {
        long identificacion;
        double saldoInicial;
        String pin;
    }

    public class CambioPinData {
        String numeroCuenta;
        String pinActual;
        String nuevoPin;
    }

    public static class TipoCambioData {
        double tipoCambio;
        String fecha;

        public TipoCambioData(double tipoCambio, String fecha) {
            this.tipoCambio = tipoCambio;
            this.fecha = fecha;
        }
    }

    public static class StandardResponse {
        private StatusResponse status;
        private String message;
        private JsonElement datos; // Usaremos siempre `data`

        // Constructor que acepta status y data
        public StandardResponse(StatusResponse status, JsonElement data) {
            this.status = status;
            this.datos = data;
        }

        // Constructor que acepta status y message
        public StandardResponse(StatusResponse status, String message) {
            this.status = status;
            this.message = message;
        }

        // Constructor que acepta status, message y data (en forma de cualquier objeto)
        public StandardResponse(StatusResponse status, String message, Object data) {
            this.status = status;
            this.message = message;
            this.datos = new Gson().toJsonTree(data); // Convertimos cualquier objeto a `JsonElement`
        }

        // Getters y setters
        public StatusResponse getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }

        public JsonElement getData() {
            return datos;
        }

        public void setStatus(StatusResponse status) {
            this.status = status;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public void setData(JsonElement data) {
            this.datos = data;
        }
    }

    // Enumeración para el estado de la respuesta
    public enum StatusResponse {
        SUCCESS, ERROR
    }

    public class ConsultaTransaccionesRequest {
        private String numeroCuenta;
        private String pin;
        private String palabraIngresada;
    
        // Getters y setters
        public String getNumeroCuenta() {
            return numeroCuenta;
        }
    
        public void setNumeroCuenta(String numeroCuenta) {
            this.numeroCuenta = numeroCuenta;
        }
    
        public String getPin() {
            return pin;
        }
    
        public void setPin(String pin) {
            this.pin = pin;
        }
    
        public String getPalabraIngresada() {
            return palabraIngresada;
        }
    
        public void setPalabraIngresada(String palabraIngresada) {
            this.palabraIngresada = palabraIngresada;
        }
    }
    
    public class TransaccionRequest {
        private String numeroCuenta;
        private int monto;

        public String getNumeroCuenta() {
            return numeroCuenta;
        }

        public int getMonto() {
            return monto;
        }
    }

    public class RetiroRequest {
        private String numeroCuenta;
        private String pin;
        private String palabraIngresada;
        private int montoRetiro;

        public String getNumeroCuenta() {
            return numeroCuenta;
        }

        public String getPin() {
            return pin;
        }

        public String getPalabraIngresada() {
            return palabraIngresada;
        }

        public int getMontoRetiro() {
            return montoRetiro;
        }
    }

    public class TransferenciaRequest {
        private String numeroCuentaOrigen;
        private String pinCuentaOrigen;
        private String numeroCuentaDestino;
        private double montoTransferencia;

        public String getNumeroCuentaOrigen() {
            return numeroCuentaOrigen;
        }

        public String getPinCuentaOrigen() {
            return pinCuentaOrigen;
        }

        public String getNumeroCuentaDestino() {
            return numeroCuentaDestino;
        }

        public double getMontoTransferencia() {
            return montoTransferencia;
        }
    }

    private static void enableCORS(final String origin, final String methods, final String headers) {
        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", origin);
            response.header("Access-Control-Request-Method", methods);
            response.header("Access-Control-Allow-Headers", headers);
            // Nota: Es posible que necesites manejar las solicitudes OPTIONS para preflight
        });
    }

}
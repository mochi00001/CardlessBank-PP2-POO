package controladores;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import modelos.Cliente;
import modelos.ClienteFisico;
import modelos.ClienteJuridico;
import modelos.Cuenta;
import servicios.PersistenciaDatos;

public class ClienteControlador {

    private List<Cliente> clientes;
    private CuentaControlador cuentaControlador;
    private TransaccionesControlador transaccionesControlador;

    public ClienteControlador() {
        this.clientes = new ArrayList<>();
        this.cuentaControlador = new CuentaControlador(clientes);
        this.transaccionesControlador = new TransaccionesControlador(clientes);
    }

    public CuentaControlador getCuentaControlador() {
        return cuentaControlador;
    }

    public TransaccionesControlador getTransaccionesControlador() {
        return transaccionesControlador;
    }

    public boolean crearClienteFisico(String nombre, long identificacion, String numTelefono, String correoElectronico,
            LocalDate fechaNacimiento, int maxCuentas) {
        if (buscarClientePorIdentificacion(identificacion).isEmpty()) {
            Cliente nuevoCliente = new ClienteFisico(nombre, identificacion, numTelefono, correoElectronico,
                    fechaNacimiento, maxCuentas);
            clientes.add(nuevoCliente);
            PersistenciaDatos.guardarDatos(clientes);
            return true;
        } else {
            return false;
        }
    }

    public boolean crearClienteJuridico(String nombre, long identificacion, String numTelefono,
            String correoElectronico, String tipoNegocio, String razonSocial) {
        if (buscarClientePorIdentificacion(identificacion).isEmpty()) {
            Cliente nuevoCliente = new ClienteJuridico(nombre, identificacion, numTelefono, correoElectronico,
                    tipoNegocio, razonSocial);
            clientes.add(nuevoCliente);
            PersistenciaDatos.guardarDatos(clientes);
            return true;
        } else {
            return false;
        }
    }

    public boolean agregarCuentaACliente(long identificacion, Cuenta nuevaCuenta) {
        Optional<Cliente> clienteOpt = buscarClientePorIdentificacion(identificacion);

        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();
            cliente.agregarCuenta(nuevaCuenta);
            PersistenciaDatos.guardarDatos(clientes);
            return true;
        }
        return false;
    }

    public Optional<Cliente> buscarClientePorIdentificacion(long identificacion) {
        for (Cliente cliente : clientes) {
            if (cliente.getIdentificacion() == identificacion) {
                return Optional.of(cliente);
            }
        }
        return Optional.empty();
    }

    public List<Cliente> obtenerClientes() {
        return clientes;
    }

    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
        this.cuentaControlador.setClientes(clientes);
        this.transaccionesControlador.setClientes(clientes);
    }

    public boolean actualizarTelefono(long identificacion, String nuevoTelefono) {
        Optional<Cliente> clienteOpt = buscarClientePorIdentificacion(identificacion);
        if (clienteOpt.isPresent()) {
            try {
                Cliente cliente = clienteOpt.get();
                cliente.setNumTelefono(nuevoTelefono); // Esto puede lanzar una excepción
                PersistenciaDatos.guardarDatos(clientes);
                return true;
            } catch (IllegalArgumentException e) {
                System.err.println("Error al actualizar el número de teléfono: " + e.getMessage());
                return false; // Podemos devolver false o manejar de otra forma.
            }
        } else {
            return false;
        }
    }

    public boolean actualizarCorreo(long identificacion, String nuevoCorreo) {
        Optional<Cliente> clienteOpt = buscarClientePorIdentificacion(identificacion);
        if (clienteOpt.isPresent()) {
            try {
                Cliente cliente = clienteOpt.get();
                cliente.setCorreoElectronico(nuevoCorreo); // Esto puede lanzar una excepción
                PersistenciaDatos.guardarDatos(clientes);
                return true;
            } catch (IllegalArgumentException e) {
                System.err.println("Error al actualizar el correo electrónico: " + e.getMessage());
                return false; // Podemos devolver false o manejar de otra forma.
            }
        } else {
            return false;
        }
    }

    /**
     * Obtiene la lista de clientes registrados ordenados ascendentemente por
     * nombre.
     * 
     * @return Lista ordenada de clientes.
     */
    public List<Cliente> obtenerClientesRegistrados() {
        List<Cliente> clientesOrdenados = new ArrayList<>(clientes);
        clientesOrdenados.sort(Comparator.comparing(Cliente::getNombre, String.CASE_INSENSITIVE_ORDER));
        return clientesOrdenados;
    }

}
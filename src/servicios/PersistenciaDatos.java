package servicios;

import modelos.Cliente;
import modelos.Cuenta;
import modelos.Transaccion;
import java.util.List;
import java.io.File;
import java.util.ArrayList;


import controladores.ClienteControlador;


public class PersistenciaDatos {
    private static final String RUTA_CUENTAS_XML = "src/data/cuentas.xml";
    private static final String RUTA_CLIENTES_XML = "src/data/clientes.xml"; 
    private static final String RUTA_TRANSACCIONES_XML = "src/data/transacciones.xml";

    // Métodos para manejar cuentas
    // Método para guardar cuentas
    public static void guardarCuentas(List<Cuenta> cuentas) {
        try {
            File archivoCuentas = new File(RUTA_CUENTAS_XML);
            if (!archivoCuentas.getParentFile().exists()) {
                archivoCuentas.getParentFile().mkdirs();
            }

            XMLUtils.escribirCuentasAArchivoXML(cuentas, RUTA_CUENTAS_XML);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Método para cargar cuentas
    public static List<Cuenta> cargarCuentas(ClienteControlador clienteControlador) {
        try {
            List<Cuenta> cuentas = XMLUtils.leerCuentasDesdeArchivoXML(RUTA_CUENTAS_XML, clienteControlador);
            return cuentas != null ? cuentas : new ArrayList<>(); 
        } catch (Exception e) {
            System.err.println("Error al cargar las cuentas desde el archivo XML: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    

    // Métodos para manejar clientes
    // Método para guardar clientes
    public static void guardarClientes(List<Cliente> clientes) {
        try {
            File archivoClientes = new File(RUTA_CLIENTES_XML);
            if (!archivoClientes.getParentFile().exists()) {
                archivoClientes.getParentFile().mkdirs();
            }

            XMLUtils.escribirClientesAArchivoXML(clientes, RUTA_CLIENTES_XML);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Método para cargar clientes
    public static List<Cliente> cargarClientes() {
        try {
            List<Cliente> clientes = XMLUtils.leerClientesDesdeArchivoXML(RUTA_CLIENTES_XML);
            if (clientes == null || clientes.isEmpty()) {
                System.out.println("No se encontraron clientes en el archivo XML.");
            } else {
                System.out.println("Clientes cargados correctamente: " + clientes.size() + " clientes encontrados.");
            }
            return clientes;
        } catch (Exception e) {
            System.err.println("Error al cargar los clientes desde el archivo XML: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Métodos para manejar transacciones
    // Método para guardar transacciones
    public static void guardarTransacciones(List<Transaccion> transacciones) {
        try {
            File archivoTransacciones = new File(RUTA_TRANSACCIONES_XML);
            if (!archivoTransacciones.getParentFile().exists()) {
                archivoTransacciones.getParentFile().mkdirs();
            }

            XMLUtils.escribirTransaccionesAArchivoXML(transacciones, RUTA_TRANSACCIONES_XML);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para cargar transacciones    
    public static List<Transaccion> cargarTransacciones() {
        List<Transaccion> transacciones = new ArrayList<>();
        try {
            File archivoTransacciones = new File(RUTA_TRANSACCIONES_XML);
            if (!archivoTransacciones.exists()) {
                return transacciones;
            }
    
            transacciones = XMLUtils.leerTransaccionesDesdeArchivoXML(RUTA_TRANSACCIONES_XML);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transacciones;
    }
}      
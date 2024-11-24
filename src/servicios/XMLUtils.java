package servicios;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import controladores.ClienteControlador;
import modelos.Cliente;
import modelos.ClienteFisico;
import modelos.ClienteJuridico;
import modelos.Cuenta;
import modelos.Transaccion;

public class XMLUtils {

    // Método para escribir clientes a un archivo XML
    public static void escribirClientesAArchivoXML(List<Cliente> listaClientes, String rutaArchivo) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            Element rootElement = doc.createElement("clientes");
            doc.appendChild(rootElement);

            for (Cliente cliente : listaClientes) {
                Element clienteElement = doc.createElement("cliente");

                // Atributo tipo para diferenciar entre fisico y juridico
                if (cliente instanceof ClienteFisico) {
                    clienteElement.setAttribute("tipo", "fisico");
                    ClienteFisico clienteFisico = (ClienteFisico) cliente;
                    agregarElemento(doc, clienteElement, "fechaNacimiento",
                            clienteFisico.getFechaNacimiento().toString());
                    agregarElemento(doc, clienteElement, "maxCuentas", String.valueOf(clienteFisico.getMaxCuentas()));
                } else if (cliente instanceof ClienteJuridico) {
                    clienteElement.setAttribute("tipo", "juridico");
                    ClienteJuridico clienteJuridico = (ClienteJuridico) cliente;
                    agregarElemento(doc, clienteElement, "tipoNegocio", clienteJuridico.getTipoNegocio());
                    agregarElemento(doc, clienteElement, "razonSocial", clienteJuridico.getRazonSocial());
                }

                agregarElemento(doc, clienteElement, "nombre", cliente.getNombre());
                agregarElemento(doc, clienteElement, "identificacion", String.valueOf(cliente.getIdentificacion()));
                agregarElemento(doc, clienteElement, "numTelefono", String.valueOf(cliente.getNumTelefono()));
                agregarElemento(doc, clienteElement, "correoElectronico", cliente.getCorreoElectronico());

                rootElement.appendChild(clienteElement);
            }

            // Escribir el contenido en el archivo XML
            try (FileOutputStream fos = new FileOutputStream(new File(rutaArchivo))) {
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(fos);
                transformer.transform(source, result);
            }
        } catch (ParserConfigurationException | TransformerException | IOException e) {
            e.printStackTrace();
        }
    }

    // Método auxiliar para agregar un elemento a un cliente
    private static void agregarElemento(Document doc, Element clienteElement, String nombre, String valor) {
        Element element = doc.createElement(nombre);
        element.appendChild(doc.createTextNode(valor));
        clienteElement.appendChild(element);
    }

    // Método para leer clientes desde un archivo XML
    public static List<Cliente> leerClientesDesdeArchivoXML(String rutaArchivo) {
        List<Cliente> listaClientes = new ArrayList<>();

        try {
            File inputFile = new File(rutaArchivo);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("cliente");

            for (int i = 0; i < nList.getLength(); i++) {
                Node nodo = nList.item(i);
                if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                    Element elementoCliente = (Element) nodo;
                    String tipo = elementoCliente.getAttribute("tipo");

                    String nombre = elementoCliente.getElementsByTagName("nombre").item(0).getTextContent();
                    long identificacion = Long
                            .parseLong(elementoCliente.getElementsByTagName("identificacion").item(0).getTextContent());
                    String numTelefono = elementoCliente.getElementsByTagName("numTelefono").item(0).getTextContent();
                    String correoElectronico = elementoCliente.getElementsByTagName("correoElectronico").item(0)
                            .getTextContent();

                    // Verifica el tipo y crea el objeto correspondiente
                    if (tipo.equals("fisico")) {
                        LocalDate fechaNacimiento = LocalDate.parse(
                                elementoCliente.getElementsByTagName("fechaNacimiento").item(0).getTextContent());
                        int maxCuentas = Integer
                                .parseInt(elementoCliente.getElementsByTagName("maxCuentas").item(0).getTextContent());
                        Cliente cliente = new ClienteFisico(nombre, identificacion, numTelefono, correoElectronico,
                                fechaNacimiento, maxCuentas);
                        listaClientes.add(cliente);
                    } else if (tipo.equals("juridico")) {
                        String tipoNegocio = elementoCliente.getElementsByTagName("tipoNegocio").item(0)
                                .getTextContent();
                        String razonSocial = elementoCliente.getElementsByTagName("razonSocial").item(0)
                                .getTextContent();
                        Cliente cliente = new ClienteJuridico(nombre, identificacion, numTelefono, correoElectronico,
                                tipoNegocio, razonSocial);
                        listaClientes.add(cliente);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaClientes;
    }

    // Método para escribir cuentas a un archivo XML
    public static void escribirCuentasAArchivoXML(List<Cuenta> listaCuentas, String rutaArchivo) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            Element rootElement = doc.createElement("cuentas");
            doc.appendChild(rootElement);

            for (Cuenta cuenta : listaCuentas) {
                Element cuentaElement = doc.createElement("cuenta");
                rootElement.appendChild(cuentaElement);

                Element codigo = doc.createElement("codigo");
                codigo.appendChild(doc.createTextNode(cuenta.getCodigo()));
                cuentaElement.appendChild(codigo);

                Element estatus = doc.createElement("estatus");
                estatus.appendChild(doc.createTextNode(cuenta.getEstatus()));
                cuentaElement.appendChild(estatus);

                Element saldo = doc.createElement("saldo");
                saldo.appendChild(doc.createTextNode(cuenta.getSaldoFormateado()));
                cuentaElement.appendChild(saldo);

                Element identificacion = doc.createElement("identificacion");
                identificacion
                        .appendChild(doc.createTextNode(String.valueOf(cuenta.getMiCliente().getIdentificacion())));
                cuentaElement.appendChild(identificacion);

                Element pin = doc.createElement("pin");
                String pinEncriptado = "";
                try {
                    pinEncriptado = CryptoUtils.encriptar(cuenta.getPin());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                pin.appendChild(doc.createTextNode(pinEncriptado));
                cuentaElement.appendChild(pin);
            }

            // Escribir el contenido en el archivo XML
            try (FileOutputStream fos = new FileOutputStream(new File(rutaArchivo))) {
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(fos);
                transformer.transform(source, result);
            }
        } catch (ParserConfigurationException | TransformerException | IOException e) {
            e.printStackTrace();
        }
    }

    // Método para leer cuentas desde un archivo XML
    public static List<Cuenta> leerCuentasDesdeArchivoXML(String rutaArchivo, ClienteControlador clienteControlador) {
        List<Cuenta> listaCuentas = new ArrayList<>();

        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new File(rutaArchivo));

            NodeList listaNodosCuenta = doc.getElementsByTagName("cuenta");

            for (int i = 0; i < listaNodosCuenta.getLength(); i++) {
                Node nodoCuenta = listaNodosCuenta.item(i);

                if (nodoCuenta.getNodeType() == Node.ELEMENT_NODE) {
                    Element elementoCuenta = (Element) nodoCuenta;

                    // Obtención de valores de acuerdo a la estructura XML dada
                    String codigo = getTagValue("codigo", elementoCuenta);
                    String estatus = getTagValue("estatus", elementoCuenta);
                    String saldoFormateado = getTagValue("saldo", elementoCuenta).replace(",", ".");
                    double saldo = saldoFormateado.isEmpty() ? 0.0 : Double.parseDouble(saldoFormateado);
                    String pinEncriptado = getTagValue("pin", elementoCuenta);
                    String pin = "";
                    try {
                        pin = CryptoUtils.desencriptar(pinEncriptado);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    String identificacionStr = getTagValue("identificacion", elementoCuenta);
                    int identificacion = identificacionStr.isEmpty() ? -1 : Integer.parseInt(identificacionStr);

                    // Buscar el cliente asociado
                    Cliente cliente = clienteControlador.buscarClientePorIdentificacion(identificacion).orElse(null);
                    if (cliente == null) {
                        System.err.println("Advertencia: Cliente con identificación " + identificacion
                                + " no encontrado. La cuenta no se asociará a ningún cliente.");
                    }

                    // Crear la cuenta con los valores obtenidos
                    Cuenta cuenta = new Cuenta(saldo, codigo, pin, cliente, estatus);
                    listaCuentas.add(cuenta);

                    if (cliente != null) {
                        cliente.getMisCuentas().add(cuenta);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error al leer el archivo XML: " + e.getMessage());
            e.printStackTrace();
        }

        return listaCuentas;
    }

    // Método auxiliar para obtener el valor de una etiqueta de manera segura
    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag);
        if (nodeList != null && nodeList.getLength() > 0) {
            Node node = nodeList.item(0);
            if (node != null) {
                return node.getTextContent().trim();
            }
        }
        return "";
    }

    // Método para escribir transacciones a un archivo XML
    public static void escribirTransaccionesAArchivoXML(List<Transaccion> transacciones, String rutaArchivo) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            Element rootElement = doc.createElement("transacciones");
            doc.appendChild(rootElement);

            for (Transaccion transaccion : transacciones) {
                Element transaccionElement = doc.createElement("transaccion");

                Element fechaElement = doc.createElement("fecha");
                fechaElement.appendChild(doc.createTextNode(transaccion.getFecha().toString()));
                transaccionElement.appendChild(fechaElement);

                Element tipoElement = doc.createElement("tipo");
                tipoElement.appendChild(doc.createTextNode(transaccion.getTipo()));
                transaccionElement.appendChild(tipoElement);

                Element montoElement = doc.createElement("monto");
                montoElement.appendChild(doc.createTextNode(String.valueOf(transaccion.getMonto())));
                transaccionElement.appendChild(montoElement);

                Element comisionElement = doc.createElement("comision");
                comisionElement.appendChild(doc.createTextNode(String.valueOf(transaccion.isComision())));
                transaccionElement.appendChild(comisionElement);

                Element numeroCuentaElement = doc.createElement("numeroCuenta");
                numeroCuentaElement.appendChild(doc.createTextNode(transaccion.getCodigoCuenta()));
                transaccionElement.appendChild(numeroCuentaElement);

                rootElement.appendChild(transaccionElement);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(rutaArchivo));
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para leer transacciones desde un archivo XML
    public static List<Transaccion> leerTransaccionesDesdeArchivoXML(String rutaArchivo) {
        List<Transaccion> transacciones = new ArrayList<>();
        try {
            File archivoXML = new File(rutaArchivo);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(archivoXML);
            doc.getDocumentElement().normalize();

            NodeList transaccionNodes = doc.getElementsByTagName("transaccion");

            for (int i = 0; i < transaccionNodes.getLength(); i++) {
                Node transaccionNode = transaccionNodes.item(i);

                if (transaccionNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element transaccionElement = (Element) transaccionNode;

                    // Leer los datos del XML
                    double monto = Double
                            .parseDouble(transaccionElement.getElementsByTagName("monto").item(0).getTextContent());
                    String tipo = transaccionElement.getElementsByTagName("tipo").item(0).getTextContent();
                    String numeroCuenta = transaccionElement.getElementsByTagName("numeroCuenta").item(0)
                            .getTextContent();
                    String fechaStr = transaccionElement.getElementsByTagName("fecha").item(0).getTextContent();
                    LocalDate fecha = LocalDate.parse(fechaStr); // Asegúrate de que el formato sea compatible

                    int cantidadTransacciones = 0;

                    Transaccion transaccion = new Transaccion(tipo, monto, numeroCuenta, cantidadTransacciones);
                    transaccion.setFecha(fecha);

                    transacciones.add(transaccion);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transacciones;
    }

}
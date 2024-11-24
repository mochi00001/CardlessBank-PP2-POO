package servicios;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class TipoDeCambioBCCR {
    private static final String BCCR_URL = "https://gee.bccr.fi.cr/indicadoreseconomicos/Cuadros/frmVerCatCuadro.aspx?idioma=1&CodCuadro=400";

    private static String tipoCambioCompra;
    private static String tipoCambioVenta;
    private static LocalDate fechaTipoCambio; // Nuevo atributo para almacenar la fecha

    public static void obtenerTipoCambioHoy() {
        try {
            Document document = Jsoup.connect(BCCR_URL).get();
            fechaTipoCambio = LocalDate.now(); // Almacena la fecha actual
            String fechaHoyStr = fechaTipoCambio.format(DateTimeFormatter.ofPattern("d MMM yyyy")).replace(".", "")
                    .toLowerCase();
            Elements celdas = document.select("td.celda400");

            int indexFecha = -1;

            for (int i = 0; i < celdas.size(); i++) {
                String textoCelda = celdas.get(i).text().toLowerCase();
                if (textoCelda.equals(fechaHoyStr)) {
                    indexFecha = i;
                    break;
                }
            }

            if (indexFecha != -1) {
                int indexCompra = indexFecha + indexFecha + 1;
                if (indexCompra < celdas.size()) {
                    tipoCambioCompra = celdas.get(indexCompra).text();
                }
                int indexVenta = indexFecha + indexFecha + indexFecha + 2;
                if (indexVenta < celdas.size()) {
                    tipoCambioVenta = celdas.get(indexVenta).text();
                }
            }
            tipoCambioCompra = tipoCambioCompra.replace(",", ".");
            tipoCambioVenta = tipoCambioVenta.replace(",", ".");
            // System.out.println("Tipo de cambio cargados. \nVenta: " + tipoCambioVenta +
            // "\nCompra: " + tipoCambioCompra + "\nFecha: " + fechaTipoCambio);
        } catch (IOException e) {
            System.err.println("Error al conectarse a la página: " + e.getMessage());
            tipoCambioCompra = "0.00";
            tipoCambioVenta = "0.00";
            fechaTipoCambio = LocalDate.now();
        }
    }

    public static double getTipoCambioCompra() {
        return Double.parseDouble(tipoCambioCompra);
    }

    public static double getTipoCambioVenta() {
        return Double.parseDouble(tipoCambioVenta);
    }

    public static String getFechaTipoCambioHoy() {
        if (fechaTipoCambio != null) {
            return fechaTipoCambio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")); // Devuelve la fecha en formato
                                                                                      // "DD/MM/AAAA"
        } else {
            return "Fecha no disponible";
        }
    }
}

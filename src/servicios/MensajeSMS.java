package servicios;

import java.security.SecureRandom;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer;
import se.akerfeldt.okhttp.signpost.SigningInterceptor;

public class MensajeSMS {
    // Credenciales
    private static final String KEY = "OCA4oownD_ZLeK4_DtTtjlP_";
    private static final String SECRET = "Uht-C1iR297CzrLL*(c!bwFcS!H(n9CjAHIyN2*8";
    // Código generado
    private String codigoGenerado;

    public MensajeSMS() {
        this.codigoGenerado = null;
    }

    /**
     * Método para enviar un SMS con un código personalizado al número
     * proporcionado.
     *
     * @param numeroDestino El número de teléfono del destinatario en formato
     *                      internacional (ej. +506XXXXXXX).
     * @param codigo        El código personalizado que deseas enviar.
     * @return true si el mensaje fue enviado correctamente, false en caso de error.
     */
    public boolean enviarMensajeVerificacion(String numeroDestino, String codigo) {
        try {
            // Configuración del consumidor OAuth
            OkHttpOAuthConsumer consumer = new OkHttpOAuthConsumer(KEY, SECRET);

            // Cliente HTTP con firma OAuth
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new SigningInterceptor(consumer))
                    .build();

            // Crear el objeto JSON con los datos del SMS
            JSONObject json = new JSONObject();

            json.put("sender", "ExampleSMS");

            // Asigna el mensaje recibido como parámetro
            json.put("message", "Tu cOdigo de verificaciOn es: " + codigo);

            // Asigna el número de destinatario recibido como parámetro
            json.put("recipients", (new JSONArray()).put(
                    (new JSONObject()).put("msisdn", numeroDestino)));

            // Crear el cuerpo de la solicitud con el JSON
            RequestBody body = RequestBody.create(json.toString(), MediaType.get("application/json; charset=utf-8"));

            // Crear la solicitud HTTP firmada
            Request signedRequest = (Request) consumer.sign(
                    new Request.Builder()
                            .url("https://gatewayapi.com/rest/mtsms")
                            .post(body)
                            .build())
                    .unwrap();

            // Ejecutar la solicitud y obtener la respuesta
            try (Response response = client.newCall(signedRequest).execute()) {
                // Imprimir la respuesta del servidor
                System.out.println(response.body().string());
            }

            // Guardar el código generado para futura verificación
            this.codigoGenerado = codigo;

            return true;
        } catch (Exception e) {
            // Manejo de errores
            System.err.println("Error al enviar mensaje de verificación: " + e.getMessage());
            return false;
        }
    }

    /**
     * Método para verificar el código que ingresa el usuario.
     *
     * @param codigoIngresado El código ingresado por el usuario.
     * @return true si el código es correcto, false si es incorrecto.
     */
    public boolean verificarCodigo(String codigoIngresado) {
        return (codigoGenerado != null && codigoGenerado.equals(codigoIngresado));
    }

    /**
     * Método para generar una palabra de verificación aleatoria.
     *
     * @return Una palabra de verificación como String.
     */
    public String generarPalabraVerificacion() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"; // Caracteres permitidos
        StringBuilder palabra = new StringBuilder();
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < 8; i++) { // Longitud de la palabra de verificación
            int indice = random.nextInt(caracteres.length());
            palabra.append(caracteres.charAt(indice));
        }

        return palabra.toString();
    }

    // Método de prueba
    public static void main(String[] args) {
        MensajeSMS mensajeSMS = new MensajeSMS();
        // Generar la palabra de verificación
        String palabraVerificacion = mensajeSMS.generarPalabraVerificacion();
        System.out.println("Palabra de verificación generada: " + palabraVerificacion);

        // Enviar el mensaje de verificación con el código generado
        boolean resultadoEnvio = mensajeSMS.enviarMensajeVerificacion("+50687068702", palabraVerificacion);

        if (resultadoEnvio) {
            System.out.println("Mensaje enviado con éxito.");

            // Simular que el usuario ingresa un código para validarlo
            String codigoUsuario = System.console().readLine("Ingrese el código de verificación: "); // Código ingresado
                                                                                                     // por el usuario

            // Verificar si el código ingresado es correcto
            if (mensajeSMS.verificarCodigo(codigoUsuario)) {
                System.out.println("Código verificado correctamente. Transacción permitida.");
            } else {
                System.out.println("El código ingresado es incorrecto. Transacción no permitida.");
            }
        } else {
            System.out.println("Error al enviar el mensaje.");
        }
    }
}

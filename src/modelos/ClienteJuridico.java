package modelos;

public class ClienteJuridico extends Cliente {
    private String tipoNegocio;
    private String razonSocial;

    public ClienteJuridico(String nombre, long identificacion, String numTelefono, String correoElectronico,
            String tipoNegocio, String razonSocial) {
        super(nombre, identificacion, numTelefono, correoElectronico);
        this.tipoNegocio = tipoNegocio;
        this.razonSocial = razonSocial;
    }

    public String getTipoNegocio() {
        return tipoNegocio;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    @Override
    public String getTipo() {
        return "Jurídico";
    }

    @Override
    public String toString() {
        return super.toString() + ", Tipo de negocio: " + tipoNegocio + ", Razón social: " + razonSocial;
    }
}

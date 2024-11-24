package modelos;

/**
 *
 * @author trigu
 * @param <Class>
 */

public interface Comparable<Class> {
    /**
     * Compara este objeto con el objeto especificado para el orden.
     * Devuelve un valor negativo si este objeto es menor que el objeto especificado,
     * cero si son iguales, o un valor positivo si es mayor.
     *
     * @param obj el objeto a comparar
     * @return un valor negativo, cero o positivo según el resultado de la comparación
     */
    int compareTo(Class obj);
}

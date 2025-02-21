// oie prof, boa jogatina! :)

public class Carta {
    private String valor;
    private String naipe;

    public Carta(String valor, String naipe) {
        this.valor = valor;
        this.naipe = naipe;
    }

    public String toString() {
        return valor + "-" + naipe;
    }

    public int getValorNumerico() {
        if ("JQK".contains(valor)) {
            return 10;
        }
        if ("A".equals(valor)) {
            return 11;
        }
        return Integer.parseInt(valor);
    }

    public boolean ehAs() {
        return "A".equals(valor);
    }

    public String getCaminhoImagem() {
        return "cartas/" + toString() + ".png";
    }
}

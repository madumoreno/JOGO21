package JOGO21;
import java.util.ArrayList;

public class Player {
    private ArrayList<Carta> mao;
    private int soma;
    private int qtdAs;

    public Player() {
        mao = new ArrayList<>();
        soma = 0;
        qtdAs = 0;
    }

    public void receberCarta(Carta carta) {
        mao.add(carta);
        soma += carta.getValorNumerico();
        if (carta.ehAs()) {
            qtdAs++;
        }
        ajustarValorAs();
    }

    private void ajustarValorAs() {
        while (soma > 21 && qtdAs > 0) {
            soma -= 10;
            qtdAs--;
        }
    }

    public int getSoma() {
        return soma;
    }

    public ArrayList<Carta> getMao() {
        return mao;
    }

    public void resetarMao() {
        mao.clear();
        soma = 0;
        qtdAs = 0;
    }
}
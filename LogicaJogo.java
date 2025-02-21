import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class LogicaJogo {
    private ArrayList<Carta> baralho;
    private Random random = new Random();
    private Player jogador;
    private Player dealer;
    private Carta cartaOculta;
    private int vitoriasJogador = 0;
    private int vitoriasDealer = 0;

    public LogicaJogo() {
        iniciarNovaPartidaVazio();
    }

    // aqui vai reiniciar o jogo, porém não vai distribuir as cartas, animação vai começar aqui
    public void iniciarNovaPartidaVazio() {
        construirEEmbaralharBaralho();
        jogador = new Player();
        dealer = new Player();
        cartaOculta = null;
    }

    private void construirEEmbaralharBaralho() {
        baralho = new ArrayList<>();
        String[] valores = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        String[] naipes = {"C", "D", "H", "S"};

        for (String naipe : naipes) {
            for (String valor : valores) {
                baralho.add(new Carta(valor, naipe));
            }
        }
        Collections.shuffle(baralho, random);
    }

    public Carta comprarCarta() {
        return baralho.remove(baralho.size() - 1);
    }

    // aq o jogador vai receber a carta
    public void receberCartaJogador(Carta carta) {
        jogador.receberCarta(carta);
    }

    // compiuter vai receber a carta
    public void receberCartaDealer(Carta carta) {
        dealer.receberCarta(carta);
    }

    public void setCartaOculta(Carta carta) {
        this.cartaOculta = carta;
    }

    public Carta getCartaOculta() {
        return cartaOculta;
    }

    public boolean dealerDeveContinuarJogando() {
        return dealer.getSoma() < 17;
    }

    // resultado
    public String getMensagemResultado() {
        int somaJogador = jogador.getSoma();
        int somaDealer = dealer.getSoma();

        if (somaJogador > 21) {
            vitoriasDealer++;
            return "Ixii, você perdeu!";
        }
        if (somaDealer > 21) {
            vitoriasJogador++;
            return "Boaaa, você ganhou! :)";
        }
        if (somaJogador == somaDealer) {
            return "Puts, empatou!";
        }
        if (somaJogador > somaDealer) {
            vitoriasJogador++;
            return "Boaaa, você ganhou! :)";
        }
        vitoriasDealer++;
        return "Ixii, você perdeu!";
    }

    public int getVitoriasJogador() {
        return vitoriasJogador;
    }

    public int getVitoriasDealer() {
        return vitoriasDealer;
    }

    public Player getJogador() {
        return jogador;
    }

    public Player getDealer() {
        return dealer;
    }
}
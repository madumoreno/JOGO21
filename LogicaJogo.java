package JOGO21;
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
        iniciarNovaPartida();
    }

    public void iniciarNovaPartida() {
        construirEEmbaralharBaralho();
        jogador = new Player();
        dealer = new Player();

        cartaOculta = comprarCarta();
        dealer.receberCarta(comprarCarta());

        jogador.receberCarta(comprarCarta());
        jogador.receberCarta(comprarCarta());
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

    public void jogadorComprarCarta() {
        jogador.receberCarta(comprarCarta());
    }

    public void dealerJogar() {
        dealer.receberCarta(cartaOculta);
        while (dealer.getSoma() < 17) {
            dealer.receberCarta(comprarCarta());
        }
    }

    public String getMensagemResultado() {
        int somaJogador = jogador.getSoma();
        int somaDealer = dealer.getSoma();

        if (somaJogador > 21) {
            vitoriasDealer++;
            return "Você perdeu!";
        }
        if (somaDealer > 21) {
            vitoriasJogador++;
            return "Você ganhou!";
        }
        if (somaJogador == somaDealer) {
            return "Empate!";
        }
        if (somaJogador > somaDealer) {
            vitoriasJogador++;
            return "Você ganhou!";
        }
        vitoriasDealer++;
        return "Você perdeu!";
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

    public Carta getCartaOculta() {
        return cartaOculta;
    }
}
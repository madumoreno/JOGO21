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

    /**
     * 🚀 Reinicia o jogo, mas **não** distribui cartas automaticamente.
     * Isso permite que `Principal.java` faça a animação da distribuição inicial.
     */
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

    /**
     * 🃏 Retorna uma carta do baralho, mas **não** adiciona à mão.
     * A adição à mão será feita **após a animação terminar**.
     */
    public Carta comprarCarta() {
        return baralho.remove(baralho.size() - 1);
    }

    /**
     * 🎴 Jogador recebe carta **após a animação terminar**.
     */
    public void receberCartaJogador(Carta carta) {
        jogador.receberCarta(carta);
    }

    /**
     * 🃏 Dealer recebe carta **após a animação terminar**.
     */
    public void receberCartaDealer(Carta carta) {
        dealer.receberCarta(carta);
    }

    /**
     * 🔹 Define a carta oculta do Dealer (a primeira carta que será revelada depois).
     */
    public void setCartaOculta(Carta carta) {
        this.cartaOculta = carta;
    }

    public Carta getCartaOculta() {
        return cartaOculta;
    }

    /**
     * 🏆 Dealer joga após o jogador parar.
     * - **Antes**: Ele comprava instantaneamente.
     * - **Agora**: Esse método **apenas retorna se o Dealer deve continuar jogando**.
     *   As compras animadas são controladas no `Principal.java`.
     */
    public boolean dealerDeveContinuarJogando() {
        return dealer.getSoma() < 17;
    }

    /**
     * 📢 Mensagem do resultado final.
     */
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
}

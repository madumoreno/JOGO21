import javax.swing.*;
import java.awt.*;

public class Principal {
    private LogicaJogo logicaJogo;
    private PainelJogo painelJogo;
    private JButton botaoComprar, botaoParar, botaoReiniciar;
    private JLabel labelPontuacao;

    public Principal() {
        logicaJogo = new LogicaJogo();
        painelJogo = new PainelJogo(logicaJogo);

        JFrame frame = new JFrame("Black Jack");
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.add(painelJogo, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel();
        botaoComprar = new JButton("Comprar Carta");
        botaoParar = new JButton("Parar");
        botaoReiniciar = new JButton("Jogar Novamente");
        botaoReiniciar.setEnabled(false);

        labelPontuacao = new JLabel("Vitórias: 0 | Derrotas: 0");
        labelPontuacao.setHorizontalAlignment(SwingConstants.CENTER);

        painelBotoes.add(botaoComprar);
        painelBotoes.add(botaoParar);
        painelBotoes.add(botaoReiniciar);
        frame.add(painelBotoes, BorderLayout.SOUTH);
        frame.add(labelPontuacao, BorderLayout.NORTH);

        // 🔹 Distribuição inicial de cartas ANIMADA
        botaoReiniciar.addActionListener(e -> iniciarNovaPartidaAnimada());

        // 🔹 Jogador compra carta ANIMADA
        botaoComprar.addActionListener(e -> comprarCartaJogador());

        // 🔹 Dealer joga ANIMADO
        botaoParar.addActionListener(e -> jogarDealer());

        frame.setVisible(true);

        // Começa a partida já animando a distribuição inicial
        iniciarNovaPartidaAnimada();
    }

    /**
     * 🔥 Reinicia o jogo e distribui as cartas iniciais com animação.
     */
    private void iniciarNovaPartidaAnimada() {
        botaoComprar.setEnabled(false);
        botaoParar.setEnabled(false);
        botaoReiniciar.setEnabled(false);

        logicaJogo.iniciarNovaPartidaVazio();
        painelJogo.resetarJogo();

        // 🔹 Sequência de distribuição animada:
        Carta cartaD1 = logicaJogo.comprarCarta();
        painelJogo.animarCompraCarta(cartaD1, false, () -> logicaJogo.receberCartaDealer(cartaD1));

        Carta cartaJ1 = logicaJogo.comprarCarta();
        painelJogo.animarCompraCarta(cartaJ1, true, () -> logicaJogo.receberCartaJogador(cartaJ1));

        Carta cartaD2 = logicaJogo.comprarCarta();
        painelJogo.animarCompraCarta(cartaD2, false, () -> logicaJogo.receberCartaDealer(cartaD2));

        Carta cartaJ2 = logicaJogo.comprarCarta();
        painelJogo.animarCompraCarta(cartaJ2, true, () -> {
            logicaJogo.receberCartaJogador(cartaJ2);
            botaoComprar.setEnabled(true);
            botaoParar.setEnabled(true);
        });
    }

    /**
     * 🎴 O jogador compra uma carta animada.
     */
    private void comprarCartaJogador() {
        botaoComprar.setEnabled(false);

        Carta novaCarta = logicaJogo.comprarCarta();
        painelJogo.animarCompraCarta(novaCarta, true, () -> {
            logicaJogo.receberCartaJogador(novaCarta);
            botaoComprar.setEnabled(true);

            // 🔹 Se o jogador estourar ( > 21 ), finaliza o jogo automaticamente
            if (logicaJogo.getJogador().getSoma() > 21) {
                botaoComprar.setEnabled(false);
                botaoParar.setEnabled(false);
                encerrarJogo();
            }
        });
    }

    /**
     * 🃏 O dealer joga (compra cartas até >= 17) com animação.
     */
    private void jogarDealer() {
        botaoComprar.setEnabled(false);
        botaoParar.setEnabled(false);

        new Thread(() -> {
            while (logicaJogo.getDealer().getSoma() < 17) {
                Carta novaCarta = logicaJogo.comprarCarta();
                painelJogo.animarCompraCarta(novaCarta, false, () -> logicaJogo.receberCartaDealer(novaCarta));

                try {
                    Thread.sleep(500); // Pequena pausa para dar tempo entre as compras do dealer
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }

            // 🔹 Ao terminar todas as compras do dealer, revelamos as cartas e encerramos o jogo
            SwingUtilities.invokeLater(() -> encerrarJogo());
        }).start();
    }

    /**
     * 🏆 Revela as cartas do dealer e mostra o resultado.
     */
    private void encerrarJogo() {
        painelJogo.revelarCartasDealer();
        botaoReiniciar.setEnabled(true);
        atualizarPontuacao();
    }

    /**
     * 🏅 Atualiza a pontuação na interface.
     */
    private void atualizarPontuacao() {
        labelPontuacao.setText("Vitórias: " + logicaJogo.getVitoriasJogador() + " | Derrotas: " + logicaJogo.getVitoriasDealer());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Principal::new);
    }
}

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

    // reiniciar o jogo / distribuir cartas
    private void iniciarNovaPartidaAnimada() {
        botaoComprar.setEnabled(false);
        botaoParar.setEnabled(false);
        botaoReiniciar.setEnabled(false);

        logicaJogo.iniciarNovaPartidaVazio();
        painelJogo.resetarJogo();

        // animação da distribuição
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

        // compra de carta
    private void comprarCartaJogador() {
        botaoComprar.setEnabled(false);

        Carta novaCarta = logicaJogo.comprarCarta();
        painelJogo.animarCompraCarta(novaCarta, true, () -> {
            logicaJogo.receberCartaJogador(novaCarta);
            botaoComprar.setEnabled(true);

            // se o jogador estourar 21, o jogo finaliza
            if (logicaJogo.getJogador().getSoma() > 21) {
                botaoComprar.setEnabled(false);
                botaoParar.setEnabled(false);
                encerrarJogo();
            }
        });
    }

    // compra dealer
    private void jogarDealer() {
        botaoComprar.setEnabled(false);
        botaoParar.setEnabled(false);

        new Thread(() -> {
            while (logicaJogo.getDealer().getSoma() < 17) {
                Carta novaCarta = logicaJogo.comprarCarta();
                painelJogo.animarCompraCarta(novaCarta, false, () -> logicaJogo.receberCartaDealer(novaCarta));

                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            
            SwingUtilities.invokeLater(() -> encerrarJogo());
        }).start();
    }

    // mostra resultado
    private void encerrarJogo() {
        painelJogo.revelarCartasDealer();
        botaoReiniciar.setEnabled(true);
        atualizarPontuacao();
    }

    // atualiza a pontuação
    private void atualizarPontuacao() {
        labelPontuacao.setText("Vitórias: " + logicaJogo.getVitoriasJogador() + " | Derrotas: " + logicaJogo.getVitoriasDealer());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Principal::new);
    }
}

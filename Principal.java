package JOGO21;
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

        botaoComprar.addActionListener(e -> {
            logicaJogo.jogadorComprarCarta();
            painelJogo.repaint();
            if (logicaJogo.getJogador().getSoma() > 21) {
                botaoComprar.setEnabled(false);
                botaoParar.setEnabled(false);
                encerrarJogo();
            }
        });

        botaoParar.addActionListener(e -> {
            botaoComprar.setEnabled(false);
            botaoParar.setEnabled(false);
            logicaJogo.dealerJogar();
            encerrarJogo();
        });

        botaoReiniciar.addActionListener(e -> {
            logicaJogo.iniciarNovaPartida();
            painelJogo.resetarJogo();
            botaoComprar.setEnabled(true);
            botaoParar.setEnabled(true);
            botaoReiniciar.setEnabled(false);
        });

        frame.setVisible(true);
    }

    private void encerrarJogo() {
        painelJogo.revelarCartasDealer();
        botaoReiniciar.setEnabled(true);
        atualizarPontuacao();
    }

    private void atualizarPontuacao() {
        labelPontuacao.setText("Vitórias: " + logicaJogo.getVitoriasJogador() + " | Derrotas: " + logicaJogo.getVitoriasDealer());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Principal::new);
    }
}
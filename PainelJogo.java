package JOGO21;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.List;

public class PainelJogo extends JPanel {
    private LogicaJogo logicaJogo;
    private boolean revelarCartasDealer = false;
    private final int larguraCarta = 100;
    private final int alturaCarta = 140;
    private final int espacamento = 20;

    public PainelJogo(LogicaJogo logicaJogo) {
        this.logicaJogo = logicaJogo;
        setBackground(new Color(39, 119, 20));
    }

    public void revelarCartasDealer() {
        revelarCartasDealer = true;
        repaint();
    }

    public void resetarJogo() {
        revelarCartasDealer = false;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        desenharCartas(g);
    }

    private void desenharCartas(Graphics g) {
        int centroX = getWidth() / 2;

        // 🃏 Desenha as cartas do Dealer (Computador)
        desenharMao(g, logicaJogo.getDealer().getMao(), centroX, 50, !revelarCartasDealer);

        // 🃏 Desenha as cartas do Jogador
        desenharMao(g, logicaJogo.getJogador().getMao(), centroX, 300, false);

        // 🏆 Exibir resultado final
        if (revelarCartasDealer) {
            String resultado = logicaJogo.getMensagemResultado();
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.setColor(Color.WHITE);
            g.drawString(resultado, getWidth() / 2 - 80, getHeight() / 2);
        }
    }

    private void desenharMao(Graphics g, List<Carta> mao, int centroX, int posY, boolean ocultarPrimeira) {
        int xInicial = centroX - (mao.size() * (larguraCarta + espacamento)) / 2;

        for (int i = 0; i < mao.size(); i++) {
            Carta carta = mao.get(i);
            Image imagemCarta = carregarImagemCarta(carta, (i == 0 && ocultarPrimeira));

            if (imagemCarta != null) {
                g.drawImage(imagemCarta, xInicial + (larguraCarta + espacamento) * i, posY, larguraCarta, alturaCarta, this);
            }
        }
    }

    private Image carregarImagemCarta(Carta carta, boolean oculta) {
        String caminhoImagem;
        if (oculta) {
            caminhoImagem = "cartas/BACK.png"; 
        } else {
            caminhoImagem = "cartas/" + carta.toString() + ".png"; 
        }
    
        try {
            // Se estiver tudo dentro de src, use o "/" na frente:
            URL imagemURL = getClass().getResource("/" + caminhoImagem);
            // Alternativa com classLoader (sem barra):
            // URL imagemURL = getClass().getClassLoader().getResource(caminhoImagem);
    
            if (imagemURL != null) {
                return new ImageIcon(imagemURL).getImage();
            } else {
                System.err.println("Imagem não encontrada: " + caminhoImagem);
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }       
}
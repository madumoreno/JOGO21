import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class PainelJogo extends JPanel {
    private LogicaJogo logicaJogo;
    private boolean revelarCartasDealer = false;
    private final int larguraCarta = 100;
    private final int alturaCarta = 140;
    private final int espacamento = 20;

    // 🎴 FILA DE ANIMAÇÕES
    private static class Animacao {
        Carta carta;
        boolean paraJogador;
        Runnable callback;

        public Animacao(Carta carta, boolean paraJogador, Runnable callback) {
            this.carta = carta;
            this.paraJogador = paraJogador;
            this.callback = callback;
        }
    }

    private Queue<Animacao> filaAnimacoes = new LinkedList<>();
    private boolean animando = false;

    // 📌 Variáveis para animação atual
    private Carta cartaAnimada;
    private boolean cartaParaJogador;
    private Runnable callbackAtual;
    private int xAnim, yAnim;
    private int xFinal, yFinal;
    private int frameAtual, totalFrames;
    private int xInicialBaralho, yInicialBaralho;

    public PainelJogo(LogicaJogo logicaJogo) {
        this.logicaJogo = logicaJogo;
        setBackground(new Color(39, 119, 20));
    }

    // 🚀 Método para adicionar uma nova animação na fila
    public void animarCompraCarta(Carta carta, boolean paraJogador, Runnable callback) {
        filaAnimacoes.offer(new Animacao(carta, paraJogador, callback));
        iniciarProximaAnimacaoSePossivel();
    }

    // 🎬 Se nenhuma animação estiver rodando, inicia a próxima na fila
    private void iniciarProximaAnimacaoSePossivel() {
        if (!animando && !filaAnimacoes.isEmpty()) {
            iniciarAnimacao(filaAnimacoes.poll());
        }
    }

    private void iniciarAnimacao(Animacao anim) {
        this.animando = true;
        this.cartaAnimada = anim.carta;
        this.cartaParaJogador = anim.paraJogador;
        this.callbackAtual = anim.callback;

        // 📌 Posição inicial (baralho, topo central)
        xInicialBaralho = getWidth() / 2 - larguraCarta / 2;
        yInicialBaralho = 10;
        this.xAnim = xInicialBaralho;
        this.yAnim = yInicialBaralho;

        // 📌 Calcula posição final (onde a carta será desenhada)
        List<Carta> maoDestino = cartaParaJogador
                ? logicaJogo.getJogador().getMao()
                : logicaJogo.getDealer().getMao();

        int centroX = getWidth() / 2;
        int yFinalBase = cartaParaJogador ? 300 : 50;
        int indiceNovaCarta = maoDestino.size();
        int xBase = centroX - ((indiceNovaCarta + 1) * (larguraCarta + espacamento)) / 2;
        xFinal = xBase + indiceNovaCarta * (larguraCarta + espacamento);
        yFinal = yFinalBase;

        frameAtual = 0;
        totalFrames = 15;

        Timer timer = new Timer(30, e -> {
            frameAtual++;
            float t = (float) frameAtual / totalFrames;
            xAnim = (int) (xInicialBaralho + t * (xFinal - xInicialBaralho));
            yAnim = (int) (yInicialBaralho + t * (yFinal - yInicialBaralho));
            repaint();

            if (frameAtual >= totalFrames) {
                ((Timer) e.getSource()).stop();
                finalizarAnimacao();
            }
        });
        timer.start();
    }

    private void finalizarAnimacao() {
        if (callbackAtual != null) {
            callbackAtual.run();
        }
        animando = false;
        cartaAnimada = null;
        callbackAtual = null;
        iniciarProximaAnimacaoSePossivel();
        repaint();
    }

    public void revelarCartasDealer() {
        revelarCartasDealer = true;
        repaint();
    }

    public void resetarJogo() {
        revelarCartasDealer = false;
        filaAnimacoes.clear();
        animando = false;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        desenharCartasFixas(g);

        if (animando && cartaAnimada != null) {
            Image img = carregarImagemCarta(cartaAnimada, false);
            if (img != null) {
                g.drawImage(img, xAnim, yAnim, larguraCarta, alturaCarta, this);
            }
        }
    }

    private void desenharCartasFixas(Graphics g) {
        int centroX = getWidth() / 2;

        // Dealer 🃏
        desenharMao(g, logicaJogo.getDealer().getMao(), centroX, 50, !revelarCartasDealer);

        // Jogador 🎴
        desenharMao(g, logicaJogo.getJogador().getMao(), centroX, 300, false);

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
            boolean virar = (i == 0 && ocultarPrimeira);
            Image imagemCarta = carregarImagemCarta(carta, virar);

            if (imagemCarta != null) {
                g.drawImage(imagemCarta,
                        xInicial + (larguraCarta + espacamento) * i,
                        posY,
                        larguraCarta,
                        alturaCarta,
                        this);
            }
        }
    }

    private Image carregarImagemCarta(Carta carta, boolean oculta) {
        String caminhoImagem = oculta ? "cartas/BACK.png" : "cartas/" + carta.toString() + ".png";
        File arquivoImagem = new File(caminhoImagem);
        if (arquivoImagem.exists()) {
            return new ImageIcon(arquivoImagem.getAbsolutePath()).getImage();
        } else {
            System.err.println("Imagem não encontrada: " + arquivoImagem.getAbsolutePath());
            return null;
        }
    }
}
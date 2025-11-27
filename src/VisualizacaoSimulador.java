import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class VisualizacaoSimulador extends JFrame implements Desenhavel {
    private static final Color COR_DESCONHECIDA = Color.gray;
    private final String PREFIXO_PASSO = "Passo: ";
    private JLabel rotuloPasso;
    private PainelLegenda painelLegenda;
    private JLabel rotuloClima;
    private JButton botaoPausar;
    private JButton botaoReiniciar;
    private VisaoCampo visaoCampo;
    private HashMap<Class<?>, Color> cores;
    private EstatisticasCampo estatisticas;

    public VisualizacaoSimulador(int altura, int largura) {
        estatisticas = new EstatisticasCampo();
        cores = new HashMap<>();

        setTitle("Simulador Ecossistema - Grupo 10");
        setLocation(100, 50);

        JPanel painelSuperior = new JPanel(new GridLayout(1, 2));
        painelSuperior.setPreferredSize(new Dimension(0, 40));
        rotuloPasso = new JLabel(PREFIXO_PASSO, JLabel.CENTER);
        rotuloClima = new JLabel("Clima: NORMAL", JLabel.CENTER);
        rotuloClima.setOpaque(true);
        rotuloClima.setBackground(new Color(135, 206, 235));

        painelSuperior.add(rotuloPasso);
        painelSuperior.add(rotuloClima);

        visaoCampo = new VisaoCampo(altura, largura);

        JPanel painelInferior = new JPanel(new BorderLayout());
        painelInferior.setPreferredSize(new Dimension(0, 140));
        painelLegenda = new PainelLegenda();
        painelLegenda.setBorder(new EmptyBorder(5, 5, 5, 5));

        JPanel painelBotoes = new JPanel(new FlowLayout());
        painelBotoes.setPreferredSize(new Dimension(0, 40));
        botaoPausar = new JButton("Pausar");
        botaoReiniciar = new JButton("Resetar Simulação");

        botaoPausar.setPreferredSize(new Dimension(100, 30));
        botaoReiniciar.setPreferredSize(new Dimension(150, 30));

        painelBotoes.add(botaoPausar);
        painelBotoes.add(botaoReiniciar);

        painelInferior.add(painelLegenda, BorderLayout.CENTER);
        painelInferior.add(painelBotoes, BorderLayout.SOUTH);

        Container conteudo = getContentPane();
        conteudo.add(painelSuperior, BorderLayout.NORTH);
        conteudo.add(visaoCampo, BorderLayout.CENTER);
        conteudo.add(painelInferior, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void setInfoClima(String texto, boolean chuvoso) {
        rotuloClima.setText(texto);
        if (chuvoso) {
            rotuloClima.setBackground(new Color(70, 130, 180));
            rotuloClima.setForeground(Color.WHITE);
            rotuloClima.setIcon(UIManager.getIcon("OptionPane.informationIcon"));
        } else {
            rotuloClima.setBackground(new Color(255, 255, 224));
            rotuloClima.setForeground(Color.BLACK);
            rotuloClima.setIcon(UIManager.getIcon("OptionPane.warningIcon"));
        }
    }

    @Override
    public void setAcaoPausar(ActionListener listener) {
        ActionListener[] listeners = botaoPausar.getActionListeners();
        for (int i = 0; i < listeners.length; i++) {
            botaoPausar.removeActionListener(listeners[i]);
        }
        botaoPausar.addActionListener(listener);
    }

    @Override
    public void setAcaoReiniciar(ActionListener listener) {
        ActionListener[] listeners = botaoReiniciar.getActionListeners();
        for (int i = 0; i < listeners.length; i++) {
            botaoReiniciar.removeActionListener(listeners[i]);
        }
        botaoReiniciar.addActionListener(listener);
    }

    @Override
    public void setTextoBotaoPausa(String texto) {
        botaoPausar.setText(texto);
    }

    @Override
    public void definirCor(Class<?> classeAnimal, Color cor) {
        cores.put(classeAnimal, cor);
    }

    private Color getCor(Class<?> classeAnimal) {
        Color c = cores.get(classeAnimal);
        if (c == null)
            return COR_DESCONHECIDA;
        else
            return c;
    }

    @Override
    public void mostrarStatus(int passo, Campo campo) {
        if (!isVisible())
            setVisible(true);

        rotuloPasso.setText(PREFIXO_PASSO + passo);
        estatisticas.reiniciar();
        visaoCampo.prepararPintura();

        for (int linha = 0; linha < campo.getProfundidade(); linha++) {
            for (int coluna = 0; coluna < campo.getLargura(); coluna++) {
                Object animal = campo.getObjetoEm(linha, coluna);
                if (animal != null) {
                    if (!(animal instanceof Obstaculo)) {
                        estatisticas.incrementarContagem(animal.getClass());
                    }

                    Color cor;
                    if (animal instanceof Obstaculo) {
                        if (animal == Obstaculo.RIO)
                            cor = Color.CYAN;
                        else
                            cor = Color.DARK_GRAY;
                    } else {
                        cor = getCor(animal.getClass());
                    }
                    visaoCampo.desenharMarca(coluna, linha, cor);
                } else {
                    if (campo.temGramaMadura(linha, coluna)) {
                        visaoCampo.desenharMarca(coluna, linha, new Color(200, 255, 200));
                    } else {
                        visaoCampo.desenharMarca(coluna, linha, new Color(240, 230, 140));
                    }
                }
            }
        }
        estatisticas.contagemConcluida();
        painelLegenda.atualizarLegenda(estatisticas, campo, cores);
        visaoCampo.repaint();
    }

    @Override
    public boolean ehViavel(Campo campo) {
        return estatisticas.ehViavel(campo);
    }

    @Override
    public void reiniciar() {
        estatisticas.reiniciar();
        visaoCampo.prepararPintura();
        visaoCampo.repaint();
    }

    @Override
    public void fechar() {
        dispose();
    }

    /**
     * Painel personalizado para exibir a legenda com quadradinhos de cor
     */
    private class PainelLegenda extends JPanel {
        private HashMap<Class<?>, String> nomesAnimais;
        private HashMap<Class<?>, Integer> contagensAnimais;
        private HashMap<Class<?>, Color> coresAnimais;

        public PainelLegenda() {
            setMinimumSize(new Dimension(400, 90));
            setPreferredSize(new Dimension(600, 90));
            nomesAnimais = new HashMap<>();
            contagensAnimais = new HashMap<>();
            coresAnimais = new HashMap<>();

            try {
                nomesAnimais.put(Class.forName("Raposa"), "Raposas");
                nomesAnimais.put(Class.forName("Coelho"), "Coelhos");
                nomesAnimais.put(Class.forName("Rato"), "Ratos");
                nomesAnimais.put(Class.forName("Cobra"), "Cobras");
                nomesAnimais.put(Class.forName("Gaviao"), "Gaviões");
                nomesAnimais.put(Class.forName("Urso"), "Ursos");
            } catch (ClassNotFoundException e) {
                // Classes não encontradas, usar nomes das classes
            }
        }

        public void atualizarLegenda(EstatisticasCampo stats, Campo campo, HashMap<Class<?>, Color> cores) {
            contagensAnimais.clear();
            coresAnimais.clear();

            for (Class<?> classe : nomesAnimais.keySet()) {
                Color cor = cores.get(classe);
                if (cor != null) {
                    coresAnimais.put(classe, cor);
                    contagensAnimais.put(classe, 0);
                }
            }

            for (int linha = 0; linha < campo.getProfundidade(); linha++) {
                for (int coluna = 0; coluna < campo.getLargura(); coluna++) {
                    Object animal = campo.getObjetoEm(linha, coluna);
                    if (animal != null && !(animal instanceof Obstaculo)) {
                        Class<?> classe = animal.getClass();
                        if (coresAnimais.containsKey(classe)) {
                            contagensAnimais.put(classe, contagensAnimais.get(classe) + 1);
                        }
                    }
                }
            }
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            FontMetrics fmTitulo = g2d.getFontMetrics();
            String titulo = "População";
            int xTitulo = (getWidth() - fmTitulo.stringWidth(titulo)) / 2;
            g2d.drawString(titulo, xTitulo, 20);

            final int TAMANHO_QUADRADO = 12;
            final int Y_PRIMEIRA_LINHA = 45;
            final int Y_SEGUNDA_LINHA = 70;

            g2d.setFont(new Font("Arial", Font.PLAIN, 14));

            Class<?>[] ordemAnimais = new Class<?>[6];
            try {
                ordemAnimais[0] = Class.forName("Raposa");
                ordemAnimais[1] = Class.forName("Coelho");
                ordemAnimais[2] = Class.forName("Rato");
                ordemAnimais[3] = Class.forName("Cobra");
                ordemAnimais[4] = Class.forName("Gaviao");
                ordemAnimais[5] = Class.forName("Urso");
            } catch (ClassNotFoundException e) {
                int i = 0;
                for (Class<?> classe : coresAnimais.keySet()) {
                    if (i < 6)
                        ordemAnimais[i++] = classe;
                }
            }

            FontMetrics fm = g2d.getFontMetrics();
            int larguraMaxima = 0;
            String[] textosAnimais = new String[6];

            for (int i = 0; i < ordemAnimais.length && i < 6; i++) {
                Class<?> classe = ordemAnimais[i];
                if (classe != null && coresAnimais.containsKey(classe)) {
                    Integer contagem = contagensAnimais.getOrDefault(classe, 0);
                    String nome = nomesAnimais.getOrDefault(classe, classe.getSimpleName());
                    textosAnimais[i] = nome + ": " + contagem;
                    int larguraTexto = TAMANHO_QUADRADO + 5 + fm.stringWidth(textosAnimais[i]);
                    if (larguraTexto > larguraMaxima) {
                        larguraMaxima = larguraTexto;
                    }
                }
            }

            final int ESPACO_ENTRE_COLUNAS = 20;
            int larguraTotalGrid = (larguraMaxima * 3) + (ESPACO_ENTRE_COLUNAS * 2);
            int xInicial = Math.max(10, (getWidth() - larguraTotalGrid) / 2);

            for (int i = 0; i < ordemAnimais.length && i < 6; i++) {
                Class<?> classe = ordemAnimais[i];
                if (classe != null && coresAnimais.containsKey(classe)) {
                    Color cor = coresAnimais.get(classe);
                    Integer contagem = contagensAnimais.getOrDefault(classe, 0);
                    String nome = nomesAnimais.getOrDefault(classe, classe.getSimpleName());

                    int coluna = i % 3;
                    int linha = i / 3;

                    int x = xInicial + (coluna * (larguraMaxima + ESPACO_ENTRE_COLUNAS));
                    int y = (linha == 0) ? Y_PRIMEIRA_LINHA : Y_SEGUNDA_LINHA;

                    g2d.setColor(cor);
                    g2d.fillRect(x, y - TAMANHO_QUADRADO + 2, TAMANHO_QUADRADO, TAMANHO_QUADRADO);
                    g2d.setColor(Color.BLACK);
                    g2d.drawRect(x, y - TAMANHO_QUADRADO + 2, TAMANHO_QUADRADO, TAMANHO_QUADRADO);

                    String textoItem = textosAnimais[i] != null ? textosAnimais[i] : (nome + ": " + contagem);
                    g2d.drawString(textoItem, x + TAMANHO_QUADRADO + 5, y);
                }
            }

            g2d.dispose();
        }
    }

    private class VisaoCampo extends JPanel {
        private final int FATOR_ESCALA_GRADE = 6;
        private int larguraGrade, alturaGrade;
        private int escalaX, escalaY;
        Dimension tamanho;
        private Graphics g;
        private Image imagemCampo;

        public VisaoCampo(int altura, int largura) {
            alturaGrade = altura;
            larguraGrade = largura;
            tamanho = new Dimension(0, 0);
        }

        public Dimension getPreferredSize() {
            return new Dimension(larguraGrade * FATOR_ESCALA_GRADE, alturaGrade * FATOR_ESCALA_GRADE);
        }

        public void prepararPintura() {
            if (!tamanho.equals(getSize())) {
                tamanho = getSize();
                imagemCampo = visaoCampo.createImage(tamanho.width, tamanho.height);
                g = imagemCampo.getGraphics();

                escalaX = tamanho.width / larguraGrade;
                if (escalaX < 1)
                    escalaX = FATOR_ESCALA_GRADE;
                escalaY = tamanho.height / alturaGrade;
                if (escalaY < 1)
                    escalaY = FATOR_ESCALA_GRADE;
            }
        }

        public void desenharMarca(int x, int y, Color cor) {
            g.setColor(cor);
            g.fillRect(x * escalaX, y * escalaY, escalaX - 1, escalaY - 1);
        }

        public void paintComponent(Graphics g) {
            if (imagemCampo != null) {
                g.drawImage(imagemCampo, 0, 0, null);
            }
        }
    }
}
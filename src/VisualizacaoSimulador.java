import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.util.ArrayList;
import java.util.List;
/**
 * Esta classe implementa a interface gráfica do simulador.
 * Ela exibe o campo de simulação, informações do passo atual,
 * clima, legenda de cores e botões de controle.
 * 
 * @author Grupo 10
 */
public class VisualizacaoSimulador extends JFrame implements Desenhavel {
    // Constantes e Componentes
    private static final Color COR_DESCONHECIDA = Color.gray;
    private static final Color COR_GRAMA_MADURA = new Color(200, 255, 200);
    private static final Color COR_GRAMA_NOVA = new Color(240, 230, 140);

    private final String PREFIXO_PASSO = "Passo: ";
    private JLabel rotuloPasso;
    private JLabel rotuloClima;
    private JButton botaoPausar;
    private JButton botaoReiniciar;
    
    // Componentes Customizados
    private PainelLegenda painelLegenda;
    private VisaoCampo visaoCampo;
    
    // Dados
    private HashMap<Object, Color> mapaDeCores;
    private EstatisticasCampo estatisticas;
    private Set<Class<?>> classesIgnoradasNaContagem;

    /**
     * Constrói a interface gráfica do simulador.
     * @param altura Altura do campo de simulação.
     * @param largura Largura do campo de simulação.
     */
    public VisualizacaoSimulador(int altura, int largura, EstatisticasCampo estatisticas) {
        this.estatisticas = estatisticas;
        
        // Inicializa com HashMap genérico
        this.mapaDeCores = new HashMap<>();
        this.classesIgnoradasNaContagem = new HashSet<>();

        configurarJanela();
        criarComponentes(altura, largura);
        setVisible(true);
    }

    // --- Configuração da UI ---
    /**
     * Configura propriedades básicas da janela.
     */
    private void configurarJanela() {
        setTitle("Simulador Ecossistema - Grupo 10");
        setLocation(100, 50);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Cria e organiza os componentes da interface.
     * @param altura Altura do campo de simulação.
     * @param largura Largura do campo de simulação.
     */
    private void criarComponentes(int altura, int largura) {
        JPanel painelSuperior = criarPainelSuperior();
        visaoCampo = new VisaoCampo(altura, largura);
        JPanel painelInferior = criarPainelInferior();
        // Layout principal
        Container conteudo = getContentPane();
        conteudo.add(painelSuperior, BorderLayout.NORTH);
        conteudo.add(visaoCampo, BorderLayout.CENTER);
        conteudo.add(painelInferior, BorderLayout.SOUTH);
        // Ajusta o tamanho da janela para acomodar os componentes
        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Cria o painel superior com informações do passo e clima.
     * @return O painel superior.
     */
    private JPanel criarPainelSuperior() {
        JPanel p = new JPanel(new GridLayout(1, 2));
        p.setPreferredSize(new Dimension(0, 40));
        // Rótulos de passo e clima
        rotuloPasso = new JLabel(PREFIXO_PASSO, JLabel.CENTER);
        rotuloClima = new JLabel("Clima: NORMAL", JLabel.CENTER);
        rotuloClima.setOpaque(true);
        rotuloClima.setBackground(new Color(135, 206, 235));

        p.add(rotuloPasso);
        p.add(rotuloClima);
        return p;
    }

    /**
     * Cria o painel inferior com legenda e botões.
     * * @return O painel inferior.
     */
    private JPanel criarPainelInferior() {
        JPanel p = new JPanel(new BorderLayout());
        p.setPreferredSize(new Dimension(0, 140));

        painelLegenda = new PainelLegenda();
        painelLegenda.setBorder(new EmptyBorder(5, 5, 5, 5));
        // Botões de controle
        JPanel painelBotoes = new JPanel(new FlowLayout());
        painelBotoes.setPreferredSize(new Dimension(0, 40));
        // Botões de controle
        botaoPausar = new JButton("Pausar");
        botaoPausar.setPreferredSize(new Dimension(100, 30));
        // Botão Reiniciar
        botaoReiniciar = new JButton("Resetar Simulação");
        botaoReiniciar.setPreferredSize(new Dimension(150, 30));
        // Adicionar botões ao painel
        painelBotoes.add(botaoPausar);
        painelBotoes.add(botaoReiniciar);
        // Adicionar legenda e botões ao painel inferior
        p.add(painelLegenda, BorderLayout.CENTER);
        p.add(painelBotoes, BorderLayout.SOUTH);
        return p;
    }

    /**
     * Percorre o campo apenas para desenhar os agentes e o terreno.
     */
    private void desenharCampo(GradeVisualizavel grade) {
            for (int linha = 0; linha < grade.getProfundidade(); linha++) {
                for (int coluna = 0; coluna < grade.getLargura(); coluna++) {
                    
                    Object objeto = grade.getObjetoEm(linha, coluna);
                    Color corPixel;

                    if (objeto != null) {
                        corPixel = determinarCorAgente(objeto);
                    } else {
                        corPixel = grade.temGramaMadura(linha, coluna) ? COR_GRAMA_MADURA : COR_GRAMA_NOVA;
                    }
                    
                    visaoCampo.desenharMarca(coluna, linha, corPixel);
                }
            }
    }

    /**
     * Adiciona uma classe de animal para ser ignorada na contagem de estatísticas.
     * @param classe A classe do animal a ser ignorada.
     */
    public void adicionarClasseIgnorada(Class<?> classe) {
            classesIgnoradasNaContagem.add(classe);
    }

    // --- Métodos da Interface Desenhavel ---

   /**
     * Agora aceita GradeVisualizavel em vez de Campo concreto
     */
    @Override
    public void mostrarStatus(int passo, GradeVisualizavel grade) {
        if (!isVisible()) setVisible(true);

        rotuloPasso.setText(PREFIXO_PASSO + passo);
        
        // Fase 1: Coleta
        estatisticas.reiniciar();
        coletarEstatisticas(grade); // Passamos a interface
        estatisticas.contagemConcluida();
        
        painelLegenda.atualizarDados(estatisticas, mapaDeCores);

        // Fase 2: Desenho
        visaoCampo.prepararPintura();
        desenharCampo(grade);       // Passamos a interface
        visaoCampo.repaint();
    }
    
    /**
     * Coleta estatísticas do campo, ignorando classes especificadas.
     */
    private void coletarEstatisticas(GradeVisualizavel grade) {
        for (int linha = 0; linha < grade.getProfundidade(); linha++) {
            for (int coluna = 0; coluna < grade.getLargura(); coluna++) {
                Object objeto = grade.getObjetoEm(linha, coluna);
                
                // Verifica se existe e se NÃO está na lista de ignorados
                if (objeto != null && !classesIgnoradasNaContagem.contains(objeto.getClass())) {
                    estatisticas.incrementarContagem(objeto.getClass());
                }
            }
        }
    }

    /**
     * Determina a cor a ser usada para um agente específico.
     * Tenta buscar pelo objeto específico primeiro (Enums), depois pela classe.
     */
    private Color determinarCorAgente(Object objeto) {
        // 1. Tenta achar cor para o OBJETO ESPECÍFICO (ex: Obstaculo.RIO)
        Color cor = mapaDeCores.get(objeto);
        if (cor != null) {
            return cor;
        }

        // 2. Tenta achar cor para a CLASSE (ex: Raposa)
        cor = mapaDeCores.get(objeto.getClass());
        if (cor != null) {
            return cor;
        }

        return COR_DESCONHECIDA;
    }

    /**
     * Define a cor para uma CLASSE ou para um OBJETO ESPECÍFICO.
     */
    public void definirCor(Object chave, Color cor) {
        mapaDeCores.put(chave, cor);
    }

    /**
     * Atualiza as informações do clima na interface.
     */
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
    /**
     * Define a ação do botão de pausar/continuar.
     */
    @Override
    public void setAcaoPausar(ActionListener listener) {
        substituirListeners(botaoPausar, listener);
    }
    
    /**
     * Define a ação do botão de reiniciar.
     */
    @Override
    public void setAcaoReiniciar(ActionListener listener) {
        substituirListeners(botaoReiniciar, listener);
    }
    
    /**
     * Substitui todos os listeners de ação de um botão por um novo listener.
     * @param botao O botão cujo listener será substituído.
     * @param novoListener O novo listener a ser adicionado.
     */
    private void substituirListeners(JButton botao, ActionListener novoListener) {
        for (ActionListener al : botao.getActionListeners()) {
            botao.removeActionListener(al);
        }
        botao.addActionListener(novoListener);
    }
    
    /**
     * Define o texto do botão de pausa.
     */
    @Override
    public void setTextoBotaoPausa(String texto) {
        botaoPausar.setText(texto);
    }
    
    /**
     * Verifica se a simulação é viável com a grade fornecida.
     */
    @Override
    public boolean ehViavel(GradeVisualizavel grade) {
        return estatisticas.ehViavel(grade);
    }
    /**
     * Reinicia a visualização da simulação.
     */
    @Override
    public void reiniciar() {
        estatisticas.reiniciar();
        visaoCampo.prepararPintura();
        visaoCampo.repaint();
    }
    /**
     * Fecha a visualização e libera recursos.
     */
    @Override
    public void fechar() {
        dispose();
    }

    // --- INNER CLASS: PainelLegenda ---
    /**
     * Painel que exibe a legenda com as cores e contagens dos animais.
     */
    private class PainelLegenda extends JPanel {
        // AJUSTE: Tipos alterados para Object para compatibilidade com o mapa geral
        private Map<Object, Integer> contagensAtuais;
        private Map<Object, Color> coresAtuais;
        /**
         * Constrói o painel de legenda.
         */
        public PainelLegenda() {
            contagensAtuais = new HashMap<>();
            coresAtuais = new HashMap<>();
            setMinimumSize(new Dimension(400, 90));
            setPreferredSize(new Dimension(600, 90));
        }
        
        /**
         * Atualiza os dados da legenda com as estatísticas atuais.
         */
        public void atualizarDados(EstatisticasCampo stats, Map<Object, Color> cores) {
            this.coresAtuais = new HashMap<>(cores);
            this.contagensAtuais.clear();

            for (Object chave : coresAtuais.keySet()) {
                // A estatística conta apenas Classes (Agentes).
                if (chave instanceof Class<?>) {
                    Class<?> classe = (Class<?>) chave;
                    
                    int count = stats.getContagem(classe); 
                    contagensAtuais.put(chave, count);
                }
            }
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // 1. Título
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            String titulo = "População";
            FontMetrics fmTitulo = g2d.getFontMetrics();
            int xTitulo = (getWidth() - fmTitulo.stringWidth(titulo)) / 2;
            g2d.drawString(titulo, xTitulo, 20);

            // 2. Configurações do Grid
            final int COLUNAS_FIXAS = 3; 
            final int TAMANHO_QUADRADO = 12;
            final int Y_BASE = 45;
            final int ESPACO_ENTRE_COLUNAS = 30; 
            g2d.setFont(new Font("Arial", Font.PLAIN, 14));
            FontMetrics fmItems = g2d.getFontMetrics();
            
            // AJUSTE: Lista de Objects, não apenas Classes
            List<Object> listaChaves = new ArrayList<>(contagensAtuais.keySet());
            if (listaChaves.isEmpty()) {
                g2d.dispose();
                return;
            }

            // 3. Calcula largura
            int larguraMaximaTexto = 0;
            for (Object chave : listaChaves) {
                if (chave instanceof Class<?>) {
                    String nome = ((Class<?>) chave).getSimpleName();
                    String txtSimulado = nome + ": 8888"; 
                    larguraMaximaTexto = Math.max(larguraMaximaTexto, fmItems.stringWidth(txtSimulado));
                }
            }
            // 4. Calcula posições iniciais
            int larguraCelula = TAMANHO_QUADRADO + 5 + larguraMaximaTexto + ESPACO_ENTRE_COLUNAS;
            int larguraTotalGrid = (COLUNAS_FIXAS * larguraCelula) - ESPACO_ENTRE_COLUNAS;
            int xInicial = (getWidth() - larguraTotalGrid) / 2;
            xInicial = Math.max(10, xInicial); 

            // 5. Loop de Desenho
            for (int i = 0; i < listaChaves.size(); i++) {
                // AJUSTE: Trabalha com Object
                Object chave = listaChaves.get(i);
                Color cor = coresAtuais.get(chave);
                Integer qtd = contagensAtuais.getOrDefault(chave, 0);
                // Calcula posição no grid
                int linhaGrid = i / COLUNAS_FIXAS; 
                int colunaGrid = i % COLUNAS_FIXAS; 

                int x = xInicial + (colunaGrid * larguraCelula);
                int y = Y_BASE + (linhaGrid * 25); 
                // Desenha retângulo colorido e texto
                g2d.setColor(cor);
                g2d.fillRect(x, y - TAMANHO_QUADRADO + 2, TAMANHO_QUADRADO, TAMANHO_QUADRADO);
                g2d.setColor(Color.BLACK);
                g2d.drawRect(x, y - TAMANHO_QUADRADO + 2, TAMANHO_QUADRADO, TAMANHO_QUADRADO);
                // Nome da classe ou descrição do objeto
                String nome = (chave instanceof Class<?>) ? ((Class<?>)chave).getSimpleName() : chave.toString();
                g2d.drawString(nome + ": " + qtd, x + TAMANHO_QUADRADO + 5, y);
            }
            g2d.dispose();
        }
    }

    // --- INNER CLASS: VisaoCampo (Manteve-se igual) ---
    /**
     * Componente que desenha o campo de simulação.
     */
    private class VisaoCampo extends JPanel {
        private final int FATOR_ESCALA_GRADE = 6;
        private int larguraGrade, alturaGrade;
        private int escalaX, escalaY;
        private Dimension tamanho;
        private Graphics gOffScreen; 
        private Image imagemCampo;
        /**
         * Constrói a visão do campo com dimensões especificadas.
         * @param altura altura do campo
         * @param largura largura do campo
         */
        public VisaoCampo(int altura, int largura) {
            alturaGrade = altura;
            larguraGrade = largura;
            tamanho = new Dimension(0, 0);
            setBackground(Color.WHITE);
        }
        /**
         * Retorna a dimensão preferida do componente.
         */
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(larguraGrade * FATOR_ESCALA_GRADE, alturaGrade * FATOR_ESCALA_GRADE);
        }
        
        /**
         * Prepara a pintura do campo, ajustando a escala e a imagem off-screen.
         */
        public void prepararPintura() {
            if (!tamanho.equals(getSize())) {
                tamanho = getSize();
                imagemCampo = createImage(tamanho.width, tamanho.height);
                gOffScreen = imagemCampo.getGraphics();

                escalaX = tamanho.width / larguraGrade;
                if (escalaX < 1) escalaX = FATOR_ESCALA_GRADE;
                escalaY = tamanho.height / alturaGrade;
                if (escalaY < 1) escalaY = FATOR_ESCALA_GRADE;
            }
        }
        /**
         * Desenha uma marca (retângulo) na posição especificada com a cor dada.
         * @param x coluna no campo
         * @param y linha no campo
         * @param cor cor do retângulo
         */
        public void desenharMarca(int x, int y, Color cor) {
            if (gOffScreen != null) {
                gOffScreen.setColor(cor);
                gOffScreen.fillRect(x * escalaX, y * escalaY, escalaX - 1, escalaY - 1);
            }
        }
        /**
         * Pinta o componente com a imagem do campo.
         */
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (imagemCampo != null) {
                g.drawImage(imagemCampo, 0, 0, null);
            }
        }
    }
}
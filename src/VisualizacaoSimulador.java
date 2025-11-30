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
 * Classe responsável pela interface gráfica do simulador de ecossistema.
 * <p>
 * Esta classe implementa a visualização completa da simulação, incluindo:
 * <ul>
 * <li>Renderização do campo de simulação com agentes e terreno</li>
 * <li>Exibição de estatísticas populacionais em tempo real</li>
 * <li>Controles de simulação (pausar, reiniciar)</li>
 * <li>Informações sobre clima e passo atual</li>
 * <li>Legenda com cores e contagens dos elementos</li>
 * </ul>
 * </p>
 * <p>
 * A classe utiliza o padrão de design Model-View para separar a lógica
 * de simulação da apresentação visual, comunicando-se através da interface
 * {@link Desenhavel} e {@link GradeVisualizavel}.
 * </p>
 * 
 * @author Grupo 10
 * @version 1.0
 * @see Desenhavel
 * @see GradeVisualizavel
 * @see EstatisticasCampo
 */
public class VisualizacaoSimulador extends JFrame implements Desenhavel {

    // ========== CONSTANTES DE COR ==========

    /**
     * Cor padrão para elementos desconhecidos ou sem cor definida.
     */
    private static final Color COR_DESCONHECIDA = Color.gray;

    /**
     * Cor da grama em estado maduro (crescimento completo).
     */
    private static final Color COR_GRAMA_MADURA = new Color(200, 255, 200);

    /**
     * Cor da grama em estado inicial (recém-crescida).
     */
    private static final Color COR_GRAMA_NOVA = new Color(240, 230, 140);

    // ========== CONSTANTES DE TEXTO ==========

    /**
     * Prefixo exibido antes do número do passo atual.
     */
    private final String PREFIXO_PASSO = "Passo: ";

    // ========== COMPONENTES DE INTERFACE ==========

    /**
     * Rótulo que exibe o número do passo atual da simulação.
     */
    private JLabel rotuloPasso;

    /**
     * Rótulo que exibe o estado climático atual.
     */
    private JLabel rotuloClima;

    /**
     * Botão para pausar ou continuar a simulação.
     */
    private JButton botaoPausar;

    /**
     * Botão para reiniciar a simulação do zero.
     */
    private JButton botaoReiniciar;

    /**
     * Painel que exibe a legenda com cores e contagens populacionais.
     */
    private PainelLegenda painelLegenda;

    /**
     * Componente responsável pela renderização visual do campo de simulação.
     */
    private VisaoCampo visaoCampo;

    // ========== ESTRUTURAS DE DADOS ==========

    /**
     * Mapa que associa objetos (classes ou instâncias específicas) às suas cores.
     * <p>
     * Chaves podem ser:
     * <ul>
     * <li>{@link Class} - para definir cor de todos os objetos de uma classe</li>
     * <li>Objetos específicos (ex: Enums) - para cores individuais</li>
     * </ul>
     * </p>
     */
    private HashMap<Object, Color> mapaDeCores;

    /**
     * Gerenciador de estatísticas populacionais do campo.
     */
    private EstatisticasCampo estatisticas;

    /**
     * Conjunto de classes que devem ser ignoradas na contagem estatística.
     * <p>
     * Útil para excluir elementos decorativos ou obstáculos da contagem
     * populacional.
     * </p>
     */
    private Set<Class<?>> classesIgnoradasNaContagem;

    // ========== CONSTRUTOR ==========

    /**
     * Constrói e inicializa a interface gráfica do simulador.
     * <p>
     * Configura todos os componentes visuais, incluindo:
     * <ul>
     * <li>Janela principal com título e posição</li>
     * <li>Painel de informações (passo e clima)</li>
     * <li>Campo de visualização do ecossistema</li>
     * <li>Legenda de cores e populações</li>
     * <li>Botões de controle da simulação</li>
     * </ul>
     * </p>
     * 
     * @param altura       Altura do campo de simulação em células
     * @param largura      Largura do campo de simulação em células
     * @param estatisticas Objeto responsável por calcular e armazenar estatísticas
     */
    public VisualizacaoSimulador(int altura, int largura, EstatisticasCampo estatisticas) {
        this.estatisticas = estatisticas;
        this.mapaDeCores = new HashMap<>();
        this.classesIgnoradasNaContagem = new HashSet<>();

        configurarJanela();
        criarComponentes(altura, largura);
        setVisible(true);
    }

    // ========== MÉTODOS DE CONFIGURAÇÃO DA INTERFACE ==========

    /**
     * Configura as propriedades básicas da janela principal.
     * <p>
     * Define título, posição inicial e comportamento de fechamento.
     * </p>
     */
    private void configurarJanela() {
        setTitle("Simulador Ecossistema - Grupo 10");
        setLocation(100, 50);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Cria e organiza todos os componentes da interface gráfica.
     * <p>
     * Utiliza BorderLayout para organizar:
     * <ul>
     * <li>NORTH: Informações de passo e clima</li>
     * <li>CENTER: Campo de visualização</li>
     * <li>SOUTH: Legenda e controles</li>
     * </ul>
     * </p>
     * 
     * @param altura  Altura do campo de simulação
     * @param largura Largura do campo de simulação
     */
    private void criarComponentes(int altura, int largura) {
        JPanel painelSuperior = criarPainelSuperior();
        visaoCampo = new VisaoCampo(altura, largura);
        JPanel painelInferior = criarPainelInferior();

        Container conteudo = getContentPane();
        conteudo.add(painelSuperior, BorderLayout.NORTH);
        conteudo.add(visaoCampo, BorderLayout.CENTER);
        conteudo.add(painelInferior, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Cria o painel superior contendo informações de passo e clima.
     * <p>
     * O painel é dividido em duas seções iguais usando GridLayout:
     * <ul>
     * <li>Esquerda: Número do passo atual</li>
     * <li>Direita: Estado climático com ícone</li>
     * </ul>
     * </p>
     * 
     * @return Painel configurado com rótulos de informação
     */
    private JPanel criarPainelSuperior() {
        JPanel p = new JPanel(new GridLayout(1, 2));
        p.setPreferredSize(new Dimension(0, 40));

        rotuloPasso = new JLabel(PREFIXO_PASSO, JLabel.CENTER);

        rotuloClima = new JLabel("Clima: NORMAL", JLabel.CENTER);
        rotuloClima.setOpaque(true);
        rotuloClima.setBackground(new Color(135, 206, 235));

        p.add(rotuloPasso);
        p.add(rotuloClima);
        return p;
    }

    /**
     * Cria o painel inferior contendo legenda e botões de controle.
     * <p>
     * Estrutura do painel:
     * <ul>
     * <li>CENTER: Legenda com cores e contagens populacionais</li>
     * <li>SOUTH: Botões de pausar e reiniciar</li>
     * </ul>
     * </p>
     * 
     * @return Painel configurado com legenda e controles
     */
    private JPanel criarPainelInferior() {
        JPanel p = new JPanel(new BorderLayout());
        p.setPreferredSize(new Dimension(0, 140));

        painelLegenda = new PainelLegenda();
        painelLegenda.setBorder(new EmptyBorder(5, 5, 5, 5));

        JPanel painelBotoes = new JPanel(new FlowLayout());
        painelBotoes.setPreferredSize(new Dimension(0, 40));

        botaoPausar = new JButton("Pausar");
        botaoPausar.setPreferredSize(new Dimension(100, 30));

        botaoReiniciar = new JButton("Resetar Simulação");
        botaoReiniciar.setPreferredSize(new Dimension(150, 30));

        painelBotoes.add(botaoPausar);
        painelBotoes.add(botaoReiniciar);

        p.add(painelLegenda, BorderLayout.CENTER);
        p.add(painelBotoes, BorderLayout.SOUTH);
        return p;
    }

    // ========== MÉTODOS DE RENDERIZAÇÃO ==========

    /**
     * Percorre e desenha todos os elementos do campo de simulação.
     * <p>
     * Para cada célula do campo:
     * <ol>
     * <li>Verifica se contém um agente (objeto)</li>
     * <li>Define cor do agente ou cor do terreno (grama)</li>
     * <li>Desenha o pixel correspondente</li>
     * </ol>
     * </p>
     * 
     * @param grade Interface de acesso aos dados do campo
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
     * Determina a cor apropriada para renderizar um agente específico.
     * <p>
     * Estratégia de busca de cor:
     * <ol>
     * <li>Tenta buscar cor para o objeto específico (ex: enum OBSTACULO.RIO)</li>
     * <li>Se não encontrar, busca cor pela classe do objeto</li>
     * <li>Se não encontrar, retorna {@link #COR_DESCONHECIDA}</li>
     * </ol>
     * </p>
     * 
     * @param objeto Agente ou elemento do campo
     * @return Cor associada ao objeto ou cor padrão para desconhecidos
     */
    private Color determinarCorAgente(Object objeto) {
        Color cor = mapaDeCores.get(objeto);
        if (cor != null) {
            return cor;
        }

        cor = mapaDeCores.get(objeto.getClass());
        if (cor != null) {
            return cor;
        }

        return COR_DESCONHECIDA;
    }

    // ========== MÉTODOS DE GERENCIAMENTO DE DADOS ==========

    /**
     * Coleta estatísticas populacionais do campo atual.
     * <p>
     * Percorre todas as células e incrementa contadores para cada tipo de agente,
     * ignorando classes marcadas em {@link #classesIgnoradasNaContagem}.
     * </p>
     * 
     * @param grade Interface de acesso aos dados do campo
     */
    private void coletarEstatisticas(GradeVisualizavel grade) {
        for (int linha = 0; linha < grade.getProfundidade(); linha++) {
            for (int coluna = 0; coluna < grade.getLargura(); coluna++) {
                Object objeto = grade.getObjetoEm(linha, coluna);

                if (objeto != null && !classesIgnoradasNaContagem.contains(objeto.getClass())) {
                    estatisticas.incrementarContagem(objeto.getClass());
                }
            }
        }
    }

    /**
     * Adiciona uma classe para ser ignorada nas estatísticas populacionais.
     * <p>
     * Útil para excluir elementos decorativos, obstáculos ou outros
     * elementos que não devem aparecer na contagem de população.
     * </p>
     * 
     * @param classe Classe do elemento a ser ignorado nas contagens
     */
    public void adicionarClasseIgnorada(Class<?> classe) {
        classesIgnoradasNaContagem.add(classe);
    }

    // ========== IMPLEMENTAÇÃO DA INTERFACE DESENHAVEL ==========

    /**
     * Atualiza a visualização completa da simulação.
     * <p>
     * Processo de atualização:
     * <ol>
     * <li>Atualiza número do passo</li>
     * <li>Coleta estatísticas populacionais</li>
     * <li>Atualiza legenda com novos dados</li>
     * <li>Renderiza o campo visual</li>
     * </ol>
     * </p>
     * 
     * @param passo Número do passo atual da simulação
     * @param grade Interface de acesso aos dados do campo
     */
    @Override
    public void mostrarStatus(int passo, GradeVisualizavel grade) {
        if (!isVisible())
            setVisible(true);

        rotuloPasso.setText(PREFIXO_PASSO + passo);

        estatisticas.reiniciar();
        coletarEstatisticas(grade);
        estatisticas.contagemConcluida();

        painelLegenda.atualizarDados(estatisticas, mapaDeCores);

        visaoCampo.prepararPintura();
        desenharCampo(grade);
        visaoCampo.repaint();
    }

    /**
     * Define a cor de renderização para uma classe ou objeto específico.
     * <p>
     * Exemplos de uso:
     * <ul>
     * <li>{@code definirCor(Raposa.class, Color.ORANGE)} - todas as raposas</li>
     * <li>{@code definirCor(Obstaculo.RIO, Color.BLUE)} - instância específica</li>
     * </ul>
     * </p>
     * 
     * @param chave Classe ou objeto específico (ex: enum)
     * @param cor   Cor a ser usada na renderização
     */
    public void definirCor(Object chave, Color cor) {
        mapaDeCores.put(chave, cor);
    }

    /**
     * Atualiza as informações climáticas na interface.
     * <p>
     * Ajusta o visual do rótulo de clima baseado no estado:
     * <ul>
     * <li>Chuvoso: fundo azul escuro, texto branco, ícone de informação</li>
     * <li>Normal: fundo amarelo claro, texto preto, ícone de aviso</li>
     * </ul>
     * </p>
     * 
     * @param texto   Descrição textual do clima (ex: "Clima: CHUVA")
     * @param chuvoso true se está chovendo, false caso contrário
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
     * Define a ação a ser executada quando o botão de pausar for clicado.
     * <p>
     * Remove todos os listeners anteriores antes de adicionar o novo.
     * </p>
     * 
     * @param listener Listener que será notificado ao clicar no botão
     */
    @Override
    public void setAcaoPausar(ActionListener listener) {
        substituirListeners(botaoPausar, listener);
    }

    /**
     * Define a ação a ser executada quando o botão de reiniciar for clicado.
     * <p>
     * Remove todos os listeners anteriores antes de adicionar o novo.
     * </p>
     * 
     * @param listener Listener que será notificado ao clicar no botão
     */
    @Override
    public void setAcaoReiniciar(ActionListener listener) {
        substituirListeners(botaoReiniciar, listener);
    }

    /**
     * Substitui todos os ActionListeners de um botão por um novo.
     * <p>
     * Método auxiliar para evitar acúmulo de listeners duplicados.
     * </p>
     * 
     * @param botao        Botão cujos listeners serão substituídos
     * @param novoListener Novo listener a ser adicionado
     */
    private void substituirListeners(JButton botao, ActionListener novoListener) {
        for (ActionListener al : botao.getActionListeners()) {
            botao.removeActionListener(al);
        }
        botao.addActionListener(novoListener);
    }

    /**
     * Atualiza o texto exibido no botão de pausa/continuar.
     * 
     * @param texto Novo texto do botão (ex: "Pausar" ou "Continuar")
     */
    @Override
    public void setTextoBotaoPausa(String texto) {
        botaoPausar.setText(texto);
    }

    /**
     * Verifica se a simulação é viável com base nas estatísticas atuais.
     * <p>
     * Delega a verificação para o objeto de estatísticas.
     * </p>
     * 
     * @param grade Interface de acesso aos dados do campo
     * @return true se a simulação pode continuar, false caso contrário
     */
    @Override
    public boolean ehViavel(GradeVisualizavel grade) {
        return estatisticas.ehViavel(grade);
    }

    /**
     * Reinicia o estado visual da simulação.
     * <p>
     * Limpa estatísticas e prepara a área de desenho para nova simulação.
     * </p>
     */
    @Override
    public void reiniciar() {
        estatisticas.reiniciar();
        visaoCampo.prepararPintura();
        visaoCampo.repaint();
    }

    /**
     * Fecha a janela de visualização e libera recursos do sistema.
     */
    @Override
    public void fechar() {
        dispose();
    }

    // ========== CLASSE INTERNA: PAINEL DE LEGENDA ==========

    /**
     * Painel customizado que exibe a legenda de cores e contagens populacionais.
     * <p>
     * Renderiza em formato de grid com 3 colunas:
     * <ul>
     * <li>Quadrado colorido representando o tipo de agente</li>
     * <li>Nome da classe do agente</li>
     * <li>Contagem atual da população</li>
     * </ul>
     * </p>
     * <p>
     * A legenda é atualizada automaticamente a cada passo da simulação,
     * refletindo mudanças nas populações em tempo real.
     * </p>
     */
    private class PainelLegenda extends JPanel {

        /**
         * Mapa com as contagens atuais de cada tipo de agente.
         * Chaves são objetos (geralmente Classes) e valores são as quantidades.
         */
        private Map<Object, Integer> contagensAtuais;

        /**
         * Mapa com as cores associadas a cada tipo de agente.
         * Sincronizado com {@link VisualizacaoSimulador#mapaDeCores}.
         */
        private Map<Object, Color> coresAtuais;

        /**
         * Constrói o painel de legenda com dimensões padrão.
         */
        public PainelLegenda() {
            contagensAtuais = new HashMap<>();
            coresAtuais = new HashMap<>();
            setMinimumSize(new Dimension(400, 90));
            setPreferredSize(new Dimension(600, 90));
        }

        /**
         * Atualiza os dados da legenda com as estatísticas mais recentes.
         * <p>
         * Sincroniza cores e contagens, considerando apenas classes de agentes
         * (ignora objetos individuais como enums para a contagem).
         * </p>
         * 
         * @param stats Objeto com estatísticas populacionais atualizadas
         * @param cores Mapa de cores atualizado
         */
        public void atualizarDados(EstatisticasCampo stats, Map<Object, Color> cores) {
            this.coresAtuais = new HashMap<>(cores);
            this.contagensAtuais.clear();

            for (Object chave : coresAtuais.keySet()) {
                if (chave instanceof Class<?>) {
                    Class<?> classe = (Class<?>) chave;
                    int count = stats.getContagem(classe);
                    contagensAtuais.put(chave, count);
                }
            }
            repaint();
        }

        /**
         * Renderiza o painel de legenda com título, cores e contagens.
         * <p>
         * Layout da renderização:
         * <ol>
         * <li>Título "População" centralizado no topo</li>
         * <li>Grid de 3 colunas com as entradas da legenda</li>
         * <li>Cada entrada mostra: [cor] Nome: quantidade</li>
         * </ol>
         * </p>
         * 
         * @param g Contexto gráfico para desenho
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Desenha título
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            String titulo = "População";
            FontMetrics fmTitulo = g2d.getFontMetrics();
            int xTitulo = (getWidth() - fmTitulo.stringWidth(titulo)) / 2;
            g2d.drawString(titulo, xTitulo, 20);

            // Configurações do grid
            final int COLUNAS_FIXAS = 3;
            final int TAMANHO_QUADRADO = 12;
            final int Y_BASE = 45;
            final int ESPACO_ENTRE_COLUNAS = 30;
            g2d.setFont(new Font("Arial", Font.PLAIN, 14));
            FontMetrics fmItems = g2d.getFontMetrics();

            List<Object> listaChaves = new ArrayList<>(contagensAtuais.keySet());
            if (listaChaves.isEmpty()) {
                g2d.dispose();
                return;
            }

            // Calcula largura máxima necessária
            int larguraMaximaTexto = 0;
            for (Object chave : listaChaves) {
                if (chave instanceof Class<?>) {
                    String nome = ((Class<?>) chave).getSimpleName();
                    String txtSimulado = nome + ": 8888";
                    larguraMaximaTexto = Math.max(larguraMaximaTexto, fmItems.stringWidth(txtSimulado));
                }
            }

            // Calcula posições iniciais centralizadas
            int larguraCelula = TAMANHO_QUADRADO + 5 + larguraMaximaTexto + ESPACO_ENTRE_COLUNAS;
            int larguraTotalGrid = (COLUNAS_FIXAS * larguraCelula) - ESPACO_ENTRE_COLUNAS;
            int xInicial = (getWidth() - larguraTotalGrid) / 2;
            xInicial = Math.max(10, xInicial);

            // Desenha cada entrada da legenda
            for (int i = 0; i < listaChaves.size(); i++) {
                Object chave = listaChaves.get(i);
                Color cor = coresAtuais.get(chave);
                Integer qtd = contagensAtuais.getOrDefault(chave, 0);

                int linhaGrid = i / COLUNAS_FIXAS;
                int colunaGrid = i % COLUNAS_FIXAS;

                int x = xInicial + (colunaGrid * larguraCelula);
                int y = Y_BASE + (linhaGrid * 25);

                // Desenha quadrado colorido
                g2d.setColor(cor);
                g2d.fillRect(x, y - TAMANHO_QUADRADO + 2, TAMANHO_QUADRADO, TAMANHO_QUADRADO);
                g2d.setColor(Color.BLACK);
                g2d.drawRect(x, y - TAMANHO_QUADRADO + 2, TAMANHO_QUADRADO, TAMANHO_QUADRADO);

                // Desenha texto com nome e contagem
                String nome = (chave instanceof Class<?>) ? ((Class<?>) chave).getSimpleName() : chave.toString();
                g2d.drawString(nome + ": " + qtd, x + TAMANHO_QUADRADO + 5, y);
            }
            g2d.dispose();
        }
    }

    // ========== CLASSE INTERNA: VISÃO DO CAMPO ==========

    /**
     * Componente Swing que renderiza o campo de simulação como imagem.
     * <p>
     * Implementa double buffering para renderização suave:
     * <ol>
     * <li>Desenha em uma imagem off-screen ({@link #imagemCampo})</li>
     * <li>Transfere a imagem completa para a tela de uma vez</li>
     * </ol>
     * </p>
     * <p>
     * Cada célula do campo lógico é representada por um retângulo colorido,
     * com escala automática baseada no tamanho do componente.
     * </p>
     */
    private class VisaoCampo extends JPanel {

        /**
         * Fator mínimo de escala para visualização.
         * Define o tamanho mínimo de cada célula em pixels.
         */
        private final int FATOR_ESCALA_GRADE = 6;

        /**
         * Largura do campo em células.
         */
        private int larguraGrade;

        /**
         * Altura do campo em células.
         */
        private int alturaGrade;

        /**
         * Escala horizontal atual (pixels por célula).
         */
        private int escalaX;

        /**
         * Escala vertical atual (pixels por célula).
         */
        private int escalaY;

        /**
         * Tamanho atual do componente, usado para detectar redimensionamento.
         */
        private Dimension tamanho;

        /**
         * Contexto gráfico da imagem off-screen para double buffering.
         */
        private Graphics gOffScreen;

        /**
         * Imagem off-screen onde o campo é desenhado antes de aparecer na tela.
         */
        private Image imagemCampo;

        /**
         * Constrói a visão do campo com dimensões especificadas.
         * 
         * @param altura  Altura do campo em células
         * @param largura Largura do campo em células
         */
        public VisaoCampo(int altura, int largura) {
            alturaGrade = altura;
            larguraGrade = largura;
            tamanho = new Dimension(0, 0);
            setBackground(Color.WHITE);
        }

        /**
         * Define o tamanho preferido do componente baseado no campo.
         * 
         * @return Dimensão preferida (células × fator de escala)
         */
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(larguraGrade * FATOR_ESCALA_GRADE, alturaGrade * FATOR_ESCALA_GRADE);
        }

        /**
         * Prepara a renderização, ajustando escala e criando buffer se necessário.
         * <p>
         * Recalcula escala quando o tamanho do componente muda.
         * Mantém escala mínima de {@link #FATOR_ESCALA_GRADE} para garantir
         * visibilidade.
         * </p>
         */
        public void prepararPintura() {
            if (!tamanho.equals(getSize())) {
                tamanho = getSize();
                imagemCampo = createImage(tamanho.width, tamanho.height);
                gOffScreen = imagemCampo.getGraphics();

                escalaX = tamanho.width / larguraGrade;
                if (escalaX < 1)
                    escalaX = FATOR_ESCALA_GRADE;
                escalaY = tamanho.height / alturaGrade;
                if (escalaY < 1)
                    escalaY = FATOR_ESCALA_GRADE;
            }
        }

        /**
         * Desenha um retângulo colorido representando uma célula do campo.
         * <p>
         * O desenho é feito na imagem off-screen para depois ser
         * transferido para a tela via {@link #paintComponent(Graphics)}.
         * </p>
         * 
         * @param x   Coordenada X (coluna) no campo lógico
         * @param y   Coordenada Y (linha) no campo lógico
         * @param cor Cor do retângulo a ser desenhado
         */
        public void desenharMarca(int x, int y, Color cor) {
            if (gOffScreen != null) {
                gOffScreen.setColor(cor);
                gOffScreen.fillRect(x * escalaX, y * escalaY, escalaX - 1, escalaY - 1);
            }
        }

        /**
         * Renderiza o componente transferindo a imagem off-screen para a tela.
         * <p>
         * Implementa double buffering para evitar flickering durante animações.
         * </p>
         * 
         * @param g Contexto gráfico da tela
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
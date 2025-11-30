import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Classe principal que gerencia a simulação de um ecossistema completo.
 * <p>
 * Esta classe coordena todos os aspectos da simulação, incluindo:
 * <ul>
 * <li>Gerenciamento do ciclo de vida de predadores e presas</li>
 * <li>Controle do sistema climático e seus efeitos</li>
 * <li>Crescimento e regeneração de vegetação</li>
 * <li>Obstáculos e terreno do ambiente</li>
 * <li>Controles de pausar, continuar e reiniciar</li>
 * <li>Visualização em tempo real das estatísticas</li>
 * </ul>
 * </p>
 * 
 * <p>
 * <strong>Arquitetura da Simulação:</strong>
 * </p>
 * 
 * <pre>
 * Simulador (Controlador)
 *    ├── Campo (Modelo - estado atual)
 *    ├── Campo (Modelo - próximo estado)
 *    ├── Desenhavel (View - interface gráfica)
 *    ├── EstatisticasCampo (Coleta de dados)
 *    ├── Clima (Sistema ambiental)
 *    └── Lista de Atores (Animais)
 * </pre>
 * 
 * <p>
 * <strong>Fluxo de um Passo de Simulação:</strong>
 * </p>
 * <ol>
 * <li>Preparar listas e campos auxiliares</li>
 * <li>Aplicar obstáculos fixos no campo de destino</li>
 * <li>Processar ações de cada animal (caçar, mover, reproduzir)</li>
 * <li>Atualizar clima e crescimento de vegetação</li>
 * <li>Trocar campos (atual ↔ atualizado)</li>
 * <li>Atualizar visualização</li>
 * </ol>
 * 
 * <p>
 * <strong>Carregamento de Mapas:</strong>
 * </p>
 * <p>
 * Suporta carregamento de mapas de obstáculos a partir de arquivos de texto,
 * permitindo criação de cenários customizados com rios e pedras.
 * </p>
 * 
 * @author David J. Barnes e Michael Kolling
 * @author Grupo 10
 * @version 2025-11-30 (traduzido e modificado)
 * @see Campo
 * @see Ator
 * @see Desenhavel
 * @see Clima
 * @see EstatisticasCampo
 */
public class Simulador {

    // ========== ATRIBUTOS PRINCIPAIS ==========

    /**
     * Lista de todos os animais atualmente vivos na simulação.
     */
    private List<Ator> animais;

    /**
     * Lista temporária para armazenar animais nascidos durante um passo.
     * É mesclada com {@link #animais} ao final de cada ciclo.
     */
    private List<Ator> novosAnimais;

    /**
     * Campo representando o estado atual da simulação (leitura).
     */
    private Campo campo;

    /**
     * Campo sendo construído para o próximo estado (escrita).
     * Implementa double buffering para evitar conflitos.
     */
    private Campo campoAtualizado;

    /**
     * Contador do número de passos executados desde o início ou último reinício.
     */
    private int passo;

    /**
     * Interface de visualização responsável por renderizar o estado da simulação.
     */
    private Desenhavel visualizacao;

    /**
     * Sistema de clima que afeta o crescimento da vegetação.
     */
    private Clima clima;

    /**
     * Flag indicando se a simulação está pausada.
     */
    private boolean pausada;

    /**
     * Flag indicando se há uma simulação em execução (pausada ou não).
     */
    private boolean emExecucao;

    /**
     * Matriz de obstáculos fixos carregados do mapa.
     * Mantida em memória para reaplicação a cada passo.
     */
    private Obstaculo[][] mapaFixo;

    /**
     * Objeto compartilhado entre Simulador e Visualização para coleta de
     * estatísticas.
     */
    private EstatisticasCampo estatisticas;

    // ========== CONSTRUTORES ==========

    /**
     * Constrói um simulador com dimensões padrão.
     * <p>
     * Utiliza valores definidos em {@link Configuracao#PROFUNDIDADE_PADRAO}
     * e {@link Configuracao#LARGURA_PADRAO}.
     * </p>
     */
    public Simulador() {
        this(Configuracao.PROFUNDIDADE_PADRAO, Configuracao.LARGURA_PADRAO);
    }

    /**
     * Cria um simulador com dimensões customizadas.
     * <p>
     * Este construtor inicia a cadeia de injeção de dependências,
     * criando primeiro o objeto de estatísticas que será compartilhado.
     * </p>
     * 
     * @param profundidade Altura do campo em células
     * @param largura      Largura do campo em células
     */
    public Simulador(int profundidade, int largura) {
        this(profundidade, largura, new EstatisticasCampo());
    }

    /**
     * Construtor auxiliar privado para injeção de dependências.
     * <p>
     * Cria a visualização injetando o objeto de estatísticas compartilhado,
     * garantindo que Simulador e Visualização trabalhem com os mesmos dados.
     * </p>
     * 
     * @param profundidade              Altura do campo
     * @param largura                   Largura do campo
     * @param estatisticasCompartilhada Objeto de estatísticas a ser compartilhado
     */
    private Simulador(int profundidade, int largura, EstatisticasCampo estatisticasCompartilhada) {
        this(profundidade, largura,
                new VisualizacaoSimulador(profundidade, largura, estatisticasCompartilhada),
                estatisticasCompartilhada);
    }

    /**
     * Construtor mestre que recebe todas as dependências prontas.
     * <p>
     * Este é o construtor final da cadeia, responsável por:
     * <ul>
     * <li>Validar dimensões do campo</li>
     * <li>Inicializar estruturas de dados</li>
     * <li>Configurar visualização e cores</li>
     * <li>Carregar mapa de obstáculos</li>
     * <li>Popular o campo inicial</li>
     * <li>Configurar controles da interface</li>
     * </ul>
     * </p>
     * 
     * @param profundidade Altura do campo
     * @param largura      Largura do campo
     * @param visualizacao Interface de visualização já configurada
     * @param estatisticas Objeto de estatísticas já configurado
     */
    public Simulador(int profundidade, int largura, Desenhavel visualizacao, EstatisticasCampo estatisticas) {
        // Valida dimensões
        if (largura <= 0 || profundidade <= 0) {
            System.out.println("As dimensões devem ser maiores que zero.");
            System.out.println("Usando valores padrão.");
            profundidade = Configuracao.PROFUNDIDADE_PADRAO;
            largura = Configuracao.LARGURA_PADRAO;
        }

        this.estatisticas = estatisticas;
        this.visualizacao = visualizacao;

        animais = new ArrayList<Ator>();
        novosAnimais = new ArrayList<Ator>();
        campo = new Campo(profundidade, largura);
        campoAtualizado = new Campo(profundidade, largura);

        definirCores();

        this.clima = new Clima(50);
        this.pausada = false;
        this.emExecucao = false;

        carregarMapa("mapa.txt");

        reiniciar();

        configurarInterface();
    }

    // ========== MÉTODOS DE CONFIGURAÇÃO ==========

    /**
     * Define o mapeamento de cores para cada tipo de entidade.
     * <p>
     * Cores configuradas:
     * <ul>
     * <li>Raposa → Azul</li>
     * <li>Coelho → Laranja</li>
     * <li>Rato → Magenta</li>
     * <li>Cobra → Verde</li>
     * <li>Gavião → Vermelho</li>
     * <li>Urso → Preto</li>
     * <li>Rio → Ciano</li>
     * <li>Pedra → Cinza escuro</li>
     * </ul>
     * </p>
     * <p>
     * Também configura obstáculos para serem ignorados nas estatísticas
     * populacionais.
     * </p>
     */
    private void definirCores() {
        this.visualizacao.definirCor(Raposa.class, Color.blue);
        this.visualizacao.definirCor(Coelho.class, Color.orange);
        this.visualizacao.definirCor(Rato.class, Color.MAGENTA);
        this.visualizacao.definirCor(Cobra.class, Color.GREEN);
        this.visualizacao.definirCor(Gaviao.class, Color.RED);
        this.visualizacao.definirCor(Urso.class, Color.BLACK);

        visualizacao.definirCor(Obstaculo.RIO, Color.CYAN);
        visualizacao.definirCor(Obstaculo.PEDRA, Color.DARK_GRAY);

        if (visualizacao instanceof VisualizacaoSimulador) {
            ((VisualizacaoSimulador) visualizacao).adicionarClasseIgnorada(Obstaculo.class);
        }
    }

    /**
     * Configura os listeners dos botões da interface gráfica.
     * <p>
     * Botões configurados:
     * <ul>
     * <li><strong>Pausar/Continuar:</strong> Alterna entre pausar e continuar a
     * simulação</li>
     * <li><strong>Reiniciar:</strong> Reseta a simulação para o estado inicial</li>
     * </ul>
     * </p>
     * <p>
     * Utiliza classes anônimas para implementar os ActionListeners.
     * </p>
     */
    private void configurarInterface() {
        visualizacao.setAcaoPausar(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pausada) {
                    continuar();
                    visualizacao.setTextoBotaoPausa("Pausar");
                } else {
                    pausar();
                    visualizacao.setTextoBotaoPausa("Continuar");
                }
            }
        });

        visualizacao.setAcaoReiniciar(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pausar();

                try {
                    Thread.sleep(200);
                } catch (InterruptedException ex) {
                }

                reiniciar();

                visualizacao.setTextoBotaoPausa("Pausar");
            }
        });
    }

    // ========== MÉTODOS DE CONTROLE DE EXECUÇÃO ==========

    /**
     * Pausa a simulação em execução.
     * <p>
     * A simulação pode ser retomada posteriormente com {@link #continuar()}.
     * Se a simulação já estiver pausada ou não estiver em execução, não há efeito.
     * </p>
     */
    private void pausar() {
        if (emExecucao && !pausada) {
            pausada = true;
        }
    }

    /**
     * Retoma uma simulação pausada.
     * <p>
     * Se a simulação não estiver pausada, não há efeito.
     * </p>
     */
    public void continuar() {
        if (pausada) {
            pausada = false;
        }
    }

    /**
     * Verifica se a simulação está pausada.
     * 
     * @return true se pausada, false caso contrário
     */
    public boolean estaPausada() {
        return pausada;
    }

    /**
     * Verifica se há uma simulação em execução (pausada ou não).
     * 
     * @return true se em execução, false caso contrário
     */
    public boolean estaEmExecucao() {
        return emExecucao;
    }

    /**
     * Reinicia completamente a simulação para o estado inicial.
     * <p>
     * <strong>Operações realizadas:</strong>
     * <ol>
     * <li>Reseta contador de passos para 0</li>
     * <li>Limpa todas as listas de animais</li>
     * <li>Limpa ambos os campos (atual e atualizado)</li>
     * <li>Reinicia o sistema de clima</li>
     * <li>Remove estado de pausa</li>
     * <li>Aplica obstáculos fixos do mapa</li>
     * <li>Popula o campo com animais iniciais</li>
     * <li>Atualiza visualização</li>
     * </ol>
     * </p>
     */
    public void reiniciar() {
        passo = 0;

        animais.clear();
        novosAnimais.clear();

        campo.limpar();
        campoAtualizado.limpar();

        if (clima != null) {
            clima.reiniciar();
        }

        pausada = false;
        visualizacao.setTextoBotaoPausa("Pausar");

        aplicarObstaculos(campo);
        new Populador().popular(campo, animais);

        visualizacao.reiniciar();
        visualizacao.mostrarStatus(passo, campo);
    }

    /**
     * Executa a simulação pelo número especificado de passos.
     * <p>
     * <strong>Comportamento:</strong>
     * <ul>
     * <li>Respeita comandos de pausa durante a execução</li>
     * <li>Para automaticamente se a simulação se tornar inviável</li>
     * <li>Para ao atingir o número de passos solicitado</li>
     * <li>Aguarda 50ms entre cada passo para visualização</li>
     * </ul>
     * </p>
     * 
     * @param numPassos Número de passos a executar
     */
    public void simular(int numPassos) {
        emExecucao = true;

        while (emExecucao) {

            if (passo >= numPassos) {
                if (!pausada) {
                    pausar();
                    visualizacao.setTextoBotaoPausa("Continuar");
                }
            }

            if (!visualizacao.ehViavel(campo)) {
                if (!pausada) {
                    pausar();
                    visualizacao.setTextoBotaoPausa("Continuar");
                }
            }

            while (pausada) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            simularUmPasso();

            try {
                Thread.sleep(50);
            } catch (Exception e) {
            }
        }
    }

    // ========== MÉTODOS DE SIMULAÇÃO ==========

    /**
     * Simula um único passo da simulação.
     * <p>
     * <strong>Sequência de operações:</strong>
     * <ol>
     * <li>Incrementa contador de passos</li>
     * <li>Prepara listas auxiliares</li>
     * <li>Aplica obstáculos no campo de destino</li>
     * <li>Processa ações de todos os animais</li>
     * <li>Atualiza clima e vegetação</li>
     * <li>Troca campos (double buffering)</li>
     * <li>Atualiza visualização</li>
     * </ol>
     * </p>
     */
    public void simularUmPasso() {
        passo++;
        prepararNovosAnimais();
        prepararObstaculos();
        processarCicloDeVidaAnimais();
        processarAmbienteEClima();
        trocarCampos();
        visualizacao.mostrarStatus(passo, campo);
    }

    /**
     * Limpa a lista de animais nascidos do passo anterior.
     * <p>
     * Preparação para receber novos nascimentos no passo atual.
     * </p>
     */
    private void prepararNovosAnimais() {
        novosAnimais.clear();
    }

    /**
     * Aplica obstáculos fixos no campo de destino.
     * <p>
     * Necessário porque o campo atualizado é limpo a cada ciclo.
     * </p>
     */
    private void prepararObstaculos() {
        aplicarObstaculos(campoAtualizado);
    }

    /**
     * Processa o ciclo de vida de todos os animais.
     * <p>
     * <strong>Para cada animal:</strong>
     * <ol>
     * <li>Verifica se está vivo</li>
     * <li>Se vivo: executa suas ações (caçar, mover, reproduzir)</li>
     * <li>Se morto: remove da lista</li>
     * </ol>
     * </p>
     * <p>
     * Ao final, adiciona todos os filhotes nascidos neste turno à lista principal.
     * </p>
     */
    private void processarCicloDeVidaAnimais() {
        Iterator<Ator> iter = animais.iterator();
        while (iter.hasNext()) {
            Ator ator = iter.next();
            if (ator.estaVivo()) {
                ator.agir(campo, campoAtualizado, novosAnimais);
            } else {
                iter.remove();
            }
        }

        animais.addAll(novosAnimais);
    }

    /**
     * Gerencia o ambiente: clima e crescimento de vegetação.
     * <p>
     * <strong>Processo:</strong>
     * <ol>
     * <li>Copia estado da vegetação do campo atual</li>
     * <li>Se houver sistema de clima: atualiza estado climático</li>
     * <li>Faz vegetação crescer uma vez</li>
     * <li>Se estiver chovendo: cresce novamente (crescimento dobrado)</li>
     * <li>Atualiza interface com informações climáticas</li>
     * </ol>
     * </p>
     */
    private void processarAmbienteEClima() {
        campoAtualizado.copiarGramaDe(campo);

        if (clima == null) {
            campoAtualizado.crescerGrama();
            return;
        }

        clima.atualizar();
        campoAtualizado.crescerGrama();

        if (clima.estaChuvoso()) {
            campoAtualizado.crescerGrama();
        }

        String textoClima = clima.estaChuvoso() ? "Clima: CHUVOSO (Crescimento Rápido)" : "Clima: NORMAL";
        visualizacao.setInfoClima(textoClima, clima.estaChuvoso());
    }

    /**
     * Realiza a troca de referências entre campos (double buffering).
     * <p>
     * O campo atualizado se torna o atual, e o antigo atual é limpo
     * para servir como buffer de escrita no próximo passo.
     * </p>
     */
    private void trocarCampos() {
        Campo temp = campo;
        campo = campoAtualizado;
        campoAtualizado = temp;
        campoAtualizado.limpar();
    }

    // ========== MÉTODOS DE CARREGAMENTO DE MAPA ==========

    /**
     * Carrega um mapa de obstáculos de um arquivo de texto.
     * <p>
     * Se as dimensões do mapa forem diferentes das atuais,
     * redimensiona toda a simulação automaticamente.
     * </p>
     * 
     * @param caminhoArquivo Caminho do arquivo de mapa (ex: "mapa.txt")
     */
    private void carregarMapa(String caminhoArquivo) {
        CarregadorMapa carregador = new CarregadorMapa();
        Obstaculo[][] novoMapa = carregador.carregarObstaculos(caminhoArquivo);

        if (novoMapa == null)
            return;

        this.mapaFixo = novoMapa;

        int novaProfundidade = novoMapa.length;
        int novaLargura = novoMapa[0].length;

        boolean dimensoesMudaram = (novaProfundidade != campo.getProfundidade()) ||
                (novaLargura != campo.getLargura());

        if (dimensoesMudaram) {
            redimensionarSimulacao(novaProfundidade, novaLargura);
        }
    }

    /**
     * Redimensiona toda a simulação para novas dimensões.
     * <p>
     * <strong>Operações realizadas:</strong>
     * <ol>
     * <li>Recria ambos os campos com novo tamanho</li>
     * <li>Fecha a visualização antiga</li>
     * <li>Cria nova visualização com dimensões corretas</li>
     * <li>Reaplica configurações (cores e controles)</li>
     * </ol>
     * </p>
     * 
     * @param novaProfundidade Nova altura do campo
     * @param novaLargura      Nova largura do campo
     */
    private void redimensionarSimulacao(int novaProfundidade, int novaLargura) {
        System.out.println("Redimensionando simulação para: " + novaProfundidade + "x" + novaLargura);

        campo = new Campo(novaProfundidade, novaLargura);
        campoAtualizado = new Campo(novaProfundidade, novaLargura);

        if (visualizacao != null) {
            visualizacao.fechar();
            visualizacao = new VisualizacaoSimulador(novaProfundidade, novaLargura, estatisticas);

            definirCores();
            configurarInterface();
        }
    }

    /**
     * Copia obstáculos fixos da matriz de mapa para o campo destino.
     * <p>
     * Percorre toda a matriz de obstáculos e coloca cada um na posição
     * correspondente do campo, se houver obstáculo definido.
     * </p>
     * 
     * @param destino Campo onde os obstáculos serão aplicados
     */
    private void aplicarObstaculos(Campo destino) {
        if (mapaFixo == null)
            return;

        for (int i = 0; i < destino.getProfundidade(); i++) {
            for (int j = 0; j < destino.getLargura(); j++) {
                if (mapaFixo[i][j] != null) {
                    destino.colocar(mapaFixo[i][j], i, j);
                }
            }
        }
    }

    // ========== GETTERS ==========

    /**
     * Retorna o número do passo atual da simulação.
     * 
     * @return Contador de passos desde o início ou último reinício
     */
    public int getPasso() {
        return passo;
    }

    /**
     * Retorna uma cópia da lista de animais vivos.
     * <p>
     * Retorna uma nova lista para evitar modificações externas.
     * </p>
     * 
     * @return Lista (cópia) de todos os atores vivos
     */
    public List<Ator> getAnimais() {
        return new ArrayList<>(animais);
    }

    /**
     * Retorna o campo atual da simulação.
     * 
     * @return Campo em uso no passo atual
     */
    public Campo getCampo() {
        return campo;
    }

    /**
     * Retorna o sistema de clima da simulação.
     * 
     * @return Objeto controlador do clima
     */
    public Clima getClima() {
        return clima;
    }

    /**
     * Define um novo sistema de clima para a simulação.
     * 
     * @param clima Novo objeto de clima a ser usado
     */
    public void setClima(Clima clima) {
        this.clima = clima;
    }
}

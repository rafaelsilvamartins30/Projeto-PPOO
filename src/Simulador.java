import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Uma simulação simples de um ecossistema.
 * Esta simulação envolve predadores e presas que interagem em um campo.
 * O campo também contém grama que cresce e pode ser comida por herbívoros.
 * O clima afeta o crescimento da grama.
 * A simulação pode ser pausada, continuada e reiniciada.
 * Os obstáculos fixos podem ser carregados de um arquivo de texto para definir o terreno.
 * 
 * @author David J. Barnes e Michael Kolling
 * @author Grupo 10
 * @version 2025-11-30 (traduzido e modificado)
 */
public class Simulador {

    private List<Ator> animais;
    private List<Ator> novosAnimais;
    private Campo campo;
    private Campo campoAtualizado;
    private int passo;
    private Desenhavel visualizacao;
    private Clima clima;
    private boolean pausada;
    private boolean emExecucao;
    private Obstaculo[][] mapaFixo;
    private EstatisticasCampo estatisticas;
    
/**
     * Constrói um campo de simulação com tamanho padrão.
     */
    public Simulador() {
        this(Configuracao.PROFUNDIDADE_PADRAO, Configuracao.LARGURA_PADRAO);
    }
    
    /**
     * Cria um campo de simulação com o tamanho especificado.
     * Este é o ponto de entrada principal que cria as dependências.
     */
    public Simulador(int profundidade, int largura) {
        // 1. Cria a estatística aqui para ela nascer antes de todo mundo
        this(profundidade, largura, new EstatisticasCampo());
    }

    /**
     * CONSTRUTOR AUXILIAR (Privado)
     * Serve apenas para criar a Visualização injetando a estatística que acabamos de criar.
     */
    private Simulador(int profundidade, int largura, EstatisticasCampo estatisticasCompartilhada) {
        this(profundidade, largura, 
             new VisualizacaoSimulador(profundidade, largura, estatisticasCompartilhada), 
             estatisticasCompartilhada);
    }

    /**
     * O "Construtor Mestre" (Master Constructor).
     * Agora ele recebe tudo pronto e apenas atribui.
     */
    public Simulador(int profundidade, int largura, Desenhavel visualizacao, EstatisticasCampo estatisticas) {   
        // Verifica se as dimensões são válidas
        if(largura <= 0 || profundidade <= 0) {
            System.out.println("As dimensões devem ser maiores que zero.");
            System.out.println("Usando valores padrão.");
            profundidade = Configuracao.PROFUNDIDADE_PADRAO;
            largura = Configuracao.LARGURA_PADRAO;
        }
        
        // --- INJEÇÃO DE DEPENDÊNCIA ---
        this.estatisticas = estatisticas; // Agora o Simulador tem a MESMA instância da View!
        this.visualizacao = visualizacao;
        
        // Inicializa listas e campos
        animais = new ArrayList<Ator>();
        novosAnimais = new ArrayList<Ator>();
        campo = new Campo(profundidade, largura);
        campoAtualizado = new Campo(profundidade, largura);
        
        definirCores();
        
        // Inicializa clima e estado da simulação
        this.clima = new Clima(50);
        this.pausada = false;
        this.emExecucao = false;
        
        // Carrega o mapa de obstáculos
        carregarMapa("mapa.txt");
        
        reiniciar();
        
        // Configura os botões da interface
        configurarInterface();
    }
    
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
     * Pausa a simulação em execução.
     * A simulação pode ser continuada posteriormente com continuar().
     */
    private void pausar() {
        if (emExecucao && !pausada) {
            pausada = true;
        } else if (pausada) {
            // Simulação já está pausada
        } else {
            // Não há simulação em execução para pausar
        }
    }

    /**
     * Continua a simulação pausada.
     */
    public void continuar() {
        if (pausada) {
            pausada = false;
        } else if (emExecucao) {
            // Simulação já está em execução
        } else {
            // Não há simulação pausada para continuar
        }
    }

    /**
     * Verifica se a simulação está pausada.
     * 
     * @return true se a simulação estiver pausada, false caso contrário.
     */
    public boolean estaPausada() {
        return pausada;
    }

    /**
     * Verifica se a simulação está em execução (pausada ou não).
     * 
     * @return true se a simulação estiver em execução.
     */
    public boolean estaEmExecucao() {
        return emExecucao;
    }

    /**
     * Reinicia completamente a simulação para o estado inicial.
     * Remove todos os animais, limpa o campo, reinicia o clima e reseta contadores.
     */
    public void reiniciar()
    {
        passo = 0;
        // Limpa todas as listas e campos
        animais.clear();
        novosAnimais.clear();
        
        // Limpa os campos de simulação
        campo.limpar();
        campoAtualizado.limpar();
        // Reinicia o clima
        if (clima != null) {
            clima.reiniciar();
        }
        // Define o estado como não pausado
        pausada = false;
        visualizacao.setTextoBotaoPausa("Pausar");
        // Aplica obstáculos fixos e popula o campo
        aplicarObstaculos(campo);
        new Populador().popular(campo, animais);
        // Reinicia a visualização e mostra o estado inicial
        visualizacao.reiniciar();
        visualizacao.mostrarStatus(passo, campo);
    }
    
    /**
     * Retorna o sistema de clima da simulação.
     * 
     * @return O objeto Clima
     */
    public Clima getClima() {
        return clima;
    }

    /**
     * Define um novo sistema de clima para a simulação.
     * 
     * @param clima O novo sistema de clima
     */
    public void setClima(Clima clima) {
        this.clima = clima;
    }
    
    /**
     * Executa a simulação a partir do estado atual pelo número de passos indicado.
     * Interrompe antes se a simulação deixar de ser viável.
     * Respeita o estado de pausa da simulação.
     * 
     * @param numPassos Quantidade de passos a simular.
     */
    public void simular(int numPassos) {
        emExecucao = true;
        
        while(emExecucao) {
            
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
            
            try { Thread.sleep(50); } catch (Exception e) {}
        }
    }

    /**
     * Simula um único passo da simulação.
     * Atualiza o clima, processa o ciclo de vida dos animais e atualiza a visualização.
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
     * Limpa a lista de nascimentos e aplica obstáculos fixos no campo de destino.
     */
    private void prepararNovosAnimais() {
        novosAnimais.clear();
    }
    /**
     * Aplica os obstáculos fixos no campo atualizado.
     */
    private void prepararObstaculos() {
        aplicarObstaculos(campoAtualizado);
    }

    /**
     * Itera sobre todos os animais: faz agir, remove os mortos e registra os nascimentos.
     */
    private void processarCicloDeVidaAnimais() {
        Iterator<Ator> iter = animais.iterator();
        while(iter.hasNext()) {
            Ator ator = iter.next();
            if (ator.estaVivo()) {
                ator.agir(campo, campoAtualizado, novosAnimais);
            }
            else {
                iter.remove();
            }
        }
        // Adiciona os filhotes nascidos neste turno
        animais.addAll(novosAnimais);
    }

    /**
     * Gerencia o crescimento da grama e as mudanças climáticas.
     */
    private void processarAmbienteEClima() {
        campoAtualizado.copiarGramaDe(campo);
        
        // Se não houver sistema de clima, apenas cresce a grama normalmente
        if (clima == null) {
            campoAtualizado.crescerGrama();
            return;
        }

        // Atualiza clima e aplica efeitos
        clima.atualizar();
        campoAtualizado.crescerGrama();
        
        // Se chover, a grama cresce uma segunda vez (dobro da velocidade)
        if(clima.estaChuvoso()) {
            campoAtualizado.crescerGrama();
        }

        // Atualiza a interface com o estado do clima
        String textoClima = clima.estaChuvoso() ? "Clima: CHUVOSO (Crescimento Rápido)" : "Clima: NORMAL";
        visualizacao.setInfoClima(textoClima, clima.estaChuvoso());
    }

    /**
     * Troca as referências dos campos para o próximo passo.
     */
    private void trocarCampos() {
        Campo temp = campo;
        campo = campoAtualizado;
        campoAtualizado = temp;
        campoAtualizado.limpar();
    }

    /**
     * Retorna o passo atual da simulação.
     * 
     * @return O número do passo atual
     */
    public int getPasso() {
        return passo;
    }

    /**
     * Retorna a lista de animais atualmente vivos.
     * 
     * @return Lista de atores
     */
    public List<Ator> getAnimais() {
        return new ArrayList<>(animais);
    }

    /**
     * Retorna o campo atual da simulação.
     * 
     * @return O campo atual
     */
    public Campo getCampo() {
        return campo;
    }

    /**
     * Configura os listeners dos botões usando Classes Anônimas.
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
        // Listener para o botão Reiniciar
        visualizacao.setAcaoReiniciar(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pausar(); 
                
                try { Thread.sleep(200); } catch (InterruptedException ex) {}
                
                reiniciar();
                
                visualizacao.setTextoBotaoPausa("Pausar");
            }
        });
    }

    /**
     * Carrega o mapa e ajusta a simulação se necessário.
     */
    private void carregarMapa(String caminhoArquivo) {
        // 1. Delegamos a leitura para a classe especialista (CarregadorMapa)
        CarregadorMapa carregador = new CarregadorMapa();
        Obstaculo[][] novoMapa = carregador.carregarObstaculos(caminhoArquivo);

        if (novoMapa == null) return; // Se falhou, mantém o atual

        this.mapaFixo = novoMapa;

        // 2. Verificamos se as dimensões mudaram
        int novaProfundidade = novoMapa.length;
        int novaLargura = novoMapa[0].length;
        
        boolean dimensoesMudaram = (novaProfundidade != campo.getProfundidade()) || 
                                   (novaLargura != campo.getLargura());

        if (dimensoesMudaram) {
            redimensionarSimulacao(novaProfundidade, novaLargura);
        }
    }

    /**
     * Método auxiliar privado para lidar com a complexidade de recriar o mundo
     * quando o tamanho do mapa muda.
     */
    private void redimensionarSimulacao(int novaProfundidade, int novaLargura) {
        System.out.println("Redimensionando simulação para: " + novaProfundidade + "x" + novaLargura);
        
        // Recria a lógica
        campo = new Campo(novaProfundidade, novaLargura);
        campoAtualizado = new Campo(novaProfundidade, novaLargura);
        
        // Recria a interface gráfica
        if (visualizacao != null) {
            visualizacao.fechar();
            visualizacao = new VisualizacaoSimulador(novaProfundidade, novaLargura, estatisticas);
            
            // Re-aplica as configurações necessárias na nova janela
            definirCores();
            configurarInterface();
        }
    }

    /**
     * Copia os obstáculos fixos para o campo destino.
     */
    private void aplicarObstaculos(Campo destino) {
        if (mapaFixo == null) return;
        
        for (int i = 0; i < destino.getProfundidade(); i++) {
            for (int j = 0; j < destino.getLargura(); j++) {
                if (mapaFixo[i][j] != null) {
                    destino.colocar(mapaFixo[i][j], i, j);
                }
            }
        }
    }
}

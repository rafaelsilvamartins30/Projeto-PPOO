import java.awt.Color;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Uma simulação simples de um ecossistema.
 * Cria e gerencia o campo, os animais, o clima e a interface gráfica.
 * Permite pausar, continuar e reiniciar a simulação.
 * A simulação pode ser executada por um número especificado de passos.
 * 
 * VERSÃO MODIFICADA: Inclui sistema de clima e controles de pausa/continuar.
 * 
 * @author David J. Barnes e Michael Kolling
 * @author Grupo 10
 * @version 2025-11-30 (traduzido e modificado)
 */
public class Simulador
{
    // As variáveis estáticas finais representam as configurações da simulação.
    
    // Largura padrão da grade.
    private static final int LARGURA_PADRAO = 120;
    // Profundidade padrão da grade.
    private static final int PROFUNDIDADE_PADRAO = 75;
    // Probabilidade de que uma raposa seja criada em uma posição da grade.
    private static final double PROBABILIDADE_CRIACAO_RAPOSA = 0.02;
    // Probabilidade de que um coelho seja criado em uma posição da grade.
    private static final double PROBABILIDADE_CRIACAO_COELHO = 0.08;
    // Probabilidade de que um rato seja criado em uma posição da grade.
    private static final double PROBABILIDADE_CRIACAO_RATO = 0.08;
    // Probabilidade de que uma cobra seja criada em uma posição da grade.
    private static final double PROBABILIDADE_CRIACAO_COBRA = 0.03;
    // Probabilidade de que um gavião seja criado em uma posição da grade.
    private static final double PROBABILIDADE_CRIACAO_GAVIAO = 0.02;
    // Probabilidade de que um urso seja criado em uma posição da grade.
    private static final double PROBABILIDADE_CRIACAO_URSO = 0.01;

    // Lista de animais no campo.
    private List<Ator> animais;
    // Lista de animais recém-nascidos.
    private List<Ator> novosAnimais;
    // Estado atual do campo.
    private Campo campo;
    // Segundo campo, usado para construir o próximo estágio da simulação.
    private Campo campoAtualizado;
    // Passo atual da simulação.
    private int passo;
    // Visualização gráfica da simulação.
    private Desenhavel visualizacao;
    // Sistema de clima da simulação
    private Clima clima;
    // Controle de pausa da simulação
    private boolean pausada;
    // Indica se a simulação está em execução
    private boolean emExecucao;
    // Matriz para memorizar onde estão os obstáculos
    private Obstaculo[][] mapaFixo;
    
    /**
     * Constrói um campo de simulação com tamanho padrão.
     */
    public Simulador()
    {
        this(PROFUNDIDADE_PADRAO, LARGURA_PADRAO);
    }
    
    public Simulador(int profundidade, int largura)
    {
        this(profundidade, largura, new VisualizacaoSimulador(profundidade, largura));
    }

    /**
     * Cria um campo de simulação com o tamanho especificado.
     * @param profundidade Profundidade do campo. Deve ser maior que zero.
     * @param largura Largura do campo. Deve ser maior que zero.
     */
    public Simulador(int profundidade, int largura, Desenhavel visualizacao)
    {
        if(largura <= 0 || profundidade <= 0) {
            System.out.println("As dimensões devem ser maiores que zero.");
            System.out.println("Usando valores padrão.");
            profundidade = PROFUNDIDADE_PADRAO;
            largura = LARGURA_PADRAO;
        }
        animais = new ArrayList<Ator>();
        novosAnimais = new ArrayList<Ator>();
        campo = new Campo(profundidade, largura);
        campoAtualizado = new Campo(profundidade, largura);

        // Cria uma visualização do estado de cada posição no campo.
        this.visualizacao = visualizacao;
        this.visualizacao.definirCor(Raposa.class, Color.blue);
        this.visualizacao.definirCor(Coelho.class, Color.orange);
        this.visualizacao.definirCor(Rato.class, Color.MAGENTA);
        this.visualizacao.definirCor(Cobra.class, Color.GREEN);
        this.visualizacao.definirCor(Gaviao.class, Color.RED);
        this.visualizacao.definirCor(Urso.class, Color.BLACK);
        
        // Inicializa o sistema de clima (muda a cada 50 ciclos por padrão)
        this.clima = new Clima(50);
        
        // Inicializa controles
        this.pausada = false;
        this.emExecucao = false;
        
        // Inicializa o sistema de clima (muda a cada 50 ciclos por padrão)
        this.clima = new Clima(50);
        
        // Inicializa controles
        this.pausada = false;
        this.emExecucao = false;
        
        // Configura o ponto inicial da simulação.
        reiniciar();

        // Configura os listeners dos botões
        configurarInterface();
    }
    
    // ===== MÉTODOS DE CONTROLE DE SIMULAÇÃO =====
    
    /**
     * Pausa a simulação em execução.
     * A simulação pode ser continuada posteriormente com continuar().
     */
    public void pausar() {
        if (emExecucao && !pausada) {
            pausada = true;
            System.out.println("⏸️  Simulação PAUSADA no passo " + passo);
        } else if (pausada) {
            System.out.println("⚠️  A simulação já está pausada.");
        } else {
            System.out.println("⚠️  Não há simulação em execução para pausar.");
        }
    }
    
    /**
     * Continua a simulação pausada.
     */
    public void continuar() {
        if (pausada) {
            pausada = false;
            System.out.println("▶️  Simulação CONTINUADA a partir do passo " + passo);
        } else if (emExecucao) {
            System.out.println("⚠️  A simulação já está em execução.");
        } else {
            System.out.println("⚠️  Não há simulação pausada para continuar.");
        }
    }
    
    /**
     * Verifica se a simulação está pausada.
     * @return true se a simulação estiver pausada, false caso contrário.
     */
    public boolean estaPausada() {
        return pausada;
    }
    
    /**
     * Verifica se a simulação está em execução (pausada ou não).
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
        System.out.println("\n🔄 REINICIANDO SIMULAÇÃO...");
        
        // Reseta o passo
        passo = 0;
        
        // Limpa todas as listas de animais
        animais.clear();
        novosAnimais.clear();
        
        // Limpa os campos
        campo.limpar();
        campoAtualizado.limpar();
        
        // Reinicia o clima
        if (clima != null) {
            clima.reiniciar();
        }
        
        // Reseta controles
        pausada = true;
        
        // Atualiza o texto do botão quando começamos uma nova simulação
        visualizacao.setTextoBotaoPausa("Iniciar");

        // Popula novamente o campo
        popular(campo);
        
        // Reinicia a visualização
        visualizacao.reiniciar();
        
        // Mostra o estado inicial
        visualizacao.mostrarStatus(passo, campo);
        
        System.out.println("✅ Simulação reiniciada com sucesso!");
        System.out.println("📊 Total de animais: " + animais.size());
    }
    
    // ===== MÉTODOS DE CLIMA =====
    
    /**
     * Retorna o sistema de clima da simulação.
     * @return O objeto Clima
     */
    public Clima getClima() {
        return clima;
    }
    
    /**
     * Define um novo sistema de clima para a simulação.
     * @param clima O novo sistema de clima
     */
    public void setClima(Clima clima) {
        this.clima = clima;
    }
    
    // ===== MÉTODOS DE SIMULAÇÃO =====
    
    /**
     * Executa a simulação a partir do estado atual por um período razoavelmente longo,
     * por exemplo, 500 passos.
     */
    public void executarSimulacaoLonga()
    {
        simular(500);
    }
    
    /**
     * Executa a simulação a partir do estado atual pelo número de passos indicado.
     * Interrompe antes se a simulação deixar de ser viável.
     * Respeita o estado de pausa da simulação.
     * 
     * @param numPassos Quantidade de passos a simular.
     */
    public void simular(int numPassos)
    {
        emExecucao = true;
        System.out.println("\n▶️  Iniciando loop de simulação...");
        
        // O loop roda enquanto o programa estiver aberto (emExecucao = true)
        while(emExecucao) {
            
            // Verifica se atingiu o limite de passos do objetivo atual
            // Note que usamos >= para garantir que ele pare EXATAMENTE no limite
            if (passo >= numPassos) {
                // Só avisa e pausa se ainda não estiver pausado
                if (!pausada) {
                    System.out.println("🏁 Limite de " + numPassos + " passos atingido. Pausando...");
                    pausar();
                    visualizacao.setTextoBotaoPausa("Continuar");
                }
            }

            // Verifica se o jogo acabou (todos morreram)
            if (!visualizacao.ehViavel(campo)) {
                if (!pausada) {
                    System.out.println("❌ Todos os animais morreram. Pausando...");
                    pausar(); 
                    visualizacao.setTextoBotaoPausa("Continuar"); 
                }
            }

            // Loop de espera (Pausa)
            // Se estiver pausado, o programa fica preso aqui esperando você clicar "Continuar"
            while (pausada) {
                try {
                    Thread.sleep(100); 
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            
            // Se não estiver pausado, executa um passo
            simularUmPasso();
            
            // Pequeno delay para a animação não ser instantânea
            try { Thread.sleep(50); } catch (Exception e) {}
        }
        
        System.out.println("\n✅ Loop de simulação encerrado.");
    }
    
    /**
     * Simula um único passo da simulação.
     * Atualiza o estado de todos os animais, o clima e a grama.
     * Atualiza a visualização ao final do passo.
     */
    public void simularUmPasso()
    {
        passo++;
        novosAnimais.clear();
        
        // Prepara o campo novo com obstáculos e grama antiga
        aplicarObstaculos(campoAtualizado);
        campoAtualizado.copiarGramaDe(campo);
        
        // Lógica de Clima e Crescimento da Grama
        if (clima != null) {
            // Atualiza o estado interno do clima (conta ciclos, muda sol/chuva)
            clima.atualizar();
            
            // Aplica o crescimento da grama baseado no clima ATUAL
            campoAtualizado.crescerGrama();
            if(clima.estaChuvoso()) {
                campoAtualizado.crescerGrama(); // Bônus de chuva: cresce 2x
            }

            // Atualiza o texto e a cor na Interface Gráfica
            String textoClima = clima.estaChuvoso() ? "Clima: CHUVOSO (Crescimento Rápido)" : "Clima: NORMAL";
            visualizacao.setInfoClima(textoClima, clima.estaChuvoso());
        } 
        else {
            // Caso o sistema de clima não exista, crescimento padrão
            campoAtualizado.crescerGrama();
        }

        // Permite que todos os animais ajam
        for(Iterator<Ator> iter = animais.iterator(); iter.hasNext(); ) {
            Ator ator = iter.next();
            if(ator.estaVivo()) {
                ator.agir(campo, campoAtualizado, novosAnimais);
            }
            else {
                iter.remove(); // Remove o animal morto da lista principal
            }
        }

        // Finalização do passo
        animais.addAll(novosAnimais);
        
        Campo temp = campo;
        campo = campoAtualizado;
        campoAtualizado = temp;
        campoAtualizado.limpar();

        // Atualiza o desenho do mapa
        visualizacao.mostrarStatus(passo, campo);
        
        // Log no console (útil para depuração)
        if (passo % 10 == 0 && clima != null) {
            System.out.println("Passo " + passo + " - " + clima + " | Animais: " + animais.size());
        }
    }
    
    /**
     * Retorna o passo atual da simulação.
     * @return O número do passo atual
     */
    public int getPasso() {
        return passo;
    }
    
    /**
     * Retorna a lista de animais atualmente vivos.
     * @return Lista de atores
     */
    public List<Ator> getAnimais() {
        return new ArrayList<>(animais); // Retorna cópia para segurança
    }
    
    /**
     * Retorna o campo atual da simulação.
     * @return O campo atual
     */
    public Campo getCampo() {
        return campo;
    }

    /**
     * Popula o campo com animais, respeitando os obstáculos carregados.
     * @param campo O campo a ser populado
     */
    private void popular(Campo campo) {
        // Carrega os obstáculos do arquivo
        carregarMapa("mapa.txt");
        
        // Preenche os espaços vazios com animais
        Random aleatorio = new Random();
        
        for(int linha = 0; linha < campo.getProfundidade(); linha++) {
            for(int coluna = 0; coluna < campo.getLargura(); coluna++) {
                
                // Só coloca animal se não houver obstáculo (null)
                if(campo.getObjetoEm(linha, coluna) == null) {
                    
                    if(aleatorio.nextDouble() <= PROBABILIDADE_CRIACAO_RAPOSA) {
                        Raposa raposa = new Raposa(true);
                        animais.add(raposa);
                        raposa.definirLocalizacao(linha, coluna);
                        campo.colocar(raposa, linha, coluna);
                    }
                    else if(aleatorio.nextDouble() <= PROBABILIDADE_CRIACAO_COELHO) {
                        Coelho coelho = new Coelho(true);
                        animais.add(coelho);
                        coelho.definirLocalizacao(linha, coluna);
                        campo.colocar(coelho, linha, coluna);
                    }
                    // --- NOVOS ANIMAIS ---
                    else if(aleatorio.nextDouble() <= PROBABILIDADE_CRIACAO_RATO) {
                        Rato rato = new Rato(true);
                        animais.add(rato);
                        rato.definirLocalizacao(linha, coluna);
                        campo.colocar(rato, linha, coluna);
                    }
                    else if(aleatorio.nextDouble() <= PROBABILIDADE_CRIACAO_COBRA) {
                        Cobra cobra = new Cobra(true);
                        animais.add(cobra);
                        cobra.definirLocalizacao(linha, coluna);
                        campo.colocar(cobra, linha, coluna);
                    }
                    else if(aleatorio.nextDouble() <= PROBABILIDADE_CRIACAO_GAVIAO) {
                        Gaviao gaviao = new Gaviao(true);
                        animais.add(gaviao);
                        gaviao.definirLocalizacao(linha, coluna);
                        campo.colocar(gaviao, linha, coluna);
                    }
                    else if(aleatorio.nextDouble() <= PROBABILIDADE_CRIACAO_URSO) {
                        Urso urso = new Urso(true);
                        animais.add(urso);
                        urso.definirLocalizacao(linha, coluna);
                        campo.colocar(urso, linha, coluna);
                    }
                }
            }
        }
        Collections.shuffle(animais);
    }

    /**
     * Configura os listeners dos botões usando Classes Anônimas.
     */
    private void configurarInterface() {
        // Botão Pausar/Continuar (Simples)
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
        
        // Botão Reiniciar
        visualizacao.setAcaoReiniciar(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Manda pausar
                pausar(); 
                
                // SEGURANÇA: Espera um pouquinho (200ms) para garantir 
                // que o loop da simulação entrou no estado de pausa 
                // antes de limparmos as listas de animais.
                try { Thread.sleep(200); } catch (InterruptedException ex) {}
                
                // Agora é seguro limpar e recriar tudo
                reiniciar();
                
                // 4. Atualiza o botão visualmente
                visualizacao.setTextoBotaoPausa("Iniciar");
            }
        });
    }

    /**
     * Carrega o mapa do arquivo e coloca os obstáculos no campo.
     */
    private void carregarMapa(String caminhoArquivo) {
        try {
            List<String> linhas = Files.readAllLines(Paths.get(caminhoArquivo));
            if (linhas.isEmpty()) return;

            int novaProfundidade = linhas.size();
            int novaLargura = linhas.get(0).length();

            // Inicializa a memória do mapa
            mapaFixo = new Obstaculo[novaProfundidade][novaLargura];

            // Se o tamanho mudou, recria os campos e a visualização
            if (novaProfundidade != campo.getProfundidade() || novaLargura != campo.getLargura()) {
                campo = new Campo(novaProfundidade, novaLargura);
                campoAtualizado = new Campo(novaProfundidade, novaLargura);
                
                visualizacao.fechar();
                visualizacao = new VisualizacaoSimulador(novaProfundidade, novaLargura);
                // Reconfigura cores se necessário (ou configure no construtor da visualização)
                visualizacao.definirCor(Raposa.class, Color.blue);
                visualizacao.definirCor(Coelho.class, Color.orange);
                visualizacao.definirCor(Rato.class, Color.MAGENTA);
                visualizacao.definirCor(Cobra.class, Color.GREEN);
                visualizacao.definirCor(Gaviao.class, Color.RED);
                visualizacao.definirCor(Urso.class, Color.BLACK);

                configurarInterface();
            } else {
                campo.limpar();
            }

            // Lê o arquivo e salva na memória (mapaFixo)
            for (int i = 0; i < novaProfundidade; i++) {
                String linha = linhas.get(i);
                for (int j = 0; j < novaLargura && j < linha.length(); j++) {
                    char c = linha.charAt(j);
                    if (c == 'R') {
                        mapaFixo[i][j] = Obstaculo.RIO;
                    } else if (c == 'P') {
                        mapaFixo[i][j] = Obstaculo.PEDRA;
                    }
                }
            }
            
            // Aplica a memória no campo atual
            aplicarObstaculos(campo);

        } catch (IOException e) {
            System.out.println("⚠️ Erro ao ler mapa.txt: " + e.getMessage());
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

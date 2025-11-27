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
    private static final int LARGURA_PADRAO = 120;
    private static final int PROFUNDIDADE_PADRAO = 75;
    private static final double PROBABILIDADE_CRIACAO_RAPOSA = 0.02;
    private static final double PROBABILIDADE_CRIACAO_COELHO = 0.08;
    private static final double PROBABILIDADE_CRIACAO_RATO = 0.08;
    private static final double PROBABILIDADE_CRIACAO_COBRA = 0.03;
    private static final double PROBABILIDADE_CRIACAO_GAVIAO = 0.02;
    private static final double PROBABILIDADE_CRIACAO_URSO = 0.01;

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

        this.visualizacao = visualizacao;
        this.visualizacao.definirCor(Raposa.class, Color.blue);
        this.visualizacao.definirCor(Coelho.class, Color.orange);
        this.visualizacao.definirCor(Rato.class, Color.MAGENTA);
        this.visualizacao.definirCor(Cobra.class, Color.GREEN);
        this.visualizacao.definirCor(Gaviao.class, Color.RED);
        this.visualizacao.definirCor(Urso.class, Color.BLACK);
        
        this.clima = new Clima(50);
        this.pausada = false;
        this.emExecucao = false;
        
        carregarMapa("mapa.txt");
        
        reiniciar();

        configurarInterface();
    }
    
    /**
     * Pausa a simulação em execução.
     * A simulação pode ser continuada posteriormente com continuar().
     */
    public void pausar() {
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
        popular(campo);
        
        visualizacao.reiniciar();
        
        visualizacao.mostrarStatus(passo, campo);
    }
    
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
     * Atualiza o estado de todos os animais, o clima e a grama.
     * Atualiza a visualização ao final do passo.
     */
    public void simularUmPasso()
    {
        passo++;
        novosAnimais.clear();
        
        aplicarObstaculos(campoAtualizado);
        campoAtualizado.copiarGramaDe(campo);
        
        if (clima != null) {
            clima.atualizar();
            
            campoAtualizado.crescerGrama();
            if(clima.estaChuvoso()) {
                campoAtualizado.crescerGrama();
            }

            String textoClima = clima.estaChuvoso() ? "Clima: CHUVOSO (Crescimento Rápido)" : "Clima: NORMAL";
            visualizacao.setInfoClima(textoClima, clima.estaChuvoso());
        } 
        else {
            campoAtualizado.crescerGrama();
        }

        for(Iterator<Ator> iter = animais.iterator(); iter.hasNext(); ) {
            Ator ator = iter.next();
            if(ator.estaVivo()) {
                ator.agir(campo, campoAtualizado, novosAnimais);
            }
            else {
                iter.remove();
            }
        }

        animais.addAll(novosAnimais);
        
        Campo temp = campo;
        campo = campoAtualizado;
        campoAtualizado = temp;
        campoAtualizado.limpar();

        visualizacao.mostrarStatus(passo, campo);
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
        return new ArrayList<>(animais);
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
        Random aleatorio = new Random();
        
        for(int linha = 0; linha < campo.getProfundidade(); linha++) {
            for(int coluna = 0; coluna < campo.getLargura(); coluna++) {
                
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
                
                try { Thread.sleep(200); } catch (InterruptedException ex) {}
                
                reiniciar();
                
                visualizacao.setTextoBotaoPausa("Pausar");
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

            mapaFixo = new Obstaculo[novaProfundidade][novaLargura];

            if (novaProfundidade != campo.getProfundidade() || novaLargura != campo.getLargura()) {
                campo = new Campo(novaProfundidade, novaLargura);
                campoAtualizado = new Campo(novaProfundidade, novaLargura);
                
                visualizacao.fechar();
                visualizacao = new VisualizacaoSimulador(novaProfundidade, novaLargura);
                visualizacao.definirCor(Raposa.class, Color.blue);
                visualizacao.definirCor(Coelho.class, Color.orange);
                visualizacao.definirCor(Rato.class, Color.MAGENTA);
                visualizacao.definirCor(Cobra.class, Color.GREEN);
                visualizacao.definirCor(Gaviao.class, Color.RED);
                visualizacao.definirCor(Urso.class, Color.BLACK);

                configurarInterface();
            }

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

        } catch (IOException e) {
            System.err.println("⚠️ Erro ao ler mapa.txt: " + e.getMessage());
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

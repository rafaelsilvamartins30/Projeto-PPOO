import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;
import java.awt.Color;

/**
 * Um simulador simples de predador e presa, baseado em um campo contendo
 * coelhos e raposas.
 * 
 * @author David J. Barnes e Michael Kolling
 * @version 2002-04-09 (traduzido)
 */
public class Simulador
{
    // As variáveis estáticas finais representam as configurações da simulação.
    
    // Largura padrão da grade.
    private static final int LARGURA_PADRAO = 50;
    // Profundidade padrão da grade.
    private static final int PROFUNDIDADE_PADRAO = 50;
    // Probabilidade de que uma raposa seja criada em uma posição da grade.
    private static final double PROBABILIDADE_CRIACAO_RAPOSA = 0.02;
    // Probabilidade de que um coelho seja criado em uma posição da grade.
    private static final double PROBABILIDADE_CRIACAO_COELHO = 0.08;    

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
        
        // Configura o ponto inicial da simulação.
        reiniciar();
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
     * @param numPassos Quantidade de passos a simular.
     */
    public void simular(int numPassos)
    {
        for(int passo = 1; passo <= numPassos && visualizacao.ehViavel(campo); passo++) {
            simularUmPasso();
        }
    }
    
    /**
     * Executa a simulação a partir do estado atual por um único passo.
     * Atualiza o estado de cada raposa e coelho.
     */
    public void simularUmPasso()
    {
        passo++;
        novosAnimais.clear();
        
        // Permite que todos os animais ajam.
        for(Iterator<Ator> iter = animais.iterator(); iter.hasNext(); ) {
            Ator ator = iter.next();
            if(ator.estaVivo()) {
                ator.agir(campo, campoAtualizado, novosAnimais);
            }
            else {
                iter.remove(); // Remove o animal morto da lista principal.
            }
        }

        // Adiciona animais recém-nascidos à lista principal.
        animais.addAll(novosAnimais);
        
        // Troca o campo e o campo atualizado no final do passo.
        Campo temp = campo;
        campo = campoAtualizado;
        campoAtualizado = temp;
        campoAtualizado.limpar();

        // Exibe o novo estado na tela.
        visualizacao.mostrarStatus(passo, campo);
    }
        
    /**
     * Reinicia a simulação para o estado inicial.
     */
    public void reiniciar()
    {
        passo = 0;
        animais.clear();
        campo.limpar();
        campoAtualizado.limpar();
        popular(campo);
        
        // Mostra o estado inicial.
        visualizacao.mostrarStatus(passo, campo);
    }
    
    /**
     * Popula o campo com raposas e coelhos.
     * @param campo O campo a ser populado.
     */
    private void popular(Campo campo)
    {
        Random aleatorio = new Random();
        campo.limpar();
        for(int linha = 0; linha < campo.getProfundidade(); linha++) {
            for(int coluna = 0; coluna < campo.getLargura(); coluna++) {
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
                // Caso contrário, deixa a posição vazia.
            }
        }
        Collections.shuffle(animais);
    }
}
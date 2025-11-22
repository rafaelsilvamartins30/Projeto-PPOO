import java.util.List;

/**
 * Um modelo simples de um coelho.
 * Coelhos envelhecem, se movem, se reproduzem e morrem.
 * 
 * @author David J. Barnes e Michael Kolling
 * @version 2002-04-11 (traduzido)
 */
public class Coelho extends Animal {
    // Características compartilhadas por todos os coelhos (campos estáticos).

    // A idade na qual um coelho pode começar a se reproduzir.
    private static final int IDADE_REPRODUTIVA = 5;
    // A idade máxima que um coelho pode alcançar.
    private static final int IDADE_MAXIMA = 50;
    // A probabilidade de um coelho se reproduzir.
    private static final double PROBABILIDADE_REPRODUCAO = 0.15;
    // O número máximo de filhotes por ninhada.
    private static final int TAMANHO_MAXIMO_NINHADA = 5;
    // O valor máximo de alimento que um coelho
    private static final int VALOR_ALIMENTAR_MAX = 20;

    /**
     * Cria um novo coelho. A idade pode ser aleatória ou zero (novo nascimento).
     * O nível de alimento também é inicializado. 
     * 
     * @param idadeAleatoria Se verdadeiro, o coelho terá uma idade aleatória.
     */
    public Coelho(boolean idadeAleatoria) {
        super(idadeAleatoria);
        if(idadeAleatoria) {
            setNivelAlimento(getAleatorio().nextInt(VALOR_ALIMENTAR_MAX));
        } else {
            setNivelAlimento(VALOR_ALIMENTAR_MAX);
        }
    }
    
    /**
     * Isto é o que o coelho faz na maior parte do tempo — ele corre
     * por aí. Às vezes ele se reproduz, come grama ou morre de velhice.
     * A fome agora também é considerada.
     * @param campoAtual O campo atual.
     * @param campoAtualizado O campo onde os animais atualizados devem ser colocados.
     * @param novosCoelhos Uma lista para armazenar os novos coelhos nascidos.
     */
    @Override
    public void agir(Campo campoAtual, Campo campoAtualizado, List<Ator> novosCoelhos)
    {
        incrementarIdade();
        incrementarFome(); // Coelhos agora sentem fome!
        
        if(estaVivo()) {
            // Tenta comer grama onde está pisando (no campo atualizado/futuro)
            int comida = campoAtualizado.comerGrama(getLocalizacao());
            if(comida > 0) {
                setNivelAlimento(getNivelAlimento() + comida);
                if (getNivelAlimento() > VALOR_ALIMENTAR_MAX) setNivelAlimento(VALOR_ALIMENTAR_MAX);
            }
            
            // Reprodução
            int nascimentos = reproduzir();
            for(int b = 0; b < nascimentos; b++) {
                Localizacao loc = campoAtualizado.localizacaoAdjacenteLivre(getLocalizacao());
                if (loc != null) {
                    Coelho novoCoelho = new Coelho(false);
                    novosCoelhos.add(novoCoelho);
                    novoCoelho.definirLocalizacao(loc);
                    campoAtualizado.colocar(novoCoelho, loc);
                }
            }
            
            // Movimento
            Localizacao novaLocalizacao = campoAtualizado.localizacaoAdjacenteLivre(getLocalizacao());
            if(novaLocalizacao != null) {
                definirLocalizacao(novaLocalizacao);
                campoAtualizado.colocar(this, novaLocalizacao);
            }
            else {
                definirEstaVivo(false);
            }
        }
    }

    /**
     * A idade máxima que um coelho pode atingir.
     * 
     * @return A idade máxima.
     */
    @Override
    protected int IDADE_MAXIMA() {
        return IDADE_MAXIMA;
    }

    /**
     * A probabilidade de um coelho se reproduzir.
     * 
     * @return A probabilidade de reprodução.
     */
    @Override
    protected double PROBABILIDADE_REPRODUCAO() {
        return PROBABILIDADE_REPRODUCAO;
    }

    /**
     * O tamanho máximo da ninhada.
     * 
     * @return O tamanho máximo da ninhada.
     */
    @Override
    protected int TAMANHO_MAXIMO_NINHADA() {
        return TAMANHO_MAXIMO_NINHADA;
    }

    /**
     * A idade em que um coelho começa a procriar.
     * @return A idade reprodutiva.
     */
    @Override
    protected int getIdadeReprodutiva() {
        return IDADE_REPRODUTIVA;
    }
}
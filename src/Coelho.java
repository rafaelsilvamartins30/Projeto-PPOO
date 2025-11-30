/**
 * Um modelo simples de um coelho.
 * Coelhos envelhecem, se movem, se reproduzem e morrem.
 * 
 * @author David J. Barnes e Michael Kolling
 * @version 2002-04-11 (traduzido)
 */
public class Coelho extends Herbivoro {

    /**
     * Cria um novo coelho. A idade pode ser aleatória ou zero (novo nascimento).
     * O nível de alimento também é inicializado. 
     * 
     * @param idadeAleatoria Se verdadeiro, o coelho terá uma idade aleatória.
     */
    public Coelho(boolean idadeAleatoria) {
        super(idadeAleatoria);
    }

    /**
     * Cria um novo coelho filhote.
     * @return Um novo coelho.
     */    
    @Override
    public Herbivoro criarFilho() {
        return new Coelho(false);
    }

    /**
     * A idade máxima que um coelho pode atingir.
     * 
     * @return A idade máxima.
     */
    @Override
    protected int idadeMaxima() {
        return Configuracao.IDADE_MAX_COELHO;
    }

    /**
     * A probabilidade de um coelho se reproduzir.
     * 
     * @return A probabilidade de reprodução.
     */
    @Override
    protected double probabilidadeReproducao() {
        return Configuracao.PROB_REPROD_COELHO;
    }

    /**
     * O tamanho máximo da ninhada.
     * 
     * @return O tamanho máximo da ninhada.
     */
    @Override
    protected int tamanhoMaximoNinhada() {
        return Configuracao.MAX_NINHADA_COELHO;
    }

    /**
     * A idade em que um coelho começa a procriar.
     * @return A idade reprodutiva.
     */
    @Override
    protected int getIdadeReprodutiva() {
        return Configuracao.IDADE_REPROD_COELHO;
    }
}
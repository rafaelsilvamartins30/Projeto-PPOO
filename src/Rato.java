/**
 * Um modelo simples de um rato.
 * Ratos envelhecem, se movem, se reproduzem e morrem.
 */
public class Rato extends Herbivoro {

    /**
     * Cria um rato. A idade pode ser aleatória ou zero (novo nascimento).
     * 
     * @param idadeAleatoria se verdadeiro, a idade do rato será aleatória
     */
    public Rato(boolean idadeAleatoria) {
        super(idadeAleatoria);
    }

    /**
     * Cria um novo rato filhote.
     * @return Um novo rato.
     */    
    @Override
    public Herbivoro criarFilho() { return new Rato(false); }

    /**
     * Retorna a idade máxima do rato.
     * 
     * @return a idade máxima do rato
     */
    @Override
    protected int idadeMaxima() { return Configuracao.IDADE_MAX_RATO; }

    /**
     * Retorna a probabilidade de reprodução do rato.
     * 
     * @return a probabilidade de reprodução do rato
     */
    @Override
    protected double probabilidadeReproducao() { return Configuracao.PROB_REPROD_RATO; }

    /**
     * Retorna o tamanho máximo da ninhada do rato.
     * 
     * @return o tamanho máximo da ninhada do rato
     */
    @Override
    protected int tamanhoMaximoNinhada() { return Configuracao.MAX_NINHADA_RATO; }

    /**
     * Retorna a idade reprodutiva do rato.
     * 
     * @return a idade reprodutiva do rato
     */
    @Override
    protected int getIdadeReprodutiva() { return Configuracao.IDADE_REPROD_RATO; }
}
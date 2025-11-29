/**
 * Modelo de um Gavião.
 * Caça Ratos.
 */
public class Gaviao extends Predador {
    /**
     * Cria um gavião. A idade pode ser aleatória ou zero (novo nascimento).
     * 
     * @param idadeAleatoria
     */
    public Gaviao(boolean idadeAleatoria) {
        super(idadeAleatoria);
        dieta.put(Rato.class, Configuracao.VALOR_NUTRICIONAL_RATO);
    }

    /**
     * Cria um novo gavião filhote.
     * 
     * @return Um novo gavião.
     */
    @Override
    public Predador criarFilho() { return new Gaviao(false); }

    /**
     * A idade máxima que um gavião pode atingir.
     * 
     * @return A idade máxima.
     */
    @Override
    protected int idadeMaxima() {
        return Configuracao.IDADE_MAX_GAVIAO;
    }

    /**
     * A probabilidade de um gavião se reproduzir.
     * 
     * @return A probabilidade de reprodução.
     */
    @Override
    protected double probabilidadeReproducao() {
        return Configuracao.PROB_REPROD_GAVIAO;
    }

    /**
     * O tamanho máximo da ninhada.
     * 
     * @return O tamanho máximo da ninhada.
     */
    @Override
    protected int tamanhoMaximoNinhada() {
        return Configuracao.MAX_NINHADA_GAVIAO;
    }

    /**
     * A idade em que um gavião começa a procriar.
     * 
     * @return A idade reprodutiva.
     */
    @Override
    protected int getIdadeReprodutiva() {
        return Configuracao.IDADE_REPROD_GAVIAO;
    }
}
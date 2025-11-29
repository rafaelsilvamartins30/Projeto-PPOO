/**
 * Modelo de uma Cobra.
 * Caça Ratos e Coelhos.
 */
public class Cobra extends Predador {

    /**
     * Cria uma cobra. A idade pode ser aleatória ou zero (novo nascimento).
     * A dieta da cobra inclui ratos e coelhos.
     * @param idadeAleatoria
     */
    public Cobra(boolean idadeAleatoria) {
        super(idadeAleatoria);
        dieta.put(Rato.class, Configuracao.VALOR_NUTRICIONAL_RATO);
        dieta.put(Coelho.class, Configuracao.VALOR_NUTRICIONAL_COELHO);
    }
    
    /**
     * Cria uma nova cobra filhote.
     * @return Uma nova cobra.
     */
    @Override
    public Predador criarFilho() { return new Cobra(false); }

    /**
     * A idade máxima que uma cobra pode atingir.
     */
    @Override protected int idadeMaxima() { return Configuracao.IDADE_MAX_COBRA; }

    /**
     * A probabilidade de reprodução da cobra.
     */
    @Override protected double probabilidadeReproducao() { return Configuracao.PROB_REPROD_COBRA; }

    /**
     * O tamanho máximo da ninhada da cobra.
     */
    @Override protected int tamanhoMaximoNinhada() { return Configuracao.MAX_NINHADA_COBRA; }

    /**
     * A idade em que uma cobra começa a procriar.
     */
    @Override protected int getIdadeReprodutiva() { return Configuracao.IDADE_REPROD_COBRA; }
}
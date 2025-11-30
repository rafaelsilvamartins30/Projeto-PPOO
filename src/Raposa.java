/**
 * Um modelo simples de uma raposa.
 * Raposas envelhecem, se movem, caçam coelhos e morrem.
 * 
 * @author David J. Barnes e Michael Kolling
 * @version 2002-04-11 (traduzido)
 */
public class Raposa extends Predador {
    /**
     * Cria uma raposa. Pode ser criada como recém-nascida (idade zero
     * e não faminta) ou com idade aleatória.
     * A dieta da raposa inclui coelhos e ratos.
     * 
     * @param idadeAleatoria Se verdadeiro, a raposa terá idade e nível de fome
     *                       aleatórios.
     */
    public Raposa(boolean idadeAleatoria) {
        super(idadeAleatoria);
        dieta.put(Coelho.class, Configuracao.VALOR_NUTRICIONAL_COELHO);
        dieta.put(Rato.class, Configuracao.VALOR_NUTRICIONAL_RATO);
    }

    /**
     * Cria uma nova raposa filhote.
     * 
     * @return Uma nova raposa.
     */
    @Override
    public Predador criarFilho() { return new Raposa(false); }

    /**
     * Retorna a idade máxima da raposa.
     * 
     * @return A idade máxima.
     */
    @Override
    protected int idadeMaxima() { return Configuracao.IDADE_MAX_RAPOSA; }

    /**
     * A probabilidade de reprodução da raposa.
     * 
     * @return A probabilidade de reprodução.
     */
    @Override
    protected double probabilidadeReproducao() { return Configuracao.PROB_REPROD_RAPOSA; }

    /**
     * O tamanho máximo da ninhada da raposa.
     * 
     * @return O tamanho máximo da ninhada.
     */
    @Override
    protected int tamanhoMaximoNinhada() { return Configuracao.MAX_NINHADA_RAPOSA; }

    /**
     * A idade em que uma raposa começa a procriar.
     * 
     * @return A idade reprodutiva.
     */
    @Override
    protected int getIdadeReprodutiva() { return Configuracao.IDADE_REPROD_RAPOSA; }
}

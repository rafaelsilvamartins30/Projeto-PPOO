import java.util.List;

/**
 * Um modelo simples de um rato.
 * Ratos envelhecem, se movem, se reproduzem e morrem.
 */
public class Rato extends Animal {

    private static final int IDADE_REPRODUTIVA = 3;
    private static final int IDADE_MAXIMA = 30;
    private static final double PROBABILIDADE_REPRODUCAO = 0.20;
    private static final int TAMANHO_MAXIMO_NINHADA = 6;
    private static final int VALOR_ALIMENTAR_MAX = 20;

    /**
     * Cria um rato. A idade pode ser aleatória ou zero (novo nascimento).
     * 
     * @param idadeAleatoria se verdadeiro, a idade do rato será aleatória
     */
    public Rato(boolean idadeAleatoria) {
        super(idadeAleatoria);
        if (idadeAleatoria) {
            setNivelAlimento(getAleatorio().nextInt(VALOR_ALIMENTAR_MAX));
        } else {
            setNivelAlimento(VALOR_ALIMENTAR_MAX);
        }
    }

    /**
     * Define o comportamento do rato em cada passo do tempo.
     * 
     * @param campoAtual      o campo atual
     * @param campoAtualizado o campo atualizado
     * @param novosRatos      a lista onde novos ratos serão adicionados
     */
    @Override
    public void agir(Campo campoAtual, Campo campoAtualizado, List<Ator> novosRatos) {
        incrementarIdade();
        incrementarFome();

        if (estaVivo()) {
            int comida = campoAtualizado.comerGrama(getLocalizacao());
            if (comida > 0) {
                setNivelAlimento(getNivelAlimento() + comida);
                if (getNivelAlimento() > VALOR_ALIMENTAR_MAX)
                    setNivelAlimento(VALOR_ALIMENTAR_MAX);
            }
            int nascimentos = reproduzir();
            for (int b = 0; b < nascimentos; b++) {
                Localizacao loc = campoAtualizado.localizacaoAdjacenteLivre(getLocalizacao());
                if (loc != null) {
                    Rato novoRato = new Rato(false);
                    novosRatos.add(novoRato);
                    novoRato.definirLocalizacao(loc);
                    campoAtualizado.colocar(novoRato, loc);
                }
            }
            Localizacao novaLocalizacao = campoAtualizado.localizacaoAdjacenteLivre(getLocalizacao());
            if (novaLocalizacao != null) {
                definirLocalizacao(novaLocalizacao);
                campoAtualizado.colocar(this, novaLocalizacao);
            } else {
                definirEstaVivo(false);
            }
        }
    }

    /**
     * Retorna a idade máxima do rato.
     * 
     * @return a idade máxima do rato
     */
    @Override
    protected int IDADE_MAXIMA() {
        return IDADE_MAXIMA;
    }

    /**
     * Retorna a probabilidade de reprodução do rato.
     * 
     * @return a probabilidade de reprodução do rato
     */
    @Override
    protected double PROBABILIDADE_REPRODUCAO() {
        return PROBABILIDADE_REPRODUCAO;
    }

    /**
     * Retorna o tamanho máximo da ninhada do rato.
     * 
     * @return o tamanho máximo da ninhada do rato
     */
    @Override
    protected int TAMANHO_MAXIMO_NINHADA() {
        return TAMANHO_MAXIMO_NINHADA;
    }

    /**
     * Retorna a idade reprodutiva do rato.
     * 
     * @return a idade reprodutiva do rato
     */
    @Override
    protected int getIdadeReprodutiva() {
        return IDADE_REPRODUTIVA;
    }
}

import java.util.Iterator;
import java.util.List;

/**
 * Modelo de um Gavião.
 * Caça Ratos.
 * Pode ser afetado por condições climáticas (ex: chuva).
 */
public class Gaviao extends Animal {
    private static final int IDADE_REPRODUTIVA = 5;
    private static final int IDADE_MAXIMA = 70;
    private static final double PROBABILIDADE_REPRODUCAO = 0.08;
    private static final int TAMANHO_MAXIMO_NINHADA = 3;
    private static final int VALOR_ALIMENTAR = 8;

    /**
     * Cria um gavião. A idade pode ser aleatória ou zero (novo nascimento).
     * 
     * @param idadeAleatoria
     */
    public Gaviao(boolean idadeAleatoria) {
        super(idadeAleatoria);
        setNivelAlimento(VALOR_ALIMENTAR);
        if (idadeAleatoria)
            setNivelAlimento(getAleatorio().nextInt(VALOR_ALIMENTAR));
    }

    /**
     * Define o comportamento do gavião em cada passo do tempo.
     * 
     * @param campoAtual      o campo atual
     * @param campoAtualizado o campo atualizado
     * @param novosGavioes    a lista onde novos gaviões serão adicionados
     */
    @Override
    public void agir(Campo campoAtual, Campo campoAtualizado, List<Ator> novosGavioes) {
        incrementarIdade();
        incrementarFome();
        if (estaVivo()) {

            int nascimentos = reproduzir();
            for (int i = 0; i < nascimentos; i++) {
                Localizacao loc = campoAtualizado.localizacaoAdjacenteLivre(getLocalizacao());
                if (loc != null) {
                    Gaviao novoGaviao = new Gaviao(false);
                    novosGavioes.add(novoGaviao);
                    novoGaviao.definirLocalizacao(loc);
                    campoAtualizado.colocar(novoGaviao, loc);
                }
            }
            Localizacao novaLocalizacao = encontrarComida(campoAtual, getLocalizacao());
            if (novaLocalizacao == null) {
                novaLocalizacao = campoAtualizado.localizacaoAdjacenteLivre(getLocalizacao());
            }
            if (novaLocalizacao != null) {
                definirLocalizacao(novaLocalizacao);
                campoAtualizado.colocar(this, novaLocalizacao);
            } else {
                definirEstaVivo(false);
            }
        }
    }

    /**
     * Encontra comida adjacente (Ratos). Se encontrar, come e retorna a
     * localização.
     * 
     * @param campo       representando o campo atual
     * @param localizacao do gavião
     * @return a localização onde a comida foi encontrada, ou null se nada foi
     *         encontrado
     */
    private Localizacao encontrarComida(Campo campo, Localizacao localizacao) {
        Iterator adjacentes = campo.localizacoesAdjacentes(localizacao);
        while (adjacentes.hasNext()) {
            Localizacao onde = (Localizacao) adjacentes.next();
            Object animal = campo.getObjetoEm(onde);
            if (animal instanceof Rato) {
                Rato rato = (Rato) animal;
                if (rato.estaVivo()) {
                    rato.definirEstaVivo(false);
                    setNivelAlimento(VALOR_ALIMENTAR);
                    return onde;
                }
            }
        }
        return null;
    }

    /**
     * A idade máxima que um gavião pode atingir.
     * 
     * @return A idade máxima.
     */
    @Override
    protected int IDADE_MAXIMA() {
        return IDADE_MAXIMA;
    }

    /**
     * A probabilidade de um gavião se reproduzir.
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
     * A idade em que um gavião começa a procriar.
     * 
     * @return A idade reprodutiva.
     */
    @Override
    protected int getIdadeReprodutiva() {
        return IDADE_REPRODUTIVA;
    }
}
import java.util.Iterator;
import java.util.List;

/**
 * Modelo de uma Cobra.
 * Caça Ratos e Coelhos.
 */
public class Cobra extends Animal {
    private static final int IDADE_REPRODUTIVA = 8;
    private static final int IDADE_MAXIMA = 80;
    private static final double PROBABILIDADE_REPRODUCAO = 0.10;
    private static final int TAMANHO_MAXIMO_NINHADA = 4;
    private static final int VALOR_ALIMENTAR = 6; 

    /**
     * Cria uma cobra. A idade pode ser aleatória ou zero (novo nascimento).
     * @param idadeAleatoria
     */
    public Cobra(boolean idadeAleatoria) {
        super(idadeAleatoria);
        setNivelAlimento(VALOR_ALIMENTAR);
        if(idadeAleatoria) {
            setNivelAlimento(getAleatorio().nextInt(VALOR_ALIMENTAR));
        }
    }
    
    /**
     * Define o comportamento da cobra em cada passo do tempo.
     * @param campoAtual o campo atual
     * @param campoAtualizado o campo atualizado
     * @param novasCobras a lista onde novas cobras serão adicionadas
     */
    @Override
    public void agir(Campo campoAtual, Campo campoAtualizado, List<Ator> novasCobras) {
        incrementarIdade();
        incrementarFome();
        if(estaVivo()) {
            int nascimentos = reproduzir();
            for(int i = 0; i < nascimentos; i++) {
                Localizacao loc = campoAtualizado.localizacaoAdjacenteLivre(getLocalizacao());
                if (loc != null) {
                    Cobra novaCobra = new Cobra(false);
                    novasCobras.add(novaCobra);
                    novaCobra.definirLocalizacao(loc);
                    campoAtualizado.colocar(novaCobra, loc);
                }
            }
            Localizacao novaLocalizacao = encontrarComida(campoAtual, getLocalizacao());
            if(novaLocalizacao == null) { 
                novaLocalizacao = campoAtualizado.localizacaoAdjacenteLivre(getLocalizacao());
            }
            if(novaLocalizacao != null) {
                definirLocalizacao(novaLocalizacao);
                campoAtualizado.colocar(this, novaLocalizacao);
            } else {
                definirEstaVivo(false);
            }
        }
    }
    
    /**
     * Encontra comida adjacente (Ratos ou Coelhos). Se encontrar, come e retorna a localização.    
     * @param campo representando o campo atual
     * @param localizacao da cobra
     * @return a localização onde a comida foi encontrada, ou null se nada foi encontrado
     */
    private Localizacao encontrarComida(Campo campo, Localizacao localizacao) {
        Iterator adjacentes = campo.localizacoesAdjacentes(localizacao);
        while(adjacentes.hasNext()) {
            Localizacao onde = (Localizacao) adjacentes.next();
            Object animal = campo.getObjetoEm(onde);
            if(animal instanceof Rato) {
                Rato rato = (Rato) animal;
                if(rato.estaVivo()) { 
                    rato.definirEstaVivo(false);
                    setNivelAlimento(VALOR_ALIMENTAR);
                    return onde;
                }
            }
            else if(animal instanceof Coelho) {
                Coelho coelho = (Coelho) animal;
                if(coelho.estaVivo()) { 
                    coelho.definirEstaVivo(false);
                    setNivelAlimento(VALOR_ALIMENTAR);
                    return onde;
                }
            }
        }
        return null;
    }
    
    /**
     * Indica que a cobra foi comida (por exemplo, por um predador maior).
     */
    public void foiComida() { definirEstaVivo(false); }

    /**
     * A idade máxima que uma cobra pode atingir.
     */
    @Override protected int IDADE_MAXIMA() { return IDADE_MAXIMA; }

    /**
     * A probabilidade de reprodução da cobra.
     */
    @Override protected double PROBABILIDADE_REPRODUCAO() { return PROBABILIDADE_REPRODUCAO; }

    /**
     * O tamanho máximo da ninhada da cobra.
     */
    @Override protected int TAMANHO_MAXIMO_NINHADA() { return TAMANHO_MAXIMO_NINHADA; }

    /**
     * A idade em que uma cobra começa a procriar.
     */
    @Override protected int getIdadeReprodutiva() { return IDADE_REPRODUTIVA; }
}

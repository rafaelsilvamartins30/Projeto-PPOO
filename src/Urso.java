import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Urso extends Animal {
    // Constantes específicas da classe Urso
    // Idade em que o urso pode começar a se reproduzir
    private static final int IDADE_REPRODUTIVA = 10;
    // Idade máxima que um urso pode atingir
    private static final int IDADE_MAXIMA = 120;
    // Probabilidade de reprodução do urso
    private static final double PROBABILIDADE_REPRODUCAO = 0.05;
    // Tamanho máximo da ninhada do urso
    private static final int TAMANHO_MAXIMO_NINHADA = 2;
    // Valor alimentar de um urso
    private static final int VALOR_ALIMENTAR = 12;

    /**
     * Cria um urso. A idade pode ser aleatória ou zero (novo nascimento).
     * @param idadeAleatoria se verdadeiro, a idade do urso será aleatória
     */
    public Urso(boolean idadeAleatoria) {
        super(idadeAleatoria);
        // Começa bem alimentado
        setNivelAlimento(VALOR_ALIMENTAR);
        // Se idade aleatória, o nível de alimento também é aleatório
        if(idadeAleatoria) setNivelAlimento(getAleatorio().nextInt(VALOR_ALIMENTAR));
    }
    
    /**
     * Define o comportamento do urso em cada passo do tempo.
     * @param campoAtual o campo atual
     * @param campoAtualizado o campo atualizado
     * @param novosUrsos a lista onde novos ursos serão adicionados
     */
    @Override
    public void agir(Campo campoAtual, Campo campoAtualizado, List<Ator> novosUrsos) {
        incrementarIdade();
        incrementarFome();
        if(estaVivo()) {
            // Tenta pescar antes de caçar
            pescar(campoAtual);

            int nascimentos = reproduzir();
            for(int i = 0; i < nascimentos; i++) {
                Localizacao loc = campoAtualizado.localizacaoAdjacenteLivre(getLocalizacao());
                if (loc != null) {
                    Urso novoUrso = new Urso(false);
                    novosUrsos.add(novoUrso);
                    novoUrso.definirLocalizacao(loc);
                    campoAtualizado.colocar(novoUrso, loc);
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
    
    // Habilidade especial: Pescar no rio
    private void pescar(Campo campo) {
        Iterator adjacentes = campo.localizacoesAdjacentes(getLocalizacao());
        while(adjacentes.hasNext()) {
            Localizacao onde = (Localizacao) adjacentes.next();
            Object coisa = campo.getObjetoEm(onde);
            if(coisa instanceof Obstaculo && coisa == Obstaculo.RIO) {
                // Se está perto do rio, tem 30% de chance de conseguir um peixe
                if(new Random().nextDouble() < 0.30) {
                    setNivelAlimento(VALOR_ALIMENTAR); // Enche a barriga
                }
                return; // Só tenta pescar uma vez por turno
            }
        }
    }
    
    /**
     *  Encontra comida adjacente (Raposa, Cobra ou Coelho). Se encontrar, come e retorna a localização.
     * @param campo representando o campo atual.
     * @param localizacao do urso.
     * @return a localização onde a comida foi encontrada, ou null se nada foi encontrado.
     */
    private Localizacao encontrarComida(Campo campo, Localizacao localizacao) {
        Iterator adjacentes = campo.localizacoesAdjacentes(localizacao);
        while(adjacentes.hasNext()) {
            Localizacao onde = (Localizacao) adjacentes.next();
            Object animal = campo.getObjetoEm(onde);
            
            // Urso é o rei: come Raposa, Cobra e Coelho
            if(animal instanceof Raposa) {
                Raposa raposa = (Raposa) animal;
                if(raposa.estaVivo()) { 
                    raposa.definirEstaVivo(false); 
                    setNivelAlimento(VALOR_ALIMENTAR); 
                    return onde;
                }
            }
            else if(animal instanceof Cobra) {
                Cobra cobra = (Cobra) animal;
                if(cobra.estaVivo()) {
                    cobra.definirEstaVivo(false);
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
     * A idade em que um urso começa a procriar.
     * @return A idade reprodutiva.
     */
    @Override protected int IDADE_MAXIMA() { return IDADE_MAXIMA; }

    /**
     * A probabilidade de um urso se reproduzir.
     * @return A probabilidade de reprodução.
     */
    @Override protected double PROBABILIDADE_REPRODUCAO() { return PROBABILIDADE_REPRODUCAO; }

    /**
     * O tamanho máximo da ninhada.
     * @return O tamanho máximo da ninhada.
     */
    @Override protected int TAMANHO_MAXIMO_NINHADA() { return TAMANHO_MAXIMO_NINHADA; }

    /**
     * A idade em que um urso começa a procriar.
     * @return A idade reprodutiva.
     */
    @Override protected int getIdadeReprodutiva() { return IDADE_REPRODUTIVA; }
}
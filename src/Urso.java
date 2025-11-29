import java.util.Iterator;
import java.util.List;

/**
 * A classe Urso representa ursos em um campo. Os ursos caçam raposas, cobras e
 * coelhos para se alimentar. Eles também podem pescar em rios adjacentes.
 * 
 * Os ursos têm uma idade reprodutiva, uma idade máxima, uma probabilidade de
 * reprodução, um tamanho máximo de ninhada e um valor alimentar.
 * 
 * @author Grupo de Projeto PPOO 10
 * @version 2025.11.30
 */
public class Urso extends Predador {
    /**
     * Cria um urso. A idade pode ser aleatória ou zero (novo nascimento).
     * A dieta do urso inclui raposas, cobras e coelhos.
     * 
     * @param idadeAleatoria se verdadeiro, a idade do urso será aleatória
     */
    public Urso(boolean idadeAleatoria) {
        super(idadeAleatoria);
        
        dieta.put(Raposa.class, Configuracao.VALOR_NUTRICIONAL_RAPOSA);
        dieta.put(Cobra.class, Configuracao.VALOR_NUTRICIONAL_COBRA);
        dieta.put(Coelho.class, Configuracao.VALOR_NUTRICIONAL_COELHO);
    }

    /**
     * O urso age: caça, se move, pesca, se reproduz e envelhece.
     * 
     * @param campoAtual       O campo atual.
     * @param campoAtualizado  O campo atualizado.
     * @param novosUrsos A lista para adicionar novos ursos nascidos.
     */
    @Override
    public void agir(Campo campoAtual, Campo campoAtualizado, List<Ator> novosUrsos) {
        super.agir(campoAtual, campoAtualizado, novosUrsos);
        if (estaVivo()) {
            pescar(campoAtual);
        }
    }

    /**
     * Cria um novo urso filhote.
     * 
     * @return Um novo urso.
     */
    @Override
    public Predador criarFilho() { return new Urso(false); }

    /**
     * Tenta pescar em um rio adjacente com 30% de chance de ser bem sucedido. Se tiver sucesso, aumenta o nível de
     * alimento.
     * 
     * @param campo representando o campo atual.
     */
    private void pescar(Campo campo) {
        Iterator<Localizacao> adjacentes = campo.localizacoesAdjacentes(getLocalizacao());
        while (adjacentes.hasNext()) {
            Localizacao onde = adjacentes.next();
            Object pescavel = campo.getObjetoEm(onde);
            if (pescavel instanceof Obstaculo && ((Obstaculo)pescavel).podePescar()) {

                if (getAleatorio().nextDouble() < Configuracao.PROB_PESCA_URSO) {
                    setNivelAlimento(Configuracao.VALOR_ALIMENTAR);
                }
                return;
            }
        }
    }

    /**
     * A idade em que um urso começa a procriar.
     * 
     * @return A idade reprodutiva.
     */
    @Override
    protected int idadeMaxima() {
        return Configuracao.IDADE_MAX_URSO;
    }

    /**
     * A probabilidade de um urso se reproduzir.
     * 
     * @return A probabilidade de reprodução.
     */
    @Override
    protected double probabilidadeReproducao() {
        return Configuracao.PROB_REPROD_URSO;
    }

    /**
     * O tamanho máximo da ninhada.
     * 
     * @return O tamanho máximo da ninhada.
     */
    @Override
    protected int tamanhoMaximoNinhada() {
        return Configuracao.MAX_NINHADA_URSO;
    }

    /**
     * A idade em que um urso começa a procriar.
     * 
     * @return A idade reprodutiva.
     */
    @Override
    protected int getIdadeReprodutiva() { return Configuracao.IDADE_REPROD_URSO; }
}
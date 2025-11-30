import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Classe abstrata que representa predadores no ecossistema de simulação.
 * <p>
 * Esta classe implementa o comportamento base de todos os predadores,
 * incluindo caça, alimentação e gestão de dieta. Cada predador específico
 * (Raposa, Urso, Cobra, Gavião) herda desta classe e define seus próprios
 * parâmetros de vida e reprodução.
 * </p>
 * 
 * <p>
 * <strong>Características Comuns de Predadores:</strong>
 * </p>
 * <ul>
 * <li>Caçam ativamente outras espécies para sobreviver</li>
 * <li>Possuem um mapa de dieta configurável (presa → valor nutricional)</li>
 * <li>Morrem de fome se não conseguirem se alimentar</li>
 * <li>Movem-se de forma direcionada ao caçar ou aleatória ao explorar</li>
 * </ul>
 * 
 * <p>
 * <strong>Mecânica de Caça:</strong>
 * </p>
 * <ol>
 * <li>Verifica localizações adjacentes em busca de presas</li>
 * <li>Identifica presas válidas usando o mapa de dieta</li>
 * <li>Ataca e se alimenta, eliminando a presa</li>
 * <li>Move-se para a localização da presa</li>
 * <li>Se não encontra presa, move-se livremente</li>
 * </ol>
 * 
 * <p>
 * <strong>Sistema de Alimentação:</strong>
 * </p>
 * <p>
 * Cada tipo de presa fornece um valor nutricional específico, definido
 * no mapa de dieta. O predador acumula alimento e perde gradualmente
 * a cada turno (fome). Quando o alimento chega a zero, o predador morre.
 * </p>
 * 
 * @author Projeto PPOO Grupo 10
 * @version 2024-06
 * @see Animal
 * @see Raposa
 * @see Urso
 * @see Cobra
 * @see Gaviao
 */
public abstract class Predador extends Animal {

    // ========== ATRIBUTOS ==========

    /**
     * Mapa que define a dieta do predador.
     * <p>
     * Estrutura:
     * <ul>
     * <li><strong>Chave:</strong> Classe da presa (ex: Coelho.class)</li>
     * <li><strong>Valor:</strong> Valor nutricional obtido ao consumir</li>
     * </ul>
     * </p>
     * <p>
     * Cada subclasse configura sua dieta específica no construtor,
     * permitindo diferentes estratégias alimentares entre predadores.
     * </p>
     */
    protected Map<Class<?>, Integer> dieta;

    // ========== CONSTRUTOR ==========

    /**
     * Cria uma nova instância de predador.
     * <p>
     * Inicializa o mapa de dieta vazio. Subclasses devem popular
     * este mapa com suas presas específicas no construtor.
     * </p>
     * 
     * @param idadeAleatoria Se true, a idade será aleatória entre 0 e idade máxima;
     *                       se false, a idade será zero (recém-nascido)
     */
    public Predador(boolean idadeAleatoria) {
        super(idadeAleatoria);
        dieta = new HashMap<>();
    }

    // ========== MÉTODOS DE AÇÃO ==========

    /**
     * Executa o ciclo completo de ação do predador em um turno.
     * <p>
     * <strong>Sequência de operações:</strong>
     * <ol>
     * <li>Incrementa idade (envelhecimento)</li>
     * <li>Incrementa fome (consome energia)</li>
     * <li>Se ainda vivo: tenta reproduzir</li>
     * <li>Se ainda vivo: caça ou move-se</li>
     * </ol>
     * </p>
     * <p>
     * O predador pode morrer de fome ou velhice durante este processo,
     * caso em que as etapas seguintes não são executadas.
     * </p>
     * 
     * @param campoAtual      Campo no estado atual (leitura)
     * @param campoAtualizado Campo sendo construído para o próximo passo
     * @param novosPredadores Lista onde filhotes nascidos serão adicionados
     */
    @Override
    public void agir(CampoInterativo campoAtual, CampoInterativo campoAtualizado, List<Ator> novosPredadores) {
        incrementarIdade();
        incrementarFome();
        if (estaVivo()) {
            processarReproducao(campoAtualizado, novosPredadores);
            processarCacaMovimento(campoAtual, campoAtualizado);
        }
    }

    // ========== MÉTODOS DE CAÇA E MOVIMENTO ==========

    /**
     * Gerencia o movimento do predador baseado na disponibilidade de presas.
     * <p>
     * <strong>Fluxo de decisão:</strong>
     * <ol>
     * <li>Tenta localizar uma presa adjacente</li>
     * <li>Se encontrou: move-se para o local da presa (caça)</li>
     * <li>Se não encontrou: move-se aleatoriamente (exploração)</li>
     * </ol>
     * </p>
     * <p>
     * Este método implementa o comportamento de caça oportunista:
     * o predador ataca quando há oportunidade, mas não persegue
     * ativamente presas distantes.
     * </p>
     * 
     * @param campoAtual      Campo atual com posições de todos os animais
     * @param campoAtualizado Campo de destino onde o predador será posicionado
     */
    private void processarCacaMovimento(CampoInterativo campoAtual, CampoInterativo campoAtualizado) {
        Localizacao locPresa = cacar(campoAtual, getLocalizacao());

        if (locPresa != null) {
            moverPara(locPresa, campoAtualizado);
        } else {
            tentarMoverLivremente(campoAtualizado);
        }
    }

    /**
     * Busca e consome uma presa em localizações adjacentes.
     * <p>
     * <strong>Algoritmo de caça:</strong>
     * <ol>
     * <li>Itera sobre todas as localizações adjacentes</li>
     * <li>Verifica se há um objeto na localização</li>
     * <li>Verifica se o objeto é uma presa válida (está no mapa de dieta)</li>
     * <li>Verifica se a presa está viva</li>
     * <li>Se todas as condições forem atendidas: consome a presa</li>
     * <li>Retorna a localização da presa encontrada</li>
     * </ol>
     * </p>
     * <p>
     * <strong>Nota:</strong> Este método apenas identifica e consome a presa,
     * mas NÃO move o predador. O movimento é feito por
     * {@link #processarCacaMovimento}.
     * </p>
     * 
     * @param campo       Campo atual onde buscar presas
     * @param localizacao Posição atual do predador
     * @return Localização da presa consumida, ou null se nenhuma foi encontrada
     */
    private Localizacao cacar(CampoInterativo campo, Localizacao localizacao) {
        Iterator<Localizacao> adjacentes = campo.localizacoesAdjacentes(localizacao);
        while (adjacentes.hasNext()) {
            Localizacao onde = adjacentes.next();
            Object objeto = campo.getObjetoEm(onde);

            if (objeto != null && dieta.containsKey(objeto.getClass())) {
                Animal presa = (Animal) objeto;
                if (presa.estaVivo()) {
                    come(presa);
                    return onde;
                }
            }
        }
        return null;
    }

    /**
     * Consome uma presa, eliminando-a e ganhando nutrição.
     * <p>
     * <strong>Efeitos da alimentação:</strong>
     * <ul>
     * <li>A presa é marcada como morta</li>
     * <li>O predador ganha calorias baseadas no tipo de presa</li>
     * <li>O nível de alimento é restaurado pelo valor nutricional da presa</li>
     * </ul>
     * </p>
     * <p>
     * O valor nutricional é obtido do mapa de dieta usando a classe da presa
     * como chave. Diferentes presas fornecem diferentes quantidades de energia.
     * </p>
     * 
     * @param presa Animal que foi capturado e será consumido
     */
    private void come(Animal presa) {
        presa.morrer();
        int calorias = dieta.get(presa.getClass());
        setNivelAlimento(calorias);
    }

    // ========== MÉTODOS ABSTRATOS ==========

    /**
     * Cria um novo filhote do tipo específico do predador.
     * <p>
     * Cada subclasse deve implementar este método retornando
     * uma nova instância de si mesma com idade zero.
     * </p>
     * <p>
     * <strong>Exemplo de implementação:</strong>
     * 
     * <pre>
     * {@code
     * @Override
     * public Predador criarFilho() {
     *     return new Raposa(false);
     * }
     * }
     * </pre>
     * </p>
     * 
     * @return Nova instância de predador representando um filhote
     */
    public abstract Predador criarFilho();
}
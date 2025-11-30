import java.util.Iterator;
import java.util.List;

/**
 * Classe que representa ursos no ecossistema de simulação.
 * <p>
 * Os ursos são predadores de topo da cadeia alimentar, caçando múltiplas
 * espécies e possuindo a habilidade única de pescar em rios.
 * </p>
 * 
 * <p>
 * <strong>Características do Urso:</strong>
 * </p>
 * <ul>
 * <li><strong>Dieta:</strong> Raposas, Cobras e Coelhos</li>
 * <li><strong>Habilidade Especial:</strong> Pesca em rios adjacentes</li>
 * <li><strong>Comportamento:</strong> Caça ativa, reprodução e
 * envelhecimento</li>
 * <li><strong>Posição na Cadeia:</strong> Predador de topo (não possui
 * predadores naturais)</li>
 * </ul>
 * 
 * <p>
 * <strong>Mecânica de Pesca:</strong>
 * </p>
 * <p>
 * Quando adjacente a um rio, o urso tem 30% de chance
 * ({@link Configuracao#PROB_PESCA_URSO})
 * de pescar com sucesso, restaurando completamente seu nível de alimento.
 * Esta habilidade oferece uma fonte alternativa de sustento além da caça.
 * </p>
 * 
 * <p>
 * <strong>Parâmetros Configuráveis:</strong>
 * </p>
 * <ul>
 * <li>Idade máxima de vida</li>
 * <li>Idade de início da reprodução</li>
 * <li>Probabilidade de reprodução</li>
 * <li>Tamanho máximo de ninhada</li>
 * <li>Valor nutricional de cada presa</li>
 * <li>Probabilidade de sucesso na pesca</li>
 * </ul>
 * 
 * @author Grupo de Projeto PPOO 10
 * @version 2025.11.30
 * @see Predador
 * @see Obstaculo
 * @see Configuracao
 */
public class Urso extends Predador {

    // ========== CONSTRUTOR ==========

    /**
     * Cria uma nova instância de urso com dieta predefinida.
     * <p>
     * A dieta é configurada automaticamente no construtor, incluindo:
     * <ul>
     * <li>Raposas - valor nutricional alto</li>
     * <li>Cobras - valor nutricional médio</li>
     * <li>Coelhos - valor nutricional baixo</li>
     * </ul>
     * </p>
     * <p>
     * A idade inicial pode ser:
     * <ul>
     * <li>Zero: representa um filhote recém-nascido</li>
     * <li>Aleatória: representa um urso já existente no início da simulação</li>
     * </ul>
     * </p>
     * 
     * @param idadeAleatoria Se true, a idade será aleatória entre 0 e idade máxima;
     *                       se false, a idade será zero (recém-nascido)
     */
    public Urso(boolean idadeAleatoria) {
        super(idadeAleatoria);

        dieta.put(Raposa.class, Configuracao.VALOR_NUTRICIONAL_RAPOSA);
        dieta.put(Cobra.class, Configuracao.VALOR_NUTRICIONAL_COBRA);
        dieta.put(Coelho.class, Configuracao.VALOR_NUTRICIONAL_COELHO);
    }

    // ========== MÉTODOS DE AÇÃO ==========

    /**
     * Executa o turno completo de ação do urso.
     * <p>
     * <strong>Sequência de ações executadas:</strong>
     * <ol>
     * <li>Executa comportamento padrão de predador (caça, movimento,
     * reprodução)</li>
     * <li>Se ainda estiver vivo, tenta pescar em rios adjacentes</li>
     * </ol>
     * </p>
     * <p>
     * A tentativa de pesca ocorre após as ações padrão, oferecendo uma
     * oportunidade adicional de obter alimento mesmo que a caça falhe.
     * </p>
     * 
     * @param campoAtual      Campo no estado atual (leitura)
     * @param campoAtualizado Campo sendo construído para o próximo passo
     * @param novosUrsos      Lista onde filhotes nascidos serão adicionados
     */
    @Override
    public void agir(CampoInterativo campoAtual, CampoInterativo campoAtualizado, List<Ator> novosUrsos) {
        super.agir(campoAtual, campoAtualizado, novosUrsos);
        if (estaVivo()) {
            pescar(campoAtual);
        }
    }

    /**
     * Tenta pescar em rios adjacentes à posição atual.
     * <p>
     * <strong>Mecânica de pesca:</strong>
     * <ol>
     * <li>Verifica todas as localizações adjacentes</li>
     * <li>Identifica se há um obstáculo pescável (rio)</li>
     * <li>Realiza teste de probabilidade
     * ({@link Configuracao#PROB_PESCA_URSO})</li>
     * <li>Em caso de sucesso, restaura completamente o nível de alimento</li>
     * </ol>
     * </p>
     * <p>
     * <strong>Nota:</strong> A pesca para após encontrar o primeiro rio,
     * mesmo que não seja bem-sucedida. Isto simula que o urso gasta tempo
     * e energia na tentativa.
     * </p>
     * 
     * @param campo Campo atual contendo a localização do urso e obstáculos
     */
    private void pescar(CampoInterativo campo) {
        Iterator<Localizacao> adjacentes = campo.localizacoesAdjacentes(getLocalizacao());
        while (adjacentes.hasNext()) {
            Localizacao onde = adjacentes.next();
            Object pescavel = campo.getObjetoEm(onde);

            if (pescavel instanceof Obstaculo && ((Obstaculo) pescavel).podePescar()) {
                if (getAleatorio().nextDouble() < Configuracao.PROB_PESCA_URSO) {
                    setNivelAlimento(Configuracao.VALOR_ALIMENTAR);
                }
                return;
            }
        }
    }

    // ========== MÉTODOS DE REPRODUÇÃO ==========

    /**
     * Cria um novo filhote de urso.
     * <p>
     * O filhote nasce com:
     * <ul>
     * <li>Idade zero (recém-nascido)</li>
     * <li>Dieta já configurada automaticamente</li>
     * <li>Nível de alimento inicial padrão</li>
     * </ul>
     * </p>
     * 
     * @return Nova instância de Urso representando um filhote
     */
    @Override
    public Predador criarFilho() {
        return new Urso(false);
    }

    // ========== MÉTODOS DE CONFIGURAÇÃO (da superclasse) ==========

    /**
     * Retorna a idade máxima que um urso pode atingir.
     * <p>
     * Quando esta idade é atingida, o urso morre naturalmente.
     * </p>
     * 
     * @return Idade máxima em passos de simulação
     * @see Configuracao#IDADE_MAX_URSO
     */
    @Override
    protected int idadeMaxima() {
        return Configuracao.IDADE_MAX_URSO;
    }

    /**
     * Retorna a idade em que o urso atinge maturidade sexual.
     * <p>
     * Ursos só podem se reproduzir após atingir esta idade.
     * </p>
     * 
     * @return Idade reprodutiva em passos de simulação
     * @see Configuracao#IDADE_REPROD_URSO
     */
    @Override
    protected int getIdadeReprodutiva() {
        return Configuracao.IDADE_REPROD_URSO;
    }

    /**
     * Retorna a probabilidade de reprodução por passo.
     * <p>
     * Em cada turno, se as condições forem atendidas (idade adequada,
     * parceiro próximo), esta probabilidade determina se ocorre reprodução.
     * </p>
     * 
     * @return Probabilidade entre 0.0 e 1.0
     * @see Configuracao#PROB_REPROD_URSO
     */
    @Override
    protected double probabilidadeReproducao() {
        return Configuracao.PROB_REPROD_URSO;
    }

    /**
     * Retorna o número máximo de filhotes em um único nascimento.
     * <p>
     * O número real de filhotes nascidos pode ser menor, dependendo
     * do espaço disponível ao redor dos pais.
     * </p>
     * 
     * @return Tamanho máximo da ninhada
     * @see Configuracao#MAX_NINHADA_URSO
     */
    @Override
    protected int tamanhoMaximoNinhada() {
        return Configuracao.MAX_NINHADA_URSO;
    }
}
import java.util.HashMap;
import java.util.Iterator;

/**
 * Classe responsável por coletar e fornecer estatísticas sobre o estado
 * populacional do campo.
 * <p>
 * Esta classe implementa um sistema flexível de contagem que automaticamente
 * rastreia qualquer tipo de entidade encontrada no campo, sem necessidade de
 * configuração prévia para novas espécies.
 * </p>
 * 
 * <p>
 * <strong>Funcionalidades Principais:</strong>
 * </p>
 * <ul>
 * <li>Contagem automática de populações por classe</li>
 * <li>Geração de relatórios textuais de população</li>
 * <li>Verificação de viabilidade do ecossistema</li>
 * <li>Sistema de cache para otimização de performance</li>
 * <li>Suporte a consultas individuais por espécie</li>
 * </ul>
 * 
 * <p>
 * <strong>Sistema de Cache:</strong>
 * </p>
 * <p>
 * As contagens são calculadas sob demanda e mantidas em cache até que sejam
 * invalidadas. Isto evita recálculos desnecessários quando múltiplas consultas
 * são feitas no mesmo passo de simulação.
 * </p>
 * 
 * <p>
 * <strong>Ciclo de Uso Típico:</strong>
 * </p>
 * <ol>
 * <li>{@link #reiniciar()} - Invalida cache e zera contadores</li>
 * <li>{@link #incrementarContagem(Class)} - Conta cada animal (múltiplas
 * chamadas)</li>
 * <li>{@link #contagemConcluida()} - Marca cache como válido</li>
 * <li>Consultas ({@link #getContagem(Class)},
 * {@link #ehViavel(GradeVisualizavel)})</li>
 * </ol>
 * 
 * <p>
 * <strong>Extensibilidade:</strong>
 * </p>
 * <p>
 * Novas espécies são automaticamente rastreadas sem modificação de código.
 * O sistema usa reflexão para obter nomes de classes dinamicamente.
 * </p>
 * 
 * @author David J. Barnes e Michael Kolling
 * @author Grupo 10
 * @version 2002-04-23 (traduzido e expandido)
 * @see Contador
 * @see GradeVisualizavel
 * @see VisualizacaoSimulador
 */
public class EstatisticasCampo {
    // ========== ATRIBUTOS ==========

    /**
     * Mapa que associa cada classe de entidade ao seu contador.
     * <p>
     * Estrutura: Class&lt;?&gt; → Contador
     * <br>
     * Permite rastreamento dinâmico de qualquer tipo de entidade
     * sem necessidade de declaração prévia.
     * </p>
     */
    private HashMap<Class<?>, Contador> contadores;

    /**
     * Flag indicando se as contagens atuais são válidas (sincronizadas com o
     * campo).
     * <p>
     * Estados:
     * <ul>
     * <li>true: Contagens refletem o estado atual do campo</li>
     * <li>false: Contagens precisam ser recalculadas</li>
     * </ul>
     * </p>
     */
    private boolean contagensValidas;

    // ========== CONSTRUTOR ==========

    /**
     * Cria uma nova instância de estatísticas do campo.
     * <p>
     * Inicializa o sistema de contadores vazio, que será populado
     * automaticamente conforme entidades são encontradas.
     * </p>
     */
    public EstatisticasCampo() {
        contadores = new HashMap<Class<?>, Contador>();
        contagensValidas = true;
    }

    // ========== MÉTODOS DE CONSULTA ==========

    /**
     * Gera uma descrição textual completa das populações no campo.
     * <p>
     * Formato da saída: {@code "Raposa: 15 Coelho: 42 Rato: 38 "}
     * </p>
     * <p>
     * Se as contagens estiverem desatualizadas, recalcula automaticamente
     * antes de gerar o relatório.
     * </p>
     * 
     * @param grade Interface de acesso ao campo para recálculo se necessário
     * @return String com todas as populações e suas contagens
     */
    public String getDetalhesPopulacao(GradeVisualizavel grade) {
        StringBuffer buffer = new StringBuffer();
        if (!contagensValidas) {
            gerarContagens(grade);
        }
        Iterator<Class<?>> chaves = contadores.keySet().iterator();
        while (chaves.hasNext()) {
            Contador info = contadores.get(chaves.next());
            buffer.append(info.getNome());
            buffer.append(": ");
            buffer.append(info.getContagem());
            buffer.append(' ');
        }
        return buffer.toString();
    }

    /**
     * Retorna a contagem atual de uma classe específica de entidade.
     * <p>
     * Utilizado principalmente pela visualização para gerar legendas dinâmicas
     * mostrando a população de cada espécie.
     * </p>
     * <p>
     * <strong>Nota:</strong> Se a classe nunca foi contada, retorna 0
     * ao invés de lançar exceção.
     * </p>
     * 
     * @param classeAnimal Classe da entidade a ser consultada (ex: Raposa.class)
     * @return Número de entidades dessa classe, ou 0 se não encontrada
     */
    public int getContagem(Class<?> classeAnimal) {
        Contador cnt = contadores.get(classeAnimal);
        if (cnt != null) {
            return cnt.getContagem();
        } else {
            return 0;
        }
    }

    /**
     * Determina se a simulação ainda é viável para continuar.
     * <p>
     * <strong>Critério de viabilidade:</strong><br>
     * A simulação é considerada viável se houver pelo menos duas espécies
     * diferentes com população maior que zero. Isto garante que há
     * interação ecológica mínima (predador-presa ou competição).
     * </p>
     * <p>
     * <strong>Cenários de inviabilidade:</strong>
     * <ul>
     * <li>Apenas uma espécie sobreviveu (sem dinâmica)</li>
     * <li>Nenhuma espécie sobreviveu (extinção total)</li>
     * </ul>
     * </p>
     * 
     * @param grade Interface de acesso ao campo para recálculo se necessário
     * @return true se há mais de uma espécie viva, false caso contrário
     */
    public boolean ehViavel(GradeVisualizavel grade) {
        int naoZero = 0;
        if (!contagensValidas) {
            gerarContagens(grade);
        }
        Iterator<Class<?>> chaves = contadores.keySet().iterator();
        while (chaves.hasNext()) {
            Contador info = contadores.get(chaves.next());
            if (info.getContagem() > 0) {
                naoZero++;
            }
        }
        return naoZero > 1;
    }

    // ========== MÉTODOS DE ATUALIZAÇÃO ==========

    /**
     * Invalida o cache de estatísticas e reseta todas as contagens para zero.
     * <p>
     * Deve ser chamado no início de cada passo de simulação antes de
     * realizar nova contagem. Não remove os contadores, apenas zera seus valores.
     * </p>
     */
    public void reiniciar() {
        contagensValidas = false;
        Iterator<Class<?>> chaves = contadores.keySet().iterator();
        while (chaves.hasNext()) {
            Contador cnt = contadores.get(chaves.next());
            cnt.reiniciar();
        }
    }

    /**
     * Incrementa a contagem de uma classe específica de entidade.
     * <p>
     * Se a classe ainda não tiver um contador, cria automaticamente
     * um novo usando o nome simples da classe.
     * </p>
     * <p>
     * <strong>Uso típico:</strong>
     * 
     * <pre>
     * for (Animal a : animais) {
     *     estatisticas.incrementarContagem(a.getClass());
     * }
     * </pre>
     * </p>
     * 
     * @param classeAnimal Classe da entidade a ter sua contagem incrementada
     */
    public void incrementarContagem(Class<?> classeAnimal) {
        Contador cnt = contadores.get(classeAnimal);
        if (cnt == null) {
            cnt = new Contador(classeAnimal.getSimpleName());
            contadores.put(classeAnimal, cnt);
        }
        cnt.incrementar();
    }

    /**
     * Marca o processo de contagem como concluído, validando o cache.
     * <p>
     * Deve ser chamado após todas as entidades terem sido contadas
     * via {@link #incrementarContagem(Class)}. Isto permite que consultas
     * subsequentes usem os valores em cache sem recálculo.
     * </p>
     */
    public void contagemConcluida() {
        contagensValidas = true;
    }

    // ========== MÉTODOS PRIVADOS ==========

    /**
     * Gera contagens percorrendo todo o campo e contando cada entidade.
     * <p>
     * Este método é chamado automaticamente quando:
     * <ul>
     * <li>Uma consulta é feita e o cache está inválido</li>
     * <li>É necessário recalcular devido a mudanças no campo</li>
     * </ul>
     * </p>
     * <p>
     * <strong>Processo:</strong>
     * <ol>
     * <li>Reseta todos os contadores</li>
     * <li>Percorre cada célula do campo</li>
     * <li>Incrementa contador para cada objeto encontrado</li>
     * <li>Marca cache como válido</li>
     * </ol>
     * </p>
     * 
     * @param grade Interface de acesso aos dados do campo
     */
    private void gerarContagens(GradeVisualizavel grade) {
        reiniciar();
        for (int linha = 0; linha < grade.getProfundidade(); linha++) {
            for (int coluna = 0; coluna < grade.getLargura(); coluna++) {
                Object animal = grade.getObjetoEm(linha, coluna);
                if (animal != null) {
                    incrementarContagem(animal.getClass());
                }
            }
        }
        contagensValidas = true;
    }
}
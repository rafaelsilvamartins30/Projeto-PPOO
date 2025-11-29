import java.util.HashMap;
import java.util.Iterator;

/**
 * Esta classe coleta e fornece dados estatísticos sobre o estado
 * de um campo. Ela é flexível: cria e mantém um contador
 * para qualquer classe de objeto encontrada dentro do campo.
 * 
 * @author David J. Barnes e Michael Kolling
 * @version 2002-04-23 (traduzido)
 */
public class EstatisticasCampo
{
    private HashMap<Class<?>, Contador> contadores;
    private boolean contagensValidas;

    /**
     * Constrói um objeto de estatísticas do campo.
     */
    public EstatisticasCampo()
    {
        contadores = new HashMap<Class<?>, Contador>();
        contagensValidas = true;
    }

    /**
     * @return Uma string descrevendo quais animais estão no campo.
     */
    public String getDetalhesPopulacao(Campo campo)
    {
        StringBuffer buffer = new StringBuffer();
        if(!contagensValidas) {
            gerarContagens(campo);
        }
        Iterator<Class<?>> chaves = contadores.keySet().iterator();
        while(chaves.hasNext()) {
            Contador info = (Contador) contadores.get(chaves.next());
            buffer.append(info.getNome());
            buffer.append(": ");
            buffer.append(info.getContagem());
            buffer.append(' ');
        }
        return buffer.toString();
    }
    
    /**
     * Invalida o conjunto atual de estatísticas; reinicia todas as contagens para zero.
     */
    public void reiniciar()
    {
        contagensValidas = false;
        Iterator<Class<?>> chaves = contadores.keySet().iterator();
        while(chaves.hasNext()) {
            Contador cnt = (Contador) contadores.get(chaves.next());
            cnt.reiniciar();
        }
    }

    /**
     * Incrementa a contagem de uma classe de animal.
     * @param classeAnimal A classe do animal a contar.
     */
    public void incrementarContagem(Class<?> classeAnimal)
    {
        Contador cnt = (Contador) contadores.get(classeAnimal);
        if(cnt == null) {
            cnt = new Contador(classeAnimal.getName());
            contadores.put(classeAnimal, cnt);
        }
        cnt.incrementar();
    }

    /**
     * Indica que a contagem de animais foi concluída.
     */
    public void contagemConcluida()
    {
        contagensValidas = true;
    }

    /**
     * Determina se a simulação ainda é viável.
     * Ou seja, se deve continuar a ser executada.
     * @return Verdadeiro se houver mais de uma espécie viva.
     */
    public boolean ehViavel(Campo campo)
    {
        int naoZero = 0;
        if(!contagensValidas) {
            gerarContagens(campo);
        }
        Iterator<Class<?>> chaves = contadores.keySet().iterator();
        while(chaves.hasNext()) {
            Contador info = (Contador) contadores.get(chaves.next());
            if(info.getContagem() > 0) {
                naoZero++;
            }
        }
        return naoZero > 1;
    }
    
    /**
     * Gera contagens do número de animais.
     * Essas contagens não são mantidas atualizadas conforme os animais
     * são colocados no campo, mas apenas quando há uma solicitação
     * por essas informações.
     */
    private void gerarContagens(Campo campo)
    {
        reiniciar();
        for(int linha = 0; linha < campo.getProfundidade(); linha++) {
            for(int coluna = 0; coluna < campo.getLargura(); coluna++) {
                Object animal = campo.getObjetoEm(linha, coluna);
                if(animal != null) {
                    incrementarContagem(animal.getClass());
                }
            }
        }
        contagensValidas = true;
    }
}

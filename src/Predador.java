import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Representa um predador na simulação de predador e presa.
 * 
 * Esta é uma classe abstrata que define os comportamentos e 
 * características comuns a todos os predadores do modelo, como caça.
 * As subclasses: por exemplo, {@link Raposa} e {@link Urso},
 * implementam comportamentos específicos, como reprodução, movimento 
 * e morte.
 * 
 * @author Projeto PPOO Grupo 10
 * @version 2024-06
 */
public abstract class Predador extends Animal {

    // Mapa de dieta específico para predadores
    protected Map<Class<?>, Integer> dieta;

    /**
     * Cria um predador.
     * @param idadeAleatoria Se verdadeiro, o predador terá uma idade aleatória.
     */
    public Predador(boolean idadeAleatoria) {
        super(idadeAleatoria);
        dieta = new HashMap<>();
    }

    /**
     * O predador age: caça, se move, reproduz-se e envelhece.
     * @param campoAtual O campo atual.
     * @param campoAtualizado O campo atualizado.
     * @param novosPredadores A lista para adicionar novos predadores nascidos.
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

 /**
     * O fluxo de movimento do predador:
     * 1. Tenta achar uma presa.
     * 2. Se achou, move para o local da presa.
     * 3. Se NÃO achou, move livremente (como qualquer animal).
     */
    private void processarCacaMovimento(CampoInterativo campoAtual, CampoInterativo campoAtualizado) {
        Localizacao locPresa = cacar(campoAtual, getLocalizacao());
        
        if (locPresa != null) {
            // Movimento direcionado (Ataque)
            moverPara(locPresa, campoAtualizado);
        } else {
            // Movimento genérico (Vagar) herdado de Animal
            tentarMoverLivremente(campoAtualizado); 
        }
    }

    /**
     * Apenas identifica ONDE está a comida e se alimenta.
     * NÃO move o predador.
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
                    return onde; // Retorna onde a presa estava
                }
            }
        }
        return null;
    }

    /**
     * Alimenta o predador com a presa capturada.
     * @param presa A presa que foi capturada e comida.
     */
    private void come(Animal presa) {
        presa.morrer();
        int calorias = dieta.get(presa.getClass());
        setNivelAlimento(calorias);
    }

    /**
     * Cria um novo filhote do predador.
     * @return Um novo predador.
     */
    public abstract Predador criarFilho();
}
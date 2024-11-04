import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TarefaManager {
    private List<Tarefa> tarefas = new ArrayList<>();
    private static final String ARQUIVO_DADOS = "tarefas.dat";

    public TarefaManager() {
        carregarTarefas();
    }

    public void adicionarTarefa(Tarefa tarefa) {
        tarefas.add(tarefa);
        salvarTarefas();
    }

    public void editarTarefa(int id, String nome, String descricao, String prioridade, String prazo, String status) {
        Tarefa tarefa = obterTarefaPorId(id);
        if (tarefa != null) {
            tarefa.setNome(nome);
            tarefa.setDescricao(descricao);
            tarefa.setPrioridade(prioridade);
            tarefa.setPrazo(prazo);
            tarefa.setStatus(status);
            salvarTarefas();
        }
    }

    public void removerTarefa(int id) {
        tarefas.removeIf(t -> t.getId() == id);
        salvarTarefas();
    }

    public Tarefa obterTarefaPorId(int id) {
        return tarefas.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    public List<Tarefa> filtrarTarefas(String statusFiltro) {
        return tarefas.stream()
                .filter(t -> t.getStatus().equalsIgnoreCase(statusFiltro))
                .collect(Collectors.toList());
    }

    public List<Tarefa> listarTarefas() {
        return tarefas;
    }

    private void salvarTarefas() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO_DADOS))) {
            oos.writeObject(tarefas);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void carregarTarefas() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARQUIVO_DADOS))) {
            tarefas = (List<Tarefa>) ois.readObject();
        } catch (FileNotFoundException e) {
            // Arquivo ainda não existe, nada a carregar
            tarefas = new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

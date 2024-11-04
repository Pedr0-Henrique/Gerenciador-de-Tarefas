import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class TarefaApp extends JFrame {
    private TarefaManager tarefaManager = new TarefaManager();
    private JTextField txtNome, txtDescricao, txtPrazo;
    private JComboBox<String> cmbPrioridade, cmbStatus;
    private JTable tabelaTarefas;
    private DefaultTableModel modeloTabela;
    private JButton btnAdicionar, btnEditar, btnRemover;
    private boolean editando = false; // Indica se estamos editando uma tarefa
    private int idEditando = -1; // Armazena o ID da tarefa que está sendo editada

    public TarefaApp() {
        setTitle("Gerenciador de Tarefas");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        inicializarComponentes();
        inicializarEventos();
    }

    private void inicializarComponentes() {
        txtNome = new JTextField(15);
        txtDescricao = new JTextField(15);
        txtPrazo = new JTextField(10);

        String[] prioridades = {"Alta", "Média", "Baixa"};
        cmbPrioridade = new JComboBox<>(prioridades);

        // Adicionando opções de status
        String[] statusOptions = {"A Fazer", "Em Andamento", "Concluída"};
        cmbStatus = new JComboBox<>(statusOptions);

        btnAdicionar = new JButton("Adicionar");
        btnEditar = new JButton("Editar");
        btnRemover = new JButton("Remover");

        JPanel painelFormulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        painelFormulario.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        painelFormulario.add(txtNome, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        painelFormulario.add(new JLabel("Descrição:"), gbc);
        gbc.gridx = 1;
        painelFormulario.add(txtDescricao, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        painelFormulario.add(new JLabel("Prioridade:"), gbc);
        gbc.gridx = 1;
        painelFormulario.add(cmbPrioridade, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        painelFormulario.add(new JLabel("Prazo:"), gbc);
        gbc.gridx = 1;
        painelFormulario.add(txtPrazo, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        painelFormulario.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        painelFormulario.add(cmbStatus, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel panelButtons = new JPanel(new FlowLayout());
        panelButtons.add(btnAdicionar);
        panelButtons.add(btnEditar);
        panelButtons.add(btnRemover);
        painelFormulario.add(panelButtons, gbc);

        add(painelFormulario, BorderLayout.NORTH);

        modeloTabela = new DefaultTableModel(new String[]{"ID", "Nome", "Descrição", "Prioridade", "Prazo", "Status"}, 0);
        tabelaTarefas = new JTable(modeloTabela);
        JScrollPane scrollTabela = new JScrollPane(tabelaTarefas);
        add(scrollTabela, BorderLayout.CENTER);
    }

    private void inicializarEventos() {
        btnAdicionar.addActionListener(e -> adicionarTarefa());
        btnEditar.addActionListener(e -> prepararEdicao());
        btnRemover.addActionListener(e -> removerTarefa());
    }

    private void adicionarTarefa() {
        if (editando) {
            salvarEdicao();
        } else {
            int id = tarefaManager.listarTarefas().size() + 1;
            String nome = txtNome.getText();
            String descricao = txtDescricao.getText();
            String prioridade = (String) cmbPrioridade.getSelectedItem();
            String prazo = txtPrazo.getText();
            String status = (String) cmbStatus.getSelectedItem();

            if (nome.isEmpty() || descricao.isEmpty() || prazo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Tarefa novaTarefa = new Tarefa(id, nome, descricao, prioridade, prazo, status);
            tarefaManager.adicionarTarefa(novaTarefa);
            atualizarTabela(tarefaManager.listarTarefas());
            limparCampos();
        }
    }

    private void prepararEdicao() {
        int linhaSelecionada = tabelaTarefas.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma tarefa para editar.", "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }

        idEditando = (int) modeloTabela.getValueAt(linhaSelecionada, 0);
        Tarefa tarefa = tarefaManager.obterTarefaPorId(idEditando);

        if (tarefa != null) {
            txtNome.setText(tarefa.getNome());
            txtDescricao.setText(tarefa.getDescricao());
            cmbPrioridade.setSelectedItem(tarefa.getPrioridade());
            txtPrazo.setText(tarefa.getPrazo());
            cmbStatus.setSelectedItem(tarefa.getStatus());

            btnAdicionar.setText("Salvar");
            editando = true;
        }
    }

    private void salvarEdicao() {
        String nome = txtNome.getText();
        String descricao = txtDescricao.getText();
        String prioridade = (String) cmbPrioridade.getSelectedItem();
        String prazo = txtPrazo.getText();
        String status = (String) cmbStatus.getSelectedItem();

        if (nome.isEmpty() || descricao.isEmpty() || prazo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        tarefaManager.editarTarefa(idEditando, nome, descricao, prioridade, prazo, status);
        atualizarTabela(tarefaManager.listarTarefas());
        limparCampos();
        btnAdicionar.setText("Adicionar");
        editando = false;
        idEditando = -1;
    }

    private void removerTarefa() {
        int linhaSelecionada = tabelaTarefas.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma tarefa para remover.", "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) modeloTabela.getValueAt(linhaSelecionada, 0);
        tarefaManager.removerTarefa(id);
        atualizarTabela(tarefaManager.listarTarefas());
        limparCampos();
    }

    private void atualizarTabela(List<Tarefa> tarefas) {
        modeloTabela.setRowCount(0);
        for (Tarefa tarefa : tarefas) {
            modeloTabela.addRow(new Object[]{
                    tarefa.getId(), tarefa.getNome(), tarefa.getDescricao(),
                    tarefa.getPrioridade(), tarefa.getPrazo(), tarefa.getStatus()
            });
        }
    }

    private void limparCampos() {
        txtNome.setText("");
        txtDescricao.setText("");
        txtPrazo.setText("");
        cmbPrioridade.setSelectedIndex(0);
        cmbStatus.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TarefaApp app = new TarefaApp();
            app.setVisible(true);
        });
    }
}

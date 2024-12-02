import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

class Funcionario {
    private int codigo;
    private String nome;
    private String cargo;
    private double salario;
    private List<Dependente> dependentes;

    public Funcionario(int codigo, String nome, String cargo, double salario) {
        this.codigo = codigo;
        this.nome = nome;
        this.cargo = cargo;
        this.salario = salario;
        this.dependentes = new ArrayList<>();
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public List<Dependente> getDependentes() {
        return dependentes;
    }

    public void adicionarDependente(Dependente dependente) {
        this.dependentes.add(dependente);
    }

    public double calcularBonus() {
        return salario * 0.02 * dependentes.size();
    }
}

class Dependente {
    private Funcionario funcionario;
    private String nome;

    public Dependente(Funcionario funcionario, String nome) {
        this.funcionario = funcionario;
        this.nome = nome;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}

public class AplicativoCadastro {
    private static List<Funcionario> funcionarios = new ArrayList<>();

    public static void main(String[] args) {
        while (true) {
            String[] opcoes = {"Cadastrar funcionário", "Mostrar bônus mensal de cada funcionário",
                    "Excluir funcionário", "Alterar salário de funcionário", "Sair"};
            int opcao = JOptionPane.showOptionDialog(null, "Escolha uma opção:", "Menu",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opcoes, opcoes[0]);

            switch (opcao) {
                case 0:
                    cadastrarFuncionario();
                    break;
                case 1:
                    mostrarBonusMensal();
                    break;
                case 2:
                    excluirFuncionario();
                    break;
                case 3:
                    alterarSalarioFuncionario();
                    break;
                case 4:
                    salvarDadosEmArquivo();
                    JOptionPane.showMessageDialog(null, "Saindo...");
                    return;
                default:
                    JOptionPane.showMessageDialog(null, "Opção inválida!");
            }

        }
    }

    private static void salvarDadosEmArquivo() {
        try (FileWriter writer = new FileWriter("bonus_funcionarios.txt")) { // CRIA O ARQUIVO TXT COM NOME, CARGO, SALARIO, DEPENDENTES E BONUS
            for (Funcionario funcionario : funcionarios) {
                double bonus = funcionario.calcularBonus();
                writer.write("Nome: " + funcionario.getNome() +
                        ", Cargo: " + funcionario.getCargo() +
                        ", Salário: R$ " + funcionario.getSalario() +
                        ", Dependentes: " + funcionario.getDependentes().size() +
                        ", Bônus: R$ " + bonus + "\n");
            }
            JOptionPane.showMessageDialog(null, "Dados salvos no arquivo bonus_funcionarios.txt com sucesso!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao salvar dados no arquivo: " + e.getMessage());
        }
    }
    private static void cadastrarFuncionario() {
        try {
            int codigo = Integer.parseInt(JOptionPane.showInputDialog("Número do funcionário (código):"));

            for (Funcionario func : funcionarios) {
                if (func.getCodigo() == codigo) {
                    JOptionPane.showMessageDialog(null, "Código já existente. Use um código único.");
                    return;
                }
            }

            String nome = JOptionPane.showInputDialog("Nome do funcionário:");
            String cargo = JOptionPane.showInputDialog("Cargo do funcionário:");
            double salario = Double.parseDouble(JOptionPane.showInputDialog("Salário do funcionário:"));

            Funcionario funcionario = new Funcionario(codigo, nome, cargo, salario);
            funcionarios.add(funcionario);

            int quantidadeDependentes = Integer.parseInt(JOptionPane.showInputDialog("Quantos dependentes deseja cadastrar para " + nome + "?"));

            for (int i = 0; i < quantidadeDependentes; i++) {
                String nomeDependente = JOptionPane.showInputDialog("Nome do dependente " + (i + 1) + ":");
                Dependente dependente = new Dependente(funcionario, nomeDependente);
                funcionario.adicionarDependente(dependente);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Entrada inválida. Tente novamente.");
        }
    }

    private static void mostrarBonusMensal() {
        StringBuilder mensagem = new StringBuilder();
        for (Funcionario funcionario : funcionarios) {
            double bonus = funcionario.calcularBonus();
            mensagem.append("Nome: ").append(funcionario.getNome()).append(", Dependentes: ").append(funcionario.getDependentes().size())
                    .append(", Bônus: R$ ").append(bonus).append("\n");
        }
        JOptionPane.showMessageDialog(null, mensagem.toString());
    }


    private static void excluirFuncionario() {
        try {
            int codigo = Integer.parseInt(JOptionPane.showInputDialog("Informe o código do funcionário que deseja excluir:"));

            Funcionario funcionarioEncontrado = null;
            for (Funcionario func : funcionarios) {
                if (func.getCodigo() == codigo) {
                    funcionarioEncontrado = func;
                    break;
                }
            }

            if (funcionarioEncontrado != null) {
                funcionarios.remove(funcionarioEncontrado);
                JOptionPane.showMessageDialog(null, "Funcionário e seus dependentes excluídos com sucesso.");
            } else {
                JOptionPane.showMessageDialog(null, "Funcionário Inexistente."); // Se o código estiver incorreto
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Entrada inválida. Informe um código numérico válido.");
        }
    }

    private static void alterarSalarioFuncionario() {
        try {
            int codigo = Integer.parseInt(JOptionPane.showInputDialog("Informe o código do funcionário cujo salário deseja alterar:"));

            Funcionario funcionarioEncontrado = null;
            for (Funcionario func : funcionarios) {
                if (func.getCodigo() == codigo) {
                    funcionarioEncontrado = func;
                    break;
                }
            }

            if (funcionarioEncontrado != null) {
                double novoSalario = Double.parseDouble(JOptionPane.showInputDialog("Informe o novo salário para " + funcionarioEncontrado.getNome() + ":"));
                funcionarioEncontrado.setSalario(novoSalario); // Altera pelo setSalario
                JOptionPane.showMessageDialog(null, "Salário atualizado com sucesso.");
            } else {
                JOptionPane.showMessageDialog(null, "Funcionário Inexistente.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Entrada inválida. Informe valores numéricos válidos.");
        }
    }

}
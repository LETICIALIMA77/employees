package funimage.api.controller;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

class Pessoa {
    private String nome;
    private LocalDate dataNascimento;
    
    public Pessoa(String nome, LocalDate dataNascimento) {
        this.nome = nome;
        this.dataNascimento = dataNascimento;
    }
    
    public String getNome() {
        return nome;
    }
    
    public LocalDate getDataNascimento() {
        return dataNascimento;
    }
    
    public int getIdade() {
        return LocalDate.now().getYear() - dataNascimento.getYear();
    }
    
    public String formatarData(LocalDate data) {
        return data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}

class Funcionario extends Pessoa {
    private BigDecimal salario;
    private String funcao;
    
    public Funcionario(String nome, LocalDate dataNascimento, BigDecimal salario, String funcao) {
        super(nome, dataNascimento);
        this.salario = salario;
        this.funcao = funcao;
    }
    
    public BigDecimal getSalario() {
        return salario;
    }
    
    public String getFuncao() {
        return funcao;
    }
    
    public BigDecimal calcularNovoSalario() {
        return salario.multiply(BigDecimal.valueOf(1.1)); 
    }
    
    @Override
    public String toString() {
        return String.format("Nome: %s, Data de Nascimento: %s, Salário: R$ %,.2f, Função: %s",
            getNome(), formatarData(getDataNascimento()), salario, funcao);
    }
}

public class Principal {
    public static void main(String[] args) {
        List<Funcionario> funcionarios = new ArrayList<>();
        
        funcionarios.add(new Funcionario("João", LocalDate.of(1990, 5, 15), BigDecimal.valueOf(2500.00), "Gerente"));
        funcionarios.add(new Funcionario("Maria", LocalDate.of(1985, 8, 20), BigDecimal.valueOf(1800.00), "Analista"));
        funcionarios.add(new Funcionario("Pedro", LocalDate.of(1995, 10, 10), BigDecimal.valueOf(2000.00), "Desenvolvedor"));
        
        funcionarios.removeIf(funcionario -> funcionario.getNome().equals("João"));
        
        System.out.println("Funcionários:");
        funcionarios.forEach(System.out::println);
        
        funcionarios.forEach(funcionario -> funcionario.salario = funcionario.calcularNovoSalario());
        
        Map<String, List<Funcionario>> funcionariosPorFuncao = funcionarios.stream()
                .collect(Collectors.groupingBy(Funcionario::getFuncao));
        
        System.out.println("\nFuncionários agrupados por função:");
        funcionariosPorFuncao.forEach((funcao, lista) -> {
            System.out.println(funcao + ":");
            lista.forEach(System.out::println);
        });
        
        System.out.println("\nFuncionários que fazem aniversário em outubro e dezembro:");
        funcionarios.stream()
                .filter(funcionario -> funcionario.getDataNascimento().getMonthValue() == 10 || funcionario.getDataNascimento().getMonthValue() == 12)
                .forEach(System.out::println);
        
        System.out.println("\nFuncionário com a maior idade:");
        Funcionario funcionarioMaiorIdade = Collections.max(funcionarios, Comparator.comparingInt(Pessoa::getIdade));
        System.out.println("Nome: " + funcionarioMaiorIdade.getNome() + ", Idade: " + funcionarioMaiorIdade.getIdade());
        
        System.out.println("\nFuncionários em ordem alfabética:");
        funcionarios.stream()
                .sorted(Comparator.comparing(Funcionario::getNome))
                .forEach(System.out::println);
        
        BigDecimal totalSalarios = funcionarios.stream()
                .map(Funcionario::getSalario)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println("\nTotal dos salários dos funcionários: R$ " + totalSalarios);
        
        BigDecimal salarioMinimo = BigDecimal.valueOf(1212.00);
        System.out.println("\nQuantidade de salários mínimos ganhos por cada funcionário:");
        funcionarios.forEach(funcionario -> {
            BigDecimal salarioMinimoGanho = funcionario.getSalario().divide(salarioMinimo, 2, BigDecimal.ROUND_HALF_UP);
            System.out.println(funcionario.getNome() + ": " + salarioMinimoGanho);
        });
    }
}

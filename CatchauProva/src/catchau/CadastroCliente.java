package catchau;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class CadastroCliente {

    public int cont;

    public CadastroCliente() {

    }

    public void cadastroCliente(ArrayList<Cliente> clientes) {
        Scanner z = new Scanner(System.in);
        int idade;
        Cliente c;
        String cnh, validade,nome,cpf;
        System.out.println("Digite o nome do cliente: ");
        nome=z.nextLine();
        System.out.println("Digite o cpf do cliente: ");
        cpf=z.next();
        do {
            System.out.println("Digite sua idade: ");
            idade = z.nextInt();
            if (idade >= 21) {
                z.nextLine();
                do {
                    if (isCPF(cpf) == true) {
                        break;

                    } else {
                        System.out.println("Cpf inválido digite novamente.");
                    }
                } while (isCPF(cpf) == true);

                do {
                    System.out.println("Digite o numero da CNH");
                    cnh = z.next();
                    if (validaCNH(cnh) == true) {
                        break;
                    } else {
                        System.out.println("CNH inválida");
                    }
                } while (validaCNH(cnh) == true);
                System.out.println("Digite a validade da sua CNH: ");
                validade = z.next();
                c = new Cliente.ClienteBuilder().setNome(nome).setIdade(idade).setCpf(cpf).setCnh(cnh).setValidade(validade).criarCliente();
                clientes.add(c);
                cont++;
                System.out.println("Cadastro realizado com sucesso!");
                break;
            } else {
                System.out.println("Idade inválida");
                break;
            }

        } while (true);
    }

    public void remover(ArrayList<Cliente> clientes, String nome, String cpf) {

        if (clientes.isEmpty() == true) {
            System.out.println("Cadastro de clientes vázio");
        } else {
            for (int i = 0; i < clientes.size(); i++) {

                if (consultarCliente(clientes, nome, cpf) == true) {
                    clientes.remove(i);
                    System.out.println("Removido com sucesso!");
                }
            }

        }
    }

    public boolean consultarCliente(ArrayList<Cliente> clientes, String nome, String cpf) {
        int i = 0;
        try {
            do {
                if (nome == null && cpf == null) {
                    return false;
                } else if (clientes.get(i).getNome().isEmpty() == true && clientes.get(i).getCpf().isEmpty() == true) {
                    return false;
                } else if (clientes.get(i).getNome().contains(nome) == true && clientes.get(i).getCpf().contains(cpf) == true) {
                    return true;
                } else {
                    i++;
                }

            } while (i < clientes.size());
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
        return false;
    }//consultas

    public void exibir(String cpf, ArrayList<Cliente> clientes) {
        for (int i = 0; i < clientes.size(); i++) {
            if (clientes.get(i).getCpf().contains(cpf) == true) {
                System.out.println(clientes.get(i).exibir());
            }
        }
    }

    public static boolean isCPF(String CPF) {
        // considera-se erro CPF's formados por uma sequencia de numeros iguais
        if (CPF.equals("00000000000")
                || CPF.equals("11111111111")
                || CPF.equals("22222222222") || CPF.equals("33333333333")
                || CPF.equals("44444444444") || CPF.equals("55555555555")
                || CPF.equals("66666666666") || CPF.equals("77777777777")
                || CPF.equals("88888888888") || CPF.equals("99999999999")
                || (CPF.length() != 11)) {
            return (false);
        }

        char dig10, dig11;
        int sm, i, r, num, peso;

        // "try" - protege o codigo para eventuais erros de conversao de tipo (int)
        try {
            // Calculo do 1o. Digito Verificador
            sm = 0;
            peso = 10;
            for (i = 0; i < 9; i++) {
                // converte o i-esimo caractere do CPF em um numero:
                // por exemplo, transforma o caractere '0' no inteiro 0
                // (48 eh a posicao de '0' na tabela ASCII)
                num = (int) (CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11)) {
                dig10 = '0';
            } else {
                dig10 = (char) (r + 48); // converte no respectivo caractere numerico
            }
            // Calculo do 2o. Digito Verificador
            sm = 0;
            peso = 11;
            for (i = 0; i < 10; i++) {
                num = (int) (CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11)) {
                dig11 = '0';
            } else {
                dig11 = (char) (r + 48);
            }

            // Verifica se os digitos calculados conferem com os digitos informados.
            if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10))) {
                return (true);
            } else {
                return (false);
            }
        } catch (InputMismatchException erro) {
            return (false);
        }
    }

    public static boolean validaCNH(String cnh) {
        char char1 = cnh.charAt(0);
        if (cnh.replaceAll("\\D+", "").length() != 11
                || String.format("%0" + 11 + "d", 0).replace('0', char1).equals(cnh)) {
            return false;
        }
        long v = 0, j = 9;
        for (int i = 0; i < 9; ++i, --j) {
            v = v + ((cnh.charAt(i) - 48) * j);
        }
        long dsc = 0, vl1 = v % 11;
        if (vl1 >= 10) {
            vl1 = 0;
            dsc = 2;
        }
        v = 0;
        j = 1;
        for (int i = 0; i < 9; ++i, ++j) {
            v += ((cnh.charAt(i) - 48) * j);
        }
        long x = v % 11;
        long vl2 = (x >= 10) ? 0 : x - dsc;
        return (String.valueOf(vl1) + String.valueOf(vl2)).equals(cnh.substring(cnh.length() - 2));
    }

}

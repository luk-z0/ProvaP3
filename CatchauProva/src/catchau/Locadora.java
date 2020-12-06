package catchau;

import java.util.ArrayList;
import catchau.Carro.Modelo;
import catchau.Carro.Preco;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Locadora extends CadastroCliente {

    private int renovacao = 0;
    private static Locadora locadora;

    private Locadora() {

        super();
    }

    public static Locadora getInstance() {
        if (locadora == null) {
            locadora = new Locadora();
        }
        return locadora;
    }

    public void exibModelo(Modelo[] m) {
        m = Modelo.values();
        int i = 0;
        do {
            System.out.println("" + m[i].getModelo());
            i++;
        } while (i < m.length);
        System.out.println(" ");
    }

    public void exibPreco(Preco[] p) {
        p = Preco.values();
        int i = 0;
        while (i < p.length) {
            System.out.println(" " + p[i].getPreco());

        }
        System.out.println(" ");
    }

    public void exibModPre(Modelo[] m, Preco[] p) {

        m = Modelo.values();
        p = Preco.values();
        for (int i = 0; i < m.length; i++) {

            System.out.println("Modelo do Carro : " + m[i].getModelo() + "\n Preço do Carro: R$ " + p[i].getPreco());
            System.out.println(" ");
        }

    }

    public void alugarCarro(Modelo[] m, Preco[] p, ArrayList<Cliente> clientes, String nome, String cpf) {
        Scanner z = new Scanner(System.in);
        String model;
        double preco;
        int j = 0, y = 0, i = 0;
        List<Modelo> modeloEnum = Arrays.asList(m);
        List<Preco> precoEnum = Arrays.asList(p);
        for (; y < clientes.size();) {
            if (consultarCliente(clientes, nome, cpf) == true) {
                System.out.println("");
                System.out.println("DIGITE O MODELO DO CARRO");
                model = z.nextLine();
                for (; i < modeloEnum.size(); i++) {
                    if (consultarCarro(modeloEnum, model) == true) {
                        System.out.println("MODELO ENCONTRADO");
                        System.out.println();
                        System.out.println("DIGITE O PREÇO DO ALUGUEL");
                        preco = z.nextDouble();
                        if (consultarPreco(precoEnum, modeloEnum, preco, model) == true) {
                            int k;
                            double restoPreco = restoPreco(precoEnum, modeloEnum, preco, model);
                            do {
                                if (restoPreco > 0) {
                                    System.out.println("Deseja depositar o dinheiro na conta ? ");
                                    System.out.println("Digite 1");
                                    System.out.println("Se NÃO deseja depositar o dinheiro ");
                                    System.out.println("Digite 2");
                                    k = z.nextInt();

                                } else {
                                    System.out.println("O valor da sua carteira digital é : " + clientes.get(y).getCarteira());
                                    break;
                                }
                                switch (k) {
                                    case 1:
                                        depositar(clientes, 0, nome, cpf);
                                        break;

                                    case 2:
                                        System.out.println("O valor da sua carteira digital é : " + clientes.get(y).getCarteira());
                                        break;
                                    default:
                                        System.out.println("Digite uma opção válida");
                                }
                            } while (k != 2);

                            clientes.get(y).setCarroAlugado(modeloEnum.get(i).getModelo());
                            clientes.get(y).setDeposito((float) preco);
                            System.out.println("Carro alugado");
                            break;
                        } else {
                            j++;
                            if (j < precoEnum.size()) {
                                System.out.println("Preço incorreto");
                            }
                        }
                    } else {
                        if (i < modeloEnum.size()) {
                            System.out.println("Carro não encontrado");
                        }
                        i++;
                    }

                }
            } else {
                if (y < clientes.size()) {
                    System.out.println("Cliente não encontrado");
                }
                y++;
            }
            break;
        }
    }

    public double restoPreco(List<Preco> pEnum, List<Modelo> mEnum, double preco, String model) {
        int i = 0;
        do {
            if (preco > pEnum.get(i).getPreco()) {
                if (consultarPreco(pEnum, mEnum, preco, model) == true) {
                    return preco - pEnum.get(i).getPreco();
                }
            }

        } while (i < pEnum.size());
        return 0;
    }

    public boolean consultarPreco(List<Preco> pEnum, List<Modelo> mEnum, double preco, String model) {
        int i = 0;
        String[] aux;
        do {
            aux = mEnum.get(i).getModelo().split(" - ");
            aux[0] = aux[0].toLowerCase();

            if (preco == 0) {
                return false;
            } else if (preco >= pEnum.get(i).getPreco() && aux[0].contains(model) == true) {
                return true;
            } else {
                i++;
            }

        } while (i < pEnum.size());
        return false;
    }

    public boolean consultarCarro(List<Modelo> mEnum, String modelo) {
        String[] aux;
        int i = 0;
        do {
            aux = mEnum.get(i).getModelo().split(" - ");
            aux[0] = aux[0].toLowerCase();
            if (modelo == null) {
                return false;
            } else if (aux[0].contains(modelo) == true) {
                return true;
            } else {
                i++;
            }

        } while (i < mEnum.size());
        return false;

    }

    public void renovarCar(ArrayList<Cliente> clientes, String nome, String cpf) {
        Scanner z = new Scanner(System.in);
        for (int i = 0; i < clientes.size(); i++) {
            if (consultarCliente(clientes, nome, cpf) == true) {
                if (clientes.get(i).getCpf().contains(cpf) == true) {
                    if (clientes.get(i).getRenovação() >= 3) {
                        System.out.println("O maximo de renovação foi atingida");
                        break;
                    } else {
                        //System.out.println("A taxa de renovação e equivalente a 15% do valor do carro");
                        renovacao++;
                        clientes.get(i).setRenovacao(renovacao);
                        renovacao = 0;
                        //fazer os 15% do valor do carro
                        System.out.println("O carro alugado foi: " + clientes.get(i).getCarroAlugado());
                        System.out.println("O carro foi renovado.");
                        break;
                    }
                }

            }
            break;
        }
    }

    public void disconto(Modelo[] m, Preco[] p, Promocao.Cupom cm, String nome, String cpf, ArrayList<Cliente> clientes, Locadora locadora) {
        Scanner z = new Scanner(System.in);
        double precSDisc, precDis;
        String carro;
        List<Modelo> modeloEnum = Arrays.asList(m);
        List<Preco> precoEnum = Arrays.asList(p);

        if (consultarCliente(clientes, nome, cpf) == true) {
            System.out.println("Digite o cupom: ");
            String cupom = z.next();
            for (int i = 0; i < clientes.size(); i++) {
                if (cm.getCupom().contains(cupom) == true) {
                    locadora.exibModPre(m, p);
                    System.out.println("Digite o modelo do carro: ");
                    carro = z.next();
                    do {

                        if (consultarCarro(modeloEnum, carro) == true) {
                            System.out.println(" O Preço do carro sem desconto: " + precoEnum.get(i).getPreco());
                            precSDisc = p[i].getPreco();
                            precDis = precSDisc - (precSDisc * 0.10);
                            System.out.println("O Preço do carro com disconto: " + precDis);
                            i++;
                            break;
                        } else {
                            System.out.println("Carro inválido");
                            break;
                        }
                    } while (i < m.length);
                } else {
                    System.out.println("Cupom inválido! ");
                    break;
                }
            }
        } else {
            System.out.println("Cliente não encontrado.");
        }
    }

    public void depositar(ArrayList<Cliente> clientes, double valorDeposito, String nome, String cpf) {
        for (int i = 0; i < clientes.size();) {
            if (consultarCliente(clientes, nome, cpf) == true) {
                System.out.println("O valor da sua carteira é: " + clientes.get(i).getCarteira());
                clientes.get(i).setCarteira((float) valorDeposito);
                clientes.get(i).setDeposito((float) valorDeposito);
                System.out.println("Valor depositado com sucesso");
                break;
            } else {
                i++;
            }

        }

    }

    public void menu() {

        String case1, case2, case3, case4, case5, case6, case7, case8, case0;
        case1 = " 1 - CONSULTAR A LISTA DE VEICULOS E PREÇOS";
        case2 = " 2 - ALUGAR UM VEICULO ";
        case3 = " 3 - Devolver veículo";
        case4 = " 4 - Renovar locação ";
        case5 = " 5 - Apagar cadastro ";
        case6 = " 6 - Usar cupom de desconto";
        case7 = " 7 - Realizar deposito";
        case8 = " 8 - Exibir informações do cliente";
        case0 = " 0 - logout ";
        System.out.println(case1 + "\n" + case2 + "\n" + case3 + "\n" + case4 + "\n" + case5 + "\n" + case6 + "\n" + case7 + "\n" + case8 + "\n" + case0);
    }

    public void login() {
        System.out.println("1 - Fazer login ");
        System.out.println("2 - Realizar cadastro");
        System.out.println("0 - Encerrar programa");
    }

    public void devolverVeiculo(ArrayList<Cliente> clientes, String nome, String cpf) {

        if (consultarCliente(clientes, nome, cpf) == true) {
            for (int i = 0; i < clientes.size();) {
                if (clientes.get(i).getNome().contains(nome) == true) {

                    System.out.println("O carro alugado foi: " + clientes.get(i).getCarroAlugado());
                    clientes.get(i).setCarroAlugado(null);
                    System.out.println("O carro foi devolvido com sucesso!");
                    break;
                } else {
                    i++;
                }
            }
        } else {
            System.out.println("Cliente não identificado.");

        }

    }

    

}

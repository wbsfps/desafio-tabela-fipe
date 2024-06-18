package br.com.tabelafipe.fipe.main;

import br.com.tabelafipe.fipe.model.Data;
import br.com.tabelafipe.fipe.model.Models;
import br.com.tabelafipe.fipe.model.Vehicle;
import br.com.tabelafipe.fipe.service.ConsumingAPI;
import br.com.tabelafipe.fipe.service.ConvertingData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    private Scanner sc = new Scanner(System.in);
    private ConsumingAPI consume = new ConsumingAPI();
    private ConvertingData converter = new ConvertingData();
    private final String URL_BASE = "https://parallelum.com.br/fipe/api/v1/";

    public void showMenu() {
        System.out.printf("Opções: \n %s \n %s \n %s \n", "carros", "motos", "caminhões");
        System.out.println("Qual a sua escolha? ");
        var option = sc.nextLine();
        String address;


        if (option.toLowerCase().contains("carr")) {
            address = URL_BASE + "carros/marcas";
        } else if (option.toLowerCase().contains("mot")) {
            address = URL_BASE + "motos/marcas";
        } else {
            address = URL_BASE + "caminhões/marcas";
        }

        var json = consume.getData(address);
        System.out.println(json);

        var brands = converter.getList(json, Data.class);
        brands.stream()
                .sorted(Comparator.comparing(Data::codigo))
                .forEach(System.out::println);

        System.out.println("Informe o código da marca para consulta: ");
        var brandCode = sc.nextLine();

        address = address + "/" + brandCode + "/modelos";
        json = consume.getData(address);
        var modelsList = converter.getData(json, Models.class);

        System.out.println("Modelos dessa marca: ");
        modelsList.modelos()
                .stream()
                .sorted(Comparator.comparing(Data::codigo))
                .forEach(System.out::println);

        System.out.println("\nDigite um trecho do nome do carro a ser buscado: ");
        var vehicleName = sc.nextLine();
        List<Data> modelsFilters = modelsList.modelos()
                .stream()
                .filter(m -> m.nome().toLowerCase().contains(vehicleName.toLowerCase()))
                .collect(Collectors.toList());
        System.out.println("\nModelos filtrados: ");
        modelsFilters.forEach(System.out::println);

        System.out.println("Digite o codigo do modelo: ");
        var modelCode = sc.nextLine();

        address = address + "/" + modelCode + "/anos";
        json = consume.getData(address);
        List<Data> years = converter.getList(json, Data.class);
        List<Vehicle> vehicles = new ArrayList<>();

        for (int i = 0; i < years.size(); i++) {
            var yearsAddress = address + "/" + years.get(i).codigo();
            json = consume.getData(yearsAddress);
            Vehicle vehicle = converter.getData(json, Vehicle.class);
            vehicles.add(vehicle);
        }

        System.out.println("Todos os veículos filtrados: ");
        vehicles.forEach(System.out::println);
    }
}

package com.example.programa_beneficios_v2;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class EmployeeManager {
    public static HashMap<String, String> User_Password = new HashMap<>();

    public static String user;
    public static String password;
    public static int electionInt = 0;
    public static Scanner sc = new Scanner(System.in);

    private static final String JSON_FILE_PATH = "empleados.json";

    public static void main(String[] args) {

        Menu();

    }

    public static void addEmployeeRecord() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Nombre del empleado: ");
        String name = scanner.nextLine();

        System.out.print("Puntos obtenidos: ");
        int points = Integer.parseInt(scanner.nextLine());

        System.out.print("Fecha de Ingreso: ");
        String fecha_ing = scanner.nextLine();

        String code = generateUniqueCode();

        // Crear un objeto JSON para el nuevo registro
        JSONObject employeeRecord = new JSONObject();
        employeeRecord.put("code", code);
        employeeRecord.put("nombre", name);
        employeeRecord.put("puntos", points);
        employeeRecord.put("fecha_ingreso", fecha_ing);

        // Leer registros existentes del archivo JSON
        JSONArray existingRecords = readExistingRecords();

        // Agregar el nuevo registro al arreglo existente
        existingRecords.add(employeeRecord);

        // Escribir el arreglo actualizado en el archivo JSON
        writeRecordsToFile(existingRecords);
    }

    private static String generateUniqueCode() {
        // Implementar la lógica para generar un código único (por ejemplo, usando la
        // fecha y hora actual)
        // Esto puede variar según tus requisitos
        long timestamp = System.currentTimeMillis();
        return "EMP-" + timestamp;
    }

    private static JSONArray readExistingRecords() {
        try {
            JSONParser parser = new JSONParser();

            // Intenta leer registros existentes del archivo JSON
            Object obj = parser.parse(new FileReader(JSON_FILE_PATH));

            if (obj instanceof JSONArray) {
                return (JSONArray) obj;
            }
        } catch (IOException | ParseException e) {
            // Si hay un error al leer el archivo o el archivo no existe, retorna un nuevo
            // JSONArray vacío
        }

        return new JSONArray();
    }

    private static void writeRecordsToFile(JSONArray records) {
        try (FileWriter file = new FileWriter(JSON_FILE_PATH)) {
            // Escribe el arreglo JSON actualizado en el archivo
            file.write(records.toJSONString());
            System.out.println("Registro de empleado almacenado con éxito.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String formatJsonArray(JSONArray jsonArray, int indent) {
        StringBuilder result = new StringBuilder();

        for (Object obj : jsonArray) {
            // Agregar sangría según el nivel de indentación
            for (int i = 0; i < indent; i++) {
                result.append("    ");
            }

            if (obj instanceof JSONObject) {
                // Si el elemento es un objeto JSON, llamar recursivamente para formatear
                result.append("{\n").append(formatJsonObject((JSONObject) obj, indent + 1));
                for (int i = 0; i < indent; i++) {
                    result.append("    ");
                }
                result.append("}");
            } else {
                // Si el elemento no es un objeto JSON, simplemente imprimir
                result.append(obj.toString());
            }

            // Agregar una coma si no es el último elemento
            if (jsonArray.indexOf(obj) < jsonArray.size() - 1) {
                result.append(",");
            }

            result.append("\n");
        }

        return result.toString();
    }

    private static String formatJsonObject(JSONObject jsonObject, int indent) {
        StringBuilder result = new StringBuilder();

        Iterator<String> keys = jsonObject.keySet().iterator();
        while (keys.hasNext()) {
            String key = keys.next();
            Object value = jsonObject.get(key);

            // Agregar sangría según el nivel de indentación
            for (int i = 0; i < indent; i++) {
                result.append("    ");
            }

            result.append("\"").append(key).append("\": ");

            if (value instanceof JSONObject) {
                // Si el valor es un objeto JSON, llamar recursivamente para formatear
                result.append("{\n").append(formatJsonObject((JSONObject) value, indent + 1));
                for (int i = 0; i < indent; i++) {
                    result.append("    ");
                }
                result.append("}");
            } else if (value instanceof JSONArray) {
                // Si el valor es un array JSON, llamar al método formatJsonArray
                result.append("[\n").append(formatJsonArray((JSONArray) value, indent + 1));
                for (int i = 0; i < indent; i++) {
                    result.append("    ");
                }
                result.append("]");
            } else {
                // Si el valor no es un objeto JSON, simplemente imprimir
                result.append("\"").append(value).append("\"");
            }

            // Agregar una coma si no es el último elemento
            if (keys.hasNext()) {
                result.append(",");
            }

            result.append("\n");
        }

        return result.toString();
    }

    public static void Menu() {
        boolean b = true;
        boolean b1 = true;

        // Cargar usuarios desde el archivo JSON
        List<Usuario> usuarios = AutenticacionJson.cargarUsuarios();

        // Iniciar sesión
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese nombre de usuario: ");
        String username = scanner.nextLine();
        System.out.print("Ingrese contraseña: ");
        String password = scanner.nextLine();

        do {
            if (AutenticacionJson.autenticar(username, password)) {
                while (b) {
                    System.out.println("Menu");
                    System.out.println("1. Ingresar nuevo Empleado");
                    System.out.println("2. Ver Empleados Existentes");
                    System.out.println("--------------------------");
                    System.out.println("0. Salir");
                    int response = sc.nextInt();
                    switch (response) {
                        case 1:
                            addEmployeeRecord();
                            break;
                        case 2:
                            JSONArray obj = readExistingRecords();
                            System.out.print(formatJsonArray(obj,0));
                        case 0:
                            b = false;
                            b1 = false;
                            break;
                    }
                }
            } else {
                System.out.println("Inicio de sesión fallido. Usuario o contraseña incorrectos.");
                b1 = false;
            }
        } while (b1 != false);

    }
}

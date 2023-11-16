package com.example.programa_beneficios_v2;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AutenticacionJson {

    private static final String ARCHIVO_JSON = "usuarios.json";

    @SuppressWarnings("unchecked")
    public static List<Usuario> cargarUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();

        try (FileReader reader = new FileReader(ARCHIVO_JSON)) {
            JSONParser parser = new JSONParser();
            JSONArray jsonArray = new JSONArray();
            jsonArray.add(parser.parse(reader));
            for (Object obj : jsonArray) {
                JSONObject jsonUsuario = (JSONObject) obj;
                Usuario usuario = new Usuario((String) jsonUsuario.get("username"), (String) jsonUsuario.get("password"));
                usuarios.add(usuario);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return usuarios;
    }

    @SuppressWarnings("unchecked")
    public static void guardarUsuarios(List<Usuario> usuarios) {
        JSONArray jsonArray = new JSONArray();
        for (Usuario usuario : usuarios) {
            JSONObject jsonUsuario = new JSONObject();
            jsonUsuario.put("username", usuario.getNombre());
            jsonUsuario.put("password", usuario.getClave());
            jsonArray.add(jsonUsuario);
        }

        try (FileWriter writer = new FileWriter(ARCHIVO_JSON)) {
            writer.write(jsonArray.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean autenticar(String username, String password) {
        List<Usuario> usuarios = cargarUsuarios();

        for (Usuario usuario : usuarios) {
            if (usuario.getNombre().equals(username) && usuario.getClave().equals(password)) {
                return true; // Autenticación exitosa
            }
        }

        return false; // Usuario no encontrado o contraseña incorrecta
    }
}
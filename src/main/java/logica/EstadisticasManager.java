package logica;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EstadisticasManager {
    private static final String ARCHIVO_ESTADISTICAS = "datos/estadisticas.json";
    private int partidasTotales;
    private int victoriasJugador;
    private int victoriasIA;
    private int empates;
    private String fechaPrimeraPartida;
    private String fechaUltimaPartida;
    private int mejorRachaJugador;
    private int mejorRachaIA;
    private int rachaActualJugador;
    private int rachaActualIA;
    
    public EstadisticasManager() {
        cargarEstadisticas();
    }
    
    public void registrarVictoriaJugador() {
        partidasTotales++;
        victoriasJugador++;
        rachaActualJugador++;
        rachaActualIA = 0;
        
        if (rachaActualJugador > mejorRachaJugador) {
            mejorRachaJugador = rachaActualJugador;
        }
        
        actualizarFechas();
        guardarEstadisticas();
    }
    
    public void registrarVictoriaIA() {
        partidasTotales++;
        victoriasIA++;
        rachaActualIA++;
        rachaActualJugador = 0;
        
        if (rachaActualIA > mejorRachaIA) {
            mejorRachaIA = rachaActualIA;
        }
        
        actualizarFechas();
        guardarEstadisticas();
    }
    
    public void registrarEmpate() {
        partidasTotales++;
        empates++;
        rachaActualJugador = 0;
        rachaActualIA = 0;
        
        actualizarFechas();
        guardarEstadisticas();
    }
    
    public void resetearEstadisticas() {
        partidasTotales = 0;
        victoriasJugador = 0;
        victoriasIA = 0;
        empates = 0;
        fechaPrimeraPartida = null;
        fechaUltimaPartida = null;
        mejorRachaJugador = 0;
        mejorRachaIA = 0;
        rachaActualJugador = 0;
        rachaActualIA = 0;
        guardarEstadisticas();
    }
    
    private void actualizarFechas() {
        String fechaActual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        if (fechaPrimeraPartida == null) {
            fechaPrimeraPartida = fechaActual;
        }
        fechaUltimaPartida = fechaActual;
    }
    
    private void cargarEstadisticas() {
        try {
            Path archivoPath = Paths.get(ARCHIVO_ESTADISTICAS);
            
            if (!Files.exists(archivoPath)) {
                Files.createDirectories(archivoPath.getParent());
                inicializarEstadisticasDefault();
                guardarEstadisticas();
                return;
            }
            
            String contenido = Files.readString(archivoPath);
            parseJson(contenido);
            
        } catch (IOException e) {
            System.err.println("Error al cargar estadísticas: " + e.getMessage());
            inicializarEstadisticasDefault();
        }
    }
    
    private void parseJson(String json) {
        try {
            partidasTotales = extraerValorEntero(json, "partidas_totales");
            victoriasJugador = extraerValorEntero(json, "victorias_jugador");
            victoriasIA = extraerValorEntero(json, "victorias_ia");
            empates = extraerValorEntero(json, "empates");
            mejorRachaJugador = extraerValorEntero(json, "mejor_racha_jugador");
            mejorRachaIA = extraerValorEntero(json, "mejor_racha_ia");
            fechaPrimeraPartida = extraerValorString(json, "fecha_primera_partida");
            fechaUltimaPartida = extraerValorString(json, "fecha_ultima_partida");
        } catch (Exception e) {
            System.err.println("Error al parsear JSON: " + e.getMessage());
            inicializarEstadisticasDefault();
        }
    }
    
    private int extraerValorEntero(String json, String clave) {
        try {
            String patron = "\"" + clave + "\"\\s*:\\s*(\\d+)";
            java.util.regex.Pattern p = java.util.regex.Pattern.compile(patron);
            java.util.regex.Matcher m = p.matcher(json);
            if (m.find()) {
                return Integer.parseInt(m.group(1));
            }
        } catch (Exception e) {
            System.err.println("Error extrayendo valor entero para " + clave + ": " + e.getMessage());
        }
        return 0;
    }
    
    private String extraerValorString(String json, String clave) {
        try {
            String patron = "\"" + clave + "\"\\s*:\\s*\"([^\"]+)\"";
            java.util.regex.Pattern p = java.util.regex.Pattern.compile(patron);
            java.util.regex.Matcher m = p.matcher(json);
            if (m.find()) {
                return m.group(1);
            }
        } catch (Exception e) {
            System.err.println("Error extrayendo valor string para " + clave + ": " + e.getMessage());
        }
        return null;
    }
    
    private void guardarEstadisticas() {
        try {
            Path archivoPath = Paths.get(ARCHIVO_ESTADISTICAS);
            Files.createDirectories(archivoPath.getParent());
            
            String json = generarJson();
            Files.writeString(archivoPath, json);
            
        } catch (IOException e) {
            System.err.println("Error al guardar estadísticas: " + e.getMessage());
        }
    }
    
    private String generarJson() {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"partidas_totales\": ").append(partidasTotales).append(",\n");
        json.append("  \"victorias_jugador\": ").append(victoriasJugador).append(",\n");
        json.append("  \"victorias_ia\": ").append(victoriasIA).append(",\n");
        json.append("  \"empates\": ").append(empates).append(",\n");
        
        if (fechaPrimeraPartida != null) {
            json.append("  \"fecha_primera_partida\": \"").append(fechaPrimeraPartida).append("\",\n");
        } else {
            json.append("  \"fecha_primera_partida\": null,\n");
        }
        
        if (fechaUltimaPartida != null) {
            json.append("  \"fecha_ultima_partida\": \"").append(fechaUltimaPartida).append("\",\n");
        } else {
            json.append("  \"fecha_ultima_partida\": null,\n");
        }
        
        json.append("  \"mejor_racha_jugador\": ").append(mejorRachaJugador).append(",\n");
        json.append("  \"mejor_racha_ia\": ").append(mejorRachaIA).append(",\n");
        json.append("  \"tiempo_promedio_partida\": 0,\n");
        json.append("  \"configuracion\": {\n");
        json.append("    \"jugador_simbolo\": \"X\",\n");
        json.append("    \"ia_simbolo\": \"O\",\n");
        json.append("    \"dificultad\": \"invencible\",\n");
        json.append("    \"sonidos_activados\": true,\n");
        json.append("    \"animaciones_activadas\": true\n");
        json.append("  }\n");
        json.append("}");
        
        return json.toString();
    }
    
    private void inicializarEstadisticasDefault() {
        partidasTotales = 0;
        victoriasJugador = 0;
        victoriasIA = 0;
        empates = 0;
        fechaPrimeraPartida = null;
        fechaUltimaPartida = null;
        mejorRachaJugador = 0;
        mejorRachaIA = 0;
        rachaActualJugador = 0;
        rachaActualIA = 0;
    }
    
    // Getters
    public int getPartidasTotales() { return partidasTotales; }
    public int getVictoriasJugador() { return victoriasJugador; }
    public int getVictoriasIA() { return victoriasIA; }
    public int getEmpates() { return empates; }
    public String getFechaPrimeraPartida() { return fechaPrimeraPartida; }
    public String getFechaUltimaPartida() { return fechaUltimaPartida; }
    public int getMejorRachaJugador() { return mejorRachaJugador; }
    public int getMejorRachaIA() { return mejorRachaIA; }
    public int getRachaActualJugador() { return rachaActualJugador; }
    public int getRachaActualIA() { return rachaActualIA; }
}

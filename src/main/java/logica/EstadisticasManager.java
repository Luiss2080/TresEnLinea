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
                recuperarConValoresPorDefecto("no existe un archivo de estadísticas previo");
                return;
            }

            String contenido = Files.readString(archivoPath);
            parseJson(contenido);

        } catch (IOException e) {
            // Archivo ilegible (binario corrupto, bytes invalidos, permisos, etc.)
            recuperarConValoresPorDefecto("no se pudo leer el archivo (" + e.getMessage() + ")");
        }
    }

    private void parseJson(String json) {
        try {
            if (!pareceUnArchivoDeEstadisticasValido(json)) {
                // extraerValorEntero/extraerValorString nunca lanzan excepcion: si una
                // clave no aparece, simplemente devuelven 0/null. Eso significa que un
                // texto basura o un JSON de un esquema totalmente distinto (formato
                // incompatible/antiguo) NO dispara el catch de abajo por si solo -
                // silenciosamente "cargaria" un archivo que en realidad no es nuestro.
                // Por eso se valida explicitamente que el archivo contenga al menos
                // una de las claves que generarJson() siempre escribe juntas.
                recuperarConValoresPorDefecto("el archivo no tiene el formato esperado de estadisticas (JSON invalido o de otro esquema)");
                return;
            }

            partidasTotales = extraerValorEntero(json, "partidas_totales");
            victoriasJugador = extraerValorEntero(json, "victorias_jugador");
            victoriasIA = extraerValorEntero(json, "victorias_ia");
            empates = extraerValorEntero(json, "empates");
            mejorRachaJugador = extraerValorEntero(json, "mejor_racha_jugador");
            mejorRachaIA = extraerValorEntero(json, "mejor_racha_ia");
            fechaPrimeraPartida = extraerValorString(json, "fecha_primera_partida");
            fechaUltimaPartida = extraerValorString(json, "fecha_ultima_partida");
        } catch (Exception e) {
            // Red de seguridad por si algo inesperado igual lanza una excepcion.
            recuperarConValoresPorDefecto("no se pudo interpretar el archivo (" + e.getMessage() + ")");
        }
    }

    /**
     * Un archivo genuino de estadisticas siempre trae estas claves, porque
     * generarJson() las escribe todas juntas cada vez que se guarda. Si el
     * contenido no trae ninguna, es basura, texto no-JSON, o un esquema
     * completamente distinto (version vieja/incompatible) - en cualquier
     * caso, no son datos que debamos intentar interpretar campo por campo.
     */
    private boolean pareceUnArchivoDeEstadisticasValido(String json) {
        return json.contains("\"partidas_totales\"")
            || json.contains("\"victorias_jugador\"")
            || json.contains("\"victorias_ia\"")
            || json.contains("\"empates\"");
    }

    /**
     * Restaura el estado en memoria a los valores por defecto y reescribe de
     * inmediato un archivo valido en disco, en vez de dejar el archivo
     * faltante, corrupto o incompatible tal cual estaba. Sin esto, un
     * archivo dañado se quedaba en disco hasta que terminara la siguiente
     * partida (la próxima llamada a guardarEstadisticas()), lo cual dejaba
     * una ventana en la que herramientas externas o un reinicio a medias
     * seguían viendo el archivo corrupto en vez de datos válidos en cero.
     */
    private void recuperarConValoresPorDefecto(String motivo) {
        System.err.println("Estadísticas reiniciadas a valores por defecto: " + motivo);
        inicializarEstadisticasDefault();
        guardarEstadisticas();
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

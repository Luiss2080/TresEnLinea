package logica;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Prueba directamente la resiliencia de la persistencia en JSON: archivo
 * faltante, JSON corrupto, JSON de un formato incompatible/antiguo y datos
 * binarios ilegibles. En todos los casos la aplicacion debe seguir
 * funcionando con valores por defecto en vez de fallar.
 *
 * <p>Como {@link EstadisticasManager} usa una ruta relativa fija
 * ("datos/estadisticas.json"), cada prueba respalda el archivo real antes de
 * manipularlo y lo restaura despues, para no perder datos de desarrollo.</p>
 */
class EstadisticasManagerTest {

    private static final Path ARCHIVO = Paths.get("datos/estadisticas.json");

    private byte[] contenidoOriginal;
    private boolean existiaOriginalmente;

    @BeforeEach
    void respaldarArchivoReal() throws IOException {
        existiaOriginalmente = Files.exists(ARCHIVO);
        contenidoOriginal = existiaOriginalmente ? Files.readAllBytes(ARCHIVO) : null;
    }

    @AfterEach
    void restaurarArchivoReal() throws IOException {
        if (existiaOriginalmente) {
            Files.createDirectories(ARCHIVO.getParent());
            Files.write(ARCHIVO, contenidoOriginal);
        } else if (Files.exists(ARCHIVO)) {
            Files.delete(ARCHIVO);
        }
    }

    @Test
    void archivoFaltanteCreaEstadisticasPorDefectoSinLanzarExcepcion() throws IOException {
        if (Files.exists(ARCHIVO)) {
            Files.delete(ARCHIVO);
        }

        EstadisticasManager em = assertDoesNotThrow(EstadisticasManager::new);

        assertEquals(0, em.getPartidasTotales());
        assertEquals(0, em.getVictoriasJugador());
        assertEquals(0, em.getVictoriasIA());
        assertEquals(0, em.getEmpates());
        assertTrue(Files.exists(ARCHIVO), "Debe autogenerar el archivo de estadisticas por defecto");
    }

    @Test
    void jsonCorruptoNoRompeLaAplicacionYUsaValoresPorDefecto() throws IOException {
        Files.createDirectories(ARCHIVO.getParent());
        Files.writeString(ARCHIVO, "{{{ esto no es json valido !!! ####");

        EstadisticasManager em = assertDoesNotThrow(EstadisticasManager::new);

        assertEquals(0, em.getPartidasTotales());
        assertEquals(0, em.getVictoriasJugador());
        assertEquals(0, em.getVictoriasIA());
        assertEquals(0, em.getEmpates());
    }

    @Test
    void jsonDeFormatoIncompatibleOAntiguoNoRompeLaAplicacion() throws IOException {
        Files.createDirectories(ARCHIVO.getParent());
        // JSON valido, pero con un esquema completamente distinto (formato viejo/incompatible)
        Files.writeString(ARCHIVO, "{\"score\": 42, \"player_name\": \"bob\", \"version\": \"0.1-old-format\"}");

        EstadisticasManager em = assertDoesNotThrow(EstadisticasManager::new);

        assertEquals(0, em.getPartidasTotales());
        assertNull(em.getFechaPrimeraPartida());
    }

    @Test
    void archivoBinarioIlegibleNoRompeLaAplicacion() throws IOException {
        Files.createDirectories(ARCHIVO.getParent());
        Files.write(ARCHIVO, new byte[]{
            (byte) 0xFF, (byte) 0xFE, 0x00, 0x01, 0x02, (byte) 0xDE, (byte) 0xAD, (byte) 0xBE, (byte) 0xEF
        });

        EstadisticasManager em = assertDoesNotThrow(EstadisticasManager::new);

        assertEquals(0, em.getPartidasTotales());
    }

    @Test
    void guardarYCargarEstadisticasEsUnRoundTripCorrecto() throws IOException {
        if (Files.exists(ARCHIVO)) {
            Files.delete(ARCHIVO);
        }

        EstadisticasManager primero = new EstadisticasManager();
        primero.registrarVictoriaJugador();
        primero.registrarVictoriaIA();
        primero.registrarVictoriaIA();
        primero.registrarEmpate();

        EstadisticasManager segundo = new EstadisticasManager();
        assertEquals(4, segundo.getPartidasTotales());
        assertEquals(1, segundo.getVictoriasJugador());
        assertEquals(2, segundo.getVictoriasIA());
        assertEquals(1, segundo.getEmpates());
    }

    @Test
    void resetearEstadisticasVuelveTodoACero() {
        EstadisticasManager em = new EstadisticasManager();
        em.registrarVictoriaJugador();
        em.registrarVictoriaIA();

        em.resetearEstadisticas();

        assertEquals(0, em.getPartidasTotales());
        assertEquals(0, em.getVictoriasJugador());
        assertEquals(0, em.getVictoriasIA());
        assertEquals(0, em.getEmpates());
        assertNull(em.getFechaPrimeraPartida());
    }

    @Test
    void rachasSeActualizanYSeCortanAlPerderLaRacha() throws IOException {
        if (Files.exists(ARCHIVO)) {
            Files.delete(ARCHIVO);
        }
        EstadisticasManager em = new EstadisticasManager();

        em.registrarVictoriaJugador();
        em.registrarVictoriaJugador();
        assertEquals(2, em.getRachaActualJugador());
        assertEquals(2, em.getMejorRachaJugador());

        em.registrarVictoriaIA();
        assertEquals(0, em.getRachaActualJugador(), "La racha del jugador debe cortarse al perder");
        assertEquals(1, em.getRachaActualIA());
        assertEquals(2, em.getMejorRachaJugador(), "La mejor racha historica no debe borrarse");
    }
}

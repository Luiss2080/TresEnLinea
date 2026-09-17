# 🎮 TresEnLinea

Tres en raya de escritorio en Java con una IA Minimax cuya invencibilidad
fue verificada recorriendo el árbol de juego **completo** (no partidas de
muestra), interfaz Swing con animaciones y estadísticas persistentes en
JSON que se auto-repara si el archivo falta o está corrupto. Pensado para
quien quiera un ejemplo compacto y bien probado de Minimax, arquitectura
MVC e hilos correctos en Swing.

## Características

- 🤖 **IA Minimax invencible, verificada de forma exhaustiva** — un test
  automatizado recorre las 569 partidas posibles con el humano moviendo
  primero (386 victorias de la IA, 183 empates, **0 derrotas**) y las 73
  partidas posibles si la IA mueve primero (71 victorias, 2 empates, 0
  derrotas). El mejor resultado posible para el humano es el empate.
- 🧠 **Puntuación con profundidad**: el Minimax prefiere ganar rápido y
  perder tarde (`10 - profundidad` / `profundidad - 10`), no solo ganar.
- 🎨 **Interfaz Swing** con look and feel Nimbus, splash screen animado,
  efectos de victoria/derrota/empate y resaltado al pasar el mouse.
- 🔒 **Turnos protegidos**: mientras la IA "piensa" (retraso de 500ms), el
  tablero se deshabilita y no acepta un segundo movimiento del jugador.
- 📊 **Estadísticas persistentes en JSON** (partidas, victorias, empates,
  rachas, fechas) sin librerías externas — y auto-reparables: si el
  archivo no existe, está corrupto, es binario ilegible o pertenece a un
  esquema incompatible, la app cae a valores por defecto y reescribe un
  archivo válido de inmediato, en vez de fallar o quedar en un estado raro.
- 🧵 **Manejo de hilos centralizado**: toda actualización de la UI pasa
  por `GestorHilos`, que garantiza que nada fuera del Event Dispatch
  Thread de Swing toque un componente gráfico.
- ✅ **Suite de pruebas JUnit 5**, incluida la verificación exhaustiva de
  invencibilidad, detección de victoria/empate en cada línea del tablero,
  y los cuatro escenarios de persistencia corrupta mencionados arriba.
- ⚙️ **CI con GitHub Actions**: cada push/PR compila y corre las pruebas
  automáticamente.

## Cómo usar

1. Ejecuta la aplicación (ver instalación abajo). Tú juegas con **X**, la
   IA con **O**, y tú siempre mueves primero.
2. Haz clic en cualquier casilla vacía. La IA responde automáticamente
   tras un breve instante.
3. Objetivo: conseguir tres en línea. Como la IA es invencible, tu mejor
   resultado realista es un empate.
4. Usa **Nueva Partida** para reiniciar el tablero y **Reset Stats** para
   borrar el historial guardado en `datos/estadisticas.json`.

## Instalación y uso local

Requiere JDK 17 o superior.

**Con Maven** (recomendado — también compila y corre las pruebas):

```bash
mvn package
java -jar target/tres-en-linea.jar
```

**Sin Maven, con javac directamente** (como estaba originalmente el
proyecto):

```bash
javac -cp "src/main/java" -d "bin" src/main/java/main/Main.java src/main/java/logica/*.java src/main/java/presentacion/*.java
java -cp "bin" main.Main
```

En Windows también puedes usar el script incluido:

```bash
ejecutar.bat
```

## Tecnologías

- **Java 17+** (compilado y probado con JDK 21)
- **Swing** para la interfaz gráfica (sin frameworks de UI externos)
- **Maven** para build y gestión de dependencias
- **JUnit 5** para las pruebas automatizadas
- **GitHub Actions** para integración continua
- JSON de estadísticas escrito/leído a mano, sin librerías externas

## Arquitectura

Patrón MVC simple: `logica/` (Tablero, MinimaxIA, EstadisticasManager) no
depende de Swing y es completamente testeable por sí sola; `presentacion/`
contiene la interfaz gráfica y el manejo de hilos (`GestorHilos`); `main/`
es el punto de entrada.

## Tests

```bash
mvn test
```

Incluye, entre otras:
- Verificación exhaustiva de que la IA nunca pierde (ambas
  configuraciones: IA primero y humano primero).
- Detección de victoria en las 3 filas, 3 columnas y ambas diagonales, y
  de empate con el tablero lleno.
- Persistencia resiliente ante archivo faltante, JSON corrupto, esquema
  incompatible y datos binarios ilegibles.
- Contrato de hilos de `GestorHilos` (EDT).
- Regresión del bug de doble clic durante el turno de la IA.

## Licencia

MIT — ver [LICENSE](LICENSE).

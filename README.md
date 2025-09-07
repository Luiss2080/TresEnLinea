# 🎮 Tres en Raya con IA Minimax

¡Un juego clásico con inteligencia artificial invencible! Desafía al algoritmo Minimax y pon a prueba tu estrategia. 🧠

## ✨ Características

- 🤖 **IA Invencible** - Algoritmo Minimax que nunca pierde
- 📊 **Sistema de Estadísticas** - Guarda tu historial de partidas
- 🎨 **Interfaz Atractiva** - Diseño moderno con efectos visuales
- ⚡ **Respuesta Rápida** - Jugabilidad fluida y sin bloqueos
- 📱 **Fácil de Usar** - Interfaz intuitiva con un clic

## 🚀 Cómo Ejecutar

### 🔧 Compilación Rápida
```bash
# Navegar al directorio del proyecto
cd "3-En-Raya"

# Compilar todos los archivos
javac -cp "src/main/java" -d "bin" src/main/java/**/*.java

# Ejecutar el juego
java -cp "bin" main.Main
```

### 🎯 Ejecución Directa
Si ya está compilado:
```bash
java -cp "bin" main.Main
```

## 📁 Estructura del Proyecto

```
3-En-Raya/
│
├── 📂 src/main/java/           # Código fuente principal
│   ├── 🎯 main/               # Punto de entrada
│   │   └── Main.java          # Clase principal con splash screen
│   │
│   ├── 🧠 logica/             # Núcleo del juego
│   │   ├── Tablero.java       # Gestión del tablero 3x3
│   │   ├── MinimaxIA.java     # Algoritmo de inteligencia artificial
│   │   └── EstadisticasManager.java # Persistencia de datos
│   │
│   └── 🎨 presentacion/       # Interfaz gráfica
│       ├── VentanaPrincipal.java    # Ventana principal del juego
│       ├── PanelTablero.java        # Grid de botones del tablero
│       ├── PanelControles.java      # Controles y estado del juego
│       ├── EfectosVisuales.java     # Animaciones y notificaciones
│       └── GestorHilos.java         # Manejo de hilos para UI fluida
│
├── 📦 bin/                    # Archivos compilados (.class)
│
├── 💾 datos/                  # Archivos de datos
│   └── estadisticas.json      # Historial de partidas guardado
│
└── 📖 README.md              # Este archivo
```

## 🎮 Cómo Jugar

1. **🟦 Tu turno**: Haz clic en cualquier casilla vacía (eres **X**)
2. **🤖 Turno IA**: La IA responde automáticamente (es **O**)
3. **🎯 Objetivo**: Consigue 3 en línea (horizontal, vertical o diagonal)
4. **🏆 Meta realista**: ¡Intenta conseguir un empate! (La IA no pierde)

### 🕹️ Controles
- **Nueva Partida** - Reinicia el tablero
- **Estadísticas** - Ver tu historial completo
- **Reset Stats** - Borrar todas las estadísticas
- **Ayuda** - Mostrar información del juego
- **Salir** - Cerrar la aplicación

## 🧠 Sobre la IA

La IA utiliza el **algoritmo Minimax**, una técnica de búsqueda que:
- 🔍 **Explora** todos los movimientos posibles
- 📊 **Evalúa** cada posición del tablero
- 🎯 **Elige** siempre el mejor movimiento
- 🛡️ **Garantiza** que nunca perderá

> **Dato curioso**: En un tablero de 3x3, existen 255,168 estados posibles del juego, ¡y la IA los conoce todos!

## 📊 Sistema de Estadísticas

El juego rastrea automáticamente:
- 📈 **Partidas Totales** jugadas
- 🏆 **Tus Victorias** (si las hay 😉)
- 🤖 **Victorias de la IA**
- 🤝 **Empates** conseguidos
- 🔥 **Rachas** actuales y mejores
- 📅 **Fechas** de primera y última partida

## ⚙️ Requisitos Técnicos

- ☕ **Java 21+** (compatible con versiones anteriores desde Java 8)
- 🖥️ **Sistema Operativo**: Windows, macOS, Linux
- 💾 **RAM**: Mínimo 512MB disponibles
- 📦 **Espacio**: ~2MB para el proyecto completo

## 🛠️ Para Desarrolladores

### 🏗️ Arquitectura
- **Patrón MVC**: Separación clara entre lógica y presentación
- **Swing**: Interfaz gráfica nativa de Java
- **JSON Manual**: Persistencia sin librerías externas
- **Hilos**: Gestión cuidadosa para UI responsiva

### 🔧 Extensiones Posibles
- 🎨 Temas personalizables
- 🔊 Efectos de sonido
- 🌐 Modo multijugador online
- 📱 Versión móvil con JavaFX
- 🤖 Diferentes niveles de dificultad

## 🎯 Estrategia Recomendada

Como la IA es invencible, aquí algunos consejos para conseguir empates:

1. **🎯 Centro primero**: Siempre juega en el centro si está disponible
2. **🛡️ Bloquea amenazas**: Impide que la IA haga líneas de 2
3. **⚔️ Crea dilemas**: Intenta crear dos amenazas simultáneas
4. **🧠 Piensa adelante**: Anticipa los movimientos de la IA

## 📜 Historia del Proyecto

Este proyecto surgió como una demostración del algoritmo Minimax aplicado a un juego clásico. La implementación se enfoca en:

- ✅ **Código limpio** y bien documentado
- ✅ **Interfaz intuitiva** para usuarios de cualquier nivel
- ✅ **Rendimiento óptimo** sin librerías externas
- ✅ **Experiencia fluida** sin bloqueos o lag

## 🎊 ¡Disfruta el Desafío!

¿Serás capaz de conseguir un empate contra la IA invencible? 

**¡Dale una oportunidad y descúbrelo!** 🎮

---

*Proyecto educativo - Perfecto para aprender sobre algoritmos de búsqueda, interfaces gráficas en Java y arquitectura de software* 📚
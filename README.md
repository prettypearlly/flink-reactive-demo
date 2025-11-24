# Disaster Detection System with Apache Flink 2.1 (Reactive Mode Demo)

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Flink](https://img.shields.io/badge/Apache%20Flink-2.1.1-orange.svg)](https://flink.apache.org)
[![Java](https://img.shields.io/badge/Java-17-red.svg)](https://openjdk.org/projects/jdk/17/)

# flink-reactive-demo
Sistema de detección de desastres en tiempo real migrado a Apache Flink 2.1. Demostración de elasticidad nativa usando Adaptive Scheduler en Modo Reactivo y persistencia de estado (Checkpointing) frente a picos de tráficos.

### Características Clave
* **Elasticidad Nativa:** Uso del *Reactive Mode* para adaptar el paralelismo del grafo de ejecución a los recursos disponibles en tiempo real.
* **Gestión de Estado (Stateful):** Conteo de alertas mediante ventanas deslizantes (*Sliding Windows*) persistidas en Checkpoints.
* **Simulación de Sharding:** Distribución de carga basada en zonas geográficas para evitar *Data Skew*.
* **Arquitectura Standalone:** Despliegue en *Application Mode* simulando un entorno de producción.

---

## 🛠️ Requisitos Previos

Para ejecutar esta demostración necesita:

* **Java 17** (Requerido para Flink 2.x).
* **Apache Maven 3.8+**.
* **Apache Flink 2.1.1** (Binarios instalados localmente).
* Sistema operativo tipo Unix (Linux/macOS) o WSL en Windows.

---

## Compilación (Build)

Clone el repositorio y genere el artefacto ejecutable (JAR):

```bash
# 1. Clonar repo
git clone [https://github.com/TU_USUARIO/flink-reactive-demo.git](https://github.com/TU_USUARIO/flink-reactive-demo.git)
cd flink-reactive-demo

# 2. Compilar y empaquetar
mvn clean package
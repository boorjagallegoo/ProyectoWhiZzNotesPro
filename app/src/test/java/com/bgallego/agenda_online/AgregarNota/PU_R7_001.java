package com.bgallego.agenda_online.AgregarNota;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class PU_R7_001 extends TestCase {
    @Test
    public void testAgregarNotaConDatosFaltantes() {
        Agregar_Nota agregarNota = new Agregar_Nota();

        // Establecer datos con algunos datos faltantes
        agregarNota.Uid_Usuario.setText("uid123");
        agregarNota.Correo_usuario.setText("correo@example.com");
        agregarNota.Fecha_hora_actual.setText("30-12-2023/12:18:21 am");
        agregarNota.Titulo.setText("Título de la nota");
        agregarNota.Descripcion.setText("");
        agregarNota.Fecha.setText("01/01/2024");
        agregarNota.Estado.setText("Activo");

        // Llamar al método Agregar_Nota que debería detectar los datos faltantes
        try {
            agregarNota.AgregarNota();
            // Si no lanza una excepción, la prueba falla
            fail("Se esperaba una excepción");
        } catch (IllegalArgumentException e) {
            // Se espera que se lance una excepción porque faltan datos
            assertEquals("Llenar todos los campos", e.getMessage());
        }
    }
}
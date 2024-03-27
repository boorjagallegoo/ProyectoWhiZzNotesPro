package com.bgallego.agenda_online.AgregarNota;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
public class PU_R6_001 {

    private Agregar_Nota agregarNota;

    @Before
    public void onBefore() {
        // Inicializar la instancia de Agregar_Nota
        agregarNota = new Agregar_Nota();
    }

    @Test
    public void testAgregarNotaConDatosValidos() {
        // Configurar datos válidos
        agregarNota.Uid_Usuario.setText("uid123");
        agregarNota.Correo_usuario.setText("correo@example.com");
        agregarNota.Fecha_hora_actual.setText("30-12-2023/12:18:21 am");
        agregarNota.Titulo.setText("Título de la nota");
        agregarNota.Descripcion.setText("Descripción de la nota");
        agregarNota.Fecha.setText("01/01/2024");
        agregarNota.Estado.setText("Activo");

        // Llamar al método AgregarNota
        try {
            agregarNota.AgregarNota();

            // Aquí podrías realizar más aserciones según el comportamiento esperado
            // Por ejemplo, verificar que se haya mostrado un Toast, etc.

        } catch (IllegalArgumentException e) {
            fail("No se esperaba una excepción: " + e.getMessage());
        }
    }

    @Test
    public void testAgregarNotaConDatosFaltantes() {
        // Configurar datos con algunos datos faltantes
        agregarNota = Robolectric.buildActivity(Agregar_Nota.class).create().start().resume().visible().get();
        agregarNota.Uid_Usuario.setText("uid123");
        agregarNota.Correo_usuario.setText("correo@example.com");
        agregarNota.Fecha_hora_actual.setText("30-12-2023/12:18:21 am");
        agregarNota.Titulo.setText("Título de la nota");
        agregarNota.Descripcion.setText("");
        agregarNota.Fecha.setText("01/01/2024");
        agregarNota.Estado.setText("Activo");

        // Llamar al método AgregarNota y esperar una excepción
        try {
            agregarNota.AgregarNota();
            fail("Se esperaba una excepción");
        } catch (IllegalArgumentException e) {
            assertEquals("Llenar todos los campos", e.getMessage());
        }
    }

}

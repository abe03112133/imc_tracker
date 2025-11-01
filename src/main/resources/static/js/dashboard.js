// Cargar mediciones recientes al cargar la página
    document.addEventListener('DOMContentLoaded', function() {
        cargarUltimasMediciones();
        cargarUltimaMedicion();
    });

    // Función para cargar las últimas mediciones desde la API REST
    async function cargarUltimasMediciones() {
        try {
            const response = await fetch('/api/imc/ultimas');
            const data = await response.json();

            document.getElementById('loadingRecientes').classList.add('hidden');

            if (Array.isArray(data) && data.length > 0) {
                mostrarMedicionesRecientes(data);
            } else {
                document.getElementById('emptyStateRecientes').classList.remove('hidden');
            }
        } catch (error) {
            console.error('Error al cargar mediciones:', error);
            document.getElementById('loadingRecientes').classList.add('hidden');
            document.getElementById('emptyStateRecientes').classList.remove('hidden');
        }
    }

    // Función para mostrar mediciones en la tabla
    function mostrarMedicionesRecientes(mediciones) {
        const tbody = document.getElementById('bodyRecientes');
        tbody.innerHTML = '';

        mediciones.forEach(medicion => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    ${formatearFecha(medicion.fechaMedicion)}
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    ${medicion.peso} kg
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-semibold text-gray-900">
                    ${medicion.imc}
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                    <span class="px-3 py-1 inline-flex text-xs leading-5 font-semibold rounded-full ${getClasificacionClass(medicion.clasificacion)}">
                        ${medicion.clasificacion}
                    </span>
                </td>
            `;
            tbody.appendChild(tr);
        });

        document.getElementById('tablaRecientes').classList.remove('hidden');
    }

    // Función para cargar la última medición en las cards
    async function cargarUltimaMedicion() {
        try {
            const response = await fetch('/api/imc/ultima');
            const data = await response.json();

            if (data && data.imc) {
                document.getElementById('imcActual').textContent = data.imc;
                document.getElementById('pesoActual').textContent = data.peso + ' kg';
                document.getElementById('clasificacionActual').textContent = data.clasificacion;
                document.getElementById('fechaUltima').textContent = 'Última medición: ' + formatearFecha(data.fechaMedicion);
            }
        } catch (error) {
            console.error('Error al cargar última medición:', error);
        }
    }

    // Manejar el envío del formulario
    document.getElementById('formCalcularIMC').addEventListener('submit', async function(e) {
        e.preventDefault();

        const peso = document.getElementById('peso').value;
        const notas = document.getElementById('notas').value;

        if (!peso || peso <= 0) {
            alert('Por favor ingrese un peso válido mayor a 0');
            return;
        }

        try {
            const response = await fetch('/api/imc/calcular', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ peso: parseFloat(peso), notas: notas })
            });

            const data = await response.json();

            if (response.ok) {
                // Mostrar resultado
                alert(`IMC calculado exitosamente!\n\nIMC: ${data.imc}\nClasificación: ${data.clasificacion}\n\n${data.mensaje}`);

                // Recargar datos
                cargarUltimasMediciones();
                cargarUltimaMedicion();

                // Limpiar formulario
                document.getElementById('formCalcularIMC').reset();

                // Recargar página para actualizar el contador
                setTimeout(() => location.reload(), 1500);
            } else {
                alert('Error: ' + (data.error || 'No se pudo calcular el IMC'));
            }
        } catch (error) {
            console.error('Error:', error);
            alert('Error al calcular IMC. Por favor intente de nuevo.');
        }
    });

    // Funciones auxiliares
    function formatearFecha(fechaString) {
        const fecha = new Date(fechaString);
        const opciones = { year: 'numeric', month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit' };
        return fecha.toLocaleDateString('es-MX', opciones);
    }

    function getClasificacionClass(clasificacion) {
        switch(clasificacion) {
            case 'Bajo peso':
                return 'bg-yellow-100 text-yellow-800';
            case 'Normal':
                return 'bg-green-100 text-green-800';
            case 'Sobrepeso':
                return 'bg-orange-100 text-orange-800';
            case 'Obesidad':
                return 'bg-red-100 text-red-800';
            default:
                return 'bg-gray-100 text-gray-800';
        }
    }
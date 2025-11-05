// Cargar historial al cargar la página
    document.addEventListener('DOMContentLoaded', function() {
        cargarHistorialCompleto();
    });

    // Función para cargar el historial completo desde la API REST
    async function cargarHistorialCompleto() {
        try {
            console.log('Solicitando historial a: /api/imc/historial');
            const response = await fetch('/api/imc/historial');

            if (!response.ok) {
                throw new Error('Error en la respuesta de la API');
            }

            const data = await response.json();
            console.log('Historial recibido:', data);

            document.getElementById('loadingHistorial').classList.add('hidden');

            if (Array.isArray(data) && data.length > 0) {
                mostrarHistorial(data);
                calcularEstadisticas(data);
            } else {
                document.getElementById('emptyStateHistorial').classList.remove('hidden');
            }
        } catch (error) {
            console.error('Error al cargar historial:', error);
            document.getElementById('loadingHistorial').classList.add('hidden');
            document.getElementById('errorStateHistorial').classList.remove('hidden');
        }
    }

    // Función para mostrar el historial en la tabla
    function mostrarHistorial(mediciones) {
        const tbody = document.getElementById('bodyHistorial');
        tbody.innerHTML = '';

        mediciones.forEach((medicion, index) => {
            const tr = document.createElement('tr');
            tr.className = 'hover:bg-gray-50';
            tr.innerHTML = `
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    ${mediciones.length - index}
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    ${formatearFechaCompleta(medicion.fechaMedicion)}
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                    ${medicion.peso} kg
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-bold text-gray-900">
                    ${medicion.imc}
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                    <span class="px-3 py-1 inline-flex text-xs leading-5 font-semibold rounded-full ${getClasificacionClass(medicion.clasificacion)}">
                        ${medicion.clasificacion}
                    </span>
                </td>
                <td class="px-6 py-4 text-sm text-gray-500">
                    ${medicion.notas || '-'}
                </td>
            `;
            tbody.appendChild(tr);
        });

        document.getElementById('tablaHistorial').classList.remove('hidden');
        document.getElementById('countMediciones').textContent = mediciones.length;
    }

    // Función para calcular estadísticas
    function calcularEstadisticas(mediciones) {
        if (mediciones.length === 0) return;

        const total = mediciones.length;
        const pesoInicial = mediciones[mediciones.length - 1].peso;
        const pesoActual = mediciones[0].peso;
        const variacion = pesoActual - pesoInicial;

        document.getElementById('totalMediciones').textContent = total;
        document.getElementById('pesoInicial').textContent = pesoInicial + ' kg';
        document.getElementById('pesoActual').textContent = pesoActual + ' kg';

        const variacionElement = document.getElementById('variacion');
        const variacionTexto = (variacion > 0 ? '+' : '') + variacion.toFixed(1) + ' kg';
        variacionElement.textContent = variacionTexto;

        // Colorear según variación
        if (variacion > 0) {
            variacionElement.classList.add('text-red-600');
        } else if (variacion < 0) {
            variacionElement.classList.add('text-green-600');
        } else {
            variacionElement.classList.add('text-gray-900');
        }
    }

    // Funciones auxiliares
    function formatearFechaCompleta(fechaString) {
        const fecha = new Date(fechaString);
        const opciones = {
            year: 'numeric',
            month: 'long',
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        };
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
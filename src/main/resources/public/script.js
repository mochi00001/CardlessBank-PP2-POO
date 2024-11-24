// script.js

var App = {
    // Función de inicialización
    init: function() {
        // Mostrar sección por defecto o página de inicio
        App.showSection('gestionClientes');
        
        // Adjuntar event listeners
        App.attachEventListeners();
    },
    
    // Método para adjuntar todos los event listeners
    attachEventListeners: function() {
        // Enlaces del menú de navegación
        var menuLinks = document.querySelectorAll('.menu a');
        menuLinks.forEach(function(link) {
            link.addEventListener('click', function(event) {
                event.preventDefault();
                var onclickAttr = this.getAttribute('onclick');
                var match = onclickAttr.match(/mostrarSeccion\('([^']+)'\)/);
                if (match && match[1]) {
                    App.showSection(match[1]);
                }
            });
        });
        
        // Event listeners para formularios y botones
        
        // Gestión de Clientes
        // Crear Cliente Físico
        var formCrearClienteFisico = document.getElementById('formClienteFisico');
        if (formCrearClienteFisico) {
            formCrearClienteFisico.addEventListener('submit', function(event) {
                event.preventDefault(); // Prevenir el comportamiento por defecto de envío
                App.crearClienteFisico(); // Llamar la función para crear el cliente
            });
        }

        
        // Crear Cliente Jurídico
        var formCrearClienteJuridico = document.getElementById('formClienteJuridico');
        if (formCrearClienteJuridico) {
            formCrearClienteJuridico.addEventListener('submit', function(event) {
                event.preventDefault(); // Prevenir el comportamiento por defecto de envío
                App.crearClienteJuridico(); // Llamar la función para crear el cliente
            });
        }
        
        // Cambiar Teléfono
        var btnValidarIdentificacionTelefono = document.querySelector('#formCambiarTelefono button');
        if (btnValidarIdentificacionTelefono) {
            btnValidarIdentificacionTelefono.addEventListener('click', App.validarIdentificacionParaTelefono);
        }

        var btnCambiarTelefono = document.querySelector('#camposCambioTelefono button');
        if (btnCambiarTelefono) {
            btnCambiarTelefono.addEventListener('click', App.cambiarTelefono);
        }

        // Cambiar Correo
        var btnValidarIdentificacionCorreo = document.querySelector('#formCambiarCorreo button');
        if (btnValidarIdentificacionCorreo) {
            btnValidarIdentificacionCorreo.addEventListener('click', App.validarIdentificacionParaCorreo);
        }

        var btnCambiarCorreo = document.querySelector('#camposCambioCorreo button');
        if (btnCambiarCorreo) {
            btnCambiarCorreo.addEventListener('click', App.cambiarCorreo);
        }

        
        // Gestión de Cuentas
        // Botón para validar la identidad del cliente
        var btnValidarCliente = document.querySelector('#btnValidarCliente');
        if (btnValidarCliente) {
            btnValidarCliente.addEventListener('click', App.validarCliente);
        }

        // Botón para crear la cuenta
        var btnCrearCuenta = document.querySelector('#btnCrearCuenta');
        if (btnCrearCuenta) {
            btnCrearCuenta.addEventListener('click', App.crearCuenta);
        }
        
        // Botón para validar la cuenta antes de cambiar el PIN
        var btnValidarCuentaPin = document.querySelector('#btnValidarCuentaPin');
        if (btnValidarCuentaPin) {
            btnValidarCuentaPin.addEventListener('click', App.validarCuentaPin);
        }

        // Botón para cambiar el PIN
        var btnCambiarPin = document.querySelector('#btnCambiarPin');
        if (btnCambiarPin) {
            btnCambiarPin.addEventListener('click', App.cambiarPin);
        }
        
        // Eliminar Cuenta
        var btnValidarCuentaEliminar = document.querySelector('#btnValidarCuentaEliminar');
        if (btnValidarCuentaEliminar) {
            btnValidarCuentaEliminar.addEventListener('click', App.validarCuentaParaEliminar);
        }
        
        var btnConfirmarEliminacion = document.querySelector('#btnConfirmarEliminacion');
        if (btnConfirmarEliminacion) {
            btnConfirmarEliminacion.addEventListener('click', App.confirmarEliminacion);
        }
        
        var btnCancelarEliminacion = document.querySelector('#btnCancelarEliminacion');
        if (btnCancelarEliminacion) {
            btnCancelarEliminacion.addEventListener('click', App.cancelarEliminacion);
        }
        
        // Operaciones Financieras
        // Depósito en Colones
        // Validar Cuenta
        var btnValidarCuentaDepositoColones = document.getElementById('btnValidarCuentaDepositoColones');
        if (btnValidarCuentaDepositoColones) {
            btnValidarCuentaDepositoColones.addEventListener('click', App.validarCuentaDepositoColones);
        }

        // Realizar Depósito
        var btnRealizarDepositoColones = document.getElementById('btnRealizarDepositoColones');
        if (btnRealizarDepositoColones) {
            btnRealizarDepositoColones.addEventListener('click', App.realizarDepositoColones);
        }

        
        // Depósito en Dólares
        // Validar Cuenta
        var btnValidarCuentaDepositoDolares = document.getElementById('btnValidarCuentaDepositoDolares');
        if (btnValidarCuentaDepositoDolares) {
            btnValidarCuentaDepositoDolares.addEventListener('click', App.validarCuentaDepositoDolares);
        }

        // Realizar Depósito
        var btnRealizarDepositoDolares = document.getElementById('btnRealizarDepositoDolares');
        if (btnRealizarDepositoDolares) {
            btnRealizarDepositoDolares.addEventListener('click', App.realizarDepositoDolares);
        }

        
        // Retiro en Colones
        // Validar Cuenta y PIN
        var btnValidarCuentaRetiroColones = document.getElementById('btnValidarCuentaRetiroColones');
        if (btnValidarCuentaRetiroColones) {
            btnValidarCuentaRetiroColones.addEventListener('click', App.validarCuentaRetiroColones);
        }

        // Validar Palabra Clave
        var btnValidarPalabraClaveRetiroColones = document.getElementById('btnValidarPalabraClaveRetiroColones');
        if (btnValidarPalabraClaveRetiroColones) {
            btnValidarPalabraClaveRetiroColones.addEventListener('click', App.validarPalabraClaveRetiroColones);
        }

        // Realizar Retiro
        var btnRealizarRetiroColones = document.getElementById('btnRealizarRetiroColones');
        if (btnRealizarRetiroColones) {
            btnRealizarRetiroColones.addEventListener('click', App.realizarRetiroColones);
        }


        
        // Retiro en Dólares
        // Validar Cuenta y PIN
        var btnValidarCuentaRetiroDolares = document.getElementById('btnValidarCuentaRetiroDolares');
        if (btnValidarCuentaRetiroDolares) {
            btnValidarCuentaRetiroDolares.addEventListener('click', App.validarCuentaRetiroDolares);
        }

        // Validar Palabra Clave
        var btnValidarPalabraClaveRetiroDolares = document.getElementById('btnValidarPalabraClaveRetiroDolares');
        if (btnValidarPalabraClaveRetiroDolares) {
            btnValidarPalabraClaveRetiroDolares.addEventListener('click', App.validarPalabraClaveRetiroDolares);
        }

        // Realizar Retiro
        var btnRealizarRetiroDolares = document.getElementById('btnRealizarRetiroDolares');
        if (btnRealizarRetiroDolares) {
            btnRealizarRetiroDolares.addEventListener('click', App.realizarRetiroDolares);
        }



        
        // Transferencia
        // Validar Cuenta Origen y PIN
        var btnValidarCuentaOrigen = document.getElementById('btnValidarCuentaOrigen');
        if (btnValidarCuentaOrigen) {
            btnValidarCuentaOrigen.addEventListener('click', App.validarCuentaOrigen);
        }

        // Validar Palabra Clave
        var btnValidarPalabraClaveTransferencia = document.getElementById('btnValidarPalabraClaveTransferencia');
        if (btnValidarPalabraClaveTransferencia) {
            btnValidarPalabraClaveTransferencia.addEventListener('click', App.validarPalabraClaveTransferencia);
        }

        // Validar Cuenta Destino
        var btnValidarCuentaDestino = document.getElementById('btnValidarCuentaDestino');
        if (btnValidarCuentaDestino) {
            btnValidarCuentaDestino.addEventListener('click', App.validarCuentaDestino);
        }

        // Realizar Transferencia
        var btnRealizarTransferencia = document.getElementById('btnRealizarTransferencia');
        if (btnRealizarTransferencia) {
            btnRealizarTransferencia.addEventListener('click', App.realizarTransferencia);
        }

        
        // Consultas y Reportes
        // Consultar Transacciones
        var btnValidarCuentaTransacciones = document.getElementById('btnValidarCuentaTransacciones');
        if (btnValidarCuentaTransacciones) {
            btnValidarCuentaTransacciones.addEventListener('click', App.validarCuentaTransacciones);
        }

        var btnValidarPalabraClaveTransacciones = document.getElementById('btnValidarPalabraClaveTransacciones');
        if (btnValidarPalabraClaveTransacciones) {
            btnValidarPalabraClaveTransacciones.addEventListener('click', App.validarPalabraClaveTransacciones);
        }


        // Asignar eventos a los botones
        document.getElementById('consultarTipoCambioCompra').addEventListener('click', App.consultarTipoCambioCompra);
        document.getElementById('consultarTipoCambioVenta').addEventListener('click', App.consultarTipoCambioVenta);

        // Consultar Saldo Actual en Colones
        var btnConsultarSaldoColones = document.querySelector('#formSaldoColones button');
        if (btnConsultarSaldoColones) {
            btnConsultarSaldoColones.addEventListener('click', App.consultarSaldoColones);
        }

        // Consultar Saldo Actual en Dólares
        var btnConsultarSaldoDolares = document.querySelector('#formSaldoDolares button');
        if (btnConsultarSaldoDolares) {
            btnConsultarSaldoDolares.addEventListener('click', App.consultarSaldoDolares);
        }

        // Consultar Estado de Cuenta
        var btnConsultarEstadoCuenta = document.querySelector('#formEstadoCuenta button');
        if (btnConsultarEstadoCuenta) {
            btnConsultarEstadoCuenta.addEventListener('click', App.consultarEstadoCuenta);
        }

        // Consultar Estatus de una Cuenta
        var btnConsultarEstatusCuenta = document.querySelector('#formConsultaCuenta button');
        if (btnConsultarEstatusCuenta) {
            btnConsultarEstatusCuenta.addEventListener('click', App.consultarEstatusCuenta);
        }

        // Consultar Cuentas de un Cliente
        var btnConsultarCuentasCliente = document.querySelector('#formConsultaCliente button');
        if (btnConsultarCuentasCliente) {
            btnConsultarCuentasCliente.addEventListener('click', App.listarCuentasDeCliente);
        }

        // Listar Clientes Registrados
        var menuClientesRegistrados = document.querySelector('a[onclick="mostrarSeccion(\'consultarClientesRegistrados\')"]');
        if (menuClientesRegistrados) {
            menuClientesRegistrados.addEventListener('click', App.listarClientesRegistrados);
        }

        // Listar Cuentas Registradas
        var menuCuentasRegistradas = document.querySelector('a[onclick="mostrarSeccion(\'consultarCuentasRegistradas\')"]');
        if (menuCuentasRegistradas) {
            menuCuentasRegistradas.addEventListener('click', App.listarCuentasRegistradas);
        }
    },
    
    // Método para mostrar una sección y ocultar las demás
    showSection: function(sectionId) {
        // Ocultar todas las secciones
        var sections = document.querySelectorAll('.contenido');
        sections.forEach(function(section) {
            section.style.display = 'none';
        });
        
        // Mostrar la sección seleccionada
        var selectedSection = document.getElementById(sectionId);
        if (selectedSection) {
            selectedSection.style.display = 'block';
        }
    },
    
    // Funciones utilitarias
    showElement: function(elementId) {
        var elem = document.getElementById(elementId);
        if (elem) {
            elem.style.display = 'block';
        }
    },
    
    hideElement: function(elementId) {
        var elem = document.getElementById(elementId);
        if (elem) {
            elem.style.display = 'none';
        }
    },
    
    displayMessage: function(elementId, message) {
        var elem = document.getElementById(elementId);
        if (elem) {
            elem.innerHTML = message;
        }
    },

    // Función para obtener fecha actual en formato DD/MM/AAAA
    getCurrentDate: function() {
        var today = new Date();
        var day = String(today.getDate()).padStart(2, '0');
        var month = String(today.getMonth() + 1).padStart(2, '0'); // Enero es 0
        var year = today.getFullYear();
        return day + '/' + month + '/' + year;
    },

    // Función para simular obtener el tipo de cambio (datos fijos para la simulación)
    obtenerTipoCambio: function() {
        return {
            compra: TipoDeCampoBCCR.obtenerTipoCambioCompra(),
            venta: TipoDeCampoBCCR.obtenerTipoCambioVenta(),
            fecha: App.getCurrentDate()
        };
    },
    
    // Gestión de Clientes
    crearClienteFisico: function() {
        // Obtener los valores del formulario
        var nombre = document.getElementById('nombreFisico').value.trim();
        var categoria = document.getElementById('categoriaFisico').value;
        var telefono = document.getElementById('telefonoFisico').value.trim();
        var correo = document.getElementById('correoFisico').value.trim();
        var identificacion = document.getElementById('identificacionFisico').value.trim();
        var maxCuentas = document.getElementById('maxCuentasFisico').value;
        var fechaNacimiento = document.getElementById('fechaNacimientoFisico').value;
        
        // Validaciones básicas
        if (!nombre || !telefono || !correo || !identificacion || !maxCuentas || !fechaNacimiento) {
            alert('Por favor, complete todos los campos requeridos.');
            return;
        }
        if (!/^\d{8}$/.test(telefono)) {
            alert('El número de teléfono debe tener exactamente 8 dígitos.');
            return;
        }
        if (!/^[^@\s]+@[^@\s]+\.[^@\s]+$/.test(correo)) {
            alert('El correo electrónico no tiene un formato válido.');
            return;
        }
        if (!identificacion || isNaN(identificacion) || parseInt(identificacion) <= 0) {
            alert('La identificación debe ser un número positivo.');
            return;
        }
        if (!maxCuentas || isNaN(maxCuentas) || parseInt(maxCuentas) <= 0) {
            alert('El máximo de cuentas debe ser un número positivo.');
            return;
        }
        if (!fechaNacimiento) {
            alert('La fecha de nacimiento es obligatoria.');
            return;
        }
        // Crear objeto con los datos del cliente
        var datosCliente = {
            nombre: nombre,
            identificacion: identificacion,
            numTelefono: telefono,
            correoElectronico: correo,
            fechaNacimiento: fechaNacimiento,
            maxCuentas: parseInt(maxCuentas)
        };
        
        // Enviar solicitud al backend
        fetch("http://localhost:4567/clientes/fisico", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(datosCliente)
        })
        .then(response => response.json())
        .then(data => {
            if (data.status === 'SUCCESS') {
                alert("Cliente creado exitosamente.");
                App.displayMessage('mensajeClienteFisico', data.message);
                App.showElement('resultadoClienteFisico');
                // Resetear el formulario
                document.getElementById('formClienteFisico').reset();
            } else {
                alert('Error: ' + data.message);
            }
        })
        .catch(error => {
            //console.error('Error:', error);
            alert('Ocurrió un error al intentar crear el cliente. Por favor, inténtelo de nuevo.');
        });
    },
    
    // Gestión de Clientes
    // Gestión de Clientes
    crearClienteJuridico : function() {
        // Obtener los valores del formulario
        var nombre = document.getElementById('nombreJuridico').value.trim();
        var categoria = document.getElementById('categoriaJuridico').value;
        var telefono = document.getElementById('telefonoJuridico').value.trim();
        var correo = document.getElementById('correoJuridico').value.trim();
        var tipoNegocio = document.getElementById('tipoNegocio').value.trim();
        var cedulaJuridica = document.getElementById('cedulaJuridica').value.trim();
        var razonSocial = document.getElementById('razonSocial').value.trim();

        // Validaciones básicas
        if (!nombre || !telefono || !correo || !tipoNegocio || !cedulaJuridica || !razonSocial) {
            alert('Por favor, complete todos los campos requeridos.');
            return;
        }

        // Validación del teléfono
        if (!telefono.match(/^\d{8}$/)) {
            alert('El número de teléfono debe tener 8 dígitos.');
            return;
        }

        // Validación del correo electrónico
        if (!correo.match(/^[^@\s]+@[^@\s]+\.[^@\s]+$/)) {
            alert('El formato del correo electrónico no es válido.');
            return;
        }

        // Validación de cédula jurídica
        if (!cedulaJuridica.match(/^\d{10}$/)) {
            alert('La cédula jurídica debe tener 10 dígitos.');
            return;
        }

        // Crear objeto con los datos del cliente
        var datosCliente = {
            nombre: nombre,
            categoria: categoria,
            numTelefono: telefono,
            correoElectronico: correo,
            tipoNegocio: tipoNegocio,
            identificacion: parseInt(cedulaJuridica, 10),
            razonSocial: razonSocial
        };

        // Enviar solicitud al backend
        fetch("http://localhost:4567/clientes/juridico", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(datosCliente)
        })
        .then(response => response.json())
        .then(data => {
            if (data.status === 'SUCCESS') {
                var mensaje = data.message;
                App.displayMessage('mensajeClienteJuridico', mensaje);
                App.showElement('resultadoClienteJuridico');
                // Resetear el formulario
                document.getElementById('formClienteJuridico').reset();
            } else {
                alert('Error: ' + data.message);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Ocurrió un error al crear el cliente.');
        });
    },
    
    // Funciones de cambio de teléfono y correo
    // Función para validar la identificación del cliente al cambiar el número de teléfono
    validarIdentificacionParaTelefono: function() {
        var identificacion = document.getElementById('identificacionTelefonoCliente').value.trim();
    
        if (!identificacion) {
            alert('Por favor, ingrese la identificación del cliente.');
            return;
        }
    
        // Verificar si el cliente existe en el backend
        fetch(`http://localhost:4567/clientes/validarCliente/${identificacion}`)
            .then(response => response.json())
            .then(data => {
                if (data.status === 'SUCCESS') {
                    // Asegúrate de que los datos del cliente están correctamente dentro de la respuesta
                    var cliente = data.datos; // Cambiar de data a dataCliente
                    App.displayMessage('datosClienteTelefono', 
                        `Cliente encontrado. Nombre: ${cliente.nombre}, Teléfono actual: ${cliente.numTelefono}`);
                    App.showElement('infoClienteTelefono');
                    App.showElement('camposCambioTelefono');
                } else {
                    alert('Error: ' + data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Ocurrió un error al validar la identificación del cliente.');
            });
    },
    
    
    // Función para cambiar el número de teléfono de un cliente
    cambiarTelefono: function() {
        var identificacion = document.getElementById('identificacionTelefonoCliente').value.trim();
        var nuevoTelefono = document.getElementById('nuevoTelefono').value.trim();
    
        if (!identificacion || !nuevoTelefono) {
            alert('Por favor, complete todos los campos.');
            return;
        }
    
        // Validar el nuevo número de teléfono (8 dígitos)
        if (!/^\d{8}$/.test(nuevoTelefono)) {
            alert('El nuevo número de teléfono debe tener exactamente 8 dígitos.');
            return;
        }
    
        // Actualizar el teléfono del cliente en el backend
        fetch('http://localhost:4567/clientes/actualizarTelefono', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                identificacion: parseInt(identificacion),
                nuevoTelefono: nuevoTelefono
            })
        })
        .then(response => response.json())
        .then(data => {
            if (data.status === 'SUCCESS') {
                var mensaje = `Estimado usuario: ${data.datos.nombre}, usted ha cambiado el número de teléfono ${data.datos.telefonoAnterior} por el nuevo número ${nuevoTelefono}.`;
                App.displayMessage('mensajeCambioTelefono', mensaje);
                App.showElement('resultadoCambioTelefono');
            } else {
                alert('Error: ' + data.message);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Ocurrió un error al actualizar el número de teléfono.');
        });
    },
    

    // Función para validar la identificación del cliente al cambiar el correo electrónico
    validarIdentificacionParaCorreo: function() {
        var identificacion = document.getElementById('identificacionCorreoCliente').value.trim();
        
        if (!identificacion) {
            alert('Por favor, ingrese la identificación del cliente.');
            return;
        }

        // Verificar si el cliente existe en el backend
        fetch(`http://localhost:4567/clientes/validarCliente/${identificacion}`)
            .then(response => response.json())
            .then(data => {
                if (data.status === 'SUCCESS') {
                    var cliente = data.datos;
                    
                    // Guardar el correo actual en una variable sin mostrarlo
                    App.correoActualCliente = cliente.correoElectronico;

                    // Mostrar campos adicionales para cambiar correo
                    App.displayMessage('datosClienteCorreo', `Cliente encontrado. Nombre: ${cliente.nombre}, Correo actual: ${cliente.correoElectronico}`);
                    App.showElement('infoClienteCorreo');
                    App.showElement('camposCambioCorreo');
                } else {
                    alert('Error: ' + data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Ocurrió un error al validar la identificación del cliente.');
            });
    },

    // Función para cambiar el correo electrónico de un cliente
    cambiarCorreo: function() {
        var identificacion = document.getElementById('identificacionCorreoCliente').value.trim();
        var nuevoCorreo = document.getElementById('nuevoCorreo').value.trim();

        if (!identificacion || !nuevoCorreo) {
            alert('Por favor, complete todos los campos.');
            return;
        }

        // Actualizar el correo electrónico del cliente en el backend
        fetch('http://localhost:4567/clientes/cambiarCorreo', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                identificacion: parseInt(identificacion),
                nuevoCorreo: nuevoCorreo
            })
        })
            .then(response => response.json())
            .then(data => {
                if (data.status === 'SUCCESS') {
                    // Usar el correo guardado previamente para el mensaje
                    var mensaje = `Estimado usuario: ${data.datos.nombre}, usted ha cambiado la dirección de correo ${App.correoActualCliente} por ${nuevoCorreo}.`;
                    App.displayMessage('mensajeCambioCorreo', mensaje);
                    App.showElement('resultadoCambioCorreo');
                } else {
                    alert('Error: ' + data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Ocurrió un error al actualizar el correo electrónico.');
            });
    },


    

    // Gestión de Cuentas
    // Crear Cuenta
    validarCliente : function() {
        var identificacion = document.getElementById('identificacionCliente').value.trim();

        if (!identificacion) {
            alert('Por favor, ingrese la identificación del cliente.');
            return;
        }

        // Verificar si el cliente existe
        fetch(`http://localhost:4567/clientes/validarCliente/${identificacion}`)
            .then(response => response.json())
            .then(data => {
                if (data.status === 'SUCCESS') {
                    // Mostrar nombre del cliente
                    var cliente = data.datos;
                    App.displayMessage('infoClienteCuenta', `Estimado usuario ${cliente.nombre} por favor ingrese los siguientes datos para generar su nueva cuenta.`);
                    App.showElement('datosClienteCuenta');
                    App.showElement('camposCrearCuenta');
                } else {
                    alert('Error: ' + data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Ocurrió un error al validar el cliente.');
            });
    },

    // Crear Cuenta
    crearCuenta: function() {
        var identificacion = document.getElementById('identificacionCliente').value.trim();
        var saldoInicial = document.getElementById('depositoInicial').value.trim();
        var pin = document.getElementById('pinCuenta').value.trim();
        
        if (!saldoInicial || !pin) {
            alert('Por favor, complete todos los campos.');
            return;
        }

        if (parseFloat(saldoInicial) < 0) {
            alert('El saldo inicial no puede ser negativo.');
            return;
        }

        // Validar formato del PIN usando una expresión regular
        var pinRegex = /^(?=.*[A-Z])(?=.*\d)[A-Za-z\d]{6}$/;
        if (!pinRegex.test(pin)) {
            alert('El PIN debe tener exactamente 6 caracteres alfanuméricos, incluir al menos una letra mayúscula y un número.');
            return;
        }
        // Enviar solicitud al backend para crear la cuenta
        fetch("http://localhost:4567/cuentas/crearCuenta", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({identificacion: identificacion, saldoInicial: parseFloat(saldoInicial), pin: pin})
        })
        .then(response => response.json())
        .then(data => {
            if (data.status === 'SUCCESS') {
                // Mostrar mensaje de éxito con detalles de la cuenta
                App.displayMessage('mensajeCrearCuenta', data.message);
                App.showElement('resultadoCrearCuenta');
                // Ocultar los campos adicionales
                App.hideElement('camposCrearCuenta');
                // Resetear el formulario
                document.getElementById('formCrearCuenta').reset();
            } else {
                alert('Error: ' + data.message);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Ocurrió un error al crear la cuenta. Por favor, inténtelo de nuevo.');
        });
    },
    
    // Cambiar PIN
    // Validar Cuenta
    validarCuentaPin: function() {
        var numeroCuenta = document.getElementById('numeroCuenta').value.trim();

        if (!numeroCuenta) {
            alert('Por favor, ingrese el número de cuenta.');
            return;
        }

        // Verificar si la cuenta existe
        fetch(`http://localhost:4567/cuentas/validarCuenta/${numeroCuenta}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Error en la respuesta del servidor: ' + response.statusText);
                }
                return response.json();
            })
            .then(data => {
                if (!data) {
                    console.error('Respuesta del servidor es nula o indefinida');
                    return;
                }
                if (data.status === 'SUCCESS') {
                    alert("Respuesta completa del servidor:" + data.status);
                    alert("Mensaje del servidor: " + data.message);
                    // Asegúrate de que data.dataCuenta y miCliente tienen los datos esperados
                    App.displayMessage('datosCuentaPin', data.message + "\nPor favor, ingrese los siguientes datos para cambiar el PIN.");
                    alert("Datos mostrados.");
                    App.showElement('infoCuentaPin');
                    App.showElement('camposCambioPin');
                } else {
                    alert('Error: ' + data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Ocurrió un error al validar la cuenta.');
            });
    },

    // Cambiar PIN
    cambiarPin: function() {
        var numeroCuenta = document.getElementById('numeroCuenta').value.trim();
        var pinActual = document.getElementById('pinActual').value.trim();
        var nuevoPin = document.getElementById('nuevoPin').value.trim();

        if (!pinActual || !nuevoPin) {
            alert('Por favor, complete todos los campos.');
            return;
        }

        // Validar formato del nuevo PIN usando una expresión regular
        var pinRegex = /^(?=.*[A-Z])(?=.*\d)[A-Za-z\d]{6}$/;
        if (!pinRegex.test(nuevoPin)) {
            alert('El nuevo PIN debe tener exactamente 6 caracteres alfanuméricos, incluir al menos una letra mayúscula y un número.');
            return;
        }

        // Enviar solicitud al backend para cambiar el PIN
        var datosCambioPin = {
            numeroCuenta: numeroCuenta,
            pinActual: pinActual,
            nuevoPin: nuevoPin
        };

        fetch("http://localhost:4567/cuentas/cambiarPin", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(datosCambioPin)
        })
        .then(response => response.json())
        .then(data => {
            if (data.status === 'SUCCESS') {
                App.displayMessage('mensajeCambioPin', data.message);
                App.showElement('resultadoCambioPin');
                // Ocultar los campos adicionales
                App.hideElement('camposCambioPin');
                // Resetear el formulario
                document.getElementById('formCambiarPin').reset();
            } else {
                alert('Error: ' + data.message);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Ocurrió un error al cambiar el PIN. Por favor, inténtelo de nuevo.');
        });
    },

    // Eliminar cuenta
    validarCuentaParaEliminar : function() {
        var numeroCuenta = document.getElementById('numeroCuentaEliminar').value.trim();
        var pinCuenta = document.getElementById('pinCuentaEliminar').value.trim();

        if (!numeroCuenta || !pinCuenta) {
            alert('Por favor, ingrese todos los campos requeridos.');
            return;
        }

        // Realizar la solicitud para validar la cuenta
        fetch(`http://localhost:4567/cuentas/validarCuentaParaEliminar/${numeroCuenta}/${pinCuenta}`)
            .then(response => response.json())
            .then(data => {
                if (data.status === 'SUCCESS') {
                    // Extraer datos de la respuesta usando `datos`
                    var cuentaData = data.datos;
                    if (cuentaData) {
                        var cliente = cuentaData.nombreCompleto;
                        var saldo = cuentaData.saldo;
                        var mensaje = `Estimado usuario: ${cliente}, usted está a un paso de eliminar su cuenta ${numeroCuenta} cuyo saldo actual es de ${saldo}.\n¿Está seguro que desea eliminar esta cuenta?`;
                        //alert(mensaje);
                        App.displayMessage('mensajeCuentaEliminar', mensaje);
                        App.showElement('mensajeConfirmacionEliminacion');
                    } else {
                        alert("Error: Los datos de la cuenta no están disponibles.");
                    }
                } else {
                    alert('Error: ' + data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Ocurrió un error al validar la cuenta.');
            });
    },

    confirmarEliminacion : function() {
        var numeroCuenta = document.getElementById('numeroCuentaEliminar').value.trim();

        // Realizar la solicitud para eliminar la cuenta
        fetch(`http://localhost:4567/cuentas/eliminarCuenta/${numeroCuenta}`, {
            method: 'DELETE'
        })
            .then(response => response.json())
            .then(data => {
                if (data.status === 'SUCCESS') {
                    var saldo = data.datos.saldo; // Accedemos al saldo de la cuenta eliminada
                    var mensaje = saldo > 0.0
                        ? `La cuenta ha sido eliminada exitosamente. Por favor, tome el dinero del dispensador (${saldo}).`
                        : `La cuenta ha sido eliminada exitosamente.`;
            
                    App.displayMessage('mensajeResultadoEliminacion', mensaje);
                    App.showElement('resultadoEliminacionCuenta');
                    App.hideElement('mensajeConfirmacionEliminacion');
                    //App.hideElement('formEliminarCuenta');
                } else {
                    alert('Error: ' + data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Ocurrió un error al eliminar la cuenta.');
            });
    },

    cancelarEliminacion : function() {
        App.hideElement('mensajeConfirmacionEliminacion');
    },
    
    // Operaciones Financieras

    // Deposito en colones
    // Validar Cuenta para Depósito en Colones
    validarCuentaDepositoColones: function() {
        var numeroCuenta = document.getElementById('numeroCuentaDepositoColones').value.trim();
        
        if (!numeroCuenta) {
            alert('Por favor, ingrese el número de cuenta.');
            return;
        }
    
        // Limpiar mensajes previos
        App.displayMessage('mensajeValidacionCuentaDepositoColones', '');
    
        // Verificar si la cuenta existe
        fetch(`http://localhost:4567/cuentas/validarCuenta/${numeroCuenta}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Error en la respuesta del servidor: ' + response.statusText);
                }
                return response.json();
            })
            .then(data => {
                if (!data) {
                    console.error('Respuesta del servidor es nula o indefinida');
                    return;
                }
                if (data.status === 'SUCCESS') {
                    // Mostrar mensaje y campos adicionales
                    App.displayMessage('mensajeValidacionCuentaDepositoColones', data.message + "\nPor favor, ingrese el monto a depositar.");
                    App.showElement('mensajeValidacionCuentaDepositoColones');
                    App.showElement('camposDepositoColones');
                } else {
                    alert('Error: ' + data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Ocurrió un error al validar la cuenta.');
            });
    },


    // Realizar Depósito en Colones
    realizarDepositoColones: function() {
        var numeroCuenta = document.getElementById('numeroCuentaDepositoColones').value.trim();
        var monto = document.getElementById('montoDepositoColones').value.trim();
        
        if (!monto) {
            alert('Por favor, ingrese el monto del depósito.');
            return;
        }
    
        monto = parseInt(monto);
        if (isNaN(monto) || monto <= 0) {
            alert('El monto debe ser un número entero mayor a cero.');
            return;
        }
    
        // Realizar la petición al backend
        var data = {
            numeroCuenta: numeroCuenta,
            monto: monto
        };
    
        fetch('http://localhost:4567/transacciones/depositoColones', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Error en la respuesta del servidor: ' + response.statusText);
                }
                return response.json();
            })
            .then(data => {
                if (!data) {
                    console.error('Respuesta del servidor es nula o indefinida');
                    return;
                }
                if (data.status === 'SUCCESS') {
                    //alert('Depósito realizado con éxito.');
                    App.displayMessage('mensajeDepositoColones', data.message);
                    App.showElement('resultadoDepositoColones');
                    //App.hideElement('formDepositoColones');
                } else {
                    alert('Error: ' + data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Ocurrió un error al realizar el depósito.');
            });
    },

    // Deposito en dolares
    // Validar Cuenta para Depósito en Dólares
    validarCuentaDepositoDolares: function() {
        var numeroCuenta = document.getElementById('numeroCuentaDepositoDolares').value.trim();
        
        if (!numeroCuenta) {
            alert('Por favor, ingrese el número de cuenta.');
            return;
        }

        // Limpiar mensajes previos
        App.displayMessage('mensajeValidacionCuentaDepositoDolares', '');

        // Verificar si la cuenta existe
        fetch(`http://localhost:4567/cuentas/validarCuenta/${numeroCuenta}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Error en la respuesta del servidor: ' + response.statusText);
                }
                return response.json();
            })
            .then(data => {
                if (!data) {
                    console.error('Respuesta del servidor es nula o indefinida');
                    return;
                }
                if (data.status === 'SUCCESS') {
                    // Mostrar mensaje y campos adicionales
                    App.displayMessage('mensajeValidacionCuentaDepositoDolares', data.message + "\nPor favor, ingrese el monto a depositar.");
                    App.showElement('mensajeValidacionCuentaDepositoDolares');
                    App.showElement('camposDepositoDolares');
                } else {
                    alert('Error: ' + data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Ocurrió un error al validar la cuenta.');
            });
    },

    // Realizar Depósito en Dólares
    realizarDepositoDolares: function() {
        var numeroCuenta = document.getElementById('numeroCuentaDepositoDolares').value.trim();
        var monto = document.getElementById('montoDepositoDolares').value.trim();
        
        if (!monto) {
            alert('Por favor, ingrese el monto del depósito.');
            return;
        }

        monto = parseInt(monto);
        if (isNaN(monto) || monto <= 0) {
            alert('El monto debe ser un número entero mayor a cero.');
            return;
        }

        // Realizar la petición al backend
        var data = {
            numeroCuenta: numeroCuenta,
            monto: monto
        };

        fetch('http://localhost:4567/transacciones/depositoDolares', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Error en la respuesta del servidor: ' + response.statusText);
                }
                return response.json();
            })
            .then(data => {
                if (!data) {
                    console.error('Respuesta del servidor es nula o indefinida');
                    return;
                }
                if (data.status === 'SUCCESS') {
                    App.displayMessage('mensajeDepositoDolares', data.message);
                    App.showElement('resultadoDepositoDolares');
                } else {
                    alert('Error: ' + data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Ocurrió un error al realizar el depósito.');
            });
    },


    // Retiro en Colones
    // Validar Cuenta y PIN para Retiro en Colones
    validarCuentaRetiroColones: function() {
        var numeroCuenta = document.getElementById('numeroCuentaRetiroColones').value.trim();
        var pin = document.getElementById('pinRetiroColones').value.trim();

        if (!numeroCuenta || !pin) {
            alert('Por favor, ingrese el número de cuenta y el PIN.');
            return;
        }

        // Realizar la petición al backend para validar la cuenta y el PIN
        fetch(`http://localhost:4567/cuentas/validarCuentaParaRetiro/${numeroCuenta}/${pin}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Error en la respuesta del servidor: ' + response.statusText);
                }
                return response.json();
            })
            .then(data => {
                if (!data) {
                    console.error('Respuesta del servidor es nula o indefinida');
                    return;
                }
                if (data.status === 'SUCCESS') {
                    //alert('Cuenta y PIN validados correctamente.');

                    // Enviar la palabra clave por SMS
                    App.enviarPalabraClaveRetiroColones(numeroCuenta);

                    // Mostrar el campo para ingresar la palabra clave
                    App.showElement('verificacionPalabraRetiroColones');
                    // Ocultar el formulario de validación de cuenta y PIN
                    //App.hideElement('formRetiroColones');
                } else {
                    alert('Error: ' + data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Ocurrió un error al validar la cuenta y el PIN.');
            });
    },

    // Paso e: Enviar Palabra Clave por SMS y mostrar mensaje al usuario
    enviarPalabraClaveRetiroColones: function(numeroCuenta) {
        // Realizar la petición al backend para enviar la palabra clave
        fetch(`http://localhost:4567/transacciones/enviarPalabraVerificacion/${numeroCuenta}`)
            .then(response => response.json())
            .then(data => {
                if (data.status === 'SUCCESS') {
                    // Mostrar mensaje al usuario con el nombre completo del dueño
                    var mensaje = `Estimado usuario ${data.nombreCompleto}, se ha enviado una palabra por mensaje de texto, por favor revise sus mensajes y proceda a digitar la palabra enviada.`;
                    App.displayMessage('mensajeEnvioPalabra', mensaje);
                    App.showElement('mensajeEnvioPalabraRetiroColones');
                    // Mostrar el campo para ingresar la palabra clave
                    App.showElement('verificacionPalabraRetiroColones');
                    // Ocultar el campo de PIN
                    //App.hideElement('camposPinRetiroColones');
                } else {
                    alert('Error al enviar la palabra clave: ' + data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Ocurrió un error al enviar la palabra clave.');
            });
    },

    // Paso g: Validar Palabra Clave para Retiro en Colones
    validarPalabraClaveRetiroColones: function() {
        var palabraIngresada = document.getElementById('palabraClaveRetiroColones').value.trim();
        var numeroCuenta = document.getElementById('numeroCuentaRetiroColones').value.trim();

        if (!palabraIngresada) {
            alert('Por favor, ingrese la palabra clave.');
            return;
        }

        // Limpiar mensajes previos
        App.displayMessage('mensajeValidacionPalabraRetiroColones', '');

        // Realizar la petición al backend para validar la palabra clave
        fetch(`http://localhost:4567/transacciones/validarPalabraClave/${numeroCuenta}/${palabraIngresada}`)
            .then(response => response.json())
            .then(data => {
                if (data.status === 'SUCCESS') {
                    // Mostrar el campo para ingresar el monto de retiro
                    App.showElement('campoMontoRetiroColones');
                    // Ocultar la sección de verificación de palabra clave
                    //App.hideElement('verificacionPalabraRetiroColones');
                    //App.hideElement('mensajeEnvioPalabraRetiroColones');
                } else {
                    App.displayMessage('mensajeValidacionPalabraRetiroColones', data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                App.displayMessage('mensajeValidacionPalabraRetiroColones', 'Ocurrió un error al validar la palabra clave.');
            });
    },

    // Paso j: Realizar Retiro en Colones
    realizarRetiroColones: function() {
        var numeroCuenta = document.getElementById('numeroCuentaRetiroColones').value.trim();
        var pin = document.getElementById('pinRetiroColones').value.trim();
        var palabraIngresada = document.getElementById('palabraClaveRetiroColones').value.trim();
        var montoRetiro = document.getElementById('montoRetiroColones').value.trim();

        if (!montoRetiro) {
            alert('Por favor, ingrese el monto a retirar.');
            return;
        }

        montoRetiro = parseInt(montoRetiro);
        if (isNaN(montoRetiro) || montoRetiro <= 0) {
            alert('El monto debe ser un número entero mayor a cero.');
            return;
        }

        var data = {
            numeroCuenta: numeroCuenta,
            pin: pin,
            palabraIngresada: palabraIngresada,
            montoRetiro: montoRetiro
        };

        // Realizar la petición al backend para realizar el retiro
        fetch('http://localhost:4567/transacciones/retiroColones', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Error en la respuesta del servidor: ' + response.statusText);
                }
                return response.json();
            })
            .then(data => {
                if (!data) {
                    console.error('Respuesta del servidor es nula o indefinida');
                    return;
                }
                if (data.status === 'SUCCESS') {
                    // Mostrar mensaje de confirmación
                    App.displayMessage('mensajeRetiroColones', data.message);
                    App.showElement('resultadoRetiroColones');
                    // Ocultar el campo de monto de retiro
                    //App.hideElement('campoMontoRetiroColones');
                } else {
                    alert('Error: ' + data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Ocurrió un error al realizar el retiro.');
            });
    },

    // Retiro en Dolares

    // Validar Cuenta y PIN para Retiro en Dólares
    validarCuentaRetiroDolares: function() {
        var numeroCuenta = document.getElementById('numeroCuentaRetiroDolares').value.trim();
        var pin = document.getElementById('pinRetiroDolares').value.trim();

        if (!numeroCuenta || !pin) {
            alert('Por favor, ingrese el número de cuenta y el PIN.');
            return;
        }

        // Limpiar mensajes previos
        App.displayMessage('mensajeValidacionCuentaRetiroDolares', '');

        // Verificar si la cuenta y el PIN son correctos
        fetch(`http://localhost:4567/cuentas/validarCuentaParaRetiro/${numeroCuenta}/${pin}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Error en la respuesta del servidor: ' + response.statusText);
                }
                return response.json();
            })
            .then(data => {
                if (!data) {
                    console.error('Respuesta del servidor es nula o indefinida');
                    return;
                }
                if (data.status === 'SUCCESS') {
                    // Enviar palabra clave y mostrar mensaje al usuario
                    App.enviarPalabraClaveRetiroDolares(numeroCuenta);
                } else {
                    App.displayMessage('mensajeValidacionCuentaRetiroDolares', data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                App.displayMessage('mensajeValidacionCuentaRetiroDolares', 'Ocurrió un error al validar la cuenta y el PIN.');
            });
    },

    // Enviar Palabra Clave por SMS y mostrar mensaje al usuario
    enviarPalabraClaveRetiroDolares: function(numeroCuenta) {
        // Realizar la petición al backend para enviar la palabra clave
        fetch(`http://localhost:4567/transacciones/enviarPalabraVerificacion/${numeroCuenta}`)
            .then(response => response.json())
            .then(data => {
                if (data.status === 'SUCCESS') {
                    // Mostrar mensaje al usuario con el nombre completo del dueño
                    var mensaje = `Estimado usuario, se ha enviado una palabra por mensaje de texto, por favor revise sus mensajes y proceda a digitar la palabra enviada.`;
                    App.displayMessage('mensajeEnvioPalabraDolares', mensaje);
                    App.showElement('verificacionPalabraRetiroDolares');
                    // Ocultar el formulario de cuenta y PIN
                    //App.hideElement('formRetiroDolares');
                } else {
                    App.displayMessage('mensajeValidacionCuentaRetiroDolares', 'Error al enviar la palabra clave: ' + data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                App.displayMessage('mensajeValidacionCuentaRetiroDolares', 'Ocurrió un error al enviar la palabra clave.');
            });
    },

    // Validar Palabra Clave para Retiro en Dólares
    validarPalabraClaveRetiroDolares: function() {
        var palabraIngresada = document.getElementById('palabraClaveRetiroDolares').value.trim();
        var numeroCuenta = document.getElementById('numeroCuentaRetiroDolares').value.trim();

        if (!palabraIngresada) {
            alert('Por favor, ingrese la palabra clave.');
            return;
        }

        // Limpiar mensajes previos
        App.displayMessage('mensajeValidacionPalabraRetiroDolares', '');

        // Realizar la petición al backend para validar la palabra clave
        fetch(`http://localhost:4567/transacciones/validarPalabraClave/${numeroCuenta}/${palabraIngresada}`)
            .then(response => response.json())
            .then(data => {
                if (data.status === 'SUCCESS') {
                    // Mostrar el campo para ingresar el monto de retiro
                    App.showElement('campoMontoRetiroDolares');
                    // Ocultar la sección de verificación de palabra clave
                    //App.hideElement('verificacionPalabraRetiroDolares');
                } else {
                    App.displayMessage('mensajeValidacionPalabraRetiroDolares', data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                App.displayMessage('mensajeValidacionPalabraRetiroDolares', 'Ocurrió un error al validar la palabra clave.');
            });
    },

    // Realizar Retiro en Dólares
    realizarRetiroDolares: function() {
        var numeroCuenta = document.getElementById('numeroCuentaRetiroDolares').value.trim();
        var pin = document.getElementById('pinRetiroDolares').value.trim();
        var palabraIngresada = document.getElementById('palabraClaveRetiroDolares').value.trim();
        var montoRetiro = document.getElementById('montoRetiroDolares').value.trim();

        if (!montoRetiro) {
            alert('Por favor, ingrese el monto a retirar.');
            return;
        }

        montoRetiro = parseInt(montoRetiro);
        if (isNaN(montoRetiro) || montoRetiro <= 0) {
            alert('El monto debe ser un número entero mayor a cero.');
            return;
        }

        var data = {
            numeroCuenta: numeroCuenta,
            pin: pin,
            palabraIngresada: palabraIngresada,
            montoRetiro: montoRetiro
        };

        // Realizar la petición al backend para realizar el retiro
        fetch('http://localhost:4567/transacciones/retiroDolares', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Error en la respuesta del servidor: ' + response.statusText);
                }
                return response.json();
            })
            .then(data => {
                if (!data) {
                    console.error('Respuesta del servidor es nula o indefinida');
                    return;
                }
                if (data.status === 'SUCCESS') {
                    // Mostrar mensaje de confirmación
                    App.displayMessage('mensajeRetiroDolares', data.message);
                    App.showElement('resultadoRetiroDolares');
                    // Ocultar el campo de monto de retiro
                    App.hideElement('campoMontoRetiroDolares');
                } else {
                    App.displayMessage('mensajeErrorRetiroDolares', data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                App.displayMessage('mensajeErrorRetiroDolares', 'Ocurrió un error al realizar el retiro.');
            });
    },

    // Trasnferencia entre cuentas de un mismo dueño

    // Validar Cuenta Origen y PIN
    validarCuentaOrigen: function() {
        var numeroCuentaOrigen = document.getElementById('numeroCuentaOrigen').value.trim();
        var pinCuentaOrigen = document.getElementById('pinCuentaOrigen').value.trim();

        if (!numeroCuentaOrigen || !pinCuentaOrigen) {
            alert('Por favor, ingrese el número de cuenta origen y el PIN.');
            return;
        }

        // Limpiar mensajes previos
        App.displayMessage('mensajeValidacionCuentaOrigen', '');

        // Verificar si la cuenta y el PIN son correctos
        fetch(`http://localhost:4567/cuentas/validarCuentaParaRetiro/${numeroCuentaOrigen}/${pinCuentaOrigen}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Error en la respuesta del servidor: ' + response.statusText);
                }
                return response.json();
            })
            .then(data => {
                if (!data) {
                    console.error('Respuesta del servidor es nula o indefinida');
                    return;
                }
                if (data.status === 'SUCCESS') {
                    // Enviar palabra clave y mostrar mensaje al usuario
                    App.enviarPalabraClaveTransferencia(numeroCuentaOrigen);
                } else {
                    App.displayMessage('mensajeValidacionCuentaOrigen', data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                App.displayMessage('mensajeValidacionCuentaOrigen', 'Ocurrió un error al validar la cuenta origen y el PIN.');
            });
    },

    // Enviar Palabra Clave por SMS y mostrar mensaje al usuario
    enviarPalabraClaveTransferencia: function(numeroCuentaOrigen) {
        // Realizar la petición al backend para enviar la palabra clave
        fetch(`http://localhost:4567/transacciones/enviarPalabraVerificacion/${numeroCuentaOrigen}`)
            .then(response => response.json())
            .then(data => {
                if (data.status === 'SUCCESS') {
                    // Mostrar mensaje al usuario con el nombre completo del dueño
                    var mensaje = `Estimado usuario, se ha enviado una palabra por mensaje de texto, por favor revise sus mensajes y proceda a digitar la palabra enviada.`;
                    App.displayMessage('mensajeEnvioPalabraTransferencia', mensaje);
                    App.showElement('verificacionPalabraTransferencia');
                    // Ocultar el formulario de cuenta origen y PIN
                    //App.hideElement('formTransferencia');
                } else {
                    App.displayMessage('mensajeValidacionCuentaOrigen', 'Error al enviar la palabra clave: ' + data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                App.displayMessage('mensajeValidacionCuentaOrigen', 'Ocurrió un error al enviar la palabra clave.');
            });
    },

    // Validar Palabra Clave para Transferencia
    validarPalabraClaveTransferencia: function() {
        var palabraIngresada = document.getElementById('palabraClaveTransferencia').value.trim();
        var numeroCuentaOrigen = document.getElementById('numeroCuentaOrigen').value.trim();

        if (!palabraIngresada) {
            alert('Por favor, ingrese la palabra clave.');
            return;
        }

        // Limpiar mensajes previos
        App.displayMessage('mensajeValidacionPalabraTransferencia', '');

        // Realizar la petición al backend para validar la palabra clave
        fetch(`http://localhost:4567/transacciones/validarPalabraClave/${numeroCuentaOrigen}/${palabraIngresada}`)
            .then(response => response.json())
            .then(data => {
                if (data.status === 'SUCCESS') {
                    // Mostrar los campos para ingresar el monto y la cuenta destino
                    App.showElement('camposMontoYDestino');
                    // Ocultar la sección de verificación de palabra clave
                    //App.hideElement('verificacionPalabraTransferencia');
                } else {
                    App.displayMessage('mensajeValidacionPalabraTransferencia', data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                App.displayMessage('mensajeValidacionPalabraTransferencia', 'Ocurrió un error al validar la palabra clave.');
            });
    },

    // Validar Cuenta Destino
    validarCuentaDestino: function() {
        var numeroCuentaDestino = document.getElementById('numeroCuentaDestino').value.trim();
        var numeroCuentaOrigen = document.getElementById('numeroCuentaOrigen').value.trim();

        if (!numeroCuentaDestino) {
            alert('Por favor, ingrese el número de cuenta destino.');
            return;
        }

        // Limpiar mensajes previos
        App.displayMessage('mensajeValidacionCuentaDestino', '');

        // Verificar si la cuenta destino existe y es del mismo dueño
        fetch(`http://localhost:4567/cuentas/validarCuentaDestino/${numeroCuentaOrigen}/${numeroCuentaDestino}`)
            .then(response => response.json())
            .then(data => {
                if (data.status === 'SUCCESS') {
                    // Mostrar el botón para realizar la transferencia
                    App.showElement('botonTransferir');
                } else {
                    App.displayMessage('mensajeValidacionCuentaDestino', data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                App.displayMessage('mensajeValidacionCuentaDestino', 'Ocurrió un error al validar la cuenta destino.');
            });
    },

    // Realizar Transferencia
    realizarTransferencia: function() {
        var numeroCuentaOrigen = document.getElementById('numeroCuentaOrigen').value.trim();
        var pinCuentaOrigen = document.getElementById('pinCuentaOrigen').value.trim();
        var montoTransferencia = document.getElementById('montoTransferencia').value.trim();
        var numeroCuentaDestino = document.getElementById('numeroCuentaDestino').value.trim();

        if (!montoTransferencia) {
            alert('Por favor, ingrese el monto a transferir.');
            return;
        }

        montoTransferencia = parseInt(montoTransferencia);
        if (isNaN(montoTransferencia) || montoTransferencia <= 0) {
            alert('El monto debe ser un número entero mayor a cero.');
            return;
        }

        var data = {
            numeroCuentaOrigen: numeroCuentaOrigen,
            pinCuentaOrigen: pinCuentaOrigen,
            montoTransferencia: montoTransferencia,
            numeroCuentaDestino: numeroCuentaDestino
        };

        // Realizar la petición al backend para realizar la transferencia
        fetch('http://localhost:4567/transacciones/transferencia', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Error en la respuesta del servidor: ' + response.statusText);
                }
                return response.json();
            })
            .then(data => {
                if (!data) {
                    console.error('Respuesta del servidor es nula o indefinida');
                    return;
                }
                if (data.status === 'SUCCESS') {
                    // Mostrar mensaje de confirmación
                    App.displayMessage('mensajeTransferencia', data.message);
                    App.showElement('resultadoTransferencia');
                    // Ocultar los campos de monto y cuenta destino
                    App.hideElement('camposMontoYDestino');
                    App.hideElement('botonTransferir');
                } else {
                    App.displayMessage('mensajeErrorTransferencia', data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                App.displayMessage('mensajeErrorTransferencia', 'Ocurrió un error al realizar la transferencia.');
            });
    },
    

    // Consultas y Reportes
    // 1. Consultar Transacciones
    // Validar Cuenta y PIN para Consulta de Transacciones
    validarCuentaTransacciones: function() {
        var numeroCuenta = document.getElementById('numeroCuentaTransacciones').value.trim();
        var pin = document.getElementById('pinCuentaTransacciones').value.trim();

        if (!numeroCuenta || !pin) {
            alert('Por favor, ingrese el número de cuenta y el PIN.');
            return;
        }

        // Realizar la petición al backend para validar la cuenta y el PIN
        fetch(`http://localhost:4567/cuentas/validarCuentaParaRetiro/${numeroCuenta}/${pin}`)
            .then(response => response.json())
            .then(data => {
                if (data.status === 'SUCCESS') {
                    // Enviar la palabra clave por SMS
                    App.enviarPalabraClaveTransacciones(numeroCuenta);

                    // Mostrar el campo para ingresar la palabra clave
                    App.showElement('verificacionPalabraTransacciones');
                } else {
                    alert('Error: ' + data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Ocurrió un error al validar la cuenta y el PIN.');
            });
    },

    // Enviar Palabra Clave por SMS para Consulta de Transacciones
    enviarPalabraClaveTransacciones: function(numeroCuenta) {
        fetch(`http://localhost:4567/transacciones/enviarPalabraVerificacion/${numeroCuenta}`)
            .then(response => response.json())
            .then(data => {
                if (data.status === 'SUCCESS') {
                    var mensaje = `Estimado usuario, se ha enviado una palabra por mensaje de texto, por favor revise sus mensajes y proceda a digitar la palabra enviada.`;
                    App.displayMessage('mensajeEnvioPalabraTransacciones', mensaje);
                } else {
                    alert('Error al enviar la palabra clave: ' + data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Ocurrió un error al enviar la palabra clave.');
            });
    },

    // Validar Palabra Clave y Consultar Transacciones
    validarPalabraClaveTransacciones: function() {
        var numeroCuenta = document.getElementById('numeroCuentaTransacciones').value.trim();
        var pin = document.getElementById('pinCuentaTransacciones').value.trim();
        var palabraClave = document.getElementById('palabraClaveTransacciones').value.trim();

        if (!palabraClave) {
            alert('Por favor, ingrese la palabra clave recibida.');
            return;
        }

        // Validar Palabra Clave
        fetch(`http://localhost:4567/transacciones/validarPalabraClave/${numeroCuenta}/${palabraClave}`)
            .then(response => response.json())
            .then(data => {
                if (data.status === 'SUCCESS') {
                    // Preparar datos para la consulta de transacciones
                    var requestData = {
                        numeroCuenta: numeroCuenta,
                        pin: pin,
                        palabraIngresada: palabraClave
                    };

                    // Realizar la consulta de transacciones
                    return fetch('http://localhost:4567/cuentas/transacciones', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify(requestData)
                    });
                } else {
                    alert(data.message);
                }
            })
            .then(response => response.json())
            .then(data => {
                if (data.status === 'SUCCESS') {
                    // Mostrar las transacciones en la tabla
                    var transacciones = data.datos.transacciones;
                    var tbody = document.querySelector('#tablaTransacciones tbody');
                    tbody.innerHTML = ''; // Limpiar transacciones previas

                    transacciones.forEach(function(transaccion) {
                        var fila = document.createElement('tr');

                        var tipo = document.createElement('td');
                        tipo.textContent = transaccion.tipo;
                        fila.appendChild(tipo);

                        var monto = document.createElement('td');
                        monto.textContent = parseFloat(transaccion.monto).toFixed(2);
                        fila.appendChild(monto);

                        var fecha = document.createElement('td');
                        fecha.textContent = transaccion.fecha;
                        fila.appendChild(fecha);

                        var comision = document.createElement('td');
                        comision.textContent = transaccion.comision ? 'Sí' : 'No';
                        fila.appendChild(comision);

                        tbody.appendChild(fila);
                    });

                    // Mostrar la sección de resultados
                    App.showElement('resultadoTransacciones');
                } else {
                    alert(data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Ocurrió un error al consultar las transacciones.');
            });
    },


    // 2. Consultar Tipo de Cambio de Compra

     // Consultar Tipo de Cambio de Compra
    consultarTipoCambioCompra: function() {
        fetch("http://localhost:4567/tipoCambio/compra")
            .then(response => response.json())
            .then(data => {
                if (data.status === 'SUCCESS') {
                    const tipoCambio = data.datos;
                    App.displayMessage('mensajeTipoCambioCompra', `El tipo de cambio de compra es: ${tipoCambio.tipoCambio} colones. Fecha: ${tipoCambio.fecha}`);
                    App.showElement('resultadoTipoCambioCompra');
                } else {
                    alert('Error al obtener el tipo de cambio de compra: ' + data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Ocurrió un error al consultar el tipo de cambio de compra.');
            });
    },

    // Consultar Tipo de Cambio de Venta
    consultarTipoCambioVenta: function() {
        fetch("http://localhost:4567/tipoCambio/venta")
            .then(response => response.json())
            .then(data => {
                if (data.status === 'SUCCESS') {
                    const tipoCambio = data.datos;
                    App.displayMessage('mensajeTipoCambioVenta', `El tipo de cambio de venta es: ${tipoCambio.tipoCambio} colones. Fecha: ${tipoCambio.fecha}`);
                    App.showElement('resultadoTipoCambioVenta');
                } else {
                    alert('Error al obtener el tipo de cambio de venta: ' + data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Ocurrió un error al consultar el tipo de cambio de venta.');
            });
    },

    // 4. Consultar Saldo Actual en Colones

    // Consultar Saldo Actual en Colones
    consultarSaldoColones: function() {
        var numeroCuenta = document.getElementById('numeroCuentaColones').value.trim();
        var pin = document.getElementById('pinCuentaColones').value.trim();

        if (!numeroCuenta || !pin) {
            alert('Por favor, ingrese el número de cuenta y el PIN.');
            return;
        }

        // Limpiar mensajes previos
        App.displayMessage('mensajeSaldoColones', '');
        App.hideElement('resultadoSaldoColones');

        // Hacer la petición al servidor
        fetch(`http://localhost:4567/cuentas/validarCuentaParaEliminar/${numeroCuenta}/${pin}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Error en la respuesta del servidor: ' + response.statusText);
                }
                return response.json();
            })
            .then(data => {
                if (!data) {
                    console.error('Respuesta del servidor es nula o indefinida');
                    return;
                }
                if (data.status === 'SUCCESS') {
                    var nombreCompleto = data.datos.nombreCompleto;
                    var saldo = parseFloat(data.datos.saldo).toFixed(2);

                    var mensaje = "Estimado usuario: " + nombreCompleto+ ", el saldo actual de su cuenta " + numeroCuenta + " es de " + saldo + " colones.";

                    App.displayMessage('mensajeSaldoColones', mensaje);
                    App.showElement('resultadoSaldoColones');
                } else {
                    App.displayMessage('mensajeSaldoColones', data.message);
                    App.showElement('resultadoSaldoColones');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                App.displayMessage('mensajeSaldoColones', 'Ocurrió un error al consultar el saldo.');
                App.showElement('resultadoSaldoColones');
            });
    },
    
    // 5. Consultar Saldo Actual en Dólares

        // Consultar Saldo Actual en Dólares
        consultarSaldoDolares: function() {
            var numeroCuenta = document.getElementById('numeroCuentaDolares').value.trim();
            var pin = document.getElementById('pinCuentaDolares').value.trim();
    
            if (!numeroCuenta || !pin) {
                alert('Por favor, ingrese el número de cuenta y el PIN.');
                return;
            }
    
            // Limpiar mensajes previos
            App.displayMessage('mensajeSaldoDolares', '');
            App.hideElement('resultadoSaldoDolares');
    
            // Hacer la petición al servidor
            fetch(`http://localhost:4567/cuentas/consultarSaldoDolares/${numeroCuenta}/${pin}`)
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Error en la respuesta del servidor: ' + response.statusText);
                    }
                    return response.json();
                })
                .then(data => {
                    if (!data) {
                        console.error('Respuesta del servidor es nula o indefinida');
                        return;
                    }
                    if (data.status === 'SUCCESS') {

                        App.displayMessage('mensajeSaldoDolares', data.datos);
                        App.showElement('resultadoSaldoDolares');
                    } else {
                        App.displayMessage('mensajeSaldoDolares', data.message);
                        App.showElement('resultadoSaldoDolares');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    App.displayMessage('mensajeSaldoDolares', 'Ocurrió un error al consultar el saldo en dólares.');
                    App.showElement('resultadoSaldoDolares');
                });
        },

    // Consultar Estado de Cuenta
    consultarEstadoCuenta: function() {
        var numeroCuenta = document.getElementById('numeroCuentaEstado').value.trim();
        var pin = document.getElementById('pinCuentaEstado').value.trim();

        if (!numeroCuenta || !pin) {
            alert('Por favor, ingrese el número de cuenta y el PIN.');
            return;
        }

        // Limpiar mensajes previos y ocultar resultados anteriores
        App.hideElement('resultadoEstadoCuenta');
        App.displayMessage('datosCuentaEstado', '');

        // Realizar la petición al servidor
        fetch(`http://localhost:4567/cuentas/estadoCuenta/${numeroCuenta}/${pin}`)
            .then(response => response.json())
            .then(data => {
                if (data.status === 'SUCCESS') {
                                        // Mostrar la información del estado de cuenta
                    var datos = data.datos;

                    // Formatear los montos y tipo de cambio
                    var saldoColones = parseFloat(datos.saldo).toFixed(2);
                    var saldoDolares = parseFloat(datos.saldoEnDolares).toFixed(2);
                    var tipoCambioCompra = parseFloat(datos.tipoCambioCompra).toFixed(2);
                    var fechaTipoCambio = datos.fechaTipoCambio;

                    var infoCuenta = `
                        <strong>Cliente:</strong><br>
                        Nombre: ${datos.nombreCompleto}<br>
                        Identificación: ${datos.identificacion}<br>
                        Número de Teléfono: ${datos.numeroTelefono}<br>
                        Correo: ${datos.correoElectronico}<br><br>
                        <strong>Cuenta:</strong><br>
                        Número de Cuenta: ${datos.numeroCuenta}<br>
                        Saldo en Colones: ${saldoColones} ₡<br>
                        Saldo en Dólares: ${saldoDolares} $<br>
                        Estatus: ${datos.estatus}<br>
                        Fecha de Creación: ${datos.fechaCreacion}<br><br>
                        <em>Para esta conversión se utilizó el tipo de cambio del dólar -precio de compra- según el BCCR, el tipo de cambio de compra del dólar de hoy (${fechaTipoCambio}) es de: ${tipoCambioCompra} ₡</em>
                    `;
                    App.displayMessage('datosCuentaEstado', infoCuenta);

                    App.showElement('resultadoEstadoCuenta');
                } else {
                    alert(data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Ocurrió un error al consultar el estado de cuenta.');
            });
    },

    // 7. Consultar Estatus de una Cuenta

    // Consultar Estatus de una Cuenta
    consultarEstatusCuenta: function() {
        var numeroCuenta = document.getElementById('numeroCuentaEstatus').value.trim();

        if (!numeroCuenta) {
            alert('Por favor, ingrese el número de cuenta.');
            return;
        }

        // Limpiar mensajes previos
        App.displayMessage('datosCuenta', '');
        App.hideElement('resultadoCuenta');

        // Realizar la petición al servidor
        fetch(`http://localhost:4567/cuentas/consultarEstatus/${numeroCuenta}`)
            .then(response => response.json())
            .then(data => {
                if (data.status === 'SUCCESS') {
                    var mensaje = data.message;
                    App.displayMessage('datosCuenta', mensaje);
                    App.showElement('resultadoCuenta');
                } else {
                    alert(data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Ocurrió un error al consultar el estatus de la cuenta.');
            });
    },
    // 8. Consultar Cuentas de un Cliente

    // Consultar Cuentas de un Cliente
    listarCuentasDeCliente : function() {
        var identificacionCliente = document.getElementById('identificacionClienteCuentas').value.trim();

        if (!identificacionCliente) {
            alert('Por favor, ingrese la identificación del cliente.');
            return;
        }

        // Limpiar mensajes previos y ocultar resultados anteriores
        App.hideElement('resultadoCliente');
        App.displayMessage('datosCliente', '');
        var tbody = document.querySelector('#tablaCuentasCliente tbody');
        tbody.innerHTML = '';

        // Realizar la petición al servidor
        fetch(`http://localhost:4567/clientes/cuentas/${identificacionCliente}`)
            .then(response => response.json())
            .then(data => {
                if (data.status === 'SUCCESS') {
                    // Mostrar la información del cliente
                    var cliente = data.datos.cliente;

                    var infoCliente = `
                        <strong>Nombre:</strong> ${cliente.nombre}<br>
                        <strong>Identificación:</strong> ${cliente.identificacion}<br>
                        <strong>Número de Teléfono:</strong> ${cliente.numTelefono}<br>
                        <strong>Correo Electrónico:</strong> ${cliente.correoElectronico}<br>
                    `;
                    App.displayMessage('datosCliente', infoCliente);

                    // Mostrar las cuentas del cliente
                    var cuentas = data.datos.cuentas;
                    cuentas.forEach(function(cuenta) {
                        var fila = document.createElement('tr');

                        var numeroCuenta = document.createElement('td');
                        numeroCuenta.textContent = cuenta.numeroCuenta;
                        fila.appendChild(numeroCuenta);

                        var saldo = document.createElement('td');
                        saldo.textContent = parseFloat(cuenta.saldo).toFixed(2) + " colones";
                        fila.appendChild(saldo);

                        tbody.appendChild(fila);
                    });

                    App.showElement('resultadoCliente');
                } else {
                    alert(data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Ocurrió un error al consultar las cuentas del cliente.');
            });
    },


    // 9. Listar Clientes Registrados
        // Listar Clientes Registrados
    listarClientesRegistrados: function() {
        // Limpiar la tabla antes de llenarla
        var tbody = document.querySelector('#tablaClientesRegistrados tbody');
        tbody.innerHTML = '';

        // Realizar la petición al servidor
        fetch('http://localhost:4567/clientes/listar')
            .then(response => response.json())
            .then(data => {
                if (data.status === 'SUCCESS') {
                    var clientes = data.datos;

                    clientes.forEach(function(cliente) {
                        var fila = document.createElement('tr');

                        // Nombre del Cliente
                        var nombreCelda = document.createElement('td');
                        nombreCelda.textContent = cliente.nombre;
                        fila.appendChild(nombreCelda);

                        // Tipo de Cliente
                        var tipoCelda = document.createElement('td');
                        tipoCelda.textContent = cliente.tipo;
                        fila.appendChild(tipoCelda);

                        // Identificación
                        var identificacionCelda = document.createElement('td');
                        identificacionCelda.textContent = cliente.identificacion;
                        fila.appendChild(identificacionCelda);

                        // Correo Electrónico
                        var correoCelda = document.createElement('td');
                        correoCelda.textContent = cliente.correoElectronico;
                        fila.appendChild(correoCelda);

                        // Teléfono
                        var telefonoCelda = document.createElement('td');
                        telefonoCelda.textContent = cliente.numTelefono;
                        fila.appendChild(telefonoCelda);

                        tbody.appendChild(fila);
                    });

                    // Mostrar la sección de clientes registrados
                    App.showElement('consultarClientesRegistrados');
                } else {
                    alert(data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Ocurrió un error al listar los clientes registrados.');
            });
    },

    // 10. Listar Cuentas Registradas

    /**
     * Función para listar todas las cuentas registradas ordenadas por saldo ascendente.
     */
    listarCuentasRegistradas: function() {
        // Limpiar la tabla antes de llenarla
        var tbody = document.querySelector('#tablaCuentasRegistradas tbody');
        tbody.innerHTML = '';

        // Realizar la petición al servidor
        fetch('http://localhost:4567/cuentas/listar')
            .then(response => response.json())
            .then(data => {
                if (data.status === 'SUCCESS') {
                    var cuentas = data.datos;

                    if (cuentas.length === 0) {
                        // Si no hay cuentas, mostrar un mensaje en la tabla
                        var fila = document.createElement('tr');
                        var celda = document.createElement('td');
                        celda.setAttribute('colspan', '4'); // Actualizar el colspan
                        celda.textContent = 'No hay cuentas registradas.';
                        celda.style.textAlign = 'center';
                        fila.appendChild(celda);
                        tbody.appendChild(fila);
                    } else {
                        cuentas.forEach(function(cuenta) {
                            var fila = document.createElement('tr');

                            // Número de Cuenta
                            var numeroCuentaCelda = document.createElement('td');
                            numeroCuentaCelda.textContent = cuenta.numeroCuenta;
                            fila.appendChild(numeroCuentaCelda);

                            // Estatus
                            var estatusCelda = document.createElement('td');
                            estatusCelda.textContent = cuenta.estatus;
                            fila.appendChild(estatusCelda);

                            // Nombre del Dueño o Apoderado Generalísimo
                            var nombreDueñoCelda = document.createElement('td');
                            nombreDueñoCelda.textContent = cuenta.nombreUsuario;
                            fila.appendChild(nombreDueñoCelda);

                            // Saldo Actual
                            var saldoCelda = document.createElement('td');
                            saldoCelda.textContent = cuenta.saldo.toFixed(2); // Formatear a dos decimales
                            fila.appendChild(saldoCelda);

                            tbody.appendChild(fila);
                        });
                    }

                    // Mostrar la sección de cuentas registradas
                    App.showElement('consultarCuentasRegistradas');
                } else {
                    alert(data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Ocurrió un error al listar las cuentas registradas.');
            });
    },

// Función auxiliar para obtener el cliente a partir del número de cuenta

    // Agrega aquí funciones adicionales para las demás funcionalidades
};

// Inicializar la aplicación cuando el DOM esté listo
document.addEventListener('DOMContentLoaded', App.init);

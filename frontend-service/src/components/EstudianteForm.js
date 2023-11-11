import React, { useState, useEffect } from 'react';
import axios from 'axios';

const RegistroEstudiante = () => {
    const [estudiante, setEstudiante] = useState({
        rut: '',
        apellidos: '',
        nombres: '',
        fechaNacimiento: '',
        tipoColegioProcedencia: '',
        metodoPago: '',
        nombreColegio: '',
        anoEgresoColegio: '',
        numeroCuotas: 1
    });

    useEffect(() => {
        actualizarMaximoCuotas();
    }, [estudiante]);

    const actualizarMaximoCuotas = () => {
        let maxCuotas = 1;
        switch (estudiante.tipoColegioProcedencia) {
            case 'Municipal': maxCuotas = 10; break;
            case 'Subvencionado': maxCuotas = 7; break;
            case 'Privado': maxCuotas = 4; break;
            default: break;
        }
        document.getElementById('numeroCuotas').setAttribute('max', maxCuotas.toString());

        if (estudiante.metodoPago === 'Al Contado') {
            setEstudiante(prev => ({ ...prev, numeroCuotas: 1 }));
            document.getElementById('labelCuotas').style.display = 'none';
            document.getElementById('numeroCuotas').style.display = 'none';
            document.getElementById('numeroCuotas').required = false;  // Desactivamos el requerimiento
        } else {
            document.getElementById('labelCuotas').style.display = 'inline';
            document.getElementById('numeroCuotas').style.display = 'inline';
            document.getElementById('numeroCuotas').required = true;  // Reactivamos el requerimiento
        }
    };

    const handleChange = (e) => {
        setEstudiante({ ...estudiante, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await axios.post('http://localhost:8080/Estudiante/crearEstudiante', estudiante);
            window.location.href = '/Estudiante/registroEstudiante';
        } catch (error) {
            console.error(error);
        }
    };
    return (
        <div style={{ backgroundColor: '#f2f2f2', padding: '20px', fontFamily: 'Arial, sans-serif' }}>
            <h2 style={{ textAlign: 'center', color: '#333', borderBottom: '2px solid #444', paddingBottom: '10px', marginBottom: '20px' }}>
                Formulario de Registro de Estudiante
            </h2>
            <div style={{ width: '33%', margin: '0 auto' }}>
                <form onSubmit={handleSubmit} style={{ backgroundColor: '#fff', padding: '20px', borderRadius: '8px', boxShadow: '0px 0px 10px rgba(0, 0, 0, 0.1)' }}>

                    <div style={{ marginBottom: '15px' }}>
                        <label htmlFor="rut" style={{ display: 'block', marginBottom: '5px' }}>RUT (sin puntos y con guion):</label>
                        <input type="text" id="rut" name="rut" pattern="\d{7,8}-[\dkK]" title="Formato: XXXXXXXX-X o XXXXXXX-X" required onChange={handleChange} style={{ width: '100%', padding: '10px', borderRadius: '4px', border: '1px solid #ccc' }} />
                    </div>

                    <div style={{ marginBottom: '15px' }}>
                        <label htmlFor="apellidos" style={{ display: 'block', marginBottom: '5px' }}>Apellidos:</label>
                        <input type="text" id="apellidos" name="apellidos" pattern="[A-Za-z\s]+" title="Solo letras permitidas" required onChange={handleChange} style={{ width: '100%', padding: '10px', borderRadius: '4px', border: '1px solid #ccc' }} />
                    </div>

                    <div style={{ marginBottom: '15px' }}>
                        <label htmlFor="nombres" style={{ display: 'block', marginBottom: '5px' }}>Nombres:</label>
                        <input type="text" id="nombres" name="nombres" pattern="[A-Za-z\s]+" title="Solo letras permitidas" required onChange={handleChange} style={{ width: '100%', padding: '10px', borderRadius: '4px', border: '1px solid #ccc' }} />
                    </div>

                    <div style={{ marginBottom: '15px' }}>
                        <label htmlFor="fechaNacimiento" style={{ display: 'block', marginBottom: '5px' }}>Fecha de Nacimiento:</label>
                        <input type="date" id="fechaNacimiento" name="fechaNacimiento" required onChange={handleChange} style={{ width: '100%', padding: '10px', borderRadius: '4px', border: '1px solid #ccc' }} />
                    </div>

                    <div style={{ marginBottom: '15px' }}>
                        <label htmlFor="tipoColegioProcedencia" style={{ display: 'block', marginBottom: '5px' }}>Tipo de Colegio de Procedencia:</label>
                        <select id="tipoColegioProcedencia" name="tipoColegioProcedencia" required onChange={handleChange} style={{ width: '100%', padding: '10px', borderRadius: '4px', border: '1px solid #ccc' }}>
                            <option value="">Seleccione</option>
                            <option value="Municipal">Municipal</option>
                            <option value="Subvencionado">Subvencionado</option>
                            <option value="Privado">Privado</option>
                        </select>
                    </div>

                    <div style={{ marginBottom: '15px' }}>
                        <label htmlFor="metodoPago" style={{ display: 'block', marginBottom: '5px' }}>Método de Pago:</label>
                        <select id="metodoPago" name="metodoPago" required onChange={handleChange} style={{ width: '100%', padding: '10px', borderRadius: '4px', border: '1px solid #ccc' }}>
                            <option value="">Seleccione</option>
                            <option value="Al Contado">Al Contado</option>
                            <option value="En Cuotas">En Cuotas</option>
                        </select>
                    </div>

                    <div style={{ marginBottom: '15px' }}>
                        <label htmlFor="nombreColegio" style={{ display: 'block', marginBottom: '5px' }}>Nombre del Colegio:</label>
                        <input type="text" id="nombreColegio" name="nombreColegio" required onChange={handleChange} style={{ width: '100%', padding: '10px', borderRadius: '4px', border: '1px solid #ccc' }} />
                    </div>

                    <div style={{ marginBottom: '15px' }}>
                        <label htmlFor="anoEgresoColegio" style={{ display: 'block', marginBottom: '5px' }}>Año de Egreso del Colegio:</label>
                        <input type="number" id="anoEgresoColegio" name="anoEgresoColegio" required onChange={handleChange} style={{ width: '100%', padding: '10px', borderRadius: '4px', border: '1px solid #ccc' }} />
                    </div>

                    <div style={{ marginBottom: '15px' }}>
                        <label htmlFor="numeroCuotas" id="labelCuotas" style={{ display: 'block', marginBottom: '5px' }}>Número de Cuotas:</label>
                        <input type="number" id="numeroCuotas" name="numeroCuotas" min="1" required onChange={handleChange} style={{ width: '100%', padding: '10px', borderRadius: '4px', border: '1px solid #ccc' }} />
                    </div>

                    <div style={{ textAlign: 'center', marginTop: '20px' }}>
                        <input
                            type="submit"
                            value="Registrar"
                            style={{ backgroundColor: '#007BFF', color: '#fff', padding: '10px 20px', borderRadius: '4px', border: 'none', cursor: 'pointer', fontSize: '16px', transition: '0.3s' }}
                            onMouseOver={(e) => e.currentTarget.style.backgroundColor = '#0056b3'}
                            onMouseOut={(e) => e.currentTarget.style.backgroundColor = '#007BFF'}
                        /></div>
                </form>
            </div>
        </div>
    );

};

export default RegistroEstudiante;

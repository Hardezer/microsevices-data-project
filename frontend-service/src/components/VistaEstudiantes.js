import React, { useState, useEffect } from 'react';
import axios from 'axios';

const VistaEstudiantes = () => {
    const [estudiantes, setEstudiantes] = useState([]);

    useEffect(() => {
        async function fetchEstudiantes() {
            try {
                const response = await axios.get('http://localhost:8080/Estudiante');
                setEstudiantes(response.data);
            } catch (error) {
                console.error("Error obteniendo los estudiantes:", error);
            }
        }

        fetchEstudiantes();
    }, []);

    const cellStyle = {
        padding: '10px 15px',
        border: '1px solid #eaecee'
    };

    return (
        <div style={{ fontFamily: 'Arial, sans-serif', margin: '20px' }}>
            <h2 style={{ marginBottom: '25px', borderBottom: '2px solid #34495e', paddingBottom: '10px' }}>Listado de Estudiantes</h2>
            <table style={{
                width: '100%',
                marginTop: '20px',
                borderCollapse: 'collapse',
                boxShadow: '0px 0px 20px rgba(0,0,0,0.1)'
            }}>
                <thead>
                <tr style={{ backgroundColor: '#2c3e50', color: '#ecf0f1' }}>
                    <th style={cellStyle}>RUT</th>
                    <th style={cellStyle}>Apellidos</th>
                    <th style={cellStyle}>Nombres</th>
                    <th style={cellStyle}>Fecha de Nacimiento</th>
                    <th style={cellStyle}>Tipo Colegio Procedencia</th>
                    <th style={cellStyle}>Método de Pago</th>
                    <th style={cellStyle}>Nombre del Colegio</th>
                    <th style={cellStyle}>Año Egreso Colegio</th>
                    <th style={cellStyle}>Número Cuotas</th>
                </tr>
                </thead>
                <tbody>
                {estudiantes.map((estudiante, index) => (
                    <tr key={estudiante.rut} style={index % 2 === 0 ? {backgroundColor: '#f4f6f6'} : null}>
                        <td style={cellStyle}>{estudiante.rut}</td>
                        <td style={cellStyle}>{estudiante.apellidos}</td>
                        <td style={cellStyle}>{estudiante.nombres}</td>
                        <td style={cellStyle}>{estudiante.fechaNacimiento}</td>
                        <td style={cellStyle}>{estudiante.tipoColegioProcedencia}</td>
                        <td style={cellStyle}>{estudiante.metodoPago}</td>
                        <td style={cellStyle}>{estudiante.nombreColegio}</td>
                        <td style={cellStyle}>{estudiante.anoEgresoColegio}</td>
                        <td style={cellStyle}>{estudiante.numeroCuotas}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );

};

export default VistaEstudiantes;

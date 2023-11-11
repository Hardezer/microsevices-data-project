// components/BuscarCuotas.js

import React, { useState } from 'react';
import axios from 'axios';

const BuscarCuotas = () => {
    const [rut, setRut] = useState('');
    const [cuotas, setCuotas] = useState([]);

    const buscarCuotasPorRut = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post('http://localhost:8080/Cuota/byRut', { rut });
            setCuotas(response.data);
            console.log(response.data);

        } catch (error) {
            console.error("Hubo un error buscando las cuotas:", error);
        }
    };

    const pagarCuota = async (cuotaId) => {
        try {
            await axios.post(`http://localhost:8080/Cuota/pagarCuota?cuotaId=${cuotaId}`);

            //actualizar cuotas después de pagar
            buscarCuotasPorRut({ preventDefault: () => {} });
        } catch (error) {
            console.error("Error al pagar cuota:", error);
        }
    };

    const cellStyle = {
        padding: '10px 15px',
        border: '1px solid #eaecee'
    };

    return (
        <div style={{ fontFamily: 'Arial, sans-serif', margin: '20px' }}>
            <h1 style={{ marginBottom: '25px', borderBottom: '2px solid #34495e', paddingBottom: '10px' }}>Buscar Cuotas por RUT</h1>
            <div style={{ border: '1px solid #eaecee', padding: '20px', boxShadow: '0px 0px 20px rgba(0,0,0,0.1)', marginBottom: '20px', display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
                <form onSubmit={buscarCuotasPorRut} style={{ width: '100%', display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
                    <div style={{ marginBottom: '15px', width: '33%' }}>
                        <label htmlFor="rut" style={{ display: 'block', marginBottom: '5px' }}>Ingrese el RUT del estudiante:</label>
                        <input type="text" id="rut" value={rut} onChange={(e) => setRut(e.target.value)} required style={{ width: '100%', padding: '10px', border: '1px solid #eaecee' }} />
                    </div>
                    <button type="submit" style={{ backgroundColor: '#2c3e50', color: '#ecf0f1', padding: '10px 15px', border: 'none', cursor: 'pointer' }}>Buscar</button>
                </form>
            </div>


            {cuotas.length > 0 && (
                <div>
                    <h2 style={{ marginBottom: '20px' }}>Resultados</h2>
                    <table style={{
                        width: '100%',
                        borderCollapse: 'collapse',
                        boxShadow: '0px 0px 20px rgba(0,0,0,0.1)'
                    }}>
                        <thead>
                        <tr style={{ backgroundColor: '#2c3e50', color: '#ecf0f1' }}>
                            <th style={cellStyle}>RUT Estudiante</th>
                            <th style={cellStyle}>Número de Cuota</th>
                            <th style={cellStyle}>Fecha de Vencimiento</th>
                            <th style={cellStyle}>Monto de Cuota</th>
                            <th style={cellStyle}>Estado de Pago</th>
                            <th style={cellStyle}>Acciones</th>
                        </tr>
                        </thead>
                        <tbody>
                        {cuotas.map((cuota, index) => (
                            <tr key={cuota.id} style={index % 2 === 0 ? {backgroundColor: '#f4f6f6'} : null}>
                                <td style={cellStyle}>{cuota.rutEstudiante || 'N/A'}</td>

                                <td style={cellStyle}>{cuota.numeroCuota}</td>
                                <td style={cellStyle}>{cuota.fechaVencimiento}</td>
                                <td style={cellStyle}>{cuota.montoCuota}</td>
                                <td style={cellStyle} className={cuota.estadoPago === 'Atrasada' ? 'text-danger' : (cuota.estadoPago === 'Pagada' ? 'text-success' : 'text-warning')}>
                                    {cuota.estadoPago}
                                </td>
                                <td style={cellStyle}>
                                    {cuota.estadoPago !== 'Pagada' && (
                                        <button onClick={() => pagarCuota(cuota.id)} style={{ backgroundColor: '#2c3e50', color: '#ecf0f1', padding: '10px 15px', border: 'none', cursor: 'pointer' }}>Pagar</button>
                                    )}
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            )}
        </div>
    );
};

export default BuscarCuotas;

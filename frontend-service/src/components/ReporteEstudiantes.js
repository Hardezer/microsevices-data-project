import React, { useState, useEffect } from 'react';
import axios from 'axios';

const ReporteEstudiantes = () => {
    const [reportes, setReportes] = useState([]);

    useEffect(() => {
        async function fetchReportes() {
            try {
                const response = await axios.get('http://localhost:8080/Estudiante/reporte');

                // Verificación adicional para asegurarse de que la respuesta es un array y no contiene valores nulos
                if (Array.isArray(response.data) && !response.data.includes(null)) {
                    setReportes(response.data);
                } else {
                    console.error("La respuesta de la API no es un array o contiene un objeto null:", response.data);
                }
            } catch (error) {
                console.error("Error obteniendo los reportes:", error);
            }
        }

        fetchReportes();
    }, []);

    const cellStyle = {
        padding: '10px 15px',
        border: '1px solid #eaecee'
    };

    return (
        <div className="container-sm my-4">
            <h1>Reporte de Estudiantes</h1>
            <table className="table table-bordered">
                <thead className="thead-dark">
                <tr>
                    <th style={cellStyle}>RUT Estudiante</th>
                    <th style={cellStyle}>Apellidos</th>
                    <th style={cellStyle}>Nombres</th>
                    <th style={cellStyle}>Examenes rendidos</th>
                    <th style={cellStyle}>Promedio exámenes</th>
                    <th style={cellStyle}>Costo Arancel</th>
                    <th style={cellStyle}>Tipo Pago</th>
                    <th style={cellStyle}>Nro. Cuotas</th>
                    <th style={cellStyle}>Nro. Cuotas Pagadas</th>
                    <th style={cellStyle}>Total Pagado</th>
                    <th style={cellStyle}>Ultimo pago</th>
                    <th style={cellStyle}>Saldo por pagar</th>
                    <th style={cellStyle}>Cuotas Atrasadas</th>
                </tr>
                </thead>
                <tbody>
                {reportes.map((reporte) => (
                    reporte ? (
                        <tr key={reporte.rut}>
                            <td style={cellStyle}>{reporte.rut}</td>
                            <td style={cellStyle}>{reporte.apellidos}</td>
                            <td style={cellStyle}>{reporte.nombres}</td>
                            <td style={cellStyle}>{reporte.examenesRendidos}</td>
                            <td style={cellStyle}>{reporte.promedioExamenes}</td>
                            <td style={cellStyle}>{reporte.costoArancel}</td>
                            <td style={cellStyle}>{reporte.tipoPago}</td>
                            <td style={cellStyle}>{reporte.numeroCuotas}</td>
                            <td style={cellStyle}>{reporte.cuotasPagadas}</td>
                            <td style={cellStyle}>{reporte.totalPagado}</td>
                            <td style={cellStyle}>{reporte.ultimoPago}</td>
                            <td style={cellStyle}>{reporte.saldoPorPagar}</td>
                            <td style={cellStyle}>{reporte.cuotasAtrasadas}</td>
                        </tr>
                    ) : null
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default ReporteEstudiantes;

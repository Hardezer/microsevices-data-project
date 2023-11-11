// ImportarNotas.js

import React, { useState } from 'react';
import axios from 'axios';

const ImportarNotas = () => {
    const [file, setFile] = useState(null);
    const [message, setMessage] = useState(null);
    const [error, setError] = useState(null);

    const handleFileChange = (event) => {
        setFile(event.target.files[0]);
    };

    const handleSubmit = async (event) => {
        event.preventDefault();

        const formData = new FormData();
        formData.append('file', file);

        try {
            const response = await axios.post('http://localhost:8080/Examen/importarNotas', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            });

            setMessage(response.data.message);
            setError(null);
        } catch (e) {
            setError('Error al importar el archivo.');
            setMessage(null);
        }
    };

    return (
        <div className="container">
            <h1>Importar Notas de Exámenes desde Archivo CSV</h1>
            <form onSubmit={handleSubmit}>
                <input type="file" accept=".csv" required onChange={handleFileChange} />
                <button type="submit">Importar CSV</button>
            </form>
            {message && <p className="alert alert-success">{message}</p>}
            {error && <p className="alert alert-danger">{error}</p>}
        </div>
    );
};

export default ImportarNotas;

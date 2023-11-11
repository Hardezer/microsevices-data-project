// App.js
import React from 'react';
import './App.css';
import { BrowserRouter as Router, Route, Routes, Link } from 'react-router-dom';
import RegistroEstudiante from './components/EstudianteForm';
import VistaEstudiantes from './components/VistaEstudiantes';
import BuscarCuotas from './components/BuscarCuotas';
import ImportarNotas from './components/ImportarNotas';
import ReporteEstudiantes from './components/ReporteEstudiantes';

function App() {
    return (
        <Router>
            <div className="App">

                {/* Navegación */}
                <nav>
                    <Link to="/">Inicio</Link> |
                    <Link to="/registro-estudiante">Registro Estudiante</Link> |
                    <Link to="/ver-estudiantes">Ver Estudiantes</Link> |
                    <Link to="/buscar-cuotas">Buscar Cuotas</Link> |
                    <Link to="/importar-notas">Importar Notas</Link> |
                    <Link to="/reporte-estudiantes">Reporte de Estudiantes</Link>
                </nav>

                {/* Rutas */}
                <Routes>
                    <Route path="/registro-estudiante" element={<RegistroEstudiante />} />
                    <Route path="/ver-estudiantes" element={<VistaEstudiantes />} />
                    <Route path="/buscar-cuotas" element={<BuscarCuotas />} />
                    <Route path="/importar-notas" element={<ImportarNotas />} />
                    <Route path="/reporte-estudiantes" element={<ReporteEstudiantes />} />
                    <Route path="/" element={
                        <div>
                            <h1>Bienvenido al inicio</h1>
                            <p>Selecciona una opción de arriba para navegar.</p>
                        </div>
                    } />
                </Routes>
            </div>
        </Router>
    );
}

export default App;

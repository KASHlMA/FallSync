from flask import Flask, request, jsonify
from flask_sqlalchemy import SQLAlchemy
import os

# Configuración básica
app = Flask(__name__)
basedir = os.path.abspath(os.path.dirname(__file__))
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///' + os.path.join(basedir, 'fallsync.db')
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False

db = SQLAlchemy(app)

# ---------------------------------------------------------
# MODELO DE BASE DE DATOS (Coincide con tu Data Class)
# ---------------------------------------------------------
class Registro(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    descripcion = db.Column(db.String(100), nullable=False)
    fecha = db.Column(db.String(20), nullable=False)

    def to_dict(self):
        return {
            'id': self.id,
            'descripcion': self.descripcion,
            'fecha': self.fecha
        }

# Crear la base de datos si no existe
with app.app_context():
    db.create_all()

# ---------------------------------------------------------
# RUTAS DE LA API
# ---------------------------------------------------------

# 1. GET: Obtener todos los registros (Para tu HomeScreen/RegistrosScreen)
@app.route('/registros', methods=['GET'])
def get_registros():
    registros = Registro.query.all()
    # Convertimos la lista de objetos a JSON
    return jsonify([reg.to_dict() for reg in registros]), 200

# 2. POST: Crear nuevo registro (Multipart/Form-data)
@app.route('/registros', methods=['POST'])
def create_registro():
    # Nota: Al ser Multipart, usamos request.form, no request.json
    descripcion = request.form.get('descripcion')
    fecha = request.form.get('fecha')

    if not descripcion or not fecha:
        return jsonify({'error': 'Faltan datos'}), 400

    nuevo_registro = Registro(descripcion=descripcion, fecha=fecha)
    db.session.add(nuevo_registro)
    db.session.commit()

    return jsonify(nuevo_registro.to_dict()), 201

# 3. PUT: Actualizar registro (Multipart/Form-data)
@app.route('/registros/<int:id>', methods=['PUT'])
def update_registro(id):
    registro = Registro.query.get(id)
    if not registro:
        return jsonify({'error': 'Registro no encontrado'}), 404

    # Actualizamos solo si envían el dato
    registro.descripcion = request.form.get('descripcion', registro.descripcion)
    registro.fecha = request.form.get('fecha', registro.fecha)

    db.session.commit()
    return jsonify(registro.to_dict()), 200

# 4. DELETE: Borrar registro
@app.route('/registros/<int:id>', methods=['DELETE'])
def delete_registro(id):
    registro = Registro.query.get(id)
    if not registro:
        return jsonify({'error': 'Registro no encontrado'}), 404

    db.session.delete(registro)
    db.session.commit()
    return jsonify({'mensaje': 'Registro eliminado'}), 200

# Ejecutar servidor
if __name__ == '__main__':
    # host='0.0.0.0' permite que tu celular vea el servidor en la misma red Wi-Fi
    app.run(debug=True, host='0.0.0.0', port=5000)
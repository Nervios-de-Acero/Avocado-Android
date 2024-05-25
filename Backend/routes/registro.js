const express = require('express')
const router = express.Router()
const bcrypt = require('bcrypt')
const db = require('../conection')
const { checkSchema, validationResult} = require('express-validator')
const validaciones = require('../utils/validacionesRegistro')

router.post('/', checkSchema(validaciones), (req, res) => {
  if(Object.keys(req.body).length === 0 || typeof req.body.password === 'undefined' || typeof req.body.email === 'undefined' || typeof req.body.nombreCompleto === 'undefined' || typeof req.body.usuario === 'undefined'){
    res.status(400).send('Bad request. Campos incorrectos')
    return
} 
  const resValidaciones = validationResult(req).array()

  if(resValidaciones.length > 0){
    res.send({
      success: false,
      message: "Error en registro. Campos inválidos",
      content: resValidaciones
    })
    return
  }

  // lógica de registro
  const email = req.body.email,
        nombreCompleto = req.body.nombreCompleto,
        usuario = req.body.usuario,
        pass = bcrypt.hashSync(req.body.password, 12);


  db.query(`CALL sp_registro(?,?,?,?,?);`, [email, nombreCompleto, usuario, pass, 0], function (error, results){
    if(error){
      res.status(error.errno === 1644 ? 200 : 500).json({
        success: false,
        message: error.message,
        content: ''
      })
      return
    } else {
      res.send({
        success: true,
        message: '¡Usuario registrado exitosamente!',
        content: ''
      })
    }
  })
})

module.exports = router
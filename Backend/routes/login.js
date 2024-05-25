const express = require('express')
const router = express.Router()
const db = require('../conection')
const bcrypt = require('bcrypt')
const { checkSchema, validationResult } = require('express-validator')
const validaciones = require('../utils/validacionesLogin')

// Iniciar sesión
router.post('/', checkSchema(validaciones), (req, res) => {

  if (Object.keys(req.body).length === 0 || typeof req.body.password === 'undefined' || typeof req.body.email === 'undefined') {
    res.status(400).send('Bad request. Campos incorrectos')
    return
  }

  const resValidaciones = validationResult(req).array()

  if (resValidaciones.length > 0) {
    res.send({
      success: false,
      message: "Error al iniciar sesión. Campos inválidos",
      content: resValidaciones
    })
    return
  }

  const pass = req.body.password;
  db.query(`CALL sp_getUsuario(?);`, [req.body.email], async function (error, results) {
      if(error){
        res.send({
          success: false,
          message: error.message,
          content: ''
        })
        return
      } else {
        //Verificar contraseña
        const user = results[0][0]
        console.log(user)

        const validPass = await bcrypt.compare(pass, user.contraseña)
        if(!validPass){
          res.send({
            success: false,
            message: 'Usuario o contraseña incorrectos',
            content: ''
          })
          return
        } else {
          res.send({
            success: true,
            message: "Sesión iniciada correctamente",
            content: ''
          })
        }
        
      }
  })
})

//#region test endpoint
// Crear hash para usuarios de prueba
// router.post('/hash', (req, res) => {
//   console.log(req.body)
//   const hash = bcrypt.hashSync(req.body.contraseña, 12)
//   res.send({
//     pass: req.body.contraseña,
//     hash
//   })
// })
//#endregion


module.exports = router
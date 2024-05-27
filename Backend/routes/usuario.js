const express = require('express')
const router = express.Router()
const db = require('../conection')
const bcrypt = require('bcrypt')
const { checkSchema, validationResult } = require('express-validator')
const validaciones = require('../utils/validacionesPerfil')
const validacionesPass = require('../utils/validacionesPassword')

router.get('/getUsuario/:email', (req, res) => {
  db.query(`CALL sp_getUsuario(?)`, [req.params.email], function (error, results) {
    if (error) {
      res.status(error.errno === 1644 ? 200 : 500)
        .json({
          success: false,
          message: error.message,
          content: ''
        })
    } else {
      const user = results[0][0]
      delete user.contraseña
      delete user.isAdmin
      res.status(200).json({
        success: false,
        message: '',
        content: user
      })
    }
  })
})

router.put('/actualizarPerfil', checkSchema(validaciones), (req, res) => {
  const resValidaciones = validationResult(req).array(),
        nombreCompleto = req.body.nombreCompleto || null,
        usuario = req.body.usuario || null,
        email = req.body.email;

  if (!req.body.email) {
    res.status(400).json('Email obligatorio')
    return
  }

  if (resValidaciones.length > 0) {
    res.send({
      success: false,
      message: 'Campos inválidos',
      content: resValidaciones
    })
    return
  }

  db.query(`CALL sp_actualizarPerfil(?, ?, NULL, ?, NULL);`, [email, nombreCompleto, usuario], function (error, results) {
    if (error) {
      res.status(error.errno === 1644 ? 200 : 500).json({
        success: false,
        message: error.message
      })
    }
    else {
      res.status(200).json({
        success: true,
        message: 'Se actualizaron los datos correctamente'
      })
    }
  })
return
})

router.put('/modificarPassword', checkSchema(validacionesPass), (req, res) =>{
  const resValidaciones = validationResult(req).array();
  const pass = req.body.password
  const nuevoPass = req.body.nuevoPassword
  const email = req.body.email

  // if(!email || !pass || !nuevoPass){
  //   res.status(400).json('Error. Faltan campos obligatorios')
  //   return
  // }
  // if(resValidaciones.length > 0){
  //   res.send({
  //     success: false,
  //     message: 'Campos inválidos',
  //     result: resValidaciones
  //   })
  //   return
  // }

  //comprobar usuario
    //si es inexistente, error
  //comparar contraseña
    // si es incorrecta, error
  // modificar contraseña
    //mensaje de éxito

    try {
      db.query(`CALL sp_getUsuario(?);`,[email], function(error, results){
        if(error){
          throw new Error(error)
        } else {
          const resultado = results[0][0]
          if(!bcrypt.compare(pass, resultado.contraseña)){
            throw new Error('La contraseña es incorrecta')
          } else {
            const hasheado = bcrypt.hashSync(nuevoPass, 12)
            db.query(`CALL sp_actualizarPerfil(?, NULL, NULL, NULL, ?);`, [email, hasheado], function(error, results){
              if(error){
                throw new Error(error)
              } else {
                res.status(200).json({
                  success: true,
                  message: 'Contraseña actualizada correctamente'
                })
                return
              }
            })
          }
        } })
      }catch (error) {
        res.status(error.errno === 1644 ? 200 : 500).json({
          success: false,
          message: error.message
        })
  }
})

router.delete('/eliminar', (req, res) => {
  const email = req.body.email
  console.log(email)
  if (typeof email === 'undefined') {
    res.status(400).json('No se encontró el email')
    return
  }
  db.query(`DELETE FROM usuarios WHERE email = '${email}'`, function (error, results) {
    if (error) {
      res.send(error)
    }
    else {
      req.session.destroy(err => {
        if (err) {
          res.send({
            success: false,
            message: err
          })
          return
        }
      })
      res.send({
        success: true,
        message: 'Usuario eliminado correctamente'
      })
    }
  })

})

//#region Deshabilitados

// router.put('/actualizarImagen', (req, res)=>{
//   if(!req.body.imagen || !req.body.email){
//     res.status(400).json('Error. Imagen y mail obligatorios.')
//     return
//   }
// db.query(`UPDATE usuarios SET imagen = '${req.body.imagen}' WHERE email = '${req.body.email}'`, function(error, results){
//   if(error){
//     res.send({
//       success: false,
//       message: error
//     })
//     return
//   } else {
//     res.send({
//       success: true,
//       message: 'Imagen actualizada'
//     })
//   }
// })
// })

//#endregion

module.exports = router
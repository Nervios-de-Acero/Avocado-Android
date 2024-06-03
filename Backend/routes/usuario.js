const express = require('express')
const router = express.Router()
const db = require('../conection')
const bcrypt = require('bcrypt')
const { checkSchema, validationResult } = require('express-validator')
const validaciones = require('../utils/validacionesPerfil')
const validacionesPass = require('../utils/validacionesPassword')
const funcionesCloudinary = require('../utils/cloudinaryFunctions')

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
  const pass = req.body.password;
  const nuevoPass = req.body.nuevoPassword;
  const email = req.body.email;

  if(!email || !pass || !nuevoPass){

    res.status(400).json('Error. Faltan campos obligatorios');
    return;
  }

  if(resValidaciones.length > 0){

    res.send({
      success: false,
      message: 'Campos inválidos',
      result: resValidaciones,
    });

    return;
  }

  try{

    db.query(`CALL sp_getUsuario('${email}')`, (error, results) =>{

      if(error){

        console.log('as')
        res.send({
          success: false,
          message: error.sqlMessage,
        });
        
        return;
      } else {

        const resultado = results[0][0];

        if(bcrypt.compareSync(pass, resultado.contraseña)){

          db.query(`CALL sp_actualizarPerfil('${email}', null, null, null, '${bcrypt.hashSync(nuevoPass, 12)}')`, function(error, results){
            
            if(error){

              console.log('ac')
              res.send({
                success: false,
                message: error,
              });
              return;
            } else {

              res.send({
                success: true,
                message: 'Contraseña actualizada correctamente',
              });
              return;
            }
          });
        } else {

          res.send({
            success: false,
            message: 'La contraseña es incorrecta'
          });
        }
      }
    });
  } catch(e){

    res.send({
      success: false,
      message: e,
    });
  }

});

router.delete('/eliminar', (req, res) => {
  const email = req.body.email;

  if (typeof email === 'undefined') {

    res.status(400).json('No se encontró el email');
    return;
  }

  try{

    db.query(`CALL sp_eliminarUsuario('${email}')`, function (error, results) {
  
      if (error) {
  
        res.send(error.sqlMessage);
      }
      else {
  
        req.session.destroy(err => {
  
          if (err) {
  
            res.send({
              success: false,
              message: err,
            });
            return;
          }
        });
  
        res.send({
          success: true,
          message: 'Usuario eliminado correctamente',
        });
      }
    });
  } catch(e){

    res.send({
      success: false,
      message: e,
    });
    return;
  }
});

router.post('/testCloudinarySubir', async (req, res) =>{

  const imagePath = './assets/test.jpg';

  const response = await funcionesCloudinary.subirImagen(imagePath);

  if(response.exito){

    res.status(201).send({
      success: true,
      message: "Imagen subida correctamente",
    });
  } else{

    res.status(400).send({
      success: false,
      message: "Error al cargar la imagen",
    });
  }
  
  return;
})

router.get('/testCloudinaryAssetData', async (req, res) =>{

  const publicId = 'test';

  const response = await funcionesCloudinary.getAssetData(publicId);

  if(response.exito){

    res.status(201).send({
      success: true,
      message: "Datos traidos correctamente",
    });
  } else{

    res.status(400).send({
      success: false,
      message: "Error al traer datos",
    });
  }
  
  return;
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
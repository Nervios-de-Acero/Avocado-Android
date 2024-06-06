const express = require('express')
const router = express.Router()
const db = require('../conection')
const { checkSchema, validationResult } = require('express-validator')
const validaciones = require('../utils/validacionesRecetas')
const funcionescomunes = require('../utils/funcionesComunes')
const funcionesCloudinary = require('../utils/cloudinaryFunctions')

router.get('/getCategorias', (req, res)=> {
  db.query(`SELECT * FROM categorias;`,function(error, results){
    if(error){
      res.send({
        success:false,
        message: error
      })
    } else {
      res.send({
        success:true,
        message: '',
        content: results
      })
    }
  })
})

router.get('/getRecetasFeed', (req, res) => {
  try{
    db.query(`CALL sp_getRecetasFeed(NULL);`, (error, results) => {
      if(error){
        res.send({
          success: false,
          message: error.sqlMessage,
        })
      } else {
        res.send({
          success: true,
          message: 'Recetas recibidas exitosamente',
          content: results[0],
        });
      }
    });
  } catch(e){

    res.send({
      success: false,
      message: e,
    });
  }
});

router.get('/buscarReceta/:titulo', (req, res)=> {
const titulo = req.params.titulo
  db.query(`CALL sp_buscarReceta('${titulo}');`, function(error, results){
    if(error){
      res.send({
        success:false,
        message: error
      })
    } else {
      res.send({
        success:true,
        message: '',
        content: results[0]
      })
    }
  })
})

router.get('/getRecetaById/:id', (req, res)=>{
  const idReceta = req.params.id
  db.query(`CALL sp_getReceta(${idReceta});`, function (error, results){
    if(error){
      res.send({
        success:false,
        message: error
      })
    } 

    else {
      res.send(results[0][0])
      return
    }
    
  })

  
})

router.get('/getProductos', (req, res)=> {
  db.query(`CALL sp_getProductos();`,function(error, results){
    if(error){
      res.send({
        success:false,
        message: error,
        content: ''
      })
    } else {
      res.send({
        success:true,
        message: '',
        content: results[0]
      })
    }
  })
})

router.post('/agregarReceta', checkSchema(validaciones), funcionescomunes.validarJSON, async (req, res)=>{
  
  const categorias = req.body.categorias ? req.body.categorias :  null;
  const tiempoCoccion =  req.body.tiempoCoccion ? req.body.tiempoCoccion :  null;
  const dificultad = req.body.dificultad ? req.body.dificultad :  null;

  const responseImagen = await funcionesCloudinary.subirImagen(req.body.imagen);
  
  try{

    db.query(`CALL sp_crearReceta('${req.body.email}', '${req.body.titulo}', '${tiempoCoccion}', '${dificultad}', '${req.body.descripcion}', '${responseImagen.secure_url}', '${JSON.stringify(req.body.ingredientes)}', '${JSON.stringify(req.body.pasos)}', '${JSON.stringify(categorias)}');`, (error, results) => {
    
      if(error){
  
        res.send({
          success: false,
          message: error.sqlMessage
        });
        return;
      } else {
  
        res.send({
          success: true,
          message: "Receta creada correctamente."
        });
      }
    });
  } catch(e){

    res.send({
      success: false,
      message: e,
    });
  }
});
  
router.put('/modificarImagenReceta', (req, res)=> {

  if(typeof req.body.imagen === 'undefined' || typeof req.body.idReceta === 'undefined'){

    res.status(400).json('Error. idReceta e imagen obligatorios');
    return;
  }
  
  try{

    db.query(`CALL sp_actualizarReceta(${req.body.idReceta} , NULL, NULL, NULL, NULL, NULL, NULL, NULL, '${req.body.imagen}');`, (error, results) => {
  
      if(error){
  
        res.send({
          success: false,
          message: error,
        });
  
        return;
      } else {
  
        res.send({
          success: true,
          message: 'Imagen actualizada',
        });
      }
     return;
    });
  } catch(e){

    res.send({
      success: false,
      message: error,
    });
  }
});
  
router.put('/modificarTituloReceta', (req, res)=> {

  if(typeof req.body.titulo === 'undefined' || typeof req.body.idReceta === 'undefined'){

    res.status(400).json('Error. idReceta e imagen obligatorios');
    return;
  }
  
  try{

    db.query(`CALL sp_actualizarReceta(${req.body.idReceta}, '${req.body.titulo}', NULL, NULL, NULL, NULL, NULL, NULL, NULL)`, (error, results) => {

      if(error){

        res.send({
          success: false,
          message: error.sqlMessage,
        });
        return;
      } else {

        res.send({
          success: true,
          message: 'Título actualizado',
        });
      }

      return;
    });
  } catch(e){

  res.send({
    success: false,
    message: e,
  });
  }
});
  
router.put('/modificarDescripcionReceta', (req, res)=> {

  if(typeof req.body.descripcion === 'undefined' || typeof req.body.idReceta === 'undefined'){

    res.status(400).json('Error. idReceta y descripcion obligatorias');
    return;
  }
  
  try{

    db.query(`CALL sp_actualizarReceta(${req.body.idReceta} , NULL,'${req.body.descripcion}', '${req.body.tiempoCoccion}', '${req.body.dificultad}', NULL, NULL, NULL, NULL); `, (error, results) => {
  
      if(error){
  
        res.send({
          success: false,
          message: error.sqlMessage,
        });
  
        return;
      } else {
  
        res.send({
          success: true,
          message: 'Datos actualizados',
        });
      }
  
      return;
    });
  } catch(e){

  res.send({
    success: false,
    message: e,
  });
  }
});
  
router.put('/modificarCategorias', (req, res) => {

  if(typeof req.body.categorias === 'undefined' || typeof req.body.idReceta === 'undefined'){

    res.status(400).json('Error. idReceta y categorias obligatorias');
    return;
  }

  const categorias = req.body.categorias ? `'${JSON.stringify(req.body.categorias)}'` : "NULL";

  try{

    db.query(`CALL sp_actualizarReceta(${req.body.idReceta} , NULL, NULL, NULL, NULL, NULL, NULL, ${categorias}, NULL); `, (error, results) => {
  
      if(error){
  
        res.send({
          success: false,
          message: error.sqlMessage,
        });
        
        return;
      } else {
  
        res.send({
          success: true,
          message: 'Datos actualizados',
        });
      }
      return;
    });
  } catch(e){

    res.send({
      success: false,
      message: e,
    });
  }
});
  
router.put('/modificarIngredientes', (req, res) => {

  if(typeof req.body.ingredientes === 'undefined' || typeof req.body.idReceta === 'undefined'){

    res.status(400).json('Error. idReceta e ingredientes obligatorios');
    return;
  }
  
  const ingredientes = req.body.ingredientes ? `'${JSON.stringify(req.body.ingredientes)}'` : "NULL";

  db.query(`CALL sp_actualizarReceta(${req.body.idReceta}, NULL, NULL, NULL, NULL, NULL, ${ingredientes}, NULL, NULL); `, (error, results) => {

    if(error){

      res.send({
        success: false,
        message: error,
      });
      return;

    } else {

      res.send({
        success: true,
        message: 'Datos actualizados',
      });
    }

    return;
  });
});
  
router.put('/modificarPasos', (req, res) => {

  if(typeof req.body.pasos === 'undefined' || typeof req.body.idReceta === 'undefined'){

    res.status(400).json('Error. idReceta y pasos obligatorios');
    return;
  }
  
  const pasos = req.body.pasos ? `'${JSON.stringify(req.body.pasos)}'` : "NULL";

  db.query(`CALL sp_actualizarReceta(${req.body.idReceta}, NULL, NULL, NULL, NULL, ${pasos}, NULL, NULL, NULL); `, (error, results) => {

    if(error){

      res.send({
        success: false,
        message: error,
      });

      return;
    } else {

      res.send({
        success: true,
        message: 'Datos actualizados',
      });
    }

    return;
  });
});

router.get('/getRecetasUsuario/:email', (req, res) => {
  const email = req.params.email
  if(!email){
    res.status(400).json('Error. Email obligatorio.')
    return
  }
    
  db.query(`CALL sp_getRecetasUsuario('${email}')`, function(error, results){
    if(error){
      res.send({
        success: false,
        message: error.message,
        content: []
      })
      return
    }
    else {
      res.send({
        success: true,
        message: '',
        content: results[0]
      })
    }  
  })
})
    
router.delete('/eliminarReceta/:id', (req, res) => {
  const id = req.params.id

  try{
        
    db.query(`CALL sp_eliminarRecetaProducto(${id}, 'receta')`,(error, results) => {

      if(error){

        res.send({
          success:false,
          message: error.sqlMessage,
        });
      } else {

        if(results.affectedRows === 0){

          res.send({
            success: false,
            message: 'No hay recetas con ese ID',
          });
        } else {

          res.send({
            success: true,
            message: 'Se eliminó la receta correctamente',
          });
        }
      }
     });
   } catch(e){

   res.send({
     success:false,
     message: e,
   });
  }
});

module.exports = router
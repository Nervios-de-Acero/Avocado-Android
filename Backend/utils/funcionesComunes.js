const {validationResult} = require('express-validator');
const funciones = {};

//#region Funciones

/**
 * Funcion para manejar las respuestas de las validaciones de checkSchema
 * @param {object} req
 * @param {object} res
 * @param {function} next
 * @return {undefined}
 */
funciones.validarJSON = (req, res, next) => {
    const resValidaciones = validationResult(req).array()
    
    if(resValidaciones.length > 0){
        funciones.manejoRespuestas(res, {
            errors: {
                message: "Error de validación",
                content: resValidaciones
            },
            meta: {
                status: 400,
            },
        });
        
        return; 
    }

    next();
}

//#endregion

module.exports = funciones;

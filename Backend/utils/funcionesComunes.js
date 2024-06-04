const { validationResult } = require('express-validator');
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

    if (resValidaciones.length > 0) {
        res.status(400).json({
            success: false,
            message: "Error de validación",
            content: resValidaciones
        });

        return;
    }

    next();
}

//#endregion

module.exports = funciones;

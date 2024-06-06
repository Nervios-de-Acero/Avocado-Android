const cloudinary = require('cloudinary').v2;

let funcionesCloudinary = {};

/**
 * Sube una imagen
 * @param {imagePath}
 */
funcionesCloudinary.subirImagen = async (base64) => {

    //Usa el nombre del archivo como el ID publico del asset
    //Habilita la sobreescritura del asset con nuevas versiones
    const options = {
      use_filename: true,
      unique_filename: false,
      overwrite: true,
    };

    const respuesta = {};
    const sanitizedBase64 = base64.replace(/\s/g, '');
    const imagePath = `data:image/jpeg;base64,${sanitizedBase64}`;

    try {

      // Sube la imagen
      const result = await cloudinary.uploader.upload(imagePath, options);
      console.log(result);

      respuesta = {
        exito: true,
        public_id: result.public_id,
        secure_url: result.secure_url, //Con secureURL se puede acceder a la imagen en la pagina de cloudinary
      }
      return respuesta;

    } catch (error) {

      console.error(error);

      respuesta = {
        exito: false,
        error: error,
      }
      return respuesta;
    }
}

funcionesCloudinary.getAssetData = async (publicId) => {

     // Devuelve los colores en la respuesta (Colores en este caso)
     const options = {
        colors: true,
      };
  
      try {

          // Pide los detalles del asset
          const result = await cloudinary.api.resource(publicId, options);
          console.log(result);

          respuesta = {
            exito: true,
            colors: result.secure_url,
          }
          return respuesta;

          } catch (error) {

          console.error(error);
          
          respuesta = {
            exito: false,
            error: error,
          }
          return respuesta;
      }
}

module.exports = funcionesCloudinary;
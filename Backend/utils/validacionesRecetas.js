const validaciones = {
  titulo: {
    exists: {
      errorMessage: "Formato inválido. Parámetro 'titulo' requerido.",
      bail: true
    },
    notEmpty: {
      errorMessage: 'Campo obligatorio',
      bail: true
    },
    isLength: {
      options: {
        min: 5,
        max: 250,
      },
      errorMessage: 'El título debe tener entre 5 y 250 caracteres',
      bail: true
    }
  },
  email: {
    exists: {
      errorMessage: "Formato inválido. Parámetro 'email' requerido.",
      bail: true
    },
    notEmpty: {
      errorMessage: 'Campo obligatorio',
      bail: true
    },
    isEmail: {
      errorMessage: 'Email inválido'
    }
  },
  descripcion: {
    exists: {
      errorMessage: "Formato inválido. Parámetro 'descripcion' requerido.",
      bail: true
    },
    notEmpty: {
      errorMessage: 'Campo obligatorio',
      bail: true
    } 
  },
  imagen: {
    exists: {
      errorMessage: "Formato inválido. Parámetro 'imagen' requerido.",
      bail: true
    },
    notEmpty: {
      errorMessage: 'Campo obligatorio',
      bail: true
    }
  },
  pasos: {
    exists: {
      errorMessage: "Formato inválido. Parámetro 'pasos' requerido.",
      bail: true
    },
    notEmpty: {
      errorMessage: 'Campo obligatorio',
      bail: true
    },
    isArray: {
      errorMessage: 'Tipo de dato inválido. Debe ser array',
      bail: true
    }
  },
  tiempoCoccion: {
    optional: true,
    isLength: {
      options: {
        min: 2,
        max: 20
      },
      errorMessage: 'El campo debe tener entre 2 y 20 caracteres'
    }
  },
  dificultad: {
    optional: true,
    isLength: {
      options: {
        min: 2,
        max: 15
      },
      errorMessage: 'El campo debe tener entre 2 y 15 caracteres'
    }
  },
  categorias: {
    optional: true,
    isArray: {
      errorMessage: 'Tipo de dato inválido. Debe ser array'
    }
  },
  ingredientes: {
    exists: {
      errorMessage: "Formato inválido. Parámetro 'ingredientes' requerido.",
      bail: true
    },
    notEmpty: {
      errorMessage: 'Campo obligatorio',
      bail: true
    },
    isArray: {
      errorMessage: 'Tipo de dato inválido. Debe ser array',
      bail: true
    }
  }
}

module.exports = validaciones;
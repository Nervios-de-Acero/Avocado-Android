const validaciones = {
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
  password: {
    exists: {
      errorMessage: "Formato inválido. Parámetro 'password' requerido.",
      bail: true
    },
    notEmpty: {
      errorMessage: 'Campo obligatorio',
      bail: true
    }
  },
  nuevoPassword: {
    exists: {
      errorMessage: "Formato inválido. Parámetro 'nuevoPassword' requerido.",
      bail: true
    },
    notEmpty: {
      errorMessage: 'Campo obligatorio',
      bail: true
    },
    isStrongPassword: {
      options: {
        minLength: 8,
        maxLength: 24,
        minLowercase: 1,
        minUppercase: 1,
        minNumbers: 1,
        minSymbols: 1
      },
      errorMessage: 'La contraseña debe tener entre 8 y 24 caracteres, una mayúscula, una minúscula, un caracter especial y un número'
    }
  }
}

module.exports = validaciones;
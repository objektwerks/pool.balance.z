package objektwerks

object Validator:
  extension (value: String)
    def isLicense: Boolean = if value.nonEmpty then value.length == 36 else false
    def isEmailAddress: Boolean = value.nonEmpty && value.length >= 3 && value.contains("@")
    def isPin: Boolean = value.length == 7

  extension (register: Register)
    def isValid: Boolean = register.emailAddress.isEmailAddress

  extension (login: Login)
    def isValid: Boolean = login.emailAddress.isEmailAddress && login.pin.isPin
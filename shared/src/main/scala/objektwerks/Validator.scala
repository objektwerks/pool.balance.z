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

  extension (account: Account)
    def isActivated: Boolean =
      account.id >= 0 &&
      account.license.isLicense &&
      account.emailAddress.isEmailAddress &&
      account.pin.isPin &&
      account.activated.nonEmpty &&
      account.deactivated.isEmpty
    def isDeactivated: Boolean =
      account.license.isLicense &&
      account.emailAddress.isEmailAddress &&
      account.pin.isPin &&
      account.activated.isEmpty &&
      account.deactivated.nonEmpty

  extension (pool: Pool)
    def isValid =
      pool.id >= 0 &&
      pool.name.nonEmpty &&
      pool.volume > 1000 &&
      pool.unit.nonEmpty
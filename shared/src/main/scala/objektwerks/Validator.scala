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

  extension (cleaning: Cleaning)
    def isValid: Boolean =
      cleaning.id >= 0 &&
      cleaning.poolId > 0 &&
      cleaning.cleaned.nonEmpty

  extension (measurement: Measurement)
    def isValid: Boolean =
      import Measurement.*

      measurement.id >= 0 &&
      measurement.poolId > 0 &&
      totalChlorineRange.contains(measurement.totalChlorine) &&
      freeChlorineRange.contains(measurement.freeChlorine) &&
      combinedChlorineRange.contains(measurement.combinedChlorine) &&
      (measurement.ph >= 6.2 && measurement.ph <= 8.4) &&
      calciumHardnessRange.contains(measurement.calciumHardness) &&
      totalAlkalinityRange.contains(measurement.totalAlkalinity) &&
      cyanuricAcidRange.contains(measurement.cyanuricAcid) &&
      totalBromineRange.contains(measurement.totalBromine) &&
      saltRange.contains(measurement.salt) &&
      temperatureRange.contains(measurement.temperature) &&
      measurement.measured.nonEmpty

  extension (chemical: Chemical)
    def isValid: Boolean =
      chemical.id >= 0 &&
      chemical.poolId > 0 &&
      chemical.typeof.nonEmpty &&
      chemical.amount > 0.00 &&
      chemical.unit.nonEmpty
      chemical.added.nonEmpty